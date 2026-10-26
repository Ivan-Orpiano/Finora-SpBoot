package org.finora.finorabudgettrackerbackend.bankimport;

import org.finora.finorabudgettrackerbackend.account.*;
import org.finora.finorabudgettrackerbackend.transaction.*;

import org.apache.commons.csv.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.*;


@Service
public class CsvImportService {

    private final AccountRepository accounts;
    private final TransactionRepository transactions;

    public CsvImportService(
            AccountRepository accounts,
            TransactionRepository transactions
    )
    {
        this.accounts = accounts;
        this.transactions = transactions;
    }
    //summary returned after importing a CSV file
    public record ImportResult(
            int imported,
            int duplicates,
            int failed,
            List<String> errors
    ) { }
    //creates a stable SHA-256 fingerprint for duplicate detection
    public static String fingerprint(String value) {
        try {
            byte[] hash = MessageDigest
                    .getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception exception) {
            throw new IllegalStateException(
                    "Unable to generate transaction fingerprint", exception
            );
        }
    }


    @Transactional
    public ImportResult importCsv(
            Long userId,
            Long accountId,
            MultipartFile file
    ) {
        //reject attempts to import into somebody else's  account
        Account account = accounts
                .findByIdAndOwnerId(accountId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account not found"
                ));

        if (file.isEmpty() || file.getSize() > 2_000_000) {
            throw new IllegalArgumentException(
                    "CSV must be nonempty and smaller than 2MB"
            );
        }
        int imported = 0;
        int duplicates = 0;
        int failed = 0;

        List<String> errors = new ArrayList<>();

        //Detect duplicate rows inside one uploaded file
        Set<String> seenKeys = new HashSet<>();

        try (
                Reader reader = new InputStreamReader(
                        file.getInputStream(),
                        StandardCharsets.UTF_8
                );

                CSVParser parser = CSVFormat.DEFAULT.builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .setIgnoreSurroundingSpaces(true)
                        .setTrim(true)
                        .get()
                        .parse(reader)
        ) {
            for (CSVRecord row : parser) {
                try {
                    //convert CSV text into typed transaction values
                    LocalDate date = LocalDate.parse(
                            row.get("date").trim()
                    );

                    String description = row.get("description").trim();

                    BigDecimal originalAmount = new BigDecimal(
                            row.get("amount").trim()
                    );

                    if (originalAmount.signum() == 0 ||
                            originalAmount.abs().scale() > 2) {
                        throw new IllegalArgumentException(
                                "Invalid transaction amount"
                        );
                    }

                    TransactionType type;

                    if (row.isMapped("type") &&
                            row.isSet("type") &&
                            !row.get("type").isBlank()) {
                        type = TransactionType.valueOf(
                                row.get("type")
                                        .trim()
                                        .toUpperCase(Locale.ROOT)
                        );
                    } else {
                        //without a type column: positive = income
                        type = originalAmount.signum() > 0 ?
                                TransactionType.INCOME
                                : TransactionType.EXPENSE;
                    }

                    String category = row.isMapped("category") &&
                            row.isSet("category") &&
                            !row.get("category").isBlank()
                            ? row.get("category").trim()
                            : "Other";

                    BigDecimal amount = originalAmount.abs();

                    //generate the same key for identical CSV records
                    String identity = date + "|" + description.toLowerCase(Locale.ROOT) + "|" + amount.setScale(2).toPlainString() + "|" + type.name();

                    String key = fingerprint(identity);

                    //skip existing database records and repeated file rows
                    if (seenKeys.contains(key) ||
                            transactions.existsByAccountIdAndExternalKey(
                                    accountId, key
                            )) {
                        duplicates++;
                        continue;
                    }

                    seenKeys.add(key);

                    //create the transaction only after validating the rows
                    Transaction transaction = new Transaction(
                            userId,
                            accountId,
                            type,
                            TransactionStatus.COMPLETED,
                            category,
                            description,
                            date,
                            amount,
                            key
                    );

                    transactions.save(transaction);
                    imported++;
                } catch (RuntimeException exception) {
                    failed++;

                    //limit detailed errors in the HTTP Response
                    if (errors.size() < 20) {
                        errors.add(
                                "CSV record" + row.getRecordNumber() +
                                        ": " + exception.getMessage()
                        );
                    }
                }
            }
        } catch (IOException exception) {
            failed++;
            errors.add("Unable to read the uploaded CSV file");
        }
        //Save the result of the most recent import
        String status = failed == 0
                ? "SUCCESS"
                : imported > 0 ? "PARTIAL" : "ERROR";

        String errorMessage = errors.isEmpty() ? null
                : String.join("; ", errors);

        if (errorMessage != null && errorMessage.length() > 1000) {
            errorMessage = errorMessage.substring(0, 1000);
        }

        account.updateSync(status, errorMessage);
        accounts.save(account);

        return new ImportResult(
                imported,
                duplicates,
                failed,
                errors
        );
    }
}
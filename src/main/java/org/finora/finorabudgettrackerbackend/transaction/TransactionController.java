package org.finora.finorabudgettrackerbackend.transaction;

import jakarta.validation.Valid;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    private Long uid(Jwt jwt) {
        return Long.valueOf(jwt.getSubject());
    }


    @GetMapping("/transactions")
    public Page<TransactionService.View> list(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) TransactionStatus status,
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sort,
            @RequestParam(defaultValue = "desc") String direction) {

        return service.search(
                uid(jwt),
                start, end, type, status,
                accountId, category, search,
                page, size, sort, direction
        );
    }

    @PostMapping("/transactions")
    public TransactionService.View create(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody TransactionService.Request request
    ){ return  service.save(uid(jwt), null, request);}

    @PutMapping("/transactions/{id}")
    public TransactionService.View update(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id,
            @Valid @RequestBody TransactionService.Request request
    ){ return service.save(uid(jwt), id, request);}

    @DeleteMapping("/transactions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id)
    {
        service.delete(uid(jwt), id);
    }

    //Export Transactions in a format excel can open
    @GetMapping(
        value ="/reports/transactions.csv",
        produces = "text/csv"
    )

    public ResponseEntity<byte[] > exportCsv (
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(required = false)LocalDate start,
            @RequestParam(required = false) LocalDate end
            ) throws IOException
    {

        StringWriter writer = new StringWriter();

        try(CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            printer.printRecord(
                    "Date", "Description", "Category",
                    "Type", "Status", "Amount", "Account ID"
            );

            int page = 0;
            Page<TransactionService.View> results;

            do
            {
                results = service.search(
                        uid(jwt),
                        start, end,
                        null, null,
                        null, null,null,
                        page, 100, "date", "desc"
                );

                for(TransactionService.View tx : results) {
                    printer.printRecord(
                            tx.date(),
                            csvSafe(tx.description()),
                            csvSafe(tx.category()),
                            tx.type(),
                            tx.status(),
                            tx.amount(),
                            tx.accountId()
                    );
                }
                page++;
            } while (results.hasNext() &&  page < 100);
        }

        byte[] content = writer.toString().getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename= budget-report.csv"
                )
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(content);
    }
    //prevent spreadsheet applications from evaluating text formulas
    private String csvSafe(String value) {

        if(value == null) {
            return "";
        }

        String trimmed = value.stripLeading();

        if(!trimmed.isEmpty() && "=+-@\t\r\n".indexOf(trimmed.charAt(0)) >= 0) {
            return "'" + value;
        }
        return value;
    }

}

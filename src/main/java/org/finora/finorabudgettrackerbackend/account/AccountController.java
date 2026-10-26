package org.finora.finorabudgettrackerbackend.account;

import org.finora.finorabudgettrackerbackend.bankimport.CsvImportService;

import jakarta.validation.Valid;

import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService service;
    private final CsvImportService importer;

    public AccountController(
            AccountService service,
            CsvImportService importer
    )
    {
        this.service = service;
        this.importer = importer;
    }

    //read the authenticated user ID from the JWT
    private Long uid(Jwt jwt)
    {
        return Long.valueOf(jwt.getSubject());
    }

    @GetMapping
    public List<AccountService.View> list(
            @AuthenticationPrincipal Jwt jwt)
    {
        return service.list(uid(jwt));
    }

    @PostMapping
    public AccountService.View create(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody AccountService.Request request)
    {
        return service.save(uid(jwt), null, request);
    }

    @PutMapping("/{id}")
    public AccountService.View update (
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id,
            @Valid @RequestBody AccountService.Request request)
    {
        return  service.save(uid(jwt), id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id)
    {
        service.delete(uid(jwt), id);
    }


    // receives a bank CSV uploaded from the react frontend
    @PostMapping(value = "/{id}/import",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public  CsvImportService.ImportResult importCsv(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file)
    {
        return importer.importCsv(uid(jwt), id, file);
    }
}

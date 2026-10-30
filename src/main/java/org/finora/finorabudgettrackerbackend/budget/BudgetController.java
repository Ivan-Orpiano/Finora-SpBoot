package org.finora.finorabudgettrackerbackend.budget;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService service;

    public BudgetController(BudgetService service) {
        this.service = service;
    }

    public Long uid(Jwt jwt) {
        return Long.valueOf(jwt.getSubject());
    }

    @GetMapping
    public List <BudgetService.View> list (
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam String month
    ) {
        return  service.list(uid(jwt), month);
    }

    @PostMapping
    public BudgetService.View createOrUpdate(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody BudgetService.Request request
    ) {
        return service.save(uid(jwt), request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id
    ) {
        service.delete(uid(jwt), id);
    }




}

package com.springbank.bankacc.query.api.controllers;

import com.springbank.bankacc.query.api.dto.AccountLookupResponse;
import com.springbank.bankacc.query.api.dto.EqualityType;
import com.springbank.bankacc.query.api.queries.FindAccountByHolderIdQuery;
import com.springbank.bankacc.query.api.queries.FindAccountByIdQuery;
import com.springbank.bankacc.query.api.queries.FindAccountWithBalanceQuery;
import com.springbank.bankacc.query.api.queries.FindAllAccountsQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bankAccountLookup")
@RequiredArgsConstructor
@Slf4j
public class AccountLookupController {

    private final QueryGateway queryGateway;

    @GetMapping
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<AccountLookupResponse> getAllAccounts() {

        try {
            var query = new FindAllAccountsQuery();
            var response = queryGateway
                    .query(query, ResponseTypes.instanceOf(AccountLookupResponse.class))
                    .join();

            if (response == null || response.getAccounts() == null) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            var safeErrorMessage = "Failed to complete get all accounts request";
            log.debug(e.toString());

            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/byId/{id}")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<AccountLookupResponse> getAccountById(@PathVariable String id) {

        try {
            var query = new FindAccountByIdQuery(id);
            var response = queryGateway
                    .query(query, ResponseTypes.instanceOf(AccountLookupResponse.class))
                    .join();

            if (response == null || response.getAccounts() == null) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            var safeErrorMessage = "Failed to complete get account by ID request";
            log.debug(e.toString());

            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/byHolderId/{accountHolderId}")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<AccountLookupResponse> getAccountByHolderId(@PathVariable String accountHolderId) {

        try {
            var query = new FindAccountByHolderIdQuery(accountHolderId);
            var response = queryGateway
                    .query(query, ResponseTypes.instanceOf(AccountLookupResponse.class))
                    .join();

            if (response == null || response.getAccounts() == null) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            var safeErrorMessage = "Failed to complete get account by holder ID request";
            log.debug(e.toString());

            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/withBalance/{equalityType}/{balance}")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<AccountLookupResponse> getAccountWithBalanceQuery(@PathVariable EqualityType equalityType,
                                                                            @PathVariable double balance) {

        try {
            var query = new FindAccountWithBalanceQuery(equalityType, balance);
            var response = queryGateway
                    .query(query, ResponseTypes.instanceOf(AccountLookupResponse.class))
                    .join();

            if (response == null || response.getAccounts() == null) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            var safeErrorMessage = "Failed to complete get account with balance request";
            log.debug(e.toString());

            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package com.springbank.bankacc.cmd.api.controllers;

import com.springbank.bankacc.cmd.api.commands.CloseAccountCommand;
import com.springbank.bankacc.cmd.api.dto.OpenAccountResponse;
import com.springbank.bankacc.core.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/closeBankAccount")
@RequiredArgsConstructor
@Slf4j
public class CloseAccountController {

    private final CommandGateway commandGateway;

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity<BaseResponse> closeAccount(@PathVariable String id) {

        try {
            var command = CloseAccountCommand.builder()
                    .id(id)
                    .build();

            commandGateway.send(command);

            return new ResponseEntity<>(new BaseResponse("Bank account successfully closed!"), HttpStatus.OK);

        } catch (Exception e) {

            var safeErrorMessage = "Error while processing request to close bank account for id: " + id;
            log.debug(safeErrorMessage.toString());

            return new ResponseEntity<>(new OpenAccountResponse(id, safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }
}

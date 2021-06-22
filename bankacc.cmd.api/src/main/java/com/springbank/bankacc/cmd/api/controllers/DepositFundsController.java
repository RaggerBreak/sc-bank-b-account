package com.springbank.bankacc.cmd.api.controllers;

import com.springbank.bankacc.cmd.api.commands.DepositFundsCommand;
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
@RequestMapping("/api/v1/depositFunds")
@RequiredArgsConstructor
@Slf4j
public class DepositFundsController {

    private final CommandGateway commandGateway;

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity<BaseResponse> depositFunds(@PathVariable String id,
                                                     @Valid @RequestBody DepositFundsCommand command) {

        try {
            command.setId(id);
            commandGateway.send(command);

            return new ResponseEntity<>(new BaseResponse("Funds successfully deposited!"), HttpStatus.OK);

        } catch (Exception e) {
            var safeErrorMessage = "Error while processing request to deposit funds into bank account for id: " + id;
            log.debug(safeErrorMessage.toString());

            return new ResponseEntity<>(new BaseResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

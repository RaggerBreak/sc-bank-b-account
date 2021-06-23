package com.springbank.bankacc.cmd.api.controllers;

import com.springbank.bankacc.cmd.api.commands.WithdrawFundsCommand;
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
@RequestMapping("/api/v1/withdrawFunds")
@RequiredArgsConstructor
@Slf4j
public class WithdrawFundsController {

    private final CommandGateway commandGateway;

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity<BaseResponse> withdraw(@PathVariable String id,
                                                 @Valid @RequestBody WithdrawFundsCommand command)  {

        try {
            command.setId(id);
            /* .get() because in AccountAggregate is "if" condition with
                throw new IllegalStateException("Withdrawal declined, insufficient funds!")
                without .get() response always will be OK.
            */
            /*
                .send() is better for debugging, in production use .sendAndWait()
            */
            commandGateway.send(command).get();



            return new ResponseEntity<>(new BaseResponse("Withdraw successfully completed!"), HttpStatus.OK);

        } catch (Exception e) {

            var safeErrorMessage = "Error while processing request to withdraw funds from bank account for id: " + id;
            log.debug(safeErrorMessage.toString());

            return new ResponseEntity<>(new BaseResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}

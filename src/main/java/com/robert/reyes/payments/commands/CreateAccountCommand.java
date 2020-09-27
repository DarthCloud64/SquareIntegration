package com.robert.reyes.payments.commands;

import com.robert.reyes.payments.dtos.AccountCreatedResponseDTO;
import com.robert.reyes.payments.utils.mediator.Command;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateAccountCommand implements Command<AccountCreatedResponseDTO> {
    private String accountToken;
}

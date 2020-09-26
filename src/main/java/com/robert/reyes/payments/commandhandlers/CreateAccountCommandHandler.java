package com.robert.reyes.payments.commandhandlers;

import com.robert.reyes.payments.commands.CreateAccountCommand;
import com.robert.reyes.payments.dtos.AccountCreatedResponseDTO;
import com.robert.reyes.payments.services.paymentservice.PaymentService;
import com.robert.reyes.payments.utils.mediator.Command;
import com.robert.reyes.payments.utils.mediator.CommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Component
public class CreateAccountCommandHandler extends CommandHandler<CreateAccountCommand, AccountCreatedResponseDTO> {
    @Autowired
    private PaymentService paymentService;

    @Override
    public AccountCreatedResponseDTO handle(CreateAccountCommand command) throws Exception{
        return null;
    }
}

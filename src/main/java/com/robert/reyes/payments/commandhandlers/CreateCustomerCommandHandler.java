package com.robert.reyes.payments.commandhandlers;

import com.robert.reyes.payments.commands.CreateCustomerCommand;
import com.robert.reyes.payments.services.paymentservice.PaymentService;
import com.robert.reyes.payments.utils.mediator.CommandHandler;

import kotlin.NotImplementedError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Deprecated
public class CreateCustomerCommandHandler extends CommandHandler<CreateCustomerCommand, String> {
    @Autowired
    private PaymentService paymentService;

    @Override
    public String handle(CreateCustomerCommand command) throws Exception {
        throw new NotImplementedError();
    }
}

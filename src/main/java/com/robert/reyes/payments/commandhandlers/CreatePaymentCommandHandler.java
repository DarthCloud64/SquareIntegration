package com.robert.reyes.payments.commandhandlers;

import com.robert.reyes.payments.commands.CreatePaymentCommand;
import com.robert.reyes.payments.dtos.PaymentDTO;
import com.robert.reyes.payments.services.paymentservice.PaymentService;
import com.robert.reyes.payments.utils.mediator.CommandHandler;
import org.springframework.beans.factory.annotation.Autowired;

public class CreatePaymentCommandHandler extends CommandHandler<CreatePaymentCommand, PaymentDTO> {
    @Autowired
    private PaymentService paymentService;

    @Override
    public PaymentDTO handle(CreatePaymentCommand command) throws Exception {
        return paymentService.createPayment(command);
    }
}

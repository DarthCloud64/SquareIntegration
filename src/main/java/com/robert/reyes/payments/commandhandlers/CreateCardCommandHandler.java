package com.robert.reyes.payments.commandhandlers;

import com.robert.reyes.payments.commands.CreateCardCommand;
import com.robert.reyes.payments.dtos.CardDTO;
import com.robert.reyes.payments.services.paymentservice.PaymentService;
import com.robert.reyes.payments.utils.mediator.CommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateCardCommandHandler extends CommandHandler<CreateCardCommand, CardDTO> {
    @Autowired
    private PaymentService paymentService;

    @Override
    public CardDTO handle(CreateCardCommand command) throws Exception {
        return paymentService.createCard(command);
    }
}

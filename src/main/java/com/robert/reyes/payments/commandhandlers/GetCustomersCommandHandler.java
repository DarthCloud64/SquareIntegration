package com.robert.reyes.payments.commandhandlers;

import com.robert.reyes.payments.commands.GetCustomersCommand;
import com.robert.reyes.payments.dtos.CustomerDTO;
import com.robert.reyes.payments.services.paymentservice.PaymentService;
import com.robert.reyes.payments.utils.mediator.Command;
import com.robert.reyes.payments.utils.mediator.CommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetCustomersCommandHandler extends CommandHandler<GetCustomersCommand, List<CustomerDTO>> {
    @Autowired
    private PaymentService paymentService;

    @Override
    public List<CustomerDTO> handle(GetCustomersCommand command) throws Exception {
        return paymentService.getCustomers();
    }
}

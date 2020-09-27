package com.robert.reyes.payments.services.paymentservice;

import com.robert.reyes.payments.commands.CreateCardCommand;
import com.robert.reyes.payments.commands.CreateCustomerCommand;
import com.robert.reyes.payments.commands.CreatePaymentCommand;
import com.robert.reyes.payments.dtos.CardDTO;
import com.robert.reyes.payments.dtos.CustomerDTO;
import com.robert.reyes.payments.dtos.PaymentDTO;

import java.util.List;

public interface PaymentService {
    String createCustomer(CreateCustomerCommand createCustomerCommand) throws Exception;
    List<CustomerDTO> getCustomers() throws Exception;
    CardDTO createCard(CreateCardCommand createCardCommand) throws Exception;
    PaymentDTO createPayment(CreatePaymentCommand createPaymentCommand) throws Exception;
}

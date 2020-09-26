package com.robert.reyes.payments.services.paymentservice;

import com.robert.reyes.payments.commands.CreateCustomerCommand;
import com.robert.reyes.payments.dtos.CustomerDTO;

import java.util.List;

public interface PaymentService {
    String createCustomer(CreateCustomerCommand createCustomerCommand) throws Exception;
    List<CustomerDTO> getCustomers() throws Exception;
}

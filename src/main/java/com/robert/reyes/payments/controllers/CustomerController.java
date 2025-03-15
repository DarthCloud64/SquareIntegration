package com.robert.reyes.payments.controllers;

import com.robert.reyes.payments.commands.CreateCustomerCommand;
import com.robert.reyes.payments.commands.GetCustomersCommand;
import com.robert.reyes.payments.dtos.AccountCreatedResponseDTO;
import com.robert.reyes.payments.dtos.CreateCustomerRequestDTO;
import com.robert.reyes.payments.dtos.CustomerDTO;
import com.robert.reyes.payments.services.paymentservice.PaymentService;
import com.robert.reyes.payments.utils.mediator.MediatorPipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CustomerController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/customers")
    public ResponseEntity<String> createCustomer(@RequestBody CreateCustomerRequestDTO createCustomerRequestDto) throws Exception{
        return new ResponseEntity<String>(paymentService.createCustomer(createCustomerRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDTO>> getCustomers() throws Exception{
        return new ResponseEntity<List<CustomerDTO>>(new ArrayList<>(), HttpStatus.OK);
    }
}

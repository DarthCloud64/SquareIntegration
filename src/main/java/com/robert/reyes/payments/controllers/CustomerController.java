package com.robert.reyes.payments.controllers;

import com.robert.reyes.payments.commands.CreateCustomerCommand;
import com.robert.reyes.payments.commands.GetCustomersCommand;
import com.robert.reyes.payments.dtos.AccountCreatedResponseDTO;
import com.robert.reyes.payments.dtos.CustomerDTO;
import com.robert.reyes.payments.utils.mediator.MediatorPipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class CustomerController {
    @Autowired
    private MediatorPipeline mediatorPipeline;

    @PostMapping("/customers")
    public ResponseEntity<String> createCustomer(@RequestBody CreateCustomerCommand createCustomerCommand) throws Exception{
        return new ResponseEntity<String>(mediatorPipeline.send(createCustomerCommand), HttpStatus.CREATED);
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDTO>> getCustomers() throws Exception{
        return new ResponseEntity<List<CustomerDTO>>(mediatorPipeline.send(new GetCustomersCommand()), HttpStatus.OK);
    }
}

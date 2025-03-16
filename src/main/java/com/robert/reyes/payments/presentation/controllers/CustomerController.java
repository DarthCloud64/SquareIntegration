package com.robert.reyes.payments.presentation.controllers;

import com.robert.reyes.payments.domain.payments.PaymentService;
import com.robert.reyes.payments.dtos.CreateCustomerRequestDTO;
import com.robert.reyes.payments.dtos.CreateCustomerResponseDTO;
import com.robert.reyes.payments.dtos.CustomerDTO;
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

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDTO>> getCustomers() throws Exception{
        return new ResponseEntity<List<CustomerDTO>>(new ArrayList<>(), HttpStatus.OK);
    }

    @PostMapping("/customers")
    public ResponseEntity<CreateCustomerResponseDTO> createCustomer(@RequestBody CreateCustomerRequestDTO createCustomerRequestDto) throws Exception {
        return new ResponseEntity<CreateCustomerResponseDTO>(paymentService.createCustomer(createCustomerRequestDto).join(), HttpStatus.CREATED);
    }
}

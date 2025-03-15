package com.robert.reyes.payments.controllers;

import com.robert.reyes.payments.commands.CreatePaymentCommand;
import com.robert.reyes.payments.dtos.CreatePaymentRequestDTO;
import com.robert.reyes.payments.dtos.PaymentDTO;
import com.robert.reyes.payments.services.paymentservice.PaymentService;
import com.robert.reyes.payments.utils.mediator.MediatorPipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class PaymentsController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payments")
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody CreatePaymentRequestDTO createPaymentRequestDto) throws Exception{
        return new ResponseEntity<PaymentDTO>(paymentService.createPayment(createPaymentRequestDto), HttpStatus.CREATED);
    }
}

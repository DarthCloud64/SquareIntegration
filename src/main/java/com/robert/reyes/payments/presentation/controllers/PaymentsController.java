package com.robert.reyes.payments.presentation.controllers;

import com.robert.reyes.payments.domain.payments.PaymentService;
import com.robert.reyes.payments.dtos.CreatePaymentRequestDTO;
import com.robert.reyes.payments.dtos.CreatePaymentResponseDTO;
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
    public ResponseEntity<CreatePaymentResponseDTO> createPayment(@RequestBody CreatePaymentRequestDTO createPaymentRequestDto) throws Exception{
        return new ResponseEntity<CreatePaymentResponseDTO>(paymentService.createPayment(createPaymentRequestDto).join(), HttpStatus.CREATED);
    }
}

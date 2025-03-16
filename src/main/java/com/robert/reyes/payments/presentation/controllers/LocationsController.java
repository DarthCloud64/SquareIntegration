package com.robert.reyes.payments.presentation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.robert.reyes.payments.domain.payments.PaymentService;
import com.robert.reyes.payments.dtos.GetLocationsResponseDTO;

@Controller
public class LocationsController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/locations")
    public ResponseEntity<GetLocationsResponseDTO> getLocations() throws Exception{
        return new ResponseEntity<GetLocationsResponseDTO>(paymentService.getLocations().join(), HttpStatus.OK);
    }
}

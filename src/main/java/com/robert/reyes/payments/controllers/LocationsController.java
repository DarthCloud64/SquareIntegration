package com.robert.reyes.payments.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.robert.reyes.payments.dtos.LocationsDTO;
import com.robert.reyes.payments.services.paymentservice.PaymentService;

@Controller
public class LocationsController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/locations")
    public ResponseEntity<LocationsDTO> getLocations() throws Exception{
        return new ResponseEntity<LocationsDTO>(paymentService.getLocations(), HttpStatus.OK);
    }
}

package com.robert.reyes.payments.controllers;

import com.robert.reyes.payments.commands.CreateCardCommand;
import com.robert.reyes.payments.dtos.CardDTO;
import com.robert.reyes.payments.utils.mediator.MediatorPipeline;
import com.squareup.square.models.Card;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class CardsController {
    @Autowired
    private MediatorPipeline mediatorPipeline;

    @PostMapping("/cards")
    public ResponseEntity<CardDTO> createCustomerCard(@RequestBody CreateCardCommand createCardCommand) throws Exception
    {
        return new ResponseEntity<CardDTO>(mediatorPipeline.send(createCardCommand), HttpStatus.CREATED);
    }
}

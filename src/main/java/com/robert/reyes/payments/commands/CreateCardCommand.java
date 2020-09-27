package com.robert.reyes.payments.commands;

import com.robert.reyes.payments.dtos.CardDTO;
import com.robert.reyes.payments.utils.mediator.Command;
import lombok.Data;

@Data
public class CreateCardCommand implements Command<CardDTO> {
    private String customerId;
    private String cardToken;
}

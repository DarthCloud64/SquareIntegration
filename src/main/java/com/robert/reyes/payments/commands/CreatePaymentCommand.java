package com.robert.reyes.payments.commands;

import com.robert.reyes.payments.dtos.PaymentDTO;
import com.robert.reyes.payments.utils.mediator.Command;
import lombok.Data;

@Data
public class CreatePaymentCommand implements Command<PaymentDTO> {
    private String idempotencyKey;
    private int amountInCents;
}

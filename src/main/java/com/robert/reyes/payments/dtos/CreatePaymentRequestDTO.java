package com.robert.reyes.payments.dtos;

import lombok.Data;

@Data
public class CreatePaymentRequestDTO {
    private String idempotencyKey;
    private String sourceId;
    private Long amount;
    private String currency;
}

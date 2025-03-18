package com.robert.reyes.payments.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePaymentRequestDTO {
    private String idempotencyKey;
    private String sourceId;
    private Long amount;
    private String currency;
}

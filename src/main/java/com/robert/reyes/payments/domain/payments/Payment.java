package com.robert.reyes.payments.domain.payments;

import lombok.Data;

@Data
public class Payment {
    private String paymentId;
    private String sourceId;
    private String status;
    private long amountInCents;
    private String currencyId;
    private String providerId;
    private String externalPaymentId;
    private String externalPaymentStatus;
}

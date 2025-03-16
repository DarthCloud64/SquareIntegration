package com.robert.reyes.payments.domain.payments;

import java.util.concurrent.CompletableFuture;

import com.robert.reyes.payments.dtos.CreateCustomerRequestDTO;
import com.robert.reyes.payments.dtos.CreateCustomerResponseDTO;
import com.robert.reyes.payments.dtos.CreatePaymentRequestDTO;
import com.robert.reyes.payments.dtos.GetLocationsResponseDTO;
import com.robert.reyes.payments.dtos.CreatePaymentResponseDTO;

public interface PaymentService {
    CompletableFuture<GetLocationsResponseDTO> getLocations() throws Exception;
    CompletableFuture<CreateCustomerResponseDTO> createCustomer(CreateCustomerRequestDTO createCustomerRequestDto) throws Exception;
    CompletableFuture<CreatePaymentResponseDTO> createPayment(CreatePaymentRequestDTO createPaymentRequestDto) throws Exception;
}

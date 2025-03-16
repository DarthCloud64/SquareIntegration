package com.robert.reyes.payments.domain.payments;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.robert.reyes.payments.domain.customers.Customer;
import com.robert.reyes.payments.domain.locations.Location;

public interface PaymentProviderService {
    CompletableFuture<List<Location>> getLocations() throws Exception;
    CompletableFuture<Customer> createCustomer(Customer customerToCreate, String idempotencyKey) throws Exception;
    CompletableFuture<Payment> createPayment(Payment paymentToCreate, String idempotencyKey) throws Exception;
}

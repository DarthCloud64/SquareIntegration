package com.robert.reyes.payments.infrastructure.paymentproviders;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import com.robert.reyes.payments.domain.customers.Customer;
import com.robert.reyes.payments.domain.locations.Location;
import com.robert.reyes.payments.domain.payments.Payment;
import com.robert.reyes.payments.domain.payments.PaymentProviderService;
import com.squareup.square.Environment;
import com.squareup.square.SquareClient;
import com.squareup.square.authentication.BearerAuthModel;
import com.squareup.square.models.Address;
import com.squareup.square.models.CreateCustomerRequest;
import com.squareup.square.models.CreatePaymentRequest;
import com.squareup.square.models.Money;

import jakarta.annotation.PostConstruct;

@Service
@RequestScope
public class SquarePaymentProviderServiceImpl implements PaymentProviderService {
    private static final Logger logger = LoggerFactory.getLogger(SquarePaymentProviderServiceImpl.class);
    
    @Value("${squareApiToken:Not Found}")
    private String squareApiToken;
    private SquareClient squareClient;

    public SquarePaymentProviderServiceImpl() {
    }

    @PostConstruct
    public void init(){
        squareClient = new SquareClient.Builder()
        .bearerAuthCredentials(new BearerAuthModel.Builder(squareApiToken).build())
        .environment(Environment.SANDBOX)
        .build();
    }

    @Override
    public CompletableFuture<List<Location>> getLocations() throws Exception {
        return squareClient
            .getLocationsApi()
            .listLocationsAsync()
            .thenApply(listLocationsResponse -> {
                List<Location> domainLocations = new ArrayList<>();

                logger.info("Locations count = {}", listLocationsResponse.getLocations().size());

                for (com.squareup.square.models.Location squareLocation : listLocationsResponse.getLocations()){
                    Location domainLocation = new Location();
                    domainLocation.setName(squareLocation.getName());
                    domainLocations.add(domainLocation);
                }

                return domainLocations;
            });
    }

    @Override
    public CompletableFuture<Customer> createCustomer(Customer customerToCreate, String idempotencyKey) throws Exception {
        Address squareAddress = new Address.Builder()
            .addressLine1(customerToCreate.getAddress().getAddressLine1())
            .addressLine2(customerToCreate.getAddress().getAddressLine2())
            .locality(customerToCreate.getAddress().getCity())
            .administrativeDistrictLevel1(customerToCreate.getAddress().getState())
            .postalCode(customerToCreate.getAddress().getPostalCode())
            .country(customerToCreate.getAddress().getCountry())
            .build();

        CreateCustomerRequest squareCreateCustomerRequest = new CreateCustomerRequest.Builder()
            .idempotencyKey(idempotencyKey)
            .givenName(customerToCreate.getFirstName())
            .familyName(customerToCreate.getLastName())
            .companyName(customerToCreate.getCompanyName())
            .nickname(customerToCreate.getNickName())
            .emailAddress(customerToCreate.getEmailAddress())
            .address(squareAddress)
            .phoneNumber(customerToCreate.getPhoneNumber())
            .referenceId(customerToCreate.getCustomerId())
            .birthday(customerToCreate.getBirthday())
            .build();
        

        return squareClient
            .getCustomersApi()
            .createCustomerAsync(squareCreateCustomerRequest)
            .thenApply(squareCreateCustomerResponse -> {
                customerToCreate.setExternalCustomerId(squareCreateCustomerResponse.getCustomer().getId());

                return customerToCreate;
            });
    }

    @Override
    public CompletableFuture<Payment> createPayment(Payment paymentToCreate, String idempotencyKey) throws Exception {
        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest.Builder(paymentToCreate.getSourceId(), idempotencyKey)
            .sourceId(paymentToCreate.getSourceId())
            .idempotencyKey(idempotencyKey)
            .amountMoney(new Money.Builder()
                .amount(paymentToCreate.getAmountInCents())
                .currency(paymentToCreate.getCurrencyId())
                .build())
            .build();
      
        return squareClient
            .getPaymentsApi()
            .createPaymentAsync(createPaymentRequest)
            .thenApply(createPaymentResponse -> {
                paymentToCreate.setExternalPaymentId(createPaymentResponse.getPayment().getId());
                paymentToCreate.setExternalPaymentStatus(createPaymentResponse.getPayment().getStatus());

                return paymentToCreate;
            });
    }
    
}

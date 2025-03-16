package com.robert.reyes.payments.application.services.payments;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import com.robert.reyes.payments.application.exceptions.InvalidCustomerException;
import com.robert.reyes.payments.application.exceptions.InvalidPaymentException;
import com.robert.reyes.payments.application.exceptions.MissingProviderDetailsException;
import com.robert.reyes.payments.domain.customers.Address;
import com.robert.reyes.payments.domain.customers.Customer;
import com.robert.reyes.payments.domain.locations.Location;
import com.robert.reyes.payments.domain.payments.Payment;
import com.robert.reyes.payments.domain.payments.PaymentProviderService;
import com.robert.reyes.payments.domain.payments.PaymentService;
import com.robert.reyes.payments.domain.payments.PaymentStatuses;
import com.robert.reyes.payments.dtos.CreateCustomerRequestDTO;
import com.robert.reyes.payments.dtos.CreateCustomerResponseDTO;
import com.robert.reyes.payments.dtos.CreatePaymentRequestDTO;
import com.robert.reyes.payments.dtos.CreatePaymentResponseDTO;
import com.robert.reyes.payments.dtos.GetLocationResponseDTO;
import com.robert.reyes.payments.dtos.GetLocationsResponseDTO;

@Service
@RequestScope
public class PaymentServiceImpl implements PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private PaymentProviderService paymentProviderService;

    @Override
    public CompletableFuture<GetLocationsResponseDTO> getLocations() throws Exception {
        logger.trace("Getting all locations");

        return paymentProviderService.getLocations().thenApply(locations -> {
            logger.info("Number of locations returned: {}", locations.size());

            GetLocationsResponseDTO getLocationsResponseDTO = new GetLocationsResponseDTO();

            for(Location location : locations) {
                getLocationsResponseDTO.getLocations().add(new GetLocationResponseDTO(location.getName()));
            }

            return getLocationsResponseDTO;
        });
    }

    @Override
    public CompletableFuture<CreateCustomerResponseDTO> createCustomer(CreateCustomerRequestDTO createCustomerRequestDto) throws Exception {
        Address address = new Address(
            createCustomerRequestDto.getAddress().getAddressLine1(),
            createCustomerRequestDto.getAddress().getAddressLine2(),
            createCustomerRequestDto.getAddress().getCity(),
            createCustomerRequestDto.getAddress().getState(),
            createCustomerRequestDto.getAddress().getPostalCode(),
            createCustomerRequestDto.getAddress().getCountry());

        Customer customer = new Customer();
        customer.setCustomerId(UUID.randomUUID().toString());
        customer.setFirstName(createCustomerRequestDto.getFirstName());
        customer.setLastName(createCustomerRequestDto.getLastName());
        customer.setCompanyName(createCustomerRequestDto.getCompanyName());
        customer.setNickName(createCustomerRequestDto.getNickName());
        customer.setEmailAddress(createCustomerRequestDto.getEmailAddress());
        customer.setAddress(address);
        customer.setPhoneNumber(createCustomerRequestDto.getPhoneNumber());
        customer.setBirthday(createCustomerRequestDto.getBirthday());

        validateCustomer(customer);

        return paymentProviderService.createCustomer(customer, createCustomerRequestDto.getIdempotencyKey()).thenApply(createdCustomer -> {
            CreateCustomerResponseDTO createCustomerResponseDto = new CreateCustomerResponseDTO();
            createCustomerResponseDto.setNewCustomerId(createdCustomer.getCustomerId());

            return createCustomerResponseDto;
        });
    }

    @Override
    public CompletableFuture<CreatePaymentResponseDTO> createPayment(CreatePaymentRequestDTO createPaymentRequestDto) throws Exception {
        validateProviderDetails(createPaymentRequestDto);

        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setStatus(PaymentStatuses.New);
        payment.setSourceId(createPaymentRequestDto.getSourceId());
        payment.setAmountInCents(createPaymentRequestDto.getAmount());
        payment.setCurrencyId(createPaymentRequestDto.getCurrency());

        validatePayment(payment);

        return paymentProviderService.createPayment(payment, createPaymentRequestDto.getIdempotencyKey()).thenApply(createdPayment -> {
            CreatePaymentResponseDTO createPaymentResponseDto = new CreatePaymentResponseDTO();
            createPaymentResponseDto.setNewPaymentId(createdPayment.getPaymentId());

            return createPaymentResponseDto;
        });
    }

    private void validateProviderDetails(CreatePaymentRequestDTO createPaymentRequestDto){
        if(createPaymentRequestDto.getIdempotencyKey().isBlank()){
            throw new MissingProviderDetailsException("Idempotency key is required to prevent duplication in provider system");
        }

        if(createPaymentRequestDto.getSourceId().isBlank()){
            throw new MissingProviderDetailsException("Source id is required for the provider to process payments");
        }
    }

    private void validatePayment(Payment payment){
        if(payment.getAmountInCents() <= 0){
            throw new InvalidPaymentException("Payment amount must be greater than 0");
        }

        if(payment.getCurrencyId().isBlank()){
            throw new InvalidPaymentException("Currency id is required");
        }
    }

    private void validateAddress(Address address){
        if(address.getAddressLine1().isBlank()){
            throw new InvalidCustomerException("Customer address line 1 is required");   
        }

        if(address.getCity().isBlank()){
            throw new InvalidCustomerException("Customer address city is required");   
        }

        if(address.getCountry().isBlank()){
            throw new InvalidCustomerException("Customer address country is required");   
        }

        if(address.getPostalCode().isBlank()){
            throw new InvalidCustomerException("Customer address postal code is required");   
        }

        if(address.getState().isBlank()){
            throw new InvalidCustomerException("Customer address state is required");   
        }
    }
    
    private void validateCustomer(Customer customer) {
        validateAddress(customer.getAddress());

        if(customer.getFirstName().isBlank()){
            throw new InvalidCustomerException("Customer first name is required");
        }

        if(customer.getLastName().isBlank()){
            throw new InvalidCustomerException("Customer last name is required");
        }

        if(customer.getEmailAddress().isBlank()){
            throw new InvalidCustomerException("Customer email address is required");
        }

        if(customer.getPhoneNumber().isBlank()){
            throw new InvalidCustomerException("Customer phone number is required");
        }

        if(customer.getBirthday().isBlank()){
            throw new InvalidCustomerException("Customer birthday is required");
        }

        LocalDate birthDate = LocalDate.parse(customer.getBirthday());
        if(LocalDate.now().getYear() - birthDate.getYear() < 18){
            throw new InvalidCustomerException("Customer must be 18 years or older");
        }
    }
}

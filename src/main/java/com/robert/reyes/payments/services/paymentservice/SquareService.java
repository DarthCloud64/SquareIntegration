package com.robert.reyes.payments.services.paymentservice;

import com.robert.reyes.payments.commands.CreateCardCommand;
import com.robert.reyes.payments.commands.CreateCustomerCommand;
import com.robert.reyes.payments.commands.CreatePaymentCommand;
import com.robert.reyes.payments.dtos.CardDTO;
import com.robert.reyes.payments.dtos.CreateCustomerRequestDTO;
import com.robert.reyes.payments.dtos.CreatePaymentRequestDTO;
import com.robert.reyes.payments.dtos.CustomerDTO;
import com.robert.reyes.payments.dtos.LocationDTO;
import com.robert.reyes.payments.dtos.LocationsDTO;
import com.robert.reyes.payments.dtos.PaymentDTO;
import com.squareup.square.Environment;
import com.squareup.square.SquareClient;
import com.squareup.square.authentication.BearerAuthModel;
import com.squareup.square.models.*;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.List;

@Service
@RequestScope
public class SquareService implements PaymentService{
    private static final Logger logger = LoggerFactory.getLogger(SquareService.class);
    
    @Value("${squareApiToken:Not Found}")
    private String squareApiToken;
    private SquareClient squareClient;

    public SquareService() {
    }

    @PostConstruct
    public void init(){
        squareClient = new SquareClient.Builder()
        .bearerAuthCredentials(new BearerAuthModel.Builder(squareApiToken).build())
        .environment(Environment.SANDBOX)
        .build();
    }

    public LocationsDTO getLocations() throws Exception {
        LocationsDTO locations = new LocationsDTO();
        
        squareClient
            .getLocationsApi()
            .listLocationsAsync()
            .thenAccept(listLocationsResponse -> {
                logger.info("Locations count = {}", listLocationsResponse.getLocations().size());
                for (Location location : listLocationsResponse.getLocations()){
                        locations.getLocations().add(new LocationDTO(location.getName()));
                }
            })
            .join();

        return locations;
    }

    @Override
    public String createCustomer(CreateCustomerRequestDTO createCustomerRequestDto) throws Exception{
        Address address = new Address.Builder()
                .addressLine1(createCustomerRequestDto.getAddress().getAddressLine1())
                .addressLine2(createCustomerRequestDto.getAddress().getAddressLine2())
                .locality(createCustomerRequestDto.getAddress().getCity())
                .administrativeDistrictLevel1(createCustomerRequestDto.getAddress().getState())
                .postalCode(createCustomerRequestDto.getAddress().getPostalCode())
                .country(createCustomerRequestDto.getAddress().getCountry())
                .build();

        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest.Builder()
                .idempotencyKey(createCustomerRequestDto.getIdempotencyKey().toString())
                .givenName(createCustomerRequestDto.getFirstName())
                .familyName(createCustomerRequestDto.getLastName())
                .companyName(createCustomerRequestDto.getCompanyName())
                .nickname(createCustomerRequestDto.getNickName())
                .emailAddress(createCustomerRequestDto.getEmailAddress())
                .address(address)
                .phoneNumber(createCustomerRequestDto.getPhoneNumber())
                .referenceId(createCustomerRequestDto.getReferenceId())
                .note(createCustomerRequestDto.getNote())
                .birthday(createCustomerRequestDto.getBirthday())
                .build();
        
        StringBuilder customerId = new StringBuilder();
        squareClient
            .getCustomersApi()
            .createCustomerAsync(createCustomerRequest)
            .thenAccept(response -> {
                customerId.append(response.getCustomer().getId());
            })
            .join();

        return customerId.toString();
    }

    @Override
    public List<CustomerDTO> getCustomers() throws Exception {
        SquareClient client = new SquareClient.Builder()
                .environment(Environment.SANDBOX)
                .accessToken(squareApiToken)
                .build();

        ArrayList<CustomerDTO> customerDTOS = new ArrayList<>();
        // ListCustomersResponse listCustomersResponse = client.getCustomersApi().listCustomers(null, null, null);
        // listCustomersResponse.getCustomers().forEach(x -> {
        //     CustomerDTO temp = new CustomerDTO();
        //     temp.setFirstName(x.getGivenName());
        //     temp.setLastName(x.getFamilyName());
        //     temp.setCustomerId(x.getId());
        //     customerDTOS.add(temp);
        // });

        return customerDTOS;
    }

    @Override
    public CardDTO createCard(CreateCardCommand createCardCommand) throws Exception {
        SquareClient client = new SquareClient.Builder()
                .environment(Environment.SANDBOX)
                .accessToken(squareApiToken)
                .build();

        CreateCustomerCardRequest createCustomerCardRequest = new CreateCustomerCardRequest.Builder(createCardCommand.getCardToken()).build();

        CreateCustomerCardResponse createCustomerCardResponse = client.getCustomersApi().createCustomerCard(createCardCommand.getCustomerId(), createCustomerCardRequest);
        CardDTO cardDTO = new CardDTO();
        cardDTO.setId(createCustomerCardResponse.getCard().getId());
        return cardDTO;
    }

    @Override
    public PaymentDTO createPayment(CreatePaymentRequestDTO createPaymentRequestDTO) throws Exception {
        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest.Builder(createPaymentRequestDTO.getSourceId(), createPaymentRequestDTO.getIdempotencyKey())
            .sourceId(createPaymentRequestDTO.getSourceId())
            .idempotencyKey(createPaymentRequestDTO.getIdempotencyKey())
            .amountMoney(new Money.Builder()
                .amount(createPaymentRequestDTO.getAmount())
                .currency(createPaymentRequestDTO.getCurrency())
                .build())
            .build();
        
        PaymentDTO paymentDTO = new PaymentDTO();
        squareClient
            .getPaymentsApi()
            .createPaymentAsync(createPaymentRequest)
            .thenAccept(response -> {
                paymentDTO.setPaymentId(response.getPayment().getId());
            })
            .join();

        return paymentDTO;
    }
}

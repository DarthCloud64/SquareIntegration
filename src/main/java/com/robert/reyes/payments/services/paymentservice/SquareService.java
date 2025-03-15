package com.robert.reyes.payments.services.paymentservice;

import com.robert.reyes.payments.commands.CreateCardCommand;
import com.robert.reyes.payments.commands.CreateCustomerCommand;
import com.robert.reyes.payments.commands.CreatePaymentCommand;
import com.robert.reyes.payments.dtos.CardDTO;
import com.robert.reyes.payments.dtos.CustomerDTO;
import com.robert.reyes.payments.dtos.LocationDTO;
import com.robert.reyes.payments.dtos.LocationsDTO;
import com.robert.reyes.payments.dtos.PaymentDTO;
import com.squareup.square.Environment;
import com.squareup.square.SquareClient;
import com.squareup.square.authentication.BearerAuthModel;
import com.squareup.square.models.*;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.List;

@Service
@RequestScope
public class SquareService implements PaymentService{
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
                        for (Location location : listLocationsResponse.getLocations()){
                                locations.getLocations().add(new LocationDTO(location.getName()));
                        }
                });
        
                return locations;
    }

    @Override
    public String createCustomer(CreateCustomerCommand createCustomerCommand) throws Exception{
        SquareClient client = new SquareClient.Builder()
                .environment(Environment.SANDBOX)
                .accessToken(squareApiToken)
                .build();

        Address address = new Address.Builder()
                .addressLine1(createCustomerCommand.getAddress().getAddressLine1())
                .addressLine2(createCustomerCommand.getAddress().getAddressLine2())
                .locality(createCustomerCommand.getAddress().getCity())
                .administrativeDistrictLevel1(createCustomerCommand.getAddress().getState())
                .postalCode(createCustomerCommand.getAddress().getPostalCode())
                .country(createCustomerCommand.getAddress().getCountry())
                .build();

        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest.Builder()
                .idempotencyKey(createCustomerCommand.getIdempotencyKey().toString())
                .givenName(createCustomerCommand.getFirstName())
                .familyName(createCustomerCommand.getLastName())
                .companyName(createCustomerCommand.getCompanyName())
                .nickname(createCustomerCommand.getNickName())
                .emailAddress(createCustomerCommand.getEmailAddress())
                .address(address)
                .phoneNumber(createCustomerCommand.getPhoneNumber())
                .referenceId(createCustomerCommand.getReferenceId())
                .note(createCustomerCommand.getNote())
                .birthday(createCustomerCommand.getBirthday())
                .build();

        CreateCustomerResponse response = client.getCustomersApi().createCustomer(createCustomerRequest);
        return response.getCustomer().getId();
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
    public PaymentDTO createPayment(CreatePaymentCommand createPaymentCommand) throws Exception {
        SquareClient client = new SquareClient.Builder()
                .environment(Environment.SANDBOX)
                .accessToken(squareApiToken)
                .build();

        // CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest.Builder(
        //         createPaymentCommand.getSourceId(),
        //         createPaymentCommand.getIdempotencyKey(),
        //         new Money.Builder().amount(createPaymentCommand.getAmountInCents()).currency("USD").build())
        //         .customerId(createPaymentCommand.getCustomerId()).build();

        // CreatePaymentResponse createPaymentResponse = client.getPaymentsApi().createPayment(createPaymentRequest);

        PaymentDTO paymentDTO = new PaymentDTO();
        // paymentDTO.setPaymentId(createPaymentResponse.getPayment().getId());
        return paymentDTO;
    }
}

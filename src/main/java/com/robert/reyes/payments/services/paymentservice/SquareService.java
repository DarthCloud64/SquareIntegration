package com.robert.reyes.payments.services.paymentservice;

import com.robert.reyes.payments.commands.CreateCustomerCommand;
import com.robert.reyes.payments.dtos.CustomerDTO;
import com.squareup.square.Environment;
import com.squareup.square.SquareClient;
import com.squareup.square.models.*;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.List;

@Service
@RequestScope
public class SquareService implements PaymentService{
    @Override
    public String createCustomer(CreateCustomerCommand createCustomerCommand) throws Exception{
        SquareClient client = new SquareClient.Builder()
                .environment(Environment.SANDBOX)
                .accessToken("")
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
                .accessToken("")
                .build();

        ArrayList<CustomerDTO> customerDTOS = new ArrayList<>();
        ListCustomersResponse listCustomersResponse = client.getCustomersApi().listCustomers(null, null, null);
        listCustomersResponse.getCustomers().forEach(x -> {
            CustomerDTO temp = new CustomerDTO();
            temp.setFirstName(x.getGivenName());
            temp.setLastName(x.getFamilyName());
            temp.setCustomerId(x.getId());
            customerDTOS.add(temp);
        });

        return customerDTOS;
    }


}

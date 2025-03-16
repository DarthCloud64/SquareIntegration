package com.robert.reyes.payments.domain.customers;

import lombok.Data;

@Data
public class Customer {
    private String customerId;
    private String firstName;
    private String lastName;
    private String companyName;
    private String nickName;
    private String emailAddress;
    private Address address;
    private String phoneNumber;
    private String externalCustomerId;
    private String birthday;
}

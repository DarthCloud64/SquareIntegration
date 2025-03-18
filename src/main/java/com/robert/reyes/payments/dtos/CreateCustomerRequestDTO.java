package com.robert.reyes.payments.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCustomerRequestDTO {
    private String idempotencyKey;
    private String firstName;
    private String lastName;
    private String companyName;
    private String nickName;
    private String emailAddress;
    private AddressDTO address;
    private String phoneNumber;
    private String referenceId;
    private String note;
    private String birthday;    
}

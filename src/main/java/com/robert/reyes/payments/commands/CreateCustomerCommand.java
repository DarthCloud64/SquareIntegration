package com.robert.reyes.payments.commands;

import com.robert.reyes.payments.dtos.AddressDTO;
import com.robert.reyes.payments.utils.mediator.Command;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Data
public class CreateCustomerCommand implements Command<String> {
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

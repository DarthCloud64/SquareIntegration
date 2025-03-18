package com.robert.reyes.payments.application.services.payments;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.robert.reyes.payments.application.exceptions.InvalidCustomerException;
import com.robert.reyes.payments.domain.customers.Address;
import com.robert.reyes.payments.domain.customers.Customer;
import com.robert.reyes.payments.domain.locations.Location;
import com.robert.reyes.payments.domain.payments.PaymentProviderService;
import com.robert.reyes.payments.dtos.AddressDTO;
import com.robert.reyes.payments.dtos.CreateCustomerRequestDTO;
import com.robert.reyes.payments.dtos.GetLocationResponseDTO;
import com.robert.reyes.payments.dtos.GetLocationsResponseDTO;

@SpringBootTest
public class PaymentServiceImplTests {
    @Mock
    private PaymentProviderService paymentProviderService;

    @InjectMocks
    private PaymentServiceImpl paymentServiceImpl;

    @Test
    public void getLocationsSuccessfullyReturnsLocations() throws Exception{
        GetLocationsResponseDTO expectedResult = new GetLocationsResponseDTO();
        GetLocationResponseDTO getLocationResponseDTO = new GetLocationResponseDTO("test location");
        expectedResult.getLocations().add(getLocationResponseDTO);

        Location location = new Location();
        location.setName("test location");
        List<Location> locationsToReturn = List.of(location);
        when(paymentProviderService.getLocations())
            .thenReturn(CompletableFuture.completedFuture(locationsToReturn));

        paymentServiceImpl
            .getLocations()
            .thenAccept(result -> {
                assertThat(result).isEqualTo(expectedResult);
            });
    }

    @ParameterizedTest
    @MethodSource("createCustomerFailsGivenInvalidDataStream")
    public void createCustomerFailsGivenInvalidData(String desc, CreateCustomerRequestDTO createCustomerRequestDTO) throws Exception{
        assertThatExceptionOfType(InvalidCustomerException.class).isThrownBy(() -> {
            paymentServiceImpl.createCustomer(createCustomerRequestDTO)
            .thenAccept(result -> {
                fail("createCustomer() did not throw an exception");
            });
        });
    }

    @Test
    public void createCustomerSuccessful() throws Exception{
        AddressDTO addressDTO = new AddressDTO(
            "line1",
            "line2",
            "city",
            "state",
            "zip",
            "US");

        CreateCustomerRequestDTO createCustomerRequestDTO = new CreateCustomerRequestDTO(
            "idempotency",
            "first",
            "last",
            "company",
            "nick",
            "email",
            addressDTO,
            "phone",
            "ref",
            "note",
            "1991-12-22"
        );

        Customer customer = new Customer();
        customer.setFirstName("first");
        customer.setLastName("last");
        customer.setCompanyName("company");
        customer.setNickName("nick");
        customer.setEmailAddress("email");
        customer.setAddress(new Address(
            "line1",
            "line2",
            "city",
            "state",
            "zip",
            "US"));
        customer.setPhoneNumber("phone");
        customer.setBirthday("1991-12-22");

        when(paymentProviderService.createCustomer(argThat(x -> x.getFirstName() == customer.getFirstName()), eq("idempotency")))
            .thenReturn(CompletableFuture.completedFuture(customer));
        
        paymentServiceImpl
            .createCustomer(createCustomerRequestDTO)
            .thenAccept(result -> {
                assertThat(result.getNewCustomerId()).isNotBlank();
            });
    }

    private static Stream<Object[]> createCustomerFailsGivenInvalidDataStream() {
        return Stream.of(
            new Object[]{
                "First name is missing",
                new CreateCustomerRequestDTO(
                    "idempotency",
                    null,
                    "last",
                    "company",
                    "nickname",
                    "email",
                    new AddressDTO(
                        "line1",
                        "line2",
                        "city",
                        "state",
                        "zip",
                        "US"
                    ),
                    "phone",
                    "ref",
                    "note",
                    "1991-12-22"
                )
            },
            new Object[]{
                "Last name is missing",
                new CreateCustomerRequestDTO(
                    "idempotency",
                    "first",
                    null,
                    "company",
                    "nickname",
                    "email",
                    new AddressDTO(
                        "line1",
                        "line2",
                        "city",
                        "state",
                        "zip",
                        "US"
                    ),
                    "phone",
                    "ref",
                    "note",
                    "1991-12-22"
                )
            },
            new Object[]{
                "Email is missing",
                new CreateCustomerRequestDTO(
                    "idempotency",
                    "first",
                    "last",
                    "company",
                    "nickname",
                    null,
                    new AddressDTO(
                        "line1",
                        "line2",
                        "city",
                        "state",
                        "zip",
                        "US"
                    ),
                    "phone",
                    "ref",
                    "note",
                    "1991-12-22"
                )
            },
            new Object[]{
                "Phone is missing",
                new CreateCustomerRequestDTO(
                    "idempotency",
                    "first",
                    "last",
                    "company",
                    "nickname",
                    "email",
                    new AddressDTO(
                        "line1",
                        "line2",
                        "city",
                        "state",
                        "zip",
                        "US"
                    ),
                    null,
                    "ref",
                    "note",
                    "1991-12-22"
                )
            },
            new Object[]{
                "Birthday is missing",
                new CreateCustomerRequestDTO(
                    "idempotency",
                    "first",
                    "last",
                    "company",
                    "nickname",
                    "email",
                    new AddressDTO(
                        "line1",
                        "line2",
                        "city",
                        "state",
                        "zip",
                        "US"
                    ),
                    "phone",
                    "ref",
                    "note",
                    null
                )
            },
            new Object[]{
                "Birthday is less than 18 years old",
                new CreateCustomerRequestDTO(
                    "idempotency",
                    "first",
                    "last",
                    "company",
                    "nickname",
                    "email",
                    new AddressDTO(
                        "line1",
                        "line2",
                        "city",
                        "state",
                        "zip",
                        "US"
                    ),
                    "phone",
                    "ref",
                    "note",
                    "2020-01-01"
                )
            }
        );
    }
}

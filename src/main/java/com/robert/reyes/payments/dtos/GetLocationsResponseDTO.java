package com.robert.reyes.payments.dtos;

import java.util.ArrayList;

import lombok.Data;

@Data
public class GetLocationsResponseDTO {
    private ArrayList<GetLocationResponseDTO> locations = new ArrayList<GetLocationResponseDTO>();
}

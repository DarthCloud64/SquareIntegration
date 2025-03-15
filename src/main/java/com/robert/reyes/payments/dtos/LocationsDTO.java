package com.robert.reyes.payments.dtos;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class LocationsDTO {
    private ArrayList<LocationDTO> locations = new ArrayList<LocationDTO>();
}

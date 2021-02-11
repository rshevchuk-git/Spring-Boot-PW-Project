package com.bookly.backend.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FlatAddress {
    private int id;
    private String country;
    private String city;
    private String streetName;
    private String postCode;
    private String buildingNumber;
    private String flatNumber;
    private String flat;
}

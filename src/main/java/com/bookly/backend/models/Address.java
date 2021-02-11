package com.bookly.backend.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Address {
    private int id;
    private String country;
    private String town;
    private String streetName;
    private String streetNumber;
}

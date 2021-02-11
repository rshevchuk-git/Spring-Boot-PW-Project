package com.bookly.backend.models;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Flat {
    private int id;
    private int maxGuests;
    private int price;
    private String name;
    private String flatType;
    private FlatAddress address;
}

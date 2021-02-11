package com.bookly.backend.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class FlatlyResponseObject {
    private int id;
    private int noOfGuests;
    private int customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Flat flat;
    private boolean active;
}

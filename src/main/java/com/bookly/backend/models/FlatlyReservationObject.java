package com.bookly.backend.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class FlatlyReservationObject {
    private int noOfGuests;
    private int customerId;
    private int flatId;
    private LocalDate startDate;
    private LocalDate endDate;
}

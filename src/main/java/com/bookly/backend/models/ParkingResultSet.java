package com.bookly.backend.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ParkingResultSet {
    private int currentPage;
    private List<Parking> items;
    private int totalItems;
    private int totalPages;
}

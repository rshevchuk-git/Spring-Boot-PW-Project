package com.bookly.backend.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Car {
    private Integer id;
    private String name;
    private String model;
    private String location;
    private String status;
    private double dailyPrice;
    private String description;
    private String tags;
    private String plateNumber;
    private LocalDateTime globalStartDateTime;
    private LocalDateTime globalEndDateTime;
    private Free[] free;
}

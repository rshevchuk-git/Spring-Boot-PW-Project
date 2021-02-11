package com.bookly.backend.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CarlyResponseObject {
    private int id;
    private int carId;
    private int tenantId;
    private String tenantFirstname;
    private String tenantSurname;
    private String carModel;
    private String carPlateNumber;
    private String carDescription;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}

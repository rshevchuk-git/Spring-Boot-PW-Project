package com.bookly.backend.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CarlyReservationObject {
    private Long carId;
    private String tenantFirstname;
    private String tenantSurname;
    private Long tenantId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}

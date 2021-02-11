package com.bookly.backend.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ParklyReservationObject {
    private Long parkingId;
    private Long userId;
    private String userFirstName;
    private String userLastName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}

package com.bookly.backend.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ParklyResponseObject {
    private int id;
    private int userId;
    private String userFirstName;
    private String userLastName;
    private String parkingName;
    private int price;
    private int parkingId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}

package com.bookly.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Reservation {
    private Long itemId;
    private Long userId;
    private ItemType itemType;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}

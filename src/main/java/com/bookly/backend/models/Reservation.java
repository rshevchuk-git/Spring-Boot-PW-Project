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

    public boolean isInvalid() {
        return isInvalidDate() || isInvalidItem() || isInvalidUser();
    }

    private boolean isInvalidDate() {
        return this.getFromDate() == null || this.getToDate() == null;
    }

    private boolean isInvalidItem() {
        return this.getItemId() == null || this.getItemId() < 0 || this.getItemType() == null;
    }

    private boolean isInvalidUser() {
        return this.getUserId() == null || this.getUserId() < 0;
    }
}

package com.bookly.backend.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Free {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean valid;
    private boolean empty;
}

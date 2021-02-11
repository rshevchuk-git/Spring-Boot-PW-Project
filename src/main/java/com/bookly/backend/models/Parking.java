package com.bookly.backend.models;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Parking {
    private int id;
    private String name;
    private int spotsTaken;
    private int spotsTotal;
    private String ownerCompanyName;
    private Address address;
    private LocalDateTime addedDateTime;
}

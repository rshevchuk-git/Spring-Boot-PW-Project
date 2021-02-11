package com.bookly.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User owner;
    @Column(name = "start_date")
    private LocalDateTime startDateTime;
    @Column(name = "is_active")
    private boolean isActive;
    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @Enumerated(EnumType.STRING)
    private ItemType itemType;
}

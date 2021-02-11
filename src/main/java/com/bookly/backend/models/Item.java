package com.bookly.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(name = "external_id")
    private int itemId;

    @Column(name = "name")
    private String name;
    @Column(name = "start_date")
    private LocalDateTime startDateTime;
    @Column(name = "end_date")
    private LocalDateTime endDateTime;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "details")
    private String details;
}

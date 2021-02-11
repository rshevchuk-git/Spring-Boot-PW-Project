package com.bookly.backend.models.flatly;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FlatsResultSet {
    private List<Flat> content;
}

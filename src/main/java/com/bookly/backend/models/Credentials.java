package com.bookly.backend.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class Credentials {
    private String login;
    private String password;
}

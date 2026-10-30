package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    private Long id;
    private String username;
    @EqualsAndHashCode.Include
    private String email;
    private String password;
    private Instant registrationDate;
}

package com.tulip.project.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Setter
@Getter
public final class UserDTO {
    private String name;
    private String email;
    private String password;
    private LocalDate dateOfBirth;
    private String address;
    private String[] roles;

}

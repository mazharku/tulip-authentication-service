package com.tulip.project.model.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LoginResponseDTO {
    private String token;
    private String refreshToken;
    private String expireTime;
}

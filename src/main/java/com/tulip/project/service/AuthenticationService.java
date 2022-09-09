package com.tulip.project.service;

import com.tulip.project.exception.CustomGeneratedException;
import com.tulip.project.jwt.JWTTokenService;
import com.tulip.project.model.dtos.LoginDTO;
import com.tulip.project.model.dtos.LoginResponseDTO;
import com.tulip.project.model.entities.User;
import com.tulip.project.repository.BlacklistedTokenDAO;
import com.tulip.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Value("${jwt.token-expire-time}")
    int tokenExpireTime;
    private final UserRepository userRepository;
    private final JWTTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final BlacklistedTokenDAO blacklistedTokenDAO;
    private final HttpServletRequest servletRequest;

    @SneakyThrows
    public LoginResponseDTO getToken(LoginDTO loginModel) {
        User user = userRepository.findByEmail(loginModel.getEmail())
                .orElseThrow(() -> new CustomGeneratedException("invalid email!"));
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginModel.getEmail(), loginModel.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenService.generateToken(user);
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setToken(token);
        loginResponseDTO.setRefreshToken(jwtTokenService.generateRefreshToken());
        loginResponseDTO.setExpireTime(tokenExpireTime + " minutes");
        return loginResponseDTO;
    }

    public void logoutUser() {
        String authHeader = servletRequest.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        Date expiryTime = jwtTokenService.getExpiryFromJwt(jwt);
        blacklistedTokenDAO.setTokenBlacklisted(jwt,expiryTime);
    }
}

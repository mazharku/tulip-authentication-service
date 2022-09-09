package com.tulip.project.controller;

import com.tulip.project.model.dtos.LoginDTO;
import com.tulip.project.service.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "bearerAuth")
public class AuthenticationController {
    private final AuthenticationService service;

    @Tags(value = @Tag(name = "login"))
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginModel) {
        return ResponseEntity.ok(service.getToken(loginModel));
    }

    @Tags(value = @Tag(name = "logout user"))
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        service.logoutUser();
        return ResponseEntity.ok("user is created successfully!");
    }

}

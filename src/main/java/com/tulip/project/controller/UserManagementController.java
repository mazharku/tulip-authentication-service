package com.tulip.project.controller;

import com.tulip.project.model.dtos.UserDTO;
import com.tulip.project.service.UserManagementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserManagementController {
    private final UserManagementService service;

    @Tags(value = @Tag(name = "create user"))
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserDTO user) {
        service.createUser(user);
        return ResponseEntity.ok("user is created successfully!");

    }

}

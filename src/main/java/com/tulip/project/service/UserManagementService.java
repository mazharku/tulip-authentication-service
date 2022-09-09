package com.tulip.project.service;

import com.tulip.project.model.dtos.UserDTO;
import com.tulip.project.model.entities.User;
import com.tulip.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserManagementService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public void createUser(UserDTO userDTO) {
        User user = getUser(userDTO);
        userRepository.save(user);
    }

    public User getUser(UserDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setAddress(dto.getAddress());
        user.setIsActive(true);
        return user;
    }

}

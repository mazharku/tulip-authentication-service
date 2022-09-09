package com.tulip.project.repository;

import com.tulip.project.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT DISTINCT user from User user JOIN FETCH user.authorities where user.email = :email")
    Optional<User> findByEmailWithAuthorities(@Param("email") String email);

}

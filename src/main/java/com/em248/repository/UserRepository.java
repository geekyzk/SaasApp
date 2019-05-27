package com.em248.repository;

import com.em248.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.stream.DoubleStream;

public interface UserRepository  extends JpaRepository<User,String> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}

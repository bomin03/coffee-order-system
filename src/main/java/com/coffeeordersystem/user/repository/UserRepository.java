package com.coffeeordersystem.user.repository;

import com.coffeeordersystem.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

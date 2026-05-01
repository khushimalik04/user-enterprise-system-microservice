package com.user.repository;

import com.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * User persistence with an additional lookup by the business identifiers used by the API.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserCodeAndCompanyName(String userCode, String companyName);
}

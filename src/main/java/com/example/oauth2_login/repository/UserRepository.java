package com.example.oauth2_login.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.oauth2_login.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
//    Optional<User> findByProviderUserId(String providerUserId);
    Optional<User> findByProviderIdAndProviderUserId(String providerId, String providerUserId);
//    Optional<User> findByProviderAndProviderUserId(String provider, String providerUserId);
}
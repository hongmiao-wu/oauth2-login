package com.example.oauth2_login.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.oauth2_login.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{

}

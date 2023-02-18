package com.avega.task.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avega.task.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Optional<Product> findById(int id);
}
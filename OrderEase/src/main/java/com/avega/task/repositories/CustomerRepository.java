package com.avega.task.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avega.task.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmailIdOrMobileNumber (String emailId, String mobileNumber);
}
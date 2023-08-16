package com.example.shopman.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopman.entity.Customer;
import com.example.shopman.entity.dto.CustomerSummary;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	Page<CustomerSummary> findAllBy(Pageable pageable); 
}

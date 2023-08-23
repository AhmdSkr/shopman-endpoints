package com.example.shopman.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopman.entity.InvoiceLine;

public interface InvoiceLineRepository extends JpaRepository<InvoiceLine, Long> {
	Page<InvoiceLine> findAllByInvoiceId(Long id, Pageable pageable);
}

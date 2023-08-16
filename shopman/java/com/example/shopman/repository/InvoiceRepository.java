package com.example.shopman.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import com.example.shopman.entity.Customer;
import com.example.shopman.entity.Invoice;
import com.example.shopman.entity.dto.InvoiceSummary;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
	
	Page<InvoiceSummary> findAllByOwnerOrderByCreationInstantDesc(@Nullable Customer owner, Pageable pageable);
	
	Page<InvoiceSummary> findAllByOrderByCreationInstantDesc(Pageable pageable);
}

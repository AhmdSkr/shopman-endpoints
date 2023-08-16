package com.example.shopman.entity.dto;

import java.time.Instant;

public interface InvoiceSummary {

	Long getId();

	CustomerSummary getOwner();

	Long getValue();

	Instant getCreationInstant();

	Instant getLastModificationInstant();
}

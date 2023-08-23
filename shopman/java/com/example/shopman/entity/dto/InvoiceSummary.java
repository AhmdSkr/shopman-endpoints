package com.example.shopman.entity.dto;

import java.time.Instant;

public interface InvoiceSummary {

	Long getId();

	CustomerSummary getOwner();

	Instant getCreationInstant();

	Instant getLastModificationInstant();
}

package com.example.shopman.entity.dto;

public interface InvoiceLineSummary {

	Long getId();

	ItemSummary getItem();

	Integer getQuantity();

	Long getTotal();
}

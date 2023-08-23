package com.example.shopman.entity.dto;

import java.util.Optional;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InvoiceLinePatchDto {
	public Optional<Long> itemId;
	public Optional<Integer> quantity;
	public Optional<Long> total;
}

package com.example.shopman.entity.dto;

import java.util.Collection;
import java.util.Optional;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InvoicePostDto {

	public Optional<Long> ownerId;
	public Collection<InvoiceLinePostDto> lines;
}

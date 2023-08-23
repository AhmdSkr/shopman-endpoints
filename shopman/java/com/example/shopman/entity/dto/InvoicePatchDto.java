package com.example.shopman.entity.dto;

import java.util.Collection;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class InvoicePatchDto {
	public Optional<Long> ownerId;
	public Collection<InvoiceLinePostDto> newLines;
}

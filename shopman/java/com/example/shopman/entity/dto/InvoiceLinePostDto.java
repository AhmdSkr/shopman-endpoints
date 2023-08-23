package com.example.shopman.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class InvoiceLinePostDto {

	public Long itemId;

	public int quantity;

	public long total;
}

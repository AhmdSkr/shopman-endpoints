package com.example.shopman.entity.dto;

import java.util.Optional;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InvoicePatchDto {

	public Optional<Long> ownerId;

	public Optional<Long> value;

}

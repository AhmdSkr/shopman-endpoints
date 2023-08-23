package com.example.shopman.entity.dto;

import java.util.Optional;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ItemPatchDto {

	public Optional<String> description;

	public Optional<Long> price;

	public Optional<Long> cost;
}

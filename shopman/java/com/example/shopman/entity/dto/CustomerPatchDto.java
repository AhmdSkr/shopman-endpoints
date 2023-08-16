package com.example.shopman.entity.dto;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.example.shopman.entity.info.Gender;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomerPatchDto {

	public Optional<String> firstname;

	public Optional<String> lastname;

	public Optional<Gender> gender;

	@DateTimeFormat(iso = ISO.DATE)
	public Optional<LocalDate> birthDate;

}

package com.example.shopman.entity.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.example.shopman.entity.info.Gender;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomerPostDto {

	public String firstname;

	public String lastname;

	public Gender gender;

	@DateTimeFormat(iso = ISO.DATE)
	public LocalDate birthDate;

}

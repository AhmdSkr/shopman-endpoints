package com.example.shopman.controller;

import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.shopman.entity.InvoiceLine;
import com.example.shopman.entity.dto.InvoiceLinePatchDto;
import com.example.shopman.repository.InvoiceLineRepository;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/lines")
public class InvoiceLineController {
	private InvoiceLineRepository repository;

	@ResponseStatus(HttpStatus.OK)
	@PatchMapping("/{lineId}")
	public void updateLine(@PathVariable(name = "lineId", required = true) Long id,
			@RequestBody InvoiceLinePatchDto in) {
		Optional<InvoiceLine> lineOpt = repository.findById(id);
		if (!lineOpt.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		repository.save(lineOpt.get().patch(in));
	}
	
	@DeleteMapping("/{lineId}")
	public void deleteLine(@PathVariable(name = "lineId", required = true) Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
	}
}

package com.example.shopman.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopman.entity.InvoiceLine;
import com.example.shopman.entity.dto.InvoiceLinePatchDto;
import com.example.shopman.repository.InvoiceLineRepository;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/lines")
public class InvoiceLineController {
	private InvoiceLineRepository repository;

	@PatchMapping("/{lineId}")
	public ResponseEntity<InvoiceLine> updateLine(@PathVariable(name = "lineId", required = true) Long id,
			@RequestBody InvoiceLinePatchDto in) {
		Optional<InvoiceLine> lineOpt = repository.findById(id);
		if (!lineOpt.isPresent()) {
			// TODO: handle failure;
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(repository.save(lineOpt.get().patch(in)));
	}

	@DeleteMapping("/{lineId}")
	public void deleteLine(@PathVariable(name = "lineId", required = true) Long id) {
		repository.deleteById(id);
	}
}

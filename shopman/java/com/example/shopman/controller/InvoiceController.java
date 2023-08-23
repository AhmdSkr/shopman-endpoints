package com.example.shopman.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopman.entity.Customer;
import com.example.shopman.entity.Invoice;
import com.example.shopman.entity.InvoiceLine;
import com.example.shopman.entity.dto.InvoiceLinePostDto;
import com.example.shopman.entity.dto.InvoicePatchDto;
import com.example.shopman.entity.dto.InvoicePostDto;
import com.example.shopman.entity.dto.InvoiceSummary;
import com.example.shopman.repository.InvoiceLineRepository;
import com.example.shopman.repository.InvoiceRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/invoices")
public class InvoiceController {

	private static int PAGE_SIZE_DEFAULT = 30;

	private InvoiceRepository repository;
	private InvoiceLineRepository lineRepository;

	@GetMapping
	public ResponseEntity<Page<InvoiceSummary>> fetchAllInvoices(
			@RequestParam(name = "page", defaultValue = "0", required = false) int page,
			@RequestParam(name = "owner", required = false) Optional<Long> ownerId) {

		PageRequest paging = PageRequest.of(page, PAGE_SIZE_DEFAULT);
		Page<InvoiceSummary> pagination;

		if (!ownerId.isPresent()) {
			pagination = repository.findAllByOrderByCreationInstantDesc(paging);
		} else {
			Customer customer = new Customer().withId(ownerId.get());
			pagination = repository.findAllByOwnerOrderByCreationInstantDesc(customer, paging);
		}

		return ResponseEntity.ok(pagination);
	}

	@GetMapping("/no-owner")
	public ResponseEntity<Page<InvoiceSummary>> fetchAllInvoicesNotOwned(
			@RequestParam(name = "page", defaultValue = "0", required = false) int page) {

		PageRequest paging = PageRequest.of(page, PAGE_SIZE_DEFAULT);
		Page<InvoiceSummary> pagination = repository.findAllByOwnerOrderByCreationInstantDesc(null, paging);
		return ResponseEntity.ok(pagination);
	}

	@GetMapping("/{invoiceId}")
	public ResponseEntity<Invoice> fetchInvoice(@PathVariable(name = "invoiceId", required = true) long id) {
		return ResponseEntity.of(repository.findById(id));
	}

	@GetMapping("/{invoiceId}/lines")
	public ResponseEntity<Page<InvoiceLine>> fetchAllLines(
			@RequestParam(name = "page", defaultValue = "0", required = false) int page,
			@RequestParam(name = "invoiceId", required = true) Long invoiceId) {
		PageRequest paging = PageRequest.of(page, PAGE_SIZE_DEFAULT);
		return ResponseEntity.ok(lineRepository.findAllByInvoiceId(invoiceId, paging));
	}

	@PostMapping
	public ResponseEntity<Invoice> postInvoice(@RequestBody InvoicePostDto in) {

		// TODO: validate input before accessing database
		Invoice invoice = new Invoice(in);
		invoice = repository.save(invoice);
		// TODO: handle failure
		if(in.lines != null) {
			Collection<InvoiceLine> lines = new ArrayList<>(in.lines.size());
			for(InvoiceLinePostDto dto : in.lines) {
				InvoiceLine line = new InvoiceLine(invoice.getId(), dto);
				lines.add(line);
			}
			invoice.setLines(lines);
			invoice = repository.save(invoice);
		}
		return ResponseEntity.ok(invoice);
	}

	@PatchMapping("/{invoiceId}")
	public ResponseEntity<Invoice> patchInvoice(@PathVariable(name = "invoiceId", required = true) long id,
			@RequestBody InvoicePatchDto in) {

		Invoice invoice;
		// TODO: validate data before accessing database
		Optional<Invoice> invoiceOpt = repository.findById(id);
		if (!invoiceOpt.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		invoice = invoiceOpt.get().patch(in);
		invoice = repository.save(invoice);
		// TODO: handle fail
		return ResponseEntity.ok(invoice);
	}

	@DeleteMapping("/{invoiceId}")
	public void deleteInvoice(@PathVariable(name = "invoiceId", required = true) long id) {
		repository.deleteById(id);
	}

}

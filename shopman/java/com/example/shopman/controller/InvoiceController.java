package com.example.shopman.controller;

import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.shopman.entity.Customer;
import com.example.shopman.entity.Invoice;
import com.example.shopman.entity.dto.InvoicePatchDto;
import com.example.shopman.entity.dto.InvoicePostDto;
import com.example.shopman.entity.dto.InvoiceSummary;
import com.example.shopman.repository.InvoiceRepository;
import com.example.shopman.validation.NegativePageException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(InvoiceController.INVOICE_ROUTE_PREFIX)
public class InvoiceController {

	private static final String INVOICE_ROUTE_PREFIX = "/invoices";
	private static final String INVOICE_ID_PARAMETER = "/{invoiceId:[0-9]+}";
	private static int PAGE_SIZE_DEFAULT = 30;

	private InvoiceRepository repository;	

	@GetMapping
	public ResponseEntity<Page<InvoiceSummary>> fetchAllInvoices(
			@RequestParam(name = "page", defaultValue = "0", required = false) int page,
			@RequestParam(name = "owner", required = false) Optional<Long> ownerId) throws NegativePageException {
		
		if (page < 0) {
			throw new NegativePageException(page);
		}
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
			@RequestParam(name = "page", defaultValue = "0", required = false) int page) throws NegativePageException {
		if (page < 0) {
			throw new NegativePageException(page);
		}
		return ResponseEntity.ok(repository.findAllByOwnerOrderByCreationInstantDesc(null, PageRequest.of(page, PAGE_SIZE_DEFAULT)));
	}

	@GetMapping(INVOICE_ID_PARAMETER)
	public ResponseEntity<Invoice> fetchInvoice(@PathVariable(name = "invoiceId", required = true) long id) {
		return ResponseEntity.of(repository.findById(id));
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public void postInvoice(@RequestBody InvoicePostDto in) {
		repository.save(new Invoice(in));
	}

	@ResponseStatus(HttpStatus.OK)
	@PatchMapping(INVOICE_ID_PARAMETER)
	public void patchInvoice(@PathVariable(name = "invoiceId", required = true) long id,
			@RequestBody InvoicePatchDto in) {
		Optional<Invoice> invoiceOpt = repository.findById(id);
		if (!invoiceOpt.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		repository.save(invoiceOpt.get().patch(in));
	}

	@DeleteMapping(INVOICE_ID_PARAMETER)
	public void deleteInvoice(@PathVariable(name = "invoiceId", required = true) long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

}

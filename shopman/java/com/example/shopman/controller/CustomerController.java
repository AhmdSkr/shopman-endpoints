package com.example.shopman.controller;

import java.util.ArrayList;
import java.util.Collection;
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
import com.example.shopman.entity.dto.CustomerPatchDto;
import com.example.shopman.entity.dto.CustomerPostDto;
import com.example.shopman.entity.dto.CustomerSummary;
import com.example.shopman.repository.CustomerRepository;
import com.example.shopman.validation.NegativePageException;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(CustomerController.CUSTOMER_ROUTE_PREFIX)
public class CustomerController {

	private static final String CUSTOMER_ROUTE_PREFIX = "/customers";
	private static final String CUSTOMER_ID_PARAMETER = "/{customerId:[0-9]+}";

	private static int PAGE_SIZE_DEFAULT = 30;

	private CustomerRepository repository;

	@GetMapping
	public ResponseEntity<Page<CustomerSummary>> fetchAllCustomers(
			@RequestParam(name = "page", defaultValue = "0", required = false) int page) throws NegativePageException {
		if (page < 0) {
			throw new NegativePageException(page);
		}
		return ResponseEntity.ok(repository.findAllBy(PageRequest.of(page, PAGE_SIZE_DEFAULT)));
	}

	@GetMapping(CUSTOMER_ID_PARAMETER)
	public ResponseEntity<Customer> fetchCustomer(@PathVariable(name = "customerId", required = true) long id) {
		return ResponseEntity.of(repository.findById(id));
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public void postCustomer(@RequestBody CustomerPostDto in) {
		repository.save(new Customer(in));
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/batch")
	public void batchPostCustomers(@RequestBody Collection<CustomerPostDto> batch) {
		Collection<Customer> customers = new ArrayList<>(batch.size());
		batch.forEach((in) -> customers.add(new Customer(in)));
		repository.saveAll(customers);
	}

	@ResponseStatus(HttpStatus.OK)
	@PatchMapping(CUSTOMER_ID_PARAMETER)
	public void patchCustomer(@PathVariable(name = "customerId", required = true) long id, @RequestBody CustomerPatchDto in) {
		Optional<Customer> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		repository.save(optional.get().patch(in));
	}

	@DeleteMapping(CUSTOMER_ID_PARAMETER)
	public void deleteCustomer(@PathVariable(name = "customerId", required = true) long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
}

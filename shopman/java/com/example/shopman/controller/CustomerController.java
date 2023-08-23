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
import com.example.shopman.entity.dto.CustomerPatchDto;
import com.example.shopman.entity.dto.CustomerPostDto;
import com.example.shopman.entity.dto.CustomerSummary;
import com.example.shopman.repository.CustomerRepository;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

	private static int PAGE_SIZE_DEFAULT = 30;

	private CustomerRepository repository;

	@GetMapping
	public ResponseEntity<Page<CustomerSummary>> fetchAllCustomers(
			@RequestParam(name = "page", defaultValue = "0", required = false) int page) {
		PageRequest paging = PageRequest.of(page, PAGE_SIZE_DEFAULT);
		return ResponseEntity.ok(repository.findAllBy(paging));
	}

	@GetMapping("/{customerId}")
	public ResponseEntity<Customer> fetchCustomer(@PathVariable(name = "customerId", required = true) long id) {
		return ResponseEntity.of(repository.findById(id));
	}

	@PostMapping
	public ResponseEntity<Customer> postCustomer(@RequestBody CustomerPostDto in) {
		// TODO: validate input before accessing database
		Customer created = repository.save(new Customer(in));
		// TODO: handle failure
		return ResponseEntity.ok(created);
	}
	
	@PostMapping("/batch")
	public void batchPostCustomers(@RequestBody Collection<CustomerPostDto> batch) {
		Collection<Customer> customers = new ArrayList<>(batch.size());
		batch.forEach((in) -> customers.add(new Customer(in)));
		repository.saveAll(customers);
	}

	@PatchMapping("/{customerId}")
	public ResponseEntity<Customer> patchCustomer(@PathVariable(name = "customerId", required = true) long id,
			@RequestBody CustomerPatchDto in) {
		Optional<Customer> optional = repository.findById(id);
		if (!optional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(repository.save(optional.get().patch(in)));
	}

	@DeleteMapping("/{customerId}")
	public void deleteCustomer(@PathVariable(name = "customerId", required = true) long id) {
		repository.deleteById(id);
	}
}

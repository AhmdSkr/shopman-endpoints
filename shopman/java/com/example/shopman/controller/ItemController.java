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

import com.example.shopman.entity.Item;
import com.example.shopman.entity.dto.ItemPatchDto;
import com.example.shopman.entity.dto.ItemPostDto;
import com.example.shopman.entity.dto.ItemSummary;
import com.example.shopman.repository.ItemRepository;
import com.example.shopman.validation.NegativePageException;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(ItemController.ITEM_ROUTE_PREFIX)
public class ItemController {

	private static final String ITEM_ROUTE_PREFIX = "/items";
	private static final String ITEM_ID_PARAMETER = "/{itemId:[0-9]+}";

	private static int PAGE_SIZE_DEFAULT = 30;

	private ItemRepository repository;

	@GetMapping
	public ResponseEntity<Page<ItemSummary>> fetchAllItems(
			@RequestParam(name = "page", defaultValue = "0", required = false) int page) throws NegativePageException {
		if (page < 0) {
			throw new NegativePageException(page);
		}
		return ResponseEntity.ok(repository.findAllBy(PageRequest.of(page, PAGE_SIZE_DEFAULT)));
	}

	@GetMapping("/code/{code}")
	public ResponseEntity<Item> fetchItemByBarcode(@PathVariable(name = "code", required = true) String code) {
		return ResponseEntity.of(repository.findByCode(code));
	}

	@GetMapping(ITEM_ID_PARAMETER)
	public ResponseEntity<Item> fetchItem(@PathVariable(name = "itemId", required = true) Long id) {
		return ResponseEntity.of(repository.findById(id));
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public void postItem(@RequestBody ItemPostDto in) {
		repository.save(new Item(in));
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/batch")
	public void batchPostItems(@RequestBody Collection<ItemPostDto> batchItems) {
		Collection<Item> items = new ArrayList<>(batchItems.size());
		batchItems.forEach((in) -> items.add(new Item(in)));
		repository.saveAll(items);
	}

	@ResponseStatus(HttpStatus.OK)
	@PatchMapping(ITEM_ID_PARAMETER)
	public void patchItem(@PathVariable(name = "itemId", required = true) Long id, @RequestBody ItemPatchDto in) {
		Optional<Item> itemOpt = repository.findById(id);
		if (!itemOpt.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		repository.save(itemOpt.get().patch(in));
	}

	@DeleteMapping(ITEM_ID_PARAMETER)
	public void deleteItem(@PathVariable(name = "itemId", required = true) Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
}

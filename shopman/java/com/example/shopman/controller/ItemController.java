package com.example.shopman.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopman.entity.Item;
import com.example.shopman.entity.dto.ItemPatchDto;
import com.example.shopman.entity.dto.ItemPostDto;
import com.example.shopman.entity.dto.ItemSummary;
import com.example.shopman.repository.ItemRepository;

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
			@RequestParam(name = "page", defaultValue = "0", required = false) int page) {
		return ResponseEntity.ok(repository.findAllBy(PageRequest.of(page, PAGE_SIZE_DEFAULT)));
	}

	@GetMapping("/cod/{code}")
	public ResponseEntity<Item> fetchItemByBarcode(@PathVariable(name = "code", required = true) String code) {
		return ResponseEntity.of(repository.findByCode(code));
	}

	@GetMapping(ITEM_ID_PARAMETER)
	public ResponseEntity<Item> fetchItem(@PathVariable(name = "itemId", required = true) Long id) {
		return ResponseEntity.of(repository.findById(id));
	}

	@PostMapping
	public ResponseEntity<Item> postItem(ItemPostDto in) {
		// TODO: validate prior of data access
		Item item = new Item(in);
		item = repository.save(item);
		// TODO: handle failure
		return ResponseEntity.ok(item);
	}

	@PatchMapping(ITEM_ID_PARAMETER)
	public ResponseEntity<Item> patchItem(@PathVariable(name = "itemId", required = true) Long id, ItemPatchDto in) {
		// TODO: validate prior of data access
		Optional<Item> itemOpt = repository.findById(id);
		if (!itemOpt.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Item item = itemOpt.get().patch(in);
		if (item != null) {
			item = repository.save(item);
		}
		// TODO: handle failure
		return ResponseEntity.accepted().body(item);
	}

	@DeleteMapping(ITEM_ID_PARAMETER)
	public void deleteItem(@PathVariable(name = "itemId", required = true) Long id) {
		repository.deleteById(id);
	}
}

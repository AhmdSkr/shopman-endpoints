package com.example.shopman.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopman.entity.Item;
import com.example.shopman.entity.dto.ItemSummary;

public interface ItemRepository extends JpaRepository<Item, Long> {

	Optional<Item> findByCode(String code);

	Page<ItemSummary> findAllBy(Pageable pageable);

}

package com.example.shopman.entity;

import java.time.Instant;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.shopman.entity.dto.ItemPatchDto;
import com.example.shopman.entity.dto.ItemPostDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "items", schema = "shop")
@AllArgsConstructor
@Data
public class Item {

	public static final int MAX_CODE_LENGTH = 255;
	public static final int MAX_DESCRIPTION_LENGTH = 255;

	public Item() {
		this.id = null;
		this.code = null;
	}

	
	public Item(ItemPostDto obj) {
		this(null, obj.code, obj.description, obj.price, obj.cost, null, null);
	}

	@Id
	@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "item_id_generator", sequenceName = "item_id_sequence")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_id_generator")
	private final Long id;

	public Item withId(Long id) {
		return new Item(id, this.code, this.description, this.price, this.cost, this.creationInstant,
				this.lastModificationInstant);
	}

	@Column(length = Item.MAX_CODE_LENGTH, unique = true)
	private final String code;

	public Item withCode(String code) {
		return new Item(null, code, this.description, this.price, this.cost, this.creationInstant,
				this.lastModificationInstant);
	}

	@Column(name = "description", length = Item.MAX_DESCRIPTION_LENGTH, nullable = false)
	private @Access(AccessType.PROPERTY) String description;

	@Column(name = "price", nullable = false)
	private @Access(AccessType.PROPERTY) long price;

	@Column(name = "cost")
	private @Access(AccessType.PROPERTY) Long cost;

	@CreatedDate
	private @Access(AccessType.PROPERTY) Instant creationInstant;

	@LastModifiedDate
	private @Access(AccessType.PROPERTY) Instant lastModificationInstant;

	public Item patch(ItemPatchDto obj) {
		Item item = this;
		
		obj.description.ifPresent(this::setDescription);
		obj.cost.ifPresent(this::setCost);
		obj.price.ifPresent(this::setPrice);
		if (obj.code.isPresent()) {
			item = this.withCode(code);
		}
		
		return item;
	}

}

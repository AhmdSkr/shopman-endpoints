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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.shopman.entity.dto.ItemPatchDto;
import com.example.shopman.entity.dto.ItemPostDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "items", schema = "shop", uniqueConstraints = @UniqueConstraint(name = "unique_item_code", columnNames = {
		"code" }))
@AllArgsConstructor
@Data
public class Item {

	public static final int MAX_CODE_LENGTH = 255;
	public static final int MAX_DESCRIPTION_LENGTH = 255;
	public static final String MESSAGE_CODE_NULL = "items' code should not be null";
	public static final String MESSAGE_CODE_BLANK = "items' code should not be blank";
	public static final String MESSAGE_CODE_LARGE = "items' code should not be less than " + MAX_CODE_LENGTH
			+ " characters";
	public static final String MESSAGE_CODE_DUPLICATE = "items' code should not be duplicated";
	public static final String MESSAGE_DESCRIPTION_NULL = "items' description should not be null";
	public static final String MESSAGE_DESCRIPTION_BLANK = "items' description should not be blank";
	public static final String MESSAGE_DESCRIPTION_LARGE = "items' description should not be less than "
			+ MAX_CODE_LENGTH + " characters";
	public static final String MESSAGE_PRICE_NULL = "items' price should not be null";
	public static final String MESSAGE_PRICE_NEGATIVE = "items' price should not be negative";
	public static final String MESSAGE_COST_NEGATIVE = "items' cost should not be negative";

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

	@NotNull(message = MESSAGE_CODE_NULL)
	@NotBlank(message = MESSAGE_CODE_BLANK)
	@Size(max = MAX_CODE_LENGTH, message = MESSAGE_CODE_LARGE)
	@Column(length = Item.MAX_CODE_LENGTH)
	private final String code;

	public Item withCode(String code) {
		return new Item(null, code, this.description, this.price, this.cost, this.creationInstant,
				this.lastModificationInstant);
	}

	@NotNull(message = MESSAGE_DESCRIPTION_NULL)
	@NotBlank(message = MESSAGE_DESCRIPTION_BLANK)
	@Size(max = MAX_DESCRIPTION_LENGTH, message = MESSAGE_DESCRIPTION_LARGE)
	@Column(name = "description", length = Item.MAX_DESCRIPTION_LENGTH, nullable = false)
	private @Access(AccessType.PROPERTY) String description;

	@NotNull(message = MESSAGE_PRICE_NULL)
	@PositiveOrZero(message = MESSAGE_PRICE_NEGATIVE)
	@Column(name = "price", nullable = false)
	private @Access(AccessType.PROPERTY) long price;

	@PositiveOrZero(message = MESSAGE_COST_NEGATIVE)
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
		return item;
	}

}

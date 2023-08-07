package com.example.shopman.item;

import java.time.Instant;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
		this.code = null;
	}

	@Id
	@Column(length = Item.MAX_CODE_LENGTH)
	private final String code;

	public Item withCode(String code) {
		return new Item(code, this.description, this.price, this.cost, this.creationInstant, this.lastModificationInstant);
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

}

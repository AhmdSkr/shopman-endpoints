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
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.shopman.entity.dto.InvoicePatchDto;
import com.example.shopman.entity.dto.InvoicePostDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "invoices", schema = "shop")
@AllArgsConstructor
@Data
public class Invoice {

	public Invoice() {
		this.id = null;
	}

	public Invoice(InvoicePostDto in) {
		this();
		this.value = in.value;
		this.owner = null;
		
		if(in.ownerId.isPresent()) {
			this.owner = new Customer().withId(in.ownerId.get());
		}
	}
	
	@Id
	@SequenceGenerator(initialValue = 1000, allocationSize = 1, name = "invoice_id_generator", sequenceName = "invoice_id_sequence")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_id_generator")
	private final Long id;

	public Invoice withId(Long id) {
		return new Invoice(id, this.owner, this.value, this.creationInstant, this.lastModificationInstant);
	} 
	
	@ManyToOne(optional = true)
	private @Access(AccessType.PROPERTY) Customer owner;
	
	@Column(name = "value", nullable = false)
	private @Access(AccessType.PROPERTY) long value;

	@CreatedDate
	private @Access(AccessType.PROPERTY) Instant creationInstant;

	@LastModifiedDate
	private @Access(AccessType.PROPERTY) Instant lastModificationInstant;
	
	public Invoice patch(InvoicePatchDto in) {
		if(in.ownerId.isPresent()) {
			Long ownerId = in.ownerId.get();
			this.owner = new Customer().withId(ownerId);
		}
		in.value.ifPresent(this::setValue);
		return this;
	}

}

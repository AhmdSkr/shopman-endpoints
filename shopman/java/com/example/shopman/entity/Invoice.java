package com.example.shopman.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.shopman.entity.dto.InvoiceLinePostDto;
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
	
	public static final String MESSAGE_LINES_NULL = "invoices' lines collection should not be null";
	public static final String MESSAGE_LINES_EMPTY = "invoices' lines should not be empty";
	public static final String MESSAGE_LINE_NULL = "invoices' lines should not be null";

	public Invoice() {
		this.id = null;
		this.owner = null;
		this.lines = new ArrayList<>();
	}

	public Invoice(InvoicePostDto in) {
		this();
		if (in.ownerId.isPresent()) {
			this.owner = new Customer().withId(in.ownerId.get());
		}
		for(InvoiceLinePostDto lineDto : in.lines) {
			InvoiceLine line = new InvoiceLine(lineDto);
			line.setInvoice(this);
			this.getLines().add(line);
		}
	}

	@Id
	@SequenceGenerator(initialValue = 1000, allocationSize = 1, name = "invoice_id_generator", sequenceName = "invoice_id_sequence")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_id_generator")
	private final Long id;

	public Invoice withId(Long id) {
		return new Invoice(id, this.owner, this.lines, this.creationInstant, this.lastModificationInstant);
	}

	@ManyToOne(optional = true)
	private @Access(AccessType.PROPERTY) Customer owner;

	@NotNull(message = MESSAGE_LINES_NULL)
	@NotEmpty(message = MESSAGE_LINES_EMPTY)
	@OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
	private Collection<@NotNull(message = MESSAGE_LINE_NULL) InvoiceLine> lines;

	@CreatedDate
	private @Access(AccessType.PROPERTY) Instant creationInstant;

	@LastModifiedDate
	private @Access(AccessType.PROPERTY) Instant lastModificationInstant;

	public Invoice patch(InvoicePatchDto in) {
		if (in.ownerId.isPresent()) {
			Long ownerId = in.ownerId.get();
			this.owner = new Customer().withId(ownerId);
		}
		
		if(in.newLines != null) {
			for (InvoiceLinePostDto dto : in.newLines) {
				InvoiceLine line = new InvoiceLine(dto);
				line.setInvoice(this);
				this.lines.add(line);
			}
		}
		return this;
	}

}

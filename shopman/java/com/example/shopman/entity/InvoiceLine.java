package com.example.shopman.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.shopman.entity.dto.InvoiceLinePatchDto;
import com.example.shopman.entity.dto.InvoiceLinePostDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity @Table(name = "invoice_line", schema = "shop")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@Data
public class InvoiceLine {
	
	public InvoiceLine() {
		this.id = null;
	}
	
	public InvoiceLine(Long invoiceId, InvoiceLinePostDto in) {
		this();
		this.invoice = new Invoice().withId(invoiceId);
		this.item = new Item().withId(in.itemId);
		this.quantity = in.quantity;
		this.total = in.total;
	}

	@Id
	@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "invoice_line_id_generator", sequenceName = "invoice_line_id_sequence")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_line_id_generator")
	private final Long id;

	public InvoiceLine withId(Long id) {
		return new InvoiceLine(this.id, this.invoice, this.item, this.quantity, this.total);
	}
	
	@ManyToOne
	@JsonIgnore
	private Invoice invoice;
	
	@ManyToOne
	private Item item;
	
	@Column(name = "quantity", nullable = false)
	private int quantity;
	
	@Column(name = "total", nullable = false)
	private long total;
	
	public InvoiceLine patch(InvoiceLinePatchDto in) {
		if(in.itemId.isPresent()) {
			this.item = new Item().withId(in.itemId.get());
		}
		in.quantity.ifPresent(this::setQuantity);
		in.total.ifPresent(this::setTotal);
		return this;
	}
}

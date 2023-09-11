package com.example.shopman.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

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
	
	public static final String MESSAGE_NULL_ITEM_ID = "invoice line item ID should not be null";
	public static final String MESSAGE_QUANTITY_NULL = "invoice line quantity should not be null";
	public static final String MESSAGE_QUANTITY_NON_POSITIVE = "invoice line quantity should be strictly positive (> 0)";
	public static final String MESSAGE_TOTAL_NULL = "invoice line total value should not be null";
	public static final String MESSAGE_TOTAL_NEGATIVE = "invoice line total value should be positive";
	
	public InvoiceLine() {
		this.id = null;
	}
	
	public InvoiceLine(InvoiceLinePostDto in) {
		this();
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
	@JoinColumn(name = "invoice_id")
	@JsonIgnore
	private Invoice invoice;
	
	@NotNull(message = MESSAGE_NULL_ITEM_ID)
	@ManyToOne
	private Item item;
	
	@NotNull(message = MESSAGE_QUANTITY_NULL)
	@Positive(message = MESSAGE_QUANTITY_NON_POSITIVE)
	@Column(name = "quantity", nullable = false)
	private int quantity;
	
	@NotNull(message = MESSAGE_TOTAL_NULL)
	@PositiveOrZero(message = MESSAGE_TOTAL_NEGATIVE)
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

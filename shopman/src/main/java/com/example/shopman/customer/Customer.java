package com.example.shopman.customer;

import java.time.Instant;
import java.time.LocalDate;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.shopman.entity.info.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "customers", schema = "shop")
@AllArgsConstructor
@Data
public class Customer {

	public static final int MAX_NAME_LENGTH = 50;

	public Customer() {
		this.id = null;
	}

	@Id
	@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "customer_id_generator", sequenceName = "customer_id_sequence")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_generator")
	private final Long id;

	public Customer withId(Long id) {
		return new Customer(id, this.firstname, this.lastname, Gender.MALE, this.birthDate, this.creationInstant,
				this.lastModificationInstant);
	}

	@Column(name = "firstname", nullable = false, length = Customer.MAX_NAME_LENGTH)
	private @Access(AccessType.PROPERTY) String firstname;

	@Column(name = "lastname", nullable = false, length = Customer.MAX_NAME_LENGTH)
	private @Access(AccessType.PROPERTY) String lastname;

	@Enumerated(EnumType.STRING)
	private @Access(AccessType.PROPERTY) Gender gender;

	@Column(name = "birth_date", nullable = true)
	private @Access(AccessType.PROPERTY) LocalDate birthDate;

	@CreatedDate
	private @Access(AccessType.PROPERTY) Instant creationInstant;

	@LastModifiedDate
	private @Access(AccessType.PROPERTY) Instant lastModificationInstant;
}

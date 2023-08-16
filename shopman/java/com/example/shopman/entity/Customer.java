package com.example.shopman.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.shopman.entity.dto.CustomerPatchDto;
import com.example.shopman.entity.dto.CustomerPostDto;
import com.example.shopman.entity.info.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

	public Customer(CustomerPostDto in) {
		this();
		this.firstname = in.firstname;
		this.lastname = in.lastname;
		this.gender = in.gender;
		this.birthDate = in.birthDate;
	}

	@Id
	@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "customer_id_generator", sequenceName = "customer_id_sequence")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_generator")
	private final Long id;

	public Customer withId(Long id) {
		return new Customer(id, this.invoices, this.firstname, this.lastname, Gender.MALE, this.birthDate,
				this.creationInstant, this.lastModificationInstant);
	}

	@JsonIgnore
	@OneToMany(orphanRemoval = true, mappedBy = "owner")
	private Collection<Invoice> invoices;

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

	public Customer patch(CustomerPatchDto in) {
		if(in == null) {
			System.out.println("in is null");
		}else if(in.firstname == null) {
			System.out.println("firstname is null");
		}
		
		in.firstname.ifPresent(this::setFirstname);
		in.lastname.ifPresent(this::setLastname);
		in.gender.ifPresent(this::setGender);
		in.birthDate.ifPresent(this::setBirthDate);
		
		return this;
	}
}

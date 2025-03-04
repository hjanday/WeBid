package com.webid.webid.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String username;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	private String postalCode;

	@Column(nullable = false)
	private String country;

	@Column(nullable = false)
	private String city;

	public User() {
	}

	public User(String username, String email, String password, String firstName, String lastName, String address,
			String postalCode, String country, String city) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.postalCode = postalCode;
		this.country = country;
		this.city = city;
	}

	public Long getId() {
		return this.id;
	}

	public String getEmail() {
		return this.email;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getAddress() {
		return this.address;
	}

	public String getPostalCode() {
		return this.postalCode;
	}

	public String getCountry() {
		return this.country;
	}

	public String getCity() {
		return this.city;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}

}

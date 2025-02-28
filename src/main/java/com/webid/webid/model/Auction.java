package com.webid.webid.model;

import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "Auction")
public class Auction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String ItemName;
	@Column(nullable = false)
	private String description;

	@Column(unique = true, nullable = false)
	private long OwnerID;
	@Column(nullable = false)
	private Double Price;

	@Column(nullable = true)
	private Double currentBid;

	@Column(nullable = true)
	private long currentBidderID;

	@Column(nullable = false)
	private double bidIncrement;

	@Column(nullable = false)
	private Time startTime;
	@Column(nullable = false)
	private String endTime;
	@Column(nullable = false)
	private String auctionType;

	public Auction() {
	}

	

	
	
	
}

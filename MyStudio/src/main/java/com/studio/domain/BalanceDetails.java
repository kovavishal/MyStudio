package com.studio.domain;
// Generated May 6, 2019 5:04:23 AM by Hibernate Tools 5.2.3.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * BalanceDetails generated by hbm2java
 */
@Entity
@Table(name = "balance_details", catalog = "studio")
public class BalanceDetails implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private double cashHand;
	private double cashBank;
	private Date createdDate;
	private Date updatedDate;

	public BalanceDetails() {
	}

	public BalanceDetails(double cashHand, double cashBank) {
		this.cashHand = cashHand;
		this.cashBank = cashBank;
	}

	public BalanceDetails(double cashHand, double cashBank, Date createdDate, Date updatedDate) {
		this.cashHand = cashHand;
		this.cashBank = cashBank;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "cash_hand", nullable = false, precision = 22, scale = 0)
	public double getCashHand() {
		return this.cashHand;
	}

	public void setCashHand(double cashHand) {
		this.cashHand = cashHand;
	}

	@Column(name = "cash_bank", nullable = false, precision = 22, scale = 0)
	public double getCashBank() {
		return this.cashBank;
	}

	public void setCashBank(double cashBank) {
		this.cashBank = cashBank;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "created_date", length = 10)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "updated_date", length = 10)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

}

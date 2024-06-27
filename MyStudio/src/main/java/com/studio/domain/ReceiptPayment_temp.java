package com.studio.domain;
// Generated Apr 11, 2019 1:01:14 PM by Hibernate Tools 5.2.3.Final

import javax.persistence.Transient;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ReceiptPayment generated by hbm2java
 */
@Entity
@Table(name = "receipt_payment", catalog = "studio")
public class ReceiptPayment_temp implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long receiptId;
	private String refTypeId;
	private String refNo;
	private Double netAmount;
	private Double paidAmount;
	private Date createdDate;
	private Date updatedDate;
	private Long orderId;

	public ReceiptPayment_temp() {
	}
/*
	public ReceiptPayment_temp(Receipt receipt, String refTypeId) {
		this.receipt = receipt;
		this.refTypeId = refTypeId;
	}
*/
	public ReceiptPayment_temp(Long receiptId, String refTypeId, String refNo, Double netAmount, Double paidAmount,
			Date createdDate, Date updatedDate,long orderId) {
		this.receiptId = receiptId;
		this.refTypeId = refTypeId;
		this.refNo = refNo;
		this.netAmount = netAmount;
		this.paidAmount = paidAmount;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.orderId=orderId;
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

	/*@ManyToOne(fetch = FetchType.LAZY)*/
	/*@JoinColumn(name = "receipt_id", nullable = false)*/
	@Column(name = "receipt_id", nullable = false)
	public Long getReceiptId() {
		return this.receiptId;
	}

	public void setReceiptId(Long receiptId) {
		this.receiptId = receiptId;
	}

	@Column(name = "ref_type_id", nullable = false, length = 50)
	public String getRefTypeId() {
		return this.refTypeId;
	}

	public void setRefTypeId(String refTypeId) {
		this.refTypeId = refTypeId;
	}

	@Column(name = "ref_no", length = 50)
	public String getRefNo() {
		return this.refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	@Column(name = "net_amount", precision = 22, scale = 0)
	public Double getNetAmount() {
		return this.netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	@Column(name = "paid_amount", precision = 22, scale = 0)
	public Double getPaidAmount() {
		return this.paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
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
	
	@Transient
	public long getOrderId() {
		return this.orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

}

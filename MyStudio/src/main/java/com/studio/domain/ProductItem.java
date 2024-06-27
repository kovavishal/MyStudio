package com.studio.domain;
// Generated Mar 21, 2019 11:56:35 PM by Hibernate Tools 5.2.3.Final

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * ProductItem generated by hbm2java
 */
@Entity
@Table(name = "product_item", catalog = "studio")
public class ProductItem implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long prodItemId;
	private long prodItemTypeId;
	private long productId;
	private Long orderId;
	private Integer quantity;
	private Double rate;
	private Double amount;
	private String remarks;
	private Date createdDate;
	private Date updatedDate;
	private String prodItemName;
	private String prodSize;
	private long prodno;
	public ProductItem() {
	}

	public ProductItem(long prodItemTypeId, long productId) {
		this.prodItemTypeId = prodItemTypeId;
		this.productId = productId;
	}

	public ProductItem(long prodItemTypeId, long productId, Long orderId, Integer quantity, Double rate, Double amount,
			String remarks, Date createdDate, Date updatedDate) {
		this.prodItemTypeId = prodItemTypeId;
		this.productId = productId;
		this.orderId = orderId;
		this.quantity = quantity;
		this.rate = rate;
		this.amount = amount;
		this.remarks = remarks;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "prod_item_id", unique = true, nullable = false)
	public Long getProdItemId() {
		return this.prodItemId;
	}

	public void setProdItemId(Long prodItemId) {
		this.prodItemId = prodItemId;
	}

	@Column(name = "prod_item_type_id", nullable = false)
	public long getProdItemTypeId() {
		return this.prodItemTypeId;
	}

	public void setProdItemTypeId(long prodItemTypeId) {
		this.prodItemTypeId = prodItemTypeId;
	}

	@Column(name = "product_id", nullable = false)
	public long getProductId() {
		return this.productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	@Column(name = "order_id")
	public Long getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	@Column(name = "quantity")
	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Column(name = "rate", precision = 22, scale = 0)
	public Double getRate() {
		return this.rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	@Column(name = "amount", precision = 22, scale = 0)
	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Column(name = "remarks", length = 100)
	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	/**
	 * @return the prodItemName
	 */
	@Transient
	public String getProdItemName() {
		return prodItemName;
	}

	/**
	 * @param prodItemName the prodItemName to set
	 */
	public void setProdItemName(String prodItemName) {
		this.prodItemName = prodItemName;
	}

	/**
	 * @return the prodSize
	 */
	@Transient
	public String getProdSize() {
		return prodSize;
	}

	/**
	 * @param prodSize the prodSize to set
	 */
	public void setProdSize(String prodSize) {
		this.prodSize = prodSize;
	}
	@Transient
	public long getProdNo() {
		return prodno;
	}

	/**
	 * @param prodSize the prodSize to set
	 */
	public void setProdNo(long prodNo) {
		this.prodno = prodNo;
	}
	
	
}

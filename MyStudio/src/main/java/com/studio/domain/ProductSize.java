package com.studio.domain;
// Generated Mar 23, 2019 10:04:50 PM by Hibernate Tools 5.2.3.Final

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * ProductSize generated by hbm2java
 */
@Entity
@Table(name = "product_size", catalog = "studio")
public class ProductSize implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private int productId;
	private String size;
	private String productName;

	public ProductSize() {
	}

	public ProductSize(int productId) {
		this.productId = productId;
	}

	public ProductSize(int productId, String size) {
		this.productId = productId;
		this.size = size;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "product_id", nullable = false)
	public int getProductId() {
		return this.productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	@Column(name = "size", length = 40)
	public String getSize() {
		return this.size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * @return the productName
	 */
	@Transient
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

}

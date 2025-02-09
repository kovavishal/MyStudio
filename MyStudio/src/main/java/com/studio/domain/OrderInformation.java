package com.studio.domain;
//Generated Mar 20, 2019 11:51:48 PM by Hibernate Tools 5.2.3.Final

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.studio.utils.DateUtils;

/**
* Order generated by hbm2java
*/

public class OrderInformation implements java.io.Serializable {

	/**
	 * 
	 */
	private String slno;
	public String getSlno() {
		return slno;
	}
	public void setSlno(String slno) {
		this.slno = slno;
	}
	private long orderId;
	private Date orderDate;
	private String orderType;
	private String agentName;
	private String custName;
	private Integer custType;
	private Double creditBalance;
	private Double balancePayable;
	private Date dueDate;
	private String wrapperName;
	private Date createdDate;
	private Date updatedDate;
	private String strOrderDate;
	private String strDueDate;
	private String custAddr1;
	private String custAddr2;
	private List<ReceiptPayment> receiptPayment= new ArrayList<ReceiptPayment>();
	private List<OrderedItems> orderedItems = new ArrayList<OrderedItems>();
	// for checklist printing 
	private String mobileNo;
	private Integer status;
	private String strOrderId;
	/*	private int days;*/
	private int noOfSheet;
	private int noOfCopy;
	private String billBy;
	private int totitem;
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public Integer getCustType() {
		return custType;
	}
	public void setCustType(Integer custType) {
		this.custType = custType;
	}
	public Double getCreditBalance() {
		return creditBalance;
	}
	public void setCreditBalance(Double creditBalance) {
		this.creditBalance = creditBalance;
	}
	public Double getBalancePayable() {
		return balancePayable;
	}
	public void setBalancePayable(Double balancePayable) {
		this.balancePayable = balancePayable;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public String getWrapperName() {
		return wrapperName;
	}
	public void setWrapperName(String wrapperName) {
		this.wrapperName = wrapperName;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getStrOrderDate() {
		return strOrderDate;
	}
	public void setStrOrderDate(String strOrderDate) {
		this.strOrderDate = strOrderDate;
	}
	public String getStrDueDate() {
		return strDueDate;
	}
	public void setStrDueDate(String strDueDate) {
		this.strDueDate = strDueDate;
	}
	public String getCustAddr1() {
		return custAddr1;
	}
	public void setCustAddr1(String custAddr1) {
		this.custAddr1 = custAddr1;
	}
	public String getCustAddr2() {
		return custAddr2;
	}
	public void setCustAddr2(String custAddr2) {
		this.custAddr2 = custAddr2;
	}
	public List<ReceiptPayment> getReceiptPayment() {
		return receiptPayment;
	}
	public void setReceiptPayment(List<ReceiptPayment> receiptPayment) {
		this.receiptPayment = receiptPayment;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getStrOrderId() {
		return strOrderId;
	}
	public void setStrOrderId(String strOrderId) {
		this.strOrderId = strOrderId;
	}
	public int getNoOfSheet() {
		return noOfSheet;
	}
	public void setNoOfSheet(int noOfSheet) {
		this.noOfSheet = noOfSheet;
	}
	public int getNoOfCopy() {
		return noOfCopy;
	}
	public void setNoOfCopy(int noOfCopy) {
		this.noOfCopy = noOfCopy;
	}
	public String getBillBy() {
		return billBy;
	}
	public void setBillBy(String billBy) {
		this.billBy = billBy;
	}
	public int getTotitem() {
		return totitem;
	}
	public void setTotitem(int totitem) {
		this.totitem = totitem;
	}
	public List<OrderedItems> getOrderedItems() {
		return orderedItems;
	}
	public void setOrderedItems(List<OrderedItems> orderedItems) {
		this.orderedItems = orderedItems;
	}
	
	
}

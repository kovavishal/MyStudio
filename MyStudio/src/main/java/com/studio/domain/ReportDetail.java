/**
 * 
 */
package com.studio.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;

/**
 * @author ezhilraja_k
 *
 */

public class ReportDetail implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String trxnDate;
	private Date trxnDate1;
	private String trxnType;
	private String referenceType;
	private String description;
	private Double debitAmount;
	private Double creditAmount;
	private Long orderId;
	private Long billNo;
	private String chqDate;
	private String chqNo;
	private String bankName;
	private String narration;
	private String invoiceDate;
	private Double invoiceAmount;
	private Long id;
	private Double balance;
	private Date fromDate;
	private Long custId;
	private Double payAmount;
	private Double openingBalance;

	private String supplierName;
	private String customerName;
	
	private String mobileNo;
	private String address1;
	private String address2;
	private String city;
	private String firstName;
	private String area;
	private int days;
	private int noOfBills;
	private double cashsale;
	private double creditsale;
	private double cashPurchase;
	private double creditPurchase;
	private double totalReceipt;
	private double totalPayment;
	private double totalpurchase;
//	private double advanceAmount;
	
	/**
	 * @return the trxnDate
	 */
	public String getTrxnDate() {
		return trxnDate;
	}
	/**
	 * @param trxnDate the trxnDate to set
	 */
	public void setTrxnDate(String trxnDate) {
		this.trxnDate = trxnDate;
	}
	
	public Date getTrxnDate1() {
		return trxnDate1;
	}
	/**
	 * @param trxnDate the trxnDate to set
	 */
	public void setTrxnDate1(Date trxnDate1) {
		this.trxnDate1 = trxnDate1;
	}
	
	/**
	 * @return the trxnType
	 */
	public String getTrxnType() {
		return trxnType;
	}
	/**
	 * @param trxnType the trxnType to set
	 */
	public void setTrxnType(String trxnType) {
		this.trxnType = trxnType;
	}
	/**
	 * @return the referenceType
	 */
	public String getReferenceType() {
		return referenceType;
	}
	/**
	 * @param referenceType the referenceType to set
	 */
	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the debiAmount
	 */  
	public Double getDebitAmount() {
		return debitAmount;
	}
	/**
	 * @param debiAmount the debiAmount to set
	 */
	public void setDebitAmount(Double debitAmount) {
		this.debitAmount = debitAmount;
	}
	/**
	 * @return the creditAmount
	 */
	public Double getCreditAmount() {
		return creditAmount;
	}
	/**
	 * @param creditAmount the creditAmount to set
	 */
	public void setCreditAmount(Double creditAmount) {
		this.creditAmount = creditAmount;
	}
	/**
	 * @return the orderId
	 */
	public Long getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	/**
	 * @return the billNo
	 */
	public Long getBillNo() {
		return billNo;
	}
	/**
	 * @param billNo the billNo to set
	 */
	public void setBillNo(Long billNo) {
		this.billNo = billNo;
	}
	/**
	 * @return the chqDate
	 */
	public String getChqDate() {
		return chqDate;
	}
	/**
	 * @param chqDate the chqDate to set
	 */
	public void setChqDate(String chqDate) {
		this.chqDate = chqDate;
	}
	/**
	 * @return the chqNo
	 */
	public String getChqNo() {
		return chqNo;
	}
	/**
	 * @param chqNo the chqNo to set
	 */
	public void setChqNo(String chqNo) {
		this.chqNo = chqNo;
	}
	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}
	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	/**
	 * @return the narration
	 */
	public String getNarration() {
		return narration;
	}
	/**
	 * @param narration the narration to set
	 */
	public void setNarration(String narration) {
		this.narration = narration;
	}
	/**
	 * @return the invoiceDate
	 */
	public String getInvoiceDate() {
		return invoiceDate;
	}
	/**
	 * @param invoiceDate the invoiceDate to set
	 */
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	/**
	 * @return the invoiceAmount
	 */
	public Double getInvoiceAmount() {
		return invoiceAmount;
	}
	/**
	 * @param invoiceAmount the invoiceAmount to set
	 */
	public void setInvoiceAmount(Double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Long getCustId() {
		return custId;
	}
	public void setCustId(Long custId) {
		this.custId = custId;
	}
	public Double getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}
	public Double getOpeningBalance() {
		return openingBalance;
	}
	public void setOpeningBalance(Double openingBalance) {
		this.openingBalance = openingBalance;
	}
	 
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	
	public int getNoOfBills() {
		return noOfBills;
	}
	public void setNoOfBills(int noOfBills) {
		this.noOfBills = noOfBills;
	}

	public double getCashsale() {
		return cashsale;
	}
	public void setCashsale(double cashsale) {
		this.cashsale = cashsale;
	}
	public double getCreditsale() {
		return creditsale;
	}
	public void setCreditsale(double creditSale) {
		this.creditsale = creditSale;
	}
	public double getCashPurchase() {
		return cashPurchase;
	}
	public void setCashPurchase(double cashPurchase) {
		this.cashPurchase = cashPurchase;
	}
	public double getCreditPurchase() {
		return creditPurchase;
	}
	public void setCreditPurchase(double creditPurchase) {
		this.creditPurchase = creditPurchase;
	}
	public double getTotalReceipt() {
		return totalReceipt;
	}
	public void setTotalReceipt(double totalReceipt) {
		this.totalReceipt = totalReceipt;
	}
	public double getTotalPayment() {
		return totalPayment;
	}
	public void setTotalPayment(double totalPayment) {
		this.totalPayment = totalPayment;
	}
	public double getTotalpurchase() {
		return totalpurchase;
	}
	public void setTotalpurchase(double totalpurchase) {
		this.totalpurchase = totalpurchase;
	}
	

	
}

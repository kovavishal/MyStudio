/**
 * 
 */
package com.studio.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.studio.utils.DateUtils;

/**
 * @author ezhilraja_k
 *
 */
public class Report implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<ReportDetail> details = new ArrayList<ReportDetail>();
	private Date fromDate;
	private Date toDate;
	private String strFromDate;
	private String strToDate;
	private Integer typeId;
	private String name;
	private long custId;
	private Double creditBalance;
	private Double openingBalance; 
	private Long orderId;
	private Long payAmount;
	private int days;
	private String reports;

	/**
	 * @return the details
	 */
	public List<ReportDetail> getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(List<ReportDetail> details) {
		this.details = details;
	}

	/**
	 * @return the fromDate
	 */
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
		this.strFromDate = DateUtils.toDate(fromDate,true);
	}

	/**
	 * @return the toDate
	 */
	public Date getToDate() {
		return toDate;
	}

	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
		this.strToDate = DateUtils.toDate(toDate,true);
	}

	/**
	 * @return the strFromDate
	 */
	public String getStrFromDate() {
		return strFromDate;
	}

	/**
	 * @param strFromDate the strFromDate to set
	 */
	public void setStrFromDate(String strFromDate) {
		this.strFromDate = strFromDate;
		this.fromDate = DateUtils.toDate(strFromDate,true);
	}

	/**
	 * @return the strToDate
	 */
	public String getStrToDate() {
		return strToDate;
	}

	/**
	 * @param strToDate the strToDate to set
	 */
	public void setStrToDate(String strToDate) {
		this.strToDate = strToDate;
		this.toDate = DateUtils.toDate(strToDate,true);
	}

	/**
	 * @return the typeId
	 */
	public Integer getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the custId
	 */
	public Long getCustId() {
		return custId;
	}

	/**
	 * @param custId the custId to set
	 */
	public void setCustId(long custId) {
		this.custId = custId;
	}

	public Double getCreditBalance() {
		return creditBalance;
	}

	public void setCreditBalance(Double creditBalance) {
		this.creditBalance = creditBalance;
	}

	public void setCustId(Order singleResult) {
		// TODO Auto-generated method stub
		
	}

	public Double getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(Double openingBalance) {
		this.openingBalance = openingBalance;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Long payAmount) {
		this.payAmount = payAmount;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public String getReports() {
		return reports;
	}

	public void setReports(String reports) {
		this.reports = reports;
	}

	
}

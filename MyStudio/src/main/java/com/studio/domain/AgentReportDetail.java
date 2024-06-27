/**
 * 
 */
package com.studio.domain;

/**
 * @author ezhilraja_k
 *
 */
public class AgentReportDetail implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String orderDate;
	private String orderId;
	private String custName;
	private Integer noOfSheets;
	private Integer noOfCopies;
	private Double netAmount;
	private Double creditBalance;
	private Double advance;
	private Double total;
	private Long custId;
	/**
	 * @return the oDate
	 */
	public String getOrderDate() {
		return orderDate;
	}
	/**
	 * @param oDate the oDate to set
	 */
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	/**
	 * @return the custName
	 */
	public String getCustName() {
		return custName;
	}
	/**
	 * @param custName the custName to set
	 */
	public void setCustName(String custName) {
		this.custName = custName;
	}
	/**
	 * @return the noOfSheets
	 */
	public Integer getNoOfSheets() {
		return noOfSheets;
	}
	/**
	 * @param noOfSheets the noOfSheets to set
	 */
	public void setNoOfSheets(Integer noOfSheets) {
		this.noOfSheets = noOfSheets;
	}
	/**
	 * @return the noOfCopies
	 */
	public Integer getNoOfCopies() {
		return noOfCopies;
	}
	/**
	 * @param noOfCopies the noOfCopies to set
	 */
	public void setNoOfCopies(Integer noOfCopies) {
		this.noOfCopies = noOfCopies;
	}
	public Double getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}
	public Double getCreditBalance() {
		return creditBalance;
	}
	public void setCreditBalance(Double creditBalance) {
		this.creditBalance = creditBalance;
	}
	public Double getAdvance() {
		return advance;
	}
	public void setAdvance(Double advance) {
		this.advance = advance;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Long getCustId() {
		return custId;
	}
	public void setCustId(Long custId) {
		this.custId = custId;
	}
	
	
	
}
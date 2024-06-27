/**
 * 
 */
package com.studio.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ezhilraja_k
 *
 */
public class DailyReportDetail {

	List<ReportDetail> reportDetails = new ArrayList<ReportDetail>();
	Double orderAmount;
	Double orderAdvanceAmount;
	Double invoiceAmount;
	Double invoicePaidAmount;
	Double otherPayment;
	Double receiptAmount;
	String strOrderAmount;
	String strOrderAdvanceAmount;
	String strInvoiceAmount;
	String strInvoicePaidAmount;
	String strOtherPayment;
	String strReceiptAmount;
	Integer noOfOrder;
	/**
	 * @return the reportDetails
	 */
	public List<ReportDetail> getReportDetails() {
		return reportDetails;
	}
	/**
	 * @param reportDetails the reportDetails to set
	 */
	public void setReportDetails(List<ReportDetail> reportDetails) {
		this.reportDetails = reportDetails;
	}
	/**
	 * @return the orderAmount
	 */
	public Double getOrderAmount() {
		return orderAmount;
	}
	/**
	 * @param orderAmount the orderAmount to set
	 */
	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}
	/**
	 * @return the orderAdvanceAmount
	 */
	public Double getOrderAdvanceAmount() {
		return orderAdvanceAmount;
	}
	/**
	 * @param orderAdvanceAmount the orderAdvanceAmount to set
	 */
	public void setOrderAdvanceAmount(Double orderAdvanceAmount) {
		this.orderAdvanceAmount = orderAdvanceAmount;
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
	 * @return the invoicePaidAmount
	 */
	public Double getInvoicePaidAmount() {
		return invoicePaidAmount;
	}
	/**
	 * @param invoicePaidAmount the invoicePaidAmount to set
	 */
	public void setInvoicePaidAmount(Double invoicePaidAmount) {
		this.invoicePaidAmount = invoicePaidAmount;
	}
	/**
	 * @return the otherPayment
	 */
	public Double getOtherPayment() {
		return otherPayment;
	}
	/**
	 * @param otherPayment the otherPayment to set
	 */
	public void setOtherPayment(Double otherPayment) {
		this.otherPayment = otherPayment;
	}
	/**
	 * @return the receiptAmount
	 */
	public Double getReceiptAmount() {
		return receiptAmount;
	}
	/**
	 * @param receiptAmount the receiptAmount to set
	 */
	public void setReceiptAmount(Double receiptAmount) {
		this.receiptAmount = receiptAmount;
	}
	/**
	 * @return the strOrderAmount
	 */
	public String getStrOrderAmount() {
		return strOrderAmount;
	}
	/**
	 * @param strOrderAmount the strOrderAmount to set
	 */
	public void setStrOrderAmount(String strOrderAmount) {
		this.strOrderAmount = strOrderAmount;
	}
	/**
	 * @return the strOrderAdvanceAmount
	 */
	public String getStrOrderAdvanceAmount() {
		return strOrderAdvanceAmount;
	}
	/**
	 * @param strOrderAdvanceAmount the strOrderAdvanceAmount to set
	 */
	public void setStrOrderAdvanceAmount(String strOrderAdvanceAmount) {
		this.strOrderAdvanceAmount = strOrderAdvanceAmount;
	}
	/**
	 * @return the strInvoiceAmount
	 */
	public String getStrInvoiceAmount() {
		return strInvoiceAmount;
	}
	/**
	 * @param strInvoiceAmount the strInvoiceAmount to set
	 */
	public void setStrInvoiceAmount(String strInvoiceAmount) {
		this.strInvoiceAmount = strInvoiceAmount;
	}
	/**
	 * @return the strInvoicePaidAmount
	 */
	public String getStrInvoicePaidAmount() {
		return strInvoicePaidAmount;
	}
	/**
	 * @param strInvoicePaidAmount the strInvoicePaidAmount to set
	 */
	public void setStrInvoicePaidAmount(String strInvoicePaidAmount) {
		this.strInvoicePaidAmount = strInvoicePaidAmount;
	}
	/**
	 * @return the strOtherPayment
	 */
	public String getStrOtherPayment() {
		return strOtherPayment;
	}
	/**
	 * @param strOtherPayment the strOtherPayment to set
	 */
	public void setStrOtherPayment(String strOtherPayment) {
		this.strOtherPayment = strOtherPayment;
	}
	/**
	 * @return the strReceiptAmount
	 */
	public String getStrReceiptAmount() {
		return strReceiptAmount;
	}
	/**
	 * @param strReceiptAmount the strReceiptAmount to set
	 */
	public void setStrReceiptAmount(String strReceiptAmount) {
		this.strReceiptAmount = strReceiptAmount;
	}
	/**
	 * @return the noOfOrder
	 */
	public Integer getNoOfOrder() {
		return noOfOrder;
	}
	/**
	 * @param noOfOrder the noOfOrder to set
	 */
	public void setNoOfOrder(Integer noOfOrder) {
		this.noOfOrder = noOfOrder;
	}
	
}

package com.studio.domain;

import java.util.Date;

public class Dashboard implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int totalsale;
	private int cashsale;
	private int creditsale;
	private int totalpurchase;
	private int cashpurchase;
	private int creditpurchase;
	private int receipts;
	private int payments;
	private long ordertaken;
	private long orderdelivered;
	private int ordercancelled;
	private int orderedsheets;
	
	
	
	
	public int getTotalsale() {
		return totalsale;
	}
	public void setTotalsale(int totalsale) {
		this.totalsale = totalsale;
	}
	
	public int getCashsale() {
		return cashsale;
	}
	public void setCashsale(int cashsale) {
		this.cashsale = cashsale;
	}
	public int getCreditsale() {
		return creditsale;
	}
	public void setCreditsale(int creditsale) {
		this.creditsale = creditsale;
	}
	
	
	public int getTotalpurchase() {
		return totalpurchase;
	}
	public void setTotalpurchase(int totalpurchase) {
		this.totalpurchase = totalpurchase;
	}
	public int getCashpurchase() {
		return cashpurchase;
	}
	public void setCashpurchase(int cashpurchase) {
		this.cashpurchase = cashpurchase;
	}
	public int getCreditpurchase() {
		return creditpurchase;
	}
	public void setCreditpurchase(int creditpurchase) {
		this.creditpurchase = creditpurchase;
	}
	
	
	public int getReceipts() {
		return receipts;
	}
	public void setReceipts(int receipts) {
		this.receipts = receipts;
	}
	public int getPayments() {
		return payments;
	}
	public void setPayments(int payments) {
		this.payments = payments;
	}
	
	public long getOrdertaken() {
		return ordertaken;
	}
	public void setOrdertaken(long ordertaken) {
		this.ordertaken = ordertaken;
	}
	public long getOrderdelivered() {
		return orderdelivered;
	}
	public void setOrderdelivered(long orderdelivered) {
		this.orderdelivered = orderdelivered;
	}
		public int getOrdercancelled() {
		return ordercancelled;
	}
	public void setOrdercancelled(int ordercancelled) {
		this.ordercancelled = ordercancelled;
	}
	public int getOrderedsheets() {
		return orderedsheets;
	}
	public void setOrderedsheets(int orderedsheets) {
		this.orderedsheets = orderedsheets;
	}

	

	
}

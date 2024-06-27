package com.studio.domain;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;



public class WorkorderDetails implements java.io.Serializable {
	
	private long prodno;
	private String prodName;
	private int noofcopies;
	private String size;
	private int  noofsheets;
	private String itemname;
	private int qty;
	private double rate;
	private double amount;
	private double totalAmount;
	
	public long getProdNo() {
		return prodno;
	}
	public void setProdNo(long prodno) {
		this.prodno = prodno;}
	
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public int getNoofcopies() {
		return noofcopies;
	}
	public void setNoofcopies(int noofcopies) {
		this.noofcopies = noofcopies;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public int getNoofsheets() {
		return noofsheets;
	}
	public void setNoofsheets(int noofsheets) {
		this.noofsheets = noofsheets;
	}
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalamount) {
		this.totalAmount = totalamount;
	}

}

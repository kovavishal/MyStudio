/**
 * 
 */
package com.studio.constants;

/**
 * @author ezhilraja_k
 *
 */ 
public enum Customer {

	PAGE_ADD_CUSTOMER("addcustomer"),
	PAGE_CUSTOMER("customer"),
	PAGE_SUPPLIER("supplier"),
	DEPORTMENTS("deportments"),
	RATE_TYPES("rateTypes"),
	CUSTOMER_TYPES("customerTypes"),
	ADD_EMPLOYEE("addemployee"),
	AREAS("areas"),
	STATES("states"),
	SUPPLIER("supplier"),
	ADD_NEW("addnew"),
	PURCHASE_DEPT("purchaseDept");
	
	private String value;

	private Customer(String value) {
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
	
}

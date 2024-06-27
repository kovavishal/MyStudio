/**
 * 
 */
package com.studio.constants;

/**
 * @author ezhilraja_k
 *
 */
public enum Payment {

	EXPENSE_TYPES("expenseTypes"),
	VOUCHER_PAGE("voucher"),
	PAYMENT_PAGE("payment"),
	SUPPLIER_PAGE("supplier"),
	SUPPLIERS("suppliers"),
	PAYMENT_TYPES("paymentTypes"),
	RECEIPT_TYPES("receiptTypes"),
	PURCHASE("purchase"),
	PURCHASE_ITEMS("purchaseItems"),
	PURCHASE_PAYMENTS("purchasePayments"),
	VOUCHER_TYPES("voucherTypes"),
	RECEIPT("receipt"),
	RECEIPT_AGENT("receiptagent"),
	ORDER_IDS("orderIds"),
	VOUCHER("voucher"),
	PAGE_BILL_COPY("billcopy"),
	PURCHASE_ORDER_VIEW("purchaseorderview");
	
	String voucher;
	
	/**
	 * 
	 */
	private Payment(String v) {
		this.voucher=v;
	}
	
	public String value(){
		return voucher;
	}
}

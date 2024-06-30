/**
 * 
 */
package com.studio.constants;

/**
 * @author ezhilraja_k
 *
 */
public enum ReportConstants {

	PAGE_LEDDGER("ledgerreport"),
	PAGE_AGENT_REPORT("agentreport"),
	PAGE_AGENT_REPORT_REMOTE("agentreport_remoteaccess"),
	PAGE_AGENT_REPORT_CORRECTION("agent_report_correction"),
	PAGE_DAILY_REPORT("dailyreport"),
	PAGE_SELECTEDDAY_REPORT("selectedday"),
	PAGE_MONTHLY_REPORT("monthlyreport"),
	PAGE_CUSTOMER_REPORT("customerreport"),
	PAGE_CANCELLED_ORDER_REPORT("cancelorderreport"),
	PAGE_SUPPLIER_REPORT("supplierreport"),
	PAGE_CUST_OUTSTANDING_REPORT("customeroutstanding"),
	PAGE_UNDELIVERED_ALBUM_REPORT("undeliveredalbum"),
	PAGE_CUST_OUTSTANDING_SUMMARY_REPORT("customeroutstandingdetails"),
	PAGE_DATE_WISE_CUST_OUTSTANDING_SUMMARY_REPORT("datewiseCustomeroutstandingdetails"),
	
	PAGE_CUST_OFFER_SALE_REPORT("customeroffersalereport"),
	PAGE_SUPP_OUTSTANDING_REPORT("supplieroutstanding"),
	PAGE_CUST_ADDRESS_REPORT("customeraddress"),
	PAGE_CUSTOMER_VS_BUSINESS("customervsbusiness"),
	PAGE_ORDER_VS_DESPATCH("ordervsdespatch"),
	PAGE_MBR_SUMMARY("mbrsummary"),
	PAGE_MB_STATEMENT("mbstatement"),
	PAGE_SRP_REPORT("srpreport"),
	REPORT("report"),
	BACKUP("backup"),
	TEST_PAGE("testpage"),
	NEWTABLE("NewTable");
	
	/**
	 * 
	 */
	String value;
	ReportConstants(String s) {
		this.value=s;
	}
	
	public String value(){
		return this.value;
	}
	
}

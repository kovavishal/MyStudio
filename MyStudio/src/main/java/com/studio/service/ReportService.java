/**
 * 
 */
package com.studio.service;

import java.util.Date;
import java.util.List;

import com.studio.domain.AgentReportDetail;
import com.studio.domain.BalanceDetails;
import com.studio.domain.DailyReportDetail;
import com.studio.domain.OrderTrxnDetail;
import com.studio.domain.Report;
import com.studio.domain.ReportDetail;
import com.studio.domain.SmsData;

/**
 * @author ezhilraja_k
 *
 */
public interface ReportService {

	List<ReportDetail> order(Report report);
	List<ReportDetail> mbrorder(Report report);
	DailyReportDetail order(Report report,Boolean isDailyReport);
	List<ReportDetail> supplier(Report report);
	List<ReportDetail> mbrsupplier(Report report);
	List<ReportDetail>mbrdetailpurchase(Report report);
	List<ReportDetail>mbrdetailexpense(Report report);
	List<ReportDetail> supplierReport(Report report);
	DailyReportDetail supplier(Report report,Boolean isDailyReport);
	List<ReportDetail> voucher(Report report);
	List<ReportDetail> mbrpaymentvoucher(Report report);
	List<ReportDetail> supplierVoucher(Report report);
	DailyReportDetail voucher(Report report,Boolean isDailyReport);
	List<ReportDetail> receipt(Report report);
	List<ReportDetail> mbrreceipt(Report report);
	DailyReportDetail receipt(Report report,Boolean isDailyReport);
	List<ReportDetail> balance(Report report,String type);
	List<AgentReportDetail> agentOrders(Report report);
	<T> Boolean saveOrUpdate(T t);
	<T> Boolean save(T t);
	BalanceDetails getBalance(Date date);
	SmsData smsData(Report report);
	List<ReportDetail> orderByIds(Report report, List<Long> orderIds);
	Boolean saveClosingBalance(List<ReportDetail> reportDetails);
	ReportDetail getOpeningBalance();
	ReportDetail getOpeningBalancewithDate(String fromDate);
	List<ReportDetail>  mbrsummary(String fromDate, String toDate );
	// for customer outstanding report
	List<ReportDetail> srpReport(String fromDate, String toDate, String type);
   // List<OrderTrxnDetail> getOrderTrxn(String orderId);
	public void Backupdbtosql();
	List<ReportDetail> orderByIdsforCustomer(Report report, List<Long> orderIds);
	List<ReportDetail> customerOSRByOrderIds(Report report, List<Long> orderIds);
	List<ReportDetail> getCOSReceipt(Report report);
	List<ReportDetail> receiptforCustomer(Report report);
	
	ReportDetail dashboardOrderStatus(String fromDate, String toDate );
	List<ReportDetail> customerOSRBFDOrderIds(Report report, List<Long> orderIds);
	List<ReportDetail> getCOSBFDReceipt(Report report);
	List<ReportDetail> salesTargetOrderIds(Report report, List<Long> orderIds);
	List<ReportDetail> cancelledOrderByIdsforCustomer(Report report, List<Long> cancelOrderIds);
	List<OrderTrxnDetail> cancelledOrderDetails(Report report, List<Long> orders);
	List<AgentReportDetail> agentOrdersCorrection(Report report);
	
}

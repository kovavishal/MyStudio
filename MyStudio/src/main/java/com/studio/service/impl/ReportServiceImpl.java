/**
 * 
 */
package com.studio.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.studio.constants.WorkOrderConstants;
import com.studio.dao.CustomerDAO;
import com.studio.dao.MetaDAO;
import com.studio.dao.OrderDAO;
import com.studio.dao.PaymentDAO;
import com.studio.dao.ReportDAO;
import com.studio.domain.AgentReportDetail;
import com.studio.domain.BalanceDetails;
import com.studio.domain.Customer;
import com.studio.domain.DailyReportDetail;
import com.studio.domain.ExpenseType;
import com.studio.domain.JobAllocation;
import com.studio.domain.Order;
import com.studio.domain.OrderTrxnDetail;
import com.studio.domain.PaymentType;
import com.studio.domain.Product;
import com.studio.domain.Purchase;
import com.studio.domain.Receipt;
import com.studio.domain.ReceiptPayment;
import com.studio.domain.ReceiptPayment_temp;
import com.studio.domain.ReceiptType;
import com.studio.domain.Report;
import com.studio.domain.ReportDetail;
import com.studio.domain.SmsData;
import com.studio.domain.Supplier;
import com.studio.domain.Voucher;
import com.studio.domain.VoucherPayment;
import com.studio.domain.VoucherType;
import com.studio.service.EmailService;
import com.studio.service.ReportService;
import com.studio.utils.DateUtils;
import com.studio.utils.StringUtils;
import com.studio.utils.Utils;

/**
 * @author ezhilraja_k
 *
 */

@Service
@PropertySource("classpath:studio.properties")
public class ReportServiceImpl implements ReportService {
	
	@ManyToOne
	@Autowired
	private ReportDAO reportDAO;
	@ManyToOne
	@Autowired
	private MetaDAO metaDAO;
	@Autowired
	private Environment env;
	@ManyToOne
	@Autowired
	private OrderDAO orderDAO;
	@ManyToOne
	@Autowired
	private CustomerDAO customerDAO;
	@ManyToOne
	@Autowired
	private PaymentDAO paymentDAO;
	
	
	
	Logger logger = Logger.getLogger(ReportServiceImpl.class);
	/* (non-Javadoc)
	 * @see com.studio.service.ReportService#order(com.studio.domain.Report)
	 */ 
	

	public DailyReportDetail order(Report report,Boolean isDailyReport) {
		List<ReportDetail> reportDetails = null;
		DailyReportDetail dailyReportDetail = null;
		try{
			dailyReportDetail= new DailyReportDetail();
			List<OrderTrxnDetail> orderTrxnDetails = reportDAO.getDailyOrders(report);
			if(orderTrxnDetails!=null && !orderTrxnDetails.isEmpty()){
				dailyReportDetail.setNoOfOrder(orderTrxnDetails.size());
				reportDetails = new ArrayList<ReportDetail>();
				List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
				Map<Long,String> payTypes = Utils.getTypes(paymentTypes);
				List<Long> orderIds = Utils.getTrxnOrderIds(orderTrxnDetails);
				List<Order> orders = orderDAO.getOrders(orderIds);
				Map<Long,Order> orderIdMap = Utils.getOrderIdOrders(orders);
				Double orderAmount = 0.00;
				Double orderAdvanceAmount = 0.00;
				for(OrderTrxnDetail trxnDetail : orderTrxnDetails){
					//System.out.println("COUNTER---1");
					if(trxnDetail.getAdvance()!=null && trxnDetail.getAdvance()>0){
						ReportDetail detail = new ReportDetail();
						//System.out.println("WITH ADVANCE");
						detail.setTrxnDate(DateUtils.toDate(trxnDetail.getCreatedDate(),true));
						detail.setTrxnType(payTypes.get(Long.valueOf(trxnDetail.getPymtType())));
						detail.setReferenceType(env.getProperty("report.type.order"));
						detail.setDescription(orderIdMap.get(trxnDetail.getOrderId()).getCustName().toUpperCase());
						detail.setCreditAmount(trxnDetail.getAdvance());
						//detail.setDebitAmount(trxnDetail.getBalance());
						if(payTypes.get(Long.valueOf(trxnDetail.getPymtType())).equals("Cheque")){
							detail.setChqDate(DateUtils.toDate(trxnDetail.getChequeDate(),true));
							detail.setChqNo(trxnDetail.getChequeNo());
							detail.setBankName(trxnDetail.getBankName());
						}
						reportDetails.add(detail);
					}
					/* added for fully credited order details STARTS */
					/*else{
						ReportDetail detail = new ReportDetail();
						System.out.println("WITHOUT ADVANCE");
						detail.setTrxnDate(DateUtils.toDate(trxnDetail.getCreatedDate(),true));
						//detail.setTrxnType(payTypes.get(Long.valueOf(trxnDetail.getPymtType())));
						detail.setReferenceType(env.getProperty("report.type.order"));
						detail.setDescription(orderIdMap.get(trxnDetail.getOrderId()).getCustName().toUpperCase());
						detail.setCreditAmount(trxnDetail.getAdvance());
						//detail.setDebitAmount(trxnDetail.getBalance());
						reportDetails.add(detail);
						
					}*/
					/* added for fully credited order details ENDS */
					orderAmount += trxnDetail.getNetAmount();
					if(trxnDetail.getAdvance()!=null){
						orderAdvanceAmount += trxnDetail.getAdvance();
						//System.out.println("COUNTER----3");
					}
					dailyReportDetail.setReportDetails(reportDetails);
					dailyReportDetail.setOrderAmount(orderAmount);
					dailyReportDetail.setOrderAdvanceAmount(orderAdvanceAmount);
					dailyReportDetail.setStrOrderAmount(StringUtils.decimalFormat(orderAmount));
					dailyReportDetail.setStrOrderAdvanceAmount(StringUtils.decimalFormat(orderAdvanceAmount));
					
				}
			}
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("order :: " + _exception);
		}
		return dailyReportDetail;
	}
	
	public List<ReportDetail> mbrorder(Report report) {
		List<ReportDetail> reportDetails = null;
		try{
			//System.out.println("StrfromDate :"+report.getStrFromDate());
			//System.out.println("StrToDate :"+report.getStrToDate());
			List<Order> orders = reportDAO.getMBROrders(report.getStrFromDate(),report.getStrToDate());
			List<Long> orderIds = Utils.getOrdersIds(orders);
			List<OrderTrxnDetail> orderTrxnDetails= reportDAO.getOrders(report, orderIds);
						
			if(orderTrxnDetails!=null && !orderTrxnDetails.isEmpty()){
				reportDetails = new ArrayList<ReportDetail>();
				List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
				Map<Long,String> payTypes = Utils.getTypes(paymentTypes);
				List<Long> trxnorderIds = Utils.getTrxnOrderIds(orderTrxnDetails);
			
				//	System.out.println("Order Ids:"+orderIds);
				//	System.out.println("OrderTrxn Ids:"+trxnorderIds);
				
				//List<Order> orders = orderDAO.getOrders(orderIds);
				
				Map<Long,Order> orderIdMap = Utils.getOrderIdOrders(orders);
			//	System.out.println("Order Mapping:"+orderIdMap);
				for(OrderTrxnDetail trxnDetail : orderTrxnDetails){
										
						ReportDetail detail = new ReportDetail();
						detail.setTrxnDate(DateUtils.toDate(trxnDetail.getCreatedDate(),true));
						detail.setTrxnType(payTypes.get(Long.valueOf(trxnDetail.getPymtType())));
						//detail.setReferenceType(env.getProperty("report.type.order"));
						detail.setReferenceType("By Order:"+trxnDetail.getOrderId());
						detail.setDescription(orderIdMap.get(trxnDetail.getOrderId()).getCustName());
						detail.setCreditAmount(trxnDetail.getNetAmount());
					if(payTypes.get(Long.valueOf(trxnDetail.getPymtType())).equals("Cheque")){
						detail.setChqDate(DateUtils.toDate(trxnDetail.getChequeDate(),true));
						detail.setChqNo(trxnDetail.getChequeNo());
						detail.setBankName(trxnDetail.getBankName());
						}
					reportDetails.add(detail);
				}

			/*	for(OrderTrxnDetail trxnDetail : orderTrxnDetails){
					
					if(trxnDetail.getAdvance()!=null && trxnDetail.getAdvance()>0){
						ReportDetail detail = new ReportDetail();
						detail.setTrxnDate(DateUtils.toDate(trxnDetail.getCreatedDate(),true));
						detail.setTrxnType(payTypes.get(Long.valueOf(trxnDetail.getPymtType())));
						detail.setReferenceType(env.getProperty("report.type.order"));
						detail.setDescription(orderIdMap.get(trxnDetail.getOrderId()).getCustName());
						detail.setCreditAmount(trxnDetail.getAdvance());
						if(payTypes.get(Long.valueOf(trxnDetail.getPymtType())).equals("Cheque")){
							detail.setChqDate(DateUtils.toDate(trxnDetail.getChequeDate(),true));
							detail.setChqNo(trxnDetail.getChequeNo());
							detail.setBankName(trxnDetail.getBankName());
						}
						reportDetails.add(detail);
					}
					
					if(trxnDetail.getBalance()!=null && trxnDetail.getBalance() > 0){
						ReportDetail detail = new ReportDetail();
						detail.setTrxnDate(DateUtils.toDate(trxnDetail.getCreatedDate(),true));
						detail.setTrxnType(payTypes.get(Long.valueOf(trxnDetail.getPymtType())));
						detail.setReferenceType(env.getProperty("report.type.order"));
						detail.setDescription(orderIdMap.get(trxnDetail.getOrderId()).getCustName());
						detail.setDebitAmount(trxnDetail.getBalance());
						if(payTypes.get(Long.valueOf(trxnDetail.getPymtType())).equals("Cheque")){
							detail.setChqDate(DateUtils.toDate(trxnDetail.getCreatedDate(),true));
							detail.setChqNo(trxnDetail.getChequeNo());
							detail.setBankName(trxnDetail.getBankName());
						}
						reportDetails.add(detail);
					}
					
				}*/
				
			}
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("order :: " + _exception);
		}
		return reportDetails;
	}
	
	public List<ReportDetail> order(Report report) {
		List<ReportDetail> reportDetails = null;
		try{
			List<Order> orderslist = reportDAO.getOrders(report.getStrFromDate(), report.getStrToDate());
			List<Long> ordersId=Utils.getOrdersIds(orderslist);
			
			List<OrderTrxnDetail> orderTrxnDetails = reportDAO.getOrders(report,ordersId);
			
			if(orderTrxnDetails!=null && !orderTrxnDetails.isEmpty()){
				reportDetails = new ArrayList<ReportDetail>();
				List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
				Map<Long,String> payTypes = Utils.getTypes(paymentTypes);
				//List<Long> orderIds = Utils.getTrxnOrderIds(orderTrxnDetails);
				//List<Order> orders = orderDAO.getOrders(orderIds);
				Map<Long,Order> orderIdMap = Utils.getOrderIdOrders(orderslist);
				
				for(OrderTrxnDetail trxnDetail : orderTrxnDetails){
					ReportDetail detail = new ReportDetail();
					detail.setTrxnDate(DateUtils.toDate(trxnDetail.getCreatedDate(),true));
					detail.setTrxnType(payTypes.get(Long.valueOf(trxnDetail.getPymtType())));
					// changed for M B R detail report
					detail.setReferenceType("By Order");
					if( orderIdMap.get(trxnDetail.getOrderId()).getCustName()=="Ameture"){
						detail.setDescription("Ameture Order Id:"+ trxnDetail.getOrderId());
					}else{
					detail.setDescription(orderIdMap.get(trxnDetail.getOrderId()).getCustName());
					}
					//changed for M B R detail report
					detail.setCreditAmount(trxnDetail.getNetAmount());
					if(payTypes.get(Long.valueOf(trxnDetail.getPymtType())).equals("Cheque")){
						detail.setChqDate(DateUtils.toDate(trxnDetail.getChequeDate(),true));
						detail.setChqNo(trxnDetail.getChequeNo());
						detail.setBankName(trxnDetail.getBankName());
					}
					reportDetails.add(detail);
				}

				for(OrderTrxnDetail trxnDetail : orderTrxnDetails){
					
					if(trxnDetail.getAdvance()!=null && trxnDetail.getAdvance()>0){
						ReportDetail detail = new ReportDetail();
						detail.setTrxnDate(DateUtils.toDate(trxnDetail.getCreatedDate(),true));
						detail.setTrxnType(payTypes.get(Long.valueOf(trxnDetail.getPymtType())));
						detail.setReferenceType(env.getProperty("report.type.order"));
						detail.setDescription(orderIdMap.get(trxnDetail.getOrderId()).getCustName());
						detail.setCreditAmount(trxnDetail.getAdvance());
						if(payTypes.get(Long.valueOf(trxnDetail.getPymtType())).equals("Cheque")){
							detail.setChqDate(DateUtils.toDate(trxnDetail.getChequeDate(),true));
							detail.setChqNo(trxnDetail.getChequeNo());
							detail.setBankName(trxnDetail.getBankName());
						}
						reportDetails.add(detail);
					}
					
					if(trxnDetail.getBalance()!=null && trxnDetail.getBalance() > 0){
						ReportDetail detail = new ReportDetail();
						detail.setTrxnDate(DateUtils.toDate(trxnDetail.getCreatedDate(),true));
						detail.setTrxnType(payTypes.get(Long.valueOf(trxnDetail.getPymtType())));
						detail.setReferenceType(env.getProperty("report.type.order"));
						detail.setDescription(orderIdMap.get(trxnDetail.getOrderId()).getCustName());
						detail.setDebitAmount(trxnDetail.getBalance());
						if(payTypes.get(Long.valueOf(trxnDetail.getPymtType())).equals("Cheque")){
							detail.setChqDate(DateUtils.toDate(trxnDetail.getCreatedDate(),true));
							detail.setChqNo(trxnDetail.getChequeNo());
							detail.setBankName(trxnDetail.getBankName());
						}
						reportDetails.add(detail);
					}
					
				}
			}
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("order :: " + _exception);
		}
		return reportDetails;
	}
	
	public List<OrderTrxnDetail> cancelledOrderDetails(Report report, List<Long> orderIds) {
		List<OrderTrxnDetail> orderTrxnDetails= new ArrayList<OrderTrxnDetail>();
		try{
		if(orderIds.size()>0 ){
			for (int i=0;i<orderIds.size();i++){
			    orderTrxnDetails = reportDAO.getOrders(report,orderIds);
				//System.out.println("orderTrxnDetails size :"+orderTrxnDetails.size());
				if (orderTrxnDetails !=null)
					return orderTrxnDetails;
			}
		}
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("order :: " + _exception);
		}
		return orderTrxnDetails;
	}
	
	
	public List<ReportDetail> orderByIds(Report report, List<Long> orderIds) {
		List<ReportDetail> reportDetails = null;
		try{
			if(orderIds !=null && !orderIds.isEmpty()){
			List<OrderTrxnDetail> orderTrxnDetails = reportDAO.getOrders(report,orderIds);
			if(orderTrxnDetails!=null && !orderTrxnDetails.isEmpty()){
				reportDetails = new ArrayList<ReportDetail>();
				List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
				Map<Long,String> payTypes = Utils.getTypes(paymentTypes);
				List<Order> orders = orderDAO.getOrders(orderIds);
				Map<Long,Order> orderIdMap = Utils.getOrderIdOrders(orders);
				for(OrderTrxnDetail trxnDetail : orderTrxnDetails){
					if(trxnDetail.getAdvance()!=null && trxnDetail.getAdvance()>0){
						ReportDetail detail = new ReportDetail();
						detail.setTrxnDate(DateUtils.toDate(trxnDetail.getCreatedDate(),true));
						detail.setTrxnType(payTypes.get(Long.valueOf(trxnDetail.getPymtType())));
						detail.setReferenceType(env.getProperty("report.type.order"));
						detail.setDescription(orderIdMap.get(trxnDetail.getOrderId()).getCustName());
						detail.setCreditAmount(trxnDetail.getAdvance());
						detail.setBillNo(trxnDetail.getOrderId());
						detail.setOrderId(trxnDetail.getOrderId());
									
						reportDetails.add(detail);
					}
					if(trxnDetail.getBalance()!=null && trxnDetail.getBalance() > 0){
						ReportDetail detail = new ReportDetail();
						detail.setTrxnDate(DateUtils.toDate(trxnDetail.getCreatedDate(),true));
						detail.setTrxnType(payTypes.get(Long.valueOf(trxnDetail.getPymtType())));
						detail.setReferenceType(env.getProperty("report.type.order"));
						detail.setDescription(orderIdMap.get(trxnDetail.getOrderId()).getCustName());
						detail.setDebitAmount(trxnDetail.getBalance());
						if(payTypes.get(Long.valueOf(trxnDetail.getPymtType())).equals("Cheque")){
							detail.setChqDate(DateUtils.toDate(trxnDetail.getCreatedDate(),true));
							detail.setChqNo(trxnDetail.getChequeNo());
							detail.setBankName(trxnDetail.getBankName());
						}
						detail.setBillNo(trxnDetail.getOrderId());
						detail.setOrderId(trxnDetail.getOrderId());
						reportDetails.add(detail);
					}
					
				}
			}
			}
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("order :: " + _exception);
		}
		return reportDetails;
	}

	
	// for customer report alone trnx date in DATE format
	public List<ReportDetail> orderByIdsforCustomer(Report report, List<Long> orderIds) {
		List<ReportDetail> reportDetails = null;
		try{
			if(orderIds !=null && !orderIds.isEmpty()){
			List<OrderTrxnDetail> orderTrxnDetails = reportDAO.getOrders(report,orderIds);
			//List<OrderTrxnDetail> cancelledOrderTrxnDetails = reportDAO.getOrders(report);
			if(orderTrxnDetails!=null && !orderTrxnDetails.isEmpty()){
				reportDetails = new ArrayList<ReportDetail>();
				List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
				Map<Long,String> payTypes = Utils.getTypes(paymentTypes);
				List<Order> orders = orderDAO.getOrders(orderIds);
				Map<Long,Order> orderIdMap = Utils.getOrderIdOrders(orders);
				for(OrderTrxnDetail trxnDetail : orderTrxnDetails){
					 //  System.out.println("ORDER NOS :"+trxnDetail.getOrderId());
						ReportDetail detail = new ReportDetail();
						detail.setTrxnDate1(trxnDetail.getCreatedDate()); 
						detail.setTrxnDate(getddmmyyyy(trxnDetail.getCreatedDate().toString())); 
				 		detail.setTrxnType(payTypes.get(Long.valueOf(trxnDetail.getPymtType())));
						detail.setReferenceType(env.getProperty("report.type.order"));
						detail.setDescription(orderIdMap.get(trxnDetail.getOrderId()).getCustName().toUpperCase());
						/* changed for NET AMOUNT in Dr. side */
						detail.setDebitAmount(trxnDetail.getNetAmount());
						if(trxnDetail.getAdvance()!=null && trxnDetail.getAdvance()>0)
							detail.setCreditAmount(trxnDetail.getAdvance());
						detail.setBillNo(trxnDetail.getOrderId());
						detail.setOrderId(trxnDetail.getOrderId());
									
						reportDetails.add(detail);
					}
									
					
					
					/*if(trxnDetail.getAdvance()!=null && trxnDetail.getAdvance()>0){
						ReportDetail detail = new ReportDetail();
						detail.setTrxnDate1(trxnDetail.getCreatedDate()); 
						detail.setTrxnDate(getddmmyyyy(trxnDetail.getCreatedDate().toString())); 
				 		detail.setTrxnType(payTypes.get(Long.valueOf(trxnDetail.getPymtType())));
						detail.setReferenceType(env.getProperty("report.type.order"));
						detail.setDescription(orderIdMap.get(trxnDetail.getOrderId()).getCustName());
						 changed for NET AMOUNT in Dr. side 
						detail.setDebitAmount(trxnDetail.getNetAmount());
						detail.setCreditAmount(trxnDetail.getAdvance());
						detail.setBillNo(trxnDetail.getOrderId());
						detail.setOrderId(trxnDetail.getOrderId());
									
						reportDetails.add(detail);
					}
					if(trxnDetail.getBalance()!=null && trxnDetail.getBalance() > 0){
						ReportDetail detail = new ReportDetail();
						detail.setTrxnDate1(trxnDetail.getCreatedDate()); 
						detail.setTrxnDate(getddmmyyyy(trxnDetail.getCreatedDate().toString())); 
						detail.setTrxnType(payTypes.get(Long.valueOf(trxnDetail.getPymtType())));
						detail.setReferenceType(env.getProperty("report.type.order"));
						detail.setDescription(orderIdMap.get(trxnDetail.getOrderId()).getCustName());
						 changed  to update the ORDER NET AMOUNT instead Order Balance 
						detail.setDebitAmount(trxnDetail.getBalance());
						detail.setDebitAmount(trxnDetail.getNetAmount() );
						if(payTypes.get(Long.valueOf(trxnDetail.getPymtType())).equals("Cheque")){
							detail.setChqDate(DateUtils.toDate(trxnDetail.getCreatedDate(),true));
							detail.setChqNo(trxnDetail.getChequeNo());
							detail.setBankName(trxnDetail.getBankName());
						}
						detail.setBillNo(trxnDetail.getOrderId());
						detail.setOrderId(trxnDetail.getOrderId());
						reportDetails.add(detail);
					}*/
					
				
			}
			}
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("order :: " + _exception);
		}
		return reportDetails;
	}
	
	public List<ReportDetail> cancelledOrderByIdsforCustomer(Report report, List<Long> orderIds) {
		List<ReportDetail> reportDetails = null;
		try{
			//System.out.println("cancelled orders Report Service Impl:");
			if(orderIds !=null && !orderIds.isEmpty()){
			List<OrderTrxnDetail> orderTrxnDetails = reportDAO.getOrders(report,orderIds);
			//List<OrderTrxnDetail> cancelledOrderTrxnDetails = reportDAO.getOrders(report);
		//	System.out.println("cancelled order Details size:"+orderTrxnDetails.size());
			if(orderTrxnDetails!=null && !orderTrxnDetails.isEmpty()){
				reportDetails = new ArrayList<ReportDetail>();
				//List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
				//Map<Long,String> payTypes = Utils.getTypes(paymentTypes);
				//List<Order> orders = orderDAO.getOrders(orderIds);
				//Map<Long,Order> orderIdMap = Utils.getOrderIdOrders(orders);
				for(OrderTrxnDetail trxnDetail : orderTrxnDetails){
					 //  System.out.println("ORDER NOS :"+trxnDetail.getOrderId());
						ReportDetail detail = new ReportDetail();
						detail.setTrxnDate1(trxnDetail.getCreatedDate()); 
						detail.setTrxnDate(getddmmyyyy(trxnDetail.getCreatedDate().toString())); 
				 		detail.setTrxnType("Cash");
						detail.setReferenceType(env.getProperty("report.type.order"));
						//detail.setDescription(orderIdMap.get(trxnDetail.getOrderId()).getCustName());
						/* changed for NET AMOUNT in Dr. side */
						//detail.setDebitAmount(trxnDetail.getNetAmount());
						if(trxnDetail.getAdvance()!=null && trxnDetail.getAdvance()>0)
							detail.setCreditAmount(trxnDetail.getAdvance());
						detail.setBillNo(trxnDetail.getOrderId());
						detail.setOrderId(trxnDetail.getOrderId());
									
						reportDetails.add(detail);
					}
					
				
			}
			}
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("order :: " + _exception);
		}
		return reportDetails;
	}
	

	// for customer Outstanding Summary report 
	public List<ReportDetail> customerOSRByOrderIds(Report report, List<Long> orderIds) {
		List<ReportDetail> reportDetails = new ArrayList<ReportDetail>();
		ReportDetail newreportDetails = new ReportDetail();
		try{
			if(orderIds !=null && !orderIds.isEmpty()){
				List<OrderTrxnDetail> orderTrxnDetails = reportDAO.getOrdersSum(report,orderIds);
				if (orderTrxnDetails.size()>0){
					//System.out.println("hai from OSRByOrdersId");
					if(orderTrxnDetails.get(0).getAdvance()!=null ){
						newreportDetails.setCashsale(orderTrxnDetails.get(0).getAdvance());
					}
					else{
						newreportDetails.setCashsale(0.00);	
					}
								
					if(orderTrxnDetails.get(0).getNetAmount()!=null){		
						newreportDetails.setInvoiceAmount(orderTrxnDetails.get(0).getNetAmount());
					}
					else{
						newreportDetails.setInvoiceAmount(0.00);
					}
									
					//System.out.println("hai from getInvoiceAmount():"+newreportDetails.getCashsale());
					//System.out.println("hai from getNetAmount():"+newreportDetails.getInvoiceAmount());
					}
			//	System.out.println("hai from OSRByOrdersId=========CHECK");
			reportDetails.add(newreportDetails);
			//System.out.println("hai from OSRByOrdersId=========CHECK++++++");
			}
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("order :: " + _exception);
		}
		//System.out.println(reportDetails.get(0).getBalance()+" balance and netamount "+reportDetails.get(0).getInvoiceAmount());
		return reportDetails;
	}
	
	public List<ReportDetail> salesTargetOrderIds(Report report, List<Long> orderIds) {
		List<ReportDetail> reportDetails = new ArrayList<ReportDetail>();
		ReportDetail newreportDetails = new ReportDetail();
		try{
			if(orderIds !=null && !orderIds.isEmpty()){
				List<OrderTrxnDetail> orderTrxnDetails = reportDAO.getSalesTargetOrdersSum(report,orderIds);
				if (orderTrxnDetails.size()>0){
					//System.out.println("hai from OSRByOrdersId");
												
					if(orderTrxnDetails.get(0).getNetAmount()!=null){		
						newreportDetails.setInvoiceAmount(orderTrxnDetails.get(0).getNetAmount());
					}
					else{
						newreportDetails.setInvoiceAmount(0.00);
					}
									
					//System.out.println("hai from getInvoiceAmount():"+newreportDetails.getCashsale());
					//System.out.println("hai from getNetAmount():"+newreportDetails.getInvoiceAmount());
					}
				//System.out.println("hai from OSRByOrdersId=========CHECK");
			reportDetails.add(newreportDetails);
			//System.out.println("hai from OSRByOrdersId=========CHECK++++++");
			}
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("order :: " + _exception);
		}
		//System.out.println(reportDetails.get(0).getBalance()+" balance and netamount "+reportDetails.get(0).getInvoiceAmount());
		return reportDetails;
	}
	
	public List<ReportDetail> customerOSRBFDOrderIds(Report report, List<Long> orderIds) {
		List<ReportDetail> reportDetails = new ArrayList<ReportDetail>();
		ReportDetail newreportDetails = new ReportDetail();
		try{
			if(orderIds !=null && !orderIds.isEmpty()){
			List<OrderTrxnDetail> orderTrxnDetails = reportDAO.getOrdersSum(report,orderIds);
			if (orderTrxnDetails.size() !=0){
				//System.out.println("hai from OSRBFDId");
				if(orderTrxnDetails.get(0).getAdvance()!=null){
					 newreportDetails.setCashsale(orderTrxnDetails.get(0).getAdvance());
				}
				else{
					 newreportDetails.setCashsale(0.00);	
				}
								
				if(orderTrxnDetails.get(0).getNetAmount()!=null){		
					newreportDetails.setInvoiceAmount(orderTrxnDetails.get(0).getNetAmount());
				}
				else{
					newreportDetails.setInvoiceAmount(0.00);
				}
									
				//System.out.println("hai from getInvoiceAmount():"+newreportDetails.getCashsale());
				//System.out.println("hai from getNetAmount():"+newreportDetails.getInvoiceAmount());
				}
			}
			reportDetails.add(newreportDetails);
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("order :: " + _exception);
		}
		//System.out.println(reportDetails.get(0).getBalance()+" balance and netamount "+reportDetails.get(0).getInvoiceAmount());
		return reportDetails;
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see com.studio.service.ReportService#supplier(com.studio.domain.Report)
	 */
	
public List<ReportDetail> mbrsupplier(Report report) {
		
		List<ReportDetail> reportDetails=null;
		try{
			List<Purchase> purchases = reportDAO.getPurchase(report);
			List<Long> supIds = Utils.getSupplierIds(purchases);
			List<Supplier> suppliers = paymentDAO.getSupplierByIds(supIds);
			Map<Long,Supplier> idSuppliers = Utils.getIdSuppliers(suppliers);
			if(purchases!=null && !purchases.isEmpty()){
				List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
				Map<Long,String> payTypes = Utils.getTypes(paymentTypes);
				reportDetails = new ArrayList<ReportDetail>();
				for(Purchase purchase: purchases){
					
					if(purchase.getAmountPaid()!=null && purchase.getAmountPaid() > 0){
						ReportDetail detail = new ReportDetail();
						detail.setTrxnDate(DateUtils.toDate(purchase.getCreatedDate(),true));
						if(purchase.getPaymentId()!=null)
							detail.setTrxnType(payTypes.get(Long.valueOf(purchase.getPaymentId())));
						detail.setReferenceType("By Purchase:"+purchase.getInvoiceNumber());
						detail.setDescription(idSuppliers.get(purchase.getSupId()).getName());
						detail.setDebitAmount(purchase.getNetPayable());
						if(purchase.getPaymentId()!=null && payTypes.get(Long.valueOf(purchase.getPaymentId())).equals("Cheque")){
							detail.setChqDate(DateUtils.toDate(purchase.getChequeDate(),true));
							detail.setChqNo(purchase.getChequeNo());
							detail.setBankName(purchase.getBankName());
						}
						reportDetails.add(detail);
					}
					
					/*if(purchase.getBalancePayable()!=null && purchase.getBalancePayable() > 0){
						ReportDetail detail1 = new ReportDetail();
						detail1.setTrxnDate(DateUtils.toDate(purchase.getCreatedDate(),true));
						if(purchase.getPaymentId()!=null)
							detail1.setTrxnType(payTypes.get(Long.valueOf(purchase.getPaymentId())));
						detail1.setReferenceType("Invoice");
						detail1.setDescription(idSuppliers.get(purchase.getSupId()).getName());
						detail1.setDebitAmount(purchase.getBalancePayable());
						if(purchase.getPaymentId()!=null && payTypes.get(Long.valueOf(purchase.getPaymentId())).equals("Cheque")){
							detail1.setChqDate(DateUtils.toDate(purchase.getChequeDate(),true));
							detail1.setChqNo(purchase.getChequeNo());
							detail1.setBankName(purchase.getBankName());
						}
						reportDetails.add(detail1);
					}*/
					
				}
			}
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("supplier : " + _exception);
		}
		
		return reportDetails;
	}
	
public List<ReportDetail> mbrdetailpurchase(Report report) {
	
	List<ReportDetail> reportDetails=null;
	try{
		List<Purchase> purchases = reportDAO.getPurchase(report);
		List<Long> supIds = Utils.getSupplierIds(purchases);
		List<Supplier> suppliers = paymentDAO.getSupplierByIds(supIds);
		Map<Long,Supplier> idSuppliers = Utils.getIdSuppliers(suppliers);
		if(purchases!=null && !purchases.isEmpty()){
			List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
			Map<Long,String> payTypes = Utils.getTypes(paymentTypes);
			reportDetails = new ArrayList<ReportDetail>();
			for(Purchase purchase: purchases){
				
				if(purchase.getNetPayable() > 0){
					ReportDetail detail = new ReportDetail();
					detail.setTrxnDate(DateUtils.toDate(purchase.getCreatedDate(),true));
					/*	if(purchase.getPaymentId()!=null)
						detail.setTrxnType(payTypes.get(Long.valueOf(purchase.getPaymentId())));*/
					detail.setReferenceType("By Purchase:"+purchase.getInvoiceNumber());
					detail.setDescription(idSuppliers.get(purchase.getSupId()).getName());
					detail.setDebitAmount(purchase.getNetPayable());
					if(purchase.getPaymentId()!=null && payTypes.get(Long.valueOf(purchase.getPaymentId())).equals("Cheque")){
						detail.setChqDate(DateUtils.toDate(purchase.getChequeDate(),true));
						detail.setChqNo(purchase.getChequeNo());
						detail.setBankName(purchase.getBankName());
					}
					reportDetails.add(detail);
				}
				
				/*if(purchase.getBalancePayable()!=null && purchase.getBalancePayable() > 0){
					ReportDetail detail1 = new ReportDetail();
					detail1.setTrxnDate(DateUtils.toDate(purchase.getCreatedDate(),true));
					if(purchase.getPaymentId()!=null)
						detail1.setTrxnType(payTypes.get(Long.valueOf(purchase.getPaymentId())));
					detail1.setReferenceType("Invoice");
					detail1.setDescription(idSuppliers.get(purchase.getSupId()).getName());
					detail1.setDebitAmount(purchase.getBalancePayable());
					if(purchase.getPaymentId()!=null && payTypes.get(Long.valueOf(purchase.getPaymentId())).equals("Cheque")){
						detail1.setChqDate(DateUtils.toDate(purchase.getChequeDate(),true));
						detail1.setChqNo(purchase.getChequeNo());
						detail1.setBankName(purchase.getBankName());
					}
					reportDetails.add(detail1);
				}*/
				
			}
		}
	}catch (Exception _exception) {
		_exception.printStackTrace();
		logger.error("supplier : " + _exception);
	}
	
	return reportDetails;
}



	
	public List<ReportDetail> supplier(Report report) {
		
		List<ReportDetail> reportDetails=null;
		try{
			List<Purchase> purchases = reportDAO.getPurchase(report);
			List<Long> supIds = Utils.getSupplierIds(purchases);
			List<Supplier> suppliers = paymentDAO.getSupplierByIds(supIds);
			Map<Long,Supplier> idSuppliers = Utils.getIdSuppliers(suppliers);
			if(purchases!=null && !purchases.isEmpty()){
				List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
				Map<Long,String> payTypes = Utils.getTypes(paymentTypes);
				reportDetails = new ArrayList<ReportDetail>();
				for(Purchase purchase: purchases){
					
					if(purchase.getAmountPaid()!=null && purchase.getAmountPaid() > 0){
						ReportDetail detail = new ReportDetail();
						detail.setTrxnDate(DateUtils.toDate(purchase.getCreatedDate(),true));
						if(purchase.getPaymentId()!=null)
							detail.setTrxnType(payTypes.get(Long.valueOf(purchase.getPaymentId())));
						detail.setReferenceType("Invoice");
						detail.setDescription(idSuppliers.get(purchase.getSupId()).getName());
						detail.setCreditAmount(purchase.getAmountPaid());
						if(purchase.getPaymentId()!=null && payTypes.get(Long.valueOf(purchase.getPaymentId())).equals("Cheque")){
							detail.setChqDate(DateUtils.toDate(purchase.getChequeDate(),true));
							detail.setChqNo(purchase.getChequeNo());
							detail.setBankName(purchase.getBankName());
						}
						reportDetails.add(detail);
					}
					
					if(purchase.getBalancePayable()!=null && purchase.getBalancePayable() > 0){
						ReportDetail detail1 = new ReportDetail();
						detail1.setTrxnDate(DateUtils.toDate(purchase.getCreatedDate(),true));
						if(purchase.getPaymentId()!=null)
							detail1.setTrxnType(payTypes.get(Long.valueOf(purchase.getPaymentId())));
						detail1.setReferenceType("Invoice");
						detail1.setDescription(idSuppliers.get(purchase.getSupId()).getName());
						detail1.setDebitAmount(purchase.getBalancePayable());
						if(purchase.getPaymentId()!=null && payTypes.get(Long.valueOf(purchase.getPaymentId())).equals("Cheque")){
							detail1.setChqDate(DateUtils.toDate(purchase.getChequeDate(),true));
							detail1.setChqNo(purchase.getChequeNo());
							detail1.setBankName(purchase.getBankName());
						}
						reportDetails.add(detail1);
					}
					
				}
			}
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("supplier : " + _exception);
		}
		
		return reportDetails;
	}
	
	public List<ReportDetail> supplierReport(Report report) {
		
		List<ReportDetail> reportDetails=null;
		String suppliername=null;
		Supplier supplier=null;
		try{
			List<Purchase> purchases = reportDAO.getPurchase(report);
		
			if(purchases!=null && !purchases.isEmpty()){
				List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
				Map<Long,String> payTypes = Utils.getTypes(paymentTypes);
				reportDetails = new ArrayList<ReportDetail>();
				for(Purchase purchase: purchases){
					if(purchase.getInvoiceAmount()!=null && purchase.getInvoiceAmount() > 0){
						ReportDetail detail = new ReportDetail();
							detail.setId(purchase.getId());
							// added for supplierNAME
							/*supplier=customerDAO.getSupplierNameById(purchase.getSupId());
							if(supplier !=null)
							detail.setSupplierName(supplier.getName());*/
							
							detail.setInvoiceDate(purchase.getStrInvoiceDate());
								if(purchase.getGoodsReceivedDate()!=null)
								detail.setTrxnDate(DateUtils.toDate(purchase.getGoodsReceivedDate(),true));
								detail.setBillNo(Long.valueOf(purchase.getInvoiceNumber()));
									if(purchase.getPaymentId()!=null)
										detail.setTrxnType(payTypes.get(Long.valueOf(purchase.getPaymentId())));
			//						detail.setCreditAmount(purchase.getInvoiceAmount());
									detail.setCreditAmount(purchase.getNetPayable());
			//						detail.setDebitAmount(purchase.getAmountPaid());
										if(purchase.getPaymentId()!=null && payTypes.get(Long.valueOf(purchase.getPaymentId())).equals("Cheque")){
											detail.setChqDate(DateUtils.toDate(purchase.getChequeDate(),true));
											detail.setChqNo(purchase.getChequeNo());
											detail.setBankName(purchase.getBankName());
						}
						reportDetails.add(detail);
					}
					
					if(purchase.getAmountPaid()!=null && purchase.getAmountPaid() > 0){
						ReportDetail detail1 = new ReportDetail();
						detail1.setId(purchase.getId());
						/*supplier=customerDAO.getSupplierNameById(purchase.getSupId());
						if(supplier !=null)
						detail1.setSupplierName(supplier.getName());*/
						detail1.setInvoiceDate(purchase.getStrInvoiceDate());
						if(purchase.getGoodsReceivedDate()!=null)
						detail1.setTrxnDate(DateUtils.toDate(purchase.getGoodsReceivedDate(),true));
						detail1.setBillNo(Long.valueOf(purchase.getInvoiceNumber()));
						if(purchase.getPaymentId()!=null)
							detail1.setTrxnType(payTypes.get(Long.valueOf(purchase.getPaymentId())));
						detail1.setDebitAmount(purchase.getAmountPaid());
						
						if(purchase.getPaymentId()!=null && payTypes.get(Long.valueOf(purchase.getPaymentId())).equals("Cheque")){
							detail1.setChqDate(DateUtils.toDate(purchase.getChequeDate(),true));
							detail1.setChqNo(purchase.getChequeNo());
							detail1.setBankName(purchase.getBankName());
						}
						reportDetails.add(detail1);
					}
					
				}
			}
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("supplier : " + _exception);
		}
		
		return reportDetails;
	}
	
	
	public DailyReportDetail supplier(Report report,Boolean isDailyReport) {
		List<ReportDetail> reportDetails=null;
		DailyReportDetail dailyReportDetail = null;
		try{
			dailyReportDetail = new DailyReportDetail();
			List<Purchase> purchases = reportDAO.getPurchase(report);
			List<Long> supIds = Utils.getSupplierIds(purchases);
			List<Supplier> suppliers = paymentDAO.getSupplierByIds(supIds);
			Map<Long,Supplier> idSuppliers = Utils.getIdSuppliers(suppliers);
			if(purchases!=null && !purchases.isEmpty()){
				List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
				Map<Long,String> payTypes = Utils.getTypes(paymentTypes);
				reportDetails = new ArrayList<ReportDetail>();
				Double invoiceAmount = 0.00;
				Double invoicePaidAmount = 0.00;
				for(Purchase purchase: purchases){
					if(purchase.getAmountPaid()!=null && purchase.getAmountPaid() > 0){
						ReportDetail detail = new ReportDetail();
						detail.setTrxnDate(DateUtils.toDate(purchase.getCreatedDate(),true));
						if(purchase.getPaymentId()!=null)
							detail.setTrxnType(payTypes.get(Long.valueOf(purchase.getPaymentId())));
						detail.setReferenceType("Invoice");
						detail.setDescription(idSuppliers.get(purchase.getSupId()).getName());
						detail.setDebitAmount(purchase.getAmountPaid());
						if(purchase.getPaymentId()!=null && payTypes.get(Long.valueOf(purchase.getPaymentId())).equals("Cheque")){
							detail.setChqDate(DateUtils.toDate(purchase.getChequeDate(),true));
							detail.setChqNo(purchase.getChequeNo());
							detail.setBankName(purchase.getBankName());
						}
						reportDetails.add(detail);
					}
					invoiceAmount += purchase.getInvoiceAmount();
					invoicePaidAmount += purchase.getAmountPaid();
					dailyReportDetail.setReportDetails(reportDetails);
					dailyReportDetail.setInvoiceAmount(invoiceAmount);
					dailyReportDetail.setInvoicePaidAmount(invoicePaidAmount);
					dailyReportDetail.setStrInvoiceAmount(StringUtils.decimalFormat(invoiceAmount));
					dailyReportDetail.setStrInvoicePaidAmount(StringUtils.decimalFormat(invoicePaidAmount));
				}
			}
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("supplier : " + _exception);
		}
		return dailyReportDetail;
	}

	/* (non-Javadoc)
	 * @see com.studio.service.ReportService#voucher(com.studio.domain.Report)
	 */
	
	
public List<ReportDetail> mbrdetailexpense(Report report){
	List<ReportDetail> reportDetails=null;
	List<Voucher> vouchers = reportDAO.getmbrdetailExpense(report);
	if(vouchers!=null && !vouchers.isEmpty()){
		reportDetails = new ArrayList<ReportDetail>();
		List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
		Map<Long,String> payTypes = Utils.getTypes(paymentTypes);
		List<ExpenseType> expenseTypes = metaDAO.getExpenseTypes();
		Map<Long,String> expTypes = Utils.getTypes(expenseTypes);
		for(Voucher voucher: vouchers){
			ReportDetail detail = new ReportDetail();
			
			/*if(voucher.getPaymentId()!=0){
			detail.setTrxnType(payTypes.get(Long.valueOf(voucher.getPaymentId())));
			//detail.setReferenceType(vTypes.get(Long.valueOf(voucher.getPaymentId())));
			detail.setReferenceType("Voucher No.:"+voucher.getVoucherId());
			}*/
		if(voucher.getExpenseId()>10000){
				if (voucher.getExpenseId() !=10012 && voucher.getExpenseId()!=10014){
					detail.setTrxnDate(DateUtils.toDate(voucher.getCreatedDate(),true));
					detail.setTrxnType(payTypes.get(Long.valueOf(voucher.getPaymentId())));
					//detail.setReferenceType(vTypes.get(Long.valueOf(voucher.getPaymentId())));
					detail.setReferenceType("Voucher No.:"+voucher.getVoucherId());
					detail.setDescription(expTypes.get((long)voucher.getExpenseId()).toUpperCase()+":"+voucher.getNaration());
					detail.setDebitAmount(voucher.getPayAmount());
					reportDetails.add(detail);
				}
				
			}
			
			
			/*if(voucher.getPaymentId()!=0 && payTypes.get(Long.valueOf(voucher.getPaymentId())).equals("Cheque")){
				detail.setChqDate(DateUtils.toDate(voucher.getChequeDate(),true));
				detail.setChqNo(voucher.getChequeNo());
				detail.setBankName(voucher.getBankName());
				detail.setNarration(voucher.getNaration());
			}*/
			
		}
		
		
	
	}
	
	return reportDetails;
}
	
public List<ReportDetail> mbrpaymentvoucher(Report report) {
		
		List<ReportDetail> reportDetails=null;
		try{
			List<Voucher> vouchers = reportDAO.getMBRVouchers(report);
			if(vouchers!=null && !vouchers.isEmpty()){
				reportDetails = new ArrayList<ReportDetail>();
				List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
				Map<Long,String> payTypes = Utils.getTypes(paymentTypes);
				List<ExpenseType> expenseTypes = metaDAO.getExpenseTypes();
				List<Long> supIds = Utils.getSupIds(vouchers);
				
				List<Supplier> suppliers = null;
				if(supIds!=null && !supIds.isEmpty())
				 suppliers = paymentDAO.getSupplierByIds(supIds);
				
				Map<Long,Supplier> sMap = null;
				if(suppliers!=null && !suppliers.isEmpty())
				 sMap = Utils.getIdSuppliers(suppliers);
				
				Map<Long,String> expTypes = Utils.getTypes(expenseTypes);
				List<VoucherType> voucherTypes =  metaDAO.getVoucherTypes();
				Map<Long,String> vTypes = Utils.getTypes(voucherTypes);
				for(Voucher voucher: vouchers){
					ReportDetail detail = new ReportDetail();
					detail.setTrxnDate(DateUtils.toDate(voucher.getCreatedDate(),true));
					if(voucher.getPaymentId()!=0){
					detail.setTrxnType(payTypes.get(Long.valueOf(voucher.getPaymentId())));
					//detail.setReferenceType(vTypes.get(Long.valueOf(voucher.getPaymentId())));
					detail.setReferenceType("Voucher No.:"+voucher.getVoucherId());
					}
					if(voucher.getExpenseId()>10000){
						detail.setDescription(expTypes.get((long)voucher.getExpenseId()).toUpperCase()+":"+voucher.getNaration());
						detail.setNarration(voucher.getNaration());
					}else{
						if(sMap!=null && sMap.get((long)voucher.getExpenseId())!=null)
							//detail.setDescription(sMap.get((long)voucher.getExpenseId()).getName());
							detail.setDescription(sMap.get((long)voucher.getExpenseId()).getName()+":"+voucher.getNaration());
							detail.setNarration(voucher.getNaration());
					}
					
					detail.setDebitAmount(voucher.getPayAmount());
					if(voucher.getPaymentId()!=0 && payTypes.get(Long.valueOf(voucher.getPaymentId())).equals("Cheque")){
						detail.setChqDate(DateUtils.toDate(voucher.getChequeDate(),true));
						detail.setChqNo(voucher.getChequeNo());
						detail.setBankName(voucher.getBankName());
						detail.setNarration(voucher.getNaration());
					}
					reportDetails.add(detail);
				}
			}
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("voucher(Report report) :" + _exception);
		}
		
		return reportDetails;
	}
	
	public List<ReportDetail> voucher(Report report) {
		
		List<ReportDetail> reportDetails=null;
		try{
			List<Voucher> vouchers = reportDAO.getVouchers(report);
			if(vouchers!=null && !vouchers.isEmpty()){
				reportDetails = new ArrayList<ReportDetail>();
				List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
				Map<Long,String> payTypes = Utils.getTypes(paymentTypes);
				List<ExpenseType> expenseTypes = metaDAO.getExpenseTypes();
				List<Long> supIds = Utils.getSupIds(vouchers);
				
				List<Supplier> suppliers = null;
				if(supIds!=null && !supIds.isEmpty())
				 suppliers = paymentDAO.getSupplierByIds(supIds);
				
				Map<Long,Supplier> sMap = null;
				if(suppliers!=null && !suppliers.isEmpty())
				 sMap = Utils.getIdSuppliers(suppliers);
				
				Map<Long,String> expTypes = Utils.getTypes(expenseTypes);
				List<VoucherType> voucherTypes =  metaDAO.getVoucherTypes();
				Map<Long,String> vTypes = Utils.getTypes(voucherTypes);
				for(Voucher voucher: vouchers){
					ReportDetail detail = new ReportDetail();
					detail.setTrxnDate(DateUtils.toDate(voucher.getCreatedDate(),true));
					if(voucher.getPaymentId()!=0){
					detail.setTrxnType(payTypes.get(Long.valueOf(voucher.getPaymentId())));
					detail.setReferenceType(vTypes.get(Long.valueOf(voucher.getPaymentId())));
					}
					if(voucher.getExpenseId()>10000){
						detail.setDescription(expTypes.get((long)voucher.getExpenseId())+":"+voucher.getNaration());
						detail.setNarration(voucher.getNaration());
					}else{
						if(sMap!=null && sMap.get((long)voucher.getExpenseId())!=null)
							//detail.setDescription(sMap.get((long)voucher.getExpenseId()).getName());
							detail.setDescription(sMap.get((long)voucher.getExpenseId()).getName()+":"+voucher.getNaration());
							detail.setNarration(voucher.getNaration());
					}
					
					detail.setDebitAmount(voucher.getPayAmount());
					if(voucher.getPaymentId()!=0 && payTypes.get(Long.valueOf(voucher.getPaymentId())).equals("Cheque")){
						detail.setChqDate(DateUtils.toDate(voucher.getChequeDate(),true));
						detail.setChqNo(voucher.getChequeNo());
						detail.setBankName(voucher.getBankName());
						detail.setNarration(voucher.getNaration());
					}
					reportDetails.add(detail);
				}
			}
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("voucher(Report report) :" + _exception);
		}
		
		return reportDetails;
	}
	
	public List<ReportDetail> supplierVoucher(Report report) {
		
		List<ReportDetail> reportDetails=null;
		Supplier suppliername=null;
		try{
			List<Voucher> vouchers = reportDAO.getVouchers(report);
			if(vouchers!=null && !vouchers.isEmpty()){
				reportDetails = new ArrayList<ReportDetail>();
				List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
				Map<Long,String> payTypes = Utils.getTypes(paymentTypes);
				
				List<VoucherType> voucherTypes =  metaDAO.getVoucherTypes();
				Map<Long,String> vTypes = Utils.getTypes(voucherTypes);
				for(Voucher voucher: vouchers){
					ReportDetail detail = new ReportDetail();
					
					if(voucher.getExpenseId() < 10000){
						
						// adding Supplier NAME in the report
						suppliername=customerDAO.getSupplierNameById(Long.valueOf((voucher.getExpenseId())));
						if (suppliername!=null)
						detail.setSupplierName(suppliername.getName());
						
						detail.setDescription(detail.getSupplierName()+":"+voucher.getNaration());
						detail.setNarration(voucher.getNaration());
						detail.setTrxnDate(voucher.getStrVoucherDate().substring(0,10));
						detail.setReferenceType(vTypes.get(Long.valueOf(voucher.getPaymentId())));
						
						detail.setInvoiceDate(DateUtils.toDate(voucher.getVoucherDate(),true));
						detail.setOrderId(voucher.getVoucherId());
						if(voucher.getPaymentId()!=0){
						detail.setTrxnType(payTypes.get(Long.valueOf(voucher.getPaymentId())));
						detail.setReferenceType(vTypes.get(Long.valueOf(voucher.getPaymentId())));
						
						}
						
						detail.setDebitAmount(voucher.getPayAmount());
						if(voucher.getPaymentId()!=0 && payTypes.get(Long.valueOf(voucher.getPaymentId())).equals("Cheque")){
							detail.setChqDate(DateUtils.toDate(voucher.getChequeDate(),true));
							detail.setChqNo(voucher.getChequeNo());
							detail.setBankName(voucher.getBankName());
							
						}
						reportDetails.add(detail);
					}
				}
			}
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("supplierVoucher : " + _exception);
		}
		
		return reportDetails;
	}
	
	public DailyReportDetail voucher(Report report,Boolean isDailyReport) {
		
		List<ReportDetail> reportDetails=null;
		DailyReportDetail dailyReportDetail = null;
		try{
			dailyReportDetail = new DailyReportDetail();
			List<Voucher> vouchers = reportDAO.getVouchers(report);
			if(vouchers!=null && !vouchers.isEmpty()){
				reportDetails = new ArrayList<ReportDetail>();
				List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
				Map<Long,String> payTypes = Utils.getTypes(paymentTypes);
				List<ExpenseType> expenseTypes = metaDAO.getExpenseTypes();
				List<Long> supIds = Utils.getSupIds(vouchers);
				
				List<Supplier> suppliers = null;
				if(supIds!=null && !supIds.isEmpty())
				 suppliers = paymentDAO.getSupplierByIds(supIds);
				
				Map<Long,Supplier> sMap = null;
				if(suppliers!=null && !suppliers.isEmpty())
				 sMap = Utils.getIdSuppliers(suppliers);
				
				Map<Long,String> expTypes = Utils.getTypes(expenseTypes);
				List<VoucherType> voucherTypes =  metaDAO.getVoucherTypes();
				Map<Long,String> vTypes = Utils.getTypes(voucherTypes);
				Double invoicePaidAmount = 0.00;
				Double otherPayment = 0.00;
				for(Voucher voucher: vouchers){
					
					if(voucher.getPayAmount() > 0){
						ReportDetail detail = new ReportDetail();
						detail.setTrxnDate(DateUtils.toDate(voucher.getCreatedDate(),true));
						if(voucher.getPaymentId()!=0)
						detail.setTrxnType(payTypes.get(Long.valueOf(voucher.getPaymentId())));
						VoucherPayment voucherPayment = paymentDAO.getVoucherPayment(voucher.getVoucherId());
						if(voucherPayment!=null )
							detail.setReferenceType(vTypes.get(Long.valueOf(voucherPayment.getRefTypeId())));
						else{
							detail.setReferenceType("Voucher");
						}
						if(voucher.getExpenseId()>10000){
							// added for contra entry starts
							//System.out.println("Expence ID for Contra:"+voucher.getExpenseId());
							if (voucher.getExpenseId()==10014){
								detail.setDescription("CONTRA: "+voucher.getNaration());
							}else{
								//detail.setDescription(expTypes.get((long)voucher.getExpenseId()));
								detail.setDescription(expTypes.get((long)voucher.getExpenseId())+":"+voucher.getNaration());
							}
							//added  for contra entry ends
							invoicePaidAmount += voucher.getPayAmount();
						}else{
							if(sMap!=null && sMap.get((long)voucher.getExpenseId())!=null)
								detail.setDescription(sMap.get((long)voucher.getExpenseId()).getName());
							otherPayment += voucher.getPayAmount();
						}
						
						detail.setDebitAmount(voucher.getPayAmount());
						if(voucher.getPaymentId()!=0 && payTypes.get(Long.valueOf(voucher.getPaymentId()))!=null && 
								payTypes.get(Long.valueOf(voucher.getPaymentId())).equals("Cheque")){
							detail.setChqDate(DateUtils.toDate(voucher.getChequeDate(),true));
							detail.setChqNo(voucher.getChequeNo());
							detail.setBankName(voucher.getBankName());
							detail.setNarration(voucher.getNaration());
						}
						reportDetails.add(detail);
					}
				}
				dailyReportDetail.setReportDetails(reportDetails);
				dailyReportDetail.setInvoicePaidAmount(invoicePaidAmount);
				dailyReportDetail.setOtherPayment(otherPayment);
				dailyReportDetail.setStrInvoiceAmount(StringUtils.decimalFormat(invoicePaidAmount));
				dailyReportDetail.setStrOtherPayment(StringUtils.decimalFormat(otherPayment));
			}
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("voucher(Report report,Boolean isDailyReport) : " + _exception);
		}
		
		return dailyReportDetail;
	}


	/* (non-Javadoc)
	 * @see com.studio.service.ReportService#receipt(com.studio.domain.Report)
	 */
	public List<ReportDetail> mbrreceipt(Report report) {
		List<ReportDetail> reportDetails=null;
		try{
			List<Receipt> receipts = reportDAO.getReceipts(report);
			if(receipts!=null && !receipts.isEmpty()){
				reportDetails = new ArrayList<ReportDetail>();
				List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
				Map<Long,String> payTypes = Utils.getTypes(paymentTypes);
				List<ReceiptType> receiptTypes = metaDAO.getMetaData(new ReceiptType());
				Map<Long,String> receiptTypeMap = Utils.getTypes(receiptTypes);
				List<Long> custIds = Utils.getReceiptsCustomersIds(receipts);
				List<Customer> customers = customerDAO.getCustomers(custIds);
				Map<Long,Customer> custIdMap = Utils.getCustIds(customers);
				for(Receipt receipt: receipts){
					ReportDetail detail = new ReportDetail();
					detail.setTrxnDate(DateUtils.toDate(receipt.getCreatedDate(),true));
					detail.setTrxnType(payTypes.get(Long.valueOf(receipt.getPaymentId())));
					String str= receipt.getNaration();
					int index = str.lastIndexOf("order");
					detail.setReferenceType(receiptTypeMap.get((long)receipt.getExpenseId())+":"+receipt.getNaration().substring(index));
					if(receiptTypeMap.get((long)receipt.getExpenseId())!=null && receiptTypeMap.get((long)receipt.getExpenseId()).equals("Order")){
						if(custIdMap.get(receipt.getCustId())!=null)
						detail.setDescription(custIdMap.get(receipt.getCustId()).getFirstName());
					}else{
						detail.setDescription(receiptTypeMap.get(receipt.getExpenseId()));
					}
					// if customer Id == 0 then print narration
					if (receipt.getCustId() == 0){
						detail.setDescription(receipt.getNaration());
					}
						
						
					detail.setBillNo(receipt.getReceiptId());
					Receipt r = reportDAO.getReceiptById(receipt.getReceiptId());
					if(r!=null && r.getNaration()!=null)
					detail.setNarration(r.getNaration());
					if(r !=null && r.getReceiptPayments()!=null && !r.getReceiptPayments().isEmpty())
					detail.setOrderId(Long.valueOf(r.getReceiptPayments().get(0).getRefNo()));
					detail.setCreditAmount(receipt.getPayAmount());
					if(payTypes.get(Long.valueOf(receipt.getPaymentId())).equals("Cheque")){
						detail.setChqDate(DateUtils.toDate(receipt.getChequeDate(),true));
						detail.setChqNo(receipt.getChequeNo());
						detail.setBankName(receipt.getBankName());
						detail.setNarration(receipt.getNaration());
					}
					reportDetails.add(detail);
				}
			}
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("receipt(report) : " + _exception);
		}
		return reportDetails;
	}
	
	
	
	
	public List<ReportDetail> receipt(Report report) {
		List<ReportDetail> reportDetails=null;
		try{
			List<Receipt> receipts = reportDAO.getReceipts(report);
			if(receipts!=null && !receipts.isEmpty()){
				reportDetails = new ArrayList<ReportDetail>();
				List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
				Map<Long,String> payTypes = Utils.getTypes(paymentTypes);
				List<ReceiptType> receiptTypes = metaDAO.getMetaData(new ReceiptType());
				Map<Long,String> receiptTypeMap = Utils.getTypes(receiptTypes);
				List<Long> custIds = Utils.getReceiptsCustomersIds(receipts);
				List<Customer> customers = customerDAO.getCustomers(custIds);
				Map<Long,Customer> custIdMap = Utils.getCustIds(customers);
				for(Receipt receipt: receipts){
					ReportDetail detail = new ReportDetail();
					detail.setTrxnDate(DateUtils.toDate(receipt.getCreatedDate(),true));
					detail.setTrxnType(payTypes.get(Long.valueOf(receipt.getPaymentId())));
					detail.setReferenceType(receiptTypeMap.get((long)receipt.getExpenseId()));
					if(receiptTypeMap.get((long)receipt.getExpenseId())!=null && receiptTypeMap.get((long)receipt.getExpenseId()).equals("Order")){
						if(custIdMap.get(receipt.getCustId())!=null)
						detail.setDescription(custIdMap.get(receipt.getCustId()).getFirstName());
					}else{
						detail.setDescription(receiptTypeMap.get(receipt.getExpenseId()));
					}
					// if customer Id == 0 then print narration
					if (receipt.getCustId() == 0){
						detail.setDescription(receipt.getNaration());
					}
						
						
					detail.setBillNo(receipt.getReceiptId());
					Receipt r = reportDAO.getReceiptById(receipt.getReceiptId());
					if(r!=null && r.getNaration()!=null)
					detail.setNarration(r.getNaration());
					if(r !=null && r.getReceiptPayments()!=null && !r.getReceiptPayments().isEmpty())
					detail.setOrderId(Long.valueOf(r.getReceiptPayments().get(0).getRefNo()));
					detail.setCreditAmount(receipt.getPayAmount());
					if(payTypes.get(Long.valueOf(receipt.getPaymentId())).equals("Cheque")){
						detail.setChqDate(DateUtils.toDate(receipt.getChequeDate(),true));
						detail.setChqNo(receipt.getChequeNo());
						detail.setBankName(receipt.getBankName());
						detail.setNarration(receipt.getNaration());
					}
					reportDetails.add(detail);
				}
			}
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("receipt(report) : " + _exception);
		}
		return reportDetails;
	}
	
	
	// for customer report changes in trxndate in DATE format
/*	public List<ReportDetail> receiptforCustomer(Report report) {
		List<ReportDetail> reportDetails=null;
		try{
			List<Receipt> receipts = reportDAO.getReceipts(report);
			if(receipts!=null && !receipts.isEmpty()){
				reportDetails = new ArrayList<ReportDetail>();
				List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
				Map<Long,String> payTypes = Utils.getTypes(paymentTypes);
				List<ReceiptType> receiptTypes = (List<ReceiptType>) metaDAO.getMetaData(new ReceiptType());
				Map<Long,String> receiptTypeMap = Utils.getTypes(receiptTypes);
				List<Long> custIds = Utils.getReceiptsCustomersIds(receipts);
				List<Customer> customers = customerDAO.getCustomers(custIds);
				Map<Long,Customer> custIdMap = Utils.getCustIds(customers);
				for(Receipt receipt: receipts){
					ReportDetail detail = new ReportDetail();
					detail.setTrxnDate1(receipt.getCreatedDate());
					detail.setTrxnDate(getddmmyyyy(receipt.getCreatedDate().toString())); 
					detail.setTrxnType(payTypes.get(Long.valueOf(receipt.getPaymentId())));
					detail.setReferenceType(receiptTypeMap.get((long)receipt.getExpenseId()));
					if(receiptTypeMap.get((long)receipt.getExpenseId())!=null && receiptTypeMap.get((long)receipt.getExpenseId()).equals("Order")){
						if(custIdMap.get(receipt.getCustId())!=null)
						detail.setDescription(custIdMap.get(receipt.getCustId()).getFirstName());
					}else{
						detail.setDescription(receiptTypeMap.get(receipt.getExpenseId()));
					}
					detail.setBillNo(receipt.getReceiptId());
					Receipt r = reportDAO.getReceiptById(receipt.getReceiptId());
					if(r!=null && r.getNaration()!=null)
					detail.setNarration(r.getNaration());
					if(r !=null && r.getReceiptPayments()!=null && !r.getReceiptPayments().isEmpty())
					detail.setOrderId(Long.valueOf(r.getReceiptPayments().get(0).getRefNo()));
					detail.setCreditAmount(receipt.getPayAmount());
					if(payTypes.get(Long.valueOf(receipt.getPaymentId())).equals("Cheque")){
						detail.setChqDate(DateUtils.toDate(receipt.getChequeDate(),true));
						detail.setChqNo(receipt.getChequeNo());
						detail.setBankName(receipt.getBankName());
						detail.setNarration(receipt.getNaration());
					}
					reportDetails.add(detail);
				}
			}
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("receipt(report) : " + _exception);
		}
		return reportDetails;
	}
	*/
	public List<ReportDetail> receiptforCustomer(Report report) {
		List<ReportDetail> reportDetails=null;
		try{
			List<Receipt> receipts = reportDAO.getReceipts(report);
			if(receipts!=null && !receipts.isEmpty()){
				reportDetails = new ArrayList<ReportDetail>();
				List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
				Map<Long,String> payTypes = Utils.getTypes(paymentTypes);
				List<ReceiptType> receiptTypes = metaDAO.getMetaData(new ReceiptType());
				Map<Long,String> receiptTypeMap = Utils.getTypes(receiptTypes);
				List<Long> custIds = Utils.getReceiptsCustomersIds(receipts);
				List<Customer> customers = customerDAO.getCustomers(custIds);
				Map<Long,Customer> custIdMap = Utils.getCustIds(customers);
				for(Receipt receipt: receipts){
					ReportDetail detail = new ReportDetail();
					detail.setTrxnDate1(receipt.getCreatedDate());
					detail.setTrxnDate(getddmmyyyy(receipt.getCreatedDate().toString())); 
					detail.setTrxnType(payTypes.get(Long.valueOf(receipt.getPaymentId())));
					detail.setReferenceType(receiptTypeMap.get((long)receipt.getExpenseId()));
					
					if(receiptTypeMap.get((long)receipt.getExpenseId())!=null && receiptTypeMap.get((long)receipt.getExpenseId()).equals("Order")){
						if(custIdMap.get(receipt.getCustId())!=null)
						detail.setDescription(custIdMap.get(receipt.getCustId()).getFirstName());
					}else{
						detail.setDescription(receiptTypeMap.get(receipt.getExpenseId()));
					}
					detail.setBillNo(receipt.getReceiptId());
					detail.setCreditAmount(receipt.getPayAmount());
					Receipt r = reportDAO.getReceiptById(receipt.getReceiptId());
					if(r!=null && r.getNaration()!=null)
					detail.setNarration(r.getNaration());
					if(payTypes.get(Long.valueOf(receipt.getPaymentId())).equals("Cheque")){
						detail.setChqDate(DateUtils.toDate(receipt.getChequeDate(),true));
						detail.setChqNo(receipt.getChequeNo());
						detail.setBankName(receipt.getBankName());
						detail.setNarration(receipt.getNaration());
					}
					if(r !=null && r.getReceiptPayments()!=null && !r.getReceiptPayments().isEmpty()){
						detail.setCreditAmount(receipt.getPayAmount());
						reportDetails.add(detail);
						for(int i=0;i<r.getReceiptPayments().size();i++){
							detail = new ReportDetail();
							detail.setTrxnDate(getddmmyyyy(receipt.getCreatedDate().toString())); 
							detail.setOrderId(Long.valueOf(r.getReceiptPayments().get(i).getRefNo()));
							detail.setBillNo(receipt.getReceiptId());
							detail.setPayAmount(r.getReceiptPayments().get(i).getPaidAmount());
							detail.setTotalReceipt(r.getReceiptPayments().get(i).getNetAmount());
							reportDetails.add(detail);
						}
					}else{
					/*detail.setOrderId(Long.valueOf(r.getReceiptPayments().get(0).getRefNo()));*/
					/*detail.setCreditAmount(receipt.getPayAmount());*/
					
					reportDetails.add(detail);
					}
				}
			}
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("receipt(report) : " + _exception);
		}
		return reportDetails;
	}
	
	
	public List<ReportDetail> getCOSReceipt(Report report) {
		List<ReportDetail> reportDetails= new ArrayList<ReportDetail>();
		ReportDetail newreportDetail = new ReportDetail();
		try{
			List<Receipt> receipts = reportDAO.getSumOfReceipts(report);
			//System.out.println("receipt size :"+receipts.size());	
			if(receipts.size()>0){
					newreportDetail.setPayAmount(receipts.get(0).getPayAmount());
			//System.out.println("Well  receipt ");	
			}else
			{
				newreportDetail.setPayAmount(0.00);
			}
			reportDetails.add(newreportDetail);	
			
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("receipt(report) : " + _exception);
		}
		//System.out.println("Well  receipt size : "+reportDetails.size());	
		return reportDetails;
	}
	
	public List<ReportDetail> getCOSBFDReceipt(Report report) {
		List<ReportDetail> reportDetails= new ArrayList<ReportDetail>();
		ReportDetail newreportDetail = new ReportDetail();
		try{
			List<Receipt> receipts = reportDAO.getSumOfReceiptsBFD(report);
			//System.out.println("receipt size :"+receipts.size());	
			if(receipts!=null && !receipts.isEmpty()){
				
				newreportDetail.setPayAmount(receipts.get(0).getPayAmount());
			//System.out.println("Well  receipt ");	
			}else
			{
				newreportDetail.setPayAmount(0.00);
			}
			reportDetails.add(newreportDetail);	
			
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("receipt(report) : " + _exception);
		}
		//System.out.println("Well  receipt size : "+reportDetails.size());	
		return reportDetails;
	}
	
	
	public DailyReportDetail receipt(Report report,Boolean isDailyReport) {
		List<ReportDetail> reportDetails=null;
		DailyReportDetail dailyReportDetail = null;
		try{
			dailyReportDetail = new DailyReportDetail();
			List<Receipt> receipts = reportDAO.getReceipts(report);
			if(receipts!=null && !receipts.isEmpty()){
				reportDetails = new ArrayList<ReportDetail>();
				List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
				Map<Long,String> payTypes = Utils.getTypes(paymentTypes);
				List<ReceiptType> receiptTypes = metaDAO.getMetaData(new ReceiptType());
				Map<Long,String> receiptTypeMap = Utils.getTypes(receiptTypes);
				List<Long> custIds = Utils.getReceiptsCustomersIds(receipts);
				List<Customer> customers = customerDAO.getCustomers(custIds);
				Map<Long,Customer> custIdMap = Utils.getCustIds(customers);
				Double receiptAmount = 0.00;
				for(Receipt receipt: receipts){
					if(receipt.getPayAmount() > 0){
						ReportDetail detail = new ReportDetail();
						detail.setTrxnDate(DateUtils.toDate(receipt.getCreatedDate(),true));
						detail.setTrxnType(payTypes.get(Long.valueOf(receipt.getPaymentId())));
						detail.setReferenceType(receiptTypeMap.get((long)receipt.getExpenseId()));
						if(receiptTypeMap.get((long)receipt.getExpenseId())!=null && receiptTypeMap.get((long)receipt.getExpenseId()).equals("Order")){
							if(custIdMap.get(receipt.getCustId())!=null)
							detail.setDescription(custIdMap.get(receipt.getCustId()).getFirstName());
						}else{
							detail.setDescription(receiptTypeMap.get(receipt.getExpenseId()));
						}
						detail.setBillNo(receipt.getReceiptId());
						Receipt r = reportDAO.getReceiptById(receipt.getReceiptId());
						if(r !=null && r.getReceiptPayments()!=null && !r.getReceiptPayments().isEmpty())
						detail.setOrderId(Long.valueOf(r.getReceiptPayments().get(0).getRefNo()));
						if(StringUtils.isEmpty(detail.getDescription()))
							detail.setDescription(r.getNaration());
						detail.setCreditAmount(receipt.getPayAmount());
						if(payTypes.get(Long.valueOf(receipt.getPaymentId())).equals("Cheque")){
							detail.setChqDate(DateUtils.toDate(receipt.getChequeDate(),true));
							detail.setChqNo(receipt.getChequeNo());
							detail.setBankName(receipt.getBankName());
							detail.setNarration(receipt.getNaration());
						}
						receiptAmount += receipt.getPayAmount();
						reportDetails.add(detail);
					}
				}
				dailyReportDetail.setReportDetails(reportDetails);
				dailyReportDetail.setReceiptAmount(receiptAmount);
				dailyReportDetail.setStrReceiptAmount(StringUtils.decimalFormat(receiptAmount));
			}
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("receipt(Report report,Boolean isDailyReport) : " + _exception);
		}
		return dailyReportDetail;
	}


	/* (non-Javadoc)
	 * @see com.studio.service.ReportService#agentOrders(com.studio.domain.Report)
	 */
	public List<AgentReportDetail> agentOrders(Report report) {
		List<AgentReportDetail> reportDetails = null;
	try{
			List<Order> orders = reportDAO.getAgentOrders(report);
			
			//OrderTrxnDetail otDetail= new OrderTrxnDetail();
				if(orders!=null && !orders.isEmpty()){
				reportDetails = new ArrayList<AgentReportDetail>();
				try{
				for(Order order:orders){
					//System.out.println("customer Id:"+order.getCustId());
					List<ReceiptPayment> receiptPayments = paymentDAO.getReceiptPaymentsByOrderId(Long.toString(order.getOrderId()));
					List<Product> products = customerDAO.getProductByOrderId(order.getOrderId());
					List<OrderTrxnDetail> otDetails= orderDAO.getTransactionByOrderId(order.getOrderId());
//					if(products !=null && !products.isEmpty()){
						AgentReportDetail agentReportDetail = new AgentReportDetail();
						agentReportDetail.setOrderDate(DateUtils.toDate(order.getOrderDate(),true));
						agentReportDetail.setOrderId(String.valueOf(order.getOrderId()));
						agentReportDetail.setCustName(order.getCustName().toUpperCase());
						agentReportDetail.setCustId(order.getCustId());
						
						//agentReportDetail.setCreditBalance(order.getCreditBalance());
						if(products !=null && !products.isEmpty()){
						for(Product product:products){
							agentReportDetail.setNoOfCopies((agentReportDetail.getNoOfCopies()!=null && product.getProductTypeId() == 102 ) ? agentReportDetail.getNoOfCopies()+ (product.getNoOfCopy()!=null  ? product.getNoOfCopy():0):product.getNoOfCopy());
							agentReportDetail.setNoOfSheets(agentReportDetail.getNoOfSheets()!=null ? agentReportDetail.getNoOfSheets()+ ((product.getNoOfSheet()!=null  && product.getProductTypeId() == 102) ? product.getNoOfSheet():0):product.getNoOfSheet());
							if(agentReportDetail.getNoOfCopies()!=  null)
							agentReportDetail.setNoOfSheets(agentReportDetail.getNoOfSheets() * agentReportDetail.getNoOfCopies());
						}
						}
													 
							for(OrderTrxnDetail otDetail:otDetails){
								agentReportDetail.setNetAmount(otDetail.getNetAmount());
								agentReportDetail.setAdvance(otDetail.getAdvance());
								agentReportDetail.setCreditBalance(agentReportDetail.getNetAmount());
								if (agentReportDetail.getAdvance() !=null)
								{
								agentReportDetail.setCreditBalance( agentReportDetail.getCreditBalance() - agentReportDetail.getAdvance());
								} 
							}
							if(receiptPayments !=null ){
								
								for(ReceiptPayment receiptPayment:receiptPayments){
									agentReportDetail.setCreditBalance( Math.ceil((agentReportDetail.getCreditBalance() - receiptPayment.getPaidAmount())));
									}
							}
						reportDetails.add(agentReportDetail);
//					}
					 
				}
				}catch(Exception e){
					System.out.println("sdfadsfasdf");
				}
			}
				//cumulative total of each customers balance 
				Double total=0.0;
							
				for (int i=0; i<=reportDetails.size()-1;i++){
					if (i==(reportDetails.size()-1)){
						 reportDetails.get(i).setTotal(total+reportDetails.get(i).getCreditBalance());
				
					}
					else if ((reportDetails.get(i).getCustId()).equals((reportDetails.get(i+1).getCustId()))){
						try{
						 total += reportDetails.get(i).getCreditBalance();
						}
						  catch(Exception e){}
						}
					else   
					{
						 try{
						  reportDetails.get(i).setTotal((total+reportDetails.get(i).getCreditBalance()));
                          total=0.0;
						 }
						 catch(Exception e){}
						 }
				}
		}catch (Exception _exception) {
			logger.error("order and OrderTrxDetail :: " + _exception);
		}
		
		return reportDetails;
	}
	
	
	public List<AgentReportDetail> agentOrdersCorrection(Report report) {
		List<AgentReportDetail> reportDetails = new ArrayList<AgentReportDetail>();
		AgentReportDetail agentReportDetail = new AgentReportDetail();
		int counter=0;
		ReceiptPayment_temp rp;
		Double currentCreditBalance=0.0;
		Double balancePay=0.0;
		 		 
	try{
		for( int j=1001;j<=1281;j++){
			
			report.setCustId(j);
		    Customer customer = new Customer();
		    customer.setCustId(j);
		    currentCreditBalance=0.0;
			currentCreditBalance= orderDAO.getCurrentCreditBalance(customer.getCustId());
			//System.out.println("currentCreditBalance :"+currentCreditBalance);
			List<Order> orders = reportDAO.getAgentOrdersCorrection(report);
			//System.out.println("ORDERS SIZE :"+orders.size());
			//OrderTrxnDetail otDetail= new OrderTrxnDetail();
			
			if(orders!=null && !orders.isEmpty()){
					for(Order order:orders){
						++counter;
						//System.out.println("Counter :"+counter);
						List<OrderTrxnDetail> otDetails= orderDAO.getTransactionByOrderId(order.getOrderId());
						//System.out.println("OrderTrxnDetail SIZE :"+otDetails.size());
						try{
						if(otDetails !=null && !otDetails.isEmpty()){
								List<ReceiptPayment> receiptPayments = paymentDAO.getReceiptPaymentsByOrderId(Long.toString(order.getOrderId()));
								//System.out.println("receiptPayments SIZE :"+receiptPayments.size());
								agentReportDetail = new AgentReportDetail();
								agentReportDetail.setOrderDate(DateUtils.toDate(order.getOrderDate(),true));
								agentReportDetail.setOrderId(String.valueOf(order.getOrderId()));
								agentReportDetail.setCustName(order.getCustName());
								agentReportDetail.setCustId(order.getCustId());
								
								if(otDetails !=null){	// ordertrxndetails . size () > 0
									agentReportDetail.setNetAmount(otDetails.get(0).getNetAmount());
								if (otDetails.get(0).getAdvance() !=null){
									agentReportDetail.setAdvance(otDetails.get(0).getAdvance());
									agentReportDetail.setCreditBalance( otDetails.get(0).getBalance());
								}else{
									  agentReportDetail.setCreditBalance( otDetails.get(0).getNetAmount());
									  agentReportDetail.setAdvance(0.00);
								}
								// currentBalance +=agentReportDetail.getCreditBalance();
								}
							
								if (currentCreditBalance==0){ // NO OUTSTANDING (if currentcreditBalance is 0)
									if(receiptPayments.size()>0 ){
										for(ReceiptPayment receiptPayment:receiptPayments)
											agentReportDetail.setCreditBalance( Math.ceil((agentReportDetail.getCreditBalance() - receiptPayment.getPaidAmount())));
										   // System.out.println("agentReportDetail.setCreditBalance  CCB == 0"+agentReportDetail.getCreditBalance()+" :"+agentReportDetail.getOrderId());
										    
										if(agentReportDetail.getCreditBalance() >0){
											rp = new ReceiptPayment_temp();
											rp.setReceiptId(new Long(1));
											rp.setCreatedDate(DateUtils.toDate(DateUtils.toDate(new Date(),true),true));
											rp.setUpdatedDate(DateUtils.toDate(DateUtils.toDate(new Date(),true),true));
											/*rp.setRefNo( Long.toString(receiptPayments.get(0).getOrderId()));*/
											rp.setRefNo( agentReportDetail.getOrderId());
											rp.setRefTypeId("101");
											rp.setNetAmount(agentReportDetail.getNetAmount());
											rp.setPaidAmount(agentReportDetail.getCreditBalance());
											saveOrUpdate(rp);
										}
									}else{
										rp = new ReceiptPayment_temp();
										rp.setReceiptId(new Long(1));
										rp.setCreatedDate(DateUtils.toDate(DateUtils.toDate(new Date(),true),true));
										rp.setUpdatedDate(DateUtils.toDate(DateUtils.toDate(new Date(),true),true));
										rp.setRefNo( Long.toString(otDetails.get(0).getOrderId()));
										rp.setRefTypeId("101");
										rp.setNetAmount(otDetails.get(0).getNetAmount());
										rp.setPaidAmount(otDetails.get(0).getBalance());
										saveOrUpdate(rp);
									}
								}
								else{	// WITH OUTSTANDING DUR (if currentcreditBalance is > 0)
								//	System.out.println("Counter :"+counter);
									//System.out.println("currentCreditBalance 1  :"+currentCreditBalance); 
								//	System.out.println("Entered  if TRUE");
								//	System.out.println("Before CB:"+agentReportDetail.getCreditBalance());
									if(receiptPayments.size()>0 ){  // CREDIT RECEIPTS IF THERE
										for(ReceiptPayment receiptPayment:receiptPayments)
											agentReportDetail.setCreditBalance( Math.ceil((agentReportDetail.getCreditBalance() - receiptPayment.getPaidAmount())));
										//System.out.println("agentReportDetail.setCreditBalance - direct :"+agentReportDetail.getCreditBalance()+" :"+agentReportDetail.getOrderId());
										if(agentReportDetail.getCreditBalance()>0 ){  // STILL PENDING DUE
											//System.out.println("agentReportDetail.setCreditBalance - direct-1 :"+agentReportDetail.getCreditBalance() );
											if (currentCreditBalance >= agentReportDetail.getCreditBalance()){
												currentCreditBalance = currentCreditBalance - agentReportDetail.getCreditBalance();
											//	System.out.println("Counter :"+counter);
											//	System.out.println("currentCreditBalance 2 :"+currentCreditBalance); 
											}else{
												balancePay = agentReportDetail.getCreditBalance()-currentCreditBalance;
												currentCreditBalance=0.0;
											//	System.out.println("currentCreditBalance Now ---:"+agentReportDetail.getCreditBalance()); 
											//	System.out.println("currentCreditBalance Now ---:"+currentCreditBalance); 
												rp = new ReceiptPayment_temp();
												rp.setReceiptId(new Long(1));
												rp.setCreatedDate(DateUtils.toDate(DateUtils.toDate(new Date(),true),true));
												rp.setUpdatedDate(DateUtils.toDate(DateUtils.toDate(new Date(),true),true));
												rp.setRefNo( Long.toString(otDetails.get(0).getOrderId()));
												rp.setRefTypeId("101");
												rp.setNetAmount(otDetails.get(0).getNetAmount());
												rp.setPaidAmount(balancePay);
												saveOrUpdate(rp);
											}
										} 
																												
									}else{
										if(agentReportDetail.getCreditBalance() > 0){
											if (currentCreditBalance >= agentReportDetail.getCreditBalance()){
												currentCreditBalance -= agentReportDetail.getCreditBalance();
												//System.out.println("currentCreditBalance Now ++++:"+currentCreditBalance); 
												
											}else{
												balancePay = agentReportDetail.getCreditBalance()-currentCreditBalance;
												currentCreditBalance=0.0;
												rp = new ReceiptPayment_temp();
												rp.setReceiptId(new Long(1));
												rp.setCreatedDate(DateUtils.toDate(DateUtils.toDate(new Date(),true),true));
												rp.setUpdatedDate(DateUtils.toDate(DateUtils.toDate(new Date(),true),true));
												rp.setRefNo( Long.toString(otDetails.get(0).getOrderId()));
												rp.setRefTypeId("101");
												rp.setNetAmount(otDetails.get(0).getNetAmount());
												rp.setPaidAmount(balancePay);
												saveOrUpdate(rp);
											}
										}
									}
							}
										
						}	
						}catch(Exception e){}
		                    if (otDetails.size()>0)  			
							if(agentReportDetail !=null && agentReportDetail.getCreditBalance()>0)
							reportDetails.add(agentReportDetail); 
						}
					}
		}
		
				//cumulative total of each customers balance 
				Double total=0.0;
				Double curTotal=0.0;			
				for (int i=0; i<=reportDetails.size()-1;i++){
					if (i==(reportDetails.size()-1)){
						 reportDetails.get(i).setTotal(total+reportDetails.get(i).getCreditBalance());
				         curTotal=reportDetails.get(i).getTotal();
					}
					else if ((reportDetails.get(i).getCustId()).equals((reportDetails.get(i+1).getCustId()))){
						try{
						 total += reportDetails.get(i).getCreditBalance();
						}
						  catch(Exception e){}
						}
					else   
					{
						 try{
						  reportDetails.get(i).setTotal((total+reportDetails.get(i).getCreditBalance()));
                          total=0.0;
                          curTotal=reportDetails.get(i).getTotal();
						 }
						 catch(Exception e){}
						 }
					
				}
			
		}catch (Exception _exception) {
			logger.error("order and OrderTrxDetail Correction :: " + _exception);
		}
		
		return reportDetails;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.studio.service.ReportService#saveOrUpdate(java.lang.Object)
	 */
	public <T> Boolean saveOrUpdate(T t) {
		Boolean isSuccess = true;
		try{
			isSuccess = reportDAO.saveOrUpdate(t);
		}catch (Exception _exception) {
			logger.error("saveOrUpdate" + _exception);
			isSuccess=false;
		}
		return isSuccess;
	}

	/* (non-Javadoc)
	 * @see com.studio.service.ReportService#save(java.lang.Object)
	 */
	public <T> Boolean save(T t) {
		Boolean isSuccess = true;
		try{
			isSuccess = reportDAO.save(t);
		}catch (Exception _exception) {
			logger.error("save" + _exception);
			isSuccess=false;
		}
		return isSuccess;
	}

	/* (non-Javadoc)
	 * @see com.studio.service.ReportService#getBalance(java.util.Date)
	 */
	public BalanceDetails getBalance(Date date) {
		BalanceDetails balanceDetails=null;
		try{
			balanceDetails = reportDAO.getBalance(date);
		}catch (Exception _exception) {
			logger.error("getBalance : " + _exception);
		}
		return balanceDetails;
	}

	/* (non-Javadoc)
	 * @see com.studio.service.ReportService#balance(com.studio.domain.Report)
	 */
	public List<ReportDetail> balance(Report report,String type) {
		List<ReportDetail> details=null;
		try{
			
			
			ReportDetail reportDetail = null;
			details = new ArrayList<ReportDetail>();
			if(type.equals(WorkOrderConstants.OPENING)){
//				BalanceDetails openBalance = reportDAO.getBalance(DateUtils.toDate(DateUtils.beforeDateStr(1)));
				BalanceDetails openBalance = reportDAO.getBalance(DateUtils.beforeDate(report.getFromDate(), 1));
				if(openBalance !=null){
					reportDetail = new ReportDetail();
					reportDetail.setCreditAmount(openBalance.getCashHand());
					reportDetail.setDescription("Cash in Hand");
					reportDetail.setReferenceType("Opening Balance");
					reportDetail.setTrxnType("Cash");
					reportDetail.setTrxnDate(DateUtils.toDate(report.getFromDate(), true));
					details.add(reportDetail);
					reportDetail = new ReportDetail();
					/*reportDetail.setCreditAmount(openBalance.getCashBank());
					reportDetail.setDescription("Cash at Bank");
					reportDetail.setReferenceType("Opening Balance");
					reportDetail.setTrxnType("Bank");
					reportDetail.setTrxnDate(DateUtils.toDate(report.getFromDate(), true));
					details.add(reportDetail);*/
				}
			}
			if(type.equals(WorkOrderConstants.CLOSING)){
				BalanceDetails closeBalance = reportDAO.getBalance(report.getToDate());
				if(closeBalance !=null){
					reportDetail = new ReportDetail();
					reportDetail.setDebitAmount(closeBalance.getCashHand());
					reportDetail.setDescription("Cash in Hand");
					reportDetail.setReferenceType("Closing Balance");
					reportDetail.setTrxnType("Cash");
					reportDetail.setTrxnDate(DateUtils.toDate(report.getToDate(), true));
					details.add(reportDetail);
				/*	reportDetail = new ReportDetail();
					reportDetail.setDebitAmount(closeBalance.getCashBank());
					reportDetail.setDescription("Cash at Bank");
					reportDetail.setReferenceType("Closing Balance");
					reportDetail.setTrxnType("Bank");
					reportDetail.setTrxnDate(DateUtils.toDate(report.getToDate(), true));
					details.add(reportDetail);*/
				}
			}
		}catch (Exception _exception) {
			logger.error("getBalance : " + _exception);
		}
		return details;
	}

	/* (non-Javadoc)
	 * @see com.studio.service.ReportService#smsData(com.studio.domain.Report)
	 */
	
	/*public SmsData smsData(Report report) {
		return smsData(report, false);
	}*/
	
	public SmsData smsData(Report report) {
		SmsData data = new SmsData();
		try{
			List<Order> orders = reportDAO.getOrdersList(report);
			
			if(orders!=null && !orders.isEmpty()){
				data.setNoOfOrders(orders.size());
				for(Order order:orders){
					if(order.getStatus()==105){
						data.setDelivered(data.getDelivered()!=null?data.getDelivered()+1:1);
//						break;
					}
					List<JobAllocation> jobAllocations = orderDAO.getJobAllocationByOrderId(order.getOrderId());
					if(jobAllocations!=null && !jobAllocations.isEmpty()){
						Boolean dCompleted = false;
						for(JobAllocation job:jobAllocations){
								
							if(job.getId().getDepotId()==101 && job.getStatusId() ==104)
								dCompleted=true;
							
							if(dCompleted && job.getId().getDepotId() == 106 && job.getStatusId()==102){
								data.setInProgress(data.getInProgress()!=null ? data.getInProgress()+1:1);
								break;
							}
							
							if(job.getId().getDepotId() == 101 && job.getStatusId()==102){
								data.setUnProcessed(data.getUnProcessed()!=null ? data.getUnProcessed()+1:1);
								break;
							}
						}
					}
				}
			}
		}catch (Exception _exception) {
			logger.error(_exception);
		}
		return data;
	}

	/* (non-Javadoc)
	 * @see com.studio.service.ReportService#saveClosingBalance(java.util.List)
	 */
	public Boolean saveClosingBalance(List<ReportDetail> reportDetails) {

		Boolean isBalanceSaved = false;
		try{
		if(reportDetails !=null && !reportDetails.isEmpty()){
			Double creditAmount=0.00;
			Double debitAmount=0.00;
			Double openingAmount = 0.00;
			for(ReportDetail detail :reportDetails){
				if(!detail.getReferenceType().equals("Opening Balance")){
					if(detail.getCreditAmount()!=null)
					creditAmount += detail.getCreditAmount();
					if(detail.getDebitAmount()!=null)
					debitAmount += detail.getDebitAmount();
				}else {
					openingAmount = detail.getCreditAmount();
				}
			}
			String fromDate = DateUtils.toDate(new Date(),true);
			BalanceDetails bDetails = new BalanceDetails();
			bDetails.setCashHand(Double.valueOf((creditAmount + openingAmount) - debitAmount));
			bDetails.setCreatedDate(DateUtils.toDate(DateUtils.toDate(new Date(),true),true));
			BalanceDetails balanceDetails = getBalance(DateUtils.toDate(fromDate,true));
			if(balanceDetails == null){
				save(bDetails);
			}else{
				balanceDetails.setCashHand(bDetails.getCashHand());
				saveOrUpdate(balanceDetails);
			}
			isBalanceSaved=true;
		}
		}catch (Exception _exception) {
			logger.error("saveClosingBalance" + _exception.getMessage());
			isBalanceSaved = false;
		}
		
		return isBalanceSaved;
	}

	/* (non-Javadoc)
	 * @see com.studio.service.ReportService#getOpeningBalance()
	 */
	public ReportDetail getOpeningBalance() {
		BalanceDetails balanceDetail = null;
		ReportDetail reportDetail = null; 
		try{
			balanceDetail = reportDAO.getLastBalance();
			if(balanceDetail!=null){
				if(DateUtils.toDate(balanceDetail.getCreatedDate(),true).equals(DateUtils.toDate(new Date(),true))){
					balanceDetail = reportDAO.getLastPreviousBalance();
				}
				reportDetail = new ReportDetail();
				reportDetail.setCreditAmount(balanceDetail.getCashHand());
				reportDetail.setDescription("Cash in Hand");
				reportDetail.setReferenceType("Opening Balance");
				reportDetail.setTrxnType("Cash");
				reportDetail.setTrxnDate(DateUtils.toDate(new Date(), true));
				
			}
		}catch (Exception _exception) {
			logger.error(_exception.getMessage());
			
		}
		return reportDetail;
	}
	
	/* (non-Javadoc)
	 * @see com.studio.service.ReportService#getOpeningBalance()
	 */
	public ReportDetail getOpeningBalancewithDate(String fromDate) {
		BalanceDetails balanceDetail = null;
		ReportDetail reportDetail = null; 
		try{
			//System.out.println("Date...for "+fromDate);
			balanceDetail = reportDAO.getLastBalancewithDate(fromDate);
			//System.out.println("cash inhand  :"+balanceDetail.getCashHand());
			
			if(balanceDetail!=null){
				/*	if(DateUtils.toDate(balanceDetail.getCreatedDate(),true).equals(DateUtils.toDate(new Date(),true))){
					balanceDetail = reportDAO.getLastPreviousBalancewithDate();
				}*/
				reportDetail = new ReportDetail();
				reportDetail.setCreditAmount(balanceDetail.getCashHand());
				reportDetail.setDescription("Cash in Hand");
				reportDetail.setReferenceType("Opening Balance");
				reportDetail.setTrxnType("Cash");
				reportDetail.setTrxnDate(fromDate);
				
			}
		}catch (Exception _exception) {
			logger.error(_exception.getMessage());
			
		}
		return reportDetail;
	}
	
	
	
	
	// for customer report customer outstandingasting 
	
	//mbrsusmmary report
@SuppressWarnings("null")
public List<ReportDetail> mbrsummary(String fromDate, String toDate) {
	
		List<ReportDetail> ReportDetails =new ArrayList<ReportDetail>();
		   
		try{
			ReportDetail detail= new ReportDetail(); 
			List<Voucher> vouchers = reportDAO.getActualPayments(fromDate,  toDate);
			List<Purchase> purchases = reportDAO.getActualPurchase( fromDate,  toDate);
			List<Receipt> receipts = reportDAO.getActualReceipts( fromDate,  toDate);
			List<OrderTrxnDetail> sales = reportDAO.getActualSales( fromDate,  toDate);
			List<Product> sheets = reportDAO.getActualSheets( fromDate,  toDate);
			//List<OrderTrxnDetail> cancelorders = reportDAO.getCancelAmount(fromDate, toDate);
			
			 
			   DateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd");			   
			   fromDate = fromDate.substring(0,10);
			   toDate = toDate.substring(0,10);
			   toDate = toDate.substring(0,10);
			   Vector vec = getDates(fromDate, toDate);
			   SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			   toDate = orderDAO.getyyyymmdd(toDate);
			   fromDate = orderDAO.getyyyymmdd(fromDate);
			   
			  // System.out.println("vec size : " + vec.size());
			
			   for (int ix=0; ix<vec.size(); ix++){
				   String stdt = vec.get(ix).toString();
				  // System.out.println("stdt  :"+stdt);
				   double payamount=0.0;
				   double amtpaid=0.0;
				   double rpayamount=0.0;
				   double netamount=0.0;
			   	   double advance=0.0;
			   	   int noofsheets=0;
			   	   double cancelamt=0.0;
			   	   double canceladv=0.0;
			   	   int cancelcount=0;
			   	   int salecount=0;
			   	   double creditsale=0.0;
			   	   double purchaseinvoiceamount=0.0;
			   	   double cashpurchase=0.0;
			   	   double creditpurchase=0.0;
			   	   double totalpurchase=0.0;
			   	   
				 detail= new ReportDetail();
				  String strDate = orderDAO.getyyyymmdd(stdt);
				 // System.out.println("strDate  :"+ strDate);
				  String  d1=reportDAO.getddmmyyyy(strDate);
				 // System.out.println(d1);
				   detail.setTrxnDate(d1); 
				     
				   if (vouchers !=null && !vouchers.isEmpty()){
					   try {  
						   
						   for (Voucher voucher: vouchers){
							   java.util.Date date = voucher.getVoucherDate(); 
							   String date1 = formatter.format(date);
							  // System.out.println("Date1  :"+ date1);		   
							   if ((strDate).equals(date1)){
								  		payamount+=voucher.getPayAmount();
							   	 	}
				   			  }
						   detail.setPayAmount(payamount);
				   		  }
				   		catch(Exception e){	}
				   	}else{
				   		detail.setPayAmount(0.0);
				   	}
				   	
				   if (purchases !=null && !purchases.isEmpty()){
					   try { 
						  //System.out.println("I am in purchase ");
						   for (Purchase purchase: purchases){
							   java.util.Date date = purchase.getCreatedDate(); 
							   String date1 = formatter.format(date);
							   /*System.out.println("date1 :"+date1);
							   System.out.println("strDate :"+strDate);*/
//				   	   			if (((strDate).equals(date1)) && purchase.getAmountPaid() !=null){
//				   	   				amtpaid+=purchase.getAmountPaid();
//				   	   				}else 
//				   	   				{amtpaid+=0.0;}
							   if ((strDate).equals(date1)){
								
								   cashpurchase+=purchase.getAmountPaid();
								   creditpurchase+=purchase.getBalancePayable();
								   totalpurchase+=purchase.getNetPayable();
				   	   				}else 
				   	   				{amtpaid+=0.0;
				   	   				 cashpurchase+=0.0;
				   	   			     creditpurchase+=0.0;
				   	   			     totalpurchase+=0.0;
				   	   				}
				   	   			
						   detail.setDebitAmount(cashpurchase);	 
						   detail.setCreditPurchase(creditpurchase);
						   detail.setTotalpurchase(totalpurchase);
						   
						  /* if (((strDate).equals(date1)) && purchase.getInvoiceAmount()>0){
			   	   				purchaseinvoiceamount+=purchase.getInvoiceAmount();
			   	   				}else {purchaseinvoiceamount+=0.0;}
						   detail.setTotalpurchase(purchaseinvoiceamount);*/
						   detail.setTotalpurchase(totalpurchase);
			   	   			}
				   		 }
					   		catch(Exception e){	}
				   		
			   		     	
				   	}else{
				   		detail.setDebitAmount(0.0);
				   	 detail.setTotalpurchase(0.0);
				   	}
				   
				   if (receipts !=null && !receipts.isEmpty()){
					   try {  
						//   System.out.println("I am in receipt ");
						   for (Receipt receipt: receipts){
							   java.util.Date date = receipt.getReceiptDate(); 
							   String date1 = formatter.format(date);
								if ((strDate).equals(date1)){
									rpayamount+=receipt.getPayAmount();
								}
							}
					   detail.setCreditAmount(rpayamount);	
				   }catch(Exception e){	}
					  
				   	}else{
				   		detail.setCreditAmount(0.0);
				   	}
				   	
				  /* if (cancelorders !=null && !cancelorders.isEmpty()){
					 try {  
						 //System.out.println("I am in cancel orders ");
						   for(OrderTrxnDetail order: cancelorders){
							   java.util.Date date = order.getCreatedDate(); 
							   String date1 = formatter.format(date);
							   if ((strDate).equals(date1)){
								   cancelamt+=order.getNetAmount();
								   canceladv+=order.getAdvance();
								   cancelcount++;
								   }
							   }     
				   		}
				   		catch(Exception e){
				   			//detail.setPayAmount(0.0);
				   		}
				   					   		
				   	} */
				   	
				   if (sales !=null && !sales.isEmpty()){
						try { 
							//System.out.println("I am in  sales ");
						   for (OrderTrxnDetail otd: sales){
							   java.util.Date date = otd.getCreatedDate(); 
							   String date1 = formatter.format(date);
							  
							   	if ((strDate).equals(date1)){
							   		if(otd.getBalance() !=null)
							   			creditsale +=otd.getBalance();
							   	 if (otd.getNetAmount()!=null )
							   		netamount+=otd.getNetAmount();
							   	if (otd.getAdvance()!=null )
							   		advance+=otd.getAdvance();
							   		salecount++;
							   		}else{
							   			netamount+=0;advance+=0;salecount +=0;creditsale +=0;
							   		}
							   	}
						  //  advance =(advance-canceladv)+cancelcount;
						   // netamount =(netamount-cancelamt)+advance;
			   		      	detail.setInvoiceAmount(netamount);
			   		        detail.setNoOfBills(salecount);
			   		        detail.setCashsale(advance);
			   		        detail.setCreditsale(creditsale);
			   		        
						}
				   		catch(Exception e){}				      
				   	} 
				   
				   if (sheets !=null  && !sheets.isEmpty()){
						try {  noofsheets=0;
						       int halfsheet=0;
							//System.out.println("I am in  products ");
							for (Product products: sheets){
								java.util.Date date = products.getCreatedDate(); 
								String date1 = formatter.format(date);
								if ((strDate).equals(date1) && products.getNoOfSheet() != null ){
									// to divde by 2  for sheets 8*24 and 12*18
									if((products.getSize()==105)||(products.getSize()==107)){
										halfsheet =(int)(Math.ceil(products.getNoOfSheet()/2.0));	
										noofsheets += halfsheet * products.getNoOfCopy();
									}else
									{
									noofsheets += (products.getNoOfSheet() * products.getNoOfCopy());
									}
									}
								}
							detail.setDays(noofsheets);
						}
						catch(Exception e){  }
						
				   	} 
				   if (detail !=null)	{			  			
				   ReportDetails.add(detail);
				   //System.out.println("reportDetails.size():"+ReportDetails.size());
				   }
				}
			   }
			  		
		      catch (Exception _exception) {
			  _exception.printStackTrace();
			  logger.error("MBR Summary Report :" + _exception);
		}
		 
		return ReportDetails;
	}

public List<ReportDetail> srpReport(String fromDate, String toDate, String type) {
	
	List<ReportDetail> ReportDetails =new ArrayList<ReportDetail>();

	try{
		ReportDetail detail= new ReportDetail(); 
		
		 
		   DateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd");			   
		     fromDate = fromDate.substring(0,10);
		   toDate = toDate.substring(0,10);
		   toDate = toDate.substring(0,10);
		   
		
		  Vector vec = getDates(fromDate, toDate);
		   SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
				   	   
			  detail= new ReportDetail();
			
			if (type.trim().equals("Sales")){
			//	System.out.println("from Date:"+fromDate);
			//	System.out.println("ToDate :"+toDate);
				List<Order> orders = reportDAO.getOrders(fromDate, toDate);
			//	System.out.println("Orders count:"+orders.size());
				fromDate=reportDAO.getddmmyyyy(fromDate);
				toDate=reportDAO.getddmmyyyy(toDate);
				List<OrderTrxnDetail> sales = reportDAO.getActualSales( fromDate,  toDate);	
			//	System.out.println("Saless count:"+sales.size());
				List<PaymentType> paytype = metaDAO.getPaymentTypes();
				 if (orders !=null && !orders.isEmpty()){
					 try {
					     for(Order order: orders){
					       	 detail= new ReportDetail();
					       	   if (sales !=null && !sales.isEmpty()){
					       	
					       	       for (OrderTrxnDetail otd: sales){
					            	    if (order.getOrderId() == otd.getOrderId()){
					            	    	/*System.out.println("order.getOrderId() :"+order.getOrderId() +"otd.getOrderId():" +otd.getOrderId());*/
					                    	detail.setTrxnDate(order.getStrOrderDate().substring(0,10));
					            	    	/*detail.setTrxnDate(order.getStrOrderDate().);*/
					                       	detail.setCustomerName(order.getCustName());
					                    	detail.setOrderId(order.getOrderId());
					                    	detail.setCreditAmount(otd.getNetAmount());
					            	       	if (otd.getBalance().doubleValue() == otd.getNetAmount().doubleValue() )
					                    		 detail.setTrxnType("Credit");
					                           	else {		
					                           		for (PaymentType ptype:paytype){
					                    				  if (ptype.getId()==Integer.parseInt(otd.getPymtType()))
					                    					  detail.setTrxnType(ptype.getName());
					                    					 // System.out.println("otd.getPymtType():"+otd.getPymtType()+ptype.getName());
					                           		}	  
					                    		 }
					            	    	}
					            	      }
					                  
					       		
					     		}
					       	 if (detail !=null )			  			
								   ReportDetails.add(detail);
					     }
					 	}catch(Exception e){}				      
				 	} 
			} 
			
			if (type.trim().equals("Receipts")){
			//	System.out.println("I am from Receipts");
				fromDate = reportDAO.getddmmyyyy(fromDate);
				toDate = reportDAO.getddmmyyyy(toDate);
			    List<Receipt> receipts = reportDAO.getActualReceipts( fromDate,  toDate);
			    List<Customer>custname= customerDAO.getCustomerInformation();
			    List<PaymentType> paytype = metaDAO.getPaymentTypes();
				if (receipts !=null && !receipts.isEmpty()){
					   try {  
						   for (Receipt receipt: receipts){
							   detail= new ReportDetail();
							   /*java.util.Date date = receipt.getReceiptDate(); 
							   String date1 = formatter.format(date);*/
								 detail.setTrxnDate(receipt.getStrReceiptDate());
								 detail.setCreditAmount(receipt.getPayAmount());
								 detail.setOrderId(receipt.getReceiptId());
								 for (PaymentType ptype:paytype)
                   				  if (ptype.getId() == receipt.getPaymentId()){
                   				//  	System.out.println("receipt.getPaymentId()"+receipt.getPaymentId() +"ptype.getId():"+ptype.getId() );
                   				  
                   						  detail.setTrxnType(ptype.getName());
								 }
								 for(Customer customer: custname ){
									 if (customer.custId==receipt.getCustId()){
										 detail.setCustomerName(customer.getFirstName());
									 }else if(receipt.getCustId() == 0)
										 detail.setCustomerName("Miscellaneous A/c.");
									 }
								 if (detail !=null)				  			
									   ReportDetails.add(detail);
								
						     
							}
					   }catch(Exception e){	}
					  
				   	}
			}
			
			
			if (type.trim().equals("Payments")){
				fromDate = reportDAO.getddmmyyyy(fromDate);
				toDate = reportDAO.getddmmyyyy(toDate);
				List<Voucher> vouchers = reportDAO.getActualPayments(fromDate,  toDate);
				List<Supplier> supplier = customerDAO.getSupplierInformation();
				List<ExpenseType> exptype=paymentDAO.getExpenseType(); 
				 List<PaymentType> paytype = metaDAO.getPaymentTypes();
				if (vouchers !=null && !vouchers.isEmpty()){
					
					   try {  
						   
						   for (Voucher voucher: vouchers){ 
							   java.util.Date date = voucher.getVoucherDate();
							   String date1 = formatter.format(date);
							   date1=reportDAO.getyyyymmdd(date1,"-");
						
							   detail= new ReportDetail();
							   detail.setTrxnDate(voucher.getStrVoucherDate().substring(0,10));
							   detail.setCreditAmount(voucher.getPayAmount());
							   detail.setOrderId(voucher.getVoucherId());
							   if(voucher.getExpenseId() < 10000){
							  	   	for(Supplier sup : supplier){
								    		if ( sup.getSupId() == voucher.getExpenseId())
								    			detail.setCustomerName(sup.getName());
								   	}
							   }
							   else{
								   for (ExpenseType exptypes: exptype){
									    if( exptypes.getId()==voucher.getExpenseId())
									    	detail.setCustomerName(exptypes.getName());
								   }
							   }
							  for(PaymentType paytypes: paytype){
								  if(paytypes.getId()==voucher.getPaymentId())
									  detail.setTrxnType(paytypes.getName());
							  }
							  
							  if (detail !=null)				  			
								   ReportDetails.add(detail);
												   
							}
					   	}  catch(Exception e){	}	   
				 }
			 }
			
			if (type.trim().equals("Cancelled")){
			//	System.out.println("fromDate :"+fromDate);
			//	System.out.println("ToDate :"+toDate);
				fromDate = reportDAO.getddmmyyyy(fromDate);
				toDate=reportDAO.getddmmyyyy(toDate);
				List<OrderTrxnDetail> cancelorders = reportDAO.getCancelAmount(fromDate, toDate);
				List<Order> orders = reportDAO.getSRPCancelOrders(fromDate, toDate);
				//List<Customer> customer = customerDAO.getCustomerInformation();
				//List<ExpenseType> exptype=paymentDAO.getExpenseType(); 
			//	System.out.println("OrderTrxnDetail:"+cancelorders.size());
			//	System.out.println(" orders===:"+orders.size());

				 List<PaymentType> paytype = metaDAO.getPaymentTypes();
				if (orders !=null && !orders.isEmpty()){
					 try {  
						   for (Order order: orders){ 
							   java.util.Date date = order.getOrderDate();
							   String date1 = formatter.format(date);
							   date1=reportDAO.getyyyymmdd(date1,"-");
							 
							   detail= new ReportDetail();
							   detail.setTrxnDate(order.getStrOrderDate().substring(0,10));
							   detail.setCustomerName(order.getCustName());
							   detail.setOrderId(order.getOrderId());
							    for (OrderTrxnDetail otd: cancelorders ){
							        if(otd.getOrderId()==order.getOrderId()){
							        	detail.setCreditAmount(otd.getNetAmount());
							        	/*detail.setOrderId(otd.getOrderId());*/
							        }
							   		   	for(PaymentType paytypes: paytype){
							   		   		if(paytypes.getId()==Integer.parseInt(otd.getPymtType()))
							   		   			detail.setTrxnType(paytypes.getName());
										 }
							    }
						  					  
							  if (detail !=null)				  			
								   ReportDetails.add(detail);
						   }
						} catch(Exception e){	}	   
				}
			}
			
			if (type.trim().equals("Discount")){
	
				List<Order> orders = reportDAO.getOrders(fromDate, toDate);
				// System.out.println("Discount orders count:"+orders.size());
	     		 List<OrderTrxnDetail> discount=null;
				
				if (orders !=null && !orders.isEmpty()){
					 try {  
						   for (Order order: orders){ 
							   detail= new ReportDetail();
							   java.util.Date date = order.getOrderDate();
							   String date1 = formatter.format(date);
							   date1=reportDAO.getyyyymmdd(date1,"-");
							//   System.out.println("order count:"+orders.size());
							   discount = reportDAO.getOrderTrxn(order.getOrderId());
						//	   System.out.println("discount now() :"+discount.size());
							   
							 if(discount !=null && discount.size()!=0)
								 if (discount.get(0).getDiscount() !=null && discount.get(0).getDiscount()!=0 ){
								  	detail.setOrderId(order.getOrderId());
							 	  	detail.setTrxnDate(order.getStrOrderDate().substring(0,10));
							 	  	detail.setCustomerName(order.getCustName());
							 	  	detail.setTrxnType("N/A");
							 		detail.setCreditAmount(discount.get(0).getDiscount());
							 		ReportDetails.add(detail);
								 }
							  /*if (detail !=null){				  			
								   ReportDetails.add(detail);
								  System.out.println("ReportDetails now() :"+ReportDetails.size());
							  }*/
							   
							}
					 } catch(Exception e){	}	   
				}
			}
			
		} catch(Exception e){	}	
		//   System.out.println("ReportDetails.size() :"+ReportDetails.size());
		  /* for (int i=0;i<ReportDetails.size();i++){
			   System.out.println("order date" + ReportDetails.get(i).getTrxnDate());
			   System.out.println("customer name" + ReportDetails.get(i).getCustomerName());
			   System.out.println("order type" + ReportDetails.get(i).getTrxnType());
			   System.out.println("order no" + ReportDetails.get(i).getOrderId());
			   System.out.println("amount" + ReportDetails.get(i).getCreditAmount());
		   }*/
		return ReportDetails;
}

public ReportDetail dashboardOrderStatus(String fromDate, String toDate ){
	ReportDetail detail= new ReportDetail(); 
	try{
	     //  System.out.println("FromDate :"+fromDate+"==="+"Todate :"+toDate);
		   DateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd");			   
		   fromDate = reportDAO.getddmmyyyytoyyyymdd(fromDate);
		   toDate = reportDAO.getddmmyyyytoyyyymdd(toDate);
		  	   
		
		 /* Vector vec = getDates(fromDate, toDate);*/
		   SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
				
				List<Order> orders = reportDAO.getOrders(fromDate, toDate);
				List<Order> cancelledorders = reportDAO.getTodaysCancelledOrders(fromDate, toDate);
				List<Order> deliveredorders = reportDAO.deliveredorders(fromDate, toDate);
							 
				 detail.setBillNo(new Long(orders.size()));
				 detail.setDays(cancelledorders.size());
				 detail.setCustId(new Long(deliveredorders.size()));
	}catch(Exception e){}
			return detail;	
}



public Vector getDates(String fromDate, String toDate) throws ParseException{
	//List<Date> dates = new ArrayList<Date>();
	Vector dates = new Vector();
	Vector datee = new Vector();
	String str_date =fromDate;
	String end_date =toDate;
												
DateFormat formatter ; 

formatter = new SimpleDateFormat("dd/MM/yyyy");
Date  startDate = formatter.parse(str_date); 
Date  endDate = formatter.parse(end_date);
long interval = 24*1000 * 60 * 60; // 1 hour in millis
long endTime =endDate.getTime() ; // create your endtime here, possibly using Calendar or Date
long curTime = startDate.getTime();
while (curTime <= endTime) {
    dates.add(new Date(curTime));
    curTime += interval;
}
for(int i=0;i<dates.size();i++){
    Date lDate =(Date)dates.get(i);
    String ds = formatter.format(lDate);
    datee.add(ds);
  //  System.out.println(" Date is ..." + ds);
}

return datee;
}



   public  void Backupdbtosql() {
      // 	System.out.println("from service implementation");
    	Process p = null;
    	 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy-HHmm");  
    	 LocalDateTime now = LocalDateTime.now();  
    	 String fname=dtf.format(now);
    	 System.out.println(fname);  
        try {
            Runtime runtime = Runtime.getRuntime();
           
           p = runtime.exec("\"C:\\Program Files\\MySQL\\MySQL Server 5.5\\bin\\mysqldump\" -uroot -ppassword123  --add-drop-database -B studio -r  \"D:\\backup\\"+fname+".sql");
           System.out.println("Backup in progress...........!");        
           
            int processComplete = p.waitFor();
            sendDBBackupEmail(fname); 
            if (processComplete == 0) {
                System.out.println("Backup created successfully!");

            } else {
            	System.out.println("Could not create the backup");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
}
   
   private void sendDBBackupEmail(String fname){
		//System.out.println("email  :"+email);
		if (fname != null){ 
			System.out.println("Hello from email block");
			String emailSubjectTxt = "Daily DB Backup Reg.";
			String emailMsgTxt = "\nDear Customer, Your Database Backup attached here\n";
			/*emailMsgTxt = emailMsgTxt + "hereby attaching your Invocie No." +orderId;
			emailMsgTxt = emailMsgTxt + ". Kindly pay the balance Rs." +balance; 
			emailMsgTxt = emailMsgTxt + " while collecting the same.\n";
			emailMsgTxt = emailMsgTxt + "for more details, kindly contact Our RECEPTION No. 7010603123 - CASH COUNTER No. 9865161234 - DELIVERY No. 8760241234  ";*/
			//emailMsgTxt = emailMsgTxt + "DATE FROM : \n";
			//...................................................
			//String[] emailList = {email};
			//...................................................
			String[] emailList = {"svdpbackup@gmail.com"};
			//...................................................
			try
			{
				String path=env.getProperty("backup"); 
			new EmailService().mailing(emailMsgTxt, emailSubjectTxt, "svdppanruti@gmail.com", emailList, "", "", path+ fname+".sql", false);
			//new EmailService().mailing("MESSAGETEXT", "SUBJECTTEXT", "vaishnavidigital2@gmail.com", emailList, "", "", "NOATTACHMENT", false);
			//JOptionPane.showMessageDialog(null,"Mail Sent Successfully !");
			}
			catch(Exception e){ e.printStackTrace();  }
			//#####################################################################################
			 }	
		}

public  void RestoreDB() {
	
	String dbUserName="root";
	String dbPassword="root";
	String source="\\d:\\backup\\test1.sql";
String[] restoreCmd = new String[]{"mysql ", "--user=" + dbUserName, "--password=" + dbPassword, "-e", "source " + source};

Process runtimeProcess;
try {

    runtimeProcess = Runtime.getRuntime().exec(restoreCmd);
    int processComplete = runtimeProcess.waitFor();

    if (processComplete == 0) {
        System.out.println("Restored successfully!");
    } else {
        System.out.println("Could not restore the backup!");
    }
} catch (Exception ex) {
    ex.printStackTrace();
}
}

        public String getddmmyyyy(String st)
        {
        	//System.out.println("STTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT "+st);
        		String sym = "/";
        		if(st.indexOf("-") !=-1) {
        			sym = "-";
        		}
          String dd = st.substring(st.length() - 2, st.length());
          String yyyy = st.substring(0, st.indexOf(sym));
          String mm = st.substring(st.indexOf(sym) + 1, st.lastIndexOf(sym));
          return dd + "/" + mm + "/" + yyyy;
        }
        }


	


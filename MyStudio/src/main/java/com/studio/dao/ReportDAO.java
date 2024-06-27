package com.studio.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
//import org.hibernate.Query;
import org.hibernate.Session;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.studio.domain.BalanceDetails;
import com.studio.domain.Order;
import com.studio.domain.OrderTrxnDetail;
import com.studio.domain.Product;
import com.studio.domain.Purchase;
import com.studio.domain.Receipt;
import com.studio.domain.Report;
import com.studio.domain.ReportDetail;
import com.studio.domain.Voucher;
import com.studio.utils.DateUtils;
import com.studio.utils.Utils;


@Repository
@Transactional
@EnableTransactionManagement
public class ReportDAO {
  @PersistenceContext
  EntityManager entityManager;
  Logger logger = Logger.getLogger(ReportDAO.class);
  
  	
	  public List<OrderTrxnDetail> getOrders(Report report){
		return getOrders(report, null);
	  }
	  
	  public List<OrderTrxnDetail> getDailyOrders(Report report){
		// System.out.println("hello from Daily orders");
		 String fromDate = report.getStrFromDate();
		  String toDate = report.getStrToDate();
		  fromDate =  getddmmyyyytoyyyymdd(fromDate);
		  toDate =getddmmyyyytoyyyymdd(toDate); 
		//  System.out.println("from   ----date"+fromDate);
		//  System.out.println("Tpo d=----- date"+toDate);
		  //OrderTrxnDetail orderTrxn = new OrderTrxnDetail();
		  List<OrderTrxnDetail> orderTrxn = new ArrayList<OrderTrxnDetail>();
		  List<OrderTrxnDetail> orderTrxnDetail = new ArrayList<OrderTrxnDetail>();
		  
		  List<Order> todayOrders = new ArrayList<Order>();
		  
		  
		  try{
			  if(report.getFromDate()!=null && report.getToDate()!=null){
				 Query query = null;
				 query = entityManager.createNativeQuery("select * from orders o where substr(o.order_date,1,10) =:fromDate and o.status!='106'",Order.class);
				 if(query!=null){
					  query.setParameter("fromDate", fromDate);
					  todayOrders = query.getResultList();
					 // System.out.println("Todays orders.size():"+todayOrders.size());
				    }
			  }
			  List<Long> orderIds = Utils.getOrdersIds(todayOrders);
								 	  	
			  if(fromDate !=null && toDate!=null){
				 Query query = null;
				 for(int i=0;i<orderIds.size();i++){
					 orderTrxn = null;
					 query = entityManager.createNativeQuery("select * from order_trxn_detail o where o.created_date =:fromDate and o.order_id=:orderIds",OrderTrxnDetail.class);
					 if(query!=null){
						 query.setParameter("fromDate", fromDate);
						 query.setParameter("orderIds", orderIds.get(i));
						 orderTrxn = query.getResultList();
						 orderTrxnDetail.add(orderTrxn.get(0));
						// System.out.println("Todays orderTrxnDetail.size():"+orderTrxnDetail.size());
					 }
			  }
			  }
			  
		 /* StringBuffer wClause = new StringBuffer();
			  if(report.getFromDate()!=null && report.getToDate()!=null){
				  wClause.append("o.createdDate BETWEEN :startDate and :endDate");
				  System.out.println("inside..1");
			  }
						  
			  logger.info("where clause to search order transaction detail ::: " + wClause.toString());
	
			  TypedQuery<OrderTrxnDetail> query = null;
			  if(!StringUtils.isEmpty(wClause.toString().trim())){
				  StringBuffer sQuery = new StringBuffer("SELECT o FROM OrderTrxnDetail o WHERE ");
				  sQuery.append(wClause);
				  query = entityManager.createQuery(sQuery.toString(), OrderTrxnDetail.class);
				  System.out.println("inside..3");
				  if(report.getFromDate() !=null && report.getToDate()!=null){
					  query.setParameter("startDate", report.getFromDate());
					  query.setParameter("endDate" , report.getToDate());
				  }
				 			  }
			  if(query!=null)
				  orders = query.getResultList();
			  System.out.println("Orders :"+orders.size());*/
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  return orderTrxnDetail;
	  }
	  
	  
		
	  public List<OrderTrxnDetail> getOrders(Report report,List<Long> orderIds){
		  List<OrderTrxnDetail> orders = new ArrayList<OrderTrxnDetail>();
		  try{
			  if (orderIds.size()==0)
				  return orders;
			  StringBuffer wClause = new StringBuffer();
			  if(report.getFromDate()!=null && report.getToDate()!=null){
				  wClause.append("o.createdDate BETWEEN :startDate and :endDate");
				//  System.out.println("inside..1");
			  }
			  System.out.println("orderIds size ; "+orderIds.size());
			 
			  if(orderIds!=null && !orderIds.isEmpty()){
				  if(wClause.length() >0)
					  wClause.append(" and ");
				  wClause.append("o.orderId in:orderIds");  
				 //  System.out.println("inside..2");
			  }
			  
			  logger.info("where clause to search order transaction detail ::: " + wClause.toString());
	
			  TypedQuery<OrderTrxnDetail> query = null;
			  if(!StringUtils.isEmpty(wClause.toString().trim())){
				  StringBuffer sQuery = new StringBuffer("SELECT o FROM OrderTrxnDetail o WHERE ");
				  sQuery.append(wClause);
				  query = entityManager.createQuery(sQuery.toString(), OrderTrxnDetail.class);
				//  System.out.println("inside..3");
				  if(report.getFromDate() !=null && report.getToDate()!=null){
					  query.setParameter("startDate", report.getFromDate());
					  query.setParameter("endDate" , report.getToDate());
				  }
				  if(orderIds!=null && !orderIds.isEmpty()){
					  query.setParameter("orderIds" , orderIds);
					//  System.out.println("inside..4");
				  }
			  }
			  if(query!=null)
				  orders = query.getResultList();
			 // System.out.println("Orders :"+orders.size());
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  return orders;
	  }
	  
	 /* public List<OrderTrxnDetail> getOrdersForCancelOrder(Report report,List<Order> orders){
		  List<OrderTrxnDetail> orderTrxnDetail = new ArrayList<OrderTrxnDetail>();
		  Long orderno =null;
		   OrderTrxnDetail otd = new OrderTrxnDetail();
		  try{
			  if (orders.size()==0)
				  return orderTrxnDetail;
			  int i = 0;
			  for(Order order:orders){
				  System.out.println("i1111:"+i);
				  otd = new OrderTrxnDetail();
				  System.out.println("i2222:"+i);
				  orderno=order.getOrderId();
				  System.out.println("i3333:"+i);
				  Query query= entityManager.createNativeQuery("select * from order_trxn_detail where order_id=:orderId",OrderTrxnDetail.class);
				  if(query !=null){
					  query.setParameter("orderId",orderno );
					  otd = (OrderTrxnDetail) query.getResultList();
					  orderTrxnDetail.add(otd);
				  }
			  }
					  
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  return orderTrxnDetail;
	  }
	  */
	  
	  
	  
	  public List<OrderTrxnDetail> getOrdersSum(Report report,List<Long> orderIds){
		  List<OrderTrxnDetail> orders = new ArrayList<OrderTrxnDetail>();
		  OrderTrxnDetail neworder=new OrderTrxnDetail();
		  Double netamount =0.00;
		  Double advance =0.00;
		  try{
			  if (orderIds.size()==0)
				  return orders;
			  Query query = null;
			  query = entityManager.createNativeQuery("SELECT sum(o.net_amount)FROM order_trxn_detail o "
					  		+ "WHERE o.order_id in:orderIds");
				 if(query!=null){
				    query.setParameter("orderIds" , orderIds);
			         netamount = (Double) query.getSingleResult();
			         neworder.setNetAmount(netamount);
				 }
				 else{
					 neworder.setNetAmount(0.00);
				 }
				 query=null;
				 query = entityManager.createNativeQuery("SELECT sum(o.advance)FROM order_trxn_detail o "
					  		+ "WHERE o.order_id in:orderIds");
				 if(query!=null){
				    query.setParameter("orderIds" , orderIds);
			        advance = (Double) query.getSingleResult();
			        neworder.setAdvance(advance);
				 }
				 else{
					 neworder.setAdvance(0.00); 
				 }
				  
				 
				 orders.add(neworder);
				  
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		   
		  return orders;
	  }
	  
	  public List<OrderTrxnDetail> getSalesTargetOrdersSum(Report report,List<Long> orderIds){
		  List<OrderTrxnDetail> orders = new ArrayList<OrderTrxnDetail>();
		  OrderTrxnDetail neworder=new OrderTrxnDetail();
		  Double netamount =0.00;
		  try{
			  if (orderIds.size()==0)
				  return orders;
			  Query query = null;
			  query = entityManager.createNativeQuery("SELECT sum(o.net_amount)FROM order_trxn_detail o "
					  		+ "WHERE o.order_id in:orderIds");
				 if(query!=null){
				    query.setParameter("orderIds" , orderIds);
			         netamount = (Double) query.getSingleResult();
			         neworder.setNetAmount(netamount);
				 }
				 else{
					 neworder.setNetAmount(0.00);
				 }
						 
				 orders.add(neworder);
				  
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		   
		  return orders;
	  }
	  
	 
			  
			  public List<Voucher> getMBRVouchers(Report report){
		  List<Voucher> orders = new ArrayList<Voucher>();
		  try{
			  StringBuffer wClause = new StringBuffer();
			  if(report.getFromDate()!=null && report.getToDate()!=null){
				  wClause.append("o.createdDate BETWEEN :startDate and :endDate  and expense_id !='10014'");
			  }
			  if(report.getTypeId()!=null && report.getTypeId()!=0){
				  if(wClause.length() > 0)
					  wClause.append(" AND o.expenseId=:expenseId");
				  else
					  wClause.append(" o.expenseId=:expenseId");
			  }
			  
			  logger.info("where clause to search order transaction detail ::: " + wClause.toString());
	
			  TypedQuery<Voucher> query = null;
			  if(!StringUtils.isEmpty(wClause.toString().trim())){
				  StringBuffer sQuery = new StringBuffer("SELECT o FROM Voucher o WHERE ");
				  sQuery.append(wClause);
				  query = entityManager.createQuery(sQuery.toString(), Voucher.class);
				  if(report.getFromDate() !=null && report.getToDate()!=null){
					  query.setParameter("startDate", report.getFromDate());
					  query.setParameter("endDate" , report.getToDate());
				  }
				  if(report.getTypeId()!=null && report.getTypeId()!=0){
					  query.setParameter("expenseId" , (int)report.getTypeId());
				  }
			  }
			  if(query!=null)
				  orders = query.getResultList();
			  
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  return orders;
	  }		  
			  
	  public List<Voucher> getVouchers(Report report){
		  List<Voucher> orders = new ArrayList<Voucher>();
		  try{
			  StringBuffer wClause = new StringBuffer();
			  if(report.getFromDate()!=null && report.getToDate()!=null){
				  wClause.append("o.createdDate BETWEEN :startDate and :endDate");
			  }
			  if(report.getTypeId()!=null && report.getTypeId()!=0){
				  if(wClause.length() > 0)
					  wClause.append(" AND o.expenseId=:expenseId");
				  else
					  wClause.append(" o.expenseId=:expenseId");
			  }
			  
			  logger.info("where clause to search order transaction detail ::: " + wClause.toString());
	
			  TypedQuery<Voucher> query = null;
			  if(!StringUtils.isEmpty(wClause.toString().trim())){
				  StringBuffer sQuery = new StringBuffer("SELECT o FROM Voucher o WHERE ");
				  sQuery.append(wClause);
				  query = entityManager.createQuery(sQuery.toString(), Voucher.class);
				  if(report.getFromDate() !=null && report.getToDate()!=null){
					  query.setParameter("startDate", report.getFromDate());
					  query.setParameter("endDate" , report.getToDate());
				  }
				  if(report.getTypeId()!=null && report.getTypeId()!=0){
					  query.setParameter("expenseId" , (int)report.getTypeId());
				  }
			  }
			  if(query!=null)
				  orders = query.getResultList();
			  
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  return orders;
	  }
	  
	  public List<Voucher> getmbrdetailExpense(Report report){
		  List<Voucher> orders = new ArrayList<Voucher>();
		  try{
			  StringBuffer wClause = new StringBuffer();
			  if(report.getFromDate()!=null && report.getToDate()!=null){
				  wClause.append("o.createdDate BETWEEN :startDate and :endDate and expense_id >'10000'");
				 
			  }
			 			 
			  logger.info("where clause to search order transaction detail ::: " + wClause.toString());
	
			  TypedQuery<Voucher> query = null;
			  if(!StringUtils.isEmpty(wClause.toString().trim())){
				  StringBuffer sQuery = new StringBuffer("SELECT o FROM Voucher o WHERE ");
				  sQuery.append(wClause);
				  query = entityManager.createQuery(sQuery.toString(), Voucher.class);
				  if(report.getFromDate() !=null && report.getToDate()!=null){
					  query.setParameter("startDate", report.getFromDate());
					  query.setParameter("endDate" , report.getToDate());
				  }
				  if(report.getTypeId()!=null && report.getTypeId()!=0){
					  query.setParameter("expenseId" , (int)report.getTypeId());
				  }
			  }
			  if(query!=null)
				  orders = query.getResultList();
			  
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  return orders;
	  }
	  
	  public List<Purchase> getPurchaseBySupplierId(Long supId){
		  List<Purchase> purchase = new ArrayList<Purchase>();
		  Query query = null;
		  try{
		  query = entityManager.createNativeQuery("select * from purchase p where "
		  		+ "p.sup_id=supId",Purchase.class);
		 if (query !=null)
			 query.setParameter("sup_id",supId);
		  }catch(Exception e){System.out.println("wow");}
		  return purchase;
	  }
	  
	  public Double getPurchaseSumBySupplierId(Long supId){
		  List<Purchase> purchase = new ArrayList<Purchase>();
		  Query query = null;
		  Double balance=0.0;
		  Double advance=0.0;
		  System.out.println("Supplier ID :"+supId);
		  try{
		  query = entityManager.createNativeQuery("SELECT sum(balance_payable) from purchase p where "
		  		+ "p.sup_id =:supId");
		 if (query !=null){
			  System.out.println("Hello");
			
			query.setParameter("supId",supId);
			  System.out.println("balance :");
		     balance = (Double)query.getSingleResult();
		     System.out.println("balance :"+balance);
		 }
		 if(balance==null)
			 balance=0.0;
		  }catch(Exception e){}
		 
		  
		  query= null;
		  try{
		  query = entityManager.createNativeQuery("SELECT sum(amount_paid) from purchase p where "
			  		+ "p.sup_id=:supId");
			 if (query !=null){
				 query.setParameter("supId",supId);
			     advance = (Double)query.getSingleResult();
			     System.out.println("advance :"+advance);
			 }
			 if(advance==null)
				 advance=0.0;
			  }catch(Exception e)
			  {}
		
			  return balance+advance;
	  }
	  
	  public Double getPaymentSumBySupplierId(Long supId){
		  List<Voucher> voucher = new ArrayList<Voucher>();
		  Query query = null;
		  Double paid=0.0;
		 
		  try{
		  query = entityManager.createNativeQuery("select sum(pay_amount) from voucher p where "
		  		+ "p.expense_id=:supId");
		 if (query !=null){
			 query.setParameter("supId",supId);
		     paid = (Double)query.getSingleResult();
		     System.out.println("Payment :"+paid);
		 }
		  }catch(Exception e)
		  {}
		  
		 	  
		  return paid;
	  }
	  
	  public List<Purchase> getPurchase(Report report){
		  List<Purchase> purchase = new ArrayList<Purchase>();
		  try{
			  StringBuffer wClause = new StringBuffer();
			  if(report.getFromDate()!=null && report.getToDate()!=null){
				  wClause.append("o.createdDate BETWEEN :startDate and :endDate");
			  }
			  if(report.getCustId()!=0){
				  wClause.append(" AND o.supId =:supId"); 
			  }
			  
			  logger.info("where clause to search order transaction detail ::: " + wClause.toString());
	
			  TypedQuery<Purchase> query = null;
			  if(!StringUtils.isEmpty(wClause.toString().trim())){
				  StringBuffer sQuery = new StringBuffer("SELECT o FROM Purchase o WHERE ");
				  sQuery.append(wClause);
				  query = entityManager.createQuery(sQuery.toString(), Purchase.class);
				  if(report.getFromDate() !=null && report.getToDate()!=null){
					  query.setParameter("startDate", report.getFromDate());
					  query.setParameter("endDate" , report.getToDate());
				  }
				  if(report.getCustId()!=0){
					  query.setParameter("supId", report.getCustId());
				  }
			  }
			  if(query!=null)
				  purchase = query.getResultList();
			  
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		
		  return purchase;
	  }
	  
	  public List<Receipt> getReceipts(Report report){
		  List<Receipt> orders = new ArrayList<Receipt>();
		  try{
			  StringBuffer wClause = new StringBuffer();
			  if(report.getFromDate()!=null && report.getToDate()!=null){
				  wClause.append("o.createdDate BETWEEN :startDate and :endDate");
			  }
			  if(!StringUtils.isEmpty(report.getCustId()) && report.getCustId()!=0){
				  if(wClause.length()>0)
					  wClause.append(" and ");
				  
				  wClause.append("o.custId =:custId");
			  }
			  
			  logger.info("where clause to search order transaction detail ::: " + wClause.toString());
	
			  TypedQuery<Receipt> query = null;
			  if(!StringUtils.isEmpty(wClause.toString().trim())){
				  StringBuffer sQuery = new StringBuffer("SELECT o FROM Receipt o WHERE ");
				  sQuery.append(wClause);
				  query = entityManager.createQuery(sQuery.toString(), Receipt.class);
				  if(report.getFromDate() !=null && report.getToDate()!=null){
					  query.setParameter("startDate", report.getFromDate());
					  query.setParameter("endDate" , DateUtils.toDate(DateUtils.toDate(report.getToDate(),true).concat(" 11:59 PM")));
				  }
				  if(!StringUtils.isEmpty(report.getCustId()) && report.getCustId()!=0){
					  query.setParameter("custId" , report.getCustId());
				  }
			  }
			  if(query!=null)
				  orders = query.getResultList();
			  
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  System.out.println("RECEPIT TOTAL COUNT  :"+orders.size());
		  return orders;
	  }
	  
	  public List<Receipt> getSumOfReceipts(Report report){
		  List<Receipt> receipts = new ArrayList<Receipt>();
		  Receipt newreceipt = new Receipt();
		  Double payamount =0.00;
		  String fromDate = report.getStrFromDate();
		  String toDate = report.getStrToDate();
		  fromDate =  getddmmyyyytoyyyymdd(fromDate);
		  toDate =getddmmyyyytoyyyymdd(toDate); 
		  Long custId= report.getCustId();
		//  System.out.println("fromdate now:"+fromDate);
		//  System.out.println("todate now:"+toDate);
		//  System.out.println("cstomer id:"+ custId);
		  try{
			  
			  Query query = null;
			  query = entityManager.createNativeQuery("SELECT sum(r.pay_amount)FROM receipt r  "
					  		+ "WHERE r.receipt_date between :fromDate and :toDate and r.cust_id=:custId");
				 if(query!=null){
				    query.setParameter("custId" , report.getCustId());
				    query.setParameter("fromDate" , fromDate);
				    query.setParameter("toDate" , toDate);
			         payamount = (Double) query.getSingleResult();
			         System.out.println("TRUE PAY amount:"+payamount);
			         if(payamount !=null){
				 	 newreceipt.setPayAmount(payamount);
			         }else{
			        	 newreceipt.setPayAmount(0.00);
			         }
				 	receipts.add(newreceipt);
				 }else{
					 System.out.println("ELSE PAY amount:"+payamount);
					 newreceipt.setPayAmount(payamount);
					 receipts.add(newreceipt);
					 
				 }
			}catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  
		  System.out.println("RECEPIT COUNT  :"+receipts.size());
		  return receipts;
	  }
	  
	  public List<Receipt> getSumOfReceiptsBFD(Report report){
		  List<Receipt> receipts = new ArrayList<Receipt>();
		  Receipt newreceipt = new Receipt();
		  Double payamount =0.00;
		  //Long custId=report.getCustId();
		  
		  String fromDate = report.getStrFromDate();
		  //String toDate = report.getStrToDate();
		 // System.out.println("fromdate ===="+fromDate);
		//  System.out.println("todate ====="+toDate);
		 // System.out.println("cust id:"+report.getCustId());
		  fromDate =  getddmmyyyytoyyyymdd(fromDate);
		  //toDate =getddmmyyyytoyyyymdd(toDate); 
		  Long custId= report.getCustId();
		//  System.out.println("fromdate now:"+fromDate);
		//  System.out.println("todate now:"+toDate);
		//  System.out.println("cstomer id:"+ custId);
		  try{
			  
			  Query query = null;
			  query = entityManager.createNativeQuery("SELECT sum(r.pay_amount)FROM receipt r  "
					  		+ "WHERE r.receipt_date <:fromDate and r.cust_id=:custId");
				 if(query!=null){
				    query.setParameter("custId" , report.getCustId());
				    query.setParameter("fromDate" , fromDate);
				    //query.setParameter("toDate" , toDate);
			         payamount = (Double) query.getSingleResult();
			        // System.out.println("pay amount:"+payamount);
				 }
				 if(payamount !=null){
				 newreceipt.setPayAmount(payamount);
				 receipts.add(newreceipt);
				 }else{
					 newreceipt.setPayAmount(0.00);
					 receipts.add(newreceipt);
				 }
					 
					 
			  
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  System.out.println("RECEPIT TOTAL COUNT  :"+receipts.size());
		  return receipts;
	  }
	  
	  
	  
	  public Receipt getReceiptById(Long receiptId){
		  return  entityManager.find(Receipt.class, receiptId);
	  }
 
	  public List<Order> getAgentOrders(Report report){
		  List<Order> orders = new ArrayList<Order>();
		  try{
			  StringBuffer wClause = new StringBuffer();
			  if(report.getFromDate()!=null && report.getToDate()!=null){
				  wClause.append("o.orderDate BETWEEN :startDate and :endDate and o.agentName=:agentName and o.status !='106' order by custId");
			  }
			  
			  logger.info("where clause to search agent order detail ::: " + wClause.toString());
	
			  TypedQuery<Order> query = null;
			  if(!StringUtils.isEmpty(wClause.toString().trim())){
				  StringBuffer sQuery = new StringBuffer("SELECT o FROM Order o WHERE ");
				  sQuery.append(wClause);
				  query = entityManager.createQuery(sQuery.toString(), Order.class);
				  if(report.getFromDate() !=null && report.getToDate()!=null){
					  query.setParameter("startDate", report.getFromDate());
					  query.setParameter("endDate" , DateUtils.toDate(DateUtils.toDate(report.getToDate(),true).concat(" 11:59 PM")));
					  query.setParameter("agentName", report.getName());
				    //query.setParameter("netAmount", otDetail.getNetAmount());
				  }
			  }
			  if(query!=null)
				  orders = query.getResultList();
			  		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  return orders;
	  }
	  
	  
	  public List<Order> getAgentOrdersCorrection(Report report){
		  List<Order> orders = new ArrayList<Order>();
		  try{
			  System.out.println("HEllo--3");
			  StringBuffer wClause = new StringBuffer();
			  if(report.getFromDate()!=null && report.getToDate()!=null){
				  wClause.append(" o.custId =:custId and o.status !='106' order by orderId");			  }
			  
			  logger.info("where clause to search customer order detail ::: " + wClause.toString());
	
			  TypedQuery<Order> query = null;
			  if(!StringUtils.isEmpty(wClause.toString().trim())){
				  StringBuffer sQuery = new StringBuffer("SELECT o FROM Order o WHERE ");
				  sQuery.append(wClause);
				  query = entityManager.createQuery(sQuery.toString(), Order.class);
				  if(query !=null ){
					 /* query.setParameter("startDate", report.getFromDate());
					  query.setParameter("endDate" , DateUtils.toDate(DateUtils.toDate(report.getToDate(),true).concat(" 11:59 PM")));*/
					 query.setParameter("custId", report.getCustId());
					 orders = query.getResultList();
				    //query.setParameter("netAmount", otDetail.getNetAmount());
				  }
			  }
			  
		   }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  return orders;
	  }
	  
	  public List<Order> getOrdersList(Report report){
		  List<Order> orders = new ArrayList<Order>();
		  try{
			  StringBuffer wClause = new StringBuffer();
			  if(report.getFromDate()!=null && report.getToDate()!=null){
				  wClause.append("o.orderDate BETWEEN :startDate and :endDate");
			  }
			  
			  if(!StringUtils.isEmpty(report.getCustId()) && report.getCustId()!= 0){
				  if(wClause.length() >0)
					  wClause.append(" and ");
				  
				  wClause.append(" o.custId =:custId and o.status !='106'");
			  }
			  
			  logger.info("where clause to search orders  ::: " + wClause.toString());
	
			  TypedQuery<Order> query = null;
			  if(!StringUtils.isEmpty(wClause.toString().trim())){
				  StringBuffer sQuery = new StringBuffer("SELECT o FROM Order o WHERE ");
				  sQuery.append(wClause);
				  query = entityManager.createQuery(sQuery.toString(), Order.class);
				  if(report.getFromDate() !=null && report.getToDate()!=null){
					  query.setParameter("startDate", report.getFromDate());
					  query.setParameter("endDate" , DateUtils.toDate(DateUtils.toDate(report.getToDate(),true).concat(" 11:59 PM")));
				  }
				  if(!StringUtils.isEmpty(report.getCustId()) && report.getCustId()!= 0){
					  query.setParameter("custId", report.getCustId()) ;
				  }
			  }
			  if(query!=null)
				  orders = query.getResultList();
			  
			  
			  
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  return orders;
	  }
	  
	
	public List<Order> getCancelOrdersList_working(Report report){
		  List<Order> orders = new ArrayList<Order>();
		  String fromDate = report.getStrFromDate();
		  String toDate = report.getStrToDate();
		  String custId=  String.valueOf(report.getCustId());
		  fromDate =  getddmmyyyytoyyyymdd(fromDate);
		  toDate =getddmmyyyytoyyyymdd(toDate); 
		  System.out.println("from   ----date"+fromDate);
		  System.out.println("To=----- date"+toDate);
		  System.out.println(" cust id "+ custId);
		  try{
			  
			  Query query = entityManager.createNativeQuery("SELECT * FROM orders o where substr(o.order_date,1,10) between :fromDate "
			  		+ " and :toDate and o.cust_id=:custId and o.status='106'",Order.class);
			  if(query !=null){
			  query.setParameter("custId",custId);
			  query.setParameter("fromDate",fromDate);
			  query.setParameter("toDate",toDate);
			 
			  orders=query.getResultList();
			  }
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		 
		  System.out.println("order sise :"+orders.size());
		 
		/*  for(int i=0;i<orders.size();i++){
			
			  System.out.println(orders.get(i).getOrderId());
			  System.out.println(orders.get(i).getUpdatedDate());
			  System.out.println(orders.get(i).getCustId());
			  System.out.println(orders.get(i).getCustAddr1());
			  System.out.println(orders.get(i).getBillBy());
				  
		  }*/
		  return orders;
		  
	  }
	  
	  
	  public List<Order> getCancelOrdersList(Report report){
		  List<Order> orders = new ArrayList<Order>();
		  try{
			  StringBuffer wClause = new StringBuffer();
			  if(report.getFromDate()!=null && report.getToDate()!=null){
				  wClause.append("o.orderDate BETWEEN :startDate and :endDate");
			  }
			  System.out.println("cuswt id:"+report.getCustId());
			  if(!StringUtils.isEmpty(report.getCustId()) && report.getCustId()!= 0){
				  if(wClause.length() >0)
					  wClause.append(" and ");
				  
				  wClause.append(" o.custId =:custId and o.status ='106'");
			  }
			  
			  logger.info("where clause to search orders  ::: " + wClause.toString());
	
			  TypedQuery<Order> query = null;
			  if(!StringUtils.isEmpty(wClause.toString().trim())){
				  StringBuffer sQuery = new StringBuffer("SELECT o FROM Order o WHERE ");
				  sQuery.append(wClause);
				  query = entityManager.createQuery(sQuery.toString(), Order.class);
				  if(report.getFromDate() !=null && report.getToDate()!=null){
					  query.setParameter("startDate", report.getFromDate());
					  query.setParameter("endDate" , DateUtils.toDate(DateUtils.toDate(report.getToDate(),true).concat(" 11:59 PM")));
				  }
				  if(!StringUtils.isEmpty(report.getCustId()) && report.getCustId()!= 0){
					  query.setParameter("custId", report.getCustId()) ;
				  }
			  }
			  if(query!=null)
				  orders = query.getResultList();
			  
			  
			  
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  System.out.println("order sise :"+orders.size());
		  return orders;
	  }
	  
	  public Double getOBCancelOrdersList(Report report){
		   Double cancelAdvance=0.00;
		   try{
			  Query query = entityManager.createNativeQuery("SELECT sum(advance) FROM order_trxn_detail tx, orders dc where tx.created_date <:fromdate and dc.cust_id =:custid and tx.order_id = dc.order_id  and dc.status ='106'");
			  query.setParameter("custid",report.getCustId());
			  query.setParameter("fromdate",report.getFromDate());
			//  System.out.println("report.getFromDate() " + report.getFromDate());
			  cancelAdvance=(Double)query.getSingleResult(); 
			  System.out.println("cancelled orders Advance for OB: "+cancelAdvance);
			  
			  
			  
			  /*
			  StringBuffer wClause = new StringBuffer();
			  if(report.getFromDate()!=null && report.getToDate()!=null){
				  wClause.append("o.orderDate < :startDate");
			  }
			  
			  if(!StringUtils.isEmpty(report.getCustId()) && report.getCustId()!= 0){
				  if(wClause.length() >0)
					  wClause.append(" and ");
				  
				  wClause.append(" o.custId =:custId and o.status ='106'");
			  }
			  
			  logger.info("where clause to search orders  ::: " + wClause.toString());
	
			  TypedQuery<Order> query = null;
			  if(!StringUtils.isEmpty(wClause.toString().trim())){
				  StringBuffer sQuery = new StringBuffer("SELECT o FROM Order o WHERE ");
				  sQuery.append(wClause);
				  query = entityManager.createQuery(sQuery.toString(), Order.class);
				  if(report.getFromDate() !=null && report.getToDate()!=null){
					  query.setParameter("startDate", report.getFromDate());
					  query.setParameter("endDate" , DateUtils.toDate(DateUtils.toDate(report.getToDate(),true).concat(" 11:59 PM")));
				  }
				  if(!StringUtils.isEmpty(report.getCustId()) && report.getCustId()!= 0){
					  query.setParameter("custId", report.getCustId()) ;
				  }
			  }
			  if(query!=null)
				  orders = query.getResultList();
			  
			  
			  */
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  return cancelAdvance;
	  }
	  
	  public  <T> Boolean saveOrUpdate(T t){
		  Boolean isSuccess=true;
		  try{
			  entityManager.merge(t);
		  }catch (Exception _exception) {
			  logger.error("ReportDAO : saveOrUpdate :"+_exception);
			  isSuccess=false;
		  }
		return isSuccess;
	  }
	  
	  public  <T> Boolean save(T t){
		  Boolean isSuccess=true;
		  try{
			  entityManager.persist(t);
		  }catch (Exception _exception) {
			  logger.error("ReportDAO : save :"+_exception);
			  isSuccess=false;
		  }
		return isSuccess;
	  }
	  
	  public BalanceDetails getBalance(Date date){
		 BalanceDetails balanceDetails = null;
		  try{
			  TypedQuery<BalanceDetails> query = entityManager.createQuery("SELECT b FROM BalanceDetails b WHERE b.createdDate=:createdDate", BalanceDetails.class);
			  query.setParameter("createdDate", date);
			  balanceDetails = query.getSingleResult();
		  }catch (Exception _exception) {
			  logger.error(_exception);
		  }
		  return balanceDetails;
	  }

	  public BalanceDetails getLastBalance(){
		 BalanceDetails balanceDetails = null;
		  try{
			 Query query = entityManager.createNativeQuery("SELECT * FROM balance_details ORDER BY id DESC LIMIT 1", BalanceDetails.class);
			  
			  balanceDetails =   (BalanceDetails) query.getSingleResult();
			  //System.out.println("balance() " + balanceDetails);
		  }catch (Exception _exception) {
			  logger.error(_exception);
		  }
		  return balanceDetails;
	  }
	  // for selected Date daily Report
	  public BalanceDetails getLastBalancewithDate(String fromDate){
			 BalanceDetails balanceDetails = null;
			  try{ 
				 // System.out.println("reached ....reportDAO");
				 fromDate = getddmmyyyytoyyyymdd(fromDate);
				 
				 Query query = entityManager.createNativeQuery("SELECT * FROM balance_details where created_date< :fromDate ORDER BY id DESC LIMIT 1", BalanceDetails.class);
				 query.setParameter("fromDate", fromDate);
				  balanceDetails =   (BalanceDetails) query.getSingleResult();
				 // System.out.println("Previous Day Balance() :" + balanceDetails);
			  }catch (Exception _exception) {
				  logger.error(_exception);
			  }
			  return balanceDetails;
		  }
	  
	  public BalanceDetails getLastPreviousBalance(){
		 BalanceDetails balanceDetails = null;
		  try{
			  Query query = entityManager.createNativeQuery("SELECT * FROM balance_details where id != (SELECT max(id) FROM balance_details) ORDER BY id DESC LIMIT 1", BalanceDetails.class);
			  balanceDetails =   (BalanceDetails) query.getSingleResult();
		  }catch (Exception _exception) {
			  logger.error(_exception);
		  }
		  return balanceDetails;
	  }
	 
// for Customer Report opening balance 
	/*@Transactional(noRollbackFor = Exception.class)
	public List<ReportDetail> getOpeningBalance(Report report) {
		// TODO Auto-generated method stub
		 System.out.println("hello from getopening balacne : ");
		 System.out.println(report.getCustId());
		 System.out.println(report.getStrFromDate());
		 System.out.println("???"+report.getStrToDate());
			  List<Order> orders = new ArrayList<Order>();
			  List<ReportDetail> repDetail=null;
		  try{
			  String fromdate= DateUtils.toDate(report.getFromDate());
			   fromdate = fromdate.substring(0,10);
			  fromdate = getyyyymmdd(fromdate);
			  Long custid = report.getCustId();
			  System.out.println(custid);
			  
			 Query query = entityManager.createNativeQuery("SELECT *  FROM orders o  WHERE o.order_Date <:fromdate and o.cust_id =:custid", Order.class);
			//  Query query = entityManager.createNativeQuery("SELECT sum(balance)  FROM studio.order_trxn_detail tx, studio.orders dc where dc.order_date < '2019/07/05' and dc.cust_id = '3' and tx.order_id = dc.order_id",Order.class,OrderTrxnDetail.class );
			   
			  query.setParameter("custid" , report.getCustId());
			  query.setParameter("fromdate" , report.getFromDate());
			
				 if(query!=null)
					 System.out.println("I am from getopening balance");
					 repDetail = query.getResultList();
					 for (ReportDetail reportDetail : repDetail) {
						Double jxs  =   reportDetail.getBalance();
						 System.out.println("<><><run do now now> " + jxs);
					 }
					 System.out.println("repDetail.size() " + repDetail.size());
					 //System.out.println("Order.size() " + order.size());
					 for(int i=0; i<repDetail.size(); i++) {
						 //System.out.println("<><><ok Wait > " + repDetail.
					 } 
					// orders = query.getR esultList();	   
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  return repDetail ;
	  }*/
		
		//end of function

	// for customer report opening balance 
	/*@Transactional(noRollbackFor = Exception.class)
	public Double getBalanceByCustomerId(List<Order> orders){
		Double orderbalance=null;
		try{ 
			//OrderTrxnDetail otDetail= new OrderTrxnDetail();
			Query query = entityManager.createNativeQuery("SELECT sum(balance) FROM OrderTrxnDetail ot, order o WHERE t.orderId = :o.orderId", OrderTrxnDetail.class);
			//query.setParameter("orderId", Balance)
			orderbalance=(Double) query.getSingleResult();
			System.out.println("I am from balancebycistID"+orderbalance);
			}catch (Exception _exception) {
				logger.error(_exception);
				_exception.printStackTrace();
			}
	    return orderbalance;
	  }*/
	  
	  // for srp reports- discount
	  public List<OrderTrxnDetail> getOrderTrxn(long orderId) {
			List<OrderTrxnDetail> otd= new ArrayList<OrderTrxnDetail>();
			try{
			 Query query = null;
		     String orderid=Long.toString(orderId);
		     query = entityManager.createNativeQuery("select * from order_trxn_detail  where order_id =:orderid  and discount>0", OrderTrxnDetail.class);
			 query.setParameter("orderid", orderId);
			 otd = query.getResultList();
		    }catch(Exception e){}  
			 // TODO Auto-generated method stub
			return otd;
		}
	  
	// for mbrsummary reports
	  public List<Voucher> getActualPayments(String fromDate, String toDate){
		  List<Voucher> vouchers = new ArrayList<Voucher>();
		  try{
			  Query query = null;
			  Session session = null;
			  System.out.println("vouchers date before :"+fromDate+toDate);
			 /* fromDate = fromDate.substring(0,10);
			  toDate = toDate.substring(0,10);	*/
			  fromDate = getMBRyyyymmdd(fromDate,"-");
			  toDate= getMBRyyyymmdd(toDate,"-"); 						
			  System.out.println("vouchers date after :"+fromDate+toDate);
		
			  query = entityManager.createNativeQuery("select * from voucher v where v.voucher_date between :fromDate and :toDate and expense_id !='10014' and expense_id !='10012'",Voucher.class);
			  query.setParameter("fromDate", fromDate);
			  query.setParameter("toDate", toDate);
			 			
		 if(query!=null){
				  vouchers = query.getResultList();
			System.out.println("vouchers.size() :"+vouchers.size());
		 }
			 }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  return vouchers;
	  }
	
	
	public List<Purchase> getActualPurchase(String fromDate, String toDate){
		  List<Purchase> purchase = new ArrayList<Purchase>();
		  try{
			 
				  
			  /*  fromDate = fromDate.substring(0,10);
			 fromDate = getyyyymmdd(fromDate);
			  toDate= getyyyymmdd(toDate); 	*/	
			 /* fromDate = getyyyymmdd(fromDate,"-");
			  toDate= getyyyymmdd(toDate,"-"); 			*/	
			  fromDate = getMBRyyyymmdd(fromDate,"-");
			  toDate= getMBRyyyymmdd(toDate,"-"); 		
			  Query query = null;
			  if(fromDate!=null && toDate!=null){
				
			 	 query = entityManager.createNativeQuery("select * from purchase p where p.created_date between :fromDate and :toDate", Purchase.class);
				 query.setParameter("fromDate", fromDate);
				 query.setParameter("toDate", toDate);			  
			  if(query!=null){
				  purchase =  query.getResultList();
				  System.out.println("purchase.size() :"+purchase.size());
			  
			  }
			  }
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  return purchase;
	  }
	  public List<Receipt> getActualReceipts(String fromDate, String toDate){
		  List<Receipt> receipts = new ArrayList<Receipt>();
		  try{
			
			  System.out.println("From date Before :"+ fromDate);
			  System.out.println("To date Before :"+ toDate);
			 /* fromDate = fromDate.substring(0,10);
			  fromDate = getyyyymmdd(fromDate,"-");
			  toDate= getyyyymmdd(toDate,"-"); 				*/		
							
			  fromDate = getMBRyyyymmdd(fromDate,"-");
			  toDate= getMBRyyyymmdd(toDate,"-"); 	
			  System.out.println("From date after:"+ fromDate);
			  System.out.println("To date after:"+ toDate);

			  if(fromDate!=null && toDate!=null){
				 Query query = null;
			 	 query = entityManager.createNativeQuery("select * from receipt r where r.receipt_date between :fromDate and  :toDate ", Receipt.class);
			

				  if(fromDate!=null && toDate!=null){
					  query.setParameter("fromDate", fromDate);
						 query.setParameter("toDate", toDate);	
				  }
				  
			  if(query!=null){
				  receipts = query.getResultList();
				  System.out.println("receipts.size() :"+receipts.size());
			  }
			  }
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  return receipts;
	  }
	  
	  public List<OrderTrxnDetail> getActualSales(String fromDate, String toDate){
		 /* List<OrderTrxnDetail> sales = new ArrayList<OrderTrxnDetail>();*/
		  List<OrderTrxnDetail> sales =null;
		  try{
			  List<Order> orders = null;
			     System.out.println("from actualsales fromdate :"+fromDate);
				 orders= getMBROrders(fromDate,toDate);
			     System.out.println("total orders  :"+orders.size());
			     Report report= new Report();
			   // Date date1=new SimpleDateFormat("yyyy/MM/dd").parse(fromDate);
			    String fromdate=getMBRyyyymmdd(fromDate,"/");
			    String todate=getMBRyyyymmdd(toDate,"/");
			    Date date1=new SimpleDateFormat("yyyy/MM/dd").parse(fromdate);
			    Date date2=new SimpleDateFormat("yyyy/MM/dd").parse(todate);
			    System.out.println("from actualsales fromdate... :"+ date1+"  "+ date2);
			     report.setFromDate(date1);
			     report.setToDate(date2);
			     
			     List<Long> orderIds = Utils.getOrdersIds(orders);
				 sales=getOrders(report, orderIds);
		         System.out.println("only oder IDs:"+sales.size());
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  } 
		  return sales;
			 /* fromDate = fromDate.substring(0,10);
			  fromDate = getyyyymmdd(fromDate,"-");
			  toDate= getyyyymmdd(toDate,"-"); 						
		
			  if(fromDate!=null && toDate!=null){
				 Query query = null;
			 	query = entityManager.createNativeQuery("select * from order_trxn_detail otd where otd.created_date between :fromDate and :toDate ", OrderTrxnDetail.class);
			  if(fromDate !=null && toDate!=null){
					  query.setParameter("fromDate", fromDate);
						 query.setParameter("toDate", toDate);	
				  }
				  
			  if(query!=null){
				  sales = query.getResultList();
		//	System.out.println("Size of sales:"+ sales.size());
			  }
			  }
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  } */
		  
	  }
	  public List<Product> getActualSheets(String fromDate, String toDate){
		  List<Product> sheets = new ArrayList<Product>();
		  try{
			
			 // fromDate = fromDate.substring(0,10);
			 // fromDate = getyyyymmdd(fromDate);
			//  toDate= getyyyymmdd(toDate); 						
		
			  List<Order> orders = null;
				 orders= getMBROrders(fromDate,toDate);
			    // System.out.println("total sheets from orders  :"+orders.size());
			     Report report= new Report();
			     fromDate = getMBRyyyymmdd(fromDate,"-");
				 toDate= getMBRyyyymmdd(toDate,"-"); 	
			//	 System.out.println("from ----getActualSheets  :"+fromDate);
			//	 System.out.println("from ----getActualSheets  :"+toDate);
			     Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
			     Date date2=new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
			//     System.out.println("from ----getActualSheets  :"+date1);
			//	 System.out.println("from ----getActualSheets  :"+date2);
			     report.setFromDate(date1);
			     report.setToDate(date2);
			   //  System.out.println("from+++++Date++++"+report.getFromDate());
			     if (orders.size()>0){
			     List<Long> orderIds = Utils.getOrdersIds(orders);
			     sheets = getProducts(report,orderIds);
			     }
			/* if(fromDate!=null && toDate!=null){
				 Query query = null;
				 query = entityManager.createNativeQuery("select * from product p where p.created_date between :fromDate and :toDate and p.product_type_id='102'",Product.class);
			 	  if(fromDate!=null && toDate!=null){
			 		 query.setParameter("fromDate", fromDate);
					 query.setParameter("toDate", toDate);	
				  }
				  
			  if(query!=null){
				  sheets = query.getResultList();
			  
			  }
			  }*/
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  return sheets;
	  }
	  public List<OrderTrxnDetail> getCancelAmount(String fromDate, String toDate) {
		  List<OrderTrxnDetail> orders = new ArrayList<OrderTrxnDetail>();
		  try{
			
			  /*System.out.println(fromDate);
			  System.out.println(toDate);	*/		  
			  fromDate = fromDate.substring(0,10);
			  toDate = toDate.substring(0,10);
			  System.out.println("fromdate ===="+fromDate);
			  System.out.println("todate ====="+toDate);
			  fromDate =  getddmmyyyytoyyyymdd(fromDate);
			  toDate=  getddmmyyyytoyyyymdd(toDate); 						
			  System.out.println("fromdate now:"+fromDate);
			  System.out.println("todate now:"+toDate);
			  if(fromDate!=null && toDate!=null){
				 Query query = null;
			 	
			 	 query = entityManager.createNativeQuery("select * from orders o, order_trxn_detail ot where o.order_id = ot.order_id and o.status='106' and substr(o.order_date,1,10) between :fromDate1 and :toDate1",OrderTrxnDetail.class);
			 	  if(fromDate!=null && toDate!=null){
			 		 query.setParameter("fromDate1", fromDate);
					 query.setParameter("toDate1", toDate);	
				  }
			 
			  if(query!=null){
				  orders = query.getResultList();
			     System.out.println("orderTrxn .size() :"+orders.size());
			  }
			  }
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  return orders;
		}
	  
	  
	  
	  public List<ReportDetail> getCOSOrderDetails(Report report,List<ReportDetail> reportdetails) {
		  List<Order> orders = new ArrayList<Order>();
		  List<ReportDetail> details= new ArrayList<ReportDetail>();
		  try{
			
			  //System.out.println(fromDate);
			  //System.out.println(toDate);		  
			  String fromDate = report.getStrFromDate();
			  String toDate = report.getStrToDate();
			 // System.out.println("fromdate ===="+fromDate);
			  //System.out.println("todate ====="+toDate);
			 // System.out.println("cust id:"+report.getCustId());
			  fromDate =  getddmmyyyytoyyyymdd(fromDate);
			  toDate =getddmmyyyytoyyyymdd(toDate); 
			   System.out.println("fromdate now:"+fromDate);
			   System.out.println("todate now:"+toDate);
			 
			  
			  for ( ReportDetail rd :reportdetails){
				  Double netamount=0.00;
				  Double advance= 0.00;
				  Double receipt=0.00;
				  Double OBnetamount=0.00;
				  Double OBadvance=0.00;
				  Double OBreceipt=0.00;
				  Query query;
				  System.out.println("customer ID :"+rd.getCustId());
				  /* TO FETCH THE NETAMOUNT FOR CUSTOMERS FOR ORDER !=106 STARTS */
				    
					 query = entityManager.createNativeQuery("SELECT sum(net_amount) FROM order_trxn_detail ot, orders o where "
							+ "o.status!='106' and (substr(o.order_date,1,10) between :fromDate and :toDate)" 
					 		+ " and o.cust_id = :custId and o.order_id=:ot.orderId ");
				 	 //query = entityManager.createNativeQuery("select sum() from orders o, order_trxn_detail ot where o.order_id = ot.order_id and o.status='106' and substr(o.order_date,1,10) between :fromDate1 and :toDate1",OrderTrxnDetail.class);
				 	  if(query!=null){
				 		 query.setParameter("fromDate", fromDate);
						 query.setParameter("toDate", toDate);	
						 query.setParameter("custId",rd.getCustId());
						 netamount = (Double)query.getSingleResult();
						 if (netamount !=null){
						 rd.setInvoiceAmount(netamount);
						 }else{
							 netamount=0.00;
							 rd.setInvoiceAmount(netamount);
						 }
						 System.out.println("netAmount :"+ rd.getInvoiceAmount());
					  }
				  /* TO FETCH THE NETAMOUNT FOR CUSTOMERS FOR ORDER !=106 ENDS*/  
				 
				  /*TO FETCH THE ADVANCE + RECEIPT FOR CUSTOMERS FOR ORDER !=106 STARTS*/ 
				 	query = null;
					query = entityManager.createNativeQuery("SELECT sum(advance) FROM order_trxn_detail ot, orders o where "
							+ "o.status!='106' and (substr(o.order_date,1,10) between :fromDate and :toDate)" 
					 		+ " and o.cust_id = :custId and o.order_id=:ot.orderId ");
				 	  
					if(query!=null){
						query.setParameter("fromDate", fromDate);
						query.setParameter("toDate", toDate);	
						query.setParameter("custId",rd.getCustId());
						advance = (Double)query.getSingleResult();
						if (advance==null){
							advance=0.00;
						}
						 System.out.println("advance :"+ advance);
					 }
					
					
					 query = null;
					 query = entityManager.createNativeQuery("SELECT sum(r.pay_amount) FROM receipt r  "
							  		+ "WHERE r.receipt_date between :fromDate and :toDate and r.cust_id=:custId");
						 if(query!=null){
						    query.setParameter("custId" , rd.getCustId());
						    query.setParameter("fromDate" , fromDate);
						    query.setParameter("toDate" , toDate);
					         receipt = (Double) query.getSingleResult();
					         	//	System.out.println("DETAILS -1 SIZE:"+ details.size());
					         	//	System.out.println("my order size is :@@@@  CHECK");
					         if(receipt !=null){
					        	receipt += advance;
					        	rd.setTotalReceipt(receipt);
					         }else{
					        	 rd.setTotalReceipt(advance);
					        	 
					         }
					         System.out.println("Total Receipt :"+ rd.getTotalReceipt());
						 }   
						 /* TO FETCH THE ADVANCE + RECEIPT FOR CUSTOMERS FOR ORDER !=106 ENDS */
						 
						 /* TO FETCH THE NETAMOUNT FOR CUSTOMERS FOR COUTSTANDING STARTS */
					     query = null;
						 query = entityManager.createNativeQuery("SELECT sum(net_amount) FROM order_trxn_detail ot, orders o where "
								+ "o.status!='106' and (substr(o.order_date,1,10) <:fromDate)" 
						 		+ " and o.cust_id = :custId and o.order_id=:ot.orderId ");
					 	 //query = entityManager.createNativeQuery("select sum() from orders o, order_trxn_detail ot where o.order_id = ot.order_id and o.status='106' and substr(o.order_date,1,10) between :fromDate1 and :toDate1",OrderTrxnDetail.class);
					 	  if(query!=null){
					 		 query.setParameter("fromDate", fromDate);
							 query.setParameter("custId",rd.getCustId());
							 OBnetamount = (Double)query.getSingleResult();
							 if(OBnetamount==null)
								 OBnetamount=0.00;
						  }
					 	  System.out.println("OBnetamount :"+OBnetamount);
					 	 query = null;
						 query = entityManager.createNativeQuery("SELECT sum(advance) FROM order_trxn_detail ot, orders o where "
								+ "o.status !='106' and (substr(o.order_date,1,10) <:fromDate)" 
						 		+ " and o.cust_id =:custId and o.order_id=:ot.orderId ");
					 	 //query = entityManager.createNativeQuery("select sum() from orders o, order_trxn_detail ot where o.order_id = ot.order_id and o.status='106' and substr(o.order_date,1,10) between :fromDate1 and :toDate1",OrderTrxnDetail.class);
					 	  if(query!=null){
					 		 query.setParameter("fromDate", fromDate);
							 query.setParameter("custId",rd.getCustId());
							 OBadvance = (Double)query.getSingleResult();
							 if(OBadvance==null)
								 OBadvance=0.00;
						  }
					 	 System.out.println("OBadvance :"+OBadvance);
					 	 query = null;
						 query = entityManager.createNativeQuery("SELECT sum(r.pay_amount) FROM receipt r  "
								  		+ "WHERE r.receipt_date <:fromDate  and r.cust_id=:custId");
							 if(query!=null){
							    query.setParameter("custId" , rd.getCustId());
							    query.setParameter("fromDate" , fromDate);
							    OBreceipt = (Double) query.getSingleResult();
							    if(OBreceipt==null)
							    	OBreceipt=0.00;
							 } 
							 System.out.println("OBreceipt :"+OBreceipt);
							 Double OBTotalreceipt=OBadvance + OBreceipt;
							 double OB=OBnetamount-OBTotalreceipt;
							 System.out.println("opening Balamnce:"+OB);
							 rd.setOpeningBalance(OB);
							// rd.setBalance((rd.getOpeningBalance()+rd.getInvoiceAmount())-rd.getTotalReceipt());
					 	 
						/* TO FETCH THE NETAMOUNT FOR CUSTOMERS FOR OUTSTANDING ENDS */ 
	
			  }
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  return reportdetails;
		}
	  
	  public List<Order> getCOSOrderList(Report report) {
		  List<Order> orders = new ArrayList<Order>();
		  try{
			
			  /*System.out.println(fromDate);
			  System.out.println(toDate);	*/		  
			  String fromDate = report.getStrFromDate();
			  String toDate = report.getStrToDate();
			 // System.out.println("fromdate ===="+fromDate);
			  //System.out.println("todate ====="+toDate);
			 // System.out.println("cust id:"+report.getCustId());
			  fromDate =  getddmmyyyytoyyyymdd(fromDate);
			  toDate =getddmmyyyytoyyyymdd(toDate); 
			  Long custId= report.getCustId();
			 // System.out.println("fromdate now:"+fromDate);
			 // System.out.println("todate now:"+toDate);
			  if(fromDate!=null && toDate!=null){
				 Query query = null;
				 query = entityManager.createNativeQuery("SELECT * FROM orders o where "
						+ "o.status!='106' and (substr(o.order_date,1,10) between :fromDate and :toDate)" 
				 		+ " and (o.cust_id = :custId) ",Order.class);
			 	 //query = entityManager.createNativeQuery("select sum() from orders o, order_trxn_detail ot where o.order_id = ot.order_id and o.status='106' and substr(o.order_date,1,10) between :fromDate1 and :toDate1",OrderTrxnDetail.class);
			 	  if(fromDate!=null && toDate!=null){
			 		 query.setParameter("fromDate", fromDate);
					 query.setParameter("toDate", toDate);	
					 query.setParameter("custId",custId);
				  }
			 
			  if(query!=null){
				  orders = query.getResultList();
			    // System.out.println("order.size() :"+orders.size());
			  }
			  
			  }
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  return orders;
		}
	  
	  
	  
	  public List<Order> getCOSOrdersBeforeDate(Report report) {
		  List<Order> orders = new ArrayList<Order>();
		  try{
			
			  /*System.out.println(fromDate);
			  System.out.println(toDate);	*/		  
			  String fromDate = report.getStrFromDate();
			  //String toDate = report.getStrToDate();
			  System.out.println("fromdate ===="+fromDate);
			 // System.out.println("todate ====="+toDate);
			  System.out.println("cust id:"+report.getCustId());
			  fromDate =  getddmmyyyytoyyyymdd(fromDate);
			  //toDate =getddmmyyyytoyyyymdd(toDate); 
			  Long custId= report.getCustId();
			  System.out.println("fromdate now:"+fromDate);
			 // System.out.println("todate now:"+toDate);
			  if(fromDate!=null ){
				 Query query = null;
				 query = entityManager.createNativeQuery("SELECT * FROM orders o where "
						+ "o.status!='106' and (substr(o.order_date,1,10) <:fromDate)" 
				 		+ " and (o.cust_id = :custId) ",Order.class);
			 	
			 	  if(fromDate!=null ){
			 		 query.setParameter("fromDate", fromDate);
					 //query.setParameter("toDate", toDate);	
					 query.setParameter("custId",custId);
				  }
			 
			  if(query!=null){
				  orders = query.getResultList();
			     System.out.println("order.size() :"+orders.size());
			  }
			  }
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  return orders;
		}
	  
	  public List<Order> getUndeliveredOrders(String fromDate, String toDate) {
		  /*  It was changed to show all booked items with its status*/
		  List<Order> orders = new ArrayList<Order>();
		  try{
		
			  fromDate = fromDate.substring(0,10);
			  toDate = toDate.substring(0,10);
			  fromDate = getyyyymmdd(fromDate,"-");
			  toDate= getyyyymmdd(toDate,"-"); 						
		
			  if(fromDate!=null && toDate!=null){
				 Query query = null;
				/* query = entityManager.createNativeQuery("select * from orders o where substr(o.order_date,1,10) between :fromDate and :toDate and o.status!='106' and o.status !='105' and o.status!='107'",Order.class);*/
				 query = entityManager.createNativeQuery("select * from orders o where substr(o.order_date,1,10) between :fromDate and :toDate",Order.class); 
				 if(fromDate!=null && toDate!=null){
			 		 query.setParameter("fromDate", fromDate);
					 query.setParameter("toDate", toDate);	
				  }
			 	 
			  if(query!=null){
				  orders = query.getResultList();
				 // System.out.println("orders.size :"+orders.size());
			    }
			  }
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
			return orders;
		}
 public List<OrderTrxnDetail>getUndeliveredAmount  (String fromdate, String todate) {
		  List<OrderTrxnDetail> otd = new ArrayList<OrderTrxnDetail>();
		  		  try{
				Query query = null;
			 	query = entityManager.createNativeQuery("select * from  order_trxn_detail ot where ot.created_date between :fromdate and :todate",OrderTrxnDetail.class);
			    query.setParameter("fromdate", fromdate);
			    query.setParameter("todate", todate);
			 			  
			  if(query!=null){
			     otd = query.getResultList();
			    // System.out.println("orders.size() :"+orders.size());
			   }
			 
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  return otd;
		}
	    
	  public List<Order> getMBROrders(String fromDate, String toDate) {
		  List<Order> orders = new ArrayList<Order>();
		  try{
			
		System.out.println("fromgetorders  "+fromDate);
		System.out.println("fromgetorders  "+toDate);
			//  fromDate = fromDate.substring(0,10);
			// toDate = toDate.substring(0,10);
			  fromDate = getMBRyyyymmdd(fromDate,"-");
			 toDate= getMBRyyyymmdd(toDate,"-"); 	
			  System.out.println("from ----getorders  :"+fromDate);
				System.out.println("from ----getorders  :"+toDate);
		
			  if(fromDate!=null && toDate!=null){
				 Query query = null;
				 query = entityManager.createNativeQuery("select * from orders o where substr(o.order_date,1,10) between :fromDate and :toDate and o.status!='106' order by o.order_id ",Order.class);
			 	  if(fromDate!=null && toDate!=null){
			 		 query.setParameter("fromDate", fromDate);
					 query.setParameter("toDate", toDate);	
				  }
				  
			  if(query!=null){
				  orders = query.getResultList();
			 System.out.println("orders.size():"+orders.size());
			    }
			  }
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
			return orders;
		}
	  
	  public List<Order> getOrders(String fromDate, String toDate) {
		  List<Order> orders = new ArrayList<Order>();
		  try{
			
	
			  fromDate = fromDate.substring(0,10);
			  toDate = toDate.substring(0,10);
			  fromDate = getyyyymmdd(fromDate,"-");
			  toDate= getyyyymmdd(toDate,"-"); 	
			 
		
			  if(fromDate!=null && toDate!=null){
				 Query query = null;
				 query = entityManager.createNativeQuery("select * from orders o where substr(o.order_date,1,10) between :fromDate and :toDate and o.status!='106' order by o.order_id ",Order.class);
			 	  if(fromDate!=null && toDate!=null){
			 		 query.setParameter("fromDate", fromDate);
					 query.setParameter("toDate", toDate);	
				  }
				  
			  if(query!=null){
				  orders = query.getResultList();
			 System.out.println("orders.size():"+orders.size());
			    }
			  }
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
			return orders;
		}
	  
	 
	  
	  public List<Order> getCancelOrders(String fromDate, String toDate) {
		  List<Order> orders = new ArrayList<Order>();
		  try{
			  fromDate = fromDate.substring(0,10);
			  toDate = toDate.substring(0,10);
			  fromDate = getyyyymmdd(fromDate,"-");
			  toDate= getyyyymmdd(toDate,"-"); 						
		
			  if(fromDate!=null && toDate!=null){
				 Query query = null;
				 query = entityManager.createNativeQuery("select * from orders o where substr(o.order_date,1,10) between :fromDate and :toDate and o.status='106'",Order.class);
			 	  if(fromDate!=null && toDate!=null){
			 		 query.setParameter("fromDate", fromDate);
					 query.setParameter("toDate", toDate);	
				  }
			 	 
			  if(query!=null){
				  orders = query.getResultList();
				//  System.out.println("orders.size :"+orders.size());
			    }
			  }
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
			return orders;
		}
	  public List<Order> getSRPCancelOrders(String fromDate, String toDate) {
		  List<Order> orders = new ArrayList<Order>();
		  try{
			 // fromDate = fromDate.substring(0,10);
			//  toDate = toDate.substring(0,10);
			  fromDate = getddmmyyyytoyyyymdd(fromDate);
			  toDate = getddmmyyyytoyyyymdd(toDate);
			 // toDate= getyyyymmdd(toDate,"-"); 						
		
			  if(fromDate!=null && toDate!=null){
				 Query query = null;
				 query = entityManager.createNativeQuery("select * from orders o where substr(o.order_date,1,10) between :fromDate and :toDate and o.status='106'",Order.class);
			 	  if(fromDate!=null && toDate!=null){
			 		 query.setParameter("fromDate", fromDate);
					 query.setParameter("toDate", toDate);	
				  }
			 	 
			  if(query!=null){
				  orders = query.getResultList();
				 System.out.println("cancelled orders count :"+orders.size());
			    }
			  }
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
			return orders;
		}
	  
	  public List<Order> getTodaysCancelledOrders(String fromDate, String toDate) {
		  List<Order> orders = new ArrayList<Order>();
		  try{
			  fromDate = fromDate.substring(0,10);
			  toDate = toDate.substring(0,10);
			  fromDate = getyyyymmdd(fromDate,"-");
			  toDate= getyyyymmdd(toDate,"-"); 						
		
			  if(fromDate!=null && toDate!=null){
				 Query query = null;
				 query = entityManager.createNativeQuery("select * from orders o where o.updated_date between :fromDate and :toDate and o.status='106'",Order.class);
			 	  if(fromDate!=null && toDate!=null){
			 		 query.setParameter("fromDate", fromDate);
					 query.setParameter("toDate", toDate);	
				  }
			 	 
			  if(query!=null){
				  orders = query.getResultList();
				//  System.out.println("orders.size :"+orders.size());
			    }
			  }
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
			return orders;
		}
	  
	  public List<Order> deliveredorders(String fromDate, String toDate) {
		  List<Order> orders = new ArrayList<Order>();
		  try{
		
			  fromDate = fromDate.substring(0,10);
			  toDate = toDate.substring(0,10);
			  fromDate = getyyyymmdd(fromDate,"-");
			  toDate= getyyyymmdd(toDate,"-"); 						
		
			  if(fromDate!=null && toDate!=null){
				 Query query = null;
				 query = entityManager.createNativeQuery("select * from orders o where o.updated_date between :fromDate and :toDate and o.status ='105'",Order.class);
			 	  if(fromDate!=null && toDate!=null){
			 		 query.setParameter("fromDate", fromDate);
					 query.setParameter("toDate", toDate);	
				  }
			 	 
			  if(query!=null){
				  orders = query.getResultList();
				//  System.out.println("orders.size :"+orders.size());
			    }
			  }
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
			return orders;
		}
	  
	
	  
	  
	  public List<Product> getProducts(Report report,List<Long> orderIds){
		  List<Product> products = new ArrayList<Product>();
		 // Date startDate = report.getFromDate();
		/*  if(report.getCustId()==17)
			  for(int j=0;j<orderIds.size();j++)
				System.out.println("ORDER IDs :"+orderIds.get(j));
			*/
		  
		  //System.out.println("StartDate:"+startDate);
		  String startDate= DateUtils.toDate(report.getFromDate());
		  startDate = startDate.substring(0,10);
		  startDate = getyyyymmdd(startDate);
		  String endDate= DateUtils.toDate(report.getToDate());
		  endDate = endDate.substring(0,10);
		  endDate = getyyyymmdd(endDate);
		  //System.out.println("StartDate:"+startDate);
		  //System.out.println("EndDate:"+endDate);
		  
		  try{
			  StringBuffer wClause = new StringBuffer();
			  if(report.getFromDate()!=null && report.getToDate()!=null){
				  wClause.append("o.createdDate BETWEEN :startDate and :endDate");
			  }
			  if(orderIds!=null && !orderIds.isEmpty()){
				  if(wClause.length() >0)
					  wClause.append(" and ");
				  wClause.append("o.orderId in:orderIds and o.productTypeId='102'"); 
			
			  }
			  
			  logger.info("where clause to search order transaction detail ::: " + wClause.toString());
	
			  TypedQuery<Product> query = null;
			  if(!StringUtils.isEmpty(wClause.toString().trim())){
				  StringBuffer sQuery = new StringBuffer("SELECT o FROM Product o WHERE ");
				  sQuery.append(wClause);
				  query = entityManager.createQuery(sQuery.toString(), Product.class);
				  if(report.getFromDate() !=null && report.getToDate()!=null){
					  query.setParameter("startDate", report.getFromDate());
					  query.setParameter("endDate" , report.getToDate());
				  }
				  if(orderIds!=null && !orderIds.isEmpty()){
					  query.setParameter("orderIds" , orderIds);
				  }
			  }
						
			  if(query!=null)
				  products = query.getResultList();
			//  System.out.println("products :"+products.size());
			  
		  }catch (Exception _exception) {
			  logger.error(_exception);
			  _exception.printStackTrace();
		  }
		  return products;
	  }
	  
	  
	public String getyyyymmdd(String st)
	  {
	    String yyyy = st.substring(st.length() - 4, st.length());
	    String dd = st.substring(0, st.indexOf("/"));
	    String mm = st.substring(st.indexOf("/") + 1, st.lastIndexOf("/"));
	    return yyyy + "/" + mm + "/" + dd;
	  }
	public String getyyyymmdd(String st,String symbol)
	  {
	    String yyyy = st.substring(0,4);
	    String mm = st.substring(5,7);
	    String dd = st.substring(8,10);
	    //System.out.println(yyyy + symbol+ mm + symbol+ dd );
	    return yyyy + symbol+ mm + symbol+ dd;
	  }
	public String getMBRyyyymmdd(String st,String symbol)
	  {
	    String yyyy = st.substring(6,10);
	    String mm = st.substring(3,5);
	    String dd = st.substring(0,2);
	    //System.out.println(yyyy + symbol+ mm + symbol+ dd );
	    return yyyy + symbol+ mm + symbol+ dd;
	  }
	public String getddmmyyyy(String st)
	  {
	    String yyyy = st.substring(0,4);
	    String dd = st.substring(8,10);
	    String mm = st.substring(5,7);
	    return dd + "/" + mm + "/" + yyyy;
	  }
	public String getddmmyyyytoyyyymdd(String st)
	  {
	    String dd= st.substring(0,2);
	    String mm = st.substring(3,5);
	    String yyyy = st.substring(6,10);
	    return yyyy+ "-" + mm + "-" + dd;
	  }
	public String getyyyymmddtoddmmyyyy(String st)
	  {
	    String yyyy = st.substring(0,4);
	    String dd = st.substring(8,10);
	    String mm = st.substring(5,7);
	    return dd + "/" + mm + "/" + yyyy;
	  }

	

	


	
}  
  


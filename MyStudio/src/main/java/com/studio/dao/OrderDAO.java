package com.studio.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.studio.domain.Customer;
import com.studio.domain.DeliveryType;
import com.studio.domain.JobAllocation;
import com.studio.domain.Order;
import com.studio.domain.OrderTrxnDetail;
import com.studio.domain.Report;
import com.studio.domain.ReportDetail;
import com.studio.utils.DateUtils;
import com.studio.utils.Utils;


@Repository
@Transactional
@EnableTransactionManagement
public class OrderDAO {
  @PersistenceContext
  EntityManager entityManager;
  Logger logger = Logger.getLogger(OrderDAO.class);
  
  
  /* to find CURRENT CREDIT BALANCE OF A CUSTOMER STARTS*/
  public Double getCurrentCreditBalance(Long custId){
	  double ccb=0.00;
	  double netamount=0.00;
	  double advance=0.00;
	  double receipt=0.00;
	  Query query;
	  try{
	  query = entityManager.createNativeQuery("select sum(net_amount) from order_trxn_detail ot, orders o where"
	  		+ " o.order_id=ot.order_id and o.cust_id=:custId and o.status !='106'");
	  if(query !=null){
		  query.setParameter("custId", custId);
		  netamount=(Double)query.getSingleResult();
	  }
	  }catch (Exception _exception) {
		  logger.error("getting NetAmount :: "+_exception);
	  }
	 // System.out.println("net Amount :"+netamount);
	  try{	  
	  query = entityManager.createNativeQuery("select sum(advance) from order_trxn_detail ot, orders o where"
		  		+ " o.order_id=ot.order_id and o.cust_id=:custId and o.status !='106'");
	  //System.out.println("query:"+query.getMaxResults());   
	  if(query != null){
		//  System.out.println("yes i am NOT NULL");
			  query.setParameter("custId", custId);
			  advance=(Double)query.getSingleResult();
		  }
		  else{
			 // System.out.println("yes i am NULL");
			 advance=0.00; 
		  }
	  }catch (Exception _exception) {
		  logger.error("getting Advance :: "+_exception);
		// System.out.println("net advance custid:"+custId); 
	  }
		//  System.out.println("net advance :"+advance); 
	try{
	query = entityManager.createNativeQuery("select sum(pay_amount) from receipt r where"
	  		+ " r.cust_id=:custId");
	 if(query !=null){
		  query.setParameter("custId", custId);
		  receipt=(Double)query.getSingleResult();
	  }else{
		  receipt=0.00;
	  }
	}catch (Exception _exception) {
		  logger.error("getting Receipt :: "+ _exception);
	  }
	// System.out.println("net RECEIPT :"+receipt); 
	 ccb=netamount - (advance+receipt);	  
	 return ccb;
  }
  
  /*TO FIND THE UNDELIVERED ALBUM DETAILS STARTS*/
  
  
/*
public List<Order> getUndeliveredAlbumJobs(){
	  List<Order> undelivered = new ArrayList<Order>();
	  try{
		  Query query = entityManager.createNativeQuery(
				  "SELECT * FROM orders o WHERE o.sms_flag >0 and o.status !=105",Order.class);
			   
		  if(query !=null)
			undelivered = query.getResultList();
		  //	System.out.println("undelivered  SIZE.._:"+undelivered.size());
		  //	System.out.println("TESTING.._exception..._exception.");
		 
	  }catch (Exception _exception) {
		  logger.error(_exception);
	  } 
	  return undelivered;
	  
  }*/
  

  public List<ReportDetail> getUndeliveredAlbumOrders(List<Long> orderIds){
	  
	  List<Order> orders = new ArrayList<Order>();
	  List<Customer> customer = new ArrayList<Customer>();
	  List<ReportDetail> reportDetails =new ArrayList<ReportDetail>();
	  Double currentCreditBalance=0.00;
	  try{
		 /* Query query = entityManager.createNativeQuery("SELECT * FROM job_allocation  WHERE "
			  		+ "status_id='102' and depot_id='106'", JobAllocation.class);*/
		 // System.out.println("orderIds size :"+orderIds);
		  Query query = entityManager.createNativeQuery("SELECT * FROM orders o WHERE "
		  		+ "o.sms_flag >0 and o.order_id in:orderIds", Order.class);
		  if(query !=null){
			  query.setParameter("orderIds", orderIds);
			  orders = query.getResultList();
			//  System.out.println("orders  SIZE.._:"+orders.size());
		  }
		     List<Long> custIds =Utils.getUndeliveredCustomersIds(orders); 
		     customer= getUndeliveredCustomerOrders(custIds);
		  for(Order order:orders){
			ReportDetail rep = new ReportDetail();
			rep.setOrderId(order.getOrderId());
			rep.setInvoiceDate(order.getStrOrderDate());
			for (Customer customers:customer){
				if(customers.getCustId()==order.getCustId()){
				    rep.setCustId(customers.getCustId());
				    rep.setFirstName(customers.getFirstName());
				    rep.setAddress2(customers.getAddress2());
					rep.setMobileNo(customers.getMobileNo());
				   currentCreditBalance =getCurrentCreditBalance(customers.getCustId());
				}
			}
			if (currentCreditBalance !=null)
				rep.setCreditAmount(currentCreditBalance);
			rep.setDays(order.getSmsFlag());
			  
		  }
		  		  
	  }catch (Exception _exception) {
		  logger.error(_exception);
	  }
	  return reportDetails;
	  
  }
  
  
  
  /*TO FIND THE UNDELIVERED ALBUM DETAILS ENDS*/
  
  
  /* to find CURRENT CREDIT BALANCE OF A CUSTOMER ENDS*/
  
  public Order getOrderById(Long orderId){
	  Order order =  entityManager.find(Order.class, orderId);
	  return order;
  }
  
  public Order getEditOrderById(Long orderId){
	  Order order=null;
	  try{
		  TypedQuery<Order> query = entityManager.createQuery("SELECT o FROM Order o WHERE o.orderId=:orderId and o.status <> 106 ", Order.class);
		  order = query.setParameter("orderId", orderId).getSingleResult();
	  }catch (Exception _exception) {
		  logger.error(_exception);
	  }
	  return order;
  }
  
  public boolean getReceiptPaymentOrderById(Long orderId){
	 boolean result=false;
	  try{
		  int x=0;
		  Integer integer = Integer.valueOf(x);
		//  System.out.println("before the query");
		  Query query = entityManager.createNativeQuery("select * from  receipt_payment r where r.ref_no=:orderId");
		//  System.out.println("after the query");
		  if(query!=null ){
			     query.setParameter("orderId", orderId);
			     BigInteger biginteger=(BigInteger)query.getSingleResult();
			     BigInteger y = BigInteger.valueOf(x);
			   //  System.out.println("biginteger"+biginteger);
			  //   System.out.println("y :"+y);
			     if(biginteger.compareTo(y)==0)
		         return result=true;
			 }else{
				 return result=false;
			 }

	  }catch (Exception _exception) {
		  logger.error(_exception);
	  }
	  return result=false;
  }
  public List<OrderTrxnDetail> getTransactionByOrderId(Long orderId){
	TypedQuery<OrderTrxnDetail> query = entityManager.createQuery(
		  "SELECT t FROM OrderTrxnDetail t WHERE t.orderId = :orderId", OrderTrxnDetail.class);
	List<OrderTrxnDetail> trxnDetails = null;
	try{
		if (query !=null)
		trxnDetails = query.setParameter("orderId", orderId).getResultList();
	}catch (Exception _exception) {
		logger.error(_exception);
	}
    return trxnDetails;
  }
  
  public List<JobAllocation> getJobAllocationByOrderId(Long orderId){
		TypedQuery<JobAllocation> query = entityManager.createQuery(
			  "SELECT j FROM JobAllocation j WHERE j.id.orderId = :orderId", JobAllocation.class);
		List<JobAllocation> jobAllocations = null;
		try{
			jobAllocations = query.setParameter("orderId", orderId).getResultList();
		}catch (Exception _exception) {
			logger.error(_exception);
		}
	    return jobAllocations;
  }
  
  public List<JobAllocation> getJobAllocationByOrderId(List<Long> orderIds){
		TypedQuery<JobAllocation> query = entityManager.createQuery(
			  "SELECT j FROM JobAllocation j WHERE j.id.orderId = :orderId", JobAllocation.class);
		List<JobAllocation> jobAllocations = null;
		try{
			jobAllocations = query.setParameter("orderId", orderIds).getResultList();
		}catch (Exception _exception) {
			logger.error(_exception);
		}
	    return jobAllocations;
}
  
  public JobAllocation getJobAllocationByOrderId(Long orderId,Long custId){
		TypedQuery<JobAllocation> query = entityManager.createQuery(
			  "SELECT j FROM JobAllocation j WHERE j.id.orderId = :orderId and j.custId=:custId", JobAllocation.class);
		JobAllocation jobAllocation = null;
		try{
			query.setParameter("orderId", orderId);
			query.setParameter("custId", custId);
			jobAllocation = query.getSingleResult();
		}catch (Exception _exception) {
			logger.error(_exception);
		}
	    return jobAllocation;
}
  
  // to count the job open count details in DASHBOARD
  
  public List<JobAllocation> getJobAllocationByEmployeeId(Long empId){
		TypedQuery<JobAllocation> query = entityManager.createQuery(
			  "SELECT j FROM JobAllocation j WHERE j.custId = :empId and j.statusId=102", JobAllocation.class);
		List<JobAllocation> jobAllocations = null;
		try{
			jobAllocations = query.setParameter("empId", empId).getResultList();
		}catch (Exception _exception) {
			logger.error(_exception);
		}
	    return jobAllocations;
}
  
  
  
  
  public List<Order> getOrders(Order order){
	  List<Order> orders = new ArrayList<Order>();
	  try{
		  StringBuffer wClause = new StringBuffer();
		  
		  if(!StringUtils.isEmpty(order.getOrderId()) && order.getOrderId() !=0){
			  wClause.append("o.orderId=:orderId");
		  }
			  
		  if(order.getOrderDate()!=null && order.getDueDate()!=null){
			  if(wClause.toString().length() > 0){
				  wClause.append(" AND ");
				  wClause.append("o.orderDate BETWEEN :statDate and :endDate");
//				  wClause.append("o.orderDate >=:statDate and o.orderDate <=:endDate");
			  }else{
				  wClause.append("o.orderDate BETWEEN :statDate and :endDate");
//				  wClause.append("o.orderDate >=:statDate and o.orderDate <=:endDate");
			  }
		  }
		  
		  if(!StringUtils.isEmpty(order.getCustType()) && order.getCustType()!=0){
			  if(wClause.toString().length() > 0){
				  wClause.append(" AND ");
				  wClause.append("o.custType=:custType");
			  }else{
				  wClause.append("o.custType=:custType");  
			  }
		  }
		  
		  if(!StringUtils.isEmpty(order.getCustName())){
			  if(wClause.toString().length() > 0){
				  wClause.append(" AND ");
				  wClause.append("o.custName=:custName");
			  }else{
				  wClause.append("o.custName=:custName");  
			  }
		  }
		  
		  if(!StringUtils.isEmpty(order.getStatus()) && order.getStatus()!=0){
			  if(wClause.toString().length() > 0){
				  wClause.append(" AND ");
				  wClause.append("o.status=:status");
			  }else{
				  wClause.append("o.status=:status");  
			  }
		  }
		  
		  logger.info("where clause to search orders ::: " + wClause.toString());
		 
		  
		  TypedQuery<Order> query = null;
		  if(!StringUtils.isEmpty(wClause.toString().trim())){
			  StringBuffer sQuery = new StringBuffer("SELECT o FROM Order o WHERE ");
			  sQuery.append(wClause);
			  query = entityManager.createQuery(sQuery.toString(), Order.class);
			  
			  if(!StringUtils.isEmpty(order.getOrderId()) && order.getOrderId() !=0){
				  query.setParameter("orderId", order.getOrderId());
			  }
			  
			  if(order.getOrderDate() !=null && order.getDueDate()!=null){
				  query.setParameter("statDate", order.getOrderDate());
				  query.setParameter("endDate" , DateUtils.toDate(order.getStrDueDate().concat(" 11:59 PM")));
			  }
			  if(!StringUtils.isEmpty(order.getCustType()) && order.getCustType()!=0){
				  query.setParameter("custType", order.getCustType());
			  }
			  if(!StringUtils.isEmpty(order.getCustName())){
				  query.setParameter("custName", order.getCustName());
			  }
			  
			  if(!StringUtils.isEmpty(order.getStatus()) && order.getStatus()!=0){
				  query.setParameter("status", order.getStatus());  
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
  
  public List<JobAllocation> getJobs(long employeeId){
	  List<JobAllocation> jobAllocations = null;
	  try{
			TypedQuery<JobAllocation> query = entityManager.createQuery(
					  "SELECT j FROM JobAllocation j WHERE j.custId =:employeeId and ((j.updatedDate>:jDate and j.statusId=104) or (j.statusId <> 104 and j.statusId<>106))", JobAllocation.class);
			query.setParameter("employeeId", employeeId);
		/*	query.setParameter("jDate", DateUtils.toDate(DateUtils.beforeDateStr(3)));*/
			query.setParameter("jDate", DateUtils.toDate(DateUtils.beforeDateStr(1)));
			jobAllocations = query.getResultList();
			//System.out.println("jobAllocations  SIZE :"+jobAllocations.size());
	  }catch (Exception _exception) {
		  logger.error("getJobs :: "+ _exception);
	  }
	  return jobAllocations;
  }
  
  public List<Order> getOrders(List<Long> orderIds){
	  List<Order> orders = null;
	  try{
		//  System.out.println("getOrders starting");
			TypedQuery<Order> query = entityManager.createQuery(
					  "SELECT o FROM Order o WHERE o.id in:orderIds and o.status !='106'", Order.class);
			//and o.status !='106'
			orders = query.setParameter("orderIds", orderIds).getResultList();
	  }catch (Exception _exception) {
		  logger.error("getOrders :: "+ _exception);
	  }
	//  System.out.println("getOrders ending with size :"+orders.size());
	  return orders;
  }
  
  public List<Order> getCustomerOrders(List<Long> custIds){
	  List<Order> orders = null;
	  try{
			TypedQuery<Order> query = entityManager.createQuery(
					  "SELECT o FROM Order o WHERE o.custId in:custIds", Order.class);
			orders = query.setParameter("custIds", custIds).getResultList();
	  }catch (Exception _exception) {
		  logger.error("getOrders :: "+ _exception);
	  }
	  return orders;
  }
  
  public List<Customer> getUndeliveredCustomerOrders(List<Long> custIds){
	  List<Customer> customers = null;
	  try{
			TypedQuery<Customer> query = entityManager.createQuery(
					  "SELECT o FROM Customer o WHERE o.custId in:custIds", Customer.class);
			customers = query.setParameter("custIds", custIds).getResultList();
	  }catch (Exception _exception) {
		  logger.error("getOrders :: "+ _exception);
	  }
	  return customers;
  }
  
  public List<String> getOrderCustomers(String searchTerm){
	  	TypedQuery<String> query = entityManager.createQuery(
		        "SELECT DISTINCT o.custName FROM Order o WHERE o.custName LIKE :name", String.class);
	  	List<String> orders = null;
		try{
			orders = query.setParameter("name", "%"+searchTerm+"%").getResultList();
		}catch (Exception _exception) {
			logger.error(_exception);
		}
	    return orders;
	}
  
  public Integer deleteOrderItems(Long orderId,long productId){
	  Query query = entityManager.createQuery("DELETE from ProductItem p WHERE p.productId =:productId and p.orderId=:orderId");
	  query.setParameter("productId", productId);
	  query.setParameter("orderId", orderId);
	  return query.executeUpdate();
  }
  // created with filtering cancelled orders
 /* public List<Order> getCustomerOrderswithcancel(long custId){
	  List<Order> orders = null;
	  try{
			TypedQuery<Order> query = entityManager.createQuery(
					  "SELECT o FROM Order o WHERE o.custId =:custId and o.status !='106'", Order.class);
			orders = query.setParameter("custId", custId).getResultList();
	  }catch (Exception _exception) {
		  logger.error("getOrders :: "+ _exception);
	  }
	  return orders;
  }
  */
  
  public List<Order> getCustomerOrders(long custId){
	  List<Order> orders = null;
	  try{
			TypedQuery<Order> query = entityManager.createQuery(
					  "SELECT o FROM Order o WHERE o.custId =:custId and o.status !='106'", Order.class);
			orders = query.setParameter("custId", custId).getResultList();
	  }catch (Exception _exception) {
		  logger.error("getOrders :: "+ _exception);
	  }
	  return orders;
  }
 
  public List<OrderTrxnDetail> getOrderTrxnDetails(List<Long> orderIds){
	List<OrderTrxnDetail> orderTrxnDetails = null;
	  try{
		  if(orderIds!=null && !orderIds.isEmpty()){
			  TypedQuery<OrderTrxnDetail> query = entityManager.createQuery("SELECT o FROM OrderTrxnDetail o WHERE o.orderId in:orderIds", OrderTrxnDetail.class);
			  query.setParameter("orderIds", orderIds);
			  orderTrxnDetails = query.getResultList();
		  }
	  }catch (Exception _exception) {
		  logger.error("getOrderTrxnDetails :: " + _exception);
	  }
	  return orderTrxnDetails;
  }
  
  public  <T> Boolean saveOrUpdate(T t){
	  entityManager.merge(t);
	return true;
  }

  // for  Order Vs. despatch report
 /* public List<OrderTrxnDetail>getOrderTrxnByDate(Report report){
	  List<OrderTrxnDetail> otd =null;
	  try{
		   String fromdate= DateUtils.toDate(report.getFromDate());
		   String todate= DateUtils.toDate(report.getToDate());
		   fromdate = fromdate.substring(0,10);
		   fromdate = getyyyymmdd(fromdate);
		   todate = todate.substring(0,10);
		   todate = getyyyymmdd(todate);
		  if(report !=null){
			  //Query query = entityManager.createNativeQuery("SELECT * FROM order_trxn_detail ot, details d WHERE ot.order_id in:d.orderId", OrderTrxnDetail.class);
			  
			  Query query = entityManager.createNativeQuery("SELECT *  FROM order_trxn_detail where careate_date between :fromdate and :todate",OrderTrxnDetail.class);
			  query.setParameter("fromdate",report.getFromDate());
			  query.setParameter("fromdate",report.getToDate());
			  otd=query.getResultList(); 
			  if (otd != null)
			  {
				  System.out.println("Ordertrxn.size() :"+otd.size());
			  }
		  }
	  }
	  catch(Exception e){logger.error("getOrderTrxnByDate :: " + e);}
	  return otd;
  }
  
  
  
  public List<Order> getOrdersByDate(Report report){
	  
	  List<Order> orders =null;
	  try{
		   String fromdate= DateUtils.toDate(report.getFromDate());
		   String todate= DateUtils.toDate(report.getToDate());
		   fromdate = fromdate.substring(0,10);
		   fromdate = getyyyymmdd(fromdate);
		   todate = todate.substring(0,10);
		   todate = getyyyymmdd(todate);
		  if(report !=null){
			  //Query query = entityManager.createNativeQuery("SELECT * FROM order_trxn_detail ot, details d WHERE ot.order_id in:d.orderId", OrderTrxnDetail.class);
			  
			  Query query = entityManager.createNativeQuery("SELECT *  FROM orders where order_date  between :fromDate and :todDate",Order.class);
			  query.setParameter("fromDate",report.getFromDate());
			  query.setParameter("toDate",report.getToDate());
			  orders=query.getResultList(); 
			  if (orders != null)
			  {
				  System.out.println("orders.size() :"+orders.size());
			  }
		  }
	  }catch(Exception e){logger.error("getOrdersByDate  :: " + e);}
	 return orders; 
  }
  */
  
   // for customer report balance
 @Transactional(noRollbackFor = Exception.class)
  public List<OrderTrxnDetail> getOrderTrxnBalance(Report report){
		List<OrderTrxnDetail> trxnDetail = null;
		
		  try{
			  String fromdate= DateUtils.toDate(report.getFromDate());
			   fromdate = fromdate.substring(0,10);
			  fromdate = getyyyymmdd(fromdate);
			   Long custid = report.getCustId();
			 // System.out.println("Customer Id from final call :"+custid);
			  if(report !=null){
				  //Query query = entityManager.createNativeQuery("SELECT * FROM order_trxn_detail ot, details d WHERE ot.order_id in:d.orderId", OrderTrxnDetail.class);
				  
	/*		  Query query = entityManager.createNativeQuery("SELECT *  FROM order_trxn_detail tx, orders dc where substring(dc.order_date,1,10) <:fromdate and dc.cust_id =:custid and tx.order_id = dc.order_id  and dc.status !='106'",OrderTrxnDetail.class);*/
				 
			  Query query = entityManager.createNativeQuery("SELECT *  FROM order_trxn_detail tx, orders dc where tx.created_date <:fromdate and dc.cust_id =:custid and tx.order_id = dc.order_id  and dc.status !='106'",OrderTrxnDetail.class);
				  query.setParameter("custid",report.getCustId());
				  query.setParameter("fromdate",report.getFromDate());
				//  System.out.println("report.getFromDate() " + report.getFromDate());
				  trxnDetail=query.getResultList(); 
				 // System.out.println("no of orders: "+trxnDetail.size());
			 }
		  }catch (Exception _exception) {
			  logger.error("getOrderTrxnBalance  :: " + _exception);
		  }
		 /* for(int i=0; i<trxnDetail.size(); i++){
		  System.out.println("trxnDetail.get(i).getCreatedDate() " + trxnDetail.get(i).getBalance());
		  }*/
		  return trxnDetail;
	  }
 
 
//for customer report balance
@Transactional(noRollbackFor = Exception.class)
public List<OrderTrxnDetail> getOrderTrxnFromDateTOEndDate(Report report){
		List<OrderTrxnDetail> trxnDetail = null;
		
		  try{
			  String fromdate= DateUtils.toDate(report.getFromDate());
			   fromdate = fromdate.substring(0,10);
			  fromdate = getyyyymmdd(fromdate);
			  String todate= DateUtils.toDate(report.getToDate());
			   todate = todate.substring(0,10);
			  todate = getyyyymmdd(todate);
			  
			   Long custid = report.getCustId();
			 // System.out.println("Customer Id from final call :"+custid);
			  if(report !=null){
				  //Query query = entityManager.createNativeQuery("SELECT * FROM order_trxn_detail ot, details d WHERE ot.order_id in:d.orderId", OrderTrxnDetail.class);
				  
	/*		  Query query = entityManager.createNativeQuery("SELECT *  FROM order_trxn_detail tx, orders dc where substring(dc.order_date,1,10) <:fromdate and dc.cust_id =:custid and tx.order_id = dc.order_id  and dc.status !='106'",OrderTrxnDetail.class);*/
				 
			  Query query = entityManager.createNativeQuery("SELECT *  FROM order_trxn_detail tx, orders dc where   tx.createdDate BETWEEN :fromdate and :todate and dc.cust_id =:custid and tx.order_id = dc.order_id  and dc.status !='106'",OrderTrxnDetail.class);
				  query.setParameter("custid",report.getCustId());
				  query.setParameter("fromdate",report.getFromDate());
				//  System.out.println("report.getFromDate() " + report.getFromDate());
				  trxnDetail=query.getResultList(); 
				 // System.out.println("no of orders: "+trxnDetail.size());
			 }
		  }catch (Exception _exception) {
			  logger.error("getOrderTrxnBalance  :: " + _exception);
		  }
		 /* for(int i=0; i<trxnDetail.size(); i++){
		  System.out.println("trxnDetail.get(i).getCreatedDate() " + trxnDetail.get(i).getBalance());
		  }*/
		  return trxnDetail;
	  }
 
 // for customer Vs Business report
 public List<Customer> getCustomerOrders(List <Customer> cd, String days){
	  List<Order> orders = null;
	  int noOfDays=0;
	  long differ;
	  Long custId=null;
	  int difference;
	  noOfDays=Integer.valueOf(days);
	  //System.out.println("no of Days "+ noOfDays);
		 //date to String 
		  Date date= new Date();
		  SimpleDateFormat formatter=new SimpleDateFormat("yyyy/mm/dd");  
		
		  
	  //System.out.println("date is :"+date);
	  String today= DateUtils.toDate(date);
	  today = today.substring(0,10);
	  today = getyyyymmdd(today);
	 // System.out.println("Today's date is :"+today);
	   try{  
		   for (Customer customers: cd){
				   // System.out.println("cd.get(index)"+ cd.indexOf(cd));
				    custId=customers.getCustId();
				    Query query = entityManager.createNativeQuery("select * FROM orders o WHERE o.cust_id =:custId order by order_date desc limit 1", Order.class);
				  	
				 try{
				  	if (query !=null ){
				  		   orders = query.setParameter("custId", custId).getResultList();
						  	String recdate= DateUtils.toDate(orders.get(0).getOrderDate());
						   	recdate = recdate.substring(0,10);
						  	recdate = getyyyymmdd(recdate);
						    //System.out.println("record's date is :"+recdate);
						    differ= gapdays(today,recdate);
						  
							difference= (int) Math.abs(differ);
							//System.out.println("Difference days :"+difference);
							customers.setCreditDays(difference);
				  }
				 }catch(Exception e){}
			 }     
	  }catch (Exception _exception) {
		  logger.error("get No of Days from Order :: "+ _exception);
	  }
	   //System.out.println("value of get customers Orders CD :"+cd.size());
	  return cd;
 }
 
 //-------------------------------------------------------------------------------------------
 //gapdays("2019/08/01","2019/08/30")) ok,thanks.
 //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
 public int gapdays(String sdt, String cdt)
 {
   int vv = 0;
   String dateStart = sdt;
   String dateStop = cdt;
   //HH converts hour in 24 hours format (0-23), day calculation
   SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
   Date d1 = null;
   Date d2 = null;
   try 
   {
     d1 = format.parse(dateStart);
     d2 = format.parse(dateStop);
     //in milliseconds
     long diff = d2.getTime() - d1.getTime();
     long diffDays = diff / (24 * 60 * 60 * 1000);
     vv = (int) diffDays;
    // System.out.print(diffDays + " days, ");
   } 
   catch (Exception e) 
   {
     e.printStackTrace();
   }
   return (vv);
 }
 
// added for check list printing mode of delivery  
 
 public  List<DeliveryType> getDeliveryType(String orderId){
	 List<DeliveryType> dt= null;
	 Query query = entityManager.createNativeQuery("select * from orders, delivery_type dt where orders.order_id=:orderId and orders.delivery_type_id=dt.id;", DeliveryType.class);
	 if (query !=null ){
		  dt = query.setParameter("orderId", orderId).getResultList();	
	 }
	 return dt;
 }
 
 
 
  public String getyyyymmdd(String st)
  {
    String yyyy = st.substring(st.length() - 4, st.length());
    String dd = st.substring(0, st.indexOf("/"));
    String mm = st.substring(st.indexOf("/") + 1, st.lastIndexOf("/"));
    return yyyy + "/" + mm + "/" + dd;
  }
  
  public String getddmmyyyy(String st)
  {
    String yyyy = st.substring(st.length() - 4, st.length());
    String dd = st.substring(0, st.indexOf("/"));
    String mm = st.substring(st.indexOf("/") + 1, st.lastIndexOf("/"));
    return dd + "/" + mm + "/" + yyyy;
  }
  
}
  
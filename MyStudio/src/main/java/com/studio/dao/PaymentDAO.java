package com.studio.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import com.studio.domain.ExpenseType;
import com.studio.domain.OrderTrxnDetail;
import com.studio.domain.Purchase;
import com.studio.domain.ReceiptPayment;
import com.studio.domain.Report;
import com.studio.domain.Supplier;
import com.studio.domain.Voucher;
import com.studio.domain.VoucherPayment;
//import com.studio.service.impl.ExpenceType;
import com.studio.utils.DateUtils;
import com.studio.domain.Receipt;


@Repository
@Transactional
@EnableTransactionManagement
public class PaymentDAO {
  @PersistenceContext
  EntityManager entityManager;
  Logger logger = Logger.getLogger(PaymentDAO.class);
  
  
  @Transactional(readOnly=false)
  public <T> Boolean  save(T t){
	  entityManager.persist(t);
	  entityManager.flush();
	return true;
  }
  
  public <T> Boolean  saveOrUpdate(T t){
	  entityManager.merge(t);
	return true;
  }
  
  public List<Supplier> getSuppliers(){
	  	TypedQuery<Supplier> query = entityManager.createQuery("SELECT s FROM Supplier s", Supplier.class);
	  	List<Supplier> suppliers = null;
		try{
			suppliers = query.getResultList();
		}catch (Exception _exception) {
			logger.error(_exception);
		}
	    return suppliers;
  }
  
  public Supplier getSupplier(Long supId){
	  	TypedQuery<Supplier> query = entityManager.createQuery("SELECT s FROM Supplier s where s.supId =:supId", Supplier.class);
	  	Supplier suppliers = null;
		try{
			query.setParameter("supId", supId);
			suppliers = query.getSingleResult();
		}catch (Exception _exception) {
			logger.error(_exception);
		}
		//System.out.println("supplier size"+ suppliers.getMobileNo());
	    return suppliers;
}
  public List<Supplier> getSuppliersWithBalance(){
	  	TypedQuery<Supplier> query = entityManager.createQuery("SELECT s FROM Supplier s", Supplier.class);
	  	List<Supplier> suppliers = null;
		try{
			suppliers = query.getResultList();
		}catch (Exception _exception) {
			logger.error(_exception);
		}
	    return suppliers;
  }
  
  public List<Purchase> getPurchaseBySupplierId(Long supId){
	  List<Purchase> purchases=null;
	  try{
		  TypedQuery<Purchase> query = entityManager.createQuery("SELECT p FROM Purchase p where p.supId=:supId", Purchase.class);
		  query.setParameter("supId", supId);
		  purchases = query.getResultList();
	  }catch (Exception _exception) {
		  logger.error(_exception);
		  _exception.printStackTrace();
	  }
	  return purchases;
  }
  
  public List<Purchase> getPurchaseBySupplierId(Long supId,String invoiceNumber){
	  List<Purchase> purchases=null;
	  try{
		  TypedQuery<Purchase> query = entityManager.createQuery("SELECT p FROM Purchase p where p.supId=:supId and p.invoiceNumber=:invoiceNumber", Purchase.class);
		  query.setParameter("supId", supId);
		  query.setParameter("invoiceNumber", invoiceNumber);
		  purchases = query.getResultList();
	  }catch (Exception _exception) {
		  logger.error(_exception);
		  _exception.printStackTrace();
	  }
	  return purchases;
  }
  
/*  public List<PurchasePayment> getPurchasePaymentByPurId(Long purId){
	  List<PurchasePayment> purchasedGoods=null;
	  try{
		  TypedQuery<PurchasePayment> query = entityManager.createQuery("SELECT p FROM PurchasePayment p where p.purchaseId=:purId", PurchasePayment.class);
		  query.setParameter("supId", purId);
		
		  purchasedGoods = query.getResultList();
	  }catch (Exception _exception) {
		  logger.error(_exception);
		  _exception.printStackTrace();
	  }
	  return purchasedGoods;
  }
  */
  
  public Purchase getPurchaseById(Long purchaseId){
	  Purchase purchase=null;
	  try{
		  purchase = entityManager.find(Purchase.class, purchaseId);
	  }catch (Exception _exception) {
		  logger.error(_exception);
		  _exception.printStackTrace();
	  }
	  return purchase;
  }
  
  public OrderTrxnDetail getOrderTransactionByCustIdOrderId(long orderId){
	  	TypedQuery<OrderTrxnDetail> query = entityManager.createQuery("SELECT o FROM OrderTrxnDetail o WHERE o.orderId =:orderId", OrderTrxnDetail.class);
	  	OrderTrxnDetail order = null;
		try{
			query.setParameter("orderId", orderId);
			order = query.getSingleResult();
		}catch (Exception _exception) {
			logger.error(_exception);
		}
	    return order;
  }
  
 public List<VoucherPayment> getVoucherPayments(String refNo){
	 List<VoucherPayment> voucherPayments = new ArrayList<VoucherPayment>();
	 try{
		 TypedQuery<VoucherPayment> query = entityManager.createQuery("SELECT v FROM VoucherPayment v WHERE v.refNo=:refNo", VoucherPayment.class);
		 query.setParameter("refNo", refNo);
		 voucherPayments = query.getResultList();
	 }catch (Exception _exception) {
		logger.error("getVoucherPayments :: "+_exception);
	}
	return voucherPayments;
 }
 
 public List<Voucher> getVouchers(int supId){
	 List<Voucher> vouchers = new ArrayList<Voucher>();
	 try{
		 TypedQuery<Voucher> query = entityManager.createQuery("SELECT v FROM Voucher v WHERE v.expenseId=:supId", Voucher.class);
		 query.setParameter("supId", supId);
		 vouchers = query.getResultList();
	 }catch (Exception _exception) {
		logger.error("getVoucherPayments :: "+_exception);
	}
	return vouchers;
 }
 
	 public List<VoucherPayment> getVoucherPayments(List<Long> vIds){
		 List<VoucherPayment> voucherPayments = new ArrayList<VoucherPayment>();
		 try{
			 TypedQuery<VoucherPayment> query = entityManager.createQuery("SELECT v FROM VoucherPayment v WHERE v.voucherId in:vIds", VoucherPayment.class);
			 
			 if(query !=null){
				 query.setParameter("vIds", vIds);
				 voucherPayments = query.getResultList();
			 }
		 }catch (Exception _exception) {
			logger.error("getVoucherPayments list voucher ids :: "+_exception);
		}
		return voucherPayments;
	 }
	 
	 
	 public VoucherPayment getVoucherPayment(Long vId){
		 VoucherPayment voucherPayment = null;
		 try{
			 if (vId !=null){
				 TypedQuery<VoucherPayment> query = entityManager.createQuery("SELECT v FROM VoucherPayment v WHERE v.voucherId =:vId", VoucherPayment.class);
				 
				 if(query !=null){
					 query.setParameter("vId", vId);
				 	 voucherPayment = query.getSingleResult();
				 }
			 }
		 }catch (Exception _exception) {
			logger.error("getVoucherPayments list voucher ids :: "+_exception);
		}
		return voucherPayment;
	 }
 
	 public List<ReceiptPayment> getReceiptPaymentsByOrderId(String orderId){
		 
		 List<ReceiptPayment> receiptPayments = null;
		 try{
			 TypedQuery<ReceiptPayment> query = entityManager.createQuery("SELECT r FROM ReceiptPayment r WHERE r.refNo =:orderId", ReceiptPayment.class);
			if(query !=null){
			 query.setParameter("orderId", orderId);
			 receiptPayments = query.getResultList();
			}
		 }catch (Exception _exception) {
			 logger.error(_exception);
		 }
		 return receiptPayments;
	 }
 
 	public List<Supplier> getSupplierByIds(List<Long> supIds){
	  	TypedQuery<Supplier> query = entityManager.createQuery(
		        "SELECT s FROM Supplier s WHERE s.supId in :supIds", Supplier.class);
	  	List<Supplier> suppliers = null;
		try{
			query.setParameter("supIds", supIds);
			suppliers = query.getResultList();
		}catch (Exception _exception) {
			logger.error(_exception);
			
		}
	    return suppliers;
	}
 	
 	// for customer opening balance report
 	
 	public List<Receipt> getReceiptByCustomer(Report reports){
		   //List<Receipt> receipt = new ArrayList<Receipt>();
		   List<Receipt> receiptDetail=null;
 			  try{
 				  String fromdate= DateUtils.toDate(reports.getFromDate());
 				 
 				  fromdate = fromdate.substring(0,10);
 				  fromdate = getyyyymmdd(fromdate);
 				  Long custId=reports.getCustId();
 				 
 				  Query query = null;
 				  //SUM(PAY_AMOUNT) 
 				 	query =  entityManager.createNativeQuery("SELECT * FROM Receipt WHERE cust_id =:custId and receipt_date <:fromDate", Receipt.class);
 					query.setParameter("fromDate", reports.getFromDate());
 					query.setParameter("custId" , reports.getCustId());
 					//query.setParameter("payAmount", reports.getPayAmount());
 				  if(query!=null){
 					 receiptDetail =  query.getResultList();
 					  
 				  }
 			  }catch (Exception _exception) {
 				  logger.error(_exception);
 				  _exception.printStackTrace();
 			  }
 			  return receiptDetail;
 		  }
 	
 	 public Double getOSReceiptByCustomer(Report reports){
		   //List<Receipt> receipt = new ArrayList<Receipt>();
		   Double receipt=null;
			  try{
				  String fromdate= DateUtils.toDate(reports.getFromDate());
				 
				  fromdate = fromdate.substring(0,10);
				  fromdate = getyyyymmdd(fromdate);
				  Long custId=reports.getCustId();
				 
				  Query query = null;
				  //SUM(PAY_AMOUNT) 
				 	/*query =  entityManager.createNativeQuery("SELECT * FROM Receipt WHERE cust_id =:custId and receipt_date <:fromDate", Receipt.class);*/
				  query =  entityManager.createNativeQuery("SELECT sum(pay_amount) FROM Receipt WHERE cust_id =:custId and receipt_date <:fromDate");
				  query.setParameter("fromDate", reports.getFromDate());
					query.setParameter("custId" , reports.getCustId());
					//query.setParameter("payAmount", reports.getPayAmount());
				  if(query!=null){
					  receipt = (Double)query.getSingleResult();
					  
				  }
			  }catch (Exception _exception) {
				  logger.error(_exception);
				  _exception.printStackTrace();
			  }
			  return receipt;
		  }
 	
 	
 	
 	
 	public List<ExpenseType> getExpenseType() {
 	
	  	List<ExpenseType> exptype = null;
		try{
			Query query = null;
			  
			 	query =  entityManager.createNativeQuery("SELECT * FROM expense_type", ExpenseType.class);
			 	if(query!=null){
			 		exptype =  query.getResultList();
				  
			  }
		}catch (Exception _exception) {
			logger.error(_exception);
		}
	    return exptype;
	}
 	
 	String getyyyymmdd(String st)
	  {
	    String yyyy = st.substring(st.length() - 4, st.length());
	    String dd = st.substring(0, st.indexOf("/"));
	    String mm = st.substring(st.indexOf("/") + 1, st.lastIndexOf("/"));
	    return yyyy + "/" + mm + "/" + dd;
	  }

	
}

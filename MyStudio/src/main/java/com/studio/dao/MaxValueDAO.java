/**
 * 
 */
package com.studio.dao;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.studio.domain.Address;
import com.studio.domain.Customer;
import com.studio.domain.Employee;
import com.studio.domain.Order;
import com.studio.domain.OrderTrxnDetail;
import com.studio.domain.Product;
import com.studio.domain.ProductItem;
import com.studio.domain.Receipt;
import com.studio.domain.Supplier;
import com.studio.domain.Voucher;

/**
 * @author ezhilraja_k
 *
 */

@Repository
@Transactional
@EnableTransactionManagement
public class MaxValueDAO {
	@PersistenceContext
	EntityManager entityManager;
	Logger logger = Logger.getLogger(UserDAO.class);
	  
	  public Customer getLastCustomer(){
		TypedQuery<Customer> query = entityManager.createQuery("SELECT c FROM Customer c ORDER BY c.custId DESC", Customer.class);
		Customer customer = null;
		try{
			customer = query.setMaxResults(1).getSingleResult();
		}catch (Exception _exception) {
			logger.error(_exception);
		}
	    return customer;
	  }
	  
	  public Employee getLastEmployee(){
			TypedQuery<Employee> query = entityManager.createQuery("SELECT e FROM Employee e ORDER BY e.empId DESC", Employee.class);
			Employee employee = null;
			try{
				employee = query.setMaxResults(1).getSingleResult();
			}catch (Exception _exception) {
				logger.error(_exception);
			}
		    return employee;
		  }
	  
	  public Order getLastOrder(){
		TypedQuery<Order> query = entityManager.createQuery("SELECT o FROM Order o ORDER BY o.orderId DESC", Order.class);
		Order orderDetail = null;
		try{
			orderDetail = query.setMaxResults(1).getSingleResult();
		}catch (Exception _exception) {
			logger.error(_exception);
		}
	    return orderDetail;
	  }
	  
	  public Address getLastAddress(){
		TypedQuery<Address> query = entityManager.createQuery("SELECT a FROM Address a ORDER BY a.addrId DESC", Address.class);
		Address address = null;
		try{
			address = query.setMaxResults(1).getSingleResult();
		}catch (Exception _exception) {
			logger.error(_exception);
		}
	    return address;
	  }
	  
	  public Product getLastProduct(){
		TypedQuery<Product> query = entityManager.createQuery("SELECT p FROM Product p ORDER BY p.productId DESC", Product.class);
		Product product = null;
		try{
			product= query.setMaxResults(1).getSingleResult();
		}catch (Exception _exception) {
			logger.error(_exception);
		}
	    return product;
	  }
	  
	  public ProductItem getLastProductItem(){
		TypedQuery<ProductItem> query = entityManager.createQuery("SELECT p FROM ProductItem p ORDER BY p.prodItemId DESC", ProductItem.class);
		ProductItem productItem = null;
		try{
			productItem= query.setMaxResults(1).getSingleResult();
		}catch (Exception _exception) {
			logger.error(_exception);
		}
	    return productItem;
	  }
	  
	  public OrderTrxnDetail getLastTransaction(){
		TypedQuery<OrderTrxnDetail> query = entityManager.createQuery("SELECT o FROM OrderTrxnDetail o ORDER BY o.trxnId DESC", OrderTrxnDetail.class);
		OrderTrxnDetail orderTrxnDetail = null;
		try{
			orderTrxnDetail = query.setMaxResults(1).getSingleResult();
		}catch (Exception _exception) {
			logger.error(_exception);
		}
	    return orderTrxnDetail;
	  }
	  
	  public Voucher getLastVoucher(){
		TypedQuery<Voucher> query = entityManager.createQuery("SELECT v FROM Voucher v ORDER BY v.voucherId DESC", Voucher.class);
		Voucher voucher = null;
		try{
			voucher = query.setMaxResults(1).getSingleResult();
		}catch (Exception _exception) {
			logger.error(_exception);
		}
	    return voucher;
	  }
	  
	  public Supplier getLastSupplier(){
			TypedQuery<Supplier> query = entityManager.createQuery("SELECT s FROM Supplier s ORDER BY s.supId DESC", Supplier.class);
			Supplier supplier = null;
			try{
				supplier = query.setMaxResults(1).getSingleResult();
			}catch (Exception _exception) {
				logger.error(_exception);
			}
		    return supplier;
	  }
	  
	  public Receipt getLastReceipt(){
			TypedQuery<Receipt> query = entityManager.createQuery("SELECT r FROM Receipt r ORDER BY r.receiptId DESC", Receipt.class);
			Receipt supplier = null;
			try{
				supplier = query.setMaxResults(1).getSingleResult();
			}catch (Exception _exception) {
				logger.error(_exception);
			}
		    return supplier;
	  }
	  
	  
	  public Long getMaxAddrdId(){
		  Address address = getLastAddress();
		  return address!=null?address.getAddrId()+1:1;
	  }
	  
	  public Long getMaxEmpId(){
		  Employee employee = getLastEmployee();
		  return employee!=null ? employee.getEmpId()+1:1;
	  }
	  
	  public Long getMaxCustId(){
		  Customer customer = getLastCustomer();
		  return customer!=null ? customer.getCustId()+1:1;
	  }
	  
	  public Long getMaxProductId(){
		  Product product = getLastProduct();
		  return product!=null?product.getProductId()+1:1;
	  }
	  public Long getMaxProductItemId(){
		  ProductItem productitem = getLastProductItem();
		  System.out.println("productitem Id... :"+productitem.getProdItemId());
		  return productitem!=null?productitem.getProdItemId()+1:1;
	  }
	  
	  public Long getMaxOrderId(){
		  Order order = getLastOrder();
		  return order!=null?order.getOrderId()+1:1;
	  }
	  
	  public Long getMaxTrxnId(){
		  OrderTrxnDetail orderTrxnDetail= getLastTransaction();
		  return orderTrxnDetail!=null?orderTrxnDetail.getTrxnId()+1:1;
	  }
	  public Long getMaxSupplierId(){
		  Supplier supplier = getLastSupplier();
		  return supplier!=null?supplier.getSupId()+1:1;
	  }
	  
	  public Long getMaxVoucherId(){
		  Voucher voucher = getLastVoucher();
		  return voucher!=null ? voucher.getVoucherId()+1:1;
	  }
	  
	  public Long getMaxReceiptId(){
		  Receipt receipt = getLastReceipt();
		  return receipt!=null ? receipt.getReceiptId()+1:1;
	  }
}


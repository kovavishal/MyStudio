package com.studio.dao;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.studio.domain.Address;
import com.studio.domain.Area;
import com.studio.domain.Customer;
import com.studio.domain.Employee;
import com.studio.domain.ItemSizeRate;
import com.studio.domain.Order;
import com.studio.domain.Product;
import com.studio.domain.ProductItem;
import com.studio.domain.Supplier;

@Repository
@Transactional
//@EnableTransactionManagement
public class CustomerDAO {
  private static final Object CustomerId = null;

@PersistenceContext
  EntityManager entityManager;
  
  Logger logger = Logger.getLogger(CustomerDAO.class);
  
  @Transactional(readOnly=false)
  public Boolean save(Customer customer){
	  entityManager.merge(customer);
	return true;
  }
  
  public Customer getCustomerById(Long customerId){
	  Customer customer =  entityManager.find(Customer.class, customerId);
	  return customer;
  }
  
  
  public Customer getCustomer(String userName){
	  TypedQuery<Customer> query = entityManager.createQuery(
		        "SELECT c FROM Customer c WHERE c.username = :name", Customer.class);
	  		Customer customer = null;
	  		try{
	  			customer = query.setParameter("name", userName).getSingleResult();
	  		}catch (Exception _exception) {
	  			logger.error(_exception);
			}
		    return customer;
  }
  
  public List<Employee> getAgents(String searchTerm){
	  	TypedQuery<Employee> query = entityManager.createQuery(
		        "SELECT e FROM Employee e WHERE e.name LIKE :name and e.depotId =:depotId", Employee.class);
	  	List<Employee> employees = null;
		try{
			query.setParameter("name", "%"+searchTerm+"%");
			query.setParameter("depotId", 107);
			employees = query.getResultList();
		}catch (Exception _exception) {
			logger.error(_exception);
		}
	    return employees;
  }
  
  public List<Supplier> getSuppliers(String searchTerm){
	  	TypedQuery<Supplier> query = entityManager.createQuery(
		        "SELECT s FROM Supplier s WHERE s.name LIKE :name", Supplier.class);
	  	List<Supplier> suppliers = null;
		try{
			query.setParameter("name", "%"+searchTerm+"%");
			suppliers = query.getResultList();
		}catch (Exception _exception) {
			logger.error(_exception);
		}
	    return suppliers;
  }
  
  		public Supplier getSupplierById(long supId){
		  	TypedQuery<Supplier> query = entityManager.createQuery(
			        "SELECT s FROM Supplier s WHERE s.supId = :supId", Supplier.class);
		  	Supplier supplier = null;
			try{
				query.setParameter("supId", supId);
				supplier = query.getSingleResult();
			}catch (Exception _exception) {
				logger.error(_exception);
			}
		    return supplier;
  		}
  
  		public List<Customer> getCustomers(String searchTerm){
		  	TypedQuery<Customer> query = entityManager.createQuery(
			        "SELECT c FROM Customer c WHERE c.firstName LIKE :name", Customer.class);
		  	List<Customer> customers = null;
			try{
				customers = query.setParameter("name", "%"+searchTerm+"%").getResultList();
			}catch (Exception _exception) {
				logger.error(_exception);
			}
		    return customers;
  		}
  		
  		public List<Customer> getOrderCustomers(String searchTerm){
		  	/*TypedQuery<Customer> query = entityManager.createQuery(
			        "SELECT c FROM Customer c WHERE c.firstName LIKE :name and c.creditBalance > 0", Customer.class);*/
  			TypedQuery<Customer> query = entityManager.createQuery(
			        "SELECT c FROM Customer c WHERE c.firstName LIKE :name", Customer.class);
		  	List<Customer> customers = null;
			try{
				customers = query.setParameter("name", "%"+searchTerm+"%").getResultList();
			}catch (Exception _exception) {
				logger.error(_exception);
			}
			
		    return customers;
  		}
  
	  @Transactional(readOnly=false)
	  public <T> Boolean saveCustomer(T t){
		  try{
			  entityManager.persist(t);
		  }catch (Exception _exception) {
			  logger.error(_exception);
		  }finally {
		  }
		  return true;
	  }
	  public <T> T saveOrUpdate(T t){
		  try{
			  t = entityManager.merge(t);
	//		  entityManager.flush();
		  }catch (Exception _exception) {
			  logger.error(_exception);
		  }finally {
		  }
		  return t;
	  }
  public List<Product> getProductByOrderId(Long orderId){
  	 TypedQuery<Product> query = entityManager.createQuery(
	        "SELECT p FROM Product p WHERE p.orderId = :orderId", Product.class);
	 List<Product> products=null;
	 try{
		products = query.setParameter("orderId", orderId).getResultList();
	 }catch (Exception _exception) {
		logger.error("getProductByOrderId() ::" + _exception);
	 }
     return products;
  }
  
  	public List<Product> getProductByOrderId(Long orderId,long productTypeId){
	  	 TypedQuery<Product> query = entityManager.createQuery(
		        "SELECT p FROM Product p WHERE p.orderId = :orderId and p.productTypeId=:productTypeId", Product.class);
		 List<Product> products=null;
		 try{
			query.setParameter("orderId", orderId);
			query.setParameter("productTypeId", productTypeId);
			products = query.getResultList();
		 }catch (Exception _exception) {
			logger.error(_exception);
		 }
	     return products;
	  }
  
  public List<ProductItem> getProductItemsByProductId(Long productId){
	  TypedQuery<ProductItem> query = entityManager.createQuery(
		        "SELECT p FROM ProductItem p WHERE p.productId = :productId", ProductItem.class);
	  		List<ProductItem> productItems=null;
	  		try{
	  			productItems = query.setParameter("productId", productId).getResultList();
	  		}catch (Exception _exception) {
	  			logger.error(_exception);
			}
		    return productItems;
  }
  
  public List<ProductItem> getProductItemsByOrderId(Long orderId){
	  TypedQuery<ProductItem> query = entityManager.createQuery(
		        "SELECT p FROM ProductItem p WHERE p.orderId = :orderId", ProductItem.class);
	  		List<ProductItem> productItems=null;
	  		try{
	  			productItems = query.setParameter("orderId", orderId).getResultList();
	  		}catch (Exception _exception) {
	  			logger.error(_exception);
			}
		    return productItems;
  }
  
  public ItemSizeRate getRate(ItemSizeRate itemSizeRate){
	  TypedQuery<ItemSizeRate> query = entityManager.createQuery(
		        "SELECT r FROM ItemSizeRate r WHERE r.itemId = :itemId and r.sizeId=:sizeId and rateId=:rateId", ItemSizeRate.class);
	  		ItemSizeRate  rate=null;
	  		try{
	  			query.setParameter("itemId", itemSizeRate.getItemId());
	  			query.setParameter("sizeId", itemSizeRate.getSizeId());
	  			query.setParameter("rateId", itemSizeRate.getRateId());
	  			rate =  query.getSingleResult();
	  		}catch (Exception _exception) {
	  			logger.error(_exception);
			}
		    return rate;
  }
  
  public List<Customer> getDeportmentsId(Integer depoId){
	  TypedQuery<Customer> query = entityManager.createQuery("SELECT c FROM Customer c WHERE c.depotId = :depoId", Customer.class);
	  		List<Customer> customers=null;
	  		try{
	  			customers = query.setParameter("depoId", depoId).getResultList();
	  		}catch (Exception _exception) {
	  			logger.error(_exception);
			}
		    return customers;
  }
  
  public List<Customer> getCustomers(List<Long> custIds){
	  TypedQuery<Customer> query = entityManager.createQuery("SELECT c FROM Customer c WHERE c.custId in:custIds", Customer.class);
	  		List<Customer> customers=null;
	  		try{
	  			customers = query.setParameter("custIds", custIds).getResultList();
	  		}catch (Exception _exception) {
	  			logger.error(_exception);
			}
		    return customers;
  }
  
  
  
  public List<Address> getAddressByCustId(Long customerId){
	  TypedQuery<Address> query = entityManager.createQuery("SELECT a FROM Address a WHERE a.custId = :custId", Address.class);
	  		List<Address> addresses=null;
	  		try{
	  			addresses = query.setParameter("custId", customerId).getResultList();
	  		}catch (Exception _exception) {
	  			logger.error(_exception);
			}
		    return addresses;
  }
  // added for supplier report corrections
  public Supplier getSupplierNameById(Long supId){
	  TypedQuery<Supplier> query = entityManager.createQuery(
		        "SELECT s FROM Supplier s WHERE s.supId = :supId", Supplier.class);
	  	   Supplier supplier = null;
	  	//String supplierName=null;
		try{
			query.setParameter("supId", supId);
			supplier =  query.getSingleResult();
		}catch (Exception _exception) {
			logger.error(_exception);
		}
	    return supplier; 
	   
  }  
  // added for customer outstanding report
/*public  List<Customer> getCustomerDetails() {
	List<Customer> cdetail= null;
	
	try{
		
		Query query= entityManager.createNativeQuery("select * from customer ",Customer.class);
		query.setParameter("CustomerId",Address.class);
		cdetail= query.getResultList();
	if (cdetail !=null){ 
		System.out.println("from getCustomer function"+cdetail.size()); 
		
	} 
	} 
	catch (Exception _exception) {
		logger.error(_exception);
	}
	
	// TODO Auto-generated method stub
	return cdetail;
}
  */
  
public List<Address> getAddressByCustId(List<Long> custIds){
	 TypedQuery<Address> query = entityManager.createQuery("SELECT a FROM Address a WHERE a.custId in:custIds", Address.class);
	  		List<Address> addresses=null;
	  		try{
	  		addresses=query.setParameter("custIds", custIds).getResultList();
	  		}catch (Exception _exception) {
	  			logger.error(_exception);
			}
	  		//System.out.println("From Address  function size :"+addresses.size());
		    return addresses;
}  
  
public List<Area> getAreaByAreaId(List<Integer> areaIds){
	 TypedQuery<Area> query = entityManager.createQuery("SELECT a FROM Area a WHERE a.id in:areaIds", Area.class);
	  		List<Area> area=null;
	  		try{
	  			area=query.setParameter("areaIds", areaIds).getResultList();
	  		}catch (Exception _exception) {
	  			logger.error(_exception);
			}
	  		//System.out.println("Area size :"+area.size());
		    return area;
} 

public List<Customer> getCustomerInformationForSelectedDate(String fromDate,String toDate){
//List<Customer> cdetail= null;
List<Customer> cdetail= null;
	
	try{
		Query query = null;
//		Query query= entityManager.createNativeQuery("select * from customer c, address ad where c.cust_id=ad.cust_id order by c.cust_id",Customer.class);
		query= entityManager.createNativeQuery("select * from customer a ,orders b where a.cust_id = b.cust_id and b.created_date BETWEEN :fromDate AND :toDate group by b.cust_id order by b.created_date desc;",Customer.class);
		/*query.setParameter("CustomerId",Address.class);*/
		if(query!=null){
			query.setParameter("fromDate",fromDate);
			query.setParameter("toDate",toDate);
		}
		System.out.println("query=="+query.toString());
		cdetail= query.getResultList();
	if (cdetail !=null){ 
		System.out.println("from getCustomer function"+cdetail.size()); 
		
	} 
	} 
	catch (Exception _exception) {
		logger.error(_exception);
	}
return cdetail;
}
	  


public  List<Customer> getCustomerInformation() {
	List<Customer> cdetail= null;
	
	try{
		
		Query query= entityManager.createNativeQuery("select * from customer c, address ad where c.cust_id=ad.cust_id order by c.cust_id",Customer.class);
		/*query.setParameter("CustomerId",Address.class);*/
		cdetail= query.getResultList();
	if (cdetail !=null){ 
		//System.out.println("from getCustomer function"+cdetail.size()); 
		
	} 
	} 
	catch (Exception _exception) {
		logger.error(_exception);
	}
return cdetail;
}
public  List<Supplier> getSupplierInformation() {
	List<Supplier> cdetail= null;
	
	try{
		
		Query query= entityManager.createNativeQuery("select * from supplier ",Supplier.class);
		/*query.setParameter("CustomerId",Address.class);*/
		cdetail= query.getResultList();
	/*if (cdetail !=null){ 
		System.out.println("from getSupplier function"+cdetail.size()); 
		
	} */
	} 
	catch (Exception _exception) {
		logger.error(_exception);
	}
return cdetail;
}

public  List<Customer> getCustomerInformationForSeletedDates(String fromDate,String toDate) {
	List<Customer> cdetail= null;
	
	try{
		
//		Query query= entityManager.createNativeQuery("select * from customer c, address ad where c.cust_id=ad.cust_id order by c.cust_id",Customer.class);
		Query query= entityManager.createNativeQuery("select * from customer a ,orders b where a.cust_id = b.cust_id and b.created_date BETWEEN :fromDate AND :toDate group by b.cust_id order by b.created_date desc",Customer.class);
		/*query.setParameter("CustomerId",Address.class);*/
		query.setParameter("fromDate",fromDate);
		query.setParameter("toDate",toDate);
		cdetail= query.getResultList();
		if (cdetail !=null){ 
			System.out.println("from getCustomerFor selected Dates function"+cdetail.size()); 
		} 
	} 
	catch (Exception _exception) {
		logger.error(_exception);
	}
	return cdetail;
}

  } 
  
  
  
  


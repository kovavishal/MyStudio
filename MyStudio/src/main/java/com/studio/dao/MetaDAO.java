package com.studio.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.studio.domain.Area;
import com.studio.domain.CustomerType;
import com.studio.domain.DeliveryType;
import com.studio.domain.Deportment;
import com.studio.domain.ExpenseType;
import com.studio.domain.ItemSizeRate;
import com.studio.domain.JobStatus;
import com.studio.domain.OrderType;
import com.studio.domain.PaymentType;
import com.studio.domain.Product;
import com.studio.domain.ProductItemType;
import com.studio.domain.ProductSize;
import com.studio.domain.ProductType;
import com.studio.domain.PurchaseItem;
import com.studio.domain.RateType;
import com.studio.domain.ReceiptType;
import com.studio.domain.Role;
import com.studio.domain.State;
import com.studio.domain.VoucherType;


@Repository
@Transactional
@EnableTransactionManagement
public class MetaDAO {
	
  @PersistenceContext
  EntityManager entityManager;
  
  Logger logger = Logger.getLogger(MetaDAO.class);
  
  @SuppressWarnings("unchecked")
  public List<OrderType> getOrderTypes(){
	  Query query = entityManager.createQuery("SELECT o FROM OrderType o");
	  List<OrderType> orderTypes = query.getResultList();
	  return  orderTypes;
  }
  
  @SuppressWarnings("unchecked")
  public List<PaymentType> getPaymentTypes(){
	  Query query = entityManager.createQuery("SELECT p FROM PaymentType p");
	  List<PaymentType> paymentTypes = query.getResultList();
	  return  paymentTypes;
  }
  @SuppressWarnings("unchecked")
  public List<ReceiptType> getReceiptTypes(){
	  Query query = entityManager.createQuery("SELECT r FROM ReceiptType r");
	  List<ReceiptType> receiptTypes = query.getResultList();
	  return  receiptTypes;
  }
  
  @SuppressWarnings("unchecked")
  public List<ProductType> getProductTypes(){
	  Query query = entityManager.createQuery("SELECT p FROM ProductType p");
	  List<ProductType> productTypes = query.getResultList();
	  return  productTypes;
  }
  // changed
 /* @SuppressWarnings("unchecked")
  public List<ProductType> getProductTypeName(long productId){
	  Query query = entityManager.createQuery("SELECT p FROM ProductType p where p.id=:productId");
	  List<ProductType> productTypes = query.getResultList();
	  return  productTypes;
  }*/
  
  
  
  @SuppressWarnings("unchecked")
  public List<ProductType> getProductTypes(String name){
	  Query query = entityManager.createQuery("SELECT p FROM ProductType p where p.name=:name");
	  return  query.setParameter("name", name).getResultList();
  }
  
  @SuppressWarnings("unchecked")
  public List<ProductSize> getProductSizes(String size){
	  Query query = entityManager.createQuery("SELECT p FROM ProductSize p where p.size=:size");
	  return  query.setParameter("size", size).getResultList();
  }
  
  @SuppressWarnings("unchecked")
  public List<ProductSize> getProductSizes(String size,Integer productId){
	  Query query = entityManager.createQuery("SELECT p FROM ProductSize p where p.size=:size and p.productId=:productId");
	  query.setParameter("size", size);
	  query.setParameter("productId", productId);
	  return  query.getResultList();
  }
  
  @SuppressWarnings("unchecked")
  public List<ProductItemType> getProductItemTypes(String name){
	  Query query = entityManager.createQuery("SELECT p FROM ProductItemType p where p.name=:name");
	  return  query.setParameter("name", name).getResultList();
  }
  
  @SuppressWarnings("unchecked")
  public List<ProductItemType> getProductItemTypesByProductId(Integer productId){
	  Query query = entityManager.createQuery("SELECT p FROM ProductItemType p where p.productId=:productId");
	  return  query.setParameter("productId", productId).getResultList();
  }
  
  @SuppressWarnings("unchecked")
  public List<ProductItemType> getProductItemTypes(String name,Integer productId){
	  Query query = entityManager.createQuery("SELECT p FROM ProductItemType p where p.name=:name and p.productId=:productId");
	  query.setParameter("name", name);
	  query.setParameter("productId", productId);
	  return  query.getResultList();
  }
  
  @SuppressWarnings("unchecked")
  public List<RateType> getRateTypes(String name){
	  Query query = entityManager.createQuery("SELECT r FROM RateType r where r.name=:name");
	  return  query.setParameter("name", name).getResultList();
  }
  
  @SuppressWarnings("unchecked")
  public List<CustomerType> getCustomerTypes(){
	  Query query = entityManager.createQuery("SELECT c FROM CustomerType c");
	  List<CustomerType> customerTypes = query.getResultList();
	  return  customerTypes;
  }
  
  @SuppressWarnings("unchecked")
  public List<DeliveryType> getDeliveryTypes(){
	  Query query = entityManager.createQuery("SELECT t FROM DeliveryType t");
	  List<DeliveryType> deliveryTypes = query.getResultList();
	  return  deliveryTypes;
  }
  
  @SuppressWarnings("unchecked")
  public List<ProductItemType> getProductItemTypes(){
	  Query query = entityManager.createQuery("SELECT p FROM ProductItemType p");
	  List<ProductItemType> productItemTypes = query.getResultList();
	  return  productItemTypes;
  }
  
  public List<ProductItemType> getProductItemTypes(Integer productId){
	  TypedQuery<ProductItemType> query = entityManager.createQuery(
		        "SELECT p FROM ProductItemType p WHERE p.productId = :productId", ProductItemType.class);
	  List<ProductItemType> productItemTypes = query.setParameter("productId", productId).getResultList();
	  return  productItemTypes;
  }
  
  public List<ProductItemType> getProductItemTypesById(Integer id){
	  TypedQuery<ProductItemType> query = entityManager.createQuery(
		        "SELECT p FROM ProductItemType p WHERE p.id = :id", ProductItemType.class);
	  List<ProductItemType> productItemTypes = query.setParameter("id", id).getResultList();
	  return  productItemTypes;
  }
  
  public RateType getRateType(Integer rateType){
	  TypedQuery<RateType> query = entityManager.createQuery(
		        "SELECT r FROM RateType r WHERE r.id = :customerType", RateType.class);
	  RateType rate = query.setParameter("customerType", rateType).getSingleResult();
	  return  rate;
  }
  public List<RateType> getRateTypes(){
	  List<RateType> rateTypes = null;
	  TypedQuery<RateType> query = entityManager.createQuery(
		        "SELECT r FROM RateType r ", RateType.class);
	  rateTypes = query.getResultList();
	  return  rateTypes;
  }
  
  public ExpenseType getExpenseType(Integer expenseType){
	  TypedQuery<ExpenseType> query = entityManager.createQuery(
		        "SELECT e FROM ExpenseType e WHERE c.id = :expenseType", ExpenseType.class);
	  ExpenseType exType = query.setParameter("expenseType", expenseType).getSingleResult();
	  return  exType;
  }
  
  public List<ExpenseType> getExpenseTypes(){
	  TypedQuery<ExpenseType> query = entityManager.createQuery(
		        "SELECT e FROM ExpenseType e", ExpenseType.class);
	  List<ExpenseType> expenserTypes = query.getResultList();
	  return  expenserTypes;
  }
  
  public List<ProductSize> getProductSizes(Integer productId){
	  TypedQuery<ProductSize> query = entityManager.createQuery(
		        "SELECT p FROM ProductSize p WHERE p.productId = :productId", ProductSize.class);
	  return  query.setParameter("productId", productId).getResultList();
  }
  
  // getting product sizes for viewworkorder Table  starts
  
  public List<ProductSize> getProductViewTableSizes(Integer productId){
	  TypedQuery<ProductSize> query = entityManager.createQuery(
		        "SELECT p FROM ProductSize p WHERE p.id = :productId", ProductSize.class);
	  return  query.setParameter("productId", productId).getResultList();
  }
  // getting product sizes for viewworkorder Table Ends
  
  
  
  public List<ProductSize> getItemSizes(Integer productId){
	  TypedQuery<ProductSize> query = entityManager.createQuery(
		        "SELECT p FROM ProductSize p WHERE p.productId = :productId", ProductSize.class);
	  return  query.setParameter("productId", productId).getResultList();
  }
  
  public List<ProductSize> getProductSizes(){
	  TypedQuery<ProductSize> query = entityManager.createQuery(
		        "SELECT p FROM ProductSize p", ProductSize.class);
	  return  query.getResultList();
  }
  
  public Deportment getDeportments(Integer deportmentId){
	  TypedQuery<Deportment> query = entityManager.createQuery(
		        "SELECT d FROM Deportment d WHERE d.id = :deportmentId", Deportment.class);
	  return  query.setParameter("productId", deportmentId).getSingleResult();
  }
  
  public List<Deportment> getDeportmentsByName(String name){
	  TypedQuery<Deportment> query = entityManager.createQuery(
		        "SELECT d FROM Deportment d WHERE d.name = :name", Deportment.class);
	  return  query.setParameter("name", name).getResultList();
  }
  
  public List<Deportment> getDeportments(){
	  TypedQuery<Deportment> query = entityManager.createQuery("SELECT d FROM Deportment d", Deportment.class);
	  return  query.getResultList();
  }
  
  public List<Role> getRoles(){
	  TypedQuery<Role> query = entityManager.createQuery("SELECT r FROM Role r", Role.class);
	  return  query.getResultList();
  }
  
  public List<Area> getAreas(){
	  TypedQuery<Area> query = entityManager.createQuery("SELECT a FROM Area a", Area.class);
	  return  query.getResultList();
  }
  
  public List<JobStatus> getJobStatus(){
	  TypedQuery<JobStatus> query = entityManager.createQuery("SELECT j FROM JobStatus j", JobStatus.class);
	  return  query.getResultList();
  }
  
  public List<ItemSizeRate> getItemSizeRates(){
	  TypedQuery<ItemSizeRate> query = entityManager.createQuery("SELECT i FROM ItemSizeRate i", ItemSizeRate.class);
	  return  query.getResultList();
  }
  
  public List<State> getStates(){
	  TypedQuery<State> query = entityManager.createQuery("SELECT s FROM State s", State.class);
	  return  query.getResultList();
  }
  public List<PurchaseItem> getPurchaseItems(){
	  TypedQuery<PurchaseItem> query = entityManager.createQuery("SELECT p FROM PurchaseItem p", PurchaseItem.class);
	  return  query.getResultList();
  }
  
  public List<VoucherType> getVoucherTypes(){
	  TypedQuery<VoucherType> query = entityManager.createQuery("SELECT v FROM VoucherType v", VoucherType.class);
	  return  query.getResultList();
  }
  
  @SuppressWarnings("unchecked")
  public <T>  List<T> getMetaData(T t){
	  CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	  CriteriaQuery<T> cq = (CriteriaQuery<T>) cb.createQuery();
	  Root<T> root  = (Root<T>) cq.from(t.getClass());
	  CriteriaQuery<T> select = cq.select(root);
	  TypedQuery<Object> typedQuery = (TypedQuery<Object>) entityManager.createQuery(select);
	  List<T> datas =  (List<T>) typedQuery.getResultList();
	  return  datas;
  }
  
  public Map<Integer,String> getRoleMap(){
	  Map<Integer,String> roleMap = new HashMap<Integer, String>();
	  for(Role role:getRoles()){
		  roleMap.put(role.getId(), role.getName());
	  }
	  return roleMap;
  }
// added for no of sheets in bill printing
public List<Product> getNoOfSheets(String orderId) {
	 Query  query = entityManager.createNativeQuery("SELECT * FROM Product p  WHERE p.order_id=:orderId and p.product_type_id='102'", Product.class);
	 List<Product> x=query.setParameter("orderId", orderId).getResultList();
	 return x; 
}
  
  
}

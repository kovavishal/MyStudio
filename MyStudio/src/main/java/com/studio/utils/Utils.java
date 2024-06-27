/**
 * 
 */
package com.studio.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.studio.domain.Address;
import com.studio.domain.Customer;
import com.studio.domain.DeliveryType;
import com.studio.domain.Deportment;
import com.studio.domain.Employee;
import com.studio.domain.ExpenseType;
import com.studio.domain.JobAllocation;
import com.studio.domain.JobStatus;
import com.studio.domain.Order;
import com.studio.domain.OrderTrxnDetail;
import com.studio.domain.OrderType;
import com.studio.domain.PaymentType;
import com.studio.domain.Product;
import com.studio.domain.ProductItemType;
import com.studio.domain.ProductSize;
import com.studio.domain.ProductType;
import com.studio.domain.Purchase;
import com.studio.domain.RateType;
import com.studio.domain.Receipt;
import com.studio.domain.ReceiptType;
import com.studio.domain.Supplier;
import com.studio.domain.Voucher;
import com.studio.domain.VoucherType;
import javax.persistence.Entity;

/**
 * @author ezhilraja_k
 *
 */

public class Utils {

	@SuppressWarnings("unchecked")
	public static <T> Map<Long,String> getTypes(T t){
		
		List<T> types= (List<T>) t;
		Map<Long,String> typeMap = new HashMap<Long, String>();
		for(T ts:types){
			if(ts instanceof ProductType){
				ProductType type = (ProductType) ts;
				typeMap.put(new Long(type.getId()), type.getName());
			}else if(ts  instanceof ProductItemType){
				ProductItemType type = (ProductItemType) ts;
				typeMap.put(new Long(type.getId()), type.getName());
			}else if(ts  instanceof ProductSize){
				ProductSize type = (ProductSize) ts;
				typeMap.put(new Long(type.getId()), type.getSize());
			}else if(ts  instanceof RateType){
				RateType type = (RateType) ts;
				typeMap.put(new Long(type.getId()), type.getName());
			}else if(ts instanceof OrderType){
				OrderType type = (OrderType)ts;
				typeMap.put(type.getId().longValue(), type.getName());
			}else if(ts instanceof Deportment){
				Deportment type = (Deportment)ts;
				typeMap.put(Long.valueOf(type.getId()), type.getName());
			}else if(ts instanceof PaymentType){
				PaymentType type = (PaymentType)ts;
				typeMap.put(Long.valueOf(type.getId()), type.getName());
			}else if(ts instanceof VoucherType){
				VoucherType type = (VoucherType)ts;
				typeMap.put(Long.valueOf(type.getId()), type.getName());
			}else if(ts instanceof ExpenseType){
				ExpenseType type = (ExpenseType)ts;
				typeMap.put(Long.valueOf(type.getId()), type.getName());
			}else if(ts instanceof ReceiptType){
				ReceiptType type = (ReceiptType)ts;
				typeMap.put(Long.valueOf(type.getId()), type.getName());
			}else if(ts instanceof DeliveryType){
				DeliveryType type = (DeliveryType)ts;
				typeMap.put(Long.valueOf(type.getId()), type.getName());
			}
			
		}
		return typeMap;
	}
	
	public static Map<String,Integer> getDepotMap(List<Deportment> deportments){
		  Map<String,Integer> depoMap = new HashMap<String, Integer>();
		  
		  if(deportments!=null && !deportments.isEmpty()){
			for(Deportment deportment : deportments){
				depoMap.put(deportment.getName(), deportment.getId());
			}
		  }
		  
		  return depoMap;	  
	  }
	  
	  public static Map<String,Integer> getJobStatusMap(List<JobStatus> jobStatus){
		  Map<String,Integer> statusMap = new HashMap<String, Integer>();
		  
		  if(jobStatus!=null && !jobStatus.isEmpty()){
			for(JobStatus status : jobStatus){
				statusMap.put(status.getName(), status.getId());
			}
		  }
		  
		  return statusMap;	  
	  }
	  
	  public static Map<Integer,String> getJobStatusIdMap(List<JobStatus> jobStatus){
		  Map<Integer,String> statusMap = new HashMap<Integer, String>();
		  
		  if(jobStatus!=null && !jobStatus.isEmpty()){
			for(JobStatus status : jobStatus){
				statusMap.put(status.getId(),status.getName());
			}
		  }
		  
		  return statusMap;	  
	  }
	  
	  public static Map<Long,Integer> getEmployeeIdMap(List<Employee> employees){
		  Map<Long,Integer> empMap = new HashMap<Long, Integer>();
		  if(employees !=null && !employees.isEmpty()){
			  for(Employee emp : employees){
				  empMap.put(emp.getEmpId(), emp.getDepotId());
			  }
		  }
		  return empMap;
	  }
	  
	  public static Map<Long,String> getEmployeeNameMap(List<Employee> employees){
		  Map<Long,String> empMap = new HashMap<Long, String>();
		  if(employees !=null && !employees.isEmpty()){
			  for(Employee emp : employees){
				  empMap.put(emp.getEmpId(), emp.getName());
			  }
		  }
		  return empMap;
	  }
	  
	  public static Set<Long> getOrderIds(List<JobAllocation> jobAllocations){
		
		  Set<Long> orderIds= new TreeSet<Long>();
		  if(jobAllocations!=null && !jobAllocations.isEmpty()){
			  for(JobAllocation job : jobAllocations){
				  orderIds.add(job.getId().getOrderId());
			  }
		  }
		  return orderIds;
	  }
	  
	  public static Map<Long,Order> getIdOrders(List<Order> orders){
		  Map<Long,Order> orderStatusIds = new HashMap<Long, Order>();
		  for(Order order : orders){
			  orderStatusIds.put(order.getOrderId(), order);
		  }
		  return orderStatusIds;
	  }
	  
	  public static Map<Long,Order> getCustIdOrders(List<Order> orders){
		  Map<Long,Order> orderStatusIds = new HashMap<Long, Order>();
		  for(Order order : orders){
			  orderStatusIds.put(order.getCustId(), order);
		  }
		  return orderStatusIds;
	  }
	  
	  public static Map<Long,Order> getOrderIdOrders(List<Order> orders){
		  Map<Long,Order> orderStatusIds = new HashMap<Long, Order>();
		  for(Order order : orders){
			  orderStatusIds.put(order.getOrderId(), order);
		  }
		  return orderStatusIds;
	  }
	  
	  public static List<Long> getOrdersIds(List<Order> orders){
		  List<Long> orderIds = new ArrayList<Long>();
		  for(Order order : orders){
			  orderIds.add(order.getOrderId());
		  }
		  return orderIds;
	  }
	  
	 /* public static List<Long> getUndeliveredOrdersIds(List<JobAllocationForUndeliveredAlbum> orders){
		  List<Long> orderIds = new ArrayList<Long>();
		  for(JobAllocationForUndeliveredAlbum ja : orders){
			  orderIds.add(ja.getOrderId());
		  }
		  for(int i=0;i<orderIds.size();i++)
			  System.out.println("ORDERIDS :"+orderIds.get(i));
		  return orderIds;
	  } 
	  */
	  public static Double formatDouble(Double amount){
		  Double truncatedDouble = BigDecimal.valueOf(amount)
				    .setScale(3, RoundingMode.HALF_UP)
				    .doubleValue();
		  return truncatedDouble;
	  }
	  
	  public static Long[] getCustomerIds(List<Customer> customers){
			Long[] custIds = null;
			if(customers!=null && !customers.isEmpty()){
				custIds = new Long[customers.size()];
				int index=0;
				for(Customer customer : customers){
					custIds[index]= customer.getCustId();
					index++;
				}
			}
			return custIds;
		}
	  
	  public static List<Long> getCustomerIdList(List<Customer> customers){
			List<Long> ids= new ArrayList<Long>();
			if(customers!=null && !customers.isEmpty()){
				for(Customer customer : customers){
					ids.add(customer.getCustId());
				}
			}
			return ids;
		}
	  
	  public static List<Long> getVoucherIds(List<Voucher> vouchers){
		  List<Long> voucherIds = null;
		  if(vouchers !=null && !vouchers.isEmpty()){
			  voucherIds = new ArrayList<Long>();
			  for(Voucher v:vouchers){
				  voucherIds.add(v.getVoucherId());
			  }
		  }
		  return voucherIds;
	  }
	  
	  public static List<Long> getTrxnOrderIds(List<OrderTrxnDetail> orderTrxnDetails){
		  List<Long> orderIds = null;
		  if(orderTrxnDetails!=null && !orderTrxnDetails.isEmpty()){
			  orderIds = new ArrayList<Long>();
			  for(OrderTrxnDetail detail : orderTrxnDetails){
				  orderIds.add(detail.getOrderId());
			  }
		  }
		  return orderIds;
	  }
	  
	 public static Map<Long,Customer> getOrderIdCustomer(List<Order> orders,List<Customer> customers){
		 Map<Long,Customer> orderCustomers = null;
		 if(orders!=null && !orders.isEmpty() && customers!=null && !customers.isEmpty()){
			 orderCustomers =new HashMap<Long, Customer>();
			 for(Order order: orders){
				 for(Customer customer:customers){
					 if(customer.getCustId() == order.getCustId()){
						 orderCustomers.put(order.getOrderId(), customer);
						 break;
					 }
				 }
			 }
		 }
		 return orderCustomers;
	 }
	 
	 public static List<Long> getSupplierIds(List<Purchase> purchases){
		 List<Long> supIds=null;
		 if(purchases!=null && !purchases.isEmpty()){
			 supIds = new ArrayList<Long>();
			 for(Purchase p:purchases){
				 supIds.add(p.getSupId());
			 }
		 }
		 return supIds;
	 }
	 
	 public static Map<Long,Supplier> getIdSuppliers(List<Supplier> suppliers){
		 Map<Long,Supplier> supplierMap = null;
		 if(suppliers!=null && !suppliers.isEmpty()){
			 supplierMap = new HashMap<Long, Supplier>();
			 for(Supplier sup:suppliers){
				 supplierMap.put(sup.getSupId(), sup);
			 }
		 }
		 return supplierMap;
	 }
	 
	 public static List<Long> getSupIds(List<Voucher> vouchers){
		 List<Long> supIds=null;
		 if(vouchers!=null && !vouchers.isEmpty()){
			 supIds=new ArrayList<Long>();
			 for(Voucher v : vouchers){
				 if(v.getExpenseId()<10000){
					 	supIds.add((long)v.getExpenseId());
				 }
			 }
		 }
		 return supIds;
	 }
	 
	 public static Map<Integer,Voucher> getIdVouchers(List<Voucher> vouchers){
		 Map<Integer,Voucher> vMap = null;
		 if(vouchers!=null && !vouchers.isEmpty()){
			 vMap = new HashMap<Integer, Voucher>();
			 for(Voucher sup:vouchers){
				 if(sup.getExpenseId()>10000){
					 vMap.put(sup.getExpenseId(), sup);
				 }
			 }
		 }
		 return vMap;
	 }
	 
	 public static List<Long> getReceiptsCustomersIds(List<Receipt> receipts){
		 List<Long> custIds = null;
		 if(receipts!=null && !receipts.isEmpty()){
			 custIds = new ArrayList<Long>();
			 for(Receipt receipt:receipts){
				 custIds.add(receipt.getCustId());
			 }
		 }
		 return custIds;
	 }
	 
	 public static Map<Long,Customer> getCustIds(List<Customer> customers){
			Map<Long,Customer> ids= new HashMap<Long, Customer>();
			if(customers!=null && !customers.isEmpty()){
				for(Customer customer : customers){
					ids.put(customer.getCustId(), customer);
				}
			}
			return ids;
	 }
	 
	 public static Map<Long,Product> getIdProducts(List<Product> products){
		 Map<Long,Product> idPproducts = null;
		 if(products !=null && !products.isEmpty()){
			 idPproducts =new HashMap<Long, Product>();
			for(Product p:products){
				idPproducts.put(p.getProductId(), p);
			}
		 }
		 
		 return idPproducts;
	 }
	 
	 public List<Long>  getProductsIds(List<Product> products){
		 List<Long> productIds = null;
		 if(products !=null && !products.isEmpty()){
			 productIds =new ArrayList<Long>();
			for(Product p:products){
				productIds.add(p.getProductTypeId());
			}
		 }
		 
		 return productIds;
	 }
	 
	 public static Map<Integer,String> getIdProductSize(List<ProductSize> productSizes){
		 Map<Integer,String> pMap= new HashMap<Integer, String>();
		 if(productSizes!=null && !productSizes.isEmpty()){
			 for(ProductSize size:productSizes){
				 pMap.put(size.getId(), size.getSize());
			 }
		 }
		 return pMap;
	 }
	 // added for customer outstanding report
	 
	 public static List<Long> getCustomersIds(List<Customer> cd){
		 List<Long> custIds = null;
		 if(cd!=null && !cd.isEmpty()){
			 custIds = new ArrayList<Long>();
			 for(Customer Customer: cd){
				 custIds.add(Customer.getCustId());
				}
		 }
		 return custIds;
	 }
	 
	 // added for undeliveredAlbum report
	 
	 public static List<Long> getUndeliveredCustomersIds(List<Order> orders){
		 List<Long> custIds = null;
		 if(orders !=null ){
			 custIds = new ArrayList<Long>();
			 for(Order order: orders){
				 custIds.add(order.getCustId());
				}
		 }
		 return custIds;
	 }
	 
	 
	 public static List<Integer> getAreaIds(List<Address> addr){
		 List<Integer> aeraIds = null;
		 if(addr!=null && !addr.isEmpty()){
			 aeraIds = new ArrayList<Integer>();
			 for(Address Address: addr){
				 aeraIds.add(Address.getAreaId());
				 
			 }
		 }
		 return aeraIds;
	 }
	 

	// Number to word converter for Rupees in words
	public static String numberToWord (String number)
	{
	String twodigitword="";
	String word="";
	String[] HTLC = {"", "Hundred", "Thousand", "Lakh", "Crore"}; //H-hundread , T-Thousand, ..
	int split[]={0,2, 3, 5, 7,9};
	String[] temp=new String[split.length];
	boolean addzero=true;
	int len1=number.length();
	if (len1>split[split.length-1]) {
//		System.out.println("Error. Maximum Allowed digits "+ split[split.length-1]);
	System.exit(0);
	}
	for (int l=1 ; l<split.length; l++ )
	if (number.length()==split[l] ) addzero=false;
	if (addzero==true) number="0"+number;
	int len=number.length();
	int j=0;
	//spliting & putting numbers in temp array.
	while (split[j]<len)
	{
	    int beg=len-split[j+1];
	    int end=beg+split[j+1]-split[j];
	    temp[j]=number.substring(beg , end);
	    j=j+1;
	}
	 
	for (int k=0;k<j;k++)
	{
	    twodigitword=convertOnesTwos(temp[k]);
	    if (k>=1){
	    if (twodigitword.trim().length()!=0) word=twodigitword+" " +HTLC[k] +" "+word;
	    }
	    else word=twodigitword ;
	    }
	   return (word);
	}

	public static String convertOnesTwos(String t)
	{
	final String[] ones ={"", "One", "Two", "Three", "Four", "Five","Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve","Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
	final String[] tens = {"", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty","Ninety"};

	String word="";
	int num=Integer.parseInt(t);
	if (num%10==0) word=tens[num/10]+" "+word ;
	else if (num<20) word=ones[num]+" "+word ;
	else
	{
	word=tens[(num-(num%10))/10]+word ;
	word=word+" "+ones[num%10] ;
	}
	return word;
	}

	  
}

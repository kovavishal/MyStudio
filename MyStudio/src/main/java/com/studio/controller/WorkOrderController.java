/**
 * 
 */
package com.studio.controller;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studio.constants.WorkOrderConstants;
import com.studio.dao.CustomerDAO;
import com.studio.dao.MaxValueDAO;
import com.studio.dao.MetaDAO;
import com.studio.dao.OrderDAO;
import com.studio.dao.ReportDAO;
import com.studio.dao.UserDAO;
import com.studio.domain.Address;
import com.studio.domain.ControlTable;
import com.studio.domain.Customer;
import com.studio.domain.CustomerType;
import com.studio.domain.DeliveryType;
import com.studio.domain.Deportment;
import com.studio.domain.Employee;
import com.studio.domain.ItemSizeRate;
import com.studio.domain.JobAllocation;
import com.studio.domain.JobAllocationId;
import com.studio.domain.JobStatus;
import com.studio.domain.Order;
import com.studio.domain.OrderTrxnDetail;
import com.studio.domain.OrderType;
import com.studio.domain.PaymentType;
import com.studio.domain.Product;
import com.studio.domain.ProductItem;
import com.studio.domain.ProductItemType;
import com.studio.domain.ProductSize;
import com.studio.domain.ProductType;
import com.studio.domain.WorkorderDetails;
import com.studio.service.SmsService;
import com.studio.utils.DateUtils;
import com.studio.utils.Utils;
import com.studio.controller.PrintController;

/**
 * @author ezhilraja_k
 *
 */

@RestController
@Transactional  // added on 28-12-2021 and made first_name as NULL in customer table
@PropertySource("classpath:studio.properties")
public class WorkOrderController implements WorkOrderConstants{

	List<Product> productlist = new ArrayList<Product>();
	
	List<ProductItem> productitemlist = new ArrayList<ProductItem>();

	Product lastprodid = new Product();
	long prodno=0;
	int onedit=0;
    
	
	
	@Autowired
	private MetaDAO metaDAO;
	
	@Autowired
	private CustomerDAO customerDAO;
	
	@Autowired
	private MaxValueDAO maxValueDAO;

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private ReportDAO reportDAO;
	@Autowired
	private Environment env;
	
	@Autowired(required=true)
	private SmsService smsService;
	
	@Autowired
	private PrintController cookie;

	Logger logger = Logger.getLogger(WorkOrderController.class);
	
	@RequestMapping(value = "/workorderdetail", method = RequestMethod.GET)
	public ModelAndView workorderdetail(ModelAndView  modelAndView,HttpServletRequest request){
		
		
		return modelAndView;
	}
	
	
	
	@RequestMapping(value="/workorder",method = RequestMethod.GET)
	public ModelAndView workorder(ModelAndView modelAndView){
		logger.info("---- workorder :: GET ----");
		setMetaData(modelAndView);
		productlist.clear();
		productitemlist.clear();
		lastprodid.setProductId(null);
		prodno=0;
		onedit=0;
		Order order = new Order();
		order.setOrderId(maxValueDAO.getMaxOrderId());
		// added
		order.setEnablePayment(false);
		order.setProdId(maxValueDAO.getMaxProductId());
		 lastprodid.setProductId(order.getProdId());
		 java.util.Date date=new java.util.Date(); 
		 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");  
		 order.setStrOrderDate(formatter.format(date));
		 
		 ControlTable  lastlogin=userDAO.getLastLoginDate();
		 Date ctdate=lastlogin.getLastLoginDate();
		 String strctdate=ctdate.toString();
		 strctdate=reportDAO.getyyyymmddtoddmmyyyy(strctdate);
//		 if(strctdate.compareTo(order.getStrOrderDate().substring(0, 10))>0 ){
//			 modelAndView.setViewName(PAGE_LOGIN);	
//			 modelAndView.addObject("message","System Date has been changed,Contact Admin... ");
//			 return modelAndView;
//		 }
		//order.setProdItemId(maxValueDAO.getMaxProductItemId());
		//System.out.println("productlist size :"+productlist.size());
		//System.out.println("productitemlist size :"+productitemlist.size());
		//System.out.println("lastprodid:"+lastprodid.getProductId());
		//System.out.println("productitemID :"+order.getProdItemId());
		Product product = new Product();
		order.getProducts().add(product);
		setDefaultItem(order);
		//System.out.println("Total products :"+order.getProducts().get(0).getName());
		//System.out.println("Total products :"+order.getProducts().get(1).getName());
		/*for(int i=1;i<5;i++){
			ProductItem productItem = new ProductItem();
			order.getProducts().get(0).getProductItems().add(productItem);
		}*/
		//System.out.println("Total products :"+order.getProducts().get(0).getProductItems().get(0).getProdItemName());
		
		setMetaData(modelAndView);
		modelAndView.addObject(PRODUCT_ITEMS, order.getProducts().get(0).getProductItems());
		modelAndView.addObject(ORDER, order);
		modelAndView.setViewName(PAGE_WORKORDER);
		return modelAndView;
	}
	
	@RequestMapping(value="/workorder",method = RequestMethod.POST)
	public ModelAndView saveWorkorder(@ModelAttribute("order") Order order){
		logger.info("--- saveWorkorder ---");
		ModelAndView modelAndView = new ModelAndView(PAGE_WORKORDER);
		onedit=0;
		setOrderStatus(order, env.getProperty("status.pending"));
		order.setCreatedDate(new Date());
		/*ADDED FOR CURRENT CUSTOMER BALANCE STARTS*/
		if(order.getCustId() !=0){
			Double currentCreditBalance	= orderDAO.getCurrentCreditBalance(order.getCustId());
			order.setCreditBalance(currentCreditBalance);
		}	
		
		/*ADDED FOR CURRENT CUSTOMER BALANCE ENDS */
		if (!order.isEnablePayment()==true)
		order.setEnablePayment(true);
		saveOrder(order);
		Product product = new Product();
		//product.setProductId(maxValueDAO.getMaxProductId());
		
		product.setProductId(order.getProdId());
		order.setProducts(new ArrayList<Product>()); 
		order.getProducts().add(product);
		order.getProducts().get(0).setProductItems(new ArrayList<ProductItem>());
		
		
		setDefaultItem(order);
		setMetaData(modelAndView);
		modelAndView.addObject(PRODUCT_ITEMS, order.getProducts().get(0).getProductItems());
		modelAndView.addObject(ORDER, order);
		return modelAndView;
	}

	// added for display the selected products in billing


	
	/**
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/viewworkorder",method=RequestMethod.GET)
	public @ResponseBody List<WorkorderDetails> getViewWorkorder(HttpServletRequest request){
		
		logger.info("--- view Workorder ---");
		// ModelAndView modelAndView = new ModelAndView(VIEW_ORDER_DETAILS);
		 ModelAndView modelAndView = new ModelAndView(PAGE_WORKORDER_DETAIL);
		List<WorkorderDetails> viewDetails = new ArrayList<WorkorderDetails>();
		ObjectMapper mapper = new ObjectMapper();
		double totalamount=0.0;
		//System.out.println("product size :"+productlist.size());
		//System.out.println("productlist  size :"+productitemlist.size());
		try{
			if (productlist.size() >0){
			viewDetails = new ArrayList<WorkorderDetails>();
		    for (int i=0;i<productlist.size();i++){
			    	WorkorderDetails detail = new WorkorderDetails();
			    	List<ProductType> existingproductName= metaDAO.getProductTypes();
			    	if(productlist.get(i).getNoOfSheet()>0 && productlist.get(i).getProdNo() !=0){
			    	/*List<ProductType> existingproductName= metaDAO.getProductTypes();*/
			    	 for(ProductType productType: existingproductName){
						 if(productType.getId() == productlist.get(i).getProductTypeId()){
							detail.setProdName(productType.getName().toUpperCase());
							detail.setProdNo(productlist.get(i).getProdNo());
						 }
						 }
			    	detail.setNoofcopies(productlist.get(i).getNoOfCopy());
			    	/*List<ProductSize> existsProductSizes = metaDAO.getProductSizes((int)productlist.get(i).getProductTypeId());*/
			    	List<ProductSize> existsProductSizes = metaDAO.getProductViewTableSizes((int)productlist.get(i).getSize());
			    	//System.out.println("PRODUCT getProdTypeId ===="+productlist.get(i).getProductTypeId());
			    	//System.out.println("PRODUCT getprodNo ===="+productlist.get(i).getProdNo());
			    //	System.out.println("PRODUCT getSIZE ===="+productlist.get(i).getSize());
			    	detail.setSize(existsProductSizes.get(0).getSize());
			    	detail.setNoofsheets(productlist.get(i).getNoOfSheet());
			    	 	
			    	if(detail!=null)
	  					viewDetails.add(detail);
	  			   	List<ProductItemType> existingproductItem= metaDAO.getProductItemTypes();
			    	for (int j=0;j<productitemlist.size();j++){
			    		if(productitemlist.get(j).getProdNo() != 0 ){
			    			if (productitemlist.get(j).getProdNo()==productlist.get(i).getProdNo()){
			    				WorkorderDetails itemdetail = new WorkorderDetails();
			    				//ProductItemType productItemTypes= metaDAO.getProductTypeName((int)productitemlist.get(j).getProdItemTypeId());
			    				for(ProductItemType proditemtype:existingproductItem)	{
			    					if (proditemtype.getId()==productitemlist.get(j).getProdItemTypeId()){
			    				  			itemdetail.setItemname(proditemtype.getName().toUpperCase());
			    				  			itemdetail.setQty(productitemlist.get(j).getQuantity());
			    				  			itemdetail.setRate(productitemlist.get(j).getRate());
			    				  			itemdetail.setAmount(productitemlist.get(j).getAmount());
			    				  			totalamount =+itemdetail.getAmount();
			    				  			itemdetail.setTotalAmount(totalamount);
			    				  			if(itemdetail!=null)
					    						viewDetails.add(itemdetail);
			    				  			}
			    						}
			    				
			    					
			    				}
			    		
			    			}
			    		}
			    	
			    	}
			   
			   
			    }
			}
			else{
				return viewDetails;
			}
			}catch (Exception _exception) {
			logger.error("getViewWorkorder::" + _exception);
			_exception.printStackTrace();
		}
		
		//System.out.println("success: size"+viewDetails.size());
		for(int i=0;i<viewDetails.size();i++){
		//	System.out.println("view Product details :"+viewDetails.get(i).getProdName()+"  view list details :"+viewDetails.get(i).getItemname());
			}
		
		return viewDetails;
	}
	
	// added for view the selected products in billing ENDS
	
	
	
	

	@RequestMapping(value="/workorderdetail",method = RequestMethod.POST)
	public ModelAndView saveWorkorderAndContinue(HttpServletRequest request, @ModelAttribute("order") Order order){
		logger.info("--- saveWorkorderAndContinue ---");
		ModelAndView modelAndView = new ModelAndView(PAGE_WORKORDER_DETAIL);
		setOrderStatus(order, env.getProperty("status.pending"));
		order.setCreatedDate(new Date());
		
		if(order.getCreditBalance()!= null)
			order.setCreditBalance(Utils.formatDouble(order.getCreditBalance()));
		//System.out.println("Quantity"+order.getProducts().get(0).getProductItems().get(0).getQuantity());
		//verify  NULL and >0 then SAVE
		if (order.getProducts().get(0).getProductItems().get(0).getQuantity() !=null && order.getProducts().get(0).getProductItems().get(0).getQuantity()>0)
		saveOrder(order);
		Double totalAmount = 0.00;
		//List<ProductItem> previousProductItems = customerDAO.getProductItemsByOrderId(order.getOrderId());
		
		for(int j=0;j<productitemlist.size();j++){
			//System.out.println("value of prodno :"+productitemlist.get(j).getProdNo());
			if(productitemlist.get(j).getProdNo()==0)
			productitemlist.get(j).setAmount(0.0);
		}
		
		List<ProductItem> previousProductItems = productitemlist;

		for(ProductItem productItem : previousProductItems){
				if(productItem.getAmount() !=null && productItem.getAmount()!=0)
					totalAmount +=productItem.getAmount();
		}
		OrderTrxnDetail orderTrxnDetail = new OrderTrxnDetail();
		orderTrxnDetail.setTrxnId(maxValueDAO.getMaxTrxnId());
		orderTrxnDetail.setOrderId(order.getOrderId());
		orderTrxnDetail.setTotal(Utils.formatDouble(totalAmount));
		
		
		double cgstAmount = 0.00;
		double sgstAmount = 0.00;
		double igstAmount = 0.00;
		double cgstRate = 0.00;
		double sgstRate = 0.00;
		double igstRate = 0.00;
		List<Address> address =null;
		if(order.getCustId() != 0){
			address = customerDAO.getAddressByCustId(order.getCustId());
			//System.out.println("address.size() :"+address.size());
		 if(totalAmount >0 ){
			if (address !=null){
		    	if (address.get(0).getStateId()==101){
		    		if(env.getProperty("tax.rate.cgst")!=null)
		    			cgstRate = Double.valueOf(env.getProperty("tax.rate.cgst"));
		    		if(env.getProperty("tax.rate.sgst")!=null)
		    			sgstRate = Double.valueOf(env.getProperty("tax.rate.sgst"));
		    		cgstAmount = Math.round(totalAmount * cgstRate / 100);
					sgstAmount = Math.round(totalAmount * sgstRate / 100);
					//csgtAmount= String.format("%.2f",cgstAmount);
					
		    	}else 
		    		{
		    		if(env.getProperty("tax.rate.igst")!=null)
		    			igstRate = Double.valueOf(env.getProperty("tax.rate.igst"));
		    			igstAmount = Math.round(totalAmount * igstRate / 100);
					}
		  }
		}
		
			if (address.get(0).getStateId()==101){
				orderTrxnDetail.setCgst(Utils.formatDouble((cgstAmount)));
				orderTrxnDetail.setSgst(Utils.formatDouble(sgstAmount));
				orderTrxnDetail.setSubTotal(Utils.formatDouble(totalAmount+cgstAmount+sgstAmount));
				orderTrxnDetail.setNetAmount(Utils.formatDouble(totalAmount+cgstAmount+sgstAmount));
			}else{
				orderTrxnDetail.setIgst(Utils.formatDouble(igstAmount));
				orderTrxnDetail.setSubTotal(Utils.formatDouble(totalAmount+igstAmount));
				orderTrxnDetail.setNetAmount(Utils.formatDouble(totalAmount+igstAmount));
			}
			
		  orderTrxnDetail.setBalance(Utils.formatDouble(orderTrxnDetail.getNetAmount()));
		}else{
			 if(totalAmount >0 ){
				 if(env.getProperty("tax.rate.cgst")!=null)
		    			cgstRate = Double.valueOf(env.getProperty("tax.rate.cgst"));
		    		if(env.getProperty("tax.rate.sgst")!=null)
		    			sgstRate = Double.valueOf(env.getProperty("tax.rate.sgst"));
		    		cgstAmount = Math.round(totalAmount * cgstRate / 100);
					sgstAmount = Math.round(totalAmount * sgstRate / 100);
					orderTrxnDetail.setCgst(Utils.formatDouble((cgstAmount)));
					orderTrxnDetail.setSgst(Utils.formatDouble(sgstAmount));
					orderTrxnDetail.setSubTotal(Utils.formatDouble(totalAmount+cgstAmount+sgstAmount));
					//orderTrxnDetail.setSubTotal(Utils.formatDouble(totalAmount+cgstAmount+sgstAmount));
					orderTrxnDetail.setNetAmount(Utils.formatDouble(totalAmount+cgstAmount+sgstAmount));
				    orderTrxnDetail.setBalance(Utils.formatDouble(orderTrxnDetail.getNetAmount()));
			 }
			
		}
		
		order.getOrderTrxnDetails().add(orderTrxnDetail);
		setMetaData(modelAndView);
		setDeportments(modelAndView,order);
		List<DeliveryType> deliveryTypes = metaDAO.getDeliveryTypes();
		List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
		modelAndView.addObject(DELIVERY_TYPES, deliveryTypes);
		modelAndView.addObject(PAYMENT_TYPES, paymentTypes);
		modelAndView.addObject(ORDER, order);
		return modelAndView;
	}
	
	
	
	@RequestMapping(value="/workorderpayment",method = RequestMethod.POST)
	public ModelAndView saveWorkorderPayment(HttpServletRequest request, @ModelAttribute("order") Order order){
		logger.info("--- saveWorkorderPayment ---");
		ModelAndView modelAndView = new ModelAndView(PAGE_WORKORDER_DETAIL);
		try{
		
			Double currentBalance = 0.00;
			if(order.getCreditBalance() !=null && order.getCreditBalance() !=0)
				currentBalance = order.getCreditBalance() + order.getOrderTrxnDetails().get(0).getBalance();
		
			else
				currentBalance = order.getOrderTrxnDetails().get(0).getBalance();
			
			Customer customer = null;
			if(order.getCustId() != 0)    
				customer = customerDAO.getCustomerById(order.getCustId());
			
			if((customer == null || customer.getCreditLimit() == null || customer.getCreditLimit()==0) ||(currentBalance <= customer.getCreditLimit())){
				// Update order detail
				order.setCreatedDate(new Date());
				String uname=null;
				uname = cookie.getCookie(request,"empid");
				order.setBillBy(uname);
				order.setGstNo(customer.getGstNo());
				//System.out.println("GST no :" + order.getGstNo());
				setOrderStatus(order, env.getProperty("status.submitted"));
				saveOrUpdate(order);
				/*int prod=productlist.size();
				int proditem=productitemlist.size();*/
				long prodno=0; 
				Product product= new Product();
				ProductItem productitem= new ProductItem();
				for (int i=0;i<productlist.size();i++){
					if (productlist.get(i).getNoOfSheet()!=0){
					    product=productlist.get(i);
						saveOrUpdate(product);
					}
					for(int j=0;j<productitemlist.size();j++){
					if(productitemlist.get(j).getQuantity()>0 && productitemlist.get(j).getProdNo() !=0 &&  productlist.get(i).getProdNo()==productitemlist.get(j).getProdNo()){
					productitemlist.get(j).setProductId(maxValueDAO.getMaxProductId()>1? maxValueDAO.getMaxProductId()-1:1);
					//productitemlist.get(j).setAmount(0.0);
					productitem=productitemlist.get(j);
					saveOrUpdate(productitem);
					}
					}
				}		
				
				//Save transaction details
				OrderTrxnDetail orderTrxnDetail = order.getOrderTrxnDetails().get(0);
				orderTrxnDetail.setOrderId(order.getOrderId());
				orderTrxnDetail.setCreatedDate(new Date());
				saveOrUpdate(orderTrxnDetail);
				//Update customer credit balance
				
				/*if(order.getCustId() !=0){
					customer.setCreditBalance(Utils.formatDouble(currentBalance));
					saveOrUpdate(customer);
				}*/
				
				if(order.getCustId() !=0){
					Double currentCreditBalance	= orderDAO.getCurrentCreditBalance(order.getCustId());
					customer.setCreditBalance(Utils.formatDouble(currentCreditBalance));
					saveOrUpdate(customer);
				}
				
				//Save Job allocation details
				List<JobStatus> jobStatus =  metaDAO.getMetaData(new JobStatus());
				Map<String,Integer> statusMap = Utils.getJobStatusMap(jobStatus);
				List<Deportment> deportments = metaDAO.getDeportments();
				Map<Long,String> depoMap = Utils.getTypes(deportments);
				Map<String,Integer> departmentMap = Utils.getDepotMap(deportments);
				int designDepoId = departmentMap.get(env.getProperty("dept.design"));
				int openId= statusMap.get(env.getProperty("status.open"));
				int stubmittedId= statusMap.get(env.getProperty("status.submitted"));
				List<Employee> employees = userDAO.getEmployees();
				Map<Long,Integer> employeeMap = Utils.getEmployeeIdMap(employees);
				JobAllocationId id =null;
				for(JobAllocation jobAllocation: order.getJobAllocations()){
					if(jobAllocation.getCustId()!=0){
						id = new JobAllocationId();
						id.setDepotId(employeeMap.get(jobAllocation.getCustId()));
						id.setOrderId(order.getOrderId());
						jobAllocation.setId(id);
						if(jobAllocation.getId().getDepotId() == designDepoId || 
								depoMap.get((long)jobAllocation.getId().getDepotId()).toLowerCase().endsWith(env.getProperty("depo.type.end.pad").toLowerCase())){
							jobAllocation.setStatusId(openId);
							//assigning jobpending counter to the employee to whom statis is open
							//System.out.println("Job employee id :"+jobAllocation.getCustId());
// created for jobopencount in employee table						
							/*for (Employee emp:employees){
								//System.out.println("employee id :"+emp.getEmpId());
								if (emp.getEmpId()==jobAllocation.getCustId()){
									emp.setJobOpenCount(emp.getJobOpenCount() !=null ?
										emp.getJobOpenCount()+1:1); 
								      saveOrUpdate(emp);
								}
							}*/ 
// created for jobopencount in employee table								
						}else
							jobAllocation.setStatusId(stubmittedId);
						
						jobAllocation.setInTime(DateUtils.toDate(DateUtils.todayDateTime()));
						jobAllocation.setCreatedDate(DateUtils.toDate(DateUtils.todayDateTime()));
						jobAllocation.setUpdatedDate(DateUtils.toDate(DateUtils.todayDateTime()));
						saveOrUpdate(jobAllocation);
						
					}
				}
				modelAndView.addObject("ordersaved","yes");
				double billamt=Math.round(order.getOrderTrxnDetails().get(0).getSubTotal());
				String billDate = DateUtils.toDate(new Date());
				String billAmount = order.getOrderTrxnDetails().get(0).getSubTotal()!=null ? String.format("%.2f",billamt):"0";
				String advance = order.getOrderTrxnDetails().get(0).getAdvance()!=null?order.getOrderTrxnDetails().get(0).getAdvance().toString():"0";
				String balance = order.getOrderTrxnDetails().get(0).getBalance()!=null?order.getOrderTrxnDetails().get(0).getBalance().toString():"0";
				String smsBalanceLimit = env.getProperty("sms.credit.balance.limit"); 
				//System.out.println("smslimit :"+smsBalanceLimit);
				String netamt= order.getOrderTrxnDetails().get(0).getNetAmount().toString();	
				/*if(!StringUtils.isEmpty(customer.getMobileNo()) && balance !=null && smsBalanceLimit!=null && Double.valueOf(balance)> Double.valueOf(smsBalanceLimit))*/
			if (order.getCustId()!=0)	
				if(!StringUtils.isEmpty(customer.getMobileNo()) && smsBalanceLimit!=null && Double.valueOf(netamt)> Double.valueOf(smsBalanceLimit))
					smsService.sendSMS(String.valueOf(order.getOrderId()), billDate, billAmount, advance, balance, customer.getMobileNo().toString());
					
				}else{
				modelAndView.addObject("message", "Your credit balance is higher than allowed credit limit");
			}
			
			
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("saveWorkorderPayment :: " + _exception);
		}finally{
			setDeportments(modelAndView,order);
			List<DeliveryType> deliveryTypes = metaDAO.getDeliveryTypes();
			List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
			modelAndView.addObject(DELIVERY_TYPES, deliveryTypes);
			modelAndView.addObject(PAYMENT_TYPES, paymentTypes);
			modelAndView.addObject(ORDER, order);
		}
		productlist.clear();
		productitemlist.clear();
		lastprodid.setProductId(null);
		prodno=0;
		onedit=0;
		//System.out.println(order.getOrderId());
		//System.out.println("productlist size after clear():"+productlist.size());
		return modelAndView;
	}
	
	@RequestMapping(value="/customerorder",method = RequestMethod.POST)
	public ModelAndView customerOrder(ModelAndView modelAndView, @ModelAttribute Customer customer){
		logger.info(" ------ customerOrder ------");
		Order order = new Order();
		order.setCustName(customer.getFirstName());
		order.setCustType(customer.getCustType());
		order.setCustAddr1(customer.getAddress().get(0).getAddress1());
		order.setCustAddr2(customer.getAddress().get(0).getAddress2());
		order.setOrderId(maxValueDAO.getMaxOrderId());
		order.setCustId(customer.getCustId());
		order.setGstNo(customer.getGstNo());
		/* Modified for CURRENT CUSTOMER BALANCE*/
		 //order.setCreditBalance(customer.getCreditBalance());
		if(order.getCustId() !=0){
			Double currentCreditBalance	= orderDAO.getCurrentCreditBalance(order.getCustId());
			if (currentCreditBalance !=null){
				order.setCreditBalance(currentCreditBalance);
			}else{
				order.setCreditBalance(0.00);
			}
				
			   
		}
		order.setRateType(customer.getRateType());
		Product product = new Product();
		order.getProducts().add(product);
		setDefaultItem(order);
		/*for(int i=1;i<7;i++){
			ProductItem productItem = new ProductItem();
			order.getProducts().get(0).getProductItems().add(productItem);
		}*/
		
		setMetaData(modelAndView);
		modelAndView.addObject(PRODUCT_ITEMS, order.getProducts().get(0).getProductItems());
		modelAndView.addObject(ORDER, order);
		modelAndView.setViewName(PAGE_WORKORDER);
		return modelAndView;
	}

	@RequestMapping(value="/workorderedit",method = RequestMethod.POST)
	public ModelAndView editWorkorder(@ModelAttribute("order") Order order){
		logger.info("--- editWorkorder ---");
		ModelAndView modelAndView = new ModelAndView(PAGE_WORKORDER);
		//order = orderDAO.getOrderById(order.getOrderId());
		List<Product> currentproductlist=new ArrayList<Product>();
		for(int i=0;i<productlist.size();i++){
				if(productlist.get(i).getProdNo() !=0)
				currentproductlist.add(productlist.get(i));
			
		}
		//System.out.println("productlist.prodno"+productlist.get(0).getProdNo());
		//System.out.println("currentproductlist size"+currentproductlist.size());
/*		List<Product> existingProducts = customerDAO.getProductByOrderId(order.getOrderId());*/
		List<Product> existingProducts = currentproductlist;
		onedit=1;
	//	order.setProducts(existingProducts);
		
		List<ProductType> productTypes = new ArrayList<ProductType>();
		 if(existingProducts !=null && !existingProducts.isEmpty()){
			 for(Product product : existingProducts){
				 for(ProductType productType:metaDAO.getProductTypes()){
					 if(productType.getId() == product.getProductTypeId()){
						 productTypes.add(productType);
						 break;
					 }
				 }
			 }
		 }
		modelAndView.addObject(PRODUCTS, productTypes);
		if(existingProducts.size() !=0){
			//System.out.println("yes product is !=0");
		List<ProductSize> existsProductSizes = metaDAO.getProductSizes((int)existingProducts.get(0).getProductTypeId());
		modelAndView.addObject(PRODUCT_SIZES,existsProductSizes);
		}
		/*List<ProductItem> existsProductItems = customerDAO.getProductItemsByProductId(existingProducts.get(0).getProductId());*/
		// added here to replace the above commented line
		List<ProductItem> selectedproditem= new ArrayList<ProductItem>();
		//System.out.println("productitemlist.size() " + productitemlist.size());
		//System.out.println("existingProducts.get(0).getProdNo() "+ existingProducts.get(0).getProdNo());
		for(int i=0;i<productitemlist.size();i++){
			//System.out.println("inside loop " + productitemlist.get(i).getProdNo());
		
			if (existingProducts.get(0).getProdNo()== productitemlist.get(i).getProdNo() )
				selectedproditem.add(productitemlist.get(i));
		}			
		List<ProductItem> existsProductItems = selectedproditem;
		while(existsProductItems.size() < 4){
			existsProductItems.add(new ProductItem());
		}
		existingProducts.get(0).setProductItems(existsProductItems);
		modelAndView.addObject(PRODUCT_ITEMS, existsProductItems);
		
		List<ProductItemType> productItemTypes= metaDAO.getProductItemTypesByProductId((int)existingProducts.get(0).getProductTypeId());
		modelAndView.addObject(PRODUCT_ITEM_TYPES, productItemTypes);
		
		modelAndView.addObject(ORDER, order);
		order.setEditMode(true);
		setEditMetaData(modelAndView);
		//System.out.println("yes ok here i am working");
		return modelAndView;
	}
	
	@RequestMapping(value="/deleteproduct/{prodno}",method = RequestMethod.GET)
	public ModelAndView deleteProduct( ModelAndView modelAndView,HttpServletRequest request,
			@ModelAttribute("order") Order order, @PathVariable long prodno){
		logger.info("--- deleteProduct ---");
		modelAndView = new ModelAndView(PAGE_WORKORDER);
		//System.out.println("PRODNO to be DELETED  :"+prodno);
		List<Product> currentproductlist=new ArrayList<Product>();
		List<ProductItem> currentproditems= new ArrayList<ProductItem>();
		/*for(int i=0;i<productlist.size();i++)
			System.out.println("Product nos  :"+productlist.get(i).getProdNo());

		long lastproduct= productlist.get(productlist.size()-1).getProdNo();
		System.out.println("Product no to be deleted:"+prodno);
		System.out.println("lastproduct no to bedeleted:"+lastproduct);*/

				for(int i=0;i<productlist.size();i++){
					/*if(productlist.get(i).getProdNo() !=prodno && productlist.get(i).getProdNo() !=lastproduct){*/
					if(productlist.get(i).getProdNo() !=prodno){
					currentproductlist.add(productlist.get(i));
					//System.out.println("NON TARGET PRODUCT  :"+productlist.get(i).getProdNo());
					/*}*/
					//System.out.println("TARGET PRODUCT  :"+productlist.get(i).getProdNo());
					
					for(int j=0;j<productitemlist.size();j++){
						//System.out.println("ITEMLIST PRODUCTNO :" + productitemlist.get(j).getProdNo());
					
						if (productlist.get(i).getProdNo() == productitemlist.get(j).getProdNo())
							currentproditems.add(productitemlist.get(j));
						}
					}
		}
		productlist=currentproductlist;
		productitemlist=currentproditems;
	    //System.out.println("redireted to workorderdetail");
	    
	   Double totalAmount = 0.00;
		//List<ProductItem> previousProductItems = customerDAO.getProductItemsByOrderId(order.getOrderId());
		
				
		List<ProductItem> previousProductItems = productitemlist;

		for(ProductItem productItem : previousProductItems){
				if(productItem.getAmount() !=null && productItem.getAmount()!=0)
					totalAmount +=productItem.getAmount();
		}
		OrderTrxnDetail orderTrxnDetail = new OrderTrxnDetail();
		orderTrxnDetail.setTrxnId(maxValueDAO.getMaxTrxnId());
		orderTrxnDetail.setOrderId(order.getOrderId());
		orderTrxnDetail.setTotal(Utils.formatDouble(totalAmount));
		
		
		double cgstAmount = 0.00;
		double sgstAmount = 0.00;
		double igstAmount = 0.00;
		double cgstRate = 0.00;
		double sgstRate = 0.00;
		double igstRate = 0.00;
		List<Address> address =null;
		if(order.getCustId() != 0){
			address = customerDAO.getAddressByCustId(order.getCustId());
			//System.out.println("address.size() :"+address.size());
		 if(totalAmount >0 ){
			if (address !=null){
		    	if (address.get(0).getStateId()==101){
		    		if(env.getProperty("tax.rate.cgst")!=null)
		    			cgstRate = Double.valueOf(env.getProperty("tax.rate.cgst"));
		    		if(env.getProperty("tax.rate.sgst")!=null)
		    			sgstRate = Double.valueOf(env.getProperty("tax.rate.sgst"));
		    		cgstAmount = Math.round(totalAmount * cgstRate / 100);
					sgstAmount = Math.round(totalAmount * sgstRate / 100);
					//csgtAmount= String.format("%.2f",cgstAmount);
					
		    	}else 
		    		{
		    		if(env.getProperty("tax.rate.igst")!=null)
		    			igstRate = Double.valueOf(env.getProperty("tax.rate.igst"));
		    			igstAmount = Math.round(totalAmount * igstRate / 100);
					}
		  }
		}
		
			if (address.get(0).getStateId()==101){
				orderTrxnDetail.setCgst(Utils.formatDouble((cgstAmount)));
				orderTrxnDetail.setSgst(Utils.formatDouble(sgstAmount));
				orderTrxnDetail.setSubTotal(Utils.formatDouble(totalAmount+cgstAmount+sgstAmount));
				orderTrxnDetail.setNetAmount(Utils.formatDouble(totalAmount+cgstAmount+sgstAmount));
			}else{
				orderTrxnDetail.setIgst(Utils.formatDouble(igstAmount));
				orderTrxnDetail.setSubTotal(Utils.formatDouble(totalAmount+igstAmount));
				orderTrxnDetail.setNetAmount(Utils.formatDouble(totalAmount+igstAmount));
			}
			
		  orderTrxnDetail.setBalance(Utils.formatDouble(orderTrxnDetail.getNetAmount()));
		}else{
			 if(totalAmount >0 ){
				 if(env.getProperty("tax.rate.cgst")!=null)
		    			cgstRate = Double.valueOf(env.getProperty("tax.rate.cgst"));
		    		if(env.getProperty("tax.rate.sgst")!=null)
		    			sgstRate = Double.valueOf(env.getProperty("tax.rate.sgst"));
		    		cgstAmount = Math.round(totalAmount * cgstRate / 100);
					sgstAmount = Math.round(totalAmount * sgstRate / 100);
					orderTrxnDetail.setCgst(Utils.formatDouble((cgstAmount)));
					orderTrxnDetail.setSgst(Utils.formatDouble(sgstAmount));
					orderTrxnDetail.setSubTotal(Utils.formatDouble(totalAmount+cgstAmount+sgstAmount));
					//orderTrxnDetail.setSubTotal(Utils.formatDouble(totalAmount+cgstAmount+sgstAmount));
					orderTrxnDetail.setNetAmount(Utils.formatDouble(totalAmount+cgstAmount+sgstAmount));
				    orderTrxnDetail.setBalance(Utils.formatDouble(orderTrxnDetail.getNetAmount()));
			 }
			
		}
		//System.out.println("PRODUCT ITEM LIST COUNT   :"+ productitemlist.size());	
		
			
		order.getOrderTrxnDetails().add(orderTrxnDetail);
		setMetaData(modelAndView);
		setDeportments(modelAndView,order);
		List<DeliveryType> deliveryTypes = metaDAO.getDeliveryTypes();
		List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
		modelAndView.addObject(DELIVERY_TYPES, deliveryTypes);
		modelAndView.addObject(PAYMENT_TYPES, paymentTypes);
		modelAndView.addObject(ORDER, order);
		//return new ModelAndView("redirect:/workorderdetail"); 
		return modelAndView;
	}
	
	@RequestMapping(value="/editproductitems",method = RequestMethod.POST)
	public ModelAndView editProductItems(@ModelAttribute("order") Order order){
		logger.info("--- editProductItems ---");
		ModelAndView modelAndView = new ModelAndView(PAGE_WORKORDER);
		/*order = orderDAO.getOrderById(order.getOrderId());*/
		//System.out.println("Order size :"+order.getOrderId());
		//List<Product> selectedProducts = customerDAO.getProductByOrderId(order.getOrderId(), order.getProducts().get(0).getProductTypeId());
		
		//System.out.println("I am at editproducts");
		List<Product> selectprod= new ArrayList<Product>();
		for (int i=0;i<productlist.size();i++){
			if (productlist.get(i).getProductTypeId()==order.getProducts().get(0).getProductTypeId() )
				selectprod.add(productlist.get(i));
		}
		List<Product> selectedProducts = selectprod;
		//System.out.println("selectedpreoducts size :"+selectedProducts.size());
		
		
		//Setting product dropdown value
		/*List<Product> existsProducts = customerDAO.getProductByOrderId(order.getOrderId());*/	
		List<Product> existsProducts = productlist;
		//System.out.println("existsProducts size :"+existsProducts.size());
		order.setProducts(selectedProducts);
		List<ProductType> productTypes = new ArrayList<ProductType>();
		 if(existsProducts !=null && !existsProducts.isEmpty()){
			 for(Product product : existsProducts){
				 for(ProductType productType:metaDAO.getProductTypes()){
					 if(productType.getId() == product.getProductTypeId()){
						 productTypes.add(productType);
						 break;
					 }
				 }
			 }
		 }
		// Setting product size
		List<ProductSize> selectedProductSizes = metaDAO.getProductSizes((int)selectedProducts.get(0).getProductTypeId());
		
		
		//List<ProductItem> selectedProductItems = customerDAO.getProductItemsByProductId(selectedProducts.get(0).getProductId());
		List<ProductItem> selectproditem= new ArrayList<ProductItem>();
		for (int i=0;i<productitemlist.size();i++)
			if (productitemlist.get(i).getProdNo()==selectedProducts.get(0).getProdNo() )
				selectproditem.add(productitemlist.get(i));
	
		List<ProductItem> selectedProductItems=selectproditem;
		while(selectedProductItems.size() < 6){
			selectedProductItems.add(new ProductItem());
		}
		selectedProducts.get(0).setProductItems(selectedProductItems);
		
		List<ProductItemType> productItemTypes= metaDAO.getProductItemTypesByProductId((int)selectedProducts.get(0).getProductTypeId());

		order.setEditMode(true);
		modelAndView.addObject(PRODUCTS, productTypes);
		modelAndView.addObject(PRODUCT_SIZES,selectedProductSizes);
		modelAndView.addObject(PRODUCT_ITEM_TYPES, productItemTypes);
		modelAndView.addObject(PRODUCT_ITEMS, selectedProductItems);
		setEditMetaData(modelAndView);
		modelAndView.addObject(ORDER, order);
		return modelAndView;
	}

	@RequestMapping(value="/editorder/{orderId}",method = RequestMethod.POST)
	public ModelAndView editOrder(@PathVariable long orderId){
		logger.info("--- editProductItems ---");
		ModelAndView modelAndView = new ModelAndView(PAGE_WORKORDER);
		Order order = orderDAO.getEditOrderById(orderId);
		if(order != null){
		List<Product> selectedProducts = customerDAO.getProductByOrderId(order.getOrderId());
		//Setting product dropdown value
		List<Product> existsProducts = customerDAO.getProductByOrderId(order.getOrderId());
		order.setProducts(selectedProducts);
		List<ProductType> productTypes = new ArrayList<ProductType>();
		 if(existsProducts !=null && !existsProducts.isEmpty()){
			 for(Product product : existsProducts){
				 for(ProductType productType:metaDAO.getProductTypes()){
					 if(productType.getId() == product.getProductTypeId()){
						 productTypes.add(productType);
						 break;
					 }
				 }
			 }
		 }
		//Setting product size
		List<ProductSize> selectedProductSizes = metaDAO.getProductSizes((int)selectedProducts.get(0).getProductTypeId());
		List<ProductItem> selectedProductItems = customerDAO.getProductItemsByProductId(selectedProducts.get(0).getProductId());
		while(selectedProductItems.size() < 4){
			selectedProductItems.add(new ProductItem());
		}
		selectedProducts.get(0).setProductItems(selectedProductItems);
		
		List<ProductItemType> productItemTypes= metaDAO.getProductItemTypesByProductId((int)selectedProducts.get(0).getProductTypeId());

		
		modelAndView.addObject(PRODUCTS, productTypes);
		modelAndView.addObject(PRODUCT_SIZES,selectedProductSizes);
		modelAndView.addObject(PRODUCT_ITEM_TYPES, productItemTypes);
		modelAndView.addObject(PRODUCT_ITEMS, selectedProductItems);
		modelAndView.addObject(ORDER, order);
		order.setEditMode(true);
		setEditMetaData(modelAndView);
		}else{
			cancelWorkorder(modelAndView);
			modelAndView.addObject("message", "No order found");
		}
		return modelAndView;
	}
//  cancel order added
	@RequestMapping(value="/cancelorder/{orderId}",method = RequestMethod.POST)
	public ModelAndView cancelOrder(@PathVariable long orderId){
		logger.info("--- editProductItems ---");
		System.out.println("From controller/ cancelorder  :");
		ModelAndView modelAndView = new ModelAndView(CANCEL_WORKORDER);
		Order order = orderDAO.getEditOrderById(orderId);
		/*boolean canDelete=orderDAO.getReceiptPaymentOrderById(orderId);
		if(canDelete==true){
			//cancelWorkorder(modelAndView);
			modelAndView.addObject("message", "U can't Delete, Since You'e Received Payment of this Order");
			return modelAndView;
		}*/
		if(order != null){
		List<Product> selectedProducts = customerDAO.getProductByOrderId(order.getOrderId());
		//Setting product dropdown value
		/* if partial data stored i.e. order exist and product details not exist - pass a message */
		if (selectedProducts == null || selectedProducts.isEmpty()){
			cancelWorkorder(modelAndView);
			modelAndView.addObject("message", "No Sales Order Details found");
			return modelAndView;
		}
		/* if partial data stored i.e. order exist and product details not exist - pass a message*/
		List<Product> existsProducts = customerDAO.getProductByOrderId(order.getOrderId());
		order.setProducts(selectedProducts);
		List<ProductType> productTypes = new ArrayList<ProductType>();
		 if(existsProducts !=null && !existsProducts.isEmpty()){
			 for(Product product : existsProducts){
				 for(ProductType productType:metaDAO.getProductTypes()){
					 if(productType.getId() == product.getProductTypeId()){
						 productTypes.add(productType);
						 break;
					 }
				 }
			 }
		 }
		//Setting product size
		List<ProductSize> selectedProductSizes = metaDAO.getProductSizes((int)selectedProducts.get(0).getProductTypeId());
		List<ProductItem> selectedProductItems = customerDAO.getProductItemsByProductId(selectedProducts.get(0).getProductId());
		while(selectedProductItems.size() < 4){
			selectedProductItems.add(new ProductItem());
		}
		selectedProducts.get(0).setProductItems(selectedProductItems);
		
		List<ProductItemType> productItemTypes= metaDAO.getProductItemTypesByProductId((int)selectedProducts.get(0).getProductTypeId());

		
		modelAndView.addObject(PRODUCTS, productTypes);
		modelAndView.addObject(PRODUCT_SIZES,selectedProductSizes);
		modelAndView.addObject(PRODUCT_ITEM_TYPES, productItemTypes);
		modelAndView.addObject(PRODUCT_ITEMS, selectedProductItems);
		modelAndView.addObject(ORDER, order);
		order.setEditMode(true);
		setEditMetaData(modelAndView);
		}else{
			cancelWorkorder(modelAndView);
			modelAndView.addObject("message", "No order found");
		}
		return modelAndView;
	}

	
	@RequestMapping(value="/workorder/cancel",method = RequestMethod.GET)
	public ModelAndView cancelWorkorder(ModelAndView modelAndView){
		logger.info("---- cancelWorkorder :: GET ----");
		Order order = new Order();
		order.setCancelMode(true);
		Product product = new Product();
		order.getProducts().add(product);
		
		while(order.getProducts().get(0).getProductItems().size() < 4){
			ProductItem productItem = new ProductItem();
			order.getProducts().get(0).getProductItems().add(productItem);
			
		}
		setMetaData(modelAndView);
		modelAndView.addObject(PRODUCT_ITEMS, order.getProducts().get(0).getProductItems());
		modelAndView.addObject(ORDER, order);
		modelAndView.setViewName(CANCEL_WORKORDER);
		return modelAndView;
	}
	
	@RequestMapping(value="/workordercancel",method = RequestMethod.POST)
	public ModelAndView cancelOrder(@ModelAttribute("order") Order order,ModelAndView modelAndView){
		logger.info("---- cancelOrder :: POST ----");
		Order order1 = new Order();	
		order1 = orderDAO.getOrderById(order.getOrderId());
		 System.out.println("Order number :"+order.getOrderId());
		boolean canDelete=orderDAO.getReceiptPaymentOrderById(order.getOrderId());
		if(canDelete==true){
			System.out.println("canDelete is TRUE");
			//cancelWorkorder(modelAndView);
			modelAndView.setViewName(CANCEL_WORKORDER);
			modelAndView.addObject("message", "U can't Delete, Since You'e Received Payment for this Order");
			return modelAndView;
		}
		order=order1;
		setOrderStatus(order, env.getProperty("status.cancel"));
		
		// creditbalance to be debited wr.r.to cancellation  
		 
		List<OrderTrxnDetail> otd = new ArrayList<OrderTrxnDetail>();
		otd = orderDAO.getTransactionByOrderId(order.getOrderId()); 
		if (otd !=null && !otd.isEmpty()){
		Double netamt=otd.get(0).getNetAmount();
		/*	Double balanceAmt=otd.get(0).getBalance();*/
			
		/* UPDATED FOR CURRENT CUSTOMER BALNCE*/	
			
		Customer customer =new Customer();
		//customer=customerDAO.getCustomerById(order.getCustId());
		//if(order.getCustId() !=0)
		//	customer.setCreditBalance(Double.valueOf(customer.getCreditBalance())- balanceAmt);
		if(order.getCustId() !=0){
			Double currentCreditBalance	= orderDAO.getCurrentCreditBalance(order.getCustId());
			if (currentCreditBalance !=null){
				customer.setCreditBalance(Double.valueOf(currentCreditBalance- netamt));
			}
			/*else{
				customer.setCreditBalance(Double.valueOf(0.00- netamt));
			}*/
		saveOrUpdate(customer);
		}
		}
		// creditbalance to be debited wr.r.to cancellation
		saveOrUpdate(order);
		// cancllation on job allcoation tablestarts
		List<JobAllocation> jobs= new ArrayList<JobAllocation>();
		jobs = orderDAO.getJobAllocationByOrderId(order.getOrderId());
		//System.out.println("jobs size: "+jobs.size());
		if(jobs !=null && !jobs.isEmpty())
		{
		for (int i=0; i<jobs.size();i++){
				jobs.get(i).setStatusId(106);
				jobs.get(i).setUpdatedDate(order.getUpdatedDate());
				saveOrUpdate(jobs.get(i));}
		
		}
		// cancellation on job allocation table ends
		order = new Order();
		order.setOrderId(maxValueDAO.getMaxOrderId());
		Product product = new Product();
		order.getProducts().add(product);
		setDefaultItem(order);
		//Customer customer=new Customer();
		
		/*while(order.getProducts().get(0).getProductItems().size() < 4){
			ProductItem productItem = new ProductItem(); 
			order.getProducts().get(0).getProductItems().add(productItem);
		}*/ 
		setMetaData(modelAndView);
		modelAndView.addObject(PRODUCT_ITEMS, order.getProducts().get(0).getProductItems());
		modelAndView.addObject(ORDER, order);
		modelAndView.setViewName(CANCEL_WORKORDER);
		return modelAndView;
	}
	
	
	@RequestMapping(value="/workordereditsave",method = RequestMethod.POST)
	public ModelAndView editWorkorderSave(@ModelAttribute("order") Order order){
		logger.info("--- editWorkorderSave ---");
		ModelAndView modelAndView = new ModelAndView(PAGE_WORKORDER);
		setOrderStatus(order, env.getProperty("status.pending"));
		order.setCreatedDate(new Date());
		saveOrder(order);
		//List<Product> existingProducts = customerDAO.getProductByOrderId(order.getOrderId());
		List<Product> existingProducts = productlist;
		order.setProducts(existingProducts);
		List<ProductType> productTypes = new ArrayList<ProductType>();
		 if(existingProducts !=null && !existingProducts.isEmpty()){
			 for(Product product : existingProducts){
				 for(ProductType productType:metaDAO.getProductTypes()){
					 if(productType.getId() == product.getProductTypeId()){
						 productTypes.add(productType);
						 break;
					 }
				 }
			 }
		 }
		modelAndView.addObject(PRODUCTS, productTypes);
		
		List<ProductSize> existingProductSizes = metaDAO.getProductSizes((int)existingProducts.get(0).getProductTypeId());
		modelAndView.addObject(PRODUCT_SIZES,existingProductSizes);
		
		// added here to replace the below commented line
		List<ProductItem> selectedproditem= new ArrayList<ProductItem>();
		for(int i=0;i<productitemlist.size();i++)
			if (existingProducts.get(0).getProdNo()== productitemlist.get(i).getProdNo() )
				selectedproditem.add(productitemlist.get(i));
		List<ProductItem> existsProductItems = selectedproditem;
		//List<ProductItem> existsProductItems = customerDAO.getProductItemsByProductId(existingProducts.get(0).getProductId());
		while(existsProductItems.size() < 4){
			existsProductItems.add(new ProductItem());
		}
		existingProducts.get(0).setProductItems(existsProductItems);
		modelAndView.addObject(PRODUCT_ITEMS, existsProductItems);
		
		List<ProductItemType> productItemTypes= metaDAO.getProductItemTypesByProductId((int)existingProducts.get(0).getProductTypeId());
		modelAndView.addObject(PRODUCT_ITEM_TYPES, productItemTypes);
		order.setEditMode(true);
		setEditMetaData(modelAndView);
		modelAndView.addObject(ORDER, order);
		return modelAndView;
	}
	
	
	@RequestMapping(value="/addrow",method = RequestMethod.POST)
	public ModelAndView addRow(@ModelAttribute("order") Order order){
		logger.info("--- addRow ---");
		ModelAndView modelAndView = new ModelAndView(PAGE_WORKORDER);
		order.getProducts().get(0).getProductItems().add(new ProductItem());
		setMetaData(modelAndView);
		modelAndView.addObject(PRODUCT_ITEMS, order.getProducts().get(0).getProductItems());
		modelAndView.addObject(ORDER, order);
		return modelAndView;
	}
	
	@RequestMapping(value="/editmodeaddrow",method = RequestMethod.POST)
	public ModelAndView editModeAddRow(@ModelAttribute("order") Order order){
		logger.info("--- editModeAddRow ---");
		ModelAndView modelAndView = new ModelAndView(PAGE_WORKORDER);
		order.getProducts().get(0).getProductItems().add(new ProductItem());
		setMetaData(modelAndView);
		List<ProductItemType> productItemTypes = metaDAO.getProductItemTypesByProductId((int)order.getProducts().get(0).getProductTypeId());
		modelAndView.addObject(PRODUCT_ITEM_TYPES, productItemTypes);
		modelAndView.addObject(PRODUCT_ITEMS, order.getProducts().get(0).getProductItems());
		modelAndView.addObject(ORDER, order);
		order.setEditMode(true);
		return modelAndView;
	}
	
	
	
	@RequestMapping(value="/rate/{rateId}/{sizeId}/{itemId}",method = RequestMethod.GET)
	public @ResponseBody ItemSizeRate getRate(@PathVariable String rateId,@PathVariable String sizeId,@PathVariable String itemId){
		System.out.println("Rate Details :"+rateId+"  "+sizeId+"  "+itemId);
		ItemSizeRate rate = new ItemSizeRate();
		rate.setRateId(Integer.valueOf(rateId));
		rate.setSizeId(Integer.valueOf(sizeId));
		rate.setItemId(Integer.valueOf(itemId));
		ItemSizeRate itemSizeRate = null;
		if(!StringUtils.isEmpty(rateId))
			itemSizeRate = customerDAO.getRate(rate);
		else
			itemSizeRate = new ItemSizeRate();
		if(itemSizeRate  == null)
			itemSizeRate = new ItemSizeRate();
		return itemSizeRate;
	}
	
	@RequestMapping(value="/productsize/{productId}",method = RequestMethod.GET)
	public @ResponseBody List<ProductSize> getProductSizes(@PathVariable String productId){
		return  metaDAO.getProductSizes(Integer.valueOf(productId));
		
	}
	
	private void setMetaData(ModelAndView modelAndView){
	  	List<OrderType> orderTypes = metaDAO.getOrderTypes();
	  	modelAndView.addObject(ORDER_TYPES, orderTypes);
	  	
		List<CustomerType> customerTypes = metaDAO.getCustomerTypes();
//		CustomerType customerType = new CustomerType();
//		customerType.setId(Integer.valueOf(env.getProperty("cust.type.amuter.id")));
//		customerType.setName(env.getProperty("cust.type.amuter.name"));
		modelAndView.addObject(CUSTOMER_TYPES, customerTypes);
//		customerTypes.add(customerType);
		modelAndView.addObject(CUSTOMER_TYPES, customerTypes);
		
		List<ProductType> products = metaDAO.getProductTypes();
		modelAndView.addObject(PRODUCTS, products);
		
		List<ProductItemType> productItemTypes = metaDAO.getProductItemTypes(products.get(0).getId());
		modelAndView.addObject(PRODUCT_ITEM_TYPES, productItemTypes);
		
		List<ProductSize> productSizes = metaDAO.getProductSizes(products.get(0).getId());
		modelAndView.addObject(PRODUCT_SIZES,productSizes);
	}
  
  	private void setEditMetaData(ModelAndView modelAndView){
	  	List<OrderType> orderTypes = metaDAO.getOrderTypes();
	  	modelAndView.addObject(ORDER_TYPES, orderTypes);
	  	
		List<CustomerType> customerTypes = metaDAO.getCustomerTypes();
		CustomerType customerType = new CustomerType();
		customerType.setId(Integer.valueOf(env.getProperty("cust.type.amuter.id")));
		customerType.setName(env.getProperty("cust.type.amuter.name"));
		customerTypes.add(customerType);
		modelAndView.addObject(CUSTOMER_TYPES, customerTypes);
  	}
	  
  	private void setDeportments(ModelAndView modelAndView,Order order){
  		Map<String,List<Employee>> empDesgs = new LinkedHashMap<String, List<Employee>>();
	  	List<Employee> designer = new ArrayList<Employee>();
	  	List<Employee> paders = new ArrayList<Employee>();
	  	List<Employee> offer= new ArrayList<Employee>();
	  	List<Employee> printers = new ArrayList<Employee>();
	  	List<Employee> binders = new ArrayList<Employee>();
	  	List<Employee> laminators = new ArrayList<Employee>();
	  	List<Employee> despatchers = new ArrayList<Employee>();
	  	List<Deportment> deportments = metaDAO.getDeportments();
	  	List<Employee> employees = userDAO.getEmployees();
	  	Map<String,Integer> depoMap= Utils.getDepotMap(deportments);
//	  	List<ProductType> prodTypes =metaDAO.getMetaData(new ProductType());
//		Map<Long,String> prodTypeMap = Utils.getTypes(prodTypes);
		List<ProductItemType> prodItemTypes= metaDAO.getProductItemTypes();
		Map<Long,String> prodItemTypeMap =Utils.getTypes(prodItemTypes);
	  	Long prodItemTypeId = 0L;
	  	
	  /*	List<ProductItem> previousProductItems = customerDAO.getProductItemsByOrderId(order.getOrderId());*/
		List<ProductItem> previousProductItems = productitemlist;
	  	for(ProductItem pItem:previousProductItems){
			if(pItem.getQuantity()!=null && pItem.getQuantity()> 0&&  prodItemTypeMap.get(pItem.getProdItemTypeId()).toLowerCase().endsWith(env.getProperty("depo.type.end.pad")))
			prodItemTypeId = pItem.getProdItemTypeId();
		}
		/*if(order.getProducts()!=null && !order.getProducts().isEmpty()){
			for(Product p:order.getProducts()){
				if(prodTypeMap.get(p.getProductTypeId()).equals(env.getProperty("prod.type.pad"))){
					if(p.getProductItems()!=null && !p.getProductItems().isEmpty()){
						for(ProductItem pItem:p.getProductItems()){
							if(pItem.getQuantity()!=null && pItem.getQuantity()> 0&&  prodItemTypeMap.get(pItem.getProdItemTypeId()).toLowerCase().endsWith(env.getProperty("depo.type.end.pad")))
							prodItemTypeId = pItem.getProdItemTypeId();
						}
					}
				}
			}
		}*/
				
	  	for(Employee employee: employees){
	  		if(employee.getDepotId() == depoMap.get(env.getProperty("dept.design"))){
	  			designer.add(employee);
	  		}
	  		if(prodItemTypeId!=0){
	  			if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.normal"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){	
	  		  			paders.add(employee);
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.3d"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
	  		  			paders.add(employee);
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.sparkal"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
	  		  			paders.add(employee);
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.metalic"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
	  		  			paders.add(employee);
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.emposs"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
	  		  			paders.add(employee);
	  		  		}
	  				// added for new empose type PADs
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.mettalemposs"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
	  		  			paders.add(employee);
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.velvetteemposs"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
	  		  			paders.add(employee);
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.cloudemposs"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
	  		  			paders.add(employee);
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.amazepad"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
	  		  			paders.add(employee);
	  		  		}
	  // added bellow pads on 23/12/2021 				
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.woodenpad"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
	  		  			paders.add(employee);
	  		  		}
	  				
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.woodenboxpad"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
	  		  			paders.add(employee);
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.foccusiyapad"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
	  		  			paders.add(employee);
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.foccusiyapink"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
	  		  			paders.add(employee);
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.foccusiyagreen"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
	  		  			paders.add(employee);
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.foccusiyablue"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
	  		  			paders.add(employee);
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.foccusiyatang"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
	  		  			paders.add(employee);
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.foccusiyayellow"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
	  		  			paders.add(employee);
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.fullgloss4mmpad"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
	  		  			paders.add(employee);
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.fullgloss5mmpad"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
	  		  			paders.add(employee);
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.dvdbox"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
	  		  			paders.add(employee);
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.pendrivebox"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
	  		  			paders.add(employee);
	  		  		}
	  				
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.agralic"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
	  		  			paders.add(employee);
	  		  		//System.out.println("Hello from Agarlic employee  loop..");
	  		  		}
	  				// added for new Agarlic type PADs
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.splagarlic"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from Special  employee  loop..");
	  		  			paders.add(employee); 
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.boxagarlic"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from box employee  loop..");
	  		  			paders.add(employee); 
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.printboxagarlic"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from printbox employee  loop..");
	  		  			paders.add(employee); 
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.handleagarlic"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
	  		  			paders.add(employee); 
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.bagandagarlicpad"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
	  		  			paders.add(employee); 
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.windowsmall"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
	  		  			paders.add(employee); 
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.windowbig"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
	  		  			paders.add(employee); 
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.boxbagemposs"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
	  		  			paders.add(employee); 
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.printinboxpad"))){
	  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
	  		  			paders.add(employee); 
	  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.dynamicapad"))){
		  				if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
			  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.unicagoldpad"))){
			  			if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
				  		  		//System.out.println("Hello from handle employee  loop..");
			  		  			paders.add(employee); 
			  			}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.embossehandlepad"))){
				 		if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
				 		  		//System.out.println("Hello from handle employee  loop..");
						  			paders.add(employee); 
				 		}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.agarlicandembosspad"))){
						if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
				  		  		//System.out.println("Hello from handle employee  loop..");
				  		  			paders.add(employee); 
				  		  }
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.smokepad"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.mangalalbum"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.vinylpad"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.budgetpadbag"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.laptophandlepad"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.vaiboga"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.hyderoleatherpad"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.premiunagarlicpad"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.premiumagarlicbox"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.2mmfullglasspad"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.vaibogaagarlicbox"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.hydrodoublebreifcase"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.hydroleatherpadwboxbag"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.hydroleatherpadbag"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.hydroleatherpadbox"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.budgetpad"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.dynamicapadboxbag"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.unicagoldpad"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.boxbagplayitpad"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			/* Below added 21/08/2022   */
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.type2smokepad"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.fifagoldpad"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.reyagoldpad"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.motionvideoalbumboxpad"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.reyagoldpadbox"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.trancypadbox"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.trancypadboxbag"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.fifagoldpadbox"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.mettalempossbox"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.smokepadbox"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.agarlicwithemposspad"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.vinylpadbox"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.type2agarlicpad"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.doubleDpad"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.twinpadbox"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.icepad"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.icepadbox"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.npad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.articapad"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.jewellbox"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			else if(prodItemTypeMap.get(prodItemTypeId).equalsIgnoreCase(env.getProperty("prod.item.type.premiumboxwithstand"))){
					if(employee.getDepotId() == depoMap.get(env.getProperty("dept.apad"))){
		  		  		//System.out.println("Hello from handle employee  loop..");
		  		  			paders.add(employee); 
		  		  	}
	  			}
	  			
	  			
	  			
	  			
	  			
	  			
	  			
	  	  			
	  			
	  		}
	  		
	  		if(employee.getDepotId() == depoMap.get(env.getProperty("dept.printing"))){
	  			printers.add(employee);
	  		}
	  		if(employee.getDepotId() == depoMap.get(env.getProperty("dept.binding"))){
	  			binders.add(employee);
	  		}
	  		if(employee.getDepotId() == depoMap.get(env.getProperty("dept.lamination"))){
	  			laminators.add(employee);
	  		}
	  		if(employee.getDepotId() == depoMap.get(env.getProperty("dept.offer"))){
	  			offer.add(employee);
	  		}
	  		if(employee.getDepotId() == depoMap.get(env.getProperty("dept.despatch"))){
	  			despatchers.add(employee);
	  		}
	  	}
	  	if(!designer.isEmpty())
	  		empDesgs.put(env.getProperty("job.title.design"), designer);
	  	if(!paders.isEmpty())
	  		empDesgs.put(env.getProperty("job.title.pad"), paders);
	  	if(!printers.isEmpty())
	  		empDesgs.put(env.getProperty("job.title.print"), printers);
	  	if(!laminators.isEmpty())
	  		empDesgs.put(env.getProperty("job.title.lamination"), laminators);
	  	if(!binders.isEmpty())
	  		empDesgs.put(env.getProperty("job.title.bind"), binders);
	  	if(!binders.isEmpty())
	  		empDesgs.put(env.getProperty("job.title.offer"), offer);
	  	if(!despatchers.isEmpty())
	  		empDesgs.put(env.getProperty("job.title.despatch"), despatchers);
	  	
		/*modelAndView.addObject(DESIGNERS,designer);q
		modelAndView.addObject(PADERS,paders);
		modelAndView.addObject(PRINTERS,printers);
		modelAndView.addObject(BINDERS,binders);
		modelAndView.addObject(LAMINATORS,laminators);
		modelAndView.addObject(DESPATCHERS,despatchers);*/
		
		modelAndView.addObject("empDesgs", empDesgs);
  	}
	  
	  private void saveOrder(Order order){
		order.setCreatedDate(new Date());
		//saveOrUpdate(order);
		Product product = order.getProducts().get(0);
		product.setOrderId(order.getOrderId());
		product.setCreatedDate(new Date());
		//System.out.println("Existing view product No :"+product.getProdNo());
	
		//System.out.println("order: productId:"+order.getProdId());
		//System.out.println("product: productId:"+product.getProductId());
		//Product savedProduct =  saveOrUpdate(product);
		long temp=0;
		int index=0;
		int indexitem=0;
		//
		//System.out.println("onedit mode:"+onedit);
		List<ProductItem> productItem_temp = new  ArrayList<ProductItem>();
		productItem_temp=order.getProducts().get(0).getProductItems();
		if (onedit==1){
		if ((product.getNoOfSheet()!= null) && (product.getNoOfSheet()!=0) && productItem_temp.get(0).getQuantity()>0){
		//	if ((product.getNoOfSheet()!= null) && (product.getNoOfSheet()!=0)) {
			//System.out.println("product item type Id:"+product.getProductTypeId());
			
				for(int i=0;i<productlist.size();i++){
					//System.out.println("productlist itemtypeId:"+productlist.get(i).getProductTypeId());	
					if ( product.getProductTypeId()==productlist.get(i).getProductTypeId()){
						index= productlist.indexOf(productlist.get(i));
						temp = productlist.get(i).getProdNo();
						//System.out.println("productlist prodno:"+temp);
						product.setProdNo(productlist.get(i).getProdNo());
					    productlist.set(index,product);
					    for( int j=0;j<productitemlist.size();j++){
							if( productitemlist.get(j).getProdNo()==temp){
								//System.out.println("productlistitem prodno:"+productitemlist.get(j).getProdNo());
								indexitem=productitemlist.indexOf(productitemlist.get(j));
								// changing existing list prodNo into 0 (ZERO) instead of deleting row in the list
								productitemlist.get(j).setProdNo(0);
								System.out.println("index item :"+indexitem);
								productitemlist.set(indexitem,productitemlist.get(j)); 
								
							}
					    }
				//System.out.println("onedit mode TRUE:"+onedit);
				//System.out.println("productlist size:"+productlist.size());
					}
				}
				for(ProductItem productItem : order.getProducts().get(0).getProductItems()){
					if(productItem.getQuantity()!=null && productItem.getQuantity() > 0){
						productItem.setOrderId(order.getOrderId());
						productItem.setProdNo(temp);
						//System.out.println("productId======:"+((order.getProdId())-1));
						//productItem.setProductId(productlist.size());
						productItem.setProductId(lastprodid.getProductId());
						productItem.setCreatedDate(new Date());
						//productItem.setProdItemId(order.getProdItemId()+1);
						//order.setProdItemId(order.getProdItemId()+1);
						productitemlist.add(productItem); 
						//System.out.println("productItemlist size:"+productitemlist.size());
						//System.out.println("product No :"+prodno);
					} 
						//ProductItem savedProductItem = saveOrUpdate(productItem);
						//productItem.setProdItemId(savedProductItem.getProdItemId());
					}
				}
			 if (product.getNoOfSheet()==0){
					for(int i=0;i<productlist.size();i++){
						//System.out.println("productlist itemtypeId:"+productlist.get(i).getProductTypeId());	
						if ( product.getProductTypeId()==productlist.get(i).getProductTypeId()){
							index= productlist.indexOf(productlist.get(i));
							temp = productlist.get(i).getProdNo();	
						}
				    for( int j=0;j<productitemlist.size();j++){
						if( productitemlist.get(j).getProdNo()==temp){
							productitemlist.get(j).setProdNo(0);
							productitemlist.get(j).setAmount(0.0);
							indexitem=productitemlist.indexOf(productitemlist.get(j));
							product.setProdNo(0);
							productlist.set(index,product);
							productitemlist.set(indexitem, productitemlist.get(j));
							}
				    	}
					}
			  }
			 /*for( int j=0;j<productitemlist.size();j++){
					System.out.println("productitemlist:item type ("+j+")"+productitemlist.get(j).getProdItemTypeId());
					System.out.println("productitemlist: quantituy ("+j+")"+productitemlist.get(j).getQuantity());
					System.out.println("productitemlist: prod no("+j+")"+productitemlist.get(j).getProdNo());
					System.out.println("productitemlist:rate ("+j+")"+productitemlist.get(j).getRate());
					System.out.println("productitemlist:amount ("+j+")"+productitemlist.get(j).getAmount());
			    }*/
			
			}else{
				try{
				
//				List<ProductItem> productItem_temp = new  ArrayList<ProductItem>();
//				productItem_temp=order.getProducts().get(0).getProductItems();
					if ((product.getNoOfSheet()!= null) && (product.getNoOfSheet()!=0)&& productItem_temp.get(0).getQuantity()>0){
				//	if ((product.getNoOfSheet()!= null) && (product.getNoOfSheet()!=0)){	
		product.setProdNo(++prodno);
		product.setProductId(order.getProdId());
		order.setProdId((order.getProdId()+1));
	
		//System.out.println("productId:"+product.getProductId());
		productlist.add(product);
		//System.out.println("Existing view product No :"+productlist.get(0).getProdNo());
		//System.out.println("onedit mode FALSE :"+onedit);
		//System.out.println("productlist size:"+productlist.size());
		//product.setProductId(savedProduct.getProductId());
		//orderDAO.deleteOrderItems(order.getOrderId(), order.getProdId()-1);
		for(ProductItem productItem : order.getProducts().get(0).getProductItems()){
			if(productItem.getQuantity()!=null && productItem.getQuantity() > 0){
				productItem.setOrderId(order.getOrderId());
				productItem.setProdNo(prodno);
				//System.out.println("productId======:"+((order.getProdId())-1));
				//productItem.setProductId(productlist.size());
				//productItem.setProductId(lastprodid.getProductId());
				productItem.setCreatedDate(new Date());
				//productItem.setProdItemId(order.getProdItemId()+1);
				//order.setProdItemId(order.getProdItemId()+1);
				productitemlist.add(productItem);
				//System.out.println("productItemlist size:"+productitemlist.size());
				//System.out.println("product No :"+prodno);

				//ProductItem savedProductItem = saveOrUpdate(productItem);
				//productItem.setProdItemId(savedProductItem.getProdItemId());
			}
		}
		
	
	  }
	}
	  
	  catch(Exception e){}
		lastprodid.setProductId(lastprodid.getProductId()+1);
	  }
	  }	  
	  private <T> T saveOrUpdate(T t){
		try{
			t = customerDAO.saveOrUpdate(t);
		}catch (Exception _exception) {
			logger.error(_exception);
		}
		return t;
	  }
	  
	  private void setOrderStatus(Order order,String status){
		  List<JobStatus> jobStatus = metaDAO.getMetaData(new JobStatus());
		  if(jobStatus!=null && !jobStatus.isEmpty()){
			  for (JobStatus jStatus : jobStatus) {
				if(jStatus.getName().equals(status)){
					order.setStatus(jStatus.getId());
					 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
				     Date date = new Date();
				     order.setUpdatedDate((date));
					break;
				}   
			  }
		  }
	  }
	  
	  private void setDefaultItem(Order order){
		  while(order.getProducts().get(0).getProductItems().size()<4){
				ProductItem productItem = new ProductItem();
				order.getProducts().get(0).getProductItems().add(productItem);
			}
	  }
	  
	 
}
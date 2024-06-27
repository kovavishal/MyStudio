/**
 * 
 */
package com.studio.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.studio.constants.WorkOrderConstants;
import com.studio.dao.CustomerDAO;
import com.studio.dao.MetaDAO;
import com.studio.dao.OrderDAO;
import com.studio.dao.UserDAO;
import com.studio.domain.Customer;
import com.studio.domain.CustomerType;
import com.studio.domain.DeliveryType;
import com.studio.domain.Deportment;
import com.studio.domain.Employee;
import com.studio.domain.JobAllocation;
import com.studio.domain.JobStatus;
import com.studio.domain.Order;
import com.studio.domain.OrderType;
import com.studio.domain.Product;
import com.studio.domain.ProductItem;
import com.studio.domain.ProductItemType;
import com.studio.domain.ProductSize;
import com.studio.domain.ProductType;
import com.studio.domain.Value;
import com.studio.service.SmsService;
import com.studio.utils.DateUtils;
import com.studio.utils.Utils;
/*import com.studio.utils.sendMail;*/

/**
 * @author ezhilraja_k
 *
 */

@RestController
@RequestMapping(value="/order")
@PropertySource("classpath:studio.properties")
public class OrderStatusController implements WorkOrderConstants{

	
	@Autowired
	private CustomerDAO customerDAO;
	
	@Autowired
	private OrderDAO orderDAO;
	
	@Autowired
	private MetaDAO metaDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private SmsService smsService;
	@Autowired
	private Environment env;
	
	private List<String> jobFlow = new ArrayList<String>();
	private List<JobAllocation> allocatedJobs= new ArrayList<JobAllocation>();
	private Map<Long,String> depoIdMap = new HashMap<Long, String>();
	Map<String,Integer> jobStatusMap = new HashMap<String, Integer>();
	
	Logger logger = Logger.getLogger(OrderStatusController.class);
	
	@RequestMapping(value="/status",method = RequestMethod.GET)
	public ModelAndView orderStatus(){
		ModelAndView modelAndView = new ModelAndView(PAGE_ORDER_STATUS);
		Order order = new Order();
		List<CustomerType> customerTypes = metaDAO.getCustomerTypes();
		modelAndView.addObject(CUSTOMER_TYPES, customerTypes);
		List<JobStatus> jobStatus = metaDAO.getMetaData(new JobStatus());
		modelAndView.addObject("orderstatus", jobStatus);
		modelAndView.addObject(ORDER, order);
		return modelAndView;
	}
	
	@RequestMapping(value="/autocomplete/customer",method = RequestMethod.GET)
	public @ResponseBody List<Value> autoCompleteAgents(@RequestParam String searchTerm){
		List<Value> values = new ArrayList<Value>();
		logger.info("Search term : " + searchTerm);
		List<String> orders = orderDAO.getOrderCustomers(searchTerm);
		for(String name : orders){
			Value value = new Value();
			value.setName(name);
//			value.setId(String.valueOf(Employee.getEmpId()));
			values.add(value);
		}
		return values;
	}
	
	

	
	@RequestMapping(value="/search",method = RequestMethod.POST)
	public ModelAndView productItemDetails(@ModelAttribute Order order){
		ModelAndView modelAndView = new ModelAndView(PAGE_ORDER_STATUS);
		if(!StringUtils.isEmpty(order.getStrOrderId()))
			order.setOrderId(Long.valueOf(order.getStrOrderId()));
		List<Order> orders = orderDAO.getOrders(order);
		for(Order or : orders){
			List<JobAllocation> jobAllocations = orderDAO.getJobAllocationByOrderId(or.getOrderId());
			or.setJobAllocations(jobAllocations);
			or.setStrOrderDate(DateUtils.toDate(or.getOrderDate(),true));
			or.setStrDueDate(DateUtils.toDate(or.getDueDate(),true));
		}
		List<JobStatus> jobStatus = metaDAO.getMetaData(new JobStatus());
		Map<Integer , String> statusMap = getJobStatus(jobStatus);
		List<Deportment> deportments = metaDAO.getDeportments();
		Map<String,Integer> depotMap = Utils.getDepotMap(deportments);
		Map<Long,String> depatmap = Utils.getTypes(deportments);
		List<CustomerType> customerTypes = metaDAO.getCustomerTypes();
		modelAndView.addObject(CUSTOMER_TYPES, customerTypes);
		modelAndView.addObject(ORDER, order);
		modelAndView.addObject("orders", orders);
		modelAndView.addObject("orderstatus", jobStatus);
		modelAndView.addObject("jobStatus", statusMap);
		modelAndView.addObject("depots", depotMap);
		modelAndView.addObject("depatmap", depatmap);
		
		return modelAndView;
	}
	
	@RequestMapping(value="/review/{orderId}",method = RequestMethod.POST)
	public ModelAndView orderReview(@ModelAttribute Order order,@PathVariable Long orderId){
		ModelAndView modelAndView = new ModelAndView(PAGE_ORDER_REVIEW);
		try{
			Order orderReview = orderDAO.getOrderById(orderId);
			Customer customer=new Customer();
			if(orderReview.getCustType() == 103){
				customer.setFirstName(orderReview.getCustName());
			}else{
				customerDAO.getCustomerById(orderReview.getCustId());	
			}
			 
			if(customer.getCreditBalance() != null)
				orderReview.setCreditBalance(Utils.formatDouble(customer.getCreditBalance()));
			orderReview.setMobileNo(customer.getMobileNo());
			
			List<ProductItem> productItems = customerDAO.getProductItemsByOrderId(orderId);
			for(ProductItem item:productItems){
				
				List<ProductItemType> itemTypes = metaDAO.getProductItemTypesById((int)item.getProdItemTypeId());
				for(ProductItemType itemType : itemTypes){
					if(itemType.getId() == item.getProdItemTypeId())
						item.setProdItemName(itemType.getName());
				}
			}
			List<JobAllocation> jobAllocations = orderDAO.getJobAllocationByOrderId(orderId);
			
			//SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
			for(JobAllocation job : jobAllocations){
				if(job.getOutTime() !=null){
					long duration  = job.getInTime().getTime() - job.getOutTime().getTime();
					//long seconds = duration / 1000 % 60;
					long mins = duration / (60 * 1000) % 60;
					long hours = duration / (60 * 60 * 1000) % 24;
					long days = duration / (24 * 60 * 60 * 1000);
					//System.out.println(days + "days:" + hours + "hours:" + mins + "mins");
					job.setTimeTaken(days + " days" + hours + " hours" + mins + " mins");
				}
			}
			List<Deportment> deportments = metaDAO.getDeportments();
			Map<Integer,String> departments = getDepartments(deportments);
			List<Employee> employees = userDAO.getEmployees();
			Map<Long,String> empNames = Utils.getEmployeeNameMap(employees);
			modelAndView.addObject("order", order);
			modelAndView.addObject("orderReview", orderReview);
			modelAndView.addObject("productItems", productItems);
			modelAndView.addObject("jobAllocations", jobAllocations);
			modelAndView.addObject("departments", departments);
			modelAndView.addObject("empNames", empNames);
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error(_exception);
		}
		
		return modelAndView;
	}
	
	@RequestMapping(value="/employeeorderreview/{orderId}",method = RequestMethod.GET)
	public ModelAndView employeeOrderReview(@ModelAttribute Order order,@PathVariable Long orderId){
		ModelAndView modelAndView = new ModelAndView(PAGE_EMPLOYEE_ORDER_REVIEW);
		try{
			Order orderReview = orderDAO.getOrderById(orderId);
			Customer customer = customerDAO.getCustomerById(orderReview.getCustId());
			if(customer!=null){
				orderReview.setCreditBalance(Utils.formatDouble(customer.getCreditBalance()));
				orderReview.setMobileNo(customer.getMobileNo());
			}
			List<ProductItem> productItems = customerDAO.getProductItemsByOrderId(orderId);
			List<Product> products = customerDAO.getProductByOrderId(orderId);
			Map<Long,Product> productMap = Utils.getIdProducts(products);
			
			List<ProductType> productTypes = metaDAO.getProductTypes();
			Map<Long,String> pTypeMap =  Utils.getTypes(productTypes);
			if(products !=null && !products.isEmpty()){
				for(Product product:products){
					product.setName(pTypeMap.get(product.getProductTypeId()));
				}
			}
			
			for(ProductItem item:productItems){
				Product product = productMap.get(item.getProductId());
				List<ProductSize> productSizes = metaDAO.getProductSizes((int)product.getProductTypeId());
//				Map<Long,String> pSizeMap =  Utils.getTypes(productSizes);
				Map<Integer,String> pSizeMap = Utils.getIdProductSize(productSizes);
				List<ProductItemType> itemTypes = metaDAO.getProductItemTypesById((int)item.getProdItemTypeId());
				for(ProductItemType itemType : itemTypes){
					if(itemType.getId() == item.getProdItemTypeId())
						item.setProdItemName(itemType.getName());
						item.setProdSize(pSizeMap.get((int)product.getSize()));
				}
			}
			List<Deportment> deportments = metaDAO.getDeportments();
			Map<Integer,String> departments = getDepartments(deportments);
			modelAndView.addObject("order", order);
			modelAndView.addObject("orderReview", orderReview);
			modelAndView.addObject("productItems", productItems);
			modelAndView.addObject("departments", departments);
			modelAndView.addObject("products", products);
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error(_exception);
		}
		return modelAndView;
	}
	
	@RequestMapping(value="/allocation/{custId}",method = RequestMethod.POST)
	public ModelAndView getEmployeeJobs(@PathVariable long custId){
		ModelAndView modelAndView = new ModelAndView("employeeworkorder");
		JobAllocation jobAllocation = new JobAllocation();
		jobAllocation.setCustId(custId);
		Employee employee = userDAO.getEmployeeById(custId);
		Boolean isDespatch=false; 
		if(employee !=null && employee.getDepotId()!=null && employee.getDepotId() == 106)
			isDespatch = true;
		modelAndView.addObject("jobAllocation", jobAllocation);
		modelAndView.addObject("isDespatch", isDespatch);
		
		return modelAndView;
	}
	
	@RequestMapping(value="/allocation/{custId}",method = RequestMethod.GET)
	public ModelAndView getJobs(@PathVariable long custId){
		ModelAndView modelAndView = new ModelAndView("employeeworkorder");
		JobAllocation jobAllocation = new JobAllocation();
		jobAllocation.setCustId(custId);
		Employee employee = userDAO.getEmployeeById(custId);
		Boolean isDespatch=false; 
		if(employee !=null && employee.getDepotId()!=null && employee.getDepotId() == 106)
			isDespatch = true;
		modelAndView.addObject("jobAllocation", jobAllocation);
		modelAndView.addObject("isDespatch", isDespatch);
		/*try{
			jobAllocations = orderDAO.getJobs(custId);
			if(jobAllocations !=null && !jobAllocations.isEmpty()){
				Set<Long> orderIds = Utils.getOrderIds(jobAllocations);
				List<Long> ids = new ArrayList<Long>();
				ids.addAll(orderIds);
				List<Order> orders = orderDAO.getOrders(ids);
				Map<Long,Order> orderTypeIds = Utils.getIdOrders(orders);
				List<JobStatus> jobStatus = metaDAO.getJobStatus();
				Map<Integer,String> jobStatusMap =  Utils.getJobStatusIdMap(jobStatus);
				List<OrderType> orderTypes = metaDAO.getOrderTypes();
				Map<Long,String> orderTypeMap =  Utils.getTypes(orderTypes);
				for(JobAllocation jobAllocation : jobAllocations){
					jobAllocation.setStatusName(jobStatusMap.get(jobAllocation.getStatusId()));
					Order order = orderTypeIds.get(jobAllocation.getId().getOrderId());
					jobAllocation.setOrderType(orderTypeMap.get(Long.valueOf(order.getOrderType())));
				}
			}
			modelAndView.addObject("jobs", jobAllocations);
		}catch (Exception _exception) {
			logger.error("getJobs ::" +_exception);
			
		}*/
		return modelAndView;
	}
	@RequestMapping(value="/joballocation/{custId}",method = RequestMethod.POST)
	@ResponseBody
	public List<JobAllocation> getAllocatedJobs(@PathVariable long custId){
		List<JobAllocation> jobAllocations = new ArrayList<JobAllocation>();
		
		try{
			Employee employee = userDAO.getEmployeeById(custId);
			Boolean isDespatch=false;
			if(employee!=null && employee.getDepotId()!=null && employee.getDepotId() == 106)
				isDespatch=true;
			jobAllocations = orderDAO.getJobs(custId);
			if(jobAllocations !=null && !jobAllocations.isEmpty()){
				Set<Long> orderIds = Utils.getOrderIds(jobAllocations);
				List<Long> ids = new ArrayList<Long>();
				ids.addAll(orderIds);
				List<Order> orders = orderDAO.getOrders(ids);
				Map<Long,Order> orderTypeIds = Utils.getIdOrders(orders);
				//System.out.println("orderTypeIds SIZE :"+orderTypeIds.size());
				List<JobStatus> jobStatus = metaDAO.getMetaData(new JobStatus());
				Map<Integer,String> jobStatusMap =  Utils.getJobStatusIdMap(jobStatus);
				List<OrderType> orderTypes = metaDAO.getOrderTypes();
				List<DeliveryType> deliveryTypes = null;
				Map<Long,String> deliveryTypeMap = null;
				if(isDespatch){
				 deliveryTypes = metaDAO.getDeliveryTypes();
				 deliveryTypeMap =  Utils.getTypes(deliveryTypes);
				// System.out.println("IS IT DESPATCH ?????");
				}
				
				Map<Long,String> orderTypeMap =  Utils.getTypes(orderTypes);
				try{
				for(JobAllocation jobAllocation : jobAllocations){
					jobAllocation.setStatusName(jobStatusMap.get(jobAllocation.getStatusId()));
				//	System.out.println("AFTER --------------->>>ORDER TYPE :"+orderTypeIds.get(jobAllocation.getId().getOrderId()));
					try{
						if (orderTypeIds.get(jobAllocation.getId().getOrderId())!=null){
					Order order = orderTypeIds.get(jobAllocation.getId().getOrderId());
					jobAllocation.setOrderType(orderTypeMap.get(Long.valueOf(order.getOrderType())));
					jobAllocation.setOrderDate(DateUtils.toDate(DateUtils.toDate(jobAllocation.getOrderDate(),true),true));
					jobAllocation.setIsDespatch(isDespatch);
					if(isDespatch){
						if(order.getDeliveryTypeId()!=null)
						jobAllocation.setDeliveryMode(deliveryTypeMap.get(Long.valueOf(order.getDeliveryTypeId())));
						jobAllocation.setRemarks(order.getRemarks());
					}
						}
					}catch(Exception _exception) {
						_exception.printStackTrace();
						logger.error("Is it Null here ::" +_exception);
						
					}
				}
				}catch(Exception _exception) {
					_exception.printStackTrace();
					logger.error("getJobs ::" +_exception);
					
				}
			}
		}catch (Exception _exception) {
			_exception.printStackTrace();
			logger.error("getJobs ::" +_exception);
			
		}
		return jobAllocations;
	}
	
	@RequestMapping(value="/updatejob/{orderId}/{custId}",method = RequestMethod.POST)
	@ResponseBody
	public Boolean updateJob(@PathVariable long orderId,@PathVariable long custId){
		Boolean isUpdated = false;
		try{
			JobAllocation jobAllocation = orderDAO.getJobAllocationByOrderId(orderId, custId);
			List<JobStatus> jobStatus = metaDAO.getMetaData(new JobStatus());
//			Map<String,Integer> jobStatusMap = Utils.getJobStatusMap(jobStatus);
			this.jobStatusMap = Utils.getJobStatusMap(jobStatus);
			
			
			Employee employee = userDAO.getEmployeeById(custId);
			List<Deportment> deportments = metaDAO.getDeportments();
//			Map<String,Integer> depoMap = Utils.getDepotMap(deportments);
//			Map<Long,String> depoIdMap = Utils.getTypes(deportments);
//			List<JobAllocation> jobAllocations = orderDAO.getJobAllocationByOrderId(orderId);
			
			this.allocatedJobs =orderDAO.getJobAllocationByOrderId(orderId);
			this.depoIdMap = Utils.getTypes(deportments);
//			this.jobStatusMap = Utils.getJobStatusMap(jobStatus);
//			Map<Long,String> jobStatusMap = Utils.getTypes(jobStatus);
			Boolean isChangesStatus=false;
			
			
			if(!depoIdMap.get(employee.getDepotId().longValue()).equals(env.getProperty("dept.despatch"))){
				
				Boolean isPadderAvailable = false;
				Boolean isAgralicPad=false;
				Boolean isDesignCompleted =false;
				Boolean isPadCompleted=false;
				Boolean isPrintCompelted=false;
				
				for(JobAllocation job: this.allocatedJobs){
					if((depoIdMap.get((long)job.getId().getDepotId()).toLowerCase()).endsWith(env.getProperty("depo.type.end.pad").toLowerCase())){
						isPadderAvailable=true;
						if(jobStatusMap.get(env.getProperty("status.completed"))==job.getStatusId())
							isPadCompleted=true;
					}
					if(depoIdMap.get((long)job.getId().getDepotId()).equalsIgnoreCase(env.getProperty("dept.design")) ){
						if(jobStatusMap.get(env.getProperty("status.completed"))==job.getStatusId())
							isDesignCompleted=true;
					}
					if(depoIdMap.get((long)job.getId().getDepotId()).equalsIgnoreCase(env.getProperty("dept.printing")) ){
						if(jobStatusMap.get(env.getProperty("status.completed"))==job.getStatusId())
							isPrintCompelted=true;
					}
				}
				
				if(isPadderAvailable){
					List<ProductItem> productItems = customerDAO.getProductItemsByOrderId(orderId);
					List<ProductItemType> prodItemTypes= metaDAO.getProductItemTypes();
					Map<Long,String> prodItemTypeMap =Utils.getTypes(prodItemTypes);
					if(productItems!=null && !productItems.isEmpty()){
						for(ProductItem productItem : productItems){
							if(prodItemTypeMap.get(productItem.getProdItemTypeId()).equalsIgnoreCase(env.getProperty("dept.apad"))){
								isAgralicPad=true;
							}
						}
					}
					
					if(!isAgralicPad){
						if(depoIdMap.get(employee.getDepotId().longValue()).equals(env.getProperty("dept.design"))){
							if(isPadCompleted){
								loadFlow();
								isChangesStatus = updateNextUser(env.getProperty("dept.apad"));
							}else{
								isChangesStatus=true;
							}
						}
						else if(depoIdMap.get(employee.getDepotId().longValue()).toLowerCase().endsWith(env.getProperty("depo.type.end.pad"))){
							if(isDesignCompleted){
							loadFlow();
							isChangesStatus = updateNextUser(depoIdMap.get(employee.getDepotId().longValue()));
							}else{
								isChangesStatus=true;
							}
						}else{
							loadFlow();
							isChangesStatus = updateNextUser(depoIdMap.get(employee.getDepotId().longValue()));
						}
					}else{
						if(depoIdMap.get(employee.getDepotId().longValue()).equals(env.getProperty("dept.printing"))){
							if(isPadCompleted){
								loadFlow();
								isChangesStatus = updateNextUser(depoIdMap.get(employee.getDepotId().longValue()));
							}else{
								isChangesStatus=true;
							}
						}
						else if(depoIdMap.get(employee.getDepotId().longValue()).equalsIgnoreCase(env.getProperty("dept.apad"))){
							if(isPrintCompelted){
								loadFlow();
								isChangesStatus = updateNextUser(env.getProperty("dept.printing"));
							}else{
								isChangesStatus=true;
							}
						}else{
							loadFlow();
							isChangesStatus = updateNextUser(depoIdMap.get(employee.getDepotId().longValue()));
						}
					}
				}else{
					loadFlow();
					isChangesStatus = updateNextUser(depoIdMap.get(employee.getDepotId().longValue()));
				}
			}else{
				try{
				Order order = orderDAO.getOrderById(orderId);
				order.setStatus(jobStatusMap.get(env.getProperty("status.delivered")));
				order.setUpdatedDate(new Date());
				orderDAO.saveOrUpdate(order);
				isChangesStatus=true;

				
//				updation in employee tabel for despatch 	
			
		/*		List<Employee> employees = new ArrayList<Employee>();
				employees=userDAO.getEmployees();
				for(Employee emp:employees){
    				if (emp.getEmpId() ==jobAllocation.getCustId()){
    					emp.setJobOpenCount(emp.getJobOpenCount() !=null ?
    						emp.getJobOpenCount()-1:null);
    			System.out.println("Despatch updated");		
    					orderDAO.saveOrUpdate(emp);
    				}
				
			}*/
// updation on jobopencount in employee table				
				/* Customer customer = customerDAO.getCustomerById(order.getCustId());
				smsService.sendOrderCompletedSMS(String.valueOf(orderId), customer.getMobileNo().toString());*/
				}   
				catch(Exception e){}
			}   
			if(isChangesStatus){
				logger.info("=================== updating job as complete  ==============");
				jobAllocation.setStatusId(jobStatusMap.get(env.getProperty("status.completed")));
				jobAllocation.setOutTime(DateUtils.toDate(DateUtils.todayDateTime()));
				jobAllocation.setUpdatedDate(DateUtils.toDate(DateUtils.todayDateTime()));
				orderDAO.saveOrUpdate(jobAllocation);
				//System.out.println("Despatch updated :"+jobAllocation.getCustId());	
/*//	updation in employee tabel for despatch 	
				// updation on jobopencount in employee table
				List<Employee> employees = new ArrayList<Employee>();
				employees=userDAO.getEmployees();
				for(Employee emp:employees){
    				if (emp.getEmpId() ==jobAllocation.getCustId()){
    					emp.setJobOpenCount(emp.getJobOpenCount() !=null ?
    						emp.getJobOpenCount()-1:null);
    			System.out.println("Despatch updated");		
    					orderDAO.saveOrUpdate(emp);
    				}
				
			}*/
			/*if(depoIdMap.get(employee.getDepotId().longValue()).equals(env.getProperty("dept.design"))){
				for(JobAllocation job:jobAllocations){
					if(depoMap.get(env.getProperty("dept.pad"))== job.getId().getDepotId()){
						job.setStatusId(jobStatusMap.get(env.getProperty("status.open")));
						orderDAO.saveOrUpdate(job);
						break;
					}
				}
			}
			
			if(depoIdMap.get(employee.getDepotId().longValue()).equals(env.getProperty("dept.pad"))){
				for(JobAllocation job:jobAllocations){
					if(depoMap.get(env.getProperty("dept.printing"))== job.getId().getDepotId()){
						job.setStatusId(jobStatusMap.get(env.getProperty("status.open")));
						orderDAO.saveOrUpdate(job);
						break;
					}
				}
			}
			
			if(depoIdMap.get(employee.getDepotId().longValue()).equals(env.getProperty("dept.printing"))){
				for(JobAllocation job:jobAllocations){
					if(depoMap.get(env.getProperty("dept.binding"))== job.getId().getDepotId()){
						job.setStatusId(jobStatusMap.get(env.getProperty("status.open")));
						orderDAO.saveOrUpdate(job);
						break;
					}
				}
			}
			
			if(depoIdMap.get(employee.getDepotId().longValue()).equals(env.getProperty("dept.binding"))){
				for(JobAllocation job:jobAllocations){
					if(depoMap.get(env.getProperty("dept.lamination"))== job.getId().getDepotId()){
						job.setStatusId(jobStatusMap.get(env.getProperty("status.open")));
						orderDAO.saveOrUpdate(job);
						break;
					}
				}
			}
			
			if(depoIdMap.get(employee.getDepotId().longValue()).equals(env.getProperty("dept.lamination"))){
				for(JobAllocation job:jobAllocations){
					if(depoMap.get(env.getProperty("dept.despatch"))== job.getId().getDepotId()){
						job.setStatusId(jobStatusMap.get(env.getProperty("status.open")));
						orderDAO.saveOrUpdate(job);
						break;
					}
				}
			}
			
			if(depoIdMap.get(employee.getDepotId().longValue()).equals(env.getProperty("dept.despatch"))){
				Order order = orderDAO.getOrderById(orderId);
				order.setStatus(jobStatusMap.get(env.getProperty("status.delivered")));
				order.setUpdatedDate(new Date());
				orderDAO.saveOrUpdate(order);
			}*/
//			orderDAO.saveOrUpdate(jobAllocation);
			isUpdated=true;
			}
		}catch (Exception _exception) {
			logger.error("updateJob :: "+_exception);
			_exception.printStackTrace();
			isUpdated=false;
		}
		return isUpdated;
	}
	
	public Boolean updateNextUser(String user){
	//	System.out.println("current user :"+user);
		String nUser="";
		Boolean isChagesStatus=false;
		Boolean isNextUserAvail = false;
		List<Employee> employees = new ArrayList<Employee>();
		employees=userDAO.getEmployees();
		
		if(user.toLowerCase().endsWith(env.getProperty("depo.type.end.pad").toLowerCase()))
			user=env.getProperty("work.flow.pad");
		
/*// existing user openjobcount -1 in employee table
    	for(JobAllocation job:allocatedJobs){
    		if((depoIdMap.get((long)job.getId().getDepotId())).equals(user)){
    		//	System.out.println("CURRENT USER "+job.getCustId());
    			for(Employee emp:employees){
    				if (emp.getEmpId() ==job.getCustId()){
    					emp.setJobOpenCount(emp.getJobOpenCount() !=null ?
    						emp.getJobOpenCount()-1:null);
    				//	System.out.println("CURRENT USER :"+user+job.getCustId());
    					orderDAO.saveOrUpdate(emp);
    				}
    			}
    		}
    	}*/
	    	for(String sFlow : jobFlow){
	    	    if(sFlow.equals(user)){
	    	    	nUser=jobFlow.get(jobFlow.indexOf(sFlow)+1);
	    	    	
/*// existing user openjobcount -1 in employee table
	    	    	for(JobAllocation job:allocatedJobs){
	    	    		if((depoIdMap.get((long)job.getId().getDepotId())).equals(user)){
	    	    			System.out.println("CURRENT USER "+job.getCustId());
	    	    			for(Employee emp:employees){
	    	    				if (emp.getEmpId() ==job.getCustId()){
	    	    					emp.setJobOpenCount(emp.getJobOpenCount() !=null ?
		        						emp.getJobOpenCount()-1:null);
	    	    					System.out.println("CURRENT USER :"+user+job.getCustId());
	    	    					orderDAO.saveOrUpdate(emp);
	    	    				}
    	    			}
	    	    		}
	    	    	}
// next user openjobcount +1 in employee table
				for (JobAllocation job : allocatedJobs) {
					if (depoIdMap.get((long) job.getId().getDepotId()).equals(nUser)) {
						for (Employee emp : employees) {
							if (emp.getEmpId() == job.getCustId()) {
								emp.setJobOpenCount(emp.getJobOpenCount() != null ? 
									emp.getJobOpenCount() + 1 : 1);
							//	System.out.println("NEXT USER :" + user + job.getCustId());
								orderDAO.saveOrUpdate(emp);
							}
						}
					}
				}*/
	        		for(JobAllocation job:allocatedJobs){
	        			        			
	        			if(depoIdMap.get((long)job.getId().getDepotId()).equals(nUser)){
	        				isNextUserAvail=true;
		        			logger.info("Assigning job from " + user +" to " + nUser);
		        			job.setStatusId(jobStatusMap.get(env.getProperty("status.open")));
		        			//job.setCreatedDate(DateUtils.toDate(DateUtils.todayDateTime()));
		        			job.setUpdatedDate(DateUtils.toDate(DateUtils.todayDateTime()));
		        			orderDAO.saveOrUpdate(job);
		        			isChagesStatus=true;
	        			}
	        		}
	        		if(!isNextUserAvail){
	        			isChagesStatus = updateNextUser(nUser);
	        		}
	    	    }
	    	}
		return isChagesStatus;
	}
	
	private Map<Integer,String> getJobStatus(List<JobStatus> jobStatus){
		Map<Integer,String> sMap = new HashMap<Integer, String>();
		for(JobStatus jStatus: jobStatus){
			sMap.put(jStatus.getId(), jStatus.getName());
		}
		
		return sMap;
	}
	
	private Map<Integer,String> getDepartments(List<Deportment> deportments){
		Map<Integer,String> sMap = new HashMap<Integer, String>();
		for(Deportment deportment: deportments){
			sMap.put(deportment.getId(), deportment.getName());
		}
		
		return sMap;
	}
	


	
	private  void loadFlow(){
		logger.info("======= Loading work flow order========");
		jobFlow = 	Arrays.asList(env.getProperty("workflow").split(","));
	}
	   
	
	// added for customer outstanding 
	@RequestMapping(value="/outstandingInformationSMS/{mobile}",method = RequestMethod.POST)
	@ResponseBody
	public Boolean outstandingInformationSMS(@PathVariable String mobile[]){
		  
	
		Boolean isUpdated = false;
		try{
			
			for (int i=0;i<mobile.length;i++){
			//System.out.println("outstanding of :"+i+"is"+mobile[i]);
			String t2 = mobile[i];
			t2 = t2.substring(t2.indexOf("~")+1,t2.length()).trim();
			String t3 = mobile[i];
			t3 = t3.substring(0,t3.indexOf("~"));
			//System.out.println("t3 = mobile"+ t3);
			//System.out.println("t2 = Balance amt"+ t2);
			smsService.sendOutstandingInformationSMS(t2, t3);
			}
			isUpdated=true;
		}catch (Exception _exception) {
			logger.error("outstandingInformationSMS :: "+_exception);
			_exception.printStackTrace();
			isUpdated=false; 
		}
		return isUpdated;
	}	  
	
	@RequestMapping(value="/customervsbusinessSMS/{mobile}",method = RequestMethod.POST)
	@ResponseBody
	public Boolean customervsbusinessSMS(@PathVariable String[] mobile){
		
		//System.out.println("hello from controller");
		//System.out.println("mobile.size:"+(mobile.length));
		//System.out.println("mobile :"+(mobile));		
		Boolean isUpdated = false;
		try{
			
			for (int i=0;i<mobile.length;i++){
				/*System.out.println("outstanding of :"+i+"is"+mobile[i]);*/
				String t2 = mobile[i];
				t2 = t2.substring(t2.indexOf("~")+1,t2.length()).trim();
				String t3 = mobile[i];
				t3 = t3.substring(0,t3.indexOf("~"));
				//System.out.println("t3 = mobile"+ t3);
				//System.out.println("t2 = TEXT   :"+ t2);
				smsService.sendCustomerVsBusinessSMS(t2, t3);	
				
			}
			
			isUpdated=true;
		}catch (Exception _exception) {
			logger.error("customerVsBusinessSMS :: "+_exception);
			_exception.printStackTrace();
			isUpdated=false; 
		}
		return isUpdated;
	}	  
	
	
	
	
	
}

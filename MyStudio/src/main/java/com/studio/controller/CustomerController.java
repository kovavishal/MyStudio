/**
 * 
 */
package com.studio.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.studio.dao.CustomerDAO;
import com.studio.dao.MaxValueDAO;
import com.studio.dao.MetaDAO;
import com.studio.dao.UserDAO;
import com.studio.domain.Address;
import com.studio.domain.Area;
import com.studio.domain.Customer;
import com.studio.domain.CustomerJson;
import com.studio.domain.CustomerType;
import com.studio.domain.Deportment;
import com.studio.domain.Employee;
import com.studio.domain.RateType;
import com.studio.domain.Role;
import com.studio.domain.State;
import com.studio.domain.Supplier;
import com.studio.domain.Value;

/**
 * @author ezhilraja_k
 * 
 */

@Controller
public class CustomerController {
	
	
	@Autowired
	private MaxValueDAO maxValueDAO;

	@Autowired
	private CustomerDAO customerDAO;
	
	@Autowired
	private MetaDAO metaDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	Logger logger = Logger.getLogger(CustomerController.class);
	
	@RequestMapping(value = "/addcustomer", method = RequestMethod.GET)
	public ModelAndView addCustomer(ModelAndView  modelAndView,HttpServletRequest request){
		String addnew = request.getParameter("addnew");
		Customer customer = new Customer();
		customer.setCustId(maxValueDAO.getMaxCustId());
		Address address = new Address();
		address.setAddrId(maxValueDAO.getMaxAddrdId());
		customer.getAddress().add(address);
		customer.setAddNew(addnew);
		modelAndView.addObject(com.studio.constants.Customer.PAGE_CUSTOMER.getValue(),customer);
		modelAndView.setViewName(com.studio.constants.Customer.PAGE_ADD_CUSTOMER.getValue());
		setCustomerMetadata(modelAndView);
		return modelAndView;
	}
	
	@RequestMapping(value = "/editcustomer", method = RequestMethod.GET)
	public ModelAndView editCustomer(ModelAndView  modelAndView){
//		String addnew = request.getParameter("addnew");
		Customer customer = new Customer();
		customer = new Customer();
		Address address = new Address();
		customer.getAddress().add(address);
		customer.setEditMode(true);
//		customer.setAddNew(addnew);
		modelAndView.addObject(com.studio.constants.Customer.PAGE_CUSTOMER.getValue(),customer);
		modelAndView.setViewName(com.studio.constants.Customer.PAGE_ADD_CUSTOMER.getValue());
		setCustomerMetadata(modelAndView);
		return modelAndView;
	}
	
	@RequestMapping(value = "/editcustomer", method = RequestMethod.POST)
	public ModelAndView editCustomer(ModelAndView  modelAndView,@ModelAttribute Customer customer){
//		String addnew = request.getParameter("addnew");
//		Customer customer = new Customer();
		if(customer.getCustId()!= 0){
			customer = customerDAO.getCustomerById(customer.getCustId());
			List<Address> address = customerDAO.getAddressByCustId(customer.getCustId());
			customer.getAddress().clear();
			customer.setAddress(address);
		}else{
			customer = new Customer();
			Address address = new Address();
			customer.getAddress().add(address);
		}
		customer.setEditMode(true);
//		customer.setAddNew(addnew);
		modelAndView.addObject(com.studio.constants.Customer.PAGE_CUSTOMER.getValue(),customer);
		modelAndView.setViewName(com.studio.constants.Customer.PAGE_ADD_CUSTOMER.getValue());
		setCustomerMetadata(modelAndView);
		return modelAndView;
	}
	
	@RequestMapping(value = "/addcustomer", method = RequestMethod.POST)
	public ModelAndView saveCustomer(@ModelAttribute("customer") Customer customer,RedirectAttributes redirectAttributes){
		
		logger.info(" --- saveCustomer ---- ");
		
		ModelAndView modelAndView =null;
		
		try{
			
			customer.setCreatedDate(new Date());
			customer.setUpdatedDate(new Date());
			if(customer.isEditMode()){
				saveOrUpdate(customer);
			}else{
				save(customer);
			}
			Address address =customer.getAddress().get(0);
			address.setCustId(customer.getCustId());
			address.setCreatedDate(new Date());
			address.setUdpatedDate(new Date());
			if(customer.isEditMode()){
				saveOrUpdate(address);
			}else{
				save(address);
			}
			if(customer.getAddNew().equals("true")){
				modelAndView= new ModelAndView("forward:/customerorder");
			}else{
				modelAndView = new ModelAndView(com.studio.constants.Customer.PAGE_ADD_CUSTOMER.getValue());
				setCustomerMetadata(modelAndView);
				customer = new Customer();
				customer.setCustId(maxValueDAO.getMaxCustId());
				address = new Address();
				address.setAddrId(maxValueDAO.getMaxAddrdId());
				customer.getAddress().add(address);
				modelAndView.addObject("customer", customer);
			}
		}catch (Exception _exception) {
			logger.error(_exception);
		}
		return modelAndView;
	}
	
	private <T> void  save(T t){
		logger.info("save :: save" + t.getClass().getName());
		try{
			customerDAO.saveCustomer(t);
		}catch (Exception _exception) {
			logger.error(_exception);
		}
	}
	
	private <T> void  saveOrUpdate(T t){
		logger.info("saveOrUpdate :: save" + t.getClass().getName());
		try{
			customerDAO.saveOrUpdate(t);
		}catch (Exception _exception) {
			logger.error(_exception);
		}
	}
	
	
	@RequestMapping(value="/customer/address/{custId}",method = RequestMethod.GET)
	public @ResponseBody List<CustomerJson> autocompleteCustomer(@PathVariable String custId){
		List<CustomerJson> values = new ArrayList<CustomerJson>();
		logger.info("Search term : " + custId);
		List<Address> addressList = customerDAO.getAddressByCustId(Long.valueOf(custId));
		Customer customer = customerDAO.getCustomerById(Long.valueOf(custId));
		for(Address address : addressList){
			CustomerJson json = new CustomerJson();
			json.setId(custId);
			json.setAddr1(address.getAddress1());
			json.setAddr2(address.getAddress2());
			json.setBalance(customer.getCreditBalance());
			json.setRateType(customer.getRateType());
			json.setGstNo(customer.getGstNo());
			values.add(json);
		}
		return values;
	}
	
	@RequestMapping(value = "/employee", method = RequestMethod.GET)
	public ModelAndView employee(ModelAndView  modelAndView){
		try{
		modelAndView.setViewName(com.studio.constants.Customer.ADD_EMPLOYEE.getValue());
		setEmployeeMetadata(modelAndView,true);
		}catch (Exception _exception) {
			logger.error(_exception);
		}
		return modelAndView;
	}
	
	@RequestMapping(value = "/editemployee", method = RequestMethod.GET)
	public ModelAndView editEmployee(ModelAndView  modelAndView){
		try{
			modelAndView.setViewName(com.studio.constants.Customer.ADD_EMPLOYEE.getValue());
			Employee employee = new Employee();
			employee.setEditMode(true);
			modelAndView.addObject("employee", employee);
		}catch (Exception _exception) {
			logger.error(_exception);
		}
		return modelAndView;
	}
	
	@RequestMapping(value = "/editemployee", method = RequestMethod.POST)
	public ModelAndView editEmployee(ModelAndView  modelAndView,@ModelAttribute Employee employee){
		try{
			modelAndView.setViewName(com.studio.constants.Customer.ADD_EMPLOYEE.getValue());
			if(employee.getEmpId()!= 0){
				employee = userDAO.getEmployeeById(employee.getEmpId());	
			}
			employee.setEditMode(true);
			modelAndView.addObject("employee", employee);
			List<Deportment> deportments = metaDAO.getDeportments();
			modelAndView.addObject("deportments", deportments);
			//employee.setEmpId(maxValueDAO.getMaxEmpId());
			List<Role> roles = metaDAO.getRoles();
			modelAndView.addObject("roles", roles);
		}catch (Exception _exception) {
			logger.error(_exception);
		}
		return modelAndView;
	}
	
	@RequestMapping(value = "/addemployee", method = RequestMethod.POST)
	public ModelAndView addEmployee(ModelAndView  modelAndView,@ModelAttribute Employee employee){
		modelAndView.setViewName(com.studio.constants.Customer.ADD_EMPLOYEE.getValue());
		try{
			if(!StringUtils.isEmpty(employee.getName())){
				
					if(employee.isEditMode()){
						saveOrUpdate(employee);
						setEmployeeMetadata(modelAndView,true);
					}else{
						Boolean isNameExists = userDAO.getEmployeeByName(employee.getName())!=null?true:false;
						Boolean isUserNameExists=userDAO.getEmployee(employee.getUsername())!=null?true:false;
						if(isNameExists || isUserNameExists){
							if(isNameExists)
								modelAndView.addObject("message", "Name already exists, try with another name");
							if(isUserNameExists)
								modelAndView.addObject("message", "User Name already exists, try with another name");
							setEmployeeMetadata(modelAndView,false);
						}else{
							save(employee);
							setEmployeeMetadata(modelAndView,true);
						}
					}
				
			}else{
				modelAndView.addObject("message", "Enter employee name atleast");
				setEmployeeMetadata(modelAndView,false);
			}
			
		}catch (Exception _exception) {
			logger.error(_exception);
		}
		return modelAndView;
	}
	
	@RequestMapping(value = "/supplier", method = RequestMethod.GET)
	public ModelAndView supplier(ModelAndView  modelAndView){
		modelAndView.setViewName(com.studio.constants.Customer.PAGE_SUPPLIER.getValue());
		Supplier supplier = new Supplier();
		supplier.setSupId(maxValueDAO.getMaxSupplierId());
		modelAndView.addObject(com.studio.constants.Customer.SUPPLIER.getValue(),supplier);
		List<State> states = metaDAO.getMetaData(new State());
		modelAndView.addObject(com.studio.constants.Customer.STATES.getValue(),states);
		return modelAndView;
	}
	
	@RequestMapping(value = "/supplier", method = RequestMethod.POST)
	public ModelAndView addSupplier(ModelAndView  modelAndView,@ModelAttribute Supplier supplier){
		modelAndView.setViewName(com.studio.constants.Customer.PAGE_SUPPLIER.getValue());
		if(supplier.isEditMode()){
			saveOrUpdate(supplier);
			supplier = new Supplier();
		}else{
			save(supplier);
			supplier = new Supplier();
			supplier.setSupId(maxValueDAO.getMaxSupplierId());
		}
		modelAndView.addObject(com.studio.constants.Customer.SUPPLIER.getValue(),supplier);
		List<State> states = metaDAO.getMetaData(new State());
		modelAndView.addObject(com.studio.constants.Customer.STATES.getValue(),states);
		return modelAndView;
	}
	
	@RequestMapping(value = "/supplier/edit", method = RequestMethod.POST)
	public ModelAndView eidtSupplier(ModelAndView  modelAndView,@ModelAttribute Supplier supplier){
		modelAndView.setViewName(com.studio.constants.Customer.PAGE_SUPPLIER.getValue());
		supplier = customerDAO.getSupplierById(supplier.getSupId());
		supplier.setEditMode(true);
		modelAndView.addObject(com.studio.constants.Customer.SUPPLIER.getValue(),supplier);
		List<State> states = metaDAO.getMetaData(new State());
		modelAndView.addObject(com.studio.constants.Customer.STATES.getValue(),states);
		return modelAndView;
	}
	
	@RequestMapping(value = "/supplier/edit", method = RequestMethod.GET)
	public ModelAndView eidtSupplier(ModelAndView  modelAndView){
		modelAndView.setViewName(com.studio.constants.Customer.PAGE_SUPPLIER.getValue());
		Supplier supplier = new Supplier();
		//System.out.println("YES its edit mode");
		supplier.setEditMode(true); 
		modelAndView.addObject(com.studio.constants.Customer.SUPPLIER.getValue(),supplier);
		List<State> states = metaDAO.getMetaData(new State());
		modelAndView.addObject(com.studio.constants.Customer.STATES.getValue(),states);
		return modelAndView;
	}
	
	@RequestMapping(value = "/autocomplete/supplier", method = RequestMethod.GET)
	public @ResponseBody  List<Value> searchSupplier(@RequestParam String searchTerm){
		List<Value> values = new ArrayList<Value>();
		logger.info("Search term : " + searchTerm);
		List<Supplier> suppliers = customerDAO.getSuppliers(searchTerm);
		for(Supplier supplier : suppliers){
			Value value = new Value();
			value.setName(supplier.getName());
			value.setId(String.valueOf(supplier.getSupId()));
			values.add(value);
		}
		return values;
	}
	
	@RequestMapping(value = "/autocomplete/employee", method = RequestMethod.GET)
	public @ResponseBody  List<Value> searchEmployee(@RequestParam String searchTerm){
		List<Value> values = new ArrayList<Value>();
		logger.info("Search term : " + searchTerm);
		List<Employee> employees = userDAO.employeeSearch(searchTerm);
		for(Employee supplier : employees){
			Value value = new Value();
			value.setName(supplier.getName());
			value.setId(String.valueOf(supplier.getEmpId()));
			values.add(value);
		}
		return values;
	}
	
	private void setEmployeeMetadata(ModelAndView modelAndView,Boolean success){
		List<Deportment> deportments = metaDAO.getDeportments();
		modelAndView.addObject("deportments", deportments);
		List<Role> roles = metaDAO.getRoles();
		modelAndView.addObject("roles", roles);
		if(success){
			Employee employee = new Employee();
			employee.setEmpId(maxValueDAO.getMaxEmpId());
			modelAndView.addObject("employee", employee);
		}
	}
	
	private void setCustomerMetadata(ModelAndView modelAndView){
		List<CustomerType> customerTypes = metaDAO.getCustomerTypes();
		List<RateType> rateTypes = metaDAO.getRateTypes();
		List<Deportment> deportments = metaDAO.getDeportments();
		List<Area> areas = metaDAO.getAreas();
		List<State> states = metaDAO.getMetaData(new State());
		modelAndView.addObject(com.studio.constants.Customer.DEPORTMENTS.getValue(),deportments);
		modelAndView.addObject(com.studio.constants.Customer.RATE_TYPES.getValue(), rateTypes);
		modelAndView.addObject(com.studio.constants.Customer.CUSTOMER_TYPES.getValue(), customerTypes);
		modelAndView.addObject(com.studio.constants.Customer.AREAS.getValue(), areas);
		modelAndView.addObject(com.studio.constants.Customer.STATES.getValue(), states);
	}
	
}

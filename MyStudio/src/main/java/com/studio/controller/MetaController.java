/**
 * 
 */
package com.studio.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.studio.constants.Meta;
import com.studio.dao.CustomerDAO;
import com.studio.dao.MetaDAO;
import com.studio.domain.Customer;
import com.studio.domain.Employee;
import com.studio.domain.ProductItemType;
import com.studio.domain.Value;

/**
 * @author ezhilraja_k
 *
 */

@RestController
public class MetaController implements Meta{

	
	@Autowired
	private MetaDAO metaDAO;
	
	
	@Autowired 
	private CustomerDAO customerDAO;
	
	Logger logger = Logger.getLogger(MetaController.class);
	
	
	/*@RequestMapping(value="/ordertype-options-page",method = RequestMethod.GET)
	public ModelAndView workorder(){
		ModelAndView modelAndView = new ModelAndView("ordertype-options-form");
		logger.info("With in meta controller::order type");
		List<OrderType> orderTypes = metaDAO.getOrderTypes();
		Map<String,String> orderTypeMap = new HashMap<String, String>();
		for(OrderType orderType:orderTypes){
			orderTypeMap.put(orderType.getId(), orderType.getName());
		}
		modelAndView.addObject(ORDER_TYPES, orderTypeMap);
		return modelAndView;
	}*/
	
	@RequestMapping(value="/autocomplete/agents",method = RequestMethod.GET)
	public @ResponseBody List<Value> autoCompleteAgents(@RequestParam String searchTerm){
		List<Value> values = new ArrayList<Value>();
		logger.info("Search term : " + searchTerm);
		List<Employee> employees = customerDAO.getAgents(searchTerm);
		for(Employee Employee : employees){
			Value value = new Value();
			value.setName(Employee.getName());
			value.setId(String.valueOf(Employee.getEmpId()));
			values.add(value);
		}
		return values;
	}
	
	@RequestMapping(value="/autocomplete/customers",method = RequestMethod.GET)
	public @ResponseBody List<Value> autoCompleteCustomers(@RequestParam String searchTerm){
		List<Value> values = new ArrayList<Value>();
		logger.info("Search term : " + searchTerm);
		List<Customer> customers = customerDAO.getCustomers(searchTerm);
		for(Customer customer : customers){
			Value value = new Value();
			value.setName(customer.getFirstName());
			value.setId(String.valueOf(customer.getCustId()));
			values.add(value);
		}
		return values;
	}
	
	@RequestMapping(value="/autocomplete/suppliers",method = RequestMethod.GET)
	public @ResponseBody List<Value> autoCompleteSuppliers(@RequestParam String searchTerm){
		List<Value> values = new ArrayList<Value>();
		logger.info("Search term : " + searchTerm);
		List<Customer> customers = customerDAO.getCustomers(searchTerm);
		for(Customer customer : customers){
			Value value = new Value();
			value.setName(customer.getFirstName());
			value.setId(String.valueOf(customer.getCustId()));
			values.add(value);
		}
		return values;
	}
	
	@RequestMapping(value="/productitem/{productId}",method = RequestMethod.GET)
	public @ResponseBody List<ProductItemType> getProductItems(@PathVariable String productId){
		List<ProductItemType> productItemTypes = null;
		if(!StringUtils.isEmpty(productId))
			productItemTypes = metaDAO.getProductItemTypes(Integer.valueOf(productId));
		else
			productItemTypes = new ArrayList<ProductItemType>();
		return productItemTypes;
	}
	
}

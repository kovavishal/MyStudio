package com.studio.dao;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.studio.domain.ControlTable;
import com.studio.domain.Customer;
import com.studio.domain.Employee;


@Repository
@Transactional
@EnableTransactionManagement
public class UserDAO {
	  @PersistenceContext
	  EntityManager entityManager;
	  Logger logger = Logger.getLogger(UserDAO.class);
  
  
	  public  <T> Boolean save(T t){
		  entityManager.merge(t);
		return true;
	  }
  
	  public Customer getCustomerById(Long customerId){
		  return entityManager.find(Customer.class, customerId);
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
	  
// get last login date
	  public ControlTable getLastLoginDate(){
		  ControlTable record=null;
		  TypedQuery<ControlTable> query = entityManager.createQuery(
			        "SELECT c FROM ControlTable c ",ControlTable.class);
		  		
		  		try{
		  			 record = query.getSingleResult();
		  		}catch (Exception _exception) {
		  			logger.error(_exception);
				}
		  		
			    return record;
	  }
  
	  public Employee getEmployee(String userName){
		  TypedQuery<Employee> query = entityManager.createQuery(
			        "SELECT e FROM Employee e WHERE e.username = :name", Employee.class);
		  	Employee employee = null;
		  		try{
		  			employee = query.setParameter("name", userName).getSingleResult();
		  		}catch (Exception _exception) {
		  			logger.error(_exception);
				}
			    return employee;
	  }
	  
	  public Employee getEmployeeByName(String name){
		  TypedQuery<Employee> query = entityManager.createQuery(
			        "SELECT e FROM Employee e WHERE e.name = :name", Employee.class);
		  	Employee employee = null;
		  		try{
		  			employee = query.setParameter("name", name).getSingleResult();
		  		}catch (Exception _exception) {
		  			logger.error(_exception);
				}
			    return employee;
	  }
	  
	  public List<Employee> employeeSearch(String searchTerm){
		  TypedQuery<Employee> query = entityManager.createQuery(
			        "SELECT e FROM Employee e WHERE e.name LIKE :name", Employee.class);
		  	List<Employee> employee = null;
		  		try{
		  			query.setParameter("name", "%"+searchTerm+"%");
		  			employee = query.getResultList();
		  		}catch (Exception _exception) {
		  			logger.error(_exception);
				}
			    return employee;
	  }
  
	@SuppressWarnings("unchecked")
	public List<Employee> getEmployees(){
		  Query query = entityManager.createQuery("SELECT e FROM Employee e");
		  	List<Employee> employees = null;
		  		try{
		  			employees = query.getResultList();
		  		}catch (Exception _exception) {
		  			logger.error(_exception);
				}
			    return employees;
	  }
  
	  public Employee getEmployeeById(Long empId){
		  return entityManager.find(Employee.class, empId);
	  }
	  
	  
	 
	public List<Employee> getEmployeeByDepotId(Long depotId){
		  List<Employee> emp=null;
		  Query query= entityManager.createNativeQuery("select * from employee where depot_id=:depotId",Employee.class);
		  if(query !=null){
			   query.setParameter("depotId",depotId);
			   emp=query.getResultList();
		  }
		  return emp;
	  }
	  public int dateCompare(String d1, String d2){
		   // String yyyy = st.substring(0,4);
		    String mm1 = d1.substring(5,7);
		    String dd1 = d1.substring(8,10);
		    String mm2 = d2.substring(5,7);
		    String dd2 = d2.substring(8,10);
		    int m1,m2,date1,date2;
		    m1=Integer.parseInt(mm1);
		    m2=Integer.parseInt(mm2);
		    date1=Integer.parseInt(dd1);
		    date2=Integer.parseInt(dd2);
		   // System.out.println("d1 :"+date1+": d2 :"+date2);
		    //System.out.println("m1 :"+m1+": m2 :"+m2);
		    if (date1 > date2 )
		    	return 1;
		     else if(m1>m2)
		    	return 1;
		     else
		    	 return 0;
	}	   
		    	
  
}

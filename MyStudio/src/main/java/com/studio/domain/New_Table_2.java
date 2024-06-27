//package com.studio.domain;
//
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.Id;
//import javax.persistence.OneToMany;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//import javax.persistence.Transient;
//
//import org.springframework.util.StringUtils;
//
//import com.studio.utils.DateUtils;
//
//
//@Entity
//@Table(name = "new_table_2", catalog = "studio")
//
//public class New_Table_2 implements java.io.Serializable{
//	
//	private int id;
//	private int emp_id;
//	private String address;
//	private String city;
//	
//	@Id
//	@Column(name = "id", unique = true, nullable = false)
//	public int getId() {
//		return id;
//	}
//	public void setId(int id) {
//		this.id = id;
//	}
//	@Column(name = "emp_id", unique = true, nullable = false)
//	public int getEmp_id() {
//		return emp_id;
//	}
//	public void setEmp_id(int emp_id) {
//		this.emp_id = emp_id;
//	}
//	@Column(name = "address")
//	public String getAddress() {
//		return address;
//	}
//	public void setAddress(String address) {
//		this.address = address;
//	}
//	@Column(name = "city")
//	public String getCity() {
//		return city;
//	}
//	public void setCity(String city) {
//		this.city = city;
//	}
//	public New_Table_2() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
//	public New_Table_2(int id, int emp_id, String address, String city) {
//		super();
//		this.id = id;
//		this.emp_id = emp_id;
//		this.address = address;
//		this.city = city;
//	}
//	@Override
//	public String toString() {
//		return "New_Table_2 [id=" + id + ", emp_id=" + emp_id + ", address=" + address + ", city=" + city + "]";
//	}
//	
//
//}

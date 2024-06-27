//package com.studio.domain;
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
//@Table(name = "new_table_3", catalog = "studio")
//
//public class New_Table_3 implements java.io.Serializable{
//	@Column(name = "id", unique = true, nullable = false)
//	private int id;
//	@Column(name = "state", unique = true, nullable = false)
//	private String state;
//	@Column(name = "country", unique = true, nullable = false)
//	private String country;
//	
//	
//	@Id
//
//	public int getId() {
//		return id;
//	}
//	public void setId(int id) {
//		this.id = id;
//	}
//
//	public int getid() {
//		return id;
//	}
//	public void setd(int id) {
//		this.id = id;
//	}
//	
//	public String getstate() {
//		return state;
//	}
//	public void setstae(String state) {
//		this.state = state;
//	}
//	
//	public String getcountry() {
//		return country;
//	}
//	public void setcountry(String country) {
//		this.country = country;
//	}
//	public New_Table_3() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
//	public New_Table_3(int id, String state, String country) {
//		super();
//		this.id = id;
//		this.state = state;
//		this.country = country;
//		
//	}
//	@Override
//	public String toString() {
//		return "New_Table_2 [id=" + id + ", state=" + state + ", country=" + country + "]";
//	}
//	
//
//}

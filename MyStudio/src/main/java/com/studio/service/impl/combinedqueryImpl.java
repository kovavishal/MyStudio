package com.studio.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import com.studio.domain.ReportDetail;
import com.studio.service.combinedquery;

@Service
public class combinedqueryImpl implements combinedquery {
	  @PersistenceContext
	    private EntityManager entityManager;

	    public List<ReportDetail> orderDetails() {
	    	 List<ReportDetail> rd = new ArrayList<ReportDetail>();
	        //use JPA query to select columns from different tables
	    /*    Query query1 = entityManager.createNativeQuery("select o.order_id ,o.cust_id from orders o, order_trxn_detail tr "
	        		+ "where o.created_date='2019-07-02' and o.order_id=tr.order_id",ReportDetail.class); */
	        Query query1 = entityManager.createNativeQuery("select orderId , custId from orders o"
	        		+ "where o.created_date='2019-07-02'",ReportDetail.class); 
	        if(query1 != null){
				  rd = query1.getResultList();
				 // System.out.println("orders size :"+rd.size());
			    }
	        return rd;   
	    }

		public void deleteAllInBatch() {
			// TODO Auto-generated method stub
			
		}

		public void deleteInBatch(Iterable arg0) {
			// TODO Auto-generated method stub
			
		}

		public List findAll() {
			// TODO Auto-generated method stub
			return null;
		}

		public List findAll(Sort arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public List findAll(Example arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public List findAll(Example arg0, Sort arg1) {
			// TODO Auto-generated method stub
			return null;
		}

		public List findAllById(Iterable arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public void flush() {
			// TODO Auto-generated method stub
			
		}

		public Object getOne(Object arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public List saveAll(Iterable arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public Object saveAndFlush(Object arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public Page findAll(Pageable arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public long count() {
			// TODO Auto-generated method stub
			return 0;
		}

		public void delete(Object arg0) {
			// TODO Auto-generated method stub
			
		}

		public void deleteAll() {
			// TODO Auto-generated method stub
			
		}

		public void deleteAll(Iterable arg0) {
			// TODO Auto-generated method stub
			
		}

		public void deleteById(Object arg0) {
			// TODO Auto-generated method stub
			
		}

		public boolean existsById(Object arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		public Optional findById(Object arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public Object save(Object arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public long count(Example arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		public boolean exists(Example arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		public Page findAll(Example arg0, Pageable arg1) {
			// TODO Auto-generated method stub
			return null;
		}

		public Optional findOne(Example arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public List findAll(Iterable arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public Object getOne(Serializable arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public List save(Iterable arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public void delete(Serializable arg0) {
			// TODO Auto-generated method stub
			
		}

		public void delete(Iterable arg0) {
			// TODO Auto-generated method stub
			
		}

		public boolean exists(Serializable arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		public Object findOne(Serializable arg0) {
			// TODO Auto-generated method stub
			return null;
		}

}

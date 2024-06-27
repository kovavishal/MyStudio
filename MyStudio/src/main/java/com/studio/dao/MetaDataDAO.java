package com.studio.dao;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
@EnableTransactionManagement
public class MetaDataDAO {
  @PersistenceContext
  EntityManager entityManager;
  
  Logger logger = Logger.getLogger(MetaDataDAO.class);
  
  
  public <T> Boolean save(T t){
	  try{
		  entityManager.persist(t);
	  }catch (Exception _exception) {
		  logger.error(_exception);
	  }
	return true;
  }
  
  public <T> Boolean saveOrUpdate(T t){
	  try{
		  entityManager.merge(t);
	  }catch (Exception _exception) {
		  logger.error("saveOrUpdate :" + _exception);
	  }
	return true;
  }
  
  @SuppressWarnings("unchecked")
  public <T> Boolean delete(T t,Integer id){
	  try{
		  	if(entityManager.find(t.getClass(),id)!=null)
		  		t =(T) entityManager.find(t.getClass(),id);
		  	entityManager.remove(t);
		  
	  }catch (Exception _exception) {
		  logger.error(_exception);
	  }
	return true;
  }
  
  public <T> Integer deleteProductType(Integer productTypeId){
	  Integer status=0;
	  try{
		  Query query = entityManager.createQuery("delete FROM ProductType p where p.id=:productTypeId");
		  query.setParameter("productTypeId", productTypeId);
		  status = query.executeUpdate();
		  
	  }catch (Exception _exception) {
		  logger.error(_exception);
	  }
	  return status;
  }
  public <T> Integer deleteProductItemType(Integer itemTypeId){
	  Integer status=0;
	  try{
		  Query query = entityManager.createQuery("delete FROM ProductItemType p where p.id=:itemTypeId");
		  query.setParameter("itemTypeId", itemTypeId);
		  status = query.executeUpdate();
		  
	  }catch (Exception _exception) {
		  logger.error(_exception);
	  }
	  return status;
  }
  
  public <T> Integer deleteProductSize(Integer sizeId){
	  Integer status=0;
	  try{
		  Query query = entityManager.createQuery("delete FROM ProductSize p where p.id=:sizeId");
		  query.setParameter("sizeId", sizeId);
		  status = query.executeUpdate();
	  }catch (Exception _exception) {
		  logger.error(_exception);
	  }
	  return status;
  }
  
  public <T> Integer deleteDepartment(Integer depotId){
	  Integer status=0;
	  try{
		  Query query = entityManager.createQuery("delete FROM Deportment d where d.id=:depotId");
		  query.setParameter("depotId", depotId);
		  status = query.executeUpdate();
	  }catch (Exception _exception) {
		  logger.error(_exception);
	  }
	  return status;
  }
  
  public <T> Integer deleteSize(Integer sizeId){
	  Integer status=0;
	  try{
		  Query query = entityManager.createQuery("delete FROM ProductSize p where p.id=:sizeId");
		  query.setParameter("sizeId", sizeId);
		  status = query.executeUpdate();
	  }catch (Exception _exception) {
		  logger.error(_exception);
	  }
	  return status;
  }
  
  public <T> Integer deleteSizeRate(Integer sizeRateId){
	  Integer status=0;
	  try{
		  Query query = entityManager.createQuery("delete FROM ItemSizeRate i where i.id=:sizeRateId");
		  query.setParameter("sizeRateId", sizeRateId);
		  status = query.executeUpdate();
	  }catch (Exception _exception) {
		  logger.error(_exception);
	  }
	  return status;
  }
  
}

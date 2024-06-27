/**
 * 
 */
package com.studio.controller;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.studio.constants.Meta;
import com.studio.dao.MetaDAO;
import com.studio.dao.MetaDataDAO;
import com.studio.domain.Area;
import com.studio.domain.CustomerType;
import com.studio.domain.DeliveryType;
import com.studio.domain.Deportment;
import com.studio.domain.ExpenseType;
import com.studio.domain.ItemSizeRate;
import com.studio.domain.JobStatus;
import com.studio.domain.MetaDataMapping;
import com.studio.domain.OrderType;
import com.studio.domain.PaymentType;
import com.studio.domain.ProductItemType;
import com.studio.domain.ProductSize;
import com.studio.domain.ProductType;
import com.studio.domain.PurchaseItem;
import com.studio.domain.RateType;
import com.studio.domain.ReceiptType;
import com.studio.domain.Role;
import com.studio.domain.State;
import com.studio.domain.VoucherType;
import com.studio.utils.Utils;

/**
 * @author ezhilraja_k
 *
 */

@RestController
@RequestMapping(value="/metadata")
public class MetaDataController implements Meta{


	@Autowired
	private MetaDAO metaDAO;
	
	
	@Autowired
	private MetaDataDAO metaDataDAO;
	
	Logger logger = Logger.getLogger(MetaDataController.class);
	
	@RequestMapping(value="/producttypes",method = RequestMethod.POST)
	@ResponseBody
	public List<ProductType> getProductTypes(){
		List<ProductType> types = metaDAO.getProductTypes();
		return types;
	}
	
	@RequestMapping(value="/departmenttypes",method = RequestMethod.POST)
	@ResponseBody
	public List<Deportment> getDeportmentTypes(){
		return metaDAO.getDeportments();
	}
	
	@RequestMapping(value="/addproduct",method = RequestMethod.POST)
	public ModelAndView addProductType(@ModelAttribute ProductType productType){
		ModelAndView modelAndView = new ModelAndView("admin/producttype");
		List<ProductType> productTypes = metaDAO.getProductTypes(productType.getName());
		try{
		if(productTypes.isEmpty())
			metaDataDAO.save(productType);
		else
			modelAndView.addObject("message", "Product already exists");
		}catch (Exception _exception) {
			logger.error(_exception);
		}
		ProductType product = new ProductType();
		modelAndView.addObject("productType", product);
		return modelAndView;
	}
	
	@RequestMapping(value="/products",method = RequestMethod.GET)
	public ModelAndView productType(){
		ModelAndView modelAndView = new ModelAndView("admin/producttype");
		ProductType productType = new ProductType();
		modelAndView.addObject("productType", productType);
		return modelAndView;
	}
	
	@RequestMapping(value="/delete/{productTypeId}/{name}",method = RequestMethod.PUT)
	@ResponseBody
	public Boolean deleteProductType(@PathVariable Integer productTypeId,@PathVariable String name){
		Boolean isDeleted = false;
		try{
			ProductType productType = new ProductType();
			productType.setId(productTypeId);
			productType.setName(name);
			metaDataDAO.saveOrUpdate(productType);
			isDeleted=true;
		}catch (Exception _exception) {
			logger.error(_exception);
		}
		return isDeleted;
	}
	
	
	@RequestMapping(value="/itemtypes",method = RequestMethod.POST)
	@ResponseBody
	public List<ProductItemType> getProductItemTypes(){
		List<ProductItemType> types = metaDAO.  getProductItemTypes();
		List<ProductType> productTypes = metaDAO.getProductTypes();
		Map<Long,String> productMap = Utils.getTypes(productTypes);
		for(ProductItemType itemType : types){
			itemType.setProductName(productMap.get(Long.valueOf(itemType.getProductId())));
		}
		return types;
	}
	
	@RequestMapping(value="/jobstatustypes",method = RequestMethod.POST)
	@ResponseBody
	public List<JobStatus> getJobStatusTypes(){
		//List<ProductItemType> types = metaDAO.  getProductItemTypes();
		List<JobStatus> jobStatusTypes = metaDAO.getJobStatus();
		Map<Long,String> JobStatusMap = Utils.getTypes(jobStatusTypes);
		for(JobStatus itemType : jobStatusTypes){
			itemType.setName(JobStatusMap.get(Long.valueOf(itemType.getId())));
		}
		return jobStatusTypes;
	}
	
	@RequestMapping(value="/itemtypes",method = RequestMethod.GET)
	public ModelAndView productItemType(){
		ModelAndView modelAndView = new ModelAndView("admin/itemtype");
		List<ProductType> products = metaDAO.getProductTypes();
		modelAndView.addObject("products", products);
		ProductItemType itemType = new ProductItemType();
		modelAndView.addObject("productItemType", itemType);
		return modelAndView;
	}
	
	@RequestMapping(value="/additemtype",method = RequestMethod.POST)
	public <T> ModelAndView addProductItemType(@ModelAttribute ProductItemType productItemType){
		ModelAndView modelAndView = new ModelAndView("admin/itemtype");
		modelAndView.addObject("productItemType", productItemType);
		
		try{
			List<ProductType> products = metaDAO.getProductTypes();
			modelAndView.addObject("products", products);
			if(productItemType.getProductId()!=0 ){
				List<ProductItemType> productItemTypes =  metaDAO.getProductItemTypes(productItemType.getName(),productItemType.getProductId());
				if(productItemTypes.isEmpty())
					metaDataDAO.save(productItemType);
				else
					modelAndView.addObject("message", "Product Item already exists");
			}else
				modelAndView.addObject("message", "Select Product");
		}catch (Exception _exception) {
			logger.error(_exception);
		}
		return modelAndView;
	}
	
	@RequestMapping(value="/deleteitem/{itemTypeId}/{prodId}/{name}",method = RequestMethod.PUT)
	@ResponseBody
	public Boolean deleteProductItemType(@PathVariable Integer itemTypeId,@PathVariable Integer prodId,@PathVariable String name){
		Boolean isDeleted = false;
		try{
			
			ProductItemType productType = new ProductItemType();
			productType.setId(itemTypeId);
			productType.setProductId(prodId);
			productType.setName(name);
			isDeleted = metaDataDAO.saveOrUpdate(productType);
		}catch (Exception _exception) {
			logger.error("deleteProductItemType : " + _exception);
		}
		return isDeleted;
	}
	
	@RequestMapping(value="/ratetypes",method = RequestMethod.POST)
	@ResponseBody
	public List<RateType> getRateTypes(){
		List<RateType> types = metaDAO.getRateTypes();
		return types;
	}
	
	@RequestMapping(value="/ratetypes",method = RequestMethod.GET)
	public ModelAndView getRate(){
		ModelAndView modelAndView = new ModelAndView("admin/ratetype");
		RateType rateType = new RateType();
		modelAndView.addObject("rateType", rateType);
		return modelAndView;
	}
	
	@RequestMapping(value="/addrate",method = RequestMethod.POST)
	public ModelAndView addRateType(@ModelAttribute RateType rateType){
		ModelAndView modelAndView = new ModelAndView("admin/ratetype");
		modelAndView.addObject("rateType", rateType);
		List<RateType> rateTypes = metaDAO.getRateTypes(rateType.getName());
		try{
			if(rateTypes.isEmpty())
				metaDataDAO.save(rateType);
			else
				modelAndView.addObject("message", "Rate already exists");
		}catch (Exception _exception) {
			logger.error(_exception);
		}
		return modelAndView;
	}
	
	@RequestMapping(value="/productsizes",method = RequestMethod.GET)
	public ModelAndView getProductSize(){
		ModelAndView modelAndView = new ModelAndView("admin/productsize");
		ProductSize productSize = new ProductSize();
		modelAndView.addObject("productSize", productSize);
		List<ProductType> products = metaDAO.getProductTypes();
		modelAndView.addObject("products", products);
		return modelAndView;
	}
	
	@RequestMapping(value="/productsizes",method = RequestMethod.POST)
	@ResponseBody
	public List<ProductSize> getProductSizes(){
		List<ProductSize> productSizes = metaDAO.getProductSizes();
		List<ProductType> products = metaDAO.getProductTypes();
		Map<Long,String> productMap = Utils.getTypes(products);
		for(ProductSize size:productSizes){
			size.setProductName(productMap.get(Long.valueOf(size.getProductId())));
		}
		return productSizes;
	}
	
	@RequestMapping(value="/addsize",method = RequestMethod.POST)
	public ModelAndView addProductSize(@ModelAttribute ProductSize productSize){
		ModelAndView modelAndView = new ModelAndView("admin/productsize");
		modelAndView.addObject("productSize", productSize);

		try{
			
			List<ProductSize> productSizes = metaDAO.getProductSizes(productSize.getSize(),productSize.getProductId());
			List<ProductType> products = metaDAO.getProductTypes();
			modelAndView.addObject("products", products);
			
			if(productSizes.isEmpty())
				metaDataDAO.save(productSize);
			else
				modelAndView.addObject("message", "Product Size already exists");
			
		}catch (Exception _exception) {
			logger.error(_exception);
		}
		return modelAndView;
	}
	
	@RequestMapping(value="/deletesize/{sizeId}/{prodId}/{name}",method = RequestMethod.PUT)
	@ResponseBody
	public Boolean deleteProductSize(@PathVariable Integer sizeId,@PathVariable Integer prodId,@PathVariable String name){
		Boolean isUpdated=false;
		try{
			ProductSize productSize = new ProductSize();
			productSize.setId(sizeId);
			productSize.setProductId(prodId);
			productSize.setSize(name);
			isUpdated = metaDataDAO.saveOrUpdate(productSize);
		}catch (Exception _exception) {
			logger.error(_exception);
		}
		return isUpdated;
	}
	
	@RequestMapping(value="/updaterate/{id}/{name}",method = RequestMethod.PUT)
	@ResponseBody
	public Boolean updateRate(@PathVariable Integer id,@PathVariable String name){
		Boolean isUpdated=false;
		try{
			RateType rateType = new RateType();
			rateType.setId(id);
			rateType.setName(name);
			isUpdated = metaDataDAO.saveOrUpdate(rateType);
		}catch (Exception _exception) {
			logger.error(_exception);
		}
		return isUpdated;
	}
	
	@RequestMapping(value="/deletedepartment/{depotId}",method = RequestMethod.DELETE)
	@ResponseBody
	public Boolean deleteDepartment(@PathVariable Integer depotId){
		Boolean isDeleted=false;
		try{
			metaDataDAO.deleteDepartment(depotId);
			isDeleted=true;
		}catch (Exception _exception) {
			logger.error("deleteDepartment ::" + _exception);
		}
		return isDeleted;
	}
	
	
	@RequestMapping(value="/itemsizerate",method = RequestMethod.GET)
	public ModelAndView getItemSizeRate(@ModelAttribute ItemSizeRate itemSizeRate){
		ModelAndView modelAndView = new ModelAndView("admin/itemsizerate");
		try{
			List<ProductItemType> productItemTypes = metaDAO.getProductItemTypes();
			List<ProductSize> productSizes = metaDAO.getProductSizes();
			List<RateType> rateTypes = metaDAO.getRateTypes();
			List<ProductType> productTypes = metaDAO.getProductTypes();
			modelAndView.addObject("productItemTypes", productItemTypes);
			modelAndView.addObject("productSizes", productSizes);
			modelAndView.addObject("rateTypes", rateTypes);
			modelAndView.addObject("productTypes", productTypes);
		}catch (Exception _exception) {
			logger.error(_exception);
		}
		return modelAndView;
	}
	
	@RequestMapping(value="/addepartment",method = RequestMethod.GET)
	public ModelAndView getDepartments(){
		ModelAndView modelAndView = new ModelAndView("admin/departmenttype");
		Deportment deportment = new Deportment();
		modelAndView.addObject("deportment", deportment);
		return modelAndView;
	}
	
	@RequestMapping(value="/adddepartment",method = RequestMethod.POST)
	public ModelAndView addDepartment(@ModelAttribute Deportment deportment){
		ModelAndView modelAndView = new ModelAndView("admin/departmenttype");
		
		if(!StringUtils.isEmpty(deportment.getName())){
			List<Deportment> deportments = metaDAO.getDeportmentsByName(deportment.getName());
			if(deportments == null || deportments.isEmpty()){
				metaDataDAO.save(deportment);
				deportment = new Deportment();
			}else{
				modelAndView.addObject("message", "Department already exists");
			}
		}else{
			modelAndView.addObject("message", "Enter department name");
		}
		modelAndView.addObject("deportment", deportment);
		return modelAndView;
	}
	
	@RequestMapping(value="/additemsizerate",method = RequestMethod.POST)
	public ModelAndView addItemSizeRate(@ModelAttribute ItemSizeRate itemSizeRate){
		ModelAndView modelAndView = new ModelAndView("admin/itemsizerate");
		try{
			List<ProductItemType> productItemTypes = metaDAO.getProductItemTypes(itemSizeRate.getProductId());
			List<ProductSize> productSizes = metaDAO.getProductSizes(itemSizeRate.getProductId());
			List<RateType> rateTypes = metaDAO.getRateTypes();
			List<ProductType> productTypes = metaDAO.getProductTypes();
			modelAndView.addObject("productItemTypes", productItemTypes);
			modelAndView.addObject("productSizes", productSizes);
			modelAndView.addObject("rateTypes", rateTypes);
			modelAndView.addObject("productTypes", productTypes);
			
			if(itemSizeRate.getItemId() ==0)
				modelAndView.addObject("message", "Select Product Item");
			if(itemSizeRate.getSizeId() ==0)
				modelAndView.addObject("message", "Select Product Size");
			if(itemSizeRate.getRateId() ==0)
				modelAndView.addObject("message", "Select Item Size Rate");
			
			if(itemSizeRate.getItemId() !=0 && itemSizeRate.getSizeId()!= 0 && itemSizeRate.getRateId()!= 0){
				
				if(productItemTypes!=null && !productItemTypes.isEmpty()){
					for(ProductItemType itemType :productItemTypes){
						if(itemType.getId() == itemSizeRate.getItemId())
							itemSizeRate.setItemName(itemType.getName());
					}
				}
				if(productSizes!=null && !productSizes.isEmpty()){
					for (ProductSize size : productSizes) {
						if(size.getId() == itemSizeRate.getSizeId())
							itemSizeRate.setSizeName(size.getSize());
					}
				}
				if(rateTypes!=null && !rateTypes.isEmpty()){
					for(RateType rateType : rateTypes){
						if(rateType.getId() == itemSizeRate.getRateId())
							itemSizeRate.setRateName(rateType.getName());
					}
				}
				
				metaDataDAO.save(itemSizeRate);
			}
			
		}catch (Exception _exception) {
			logger.error(_exception);
		}
		return modelAndView;
	}
	
	@RequestMapping(value="/itemsizerates",method = RequestMethod.POST)
	@ResponseBody
	public List<ItemSizeRate> getItemSizeRates(){
		List<ItemSizeRate> itemSizeRates = null;
		try{
			//List<ProductItemType> productItemTypes = metaDAO.getProductItemTypes();
			//List<ProductSize> productSizes = metaDAO.getProductSizes();
			//List<RateType> rateTypes = metaDAO.getRateTypes();
			itemSizeRates = metaDAO.getItemSizeRates();
			
			//Map<Long,String> productItemMap = Utils.getTypes(productItemTypes);
			//Map<Long,String> productSizeMap = Utils.getTypes(productSizes);
			//Map<Long,String> rateTypeMap = Utils.getTypes(rateTypes);
			/*for(ItemSizeRate itemSizeRate :  itemSizeRates){
				itemSizeRate.setItemName(productItemMap.get(Long.valueOf(itemSizeRate.getItemId())));
				itemSizeRate.setSizeName(productSizeMap.get(Long.valueOf(itemSizeRate.getSizeId())));
				itemSizeRate.setRateName(rateTypeMap.get(Long.valueOf(itemSizeRate.getRateId())));
			}*/
		}catch (Exception _exception) {
			logger.error(_exception);
		}
		return itemSizeRates;
	}
	
	@RequestMapping(value="/deletesizerate/{sizeRateId}",method = RequestMethod.DELETE)
	@ResponseBody
	public Boolean deleteSizeRate(@PathVariable Integer sizeRateId){
		Boolean isDeleted=false;
		try{
			metaDataDAO.deleteSizeRate(sizeRateId);
			isDeleted=true;
		}catch (Exception _exception) {
			logger.error(_exception);
		}
		return isDeleted;
	}
	
	@RequestMapping(value="/add", method=RequestMethod.GET)
	public ModelAndView dataMapping(ModelAndView modelAndView){
		MetaDataMapping metadata = new MetaDataMapping();
		List<MetaDataMapping> mappings = metaDAO.getMetaData(new MetaDataMapping());
		modelAndView.addObject("mappings", mappings);
		modelAndView.setViewName("admin/metadata");
		modelAndView.addObject("metadata", metadata);
		return modelAndView;
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public ModelAndView getMetaData(ModelAndView modelAndView,@ModelAttribute MetaDataMapping metadata){
		try{
		List<MetaDataMapping> mappings = metaDAO.getMetaData(new MetaDataMapping());
		modelAndView.setViewName("admin/metadata");
		modelAndView.addObject("showtable", true);
		modelAndView.addObject("mappings", mappings);
		modelAndView.addObject("metadata", metadata);
		}catch (Exception _exception) {
			logger.error("addMetaData ::" + _exception);
		}
		return modelAndView;
	}
	
	
	@RequestMapping(value="/adddata", method=RequestMethod.POST)
	public ModelAndView addMetaData(ModelAndView modelAndView,@ModelAttribute MetaDataMapping metadata){
		try{
		List<MetaDataMapping> mappings = metaDAO.getMetaData(new MetaDataMapping());
		Object metaObject = getMetaClass(metadata.getMethodName());
		Method method = metaObject.getClass().getMethod("setName",String.class);
		method.invoke(metaObject,metadata.getName());
		metaDataDAO.save(metaObject);
		metadata.setName("");
		modelAndView.setViewName("admin/metadata");
		modelAndView.addObject("showtable", true);
		modelAndView.addObject("mappings", mappings);
		modelAndView.addObject("metadata", metadata);
		}catch (Exception _exception) {
			logger.error("addMetaData ::" + _exception);
		}
		return modelAndView;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/mapping/{name}",method = RequestMethod.POST)
	@ResponseBody
	public <T> List<T> methodData(@PathVariable String name){
		List<T> types = (List<T>) metaDAO.getMetaData(getMetaClass(name));
		return types;
	}
	
	@RequestMapping(value="/delete/{name}/{id}",method = RequestMethod.DELETE)
	@ResponseBody
	public Boolean deleteMetaData(@PathVariable String name, @PathVariable Integer id){
		Boolean isDeleted = false;
		try{
			
			Object metaObject = getMetaClass(name);
			Method method = metaObject.getClass().getMethod("setId",Integer.class);
			method.invoke(metaObject,id);
			isDeleted = metaDataDAO.delete(metaObject,id);
		}catch (Exception _exception) {
			logger.error(_exception);
		}
		return isDeleted;
	}
	
	@RequestMapping(value="/update/{name}/{id}/{metaName}",method = RequestMethod.PUT)
	@ResponseBody
	public Boolean updateMetaData(@PathVariable String name, @PathVariable Integer id,@PathVariable String metaName){
		Boolean isUpdated = false;
		try{
			
			Object metaObject = getMetaClass(name);
			Method method = metaObject.getClass().getMethod("setId",Integer.class);
			method.invoke(metaObject,id);
			method = metaObject.getClass().getMethod("setName",String.class);
			method.invoke(metaObject,metaName);
			isUpdated = metaDataDAO.saveOrUpdate(metaObject);
		}catch (Exception _exception) {
			logger.error(_exception);
		}
		return isUpdated;
	}
	
	private Object getMetaClass(String name){
		
		if(!StringUtils.isEmpty(name)){
			if(name.equals("PaymentType"))
				return new PaymentType();
			else if(name.equals("ExpenseType"))
				return new ExpenseType();
			else if(name.equals("State"))
				return new State();
			else if(name.equals("CustomerType"))
				return new CustomerType();
			else if(name.equals("DeliveryType"))
				return new DeliveryType();
			else if(name.equals("Deportment"))
				return new Deportment();
			else if(name.equals("OrderType"))
				return new OrderType();
			else if(name.equals("ReceiptType"))
				return new ReceiptType();
			else if(name.equals("VoucherType"))
				return new VoucherType();
			else if(name.equals("PurchaseItem"))
				return new PurchaseItem();
			else if(name.equals("Deportment"))
				return new Deportment();
			else if(name.equals("Role"))
				return new Role();
			else if(name.equals("Area"))
				return new Area();
		}
		return new MetaDataMapping();
	}
	
	
}

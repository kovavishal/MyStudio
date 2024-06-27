/**
 * 
 */
package com.studio.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.studio.constants.Payment;
import com.studio.constants.WorkOrderConstants;
import com.studio.dao.CustomerDAO;
import com.studio.dao.MaxValueDAO;
import com.studio.dao.MetaDAO;
import com.studio.dao.OrderDAO;
import com.studio.dao.ReportDAO;
import com.studio.dao.UserDAO;
import com.studio.dao.PaymentDAO;
import com.studio.domain.Address;
import com.studio.domain.Customer;
import com.studio.domain.ExpenseType;
import com.studio.domain.Order;
import com.studio.domain.OrderTrxnDetail;
import com.studio.domain.PaymentType;
import com.studio.domain.Purchase;
import com.studio.domain.PurchaseItem;
import com.studio.domain.PurchasePayment;
import com.studio.domain.Receipt;
import com.studio.domain.ReceiptPayment;
import com.studio.domain.ReceiptType;
import com.studio.domain.Supplier;
import com.studio.domain.Value;
import com.studio.domain.Voucher;
import com.studio.domain.VoucherPayment;
import com.studio.domain.VoucherType;
import com.studio.utils.AmountUtils;
import com.studio.utils.DateUtils;
import com.studio.utils.Utils;



import com.studio.controller.PrintController;

/**
 * @author ezhilraja_k
 *
 */

@Controller
@RequestMapping(value="/payment")
public class PaymentController implements WorkOrderConstants{
	
	
	

	@Autowired
	private PaymentDAO paymentDAO;

	@Autowired
	private MetaDAO metaDAO;
	
	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private MaxValueDAO maxValueDAO;

	@Autowired
	private OrderDAO orderDAO;
	
	@Autowired
	private ReportDAO reportDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private PrintController cookie;
	
	@Autowired
	private Environment env;
	
	Logger logger = Logger.getLogger(PaymentController.class);

	@RequestMapping(value = "/pay", method = RequestMethod.GET)
	public ModelAndView addPayment(ModelAndView  modelAndView, HttpServletRequest HttpServletRequest){
		
		modelAndView.setViewName(Payment.PAYMENT_PAGE.value());
			String uname = cookie.getRoleCookie(HttpServletRequest, "role");
		//	System.out.println("dept name   :"+uname);
			if (uname.equals("Purchase")){
				setSupplierMetaData(modelAndView);
			}else{
				setPaymentMetaData(modelAndView);
			}
	 	 Voucher voucher = new Voucher();
		 voucher.setVoucherId(maxValueDAO.getMaxVoucherId());
		 java.util.Date date=new java.util.Date(); 
		 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
		 voucher.setStrVoucherDate(formatter.format(date));
// for Systemdate verification starts		
	/*	 ControlTable  lastlogin=userDAO.getLastLoginDate();
		 Date ctdate=lastlogin.getLastLoginDate();
		 String strctdate=ctdate.toString();
		 strctdate=reportDAO.getyyyymmddtoddmmyyyy(strctdate);
		 if(strctdate.compareTo(voucher.getStrVoucherDate().substring(0, 10))>0 ){
			 modelAndView = new ModelAndView("redirect:/login");
			// modelAndView.setViewName(PAGE_LOGIN);
			 modelAndView.addObject("message","System Date has been changed,Contact Admin... ");
			 return modelAndView;
		 }*/
// for Systemdate averification Ends
		voucher.getVoucherPayments().add(new VoucherPayment());
		modelAndView.addObject("voucher", voucher);
		return modelAndView;
	}
	
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView savePayment(HttpServletRequest request,ModelAndView  modelAndView,@ModelAttribute Voucher voucher){
		modelAndView.setViewName(Payment.PAYMENT_PAGE.value());
		setPaymentMetaData(modelAndView);
		voucher.setCreatedDate(DateUtils.toDate(DateUtils.toDate(new Date(),true),true));
		String uname=null;
		uname = cookie.getCookie(request,"empid");
		voucher.setPreparedBy(uname);
		save(voucher);
		if(voucher.getExpenseId()!=0 &&!String.valueOf(voucher.getExpenseId()).startsWith("100") && !voucher.getVoucherPayments().isEmpty()){
			for(VoucherPayment voucherPayment : voucher.getVoucherPayments()){
				if(!"0".equals(voucherPayment.getRefTypeId())){
					voucherPayment.setVoucherId(voucher.getVoucherId());
					voucherPayment.setPaidAmount(voucher.getPayAmount());
					voucherPayment.setCreatedDate(DateUtils.toDate(DateUtils.toDate(new Date(),true),true));
					save(voucherPayment);
				}
			}
			Supplier supplier =	customerDAO.getSupplierById(voucher.getExpenseId());
			if(supplier.getCreditBalance()!=null)
				supplier.setCreditBalance(supplier.getCreditBalance()-voucher.getPayAmount());
			saveOrUpdate(supplier);
		}
		voucher = new Voucher();
		voucher.setVoucherId(maxValueDAO.getMaxVoucherId());
		java.util.Date date=new java.util.Date(); 
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
		voucher.setStrVoucherDate(formatter.format(date));
		voucher.getVoucherPayments().add(new VoucherPayment());
		modelAndView.addObject("voucher", voucher);
		return modelAndView;
	}

	@RequestMapping(value = "/details", method = RequestMethod.POST)
	public ModelAndView setSupplierDetails(ModelAndView  modelAndView,@ModelAttribute Voucher voucher){
		modelAndView.setViewName(Payment.PAYMENT_PAGE.value());
		setPaymentMetaData(modelAndView);
		voucher.getVoucherPayments().clear();
		if(voucher.getExpenseId()!=0 &&!String.valueOf(voucher.getExpenseId()).startsWith("100")){
			Supplier supplier =	customerDAO.getSupplierById(voucher.getExpenseId());
			List<String> invoiceNumbers=new ArrayList<String>();
			if(supplier !=null){
				voucher.setCreditBalance(supplier.getCreditBalance());
				List<Purchase> purchases = paymentDAO.getPurchaseBySupplierId(supplier.getSupId());
				if(purchases!=null && !purchases.isEmpty()){
					for(Purchase purchase : purchases){
						
						if(purchase.getBalancePayable()!=null && purchase.getBalancePayable() > 0){
							List<Voucher> vouchers = paymentDAO.getVouchers(purchase.getSupId().intValue());
							List<Long> vIds = Utils.getVoucherIds(vouchers);
							purchase.getInvoiceNumber();
//							List<VoucherPayment> payments = paymentDAO.getVoucherPayments(String.valueOf(purchase.getInvoiceNumber()));
							List<VoucherPayment> payments =  paymentDAO.getVoucherPayments(vIds);
							Double invoiceAmount=0.0;
							if(payments!=null && !payments.isEmpty()){
								for(VoucherPayment payment : payments){
									if(payment.getPaidAmount()!=null && payment.getRefNo().equals(purchase.getInvoiceNumber()))
										invoiceAmount = invoiceAmount+payment.getPaidAmount();
								}
							}
							if(invoiceAmount < purchase.getBalancePayable())
								invoiceNumbers.add(purchase.getInvoiceNumber());
						}
					}
					Purchase purchase = purchases.get(0);
					VoucherPayment payment = new VoucherPayment();
					payment.setRefTypeId(purchase.getInvoiceNumber());
					payment.setRefNo(String.valueOf(purchase.getId()));
					//payment.setNetAmount(purchase.getInvoiceAmount());
					//payment.setPaidAmount(purchase.getAmountPaid());
					voucher.getVoucherPayments().add(payment);
				}
				modelAndView.addObject("invoiceNumbers", invoiceNumbers);
			}else{
				logger.info("No supplier available for the expense/supplier id : " + voucher.getExpenseId());
			}
			
		}else{
			
			voucher.getVoucherPayments().add(new VoucherPayment());
		}
		
		modelAndView.addObject("voucher", voucher);
		return modelAndView;
	}

	
	@RequestMapping(value = "/purchase", method = RequestMethod.GET)
	public ModelAndView purchasePayment(ModelAndView  modelAndView){
		List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
		modelAndView.addObject(Payment.PAYMENT_TYPES.value(), paymentTypes);
		modelAndView.setViewName(Payment.PURCHASE.value());
		Purchase purchase = new Purchase();
		while(purchase.getPurchasePayments().size()<10){
			purchase.getPurchasePayments().add(new PurchasePayment());
		}
		modelAndView.addObject(Payment.PURCHASE.value(), purchase);
		List<Supplier> suppliers = paymentDAO.getSuppliers();
		modelAndView.addObject(Payment.SUPPLIERS.value(), suppliers);
		List<PurchaseItem> purchaseItems = metaDAO.getMetaData(new PurchaseItem());
		modelAndView.addObject(Payment.PURCHASE_ITEMS.value(), purchaseItems);
		modelAndView.addObject(Payment.PURCHASE_PAYMENTS.value(), purchase.getPurchasePayments());
		return modelAndView;
	}
	
	/*@RequestMapping(value = "/purchase/{purchaseId}", method = RequestMethod.GET)
	public ModelAndView loadPurchase(ModelAndView  modelAndView,@PathVariable String purchaseId){
		List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
		modelAndView.addObject(Payment.PAYMENT_TYPES.value(), paymentTypes);
		modelAndView.setViewName(Payment.PURCHASE.value());
		Purchase purchase = paymentDAO.getPurchaseById(Long.valueOf(purchaseId));
		if(purchase == null)
			purchase = new Purchase();
		
		if(purchase!=null && purchase.getGoodsReceivedDate()!=null)
			purchase.setStrGoodsReceivedDate(DateUtils.toDate(purchase.getGoodsReceivedDate(),true));
		
		while(purchase.getPurchasePayments().size()<10){
			purchase.getPurchasePayments().add(new PurchasePayment());
		}
		System.out.println("purchasepayment details :"+purchase.getPurchasePayments().get(0).getRate());
		modelAndView.addObject(Payment.PURCHASE.value(), purchase);
		List<Supplier> suppliers = paymentDAO.getSuppliers();
		modelAndView.addObject(Payment.SUPPLIERS.value(), suppliers);
		List<PurchaseItem> purchaseItems = metaDAO.getMetaData(new PurchaseItem());
		modelAndView.addObject(Payment.PURCHASE_ITEMS.value(), purchaseItems);
		modelAndView.addObject(Payment.PURCHASE_PAYMENTS.value(), purchase.getPurchasePayments());
		return modelAndView;
	}*/
	
	@RequestMapping(value = "/purchase/{purchaseId}", method = RequestMethod.GET)
	public ModelAndView loadPurchase(ModelAndView  modelAndView,@PathVariable String purchaseId){
		//System.out.println("purchase Id"+purchaseId);
		List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
		modelAndView.addObject(Payment.PAYMENT_TYPES.value(), paymentTypes);
		modelAndView.setViewName(Payment.PURCHASE_ORDER_VIEW.value());
		Purchase purchase = paymentDAO.getPurchaseById(Long.valueOf(purchaseId));
		//System.out.println("purchase size"+purchase.getStrInvoiceDate());
		if(purchase == null)
			purchase = new Purchase();
		if(purchase!=null && purchase.getGoodsReceivedDate()!=null)
			purchase.setStrGoodsReceivedDate(DateUtils.toDate(purchase.getGoodsReceivedDate(),true));
		
	//	System.out.println("purchse payments details:"+purchase.getPurchasePayments().get(0).getAmount() );
		modelAndView.addObject(Payment.PURCHASE.value(), purchase);
		Supplier suppliers = paymentDAO.getSupplier(purchase.getSupId());
		
		modelAndView.addObject(Payment.SUPPLIERS.value(), suppliers);
		List<PurchaseItem> purchaseItems = metaDAO.getMetaData(new PurchaseItem());
		//System.out.println("purchseItems size :"+purchaseItems.size());
		Map<Long,String> purchaseItemsTypeMap = Utils.getTypes(purchaseItems);
		
	  
	/*	modelAndView.addObject(Payment.PURCHASE_ITEMS.value(), purchaseItems);*/
		modelAndView.addObject("purchaseItems",purchaseItems);
		modelAndView.addObject("purchaseItemsTypeMap",purchaseItemsTypeMap);
		modelAndView.addObject(Payment.PURCHASE_PAYMENTS.value(), purchase.getPurchasePayments());
		 
		
		return modelAndView;
	}

	@RequestMapping(value = "/purchase", method = RequestMethod.POST)
	public ModelAndView savePurchasePayment(ModelAndView  modelAndView,@ModelAttribute Purchase purchase){
		try{
			purchase.setCreatedDate(DateUtils.toDate(DateUtils.toDate(new Date(),true),true));
		if(save(purchase)){
		
			for(PurchasePayment payment: purchase.getPurchasePayments()){
				if(!StringUtils.isEmpty(payment.getQty()) && !StringUtils.isEmpty(payment.getAmount())){
					payment.setPurchase(purchase);
					payment.setCreatedDate(DateUtils.toDate(DateUtils.toDate(new Date(),true),true));
					save(payment);
				}
			}
		}
		purchase.getSupId();
		Supplier supplier = customerDAO.getSupplierById(purchase.getSupId());
		if(supplier !=null && supplier.getCreditBalance()!=null){
			supplier.setCreditBalance(purchase.getBalancePayable()!=null ? supplier.getCreditBalance() + purchase.getBalancePayable():supplier.getCreditBalance());
			saveOrUpdate(supplier);
		}
		if(purchase.getAdvance()!=null && purchase.getAdvance()!=0){
			List<Voucher> vouchers = paymentDAO.getVouchers((int)supplier.getSupId());
			List<VoucherType> voucherTypes = metaDAO.getMetaData(new VoucherType());
			Map<Long,String> vMap =  Utils.getTypes(voucherTypes);
			if(vouchers!=null && !vouchers.isEmpty()){
				List<Long> vIds = Utils.getVoucherIds(vouchers);
				List<VoucherPayment> payments =  paymentDAO.getVoucherPayments(vIds);
				if(payments!=null && !payments.isEmpty()){
					for(VoucherPayment vp:payments){
						if(vMap.get(Long.valueOf(vp.getRefTypeId()))!=null && vMap.get(Long.valueOf(vp.getRefTypeId())).equals("Advance")){
							vp.setPaidAmount(0.0);
							logger.info("Updating advance amount to the supplier....");
							paymentDAO.saveOrUpdate(vp);
							
						}
					}
				}
			}
		}
		purchase = new Purchase();
		List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
		modelAndView.addObject(Payment.PAYMENT_TYPES.value(), paymentTypes);
		modelAndView.setViewName(Payment.PURCHASE.value());
		while(purchase.getPurchasePayments().size()<10){ 
			purchase.getPurchasePayments().add(new PurchasePayment());
		}
		modelAndView.addObject(Payment.PURCHASE.value(), purchase);
		List<Supplier> suppliers = paymentDAO.getSuppliers();
		modelAndView.addObject(Payment.SUPPLIERS.value(), suppliers);
		List<PurchaseItem> purchaseItems = metaDAO.getMetaData(new PurchaseItem());
		modelAndView.addObject(Payment.PURCHASE_ITEMS.value(), purchaseItems);
		modelAndView.addObject(Payment.PURCHASE_PAYMENTS.value(), purchase.getPurchasePayments());
		}catch (Exception _exception) {
			logger.error("savePurchasePayment :: "+_exception);
		}
		return modelAndView;
	}
	@RequestMapping(value="/supplier/{supplierId}", method=RequestMethod.GET)
	public @ResponseBody Supplier supplierById(@PathVariable long supplierId){
		Supplier supplier = null;
		try{
			supplier = customerDAO.getSupplierById(supplierId);
		}catch (Exception _exception) {
			
		}
		return supplier;
	}
	
	@RequestMapping(value="/receipt", method=RequestMethod.GET)
	public <T> ModelAndView receipt(ModelAndView modelAndView){
		modelAndView.setViewName(Payment.RECEIPT.value());
		Receipt receipt = new Receipt();
		receipt.setMode(0);
		receipt.setReceiptId(maxValueDAO.getMaxReceiptId());
		java.util.Date date=new java.util.Date(); 
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
		receipt.setStrReceiptDate(formatter.format(date));
// for Systemdate averification starts		
/*				 ControlTable  lastlogin=userDAO.getLastLoginDate();
				 Date ctdate=lastlogin.getLastLoginDate();
				 String strctdate=ctdate.toString();
				 strctdate=reportDAO.getyyyymmddtoddmmyyyy(strctdate);
				 if(strctdate.compareTo(receipt.getStrReceiptDate().substring(0, 10))>0 ){
					 modelAndView = new ModelAndView("redirect:/login");
					// modelAndView.setViewName(PAGE_LOGIN);
					 modelAndView.addObject("message","System Date has been changed,Contact Admin... ");
					 return modelAndView;
				 }*/
// for Systemdate averification Ends
		
		receipt.getReceiptPayments().add(new ReceiptPayment());
		modelAndView.addObject(Payment.RECEIPT.value(), receipt);
		setReceiptMetaData(modelAndView);
		return modelAndView;
	}
	
	@RequestMapping(value="/agentreceipt", method=RequestMethod.GET)
	public <T> ModelAndView agentReceipt(ModelAndView modelAndView){
		modelAndView.setViewName(Payment.RECEIPT_AGENT.value());
		Receipt receipt = new Receipt();
		receipt.setReceiptId(maxValueDAO.getMaxReceiptId());
		java.util.Date date=new java.util.Date(); 
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
		receipt.setStrReceiptDate(formatter.format(date));
		receipt.getReceiptPayments().add(new ReceiptPayment());
		modelAndView.addObject(Payment.RECEIPT_AGENT.value(), receipt);
		setReceiptMetaData(modelAndView);
		return modelAndView;
	}
	@RequestMapping(value="/agentreceipt", method=RequestMethod.POST)
	public <T> ModelAndView saveAgentReceipt( HttpServletRequest request,ModelAndView modelAndView, @ModelAttribute Receipt receipt){
		modelAndView.setViewName(Payment.RECEIPT.value());
		ReceiptPayment receiptpayment= null;
		try{
			receipt.setCreatedDate(DateUtils.toDate(DateUtils.toDate(new Date(),true),true));
			String uname=null;
			Double receiptAmt=0.0;
			uname = cookie.getCookie(request,"empid");
			receipt.setPreparedBy(uname);
			save(receipt);
			
			//System.out.println("receipt id : "+receipt.getReceiptId());
			//System.out.println("before receiptpayment.size () :"+receipt.getReceiptPayments().size());
			receiptAmt=receipt.getPayAmount();
		//	System.out.println("receipt Credited Amount : "+receiptAmt);

			receipt=orderDetail(receipt);
			//System.out.println("after receiptpayment.size () :"+receipt.getReceiptPayments().size());
			
			if(receipt.getReceiptPayments()!=null && !receipt.getReceiptPayments().isEmpty()){
				for(ReceiptPayment rp:receipt.getReceiptPayments()){
					Receipt rcpt=new Receipt();
					if(receiptAmt>0){
						rcpt.setReceiptId(receipt.getReceiptId());
						if(rp.getPaidAmount()>receiptAmt)
							rp.setPaidAmount(receiptAmt);
						rp.setReceipt(rcpt);
						rp.setRefTypeId(String.valueOf(receipt.getExpenseId()));
						rp.setCreatedDate(DateUtils.toDate(DateUtils.toDate(new Date(),true),true));
						paymentDAO.saveOrUpdate(rp);
						receiptAmt -= rp.getPaidAmount();
					}	
					
				}
			}
			if(receipt.getCustId()!=0){//Receipt against order only
				Customer customer = customerDAO.getCustomerById(receipt.getCustId());
				customer.setCreditBalance(customer.getCreditBalance()-receipt.getPayAmount());
				saveOrUpdate(customer);
			}
			receipt = new Receipt();
			
			receipt.setReceiptId(maxValueDAO.getMaxReceiptId());
			receipt.getReceiptPayments().add(new ReceiptPayment());
			java.util.Date date=new java.util.Date(); 
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
			receipt.setStrReceiptDate(formatter.format(date));
			modelAndView.addObject(Payment.RECEIPT.value(), receipt);
		}catch (Exception _exception) {
			logger.error("saveReceipt" + _exception);
		}
		setReceiptMetaData(modelAndView);
		return modelAndView;
	}
	/*
	
	@RequestMapping(value="/receipt", method=RequestMethod.POST)
	public <T> ModelAndView saveReceipt( HttpServletRequest request,ModelAndView modelAndView, @ModelAttribute Receipt receipt){
		modelAndView.setViewName(Payment.RECEIPT.value());
		try{
			receipt.setCreatedDate(DateUtils.toDate(DateUtils.toDate(new Date(),true),true));
			String uname=null;
			uname = cookie.getCookie(request,"empid");
			receipt.setPreparedBy(uname);
			save(receipt);
			if(receipt.getReceiptPayments()!=null && !receipt.getReceiptPayments().isEmpty()){
				for(ReceiptPayment rp:receipt.getReceiptPayments()){
					Receipt rcpt=new Receipt();
					rcpt.setReceiptId(receipt.getReceiptId());
					rp.setPaidAmount(receipt.getPayAmount());
					rp.setReceipt(rcpt);
					rp.setRefTypeId(String.valueOf(receipt.getExpenseId()));
					rp.setCreatedDate(DateUtils.toDate(DateUtils.toDate(new Date(),true),true));
					paymentDAO.saveOrUpdate(rp);
				}
			}
			if(receipt.getCustId()!=0){//Receipt against order only
				Customer customer = customerDAO.getCustomerById(receipt.getCustId());
				customer.setCreditBalance(customer.getCreditBalance()-receipt.getPayAmount());
				saveOrUpdate(customer);
			}
			receipt = new Receipt();
			receipt.setReceiptId(maxValueDAO.getMaxReceiptId());
			receipt.getReceiptPayments().add(new ReceiptPayment());
			modelAndView.addObject(Payment.RECEIPT.value(), receipt);
		}catch (Exception _exception) {
			logger.error("saveReceipt" + _exception);
		}
		setReceiptMetaData(modelAndView);
		return modelAndView;
	}
	*/
	
	
// changed for single receipt Auto Adjustment starts	
	@RequestMapping(value="/receipt", method=RequestMethod.POST)
	public <T> ModelAndView saveReceipt( HttpServletRequest request,HttpServletResponse response,ModelAndView modelAndView, @ModelAttribute Receipt receipt){
	//public <T> ModelAndView saveReceipt( HttpServletRequest request,ModelAndView modelAndView, @ModelAttribute Receipt receipt){
		modelAndView.setViewName(Payment.RECEIPT.value());
		ReceiptPayment receiptpayment= null;
		
		//System.out.println("print status Y/N : "+receipt.getStatusPrint().trim());		
	if(receipt.getStatusPrint().trim().equals("No")){	
		//System.out.println("I PRESSED NO");
		try{
			receipt.setCreatedDate(DateUtils.toDate(DateUtils.toDate(new Date(),true),true));
			String uname=null;
			Double receiptAmt=0.0;
			uname = cookie.getCookie(request,"empid");
			receipt.setPreparedBy(uname);
			save(receipt);
			
			//System.out.println("receipt id : "+receipt.getReceiptId());
			//System.out.println("before receiptpayment.size () :"+receipt.getReceiptPayments().size());
			receiptAmt=receipt.getPayAmount();
		//	System.out.println("receipt Credited Amount : "+receiptAmt);

			receipt=orderDetail(receipt);
			//System.out.println("after receiptpayment.size () :"+receipt.getReceiptPayments().size());
			
			if(receipt.getReceiptPayments()!=null && !receipt.getReceiptPayments().isEmpty()){
				for(ReceiptPayment rp:receipt.getReceiptPayments()){
					Receipt rcpt=new Receipt();
					if(receiptAmt>0){
						rcpt.setReceiptId(receipt.getReceiptId());
						if(rp.getPaidAmount()>receiptAmt)
							rp.setPaidAmount(receiptAmt);
						rp.setReceipt(rcpt);
						rp.setRefTypeId(String.valueOf(receipt.getExpenseId()));
						rp.setCreatedDate(DateUtils.toDate(DateUtils.toDate(new Date(),true),true));
						paymentDAO.saveOrUpdate(rp);
						receiptAmt -= rp.getPaidAmount();
					}	
					
				}
			}
			if(receipt.getCustId()!=0){//Receipt against order only
				Customer customer = customerDAO.getCustomerById(receipt.getCustId());
				customer.setCreditBalance(customer.getCreditBalance()-receipt.getPayAmount());
				saveOrUpdate(customer);
			}
			receipt = new Receipt();
			receipt.setMode(0);
			receipt.setReceiptId(maxValueDAO.getMaxReceiptId());
			receipt.getReceiptPayments().add(new ReceiptPayment());
			java.util.Date date=new java.util.Date(); 
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
			receipt.setStrReceiptDate(formatter.format(date));
			modelAndView.addObject(Payment.RECEIPT.value(), receipt);
		}catch (Exception _exception) {
			logger.error("saveReceipt" + _exception);
		}
		}
		else
		{// System.out.println("I PRESSED YES");
			
			try{
				
				receipt.setCreatedDate(DateUtils.toDate(DateUtils.toDate(new Date(),true),true));
				String uname=null;
				Double receiptAmt=0.0;
				uname = cookie.getCookie(request,"empid");
				receipt.setPreparedBy(uname);
				save(receipt);
				
				//System.out.println("receipt id : "+receipt.getReceiptId());
				//System.out.println("before receiptpayment.size () :"+receipt.getReceiptPayments().size());
				receiptAmt=receipt.getPayAmount();
			//	System.out.println("receipt Credited Amount : "+receiptAmt);

				receipt=orderDetail(receipt);
				//System.out.println("after receiptpayment.size () :"+receipt.getReceiptPayments().size());
				
				if(receipt.getReceiptPayments()!=null && !receipt.getReceiptPayments().isEmpty()){
					for(ReceiptPayment rp:receipt.getReceiptPayments()){
						Receipt rcpt=new Receipt();
						if(receiptAmt>0){
							rcpt.setReceiptId(receipt.getReceiptId());
							if(rp.getPaidAmount()>receiptAmt)
								rp.setPaidAmount(receiptAmt);
							rp.setReceipt(rcpt);
							rp.setRefTypeId(String.valueOf(receipt.getExpenseId()));
							rp.setCreatedDate(DateUtils.toDate(DateUtils.toDate(new Date(),true),true));
							
							
							paymentDAO.saveOrUpdate(rp);
							receiptAmt -= rp.getPaidAmount();
						}	
						
					}
				}
				if(receipt.getCustId()!=0){//Receipt against order only
					Customer customer = customerDAO.getCustomerById(receipt.getCustId());
					customer.setCreditBalance(customer.getCreditBalance()-receipt.getPayAmount());
					saveOrUpdate(customer);
					
				}
				// appending address for receipt printing 
				Customer customer= customerDAO.getCustomerById(receipt.getCustId());
				List <Address> address=customerDAO.getAddressByCustId(receipt.getCustId());
				
				if (address !=null)
					customer.setAddress(address);
				
				receipt.setCustomer(customer);
				//System.out.println("address  1 :"+receipt.getCustomer().getAddress().get(0).getAddress1());
			//	System.out.println("address  2 :"+receipt.getCustomer().getAddress().get(0).getAddress2());
				List<PaymentType> pt = metaDAO.getPaymentTypes();
				Map<Long,String> paymentTypeMap = Utils.getTypes(pt);
				String payid=paymentTypeMap.get(Long.valueOf(receipt.getPaymentId()));
				List<ReceiptType> rt = metaDAO.getReceiptTypes();
				Map<Long,String> receiptTypeMap = Utils.getTypes(rt);
				String expid=receiptTypeMap.get(Long.valueOf(receipt.getExpenseId()));
				
				receipt.setRefType(expid);
				receipt.setPayType(payid);
				/*creating PDF in RECEIPT Folder */
				//System.out.println("I am starting to print");
				printReceipt(receipt);
				
			      System.out.println("I am AFTER print");
				receipt = new Receipt();
				receipt.setMode(1);
				receipt.setReceiptId(maxValueDAO.getMaxReceiptId());
				receipt.getReceiptPayments().add(new ReceiptPayment());
				java.util.Date date=new java.util.Date(); 
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
				receipt.setStrReceiptDate(formatter.format(date));
				modelAndView.addObject(Payment.RECEIPT.value(), receipt);
			}catch (Exception _exception) {
				logger.error("saveReceipt" + _exception);
			}
			
			
		}
		setReceiptMetaData(modelAndView);
		return modelAndView;
	}
	
	@RequestMapping(value ="/showPDF/{receiptId}", method = RequestMethod.GET)
	@ResponseBody
	    public void downloadPDFResource( 
	                                     HttpServletResponse response,
	                                     @PathVariable String receiptId,
	                                     @RequestHeader String referer,HttpServletRequest request
	                                      )
	    {
		/*Reading PDF to Display*/
		
		
		  System.out.println("I am at showPDF now");
		String fileName=null;
			 fileName = receiptId+".pdf";
			 String dataDirectory = env.getProperty("receipt");
		        Path file = Paths.get(dataDirectory, fileName);
		        if (Files.exists(file))
		        {  
		            response.setContentType("application/pdf");
		           response.addHeader("Content-Disposition", "inline; filename="+fileName);
		        try
		            {
		        	    Files.copy(file, response.getOutputStream());
		                response.getOutputStream().flush();
		               
		            }catch (IOException ex) {
		                ex.printStackTrace();
		            }
		   
		        }
		
	    }

	
	public  Receipt orderDetail(Receipt receipt){
		List<OrderTrxnDetail> balanceDetails = new ArrayList<OrderTrxnDetail>(); 
		ReceiptPayment receiptpayment = null;
		if(receipt.getCustId()!=0){
		Customer customer = customerDAO.getCustomerById(receipt.getCustId());
		BigDecimal bd = new BigDecimal(customer.getCreditBalance());
	    bd = bd.setScale(2, RoundingMode.HALF_UP);
		//receipt.setCustBalance(AmountUtils.round(customer.getCreditBalance(), 2));
		List<Order> orders = orderDAO.getCustomerOrders(receipt.getCustId());
		System.out.println("order size () :"+receipt.getCustBalance());

		List<Long> orderIds = new ArrayList<Long>();
		if(orders!=null && !orders.isEmpty()){
			List<Long> oIds = Utils.getOrdersIds(orders);
			List<OrderTrxnDetail> details =  orderDAO.getOrderTrxnDetails(oIds);
		//	System.out.println("trxn order size () :"+details.size());
//
			if(details !=null && !details.isEmpty()){
				for(OrderTrxnDetail trxnDetail: details){
					if(trxnDetail.getBalance()>0){
//						orderIds.add(trxnDetail.getOrderId());
						List<ReceiptPayment> payments = paymentDAO.getReceiptPaymentsByOrderId(String.valueOf(trxnDetail.getOrderId()));
						//System.out.println("ReceiptPayment payments size () :"+payments.size());

						Double reciptAmout = 0.0;
						if(payments!=null && !payments.isEmpty()){
							for(ReceiptPayment rp:payments){
								if(rp.getPaidAmount()!=null)
								reciptAmout = reciptAmout+rp.getPaidAmount();
							}
							
						}
						if(trxnDetail.getAdvance()!=null)
							reciptAmout=reciptAmout+trxnDetail.getAdvance();
			//System.out.println("ReceiptAmount :"+reciptAmout);

						if(reciptAmout < trxnDetail.getNetAmount()){
							//receipt.getReceiptPayments().add(new ReceiptPayment());
							receiptpayment = new ReceiptPayment();
							receiptpayment.setRefNo(trxnDetail.getOrderId()+"");
							receiptpayment.setPaidAmount(trxnDetail.getNetAmount()-reciptAmout);
							receiptpayment.setNetAmount(trxnDetail.getNetAmount());
							receipt.getReceiptPayments().add(receiptpayment);
						}
					}
					
				}
				
				//System.out.println("receiptpayment size () :"+receipt.getReceiptPayments().size());

				
					}
		}
	
		}
	return receipt;		
	}
// changed for single receipt Auto Adjustment ENDS
	
	
	@RequestMapping(value="/customers",method = RequestMethod.GET)
	public @ResponseBody List<Value> autoCompleteCustomers(@RequestParam String searchTerm){
		List<Value> values = new ArrayList<Value>();
		logger.info("Search term : " + searchTerm);
		
		List<Customer> customers = customerDAO.getOrderCustomers(searchTerm);
		if(customers!=null && !customers.isEmpty()){
			
			List<Long> custIds = Utils.getCustomerIdList(customers);
			List<Order> custOrders = orderDAO.getCustomerOrders(custIds);
			Map<Long,Order> orderIds = Utils.getCustIdOrders(custOrders);
			for(Customer customer : customers){
				if(orderIds.get(customer.getCustId())!=null){
					Value value = new Value();
					value.setName(customer.getFirstName());
					value.setId(String.valueOf(customer.getCustId()));
					values.add(value);
				}
			}
		}
		
		return values;
	}

	/*@RequestMapping(value="/orderdetail", method=RequestMethod.POST)
	public <T> ModelAndView orderDetail(ModelAndView modelAndView, @ModelAttribute Receipt receipt){
		modelAndView.setViewName(Payment.RECEIPT.value());
		if(receipt.getCustId()!=0){
		Customer customer = customerDAO.getCustomerById(receipt.getCustId());
		BigDecimal bd = new BigDecimal(customer.getCreditBalance());
	    bd = bd.setScale(2, RoundingMode.HALF_UP);
		receipt.setCustBalance(AmountUtils.round(customer.getCreditBalance(), 2));
		List<Order> orders = orderDAO.getCustomerOrders(receipt.getCustId());
	
		List<Long> orderIds = new ArrayList<Long>();
		if(orders!=null && !orders.isEmpty()){
			List<Long> oIds = Utils.getOrdersIds(orders);
			List<OrderTrxnDetail> details =  orderDAO.getOrderTrxnDetails(oIds);
			if(details !=null && !details.isEmpty()){
				for(OrderTrxnDetail trxnDetail: details){
					if(trxnDetail.getBalance()>0){
//						orderIds.add(trxnDetail.getOrderId());
						List<ReceiptPayment> payments = paymentDAO.getReceiptPaymentsByOrderId(String.valueOf(trxnDetail.getOrderId()));
						Double reciptAmout = 0.0;
						if(payments!=null && !payments.isEmpty()){
							for(ReceiptPayment rp:payments){
								if(rp.getPaidAmount()!=null)
								reciptAmout = reciptAmout+rp.getPaidAmount();
							}
						}
						if(trxnDetail.getAdvance()!=null)
							reciptAmout=reciptAmout+trxnDetail.getAdvance();
						if(reciptAmout < trxnDetail.getNetAmount()){
							orderIds.add(trxnDetail.getOrderId());
						}
					}
				}
				
			}
				
		}
		modelAndView.addObject(Payment.ORDER_IDS.value(), orderIds);
		receipt.getReceiptPayments().clear();
		receipt.getReceiptPayments().add(new ReceiptPayment());
		}
		modelAndView.addObject(Payment.RECEIPT.value(), receipt);
		setReceiptMetaData(modelAndView);
		return modelAndView;
	}*/
	
	@RequestMapping(value="/orderdetail", method=RequestMethod.POST)
	public <T> ModelAndView orderDetail(ModelAndView modelAndView, @ModelAttribute Receipt receipt){
		modelAndView.setViewName(Payment.RECEIPT.value());
		if(receipt.getCustId()!=0){
		/* ADDED FOR CURRENT CUSTOMER OUTSTANDING */
		Double currentCreditBalance	= orderDAO.getCurrentCreditBalance(receipt.getCustId());
		//Customer customer = customerDAO.getCustomerById(receipt.getCustId());
		Customer customer = new Customer();
		customer.setCreditBalance(currentCreditBalance);
		//System.out.println("receipt Credited Amount : 12");
		/* ADDED FOR CURRENT CUSTOMER OUTSTANDING */
		BigDecimal bd = new BigDecimal(customer.getCreditBalance());
	 	bd = bd.setScale(2, RoundingMode.HALF_UP);
		receipt.setCustBalance(AmountUtils.round(customer.getCreditBalance(), 2));
		List<Order> orders = orderDAO.getCustomerOrders(receipt.getCustId());
	
		List<Long> orderIds = new ArrayList<Long>();
		if(orders!=null && !orders.isEmpty()){
			List<Long> oIds = Utils.getOrdersIds(orders);
			List<OrderTrxnDetail> details =  orderDAO.getOrderTrxnDetails(oIds);
			if(details !=null && !details.isEmpty()){
				for(OrderTrxnDetail trxnDetail: details){
					if(trxnDetail.getBalance()>0){
//						orderIds.add(trxnDetail.getOrderId());
						List<ReceiptPayment> payments = paymentDAO.getReceiptPaymentsByOrderId(String.valueOf(trxnDetail.getOrderId()));
						Double reciptAmout = 0.0;
						if(payments!=null && !payments.isEmpty()){
							for(ReceiptPayment rp:payments){
								if(rp.getPaidAmount()!=null)
								reciptAmout = reciptAmout+rp.getPaidAmount();
							}
						}
						if(trxnDetail.getAdvance()!=null)
							reciptAmout=reciptAmout+trxnDetail.getAdvance();
						if(reciptAmout < trxnDetail.getNetAmount()){
							orderIds.add(trxnDetail.getOrderId());
						}
					}
				}
				
			}
				
		}
		modelAndView.addObject(Payment.ORDER_IDS.value(), orderIds);
		receipt.getReceiptPayments().clear();
		receipt.getReceiptPayments().add(new ReceiptPayment());
		}
		modelAndView.addObject(Payment.RECEIPT.value(), receipt);
		setReceiptMetaData(modelAndView);
		return modelAndView;
	}
	
	
	@RequestMapping(value="/invoice/{invoiceNum}/{supId}",method = RequestMethod.GET)
	public @ResponseBody Purchase getInvoiceAmount(@PathVariable String invoiceNum,@PathVariable Long supId){
		Purchase purchase = null;
		
		try{
			purchase = new Purchase();
			logger.info("invoice number :" + invoiceNum +" supplier id :"+ supId +" to get the invoice amount");
			List<Purchase> purchases = paymentDAO.getPurchaseBySupplierId(supId,invoiceNum);
			for(Purchase pur:purchases){
				purchase.setInvoiceAmount(pur.getNetPayable());
				if(pur.getAmountPaid() !=null){
					List<Voucher> vouchers = paymentDAO.getVouchers(pur.getSupId().intValue());
					List<Long> vIds = Utils.getVoucherIds(vouchers);
					List<VoucherPayment> inVoucherPayments =  paymentDAO.getVoucherPayments(vIds);
//					List<VoucherPayment> inVoucherPayments = paymentDAO.getVoucherPayments(String.valueOf(pur.getInvoiceNumber()));
					Double invoiceAmtPaid = 0.0;
					if(inVoucherPayments !=null && !inVoucherPayments.isEmpty()){
						for(VoucherPayment payment : inVoucherPayments){
							if(payment.getRefNo().equals(pur.getInvoiceNumber()) && payment.getPaidAmount()!=null)
							invoiceAmtPaid = invoiceAmtPaid+payment.getPaidAmount();
						}
					}
					
					purchase.setAmountPaid(purchase.getAmountPaid()!=null ?purchase.getAmountPaid()+pur.getAmountPaid():pur.getAmountPaid());
					purchase.setAmountPaid(purchase.getAmountPaid()+invoiceAmtPaid);
					purchase.setBalancePayable(purchase.getInvoiceAmount()-purchase.getAmountPaid());
				}
			}
		}catch (Exception _exception) {
			logger.error("getInvoiceAmount ::"+_exception);
		}
		
		return purchase;
	}
	
	@RequestMapping(value="/receipt/{orderId}/{custId}",method = RequestMethod.GET)
	public @ResponseBody OrderTrxnDetail getOrderAmount(@PathVariable long orderId,@PathVariable long custId){
		OrderTrxnDetail order = null;
		
		try{
			order = new OrderTrxnDetail();
			logger.info("order number :" + orderId +" supplier id :"+ custId +" to get the invoice amount");
			OrderTrxnDetail pOrder = paymentDAO.getOrderTransactionByCustIdOrderId(orderId);
			//System.out.println("Net Amount   ...:" +pOrder.getNetAmount());
			if(pOrder!=null){
				order.setNetAmount(pOrder.getNetAmount());
				List<ReceiptPayment> receiptPayments = paymentDAO.getReceiptPaymentsByOrderId(String.valueOf(pOrder.getOrderId()));
				Double receiptAmnt=0.0;
				if(receiptPayments!=null && !receiptPayments.isEmpty()){
					for(ReceiptPayment rp:receiptPayments){
						receiptAmnt = receiptAmnt+rp.getPaidAmount();
					}
				}
				if(pOrder.getAdvance()!=null)
					receiptAmnt = receiptAmnt+pOrder.getAdvance();
				order.setAdvance(receiptAmnt);
				order.setBalance(order.getNetAmount()-receiptAmnt);
				
			}
			
		}catch (Exception _exception) {
			logger.error("getInvoiceAmount ::"+_exception);
		}
		
		return order;
	}
	
	@RequestMapping(value="/advance/{supId}",method = RequestMethod.GET)
	public @ResponseBody VoucherPayment getOrderAmount(@PathVariable int supId){
		VoucherPayment payment = new VoucherPayment();
		try{
			
			List<Voucher> vouchers = paymentDAO.getVouchers(supId);	
			if(vouchers!=null && !vouchers.isEmpty()){
				List<Long> vIds = Utils.getVoucherIds(vouchers);
				List<VoucherPayment> payments = paymentDAO.getVoucherPayments(vIds);
				List<VoucherType> voucherTypes = metaDAO.getMetaData(new VoucherType());
				Map<Long,String> vTypes = Utils.getTypes(voucherTypes);
				
				if(payments!=null && !payments.isEmpty()){
					for(VoucherPayment vp: payments){
						if(vTypes.get(Long.valueOf(vp.getRefTypeId()))!=null && 
								vTypes.get(Long.valueOf(vp.getRefTypeId())).equals("Advance")){
							payment = vp;
						}
					}
				}
 			}
			Supplier supplier = customerDAO.getSupplierById(supId);
			if(supplier!=null)
				payment.setGstNo(supplier.getGstNo());
		}catch (Exception _exception) {
			logger.error("getOrderAmount ::"+_exception);
		}
		
		return payment;
	}
	
	private <T> Boolean  save(T t){
		logger.info("save :: save" + t.getClass().getName());
		try{
			return paymentDAO.save(t);
		}catch (Exception _exception) {
			logger.error(_exception);
			_exception.printStackTrace();
		}
		return false;
	}
	
	private <T> Boolean  saveOrUpdate(T t){
		logger.info("save :: save" + t.getClass().getName());
		try{
			return paymentDAO.saveOrUpdate(t);
		}catch (Exception _exception) {
			logger.error(_exception);
			_exception.printStackTrace();
		}
		return false;
	}
	
	
	private void setPaymentMetaData(ModelAndView modelAndView){
		List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
		modelAndView.addObject(Payment.PAYMENT_TYPES.value(), paymentTypes);
		List<ExpenseType> expenseTypes = metaDAO.getExpenseTypes();	
		List<Supplier> suppliers = paymentDAO.getSuppliersWithBalance();
		if(suppliers!=null && !suppliers.isEmpty()){
			for(Supplier supplier:suppliers){
				ExpenseType type = new ExpenseType();
				type.setId((int)supplier.getSupId());
				type.setName(supplier.getName());
				expenseTypes.add(type);
			}
		}
		modelAndView.addObject(Payment.EXPENSE_TYPES.value(), expenseTypes);
		List<VoucherType> voucherTypes = metaDAO.getMetaData(new VoucherType());
		modelAndView.addObject(Payment.VOUCHER_TYPES.value(), voucherTypes);
	}
	
	private void setSupplierMetaData(ModelAndView modelAndView){
		List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
		modelAndView.addObject(Payment.PAYMENT_TYPES.value(), paymentTypes);
		List<ExpenseType> expenseTypes = new ArrayList<ExpenseType>();
		List<Supplier> suppliers = paymentDAO.getSuppliersWithBalance();
		if(suppliers!=null && !suppliers.isEmpty()){
			for(Supplier supplier:suppliers){
				ExpenseType type = new ExpenseType();
				type.setId((int)supplier.getSupId());
				type.setName(supplier.getName());
				expenseTypes.add(type);
			}
		}
		modelAndView.addObject(Payment.EXPENSE_TYPES.value(), expenseTypes);
		List<VoucherType> voucherTypes = metaDAO.getMetaData(new VoucherType());
		modelAndView.addObject(Payment.VOUCHER_TYPES.value(), voucherTypes);
	}
	
	@SuppressWarnings("unchecked")
	private <T> void setReceiptMetaData(ModelAndView modelAndView){
		List<T> paymentTypes = (List<T>) metaDAO.getMetaData(new PaymentType());
		modelAndView.addObject(Payment.PAYMENT_TYPES.value(), paymentTypes);
		List<T> receiptTypes = (List<T>) metaDAO.getMetaData(new ReceiptType());
		modelAndView.addObject(Payment.RECEIPT_TYPES.value(), receiptTypes);
	}
	
	@RequestMapping(value="/billcopy", method=RequestMethod.GET)
	public ModelAndView billCopy(ModelAndView modelAndView){
		
		modelAndView.setViewName(Payment.PAGE_BILL_COPY.value());
		
		return modelAndView;
	}
	

/*TO PRINT THE RECEIPT*/

public void printReceipt(Receipt receipt){

	try{
		
		System.out.println("i m working");
		if (receipt==null){
			//System.out.println("Deleted Record :"+orderId);
			return ;
		}
		String pDoc=null;
	    pDoc = env.getProperty("receipt")+receipt.getReceiptId()+".pdf";
		try
	       {
	    	  PdfReader reader   = new PdfReader(env.getProperty("invoice.template")+"//"+"VAISHNAVI_RECEIPT.pdf");
	    	  PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pDoc));
	          AcroFields form    = stamper.getAcroFields();
	          PdfContentByte content = stamper.getOverContent(reader.getNumberOfPages());
	          System.out.println("CUST BALANCE OP BAL :"+String.format("%.2f",Double.valueOf(receipt.getCustBalance())));
	          System.out.println("CUST BALANCE OP BAL without:"+receipt.getCustBalance());
	          String str=receipt.getStrPayAmount();
	          String receipt_amt= Utils.numberToWord(str.substring(0,str.indexOf('.')));
	          System.out.println("payment mode :"+receipt.getStrPaymentId());
	          System.out.println("expences id :"+receipt.getStrExpenseId());
	          String strdate = receipt.getStrReceiptDate();
	          // header of the bill
	          form.setField("NAME", receipt.getCustName().toUpperCase());
	          form.setField("ADDR_1", receipt.getCustomer().getAddress().get(0).getAddress1().toUpperCase());
	          form.setField("ADDR_2", receipt.getCustomer().getAddress().get(0).getAddress2().toUpperCase());
	          form.setField("PHONE", "Mobile:"+receipt.getCustomer().getMobileNo());
	          form.setField("EMAIL", receipt.getCustomer().getEamil());
	          form.setField("GST",receipt.getCustomer().getGstNo());
	          form.setField("RECEIPT_ID", Long.toString(receipt.getReceiptId()));
	          form.setField("DATE",  strdate.substring(0,10));
	          form.setField("OP_BAL", String.format("%.2f",Double.valueOf(receipt.getCustBalance())));
	          form.setField("PAYMENT",String.format("%.2f", Double.valueOf(receipt.getStrPayAmount()))); 
	          form.setField("PAYMENT_1","Rs."+String.format("%.2f", Double.valueOf(receipt.getStrPayAmount()))); 
	          form.setField("DUE",String.format("%.2f",receipt.getBalanceAmount()) );
	          form.setField("PREPARED_BY", receipt.getPreparedBy().toUpperCase());
	          form.setField("REF_ID", receipt.getRefType());
	          form.setField("MODE",receipt.getPayType().toUpperCase());
	          form.setField("PAYMENT_SCRIPT","Rupees "+receipt_amt+"Only" );
	          if(receipt.getPaymentId()==103){
	        	  form.setField("CHEQUE_NO", "CQ.No:"+receipt.getChequeNo());
	        	  form.setField("CHEQUE_DATE","Date:"+receipt.getStrChequeDate());
	        	  form.setField("BANK_NAME","Bank:"+receipt.getBankName());
	          }else{
	        	  form.setField("REMARKS",receipt.getNaration());  
	          }
	          
	           
	          stamper.close();
	          List<File> files =new ArrayList<File>();
        	   files.add(new File( env.getProperty("receipt")+receipt.getReceiptId()+".pdf"));
	          
	        }
	        catch (Exception e)
	        {
	          e.printStackTrace();
	        }
	}catch (Exception _exception) { 
		logger.error("printOrder exception ::" + _exception);
	
	}
	return ;
	}
}

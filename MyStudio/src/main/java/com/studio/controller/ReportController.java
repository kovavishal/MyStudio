/**
 * 
 */
package com.studio.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.studio.constants.Payment;
import com.studio.constants.ReportConstants;
import com.studio.constants.WorkOrderConstants;
import com.studio.dao.CustomerDAO;
import com.studio.dao.MetaDAO;
import com.studio.dao.OrderDAO;
import com.studio.dao.ReportDAO;
import com.studio.dao.UserDAO;
import com.studio.dao.PaymentDAO;
import com.studio.domain.Address;
import com.studio.domain.AgentReportDetail;
import com.studio.domain.Area;
import com.studio.domain.Customer;
import com.studio.domain.DailyReportDetail;
import com.studio.domain.Dashboard;
import com.studio.domain.Deportment;
import com.studio.domain.Employee;
import com.studio.domain.ExpenseType;
import com.studio.domain.JobAllocation;
import com.studio.domain.JobStatus;
import com.studio.domain.Order;
import com.studio.domain.OrderTrxnDetail;
import com.studio.domain.PaymentType;
import com.studio.domain.Product;
import com.studio.domain.Receipt;
import com.studio.domain.ReceiptPayment;
import com.studio.domain.Report;
import com.studio.domain.ReportDetail;
import com.studio.domain.SmsData;
import com.studio.domain.Supplier;
//import com.studio.domain.NewTable;
import com.studio.service.ReportService;

import com.studio.service.SmsService;
import com.studio.service.combinedquery;
import com.studio.utils.DateUtils;
import com.studio.utils.Utils;

/**
 * @author ezhilraja_k
 *
 */

@RestController
@RequestMapping(value="/report")
@PropertySource("classpath:studio.properties")
public class ReportController implements WorkOrderConstants{
	
	@PersistenceContext
	  EntityManager entityManager;
	

	@Autowired
	private ReportService reportService;

	@Autowired
	private combinedquery combinedQuery;
	
	@Autowired
	private MetaDAO metaDAO;
	@ManyToOne
	@Autowired
	private UserDAO userDAO;

	@Autowired(required=true)
	private SmsService smsService;

	@Autowired
	private ReportDAO reportDAO;

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private PaymentDAO paymentDAO;
	
	@Autowired
	private CustomerDAO customerDAO;
	@Autowired
	private PrintController cookies;
	@Autowired
	private Environment env;
	
	
	Logger logger = Logger.getLogger(ReportController.class);
	
//	@RequestMapping(value="/testpage",method = RequestMethod.GET)
//	public ModelAndView testPage(ModelAndView model){
//		ModelAndView modelAndView = new ModelAndView(ReportConstants.TEST_PAGE.value());
//		NewTable newtable= new NewTable();
//		modelAndView.addObject(ReportConstants.NEWTABLE.value(), newtable);
//		return modelAndView;
//	}
//	
	
	@RequestMapping(value="/ledger",method = RequestMethod.GET)
	public ModelAndView reportLedger(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.PAGE_LEDDGER.value());
		Report report= new Report();
		List<ExpenseType> expenseTypes = metaDAO.getExpenseTypes();
		modelAndView.addObject(Payment.EXPENSE_TYPES.value(), expenseTypes);
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	
	// newly added for myreport stats
	
	@RequestMapping(value="/ordervsdespatch",method = RequestMethod.GET)
	public ModelAndView reportOrdervsDespatch(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.PAGE_ORDER_VS_DESPATCH.value());
		Report report= new Report();
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	@RequestMapping(value="/undeliveredalbum",method = RequestMethod.GET)
	public ModelAndView reportUndeliveredAlbum(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.PAGE_UNDELIVERED_ALBUM_REPORT.value());
		Report report= new Report();
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	
	@RequestMapping(value="/customeroutstanding",method = RequestMethod.GET)
	public ModelAndView reportCustomerOutstanding(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.PAGE_CUST_OUTSTANDING_REPORT.value());
		Report report= new Report();
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	@RequestMapping(value="/customeroutstandingdetails",method = RequestMethod.GET)
	public ModelAndView reportCustomerOutstandingDetails(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.PAGE_CUST_OUTSTANDING_SUMMARY_REPORT.value());
		Report report= new Report();
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	@RequestMapping(value="/dateWiseCustomeroutstandingdetails",method = RequestMethod.GET)
	public ModelAndView reportDateWiseCustomerOutstandingDetails(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.PAGE_DATE_WISE_CUST_OUTSTANDING_SUMMARY_REPORT.value());
		Report report= new Report();
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	@RequestMapping(value="/customeroffersalereport",method = RequestMethod.GET)
	public ModelAndView reportCustomerOfferSale(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.PAGE_CUST_OFFER_SALE_REPORT.value());
		Report report= new Report();
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	
	@RequestMapping(value="/supplieroutstanding",method = RequestMethod.GET)
	public ModelAndView reportSupplierOutstanding(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.PAGE_SUPP_OUTSTANDING_REPORT.value());
		Report report= new Report();
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	
	@RequestMapping(value="/customeraddress",method = RequestMethod.GET)
	public ModelAndView reportCustomeraddress(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.PAGE_CUST_ADDRESS_REPORT.value());
		Report report= new Report();
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	
	
	
	@RequestMapping(value="/customervsbusiness",method = RequestMethod.GET)
	public ModelAndView reportCustomervsBusiness(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.PAGE_CUSTOMER_VS_BUSINESS.value());
		Report report= new Report();
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	@RequestMapping(value="/mbrsummary",method = RequestMethod.GET)
	public ModelAndView reportMBRSummary(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.PAGE_MBR_SUMMARY.value());
		Report report= new Report();
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	
	@RequestMapping(value="/mbstatement",method = RequestMethod.GET)
	public ModelAndView reportMBStatement(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.PAGE_MB_STATEMENT.value());
		Report report= new Report();
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	
	
	@RequestMapping(value="/srpreport",method = RequestMethod.GET)
	public ModelAndView reportSRP(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.PAGE_SRP_REPORT.value());
		Report report= new Report();
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	
	@ModelAttribute("reportList")
	 public List<String> getReportList() {
	      List<String> reportList = new ArrayList<String>();
	      reportList.add("Sales");
	      reportList.add("Receipts");
	      reportList.add("Payments");
	      reportList.add("Cancelled");
	      reportList.add("Discount");
	      
	      return reportList;
	   }
	//newly added for myreport ending
	
	@RequestMapping(value="/customer",method = RequestMethod.GET)
	public ModelAndView reportCustomer(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.PAGE_CUSTOMER_REPORT.value());
		Report report= new Report();
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	
	@RequestMapping(value="/cancelorderreport",method = RequestMethod.GET)
	public ModelAndView reportCancelOrder(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.PAGE_CANCELLED_ORDER_REPORT.value());
		Report report= new Report();
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	@RequestMapping(value="/supplier",method = RequestMethod.GET)
	public ModelAndView reportSupplier(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.PAGE_SUPPLIER_REPORT.value());
		Report report= new Report();
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	@RequestMapping(value="/backup",method = RequestMethod.GET)
	public ModelAndView reportBackup(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.BACKUP.value());
		Report report= new Report();
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	@RequestMapping(value="/daily",method = RequestMethod.GET)
	public ModelAndView reportDaily(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.PAGE_DAILY_REPORT.value());
		Report report= new Report();
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	@RequestMapping(value="/selectedday",method = RequestMethod.GET)
	public ModelAndView reportSelectedDay(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.PAGE_SELECTEDDAY_REPORT.value());
		Report report= new Report();
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	@RequestMapping(value="/monthly",method = RequestMethod.GET)
	public ModelAndView reportMonthly(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.PAGE_MONTHLY_REPORT.value());
		Report report= new Report();
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	
	@RequestMapping(value="/search",method = RequestMethod.POST)
	public ModelAndView searchLedgerReport(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.PAGE_LEDDGER.value());
		Report report= new Report();
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	
	
	
	@RequestMapping(value="/detail",method=RequestMethod.POST)
	public @ResponseBody List<ReportDetail> getReport(HttpServletRequest request){
		List<ReportDetail> reportDetails = new ArrayList<ReportDetail>();
		try{
			Report report = new Report();
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			String type = request.getParameter("type");
			String rType = request.getParameter("report");
			System.out.println("TYPE ==== :"+type);
			if(!StringUtils.isEmpty(type)){
				report.setTypeId(Integer.valueOf(type));
				report.setFromDate(DateUtils.toDate(fromDate,true));
				report.setToDate(DateUtils.toDate(toDate,true));
				List<ReportDetail> details = reportService.voucher(report);
				System.out.println("VOUCHERS :"+details.size());
				if(details!=null && !details.isEmpty())
					reportDetails.addAll(details);
			}else{
				report.setFromDate(DateUtils.toDate(fromDate,true));
				report.setToDate(DateUtils.toDate(toDate,true));
				List<ReportDetail> details = null;
				
				/*if(!StringUtils.isEmpty(rType)){
					details = reportService.balance(report, OPENING);
					if(details!=null && !details.isEmpty())
						reportDetails.addAll(details);
				}
				*/
				details = reportService.mbrorder(report);
				if(details!=null && !details.isEmpty())
					reportDetails.addAll(details);
				
				/*details = reportService.mbrsupplier(report);*/
				details = reportService.mbrdetailpurchase(report);
				if(details!=null && !details.isEmpty())
					reportDetails.addAll(details);
				
				 details = reportService.supplierVoucher(report);
				//System.out.println("VOUCHERS :"+details.size());
				if(details!=null && !details.isEmpty()){
				  	   reportDetails.addAll(details);
				  
				}
					
				
				details = reportService.mbrdetailexpense(report);
				if(details!=null && !details.isEmpty())
					reportDetails.addAll(details);
				
				/*details = reportService.mbrpaymentvoucher(report);
				if(details!=null && !details.isEmpty())
					reportDetails.addAll(details);*/
			/*	
				details = reportService.mbrreceipt(report);
				if(details!=null && !details.isEmpty())
					reportDetails.addAll(details);*/
				
				/*if(!StringUtils.isEmpty(rType)){
					details = reportService.balance(report, CLOSING);
					if(details!=null && !details.isEmpty())
						reportDetails.addAll(details);
				}*/
				
			}
		}catch (Exception _exception) {
			logger.error("getReport::" + _exception);
		}
		
	/*	Collections.sort(reportDetails,new Comparator<ReportDetail>(){
			 DateFormat f = new SimpleDateFormat("dd/MM/yyyy");	
			public int compare(ReportDetail o1, ReportDetail o2) {
				try{
				return f.parse(o1.getTrxnDate()).compareTo(f.parse(o2.getTrxnDate()));
					//return (o1.getTrxnDate1()).compareTo((o2.getTrxnDate1()));
				
				}catch(ParseException e){
	                throw new IllegalArgumentException(e);
	            }
			}	
			
		    });*/
		return reportDetails;
	}

	
	/*// added for display the selected products in billing
	
		@RequestMapping(value="/viewworkorder",method=RequestMethod.POST)
		public @ResponseBody List<WorkorderDetails> getViewWorkorder(HttpServletRequest request){
			List<WorkorderDetails> viewDetails = new ArrayList<WorkorderDetails>();
			try{
				viewDetails = new ArrayList<WorkorderDetails>();
				
				    for (int i=0;i<productlist.size();i++){
				    	WorkorderDetails detail = new WorkorderDetails();
				    	if(productlist.get(i).getNoOfSheet()>0 && productlist.get(i).getProdNo() !=0){
				    	List<ProductType> existingproductName= metaDAO.getProductTypeName(productlist.get(i).getProductId());
				    	detail.setProdName(existingproductName.get(0).getName());
				    	detail.setNoofcopies(productlist.get(i).getNoOfCopy());
				    	List<ProductSize> existsProductSizes = metaDAO.getProductSizes((int)productlist.get(i).getProductTypeId());
				    	detail.setSize(existsProductSizes.get(0).getSize());
				    	detail.setNoofsheets(productlist.get(i).getNoOfSheet());
				    	if(detail!=null)
		  					viewDetails.add(detail);
				    	for (int j=0;j<productitemlist.size();j++){
				    		if(productitemlist.get(j).getProdNo() !=0){
				    			if (productitemlist.get(j).getProdNo()==productlist.get(i).getProdNo()){
				    				detail = new WorkorderDetails();
				    				List<ProductItemType> productItemTypes= metaDAO.getProductItemTypesByProductId((int)productitemlist.get(j).getProductId());
				    				detail.setItemname(productItemTypes.get(0).getProductName());
				    				detail.setQty(productitemlist.get(j).getQuantity());
				    				detail.setRate(productitemlist.get(j).getRate());
				    				detail.setAmount(productitemlist.get(j).getAmount());
				    				if(detail!=null)
				    					viewDetails.add(detail);
				    			  
				    				}
				    			}
				    		
				    		}
				    	}
				    }
					
				}catch (Exception _exception) {
				logger.error("getVeiewWorkorder::" + _exception);
			}
		
			return viewDetails;
		}
	// added for view the selected products in billing ENDS
		*/
		
	
	
	
	
	/*@RequestMapping(value="/detail",method=RequestMethod.POST)
	public @ResponseBody List<ReportDetail> getReport(HttpServletRequest request){
		List<ReportDetail> reportDetails = new ArrayList<ReportDetail>();
		try{
			Report report = new Report();
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			String type = request.getParameter("type");
			String rType = request.getParameter("report");
			
			if(!StringUtils.isEmpty(type)){
				report.setTypeId(Integer.valueOf(type));
				report.setFromDate(DateUtils.toDate(fromDate,true));
				report.setToDate(DateUtils.toDate(toDate,true));
				List<ReportDetail> details = reportService.voucher(report);
				if(details!=null && !details.isEmpty())
					reportDetails.addAll(details);
			}else{
				report.setFromDate(DateUtils.toDate(fromDate,true));
				report.setToDate(DateUtils.toDate(toDate,true));
				List<ReportDetail> details = null;
				
				if(!StringUtils.isEmpty(rType)){
					details = reportService.balance(report, OPENING);
					if(details!=null && !details.isEmpty())
						reportDetails.addAll(details);
				}
				
				details = reportService.order(report);
				if(details!=null && !details.isEmpty())
					reportDetails.addAll(details);
				
				details = reportService.supplier(report);
				if(details!=null && !details.isEmpty())
					reportDetails.addAll(details);
				
				details = reportService.voucher(report);
				if(details!=null && !details.isEmpty())
					reportDetails.addAll(details);
				
				details = reportService.receipt(report);
				if(details!=null && !details.isEmpty())
					reportDetails.addAll(details);
				
				if(!StringUtils.isEmpty(rType)){
					details = reportService.balance(report, CLOSING);
					if(details!=null && !details.isEmpty())
						reportDetails.addAll(details);
				}
				
			}
		}catch (Exception _exception) {
			logger.error("getReport::" + _exception);
		}
		return reportDetails;
	}

	
	*/
	
	@RequestMapping(value = "/dashboardTable", method = RequestMethod.GET)
	public  @ResponseBody List<Employee> dashboardTable(HttpServletRequest request){
			logger.info("---- dashboard TABLE :: POST ----");
			//Employee emp1=new Employee();
			List<Employee> employee1= new ArrayList<Employee>();
			employee1=userDAO.getEmployees();
			for(Employee emp:employee1)
				emp.setJobOpenCount(0);
			
			//System.out.println("EMPLOYEE::1 SIZE"+employee1.size());
			List<Employee> employee= new ArrayList<Employee>();
			
			try{
			List<JobAllocation> openedJobs= new ArrayList<JobAllocation>();
			openedJobs=null;
			List<Deportment> deportments = metaDAO.getDeportments();
			Map<Integer,String> departments = getDepartments(deportments);
			
			for(Employee emp:employee1){
				Employee emp1=new Employee();
				openedJobs=orderDAO.getJobAllocationByEmployeeId(emp.getEmpId());
			//	System.out.println("emp Id :"+emp.getEmpId()+"  count : "+openedJobs.size());
				if (openedJobs !=null){
					emp.setJobOpenCount(openedJobs.size());
				}
				if (emp.getJobOpenCount() >0){
					emp1.setName(emp.getName());
				    emp1.setCity(departments.get(emp.getDepotId()));
				    emp1.setJobOpenCount(emp.getJobOpenCount());
				    employee.add(emp1); 
			}
				
			}}
			catch(Exception e){logger.error("employee job open report::" + e);}
		//	System.out.println("EMPLOYEE SIZE"+employee.size());
		return employee;	
	}
	
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ModelAndView dashboard(ModelAndView  modelAndView){
		logger.info("---- dashboard :: GET ----");
		Dashboard dashboard = new Dashboard();
		/*List<Employee> employee1= new ArrayList<Employee>();
		employee1=userDAO.getEmployees();
		System.out.println("EMPLOYEE1 size :"+employee1.size());
		List<Employee> employee= new ArrayList<Employee>();
		employee=null;*/
		
		List<ReportDetail> reportDetails = new ArrayList<ReportDetail>();
		try{
		Report report = new Report();
		String fromDate = DateUtils.toDate(new Date(),true);
		String toDate = DateUtils.toDate(new Date(),true);
		report.setFromDate(DateUtils.toDate(fromDate,true));
		report.setToDate(DateUtils.toDate(toDate,true));
		reportDetails = reportService.mbrsummary(fromDate, toDate);
		
		int totalsale=0;
		int cashsale=0;
		int creditsale=0;
		int cashpurchase=0;
		int totalpurchase=0;
		int creditpurchase=0;
		int receipts=0;
		int payments=0;
		int totalsheet=0;
		try{
		for(int i=0;i<reportDetails.size();i++){
			 	 totalsale= (int)(reportDetails.get(i).getInvoiceAmount() !=null ?
				reportDetails.get(i).getInvoiceAmount():0); 
		 cashsale=(int)(reportDetails.get(i).getCashsale()>=0 ?
					reportDetails.get(i).getCashsale():0);
		 creditsale=(int)(reportDetails.get(i).getCreditsale()>=0 ?
			    	reportDetails.get(i).getCreditsale():0);
			 cashpurchase=(int)(reportDetails.get(i).getDebitAmount() !=null ?
				reportDetails.get(i).getDebitAmount():0);
		 totalpurchase=(int)(reportDetails.get(i).getTotalpurchase()>=0 ?
				reportDetails.get(i).getTotalpurchase():0);
		 creditpurchase=(totalpurchase-cashpurchase);
		 receipts=(int)(reportDetails.get(0).getCreditAmount() !=null ?
				reportDetails.get(0).getCreditAmount():0);
		 payments=(int)(reportDetails.get(0).getPayAmount() !=null ?
				reportDetails.get(0).getPayAmount():0);
			dashboard.setTotalsale(totalsale);
			dashboard.setCashsale(cashsale);
			dashboard.setCreditsale(creditsale);
			dashboard.setCashpurchase(cashpurchase);
			dashboard.setTotalpurchase(totalpurchase);
			dashboard.setCreditpurchase(creditpurchase);
			dashboard.setReceipts(receipts);
			dashboard.setPayments(payments);

		}
		}catch(Exception e){}
		ReportDetail reportDetails1 = new ReportDetail();
		reportDetails1 = reportService.dashboardOrderStatus(fromDate, toDate);
		List<Product> sheets = reportDAO.getActualSheets( fromDate,  toDate);
		   if (sheets !=null  && !sheets.isEmpty()){
				try {  
					totalsheet=0;
				  	for (Product products: sheets){
						if (products.getNoOfSheet() != null )																				
							totalsheet += (products.getNoOfSheet() * products.getNoOfCopy());
						}
						reportDetails1.setNoOfBills(totalsheet);
				}
				catch(Exception e){  }
		   	} 
		//System.out.println("order taken: "+reportDetails1.getBillNo());
		//System.out.println("order delivered: "+reportDetails1.getCustId());
		//System.out.println("order cancelled: "+reportDetails1.getDays());
		dashboard.setOrdertaken(reportDetails1.getBillNo());
		dashboard.setOrderdelivered(reportDetails1.getCustId());
		dashboard.setOrdercancelled(reportDetails1.getDays());
		dashboard.setOrderedsheets(reportDetails1.getNoOfBills()); 
		}catch(Exception _exception){logger.error("Dashboard::" + _exception);}
		
		modelAndView.addObject(DASHBOARD, dashboard);
		modelAndView.setViewName(DASHBOARD);
		return modelAndView;
	}
	
	
	@RequestMapping(value="/dailydetail",method=RequestMethod.POST)
	public @ResponseBody List<ReportDetail> getDailyReport(HttpServletRequest request){
		List<ReportDetail> reportDetails = new ArrayList<ReportDetail>();
		try{
			Report report = new Report();
//			String cHand = request.getParameter("chand");
//			String cBank = request.getParameter("cbank");
			String fromDate = DateUtils.toDate(new Date(),true);
			String toDate = DateUtils.toDate(new Date(),true);
			
			/*if(!StringUtils.isEmpty(cHand) || !StringUtils.isEmpty(cBank)){
				BalanceDetails bDetails = new BalanceDetails();
				if(!StringUtils.isEmpty(cHand))
				bDetails.setCashHand(Double.valueOf(cHand));
				if(!StringUtils.isEmpty(cBank))
				bDetails.setCashBank(Double.valueOf(cBank));
				bDetails.setCreatedDate(DateUtils.toDate(DateUtils.toDate(new Date(),true),true));
				BalanceDetails balanceDetails = reportService.getBalance(DateUtils.toDate(fromDate,true));
				if(balanceDetails == null)
					reportService.save(bDetails);
				else{
					if(!StringUtils.isEmpty(cHand))
					balanceDetails.setCashHand(Double.valueOf(cHand));
					if(!StringUtils.isEmpty(cBank))
					balanceDetails.setCashBank(Double.valueOf(cBank));
					reportService.saveOrUpdate(balanceDetails);
				}
			}*/
			
			report.setFromDate(DateUtils.toDate(fromDate,true));
			report.setToDate(DateUtils.toDate(toDate,true));
			
//			List<ReportDetail> details = reportService.balance(report, OPENING);
//			DailyReportDetail dailyReportDetail =null;
			
			/*if(details!=null && !details.isEmpty())
				reportDetails.addAll(details);*/
			
			reportDetails.add(reportService.getOpeningBalance());
			DailyReportDetail dailyReportDetail = reportService.order(report,true);
			
			if(dailyReportDetail.getReportDetails()!=null && !dailyReportDetail.getReportDetails().isEmpty())
				reportDetails.addAll(dailyReportDetail.getReportDetails());
			
			DailyReportDetail dailyReportDetail1 = reportService.supplier(report,true);
			if(dailyReportDetail1.getReportDetails()!=null && !dailyReportDetail1.getReportDetails().isEmpty())
				reportDetails.addAll(dailyReportDetail1.getReportDetails());
			
			dailyReportDetail.setInvoiceAmount(dailyReportDetail1.getInvoiceAmount());
			dailyReportDetail.setInvoicePaidAmount(dailyReportDetail1.getInvoicePaidAmount());
			dailyReportDetail.setStrInvoiceAmount(dailyReportDetail1.getStrInvoiceAmount());
			dailyReportDetail.setStrInvoicePaidAmount(dailyReportDetail1.getStrInvoicePaidAmount());
			
			DailyReportDetail dailyReportDetail2 = reportService.voucher(report,true);
			if(dailyReportDetail2.getReportDetails()!=null && !dailyReportDetail2.getReportDetails().isEmpty())
				reportDetails.addAll(dailyReportDetail2.getReportDetails());
			if(dailyReportDetail.getInvoicePaidAmount()!=null)
				dailyReportDetail.setInvoicePaidAmount(dailyReportDetail2.getInvoicePaidAmount()+dailyReportDetail.getInvoicePaidAmount());
			else
				dailyReportDetail.setInvoicePaidAmount(dailyReportDetail2.getInvoicePaidAmount());
			   
			dailyReportDetail.setOtherPayment(dailyReportDetail2.getOtherPayment());
			if(dailyReportDetail2.getInvoicePaidAmount()!=null)
				dailyReportDetail.setStrInvoicePaidAmount(com.studio.utils.StringUtils.decimalFormat(dailyReportDetail2.getInvoicePaidAmount()));
			dailyReportDetail.setStrOtherPayment(dailyReportDetail2.getStrOtherPayment());
			
			
			DailyReportDetail dailyReportDetail3 = reportService.receipt(report,true);
			if(dailyReportDetail3.getReportDetails()!=null && !dailyReportDetail3.getReportDetails().isEmpty())
				reportDetails.addAll(dailyReportDetail3.getReportDetails());
			
			dailyReportDetail.setReceiptAmount(dailyReportDetail3.getReceiptAmount());
			dailyReportDetail.setStrReceiptAmount(dailyReportDetail3.getStrReceiptAmount());
			
			SmsData data = reportService.smsData(report);

			Double crAmnt=0.0;
			Double drAmnt=0.0;
			if(!reportDetails.isEmpty()){
				for(ReportDetail rDetail:reportDetails){
					if(rDetail.getCreditAmount() != null)
						crAmnt += rDetail.getCreditAmount();
					if(rDetail.getDebitAmount()!= null)
						drAmnt += rDetail.getDebitAmount();
				}
			}
			data.setcHand(crAmnt-drAmnt);
			
			reportService.saveClosingBalance(reportDetails);
			//System.out.println("near DAily SMS");
			smsService.sendDailyTrxnSMS(data,dailyReportDetail);
			
		}catch (Exception _exception) {
			logger.error("getDailyReport::" + _exception);
		} 
		return reportDetails;
	}

	@RequestMapping(value="/selecteddaydetail",method=RequestMethod.POST)
	public @ResponseBody List<ReportDetail> getSelectedDayReport(HttpServletRequest request){
		List<ReportDetail> reportDetails = new ArrayList<ReportDetail>();
		try{
			Report report = new Report();
//			String cHand = request.getParameter("chand");
//			String cBank = request.getParameter("cbank");
//			String fromDate = DateUtils.toDate(new Date(),true);
//			String toDate = DateUtils.toDate(new Date(),true);
			String fromDate = request.getParameter("fromDate");
			//System.out.println("fromdate :"+ fromDate);
			String toDate = request.getParameter("fromDate");
			//String fromDate1 = orderDAO.getyyyymmdd(fromDate);
			//System.out.println("fromdate :"+ fromDate);
			SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy"); 
			//System.out.println("hello................");
			Date date1 = dateformat.parse(fromDate);
			//System.out.println("date 1 :"+ dateformat.format(date1));
			/*Date date2 = dateformat.parse(toDate);
			System.out.println("date 2 :"+ date2);*/
			Date date = Calendar.getInstance().getTime();   
			String today = dateformat.format(date);
			Date date3= dateformat.parse(today);
			//System.out.println("today :"+ dateformat.format(date3));
			/*if (date1.compareTo(date3) < 0) {
			    System.out.println("earlier");
			}
           
			if(date1.compareTo(date3) >= 0)
				 System.out.println("greater");*/
			/*if(!StringUtils.isEmpty(cHand) || !StringUtils.isEmpty(cBank)){
				BalanceDetails bDetails = new BalanceDetails();
				if(!StringUtils.isEmpty(cHand))
				bDetails.setCashHand(Double.valueOf(cHand));
				if(!StringUtils.isEmpty(cBank))
				bDetails.setCashBank(Double.valueOf(cBank));
				bDetails.setCreatedDate(DateUtils.toDate(DateUtils.toDate(new Date(),true),true));
				BalanceDetails balanceDetails = reportService.getBalance(DateUtils.toDate(fromDate,true));
				if(balanceDetails == null)
					reportService.save(bDetails);
				else{
					if(!StringUtils.isEmpty(cHand))
					balanceDetails.setCashHand(Double.valueOf(cHand));
					if(!StringUtils.isEmpty(cBank))
					balanceDetails.setCashBank(Double.valueOf(cBank));
					reportService.saveOrUpdate(balanceDetails);
				}
			}*/
			/* Date fromDate2=new SimpleDateFormat().parse(orderDAO.getyyyymmdd(date1));  */
		/*	 Date toDate2=new SimpleDateFormat().parse(toDate);  
*/			report.setFromDate(date1);
			report.setToDate(date1);
			
//			List<ReportDetail> details = reportService.balance(report, OPENING);
//			DailyReportDetail dailyReportDetail =null;
			
			/*if(details!=null && !details.isEmpty())
				reportDetails.addAll(details);*/
			
			reportDetails.add(reportService.getOpeningBalancewithDate(fromDate));
			DailyReportDetail dailyReportDetail = reportService.order(report,true);
			
		if(dailyReportDetail.getReportDetails()!=null && !dailyReportDetail.getReportDetails().isEmpty())
				reportDetails.addAll(dailyReportDetail.getReportDetails());
			
			DailyReportDetail dailyReportDetail1 = reportService.supplier(report,true);
			if(dailyReportDetail1.getReportDetails()!=null && !dailyReportDetail1.getReportDetails().isEmpty())
				reportDetails.addAll(dailyReportDetail1.getReportDetails());
			
			dailyReportDetail.setInvoiceAmount(dailyReportDetail1.getInvoiceAmount());
			dailyReportDetail.setInvoicePaidAmount(dailyReportDetail1.getInvoicePaidAmount());
			dailyReportDetail.setStrInvoiceAmount(dailyReportDetail1.getStrInvoiceAmount());
			dailyReportDetail.setStrInvoicePaidAmount(dailyReportDetail1.getStrInvoicePaidAmount());
			
			DailyReportDetail dailyReportDetail2 = reportService.voucher(report,true);
			if(dailyReportDetail2.getReportDetails()!=null && !dailyReportDetail2.getReportDetails().isEmpty())
				reportDetails.addAll(dailyReportDetail2.getReportDetails());
			if(dailyReportDetail.getInvoicePaidAmount()!=null)
				dailyReportDetail.setInvoicePaidAmount(dailyReportDetail2.getInvoicePaidAmount()+dailyReportDetail.getInvoicePaidAmount());
			else
				dailyReportDetail.setInvoicePaidAmount(dailyReportDetail2.getInvoicePaidAmount());
			   
			dailyReportDetail.setOtherPayment(dailyReportDetail2.getOtherPayment());
			if(dailyReportDetail2.getInvoicePaidAmount()!=null)
				dailyReportDetail.setStrInvoicePaidAmount(com.studio.utils.StringUtils.decimalFormat(dailyReportDetail2.getInvoicePaidAmount()));
			dailyReportDetail.setStrOtherPayment(dailyReportDetail2.getStrOtherPayment());
			
			
			DailyReportDetail dailyReportDetail3 = reportService.receipt(report,true);
			if(dailyReportDetail3.getReportDetails()!=null && !dailyReportDetail3.getReportDetails().isEmpty())
				reportDetails.addAll(dailyReportDetail3.getReportDetails());
			
			dailyReportDetail.setReceiptAmount(dailyReportDetail3.getReceiptAmount());
			dailyReportDetail.setStrReceiptAmount(dailyReportDetail3.getStrReceiptAmount());
			
			//SmsData data = reportService.smsData(report);

			Double crAmnt=0.0;
		 	Double drAmnt=0.0;
			if(!reportDetails.isEmpty()){
				for(ReportDetail rDetail:reportDetails){
					if(rDetail.getCreditAmount() != null)
						crAmnt += rDetail.getCreditAmount();
					if(rDetail.getDebitAmount()!= null)
						drAmnt += rDetail.getDebitAmount();
				}
			}
			//data.setcHand(crAmnt-drAmnt);
			//reportService.saveClosingBalance(reportDetails);
			//smsService.sendDailyTrxnSMS(data,dailyReportDetail);
			
		}catch (Exception _exception) {
			logger.error("getDailyReport::" + _exception);
		} 
		return reportDetails;
	}
	
	
	@RequestMapping(value="/agent",method = RequestMethod.GET)
	public ModelAndView reportAgent(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.PAGE_AGENT_REPORT.value());
		Report report= new Report();
		List<ExpenseType> expenseTypes = metaDAO.getExpenseTypes();
		modelAndView.addObject(Payment.EXPENSE_TYPES.value(), expenseTypes);
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	@RequestMapping(value="/agent-remoteAccess",method = RequestMethod.GET)
	public ModelAndView reportRemoteAgent(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.PAGE_AGENT_REPORT_REMOTE.value() );
		Report report= new Report();
		//report.setCustId(Long.parseLong(aId));
		List<ExpenseType> expenseTypes = metaDAO.getExpenseTypes();
		modelAndView.addObject(Payment.EXPENSE_TYPES.value(), expenseTypes);
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	
	@RequestMapping(value="/adetail",method=RequestMethod.POST)
	public @ResponseBody List<AgentReportDetail> getAgentReport(HttpServletRequest request){
		List<AgentReportDetail> reportDetails = new ArrayList<AgentReportDetail>();
		try{
			Report report = new Report();
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			String agentId = request.getParameter("aId");
			Employee employee = null;;
			if(agentId!=null)
				employee = userDAO.getEmployeeById(Long.valueOf(agentId));
			if(employee == null)
			logger.info("No agent found to the id : " + agentId);
				report.setFromDate(DateUtils.toDate(fromDate,true));
				report.setToDate(DateUtils.toDate(toDate,true));
				report.setName(employee.getName());
				List<AgentReportDetail> details = reportService.agentOrders(report);
				if(details!=null && !details.isEmpty())
					reportDetails.addAll(details);
				
		}catch (Exception _exception) { 
			logger.error("getReport::" + _exception);
		}
		return reportDetails;
	}
	
	
	@RequestMapping(value="/adetail_remoteAccess",method=RequestMethod.POST)
	public @ResponseBody List<AgentReportDetail> getAgentReport_remote(HttpServletRequest request){
		List<AgentReportDetail> reportDetails = new ArrayList<AgentReportDetail>();
		try{
			Report report = new Report();
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			
			/*String agentId = cookies.getCookie(request, "empid");
			System.out.println("Agent name:"+ agentId);*/
			String agentId = cookies.getRoleCookie(request, "empid");
			System.out.println("Agent role:"+ agentId);
			
			Employee employee = null;;
			if(agentId!=null)
				employee = userDAO.getEmployeeById(Long.valueOf(agentId));
			System.out.println("Employee namw:"+ employee.getName());
			if(employee == null)
			logger.info("No agent found to the id : " + agentId);
				report.setFromDate(DateUtils.toDate(fromDate,true));
				report.setToDate(DateUtils.toDate(toDate,true));
				report.setName(employee.getName());
				List<AgentReportDetail> details = reportService.agentOrders(report);
				if(details!=null && !details.isEmpty())
					reportDetails.addAll(details);
				
		}catch (Exception _exception) { 
			logger.error("getReport::" + _exception);
		}
		return reportDetails;
	}
	
	@RequestMapping(value="/agentcorrection",method = RequestMethod.GET)
	public ModelAndView reportAgentCorrection(){
		ModelAndView modelAndView = new ModelAndView(ReportConstants.PAGE_AGENT_REPORT_CORRECTION.value());
		Report report= new Report();
		List<ExpenseType> expenseTypes = metaDAO.getExpenseTypes();
		modelAndView.addObject(Payment.EXPENSE_TYPES.value(), expenseTypes);
		modelAndView.addObject(ReportConstants.REPORT.value(), report);
		return modelAndView;
	}
	
	@RequestMapping(value="/adcorrection",method=RequestMethod.POST)
	public @ResponseBody List<AgentReportDetail> getAgentReportCorrection(HttpServletRequest request){
		//System.out.println("HEllo77777777777777777777777777");
		List<AgentReportDetail> reportDetails = new ArrayList<AgentReportDetail>();
		
		try{
			
			Report report = new Report();
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			/*String agentId = request.getParameter("aId");*/
			/*Employee employee = null;;
			if(agentId!=null)
				employee = userDAO.getEmployeeById(Long.valueOf(agentId));
			if(employee == null)
			logger.info("No agent found to the id : " + agentId);*/
				report.setFromDate(DateUtils.toDate(fromDate,true));
				report.setToDate(DateUtils.toDate(toDate,true));
				/*report.setName(employee.getName());*/
				List<AgentReportDetail> details = reportService.agentOrdersCorrection(report);
				if(details!=null && !details.isEmpty())
					reportDetails.addAll(details);
				
		}catch (Exception _exception) { 
			logger.error("getAgentReportCorrection::" + _exception);
			
		}
		return reportDetails;
	}
	
	
@RequestMapping(value="/cancel_order_report",method=RequestMethod.POST)
	public @ResponseBody List<ReportDetail> getCancelledOrderReport(HttpServletRequest request){
		List<ReportDetail> reportDetails = new ArrayList<ReportDetail>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		List<OrderTrxnDetail> canceldetails = new ArrayList<OrderTrxnDetail>();
		try{
			Report report = new Report();
			String fromDate =  request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			String custId = request.getParameter("custid");
			//System.out.println("cust Id :"+ custId);
			
				report.setFromDate((DateUtils.toDate(fromDate,true)));
				report.setToDate((DateUtils.toDate(toDate,true)));
				report.setCustId(Long.valueOf(custId));
				//report.setCustId(Long.parseLong(request.getParameter("custid")));
				
			   
				List<Order> orders  = reportDAO.getCancelOrdersList_working(report);
				//System.out.println("canelled order size :"+orders.size());
				List<Long> orderIds = Utils.getOrdersIds(orders);
				canceldetails = reportService.cancelledOrderDetails(report, orderIds);
				//System.out.println(" canceldetails :"+canceldetails.size());
				/* for (int j=0; j<canceldetails.size();j++){
					 	System.out.println("orderid :"+canceldetails.get(j).getOrderId());
						System.out.println(canceldetails.get(j).getNetAmount());
						System.out.println(canceldetails.get(j).getAdvance());
						System.out.println(canceldetails.get(j).getBalance());
						
					 
				 }*/
				
				for (int i=0; i<orders.size();i++){
					ReportDetail rd= new ReportDetail();
					try{
						for(int j=0;j<canceldetails.size();j++){
							//System.out.println("ORDERID from ORD DETAIL:"+canceldetails.get(j).getOrderId());
						//	System.out.println("orders from order:"+orders.get(i).getOrderId());
						 if (orders.get(i).getOrderId() == canceldetails.get(j).getOrderId()){
						//	 System.out.println("TRUE===========");
								
							rd.setChqDate(orders.get(i).getStrOrderDate());
							rd.setOrderId(orders.get(i).getOrderId());
							
							rd.setInvoiceAmount(canceldetails.get(j).getNetAmount());
							
							if(canceldetails.get(j).getAdvance()!=null){
							rd.setCashsale(canceldetails.get(j).getAdvance());
							}else{
								rd.setCashsale(0.0);
							}
							//System.out.println("reportDetails getbalance  :"+canceldetails.get(j).getBalance());
							rd.setBalance(canceldetails.get(j).getBalance());
							if(orders.get(i).getUpdatedDate() !=null ){
							String updateddate=orders.get(i).getUpdatedDate().toString();
						//	System.out.println("updateddate:"+updateddate);
							updateddate=reportDAO.getddmmyyyy(updateddate); 
							rd.setInvoiceDate(updateddate);
							}
							reportDetails.add(rd);
						//	System.out.println("reportDetails SIZE :"+reportDetails.size());
						}
						}
						 
					}catch(Exception e){}
				}
								
		}catch (Exception _exception) {
			logger.error("get cancelled Order Report ::" + _exception);
			_exception.printStackTrace();
			
		}
	//	System.out.println("TOTAL :"+reportDetails.size());
		return reportDetails;
}
		
// testing from existing one for debug.....purpose

@RequestMapping(value="/cdetail",method=RequestMethod.POST)

public @ResponseBody List<ReportDetail> getCustomerReport(HttpServletRequest request){
	List<ReportDetail> reportDetails = new ArrayList<ReportDetail>();
	List<ReportDetail> s = new ArrayList<ReportDetail>();
	try{
		Report report = new Report();
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String custId = request.getParameter("custid");
					
			report.setFromDate((DateUtils.toDate(fromDate,true)));
			report.setToDate((DateUtils.toDate(toDate,true)));
			report.setCustId(Long.valueOf(custId));
			List<ReportDetail> details=null;
			List<ReportDetail> canceldetails=null;
			
// added newly for opening balance
			
			List<ReportDetail> details1=null;
			List<Receipt> receiptDetails=null;
			List<OrderTrxnDetail> trxndetails=null;
			 Double payamount=0.0;
			 Double crpayment=0.0;
			 Double balancepayment=0.0;
			 Double openingbalance=0.0;
			 Double trxnbalance=0.0;
		     String customername=null;
		     Double netAmount=0.00;
		     Double advance=0.00;
		     Double canceledordersAdvance=0.00;
		   
		     
		/*   List<ReportDetail> rd = new ArrayList<ReportDetail>();
		   rd=combinedQuery.orderDetails();
		    System.out.println("RD.SIZE :"+rd.size());*/
		    
		     crpayment =  paymentDAO.getOSReceiptByCustomer(report);
		     System.out.println("Receipt before Fdate :"+crpayment);
		     if(crpayment==null)
		    	 crpayment=0.00;
		  // System.out.println("receiptDetails :"+receiptDetails.size());
		    /*if (receiptDetails !=null ){
				 for (Receipt receipt : receiptDetails) {
					 payamount=receipt.getPayAmount();
					 crpayment=crpayment+payamount;
				 }
			}*/
			
			trxndetails=  orderDAO.getOrderTrxnBalance(report);
			/*for (OrderTrxnDetail trxnDetail: trxndetails) {
				System.out.println("Selected Order Ids:"+trxnDetail.getOrderId());
			}*/
			if (trxndetails !=null){
				 for (OrderTrxnDetail trxnDetail: trxndetails) {
					/* trxnbalance=trxnDetail.getBalance();*/
				// System.out.println("Balance of transaction is :"+trxnbalance);
					 netAmount += trxnDetail.getNetAmount();
					 if (netAmount ==null)
						 netAmount=0.00;
					 
					 if(trxnDetail.getAdvance()!=null)
						 advance +=trxnDetail.getAdvance();
					 if (advance ==null)
						 advance=0.00;
					 //balancepayment=balancepayment+trxnbalance;
					 }
				} 
		/* To get cancelled  orders advance amount */
			// canceledordersAdvance = reportDAO.getOBCancelOrdersList(report);
			//System.out.println("cancelled orders amount:"+canceledordersAdvance);
			//if(canceledordersAdvance==null){
			//	openingbalance=netAmount-(advance+crpayment);}
			//else {
				openingbalance=netAmount-(advance+crpayment);	
			//}
			     System.out.println("OPENING BALANCE :"+openingbalance);
		/* arriving  opening balance */	
				
			//openingbalance= balancepayment-crpayment;	
			//System.out.println(openingbalance);
//Ending newly added for opening balance 
			
			//System.out.println("now its ok");
			List<Order> orders = reportDAO.getOrdersList(report);
			List<Long> orderIds = Utils.getOrdersIds(orders);
			details = reportService.orderByIdsforCustomer(report, orderIds);
	/* for Cancelled orders Advance pickup*/		
			//List<Order> canceledorders = reportDAO.getCancelOrdersList(report);
			//System.out.println("cancelled orders :"+canceledorders.get(0).getOrderId());
			//List<Long> cancelOrderIds = Utils.getOrdersIds(canceledorders);
			//canceldetails = reportService.cancelledOrderByIdsforCustomer(report, cancelOrderIds);
			//System.out.println("canceldetails final size:"+canceldetails.size());;
			//if(canceldetails!=null && !canceldetails.isEmpty())
			//	reportDetails.addAll(canceldetails);	
		
			if(details!=null && !details.isEmpty())
				reportDetails.addAll(details);				
			details = reportService.receiptforCustomer(report);
			double receiptAmt=0.0;
			for (int i =0;i<details.size();i++){
				if (details.get(i).getCreditAmount() !=null)
				receiptAmt+=details.get(i).getCreditAmount();	
				}
			if(details!=null && !details.isEmpty()){
				reportDetails.addAll(details);
			}
			trxndetails=orderDAO.getOrderTrxnFromDateTOEndDate(report);
			if (trxndetails !=null){
				 for (OrderTrxnDetail trxnDetail: trxndetails) {
					/* trxnbalance=trxnDetail.getBalance();*/
				// System.out.println("Balance of transaction is :"+trxnbalance);
					 netAmount += trxnDetail.getNetAmount();
					 if (netAmount ==null)
						 netAmount=0.00;
					 
					 if(trxnDetail.getAdvance()!=null)
						 advance +=trxnDetail.getAdvance();
					 if (advance ==null)
						 advance=0.00;
					 //balancepayment=balancepayment+trxnbalance;
					 }
				} 
			Double closingbalance=netAmount-(advance+receiptAmt);	
			//}
			     System.out.println("Closing BALANCE :"+closingbalance);
		/* arriving  opening balance */	
			
			Customer customer = userDAO.getCustomerById(Long.valueOf(custId)); 
			//System.out.println(customer.getFirstName());
												
			if(customer != null && !reportDetails.isEmpty()){
				reportDetails.get(reportDetails.size()-1).setInvoiceAmount(customer.getCreditBalance());
			    reportDetails.get(reportDetails.size()-1).setOpeningBalance(openingbalance);
			    for (int i=0;i<=reportDetails.size()-1;i++){
					reportDetails.get(i).setCustomerName(customer.getFirstName());	
				}
			}
		
	}catch (Exception _exception) {
		logger.error("getReport::" + _exception);
		_exception.printStackTrace();
		
	}

/*	for (ReportDetail s1 : reportDetails)
	{
	    System.out.println("s1.getTrxnDate() :"+s1.getTrxnDate());
	}
	
	int i=0;
	for (ReportDetail s2 : reportDetails)
	{
	   
	   s.add(s2 );
	   System.out.println("s.get(i).getTrxnDate()--> :"+s.get(i).getTrxnDate());
	   System.out.println("s.get(i).getOrderId()--> :"+s.get(i).getOrderId());
	   System.out.println("s.get(i).getTrxnType()--> :"+s.get(i).getTrxnType());
	   System.out.println("s.get(i).getDebitAmount()--> :"+s.get(i).getDebitAmount());
	   System.out.println("s.get(i).getCreditAmount()--> :"+s.get(i).getCreditAmount());
	   System.out.println("s.get(i).getInvoiceAmount()--> :"+s.get(i).getInvoiceAmount());
	   System.out.println("s.get(i).getOpeningBalance()--> :"+s.get(i).getOpeningBalance());
	   System.out.println("s.get(i).getBillNo()--> :"+s.get(i).getBillNo());
	   System.out.println("s.get(i).getTrxnType()--> :"+s.get(i).getTrxnType());
	   System.out.println("s.get(i).getCustomerName()--> :"+s.get(i).getCustomerName());
	   i++;
	   
	}*/
	//System.out.println("size Before sorting :"+s.size());
	Collections.sort(reportDetails,new Comparator<ReportDetail>(){
		 DateFormat f = new SimpleDateFormat("dd/MM/yyyy");	
		public int compare(ReportDetail o1, ReportDetail o2) {
			try{
			return f.parse(o1.getTrxnDate()).compareTo(f.parse(o2.getTrxnDate()));
				//return (o1.getTrxnDate1()).compareTo((o2.getTrxnDate1()));
			
			}catch(ParseException e){
                throw new IllegalArgumentException(e);
            }
		}	
		
	    });
	
	//Collections.addAll(s);
	
	//System.out.println("size after sorting:"+s.size());
	/*List<ReportDetail> s1 = reportDetails.stream()
			  .collect(Collectors.toList());
	System.out.println("size :"+s1.size());*/

	//reportDetails.clear();
	
	return reportDetails;
}
	
	/* 
	@RequestMapping(value="/cdetail",method=RequestMethod.POST)
	
	public @ResponseBody List<ReportDetail> getCustomerReport(HttpServletRequest request){
		List<ReportDetail> reportDetails = new ArrayList<ReportDetail>();
		List<ReportDetail> s = new ArrayList<ReportDetail>();
		try{
			Report report = new Report();
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			String custId = request.getParameter("custid");
						
				report.setFromDate((DateUtils.toDate(fromDate,true)));
				report.setToDate((DateUtils.toDate(toDate,true)));
				report.setCustId(Long.valueOf(custId));
				List<ReportDetail> details=null;
				List<ReportDetail> canceldetails=null;
				
	// added newly for opening balance
				
				List<ReportDetail> details1=null;
				List<Receipt> receiptDetails=null;
				List<OrderTrxnDetail> trxndetails=null;
				 Double payamount=0.0;
				 Double crpayment=0.0;
				 Double balancepayment=0.0;
				 Double openingbalance=0.0;
				 Double trxnbalance=0.0;
			     String customername=null;
			     Double netAmount=0.00;
			     Double advance=0.00;
			     Double canceledordersAdvance=0.00;
			   
			     
			   List<ReportDetail> rd = new ArrayList<ReportDetail>();
			   rd=combinedQuery.orderDetails();
			    System.out.println("RD.SIZE :"+rd.size());
			    
			     crpayment =  paymentDAO.getOSReceiptByCustomer(report);
			     System.out.println("Receipt before Fdate :"+crpayment);
			     if(crpayment==null)
			    	 crpayment=0.00;
			  // System.out.println("receiptDetails :"+receiptDetails.size());
			    if (receiptDetails !=null ){
					 for (Receipt receipt : receiptDetails) {
						 payamount=receipt.getPayAmount();
						 crpayment=crpayment+payamount;
					 }
				}
				
				trxndetails=  orderDAO.getOrderTrxnBalance(report);
				for (OrderTrxnDetail trxnDetail: trxndetails) {
					System.out.println("Selected Order Ids:"+trxnDetail.getOrderId());
				}
				if (trxndetails !=null){
					 for (OrderTrxnDetail trxnDetail: trxndetails) {
						 trxnbalance=trxnDetail.getBalance();
					// System.out.println("Balance of transaction is :"+trxnbalance);
						 netAmount += trxnDetail.getNetAmount();
						 if (netAmount ==null)
							 netAmount=0.00;
						 
						 if(trxnDetail.getAdvance()!=null)
							 advance +=trxnDetail.getAdvance();
						 if (advance ==null)
							 advance=0.00;
						 //balancepayment=balancepayment+trxnbalance;
						 }
					} 
			 To get cancelled  orders advance amount 
				// canceledordersAdvance = reportDAO.getOBCancelOrdersList(report);
				//System.out.println("cancelled orders amount:"+canceledordersAdvance);
				//if(canceledordersAdvance==null){
				//	openingbalance=netAmount-(advance+crpayment);}
				//else {
					openingbalance=netAmount-(advance+crpayment);	
				//}
				     System.out.println("OPENING BALANCE :"+openingbalance);
			 arriving  opening balance 	
					
				//openingbalance= balancepayment-crpayment;	
				//System.out.println(openingbalance);
	//Ending newly added for opening balance 
				
				//System.out.println("now its ok");
				List<Order> orders = reportDAO.getOrdersList(report);
				List<Long> orderIds = Utils.getOrdersIds(orders);
				details = reportService.orderByIdsforCustomer(report, orderIds);
		 for Cancelled orders Advance pickup		
				//List<Order> canceledorders = reportDAO.getCancelOrdersList(report);
				//System.out.println("cancelled orders :"+canceledorders.get(0).getOrderId());
				//List<Long> cancelOrderIds = Utils.getOrdersIds(canceledorders);
				//canceldetails = reportService.cancelledOrderByIdsforCustomer(report, cancelOrderIds);
				//System.out.println("canceldetails final size:"+canceldetails.size());;
				//if(canceldetails!=null && !canceldetails.isEmpty())
				//	reportDetails.addAll(canceldetails);	
			
				if(details!=null && !details.isEmpty())
					reportDetails.addAll(details);				
				details = reportService.receiptforCustomer(report);
				if(details!=null && !details.isEmpty()){
					reportDetails.addAll(details);
				}
				Customer customer = userDAO.getCustomerById(Long.valueOf(custId)); 
				//System.out.println(customer.getFirstName());
													
				if(customer != null && !reportDetails.isEmpty()){
					reportDetails.get(reportDetails.size()-1).setInvoiceAmount(customer.getCreditBalance());
				    reportDetails.get(reportDetails.size()-1).setOpeningBalance(openingbalance);
				    for (int i=0;i<=reportDetails.size()-1;i++){
						reportDetails.get(i).setCustomerName(customer.getFirstName());	
					}
				}
			
		}catch (Exception _exception) {
			logger.error("getReport::" + _exception);
			_exception.printStackTrace();
			
		}
	
		for (ReportDetail s1 : reportDetails)
		{
		    System.out.println("s1.getTrxnDate() :"+s1.getTrxnDate());
		}
		
		int i=0;
		for (ReportDetail s2 : reportDetails)
		{
		   
		   s.add(s2 );
		   System.out.println("s.get(i).getTrxnDate()--> :"+s.get(i).getTrxnDate());
		   System.out.println("s.get(i).getOrderId()--> :"+s.get(i).getOrderId());
		   System.out.println("s.get(i).getTrxnType()--> :"+s.get(i).getTrxnType());
		   System.out.println("s.get(i).getDebitAmount()--> :"+s.get(i).getDebitAmount());
		   System.out.println("s.get(i).getCreditAmount()--> :"+s.get(i).getCreditAmount());
		   System.out.println("s.get(i).getInvoiceAmount()--> :"+s.get(i).getInvoiceAmount());
		   System.out.println("s.get(i).getOpeningBalance()--> :"+s.get(i).getOpeningBalance());
		   System.out.println("s.get(i).getBillNo()--> :"+s.get(i).getBillNo());
		   System.out.println("s.get(i).getTrxnType()--> :"+s.get(i).getTrxnType());
		   System.out.println("s.get(i).getCustomerName()--> :"+s.get(i).getCustomerName());
		   i++;
		   
		}
		//System.out.println("size Before sorting :"+s.size());
		Collections.sort(reportDetails,new Comparator<ReportDetail>(){
			 DateFormat f = new SimpleDateFormat("dd/MM/yyyy");	
			public int compare(ReportDetail o1, ReportDetail o2) {
				try{
				return f.parse(o1.getTrxnDate()).compareTo(f.parse(o2.getTrxnDate()));
					//return (o1.getTrxnDate1()).compareTo((o2.getTrxnDate1()));
				
				}catch(ParseException e){
	                throw new IllegalArgumentException(e);
	            }
			}	
			
		    });
		
		//Collections.addAll(s);
		
		//System.out.println("size after sorting:"+s.size());
		List<ReportDetail> s1 = reportDetails.stream()
				  .collect(Collectors.toList());
		System.out.println("size :"+s1.size());
	
		//reportDetails.clear();
		
		return reportDetails;
	}*/
	
	@RequestMapping(value="/cosdetail-test",method=RequestMethod.POST)
	public @ResponseBody List<ReportDetail> getCustomerSummaryReportTest(HttpServletRequest request){
	List<ReportDetail> reportDetails = new ArrayList<ReportDetail>();
	List<ReportDetail> s = new ArrayList<ReportDetail>();
	double bfdAdvance=0.00;
	double bfdNetAmount=0.00; 
	double bfdReceipt=0.00;
	double outstanding=0.00;
	double balance=0.00;
	double invAmount=0.00;
	double receipt=0.00;
	double advance=0.00;
	try{
		Report report = new Report();
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
								
			report.setFromDate((DateUtils.toDate(fromDate,true)));
			report.setToDate((DateUtils.toDate(toDate,true)));
			//System.out.println("report date format:"+report.getFromDate());
			//System.out.println("report date format:"+report.getToDate());
			List<ReportDetail> details=null;
			List<ReportDetail> detailsBFD=null;
//			List <Customer> cd =  customerDAO.getCustomerInformation();
			List <Customer> cd1 =  customerDAO.getCustomerInformationForSelectedDate(fromDate,toDate);
// Getting Address table information
//			List<Long> custIds = Utils.getCustomersIds(cd);
			List<Long> custIds = Utils.getCustomersIds(cd1);
			//List<Address> address = customerDAO.getAddressByCustId(custIds);
			// Getting area table information
			//List<Integer> areaIds = Utils.getAreaIds(address);
			//List<Area> area = customerDAO.getAreaByAreaId(areaIds);
			//reportDetails = new ArrayList<ReportDetail>();
			ReportDetail cdetail= new ReportDetail();
			if (cd1 != null && !cd1.isEmpty()){	
				for (Customer customers: cd1){
					cdetail= new ReportDetail();
					cdetail.setCustId(customers.getCustId());
					cdetail.setFirstName(customers.getFirstName().toUpperCase());
					cdetail.setMobileNo(customers.getMobileNo());
					/*for (Address addresses: address){
					 for (Area areas: area){
						if (customers.getCustId()==addresses.getCustId() && areas.getId()==addresses.getAreaId()){
							cdetail.setAddress2(addresses.getAddress2());
							cdetail.setArea(areas.getName());				
						}
					   }
				   	}*/
				reportDetails.add(cdetail);
				for(int i =0;reportDetails.size()>0;i++) {
					System.out.println("reportDetails===="+reportDetails.get(i));
				}
				
	 			}
			}
			
			 reportDetails = reportDAO.getCOSOrderDetails(report, reportDetails );	
		
	}catch (Exception _exception) {
		logger.error("getReport::" + _exception);
		_exception.printStackTrace();
		
		}
	return reportDetails;
	}
	
	
	
@RequestMapping(value="/cosdetail",method=RequestMethod.POST)
		public @ResponseBody List<ReportDetail> getCustomerSummaryReport(HttpServletRequest request){
		List<ReportDetail> reportDetails = new ArrayList<ReportDetail>();
		List<ReportDetail> s = new ArrayList<ReportDetail>();
		double bfdAdvance=0.00;
		double bfdNetAmount=0.00; 
		double bfdReceipt=0.00;
		double outstanding=0.00;
		double balance=0.00;
		double invAmount=0.00;
		double receipt=0.00;
		double advance=0.00;
		double totalAmount=0.00;
		try{
			Report report = new Report();
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
									
				report.setFromDate((DateUtils.toDate(fromDate,true)));
				report.setToDate((DateUtils.toDate(toDate,true)));
				//System.out.println("report date format:"+report.getFromDate());
				//System.out.println("report date format:"+report.getToDate());
				List<ReportDetail> details=null;
				List<ReportDetail> detailsBFD=null;
				List <Customer> cd =  customerDAO.getCustomerInformation();
	// Getting Address table information
				List<Long> custIds = Utils.getCustomersIds(cd);
				//List<Address> address = customerDAO.getAddressByCustId(custIds);
				// Getting area table information
				//List<Integer> areaIds = Utils.getAreaIds(address);
				//List<Area> area = customerDAO.getAreaByAreaId(areaIds);
				//reportDetails = new ArrayList<ReportDetail>();
				ReportDetail cdetail= new ReportDetail();
				if (cd != null && !cd.isEmpty()){	
					for (Customer customers: cd){
						cdetail= new ReportDetail();
						cdetail.setCustId(customers.getCustId());
						cdetail.setFirstName(customers.getFirstName());
						cdetail.setMobileNo(customers.getMobileNo());
						/*for (Address addresses: address){
						 for (Area areas: area){
							if (customers.getCustId()==addresses.getCustId() && areas.getId()==addresses.getAreaId()){
								cdetail.setAddress2(addresses.getAddress2());
								cdetail.setArea(areas.getName());				
							}
						   }
					   	}*/
					reportDetails.add(cdetail);
					
		 			}
				}
				
				 //reportDetails = reportDAO.getCOSOrderDetails(report, reportDetails );
				 
				 

				//System.out.println("hello");
				for (int i=0; i<reportDetails.size();i++){
				//for (int i=0; i<10;i++){
					 bfdAdvance=0.00;
					 bfdNetAmount=0.00;
					 bfdReceipt=0.00;
					 outstanding=0.00;
					 balance=0.00;
					 invAmount=0.00;
					 receipt=0.00;
					 advance=0.00;
					 totalAmount = 0.00;
					try{
						cdetail= new ReportDetail();
					// get customer IDs
						report.setCustId(reportDetails.get(i).getCustId());
					// get order for the period
						
						
						
						List<Order> orders = reportDAO.getCOSOrderList(report);
						
						
						
					//	System.out.println("my order size is :" + orders.size());
					// get order IDs and netAmount for the period
						if(orders.size()>0){
							List<Long> orderIds = Utils.getOrdersIds(orders);
							if(orderIds !=null && !orderIds.isEmpty()){
								details = reportService.customerOSRByOrderIds(report, orderIds);
							//	System.out.println("DETAILS SIZE:"+ details.size());
							//	System.out.println("CHECK 1");
								if (details !=null && !details.isEmpty()){
									invAmount=details.get(0).getInvoiceAmount() !=null? details.get(0).getInvoiceAmount():0.00;
									advance=(Double)details.get(0).getCashsale() != null? details.get(0).getCashsale():0.00;
									reportDetails.get(i).setInvoiceAmount(invAmount);
							//		System.out.println("true");
									totalAmount = totalAmount+invAmount;
								}
								}
						       }else{
						    	   invAmount=0.00;	
						    	   advance=0.00;
						    	   reportDetails.get(i).setInvoiceAmount(invAmount);
						    	//   System.out.println("false");
						       }
							details = reportService.getCOSReceipt(report);
						//	System.out.println("DETAILS -1 SIZE:"+ details.size());
						//	System.out.println("my order size is :@@@@  CHECK");
							receipt=details.get(0).getPayAmount()+advance;
							if(details!=null && !details.isEmpty())
								reportDetails.get(i).setTotalReceipt(details.get(0).getPayAmount()+advance);
					/* TO FIND OUTSTANDING - BEFORE FROM DATE ORDER DETAILS */
							List<Order> ordersBeforeDate = reportDAO.getCOSOrdersBeforeDate(report);
							if (ordersBeforeDate !=null && !ordersBeforeDate.isEmpty()){
								List<Long> beforeDateOrderIds = Utils.getOrdersIds(ordersBeforeDate);
								if (beforeDateOrderIds !=null && !beforeDateOrderIds.isEmpty()){
									detailsBFD = reportService.customerOSRByOrderIds(report, beforeDateOrderIds);
									if (detailsBFD !=null && !detailsBFD.isEmpty()){
										bfdAdvance=detailsBFD.get(0).getCashsale();
										bfdNetAmount=detailsBFD.get(0).getInvoiceAmount();
									}
									else{
										bfdAdvance=0.00;
										bfdNetAmount=0.00;
									}
								}
							}
							details = reportService.getCOSBFDReceipt(report);
							if(details!=null && !details.isEmpty())
								bfdReceipt=details.get(0).getPayAmount();
							outstanding=bfdNetAmount-(bfdAdvance+bfdReceipt);
							reportDetails.get(i).setOpeningBalance(outstanding);
							balance=(outstanding+invAmount)-receipt;
							reportDetails.get(i).setBalance(balance);
				//System.out.println("result:"+reportDetails.get(i).getInvoiceAmount()+":::"+reportDetails.get(i).getCashsale()+":::"+
						//+reportDetails.get(i).getTotalReceipt());
								}catch (Exception _exception) {
							logger.error("getReport::" + _exception);
							_exception.printStackTrace();
						}
				}
			}catch (Exception _exception) {
			logger.error("getReport::" + _exception);
			_exception.printStackTrace();
			
			}
		/*	for (ReportDetail s1 : reportDetails)
		{
		    System.out.println("s1.getTrxnDate() :"+s1.getTrxnDate());
		}
		
		int i=0;
		for (ReportDetail s2 : reportDetails)
		{
		   
		   s.add(s2 );
		   System.out.println("s.get(i).getTrxnDate()--> :"+s.get(i).getTrxnDate());
		   System.out.println("s.get(i).getOrderId()--> :"+s.get(i).getOrderId());
		   System.out.println("s.get(i).getTrxnType()--> :"+s.get(i).getTrxnType());
		   System.out.println("s.get(i).getDebitAmount()--> :"+s.get(i).getDebitAmount());
		   System.out.println("s.get(i).getCreditAmount()--> :"+s.get(i).getCreditAmount());
		   System.out.println("s.get(i).getInvoiceAmount()--> :"+s.get(i).getInvoiceAmount());
		   System.out.println("s.get(i).getOpeningBalance()--> :"+s.get(i).getOpeningBalance());
		   System.out.println("s.get(i).getBillNo()--> :"+s.get(i).getBillNo());
		   System.out.println("s.get(i).getTrxnType()--> :"+s.get(i).getTrxnType());
		   System.out.println("s.get(i).getCustomerName()--> :"+s.get(i).getCustomerName());
		   i++;
		   
		}*/
		//System.out.println("size Before sorting :"+s.size());
	/*	Collections.sort(reportDetails,new Comparator<ReportDetail>(){
			 DateFormat f = new SimpleDateFormat("dd/MM/yyyy");	
			public int compare(ReportDetail o1, ReportDetail o2) {
				try{
				return f.parse(o1.getTrxnDate()).compareTo(f.parse(o2.getTrxnDate()));
					//return (o1.getTrxnDate1()).compareTo((o2.getTrxnDate1()));
				
				}catch(ParseException e){ 
	                throw new IllegalArgumentException(e);
	            }
			}	
			
		    });*/
		
		//Collections.addAll(s);
		
		//System.out.println("size after sorting:"+s.size());
		/*List<ReportDetail> s1 = reportDetails.stream()
				  .collect(Collectors.toList());
		System.out.println("size :"+s1.size());*/
	
		//reportDetails.clear();
		
		return reportDetails;
	}


@RequestMapping(value="/saletarget",method=RequestMethod.POST)
public @ResponseBody List<ReportDetail> getCustomerSaleTargetReport(HttpServletRequest request){
List<ReportDetail> reportDetails = new ArrayList<ReportDetail>();
List<ReportDetail> reportDetails_1 = new ArrayList<ReportDetail>();
double netAmount=0.00;
int sum=0;
try{
	Report report = new Report();
	String fromDate = request.getParameter("fromDate");
	String toDate = request.getParameter("toDate");
	
	//System.out.println("hello"); 
	
		Double saleTarget=Double.parseDouble(request.getParameter("saleTarget"));	
	  
		report.setFromDate((DateUtils.toDate(fromDate,true)));
		report.setToDate((DateUtils.toDate(toDate,true)));
		List<ReportDetail> details=null;
		
		List <Customer> cd =  customerDAO.getCustomerInformation();
// Getting Address table information
		List<Long> custIds = Utils.getCustomersIds(cd);
		List<Address> address = customerDAO.getAddressByCustId(custIds);
	// Getting area table information
		List<Integer> areaIds = Utils.getAreaIds(address);
		List<Area> area = customerDAO.getAreaByAreaId(areaIds);
		reportDetails = new ArrayList<ReportDetail>();
		ReportDetail cdetail= new ReportDetail();
		if (cd != null && !cd.isEmpty()){	
			for (Customer customers: cd){
				cdetail= new ReportDetail();
				cdetail.setCustId(customers.getCustId());
				cdetail.setFirstName(customers.getFirstName().toUpperCase());
				cdetail.setMobileNo(customers.getMobileNo());
				for (Address addresses: address){
				 for (Area areas: area){
					if (customers.getCustId()==addresses.getCustId() && areas.getId()==addresses.getAreaId()){
						cdetail.setAddress2(addresses.getAddress2().toUpperCase());
						//System.out.println("address 2 :"+cdetail.getAddress2());
						cdetail.setArea(areas.getName().toUpperCase());				
					}
				   }
			   	}
			reportDetails.add(cdetail);
			
 			}
		}
		//System.out.println("hellooo"); 
		for (int i=0; i<reportDetails.size()-1;i++){
			netAmount=0.00;
			sum=0;
			try{
				cdetail= new ReportDetail();
			// get customer IDs
				report.setCustId(reportDetails.get(i).getCustId());
			//	report.setCustId(21);
			// get order for the period
				List<Order> orders = reportDAO.getCOSOrderList(report);
				//System.out.println("my order size is :" + orders.size());
			// get order IDs and netAmount for the period    
			
				if(orders.size()>0){
				//if(orders.size()>0 && report.getCustId()==17){
					List<Long> orderIds = Utils.getOrdersIds(orders);
					/*for(int j=0;j<orderIds.size();j++){
					System.out.println("ORDER IDs :"+orderIds.get(j));
					}*/
					if(orderIds !=null && !orderIds.isEmpty()){
						details = reportService.salesTargetOrderIds(report, orderIds);
					
						if (details !=null && !details.isEmpty()){
							netAmount=details.get(0).getInvoiceAmount() !=null? details.get(0).getInvoiceAmount():0.00;
							reportDetails.get(i).setInvoiceAmount(netAmount);
							//System.out.println("HELLOOO"); 
						}
						// for finding no of sheets
						// System.out.println("products ");
						 List<Product> products = new ArrayList<Product>();
						 products=reportDAO.getProducts(report, orderIds);
					//	 System.out.println("products size :"+products.size());
						if (products.size()>0)  
						 {
							 for( Product p :products){
								 if(p.getNoOfSheet() !=null && p.getNoOfSheet()>=1)
									sum=sum+p.getNoOfSheet()*p.getNoOfCopy();
						//		    System.out.println("sum"+sum);
							 }
							 reportDetails.get(i).setDays(sum);
						 }
										 
						}
				       }else{
				    	   netAmount=0.00;	
				    	   reportDetails.get(i).setInvoiceAmount(netAmount);
				    	   
				    		   
				    	   
				    	//   System.out.println("false");
				       }
				}catch (Exception _exception) {
					logger.error("getReport::" + _exception);
					_exception.printStackTrace();
				}
		}
		for (int i=0;i<reportDetails.size()-1;i++){
			if(reportDetails.get(i).getInvoiceAmount()>=saleTarget){
				reportDetails_1.add(reportDetails.get(i));
			}
		}
	}catch (Exception _exception) {
	logger.error("getReport::" + _exception);
	_exception.printStackTrace();
	}
return reportDetails_1;
}



// end  of customer outstanding detail report
	@RequestMapping(value="/sdetail",method=RequestMethod.POST)
	public @ResponseBody List<ReportDetail> getSupplierReport(HttpServletRequest request){
		List<ReportDetail> reportDetails = new ArrayList<ReportDetail>();
		try{
			Report report = new Report();
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			String custId = request.getParameter("custid"); 
			
			
				report.setFromDate(DateUtils.toDate(fromDate,true));
				report.setToDate(DateUtils.toDate(toDate,true));
				if(!StringUtils.isEmpty(custId)){
					report.setCustId(Long.valueOf(custId));
					report.setTypeId(Integer.valueOf(custId));
				}
				List<ReportDetail> details=null;
					
				details = reportService.supplierReport(report);
				if(details!=null && !details.isEmpty())
					reportDetails.addAll(details);
				
				details = reportService.supplierVoucher(report);
				if(details!=null && !details.isEmpty())
					reportDetails.addAll(details);
				
		}catch (Exception _exception) {
			logger.error("getReport::" + _exception);
			_exception.printStackTrace();
		}
		
		Collections.sort(reportDetails,new Comparator<ReportDetail>(){
			 DateFormat f = new SimpleDateFormat("dd/MM/yyyy");	
			 
			public int compare(ReportDetail o1, ReportDetail o2) {
				try{
					return f.parse(o1.getInvoiceDate()).compareTo(f.parse(o2.getInvoiceDate()));
					//return (o1.getTrxnDate1()).compareTo((o2.getTrxnDate1()));
					
				}catch(ParseException e){
	                throw new IllegalArgumentException(e);
	            }
			}	
			
		    });
		return reportDetails;
	}
	// newly added for order vs despatch report
	
@RequestMapping(value="/ordervsdespatch",method=RequestMethod.POST)
	public @ResponseBody List<ReportDetail> getOrderVsDespatchReport(HttpServletRequest request){
		List<ReportDetail> reportDetails = new ArrayList<ReportDetail>();
		try{
			Report report = new Report();
			String fromdate = request.getParameter("fromDate");
			String todate = request.getParameter("toDate");
		//	System.out.println("From date & To Date :"+fromdate+"  "+todate);
			fromdate = reportDAO.getyyyymmdd(fromdate);
			todate=reportDAO.getyyyymmdd(todate);
			List<Receipt> receiptDetails=null;
			List<OrderTrxnDetail> trxndetails=null;
			List<JobAllocation> jobstatus=null;
			List<ReceiptPayment> payments=null;
			long orderid;
			Double billAmount=0.0;
			Double paidAmount=0.0;
			String  orderdate;
			final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String date1=sdf.format(date);
			//System.out.println(date1);
			List<PaymentType> paymentTypes = metaDAO.getPaymentTypes();
			Map<Long,String> payTypes = Utils.getTypes(paymentTypes);			
			//System.out.println( "today : " + date1);
			List<Order> orders = reportDAO.getUndeliveredOrders(fromdate, todate);
			//	System.out.println("size of order:"+ orders.size());
			
				ReportDetail detail= new ReportDetail();
			if(orders !=null && !orders.isEmpty()){
				/*Vector vec_txn_id = new Vector();
				Vector vec_txn_adv = new Vector();
				Vector vec_txn_net = new Vector();
				*/
				try{
				for(Order order:orders){
				    detail= new ReportDetail();	
					orderid=order.getOrderId();
				    detail.setOrderId(orderid);
					detail.setCustomerName(order.getCustName().toUpperCase());
					orderdate=order.getOrderDate().toString();
					orderdate = orderdate.substring(0,10);
					orderdate=reportDAO.getddmmyyyy(orderdate);
					detail.setInvoiceDate(orderdate);
					String duedate=order.getDueDate().toString();
					duedate=reportDAO.getddmmyyyy(duedate);
			//		System.out.println("DueDate:"+duedate);
					detail.setChqDate(duedate);
					//duedate=reportDAO.getyyyymmdd(duedate, "-");
					duedate=reportDAO.getddmmyyyytoyyyymdd(duedate);
			//		System.out.println("Due DATe :"+duedate);
					Customer customer = customerDAO.getCustomerById(order.getCustId());
					if(customer !=null)
						if ( customer.getMobileNo() !=null && !customer.getMobileNo().isEmpty()){
							detail.setMobileNo(customer.getMobileNo());
						}else{
								detail.setMobileNo("0");
							}
											
					trxndetails=orderDAO.getTransactionByOrderId(orderid);
					if(trxndetails != null && !trxndetails.isEmpty()){
						    billAmount=	trxndetails.get(0).getNetAmount();
							detail.setInvoiceAmount(billAmount);
							//added transaction type and advance amount
							detail.setTrxnType(payTypes.get(Long.valueOf(trxndetails.get(0).getPymtType())));
							if(trxndetails.get(0).getAdvance() !=null) {								
								detail.setPayAmount(trxndetails.get(0).getAdvance());
							}else {
								detail.setPayAmount(0.0);
							}
							//added transaction type and advance amount
						
						   	if (trxndetails.get(0).getAdvance()!=null && trxndetails.get(0).getAdvance()!=0) {
						   		paidAmount=trxndetails.get(0).getAdvance();
							}else {
							paidAmount=0.0;
						}
					}
						     payments = paymentDAO.getReceiptPaymentsByOrderId(String.valueOf(orderid));
		                      if(payments!=null && !payments.isEmpty()){
		                             for(ReceiptPayment receipt: payments)
		                            	 paidAmount+=receipt.getPaidAmount();
		                      }
		               //  STOPPED TO PICKUP FROM JOB ALLOCATION  STARTS      
		                   /*   jobstatus=orderDAO.getJobAllocationByOrderId(orderid);
		                      for (JobAllocation joballoc:jobstatus){
		                    	  int flag =dateCompare(date1,duedate);
		                    	  //System.out.println("joballoc.getId().getDepotId() :"+joballoc.getId().getDepotId());
		                    	//  System.out.println("joballoc.getStatusId() :"+joballoc.getStatusId());
		                    
		                   if(joballoc.getId().getDepotId()==101 && joballoc.getStatusId()==101 ){
                 	   			detail.setNarration("Pending");
                 	   			break;
		                  	  }else if(joballoc.getId().getDepotId()==101 && joballoc.getStatusId()==104 && flag ==1 ){
	                    		   detail.setNarration("UnDelivered");
	                    		   break;
		                  	  }else if (joballoc.getId().getDepotId()==101 && joballoc.getStatusId()!=104)
		                  	   	   	    	detail.setNarration("In-Progress");
		                        			break;
		                  	   	   	    	
		                    	   }*/
		                  //  STOPPED TO PICKUP FROM JOB ALLOCATION  ENDS 
		                      List<JobStatus> jobstatus1= metaDAO.getJobStatus();
		          			Map<Integer,String> jobStatusMap =Utils.getJobStatusIdMap(jobstatus1); 
		                    detail.setNarration(jobStatusMap.get(order.getStatus()));  
		          			
			                    detail.setBalance(billAmount-paidAmount); 
			            
						reportDetails.add(detail);
						paidAmount=0.0;
						billAmount=0.0;
						payments=null;
						jobstatus=null;
						duedate=null;
				}
				}catch(Exception re) {
						//System.out.println(re);
						re.printStackTrace();
					} 
			}				
				
		
							//Vector vec_txn_id = new Vector();
							//Vector vec_txn_adv = new Vector();
							//Vector vec_txn_net = new Vector();
							/*if(vec_txn_id.indexOf(order.getOrderId())!=-1){
								dou_adv = new Double(vec_txn_adv.get(vec_txn_id.indexOf(order.getOrderId())).toString()).doubleValue();
								dou_net = new Double(vec_txn_adv.get(vec_txn_id.indexOf(order.getOrderId())).toString()).doubleValue();
							}
							*/	               
		                   
		   
		}catch (Exception _exception) {
			//System.out.println(_exception);
			_exception.printStackTrace();
		}
			//logger.error("get order Vs Despatch::" + _exception);
			//_exception.printStackTrace();}
		return reportDetails;
	}
	


//UNDELIVERED ALBUM REPORT START
@RequestMapping(value="undelivered_album_detail",method = RequestMethod.POST)
public @ResponseBody List<ReportDetail>  getUndeliverdAlbumReport(HttpServletRequest request){
		List<JobAllocation> jobAllocations = new ArrayList<JobAllocation>();
		List<ReportDetail> reportDetails = new ArrayList<ReportDetail>();
		Double currentCreditBalance=0.00;
		/*try{*/
			List <Customer> cd =  customerDAO.getCustomerInformation();
			System.out.println("customer size:"+cd.size());
			Long depotid=Long.parseLong("106");
			List<Employee> employee = userDAO.getEmployeeByDepotId(depotid);
			System.out.println("Employee size:"+employee.size());
			try{
				for(Employee emp:employee){
					jobAllocations = orderDAO.getJobs(emp.getEmpId());
					//System.out.println("jobAllocations size:"+jobAllocations.size());
						if(jobAllocations !=null && !jobAllocations.isEmpty()){
							Set<Long> orderIds = Utils.getOrderIds(jobAllocations);
							List<Long> ids = new ArrayList<Long>();
							ids.addAll(orderIds);
							List<Order> orders = orderDAO.getOrders(ids);
						//	System.out.println("orders size:"+orders.size());
							try{
							for(Order order:orders){
								//System.out.println("orders number:"+order.getCustId());
								if(order.getSmsFlag() !=null && order.getStatus()!=106 && order.getStatus() !=105){
							ReportDetail rd=new ReportDetail();
							//System.out.println("orders number inside loop:"+order.getCustId());
							rd.setCustId(order.getCustId());
							rd.setCustomerName(order.getCustName().toUpperCase());
							rd.setOrderId(order.getOrderId());
							rd.setInvoiceDate(order.getStrOrderDate());
							rd.setAddress2(order.getCustAddr2().toUpperCase());
							for (Customer customer:cd){
								
								if(customer.getCustId()==order.getCustId()){
									//System.out.println("mobile number:"+customer.getMobileNo());
									rd.setMobileNo(customer.getMobileNo());
									currentCreditBalance	= orderDAO.getCurrentCreditBalance(customer.getCustId());
								}
								}
							if (currentCreditBalance !=null)
								
								rd.setCreditAmount(currentCreditBalance);
							if (order.getSmsFlag() !=null)
								rd.setDays(order.getSmsFlag());
							reportDetails.add(rd);
								}				
							} 
						}catch(Exception e){}
					}
				}
						}catch(Exception _exception) {
							_exception.printStackTrace();
							logger.error("Is it Null here ::" +_exception);
							
						}
					
				
			return reportDetails;
		}		
		
	
			
			//myorders=orderDAO.getUndeliveredAlbumJobs();
			//List<Long> orderIds=Utils.getOrdersIds(myorders);
			//List <JobAllocation> finishedjob = orderDAO.getJobAllocationByOrderId(orderIds);
			//List <JobAllocationForUndeliveredAlbum> finishedjob =  orderDAO.getUndeliveredAlbumJobs();
			//List<Long> orderIds = Utils.getUndeliveredOrdersIds(finishedjob);
			//reportDetails = orderDAO.getUndeliveredAlbumOrders(orderIds);
		
			
		
//UNDELIVERED ALBUM REPORT ENDS

	// customer outstanding report starts
	@RequestMapping(value="/cust_outstanding_detail",method=RequestMethod.POST)
	public @ResponseBody List<ReportDetail> getCustomerOutstandingReport(HttpServletRequest request){
		List<ReportDetail> ReportDetails =new ArrayList<ReportDetail>();
			
		try{ 
			Report report = new Report();
			//System.out.println("hai from cust_outstanding ");
			String type = request.getParameter("type");
			String rType = request.getParameter("report");
			
			// Getting customer table information
			List <Customer> cd =  customerDAO.getCustomerInformation();
			
			// Getting Address table information
			List<Long> custIds = Utils.getCustomersIds(cd);
			List<Address> address = customerDAO.getAddressByCustId(custIds);
				
			
			// Getting area table information
			List<Integer> areaIds = Utils.getAreaIds(address);
			List<Area> area = customerDAO.getAreaByAreaId(areaIds);
			
			ReportDetails = new ArrayList<ReportDetail>();
			ReportDetail cdetail= new ReportDetail();
		if (cd != null && !cd.isEmpty()){	
			
			
			for (Customer customers: cd){
				cdetail= new ReportDetail();
				cdetail.setFirstName(customers.getFirstName());
				/* UPDATED FOR CURRENT CREDIT BALANCE STARTS*/
					//cdetail.setCreditAmount(customers.getCreditBalance());
				//System.out.println("customers.getCustId() STARTS :"+customers.getCustId());
					Double currentCreditBalance	= orderDAO.getCurrentCreditBalance(customers.getCustId());
					if (currentCreditBalance !=null){
						cdetail.setCreditAmount(currentCreditBalance);
					}else{
						cdetail.setCreditAmount(0.00);
					}
				//	System.out.println("customers.getCustId() ENDS :"+customers.getCustId());
				/* UPDATED FOR CURRENT CREDIT BALANCE ENDS*/
				cdetail.setMobileNo(customers.getMobileNo());
				cdetail.setCustId(customers.getCustId());
				for (Address addresses: address){
					 for (Area areas: area){
						if (customers.getCustId()==addresses.getCustId() && areas.getId()==addresses.getAreaId()){
							cdetail.setAddress2(addresses.getAddress2());
							cdetail.setArea(areas.getName());				
						}
					   }
				   }
				ReportDetails.add(cdetail);
	 			}
			}
		}
			catch (Exception _exception) {
			logger.error("getCustomer Outstanding Report::" + _exception);
		}
		return ReportDetails;
	}
	
	
	// customer outstanding report ends
	
	
		
	
		@RequestMapping(value="/supplier_outstanding_detail",method=RequestMethod.POST)
		public @ResponseBody List<ReportDetail> getSupplierOutstandingReport(HttpServletRequest request){
			List<ReportDetail> ReportDetails =new ArrayList<ReportDetail>();
			Double pur=0.0;
			Double paid=0.0;
			try{ 
								
				List <Supplier> supplier =  paymentDAO.getSuppliers();
				
				ReportDetails = new ArrayList<ReportDetail>();
				ReportDetail cdetail= new ReportDetail();
				
				for(int i=0;i<supplier.size();i++){
					cdetail= new ReportDetail();
					{
					cdetail.setCustId(supplier.get(i).getSupId());
					cdetail.setSupplierName(supplier.get(i).getName().toUpperCase());
					cdetail.setAddress1(supplier.get(i).getAddress1().toUpperCase());
					cdetail.setAddress2(supplier.get(i).getAddress2().toUpperCase());
					cdetail.setMobileNo(supplier.get(i).getMobileNo());
					pur = reportDAO.getPurchaseSumBySupplierId(supplier.get(i).getSupId());
					paid = reportDAO.getPaymentSumBySupplierId(supplier.get(i).getSupId());
					if (pur!=null ){
						if (paid !=null){
							cdetail.setCreditAmount(pur-paid);
						}else{
								cdetail.setCreditAmount(pur);
							}
					}else{
						if (paid!=null){
							cdetail.setCreditAmount(pur-paid);
						}else{
								cdetail.setCreditAmount(0.0);
							}
					}
						
					
					ReportDetails.add(cdetail);
					}
				}
					
			}
				catch (Exception _exception) {
				logger.error("getSupplier Outstanding Report::" + _exception);
			}
			return ReportDetails;
		}
		
		
	
	
	
	
	
	
	// customer addressList report
	@RequestMapping(value="/cust_address_detail",method=RequestMethod.POST)
		public @ResponseBody List<ReportDetail> getCustomerAddressReport(HttpServletRequest request){
			List<ReportDetail> ReportDetails =new ArrayList<ReportDetail>();
				
			try{ 
				Report report = new Report();
			//	System.out.println("hai from cust_address Report ");
				String type = request.getParameter("type");
				String rType = request.getParameter("report");
				
				// Getting customer table information
				List <Customer> cd =  customerDAO.getCustomerInformation();
				//System.out.println("customer  size   "+cd.size());
				// Getting Address table information
				List<Long> custIds = Utils.getCustomersIds(cd);
				List<Address> address = customerDAO.getAddressByCustId(custIds);
				//System.out.println("address  size   "+address.size());
				
				// Getting area table information
				List<Integer> areaIds = Utils.getAreaIds(address);
				List<Area> area = customerDAO.getAreaByAreaId(areaIds);
				//System.out.println("area  size   "+area.size());
				ReportDetails = new ArrayList<ReportDetail>();
				ReportDetail cdetail= new ReportDetail();
			if (cd != null && !cd.isEmpty()){	
				
				
				for (Customer customers: cd){
					cdetail= new ReportDetail();
					cdetail.setFirstName(customers.getFirstName().toUpperCase());
					//cdetail.setCreditAmount(customers.getCreditBalance());
					cdetail.setMobileNo(customers.getMobileNo());
					cdetail.setCustId(customers.getCustId());
					for (Address addresses: address){
						 for (Area areas: area){
							if (customers.getCustId()==addresses.getCustId() && areas.getId()==addresses.getAreaId()){
								try{
								cdetail.setAddress1(addresses.getAddress1().toUpperCase());
								cdetail.setAddress2(addresses.getAddress2().toUpperCase());
								//cdetail.setCity(addresses.getCity().toUpperCase());	
								cdetail.setArea(areas.getName().toUpperCase());		
								//System.out.println(areas.getName().toUpperCase());
								}catch(Exception e){}
							}
						   }
					   }
					ReportDetails.add(cdetail);
		 			}
				}
			}
				catch (Exception _exception) {
				logger.error("getCustomer address Report::" + _exception);
			}
			return ReportDetails;
		}
		
	
	
	
	
	
	
	
	@RequestMapping(value="/customer_vs_business",method=RequestMethod.POST)
	public @ResponseBody List<ReportDetail> getCustomervsBusiness(HttpServletRequest request){
		List<ReportDetail> ReportDetails =new ArrayList<ReportDetail>();
		List <Customer> cd =null;	
		try{
			 
			Report report = new Report();
			String days = request.getParameter("days");
			String text=request.getParameter("smsText");
			Double currentCreditBalance;
			//System.out.println("days :"+ days);
			int noOfDays=Integer.parseInt(days);
			//System.out.println("hai from cust_vs Business ");
			String type = request.getParameter("type");
			String rType = request.getParameter("report");
			
			// Getting customer table information
			 cd =  customerDAO.getCustomerInformation();
			 
			// Getting Address table information
			List<Long> custIds = Utils.getCustomersIds(cd);
			List<Address> address = customerDAO.getAddressByCustId(custIds);
			//System.out.println("Yes.... I have completed address");
			List<Customer> customers =	orderDAO.getCustomerOrders( cd, days);
			//System.out.println("customers.size() :"+customers.size());
			// Getting area table information
			List<Integer> areaIds = Utils.getAreaIds(address);
			List<Area> area = customerDAO.getAreaByAreaId(areaIds);
			
			ReportDetails = new ArrayList<ReportDetail>();
			ReportDetail cdetail= new ReportDetail();
		if (cd != null && !cd.isEmpty()){	
			try{		
			for (Customer customer:customers){
				cdetail= new ReportDetail();
				cdetail.setFirstName(customer.getFirstName());
				currentCreditBalance= orderDAO.getCurrentCreditBalance(customer.getCustId());
				if (currentCreditBalance !=null){
				cdetail.setCreditAmount(currentCreditBalance);
				}else{
					cdetail.setCreditAmount(0.00);	
				}
				cdetail.setMobileNo(customer.getMobileNo());
				cdetail.setCustId(customer.getCustId());
				try{
				cdetail.setDays(customer.getCreditDays());
				}
				catch(Exception e){}
				for (Address addresses: address){
					 for (Area areas: area){
						if (customer.getCustId()==addresses.getCustId() && areas.getId()==addresses.getAreaId()){
							cdetail.setAddress2(addresses.getAddress2());
							cdetail.setArea(areas.getName());				
						}
					   }
				   }
				for (Address addresses: address){
					 	if (customer.getCustId()==addresses.getCustId()) 
					 	{
							cdetail.setAddress2(addresses.getAddress2());
						    break;}			
					   }
				  		
				if (cdetail.getDays()>=noOfDays){
					ReportDetails.add(cdetail);
				}
				
	 			}
			}
			catch(Exception e){}
			}
		}
			catch (Exception _exception) {
			logger.error("getCustomer Vs Business Report::" + _exception);
		}
		return ReportDetails;
	}
	 
// monthly business report summary
	@RequestMapping(value="/mbrsummaryreport",method=RequestMethod.POST)
	public @ResponseBody List<ReportDetail> getMBRSummaryReport(HttpServletRequest request){
		List<ReportDetail> reportDetails = new ArrayList<ReportDetail>();
		try{
			Report report = new Report();
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			String type = request.getParameter("type");
			
			List<ReportDetail> details=null;
			 /*DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");			   
			   fromDate = fromDate.substring(0,10);
			   toDate = toDate.substring(0,10);
			   fromDate = orderDAO.getyyyymmdd(fromDate);
			   toDate = orderDAO.getyyyymmdd(toDate);*/
			/*System.out.println("fromdate     :"+fromDate);
			System.out.println("Todate     :"+toDate);*/
			
			details = reportService.mbrsummary( fromDate, toDate);
			//System.out.println("ALL details.size():"+ details.size());
						
			
			reportDetails.addAll(details);
					  
		}catch (Exception _exception) {
			logger.error("MBR Summary Report::" + _exception);
		}
		// for sorting the dates
		Collections.sort(reportDetails,new Comparator<ReportDetail>(){
			 DateFormat f = new SimpleDateFormat("dd/MM/yyyy");	
			public int compare(ReportDetail o1, ReportDetail o2) {
				try{
				return f.parse(o1.getTrxnDate()).compareTo(f.parse(o2.getTrxnDate()));
					//return (o1.getTrxnDate1()).compareTo((o2.getTrxnDate1()));
				
				}catch(ParseException e){
	                throw new IllegalArgumentException(e);
	            }
			}	
			
		    });
		
		
		return reportDetails;
	}
	
	 
	// monthly business Statement
		@RequestMapping(value="/mbstatement",method=RequestMethod.POST)
		public @ResponseBody List<ReportDetail> getMBStatement(HttpServletRequest request){
			List<ReportDetail> reportDetails = new ArrayList<ReportDetail>();
			try{
				Report report = new Report();
				String fromDate = request.getParameter("fromDate");
				String toDate = request.getParameter("toDate");
				String type = request.getParameter("type");
				
				List<ReportDetail> details=null;
				 /*DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");			   
				   fromDate = fromDate.substring(0,10);
				   toDate = toDate.substring(0,10);
				   fromDate = orderDAO.getyyyymmdd(fromDate);
				   toDate = orderDAO.getyyyymmdd(toDate);*/
				/*System.out.println("fromdate     :"+fromDate);
				System.out.println("Todate     :"+toDate);*/
				
				details = reportService.mbrsummary( fromDate, toDate);
				//System.out.println("ALL details.size():"+ details.size());
							
				
				reportDetails.addAll(details);
						  
			}catch (Exception _exception) {
				logger.error("MBStatement::" + _exception);
			}
			// for sorting the dates
			Collections.sort(reportDetails,new Comparator<ReportDetail>(){
				 DateFormat f = new SimpleDateFormat("dd/MM/yyyy");	
				public int compare(ReportDetail o1, ReportDetail o2) {
					try{
					return f.parse(o1.getTrxnDate()).compareTo(f.parse(o2.getTrxnDate()));
						//return (o1.getTrxnDate1()).compareTo((o2.getTrxnDate1()));
					
					}catch(ParseException e){
		                throw new IllegalArgumentException(e);
		            }
				}	
				
			    });
			
			
			return reportDetails;
		}
		
	
	// data backup
	
	@RequestMapping(value="/backup",method=RequestMethod.POST)
	public ModelAndView getBackup(){
		reportService.Backupdbtosql();
		ModelAndView modelAndView = new ModelAndView("redirect:/home");
	return modelAndView;
	}
	
	
	
	// Sales Receipts and Payments Report
		@RequestMapping(value="/srpreport",method=RequestMethod.POST)
		public @ResponseBody List<ReportDetail> getSRPReport(HttpServletRequest request){
			List<ReportDetail> reportDetails = new ArrayList<ReportDetail>();
			try{
				Report report = new Report();
				String fromDate = request.getParameter("fromDate");
				String toDate = request.getParameter("toDate");
				String type = request.getParameter("type");
				
				List<ReportDetail> details=null;
				 DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");			   
				   fromDate = fromDate.substring(0,10);
				   toDate = toDate.substring(0,10);
				   fromDate = orderDAO.getyyyymmdd(fromDate);
				   toDate = orderDAO.getyyyymmdd(toDate);
				//System.out.println("fromdate     :"+fromDate);
			    //System.out.println("Todate     :"+toDate);
			
				
				details = reportService.srpReport( fromDate, toDate, type);
				//System.out.println("ALL SRP report details.size():"+ details.size());
				reportDetails.addAll(details);
				/*for(ReportDetail reportdetails: details)
					System.out.println(reportdetails.getCustomerName());	*/  
			}catch (Exception _exception) {
				logger.error("SRP Report::" + _exception);
			}
			return reportDetails;
		}
private Map<Integer,String> getDepartments(List<Deportment> deportments){
			Map<Integer,String> sMap = new HashMap<Integer, String>();
			for(Deportment deportment: deportments){
				sMap.put(deportment.getId(), deportment.getName());
			}
			
			return sMap;
		}

//@RequestMapping(value="/saveNewTable",method=RequestMethod.POST)
//public @ResponseBody ModelAndView getsaveNewTable(@ModelAttribute("NewTable") NewTable order){
//	ModelAndView modelAndView = new ModelAndView(ReportConstants.TEST_PAGE.value());
//	System.out.println("hello....");
//	List<NewTable> nt = new ArrayList<NewTable>();
//	try{
//		NewTable newtable = new NewTable();
//		System.out.println("hello");
//	    customerDAO.saveOrUpdate(order);
//	    Query query= entityManager.createNativeQuery("select * from new_table,new_table_2 where new_table.emp_id=new_table_2.emp_id",NewTable.class);
//        nt=query.getResultList();	 
//        System.out.println("query size: "+nt.size());
//	}catch (Exception _exception) {
//		logger.error("New Table Report::" + _exception);
//	}
//	return modelAndView;
//}



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

/**
 * 
 */
package com.studio.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.studio.controller.LoginController;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
//import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.studio.dao.CustomerDAO;
import com.studio.dao.MetaDAO;
import com.studio.dao.OrderDAO;
import com.studio.dao.PaymentDAO;
import com.studio.dao.ReportDAO;
import com.studio.dao.UserDAO;
import com.studio.domain.Customer;
import com.studio.domain.DeliveryType;
import com.studio.domain.Employee;
import com.studio.domain.JobStatus;
import com.studio.domain.Order;
import com.studio.domain.OrderInformation;
import com.studio.domain.OrderTrxnDetail;
import com.studio.domain.OrderedItems;
import com.studio.domain.Product;
import com.studio.domain.ProductItem;
import com.studio.domain.ProductItemType;
import com.studio.domain.ProductSize;
import com.studio.domain.ProductType;
import com.studio.domain.Receipt;
import com.studio.domain.ReceiptPayment;
import com.studio.service.EmailService;
import com.studio.service.SmsService;
import com.studio.utils.DateUtils;
import com.studio.utils.Utils;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;

import com.lowagie.text.Image;
// pdf generator
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

/**
 * @author ezhilraja_k
 * @param <paymentDAO>
 *
 */

@Controller
public class PrintController<paymentDAO> {

	Logger logger = Logger.getLogger(PrintController.class);

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private MetaDAO metaDAO;

	@Autowired
	private PaymentDAO paymentDAO;

	@Autowired
	private ReportDAO reportDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private LoginController login;
	@Autowired
	private Environment env;

	@Autowired
	private SmsService smsService;

	/*
	 * @RequestMapping(value ="/download/{orderId}/{mode}", method =
	 * RequestMethod.GET)
	 * 
	 * @ResponseBody public void downloadPDFResource( HttpServletResponse response,
	 * 
	 * @PathVariable String orderId, @PathVariable int mode,
	 * 
	 * @RequestHeader String referer,HttpServletRequest request ) { //Check the
	 * renderer if(referer != null && !referer.isEmpty()) { //do nothing //or send
	 * error } Order order= getOrder(orderId); if(order !=null){ String
	 * fileName=null; int totpage=1; int totitem=order.getTotitem(); if
	 * (totitem>10){ totpage=totitem/10; int modpage=totitem%10; if (modpage>0)
	 * totpage++; } if (mode==0){ fileName = orderId+"M.pdf"; }else{ fileName =
	 * orderId+".pdf"; }
	 * 
	 * System.out.println("from download controller"+ fileName); String
	 * dataDirectory =
	 * request.getServletContext().getRealPath("/WEB-INF/downloads/pdf/"); String
	 * dataDirectory = request.getServletContext().getRealPath(
	 * "D:\\MyStudio\\src\\main\\webapp\\WEB-INF\\downloads\\pdf\\");
	 * System.out.println("dataDirectory:"+dataDirectory);
	 * 
	 * 
	 * 
	 * //String dataDirectory =
	 * "D://MyStudio//src//main//webapp//WEB-INF//downloads//pdf//"; String
	 * dataDirectory = env.getProperty("invoice"); Path file =
	 * Paths.get(dataDirectory, fileName); if (Files.exists(file)) {
	 * response.setContentType("application/pdf");
	 * response.addHeader("Content-Disposition", "attachment; filename="+fileName);
	 * try { Files.copy(file, response.getOutputStream());
	 * response.getOutputStream().flush();
	 * 
	 * }catch (IOException ex) { ex.printStackTrace(); }
	 * 
	 * } if(mode==0){ if (order !=null) if (order.getEmail()!=null &&
	 * !order.getEmail().isEmpty()) sendEmail(order.getOrderId(),
	 * order.getEmail(),order.getOrderTrxnDetails().get(0).getBalance());
	 * 
	 * } }else{ final JPanel panel = new JPanel();
	 * JOptionPane.showMessageDialog(panel, "Invoice has been Cancelled", "Error",
	 * JOptionPane.ERROR_MESSAGE); }
	 * 
	 * }
	 */

	@RequestMapping(value = "/download/{orderId}/{mode}", method = RequestMethod.GET)
	@ResponseBody
	public void downloadPDFResource(HttpServletResponse response, @PathVariable String orderId, @PathVariable int mode,
			@RequestHeader String referer, HttpServletRequest request)
			throws UnsupportedEncodingException, IOException {
		// Check the renderer
		if (referer != null && !referer.isEmpty()) {
			// do nothing
			// or send error
		}
		Order order = getOrder(orderId);
		if (order != null) {
			String fileName = null;
			int totpage = 1;
			int totitem = order.getTotitem();
			if (totitem > 10) {
				totpage = totitem / 10;
				int modpage = totitem % 10;
				if (modpage > 0)
					totpage++;
			}
			if (mode == 0) {
				fileName = orderId + "M.pdf";
				// fileName = orderId+".pdf";
			} else {
				fileName = orderId + ".pdf";
			}
			/*
			 * System.out.println("from download controller"+ fileName); String
			 * dataDirectory =
			 * request.getServletContext().getRealPath("/WEB-INF/downloads/pdf/"); String
			 * dataDirectory = request.getServletContext().getRealPath(
			 * "D:\\MyStudio\\src\\main\\webapp\\WEB-INF\\downloads\\pdf\\");
			 * System.out.println("dataDirectory:"+dataDirectory);
			 */

			// String dataDirectory =
			// "D://MyStudio//src//main//webapp//WEB-INF//downloads//pdf//";
			String dataDirectory = env.getProperty("invoice");
			// String dataDirectory = env.getProperty("invoice1");
			Path file = Paths.get(dataDirectory, fileName);
			if (Files.exists(file)) {
				response.setContentType("application/pdf");
				/*
				 * response.addHeader("Content-Disposition", "attachment; filename="+fileName);
				 */
				try {
					Files.copy(file, response.getOutputStream());
					response.getOutputStream().flush();

				} catch (IOException ex) {
					ex.printStackTrace();
				}

			}
			if (mode == 0) {
				if (order != null)
					if (order.getEmail() != null && !order.getEmail().isEmpty())

						logger.info("iam here");
//	       sendEmail(order.getOrderId(), order.getEmail(),order.getOrderTrxnDetails().get(0).getBalance());
				Order ord = orderDAO.getOrderById(order.getOrderId());
				Customer customer = customerDAO.getCustomerById(ord.getCustId());
				String custName = order.getCustName();
				int noOfSheets = order.getNoOfSheet();
				double billamt = Math.round(order.getOrderTrxnDetails().get(0).getSubTotal());
				String billDate = DateUtils.toDate(new Date());
				String dueDate = order.getStrDueDate();
				String billAmount = order.getOrderTrxnDetails().get(0).getSubTotal() != null
						? String.format("%.2f", billamt)
						: "0";
				String advance = order.getOrderTrxnDetails().get(0).getAdvance() != null
						? order.getOrderTrxnDetails().get(0).getAdvance().toString()
						: "0";
				String balance = order.getOrderTrxnDetails().get(0).getBalance() != null
						? order.getOrderTrxnDetails().get(0).getBalance().toString()
						: "0";
				// System.out.println("customer.getMobileNo() :"+customer.getMobileNo());
				// smsService.sendOrderCompletedSMS(String.valueOf(orderId),
				// customer.getMobileNo().toString());
				// sendWhatsapp(order.getOrderId(),
				// order.getOrderTrxnDetails().get(0).getBalance(),
				// customer.getMobileNo().toString());
				JSONObject body = new JSONObject();
				body.put("countryCode", "+91");
				body.put("phoneNumber", customer.getMobileNo().toString());
				// body.put("phoneNumber", "9443453320");
				body.put("type", "Template");
				JSONObject template = new JSONObject();
				// template.put("name", "bill");
				template.put("name", "confirm_order");
				template.put("languageCode", "en");
				// template.put("fileName", order.getOrderId()+".pdf");
				JSONArray bodyValue = new JSONArray();
				// headerValue.add("https://www.africau.edu/images/default/sample.pdf");

				bodyValue.add(order.getOrderId());
				bodyValue.add(order.getCustName());
				bodyValue.add(order.getNoOfSheet());
				bodyValue.add(billAmount);
				bodyValue.add(billDate);
				bodyValue.add(advance);
				bodyValue.add(balance);
				bodyValue.add(dueDate);

				template.put("bodyValues", bodyValue);
				body.put("template", template);

				logger.info("sample json format======" + body);

				String auth = "Basic " + "LW1oSVVJeGY3ZjNvdDNkOU9PLXR2eEJnX04zVWlpTlN5NFRURVh1ZFlGbzo=";
				URL postUrl = new URL("https://api.interakt.ai/v1/public/message/");
				HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
				connection.setRequestProperty("Authorization", auth);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				//// connection.setRequestProperty("Cookie",
				//// "ApplicationGatewayAffinity=a8f6ae06c0b3046487ae2c0ab287e175;
				//// ApplicationGatewayAffinityCORS=a8f6ae06c0b3046487ae2c0ab287e175");
				connection.setUseCaches(false);
				connection.setDoInput(true);
				connection.setDoOutput(true);

				// String params = "{\n \"countryCode\": \"+91\",\n \"phoneNumber\":
				// \"9443453320\",\n \"callbackData\": \"some text here\",\n \"type\":
				// \"Template\",\n \"template\": {\n \"name\": \"bill\",\n \"languageCode\":
				// \"en\",\n \"headerValues\": [\n
				// \"https://drive.google.com/file/d/1HZ3R6NZc7fl4UyYCQ83PoqZOL5ew4_KL/view?usp=sharing\"\n
				// ],\n \"fileName\": \"ecertificate.pdf\",\n \"bodyValues\": [\n \"thanks for
				// shopping with us\"\n ]\n }\n}";
				// JSONObject test = new JSONObject();
				// test.put("phoneNumber", customer.getMobileNo());

				BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
				wr.write(body.toJSONString());
//	        wr.write(params);
				wr.close();
				connection.connect();

				int responseCode = connection.getResponseCode();
				logger.info("responsecode=======" + responseCode);

				if (responseCode == HttpURLConnection.HTTP_CREATED) {

					StringBuffer jsonResponseData = new StringBuffer();
					String readLine = null;
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(connection.getInputStream()));

					while ((readLine = bufferedReader.readLine()) != null) {
						jsonResponseData.append(readLine + "\n");
					}

					bufferedReader.close();
					System.out.println(jsonResponseData.toString());

				} else {
					System.out.println(responseCode);
				}

			}
		} else {
			final JPanel panel = new JPanel();
			JOptionPane.showMessageDialog(panel, "Invoice has been Cancelled", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	/*
	 * public ModelAndView printOrder(ModelAndView modelAndView, @PathVariable
	 * String orderId){
	 */

	@RequestMapping(value = "/printorder/{orderId}/{mode}", method = RequestMethod.GET)
	public String printOrder(ModelAndView modelAndView, HttpServletRequest request, @PathVariable String orderId,
			@PathVariable int mode) {
		logger.info("-- printOrder -- order id :" + orderId + "Mode :" + mode);
		try {

			List<ProductType> productTypes = metaDAO.getProductTypes();

			Map<Long, String> productTypeMap = Utils.getTypes(productTypes);
			Order order = getOrder(orderId);
			if (order == null) {
				// System.out.println("Deleted Record :"+orderId);
				return "redirect:/download/{orderId}/{mode}";
			}
			List<Product> sheet = metaDAO.getNoOfSheets(orderId);
			modelAndView.addObject("productTypeMap", productTypeMap);
			modelAndView.addObject("order", order);
			modelAndView.addObject("product", sheet);
			String uname = null;
			String pDoc = null;
			String pDoclocal = null;
			if (order.getBillBy() != null) {
				uname = order.getBillBy();
			} else {
				uname = "Unknown";
			}

			// Page verification
			// int y=0;
			int no = 1;
			// int k=0;
			// int l=0;
			int i = 0;
			int j = 0;
			// int kk=0;
			// int ll=0;
			int slno = 1;
			int totpage = 1;
			int totitem = order.getTotitem();
			// System.out.println("totitem :"+totitem);
			String[][] s = new String[totitem][7];

			// array initializing
			int prod = order.getProducts().size();
			// System.out.println("prod :"+prod);
			int rows = 0;
			Vector v = new Vector();
			for (i = 0; i < order.getProducts().size(); i++)
				v.addElement(order.getProducts().get(i).getProductItems().size());
			// System.out.println("Vector sise:"+v.size());

			for (i = 0; i < totitem; i++)
				for (j = 0; j < 7; j++)
					s[i][j] = "0";
			// Coping data into array
			int x = 0;
			for (i = 0; i < prod; i++) {
				for (j = 0; j < (Integer) (v.get(i)); j++) {
					s[x][0] = String.valueOf(x + 1);
					s[x][1] = productTypeMap.get(order.getProducts().get(i).getProductTypeId()).toUpperCase();
					s[x][2] = order.getProducts().get(i).getSizeName();
					s[x][3] = order.getProducts().get(i).getProductItems().get(j).getProdItemName().toUpperCase();
					s[x][4] = order.getProducts().get(i).getProductItems().get(j).getQuantity().toString();
					s[x][5] = String.format("%.2f", order.getProducts().get(i).getProductItems().get(j).getRate());
					s[x][6] = String.format("%.2f", order.getProducts().get(i).getProductItems().get(j).getAmount());
					// slno++;
					x++;

				}
			}
			/*
			 * for(i=0;i<totitem;i++) System.out.println(s[i][3]);
			 */
			if (totitem > 10) {
				totpage = totitem / 10;
				int modpage = totitem % 10;
				if (modpage > 0)
					totpage++;
			}
			// System.out.println("Total Pages :"+totpage);
			if (mode == 1) {
				for (int count = 0; count < totpage; count++) {
					if (totpage > 1) {

						pDoc = env.getProperty("invoice") + order.getOrderId() + "-" + count + ".pdf";
					} else {
						pDoc = env.getProperty("invoice") + order.getOrderId() + ".pdf";

					}

					try {
						PdfReader reader = new PdfReader(
								env.getProperty("invoice.template") + "//" + "VAISHNAVI_INV.pdf");
						PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pDoc));
						AcroFields form = stamper.getAcroFields();

						String orderDate = order.getOrderDate().toString();
						String ordDate = orderDate.substring(0, 10);
						ordDate = reportDAO.getddmmyyyy(ordDate);
						// ordDate =ordDate.concat(orderDate.substring(10,11));
						ordDate = ordDate.concat(orderDate.substring(10, 20));
						String dueDate = order.getDueDate().toString();
						dueDate = reportDAO.getddmmyyyy(dueDate);

						// *************
						// Barcode barcode = BarcodeFactory.createEAN13("INV#"+order.getOrderId());
						String zord = "" + order.getOrderId();
						Barcode barcode = BarcodeFactory.createCode128(zord);
						barcode.setDrawingText(false);
						// LENGTH MUST BE EITHER 12 OR 13 OK
						String barfile = env.getProperty("invoice") + order.getOrderId() + ".png";
						File imgFile = new File(barfile);
						BarcodeImageHandler.savePNG(barcode, imgFile);
						// ***bar code fitting in the pdf file

						PdfContentByte content = stamper.getOverContent(reader.getNumberOfPages());
						try {
							if (new File(barfile).isFile()) {
								Image image1 = Image.getInstance(barfile);
								image1.setAbsolutePosition(50, 500);
								image1.scaleAbsolute(75, 20);
								content.addImage(image1);
							}
						} catch (Exception re) {
							// System.out.println(re);
							re.printStackTrace();
						}
						// header of the bill
						double crbalance = Math.round(order.getCreditBalance());
						int noOfSheet = order.getNoOfSheet() * order.getNoOfCopy();
						form.setField("LH1", (order.getCustName().toUpperCase()));
						form.setField("LH2", order.getCustAddr1().toUpperCase());
						form.setField("LH3", order.getCustAddr2().toUpperCase());
						form.setField("LH4", String.format("%.2f", crbalance));
						form.setField("PAGEH", "Page :");
						form.setField("GPAYNO", "GPay: " + env.getProperty("gpayno"));
						form.setField("COPY_COUNT", "Copies :" + Integer.toString(order.getNoOfCopy()));
						if (!order.getGstNo().trim().equals("99999") && !order.getGstNo().trim().equals(""))
							form.setField("LH5", order.getGstNo());
						form.setField("RH1", ordDate);
						form.setField("RH2", Long.toString(order.getOrderId()));
						form.setField("RH3", String.valueOf(noOfSheet));
						form.setField("RH4", dueDate);
						form.setField("TOTSHEET", "Sheets   :");
						form.setField("BPBYH", "Prepared By:");
						form.setField("MOB", "Enq:7010603123");

						// uname = getCookie(request, "empid");
						// System.out.println("USER NAME :"+uname);
						form.setField("COPY", "Duplicate Copy");

						// uname = getCookie(request, "empid");

						form.setField("BPBYV", uname);
						// Description of the Bill
						no = 1;
						// System.out.println("Count : "+count);
						for (i = count * 10; i < totitem; i++) {
							// System.out.println("I value : "+i);
							form.setField("SN" + String.valueOf(no), s[i][0]);
							form.setField("BL" + String.valueOf(no), s[i][1] + "   " + (s[i][2]));
							form.setField("AD" + String.valueOf(no), s[i][3].toUpperCase());
							form.setField("HSN" + String.valueOf(no), s[i][4].toString());
							form.setField("CR" + String.valueOf(no), s[i][5]);
							form.setField("LL" + String.valueOf(no), s[i][6]);
							form.setField("PAGEV", String.valueOf(count + 1 + " / " + totpage));
							no++;
						}

						if (count == totpage - 1) {
							// footer of the bill
							form.setField("CGSTH", "CGST");
							form.setField("IGSTH", "IGST");
							form.setField("SGSTH", "SGST");
							form.setField("TOTTAXH", "TOTAL GST");
							form.setField("TOTH", "TOTAL");
							form.setField("DISH", "DISCOUNT");
							form.setField("DLYH", "DELIVERY");
							form.setField("GSTH", "GST AMOUNT.");
							form.setField("ADVH", "ADVANCE");
							form.setField("BALH", "NET PAYABLE");
							double adv = 0.00;
							double delivery = 0.00;
							double discount = 0.00;
							double totalgst = 0.00;
							if (order.getOrderTrxnDetails().get(0).getSgst() != null
									&& order.getOrderTrxnDetails().get(0).getCgst() > 0.00) {
								totalgst = Math.round(((order.getOrderTrxnDetails().get(0).getSgst())
										+ (order.getOrderTrxnDetails().get(0).getCgst())));
							}
							if (order.getOrderTrxnDetails().get(0).getIgst() != null
									&& order.getOrderTrxnDetails().get(0).getIgst() > 0.00) {
								totalgst = Math.round(((order.getOrderTrxnDetails().get(0).getIgst())));
							}
							double balance = Math.round(order.getOrderTrxnDetails().get(0).getBalance());

							if (order.getOrderTrxnDetails().get(0).getDelvCharge() != null) {
								delivery = order.getOrderTrxnDetails().get(0).getDelvCharge();
							} else {
								delivery = 0.00;
							}

							if (order.getOrderTrxnDetails().get(0).getDiscount() != null) {
								discount = order.getOrderTrxnDetails().get(0).getDiscount();
							} else {
								discount = 0.00;
							}
							if (order.getOrderTrxnDetails().get(0).getAdvance() != null) {
								adv = order.getOrderTrxnDetails().get(0).getAdvance();
							} else {
								adv = 0.00;
							}
							double igst = 0.00;
							if (order.getOrderTrxnDetails().get(0).getIgst() != null)
								igst = order.getOrderTrxnDetails().get(0).getIgst();

							form.setField("TOTV", String.format("%.2f", order.getOrderTrxnDetails().get(0).getTotal()));
							form.setField("SGSTV", String.format("%.2f", order.getOrderTrxnDetails().get(0).getSgst()));
							form.setField("CGSTV", String.format("%.2f", order.getOrderTrxnDetails().get(0).getCgst()));
							form.setField("IGSTV", String.format("%.2f", igst));
							form.setField("TOTTAXV", String.format("%.2f", totalgst));
							form.setField("GSTV", String.format("%.2f", totalgst));
							form.setField("ADVV", String.format("%.2f", adv));
							form.setField("BALV", String.format("%.2f", (balance)));
							form.setField("DLYV", String.format("%.2f", delivery));
							form.setField("DISV", String.format("%.2f", discount));
							double nettotal = adv + balance;
							String amtWord;
							int netamt = (int) (Math.ceil(nettotal));
							amtWord = String.valueOf(netamt);
							// System.out.println("Amount :"+amtWord);
							amtWord = numberToWord(amtWord);
							if (amtWord.trim().length() == 0) {
								amtWord = "Zero";
							}
							amtWord = "(" + amtWord + " Rupees Only.)";
							form.setField("WORDV", amtWord);
						}

						stamper.close();
						if (totpage > 1) {
							List<File> files = new ArrayList<File>();
							for (i = 0; i < totpage; i++)
								files.add(new File(env.getProperty("invoice") + order.getOrderId() + "-" + i + ".pdf"));
							mergePDFFiles(files,
									String.valueOf(env.getProperty("invoice") + order.getOrderId() + ".pdf"));
						}
					}

					catch (Exception e) {
						e.printStackTrace();
					}
				} // end of for loop
			} else {
				// for mode 0
				for (int count = 0; count < totpage; count++) {
					if (totpage > 1) {

						pDoc = env.getProperty("invoice") + order.getOrderId() + "-" + count + ".pdf";
						pDoclocal = env.getProperty("invoice") + order.getOrderId() + "-" + count + "L.pdf";
					} else {
						pDoc = env.getProperty("invoice") + order.getOrderId() + ".pdf";
						pDoclocal = env.getProperty("invoice") + order.getOrderId() + "L.pdf";

					}

					try {
						PdfReader reader = new PdfReader(
								env.getProperty("invoice.template") + "//" + "VAISHNAVI_INV.pdf");
						PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pDoc));
						AcroFields form = stamper.getAcroFields();
						PdfReader readerlocal = new PdfReader(
								env.getProperty("invoice.template") + "//" + "LOCAL_INV.pdf");
						PdfStamper stamperlocal = new PdfStamper(readerlocal, new FileOutputStream(pDoclocal));
						AcroFields formlocal = stamperlocal.getAcroFields();

						String orderDate = order.getOrderDate().toString();
						String ordDate = orderDate.substring(0, 10);
						ordDate = reportDAO.getddmmyyyy(ordDate);
						// ordDate =ordDate.concat(orderDate.substring(10,11));
						ordDate = ordDate.concat(orderDate.substring(10, 20));
						String dueDate = order.getDueDate().toString();
						dueDate = reportDAO.getddmmyyyy(dueDate);

						// *************
						// Barcode barcode = BarcodeFactory.createEAN13("INV#"+order.getOrderId());
						String zord = "" + order.getOrderId();
						Barcode barcode = BarcodeFactory.createCode128(zord);
						barcode.setDrawingText(false);
						// LENGTH MUST BE EITHER 12 OR 13 OK
						String barfile = env.getProperty("invoice") + order.getOrderId() + ".png";
						File imgFile = new File(barfile);
						BarcodeImageHandler.savePNG(barcode, imgFile);
						// ***bar code fitting in the pdf file

						PdfContentByte content = stamper.getOverContent(reader.getNumberOfPages());
						try {
							if (new File(barfile).isFile()) {
								Image image1 = Image.getInstance(barfile);
								image1.setAbsolutePosition(50, 500);
								image1.scaleAbsolute(75, 20);
								content.addImage(image1);
							}
						} catch (Exception re) {
							// System.out.println(re);
							re.printStackTrace();
						}
						PdfContentByte contentlocal = stamperlocal.getOverContent(readerlocal.getNumberOfPages());
						try {
							if (new File(barfile).isFile()) {
								Image image1 = Image.getInstance(barfile);
								image1.setAbsolutePosition(50, 500);
								image1.scaleAbsolute(75, 20);
								contentlocal.addImage(image1);
							}
						} catch (Exception re) {
							// System.out.println(re);
							re.printStackTrace();
						}
						double crbalance = 0.0;
						// header of the bill
						if (order.getCreditBalance() != null)
							crbalance = Math.round(order.getCreditBalance());
						int noOfSheet = order.getNoOfSheet() * order.getNoOfCopy();
						form.setField("LH1", (order.getCustName().toUpperCase()));
						form.setField("LH2", order.getCustAddr1().toUpperCase());
						form.setField("LH3", order.getCustAddr2().toUpperCase());
						form.setField("LH4", Double.toString(crbalance));
						form.setField("PAGEH", "Page :");
						form.setField("GPAYNO", "GPay: " + env.getProperty("gpayno"));
						form.setField("COPY_COUNT", "Copies :" + Integer.toString(order.getNoOfCopy()));
						if (!order.getGstNo().trim().equals("99999") && !order.getGstNo().trim().equals(""))
							form.setField("LH5", order.getGstNo());
						form.setField("RH1", ordDate);
						form.setField("RH2", Long.toString(order.getOrderId()));
						form.setField("RH3", String.valueOf(noOfSheet));
						form.setField("RH4", dueDate);
						form.setField("TOTSHEET", "Sheets   :");
						form.setField("BPBYH", "Prepared By:");

						// uname = getCookie(request, "empid");
						form.setField("COPY", "Customer's Copy");
						form.setField("MOB", "Enq:7010603123 ");
						form.setField("BPBYV", uname);

						// header of the bill
						// double crbalance=Math.round(order.getCreditBalance());
						// int noOfSheet= order.getNoOfSheet() * order.getNoOfCopy();
						formlocal.setField("LH1", (order.getCustName().toUpperCase()));
						formlocal.setField("LH2", order.getCustAddr1().toUpperCase());
						formlocal.setField("LH3", order.getCustAddr2().toUpperCase());
						formlocal.setField("LH4", String.format("%.2f", crbalance));
						formlocal.setField("PAGEH", "Page :");
						if (!order.getGstNo().trim().equals("99999") && !order.getGstNo().trim().equals(""))
							formlocal.setField("LH5", order.getGstNo());
						formlocal.setField("RH1", ordDate);
						formlocal.setField("RH2", Long.toString(order.getOrderId()));
						formlocal.setField("RH3", String.valueOf(noOfSheet));
						formlocal.setField("RH4", dueDate);
						formlocal.setField("TOTSHEET", "Sheets   :");
						formlocal.setField("BPBYH", "Prepared By:");

						// uname = getCookie(request, "empid");
						formlocal.setField("COPY", "Office Copy");

						formlocal.setField("BPBYV", uname);
						// Description of the Bill
						no = 1;
						// System.out.println("Count : "+count);
						for (i = count * 10; i < totitem; i++) {
							// System.out.println("I value : "+i);
							form.setField("SN" + String.valueOf(no), s[i][0]);
							form.setField("BL" + String.valueOf(no), s[i][1] + "   " + (s[i][2]));
							form.setField("AD" + String.valueOf(no), s[i][3].toUpperCase());
							form.setField("HSN" + String.valueOf(no), s[i][4].toString());
							form.setField("CR" + String.valueOf(no), s[i][5]);
							form.setField("LL" + String.valueOf(no), s[i][6]);
							form.setField("PAGEV", String.valueOf(count + 1 + " / " + totpage));
							no++;
						}
						no = 1;
						for (i = count * 10; i < totitem; i++) {
							// System.out.println("I value : "+i);
							formlocal.setField("SN" + String.valueOf(no), s[i][0]);
							formlocal.setField("BL" + String.valueOf(no), s[i][1] + "   " + (s[i][2]));
							formlocal.setField("AD" + String.valueOf(no), s[i][3].toUpperCase());
							formlocal.setField("HSN" + String.valueOf(no), s[i][4].toString());
							formlocal.setField("CR" + String.valueOf(no), s[i][5]);
							formlocal.setField("LL" + String.valueOf(no), s[i][6]);
							formlocal.setField("PAGEV", String.valueOf(count + 1 + " / " + totpage));
							no++;
						}

						if (count == totpage - 1) {
							// footer of the bill
							form.setField("CGSTH", "CGST");
							form.setField("IGSTH", "IGST");
							form.setField("SGSTH", "SGST");
							form.setField("TOTTAXH", "TOTAL GST");
							form.setField("TOTH", "TOTAL");
							form.setField("DISH", "(-) DISCOUNT");
							form.setField("DLYH", "(+) DELIVERY");
							form.setField("GSTH", "(+) GST (12%)");
							form.setField("ADVH", "(-) ADVANCE");
							form.setField("BALH", "NET BALANCE");
							double adv = 0.00;
							double delivery = 0.00;
							double discount = 0.00;
							double totalgst = 0.00;
							if (order.getOrderTrxnDetails().get(0).getSgst() != null
									&& order.getOrderTrxnDetails().get(0).getCgst() > 0.00) {
								totalgst = Math.round(((order.getOrderTrxnDetails().get(0).getSgst())
										+ (order.getOrderTrxnDetails().get(0).getCgst())));

							}
							if (order.getOrderTrxnDetails().get(0).getIgst() != null
									&& order.getOrderTrxnDetails().get(0).getIgst() > 0.00) {
								totalgst = Math.round(((order.getOrderTrxnDetails().get(0).getIgst())));
							}

							double balance = Math.round(order.getOrderTrxnDetails().get(0).getBalance());

							if (order.getOrderTrxnDetails().get(0).getDelvCharge() != null) {
								delivery = order.getOrderTrxnDetails().get(0).getDelvCharge();
							} else {
								delivery = 0.00;
							}

							if (order.getOrderTrxnDetails().get(0).getDiscount() != null) {
								discount = order.getOrderTrxnDetails().get(0).getDiscount();
							} else {
								discount = 0.00;
							}
							if (order.getOrderTrxnDetails().get(0).getAdvance() != null) {
								adv = order.getOrderTrxnDetails().get(0).getAdvance();
							} else {
								adv = 0.00;
							}
							double igst = 0.00;
							if (order.getOrderTrxnDetails().get(0).getIgst() != null)
								igst = order.getOrderTrxnDetails().get(0).getIgst();

							form.setField("TOTV", String.format("%.2f", order.getOrderTrxnDetails().get(0).getTotal()));
							form.setField("SGSTV", String.format("%.2f", order.getOrderTrxnDetails().get(0).getSgst()));
							form.setField("CGSTV", String.format("%.2f", order.getOrderTrxnDetails().get(0).getCgst()));
							form.setField("IGSTV", String.format("%.2f", igst));
							form.setField("TOTTAXV", String.format("%.2f", totalgst));
							form.setField("GSTV", String.format("%.2f", totalgst));
							form.setField("ADVV", String.format("%.2f", adv));
							form.setField("BALV", String.format("%.2f", (balance)));
							form.setField("DLYV", String.format("%.2f", delivery));
							form.setField("DISV", String.format("%.2f", discount));
							double nettotal = adv + balance;
							String amtWord;
							int netamt = (int) (Math.ceil(nettotal));
							amtWord = String.valueOf(netamt);
							// System.out.println("Amount :"+amtWord);
							amtWord = numberToWord(amtWord);
							if (amtWord.trim().length() == 0) {
								amtWord = "Zero";
							}
							amtWord = "(" + amtWord + " Rupees Only.)";
							form.setField("WORDV", amtWord);
						}

						stamper.close();
						stamperlocal.close();
						if (totpage > 1) {
							List<File> files = new ArrayList<File>();
							for (i = 0; i < totpage; i++)
								files.add(new File(env.getProperty("invoice") + order.getOrderId() + "-" + i + ".pdf"));
							mergePDFFiles(files,
									String.valueOf(env.getProperty("invoice") + order.getOrderId() + ".pdf"));
						}
						if (totpage > 1) {
							List<File> files1 = new ArrayList<File>();
							for (i = 0; i < totpage; i++)
								files1.add(
										new File(env.getProperty("invoice") + order.getOrderId() + "-" + i + "L.pdf"));
							mergePDFFiles(files1,
									String.valueOf(env.getProperty("invoice") + order.getOrderId() + "L.pdf"));
						}
						List<File> files1 = new ArrayList<File>();
						files1.add(new File(env.getProperty("invoice") + order.getOrderId() + ".pdf"));
						files1.add(new File(env.getProperty("invoice") + order.getOrderId() + "L.pdf"));
						mergePDFFiles(files1,
								String.valueOf(env.getProperty("invoice") + order.getOrderId() + "M.pdf"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} // end of if statement for mode 0

		} catch (Exception _exception) {
			logger.error("printOrder exception ::" + _exception);
		}

		return "redirect:/download/{orderId}/{mode}";
	}

	/*
	 * @RequestMapping(value ="/printordercopy/{orderId}{mode}", method =
	 * RequestMethod.GET) public String printOrderCopy(ModelAndView
	 * modelAndView,@PathVariable String orderId,@PathVariable int mode){
	 * logger.info("-- printOrderCopy -- order id :" + orderId); try{ Order order=
	 * getOrder(orderId); order.setEditMode(true);
	 * 
	 * List<Product> sheet=metaDAO.getNoOfSheets(orderId); List<ProductType>
	 * productTypes = metaDAO.getProductTypes(); Map<Long,String> productTypeMap =
	 * Utils.getTypes(productTypes); modelAndView.addObject("productTypeMap",
	 * productTypeMap); modelAndView.addObject("order", order);
	 * modelAndView.addObject("product",sheet); }catch (Exception _exception) {
	 * logger.error("printOrder Copy ::" + _exception); }
	 * //modelAndView.setViewName("printorder"); return
	 * "redirect:/printorder/{orderId}/{mode}"; }
	 */
	private <T> T saveOrUpdate(T t) {
		try {
			t = customerDAO.saveOrUpdate(t);
		} catch (Exception _exception) {
			logger.error(_exception);
		}
		return t;
	}

	@RequestMapping(value = "/printCheckList/{orderId}", method = RequestMethod.GET)
	public ModelAndView printCheckList(ModelAndView modelAndView, @PathVariable long orderId) {
		logger.info("-- printCheckList -- order id :" + orderId);
		try {
			Order ord = orderDAO.getOrderById(orderId);
			Customer customer = customerDAO.getCustomerById(ord.getCustId());
			// System.out.println("customer.getMobileNo() :"+customer.getMobileNo());
			smsService.sendOrderCompletedSMS(String.valueOf(orderId), customer.getMobileNo().toString());
			/* TO UPDATE SMS IN ORDERS TABLE STARTS */
			if (ord.getSmsFlag() != null) {
				int x = ord.getSmsFlag();
				ord.setSmsFlag(++x);
			} else {
				ord.setSmsFlag(1);
			}
			saveOrUpdate(ord);
			/* TO UPDATE SMS IN ORDERS TABLE ENDS */
			List<ProductType> productTypes = metaDAO.getProductTypes();
			Map<Long, String> productTypeMap = Utils.getTypes(productTypes);
			Order order = getOrder(Long.toString(orderId));
			List<Product> sheet = metaDAO.getNoOfSheets(Long.toString(orderId));
			modelAndView.addObject("productTypeMap", productTypeMap);
			modelAndView.addObject("order", order);
			modelAndView.addObject("product", sheet);

		} catch (Exception _exception) {
			logger.error("printCheckList ::" + _exception);
		}
		modelAndView.setViewName("printchecklist");
		return modelAndView;
	}
//	@RequestMapping(value = "/printCheckListWA/{orderId}", method = RequestMethod.GET)
//	public ModelAndView printCheckListWA(ModelAndView modelAndView, @PathVariable long orderId) {
//		logger.info("-- printCheckListWA -- order id :" + orderId);
//		try {
//			Order ord = orderDAO.getOrderById(orderId);
//			Customer customer = customerDAO.getCustomerById(ord.getCustId());
//			// System.out.println("customer.getMobileNo() :"+customer.getMobileNo());
////			smsService.sendOrderCompletedSMS(String.valueOf(orderId), customer.getMobileNo().toString());
//			smsService.sendOrderCompletedWA(String.valueOf(orderId), customer.getMobileNo().toString());
//			/* TO UPDATE SMS IN ORDERS TABLE STARTS */
//			if (ord.getSmsFlag() != null) {
//				int x = ord.getSmsFlag();
//				ord.setSmsFlag(++x);
//			} else {
//				ord.setSmsFlag(1);
//			}
//			saveOrUpdate(ord);
//			/* TO UPDATE SMS IN ORDERS TABLE ENDS */
//			List<ProductType> productTypes = metaDAO.getProductTypes();
//			Map<Long, String> productTypeMap = Utils.getTypes(productTypes);
//			Order order = getOrder(Long.toString(orderId));
//			List<Product> sheet = metaDAO.getNoOfSheets(Long.toString(orderId));
//			modelAndView.addObject("productTypeMap", productTypeMap);
//			modelAndView.addObject("order", order);
//			modelAndView.addObject("product", sheet);
//			
//		} catch (Exception _exception) {
//			logger.error("printCheckList ::" + _exception);
//		}
//		modelAndView.setViewName("printchecklist");
//		return modelAndView;
//	}

	@RequestMapping(value = "/sendinvoiceemail/{orderId}/{mode}", method = RequestMethod.GET)
	public void sendInvoiceEmail(ModelAndView modelAndView, HttpServletRequest request, @PathVariable String orderId,
			@PathVariable int mode) {
		logger.info("-- printOrder -- order id :" + orderId + "Mode :" + mode);
		try {
			if (mode == 0) {
				Order order = getOrder(orderId);
				if (order != null)
					if (order.getEmail() != null && !order.getEmail().isEmpty())
						sendEmail(order.getOrderId(), order.getEmail(),
								order.getOrderTrxnDetails().get(0).getBalance());

			}
		} catch (Exception _exception) {
			logger.error("send Invoice Email ::" + _exception);

		}
		modelAndView.setViewName("workorderpayment");
		// return modelAndView;
	}

	@RequestMapping(value = "/orderdetails/{orderId}", method = RequestMethod.GET)
	public ModelAndView orderDetails(ModelAndView modelAndView, @PathVariable long orderId) {
		logger.info("-- orderdetails -- order id :" + orderId);

		OrderInformation orderinfo = new OrderInformation();
		Order myorder = new Order();
		OrderedItems orderedItems = new OrderedItems();
		try {
			Order ord = orderDAO.getOrderById(orderId);
			Customer customer = customerDAO.getCustomerById(ord.getCustId());
			List<ProductType> productTypes = metaDAO.getProductTypes();

			Map<Long, String> productTypeMap = Utils.getTypes(productTypes);
			Order order = getOrder(Long.toString(orderId));
			List<Product> sheet = metaDAO.getNoOfSheets(Long.toString(orderId));
			List<JobStatus> jobstatus = metaDAO.getJobStatus();
			Map<Integer, String> jobStatusMap = Utils.getJobStatusIdMap(jobstatus);

			List<ReceiptPayment> receiptPayments = paymentDAO.getReceiptPaymentsByOrderId(Long.toString(orderId));

			if (receiptPayments != null && !receiptPayments.isEmpty())
				modelAndView.addObject("receiptPayments", receiptPayments);

			modelAndView.addObject("jobStatusMap", jobStatusMap);
			modelAndView.addObject("productTypeMap", productTypeMap);
			modelAndView.addObject("order", order);
			modelAndView.addObject("product", sheet);

		} catch (Exception _exception) {
			logger.error("orderdetails ::" + _exception);
		}
		modelAndView.setViewName("orderinformation");
		return modelAndView;
	}

	private Order getOrder(String orderId) {
		Order order = null;
		if (!StringUtils.isEmpty(orderId)) {
			// order = orderDAO.getOrderById(Long.valueOf(orderId));
			order = orderDAO.getEditOrderById(Long.valueOf(orderId));
			if (order != null) {
				List<Product> products = customerDAO.getProductByOrderId(Long.valueOf(orderId));
				// System.out.println("Products count:"+products.size());
				List<DeliveryType> orderDeliveryType = orderDAO.getDeliveryType(orderId);

				for (Product product : products) {

					List<ProductSize> productSizes = metaDAO.getProductSizes((int) product.getProductTypeId());
					Map<Long, String> sizeMap = Utils.getTypes(productSizes);
					product.setSizeName(sizeMap.get((long) product.getSize()));
					List<ProductItemType> productItemTypes = metaDAO
							.getProductItemTypes((int) product.getProductTypeId());
					Map<Long, String> prodItemTypeMap = Utils.getTypes(productItemTypes);
					List<ProductItem> productItems = customerDAO.getProductItemsByProductId(product.getProductId());
					List<ProductItem> totalItems = customerDAO.getProductItemsByOrderId(Long.parseLong(orderId));
					order.setTotitem(totalItems.size());
					// System.out.println("order.getTotitem():"+order.getTotitem());
					if (productItems != null && !productItems.isEmpty())
						for (ProductItem productItem : productItems) {
							productItem.setProdItemName(prodItemTypeMap.get(productItem.getProdItemTypeId()));
						}
					product.setProductItems(productItems);
				}
				order.setProducts(products);
				List<OrderTrxnDetail> orderTrxnDetails = orderDAO.getTransactionByOrderId(Long.valueOf(orderId));
				if (orderTrxnDetails != null && !orderTrxnDetails.isEmpty())
					order.setOrderTrxnDetails(orderTrxnDetails);
				List<ReceiptPayment> receiptPayments = paymentDAO.getReceiptPaymentsByOrderId(orderId);
				Double receipts = 0.0;
				if (receiptPayments != null && !receiptPayments.isEmpty())
					for (ReceiptPayment receipt : receiptPayments)
						receipts += receipt.getPaidAmount();

				Customer customer = customerDAO.getCustomerById(order.getCustId());
				if (customer != null) {
					order.setCreditBalance(customer.getCreditBalance());
					order.setMobileNo(customer.getMobileNo());
					if (orderDeliveryType != null && !orderDeliveryType.isEmpty())
						order.setDeliveryType(orderDeliveryType.get(0).getName());
					// storing the total receipts of the orderID
					order.setBalancePayable(receipts);
					order.setEmail(customer.getEamil());
				}
				List<Product> sheet = metaDAO.getNoOfSheets(orderId);
				if (sheet != null && !sheet.isEmpty()) {
					if (sheet.get(0).getNoOfSheet() != null && sheet.get(0).getNoOfSheet() != 0)
						order.setNoOfSheet(sheet.get(0).getNoOfSheet());
					order.setNoOfCopy(sheet.get(0).getNoOfCopy());
				}
				/*
				 * else{ //order=null; return order; }
				 */
				return order;
			} else {
				order = null;
			}
		}
		return order;
	}

	private void sendEmail(Long orderId, String email, double balance) {
		// System.out.println("email :"+email);
		if (email != null) {
			// System.out.println("Hello from email block");
			String emailSubjectTxt = "Invoice copy Reg.";
			String emailMsgTxt = "\nDear Customer, \n";
			emailMsgTxt = emailMsgTxt + "hereby attaching your Invocie No." + orderId;
			emailMsgTxt = emailMsgTxt + ". Kindly pay the balance Rs." + balance;
			emailMsgTxt = emailMsgTxt + " while collecting the same.\n";
			emailMsgTxt = emailMsgTxt
					+ "for more details, kindly contact Our RECEPTION No. 7010603123 - CASH COUNTER No. 9865161234 - DELIVERY No. 8760241234  ";
			// emailMsgTxt = emailMsgTxt + "DATE FROM : \n";
			// ...................................................
			String[] emailList = { email };
			// ...................................................
			// String[] emailList = {"ldinakaran@gmail.com"};
			// ...................................................
			try {
				String path = env.getProperty("invoice");
				new EmailService().mailing(emailMsgTxt, emailSubjectTxt, "vaishnavidigital2@gmail.com", emailList, "",
						"", path + orderId + ".pdf", false);
				// new EmailService().mailing("MESSAGETEXT", "SUBJECTTEXT",
				// "vaishnavidigital2@gmail.com", emailList, "", "", "NOATTACHMENT", false);
				// JOptionPane.showMessageDialog(null,"Mail Sent Successfully !");
			} catch (Exception e) {
				e.printStackTrace();
			}
			// #####################################################################################
		}
	}

// merge pdf files
	public void mergePDFFiles(List<File> files, String mergedFileName) {
		try {
			PDFMergerUtility pdfmerger = new PDFMergerUtility();
			for (File file : files) {
				PDDocument document = PDDocument.load(file);
				pdfmerger.setDestinationFileName(mergedFileName);
				pdfmerger.addSource(file);
				pdfmerger.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());
				document.close();
			}
		} catch (IOException e) {
			logger.error("Error to merge files. Error: " + e.getMessage());
		}
	}

	public String getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		String username = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					Employee employee = userDAO.getEmployeeById(Long.parseLong(cookie.getValue()));
					if (employee != null) {
						username = employee.getName();
					}
				}

			}
		}
		return username;
	}

	public String getRoleCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		String deptid = "";
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					// System.out.println("COKKO+IEE VALUE : "+cookie.getValue());
					deptid = cookie.getValue();
					return deptid;
				} else {
					deptid = "Others";
				}
			}
		}
		return deptid;
	}

	/**/
	private void sendDBBackupEmail(String fname) {
		// System.out.println("email :"+email);
		if (fname != null) {
			System.out.println("Hello from email block");
			String emailSubjectTxt = "Daily DB Backup Reg.";
			String emailMsgTxt = "\nDear Customer, Your Database Backup attached here\n";
			String[] emailList = { "r.rajthalapathi@gmail.com" };

			try {
				String path = env.getProperty("backup");
				new EmailService().mailing(emailMsgTxt, emailSubjectTxt, "orteaor@gmail.com", emailList, "", "",
						path + fname + ".sql", false);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	/**/

// Number to word converter for Rupees in words
	private String numberToWord(String number) {
		String twodigitword = "";
		String word = "";
		String[] HTLC = { "", "Hundred", "Thousand", "Lakh", "Crore" }; // H-hundread , T-Thousand, ..
		int split[] = { 0, 2, 3, 5, 7, 9 };
		String[] temp = new String[split.length];
		boolean addzero = true;
		int len1 = number.length();
		if (len1 > split[split.length - 1]) {
//	System.out.println("Error. Maximum Allowed digits "+ split[split.length-1]);
			System.exit(0);
		}
		for (int l = 1; l < split.length; l++)
			if (number.length() == split[l])
				addzero = false;
		if (addzero == true)
			number = "0" + number;
		int len = number.length();
		int j = 0;
//spliting & putting numbers in temp array.
		while (split[j] < len) {
			int beg = len - split[j + 1];
			int end = beg + split[j + 1] - split[j];
			temp[j] = number.substring(beg, end);
			j = j + 1;
		}

		for (int k = 0; k < j; k++) {
			twodigitword = convertOnesTwos(temp[k]);
			if (k >= 1) {
				if (twodigitword.trim().length() != 0)
					word = twodigitword + " " + HTLC[k] + " " + word;
			} else
				word = twodigitword;
		}
		return (word);
	}

	private String convertOnesTwos(String t) {
		final String[] ones = { "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
				"Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen" };
		final String[] tens = { "", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty",
				"Ninety" };

		String word = "";
		int num = Integer.parseInt(t);
		if (num % 10 == 0)
			word = tens[num / 10] + " " + word;
		else if (num < 20)
			word = ones[num] + " " + word;
		else {
			word = tens[(num - (num % 10)) / 10] + word;
			word = word + " " + ones[num % 10];
		}
		return word;
	}

}
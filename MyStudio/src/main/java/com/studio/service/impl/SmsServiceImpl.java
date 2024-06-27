/**
 * 
 */
package com.studio.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.persistence.Entity;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.studio.controller.PrintController;
import com.studio.domain.DailyReportDetail;
import com.studio.domain.SmsData;
import com.studio.service.SmsService;
import com.studio.utils.StringUtils;

/**
 * @author ezhilraja_k
 *
 */

@Service
@PropertySource("classpath:studio.properties")
public class SmsServiceImpl implements SmsService {

	@Autowired
	private Environment env;
	
	Logger logger = Logger.getLogger(PrintController.class);
	
	public Boolean sendSMS(String billNo, String billDate, String amount, String advance, String balance,
			String mobileNumbers) {
		HttpClient client=null;
        PostMethod post=null;
//        String sURL;
        Boolean sendSuccess;
        try {
	        client = new HttpClient(new MultiThreadedHttpConnectionManager());
	        client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);

	        post = new PostMethod(env.getProperty("sms.url"));
	        post.addParameter("apikey", env.getProperty("sms.apikey"));
	        post.addParameter("clientid", env.getProperty("sms.clientid"));
	        post.addParameter("msisdn", mobileNumbers);
	        post.addParameter("sid",env.getProperty("sms.sid"));
	        
	        StringBuffer message = new StringBuffer();
	        message.append("Bill No:").append(billNo);
	        message.append(" Bill Date:").append(billDate);
	        message.append(" Bill Amount:").append(amount);
	        message.append(" Advance:").append(advance);
	        message.append(" Balance:").append(balance);
	        
	        post.addParameter("msg", message.toString());
	        post.addParameter("fl", "0");
	        post.addParameter("gwid", "2");
       
            int statusCode = client.executeMethod(post);
           // System.out.println(post.getStatusLine().toString());
            if (statusCode != HttpStatus.SC_OK) {
            	System.err.println("Method failed: " + post.getStatusLine());
            }
	       // System.out.println(post.getResponseBodyAsString());
	        sendSuccess=true;
        }catch (Exception e) {
            e.printStackTrace();
            sendSuccess=false;
        }finally {
            post.releaseConnection();
        }
        return sendSuccess;
	}
	
	public Boolean sendOrderCompletedSMS(String orderNo,String mobileNumbers) {
		HttpClient client=null;
        PostMethod post=null;
//        String sURL;
        Boolean sendSuccess;
        try {
        	//  System.out.println("from send OrderCompletedSMS function :");
	        client = new HttpClient(new MultiThreadedHttpConnectionManager());
	        client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);

	        post = new PostMethod(env.getProperty("sms.url"));
	        post.addParameter("apikey", env.getProperty("sms.apikey"));
	        post.addParameter("clientid", env.getProperty("sms.clientid"));
	        post.addParameter("msisdn", mobileNumbers);
	        post.addParameter("sid",env.getProperty("sms.sid"));
	        
	        StringBuffer message = new StringBuffer();
	        message.append("Dear Customer Your order :");
	        message.append(orderNo);
	        message.append(" is ready for delivery kindly pay the balance(if so), collect your articals Call:8760241234-Vaishnavi Digital Press");
	        post.addParameter("msg", message.toString());
	        post.addParameter("fl", "0");
	        post.addParameter("gwid", "2");
       
            int statusCode = client.executeMethod(post);
            System.out.println(post.getStatusLine().toString());
            if (statusCode != HttpStatus.SC_OK) {
            	System.err.println("Method failed: " + post.getStatusLine());
            }
	        System.out.println(post.getResponseBodyAsString());
	        sendSuccess=true;
        }catch (Exception e) {
            e.printStackTrace();
            sendSuccess=false;
        }finally {
            post.releaseConnection();
        }
        return sendSuccess;
	}
//	@SuppressWarnings("unchecked")
//	public Boolean sendOrderCompletedWA(String orderNo,String mobileNumbers) {
//		HttpClient client=null;
//		PostMethod post=null;
//		Boolean sendSuccess;
//		try {
//			JSONObject body = new JSONObject();
//			body.put("countryCode", "+91");
////			body.put("phoneNumber", mobileNumbers);
//			body.put("phoneNumber", "9443453320");
//			body.put("type", "Template");
//			
//			JSONObject template = new JSONObject();
//			template.put("name", "order_ready_");
//			template.put("languageCode", "en");
//			
//			JSONArray bodyValue = new JSONArray();
//			bodyValue.add(orderNo);
//			
//			template.put("bodyValues", bodyValue);
//			body.put("template", template);
//
//			logger.info("sample json format======" + body);
//
//			String auth = "Basic " + "LW1oSVVJeGY3ZjNvdDNkOU9PLXR2eEJnX04zVWlpTlN5NFRURVh1ZFlGbzo=";
//			URL postUrl = new URL("https://api.interakt.ai/v1/public/message/");
//			HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
//			connection.setRequestProperty("Authorization", auth);
//			connection.setRequestMethod("POST");
//			connection.setRequestProperty("Content-Type", "application/json");
//			connection.setUseCaches(false);
//			connection.setDoInput(true);
//			connection.setDoOutput(true);
//			
//			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
//			wr.write(body.toJSONString());
//			wr.close();
//			connection.connect();
//
//			int responseCode = connection.getResponseCode();
//			logger.info("responsecode=======" + responseCode);
//
//			if (responseCode == HttpURLConnection.HTTP_CREATED) {
//
//				StringBuffer jsonResponseData = new StringBuffer();
//				String readLine = null;
//				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//
//				while ((readLine = bufferedReader.readLine()) != null) {
//					jsonResponseData.append(readLine + "\n");
//				}
//
//				bufferedReader.close();
//				System.out.println(jsonResponseData.toString());
//
//			} else {
//				System.out.println(responseCode);
//			}
//			sendSuccess=true;
//		}catch (Exception e) {
//			e.printStackTrace();
//			sendSuccess=false;
//		}finally {
//			post.releaseConnection();
//		}
//		return sendSuccess;
//	}

	/* (non-Javadoc)
	 * @see com.studio.service.SmsService#sendDailyTrxnSMS(com.studio.domain.SmsData)
	 */
	public Boolean sendDailyTrxnSMS(SmsData smsData,DailyReportDetail dailyReportDetail) {
		HttpClient client=null;
        PostMethod post=null;
        Boolean sendSuccess;
        //System.out.println("from SMS Function");
        try {
	        client = new HttpClient(new MultiThreadedHttpConnectionManager());
	        client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);

	        post = new PostMethod(env.getProperty("sms.url"));
	        post.addParameter("apikey", env.getProperty("sms.apikey"));
	        post.addParameter("clientid", env.getProperty("sms.clientid"));
	        post.addParameter("msisdn", env.getProperty("sms.owner.mobile"));
	        post.addParameter("sid",env.getProperty("sms.sid"));
	        
	        StringBuffer message = new StringBuffer();
	        message.append("Tot.Order:").append(smsData.getNoOfOrders()!=null ? smsData.getNoOfOrders():0);
	      /*message.append(" Delivered:").append(smsData.getDelivered()!=null ? smsData.getDelivered():0);
	        message.append(" InProgress:").append(smsData.getInProgress()!=null ? smsData.getInProgress():0);
	        message.append(" UnProcessed:").append(smsData.getUnProcessed()!= null?smsData.getUnProcessed():0);*/
	        message.append(" Clo.Bal:").append(smsData.getcHand()!=null ? StringUtils.decimalFormat(smsData.getcHand()):0);
	        
	        message.append(" Tot.Bill:").append(dailyReportDetail.getStrOrderAmount()!=null?dailyReportDetail.getStrOrderAmount():0);
	        message.append(" Adv.:").append(dailyReportDetail.getStrOrderAdvanceAmount()!= null?dailyReportDetail.getStrOrderAdvanceAmount():0);
	        if(dailyReportDetail.getOrderAdvanceAmount()!=null)
	        	message.append(" Cr.Bill:").append(StringUtils.decimalFormat(dailyReportDetail.getOrderAmount()-dailyReportDetail.getOrderAdvanceAmount()));
	        else
	        	message.append(" Cr.Bill:").append(dailyReportDetail.getOrderAmount()!=null ? dailyReportDetail.getOrderAmount():0);
	        
	        message.append(" Receipt:").append(dailyReportDetail.getStrReceiptAmount()!= null?dailyReportDetail.getStrReceiptAmount():0);
	        if(dailyReportDetail.getOrderAdvanceAmount()!=null && dailyReportDetail.getReceiptAmount()!=null)
	        	message.append(" Collection:").append(StringUtils.decimalFormat(dailyReportDetail.getOrderAdvanceAmount() + dailyReportDetail.getReceiptAmount()));
	        else if(dailyReportDetail.getStrOrderAdvanceAmount()!=null && dailyReportDetail.getStrReceiptAmount() == null)
	        	message.append(" Collection:").append(dailyReportDetail.getStrOrderAdvanceAmount());
	        else if(dailyReportDetail.getStrOrderAdvanceAmount()==null && dailyReportDetail.getStrReceiptAmount() != null)
	        	message.append(" Collection:").append(dailyReportDetail.getStrReceiptAmount());
	        
	        message.append(" Purchase:").append(dailyReportDetail.getStrInvoiceAmount()!=null ? dailyReportDetail.getStrInvoiceAmount():0);
	        if(dailyReportDetail.getInvoicePaidAmount()!=null && dailyReportDetail.getOtherPayment()!=null)
	        	message.append(" Payment:").append(dailyReportDetail.getInvoicePaidAmount() + dailyReportDetail.getOtherPayment());
	        else if(dailyReportDetail.getInvoicePaidAmount()==null && dailyReportDetail.getOtherPayment()!=null)
	        	message.append(" Payment:").append(dailyReportDetail.getOtherPayment());
	        else if(dailyReportDetail.getInvoicePaidAmount()!=null && dailyReportDetail.getOtherPayment()==null)
	        	message.append(" Payment:").append(dailyReportDetail.getInvoicePaidAmount());
	      
	       // System.out.println("from Inside of SMS Function");
	        
	        post.addParameter("msg", message.toString());
	        post.addParameter("fl", "0");
	        post.addParameter("gwid", "2");
       
            int statusCode = client.executeMethod(post);
           // System.out.println(post.getStatusLine().toString());
            if (statusCode != HttpStatus.SC_OK) {
            	System.err.println("Method failed: " + post.getStatusLine());
            }
	        System.out.println(post.getResponseBodyAsString());
	        sendSuccess=true;
        }catch (Exception e) {
            e.printStackTrace();
            sendSuccess=false;
        }finally {
            post.releaseConnection();
        }
       // System.out.println("from Finall line of SMS Function");
        return sendSuccess;
	}
	
	// Added for customer's outstanding amount intimation 
	public Boolean sendOutstandingInformationSMS(String creditBalance,String mobileNumbers) {
		HttpClient client=null;
        PostMethod post=null;
//        String sURL;
        Boolean sendSuccess;
        //System.out.println("MobileNumbers : "+ mobileNumbers);
        try {
	        client = new HttpClient(new MultiThreadedHttpConnectionManager());
	        client.getHttpConnectionManager().getParams().setConnectionTimeout(30000); 
	        post = new PostMethod(env.getProperty("sms.url"));
	        post.addParameter("apikey", env.getProperty("sms.apikey"));
	        post.addParameter("clientid", env.getProperty("sms.clientid"));
	        post.addParameter("msisdn", mobileNumbers);
	        post.addParameter("sid",env.getProperty("sms.sid"));
	        
	        StringBuffer message = new StringBuffer();
	        message.append("Dear Customer,Rs.");
	        message.append(creditBalance);
	        message.append(" is due as on Today Kindly Pay the DUE AMOUNT at the earliest any queries call 7010603123-Vaishnavi Digital Press");
	        post.addParameter("msg", message.toString());
	        post.addParameter("fl", "0");
	        post.addParameter("gwid", "2");
       
            int statusCode = client.executeMethod(post);
            System.out.println(post.getStatusLine().toString());
            if (statusCode != HttpStatus.SC_OK) {
            	System.err.println("Method failed: " + post.getStatusLine());
            }
	        System.out.println(post.getResponseBodyAsString());
	        sendSuccess=true;
        }catch (Exception e) {
            e.printStackTrace();
            sendSuccess=false;
          
        }finally {
            post.releaseConnection();
        }
        return sendSuccess;
	}
	
	
	public Boolean sendCustomerVsBusinessSMS(String smsText, String mobileNumbers) {
		HttpClient client=null;
        PostMethod post=null;
//        String sURL;
        Boolean sendSuccess;
        try {
	        client = new HttpClient(new MultiThreadedHttpConnectionManager());
	        client.getHttpConnectionManager().getParams().setConnectionTimeout(30000); 
	        post = new PostMethod(env.getProperty("sms.url"));
	        post.addParameter("apikey", env.getProperty("sms.apikey"));
	        post.addParameter("clientid", env.getProperty("sms.clientid"));
	        post.addParameter("msisdn", mobileNumbers);
	        post.addParameter("sid",env.getProperty("sms.sid"));
	        
	        StringBuffer message = new StringBuffer();
	        smsText=smsText.replace('+',',');
	        smsText=smsText.replace('-','%');
	        message.append(smsText);
	  /*      message.append(creditBalance);
	        message.append(" We have MORE ADVANCED printing technologies to satisfy our customer's need, Your business is more valuable to us. We hope our services satisfies your needs, any queries, please call us on: 7010603123-Vaishnavi Digital Press");
	        */
	        post.addParameter("msg", message.toString());
	        post.addParameter("fl", "0");
	        post.addParameter("gwid", "2");
       
            int statusCode = client.executeMethod(post);
            System.out.println(post.getStatusLine().toString());
            if (statusCode != HttpStatus.SC_OK) {
            	System.err.println("Method failed: " + post.getStatusLine());
            }
	       System.out.println(post.getResponseBodyAsString());
	        sendSuccess=true;
        }catch (Exception e) {
            e.printStackTrace();
            sendSuccess=false;
          
        }finally {
            post.releaseConnection();
        }
        return sendSuccess;
	}
	

}

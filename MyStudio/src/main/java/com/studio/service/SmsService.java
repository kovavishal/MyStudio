/**
 * 
 */
package com.studio.service;

import com.studio.domain.DailyReportDetail;
import com.studio.domain.SmsData;

/**
 * @author ezhilraja_k
 *
 */
public interface SmsService {

	Boolean sendSMS(String billNo,String billDate,String amount,String advance,String balance,String mobileNumbers);
	Boolean sendOrderCompletedSMS(String orderNo,String mobileNumbers);
//	Boolean sendOrderCompletedWA(String orderNo,String mobileNumbers);
	Boolean sendDailyTrxnSMS(SmsData smsData,DailyReportDetail dailyReportDetail);
	Boolean sendOutstandingInformationSMS(String creditBalance,String mobileNumbers);
	Boolean sendCustomerVsBusinessSMS(String smsText, String mobileNumbers);
	
}

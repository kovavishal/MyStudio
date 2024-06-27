/**
 * 
 */
package com.studio.utils;

import javax.persistence.Entity;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * @author ezhilraja_k
 *
 */

public class SmsSender {
	
	public static void main(String[] args) {
//		sendSMS(null,null,null);
	}

	public static void sendSMS(String billNo,String billDate,String amount,String balance,String advance,String mobileNumbers){
		HttpClient client=null;
        PostMethod post=null;
        String sURL;
        client = new HttpClient(new MultiThreadedHttpConnectionManager());
        client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
        sURL = "http://45.127.101.185/vendorsms/pushsms.aspx";
        post = new PostMethod(sURL);
        post.addParameter("apikey", "b61bdb6b-af54-4c0f-9e1d-77a4139a7896");
        post.addParameter("clientid", "2cba6438-8159-416a-8c63-f76f26d8b959");
        post.addParameter("msisdn", mobileNumbers);
        post.addParameter("sid","VAISHN");
        String message ="Bill No:" + billNo + ":Bill Date:"+ billDate +":Bill Amount" + amount.toString()+ ": Advance:" 
        + advance.toString()+ ":Balance:"+ balance;
        post.addParameter("msg", message);
        post.addParameter("fl", "0");
        post.addParameter("gwid", "2");
        try {
                int statusCode = client.executeMethod(post);
                System.out.println(post.getStatusLine().toString());
                if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + post.getStatusLine());
            }
            System.out.println(post.getResponseBodyAsString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            post.releaseConnection();
        }
    }
	}

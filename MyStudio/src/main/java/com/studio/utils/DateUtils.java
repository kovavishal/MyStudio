/**
 * 
 */
package com.studio.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

/**
 * @author ezhilraja_k
 *
 */

public class DateUtils {
	static Logger logger = Logger.getLogger(DateUtils.class);

	public static String toDate(Date date){
		return toDate(date, false);
	}
	public static String toDate(Date date,Boolean dateOnly){
		String dateStr=null;
		try{
			if(date!=null){
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
				if(dateOnly)
					sdf = new SimpleDateFormat("dd/MM/yyyy");
				dateStr = sdf.format(date);
			}
			
		}catch (Exception _exception) {
			logger.error(_exception);
		}
		return dateStr;
	}
	public static Date toDate(String date){
	
		return toDate(date, false);
	}
	
	public static Date toDate(String date, Boolean dateOnly){
		Date dateObject=null;
		try{
			if(!StringUtils.isEmpty(date)){
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
				if(dateOnly)
					sdf = new SimpleDateFormat("dd/MM/yyyy");
				dateObject = sdf.parse(date);
			}
		}catch (Exception _exception) {
			logger.error(_exception);
		}
		return dateObject;
	}
	
	public static String todayDateTime(){
       DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
       Date date = new Date();
       return dateFormat.format(date);
	}
	
	public static String todayDate(){
       DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
       Date date = new Date();
       return dateFormat.format(date);
	}

	public static Date beforeDate(Integer noOfDay) {
	    final Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DATE, -noOfDay);
	    return cal.getTime();
    }
    
    public static String beforeDateStr(Integer noOfDay) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        return dateFormat.format(beforeDate(noOfDay));
    }
    
    public static Date beforeDate(Date date,Integer noOfDay) {
	    final Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    cal.add(Calendar.DATE, -noOfDay);
	    return cal.getTime();
    }
	
}

/**
 * 
 */
package com.studio.utils;

import java.text.DecimalFormat;
import javax.persistence.Entity;

/**
 * @author ezhilraja_k
 *
 */

public class StringUtils {

	
	public static Boolean isNull(String s){
		if(s==null)
			return true;
		else
			return false;
	}
	
	public static Boolean isNotNull(String s){
		return !isNull(s);
	}
	
	public static Boolean isEmpty(String s){
		if(s==null)
			return true;
		else if (s.trim().length()>0)
			return false;
		else
			return true;
	}
	
	public static Boolean isNotEmpty(String s){
		return !isEmpty(s);
	}
	
	public static String decimalFormat(Double value){
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(value);
	}
}

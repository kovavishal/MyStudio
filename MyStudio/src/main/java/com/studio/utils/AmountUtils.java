/**
 * 
 */
package com.studio.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.persistence.Entity;

/**
 * @author ezhilraja_k
 *
 */

public class AmountUtils {

	public static Double round(Double value, int places){
		BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}

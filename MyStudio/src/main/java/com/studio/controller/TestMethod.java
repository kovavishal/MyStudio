/**
 * 
 */
package com.studio.controller;

import java.lang.reflect.Method;

import com.studio.domain.PaymentType;
import javax.persistence.Entity;

/**
 * @author ezhilraja_k
 *
 */

public class TestMethod {

	public static void main(String[] args) {
		try{
		Object metaObject = new PaymentType();
		Method method = metaObject.getClass().getMethod("setName",String.class);
		method.invoke(metaObject,"ezhil");
		System.out.println(((PaymentType)metaObject).getName());
	}catch (Exception _exception) {
		_exception.printStackTrace();
	}
	}
}

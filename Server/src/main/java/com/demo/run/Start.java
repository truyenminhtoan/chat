/*
 * @(#)Start.java	1.0  22-06-2015 
 *
 * Copyright 2015 Viettel Telecome. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.demo.run;


import org.apache.log4j.xml.DOMConfigurator;

import com.demo.context.AppContext;

/**
 * Mô tả đoạn code bên dưới
 * 
 * @author: toantm1
 * @since: 22-06-2015
 */
public class Start {

	/**
	 * TODO
	 *
	 * @author: toantm1
	 * @since: 22-06-2015
	 * @Param: @param args
	 * @Return: void
	 * @Description: Mo ta cho phan chinh sua (neu co)
	 * @Modified by: Nguoi chinh sua (nếu có)
	 * @Modified Date: Ngay chinh sua (nếu có)
	 */
	public static void main(String[] args) {
		DOMConfigurator.configure("../etc/log4j.xml");
		AppContext.getInstance();
	}
}

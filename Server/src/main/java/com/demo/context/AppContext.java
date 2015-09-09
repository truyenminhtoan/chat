/**
 * @(#)AppContext.java.java	1.0  10-10-2014 
 *
 * Copyright 2014 sgreen Telecome. All rights reserved.
 * sgreen PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.demo.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;


/**
 *  READ SPRING CONTENT
 *  @author: toantm1
 *  @since: 10-10-2014 - 16:59:42
 */
public class AppContext {
	private static AppContext instance;
    private ApplicationContext context;

    public static AppContext getInstance() {
        if (instance == null) {
            instance = new AppContext();
        }
        return instance;
    }

    public AppContext() {
        context = new FileSystemXmlApplicationContext(
                new String[]{"../etc/boot-file.xml"});
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public ApplicationContext getContext() {
        return context;
    }
}

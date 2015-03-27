package com.sapienter;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by marcolin on 13/03/15.
 */
public class SpringContext {
    private static ClassPathXmlApplicationContext classPathXmlApplicationContext;
    public static void init() {
         classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:/spring-context.xml");
    }

    public static <T> T bean(String beanName) {
        return (T) classPathXmlApplicationContext.getBean(beanName);
    }
}

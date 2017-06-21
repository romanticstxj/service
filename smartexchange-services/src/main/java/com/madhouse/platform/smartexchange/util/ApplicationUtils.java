package com.madhouse.platform.smartexchange.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by Administrator on 2015/7/6.
 */
public class ApplicationUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationUtils.applicationContext = applicationContext;
        for (String s : applicationContext.getBeanDefinitionNames()) {
            System.out.println(s);
        }
    }
}

package org.jmxdatamart.testwebapp;

import javax.management.*;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.lang.management.ManagementFactory;

public class RegisterMBeansOnWebAppStartup implements ServletContextListener {

  private final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

  public void contextInitialized(ServletContextEvent servletContextEvent) {
    TestWebAppMBean mBean = new TestWebAppMBean();
    try {
      mBeanServer.registerMBean(mBean, new ObjectName("org.jmxdatamart:Type=TestWebAppMBean"));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void contextDestroyed(ServletContextEvent servletContextEvent) {

  }
}

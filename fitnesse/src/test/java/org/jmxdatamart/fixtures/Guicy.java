package org.jmxdatamart.fixtures;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

/**
 * A enum that initializes the Guice Injector in a thread-safe manner
 */
public enum Guicy {
  INJECTOR {
    @Override
    Injector getInjector() {
      return Guice.createInjector(new AbstractModule() {
        @Override
        protected void configure() {
          bind(MBeanServer.class).toInstance(ManagementFactory.getPlatformMBeanServer());
        }
      });
    }
  };

  abstract Injector getInjector();
}

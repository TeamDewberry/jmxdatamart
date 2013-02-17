package org.jmxdatamart.fixtures;

import dbfit.DerbyTest;

/**
 * This do fixture wraps all of the other fixtures needed to perform an end-to-end
 * extract and load test for the JMX Data Mart.
 */
public class ExtractAndLoadTest extends DerbyTest {

  public CreateMBeans createMBeans() {
    return new CreateMBeans();
  }

  public ConfigureMBeans configureMBeans() {
    return new ConfigureMBeans();
  }

  public ConfigureExtractor configureExtractor() {
    return new ConfigureExtractor();
  }

  public ConfigureMBeansToBeExtracted configureMBeansToBeExtracted() {
    return new ConfigureMBeansToBeExtracted();
  }

  public void collectStatisticsFor(int collectTime, String timeUnit) {

  }

  public LoadStatistics loadStatistics() {
    return new LoadStatistics();
  }

  public ExtractAndLoadTest getOrgDotJmxdatamartDotFixturesDotExtractAndLoadTest() {
    // The particular version of fitnesse which is published to the central Maven
    // repository requires this method.  Lame.
    return this;
  }
}

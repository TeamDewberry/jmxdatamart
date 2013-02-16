package org.jmxdatamart.fixtures;

import fitlibrary.DoFixture;

/**
 * This do fixture wraps all of the other fixtures needed to perform an end-to-end
 * extract and load test for the JMX Data Mart.
 */
public class ExtractAndLoadTest extends DoFixture {

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

  public void collectStatisticsForSeconds(int collectTime) {

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

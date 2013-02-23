package org.jmxdatamart.Extractor;

import org.junit.Test;

import java.io.InputStream;

public class ExtractionWithXmlFileIT {

  @Test
  public void extractTestWebAppMXBean() throws Exception {
    InputStream xmlConfiguration = getXmlConfiguration("extractTestWebAppMXBean.xml");
    Settings settings = Settings.fromXML(xmlConfiguration);
    Extractor extractor = new Extractor(settings);
    extractor.extract();
  }

  private InputStream getXmlConfiguration(String xmlFileName) {
    return getClass().getResourceAsStream(xmlFileName);
  }

}

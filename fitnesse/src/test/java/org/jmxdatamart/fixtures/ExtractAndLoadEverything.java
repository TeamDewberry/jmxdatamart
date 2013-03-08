package org.jmxdatamart.fixtures;

import fitlibrary.DoFixture;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class ExtractAndLoadEverything extends DoFixture{
  public void extractUsingSettings(String settings) {
    org.jmxdatamart.Extractor.Main.main(new String[] {settings});
  }
  
  public void loadWithSettingsFromURLUserNamePassword(String setting, String folder, String url, String user, String password){
    org.jmxdatamart.Loader.MainForFitnesse.main(new String[]{setting, folder, url, user, password});
  }
}

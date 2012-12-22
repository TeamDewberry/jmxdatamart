package org.jmxdatamart.fixtures;

import fitlibrary.SetUpFixture;

public class GuicySetUpFixture extends SetUpFixture {

  public GuicySetUpFixture() {
    Guicy.INJECTOR.getInjector().injectMembers(this);
  }

}

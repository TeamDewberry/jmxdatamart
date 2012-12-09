package org.jmxdatamart.fixtures;

import fit.ColumnFixture;

public class GuicyColumnFixture extends ColumnFixture {

  public GuicyColumnFixture() {
    Guicy.INJECTOR.getInjector().injectMembers(this);
  }

}

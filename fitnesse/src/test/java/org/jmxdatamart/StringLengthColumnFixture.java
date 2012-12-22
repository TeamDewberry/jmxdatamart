package org.jmxdatamart;

import fit.ColumnFixture;

public class StringLengthColumnFixture extends ColumnFixture {

  public String string;

  public int length() {
    return string.length();
  }

}

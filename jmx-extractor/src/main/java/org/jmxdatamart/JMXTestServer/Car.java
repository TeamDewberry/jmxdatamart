/*
 * Copyright (c) 2012 Tripwire, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *  o Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.jmxdatamart.JMXTestServer;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class Car {
  
  public static final String NAME = "2014 Chevy Corvette Stingray";
  public static final String AUTOSHOW = "2013 Detroit";
  public static final int ENGINE = 8;
  public static final int POWER = 450;

  private String name;
  private String autoShow;
  private int engine;
  private int power;

  public Car() {
    this.name = NAME;
    this.autoShow = AUTOSHOW;
    this.engine = ENGINE;
    this.power = POWER;
  }

  public Car(String name, String autoShow, int engine, int power) {
    this.name = name;
    this.autoShow = autoShow;
    this.engine = engine;
    this.power = power;
  }

  public String getName() {
    return name;
  }

  public String getAutoShow() {
    return autoShow;
  }

  public int getEngine() {
    return engine;
  }

  public int getPower() {
    return power;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAutoShow(String autoShow) {
    this.autoShow = autoShow;
  }

  public void setEngine(int engine) {
    this.engine = engine;
  }

  public void setPower(int power) {
    this.power = power;
  }
}

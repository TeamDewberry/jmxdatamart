/*
 * Copyright (c) 2012, Tripwire, Inc.
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

import java.util.Date;
import java.util.Random;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class TestBean implements TestBeanMBean{

    Integer a;
    Long b;
    Random prng;
    boolean aBool;
    Date aDate;

    public TestBean() {
        a = new Integer(0);
        b = new Long(-1);
        prng = new Random();
        aBool = true;
        aDate = new Date(0xdeadbeef);
    }
    
    @Override
    public int getA() {
        return a;
    }

    @Override
    public void setA(int obj) {
        a = obj;
    }

    public void randomize() {
        a = prng.nextInt(100);
    }
    
    @Override
    public String toString() {
        return "A = " + a.toString();
    }

    @Override
    public long getB() {
        return b;
    }

    @Override
    public void setB(long obj) {
        b = obj;
    }

  @Override
  public boolean getBoolVar() {
    return this.aBool;
  }

  @Override
  public void setBoolVar(boolean aBool) {
    this.aBool = aBool;
  }

  @Override
  public Date getDateVar() {
    return this.aDate;
  }

  @Override
  public void setDateVar(Date aDate) {
    this.aDate = aDate;
  }

}

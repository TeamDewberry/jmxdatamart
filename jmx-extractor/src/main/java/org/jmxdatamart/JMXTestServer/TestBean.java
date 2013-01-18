/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jmxdatamart.JMXTestServer;

import java.util.Random;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class TestBean implements TestBeanMBean{

    Integer a;
    Random prng;

    public TestBean() {
        a = new Integer(0);
        prng = new Random();
    }
    
    @Override
    public Object getA() {
        return a;
    }

    @Override
    public void setA(Object obj) {
        if (obj instanceof Integer) {
            a = (Integer)obj;
        }
    }

    public void randomize() {
        a = prng.nextInt(100);
    }
    
    @Override
    public String toString() {
        return "A = " + a.toString();
    }

}

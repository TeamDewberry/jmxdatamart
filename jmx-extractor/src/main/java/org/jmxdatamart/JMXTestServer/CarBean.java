/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmxdatamart.JMXTestServer;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class CarBean implements CarMXBean{
    private Car dream;
    
    public static final String name = "2014 Chevy Corvette Stingray";
    public static final String year = "2013 Detroit auto show";
    public static final int engine = 8;
    public static final int power = 450;
    
    public CarBean() {
        dream = new Car(name , year, engine, power);
    }

    @Override
    public Car getCar() {
        return dream;
    }
}

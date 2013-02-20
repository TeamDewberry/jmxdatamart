/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmxdatamart.Extractor.Setting;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class MBeanDataTest {

    @Test
    public void testCheck() {
        MBeanData mbd = new MBeanData("RandomName", "ThisIsNotAnAlias", null, true);
        assertTrue(mbd.check());
        mbd = new MBeanData("RandomName", "!ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "\"ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "#ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "$ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "%ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "&ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "'ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "(ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", ")ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "*ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "+ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", ",ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "-ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", ".ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "/ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", ":ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", ";ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "<ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "=ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", ">ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "?ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "@ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "[ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "\\ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "]ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "^ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "_ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "`ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "{ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "|ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "}ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", "~ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
        mbd = new MBeanData("RandomName", " ThisIsNotAnAlias", null, true);
        assertFalse(mbd.check());
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmxdatamart.Extractor.Setting;

import org.jmxdatamart.Extractor.Attribute;
import org.jmxdatamart.common.DataType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class AttributeTest {

    @Test
    public void testCheck() {
        Attribute a;
        a = new Attribute("randomName", "TestBean", DataType.BYTE);
        assertTrue(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "", DataType.BYTE);
        assertTrue(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", null, DataType.BYTE);
        assertTrue(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "ID", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "TimeCollected", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "!@#$%^&*()_+=", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "!TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "\"TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "#TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "$TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "%TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "&TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "'TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "(TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", ")TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "*TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "+TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", ",TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "-TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", ".TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "/TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", ":TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", ";TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "<TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "=TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", ">TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "?TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "@TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "[TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "\\TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "]TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "^TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "_TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "`TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "{TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "|TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "}TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
        a = new Attribute("edu.jmx:type=TestBean", "~TestBean", DataType.BYTE);
        assertFalse(a.checkForValidAlias());
    }
}

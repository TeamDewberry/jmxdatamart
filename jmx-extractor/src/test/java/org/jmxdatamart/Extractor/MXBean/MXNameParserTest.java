/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmxdatamart.Extractor.MXBean;

import org.jmxdatamart.Extractor.MXBean.MXNameParser;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public class MXNameParserTest {

    public MXNameParserTest() {
    }

    /**
     * Test for basic case that consume whole string as one token. This
     * primarily test hasNext().
     */
    @Test
    public void testBasicCase() {
        MXNameParser instance = new MXNameParser("ABC");

        for (int i = 0; i < 100; ++i) {
            assertTrue((new Integer(i)).toString(), instance.hasNext());
        }

        assertThat(instance.nextToken(), equalTo("ABC"));

        for (int i = 0; i < 100; ++i) {
            assertFalse((new Integer(i)).toString(), instance.hasNext());
        }

        instance = new MXNameParser("");

        for (int i = 0; i < 100; ++i) {
            assertFalse((new Integer(i)).toString(), instance.hasNext());
        }

        instance = new MXNameParser(null);

        for (int i = 0; i < 100; ++i) {
            assertFalse((new Integer(i)).toString(), instance.hasNext());
        }
    }

    /**
     * Test for case where string contains multiple tokens that separated by a
     * dot.
     */
    @Test
    public void testDotSeparator() {
        MXNameParser instance = new MXNameParser("AA.AB.CC");

        for (int i = 0; i < 100; ++i) {
            assertTrue((new Integer(i)).toString(), instance.hasNext());
        }

        assertThat(instance.nextToken(), equalTo("AA"));

        for (int i = 0; i < 100; ++i) {
            assertTrue((new Integer(i)).toString(), instance.hasNext());
        }

        assertThat(instance.nextToken(), equalTo("AB"));

        for (int i = 0; i < 100; ++i) {
            assertTrue((new Integer(i)).toString(), instance.hasNext());
        }

        assertThat(instance.nextToken(), equalTo("CC"));

        for (int i = 0; i < 100; ++i) {
            assertFalse((new Integer(i)).toString(), instance.hasNext());
        }
    }

    /**
     * Test ability to handle quote character.
     */
    @Test
    public void testQuote() {
        MXNameParser instance = new MXNameParser("AA.\"BB\"");

        for (int i = 0; i < 100; ++i) {
            assertTrue((new Integer(i)).toString(), instance.hasNext());
        }

        assertThat(instance.nextToken(), equalTo("AA"));

        for (int i = 0; i < 100; ++i) {
            assertTrue((new Integer(i)).toString(), instance.hasNext());
        }

        assertThat(instance.nextToken(), equalTo("BB"));

        for (int i = 0; i < 100; ++i) {
            assertFalse((new Integer(i)).toString(), instance.hasNext());
        }

        instance = new MXNameParser("AA.AC.\"BB.CC\".DD.EE");

        for (int i = 0; i < 100; ++i) {
            assertTrue((new Integer(i)).toString(), instance.hasNext());
        }

        assertThat(instance.nextToken(), equalTo("AA"));

        for (int i = 0; i < 100; ++i) {
            assertTrue((new Integer(i)).toString(), instance.hasNext());
        }

        assertThat(instance.nextToken(), equalTo("AC"));

        for (int i = 0; i < 100; ++i) {
            assertTrue((new Integer(i)).toString(), instance.hasNext());
        }

        assertThat(instance.nextToken(), equalTo("BB.CC"));

        for (int i = 0; i < 100; ++i) {
            assertTrue((new Integer(i)).toString(), instance.hasNext());
        }

        assertThat(instance.nextToken(), equalTo("DD"));

        for (int i = 0; i < 100; ++i) {
            assertTrue((new Integer(i)).toString(), instance.hasNext());
        }

        assertThat(instance.nextToken(), equalTo("EE"));

        for (int i = 0; i < 100; ++i) {
            assertFalse((new Integer(i)).toString(), instance.hasNext());
        }

    }

    /**
     * Go for style points.
     */
    @Test
    public void testCornerCases() {
        // Binh's note: these are unexpected behaviors. Feel free to redefine it
        // as you see fit. Easiest way would be to throw an exception.        
        
        MXNameParser instance;
        
        // case 1: no close quote.
        try {
            instance = new MXNameParser("AA.\"BB.CC");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage(), equalTo("Not a valid pattern - uneven quote"));
        }

        // case 2: quote is also separator
        try {
            instance = new MXNameParser("AA\"BB.CC\"");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage(), equalTo("Quote sign indicates key without TabularData name"));
        }
        
        try {
            instance = new MXNameParser("\"AABB.CC\"");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage(), equalTo("Attribute can not starts with TabularData key"));
        }
        
        // case 3: end with a separator
        instance = new MXNameParser("AA.CC.");
        assertTrue(instance.hasNext());
        assertThat(instance.nextToken(), equalTo("AA"));
        assertTrue(instance.hasNext());
        assertThat(instance.nextToken(), equalTo("CC"));
        assertFalse(instance.hasNext());
        
        // case 4: end with a quote
        instance = new MXNameParser("AA.CC");
        assertTrue(instance.hasNext());
        assertThat(instance.nextToken(), equalTo("AA"));
        assertTrue(instance.hasNext());
        assertThat(instance.nextToken(), equalTo("CC"));
        assertFalse(instance.hasNext());
    }
}

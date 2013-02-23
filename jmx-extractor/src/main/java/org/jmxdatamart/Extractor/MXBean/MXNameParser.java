/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmxdatamart.Extractor.MXBean;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Binh Tran <mynameisbinh@gmail.com>
 */
public final class MXNameParser {

    private final Pattern nameRegex = Pattern.compile("\"([^\"]*)\"|(?<=\\.|^)([^\\.]*)(?=\\.|$)");
    private String input;
    private List<String> matches = new ArrayList<String>();
    private int curr = 0;

    public MXNameParser(String s) {
        if (s == null) {
            matches = null;
        } else {
            checkValidPattern(s);
            Matcher matcher = nameRegex.matcher(s);
            ArrayList<String> temp = new ArrayList<String>();
            while (matcher.find()) {
                String m = matcher.group(1) == null
                        ? matcher.group(0)
                        : matcher.group(1);
                if (m == null) {
                    throw new IllegalArgumentException(s + " is not a valid attribute name");
                }
                temp.add(m);
            }
            for (String str : temp) {
                if (!str.isEmpty()) {
                    matches.add(str);
                }
            }
        }
    }

    public void checkValidPattern(String s) {
        int strlen = s.length();
        int quoteCnt = 0;
        for (int i = 0; i < strlen; ++i) {
            if (s.charAt(i) == '"') {
                ++quoteCnt;
            }
        }
        if (quoteCnt % 2 != 0) {
            throw new IllegalArgumentException("Not a valid pattern - uneven quote");
        }

        quoteCnt = 0;

        for (int i = 0; i < strlen; ++i) {
            if (s.charAt(i) == '"') {
                if (quoteCnt == 1) {
                    if ((i == strlen - 1) || (s.charAt(i + 1) == '.')) {
                        --quoteCnt;
                    } else {
                        throw new IllegalArgumentException("Char '\"' not followed by dot nor EndOfString");
                    }
                } else {
                    if (i == 0) {
                        throw new IllegalArgumentException("Attribute can not starts with TabularData key");
                    } else if (s.charAt(i - 1) != '.') {
                        throw new IllegalArgumentException("Quote sign indicates key without TabularData name");
                    } else {
                        ++quoteCnt;
                    }
                }
            }
        }
    }

    public boolean hasNext() {
        return (matches != null) && (curr < matches.size());
    }

    public String nextToken() {
        return matches.get(curr++);
    }
}

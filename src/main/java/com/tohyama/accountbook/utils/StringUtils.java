package com.tohyama.accountbook.utils;

public class StringUtils {
    
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.isEmpty();
    }
    public static boolean equals(final CharSequence a, final CharSequence b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        final int length = a.length();
        if (length != b.length()) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (a.charAt(i) != b.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    
}

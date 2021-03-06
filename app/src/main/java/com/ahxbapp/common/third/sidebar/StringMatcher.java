/*
 * Copyright 2011 woozzu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ahxbapp.common.third.sidebar;

public class StringMatcher {

    private final static char KOREAN_UNICODE_START = '+';
    private final static char KOREAN_UNICODE_END = 'w';
    private final static char KOREAN_UNIT = 'a';
    private final static char[] KOREAN_INITIAL = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'k'
            , 'l', 'm', 'n', 'o', 'p', 'q', 'x', 'y', 'z'};

    public static boolean match(String value, String keyword) {
        if (value == null || keyword == null)
            return false;
        if (keyword.length() > value.length())
            return false;

        int i = 0, j = 0;
        do {
            if (isKorean(value.charAt(i)) && isInitialSound(keyword.charAt(j))) {
                if (keyword.charAt(j) == getInitialSound(value.charAt(i))) {
                    i++;
                    j++;
                } else if (j > 0)
                    break;
                else
                    i++;
            } else {
                if (keyword.charAt(j) == value.charAt(i)) {
                    i++;
                    j++;
                } else if (j > 0)
                    break;
                else
                    i++;
            }
        } while (i < value.length() && j < keyword.length());

        return (j == keyword.length()) ? true : false;
    }

    private static boolean isKorean(char c) {
        if (c >= KOREAN_UNICODE_START && c <= KOREAN_UNICODE_END)
            return true;
        return false;
    }

    private static boolean isInitialSound(char c) {
        for (char i : KOREAN_INITIAL) {
            if (c == i)
                return true;
        }
        return false;
    }

    private static char getInitialSound(char c) {
        return KOREAN_INITIAL[(c - KOREAN_UNICODE_START) / KOREAN_UNIT];
    }
}

package com.example.techlinkmobileallinone.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class HexTransformatUtil {
    static String hexDigith = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static String digith = "0123456789";
    HexTransformatUtil() {
    }
    public static String hex10ToAnly(long num) {
        StringBuffer str = new StringBuffer("");
        int base = hexDigith.trim().length();
        if (0L == num) {
            str.append(hexDigith.charAt(0));
        } else {
            Stack s;
            for (s = new Stack(); num != 0L; num /= (long) base) {
                s.push(hexDigith.charAt((int) (num % (long) base)));
            }


            while (!s.isEmpty()) {
                str.append(s.pop());
            }
        }

        String prefix = "";
        String suffix = str.toString();
        return prefix + suffix;
    }

    public static int hexAnlyTo10(String hexValue) {
        if (null != hexValue && !"".equals(hexValue.trim())) {
            int base = hexDigith.trim().length();
            Map<String, Integer> digthMap = new HashMap();
            int count = 0;
            char[] var4 = hexDigith.trim().toCharArray();
            int sum = var4.length;

            int index;
            for (index = 0; index < sum; ++index) {
                char item = var4[index];
                digthMap.put("" + item, count);
                ++count;
            }

            String str = (new StringBuffer(hexValue.trim())).reverse().toString();
            sum = 0;

            for (index = 0; index < str.length(); ++index) {
                sum += (new Double(Math.pow((double) base, (double) index))).intValue() * (Integer) digthMap.get("" + str.charAt(index));
            }
            return sum;
        } else {
            return 0;
        }
    }
}

package au.com.suttons.notification.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtil
{
    public static String spaceConcatStrings(String... strings)
    {
        StringBuilder sb = new StringBuilder();

        for (String str : strings) {
            if (str != null) {
                sb.append(str).append(" ");
            }
        }

        return sb.toString();
    }

    public static String trim(String value) {
        if (value != null) {
            return value.trim();
        }
        return null;
    }

    /**
     * Splits a string, from the end, stopping if a given number of chars have been reached
     *
     * @param str - the string to split
     * @param maxChars - the maximum number of characters to split from the end of the string
     *
     * @return A string, from the end of the source string, containing up the the given number of chars
     */
    public static String splitEnd(String str, int maxChars)
    {
        int endIndex = str.length();
        int startIndex = endIndex > maxChars ? str.length() - maxChars : 0;

        return str.substring(startIndex, endIndex);
    }

    public static String maxLength(String str, int maxLen)
    {
        if (str.length() <= maxLen) {
            return str;
        }
        else {
            return str.substring(0, maxLen - 1);
        }
    }

    public static List<String> trimList(final List<String> originalList)
    {
        List<String> trimmedList = new ArrayList<String>();

        for (String value : originalList) {
            trimmedList.add(StringUtil.trim(value));
        }

        return trimmedList;
    }

    public static List<String> trimList(final String[] originalList)
    {
        List<String> trimmedList = new ArrayList<String>();

        for (String value : originalList) {
            trimmedList.add(StringUtil.trim(value));
        }

        return trimmedList;
    }
    
    public static String removeLeadingZeros(String string) {
        return string.replaceFirst("^0+(?!$)", "");
    }
}

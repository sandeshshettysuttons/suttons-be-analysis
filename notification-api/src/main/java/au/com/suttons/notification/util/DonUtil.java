package au.com.suttons.notification.util;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 * Provides methods related to DON!
 */
public class DonUtil
{
    private static final String KEY_EXPRESSION = "[a-zA-Z0-9]+";
    private static final String VALUE_EXPRESSION = "[a-zA-Z0-9\\*\\+,%_\\-!@#\\$\\^=<>\\.\\?';:\\|~`&\\{\\}\\[\\]/ ]*";
//    private static final String CHUNK_EXPRESSION = "(" + KEY_EXPRESSION + ")\\((" + VALUE_EXPRESSION + ")\\)";
    private static final String CHUNK_EXPRESSION = "([a-zA-Z0-9]+)\\((.*?)\\)(?=\\+|$)";
    private static final String FULL_EXPRESSION = "^" + KEY_EXPRESSION + "\\(" + VALUE_EXPRESSION + "\\)(\\+" + KEY_EXPRESSION + "\\(" + VALUE_EXPRESSION + "\\))*$";

    private static final Pattern CHUNK_PATTERN = Pattern.compile(CHUNK_EXPRESSION);

    /**
     * Parses 'don' string value into a map of <key, value>
     *
     * e.g. customer(title,firstName)+vehicle(make,model)+...
     *
     * @return A {@link java.util.Map} of <key, value> based on the 'don' string
     * */
    public static Map<String, String> parse(String don)
    {
        Map<String, String> result = new HashMap<String, String>();

        if (don != null) {
            Matcher matcher = CHUNK_PATTERN.matcher(don);

            while(matcher.find()) {
                // Only process this chunk if there are two groups (i.e. the don is formed correctly)
                if (matcher.groupCount() == 2) {
                    String key = matcher.group(1);
                    String value = matcher.group(2);

                    if (result.containsKey(key)) {
                        value = result.get(key) + "," + value;
                    }

                    result.put(key, value);
                }
            }
        }

        return result;
    }

    public static boolean isValid(String don)
    {
        boolean result = false;

        if (don != null) {
            result = don.matches(FULL_EXPRESSION);
        }

        if(!result) {
            Logger.getLogger(DonUtil.class).error("don validation failed: " + don);
        }
        return result;
    }
}

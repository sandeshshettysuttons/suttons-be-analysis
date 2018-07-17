package au.com.suttons.notification.util;

import java.lang.reflect.Field;
import java.util.List;

public class ListUtil
{
    public static String getGenericTypeOfList(Field listField)
    {
        if (listField.getType().equals(List.class)) {
            String[] listItemTypeTokens = listField.getGenericType().toString().replaceAll("[><]","").split("\\.");
            return listItemTypeTokens[listItemTypeTokens.length-1];
        }

        return null;
    }

    public static String getGenericTypeOfList(java.lang.Class<?> fieldType, java.lang.reflect.Type fieldGenericType)
    {
        if (fieldType.equals(List.class)) {
            String[] listItemTypeTokens = fieldGenericType.toString().replaceAll("[><]","").split("\\.");
            return listItemTypeTokens[listItemTypeTokens.length-1];
        }

        return null;
    }
}

package au.com.suttons.notification.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ObjectUtil
{
    public static <T> T getFirstObjectOfType(Object[] objs, Class<T> type)
    {
        if (objs != null && objs.length > 0) {
            for (Object obj : objs) {
                if (type.isInstance(obj)) {
                    return (T) obj;
                }
            }
        }

        return null;
    }

    public static boolean isObjectEmpty(Object value) {
        if (value == null) {
            return true;
        } else if (value instanceof String) {
            if (null == value || "".equals(((String) value).trim())) {
                return true;
            }
        } else if (value instanceof Collection) {
            Collection collection = (Collection) value;
            if (null == value || collection.size() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Compares o1 to o1, returning true if they are the same
     *
     * @param o1 Object
     * @param o2 Object
     * @return true if o1 is comparatively the same as o2
     */
    public static boolean compare(Object o1, Object o2)
    {
        if (ObjectUtil.isObjectEmpty(o1)) {
            return ObjectUtil.isObjectEmpty(o2);
        }
        else {
            if (o1 instanceof BigDecimal && o2 instanceof BigDecimal) {
                return ((BigDecimal) o1).compareTo(((BigDecimal) o2)) == 0;
            }
            else {
                return o1.equals(o2);
            }
        }
    }

    public static Field[] getAllFields(Class clazz){
        List<Field> fields = new ArrayList<Field>();
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        if (clazz.getSuperclass() != null) {
            fields.addAll(Arrays.asList(getAllFields(clazz.getSuperclass())));
        }
        return fields.toArray(new Field[] {});
    }

    public static Field[] getAllFieldsWithAnnotation(Class clazz, Class annotation) {
        List<Field> fields = new ArrayList<Field>();

        for(Field field : clazz.getDeclaredFields()){
            if(field.getAnnotation(annotation) != null){
                fields.add(field);
            }
        }

//        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        if (clazz.getSuperclass() != null) {
            fields.addAll(Arrays.asList(getAllFieldsWithAnnotation(clazz.getSuperclass(), annotation)));
        }
        return fields.toArray(new Field[] {});
    }
}

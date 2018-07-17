package au.com.suttons.notification.util;

import java.util.Arrays;
import java.util.List;

import au.com.suttons.notification.mapper.ResourceAlias;
import au.com.suttons.notification.resource.bean.ResponseCollectionBean;
import au.com.suttons.notification.resource.bean.ResponseInstanceBean;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonFactory
{
    //public static Gson build(final List<String> includedFields, final List<String> excludedFields)
    public static Gson build(final List<String> includedFields, final List<String> excludedFields, final Class rootResourceClass)
    {
        GsonBuilder b = new GsonBuilder();

        b.addSerializationExclusionStrategy(new ExclusionStrategy()
        {
            @Override
            public boolean shouldSkipField(FieldAttributes field)
            {
                boolean shouldInclude = false;
                String fieldName = field.getName();
                String className = field.getDeclaringClass().getSimpleName();
                String declaringClassFieldName;

                declaringClassFieldName = className + "." + fieldName;

                /* First, check to see if the field should be explicitly excluded (this takes preference over inclusion) */
                boolean shouldExclude = excludedFields.contains("*." + fieldName)
                                            || excludedFields.contains(className + ".*")
                                            || excludedFields.contains(declaringClassFieldName);
                if (shouldExclude) {
                    return true;
                }

                /* return true (skip) if declaringClassFieldName not found in includedFields list */
                boolean shouldIncludeAsField = includedFields.contains("*." + fieldName)
                                            || includedFields.contains(className + ".*")
                                            || includedFields.contains(declaringClassFieldName);

                if (field.getDeclaredClass() != null)
                {
                    /***************************************************************/
                    /* check if field is of a known related resource type */
                    boolean fieldTooDeep = this.isFieldDeeperThenSecondLevel(field);
                    //should skip field
                    if (fieldTooDeep)
                    {
                      return true;
                    }

                    shouldInclude = shouldIncludeAsField;

                    if (field.getDeclaredClass().equals(List.class))
                    {
                        // this should take care of "items" list on ResponseCollectionBean
                        if (field.getDeclaringClass().equals(ResponseCollectionBean.class))
                        {
                            shouldInclude = true;
                        }
                        else if (field.getDeclaringClass().equals(ResponseInstanceBean.class))
                        {
                            shouldInclude = true;
                        }
                    }
                }

                return !shouldInclude;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz)
            {
                boolean shouldInclude = false;
                return shouldInclude;
            }

            private boolean isFieldDeeperThenSecondLevel(FieldAttributes field)
            {
                boolean result = true;

                List<Class> allowedRootClasses = Arrays.asList(rootResourceClass, ResponseCollectionBean.class);

                /* check if field is of a known related resource type */
                String fieldDeclaredClass = null;
                if (field.getDeclaredClass().equals(List.class))
                {
                    fieldDeclaredClass = ListUtil.getGenericTypeOfList(field.getDeclaredClass(), field.getDeclaredType());
                }
                else
                {
                    ResourceAlias alias = ResourceAlias.fromClass(field.getDeclaredClass());
                    if (alias != null)
                    {
                        fieldDeclaredClass = alias.name();
                    }
                }

                if (allowedRootClasses.contains(field.getDeclaringClass())
                        || (!allowedRootClasses.contains(field.getDeclaringClass()) && fieldDeclaredClass == null))
                {
                    result = false;
                }

                return result;
            }
        });

        /* do not omit fields with NULL value */
        b.serializeNulls();

        //b.registerTypeAdapter(Date.class, new GsonDateSerialiser(DateUtil.dateFormatter));
        //b.registerTypeAdapter(Time.class, new GsonJodaSerialiser(DateUtil.fullTimeFormatter));
        //b.registerTypeAdapter(LocalDateTime.class, new GsonJodaSerialiser(DateUtil.dateTimeFormatter));
        //b.registerTypeAdapter(DateTime.class, new GsonJodaDateTimeSerialiser(DateUtil.dateTimeZoneFormatter));

        return b.create();
    }
}

package au.com.suttons.notification.util;

import au.com.suttons.notification.data.entity.BaseEntity;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

public class JsonUtil
{
    /**
     * Deserialises a JSON formatted String into a {@link Map}.
     *
     * @param json - A JSON formatted String
     * @return - A {@link Map} representation of the JSON String {@code json}, or {@code null} if {@code json} is {@code null}
     *           or empty.
     */
    public static Map<String, Object> toMap(String json)
    {
        Map<String, Object> map = new HashMap<String, Object>();

        if (json != null && !json.trim().equals("")) {
            map = new Gson().fromJson(json, Map.class);
        }

        return map;
    }

    public static Map<String, Object> toMap(BaseEntity entity)
    {
        return toMap(getEntityImage(entity));
    }

    public static String getEntityImage(BaseEntity entity) {
        
        if(entity == null) {
            return null;
        }

        try {
            ObjectWriter writer = new ObjectMapper().writer();  
//            FilterProvider filters = new SimpleFilterProvider()  
//                    .addFilter("exclude nested properties",
//                            SimpleBeanPropertyFilter.serializeAllExcept(new String[]{"tote","part","receiptJob","franchise"}));  
//            ObjectWriter writer = mapper.writer(filters);  
//            writer.withFilters(filters);
            return new String(writer.writeValueAsBytes(entity));
        } catch (IOException ex) {
            Logger.getLogger(JsonUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}

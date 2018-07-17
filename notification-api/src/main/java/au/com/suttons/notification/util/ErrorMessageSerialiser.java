package au.com.suttons.notification.util;

import au.com.suttons.notification.model.ErrorCode;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

public class ErrorMessageSerialiser extends JsonSerializer<ErrorCode>
{
    @Override
    public void serialize(ErrorCode value, JsonGenerator generator, SerializerProvider provider) throws IOException
    {
        generator.writeStartObject();
        generator.writeFieldName("code");
        generator.writeString(value.name());
        generator.writeFieldName("friendlyMessage");
        generator.writeString(value.getFriendlyMsg());
        generator.writeFieldName("developerMessage");
        generator.writeString(value.getDeveloperMsg());
        generator.writeEndObject();
    }
}
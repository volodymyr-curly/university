package ua.foxminded.university.util.converters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class CustomDeserializer extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonParser jsonParser,
                              DeserializationContext deserializationContext)
        throws IOException {

        String text = jsonParser.getText();
        if (text.equals("0") || text.isEmpty()) {
            return null;
        } else {
            return Integer.valueOf(text);
        }
    }
}

package ru.javawebinar.topjava.web.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ru.javawebinar.topjava.to.MealTo;

import java.io.IOException;
import java.time.LocalDateTime;

public class MealToJsonDeserializer extends JsonDeserializer<MealTo> {
    @Override
    public MealTo deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        final Integer id = node.get("id").asInt();
        final LocalDateTime dateTime = LocalDateTime.parse(node.get("dateTime").asText());
        final String description = node.get("description").asText();
        final int calories = node.get("calories").asInt();
        final boolean excess = node.get("excess").asBoolean();

        return new MealTo(id, dateTime, description, calories, excess);
    }
}

package com.hangman.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class ListDeserializer extends JsonDeserializer<String> {

	@Override
	public String deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		if (jsonParser.currentToken() == JsonToken.START_ARRAY) {
	           List<String> list = new ArrayList<>();
	           jsonParser.nextToken();

	           while (jsonParser.hasCurrentToken() && jsonParser.currentToken() != JsonToken.END_ARRAY) {
	              list.add(jsonParser.getValueAsString());
	              jsonParser.nextToken();
	           }
	          return String.join("#", list);
	       }
		return null;
	}

}

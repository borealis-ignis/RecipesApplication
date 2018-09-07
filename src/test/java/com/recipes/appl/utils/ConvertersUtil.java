package com.recipes.appl.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertersUtil {
	
	public static String json(final Object object) throws JsonProcessingException {
		final ObjectMapper mapperObj = new ObjectMapper();
		return mapperObj.writeValueAsString(object);
	}
	
}

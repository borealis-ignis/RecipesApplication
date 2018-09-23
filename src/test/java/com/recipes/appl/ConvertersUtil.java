package com.recipes.appl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Kastalski Sergey
 */
public abstract class ConvertersUtil {
	
	public static String json(final Object object) throws JsonProcessingException {
		final ObjectMapper mapperObj = new ObjectMapper();
		return mapperObj.writeValueAsString(object);
	}
	
}

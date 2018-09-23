package com.recipes.appl.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.StringUtils;

/**
 * @author Kastalski Sergey
 */
public abstract class StringUtil {
	
	public static List<Long> toList(final String idsString, final String delimiter) {
		final List<Long> list = new ArrayList<>();
		if (!StringUtils.isEmpty(idsString)) {
			Arrays.stream(idsString.split(delimiter)).forEach(id -> list.add(Long.valueOf(id)));
		}
		return list;
	}
	
}

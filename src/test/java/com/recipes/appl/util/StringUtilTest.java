package com.recipes.appl.util;

import org.assertj.core.util.Arrays;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author Kastalski Sergey
 */
public class StringUtilTest {
	
	@Test
	public void toList() {
		Assert.assertNotNull(StringUtil.toList("", ";"));
		Assert.assertNotNull(StringUtil.toList(null, ";"));
		Assert.assertTrue(StringUtil.toList("", ";").isEmpty());
		Assert.assertTrue(StringUtil.toList(null, ";").isEmpty());
		Assert.assertTrue(StringUtil.toList(null, null).isEmpty());
		Assert.assertTrue(StringUtil.toList(";", ";").isEmpty());
		Assert.assertTrue(StringUtil.toList("1;2;3;4;", ";").size() == 4);
		Assert.assertTrue(StringUtil.toList("1;2;3;4", ";").size() == 4);
		Assert.assertTrue(StringUtil.toList("1;2;3;4;", ";").containsAll(Arrays.asList(new Long[] { 1L, 2L, 3L, 4L })));
	}
	
}

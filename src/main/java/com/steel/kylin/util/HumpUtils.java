package com.steel.kylin.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

/**
 * @author steel
 * datetime 2020/1/14 11:39
 */
public class HumpUtils {
	/**
	 * 字符串转驼峰
	 *
	 * @param source no desc
	 * @return java.lang.String
	 * @author steel
	 * datetime 2020/1/14 11:39
	 */
	public static String hump(String source) {
		return WordUtils.uncapitalize(StringUtils.remove(WordUtils.capitalizeFully(source, '_'), "_"));
	}
}

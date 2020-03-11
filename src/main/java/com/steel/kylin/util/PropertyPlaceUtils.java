package com.steel.kylin.util;

import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Properties;

/**
 * @author steel
 * datetime 2020/1/9 15:14
 */
public class PropertyPlaceUtils {

	/** Prefix for system property placeholders: "${". */
	public static final String PLACEHOLDER_PREFIX = "${";

	/** Suffix for system property placeholders: "}". */
	public static final String PLACEHOLDER_SUFFIX = "}";

	/** Value separator for system property placeholders: ":". */
	public static final String VALUE_SEPARATOR = ":";


	private static final PropertyPlaceholderHelper strictHelper =
		new PropertyPlaceholderHelper(PLACEHOLDER_PREFIX, PLACEHOLDER_SUFFIX, VALUE_SEPARATOR, false);

	private static final PropertyPlaceholderHelper nonStrictHelper =
		new PropertyPlaceholderHelper(PLACEHOLDER_PREFIX, PLACEHOLDER_SUFFIX, VALUE_SEPARATOR, true);

	public static String resolvePlaceholders(String text, Properties properties) {
		return resolvePlaceholders(text, properties, false);
	}

	public static String resolvePlaceholders(String text, Properties properties, boolean ignoreUnresolvablePlaceholders) {
		PropertyPlaceholderHelper helper = (ignoreUnresolvablePlaceholders ? nonStrictHelper : strictHelper);
		return helper.replacePlaceholders(text, properties);
	}
}

package com.steel.kylin.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author steel
 * datetime 2020/1/9 11:01
 */
public class MapToObjectUtils {

	public static <T> T mapToObject(Map<String, Object> map, Class<T> entity) {
		T result;
		try {
			result = entity.newInstance();
			for(Field field : entity.getDeclaredFields()) {
				if (map.containsKey(field.getName())) {
					boolean flag = field.isAccessible();
					field.setAccessible(true);
					Object object = map.get(field.getName());
					if (object!= null && field.getType().isAssignableFrom(object.getClass())) {
						field.set(result, object);
					}
					field.setAccessible(flag);
				}
			}
			return result;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static Map<String, Object> entityToMap(Object object) {
		Map<String, Object> resultMap = new HashMap<>();
		if (object == null) {
			return resultMap;
		}
		for (Field field : object.getClass().getDeclaredFields()){
			try {
				boolean flag = field.isAccessible();
				field.setAccessible(true);
				Object o = field.get(object);
				resultMap.put(field.getName(), o);
				field.setAccessible(flag);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return resultMap;
	}
}

package com.steel.kylin.convert;

import com.steel.kylin.dto.KylinResponseDTO;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author steel
 * datetime 2020/1/8 10:14
 */
public interface KylinResponseConvert {
	Object convert(Method method, KylinResponseDTO kylinResponseDTO) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;
}

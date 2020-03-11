package com.steel.kylin.convert;

import com.alibaba.fastjson.JSON;
import com.steel.kylin.dto.KylinColumnMetaDTO;
import com.steel.kylin.dto.KylinResponseDTO;
import com.steel.kylin.util.HumpUtils;
import com.steel.kylin.util.MapToObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author steel
 * datetime 2020/1/8 10:15
 */
public class KylinResponseReflactConvert implements KylinResponseConvert {
	private static final Logger LOGGER = LoggerFactory.getLogger(KylinResponseReflactConvert.class);

	@Override
	public Object convert(Method method, KylinResponseDTO kylinResponseDTO) {
		Type returnType = method.getGenericReturnType();
		Class<?> returnClazz = method.getReturnType();
		boolean manyResultClazz = Collection.class.isAssignableFrom(returnClazz);
		this.checkManyResult(manyResultClazz, kylinResponseDTO);

		Object result;
		List<KylinColumnMetaDTO> kylinColumnMetaDTOS = kylinResponseDTO.getColumnMetas();
		List<List<String>> resultLists = kylinResponseDTO.getResults();
		List<Map<String, Object>> list = this.convertToMapList(kylinColumnMetaDTOS, resultLists);
		if (manyResultClazz) {
			if (CollectionUtils.isEmpty(list)) {
				return new ArrayList<>();
			}
			if (returnType instanceof ParameterizedType) {
				List<Object> resultList = new LinkedList<>();
				try {
					list.forEach(stringObjectMap -> resultList.add(MapToObjectUtils.mapToObject(stringObjectMap,
						(Class<?>) ((ParameterizedType) returnType).getActualTypeArguments()[0])));
					result = resultList;
				} catch (Throwable throwable) {
					LOGGER.warn("convert by actualType class fail by {}, turn to use fastJson convert.", throwable.getMessage());
					result = JSON.parseObject(JSON.toJSONString(list), returnType);
				}
			} else {
				result = JSON.parseObject(JSON.toJSONString(list), returnType);
			}
		} else {
			if (CollectionUtils.isEmpty(list)) {
				return null;
			}
			result = MapToObjectUtils.mapToObject(list.get(0), returnClazz);
		}
		LOGGER.debug("after convert result to java object -> {}", JSON.toJSONString(result));
		return result;
	}

	private void checkManyResult(boolean manyResultClazz, KylinResponseDTO kylinResponseDTO) {
		if (!manyResultClazz && kylinResponseDTO.getManyResult()) {
			throw new IllegalArgumentException("expect one ,but return more.");
		}
	}

	private List<Map<String, Object>> convertToMapList(List<KylinColumnMetaDTO> kylinColumnMetaDTOS,
													   List<List<String>> resultLists) {
		int kylinColumnMetaSize = kylinColumnMetaDTOS.size();
		List<Map<String, Object>> list = new LinkedList<>();
		resultLists.forEach(lists -> {
			int listSize = lists.size();
			if (listSize != kylinColumnMetaSize) {
				throw new IllegalArgumentException(String.format("result's list size %s is not match column meta %s.",
					listSize, kylinColumnMetaSize));
			}
			Map<String, Object> map = new HashMap<>();
			for (int i = 0; i < listSize; i++) {
				KylinColumnMetaDTO kylinColumnMetaDTO = kylinColumnMetaDTOS.get(i);
				String strValue = lists.get(i);
				map.put(HumpUtils.hump(kylinColumnMetaDTO.getLabel()),
					this.convertStrValue(strValue, kylinColumnMetaDTO.getColumnTypeName()));
			}
			list.add(map);
		});
		LOGGER.debug("after convert kylin result to list<map> -> {}", JSON.toJSONString(list));
		return list;
	}

	private Object convertStrValue(String strValue, String valueType) {
		if ("BIGINT".equals(valueType)) {
			return Long.valueOf(strValue);
		}
		return strValue;
	}


}

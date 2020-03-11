package com.steel.kylin.convert;

import com.alibaba.fastjson.JSON;
import com.steel.kylin.KylinRequest;
import com.steel.kylin.annotion.KylinMethod;
import com.steel.kylin.dto.KylinPageRequestDTO;
import com.steel.kylin.dto.KylinRequestDTO;
import com.steel.kylin.util.MapToObjectUtils;
import com.steel.kylin.util.PropertyPlaceUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Properties;

/**
 * @author steel
 * datetime 2020/1/9 16:08
 */
public class KylinRequestDefaultConvert implements KylinRequestConvert {
	private static final Logger LOGGER = LoggerFactory.getLogger(KylinRequestDefaultConvert.class);

	@Override
	public KylinRequestDTO<Object> convert(KylinMethod kylinMethod, Object[] args) {
		Assert.isTrue(args.length == 1, "args length is not 1");
		return this.handleKylinRequestDTO(kylinMethod.sql(), args[0]);
	}

	@Override
	public KylinRequestDTO<Object> convertTotal(KylinMethod kylinMethod, Object[] args) {
		return this.handleKylinTotalRequestDTO(kylinMethod.totalSql(), args[0]);
	}

	private boolean argsCheck(Object parmObject) {
		return parmObject instanceof KylinRequestDTO;
	}

	private boolean argsTotalCheck(Object parmObject) {
		return parmObject instanceof KylinPageRequestDTO;
	}

	@SuppressWarnings("unchecked")
	private KylinRequestDTO<Object> handleKylinRequestDTO(String sql, Object parmObject) {
		Assert.isTrue(argsCheck(parmObject), "method args must instance of KylinRequestDTO.");
		KylinRequestDTO<Object> kylinRequestDTO = (KylinRequestDTO<Object>) parmObject;
		kylinRequestDTO.setSql(StringUtils.isEmpty(sql) ? kylinRequestDTO.getSql() : sql);
		Assert.isTrue(StringUtils.isNotBlank(kylinRequestDTO.getSql()), "the request sql can't be blank.");
		return this.handleKylinRequestDTO(kylinRequestDTO);
	}

	@SuppressWarnings("unchecked")
	private KylinRequestDTO<Object> handleKylinTotalRequestDTO(String sql, Object parmObject) {
		Assert.isTrue(argsTotalCheck(parmObject), "method args must instance of KylinPageRequestDTO.");
		KylinPageRequestDTO<Object> kylinPageRequestDTO = (KylinPageRequestDTO<Object>) parmObject;
		kylinPageRequestDTO.setSql(StringUtils.isEmpty(sql) ? kylinPageRequestDTO.getTotalSql() : sql);
		Assert.isTrue(StringUtils.isNotBlank(kylinPageRequestDTO.getSql()), "the request total sql can't be blank.");
		return this.handleKylinRequestDTO(kylinPageRequestDTO);
	}

	private KylinRequestDTO<Object> handleKylinRequestDTO(KylinRequestDTO<Object> kylinRequestDTO) {
		Assert.isTrue(StringUtils.isNotBlank(kylinRequestDTO.getSql()), "the request sql can't be blank.");
		Properties properties = this.getProperties(MapToObjectUtils.entityToMap(kylinRequestDTO.getParm()));
		if (kylinRequestDTO instanceof KylinPageRequestDTO) {
			KylinPageRequestDTO<Object> kylinPageRequestDTO = (KylinPageRequestDTO<Object>) kylinRequestDTO;
			KylinRequest kylinRequest = new KylinRequest(kylinPageRequestDTO.getPageNum(), kylinPageRequestDTO.getPageSize());
			properties.put("offset", kylinRequest.getOffset());
			properties.put("limit", kylinRequest.getLimit());
		}
		LOGGER.debug("before resolve sql {}.", kylinRequestDTO.getSql());
		LOGGER.debug("before resolve parm {}", JSON.toJSONString(properties));
		kylinRequestDTO.setSql(PropertyPlaceUtils.resolvePlaceholders(kylinRequestDTO.getSql(), properties, true));
		LOGGER.debug("after resolve sql {}.", kylinRequestDTO.getSql());
		return kylinRequestDTO;
	}

	private Properties getProperties(Map<String, Object> resultMap) {
		Properties properties = new Properties();
		resultMap.forEach((key,value) -> {
			if (value == null) {
				return;
			}
			String result = String.valueOf(value);
			if (StringUtils.isEmpty(result)) {
				return;
			}
			properties.put(key, result);
		});
		return properties;
	}
}

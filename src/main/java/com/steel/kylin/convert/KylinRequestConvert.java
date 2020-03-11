package com.steel.kylin.convert;


import com.steel.kylin.annotion.KylinMethod;
import com.steel.kylin.dto.KylinRequestDTO;

/**
 * @author steel
 * datetime 2020/1/9 16:07
 */
public interface KylinRequestConvert {
	/**
	 * 入参转换
	 *
	 * @param kylinMethod no desc
	 * @param args no desc
	 * @return com.unisound.aios.pandora.kylin.dto.KylinRequestDTO<java.lang.Object>
	 * @author steel
	 * datetime 2020/1/9 16:08
	 */
	KylinRequestDTO<Object> convert(KylinMethod kylinMethod, Object[] args);
	/**
	 * 入参转换
	 *
	 * @param kylinMethod no desc
	 * @param args no desc
	 * @return com.unisound.aios.pandora.kylin.dto.KylinRequestDTO<java.lang.Object>
	 * @author steel
	 * datetime 2020/1/13 11:54
	 */
	KylinRequestDTO<Object> convertTotal(KylinMethod kylinMethod, Object[] args);
}

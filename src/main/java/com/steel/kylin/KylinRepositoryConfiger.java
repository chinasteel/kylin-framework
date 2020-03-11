package com.steel.kylin;

import com.steel.kylin.convert.KylinRequestConvert;
import com.steel.kylin.convert.KylinRequestDefaultConvert;
import com.steel.kylin.convert.KylinResponseConvert;
import com.steel.kylin.convert.KylinResponseReflactConvert;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author steel
 * datetime 2020/1/8 12:52
 */
@Configuration
public class KylinRepositoryConfiger {

	@Bean
	@ConditionalOnMissingBean
	KylinResponseConvert kylinResponseConvert() {
		return new KylinResponseReflactConvert();
	}

	@Bean
	@ConditionalOnMissingBean
	KylinRequestConvert kylinRequestConvert() {
		return new KylinRequestDefaultConvert();
	}

}

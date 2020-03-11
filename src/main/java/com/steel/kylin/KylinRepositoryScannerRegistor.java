package com.steel.kylin;

import com.steel.kylin.annotion.KylinRepositoryScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author steel
 * datetime 2020/1/6 16:50
 */
public class KylinRepositoryScannerRegistor implements ImportBeanDefinitionRegistrar, EnvironmentAware {
	private static final Logger LOGGER = LoggerFactory.getLogger(KylinRepositoryScannerRegistor.class);

    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        LOGGER.info("#######start register kylin bean#######");
        AnnotationAttributes mapperScanAttrs = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(KylinRepositoryScan.class.getName()));
        if (mapperScanAttrs != null) {
            this.registerBeanDefinitions(mapperScanAttrs, registry);
        }
        LOGGER.info("#######end register kylin bean#######");
    }

    private void registerBeanDefinitions(AnnotationAttributes annoAttrs, BeanDefinitionRegistry registry) {

        ClassPathKylinRepositoryScanner scanner = new ClassPathKylinRepositoryScanner(registry);

        Class<? extends Annotation> annotationClass = annoAttrs.getClass("annotationClass");
        if (!Annotation.class.equals(annotationClass)) {
            scanner.setAnnotationClass(annotationClass);
        }

        Class<?> markerInterface = annoAttrs.getClass("markerInterface");
        if (!Class.class.equals(markerInterface)) {
            scanner.setMarkerInterface(markerInterface);
        }

        Class<? extends BeanNameGenerator> generatorClass = annoAttrs.getClass("nameGenerator");
        if (!BeanNameGenerator.class.equals(generatorClass)) {
            scanner.setBeanNameGenerator(BeanUtils.instantiateClass(generatorClass));
        }

        Class<? extends KylinRepositoryFactoryBean<?>> mapperFactoryBeanClass = annoAttrs.getClass("factoryBean");
        if (!KylinRepositoryFactoryBean.class.equals(mapperFactoryBeanClass)) {
            scanner.setKylinRepositoryFactoryBean(BeanUtils.instantiateClass(mapperFactoryBeanClass));
        }

        scanner.setRestUrl(this.getReplaceStr(annoAttrs.getString("restUrl")));
		scanner.setUserName(this.getReplaceStr(annoAttrs.getString("userName")));
		scanner.setPassword(this.getReplaceStr(annoAttrs.getString("password")));
        scanner.setRestTemplateName(annoAttrs.getString("restTemplateName"));
		scanner.setKylinRequestConvertName(annoAttrs.getString("kylinRequestConvertName"));
		scanner.setKylinResponseConvertName(annoAttrs.getString("kylinResponseConvertName"));

        List<String> basePackages = new ArrayList<>();
        basePackages.addAll(
                Arrays.stream(annoAttrs.getStringArray("value"))
                        .filter(StringUtils::hasText)
                        .collect(Collectors.toList()));

        basePackages.addAll(
                Arrays.stream(annoAttrs.getStringArray("basePackages"))
                        .filter(StringUtils::hasText)
                        .collect(Collectors.toList()));

        basePackages.addAll(
                Arrays.stream(annoAttrs.getClassArray("basePackageClasses"))
                        .map(ClassUtils::getPackageName)
                        .collect(Collectors.toList()));

        scanner.registerFilters();
        scanner.doScan(StringUtils.toStringArray(basePackages));
    }

    private String getReplaceStr(String sourceStr) {
        if (StringUtils.hasText(sourceStr)) {
            return this.environment.resolvePlaceholders(sourceStr);
        }
        return sourceStr;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}

package com.steel.kylin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

/**
 * 扫描注册KylinReponsitoryBean
 *
 * @author steel
 * datetime 2020/1/9 18:32
 */
public class ClassPathKylinRepositoryScanner extends ClassPathBeanDefinitionScanner {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClassPathKylinRepositoryScanner.class);

    private Class<? extends Annotation> annotationClass;

    private Class<?> markerInterface;

    private String restTemplateName;

    private String restUrl;

	private String userName;

	private String password;

    private String kylinRequestConvertName;

    private String kylinResponseConvertName;

    private KylinRepositoryFactoryBean<?> kylinRepositoryFactoryBean = new KylinRepositoryFactoryBean<>();

    public ClassPathKylinRepositoryScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public void setMarkerInterface(Class<?> markerInterface) {
        this.markerInterface = markerInterface;
    }

    public void setKylinRepositoryFactoryBean(KylinRepositoryFactoryBean<?> kylinRepositoryFactoryBean) {
        this.kylinRepositoryFactoryBean = kylinRepositoryFactoryBean;
    }

    public void setRestTemplateName(String restTemplateName) {
        this.restTemplateName = restTemplateName;
    }

    public void setRestUrl(String restUrl) {
        this.restUrl = restUrl;
    }

	public void setKylinRequestConvertName(String kylinRequestConvertName) {
		this.kylinRequestConvertName = kylinRequestConvertName;
	}

	public void setKylinResponseConvertName(String kylinResponseConvertName) {
		this.kylinResponseConvertName = kylinResponseConvertName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
     * Configures parent scanner to search for the right interfaces. It can search
     * for all interfaces or just for those that extends a markerInterface or/and
     * those annotated with the annotationClass
     */
    public void registerFilters() {
        boolean acceptAllInterfaces = true;

        // if specified, use the given annotation and / or marker interface
        if (this.annotationClass != null) {
            super.addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
            acceptAllInterfaces = false;
        }

        // override AssignableTypeFilter to ignore matches on the actual marker interface
        if (this.markerInterface != null) {
			super.addIncludeFilter(new AssignableTypeFilter(this.markerInterface) {
                @Override
                protected boolean matchClassName(String className) {
                    return false;
                }
            });
            acceptAllInterfaces = false;
        }

        if (acceptAllInterfaces) {
            // default include filter that accepts all classes
			super.addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
        }

        // exclude package-info.java
		super.addExcludeFilter((metadataReader, metadataReaderFactory) -> {
            String className = metadataReader.getClassMetadata().getClassName();
            return className.endsWith("package-info");
        });
    }

    /**
     * Calls the parent search that will search and register all the candidates.
     * Then the registered objects are post processed to set them as
     * MapperFactoryBeans
     */
    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty()) {
            LOGGER.warn("No KylinRepository interface was found in '" + Arrays.toString(basePackages) + "' package. " +
                    "Please check your configuration.");
        } else {
            this.processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            String beanClassName = definition.getBeanClassName();
            LOGGER.info("Creating KylinRepositoryFactoryBean with name {} for {}", holder.getBeanName(), beanClassName);

            Assert.notNull(beanClassName, "beanClassName is null");
            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
            definition.setBeanClass(this.kylinRepositoryFactoryBean.getClass());

            if (StringUtils.hasText(restTemplateName)) {
                definition.getPropertyValues().add("restTemplate", new RuntimeBeanReference(restTemplateName));
            }

            if (StringUtils.hasText(restUrl)) {
                definition.getPropertyValues().add("restUrl", this.restUrl);
            }

			if (StringUtils.hasText(this.userName)) {
				definition.getPropertyValues().add("userName", this.userName);
			}

			if (StringUtils.hasText(this.password)) {
				definition.getPropertyValues().add("password", this.password);
			}

			if (StringUtils.hasText(kylinRequestConvertName)) {
				definition.getPropertyValues().add("kylinRequestConvert",
					new RuntimeBeanReference(this.kylinRequestConvertName));
			}

			if (StringUtils.hasText(kylinResponseConvertName)) {
				definition.getPropertyValues().add("kylinResponseConvert",
					new RuntimeBeanReference(this.kylinResponseConvertName));
			}

            LOGGER.debug("Enabling autowire by type for KylinRepositoryFactoryBean with name '" + holder.getBeanName() + "'.");
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
        if (super.checkCandidate(beanName, beanDefinition)) {
            return true;
        } else {
            LOGGER.warn("Skipping KylinRepositoryFactoryBean with name '" + beanName
                    + "' and '" + beanDefinition.getBeanClassName() + "' kylinRepositoryInterface"
                    + ". Bean already defined with the same name!");
            return false;
        }
    }

}

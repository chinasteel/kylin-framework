package com.steel.kylin.annotion;

import com.steel.kylin.KylinRepositoryFactoryBean;
import com.steel.kylin.KylinRepositoryScannerRegistor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(KylinRepositoryScannerRegistor.class)
public @interface KylinRepositoryScan {
    /**
     * Alias for the {@link #basePackages()} attribute. Allows for more concise
     * annotation declarations e.g.:
     * {@code @MapperScan("org.my.pkg")} instead of {@code @MapperScan(basePackages = "org.my.pkg"})}.
     *
     * @return base package names
     */
    String[] value() default {};

    /**
     * Base packages to scan for MyBatis interfaces. Note that only interfaces
     * with at least one method will be registered; concrete classes will be
     * ignored.
     *
     * @return base package names for scanning mapper interface
     */
    String[] basePackages() default {};

    /**
     * Type-safe alternative to {@link #basePackages()} for specifying the packages
     * to scan for annotated components. The package of each class specified will be scanned.
     * <p>Consider creating a special no-op marker class or interface in each package
     * that serves no purpose other than being referenced by this attribute.
     *
     * @return classes that indicate base package for scanning mapper interface
     */
    Class<?>[] basePackageClasses() default {};

    /**
     * The {@link BeanNameGenerator} class to be used for naming detected components
     * within the Spring container.
     *
     * @return the class of {@link BeanNameGenerator}
     */
    Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;

    /**
     * This property specifies the annotation that the scanner will search for.
     * <p>
     * The scanner will register all interfaces in the base package that also have
     * the specified annotation.
     * <p>
     * Note this can be combined with markerInterface.
     *
     * @return the annotation that the scanner will search for
     */
    Class<? extends Annotation> annotationClass() default Annotation.class;

    /**
     * This property specifies the parent that the scanner will search for.
     * <p>
     * The scanner will register all interfaces in the base package that also have
     * the specified interface class as a parent.
     * <p>
     * Note this can be combined with annotationClass.
     *
     * @return the parent that the scanner will search for
     */
    Class<?> markerInterface() default Class.class;

    /**
     * Specifies a custom MapperFactoryBean to return a mybatis proxy as spring bean.
     *
     * @return the class of {@code MapperFactoryBean}
     */
    Class<? extends KylinRepositoryFactoryBean> factoryBean() default KylinRepositoryFactoryBean.class;

    /**
     * rest url .
     *
     * @return java.lang.String
     * @author steel
     * datetime 2020/1/7 17:39
     */
    String restUrl();
    /**
     * 用户名
     *
     * @return java.lang.String
     * @author steel
     * datetime 2020/1/13 18:27
     */
    String userName() default "";
    /**
     * 密码
     *
     * @return java.lang.String
     * @author steel
     * datetime 2020/1/13 18:27
     */
    String password() default "";

    /**
     * restTemplate name.
     *
     * @return java.lang.String
     * @author steel
     * datetime 2020/1/7 17:58
     */
    String restTemplateName() default "restTemplate";
	/**
	 * 求情对象参数转换类
	 *
	 * @return java.lang.String
	 * @author steel
	 * datetime 2020/1/9 16:17
	 */
	String kylinRequestConvertName() default "kylinRequestConvert";
	/**
	 * 响应对象转换类
	 *
	 * @return java.lang.String
	 * @author steel
	 * datetime 2020/1/8 10:27
	 */
	String kylinResponseConvertName() default "kylinResponseConvert";
}

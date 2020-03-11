package com.steel.kylin.annotion;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KylinMethod {
	/**
	 * 是否统计总页数
	 *
	 * @return boolean
	 * @author steel
	 * datetime 2020/1/13 16:58
	 */
	boolean total() default true;
    /**
     * 业务SQL
     *
     * @return java.lang.String
     * @author steel
     * datetime 2020/1/13 18:02
     */
    String sql() default "";
	/**
	 * 分页总记录数sql
	 *
	 * @return java.lang.String
	 * @author steel
	 * datetime 2020/1/13 11:40
	 */
	String totalSql() default "";
}

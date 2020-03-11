package com.steel.kylin.dto;

import java.io.Serializable;

/**
 * @author steel
 * datetime 2020/1/9 14:48
 */
public class KylinRequestDTO<PARM> implements Serializable {

	private static final long serialVersionUID = -8887399484758887789L;
	/**
	 * Olap cube所属项目
	 * 在具体接口中说明，默认取值为DEFAULT
	 */
	private String project;
	/**
	 * KylinMethod中的sql优先级会大于此处
	 * 参数用${}传入
	 */
	private String sql;
	/**
	 * 查询sql中需要的参数
	 * 默认为false
	 */
	private boolean acceptPartial;
	/**
	 * 需要传入sql中的参数
	 */
	private PARM parm;

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public boolean isAcceptPartial() {
		return acceptPartial;
	}

	public void setAcceptPartial(boolean acceptPartial) {
		this.acceptPartial = acceptPartial;
	}

	public PARM getParm() {
		return parm;
	}

	public void setParm(PARM parm) {
		this.parm = parm;
	}
}

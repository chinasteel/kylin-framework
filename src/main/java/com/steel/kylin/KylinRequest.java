package com.steel.kylin;

/**
 * @author steel
 * datetime 2020/1/9 14:48
 */
public class KylinRequest {
	/**
     * Olap cube所属项目
	 * 在具体接口中说明，默认取值为DEFAULT
	 */
	private String project;
	/**
     * 参数用${}传入
	 */
	private String sql;
	/**
	 * 分页偏移量
	 */
	private Integer offset;
	/**
	 * 查询limit
	 * 如果在sql中设置，perpage将被忽略
	 */
	private Integer limit;
	/**
	 * 查询sql中需要的参数
	 * 默认为false
	 */
	private boolean acceptPartial;

	public KylinRequest() {
	}

	public KylinRequest(Integer pageNum, Integer pageSize) {
		this.offset = pageNum == null || pageSize == null ? null : (pageNum - 1) * pageSize;
		this.limit = pageSize;
	}

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

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public boolean isAcceptPartial() {
		return acceptPartial;
	}

	public void setAcceptPartial(boolean acceptPartial) {
		this.acceptPartial = acceptPartial;
	}
}

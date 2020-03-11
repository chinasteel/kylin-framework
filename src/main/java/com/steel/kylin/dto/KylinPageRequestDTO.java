package com.steel.kylin.dto;

/**
 * @author steel
 * datetime 2020/1/9 14:48
 */
public class KylinPageRequestDTO<PARM> extends KylinRequestDTO<PARM> {

	private static final long serialVersionUID = 9149829942081773163L;
	/**
	 * 当前页
	 */
	private Integer pageNum;
	/**
	 * 分页大小
	 */
	private Integer pageSize;
	/**
	 * 总记录数sql
	 */
	private String totalSql;
	/**
	 * 总记录数
	 */
	private Long total;

	public KylinPageRequestDTO() {
		this.pageNum = 1;
		this.pageSize = 10;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getTotalSql() {
		return totalSql;
	}

	public void setTotalSql(String totalSql) {
		this.totalSql = totalSql;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}
}

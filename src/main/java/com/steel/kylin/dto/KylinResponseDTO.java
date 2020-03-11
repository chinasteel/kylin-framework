package com.steel.kylin.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author steel
 * datetime 2020/1/7 15:23
 */
public class KylinResponseDTO implements Serializable {
    private static final long serialVersionUID = -6658501387213950730L;
    private List<KylinColumnMetaDTO> columnMetas;
    private List<List<String>> results;

    private Long totalScanCount;
    private Long affectedRowCount;
	private Boolean manyResult;
	/**
	 * 总耗时
	 */
	private Long duration;
	private boolean exception;
	private String exceptionMessag;

    public List<KylinColumnMetaDTO> getColumnMetas() {
        return columnMetas;
    }

    public void setColumnMetas(List<KylinColumnMetaDTO> columnMetas) {
        this.columnMetas = columnMetas;
    }

    public Long getTotalScanCount() {
        return totalScanCount;
    }

    public void setTotalScanCount(Long totalScanCount) {
        this.totalScanCount = totalScanCount;
    }

    public Long getAffectedRowCount() {
        return affectedRowCount;
    }

    public void setAffectedRowCount(Long affectedRowCount) {
        this.affectedRowCount = affectedRowCount;
    }

	public boolean isException() {
		return exception;
	}

	public void setException(boolean exception) {
		this.exception = exception;
	}

	public List<List<String>> getResults() {
        return results;
    }

    public void setResults(List<List<String>> results) {
        this.results = results;
    }

	public Boolean getManyResult() {
		return this.results != null && this.results.size() > 1;
	}

	public void setManyResult(Boolean manyResult) {
		this.manyResult = manyResult;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getExceptionMessag() {
		return exceptionMessag;
	}

	public void setExceptionMessag(String exceptionMessag) {
		this.exceptionMessag = exceptionMessag;
	}
}

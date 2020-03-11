package com.steel.kylin.dto;

import java.io.Serializable;

/**
 * @author steel
 * datetime 2020/1/7 15:24
 */
public class KylinColumnMetaDTO implements Serializable {
    private static final long serialVersionUID = 907362862037139973L;

    private String label;
    private String name;
    private String columnTypeName;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColumnTypeName() {
        return columnTypeName;
    }

    public void setColumnTypeName(String columnTypeName) {
        this.columnTypeName = columnTypeName;
    }
}

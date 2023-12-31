package com.marmot.db.query.tools.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum QueryOrderEnum {
    DESC("DESC"),
    ASC("ASC"),
    ;

    private String orderStr;

    public String getOrderStr() {
        return orderStr;
    }

    QueryOrderEnum(String orderStr) {
        this.orderStr = orderStr;
    }

    public static QueryOrderEnum of(String orderStr) {
        return StringUtils.isBlank(orderStr) ? DESC : Arrays.stream(QueryOrderEnum.values()).filter(str -> str.getOrderStr().equals(orderStr)).findFirst().orElse(DESC);
    }
}

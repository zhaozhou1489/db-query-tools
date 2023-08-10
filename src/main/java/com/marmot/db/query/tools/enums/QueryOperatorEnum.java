package com.marmot.db.query.tools.enums;

public enum QueryOperatorEnum {
    AND("and"),
    OR("or"),;
    private String operator;

    public String getOperator() {
        return operator;
    }

    QueryOperatorEnum(String operator) {
        this.operator = operator;
    }
}

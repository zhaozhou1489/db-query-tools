package com.marmot.db.query.tools.query;

import com.marmot.db.query.tools.enums.QueryTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author:zhaozhou
 * @Date: 2023/07/18
 * @Desc:
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RangeQuery extends AbstractFieldQuery {
    private String max;
    private String min;
    private boolean includeMax;
    private boolean includeMin;

    public RangeQuery(String field, String max, String min, boolean includeMax,boolean includeMin) {
        this.setType(QueryTypeEnum.RANGE.getName());
        this.setField(field);
        this.setMax(max);
        this.setMin(min);
        this.setIncludeMax(includeMax);
        this.setIncludeMin(includeMin);
    }
}

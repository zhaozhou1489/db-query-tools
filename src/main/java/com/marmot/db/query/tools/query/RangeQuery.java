package com.marmot.db.query.tools.query;

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
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RangeQuery extends BaseQuery{
    private String max;
    private String min;
    private Boolean includeMax;
    private Boolean includeMin;
}

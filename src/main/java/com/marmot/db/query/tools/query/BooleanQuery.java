package com.marmot.db.query.tools.query;

import cn.hutool.json.JSONUtil;
import com.marmot.db.query.tools.enums.QueryTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author:zhaozhou
 * @Date: 2023/07/18
 * @Desc:
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class BooleanQuery extends AbstractBaseQuery {
    private List<String> subQueries;
    private List<? extends AbstractBaseQuery> queries;
    private String operator;

    public BooleanQuery(List<String> queries, String operator) {
        super(QueryTypeEnum.BOOLEAN.getName());
        this.operator = operator;
        this.subQueries = queries;
    }

}

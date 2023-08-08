package com.marmot.db.query.tools.params;

import com.marmot.db.query.tools.query.BaseQuery;
import com.marmot.db.query.tools.query.Limit;
import com.marmot.db.query.tools.query.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author:zhaozhou
 * @Date: 2023/07/24
 * @Desc:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryCond {
    private List<? extends BaseQuery> queries;
    private Limit limit;
    private List<Order> orders;
}

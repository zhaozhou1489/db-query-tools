package com.marmot.db.query.tools.params;

import com.marmot.db.query.tools.query.AbstractBaseQuery;
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
public class QueryParam {
    //查询子句，如equal，like等
    private List<String> queries;

    //limit
    private Limit limit;

    //排序方式
    private List<Order> orders;
}

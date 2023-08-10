package com.marmot.db.query.tools.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:zhaozhou
 * @Date: 2023/07/18
 * @Desc: 基于数据库字段的查询类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractFieldQuery extends AbstractBaseQuery{
    private String field;
}

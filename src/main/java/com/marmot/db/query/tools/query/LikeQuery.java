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
public class LikeQuery extends BaseQuery {
    private String likeValue;

    //true：左模糊匹配
    private boolean left;

    //true:右模糊匹配
    private boolean right;
}

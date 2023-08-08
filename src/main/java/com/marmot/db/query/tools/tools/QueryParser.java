package com.marmot.db.query.tools.tools;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.marmot.db.query.tools.enums.QueryTypeEnum;
import com.marmot.db.query.tools.query.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:zhaozhou
 * @Date: 2023/07/18
 * @Desc:
 */
public class QueryParser {
    private static final Map<String,Class<? extends BaseQuery>> queryNameClassMap = new HashMap<String,Class<? extends BaseQuery>>(){{
        put(QueryTypeEnum.EQUAL.getName(), EqualQuery.class);
        put(QueryTypeEnum.LIKE.getName(), LikeQuery.class);
        put(QueryTypeEnum.RANGE.getName(), RangeQuery.class);
        put(QueryTypeEnum.SET.getName(), InQuery.class);
    }};


    public static BaseQuery parseQuery(String queryStr, Class<? extends BaseQuery> queryClass){
        if (StringUtils.isBlank(queryStr)) return null;
        return JSONUtil.toBean(queryStr, queryClass);
    }


    public static String parseQueries(List<String> queryStrs, List<BaseQuery> queries) {
        if (CollectionUtils.isEmpty(queryStrs)) return "";
        for (String q : queryStrs) {
            JSONObject jsonObj = JSONUtil.parseObj(q);
            String queryType = jsonObj.getStr("queryType");
            Class<? extends BaseQuery> queryClass = queryNameClassMap.get(queryType);
            if (queryClass == null) {
                return "queryType=" + queryType + " is invalid";
            }
            queries.add(parseQuery(q, queryClass));
        }
        return "";
    }
}

package com.marmot.db.query.tools.tools;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.marmot.db.query.tools.enums.QueryTypeEnum;
import com.marmot.db.query.tools.query.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author:zhaozhou
 * @Date: 2023/07/18
 * @Desc:
 */
@Slf4j
public class QueryParser {
    private static final Map<String,Class<? extends AbstractBaseQuery>> queryNameClassMap = new HashMap<String,Class<? extends AbstractBaseQuery>>(){{
        put(QueryTypeEnum.EQUAL.getName(), EqualQuery.class);
        put(QueryTypeEnum.LIKE.getName(), LikeQuery.class);
        put(QueryTypeEnum.RANGE.getName(), RangeQuery.class);
        put(QueryTypeEnum.IN.getName(), InQuery.class);
        put(QueryTypeEnum.NULL.getName(), NullQuery.class);
        put(QueryTypeEnum.BOOLEAN.getName(), BooleanQuery.class);
    }};


    public static AbstractBaseQuery parseQuery(String queryStr, Class<? extends AbstractBaseQuery> queryClass){
        if (StringUtils.isBlank(queryStr)) return null;
        return JSONUtil.toBean(queryStr, queryClass);
    }


    public static String parseQueries(List<String> queryStrs, List<AbstractBaseQuery> queries) {
        if (CollectionUtils.isEmpty(queryStrs)) return "";
        for (String q : queryStrs) {
            JSONObject jsonObj = JSONUtil.parseObj(q);
            String queryType = jsonObj.getStr("type");
            Class<? extends AbstractBaseQuery> queryClass = queryNameClassMap.get(queryType);
            if (queryClass == null) {
                return "type=" + queryType + " is not support";
            }
            AbstractBaseQuery query = parseQuery(q, queryClass);
            queries.add(query);
            //若是Boolean查询，则解析子查询
            if (query instanceof BooleanQuery){
                List<AbstractBaseQuery> boolSubQueries = new LinkedList<>();
                String errMsg = parseQueries(((BooleanQuery) query).getQueries(),boolSubQueries);
                if (StringUtils.isNotBlank(errMsg)){
                    log.error("parse boolean sub query error, subQuery={}, errMsg={}", ((BooleanQuery) query).getQueries(), errMsg);
                    return errMsg;
                }
                ((BooleanQuery) query).setBaseQueries(boolSubQueries);
            }
        }
        return "";
    }
}

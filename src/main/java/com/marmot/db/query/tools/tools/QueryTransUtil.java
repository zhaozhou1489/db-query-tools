package com.marmot.db.query.tools.tools;

import cn.hutool.json.JSONUtil;
import com.marmot.db.query.tools.enums.QueryOrderEnum;
import com.marmot.db.query.tools.params.QueryCond;
import com.marmot.db.query.tools.params.QueryParam;
import com.marmot.db.query.tools.query.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author:zhaozhou
 * @Date: 2023/08/01
 * @Desc: todo
 */
public class QueryTransUtil {


    private static final long LIMIT_COUNT_MAX = 10000;

    /**
     * @Desc 转换并校验查询参数
     * @Param
     * @return
     **/
    public static String transQueryParam(QueryParam queryParam, QueryCond queryCond, Set<String> validFields) {
        String errMsg = "";
        List<AbstractBaseQuery> queries = new LinkedList<>();

        queryCond.setQueries(new LinkedList<>());
        if (queryParam == null){
            return "";
        }
        //解析及校验查询参数
        errMsg = StringUtils.isNotBlank(errMsg = QueryParser.parseQueries(queryParam.getQueries(), queries)) ? errMsg : QueryValidator.validQueries(queries);
        if (StringUtils.isNotBlank(errMsg)) {
            return errMsg;
        }

        if (queryParam.getLimit() != null && queryParam.getLimit().getCount() > LIMIT_COUNT_MAX) {
            return "limit count should less than " + LIMIT_COUNT_MAX;
        }
        if (queryParam.getLimit() == null){
            queryParam.setLimit(new Limit(0L, LIMIT_COUNT_MAX));
        }

        if (CollectionUtils.isNotEmpty(queryParam.getOrders())){
            queryParam.getOrders().forEach(order -> order.setSortStr(QueryOrderEnum.of(order.getSortStr()).getOrderStr()));
        }
        queryCond.setQueries(queries);
        queryCond.setLimit(queryParam.getLimit());
        queryCond.setOrders(queryParam.getOrders());

        //校验字段是否支持
        return validFieldOfQueryCond(queryCond, validFields);
    }



    /**
     * @Desc 检测查询参数是否合法
     **/
    private static String validFieldOfQueryCond(QueryCond queryCond, Set<String> validFields){
        if (CollectionUtils.isEmpty(validFields)){
            return "";
        }
        //查询字段检查
        Set<String> queryInvalidFields = CollectionUtils.isEmpty(queryCond.getQueries()) ? new HashSet<>() :
                validQueryField(queryCond.getQueries(), validFields);
        //排序字段检查
        Set<String> orderInvalidFields = CollectionUtils.isEmpty(queryCond.getOrders()) ? new HashSet<>():
                queryCond.getOrders().stream().map(Order::getField).filter(field -> !validFields.contains(field)).collect(Collectors.toSet());

        Set<String> invalidFields = Stream.of(queryInvalidFields,orderInvalidFields).flatMap(Set::stream).collect(Collectors.toSet());
        return CollectionUtils.isEmpty(invalidFields) ? "": "fields " + JSONUtil.toJsonStr(invalidFields) + " are not support";
    }


    /**
     * @Desc 检测查询字段是否合法
     **/
    private static Set<String> validQueryField(List<? extends AbstractBaseQuery> queries, Set<String> validFields){
        Set<String> invalidFields = new HashSet<>();
        for (AbstractBaseQuery query:queries){
            if (query instanceof AbstractFieldQuery){
                if (!validFields.contains(((AbstractFieldQuery) query).getField())){
                    invalidFields.add(((AbstractFieldQuery) query).getField());
                }
            }
            if (query instanceof BooleanQuery){
                Set<String> boolInvalidFields = validQueryField(((BooleanQuery) query).getBaseQueries(), validFields);
                if (CollectionUtils.isNotEmpty(boolInvalidFields)){
                    invalidFields.addAll(boolInvalidFields);
                }
            }
        }
        return invalidFields;
    }
}

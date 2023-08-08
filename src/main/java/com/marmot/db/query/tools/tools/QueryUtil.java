package com.marmot.db.query.tools.tools;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.marmot.db.query.tools.enums.QueryOrderEnum;
import com.marmot.db.query.tools.params.QueryCond;
import com.marmot.db.query.tools.query.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Author:zhaozhou
 * @Date: 2023/07/18
 * @Desc:
 */
public class QueryUtil {

    public static AbstractWrapper transQueryCond(AbstractWrapper wrapper, QueryCond cond){
        transQueries(wrapper,cond.getQueries());
        setLimit(wrapper,cond.getLimit());
        setOrderBy(wrapper,cond.getOrders());
        return wrapper;
    }

    public static <T extends BaseQuery> AbstractWrapper transQueries(AbstractWrapper wrapper, List<T> queries){
        queries.stream().forEach(q -> {
            q.setField(StrUtil.toUnderlineCase(q.getField()));
            transQuery(wrapper, q);
        });
        return wrapper;
    }



    public static <T extends BaseQuery> AbstractWrapper transQuery(AbstractWrapper wrapper, T query){
        if (query instanceof EqualQuery){
            EqualQuery q = (EqualQuery) query;
            transEqualQuery(wrapper, q);
        }else if (query instanceof LikeQuery){
            LikeQuery q = (LikeQuery) query;
            transLikeQuery(wrapper,q);

        }else if (query instanceof RangeQuery){
            RangeQuery q = (RangeQuery) query;
            transRangeQuery(wrapper,q);

        }else if (query instanceof InQuery){
            InQuery q = (InQuery) query;
            transInQuery(wrapper,q);
        }
        return wrapper;
    }



    public static AbstractWrapper transEqualQuery(AbstractWrapper wrapper, EqualQuery query){
        wrapper.eq(query.getField(), query.getValue());
        return wrapper;
    }

    public static AbstractWrapper transLikeQuery(AbstractWrapper wrapper, LikeQuery query){
        if (query.isLeft() && query.isRight()){
            wrapper.like(query.getField(), query.getLikeValue());
        }else if (query.isLeft()){
            wrapper.likeLeft(query.getField(), query.getLikeValue());
        }else {
            wrapper.likeRight(query.getField(), query.getLikeValue());
        }

        return wrapper;
    }

    public static AbstractWrapper transRangeQuery(AbstractWrapper wrapper, RangeQuery query){
        if (StringUtils.isNotBlank(query.getMax())){
            if (query.getIncludeMax()){
                wrapper.le(query.getField(), query.getMax());
            }else {
                wrapper.lt(query.getField(), query.getMax());
            }
        }
        if (StringUtils.isNotBlank(query.getMin())){
            if (query.getIncludeMin()){
                wrapper.ge(query.getField(), query.getMin());
            }else {
                wrapper.gt(query.getField(), query.getMin());
            }
        }

        return wrapper;
    }

    public static AbstractWrapper transInQuery(AbstractWrapper wrapper, InQuery query){
        wrapper.in(query.getField(), query.getValues());
        return wrapper;
    }

    public static AbstractWrapper setLimit(AbstractWrapper wrapper, Limit limit){
        if (limit != null){
            wrapper.last("limit " + limit.getOffset() + " , " + limit.getCount());
        }
        return wrapper;
    }

    public static AbstractWrapper setOrderBy(AbstractWrapper wrapper, List<Order> orders){
        if (CollectionUtils.isEmpty(orders)){
            return wrapper;
        }
        //按order排序
        Collections.sort(orders, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return Integer.compare(o1.getOrder(), o2.getOrder());
            }
        });
        orders.stream().forEach(o -> {
            wrapper.orderBy(true,o.getSortStr().equalsIgnoreCase(QueryOrderEnum.ASC.getOrderStr()),StrUtil.toUnderlineCase(o.getField()));
        });

        return wrapper;
    }




}

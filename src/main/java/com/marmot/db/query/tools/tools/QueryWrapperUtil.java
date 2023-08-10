package com.marmot.db.query.tools.tools;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marmot.db.query.tools.enums.QueryOperatorEnum;
import com.marmot.db.query.tools.enums.QueryOrderEnum;
import com.marmot.db.query.tools.params.QueryCond;
import com.marmot.db.query.tools.params.QueryParam;
import com.marmot.db.query.tools.query.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author:zhaozhou
 * @Date: 2023/07/18
 * @Desc:
 */
public class QueryWrapperUtil {

    public static QueryWrapper transQueryCond(QueryWrapper wrapper, QueryCond cond){
        transQueries(wrapper,cond.getQueries());
        setLimit(wrapper,cond.getLimit());
        setOrderBy(wrapper,cond.getOrders());
        return wrapper;
    }

    public static <T extends AbstractBaseQuery> QueryWrapper transWithBooleanQuery(QueryWrapper wrapper, List<T> queries, String parentOperator){
        queries.stream().forEach(q -> {
            if (q instanceof BooleanQuery){
                if (parentOperator.equals(QueryOperatorEnum.OR.getOperator())){
                    wrapper.or((wq) -> transWithBooleanQuery(wq, ((BooleanQuery) q).getBaseQueries(), ((BooleanQuery) q).getOperator()));
                }else {
                    wrapper.and((wq) -> transWithBooleanQuery(wq, ((BooleanQuery) q).getBaseQueries(), ((BooleanQuery) q).getOperator()));
                }
            }
            if (q instanceof AbstractFieldQuery){
                ((AbstractFieldQuery) q).setField(StrUtil.toUnderlineCase(((AbstractFieldQuery) q).getField()));
                if (parentOperator.equals(QueryOperatorEnum.OR.getOperator())){
                    wrapper.or();
                }
                transQuery(wrapper, q);
            }
        });
        return wrapper;
    }


    public static <T extends AbstractBaseQuery> QueryWrapper transQueries(QueryWrapper wrapper, List<T> queries){
        return transWithBooleanQuery(wrapper, queries, QueryOperatorEnum.AND.getOperator());
    }



    public static <T extends AbstractBaseQuery> AbstractWrapper transQuery(AbstractWrapper wrapper, T query){
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
            if (query.isIncludeMax()){
                wrapper.le(query.getField(), query.getMax());
            }else {
                wrapper.lt(query.getField(), query.getMax());
            }
        }
        if (StringUtils.isNotBlank(query.getMin())){
            if (query.isIncludeMin()){
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



    public static void main(String[] args){
        QueryParam queryParam = QueryBuilder.newBuilder()
                .addEqualQuery("test","121212")
                .addLikeQuery("name","tom")
                .addRangeQuery("createTime","1000","10000",true,false)
                .addInQuery("id", Arrays.asList("12","1212"))
                .setLimit(0,100)
                .addOrder("test",QueryOrderEnum.ASC)
                .addOrder("name",QueryOrderEnum.DESC)
                .addBooleanQuery(
                        QueryBuilder.newBuilder().addEqualQuery("test","121212").addInQuery("name",Arrays.asList("1212","12131")),QueryOperatorEnum.OR)
                .addBooleanQuery(
                        QueryBuilder.newBuilder().addEqualQuery("test","121212").addInQuery("name",Arrays.asList("1212","12131")),QueryOperatorEnum.AND)
                .addBooleanQuery(
                        QueryBuilder.newBuilder().addEqualQuery("test","121212").addInQuery("name",Arrays.asList("1212","12131"))
                                .addBooleanQuery(QueryBuilder.newBuilder().addEqualQuery("createTime","121212").addInQuery("name",Arrays.asList("1212","12131")),QueryOperatorEnum.OR)
                                .addBooleanQuery(QueryBuilder.newBuilder().addEqualQuery("createTime","121212").addInQuery("name",Arrays.asList("1212","12131")),QueryOperatorEnum.AND)
                        ,QueryOperatorEnum.OR)
                .getQueryParam();
        System.out.println(JSONUtil.toJsonStr(queryParam));

        QueryCond cond = new QueryCond();
        String errMsg = QueryTransUtil.transQueryParam(queryParam,cond, Stream.of("test", "name","createTime","id").collect(Collectors.toSet()));
        System.out.println("trans errMsg=" + errMsg);

        QueryWrapper wrapper = new QueryWrapper();
        QueryWrapperUtil.transQueryCond(wrapper, cond);
        System.out.println("QueryWrapper=" + wrapper.getCustomSqlSegment());
    }



}

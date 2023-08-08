package com.marmot.db.query.tools.tools;

import cn.hutool.json.JSONUtil;
import com.marmot.db.query.tools.enums.QueryTypeEnum;
import com.marmot.db.query.tools.params.QueryParam;
import com.marmot.db.query.tools.query.*;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author:zhaozhou
 * @Date: 2023/07/18
 * @Desc:
 */
public class QueryBuilder {
    private QueryParam queryParam;

    public static QueryBuilder newBuilder(){
        QueryBuilder qb = new QueryBuilder();
        qb.queryParam = new QueryParam();
        qb.queryParam.setQueries(new LinkedList<>());
        return qb;
    }

    private QueryBuilder() {
    }

    public QueryBuilder addEqualQuery(String field, String value){
        Assert.hasLength(field,"field is empty");
        Assert.hasLength(field,"value is empty");
        EqualQuery query = new EqualQuery();
        query.setField(field);
        query.setValue(value);
        query.setQueryType(QueryTypeEnum.EQUAL.getName());
        queryParam.getQueries().add(JSONUtil.toJsonStr(query));
        return this;
    }

    public QueryBuilder addRangeQuery(String field, String max,String min,boolean includeMax, boolean includeMin){
        Assert.hasLength(field,"field is empty");
        Assert.isTrue(StringUtils.hasLength(max) || StringUtils.hasLength(min), "max or min, at least one exist");
        RangeQuery query = new RangeQuery();
        query.setField(field);
        query.setMax(max);
        query.setMin(min);
        query.setIncludeMax(includeMax);
        query.setIncludeMin(includeMin);
        query.setQueryType(QueryTypeEnum.RANGE.getName());
        queryParam.getQueries().add(JSONUtil.toJsonStr(query));
        return this;
    }


    public QueryBuilder addInQuery(String field, List<String> values){
        Assert.hasLength(field,"field is empty");
        Assert.notEmpty(values, "valueSet should not empty");
        InQuery query = new InQuery();
        query.setField(field);
        query.setValues(values);
        query.setQueryType(QueryTypeEnum.SET.getName());
        queryParam.getQueries().add(JSONUtil.toJsonStr(query));
        return this;
    }

    private QueryBuilder addLikeQuery(String field, String likeValue, boolean left, boolean right){
        Assert.hasLength(field,"field is empty");
        Assert.hasLength(likeValue, "likeValue should not empty");
        LikeQuery query = new LikeQuery();
        query.setField(field);
        query.setLikeValue(likeValue);
        query.setQueryType(QueryTypeEnum.LIKE.getName());
        query.setLeft(left);
        query.setRight(right);
        queryParam.getQueries().add(JSONUtil.toJsonStr(query));
        return this;
    }

    public QueryBuilder setLimit(long offset, long count){
        queryParam.setLimit(new Limit(offset, count));
        return this;
    }

    public QueryBuilder addLikeQuery(String field, String likeValue){
        return this.addLikeQuery(field, likeValue,true, true);
    }
    public QueryBuilder addLeftLikeQuery(String field, String likeValue){
        return this.addLikeQuery(field, likeValue,true, false);
    }

    public QueryBuilder addRightLikeQuery(String field, String likeValue){
        return this.addLikeQuery(field, likeValue,false, true);
    }

    public QueryParam getQueryParam() {
        return queryParam;
    }

    public static void main(String[] args){
        QueryParam queryParam = QueryBuilder.newBuilder()
                .addEqualQuery("test","121212")
                .addLikeQuery("name","tom", true, true)
                .addRangeQuery("createTime","1000","10000",true,false)
                .addInQuery("id", Arrays.asList("12","1212"))
                .setLimit(100,100)
                .getQueryParam();
        System.out.println(JSONUtil.toJsonStr(queryParam));
    }
}

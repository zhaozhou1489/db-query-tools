package com.marmot.db.query.tools.tools;

import cn.hutool.json.JSONUtil;
import com.marmot.db.query.tools.enums.QueryOperatorEnum;
import com.marmot.db.query.tools.enums.QueryOrderEnum;
import com.marmot.db.query.tools.params.QueryParam;
import com.marmot.db.query.tools.query.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author:zhaozhou
 * @Date: 2023/07/18
 * @Desc: 查询语句构建器
 */
public class QueryBuilder {
    //查询参数
    private QueryParam queryParam;

    //排序顺序缓存
    private int orderNum = 0;

    public static QueryBuilder newBuilder(){
        QueryBuilder qb = new QueryBuilder();
        qb.queryParam = new QueryParam();
        qb.queryParam.setQueries(new LinkedList<>());

        return qb;
    }


    /**
     * @Desc 添加Equal查询
     **/
    public QueryBuilder addEqualQuery(String field, String value){
        Assert.hasLength(field,"field is blank");
        Assert.hasLength(field,"value is blank");
        EqualQuery query = new EqualQuery(field,value,false);
        this.addQuery(query);
        return this;
    }

    /**
     * @Desc 添加NotEqual查询
     **/
    public QueryBuilder addNotEqualQuery(String field, String value){
        Assert.hasLength(field,"field is blank");
        Assert.hasLength(field,"value is blank");
        EqualQuery query = new EqualQuery(field,value,true);
        this.addQuery(query);
        return this;
    }

    /**
     * @Desc 添加Range查询
     **/
    public QueryBuilder addRangeQuery(String field, String max,String min,boolean includeMax, boolean includeMin){
        Assert.hasLength(field,"field is blank");
        Assert.isTrue(StringUtils.hasLength(max) || StringUtils.hasLength(min), "max or min, at least one exist");
        RangeQuery query = new RangeQuery(field, max, min,includeMax,includeMin);
        this.addQuery(query);
        return this;
    }


    /**
     * @Desc 添加In查询
     **/
    public QueryBuilder addInQuery(String field, List<String> values){
        Assert.hasLength(field,"field is blank");
        Assert.notEmpty(values, "valueSet should not blank");
        InQuery query = new InQuery(field,values,false);
        this.addQuery(query);
        return this;
    }

    /**
     * @Desc 添加NotIn查询
     **/
    public QueryBuilder addNotInQuery(String field, List<String> values){
        Assert.hasLength(field,"field is blank");
        Assert.notEmpty(values, "valueSet should not blank");
        InQuery query = new InQuery(field,values,true);
        this.addQuery(query);
        return this;
    }

    private QueryBuilder addLikeQuery(String field, String likeValue, boolean left, boolean right,boolean opposition){
        Assert.hasLength(field,"field is blank");
        Assert.hasLength(likeValue, "likeValue should not blank");
        LikeQuery query = new LikeQuery(field,likeValue, left,right,opposition);
        this.addQuery(query);
        return this;
    }

    private void addQuery(AbstractBaseQuery query){
        this.queryParam.getQueries().add(JSONUtil.toJsonStr(query));
    }


    /**
     * @Desc 添加Like查询
     **/
    public QueryBuilder addLikeQuery(String field, String likeValue){
        return this.addLikeQuery(field, likeValue,true, true, false);
    }

    /**
     * @Desc 添加NotLike查询
     **/
    public QueryBuilder addNotLikeQuery(String field, String likeValue){
        return this.addLikeQuery(field, likeValue,true, true, true);
    }

    /**
     * @Desc 添加leftLike查询
     **/
    public QueryBuilder addLeftLikeQuery(String field, String likeValue){
        return this.addLikeQuery(field, likeValue,true, false,false);
    }

    /**
     * @Desc 添加NotleftLike查询
     **/
    public QueryBuilder addNoteLeftLikeQuery(String field, String likeValue){
        return this.addLikeQuery(field, likeValue,true, false,true);
    }

    /**
     * @Desc 添加RightLike查询
     **/
    public QueryBuilder addRightLikeQuery(String field, String likeValue){
        return this.addLikeQuery(field, likeValue,false, true,false);
    }

    /**
     * @Desc 添加rightLike查询
     **/
    public QueryBuilder addNotRightLikeQuery(String field, String likeValue){
        return this.addLikeQuery(field, likeValue,false, true,true);
    }

    /**
     * @Desc 添加isNull查询
     **/
    public QueryBuilder addNullQuery(String field){
        Assert.hasLength(field,"field is blank");
        NullQuery query = new NullQuery(field,false);
        this.addQuery(query);
        return this;
    }

    /**
     * @Desc 添加isNotNull查询
     **/
    public QueryBuilder addNotNullQuery(String field){
        Assert.hasLength(field,"field is blank");
        NullQuery query = new NullQuery(field,true);
        this.addQuery(query);
        return this;
    }

    /**
     * @Desc 添加Boolean查询
     **/
    public QueryBuilder addBooleanQuery(QueryBuilder qb, QueryOperatorEnum operator){
        Assert.notNull(operator == null,"operator is empty");
        if (qb != null && qb.getQueryParam() != null && CollectionUtils.isNotEmpty(qb.getQueryParam().getQueries())){
            this.queryParam.getQueries().add(JSONUtil.toJsonStr(new BooleanQuery(qb.getQueryParam().getQueries(),operator.getOperator())));
        }
        return this;
    }

    /**
     * @Desc 设置limit
     **/
    public QueryBuilder setLimit(long offset, long count){
        queryParam.setLimit(new Limit(offset, count));
        return this;
    }


    /**
     * @Desc 添加排序规则
     **/
    public QueryBuilder addOrder(String field, QueryOrderEnum orderEnum){
        Assert.hasLength(field,"field is blank");
        Assert.notNull(orderEnum,"orderEnum is null");
        if (queryParam.getOrders()==null) queryParam.setOrders(new LinkedList<>());
        queryParam.getOrders().add(new Order(field, orderNum++, orderEnum.getOrderStr()));
        return this;
    }


    public QueryParam getQueryParam() {
        return this.queryParam;
    }



    public static void main(String[] args){
        QueryParam queryParam = QueryBuilder.newBuilder()
                .addEqualQuery("test","121212")
                .addLikeQuery("name","tom", true, true,false)
                .addRangeQuery("createTime","1000","10000",true,false)
                .addInQuery("id", Arrays.asList("12","1212"))
                .setLimit(0,100)
                .addOrder("test",QueryOrderEnum.ASC)
                .addOrder("name",QueryOrderEnum.DESC)
                .addBooleanQuery(QueryBuilder.newBuilder().addEqualQuery("test","121212").addInQuery("name",Arrays.asList("1212","12131")),QueryOperatorEnum.OR)
                .getQueryParam();
        System.out.println(JSONUtil.toJsonStr(queryParam));
    }
}

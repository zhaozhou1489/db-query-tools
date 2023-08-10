package com.marmot.db.query.tools.tools;

import cn.hutool.json.JSONUtil;
import com.marmot.db.query.tools.enums.QueryOperatorEnum;
import com.marmot.db.query.tools.query.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @Author:zhaozhou
 * @Date: 2023/07/18
 * @Desc:
 */
public class QueryValidator {
    public static <T extends AbstractBaseQuery>  String validQuery(T query){
        if (query instanceof EqualQuery){
            EqualQuery q = (EqualQuery) query;
            if (StringUtils.isBlank(q.getField()) || StringUtils.isBlank(q.getValue())){
                return "EqualQuery, [field] and [value] are required";
            }
        }else if (query instanceof LikeQuery){
            LikeQuery q = (LikeQuery) query;
            if (StringUtils.isBlank(q.getField()) || StringUtils.isBlank(q.getLikeValue())){
                return "LikeQuery, [field] and [likeValue] are required";
            }
            if (!q.isLeft() && !q.isRight()){
                return "LikeQuery, [left] or [right], at least one should true";
            }
        }else if (query instanceof RangeQuery){
            RangeQuery q = (RangeQuery) query;
            if (StringUtils.isBlank(q.getField())){
                return "LikeQuery, [field] is required";
            }
            if (StringUtils.isBlank(q.getMax()) || StringUtils.isBlank(q.getMin())){
                return "LikeQuery,[max] or [min] is required";
            }
        }else if (query instanceof InQuery){
            InQuery q = (InQuery) query;
            if (StringUtils.isBlank(q.getField()) || CollectionUtils.isEmpty(q.getValues())){
                return "LikeQuery, [field] and [valueSet] are required";
            }
        }else if (query instanceof NullQuery){
            NullQuery q = (NullQuery) query;
            if (StringUtils.isBlank(q.getField())){
                return "NullQuery, [field] is required";
            }
        }else if (query instanceof BooleanQuery){
            BooleanQuery q = (BooleanQuery) query;
            List<String> operators = Arrays.asList(QueryOperatorEnum.OR.getOperator(), QueryOperatorEnum.AND.getOperator());
            if (StringUtils.isBlank(q.getOperator()) || !operators.contains(q.getOperator().toLowerCase())){
                return "BooleanQuery, [operator] is invalid";
            }
            if (CollectionUtils.isEmpty(q.getQueries())){
                return "BooleanQuery, [queries] is empty";
            }
            String errMsg = validQueries(q.getBaseQueries());
            return errMsg;
        } else {
            return "Unknown query, str=" + JSONUtil.toJsonStr(query);
        }

        return null;
    }

    public static <T extends AbstractBaseQuery>  String validQueries(List<T> queries){
        for (T query: queries){
            String errMsg = validQuery(query);
            if (StringUtils.isNotBlank(errMsg)){
                return errMsg;
            }
        }
        return null;
    }

}

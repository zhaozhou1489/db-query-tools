package com.marmot.db.tools.example.controller;

import com.marmot.db.query.tools.params.QueryCond;
import com.marmot.db.query.tools.params.QueryParam;
import com.marmot.db.query.tools.tools.QueryTransUtil;
import com.marmot.db.tools.example.common.ResponseResult;
import com.marmot.db.tools.example.domain.entity.User;
import com.marmot.db.tools.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashSet;


/**
 * @Author:zhaozhou
 * @Date: 2023/08/22
 * @Desc: todo
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;


    /**
     {
     "queries": ["{\"likeValue\":\"11\",\"left\":true,\"right\":true,\"opposition\":false,\"field\":\"name\",\"type\":\"like\"}", "{\"max\":\"1000\",\"min\":\"1\",\"includeMax\":true,\"includeMin\":false,\"field\":\"code\",\"type\":\"range\"}", "{\"values\":[\"1\",\"2\",\"3\"],\"opposition\":false,\"field\":\"id\",\"type\":\"in\"}"],
     "limit": {
     "offset": 0,
     "count": 100
     },
     "orders": [{
     "field": "id",
     "order": 0,
     "sortStr": "ASC"
     }, {
     "field": "name",
     "order": 1,
     "sortStr": "DESC"
     }]
     }



     {
     "queries": ["{\"likeValue\":\"11\",\"left\":true,\"right\":true,\"opposition\":false,\"field\":\"name\",\"type\":\"like\"}"],
     "limit": {
     "offset": 0,
     "count": 100
     },
     "orders": [{
     "field": "id",
     "order": 0,
     "sortStr": "ASC"
     }, {
     "field": "name",
     "order": 1,
     "sortStr": "DESC"
     }]
     }
     **/
    @RequestMapping("/find")
    public ResponseResult findUser(@RequestBody QueryParam qp){
        QueryCond cond = new QueryCond();
        String msg = QueryTransUtil.transQueryParam(qp,cond,new HashSet<>(Arrays.asList("id","name","code","birthday","gender","country","createTime","updateTime")));
        if (StringUtils.isNotBlank(msg)){
            return new ResponseResult<>(-1,msg,null);
        }

        return new ResponseResult<>(0,"success",userService.queryByCond(cond));
    }
}

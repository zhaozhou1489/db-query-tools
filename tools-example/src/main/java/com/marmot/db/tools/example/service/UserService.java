package com.marmot.db.tools.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.marmot.db.query.tools.params.QueryCond;
import com.marmot.db.query.tools.tools.QueryWrapperUtil;
import com.marmot.db.tools.example.domain.entity.User;
import com.marmot.db.tools.example.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:zhaozhou
 * @Date: 2023/08/22
 * @Desc:
 */
@Service
@Slf4j
public class UserService extends ServiceImpl<UserMapper, User> {

    public List<User> queryByCond(QueryCond cond){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        QueryWrapperUtil.transQueryCond(wrapper,cond);
        return baseMapper.selectList(wrapper);
    }
}

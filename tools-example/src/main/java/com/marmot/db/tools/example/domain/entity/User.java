package com.marmot.db.tools.example.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:zhaozhou
 * @Date: 2023/08/22
 * @Desc:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;

    private String code;

    private String name;

    private String birthday;

    private String gender;

    private String country;

    private Long createTime;

    private Long updateTime;
}

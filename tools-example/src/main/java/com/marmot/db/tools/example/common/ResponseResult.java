package com.marmot.db.tools.example.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Author:zhaozhou
 * @Date: 2023/08/22
 * @Desc: todo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult<T> implements Serializable {

    private Integer code;

    private String msg;

    private T data;
}

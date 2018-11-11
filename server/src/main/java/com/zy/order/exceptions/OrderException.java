package com.zy.order.exceptions;

import com.zy.order.enums.ResultEnum;

public class OrderException extends RuntimeException {

    private Integer code;

    public OrderException(ResultEnum resultEnum){
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public OrderException(Integer code, String message){
        super(message);
        this.code = code;
    }
}

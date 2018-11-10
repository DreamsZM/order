package com.zy.order.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    PARAM_ERROR(11, "参数异常"),

    ORDER_NOT_EXIST(12, "订单不存在"),

    ORDERDETAIL_NOT_EXIST(13, "订单详情不存在"),

    ORDER_STATUS_ERROR(14, "订单状态异常"),

    ORDER_UPDATE_ERROR(15, "订单状态更新失败"),

    ORDER_DETAIL_EMPTY(15, "订单为空"),

    ORDER_PAY_STATUS_ERROR(16, "订单支付状态不正确"),

    CART_EMPTY(17, "购物出不能为空"),

    ORDER_OWENER_ERROR(18, "该订单不属于当前用户"),

    ORDER_CANCEL_SUCCESS(21, "订单取消成功"),

    ORDER_FINISH_SUCCESS(22, "订单完成成功");

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

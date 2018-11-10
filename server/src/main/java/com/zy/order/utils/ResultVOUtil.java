package com.zy.order.utils;

import com.zy.order.vo.ResultVO;

public class ResultVOUtil {

    public static ResultVO success(Object object){
        return new ResultVO(0, "成功", object);
    }
}

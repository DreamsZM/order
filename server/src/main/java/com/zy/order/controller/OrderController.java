package com.zy.order.controller;

import com.zy.order.utils.ResultVOUtil;
import com.zy.order.vo.ResultVO;
import com.zy.order.converter.OrderForm2OrderDTOConverter;
import com.zy.order.dto.OrderDTO;
import com.zy.order.enums.ResultEnum;
import com.zy.order.exceptions.SellException;
import com.zy.order.form.OrderForm;
import com.zy.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResultVO<Map<String, String>> create(@Valid OrderForm orderForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("【创建订单】参数不正确, orderForm = {}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }

        OrderDTO orderDTO = OrderForm2OrderDTOConverter.converter(orderForm);
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("【创建订单】购物车不能为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }

        OrderDTO resultOrder = orderService.create(orderDTO);
        if (resultOrder == null){
            log.error("【创建订单】订单创建失败");
        }

        Map<String, String> map = new HashMap<>();
        map.put("orderId", resultOrder.getOrderId());
        return ResultVOUtil.success(map);
    }
}

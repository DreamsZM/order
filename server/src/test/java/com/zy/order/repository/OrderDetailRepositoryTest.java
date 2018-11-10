package com.zy.order.repository;

import com.zy.order.OrderApplicationTests;
import com.zy.order.utils.KeyUtil;
import com.zy.order.pojo.OrderDetail;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderDetailRepositoryTest extends OrderApplicationTests{

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Test
    public void testSave(){
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setDetailId(KeyUtil.getUniqueKey());
        orderDetail.setOrderId("15403030314499258866");
        orderDetail.setProductId("1");
        orderDetail.setProductName("TEST");
        orderDetail.setProductPrice(new BigDecimal(100));
        orderDetail.setProductQuantity(10);
        orderDetail.setProductIcon("xxx");
        OrderDetail result = orderDetailRepository.save(orderDetail);
        Assert.assertNotNull(result);

    }

}
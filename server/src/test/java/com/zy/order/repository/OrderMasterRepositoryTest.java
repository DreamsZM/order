package com.zy.order.repository;

import com.zy.order.utils.KeyUtil;
import com.zy.order.enums.OrderStatusEnum;
import com.zy.order.enums.PayStatusEnum;
import com.zy.order.pojo.OrderMaster;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Test
    public void testSave(){
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId(KeyUtil.getUniqueKey());
        orderMaster.setBuyerName("zy");
        orderMaster.setBuyerPhone("13183812001");
        orderMaster.setBuyerOpenid("123");
        orderMaster.setBuyerAddress("滨江楼");
        orderMaster.setOrderAmount(new BigDecimal(100));
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        OrderMaster result = orderMasterRepository.save(orderMaster);
        System.out.println(result.toString());

    }
}
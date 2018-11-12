package com.zy.order.service.impl;

import com.zy.order.enums.ResultEnum;
import com.zy.order.exceptions.OrderException;
import com.zy.order.utils.KeyUtil;
import com.zy.order.dto.OrderDTO;
import com.zy.order.enums.OrderStatusEnum;
import com.zy.order.enums.PayStatusEnum;
import com.zy.order.pojo.OrderDetail;
import com.zy.order.pojo.OrderMaster;
import com.zy.order.repository.OrderDetailRepository;
import com.zy.order.repository.OrderMasterRepository;
import com.zy.order.service.OrderService;
import com.zy.product.client.ProductClient;
import com.zy.product.common.DecreaseStockInput;
import com.zy.product.common.ProductInfoOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private ProductClient productClient;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        //订单id
        String orderId = KeyUtil.getUniqueKey();
        List<OrderDetail> orderDetailList = orderDTO.getOrderDetailList();

        List<String> productIdList = orderDetailList.stream()
                .map(OrderDetail :: getProductId)
                .collect(Collectors.toList());
        List<ProductInfoOutput> productInfoOutputList = productClient.listForOrder(productIdList);

        BigDecimal orderAmount = new BigDecimal(0);
        for (OrderDetail orderDetail : orderDetailList){
            //查询商品信息，得到商品单价
            for (ProductInfoOutput productInfo : productInfoOutputList){
                if (productInfo.getProductId().equals(orderDetail.getProductId())){
                    //计算orderAmount
                    orderAmount = productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity())).add(orderAmount);

                    BeanUtils.copyProperties(productInfo, orderDetail);
                    orderDetail.setDetailId(KeyUtil.getUniqueKey());
                    //订单详情设置orderId和其他字段后入库
                    orderDetail.setOrderId(orderId);
                    OrderDetail resultOrderDetail = orderDetailRepository.save(orderDetail);
                    if (resultOrderDetail == null){
                        log.error("订单详情入库失败");
                        throw new RuntimeException("订单详情入库失败");
                    }
                }
            }
        }

        //减库存
        List<DecreaseStockInput> decreaseStockInputList = orderDTO.getOrderDetailList().stream()
                .map(e -> new DecreaseStockInput(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productClient.decreaseStock(decreaseStockInputList);
        orderDTO.setOrderId(orderId);
        orderDTO.setOrderAmount(orderAmount);
        orderDTO.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderDTO.setPayStatus(PayStatusEnum.WAIT.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMasterRepository.save(orderMaster);

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(String orderId) {
        //1、查询订单
        Optional<OrderMaster> orderMasterOptional = orderMasterRepository.findById(orderId);
        if (!orderMasterOptional.isPresent()){
            throw new OrderException(ResultEnum.ORDER_NOT_EXIST);
        }
        //2、判断订单状态
        OrderMaster orderMaster = orderMasterOptional.get();
        if (OrderStatusEnum.NEW.getCode() != orderMaster.getOrderStatus()){
            throw new OrderException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //3、修改订单状态
        orderMaster.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        orderMasterRepository.save(orderMaster);

        OrderDTO orderDTO = new OrderDTO();
        //查询订单详情
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)){
            throw new OrderException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }

        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }
}

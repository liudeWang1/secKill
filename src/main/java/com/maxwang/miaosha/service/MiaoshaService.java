package com.maxwang.miaosha.service;

import com.maxwang.miaosha.domain.OrderInfo;
import com.maxwang.miaosha.domain.User;
import com.maxwang.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiaoshaService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Transactional
    public OrderInfo miaosha(User user, GoodsVo goodsVo) {

        /**
         * 减库存
         */
        goodsService.reduceStock(goodsVo);

        /**
         * 写入秒杀订单
         */
        return orderService.createOrder(user,goodsVo);

    }
}

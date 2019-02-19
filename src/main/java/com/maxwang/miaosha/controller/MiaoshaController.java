package com.maxwang.miaosha.controller;

import com.maxwang.miaosha.domain.MiaoshaOrder;
import com.maxwang.miaosha.domain.OrderInfo;
import com.maxwang.miaosha.domain.User;
import com.maxwang.miaosha.service.GoodsService;
import com.maxwang.miaosha.service.MiaoshaService;
import com.maxwang.miaosha.service.OrderService;
import com.maxwang.miaosha.util.CodeMsg;
import com.maxwang.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @RequestMapping("/do_miaosha")
    public String miaosha(Model model,User user,@RequestParam("goodsId") long goodsId){

        model.addAttribute("user",user);
        if (user == null){
            return "login";
        }

        /**
         * 判断库存
         */
        GoodsVo goodsVo = goodsService.getGoodsDetailById(goodsId);
        int stock = goodsVo.getStockCount();
        if (stock <= 0){
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }

        /**
         * 判断是否已经秒杀到了
         */
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if (order != null){
            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            return "miaosha_fail";
        }

        /**
         * 减少库存  下订单 写入秒杀订单
         */
        OrderInfo orderInfo = miaoshaService.miaosha(user,goodsVo);

        model.addAttribute("orderInfo",orderInfo);
        model.addAttribute("goods",goodsVo);

        return "order_detail";
    }
}

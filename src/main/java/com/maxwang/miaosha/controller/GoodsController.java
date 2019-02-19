package com.maxwang.miaosha.controller;

import com.maxwang.miaosha.domain.User;
import com.maxwang.miaosha.service.GoodsService;
import com.maxwang.miaosha.service.UserService;
import com.maxwang.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    UserService userService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/to_list")
    public String list(Model model,User user){

        model.addAttribute("user",user);
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodsList);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, User user,@PathVariable("goodsId") long goodsId){

        model.addAttribute("user",user);

        GoodsVo goods = goodsService.getGoodsDetailById(goodsId);
        model.addAttribute("goods",goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;

        if (now < startAt){
            miaoshaStatus = 0;
            remainSeconds = (int) ((startAt-now)/1000);
        }else if (now > endAt){
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {
            miaoshaStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("miaoshaStatus",miaoshaStatus);
        model.addAttribute("remainSeconds",remainSeconds);
        return "goods_detail";
    }
}

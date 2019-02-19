package com.maxwang.miaosha.service;

import com.maxwang.miaosha.dao.GoodsDao;
import com.maxwang.miaosha.domain.MiaoshaGoods;
import com.maxwang.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsDetailById(long goodsId){
        return goodsDao.getGoodsDetailById(goodsId);
    }


    public void reduceStock(GoodsVo goodsVo) {
        MiaoshaGoods g=new MiaoshaGoods();
        g.setGoodsId(goodsVo.getId());
        goodsDao.reduceStock(g);
    }
}

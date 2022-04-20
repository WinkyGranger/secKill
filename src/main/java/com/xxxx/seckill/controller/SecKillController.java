package com.xxxx.seckill.controller;

import com.xxxx.seckill.pojo.TOrder;
import com.xxxx.seckill.pojo.TSeckillOrder;
import com.xxxx.seckill.pojo.TUser;
import com.xxxx.seckill.service.TGoodsService;
import com.xxxx.seckill.service.TOrderService;
import com.xxxx.seckill.service.TSeckillOrderService;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;

@Controller
@RequestMapping("/seckill")
public class SecKillController {
    @Autowired
    private TGoodsService tGoodsService;
    @Autowired
    private TSeckillOrderService tSeckillOrderService;
    @Autowired
    private TOrderService tOrderService;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/doSeckill2")
    public String doSecKill(Model model, TUser user, Long goodsId){
        if(user == null){
            return "login";
        }
        model.addAttribute("user",user);
        GoodsVo goods = tGoodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if(goods.getStockCount() < 1){
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "secKillFail";
        }
        //判断订单是否重复抢购
        boolean one = tSeckillOrderService.getOne(user.getId(), goodsId);
        if(one == false){
            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
            return "secKillFail";
        }
        TOrder order = tOrderService.secKill(user, goods);
        model.addAttribute("order",order);
        model.addAttribute("goods",goods);
        return "orderDetail";
    }

    @RequestMapping(value = "/doSeckill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSecKill(@PathVariable String path, TUser user, Long goodsId,Model model) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        GoodsVo goods = tGoodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if(goods.getStockCount() < 1){
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //判断订单是否重复抢购
        boolean one = tSeckillOrderService.getOne(user.getId(), goodsId);
        if(one == false){
            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        TOrder order = tOrderService.secKill(user, goods);
        return RespBean.success(order);


    }


}

package com.xxxx.seckill.controller;

import com.xxxx.seckill.pojo.TUser;
import com.xxxx.seckill.service.TGoodsService;
import com.xxxx.seckill.vo.DetailVo;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;


@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    TGoodsService tGoodsService;

    @RequestMapping("/toList")
    public String toList( Model model, TUser user){

        model.addAttribute("user",user);
        model.addAttribute("goodsList",tGoodsService.findGoodsVo());
        return "goodsList";
    }

    @RequestMapping(value = "/detail/{goodsId}",method = RequestMethod.GET)
    @ResponseBody
    public RespBean toDetail(@PathVariable("goodsId")Long goodsId, Model model, TUser user){
        model.addAttribute("user",user);
        GoodsVo goodsVo = tGoodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date date = new Date();
        //秒杀状态
        int secKillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;

        if(date.before(startDate)){
            remainSeconds = (int) ((startDate.getTime() - date.getTime()) / 1000);
        }else if(date.after(endDate)){
            secKillStatus = 1;
            remainSeconds = -1;
        }else{
            secKillStatus = 2;
            remainSeconds = 0;
        }
        DetailVo detailVo = new DetailVo();
        detailVo.setTUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setRemainSeconds(remainSeconds);
        detailVo.setSecKillStatus(secKillStatus);
        return RespBean.success(detailVo);
    }
}

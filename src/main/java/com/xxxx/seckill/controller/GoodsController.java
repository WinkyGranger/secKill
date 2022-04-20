package com.xxxx.seckill.controller;

import com.xxxx.seckill.pojo.TUser;
import com.xxxx.seckill.service.TGoodsService;
import com.xxxx.seckill.vo.DetailVo;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private TGoodsService tGoodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, TUser user, HttpServletRequest request, HttpServletResponse response){
        //redis中查找是否有前端的缓存
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String)valueOperations.get("goodsList");
        if(!StringUtils.isEmpty(html)){
            return html;
        }

        model.addAttribute("user",user);
        model.addAttribute("goodsList",tGoodsService.findGoodsVo());

        //手动渲染
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(),
                model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", context);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList",html);
            log.info("redis对goodsList页面进行缓存");
        }
        return html;
//        return "goodsList";
    }


    @RequestMapping(value = "/toDetail/{goodsId}", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail2(Model model, TUser user, @PathVariable Long goodsId,
                            HttpServletRequest request, HttpServletResponse response) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //Redis中获取页面，如果不为空，直接返回页面
        String html = (String) valueOperations.get("goodsDetail:" + goodsId);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        model.addAttribute("user", user);
        GoodsVo goodsVo = tGoodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        //秒杀状态
        int secKillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;
        //秒杀还未开始
        if (nowDate.before(startDate)) {
            remainSeconds = ((int) ((startDate.getTime() - nowDate.getTime()) / 1000));
        } else if (nowDate.after(endDate)) {
            //	秒杀已结束
            secKillStatus = 2;
            remainSeconds = -1;
        } else {
            //秒杀中
            secKillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("secKillStatus", secKillStatus);
        model.addAttribute("goods", goodsVo);
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(),
                model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", context);
        if (!StringUtils.isEmpty(html)) {
            valueOperations.set("goodsDetail:" + goodsId, html, 60, TimeUnit.SECONDS);
        }
        return html;
        // return "goodsDetail";
    }


    @RequestMapping(value = "/detail/{goodsId}")
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

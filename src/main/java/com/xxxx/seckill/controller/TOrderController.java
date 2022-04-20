package com.xxxx.seckill.controller;


import com.xxxx.seckill.pojo.TUser;
import com.xxxx.seckill.service.TOrderService;
import com.xxxx.seckill.vo.OrderDetailVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Winky
 * @since 2022-04-17
 */
@RestController
@RequestMapping("/order")
public class TOrderController {

    @Autowired
    TOrderService tOrderService;

    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(TUser user, Long orderId){
        if(user == null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        OrderDetailVo detail = tOrderService.detail(orderId);
        return RespBean.success(detail);

    }


}


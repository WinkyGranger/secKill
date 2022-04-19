package com.xxxx.seckill.controller;


import com.xxxx.seckill.pojo.TUser;
import com.xxxx.seckill.service.TUserService;
import com.xxxx.seckill.vo.RespBean;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author Winky
 * @since 2022-04-16
 */
@RestController
@RequestMapping("/user")
public class TUserController {

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("返回用户信息")
    public RespBean info(TUser user) {
        return RespBean.success(user);
    }

}


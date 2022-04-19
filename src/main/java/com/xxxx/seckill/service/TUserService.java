package com.xxxx.seckill.service;

import com.xxxx.seckill.pojo.TUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.vo.LoginVo;
import com.xxxx.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Winky
 * @since 2022-04-16
 */
public interface TUserService extends IService<TUser> {

    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    TUser getUserByCookie(String userTicket,  HttpServletRequest request, HttpServletResponse response);
}

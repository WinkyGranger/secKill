package com.xxxx.seckill.service.impl;

import com.xxxx.seckill.exception.GlobalException;
import com.xxxx.seckill.pojo.TUser;
import com.xxxx.seckill.mapper.TUserMapper;
import com.xxxx.seckill.service.TUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.utils.CookieUtil;
import com.xxxx.seckill.utils.MD5Util;
import com.xxxx.seckill.utils.UUIDUtil;
import com.xxxx.seckill.utils.ValidatorUtil;
import com.xxxx.seckill.vo.LoginVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Winky
 * @since 2022-04-16
 */
@Service
@Slf4j
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements TUserService {
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        if(!ValidatorUtil.isMobile(mobile)){
            throw new GlobalException(RespBeanEnum.MOBILE_ERROR);
        }
        TUser tUser = baseMapper.selectById(mobile);
        if(tUser == null){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        if(!MD5Util.formPassToDBPass(password,tUser.getSalt()).equals(tUser.getPassword())){
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        //生成Cookie
        String ticket = UUIDUtil.uuid();
//        request.getSession().setAttribute(ticket,tUser);
        CookieUtil.setCookie(request,response,"userTicket",ticket);
        redisTemplate.opsForValue().set("user" + ticket,tUser);

        return RespBean.success(ticket);


    }

    @Override
    public TUser getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        if(userTicket == null){
            return null;
        }
        TUser tUser = (TUser) redisTemplate.opsForValue().get("user" + userTicket);

        if(tUser != null){
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }
        return tUser;
    }
}

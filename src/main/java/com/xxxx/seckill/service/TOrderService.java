package com.xxxx.seckill.service;

import com.xxxx.seckill.pojo.TOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.pojo.TUser;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Winky
 * @since 2022-04-17
 */
public interface TOrderService extends IService<TOrder> {

    TOrder secKill(TUser user, GoodsVo goods);

    OrderDetailVo detail(Long orderId);
}

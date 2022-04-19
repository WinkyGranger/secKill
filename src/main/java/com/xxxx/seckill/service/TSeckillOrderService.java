package com.xxxx.seckill.service;

import com.xxxx.seckill.pojo.TSeckillOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Winky
 * @since 2022-04-17
 */
public interface TSeckillOrderService extends IService<TSeckillOrder> {

    boolean getOne(Long id, Long goodsId);
}

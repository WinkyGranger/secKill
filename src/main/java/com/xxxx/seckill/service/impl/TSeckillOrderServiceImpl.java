package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.seckill.pojo.TSeckillOrder;
import com.xxxx.seckill.mapper.TSeckillOrderMapper;
import com.xxxx.seckill.pojo.TUser;
import com.xxxx.seckill.service.TOrderService;
import com.xxxx.seckill.service.TSeckillOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Winky
 * @since 2022-04-17
 */
@Service
public class TSeckillOrderServiceImpl extends ServiceImpl<TSeckillOrderMapper, TSeckillOrder> implements TSeckillOrderService {

    @Autowired
    private TSeckillOrderMapper tSeckillOrderMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean getOne(Long id, Long goodsId) {
        QueryWrapper<TSeckillOrder> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",id);
        queryWrapper.eq("goods_id",goodsId);

        Integer integer = baseMapper.selectCount(queryWrapper);
        if(integer >= 1){
            return false;
        }
        return true;
    }

    @Override
    public Long getResult(TUser user, Long goodsId) {
        QueryWrapper<TSeckillOrder> orderQW = new QueryWrapper<>();
        orderQW.eq("user_id",user.getId()).eq("goods_id",goodsId);
        TSeckillOrder tSeckillOrder = tSeckillOrderMapper.selectOne(orderQW);
        if(tSeckillOrder != null){
            return tSeckillOrder.getId();
        }else if(redisTemplate.hasKey("isStockEmpty" + goodsId)){
            return -1L;
        }else{
            return 0L;
        }


    }
}

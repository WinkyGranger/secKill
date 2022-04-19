package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxxx.seckill.pojo.TSeckillOrder;
import com.xxxx.seckill.mapper.TSeckillOrderMapper;
import com.xxxx.seckill.service.TSeckillOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
}

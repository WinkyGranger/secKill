package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xxxx.seckill.exception.GlobalException;
import com.xxxx.seckill.mapper.TSeckillOrderMapper;
import com.xxxx.seckill.pojo.TOrder;
import com.xxxx.seckill.mapper.TOrderMapper;
import com.xxxx.seckill.pojo.TSeckillGoods;
import com.xxxx.seckill.pojo.TSeckillOrder;
import com.xxxx.seckill.pojo.TUser;
import com.xxxx.seckill.service.TGoodsService;
import com.xxxx.seckill.service.TOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.service.TSeckillGoodsService;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.OrderDetailVo;
import com.xxxx.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Winky
 * @since 2022-04-17
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

    @Autowired
    private TSeckillGoodsService tSeckillGoodsService;
    @Autowired
    private TOrderMapper tOrderMapper;
    @Autowired
    private TSeckillOrderMapper tSeckillOrderMapper;
    @Autowired
    private TGoodsService tGoodsService;
    @Autowired
    private RedisTemplate redisTemplate;




    @Override
    @Transactional
    public TOrder secKill(TUser user, GoodsVo goods) {

        ValueOperations valueOperations = redisTemplate.opsForValue();
        TSeckillGoods seckillGoods = tSeckillGoodsService.getOne(new QueryWrapper<TSeckillGoods>().eq("goods_id", goods.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);

        tSeckillGoodsService.update(new UpdateWrapper<TSeckillGoods>()
                .setSql("stock_count = " + "stock_count-1")
                .eq("goods_id", goods.getId())
                .gt("stock_count", 0)
        );
        if (seckillGoods.getStockCount() < 1) {
            //判断是否还有库存
            valueOperations.set("isStockEmpty:" + goods.getId(), "0");
            return null;
        }

        //加一条流水号 生成订单
        TOrder tOrder = new TOrder();
        tOrder.setUserId(user.getId());
        tOrder.setDeliveryAddrId(0L);
        tOrder.setGoodsId(goods.getId());
        tOrder.setGoodsName(goods.getGoodsName());
        tOrder.setGoodsCount(1);
        tOrder.setGoodsPrice(goods.getGoodsPrice());
        tOrder.setOrderChannel(1);
        tOrder.setStatus(0);
        tOrder.setCreateDate(new Date());
        tOrderMapper.insert(tOrder);

        //生成秒杀订单
        TSeckillOrder tSeckillOrder = new TSeckillOrder();
        tSeckillOrder.setUserId(user.getId());
        tSeckillOrder.setGoodsId(goods.getId());
        tSeckillOrder.setOrderId(tOrder.getId());
        tSeckillOrderMapper.insert(tSeckillOrder);

        redisTemplate.opsForValue().set("order" + user.getId() +":" + goods.getId(),tSeckillOrder);

        return tOrder;
    }

    @Override
    public OrderDetailVo detail(Long orderId) {
        if(null == orderId){
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        TOrder tOrder = baseMapper.selectById(orderId);
        GoodsVo goodsVoByGoodsId = tGoodsService.findGoodsVoByGoodsId(tOrder.getGoodsId());
        OrderDetailVo deatilVo = new OrderDetailVo();
        deatilVo.setTOrder(tOrder);
        deatilVo.setGoodsVo(goodsVoByGoodsId);
        return deatilVo;


    }
}

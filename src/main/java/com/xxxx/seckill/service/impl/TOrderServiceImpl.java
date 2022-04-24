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
import com.xxxx.seckill.service.TSeckillOrderService;
import com.xxxx.seckill.utils.MD5Util;
import com.xxxx.seckill.utils.UUIDUtil;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.OrderDetailVo;
import com.xxxx.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

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
    private TSeckillOrderService tSeckillOrderService;
    @Autowired
    private TGoodsService tGoodsService;
    @Autowired
    private RedisTemplate redisTemplate;




    @Override
    @Transactional
    public TOrder secKill(TUser user, GoodsVo goodsVo) {
        ValueOperations valueOperations = redisTemplate.opsForValue();

        TSeckillGoods seckillGoods = tSeckillGoodsService.getOne(new QueryWrapper<TSeckillGoods>().eq("goods_id", goodsVo.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        boolean seckillGoodsResult = tSeckillGoodsService.update(new UpdateWrapper<TSeckillGoods>()
                .setSql("stock_count = " + "stock_count-1")
                .eq("goods_id", goodsVo.getId())
                .gt("stock_count", 0)
        );
        if (seckillGoods.getStockCount() < 1) {
            //判断是否还有库存
            valueOperations.set("isStockEmpty:" + goodsVo.getId(), "0");
            return null;
        }

        //生成订单
        TOrder order = new TOrder();
        order.setUserId(user.getId());
        order.setGoodsId(goodsVo.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        tOrderMapper.insert(order);
        //生成秒杀订单
        TSeckillOrder tSeckillOrder = new TSeckillOrder();
        tSeckillOrder.setUserId(user.getId());
        tSeckillOrder.setOrderId(order.getId());
        tSeckillOrder.setGoodsId(goodsVo.getId());
        tSeckillOrderService.save(tSeckillOrder);
        redisTemplate.opsForValue().set("order:" + user.getId() + ":" + goodsVo.getId(), tSeckillOrder);
        return order;
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

    @Override
    public String createPath(TUser user, Long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisTemplate.opsForValue().set("seckillPath" + user.getId() + ":" + goodsId, str, 60, TimeUnit.SECONDS);
        return str;

    }

    @Override
    public Boolean checkPath(TUser user, Long goodsId, String path) {
        if(user == null || goodsId < 0 || StringUtils.isEmpty(path)){
            return false;
        }
        String s = (String) redisTemplate.opsForValue().get("seckillPath" + user.getId() + ":" + goodsId);
        return path.equals(s);
    }

    @Override
    public boolean checkCaptcha(TUser user, Long goodsId, String captcha) {
        if (user == null || goodsId < 0 || StringUtils.isEmpty(captcha)) {
            return false;
        }
        String redisCaptcha = (String) redisTemplate.opsForValue().get("captcha:" + user.getId() + ":" + goodsId);
        return captcha.equals(redisCaptcha);
    }

}

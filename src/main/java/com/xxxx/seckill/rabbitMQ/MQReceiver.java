package com.xxxx.seckill.rabbitMQ;

import com.xxxx.seckill.pojo.SeckillMessage;
import com.xxxx.seckill.pojo.TSeckillOrder;
import com.xxxx.seckill.pojo.TUser;
import com.xxxx.seckill.service.TGoodsService;
import com.xxxx.seckill.service.TOrderService;
import com.xxxx.seckill.utils.JsonUtil;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {
    @Autowired
    private TGoodsService tGoodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TOrderService orderService;

    @RabbitListener(queues = "seckillQueue")
    public void receive(String msg){
        log.info("接受消息"  + msg);
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(msg, SeckillMessage.class);
        Long goodId = seckillMessage.getGoodId();
        TUser user = seckillMessage.getUser();
        GoodsVo goodsVoByGoodsId = tGoodsService.findGoodsVoByGoodsId(goodId);
        if(goodsVoByGoodsId.getStockCount() < 1){
            return ;
        }
        //再次判断是否重复抢购
        ValueOperations valueOperations = redisTemplate.opsForValue();
        TSeckillOrder seckillOrder = (TSeckillOrder) valueOperations.get("order:" + user.getId() + ":" + goodId);
        if (seckillOrder != null) {
            return ;
        }
        //下单
        orderService.secKill(user,goodsVoByGoodsId);


    }



//    @RabbitListener(queues = "queue")
//    public void receive(Object msg){
//        log.info("接受消息"  + msg);
//    }
//    @RabbitListener(queues = "queue_fanout01")
//    public void receive01(Object msg){
//        log.info("QUEUE01接收消息：" + msg);
//    }
//    @RabbitListener(queues = "queue_fanout02")
//    public void receive02(Object msg){
//        log.info("QUEUE02接收消息：" + msg);
//    }
//
//
//    @RabbitListener(queues = "queue_direct01")
//    public void receive03(Object msg){
//        log.info("QUEUE01接收消息：" + msg);
//    }
//    @RabbitListener(queues = "queue_direct02")
//    public void receive04(Object msg){
//        log.info("QUEUE02接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = "queue_topic01")
//    public void receive05(Object msg){
//        log.info("QUEUE01接收消息：" + msg);
//    }
//    @RabbitListener(queues = "queue_topic02")
//    public void receive06(Object msg){
//        log.info("QUEUE02接收消息：" + msg);
//    }
}

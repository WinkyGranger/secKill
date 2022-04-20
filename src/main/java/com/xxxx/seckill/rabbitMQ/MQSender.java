package com.xxxx.seckill.rabbitMQ;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送秒杀信息
     * @param msg
     */
        public void sendSeckillMessage(String msg){
        log.info("发送消息" + msg);
        rabbitTemplate.convertAndSend("seckillExchange","seckill.message",msg);
    }










//    public void send(Object msg){
//        log.info("发送消息" + msg);
//        rabbitTemplate.convertAndSend("fanoutExchange","",msg);
//    }
//
//    public void send01(Object msg){
//        log.info("发送red消息" + msg);
//        rabbitTemplate.convertAndSend("directExchange","queue.red",msg);
//    }
//
//    public void send02(Object msg){
//        log.info("发送green消息" + msg);
//        rabbitTemplate.convertAndSend("directExchange","queue.green",msg);
//    }
//
//    public void send03(Object msg){
//        log.info("发送(QUEUE1)消息" + msg);
//        rabbitTemplate.convertAndSend("topicExchange","queue.green.message",msg);
//    }
//
//    public void send04(Object msg){
//        log.info("发送(QUEUE1和QUEUE2)消息" + msg);
//        rabbitTemplate.convertAndSend("topicExchange","message.queue.green",msg);
//    }
}

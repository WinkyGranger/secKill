package com.xxxx.seckill.controller;

import com.xxxx.seckill.rabbitMQ.MQSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/rabbitmq")
@Slf4j
public class RabbitMQTestController {

    @Autowired
    private MQSender mqSender;

    @RequestMapping("/mq")
    @ResponseBody
    public void mq(){
        mqSender.send("Controller发消息了");
    }

    @RequestMapping("/mq/fanout")
    @ResponseBody
    public void mq01(){
        mqSender.send("Controller发消息了 fanout");
    }

    @RequestMapping("/mq/direct01")
    @ResponseBody
    public void mqDirect01(){
        mqSender.send01("hello red");
    }

    @RequestMapping("/mq/direct02")
    @ResponseBody
    public void mqDirect02(){
        mqSender.send02("hello green");
    }

    @RequestMapping("/mq/topic01")
    @ResponseBody
    public void mqTopic01(){
        mqSender.send03("hello red");
    }

    @RequestMapping("/mq/topic02")
    @ResponseBody
    public void mqTopic02(){
        mqSender.send04("hello green");
    }

}

package com.xxxx.seckill;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisLockTest {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisScript redisScript;

    @Test
    public void contextLoads(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //如果key不存在才可以设置成功
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1");
        //如果占位成功才可以进行正常操作
        if(isLock){
            valueOperations.set("name","xxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name = " + name);
            //操作结束，删除锁
            redisTemplate.delete("k1");
        }else{
            System.out.println("有线程正在使用，请稍后");
        }
    }

    @Test
    public void contextLoads1(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //防止运行过程中有异常导致锁无法释放，设置锁的过期时长。
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1",5, TimeUnit.SECONDS);
        //如果占位成功才可以进行正常操作
        if(isLock){
            valueOperations.set("name","xxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name = " + name);
            //让线程抛异常
            Integer.parseInt("XXX");
            //操作结束，删除锁
            redisTemplate.delete("k1");
        }else{
            System.out.println("有线程正在使用，请稍后");
        }
    }

    @Test
    public void contextLoads3(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String s = UUID.randomUUID().toString();
        Boolean isLock = valueOperations.setIfAbsent("k1", s,120, TimeUnit.SECONDS);
        //如果占位成功才可以进行正常操作
        if(isLock){
            valueOperations.set("name","xxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name = " + name);
            System.out.println(valueOperations.get("k1"));
            Boolean k1 = (Boolean) redisTemplate.execute(redisScript, Collections.singletonList("k1"), name);
            System.out.println(k1);
            //操作结束，删除锁
            redisTemplate.delete("k1");
        }else{
            System.out.println("有线程正在使用，请稍后");
        }
    }
}

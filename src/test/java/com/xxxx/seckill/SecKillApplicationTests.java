package com.xxxx.seckill;

import com.xxxx.seckill.utils.MD5Util;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SecKillApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(MD5Util.inputPassToDBPass("123456", "1a2b3c4d"));
    }

}

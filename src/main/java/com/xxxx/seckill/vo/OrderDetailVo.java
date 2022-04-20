package com.xxxx.seckill.vo;

import com.xxxx.seckill.pojo.TOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单详情返回对象
 *
 * @author: LC
 * @date 2022/3/6 10:20 下午
 * @ClassName: OrderDeatilVo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailVo {

    private TOrder tOrder;

    private GoodsVo goodsVo;
}

package com.xxxx.seckill.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Winky
 * @since 2022-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TSeckillOrder对象", description="")
public class TSeckillOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "秒杀订单ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;


}

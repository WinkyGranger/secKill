package com.xxxx.seckill.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsVo {
    private Long id;
    private String goodsDetail;
    private BigDecimal seckillPrice;
    private Integer stockCount;
    private String goodsName;
    private BigDecimal goodsPrice;
    private Integer goodsStock;
    private String goodsTitle;
    private String goodsImg;
    private Date startDate;
    private Date endDate;
}

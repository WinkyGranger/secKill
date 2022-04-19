package com.xxxx.seckill.service;

import com.xxxx.seckill.pojo.TGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Winky
 * @since 2022-04-17
 */
public interface TGoodsService extends IService<TGoods> {

    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}


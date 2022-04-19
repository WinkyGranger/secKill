package com.xxxx.seckill.mapper;

import com.xxxx.seckill.pojo.TGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Winky
 * @since 2022-04-17
 */
public interface TGoodsMapper extends BaseMapper<TGoods> {

    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}

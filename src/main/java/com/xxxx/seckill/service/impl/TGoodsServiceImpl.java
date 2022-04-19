package com.xxxx.seckill.service.impl;

import com.xxxx.seckill.pojo.TGoods;
import com.xxxx.seckill.mapper.TGoodsMapper;
import com.xxxx.seckill.service.TGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.vo.GoodsVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Winky
 * @since 2022-04-17
 */
@Service
public class TGoodsServiceImpl extends ServiceImpl<TGoodsMapper, TGoods> implements TGoodsService {

    @Override
    public List<GoodsVo> findGoodsVo() {
        return baseMapper.findGoodsVo();

    }

    @Override
    public GoodsVo findGoodsVoByGoodsId(Long goodsId) {
        return baseMapper.findGoodsVoByGoodsId(goodsId);

    }
}

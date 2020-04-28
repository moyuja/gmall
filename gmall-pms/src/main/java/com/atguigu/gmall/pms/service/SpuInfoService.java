package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.vo.SpuInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.pms.entity.SpuInfoEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * spu信息
 *
 * @author moyu
 * @email 1017565766@qq.com
 * @date 2020-04-05 00:12:40
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageVo queryPage(QueryCondition params);

    PageVo querySpuInfo(QueryCondition queryCondition, Long catId);

    void Bigsave(SpuInfoVo spuInfoVo);

    void saveSpuInfo(SpuInfoVo spuInfoVo);



    void saveBaseAttrs(SpuInfoVo spuInfoVo);

    void saveSkuInfoWithSaleInfo(SpuInfoVo spuInfoVo);
}


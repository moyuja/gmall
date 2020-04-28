package com.atguigu.gmall.pms.vo;


import com.atguigu.gmall.pms.entity.SpuInfoEntity;
import lombok.Data;

import java.util.List;

/**
 * @Author: KKQ
 * @CreateTime: 2020/4/13 11:16
 * @我是中国人，我爱自己的祖国
 * @Description:
 **/
@Data
public class SpuInfoVo extends SpuInfoEntity {

    private List<String> spuImages;

    private List<BaseAttrValueVo> baseAttrs;

    private List<SkuInfoVo> skus;

}

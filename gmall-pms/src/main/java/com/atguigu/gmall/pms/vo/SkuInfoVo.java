package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.SkuInfoEntity;
import com.atguigu.gmall.pms.entity.SkuSaleAttrValueEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: KKQ
 * @CreateTime: 2020/4/13 11:35
 * @我是中国人，我爱自己的祖国
 * @Description:
 **/
@Data
public class SkuInfoVo extends SkuInfoEntity {

    //pms_product_attr_value 销售属性值表
    List<SkuSaleAttrValueEntity> saleAttrs;

    private List<String> images;

    //营销信息相关的三张 表
//    营销相关的字段
    private BigDecimal growBounds;
    private BigDecimal buyBounds;
    private List<Integer> work;
//    打折相关的字段
    private Integer fullCount;
    private BigDecimal discount;
    private Integer fullAddOther;
//    满减相关的字段
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Integer ladderAddOther;

}

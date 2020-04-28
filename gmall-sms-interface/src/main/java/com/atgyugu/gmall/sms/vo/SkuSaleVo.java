package com.atgyugu.gmall.sms.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: KKQ
 * @CreateTime: 2020/4/13 16:45
 * @我是中国人，我爱自己的祖国
 * @Description:
 **/
@Data
public class SkuSaleVo {

    private Long skuId;
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

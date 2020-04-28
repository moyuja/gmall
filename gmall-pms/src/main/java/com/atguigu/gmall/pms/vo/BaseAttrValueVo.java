package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import com.atguigu.gmall.pms.entity.SkuSaleAttrValueEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: KKQ
 * @CreateTime: 2020/4/13 11:23
 * @我是中国人，我爱自己的祖国
 * @Description:
 **/
public class BaseAttrValueVo extends ProductAttrValueEntity {

    public void setValueSelected(List<String> valueSelected){
        if (CollectionUtils.isEmpty(valueSelected)){
            return;
        }
        this.setAttrValue(StringUtils.join(valueSelected, ","));
    }

}

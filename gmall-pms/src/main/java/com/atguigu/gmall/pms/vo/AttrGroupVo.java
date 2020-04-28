package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import lombok.Data;

import java.util.List;

/**
 * @Author: KKQ
 * @CreateTime: 2020/4/6 20:01
 * @我是中国人，我爱自己的祖国
 * @Description:
 **/
@Data
public class AttrGroupVo extends AttrGroupEntity {

    private List<AttrEntity> attrEntities;
    private List<AttrAttrgroupRelationEntity> relations;

}

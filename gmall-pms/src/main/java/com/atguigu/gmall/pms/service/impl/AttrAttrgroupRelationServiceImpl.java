package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.pms.dao.AttrAttrgroupRelationDao;
import com.atguigu.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gmall.pms.service.AttrAttrgroupRelationService;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public void deleteAttr(List<AttrAttrgroupRelationEntity> relationEntities) {

        /*QueryWrapper<AttrAttrgroupRelationEntity> wrapper = new QueryWrapper<>();
        for (AttrAttrgroupRelationEntity relationEntity : relationEntities) {
        wrapper.eq("attr_group_id",relationEntity.getAttrGroupId()).eq("attr_id",relationEntity.getAttrId());
        this.remove(wrapper);
        }*/
//        java8做法
        relationEntities.forEach(relationEntity ->
            this.remove(new QueryWrapper<AttrAttrgroupRelationEntity>().
                    eq("attr_group_id",relationEntity.getAttrGroupId()).
                    eq("attr_id",relationEntity.getAttrId())
            ));



    }

}
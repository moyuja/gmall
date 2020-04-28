package com.atguigu.gmall.pms.service.impl;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.gmall.pms.dao.AttrAttrgroupRelationDao;
import com.atguigu.gmall.pms.dao.AttrDao;
import com.atguigu.gmall.pms.dao.AttrGroupDao;
import com.atguigu.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import com.atguigu.gmall.pms.service.AttrGroupService;
import com.atguigu.gmall.pms.vo.AttrGroupVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    AttrAttrgroupRelationDao relationDao;

    @Autowired
    AttrDao attrDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo queryByCid(QueryCondition queryCondition, Long cid) {
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();

        if (cid != null) {
            wrapper.eq("catelog_id", cid);
        }

        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(queryCondition),
                wrapper
        );

        return new PageVo(page);

    }

    @Override
    public AttrGroupVo queryWithattrGid(Long gid) {
        AttrGroupVo attrGroupVo = new AttrGroupVo();
        AttrGroupEntity groupEntity = this.getById(gid);
        BeanUtils.copyProperties(groupEntity, attrGroupVo);
//        获取关系表
        List<AttrAttrgroupRelationEntity> relations = this.relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", gid));
        if (CollectionUtils.isEmpty(relations)) {
            return attrGroupVo;
        }
        attrGroupVo.setRelations(relations);

        List<Long> attrIds = relations.stream().map(relationEntitiey-> relationEntitiey.getAttrId()).collect(Collectors.toList());

        List<AttrEntity> attrEntities = this.attrDao.selectBatchIds(attrIds);
        attrGroupVo.setAttrEntities(attrEntities);

        return attrGroupVo;
    }

    @Override
    public List<AttrGroupVo> withattrsByCatId(Long catId) {
        List<AttrGroupEntity> groupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catId));
        // 查询出每组下的规格参数
        return groupEntities.stream().map(t-> this.queryWithattrGid(t.getAttrGroupId())).collect(Collectors.toList());

    }

}
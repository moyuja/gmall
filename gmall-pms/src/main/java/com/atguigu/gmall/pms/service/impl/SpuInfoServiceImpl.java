package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.dao.*;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.feign.GmallSmsClient;
import com.atguigu.gmall.pms.service.*;
import com.atguigu.gmall.pms.vo.BaseAttrValueVo;
import com.atguigu.gmall.pms.vo.SkuInfoVo;
import com.atguigu.gmall.pms.vo.SpuInfoVo;
import com.atgyugu.gmall.sms.vo.SkuSaleVo;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    ProductAttrValueService productAttrValueService;
    @Autowired
    SpuInfoDescService spuInfoDescService;
    @Autowired
    AttrDao attrDao;

    @Autowired
    SkuInfoDao skuInfoDao;
    @Autowired
    SkuImagesService skuImagesService;
    @Autowired
    SkuSaleAttrValueService skuAttrService;

    @Autowired
    GmallSmsClient gmallSmsClient;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo querySpuInfo(QueryCondition queryCondition, Long catId) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
//        需要判断是否全站
        if (catId != 0) {
            wrapper.eq("catalog_id",catId);
        }

//        需要判断是否输入key
        String key = queryCondition.getKey();
        if (StringUtils.isNotBlank(key)) {
            wrapper.and(t-> t.eq("id",key).or().like("spu_name",key));
        }


        IPage<SpuInfoEntity> page = this.page(
                new Query().getPage(queryCondition),
                wrapper
        );
        return new PageVo(page);
    }

    @GlobalTransactional
    @Transactional
    public void Bigsave(SpuInfoVo spuInfoVo) {

        this.saveSpuInfo(spuInfoVo);

        this.spuInfoDescService.saveSpuDesc(spuInfoVo);
//        运行时异常
//
//        编译异常
//        new File(new)

        this.saveBaseAttrs(spuInfoVo);

        this.saveSkuInfoWithSaleInfo(spuInfoVo);
//        int i = 1/0;
    }

    /**
     * 保存sku相关信息及营销信息
     * @param spuInfoVo
     */
    public void saveSkuInfoWithSaleInfo(SpuInfoVo spuInfoVo) {
        Long spuId = spuInfoVo.getId();
        List<SkuInfoVo> skus = spuInfoVo.getSkus();
        skus.forEach(skuVo -> {
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            BeanUtils.copyProperties(skuVo, skuInfoEntity);

            // 品牌和分类的id需要从spuInfo中获取
            skuInfoEntity.setSpuId(spuId);
            skuInfoEntity.setSkuCode(UUID.randomUUID().toString());
            skuInfoEntity.setCatalogId(spuInfoVo.getCatalogId());
            skuInfoEntity.setBrandId(spuInfoVo.getBrandId());
            List<String> images = skuVo.getImages();
//            设置默认图片
            if (!CollectionUtils.isEmpty(images)) {
                //放置图片
                skuInfoEntity.setSkuDefaultImg(StringUtils.isEmpty(skuInfoEntity.getSkuDefaultImg()) ? images.get(0) : skuInfoEntity.getSkuDefaultImg());
            }
//            保存信息
            this.skuInfoDao.insert(skuInfoEntity);
            Long skuId = skuInfoEntity.getSkuId();
//            2.2 skuimage表
            if (!CollectionUtils.isEmpty(images)) {
                List<SkuImagesEntity> imagesEntities = images.stream().map(image -> {
                    SkuImagesEntity skuImage = new SkuImagesEntity();
                    skuImage.setImgUrl(image);
                    skuImage.setSkuId(skuId);
                    skuImage.setImgSort(0);
                    skuImage.setDefaultImg(skuInfoEntity.getSkuDefaultImg().equals(image) ? 0 : 1);
                    return skuImage;
                }).collect(Collectors.toList());
                //            保存图片
                skuImagesService.saveBatch(imagesEntities);
            }
//            2.3 sku销售属性表，保存sku的规格参数（销售属性）
            List<SkuSaleAttrValueEntity> saleAttrs = skuVo.getSaleAttrs();
            saleAttrs.forEach(saleAttr -> {
                // 设置属性名，需要根据id查询AttrEntity
                saleAttr.setSkuId(skuId);
                saleAttr.setAttrSort(0);
                saleAttr.setAttrName(attrDao.selectById(saleAttr.getAttrId()).getAttrName());
            });
            this.skuAttrService.saveBatch(saleAttrs);
//        3.保存营销信息相关
            SkuSaleVo skuSaleVo = new SkuSaleVo();

            BeanUtils.copyProperties(skuVo, skuSaleVo);
            skuSaleVo.setSkuId(skuId);

            gmallSmsClient.skuSaleSave(skuSaleVo);
        });
    }


    /**
     * 保存spu属性（图片）
     * @param spuInfoVo
     */
    public void saveBaseAttrs(SpuInfoVo spuInfoVo) {
        Long spuId = spuInfoVo.getId();
        List<BaseAttrValueVo> baseAttrs = spuInfoVo.getBaseAttrs();
        if (!CollectionUtils.isEmpty(baseAttrs)) {
//            对象流转换
            List<ProductAttrValueEntity> productAttrValueEntities = baseAttrs.stream().map(baseAttrValueVo -> {
                        baseAttrValueVo.setSpuId(spuId);
                        baseAttrValueVo.setQuickShow(0);
                        baseAttrValueVo.setAttrSort(0);
                        return (ProductAttrValueEntity) baseAttrValueVo;
                    }
            ).collect(Collectors.toList());
//        批量添加
        this.productAttrValueService.saveBatch(productAttrValueEntities);
        }
    }

    /**
     * 保存spu基本信息
     * @param spuInfoVo
     */
    @Transactional
    public void saveSpuInfo(SpuInfoVo spuInfoVo) {
        spuInfoVo.setPublishStatus(1);//默认上架
        spuInfoVo.setCreateTime(new Date());
        spuInfoVo.setUodateTime(spuInfoVo.getCreateTime());
        this.save(spuInfoVo);
    }

}
package com.atguigu.gmall.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.sms.entity.HomeAdvEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;


/**
 * 首页轮播广告
 *
 * @author moyu
 * @email 1017565766@qq.com
 * @date 2020-04-05 00:20:01
 */
public interface HomeAdvService extends IService<HomeAdvEntity> {

    PageVo queryPage(QueryCondition params);
}


package com.atguigu.gmall.sms.dao;

import com.atguigu.gmall.sms.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author moyu
 * @email 1017565766@qq.com
 * @date 2020-04-05 00:20:01
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}

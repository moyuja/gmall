package com.atgyugu.gmall.sms.api;

import com.atguigu.core.bean.Resp;
import com.atgyugu.gmall.sms.vo.SkuSaleVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: KKQ
 * @CreateTime: 2020/4/13 17:08
 * @我是中国人，我爱自己的祖国
 * @Description:
 **/

public interface GmallSmsApi {

    @PostMapping("sms/skubounds/skuSale/save")
    public Resp<Object> skuSaleSave(@RequestBody SkuSaleVo skuSaleVo);

}

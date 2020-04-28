package com.atguigu.gmall.pms.feign;

import com.atgyugu.gmall.sms.api.GmallSmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: KKQ
 * @CreateTime: 2020/4/13 17:47
 * @我是中国人，我爱自己的祖国
 * @Description:
 **/
@FeignClient("sms-service")
public interface GmallSmsClient extends GmallSmsApi {

}

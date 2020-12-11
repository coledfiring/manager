package com.whaty.framework.api.grant.strategy.checksign;

import com.whaty.util.GenerateSortMd5SignUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 排序并使用md5处理的签名校验抽象策略
 *
 * @author suoqiangqiang
 */
@Lazy
@Component("sortMd5CheckSignStrategy")
public class SortMd5CheckSignStrategy extends AbstractCheckSignStrategy {

    @Override
    public boolean checkSign(Map<String, String> paramsMap, String sign) {
        return GenerateSortMd5SignUtil.generate(paramsMap).equals(sign);
    }

}

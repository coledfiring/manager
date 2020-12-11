package com.whaty.products.service.siteconfig.impl;

import com.whaty.core.framework.bean.EnumConst;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * enumConst管理
 *
 * @author weipengsen
 */
@Lazy
@Service("enumConstManageService")
public class EnumConstManageServiceImpl extends TycjGridServiceAdapter<EnumConst> {
}

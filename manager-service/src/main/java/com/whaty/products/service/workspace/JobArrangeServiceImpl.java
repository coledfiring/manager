package com.whaty.products.service.workspace;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.*;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.utils.UserUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * 工作安排服务类
 *
 * @author shangyu
 */
@Lazy
@Service("jobArrangeService")
public class JobArrangeServiceImpl extends TycjGridServiceAdapter<JobArrange> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    @Override
    public void checkBeforeAdd(JobArrange bean, Map<String, Object> params) throws EntityException {
        super.checkBeforeAdd(bean, params);
        bean.setCreateBy(UserUtils.getCurrentUser());
        bean.setPeUnit(UserUtils.getCurrentUnit());
        bean.setCreateDate(new Date());
    }

    /**
     * 获取工作安排内容
     * @param extendId
     * @return
     */
    public Map<String, Object> getJobArrangeContent(String extendId) {
        String content = this.myGeneralDao
                .getOneBySQL("select content from job_arrange_extend where id = ?", extendId);
        return Collections.singletonMap("jobArrangeContent", content);
    }

    /**
     * 保存工作安排内容
     * @param id
     * @param content
     */
    public void saveJobArrangeContent(String id, String content) {
        TycjParameterAssert.isAllNotBlank(id, content);
        String extendId = this.myGeneralDao
                .getOneBySQL("select fk_arrange_extend_id from job_arrange where id = ?", id);
        JobArrangeExtend extend = new JobArrangeExtend();
        extend.setContent(content);
        if (StringUtils.isNotBlank(extendId)) {
            extend.setId(extendId);
        }
        this.myGeneralDao.save(extend);
        this.myGeneralDao.flush();
        if (StringUtils.isBlank(extendId)) {
            this.myGeneralDao.executeBySQL("update job_arrange set fk_arrange_extend_id = ? where id = ?",
                    extend.getId(), id);
        }
    }
}

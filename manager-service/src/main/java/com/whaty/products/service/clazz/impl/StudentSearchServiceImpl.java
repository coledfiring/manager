package com.whaty.products.service.clazz.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeStudent;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.PlatformConfigUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.generator.WordImageGenerator;
import com.whaty.framework.im.TencentImManageSDK;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.whaty.constant.CommonConstant.PROFILE_PICTURE_PATH;

/**
 * 学员信息查询
 *
 * @author weipengsen
 */
@Lazy
@Service("studentSearchService")
public class StudentSearchServiceImpl extends TycjGridServiceAdapter<PeStudent> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    private final WordImageGenerator generator = new WordImageGenerator(120, 120);

    /**
     * 生成头像
     * @param ids
     */
    public void doGenerateProfilePicture(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        List<Map<String, Object>> userInfo = this.myGeneralDao
                .getMapBySQL("select ss.id as userId, stu.true_name as name, ss.login_id as loginId " +
                        "from pe_student stu " +
                        "inner join sso_user ss on ss.id = stu.fk_sso_user_id " +
                        "where " + CommonUtils.madeSqlIn(ids, "stu.id"));
        if (CollectionUtils.isEmpty(userInfo)) {
            return;
        }
        userInfo.forEach(user -> {
            String profilePicture = String.format(PROFILE_PICTURE_PATH, SiteUtil.getSiteCode(), "student",
                    user.get("loginId"));
            try {
                String name = (String) user.get("name");
                this.generator.generateProfilePicture(name.substring(name.length() - 2),
                        CommonUtils.mkDir(CommonUtils.getRealPath(profilePicture)));
            } catch (IOException e) {
                throw new UncheckException(e);
            }
            this.myGeneralDao.executeBySQL("update sso_user set profile_picture = ? where id = ?",
                    profilePicture, user.get("userId"));
        });
    }

    /**
     * 同步学生到im
     * @param ids
     * @return
     * @throws Exception
     */
    public int syncUserToIm(String ids) throws Exception {
        List<String> userIds = this.myGeneralDao.getBySQL("select fk_sso_user_id from pe_student where " +
                CommonUtils.madeSqlIn(ids, "id"));
        if (PlatformConfigUtil.getPlatformConfig().getOpenTencentIm()) {
            new TencentImManageSDK().insertUser(userIds);
        }
        return userIds.size();
    }

    /**
     * 解绑微信用户
     *
     * @param ids
     * @return
     */
    public int doUnbindWeChatUser(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        StringBuilder sql = new StringBuilder();
        sql.append(" delete wu                                                             ");
        sql.append(" from wechat_user wu                                                   ");
        sql.append(" inner join pe_student man on man.fk_sso_user_id=wu.fk_sso_user_id     ");
        sql.append(" where                                                                 ");
        sql.append(CommonUtils.madeSqlIn(ids, "man.id"));
        return this.myGeneralDao.executeBySQL(sql.toString());
    }

    /**
     * 解除小程序绑定
     * @param ids
     * @return
     */
    public int doUnbindWeChatAppUser(String ids) {
        TycjParameterAssert.isAllNotBlank(ids);
        String currentCode = MasterSlaveRoutingDataSource.getDbType();
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        int count = this.myGeneralDao.executeBySQL("delete from wechat_app_user where "
                + CommonUtils.madeSqlIn(ids, "fk_sso_user_id"));
        MasterSlaveRoutingDataSource.setDbType(currentCode);
        return count;
    }
}

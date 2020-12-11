package com.whaty.products.service.training.impl;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeCourse;
import com.whaty.domain.bean.SsoUser;
import com.whaty.domain.bean.TrainingItem;
import com.whaty.domain.bean.TrainingItemCourse;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import com.whaty.utils.UserUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 培训项目课程管理
 *
 * @author weipengsen
 */
@Lazy
@Service("trainingItemCourseManageService")
public class TrainingItemCourseManageServiceImpl extends AbstractTwoLevelListGridServiceImpl<TrainingItemCourse> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    @Override
    public void checkBeforeAdd(TrainingItemCourse bean, Map<String, Object> params) throws EntityException {
        super.checkBeforeAdd(bean, params);
        if (this.myGeneralDao.checkNotEmpty("select 1 from training_item_course where fk_course_id = ? " +
                        "and fk_training_item_id = ?", bean.getPeCourse().getId(),
                bean.getTrainingItem().getId())) {
            throw new EntityException("此课程已存在");
        }
        bean.setCreateBy(this.myGeneralDao.getById(SsoUser.class, this.userService.getCurrentUser().getId()));
        bean.setCreateDate(new Date());
    }

    /**
     * 获取未添加课程
     *
     * @param name
     * @param target
     * @param teacher
     * @param parentId
     * @param page
     * @return
     */
    public Map<String, Object> getNotAddedCourseList(String name, String target, String teacher, String parentId,
                                                     Map<String, Object> page) {
        String siteCode = MasterSlaveRoutingDataSource.getDbType();
        StringBuilder sql = new StringBuilder();
        sql.append(" select                        ");
        sql.append("   count(1)                    ");
        sql.append(" from                          ");
        sql.append("   pe_course cou               ");
        sql.append(" INNER JOIN pe_teacher tea on tea.id=cou.fk_teacher_id         ");
        sql.append(" INNER JOIN enum_const tt on tt.id=cou.flag_training_target    ");
        sql.append(" INNER JOIN enum_const ct on ct.id=cou.flag_course_type        ");
        sql.append(" WHERE                                                         ");
        sql.append("   cou.id not in (                                             ");
        sql.append("     SELECT                                                    ");
        sql.append("       fk_course_id                                            ");
        sql.append("     FROM                                                      ");
        sql.append("       training_item_course                                    ");
        sql.append("     where fk_training_item_id = ?                             ");
        sql.append("   )                                                           ");
        sql.append(" and cou.site_code = ?                                         ");
        if (StringUtils.isNotBlank(name)) {
            sql.append(" and cou.name like '%" + name + "%'");
        }
        if (StringUtils.isNotBlank(target)) {
            sql.append(" and tt.id = '" + target + "'");
        }
        if (StringUtils.isNotBlank(teacher)) {
            sql.append(" and tea.name like '%" + teacher + "%'");
        }
        int count = this.myGeneralDao.<BigInteger>getOneBySQL(sql.toString(), parentId, siteCode).intValue();
        Integer pageSize = (Integer) page.get("pageSize");
        page.put("totalNumber", count);
        Integer totalPage = count % pageSize == 0 ? count / pageSize : (count / pageSize + 1);
        page.put("totalPage", totalPage);
        Integer currentPage = (Integer) page.get("currentPage");
        currentPage = currentPage > totalPage ? totalPage : currentPage;
        currentPage = currentPage < 1 ? 1 : currentPage;
        page.put("currentPage", currentPage);
        sql.delete(0, sql.length());
        sql.append(" select                                                        ");
        sql.append("   COUNT(DISTINCT stu.fk_class_id) as classNum,                ");
        sql.append("   COUNT(DISTINCT stu.id) as stuNum,                           ");
        sql.append("   cou.id as courseId,                                         ");
        sql.append("   cou.name as courseName,                                     ");
        sql.append("   tea.name as teaName,                                        ");
        sql.append("   tea.true_name as teaTrueName,                               ");
        sql.append("   tt.name as targetType,                                      ");
        sql.append("   ct.name as courseType                                       ");
        sql.append(" from                                                          ");
        sql.append("   pe_course cou                                               ");
        sql.append(" INNER JOIN pe_teacher tea on tea.id=cou.fk_teacher_id         ");
        sql.append(" INNER JOIN enum_const tt on tt.id=cou.flag_training_target    ");
        sql.append(" INNER JOIN enum_const ct on ct.id=cou.flag_course_type        ");
        sql.append(" LEFT JOIN class_course cc on cc.fk_course_id=cou.id           ");
        sql.append(" LEFT JOIN pe_student stu on stu.fk_class_id=cc.fk_class_id    ");
        sql.append(" WHERE                                                         ");
        sql.append("   cou.id not in (                                             ");
        sql.append("     SELECT                                                    ");
        sql.append("       fk_course_id                                            ");
        sql.append("     FROM                                                      ");
        sql.append("       training_item_course                                    ");
        sql.append("     where fk_training_item_id = ?                             ");
        sql.append("   )                                                           ");
        sql.append(" and cou.site_code = ?                                         ");
        if (StringUtils.isNotBlank(name)) {
            sql.append(" and cou.name like '%" + name + "%'");
        }
        if (StringUtils.isNotBlank(target)) {
            sql.append(" and tt.id = '" + target + "'");
        }
        if (StringUtils.isNotBlank(teacher)) {
            sql.append(" and tea.name like '%" + teacher + "%'");
        }
        sql.append(" GROUP BY cou.id                                               ");
        sql.append(" LIMIT " + ((currentPage - 1) * pageSize) + ", " + pageSize);
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("page", page);
        resultMap.put("notAddedCourse", this.myGeneralDao.getMapBySQL(sql.toString(), parentId, siteCode));
        return resultMap;
    }

    /**
     * 获取已添加课程
     *
     * @param parentId
     * @return
     */
    public List<Map<String, Object>> getAddedCourseList(String parentId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select                                                                ");
        sql.append("   tic.id as id,                                                       ");
        sql.append("   cou.id as courseId,                                                 ");
        sql.append("   cou.name as courseName,                                             ");
        sql.append("   tea.name as teaName,                                                ");
        sql.append("   tt.name as targetType,                                              ");
        sql.append("   ct.name as courseType                                               ");
        sql.append(" from                                                                  ");
        sql.append("   pe_course cou                                                       ");
        sql.append(" INNER JOIN pe_teacher tea on tea.id=cou.fk_teacher_id                 ");
        sql.append(" INNER JOIN enum_const tt on tt.id=cou.flag_training_target            ");
        sql.append(" INNER JOIN enum_const ct on ct.id=cou.flag_course_type                ");
        sql.append(" INNER JOIN training_item_course tic on tic.fk_course_id = cou.id      ");
        sql.append(" WHERE                                                                 ");
        sql.append("   tic.fk_training_item_id = ?                                         ");
        return this.myGeneralDao.getMapBySQL(sql.toString(), parentId);
    }

    /**
     * 添加课程
     *
     * @param courseId
     * @param parentId
     */
    public void doAddCourse(String courseId, String parentId) {
        if (this.myGeneralDao.checkNotEmpty("select 1 from training_item_course " +
                "where fk_course_id = ? and fk_training_item_id = ?", courseId, parentId)) {
            throw new ServiceException("当前课程已添加，请勿重复添加");
        }
        TrainingItemCourse itemCourse = new TrainingItemCourse();
        itemCourse.setCreateBy(UserUtils.getCurrentUser());
        itemCourse.setCreateDate(new Date());
        itemCourse.setPeCourse(this.myGeneralDao.getById(PeCourse.class, courseId));
        itemCourse.setTrainingItem(this.myGeneralDao.getById(TrainingItem.class, parentId));
        this.myGeneralDao.save(itemCourse);
    }

    /**
     * 删除课程
     *
     * @param id
     */
    public void doRemoveCourse(String id) {
        this.myGeneralDao.deleteByIds(TrainingItemCourse.class, Arrays.asList(id));
    }

    @Override
    protected String getParentIdSearchParamName() {
        return "trainingItem.id";
    }

    @Override
    protected String getParentIdBeanPropertyName() {
        return "trainingItem.id";
    }
}

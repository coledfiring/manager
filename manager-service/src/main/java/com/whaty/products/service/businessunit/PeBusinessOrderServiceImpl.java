package com.whaty.products.service.businessunit;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.user.service.UserService;
import com.whaty.core.framework.util.BeanNames;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeBusinessOrder;
import com.whaty.domain.bean.PeBusinessUnit;
import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.products.service.businessunit.constants.BusinessUnitStudentEnrollConstant;
import com.whaty.products.service.businessunit.constants.EasyPayConstant;
import com.whaty.products.service.businessunit.constants.EasyPayUtils;
import com.whaty.products.service.businessunit.domain.EasyPayParameter;
import com.whaty.products.service.common.UtilService;
import com.whaty.util.CommonUtils;
import com.whaty.util.ValidateUtils;
import com.whaty.utils.UserUtils;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 单位订单管理service
 *
 * @author shanshuai
 */
@Lazy
@Service("peBusinessOrderService")
public class PeBusinessOrderServiceImpl extends TycjGridServiceAdapter<PeBusinessOrder> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = BeanNames.USER_SERVICE)
    private UserService userService;

    @Resource(name = CommonConstant.UTIL_SERVICE_BEAN_NAME)
    private UtilService utilService;

    @Override
    public void checkBeforeAdd(PeBusinessOrder bean) throws EntityException {
        // 默认订单状态，未支付
        bean.setEnumConstByFlagBusinessFeeStatus(myGeneralDao.
                getEnumConstByNamespaceCode("flagBusinessFeeStatus", "0"));
        bean.setOrderNumber(userService.getCurrentUser().getLoginId() + System.currentTimeMillis());
        bean.setPeBusinessUnit((PeBusinessUnit) myGeneralDao.
                getOneByHQL("FROM PeBusinessUnit WHERE siteCode = ? AND ssoUser.id = ?",
                        SiteUtil.getSiteCode(), userService.getCurrentUser().getId()));
        bean.setCreateUser(UserUtils.getCurrentUser());
        bean.setCreateTime(new Date());
        bean.setSiteCode(SiteUtil.getSiteCode());
    }

    @Override
    public void checkBeforeUpdate(PeBusinessOrder bean) throws EntityException {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                          ");
        sql.append("    	1                                                                           ");
        sql.append("    FROM                                                                            ");
        sql.append("    	pe_business_order ord                                                       ");
        sql.append("    INNER JOIN enum_const feeStatus ON feeStatus.id = ord.flag_business_fee_status  ");
        sql.append("    WHERE                                                                           ");
        sql.append("       ord.id = ?                                                                   ");
        sql.append("    AND feeStatus. CODE <> '0'                                                      ");
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL(sql.toString(), bean.getId()))) {
            throw new EntityException("只有未支付的订单可以修改缴费方式,请确认");
        }
        bean.setUpdateUser(UserUtils.getCurrentUser());
        bean.setUpdateTime(new Date());
    }

    @Override
    public void checkBeforeDelete(List idList) throws EntityException {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                          ");
        sql.append("    	1                                                                           ");
        sql.append("    FROM                                                                            ");
        sql.append("    	pe_business_order ord                                                       ");
        sql.append("    INNER JOIN enum_const feeStatus ON feeStatus.id = ord.flag_business_fee_status  ");
        sql.append("    WHERE                                                                           ");
        sql.append("       " + CommonUtils.madeSqlIn(idList, "ord.id"));
        sql.append("    AND feeStatus. CODE <> '0'                                                      ");
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL(sql.toString()))) {
            throw new EntityException("只能删除删除未支付的订单,请确认");
        }
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL("SELECT 1 FROM pe_student WHERE site_code = ? AND " +
                CommonUtils.madeSqlIn(idList, "fk_business_order_id"), SiteUtil.getSiteCode()))) {
            throw new EntityException("该订单中已经有缴费的学生，不能删除");
        }
    }

    /**
     * 单位订单支付
     *
     * @param ids
     * @return
     */
    @LogAndNotice("单位订单支付")
    public int doPayOrder(String ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                 ");
        sql.append("    	payway.code payWayCode,                                            ");
        sql.append("    	ord.money_order_path moneyOrderPath                                ");
        sql.append("    FROM                                                                   ");
        sql.append("    	pe_business_order ord                                              ");
        sql.append("    INNER JOIN enum_const payway ON payway.ID = ord.flag_business_pay_way  ");
        sql.append("    WHERE                                                                  ");
        sql.append("        ord.id = ?                                                         ");
        sql.append("    AND payway. CODE in ('1', '2')                                         ");
        Map<String, Object> payWayMap = myGeneralDao.getOneMapBySQL(sql.toString(), ids);
        if (MapUtils.isEmpty(payWayMap)) {
            throw new ServiceException("此功能只支持缴费方式为汇款支付、现场支付的订单缴费");
        }
        // 如果为汇款支付，就必须有汇款单
        if ("1".equals(payWayMap.get("payWayCode")) && payWayMap.get("moneyOrderPath") == null) {
            throw new ServiceException("支付缴费方式为汇款方式的订单，请先上传汇款单");
        }
        sql.delete(0, sql.length());
        sql.append("    SELECT                                                                         ");
        sql.append("    	1                                                                          ");
        sql.append("    FROM                                                                           ");
        sql.append("    	pe_business_order ord                                                      ");
        sql.append("    INNER JOIN enum_const feeStatus ON feeStatus.ID = ord.flag_business_fee_status ");
        sql.append("    WHERE                                                                          ");
        sql.append("        ord.id = ?                                                                 ");
        sql.append("    AND feeStatus. CODE = '4'                                                      ");
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL(sql.toString(), ids))) {
            throw new ServiceException("订单已经支付，请勿重新缴费");
        }
        sql.delete(0, sql.length());
        sql.append("    SELECT                                                                         ");
        sql.append("    	1                                                                          ");
        sql.append("    FROM                                                                           ");
        sql.append("    	pe_business_order ord                                                      ");
        sql.append("    INNER JOIN enum_const feeStatus ON feeStatus.ID = ord.flag_business_fee_status ");
        sql.append("    WHERE                                                                          ");
        sql.append("        ord.id = ?                                                                 ");
        sql.append("    AND feeStatus. CODE IN ('0', '3')                                              ");
        if (CollectionUtils.isEmpty(myGeneralDao.getBySQL(sql.toString(), ids))) {
            throw new ServiceException("订单支付状态为未支付或汇款单审核不通过时，才可以进行缴费操作");
        }
        sql.delete(0, sql.length());
        sql.append("    SELECT                                                               ");
        sql.append("    	invoice.invoiceCount                                             ");
        sql.append("    FROM                                                                 ");
        sql.append("    	pe_business_order ord                                            ");
        sql.append("    LEFT JOIN pe_student ps ON ps.fk_business_order_id = ord.id          ");
        sql.append("    LEFT JOIN student_enroll_fee fee ON fee.fk_student_id = ps.id        ");
        sql.append("    LEFT JOIN (                                                          ");
        sql.append("    	SELECT                                                           ");
        sql.append("    		invoice.fk_business_order_id ordId,                          ");
        sql.append("    		ifnull(sum(invoice.invoice_num), 0) invoiceNum,              ");
        sql.append("    		ifnull(                                                      ");
        sql.append("    			sum(                                                     ");
        sql.append("    				invoice.invoice_num * invoice.invoice_single_amount  ");
        sql.append("    			),                                                       ");
        sql.append("    			0                                                        ");
        sql.append("    		) invoiceCount                                               ");
        sql.append("    	FROM                                                             ");
        sql.append("    		pe_business_order_invoice invoice                            ");
        sql.append("    	WHERE                                                            ");
        sql.append("    		invoice.site_code = ?                                        ");
        sql.append("    	and invoice.fk_business_order_id = ?                             ");
        sql.append("    	GROUP BY                                                         ");
        sql.append("    		invoice.fk_business_order_id                                 ");
        sql.append("    ) invoice ON invoice.ordId = ord.id                                  ");
        sql.append("    WHERE                                                                ");
        sql.append("        ord.id = ?                                                       ");
        sql.append("    GROUP BY                                                             ");
        sql.append("    	ord.id                                                           ");
        sql.append("    HAVING                                                               ");
        sql.append("    	SUM(IFNULL(fee.total_fee, 0)) = invoice.invoiceCount             ");
        if (CollectionUtils.isEmpty(myGeneralDao.getBySQL(sql.toString(), SiteUtil.getSiteCode(), ids, ids))) {
            throw new ServiceException("发票总金额必须和缴费总金额一致，请先完善发票信息");
        }

        // 支付方式和对应支付状态的code一致，payWayMap.get("payWayCode")
        sql.delete(0, sql.length());
        sql.append("    UPDATE pe_business_order ord                                                     ");
        sql.append("    INNER JOIN enum_const feeStatus ON feeStatus.NAMESPACE = 'flagbusinessfeestatus' ");
        sql.append("    AND feeStatus. CODE = '" + payWayMap.get("payWayCode") + "'                      ");
        sql.append("    SET ord.flag_business_fee_status = feeStatus.id                                  ");
        sql.append("    WHERE                                                                            ");
        sql.append("    " + CommonUtils.madeSqlIn(ids, "ord.id"));
        return myGeneralDao.executeBySQL(sql.toString());
    }

    /**
     * 在线支付(订单状态)
     *
     * @param ids
     * @return
     */
    @LogAndNotice("在线支付")
    public int doPayOnlineOrder(String ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                 ");
        sql.append("    	1                                                                  ");
        sql.append("    FROM                                                                   ");
        sql.append("    	pe_business_order ord                                              ");
        sql.append("    INNER JOIN enum_const payway ON payway.ID = ord.flag_business_pay_way  ");
        sql.append("    WHERE                                                                  ");
        sql.append("        ord.id = ?                                                         ");
        sql.append("    AND payway. CODE = '0'                                                 ");
        if (CollectionUtils.isEmpty(myGeneralDao.getBySQL(sql.toString(), ids))) {
            throw new ServiceException("此功能只支持状态为线上支付的订单缴费");
        }
        throw new ServiceException("暂未对接在线支付功能");
    }

    /**
     * 取消缴费
     *
     * @param ids
     * @return
     */
    @LogAndNotice("取消缴费")
    public int doCancelPayOrder(String ids) {
        return this.doOperateOrder(ids, "0",
                "只有缴费状态为现场待确认或者汇款单待审核的订单可以取消缴费");
    }

    /**
     * 确认已支付
     *
     * @param ids
     * @return
     */
    @LogAndNotice("确认已支付")
    public int doConfirmPaymentOrder(String ids) {
        int count = this.doOperateOrder(ids, "4", "只有缴费状态为现场待确认或者汇款单待审核的订单可以确认已支付");
        this.generateRegNo(ids);
        return count;
    }

    /**
     * 生成学号
     *
     * @param orderIds
     * @return
     */
    private int generateRegNo(String orderIds) {
        String namespace = UUID.randomUUID().toString().replaceAll("-", "");
        // 生成学号
        StringBuilder sql = new StringBuilder();
        sql.append("    INSERT INTO util_excel_table (                                                            ");
        sql.append("    	ID,                                                                                   ");
        sql.append("    	ROW,                                                                                  ");
        sql.append("    	NAMESPACE,                                                                            ");
        sql.append("    	COLUMN1,                                                                              ");
        sql.append("    	COLUMN2,                                                                              ");
        sql.append("    	COLUMN3                                                                               ");
        sql.append("    ) SELECT                                                                                  ");
        sql.append("    	UUID(),                                                                               ");
        sql.append("    	@row_index/*'*/:=/*'*/@row_index + 1,                                                 ");
        sql.append("    	'" + namespace + "',                                                                  ");
        sql.append("    	stu.id,                                                                               ");
        sql.append("    	concat(                                                                               ");
        sql.append("    		class. CODE,                                                                      ");
        sql.append("    		lpad(                                                                             ");
        sql.append("    			ifnull(classMaxRegNo.maxRegNo, 0) +                                           ");
        sql.append("    			IF (                                                                          ");
        sql.append("    				@last_prefix = class. CODE,                                               ");
        sql.append("    				@row_num/*'*/:=/*'*/@row_num + 1,                                         ");
        sql.append("    				@row_num/*'*/:=/*'*/ 1                                                    ");
        sql.append("    			),                                                                            ");
        sql.append("    			3,                                                                            ");
        sql.append("    			'0'                                                                           ");
        sql.append("    		)                                                                                 ");
        sql.append("    	),                                                                                    ");
        sql.append("    	@last_prefix /*'*/:=/*'*/ class. CODE                                                 ");
        sql.append("    FROM                                                                                      ");
        sql.append("    	pe_student stu                                                                        ");
        sql.append("    INNER JOIN pe_business_order ord ON ord.id = stu.fk_business_order_id                     ");
        sql.append("    INNER JOIN pe_class class ON class.id = stu.fk_class_id                                   ");
        sql.append("    INNER JOIN (                                                                              ");
        sql.append("    	SELECT                                                                                ");
        sql.append("    		@row_num/*'*/:=/*'*/ 0 ,@last_prefix/*'*/:=/*'*/ NULL                             ");
        sql.append("    ) a                                                                                       ");
        sql.append("    INNER JOIN (SELECT @row_index/*'*/:=/*'*/ 0) b                                            ");
        sql.append("    LEFT JOIN (                                                                               ");
        sql.append("    	SELECT                                                                                ");
        sql.append("    		ps.fk_class_id classId,                                                           ");
        sql.append("    		max(                                                                              ");
        sql.append("    			IFNULL(RIGHT(ps.reg_no, 3), 0)                                                ");
        sql.append("    		) maxRegNo                                                                        ");
        sql.append("    	FROM                                                                                  ");
        sql.append("    		pe_student ps                                                                     ");
        sql.append("    	WHERE                                                                                 ");
        sql.append("    		ps.site_code = ?                                                                  ");
        sql.append("    	AND ps.reg_no IS NOT NULL                                                             ");
        sql.append("    	GROUP BY                                                                              ");
        sql.append("    		ps.fk_class_id                                                                    ");
        sql.append("    ) classMaxRegNo ON classMaxRegNo.classId = stu.fk_class_id                                ");
        sql.append("    WHERE                                                                                     ");
        sql.append("    	stu.site_code = ?                                                                     ");
        sql.append("    AND stu.reg_no IS NULL                                                                    ");
        sql.append("    AND     " + CommonUtils.madeSqlIn(orderIds, "ord.id"));
        myGeneralDao.executeBySQL(sql.toString(), SiteUtil.getSiteCode(), SiteUtil.getSiteCode());
        sql.delete(0, sql.length());
        sql.append("    UPDATE util_excel_table uet                                                   ");
        sql.append("    INNER JOIN pe_student ps ON ps.id = uet.COLUMN1                               ");
        sql.append("    INNER JOIN enum_const feeStatus ON feeStatus.NAMESPACE = 'FlagFeeStatus'      ");
        sql.append("    AND feeStatus. CODE = '1'                                                     ");
        sql.append("    SET ps.reg_no = uet.COLUMN2,                                                  ");
        sql.append("        ps.flag_fee_status = feeStatus.ID,                                        ");
        sql.append("        fee_operate_time = NOW(),                                                 ");
        sql.append("        fee_operate_user = ?                                                      ");
        sql.append("    WHERE                                                                         ");
        sql.append("    	uet.NAMESPACE = ?                                                         ");
        return myGeneralDao.executeBySQL(sql.toString(), userService.getCurrentUser() == null ?
                null : userService.getCurrentUser().getId(), namespace);
    }

    /**
     * 订单审核不通过
     *
     * @param ids
     * @param auditOpinion
     * @return
     */
    @LogAndNotice("订单审核不通过")
    public int doNoPassPaymentOrder(String ids, String auditOpinion) {
        TycjParameterAssert.isAllNotBlank(ids, auditOpinion);
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                       ");
        sql.append("    	1                                                                        ");
        sql.append("    FROM                                                                         ");
        sql.append("    	pe_business_order ord                                                    ");
        sql.append("    INNER JOIN enum_const feeWay ON feeWay.ID = ord.flag_business_pay_way        ");
        sql.append("    WHERE                                                                        ");
        sql.append("    	feeWay. CODE <> '1'                                                      ");
        sql.append("    AND  " + CommonUtils.madeSqlIn(ids, "ord.id"));
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL(sql.toString()))) {
            throw new ServiceException("只能操作订单缴费方式为汇款支付的订单");
        }

        sql.delete(0, sql.length());
        sql.append("    SELECT                                                                         ");
        sql.append("    	1                                                                          ");
        sql.append("    FROM                                                                           ");
        sql.append("    	pe_business_order ord                                                      ");
        sql.append("    INNER JOIN enum_const feeStatus ON feeStatus.ID = ord.flag_business_fee_status ");
        sql.append("    WHERE                                                                          ");
        sql.append("        " + CommonUtils.madeSqlIn(ids, "ord.id"));
        sql.append("    AND feeStatus. CODE <> '1'                                                   ");
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL(sql.toString()))) {
            throw new ServiceException("只能操作缴费状态为汇款待审批的订单");
        }

        sql.delete(0, sql.length());
        sql.append("    UPDATE pe_business_order ord                                                     ");
        sql.append("    INNER JOIN enum_const feeStatus ON feeStatus.NAMESPACE = 'flagbusinessfeestatus' ");
        sql.append("    AND feeStatus. CODE = '3'                                                        ");
        sql.append("    SET                                                                              ");
        sql.append("        ord.flag_business_fee_status = feeStatus.id,                                 ");
        sql.append("        ord.audit_opinion = ?                                                        ");
        sql.append("    WHERE                                                                            ");
        sql.append("        " + CommonUtils.madeSqlIn(ids, "ord.id"));
        return myGeneralDao.executeBySQL(sql.toString(), auditOpinion);
    }

    /**
     * 操作订单支付状态
     *
     * @param ids
     * @param feeStatusCode
     * @param errorMsg
     * @return
     */
    private int doOperateOrder(String ids, String feeStatusCode, String errorMsg) {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                         ");
        sql.append("    	1                                                                          ");
        sql.append("    FROM                                                                           ");
        sql.append("    	pe_business_order ord                                                      ");
        sql.append("    INNER JOIN enum_const feeStatus ON feeStatus.ID = ord.flag_business_fee_status ");
        sql.append("    WHERE                                                                          ");
        sql.append("        " + CommonUtils.madeSqlIn(ids, "ord.id"));
        sql.append("    AND feeStatus. CODE not IN ('1', '2')                                          ");
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL(sql.toString()))) {
            throw new ServiceException(errorMsg);
        }
        sql.delete(0, sql.length());
        sql.append("    UPDATE pe_business_order ord                                                     ");
        sql.append("    INNER JOIN enum_const feeStatus ON feeStatus.NAMESPACE = 'flagbusinessfeestatus' ");
        sql.append("    AND feeStatus. CODE = ?                                                          ");
        sql.append("    SET ord.flag_business_fee_status = feeStatus.id                                  ");
        sql.append("    WHERE                                                                            ");
        sql.append("        " + CommonUtils.madeSqlIn(ids, "ord.id"));
        return myGeneralDao.executeBySQL(sql.toString(), feeStatusCode);
    }

    /**
     * 上传汇款单文件
     *
     * @param file
     * @param originFileName
     * @param orderId
     * @return
     * @throws Exception
     */
    @LogAndNotice("上传汇款单文件")
    public String doUploadMoneyOrderFile(File file, String originFileName, String orderId) throws Exception {
        String originFilePath = myGeneralDao.
                getOneBySQL("SELECT money_order_path moneyOrderPath FROM pe_business_order where id = ?", orderId);
        String fileNameSuffix = originFileName.substring(originFileName.lastIndexOf("."));
        String url = String.join("/", BusinessUnitStudentEnrollConstant.MONEY_ORDER_PATH,
                SiteUtil.getSiteCode(), CommonUtils.changeDateToString(new Date(), CommonConstant.DEFAULT_DATE_FORMAT),
                System.currentTimeMillis() + fileNameSuffix);
        this.myGeneralDao.executeBySQL("UPDATE pe_business_order set money_order_path = ? where id = ?", url, orderId);
        FileUtils.copyFile(file, new File(CommonUtils.mkDir(CommonUtils.getRealPath(url))));

        // 删除源文件
        if (StringUtils.isNotBlank(originFilePath)) {
            new File(originFilePath).delete();
        }
        return url;
    }

    /**
     * 下载导入学生缴费信息模板
     *
     * @param outputStream
     */
    public void downloadStudentFeeDetailTemplate(OutputStream outputStream) {
        this.utilService.generateExcelAndWrite(outputStream, null,
                BusinessUnitStudentEnrollConstant.UPLOAD_EXCEL_STUDENT_FEE_DETAIL);
    }

    /**
     * 批量导入学生缴费信息
     *
     * @param file
     * @return
     */
    public Map<String, Object> doUploadStudentFeeDetailInfo(File file) {
        List<String[]> checkList = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("    NOT EXISTS (                                                        ");
        sql.append("    	SELECT                                                          ");
        sql.append("    		1                                                           ");
        sql.append("    	FROM                                                            ");
        sql.append("    		pe_class class                                              ");
        sql.append("    	WHERE                                                           ");
        sql.append("    		class.site_code = '" + SiteUtil.getSiteCode() + "'          ");
        sql.append("    	AND class.name = e.COLUMN1                                      ");
        sql.append("    )                                                                   ");
        checkList.add(new String[]{"不存在此期次信息;", sql.toString()});

        sql.delete(0, sql.length());
        sql.append("    NOT EXISTS (                                                        ");
        sql.append("    	SELECT                                                          ");
        sql.append("    		1                                                           ");
        sql.append("    	FROM                                                            ");
        sql.append("    		pe_student ps                                               ");
        sql.append("    	WHERE                                                           ");
        sql.append("    		ps.site_code = '" + SiteUtil.getSiteCode() + "'             ");
        sql.append("    	AND ps.card_no = e.COLUMN2                                      ");
        sql.append("    )                                                                   ");
        checkList.add(new String[]{"系统无此学生记录;", sql.toString()});

        checkList.add(new String[]{"身份证号格式错误;", "NOT (e.COLUMN2 REGEXP '" + ValidateUtils.PERSON_CARD_NO_REG_STR + "')"});

        sql.delete(0, sql.length());
        sql.append("    NOT EXISTS (                                                               ");
        sql.append("    	SELECT                                                                 ");
        sql.append("    		1                                                                  ");
        sql.append("    	FROM                                                                   ");
        sql.append("    		pe_student ps                                                      ");
        sql.append("    	INNER JOIN pe_class class ON class.id = ps.fk_class_id                 ");
        sql.append("    	WHERE                                                                  ");
        sql.append("    		ps.site_code = '" + SiteUtil.getSiteCode() + "'                    ");
        sql.append("    	AND ps.card_no = e.column2                                             ");
        sql.append("    	AND class. NAME = e.column1                                            ");
        sql.append("    )                                                                          ");
        checkList.add(new String[]{"学生未报名该期次;", sql.toString()});

        sql.delete(0, sql.length());
        sql.append("    EXISTS (                                                                  ");
        sql.append("    	SELECT                                                                ");
        sql.append("    		1                                                                 ");
        sql.append("    	FROM                                                                  ");
        sql.append("    		pe_student ps                                                     ");
        sql.append("    	WHERE                                                                 ");
        sql.append("    		ps.site_code = '" + SiteUtil.getSiteCode() + "'                   ");
        sql.append("    	AND ps.card_no = e.column2                                            ");
        sql.append("    	AND ps.fk_business_order_id IS NOT NULL                               ");
        sql.append("    )                                                                         ");
        checkList.add(new String[]{"学生已经存在订单信息;", sql.toString()});

        sql.delete(0, sql.length());
        sql.append("    EXISTS (                                                                   ");
        sql.append("    	SELECT                                                                 ");
        sql.append("    		1                                                                  ");
        sql.append("    	FROM                                                                   ");
        sql.append("    		pe_student ps                                                      ");
        sql.append("    	INNER JOIN enum_const ec ON ec.id = ps.flag_fee_status                 ");
        sql.append("    	WHERE                                                                  ");
        sql.append("    		ps.site_code = '" + SiteUtil.getSiteCode() + "'                    ");
        sql.append("    	AND ps.card_no = e.column2                                             ");
        sql.append("    	AND ec. CODE = '1'                                                     ");
        sql.append("    )                                                                          ");
        checkList.add(new String[]{"学生已经缴费;", sql.toString()});

        sql.delete(0, sql.length());
        sql.append("    EXISTS (                                                                   ");
        sql.append("    	SELECT                                                                 ");
        sql.append("    		1                                                                  ");
        sql.append("    	FROM                                                                   ");
        sql.append("    		pe_business_order ord                                              ");
        sql.append("    	INNER JOIN enum_const ec ON ec.id = ord.flag_business_fee_status       ");
        sql.append("    	WHERE                                                                  ");
        sql.append("    		ord.site_code = '" + SiteUtil.getSiteCode() + "'                   ");
        sql.append("    	AND ord.order_number = e.column4                                       ");
        sql.append("    	AND ec. CODE IN ('1', '2', '4')                                        ");
        sql.append("    )                                                                          ");
        checkList.add(new String[]{"学生已经缴费;", sql.toString()});

        sql.delete(0, sql.length());
        sql.append("    UPDATE util_excel_table uet                                                ");
        sql.append("    INNER JOIN pe_business_order ord ON ord.order_number = uet.column4         ");
        sql.append("    INNER JOIN pe_student ps ON ps.card_no = uet.column2                       ");
        sql.append("    SET ps.fk_business_order_id = ord.id                                       ");
        sql.append("    WHERE                                                                      ");
        sql.append("    	uet.NAMESPACE = ${namespace}                                           ");
        sql.append("    AND ord.site_code = '" + SiteUtil.getSiteCode() + "'                       ");
        return this.utilService.doUploadFile(file, BusinessUnitStudentEnrollConstant.UPLOAD_EXCEL_STUDENT_FEE_DETAIL,
                checkList, Arrays.asList(sql.toString()), 0, "导入学生缴费信息");
    }

    /**
     * 确认已支付的订单才可以打印听课证
     *
     * @param ids
     */
    public void checkIsCanPrintCertificate(String ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                  ");
        sql.append("    	1                                                                   ");
        sql.append("    FROM                                                                    ");
        sql.append("    	pe_business_order ord                                               ");
        sql.append("    INNER JOIN enum_const ec ON ec.id = ord.flag_business_fee_status        ");
        sql.append("    WHERE                                                                   ");
        sql.append("    	" + CommonUtils.madeSqlIn(ids, "ord.id"));
        sql.append("    AND ec. CODE <> '4'                                                     ");
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL(sql.toString()))) {
            throw new ServiceException("只有支付状态为已支付的订单才可以打印听课证");
        }
    }

    /**
     * 获取易支付参数信息
     *
     * @param businessOrderId
     * @return
     * @throws Exception
     */
    public EasyPayParameter getEasyPayParametersInfo(String businessOrderId) throws Exception {
        this.validateOnlinePay(businessOrderId);
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                          ");
        sql.append("    	ord.pay_order_no payOrderNo,                                                ");
        sql.append("    	SUM(IFNULL(fee.total_fee, 0)) feeCount                                      ");
        sql.append("    FROM                                                                            ");
        sql.append("    	pe_business_order ord                                                       ");
        sql.append("    LEFT JOIN pe_student ps ON ps.fk_business_order_id = ord.id                     ");
        sql.append("    LEFT JOIN student_enroll_fee fee ON fee.fk_student_id = ps.id                   ");
        sql.append("    WHERE                                                                           ");
        sql.append("    	ord.id = ?                                                                  ");
        Map<String, Object> orderInfoMap = myGeneralDao.getOneMapBySQL(sql.toString(), businessOrderId);
        BigDecimal feeCount = (BigDecimal) orderInfoMap.get("feeCount");
        if (feeCount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("选择的订单金额为0，不需要交费 ");
        }
        String orderDate = CommonUtils.changeDateToString(new Date(), "yyyyMMdd");
        EasyPayParameter easyPayParameter = new EasyPayParameter(feeCount, orderDate);
        String orderNo;
        if (orderInfoMap.get("payOrderNo") == null) {
            orderNo = easyPayParameter.generateOrderNo(orderDate, businessOrderId);
            myGeneralDao.executeBySQL("UPDATE pe_business_order set pay_order_no = ?, " +
                            "pay_order_no_create_time = now(), " +
                            "pay_order_no_create_user_id = ? WHERE id = ?",
                    orderNo, userService.getCurrentUser().getId(), businessOrderId);
        } else {
            orderNo = (String) orderInfoMap.get("payOrderNo");
        }
        // 易智付md5加密原字符串
        String key = easyPayParameter.getMoneytype() + orderDate + feeCount.setScale(2, BigDecimal.ROUND_DOWN).toString() +
                orderNo + orderNo + easyPayParameter.getMid() + easyPayParameter.getCallBackUrl();
        easyPayParameter.setMd5Info(EasyPayUtils.hmac_Md5(key));
        easyPayParameter.setOrderNo(orderNo);
        easyPayParameter.setAmount(feeCount);
        easyPayParameter.setOrderDate(orderDate);
        return easyPayParameter;
    }

    /**
     * 在线易智付支付校验方法
     *
     * @param orderId
     */
    private void validateOnlinePay(String orderId) {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                         ");
        sql.append("    	1                                                                          ");
        sql.append("    FROM                                                                           ");
        sql.append("    	pe_business_order ord                                                      ");
        sql.append("    INNER JOIN enum_const ec ON ec.id = %s                                         ");
        sql.append("    WHERE                                                                          ");
        sql.append("    	ec. CODE <> '0'                                                            ");
        sql.append("    AND ord.id = ?                                                                 ");
        if (CollectionUtils.isNotEmpty(myGeneralDao.
                getBySQL(String.format(sql.toString(), "ord.flag_business_pay_way"), orderId))) {
            throw new ServiceException("只能选择支付方式为线上支付的订单");
        }
        if (CollectionUtils.isNotEmpty(myGeneralDao.
                getBySQL(String.format(sql.toString(), "ord.flag_business_fee_status"), orderId))) {
            throw new ServiceException("只能选择支付状态为未支付的订单");
        }
    }

    /**
     * 易智付支付后同步回调方法
     *
     * @param requestMap
     * @throws UnsupportedEncodingException
     */
    public void doSyncReceiveEasyPayOrderInfo(Map<String, String> requestMap) throws UnsupportedEncodingException {
        String orderNo = requestMap.get("v_oid");
        int payStatus = Integer.parseInt(requestMap.get("v_pstatus"));
        // v_pstatus支付状态：1 已提交	20 支付成功 30 支付失败
        StringBuilder sql = new StringBuilder();
        sql.append("    UPDATE pe_business_order ord                                                                 ");
        sql.append("    INNER JOIN enum_const ec ON ec.namespace = 'flagbusinessfeestatus'                           ");
        sql.append("    AND ec. CODE = '4'                                                                           ");
        sql.append("    SET ord.flag_business_fee_status =                                                           ");
        sql.append("    IF (                                                                                         ");
        sql.append("    	" + payStatus + " = " + EasyPayConstant.EASY_PAY_STATUS_SYNC_IS_OK + ",                       ");
        sql.append("    	ec.id,                                                                                    ");
        sql.append("    	ord.flag_business_fee_status                                                              ");
        sql.append("    ),                                                                                           ");
        sql.append("     ord.pay_mode = '" + requestMap.get("v_pmode") + "',                                           ");
        sql.append("     ord.pay_date = NOW(),                                                                       ");
        sql.append("     ord.pay_real_amount = '" + requestMap.get("v_amount") + "'                                    ");
        sql.append("    WHERE                                                                                        ");
        sql.append("    	ord.pay_order_no = ?                                                                     ");
        myGeneralDao.executeBySQL(sql.toString(), orderNo);
        if (payStatus == EasyPayConstant.EASY_PAY_STATUS_SYNC_IS_OK) {
            this.generateRegNo(orderNo.substring(orderNo.lastIndexOf("-") + 1));
        }
        requestMap.put("v_pmode", requestMap.get("v_pmode"));
        requestMap.put("v_pstring", requestMap.get("v_pstring"));
        this.saveEasyPayData(requestMap, "easyPay-bjtupx", "easyPay-sync");
    }

    /**
     * 保存第三方易智付订单数据
     *
     * @param params
     * @param orderType
     * @param payType
     */
    private void saveEasyPayData(Map params, String orderType, String payType) {
        String responseData = JSONObject.fromObject(params).toString();
        String sql = "insert into third_pay_data(order_no, response_data, pay_type, order_type, return_code, site_code)"
                + "  values('" + params.get("v_oid") + "', '" + responseData + "', '" + payType + "', '"
                + orderType + "', '" + params.get("payStatus") + "', '" + SiteUtil.getSiteCode() + "')";
        this.myGeneralDao.executeBySQL(sql);
    }

    /**
     * 易智付支付后异步回调方法
     *
     * @param requestMap
     */
    public void doAsyncReceiveEasyPayOrderInfo(Map<String, String> requestMap) throws UnsupportedEncodingException {
        int payStatus = Integer.parseInt(requestMap.get("v_pstatus"));
        String payType = "easyPay-async";
        String orderNo = requestMap.get("v_oid");
        // 只操作状态为未支付的的订单
        // v_pstatus支付状态：0 待处理 1 支付完成 2 未支付 3 支付被拒绝 4 商户退款中 5 退款已成功 6 退款被拒绝 7 持卡人拒付 8 异常退款 9 持卡人取消订单
        if (orderNo.split("-").length == 4) {
            this.updateBusinessOrder(payStatus, requestMap.get("v_pmode"), requestMap, orderNo);
            if (payStatus == EasyPayConstant.EASY_PAY_STATUS_ASYNC_IS_OK) {
                this.generateRegNo(orderNo.substring(orderNo.lastIndexOf("-") + 1));
            }
        } else {
            payType = "easyPay-async-person";
            this.updatePersonOrder(payStatus, orderNo, requestMap.get("v_pmode"), requestMap);
        }
        requestMap.put("v_pmode", requestMap.get("v_pmode"));
        requestMap.put("v_pstring", requestMap.get("v_pstring"));
        this.saveEasyPayData(requestMap, "easyPay-bjtupx", payType);
    }

    /**
     * 集体报名异步回调处理
     *
     * @param payStatus
     * @param payMode
     * @param requestMap
     * @param orderNo
     */
    public void updateBusinessOrder(int payStatus, String payMode, Map<String, String> requestMap, String orderNo) {
        StringBuilder sql = new StringBuilder();
        sql.append("    UPDATE pe_business_order ord                                                          ");
        sql.append("    INNER JOIN enum_const feeStatus ON feeStatus.id = ord.flag_business_fee_status        ");
        sql.append("    INNER JOIN enum_const ec ON ec.namespace = 'flagbusinessfeestatus'                    ");
        sql.append("    AND ec. CODE = '4'                                                                    ");
        sql.append("    SET ord.flag_business_fee_status =                                                    ");
        sql.append("    IF (                                                                                  ");
        sql.append("    	" + payStatus + " = " + EasyPayConstant.EASY_PAY_STATUS_ASYNC_IS_OK + ",          ");
        sql.append("    	ec.id,                                                                            ");
        sql.append("    	ord.flag_business_fee_status                                                      ");
        sql.append("    ),                                                                                    ");
        sql.append("     ord.pay_mode = '" + payMode + "',                                                    ");
        sql.append("     ord.pay_date = NOW(),                                                                ");
        sql.append("     ord.pay_real_amount = '" + requestMap.get("v_amount") + "'                           ");
        sql.append("    WHERE                                                                                 ");
        sql.append("    	ord.pay_order_no = ?                                                              ");
        sql.append("    AND feeStatus. CODE = '0'                                                             ");
        myGeneralDao.executeBySQL(sql.toString(), orderNo);
    }

    /**
     * 单个报名异步回调处理
     * @param payStatus
     * @param orderNo
     * @param payMode
     * @param requestMap
     */
    public void updatePersonOrder(int payStatus, String orderNo, String payMode, Map<String, String> requestMap) {
        String checkId;
        String feeStatus;
        String isPay;
        int remainderNum = 0;
        if (payStatus == EasyPayConstant.EASY_PAY_STATUS_SYNC_IS_OK) {
            feeStatus = this.myGeneralDao.getEnumConstByNamespaceCode("FlagFeeStatus", "1").getId();
            isPay = this.myGeneralDao.getEnumConstByNamespaceCode("FlagIsPay", "1").getId();
            checkId = this.myGeneralDao.getEnumConstByNamespaceCode("flagCheckStatus", "2").getId();
            remainderNum = 3;
        } else {
            feeStatus = this.myGeneralDao.getEnumConstByNamespaceCode("FlagFeeStatus", "0").getId();
            isPay = this.myGeneralDao.getEnumConstByNamespaceCode("FlagIsPay", "0").getId();
            checkId = this.myGeneralDao.getEnumConstByNamespaceCode("flagCheckStatus", "3").getId();
        }
        //生成学号
        String classId = this.myGeneralDao.getBySQL("select ps.fk_class_id from pe_student ps inner join " +
                "pe_order po on po.fk_student_id = ps.id  where po.order_number = '" + orderNo + "'").get(0).toString();
        Map<String, Object> map = this.myGeneralDao.getMapBySQL("SELECT pc.code code,MAX(RIGHT(ps.reg_no, 3)) num " +
                "FROM pe_student ps INNER JOIN pe_class pc ON ps.fk_class_id = pc.id " +
                "WHERE pc.id = '" + classId + "'").get(0);
        String code = (String) map.get("code");
        Object num = map.get("num");
        String regNo = num == null ? "001" : ((Integer.parseInt((String) num) + 1) + "");
        StringBuilder sql = new StringBuilder();
        sql.append(" update pe_order po                                           ");
        sql.append(" inner join pe_student ps on po.fk_student_id = ps.id         ");
        sql.append(" inner join enum_const ec on ec.namespace = 'FlagPayWay'      ");
        sql.append(" and ec. CODE = '5'                                           ");
        sql.append(" inner join enum_const ec1 on ec1.namespace = 'FlagIsPay'     ");
        sql.append(" and ec1. CODE = '0'                                          ");
        sql.append(" set ps.flag_pay_way = ec.id,                                 ");
        sql.append("   po.pay_mode = '" + payMode + "',                             ");
        sql.append("   po.fee_amount = '" + requestMap.get("v_amount") + "',         ");
        sql.append("   po.pay_date = now(),                                       ");
        sql.append("   po.flag_is_pay = '" + isPay + "',                           ");
        sql.append("   po.flag_check_status = '" + checkId + "',                    ");
        sql.append("   po.remainder_num = '" + remainderNum + "',                   ");
        sql.append("   ps.flag_fee_status = '" + feeStatus + "',                     ");
        sql.append("   ps.reg_no = concat('" + code + "', lpad('" + regNo + "',3,0)) ");
        sql.append(" where po.order_number = ?                                    ");
        sql.append(" and ec1.id = po.flag_is_pay                                  ");
        this.myGeneralDao.executeBySQL(sql.toString(), orderNo);
    }


    /**
     * 更新企事业单位易智付订单号
     *
     * @param ids
     * @return
     */
    @LogAndNotice("更新企事业单位易智付订单号")
    public int updateEasyPayOrderNo(String ids) {
        this.validateOnlinePay(ids);
        if (CollectionUtils.isNotEmpty(myGeneralDao.
                getBySQL("SELECT 1 FROM pe_business_order WHERE pay_order_no IS NULL AND id = ?", ids))) {
            throw new ServiceException("只能更新已经生成易智付订单号的订单，请确认");
        }
        Date orderExpiresTime = myGeneralDao.getOneBySQL("SELECT date_add(pay_order_no_create_time, " +
                        "INTERVAL ? MINUTE) expiresTime FROM pe_business_order WHERE id = ?",
                EasyPayConstant.EASY_PAY_ORDER_NO_UPDATE_INTERVAL, ids);
        if (com.whaty.util.CommonUtils.compareDateDetail(new Date(), orderExpiresTime)) {
            throw new ServiceException("更新易智付订单号需要在" + orderExpiresTime + "之后才能更新");
        }
        String newOrderNo = new EasyPayParameter().
                generateOrderNo(CommonUtils.changeDateToString(new Date(), "yyyyMMdd"), ids);
        return myGeneralDao.executeBySQL("UPDATE pe_business_order set pay_order_no = ?, " +
                        "pay_order_no_create_time = now(), pay_order_no_create_user_id = ? WHERE id = ?",
                newOrderNo, userService.getCurrentUser().getId(), ids);
    }

    /**
     * 获取易支付订单信息
     *
     * @param businessOrderId
     * @return
     */
    public JSONObject getEasyPayOrderInfo(String businessOrderId) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("    SELECT                                                                         ");
        sql.append("    	1                                                                          ");
        sql.append("    FROM                                                                           ");
        sql.append("    	pe_business_order ord                                                      ");
        sql.append("    INNER JOIN enum_const ec ON ec.id = ord.flag_business_pay_way                  ");
        sql.append("    WHERE                                                                          ");
        sql.append("    	ec. CODE <> '0'                                                            ");
        sql.append("    AND ord.id = ?                                                                 ");
        if (CollectionUtils.isNotEmpty(myGeneralDao.getBySQL(sql.toString(), businessOrderId))) {
            throw new ServiceException("只能选择支付方式为线上支付的订单");
        }
        String orderNo = myGeneralDao.
                getOneBySQL("SELECT pay_order_no orderNo FROM pe_business_order WHERE id = ?", businessOrderId);
        JSONObject jsonObject = null;
        if (StringUtils.isNotBlank(orderNo)) {
            String xmlResponseData = EasyPayUtils.queryEasyPayOrderInfo(orderNo).replaceAll("null", "");
            jsonObject = JSONObject.fromObject(new XMLSerializer().read(xmlResponseData).toString());
        }
        return jsonObject;
    }
}

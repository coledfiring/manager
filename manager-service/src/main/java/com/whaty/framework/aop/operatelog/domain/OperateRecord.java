package com.whaty.framework.aop.operatelog.domain;

import com.opencsv.CSVWriter;
import com.whaty.common.string.StringUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.framework.aop.operatelog.constant.OperateRecordConstant;
import com.whaty.framework.aop.operatelog.util.OperateRecordConfigUtils;
import com.whaty.framework.common.spring.SpringUtil;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.DataOperateException;
import com.whaty.util.CommonUtils;
import com.whaty.utils.StaticBeanUtils;
import lombok.Data;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Transient;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * 操作日志
 *
 * @author weipengsen
 */
@Data
public class OperateRecord extends AbstractAsyAndMethodAspectRecord implements Serializable {

    private static final transient long serialVersionUID = 8343639380530077805L;

    private String id;

    private String operateName;

    private String moduleCode;

    private String moduleName;

    private Long operateTimeMillis;

    private String params;

    private String result;

    private String siteCode;

    private String url;

    private String requestIp;

    private String serverIp;

    private String userId;

    private String loginId;

    private String operateTimeStr;

    private transient EnumConst module;

    private transient Boolean isImport;

    private transient Date operateTime;

    private final transient GeneralDao generalDao;

    @Transient
    private final transient static Logger logger = LoggerFactory.getLogger(OperateRecord.class);

    public OperateRecord() {
        this.generalDao = (GeneralDao) SpringUtil.getBean(CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME);
    }

    @Override
    public void operateReturning(JoinPoint join, Object result, Long operateTimeMillis) {
        this.operateRecordAfter(join, result, operateTimeMillis);
    }

    @Override
    public void operateThrowing(JoinPoint join, Throwable t, Long operateTimeMillis) {
        this.operateRecordAfter(join, t, operateTimeMillis);
    }

    @Override
    public void record() {
        String currentDB = MasterSlaveRoutingDataSource.getDbType();
        try {
            MasterSlaveRoutingDataSource.setDbType(this.getSiteCode());
            this.handleModule();
            this.writeInfoToFile();
            if (this.getIsImport()) {
                this.saveInfoToDatabase();
            }
        } finally {
            Optional.ofNullable(currentDB).ifPresent(MasterSlaveRoutingDataSource::setDbType);
        }
    }

    /**
     * 处理模块数据
     */
    private void handleModule() {
        try {
            Optional.ofNullable(this.getModuleCode()).map(e -> this.generalDao
                    .getEnumConstByNamespaceCode(OperateRecordConstant.ENUM_CONST_NAME_SPACE_MODULE_CODE, e))
                    .ifPresent(this::setModule);
        } catch (Exception e) {
            logger.error("handle module data error", e);
        }
    }

    /**
     * 操作日志记录，操作后处理
     *
     * @param join
     * @param result
     * @param operateTimeMillis
     */
    private void operateRecordAfter(JoinPoint join, Object result, Long operateTimeMillis) {
        this.recordFunctional(join, operateRecord -> {
                    OperateRecordData data = new OperateRecordData(operateRecord, result);
                    if (StringUtils.isNotBlank(operateRecord.recordSql())) {
                        try {
                            data.getParams().put(OperateRecordConstant.ARG_NAME_CUSTOM_RECORD_PARAMS,
                                    this.generalDao.getMapBySQL(operateRecord.recordSql()));
                        } catch (DataOperateException e) {
                            logger.error(String.format("the operate [%s] is error when execute the custom sql",
                                    operateRecord.value()), e);
                        }
                    }
                    data.handleOperate(this.getParams(join), operateTimeMillis);
                },
                (methodName, basicOperateRecord) -> {
                    String operateName = basicOperateRecord.value() + OperateRecordConfigUtils
                            .getProperty(String.format(OperateRecordConstant.PROPERTY_KEY_BASIC_OPERATE_NAME,
                                    methodName));
                    new OperateRecordData(operateName, basicOperateRecord.isImportant(),
                            basicOperateRecord.moduleCode(), result)
                            .handleOperate(this.getParams(join), operateTimeMillis);
                }
        );
    }

    /**
     * 将信息写入文件
     */
    private void writeInfoToFile() {
        File recordFile = new File(CommonUtils.mkDir(CommonUtils.getRealPathNoRequest(this.generateFileName())));
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(recordFile, true))) {
            Field[] fields = this.getCanPersistFields();
            // 写入表头
            if (!recordFile.exists() || recordFile.length() == 0) {
                csvWriter.writeNext(Arrays.stream(fields).map(Field::getName).toArray(String[]::new));
            }
            // 写入操作信息
            csvWriter.writeNext(Arrays.stream(fields).peek(e -> e.setAccessible(true))
                    .map(this::extractFieldValue).map(e -> e == null ? "" : String.valueOf(e))
                    .toArray(String[]::new));
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("failure when write info to csv [%s]", this.getOperateName()), e);
            }
        }
    }

    /**
     * 生成文件名
     *
     * @return
     */
    private String generateFileName() {
        String month = CommonUtils.changeDateToString(this.getOperateTime(), "yyyy-MM");
        return OperateRecordConstant.CSV_FILE_PATH + month + "/"
                + String.format(OperateRecordConstant.CSV_FILE_NAME,
                this.getSiteCode(),
                CommonUtils.changeDateToString(this.getOperateTime()),
                this.getServerIp());
    }

    /**
     * 获取可以持久化的字段
     *
     * @return
     */
    private Field[] getCanPersistFields() {
        return Arrays.stream(this.getClass().getDeclaredFields())
                .filter(e -> !Modifier.isTransient(e.getModifiers()))
                .filter(e -> Objects.isNull(e.getAnnotation(Transient.class)))
                .sorted(Comparator.comparing(Field::getName)).toArray(Field[]::new);
    }

    /**
     * 提取字段的值
     *
     * @param field
     * @return
     */
    private Object extractFieldValue(Field field) {
        try {
            return field.get(this);
        } catch (IllegalAccessException e) {
            logger.error("extract field value error", e);
            return null;
        }
    }

    /**
     * 将信息写入数据库
     */
    private void saveInfoToDatabase() {
        this.generalDao.executeBySQL("INSERT INTO operate_record (id, flag_module_id, NAME, url, request_ip, " +
                        "fk_user_id, operate_time, operate_date, site_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                this.getId(), this.getModule() == null ? null : this.getModule().getId(),
                this.getOperateName(), this.getUrl(), this.getRequestIp(), this.getUserId(),
                this.getOperateTime(), CommonUtils.changeDateToString(this.getOperateTime()), this.getSiteCode());
    }

    /**
     * 将dto转化为do
     *
     * @param dto
     * @return
     */
    private static OperateRecord convertToDO(OperateRecordData dto) {
        OperateRecord operateRecordDO = new OperateRecord();
        operateRecordDO.setId(UUID.randomUUID().toString().replace("-", ""));
        operateRecordDO.setOperateName(dto.getOperateName());
        operateRecordDO.setModuleCode(dto.getModuleCode());
        operateRecordDO.setIsImport(dto.getIsImport());
        operateRecordDO.setOperateTimeMillis(dto.getOperateTimeMillis());
        operateRecordDO.setParams(dto.getParams() == null ? null : dto.getParams().toString());
        operateRecordDO.setResult(dto.getResult() == null ? null : dto.getResult().toString());
        // 写入站点code
        operateRecordDO.setSiteCode(SiteUtil.getSiteCode() == null ? MasterSlaveRoutingDataSource.getDbType()
                : SiteUtil.getSiteCode());
        operateRecordDO.setOperateTime(new Date());
        if (CommonUtils.getRequest() != null) {
            // 收集访问地址
            String url = CommonUtils.getRequest().getRequestURI();
            if (url.contains(OperateRecordConstant.URL_PARAM_SPLIT_SIGN)) {
                url = url.substring(0, url.indexOf(OperateRecordConstant.URL_PARAM_SPLIT_SIGN));
            }
            operateRecordDO.setUrl(url);
            // 收集用户ip
            operateRecordDO.setRequestIp(CommonUtils.getIpAddress());
            // 收集服务器ip
            try {
                operateRecordDO.setServerIp(CommonUtils.getServerIp());
            } catch (SocketException | UnknownHostException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("get server ip error", e);
                }
            }
            Optional.ofNullable(StaticBeanUtils.getUserService().getCurrentUser()).ifPresent(e -> {
                operateRecordDO.setUserId(e.getId());
                operateRecordDO.setLoginId(e.getLoginId());
            });
        }
        return operateRecordDO;
    }

    private void setModule(EnumConst module) {
        Optional.ofNullable(module).ifPresent(e -> {
            this.module = e;
            this.setModuleName(this.module.getName());
        });
    }

    private void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
        this.operateTimeStr = CommonUtils.changeDateToString(operateTime,
                CommonConstant.MYSQL_DEFAULT_DATE_FORMAT_STR);
    }

    /**
     * 原始数据收集类
     *
     * @author weipengsen
     */
    @Data
    private class OperateRecordData {

        private static final long serialVersionUID = 6976674771154712835L;

        private String operateName;

        private Boolean isImport;

        private String moduleCode;

        private Long operateTimeMillis;

        private Object result;

        private Map<String, Object> params;

        private OperateRecordData(com.whaty.framework.aop.operatelog.annotation.OperateRecord operateRecord, Object result) {
            this(operateRecord.value(), operateRecord.isImportant(), operateRecord.moduleCode(), result);
        }

        private OperateRecordData(String operateName, boolean important, String moduleCode, Object result) {
            this.operateName = operateName;
            this.isImport = important;
            this.moduleCode = moduleCode;
            this.params = new LinkedHashMap<>();
            this.result = result;
        }

        /**
         * 日志记录方法
         *
         * @param params
         * @param operateTimeMillis
         */
        private void handleOperate(Map<String, Object> params, long operateTimeMillis) {
            this.getParams().put(OperateRecordConstant.ARG_NAME_PARAMS, params);
            this.setOperateTimeMillis(operateTimeMillis);
            // 获取返回值
            if (this.getResult() != null && this.getResult() instanceof ResultDataModel) {
                Map<String, Object> resultMap = new LinkedHashMap<>();
                resultMap.put("errCode", ((ResultDataModel) this.getResult()).getMeta().getErrCode());
                resultMap.put("errMsg", ((ResultDataModel) this.getResult()).getMeta().getErrMsg());
                resultMap.put("data", ((ResultDataModel) this.getResult()).getData());
                this.setResult(resultMap);
            }
            OperateRecord.convertToDO(this).offerToProvider();
        }

    }


}

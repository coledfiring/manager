package com.whaty.framework.aop.operatelog.domain;

import com.alibaba.fastjson.JSON;
import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.framework.aop.MethodAspectParseUtils;
import com.whaty.framework.aop.operatelog.constant.DataDiffType;
import com.whaty.framework.aop.operatelog.constant.OperateRecordConstant;
import com.whaty.framework.aop.operatelog.strategy.AbstractCollectStrategy;
import com.whaty.framework.aop.operatelog.util.OperateRecordConfigUtils;
import com.whaty.framework.exception.UncheckException;
import com.whaty.util.CollectionDivider;
import com.whaty.util.CommonUtils;
import com.whaty.util.TycjCollectionUtils;
import com.whaty.utils.StaticBeanUtils;
import com.whaty.utils.UserUtils;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.aspectj.lang.JoinPoint;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 数据历史记录
 *
 * @author weipengsen
 */
@Data
public class DataHistoryRecord extends AbstractAsyAndMethodAspectRecord implements Serializable {

    private static final long serialVersionUID = 5897043739516454158L;

    private String operateName;

    private String nameSpace;

    private String siteCode;

    private Date operateTime;

    private String serverIp;

    private String clientIp;

    private String userId;

    private Map<String, Map<String, Object>> beforeData;

    private Map<String, Map<String, Object>> afterData;

    private Map<String, Map> diffData;

    private Boolean init;

    private transient AbstractCollectStrategy<? extends Annotation> recordStrategy;

    public DataHistoryRecord() throws SocketException, UnknownHostException {
        this.init = false;
        this.serverIp = CommonUtils.getServerIp();
        this.clientIp = CommonUtils.getIpAddress();
    }

    @Override
    public void operateBefore(JoinPoint join) {
        if (!this.initStatusData(join)) {
            return;
        }
        if (this.init) {
            this.setBeforeData(this.recordStrategy.collect());
        }
    }

    @Override
    public void operateReturning(JoinPoint join, Object result, Long aLong) {
        this.dataHistoryAfter();
    }

    @Override
    public void operateThrowing(JoinPoint join, Throwable t, Long aLong) {
        this.dataHistoryAfter();
    }

    @Override
    public void record() {
        String currentDB = MasterSlaveRoutingDataSource.getDbType();
        try {
            MasterSlaveRoutingDataSource.setDbType(this.siteCode);
            this.parseData();
            if (MapUtils.isEmpty(this.diffData)) {
                return;
            }
            StaticBeanUtils.getOpenGeneralDao().batchExecuteSql(this.getDiffData().entrySet().stream().flatMap(e -> {
                String statusSql = "INSERT INTO operate_status_record" +
                        "(operate_name, name_space, site_code, fk_link_id, operate_date, operate_time, server_ip," +
                        "client_ip, fk_operate_user_id ) VALUES (" + this.getNullOrTextValue(this.getOperateName()) +
                        ", " + this.getNullOrTextValue(this.getNameSpace()) + ", " +
                        this.getNullOrTextValue(this.getSiteCode()) + ", '" + e.getKey() + "', '" +
                        CommonUtils.changeDateToString(this.getOperateTime()) + "', '" +
                        CommonUtils.changeDateToString(this.getOperateTime(),
                                CommonConstant.MYSQL_DEFAULT_DATE_FORMAT_STR) + "', " +
                        this.getNullOrTextValue(this.getServerIp()) + ", " +
                        this.getNullOrTextValue(this.getClientIp()) + ", " +
                        this.getNullOrTextValue(this.getUserId()) + ")";
                String statusDetailSql = "INSERT INTO operate_status_record_detail" +
                        "(fk_operate_record_status_id, type, status_data ) SELECT LAST_INSERT_ID(), '" +
                        e.getValue().get("type") + "', " +
                        this.getNullOrTextValue(JSON.toJSONString(e.getValue()));
                return Stream.of(statusSql, statusDetailSql);
            }).collect(Collectors.toList()));
        } finally {
            Optional.ofNullable(currentDB).ifPresent(MasterSlaveRoutingDataSource::setDbType);
        }
    }

    /**
     * 分析数据
     * @return
     */
    private void parseData() {
        if (this.beforeData == null && this.afterData == null) {
            return;
        }
        Map<String, Map> resultMap = new HashMap<>(4);
        CollectionDivider<String> divide = new CollectionDivider<>(
                MapUtils.isNotEmpty(this.beforeData) ? this.beforeData.keySet() : null,
                MapUtils.isNotEmpty(this.afterData) ? this.afterData.keySet() : null);
        if (CollectionUtils.isNotEmpty(divide.getAdd())) {
            resultMap.putAll(divide.getAdd().stream().collect(Collectors
                    .toMap(e -> e, e -> this.packageData(DataDiffType.INSERT, this.afterData.get(e)))));
        }
        if (CollectionUtils.isNotEmpty(divide.getDelete())) {
            resultMap.putAll(divide.getDelete().stream().collect(Collectors
                    .toMap(e -> e, e -> this.packageData(DataDiffType.DELETE, this.beforeData.get(e)))));
        }
        if (CollectionUtils.isNotEmpty(divide.getSame())) {
            for (String key : divide.getSame()) {
                Map<String, Object> target = this.packageData(DataDiffType.CHANGE,
                        this.parseDiff(this.beforeData.get(key), this.afterData.get(key)));
                if (MapUtils.isNotEmpty(target)) {
                    resultMap.put(key, target);
                }
            }
        }
        this.setDiffData(resultMap);
    }

    /**
     * 封装信息
     * @param type
     * @param data
     * @return
     */
    private Map<String, Object> packageData(DataDiffType type, Object data) {
        if (data == null) {
            return null;
        }
        return TycjCollectionUtils.map("type", type.getType(), "data", data);
    }

    /**
     * 分析差异
     * @return
     * @param beforeData
     * @param afterData
     */
    private List<ItemDiff> parseDiff(Map<String, Object> beforeData, Map<String, Object> afterData) {
        List<ItemDiff> diffs = TycjCollectionUtils.union(beforeData.keySet(), afterData.keySet()).stream()
                .filter(e -> !this.equals(beforeData.get(e), afterData.get(e)))
                .map(e -> ItemDiff.newInstance(e, beforeData.get(e), afterData.get(e)))
                .filter(Objects::nonNull).collect(Collectors.toList());
        // 去除为空和空串
        diffs = diffs.stream().peek(ItemDiff::convertBlankStringToNull)
                .filter(e -> e.getBefore() != e.getAfter()).collect(Collectors.toList());
        return CollectionUtils.isEmpty(diffs) ? null : diffs;
    }

    /**
     * 对比对象是否相同
     * @param o1
     * @param o2
     * @param <T>
     * @return
     */
    private <T> boolean equals(T o1, T o2) {
        if (o1 == o2) {
            return true;
        }
        return o1 != null && o2 != null && o1.equals(o2);
    }

    /**
     * 初始化状态数据
     *
     * @param join
     * @return
     */
    private boolean initStatusData(JoinPoint join) {
        return this.recordFunctional(join, operateRecord -> {
            this.setOperateName(operateRecord.value());
            this.initStatusRecordContext(join, MethodAspectParseUtils.getMethodAnnotation(join));
        }, (methodName, basicOperateRecord) -> {
            String operateName = basicOperateRecord.value() + OperateRecordConfigUtils
                    .getProperty(String.format(OperateRecordConstant.PROPERTY_KEY_BASIC_OPERATE_NAME, methodName));
            this.setOperateName(operateName);
            this.initStatusRecordContext(join, MethodAspectParseUtils.getClassAnnotation(join));
        });
    }

    /**
     * 初始化
     *
     * @param join
     * @param annotations
     */
    private void initStatusRecordContext(JoinPoint join, Annotation[] annotations) {
        try {
            AbstractCollectStrategy<? extends Annotation> strategy = AbstractCollectStrategy
                    .newInstance(this.getParams(join), annotations, join);
            if (strategy == null) {
                return;
            }
            this.siteCode = MasterSlaveRoutingDataSource.getDbType();
            this.operateTime = new Date();
            this.userId = UserUtils.getCurrentUserId();
            this.nameSpace = strategy.namespace();
            this.recordStrategy = strategy;
            this.init = true;
        } catch (Exception e) {
            throw new UncheckException(e);
        }
    }

    /**
     * 数据历史记录，操作后处理
     *
     */
    private void dataHistoryAfter() {
        if (!this.init) {
            return;
        }
        this.setAfterData(this.recordStrategy.collect());
        this.offerToProvider();
    }

    /**
     * 获取null或者文本值，增加sql中的引用
     * @param originValue
     * @return
     */
    private String getNullOrTextValue(String originValue) {
        return originValue == null ? "null" : "'" + originValue + "'";
    }

}

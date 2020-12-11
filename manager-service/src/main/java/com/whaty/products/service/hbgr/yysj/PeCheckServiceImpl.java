package com.whaty.products.service.hbgr.yysj;

import com.alibaba.fastjson.JSONObject;
import com.whaty.constant.CommonConstant;
import com.whaty.constant.SiteConstant;
import com.whaty.core.commons.datasource.MasterSlaveRoutingDataSource;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.framework.bean.EnumConst;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.PeReceived;
import com.whaty.domain.bean.hbgr.yysj.PeCheck;
import com.whaty.framework.config.util.SiteUtil;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.framework.httpClient.helper.HttpClientHelper;
import com.whaty.products.service.hbgr.yysj.constant.YysjConstants;
import com.whaty.schedule.bean.PeScheduleJob;
import com.whaty.schedule.bean.PeScheduleTrigger;
import com.whaty.schedule.job.JobStatusBus;
import com.whaty.util.CommonUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * author weipengsen  Date 2020/7/7
 */
@Lazy
@Service("peCheckService")
public class PeCheckServiceImpl extends TycjGridServiceAdapter<PeCheck> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Resource(name = CommonConstant.OPEN_GENERAL_DAO_BEAN_NAME)
    private GeneralDao openGeneralDao;

    public Map<String, Object> getCheckOptions() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("checkPoints", this.myGeneralDao.getMapBySQL("SELECT point.id as id," +
                " CONCAT(e.name, '-', point.name) as name, address FROM pe_check_point point" +
                " INNER JOIN enum_const e ON e.id = point.flag_scene"));
        resultMap.put("users", this.myGeneralDao.getMapBySQL("SELECT manager.id as id, manager.`name` as name" +
                " FROM pe_manager manager INNER JOIN pe_pri_role role ON role.id = manager.fk_role_id" +
                " WHERE role.site_code = ? AND role.`CODE`= '229'", SiteUtil.getSiteCode()));
        resultMap.put("checkTypes", this.myGeneralDao.getMapBySQL("SELECT id, name FROM enum_const WHERE namespace = 'flagCheckType'"));
        return resultMap;
    }

    public void setValid(String ids) {
        this.myGeneralDao.executeBySQL("UPDATE pe_check SET flag_Isvalid = ? WHERE "
                + CommonUtils.madeSqlIn(ids, "id"), this.myGeneralDao
                .getEnumConstByNamespaceCode("FlagIsvalid", "1").getId());
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        this.openGeneralDao.executeBySQL("UPDATE pe_schedule_trigger SET flag_job_valid = ? WHERE "
                        + CommonUtils.madeSqlIn(ids, "trigger_name"),
                this.openGeneralDao.getEnumConstByNamespaceCode("FlagJobValid", "1").getId());

    }

    public void setNoValid(String ids) {
        this.myGeneralDao.executeBySQL("UPDATE pe_check SET flag_Isvalid = ? WHERE "
                + CommonUtils.madeSqlIn(ids, "id"), this.myGeneralDao
                .getEnumConstByNamespaceCode("FlagIsvalid", "0").getId());
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        this.openGeneralDao.executeBySQL("UPDATE pe_schedule_trigger SET flag_job_valid = ? WHERE "
                + CommonUtils.madeSqlIn(ids, "trigger_name"),
                this.openGeneralDao.getEnumConstByNamespaceCode("FlagJobValid", "0").getId());
    }

    @Override
    protected void afterDelete(List idList) throws EntityException {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        this.openGeneralDao.executeBySQL("DELETE FROM pe_schedule_trigger WHERE "
                + CommonUtils.madeSqlIn(idList, "trigger_name"));
    }

    public void addOrUpdateCheck(PeCheck peCheck) throws IOException, ParseException {
        peCheck.setEnumConstByFlagIsvalid(this.myGeneralDao
                .getEnumConstByNamespaceCode("FlagIsvalid", "1"));
        Calendar cal = Calendar.getInstance();
        cal.setTime(peCheck.getStartTime());
        cal.add(Calendar.HOUR, 9);
        peCheck.setStartTime(cal.getTime());
        List<String> list = this.myGeneralDao.getBySQL("SELECT mobile FROM pe_manager WHERE "
                + CommonUtils.madeSqlIn(peCheck.getUsers(), "id"));
        List<String> names = this.myGeneralDao.getBySQL("SELECT name FROM pe_check_point WHERE " +
                CommonUtils.madeSqlIn(peCheck.getPoints(), "id"));
        Map<String, Object> map = new HashMap<>(16);
        map.put("checkType", this.myGeneralDao.getById(EnumConst.class,
                peCheck.getEnumConstByFlagCheckType().getId()).getName());
        map.put("note", peCheck.getNote());
        map.put("date", peCheck.getStartTime());
        map.put("mobiles", list.stream().map(e -> "'" + e + "'").collect(Collectors.joining(",")));
        map.put("points", String.join(",", names));
        map.put("days", peCheck.getDays());
        String linkId = this.myGeneralDao.save(peCheck).getId();
        Stream.of(peCheck.getUsers().split(CommonConstant.SPLIT_ID_SIGN)).forEach(x ->
                this.myGeneralDao.executeBySQL("INSERT INTO pr_check_link (id, fk_check_id, link) " +
                        "VALUES (?, ?, ?)", com.whaty.util.CommonUtils.generateUUIDNoSign(), linkId, x));
        Stream.of(peCheck.getPoints().split(CommonConstant.SPLIT_ID_SIGN)).forEach(x ->
                this.myGeneralDao.executeBySQL("INSERT INTO pr_check_link (id, fk_check_id, link) " +
                        "VALUES (?, ?, ?)", com.whaty.util.CommonUtils.generateUUIDNoSign(), linkId, x));
        peCheck.setCreateTime(new Date());
        System.out.println(sendDingTalk(map));
        this.saveTrigger(linkId, peCheck.getStartTime(), 0, "checkOnceJob");
    }

    private void saveTrigger(String id, Date startTime, int days, String groupName) {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SITE_CODE_CONTROL);
        this.openGeneralDao.executeBySQL("DELETE FROM pe_schedule_trigger WHERE trigger_name = ?", id);
        PeScheduleTrigger peScheduleTrigger = new PeScheduleTrigger();
        if(days == 0) {
            peScheduleTrigger.setTriggerTime(startTime);
        } else {
            peScheduleTrigger.setCronExpression("0 0 9 */" + days + " * ?");
        }
        peScheduleTrigger.setName(id);
        Map<String, Object> jobMap = this.openGeneralDao.getOneMapBySQL("SELECT id as id, job_group as name " +
                "FROM pe_schedule_job WHERE job_group = '" + groupName + "'");
        PeScheduleJob peScheduleJob = new PeScheduleJob();
        peScheduleJob.setId((String) jobMap.get("id"));
        peScheduleTrigger.setData(JSONObject.toJSONString(Collections.singletonMap("id", id)));
        peScheduleTrigger.setPeScheduleJob(peScheduleJob);
        peScheduleTrigger.setGroup((String) jobMap.get("name"));
        peScheduleTrigger.setEnumConstByFlagJobValid(this.openGeneralDao
                .getEnumConstByNamespaceCode("FlagJobValid", "1"));
        this.openGeneralDao.save(peScheduleTrigger);
            try {
                // 更新任务版本
                this.openGeneralDao.executeBySQL("UPDATE pe_schedule_job SET version = version + 1 WHERE id = ?",
                        jobMap.get("id"));
                new JobStatusBus().setUpdateStatus();
            } catch (Exception e) {
                throw new UncheckException(e);
            }
    }

    public String sendDingTalk(Map<String, Object> map) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时");
        String s = "{'msgtype':'text'," +
                " 'text': {'content':'111 巡检任务通知 \n\n" +
                "巡检类型：" + map.get("checkType") + "\n\n" +
                "巡检点：" + map.get("points") + "\n\n" +
                "开始巡检时间：" + format.format(map.get("date")) + "\n\n" +
                "循环间隔（天数）：" + map.get("days") + "天\n\n" +
                "注意事项：" + map.get("note") + "\n\n" +
                "请注意本群通知，巡检当天会再次通知，巡检完成务必扫描巡检点二维码进行巡检记录！'}, " +
                " 'at': {'atMobiles': [" + map.get("mobiles") + "]}}";
       return new HttpClientHelper().doPostJSON(CommonConstant.DING_DING_ROBOT_URL, s).getContent();
    }

    public void executeOnceCheckJob(Map<String, Object> data) throws IOException {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SERVICE_SITE_CODE_HB);
         PeCheck peCheck = this.myGeneralDao.getById(PeCheck.class, (String)data.get("id"));
        this.saveTrigger(peCheck.getId(), null, peCheck.getDays(), "checkTimesJob");
        this.sendDingNotice(peCheck);
    }

    public void executeTimesCheckJob(Map<String, Object> data) throws IOException {
        PeCheck peCheck = this.myGeneralDao.getById(PeCheck.class, (String)data.get("id"));
        sendDingNotice(peCheck);
    }

    private void sendDingNotice(PeCheck peCheck) throws IOException {
        MasterSlaveRoutingDataSource.setDbType(SiteConstant.SERVICE_SITE_CODE_HB);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR, peCheck.getDays());
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd HH时");
        List<String> list = this.myGeneralDao.getBySQL("SELECT mobile FROM pe_manager WHERE "
                + CommonUtils.madeSqlIn(peCheck.getUsers(), "id"));
        List<String> names = this.myGeneralDao.getBySQL("SELECT name FROM pe_check_point WHERE " +
                CommonUtils.madeSqlIn(peCheck.getPoints(), "id"));
        Map<String, Object> noticeMap = new HashMap<>(16);
        noticeMap.put("checkType", this.myGeneralDao.getById(EnumConst.class,
                peCheck.getEnumConstByFlagCheckType().getId()).getName());
        noticeMap.put("mobiles", list.stream().map(e -> "'" + e + "'").collect(Collectors.joining(",")));
        noticeMap.put("points", String.join(",", names));
        String s = "{'msgtype':'text'," +
                " 'text': {'content':'111 巡检任务通知 \n\n" +
                "巡检类型：" + noticeMap.get("checkType") + "\n\n" +
                "巡检点：" + noticeMap.get("points") + "\n\n" +
                "循环间隔（天数）：" + peCheck.getDays() + "天\n\n" +
                "下次巡检时间：" + format.format(cal.getTime()) + "\n\n" +
                "注意事项：" + peCheck.getNote() + "\n\n" +
                "请注意本群通知，巡检当天会再次通知，巡检完成务必扫描巡检点二维码进行巡检记录！'}, " +
                " 'at': {'atMobiles': [" + noticeMap.get("mobiles") + "]}}";
        System.out.println(new HttpClientHelper().doPostJSON(CommonConstant.DING_DING_ROBOT_URL, s));
    }

    public PeCheck getCheckData(String id) {
        return this.myGeneralDao.getById(PeCheck.class, id);
    }


/*    PeReceived received = this.generalDao.getById(PeReceived.class, id);
        received.setReceivedUserList(this.generalDao.getBySQL("SELECT pm.id AS id" +
                " FROM pri_received_manager prm " +
                "INNER JOIN pe_received  pr ON pr.id = prm.fk_received_id " +
                "INNER JOIN pe_manager pm ON pm.id = prm.fk_pe_manager_id WHERE pr.id = ?", id));
        return received;*/
}

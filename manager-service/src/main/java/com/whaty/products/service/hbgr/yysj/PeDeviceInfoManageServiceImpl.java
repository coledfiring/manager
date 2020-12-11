package com.whaty.products.service.hbgr.yysj;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.core.commons.util.excel.ExcelStyleUtils;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.hbgr.yysj.PeCheckPoint;
import com.whaty.domain.bean.hbgr.yysj.PeDevice;
import com.whaty.domain.bean.hbgr.yysj.PeDeviceExperience;
import com.whaty.framework.asserts.TycjParameterAssert;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.framework.grid.twolevellist.service.impl.AbstractTwoLevelListGridServiceImpl;
import com.whaty.products.service.hbgr.yysj.constant.YysjConstants;
import com.whaty.util.CommonUtils;
import com.whaty.util.QrCodeUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

/**
 * author weipengsen  Date 2020/6/20
 */
@Lazy
@Service("peDeviceInfoManageService")
public class PeDeviceInfoManageServiceImpl extends TycjGridServiceAdapter<PeDevice> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(PeDevice bean, Map<String, Object> params) throws EntityException {
        checkBeforeAddOrUpdate(bean);
    }


    public void checkBeforeAddOrUpdate(PeDevice bean) {
        String additionalSql = bean.getId() == null ? "" : " AND id <> '" + bean.getId() + "'";
        if (this.myGeneralDao.checkNotEmpty("SELECT 1 FROM pe_device WHERE code = ?" + additionalSql,
                bean.getCode())) {
            throw new ServiceException("此设备编号已存在");
        }
    }

    @Override
    public void checkBeforeUpdate(PeDevice bean) throws EntityException {
        checkBeforeAddOrUpdate(bean);
    }

    public void downLoadDeviceInfoQrcode(String ids, OutputStream out) throws Exception {
        List<Map<String, Object>> listCode = this.myGeneralDao.getMapBySQL("SELECT code FROM pe_device WHERE "
                + CommonUtils.madeSqlIn(ids, "id"));
        Map<String, String> fileMap = new HashMap<>();
        listCode.forEach(e-> fileMap.put("设备-" + e.get("code"), String.format(YysjConstants.DEVICE_INFO_QRCODE_WEB_PATH, (String)e.get("code"))));
        QrCodeUtil.generateQrCodes(fileMap, 500, YysjConstants.DEVICE_INFO_QRCODE_FILE_PATH);
        try (ZipOutputStream zipOut = new ZipOutputStream(out)) {
            List<File> files = Arrays.asList(Objects.requireNonNull(new File(CommonUtils.getRealPath(YysjConstants.DEVICE_INFO_QRCODE_FILE_PATH)).listFiles()));
            CommonUtils.packageToZip(files, zipOut);
        } catch (Exception e) {
            throw new ServiceException("导出错误，存在无法预览的图片");
        }
    }

    public Map<String, Object> getDeviceInfoByCode(String code) {
        TycjParameterAssert.isAllNotBlank(code);
        if(!this.myGeneralDao.checkNotEmpty("SELECT 1 FROM pe_device WHERE code = ?", code)) {
            throw new ServiceException("不存在此设备编号");
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                                                                  ");
        sql.append(" 	device.id AS id,                                                     ");
        sql.append(" 	device.NAME AS name,                                                 ");
        sql.append(" 	device.CODE AS code,                                                 ");
        sql.append(" 	power AS power,                                                      ");
        sql.append(" 	factory AS factory,                                                  ");
        sql.append(" 	provide AS provide,                                                  ");
        sql.append(" 	note AS note,                                                        ");
        sql.append(" 	type.`name` AS deviceTypeName,                                       ");
        sql.append(" 	type.`code` AS deviceTypeCode                                        ");
        sql.append(" FROM                                                                    ");
        sql.append(" 	pe_device device                                                     ");
        sql.append(" 	INNER JOIN pe_device_type type ON type.id = device.flag_device_type  ");
        sql.append(" 	WHERE device.code = ?                                                ");
        return this.myGeneralDao.getOneMapBySQL(sql.toString(), code);
    }

    public List<Map<String, Object>> getDeviceRepair(String code) {
        TycjParameterAssert.isAllNotBlank(code);
        if(!this.myGeneralDao.checkNotEmpty("SELECT 1 FROM pe_device WHERE code = ?", code)) {
            throw new ServiceException("不存在此设备编号");
        }
        return this.myGeneralDao.getMapBySQL("SELECT reason, experience, DATE_FORMAT(e.create_time, " +
                "'%Y-%m-%d %H:%i') as createTime " +
                " FROM pe_device_experience e " +
                " INNER JOIN pe_device device ON device.id = e.fk_device_id WHERE device.code = ? " +
                " ORDER BY createTime DESC", code);
    }

    public void toAddDeviceRepair(String code, String reason, String experience) {
        TycjParameterAssert.isAllNotBlank(code, reason, experience);
        if(!this.myGeneralDao.checkNotEmpty("SELECT 1 FROM pe_device WHERE code = ?", code)) {
            throw new ServiceException("不存在此设备编号");
        }
       this.myGeneralDao.executeBySQL("INSERT INTO `pe_device_experience`(`id`, `experience`, `fk_device_id`," +
               " `reason`, create_time) SELECT REPLACE(UUID(),'-',''),  ?, id, ?, now() FROM pe_device WHERE code = ?",
               experience, reason, code);
    }
}

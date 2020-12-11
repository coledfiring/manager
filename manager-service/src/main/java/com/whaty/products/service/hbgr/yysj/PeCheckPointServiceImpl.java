package com.whaty.products.service.hbgr.yysj;

import com.whaty.constant.CommonConstant;
import com.whaty.core.commons.exception.EntityException;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.hbgr.yysj.PeCheckPoint;
import com.whaty.domain.bean.hbgr.yysj.PeCheckRecord;
import com.whaty.framework.exception.ServiceException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.products.service.hbgr.yysj.constant.YysjConstants;
import com.whaty.util.CommonUtils;
import com.whaty.util.QrCodeUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.OutputStream;
import java.util.*;
import java.util.zip.ZipOutputStream;

/**
 * author weipengsen  Date 2020/7/13
 */
@Lazy
@Service("peCheckPointService")
public class PeCheckPointServiceImpl extends TycjGridServiceAdapter<PeCheckPoint> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    @Override
    public void checkBeforeAdd(PeCheckPoint bean) throws EntityException {
        checkBeforeAddOrUpdate(bean);
    }

    @Override
    public void checkBeforeUpdate(PeCheckPoint bean) throws EntityException {
        checkBeforeAddOrUpdate(bean);
    }

    public void checkBeforeAddOrUpdate(PeCheckPoint bean) {
        String additionSql = bean.getId() == null ? "" : " and id <> '" + bean.getId() + "'";
        if (this.myGeneralDao.checkNotEmpty("SELECT 1 FROM pe_check_point WHERE code = ? " + additionSql,
                bean.getCode())) {
            throw new ServiceException("此现场编号已存在");
        }
    }

    public void downLoadCheckRecordQrcode(String ids, OutputStream out) throws Exception {
        List<Map<String, Object>> listCode = this.myGeneralDao.getMapBySQL("SELECT id, name FROM pe_check_point  WHERE "
                + CommonUtils.madeSqlIn(ids, "id"));
        Map<String, String> fileMap = new HashMap<>();
        listCode.forEach(e-> fileMap.put("巡检点-" + e.get("name"),
                String.format(YysjConstants.CHECK_POINT_RECORD_QRCODE_WEB_PATH, (String)e.get("id"))));
        QrCodeUtil.generateQrCodes(fileMap, 500, YysjConstants.CHECK_POINT_QRCODE_FILE_PATH);
        try (ZipOutputStream zipOut = new ZipOutputStream(out)) {
            List<File> files = Arrays.asList(Objects.requireNonNull(new File(CommonUtils.getRealPath(YysjConstants.CHECK_POINT_QRCODE_FILE_PATH)).listFiles()));
            CommonUtils.packageToZip(files, zipOut);
        } catch (Exception e) {
            throw new ServiceException("导出错误，存在无法预览的图片");
        }
    }
}

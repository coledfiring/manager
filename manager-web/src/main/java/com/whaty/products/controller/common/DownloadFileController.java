package com.whaty.products.controller.common;

import com.whaty.framework.grid.base.controller.TycjBaseControllerOperateSupport;
import com.whaty.products.service.common.DownloadFileService;
import com.whaty.products.service.common.constant.ComConstant;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 下载专用controller
 *
 * @author weipengsen
 */
@Lazy
@Controller("downloadFileController")
@RequestMapping("/common/downloadFile")
public class DownloadFileController extends TycjBaseControllerOperateSupport {

    @Resource(name = "downloadFileService")
    private DownloadFileService downloadFileService;

    static {
        //将可下载的资源写入map中
        /*
         * excel
         */
        // 毕业证号
        ComConstant.FILE_PATH_LINK_MAP.put("graduateCertificateNo.xls",
                "/graduateDegree/excel/graduateCertificateNo.xls");
        // 学位证号
        ComConstant.FILE_PATH_LINK_MAP.put("degreeCertificateNo.xls",
                "/graduateDegree/excel/degreeCertificateNo.xls");
        // 教材征订
        ComConstant.FILE_PATH_LINK_MAP.put("bookMaterialSolicit.xls",
                "/teaching/excel/bookMaterialSolicit.xls");
        // 批量导入考生号
        ComConstant.FILE_PATH_LINK_MAP.put("uploadExamNo.xls",
                "/selfEnroll/excel/uploadExamNo.xls");
        //百校千课教材库导入
        ComConstant.FILE_PATH_LINK_MAP.put("teachingMaterial.xls",
                "/bxqk/basic/excel/teachingMaterial.xls");
        //百校千课高校课程导入
        ComConstant.FILE_PATH_LINK_MAP.put("schoolCourse.xls",
                "/bxqk/course/excel/schoolCourse.xls");
        //百校千课高校课程教材导入
        ComConstant.FILE_PATH_LINK_MAP.put("schoolCourseTeachingMaterial.xls",
                "/bxqk/course/excel/schoolCourseTeachingMaterial.xls");
        //百校千课教材征订导入
        ComConstant.FILE_PATH_LINK_MAP.put("schoolSolicit.xls",
                "/bxqk/solicit/excel/schoolSolicit.xls");
        /*
         * zip
         */
        //课件配置
        ComConstant.FILE_PATH_LINK_MAP.put("imsmanifest.zip",
                "/teaching/zip/imsmanifest.zip");
        // 开设站点信息
        ComConstant.FILE_PATH_LINK_MAP.put("createSite.xls",
                "/superAdmin/excel/createSite.xls");
    }

    /**
     * 下载已存在的文件
     */
    @RequestMapping("/downloadExistentFile")
    public void downloadExistentFile(HttpServletResponse response, HttpServletRequest request)
            throws IOException, ServletException {
        this.downFile(response, out -> this.downloadFileService
                .downloadExistentFile(request.getParameter(ComConstant.PARAM_FILE_KEY), out),
                request.getParameter(ComConstant.PARAM_FILE_KEY));
    }

}

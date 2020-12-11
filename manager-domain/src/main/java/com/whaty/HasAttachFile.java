package com.whaty;

import com.whaty.core.bean.AbstractBean;
import com.whaty.domain.bean.*;
import com.whaty.domain.bean.hbgr.yysj.PeDevice;
import com.whaty.domain.bean.online.OlPeClass;
import com.whaty.domain.bean.online.OlPeCourse;
import com.whaty.domain.bean.online.OlPeOrganization;
import com.whaty.domain.bean.vehicle.Motorcade;
import com.whaty.domain.bean.vehicle.Vehicle;
import com.whaty.domain.bean.vehicle.VehicleApplication;

import java.util.Arrays;

/**
 * 抽象行为，关联附件
 *
 * @author weipengsen
 */
public interface HasAttachFile {

    enum AttachFileBean {

        /** 班级 */
        PE_CLASS(PeClass.class, "peClass", 999, "附件"),

        /** 班级相册 */
        PE_CLASS_ALBUM(PeClass.class, "peClassAlbum", 999, "附件"),

        /** 课程 */
        PE_COURSE(PeCourse.class, "courseSource", 999, "附件"),

        /** 项目 */
        TRAINING_ITEM(TrainingItem.class, "trainingItem", 999, "附件"),

        /** 宾馆 */
        HOTEL_AGREEMENT(PeHotel.class, "hotelAgreement", 999, "合作协议"),

        /** 证书 */
        TRAINING_CERTIFICATE(TrainingCertificate.class, "trainingCertificate", 1, "证书"),

        /** 合作单位协议 */
        COOPERATE_UNIT(CooperateUnit.class, "cooperateUnit", 1, "合作协议"),

        /** 文件管理 */
        FILE_MANAGE(CooperateUnit.class, "fileManage", 999, "文件管理"),

        /** 用印 */
        USE_SEAL_MANAGE(UseSeal.class, "useSealManage", 999, "用印附件"),

        /** 印刷 */
        PE_PRINTING_MANAGE(PePrinting.class, "pePrintingManage", 999, "印刷附件"),

        /** 需求 */
        REQUIREMENT_INFO(RequirementInfo.class, "requirementInfoFollowUp", 999, "附件"),

        /** 接待 */
        PE_RECEIVED_MANAGE(PeReceived.class, "peReceivedManage", 999, "接待附件"),

        /** 项目评审 */
        PE_REVIEW_MANAGE(PeReview.class, "peReviewManage", 999, "项目评审附件"),
        /** 待办催办 */
        TRAINING_MANAGER_TASK(TrainingManagerTask.class, "trainingManagerTask", 999, "待办催办附件"),

        /** 车队附件 */
        MOTORCADE_ATTACH_FILE_MANAGE(Motorcade .class, "motorcadeAttachFileManage", 999, "车队附件"),

        /** 车辆附件 */
        VEHICLE_ATTACH_FILE_MANAGE(Vehicle.class, "vehicleAttachFileManage", 999, "车辆附件"),

        OL_PE_ORGANIZATION(OlPeOrganization.class, "olPeOrganization", 999, "机构附件"),

        OL_PE_COURSE(OlPeCourse.class, "olPeCourse", 999, "课程附件"),

        /** 班级相册 */
        OL_PE_CLASS_ALBUM(OlPeClass.class, "olPeClassAlbum", 999, "附件"),

        OL_PE_CLASS(OlPeClass.class, "olPeClass", 999, "班级附件"),

        /**
         * 设备附件管理
         */
        PE_DEVICE_INFO(PeDevice.class, "peDeviceInfo", 999, "设备附件管理"),

        /** 用车申请附件 */
        VEHICLE_APPLICATION_ATTACH_FILE_MANAGE(VehicleApplication.class,
                "vehicleApplicationAttachFileManage", 999, "用车申请附件");

        private Class<? extends AbstractBean> bean;

        private String namespace;

        private Integer limit;

        private String listName;

        AttachFileBean(Class<? extends AbstractBean> bean, String namespace, Integer limit, String listName) {
            this.bean = bean;
            this.namespace = namespace;
            this.limit = limit;
            this.listName = listName;
        }

        public static String getNamespaceByClass(Class<? extends AbstractBean> bean) {
            AttachFileBean attachFileBean = Arrays.stream(values()).filter(e -> bean.equals(e.getBean()))
                    .findFirst().orElse(null);
            if (attachFileBean == null) {
                throw new IllegalArgumentException();
            }
            return attachFileBean.getNamespace();
        }

        public static AttachFileBean getAttachConfigByNamespace(String namespace) {
            AttachFileBean attachFileBean = Arrays.stream(values()).filter(e -> namespace.equals(e.getNamespace()))
                    .findFirst().orElse(null);
            if (attachFileBean == null) {
                throw new IllegalArgumentException();
            }
            return attachFileBean;
        }

        public Class<? extends AbstractBean> getBean() {
            return bean;
        }

        public String getNamespace() {
            return namespace;
        }

        public Integer getLimit() {
            return limit;
        }

        public String getListName() {
            return listName;
        }
    }

}

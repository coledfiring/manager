package com.whaty.products.service.resource.constant;

/**
 * 课程资源常量
 *
 * @author weipengsen
 */
public interface ResourceConstants {

    /**
     * 课程图片保存路径
     */
    String COURSE_IMAGE_URL = "/incoming/course/images/%s/%s";
    /**
     * 默认课程照片
     */
    String COURSE_DEFAULT_PHOTO = "/images/course/default_picture.png";

    /**
     * 课程设计的访问路径
     */
    String LEARNING_SPACE_COURSE_DESIGN =
            "%s/sign/signLearn.action?sign=1&courseId=${value}&loginId=%s&siteCode=%s&loginType=true&domain=%s";
    /**
     * 课程预览的访问路径
     */
    String LEARNING_SPACE_COURSE_PREPARE_SHOW =
            "%s/sign/signLearn.action?sign=0&courseId=${value}&loginId=%s&siteCode=%s&loginType=true&domain=%s";
}

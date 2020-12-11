package com.whaty.products.service.clazz.domain;


import com.whaty.util.CommonUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 培训班地点使用一览的vo
 *
 * @author weipengsen
 */
public class ClassPlaceVo implements Serializable {

    private static final long serialVersionUID = -1823077989893663822L;
    /**
     * 对应日期
     */
    private String day;
    /**
     * 星期
     */
    private String week;
    /**
     * 课程表
     */
    private Map<String, Map<String, ClassTimetableVo>> classTimetable;

    public ClassPlaceVo(String day, List<Map<String, Object>> dayUsage) {
        this.day = day;
        Date date = CommonUtils.changeStringToDate(day);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.week = WeekNum.getWeekNameByNumber(calendar.get(Calendar.DAY_OF_WEEK));
        if (dayUsage == null) {
            this.classTimetable = null;
        } else {
            Map<String, List<Map<String, Object>>> timeMap = dayUsage.stream()
                    .collect(Collectors.groupingBy(e -> (String) e.get("timeId")));
            this.classTimetable = timeMap.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> convertMap(e.getValue())));
        }
    }

    private enum WeekNum {

        MONDAY(2, "星期一"),

        TUESDAY(3, "星期二"),

        WEDNESDAY(4, "星期三"),

        THURSDAY(5, "星期四"),

        FRIDAY(6, "星期五"),

        SATURDAY(7, "星期六"),

        SUNDAY(1, "星期日"),
        ;

        private Integer weekNumber;

        private String weekName;

        WeekNum(Integer weekNumber, String weekName) {
            this.weekNumber = weekNumber;
            this.weekName = weekName;
        }

        static String getWeekNameByNumber(int weekNumber) {
            WeekNum weekNum = Arrays.stream(values()).filter(e -> e.getWeekNumber() == weekNumber)
                    .findFirst().orElse(null);
            if (weekNum == null) {
                throw new IllegalArgumentException();
            }
            return weekNum.getWeekName();
        }

        public Integer getWeekNumber() {
            return weekNumber;
        }

        public String getWeekName() {
            return weekName;
        }
    }

    private static Map<String, ClassTimetableVo> convertMap(List<Map<String, Object>> origin) {
        return origin.stream().map(ClassTimetableVo::new)
                .collect(Collectors.toMap(ClassTimetableVo::getPlaceId, e -> e));
    }

    /**
     * 将传入数据转化成vo
     * @param allDateInLimit
     * @param dayUsage
     * @return
     */
    public static List<ClassPlaceVo> convertVO(List<String> allDateInLimit,
                                               Map<String, List<Map<String, Object>>> dayUsage) {
        return allDateInLimit.stream().map(e -> new ClassPlaceVo(e, dayUsage.get(e))).collect(Collectors.toList());
    }

    private static class ClassTimetableVo implements Serializable {

        private static final long serialVersionUID = 6801358110476601811L;

        private String id;
        /**
         * 地点id
         */
        private String placeId;
        /**
         * 培训班名称
         */
        private String className;
        /**
         * 培训单位
         */
        private String trainingUnit;
        /**
         * 业务类型
         */
        private String itemType;
        /**
         * 课程名称
         */
        private String courseName;
        /**
         * 班主任
         */
        private String classMaster;
        /**
         * 学生数
         */
        private Integer studentNumber;
        /**
         * 安排方式
         */
        private String placeType;
        /**
         * 安排方式编号
         */
        private String placeTypeCode;
        /**
         * 安排类型
         */
        private String arrangeType;
        /**
         * 用途
         */
        private String application;
        /**
         * 背景色
         */
        private String color;

        public ClassTimetableVo(Map<String, Object> origin) {
            this((String) origin.get("id"), (String) origin.get("placeId"), (String) origin.get("className"),
                    (String) origin.get("trainingUnit"), (String) origin.get("itemType"),
                    (String) origin.get("courseName"), (String) origin.get("classMaster"),
                    origin.get("studentNumber") != null ? ((BigInteger) origin.get("studentNumber")).intValue() : null,
                    (String) origin.get("placeType"), (String) origin.get("placeTypeCode"),
                    (String) origin.get("application"), (String) origin.get("arrangeType"),
                    (String) origin.get("color"));
        }

        public ClassTimetableVo(String id, String placeId, String className, String trainingUnit, String itemType,
                                String courseName, String classMaster, Integer studentNumber,
                                String placeType, String placeTypeCode, String application,
                                String arrangeType, String color) {
            this.id = id;
            this.placeId = placeId;
            this.className = className;
            this.trainingUnit = trainingUnit;
            this.itemType = itemType;
            this.courseName = courseName;
            this.classMaster = classMaster;
            this.studentNumber = studentNumber;
            this.placeTypeCode = placeTypeCode;
            this.placeType = placeType;
            this.application = application;
            this.arrangeType = arrangeType;
            this.color = color;
        }

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getTrainingUnit() {
            return trainingUnit;
        }

        public void setTrainingUnit(String trainingUnit) {
            this.trainingUnit = trainingUnit;
        }

        public String getItemType() {
            return itemType;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public String getClassMaster() {
            return classMaster;
        }

        public void setClassMaster(String classMaster) {
            this.classMaster = classMaster;
        }

        public Integer getStudentNumber() {
            return studentNumber;
        }

        public void setStudentNumber(Integer studentNumber) {
            this.studentNumber = studentNumber;
        }

        public String getPlaceType() {
            return placeType;
        }

        public void setPlaceType(String placeType) {
            this.placeType = placeType;
        }

        public String getApplication() {
            return application;
        }

        public void setApplication(String application) {
            this.application = application;
        }

        public String getArrangeType() {
            return arrangeType;
        }

        public void setArrangeType(String arrangeType) {
            this.arrangeType = arrangeType;
        }

        public String getPlaceTypeCode() {
            return placeTypeCode;
        }

        public void setPlaceTypeCode(String placeTypeCode) {
            this.placeTypeCode = placeTypeCode;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ClassTimetableVo that = (ClassTimetableVo) o;
            return Objects.equals(id, that.id) &&
                    Objects.equals(placeId, that.placeId) &&
                    Objects.equals(className, that.className) &&
                    Objects.equals(trainingUnit, that.trainingUnit) &&
                    Objects.equals(itemType, that.itemType) &&
                    Objects.equals(courseName, that.courseName) &&
                    Objects.equals(classMaster, that.classMaster) &&
                    Objects.equals(studentNumber, that.studentNumber) &&
                    Objects.equals(placeType, that.placeType) &&
                    Objects.equals(placeTypeCode, that.placeTypeCode) &&
                    Objects.equals(arrangeType, that.arrangeType) &&
                    Objects.equals(application, that.application) &&
                    Objects.equals(color, that.color);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, placeId, className, trainingUnit, itemType, courseName, classMaster,
                    studentNumber, placeType, placeTypeCode, arrangeType, application, color);
        }

        @Override
        public String toString() {
            return "ClassTimetableVo{" +
                    "id='" + id + '\'' +
                    ", placeId='" + placeId + '\'' +
                    ", className='" + className + '\'' +
                    ", trainingUnit='" + trainingUnit + '\'' +
                    ", itemType='" + itemType + '\'' +
                    ", courseName='" + courseName + '\'' +
                    ", classMaster='" + classMaster + '\'' +
                    ", studentNumber=" + studentNumber +
                    ", placeType='" + placeType + '\'' +
                    ", placeTypeCode='" + placeTypeCode + '\'' +
                    ", arrangeType='" + arrangeType + '\'' +
                    ", application='" + application + '\'' +
                    ", color='" + color + '\'' +
                    '}';
        }
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public Map<String, Map<String, ClassTimetableVo>> getClassTimetable() {
        return classTimetable;
    }

    public void setClassTimetable(Map<String, Map<String, ClassTimetableVo>> classTimetable) {
        this.classTimetable = classTimetable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClassPlaceVo that = (ClassPlaceVo) o;
        return Objects.equals(day, that.day) &&
                Objects.equals(week, that.week) &&
                Objects.equals(classTimetable, that.classTimetable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, week, classTimetable);
    }

    @Override
    public String toString() {
        return "ClassPlaceVo{" +
                "day='" + day + '\'' +
                ", week='" + week + '\'' +
                ", classTimetable=" + classTimetable +
                '}';
    }
}

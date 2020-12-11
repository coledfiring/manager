package com.whaty.products.service.businessunit.domain;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * 单个学生报名DTO领域模型
 */
public class EnrollStudentInfParameter implements Serializable {

    private static final long serialVersionUID = 6184135997358253057L;

    /**
     * 学生姓名
     */
    @NotNull
    private String trueName;

    /**
     * 期次id
     */
    @NotNull
    private String classId;

    /**
     * 学生学号
     */
    @NotNull
    private String cardNo;

    /**
     * 性别id
     */
    private String genderId;

    /**
     * 学生手机号
     */
    @NotNull
    private String mobile;

    /**
     * 学生邮箱
     */
    @NotNull
    private String email;

    /**
     * 学生地址
     */
    @NotNull
    private String address;

    /**
     * 职位
     */
    private String positional;

    /**
     * 职称
     */
    @NotNull
    private String professional;

    /**
     * 单位名称
     */
    @NotNull
    private String unitName;

    /**
     * 是否选择食物
     */
    @NotNull
    private boolean chooseFoodFee;

    /**
     * 是否选择资料费
     */
    @NotNull
    private boolean chooseMaterialFee;

    /**
     * 是否选择住宿费
     */
    @NotNull
    private boolean chooseRoomFee;

    /**
     * 食物费
     */
    @NotNull
    private BigDecimal foodFee;

    /**
     * 资料费
     */
    @NotNull
    private BigDecimal materialFee;

    /**
     * 住宿费
     */
    @NotNull
    private BigDecimal roomFee;

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getGenderId() {
        return genderId;
    }

    public void setGenderId(String genderId) {
        this.genderId = genderId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPositional() {
        return positional;
    }

    public void setPositional(String positional) {
        this.positional = positional;
    }

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public boolean isChooseFoodFee() {
        return chooseFoodFee;
    }

    public void setChooseFoodFee(boolean chooseFoodFee) {
        this.chooseFoodFee = chooseFoodFee;
    }

    public boolean isChooseMaterialFee() {
        return chooseMaterialFee;
    }

    public void setChooseMaterialFee(boolean chooseMaterialFee) {
        this.chooseMaterialFee = chooseMaterialFee;
    }

    public boolean isChooseRoomFee() {
        return chooseRoomFee;
    }

    public void setChooseRoomFee(boolean chooseRoomFee) {
        this.chooseRoomFee = chooseRoomFee;
    }

    public BigDecimal getFoodFee() {
        return foodFee;
    }

    public void setFoodFee(BigDecimal foodFee) {
        this.foodFee = foodFee;
    }

    public BigDecimal getMaterialFee() {
        return materialFee;
    }

    public void setMaterialFee(BigDecimal materialFee) {
        this.materialFee = materialFee;
    }

    public BigDecimal getRoomFee() {
        return roomFee;
    }

    public void setRoomFee(BigDecimal roomFee) {
        this.roomFee = roomFee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EnrollStudentInfParameter that = (EnrollStudentInfParameter) o;
        return chooseFoodFee == that.chooseFoodFee &&
                chooseMaterialFee == that.chooseMaterialFee &&
                chooseRoomFee == that.chooseRoomFee &&
                Objects.equals(trueName, that.trueName) &&
                Objects.equals(classId, that.classId) &&
                Objects.equals(cardNo, that.cardNo) &&
                Objects.equals(genderId, that.genderId) &&
                Objects.equals(mobile, that.mobile) &&
                Objects.equals(email, that.email) &&
                Objects.equals(address, that.address) &&
                Objects.equals(positional, that.positional) &&
                Objects.equals(professional, that.professional) &&
                Objects.equals(unitName, that.unitName) &&
                Objects.equals(foodFee, that.foodFee) &&
                Objects.equals(materialFee, that.materialFee) &&
                Objects.equals(roomFee, that.roomFee);
    }

    @Override
    public int hashCode() {

        return Objects.hash(trueName, classId, cardNo, genderId, mobile, email, address, positional, professional,
                unitName, chooseFoodFee, chooseMaterialFee, chooseRoomFee, foodFee, materialFee, roomFee);
    }

    @Override
    public String toString() {
        return "EnrollStudentInfParameter{" +
                "trueName='" + trueName + '\'' +
                ", classId='" + classId + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", genderId='" + genderId + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", positional='" + positional + '\'' +
                ", professional='" + professional + '\'' +
                ", unitName='" + unitName + '\'' +
                ", chooseFoodFee=" + chooseFoodFee +
                ", chooseMaterialFee=" + chooseMaterialFee +
                ", chooseRoomFee=" + chooseRoomFee +
                ", foodFee=" + foodFee +
                ", materialFee=" + materialFee +
                ", roomFee=" + roomFee +
                '}';
    }
}

package com.whaty.framework.aop.operatelog.param;

import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.framework.common.spring.SpringUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 状态机枚举
 *
 * @author weipengsen
 */
public enum StateEnum {

    /**
     * multipartFile
     */
    MULTIPART_FILE(MultipartFile.class, () -> (AbstractParamState) SpringUtil.getBean("multipartFileParamState")),

    /**
     * paramsDataModel
     */
    PARAMS_DATA_MODEL(ParamsDataModel.class, () -> (AbstractParamState) SpringUtil.getBean("paramsDataModelParamState")),

    /**
     * pojo
     */
    POJO(StateEnum.class, () -> (AbstractParamState) SpringUtil.getBean("pojoParamSate")),
    ;

    private Class<?> paramType;

    private Supplier<AbstractParamState> paramStateSupplier;

    StateEnum(Class<?> paramType, Supplier<AbstractParamState> paramStateSupplier) {
        this.paramType = paramType;
        this.paramStateSupplier = paramStateSupplier;
    }

    /**
     * 根据传入的参数类型选择状态机
     * @param originClass
     * @return
     */
    public static AbstractParamState getParamState(Class originClass) {
        return Arrays.stream(values())
                .filter(e -> Objects.nonNull(e.getParamType()))
                .filter(e -> e.getParamType().isAssignableFrom(originClass))
                .findFirst().orElse(POJO)
                .getParamStateSupplier().get();
    }

    public Class<?> getParamType() {
        return paramType;
    }

    public Supplier<AbstractParamState> getParamStateSupplier() {
        return paramStateSupplier;
    }
}

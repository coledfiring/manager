package com.whaty.framework.ucenter.utils;

import com.whaty.domain.bean.SsoUser;
import com.whaty.constant.CommonConstant;
import com.whaty.core.framework.bean.CoreSsoUser;
import com.whaty.framework.exception.UCenterException;
import com.whaty.framework.ucenter.domain.UCenterResult;
import com.whaty.ucenter.oauth2.exception.ApiInvokeFailedException;
import com.whaty.ucenter.oauth2.sdk.bean.UserDto;
import com.whaty.utils.StaticBeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 用户中心工具类
 *
 * @author weipengsen
 */
public class UCenterUtils {

    private static final String ERROR_INFO_MAP = "errorInfoMap";

    private static final Logger logger = LoggerFactory.getLogger(UCenterUtils.class);

    /**
     * 同步用户到用户中心
     * @param loginId
     * @param password
     * @param siteCode
     */
    public static void synchronousUserToUCenter(String loginId, String password, String siteCode) {
        Map<String, Object> userInfo = new HashMap<>(2);
        userInfo.put("loginId", loginId);
        userInfo.put("siteCode", siteCode);
        userInfo.put("password", password);
        synchronousUserToUCenter(Collections.singleton(userInfo), siteCode);
    }

    /**
     * 同步用户到用户中心
     *
     * @param userInfo
     * @throws UCenterException
     */
    public static void synchronousUserToUCenter(Collection<Map<String, Object>> userInfo, String siteCode) {
        List<String> passwords = new LinkedList<>();
        List<UserDto> users = userInfo.stream().peek(e -> passwords.add((String) e.get("password")))
                .map(e -> buildAddUserDto(e, siteCode)).collect(Collectors.toList());
        try {
            UCenterResult result = UCenterResult.convert(StaticBeanUtils.getUCenterService()
                    .batchAddUser(users, true, siteCode));
            handleResponseResult(result, errorIds -> {
                List<String> successIds = users.stream().filter(e -> !errorIds.contains(e.getLoginId()))
                        .map(UserDto::getLoginId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(successIds)) {
                    removeUserFromUCenter(successIds, siteCode);
                }
            });
        } catch (ApiInvokeFailedException e) {
            throw new UCenterException(e);
        }
    }

    /**
     * 构建添加用户的用户中心DTO对象
     *
     * @param userInfo
     * @param siteCode
     * @return
     */
    private static UserDto buildAddUserDto(Map<String, Object> userInfo, String siteCode) {
        if (!userInfo.containsKey("loginId")) {
            throw new IllegalArgumentException("not found argument loginId");
        }
        UserDto user = new UserDto((String) userInfo.get("loginId"), siteCode);
        user.setNickName((String) userInfo.get("loginId"));
        user.setTrueName((String) userInfo.get("trueName"));
        user.setPassword((String) userInfo.get("password"));
        return user;
    }

    /**
     * 从用户中心删除用户
     *
     * @param loginIds
     * @param siteCode
     */
    public static void removeUserFromUCenter(String loginIds, String siteCode) {
        removeUserFromUCenter(Arrays.asList(loginIds.split(CommonConstant.SPLIT_ID_SIGN)), siteCode);
    }

    /**
     * 从用户中心删除用户
     *
     * @param loginIds
     * @param siteCode
     */
    public static void removeUserFromUCenter(List<String> loginIds, String siteCode) {
        try {
            if (!StaticBeanUtils.getUCenterService().deleteUserByAccount(loginIds, siteCode)) {
                throw new UCenterException(String.format("delete account of %s failure %s", siteCode, loginIds));
            }
        } catch (ApiInvokeFailedException e) {
            throw new UCenterException(e);
        }
    }

    /**
     * 更新用户中心的用户
     * @param oldUserInfo
     * @param userInfo
     * @param siteCode
     */
    public static void updateUCenterBatchUser(List<Map<String, Object>> oldUserInfo,
                                              List<Map<String, Object>> userInfo, String siteCode) {
        try {
            Map<String, Map<String, Object>> oldStudentMap = oldUserInfo.stream()
                    .collect(Collectors.toMap(e -> (String) e.get("id"), e -> e));
            List<UserDto> users = userInfo.stream().map(e ->
                buildUpdateUserDto((String) oldStudentMap.get(e.get("id")).get("loginId"), e, siteCode)
            ).collect(Collectors.toList());

            handleResponseResult(UCenterResult
                    .convert(StaticBeanUtils.getUCenterService().batchUpdateUser(users, siteCode)), errorIds -> {
                Map<String, Map<String, Object>> studentMap = userInfo.stream()
                        .collect(Collectors.toMap(e -> (String) e.get("id"), e -> e));
                List<UserDto> backUsers = oldUserInfo.stream().map(e ->
                        buildUpdateUserDto((String) studentMap.get(e.get("id")).get("loginId"), e, siteCode)
                ).collect(Collectors.toList());
                try {
                    StaticBeanUtils.getUCenterService().batchUpdateUser(backUsers, siteCode);
                } catch (ApiInvokeFailedException e) {
                    logger.error("back update user failure", e);
                }
            });
        } catch (ApiInvokeFailedException e) {
            throw new UCenterException(e);
        }
    }

    /**
     * 更新用户中心的用户
     * @param oldUser
     * @param newUser
     * @param siteCode
     */
    public static void updateUCenterSingleUser(SsoUser oldUser, SsoUser newUser, String siteCode) {
        updateUCenterSingleUser(newUser.getTrueName(), oldUser.getLoginId(), newUser.getLoginId(), siteCode);
    }

    /**
     * 更新用户中心的用户
     * @param oldUser
     * @param newUser
     * @param siteCode
     */
    public static void updateUCenterSingleUser(CoreSsoUser oldUser, CoreSsoUser newUser, String siteCode) {
        updateUCenterSingleUser(newUser.getTrueName(), oldUser.getLoginId(), newUser.getLoginId(), siteCode);
    }

    /**
     * 更新单个用户中心
     * @param trueName
     * @param loginId
     * @param newLoginId
     * @param siteCode
     */
    public static void updateUCenterSingleUser(String trueName, String loginId, String newLoginId, String siteCode) {
        try {
            UserDto user = new UserDto();
            user.setTrueName(trueName);
            user.setNickName(newLoginId);
            if (!newLoginId.equals(loginId)) {
                user.setNewLoginId(newLoginId);
            }
            user.setLoginId(loginId);
            handleResponseResult(UCenterResult.convert(StaticBeanUtils.getUCenterService()
                    .batchUpdateUser(Collections.singletonList(user), siteCode)), null);
        } catch (ApiInvokeFailedException e) {
            throw new UCenterException(e);
        }
    }


    /**
     * 修改用户中心密码
     * @param userInfo
     * @param oldUserInfo
     * @param siteCode
     */
    public static void updatePasswordForUCenterUser(List<Map<String, Object>> userInfo,
                                                    List<Map<String, Object>> oldUserInfo, String siteCode) {
        List<String> loginIdList = new ArrayList<>();
        List<String> passwordList = userInfo.stream().peek(e -> loginIdList.add((String) e.get("loginId")))
                .map(e -> (String) e.get("password")).collect(Collectors.toList());
        try {
            handleResponseResult(UCenterResult.convert(StaticBeanUtils.getUCenterService()
                    .batchResetPassword(loginIdList, passwordList, true, siteCode)), errorIds -> {
                List<String> backLoginIdList = new ArrayList<>();
                List<String> backPasswordList = oldUserInfo.stream().filter(e -> !errorIds.contains(e.get("loginId")))
                        .peek(e -> backLoginIdList.add((String) e.get("LoginId")))
                        .map(e -> (String) e.get("loginId")).collect(Collectors.toList());
                try {
                    StaticBeanUtils.getUCenterService()
                            .batchResetPassword(backLoginIdList, backPasswordList, true, siteCode);
                } catch (ApiInvokeFailedException e) {
                    logger.error("back reset user password failure", e);
                }
            });
        } catch (ApiInvokeFailedException e) {
            throw new UCenterException(e);
        }
    }

    /**
     * 处理返回的结果
     * @param result
     * @param callback
     */
    private static void handleResponseResult(UCenterResult result, Consumer<List<String>> callback) {
        if (!result.getSuccess()) {
            throw new UCenterException(result.getMessage());
        }
        if (result.getData() != null && result.getData().get(ERROR_INFO_MAP) != null) {
            Map<String, String> errorInfoMap = (Map<String, String>) result.getData().get(ERROR_INFO_MAP);
            List<String> errorIds = errorInfoMap.entrySet().stream().map(Map.Entry::getKey)
                    .sorted().collect(Collectors.toList());
            if (callback != null && CollectionUtils.isNotEmpty(errorIds)) {
                callback.accept(errorIds);
            }
            StringBuilder message = new StringBuilder("同步用户中心出现以下问题：");
            errorInfoMap.forEach((k, v) -> message.append(String.format("'%s'用户名%s", k, v)));
            throw new UCenterException(message.toString());
        }
    }

    /**
     * 构建更新用户的userDto
     * @param oldLoginId
     * @param studentInfo
     * @param siteCode
     * @return
     */
    private static UserDto buildUpdateUserDto(String oldLoginId, Map<String, Object> studentInfo, String siteCode) {
        if (StringUtils.isBlank(oldLoginId)) {
            throw new IllegalArgumentException("not found argument oldLoginId");
        }
        UserDto user = new UserDto(oldLoginId, siteCode);
        if (!oldLoginId.equals(studentInfo.get("loginId"))) {
            user.setNewLoginId((String) studentInfo.get("loginId"));
        }
        user.setNickName((String) studentInfo.get("loginId"));
        user.setTrueName((String) studentInfo.get("trueName"));
        return user;
    }

    /**
     * 更新单个用户中心的密码
     * @param loginId
     * @param password
     * @param siteCode
     */
    public static void updatePasswordForUCenterSingleUser(String loginId, String password, String siteCode) {
        try {
            UCenterResult result = UCenterResult.convert(StaticBeanUtils.getUCenterService()
                    .resetPassword(loginId, password, true, siteCode));
            if (!result.getSuccess()) {
                throw new UCenterException(result.getMessage());
            }
        } catch (ApiInvokeFailedException e) {
            throw new UCenterException(e);
        }
    }
}

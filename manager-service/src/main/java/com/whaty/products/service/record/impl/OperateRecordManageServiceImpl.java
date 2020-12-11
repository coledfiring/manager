package com.whaty.products.service.record.impl;

import com.whaty.framework.aop.notice.annotation.LogAndNotice;
import com.whaty.framework.aop.operatelog.constant.OperateRecordConstant;
import com.whaty.framework.exception.UncheckException;
import com.whaty.util.CommonUtils;
import com.whaty.util.TycjCollectionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 操作日志管理查看服务类
 *
 * @author weipengsen
 */
@Lazy
@Service("operateRecordManageService")
public class OperateRecordManageServiceImpl {

    private final static String PARAM_SITE = "site";

    private final static String PARAM_DATE = "date";

    private final static String PARAM_NAME = "name";

    /**
     * 列举操作日志文件
     *
     * @param params
     * @return
     */
    @LogAndNotice("列举操作日志文件")
    public List<Map<String, Object>> listFile(Map<String, Object> params) {
        File root = new File(CommonUtils.getRealPath(OperateRecordConstant.CSV_FILE_PATH));
        return this.searchFiles(root.listFiles(), this.buildPredicate(params));
    }

    /**
     * 构建过滤用的predicate
     *
     * @param params
     * @return
     */
    private Predicate<File> buildPredicate(Map<String, Object> params) {
        return f -> {
            // 站点条件
            if (CollectionUtils.isNotEmpty((List<String>) params.get(PARAM_SITE))) {
                boolean inSite = false;
                for (String siteCode : ((List<String>) params.get(PARAM_SITE))) {
                    if (inSite |= f.getName().contains(siteCode)) {
                        break;
                    }
                }
                if (!inSite) {
                    return false;
                }
            }
            // 时间条件
            if (CollectionUtils.isNotEmpty((List) params.get(PARAM_DATE))) {
                String operateDate = this.extractOperateDate(f.getName());
                String start = (String) ((List) params.get(PARAM_DATE)).get(0);
                String end = (String) ((List) params.get(PARAM_DATE)).get(1);
                if (operateDate.compareTo(start) < 0 || end.compareTo(operateDate) < 0) {
                    return false;
                }
            }
            // 名称模糊
            if (StringUtils.isNotBlank((String) params.get(PARAM_NAME))) {
                if (!f.getName().contains((String) params.get(PARAM_NAME))) {
                    // 文件内字符串
                    return this.searchStringFromFile(f, (String) params.get(PARAM_NAME));
                }
            }
            return true;
        };
    }

    /**
     * 从文件中查询字符串
     * @param file
     * @param target
     * @return
     */
    private boolean searchStringFromFile(File file, String target) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.lines().parallel().filter(StringUtils::isNotBlank)
                    .anyMatch(e -> CommonUtils.kmpIndexOf(e, target) >= 0);
        } catch (IOException e) {
            throw new UncheckException(e);
        }
    }

    /**
     * 从文件名中提取操作日期
     *
     * @param name
     * @return
     */
    private String extractOperateDate(String name) {
        return name.substring(name.indexOf("_", name.indexOf("_") + 1) + 1, name.lastIndexOf("_"));
    }

    /**
     * 搜索符合规则的日志文件
     *
     * @param foundFiles
     * @return
     */
    private List<Map<String, Object>> searchFiles(File[] foundFiles, Predicate<File> filter) {
        if (ArrayUtils.isEmpty(foundFiles)) {
            return null;
        }
        List<File> validFile = Arrays.stream(foundFiles).parallel().filter(File::isFile)
                .filter(filter).collect(Collectors.toList());
        List<Map<String, Object>> children = Arrays.stream(foundFiles).filter(File::isDirectory).flatMap(d -> {
            List<Map<String, Object>> subFiles = this.searchFiles(d.listFiles(), filter);
            return CollectionUtils.isEmpty(subFiles) ? null : subFiles.stream();
        }).collect(Collectors.toList());
        children.addAll(validFile.stream().map(this::convertToVO).collect(Collectors.toList()));
        return children.stream()
                .sorted(Comparator.comparing(e -> (String) e.get("name"))).collect(Collectors.toList());
    }

    /**
     * 将文件转换成展示形式
     * @param file
     * @return
     */
    private Map<String, Object> convertToVO(File file) {
        return TycjCollectionUtils.map("name", file.getName(), "size", file.length(), "url",
                CommonUtils.getBasicUrl() + CommonUtils.getRequest().getContextPath() + "/" +
                        CommonUtils.getRelativePath(file.getPath()));
    }
}

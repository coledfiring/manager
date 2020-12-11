package com.whaty.file.domain.bean;


import com.whaty.domain.bean.AbstractSiteBean;
import com.whaty.file.grid.constant.PrintConstant;
import com.whaty.file.template.constant.TemplateConstant;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 打印模板
 */
@Data
@Entity(name = "PePrintTemplate")
@Table(name = "pe_print_template")
public class PePrintTemplate extends AbstractSiteBean {

    private static final long serialVersionUID = -2548533809331253655L;
    /**
     * 主键
     */
    @Id
    private String id;
    /**
     * 标题
     */
    @Column(name = "TITLE")
    private String title;
    /**
     * 模版编码
     */
    @Column(name = "template_code")
    private String templateCode;
    /**
     * 路径
     */
    @Column(name = "PATH")
    private String path;
    /**
     * 样本路径
     */
    @Column(name = "THUMB")
    private String thumb;
    /**
     * 学校代码
     */
    @Column(name = "TEAM")
    private String team;
    /**
     * 学生人数
     */
    @Column(name = "NUM")
    private String num;
    /**
     * 自定义变量
     */
    @Column(name = "VARIABLE")
    private String variable;
    /**
     * 时间
     */
    @Column(name = "MODIFY_DATE")
    private Date modifyDate;
    /**
     * 标志
     */
    @Column(name = "CODE")
    private String code;
    /**
     * 默认路径
     */
    @Column(name = "DEFAULT_PATH")
    private String defaultPath;
    /**
     * 查询模板数据使用的sql
     */
    @Column(name = "SEARCH_SQL")
    private String searchSql;
    /**
     * 占位符
     */
    @Transient
    private List<PePrintTemplateSign> pePrintTemplateSignList;
    /**
     * 模板查询数据的方式
     */
    @Column(name = "SEARCH_TYPE")
    private String searchType;

    /**
     * 每个word存放的单位数量
     */
    @Column(name = "PRINT_PAGE_SIZE")
    private Integer printPageSize;
    @Column(
            name = "site_code"
    )
    private String siteCode;

    @Transient
    private List<String> signs;

    /**
     * 编码非法字符
     * @param origin
     * @return
     */
    public String encodeIllegalSign(String origin) {
        if (this.code.equals(PrintConstant.PRINT_TEMPLATE_TYPE_FREEMARKER_TO_PDF)) {
            return IllegalHtmlSign.encode(origin);
        }
        return origin;
    }

    /**
     * 占位符是否存在
     * @param key
     * @return
     */
    public boolean signExists(String key) {
        return this.signs.contains(key);
    }

    /**
     * 构建占位符集
     */
    public void buildSignKeySet() {
        this.signs = this.getPePrintTemplateSignList().stream()
                .map(e -> e.getSign().replace(TemplateConstant.SIGN_PREFIX, "")
                        .replace(TemplateConstant.SIGN_SUFFIX, ""))
                .collect(Collectors.toList());
    }

    enum IllegalHtmlSign {

        LT("<", "&lt;"),

        GT(">", "&gt;"),

        QUOT("'", "&quot;"),

        APOS("\"", "&apos; "),

        DIVIDE("+", "&divide;"),
        /**
         * 必须放到最后
         */
        AMP("&", "&amp;"),
        ;

        private String origin;

        private String target;

        IllegalHtmlSign(String origin, String target) {
            this.origin = origin;
            this.target = target;
        }

        /**
         * 替换特殊字符
         * @param origin
         * @return
         */
        static String encode(String origin) {
            for (IllegalHtmlSign illegalHtmlSign : values()) {
                origin = origin.replace(illegalHtmlSign.getOrigin(), illegalHtmlSign.getTarget());
            }
            return origin;
        }

        public String getOrigin() {
            return origin;
        }

        public String getTarget() {
            return target;
        }
    }

}
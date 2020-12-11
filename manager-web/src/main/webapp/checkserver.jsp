<%@ page language="java"  pageEncoding="utf-8"%>
<%@ page import="com.alibaba.druid.pool.DruidDataSource,com.whaty.cache.service.RedisCacheService" %>
<%@page import="com.whaty.dao.GeneralDao" %>
<%@ page import="com.whaty.framework.common.spring.SpringUtil" %>
<%@ page import="javax.sql.DataSource" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.whaty.framework.config.BasicApplicationContext" %>
<%@ page import="com.whaty.core.framework.bean.PeWebSite" %>
<%@page language="java" %>

<header>
    <title>各站点项目检测页面</title>
    <meta http-equiv="Content-Type" content="text/html charset=GBK">
</header>
<body>
<div>
    <p style="font-weight:bold;">项目检测页面 </p>
</div>
<div>
    <%
        //获取所有的站点信息
        Map<String, PeWebSite> siteMap = BasicApplicationContext.CODE_KEY_SITE_MAP;

        String testSql = "select 2";
        ResultSet rs = null;
        Connection conn = null;
        StringBuilder sb = new StringBuilder();
        boolean isError = false;

        //循环测试所有站点的数据库连接数
        for (PeWebSite site : siteMap.values()) {
            try {
                DruidDataSource cpds = (DruidDataSource) SpringUtil.getBean(site.getCode());
                if (cpds != null) {
                    sb.append("siteCode:" + site.getCode() +
                            " druid----ALL:" + cpds.getPoolingCount() +
                            "          USE:" + cpds.getActiveCount() +
                            "          FREE:" + (cpds.getPoolingCount() - cpds.getActiveCount()) +
                            "          hasGotCount:" + cpds.getConnectCount() +"<br/>");
                } else {
                    sb.append("siteCode:" + site.getCode() + "   no ComboPooledDataSource!<br/>");
                }
            } catch (Exception e) {
                sb.append("<font color=red>siteCode:" + site.getCode() + "    c3p0 is bad!</font><br/>");
                isError = true;
            }
        }
        sb.append("<br/>");
        //循环测试所有站点的dbpool
        for (PeWebSite site : siteMap.values()) {
            try {
                DataSource combo = (DataSource) SpringUtil.getBean(site.getCode());
                if (combo != null) {
                    conn = combo.getConnection();
                    PreparedStatement ps = conn.prepareStatement(testSql);
                    rs = ps.executeQuery();
                    sb.append("siteCode:" + site.getCode() + "   dbpool is ok!<br/>");
                }

            } catch (Exception e) {
                sb.append("<font color=red>siteCode:" + site.getCode() + "    dbpool is bad!</font><br/>");
                isError = true;
            } finally {
                if (conn != null) {
                    conn.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }
        }
        sb.append("<br/>");
        //测试hibernate是否正常
        try {
            GeneralDao generalDao = getGeneralService();
            List list = generalDao.getBySQL(testSql);
            if (list.size() == 0) {
                throw new Exception("<font color=red>hibernate 异常！</font><br/>");
            }
            sb.append("hibernate is ok!<br/>");
        } catch (Exception e) {
            sb.append("<font color=red>hibernate is bad!</font><br/>");
            isError = true;
        }
        sb.append("<br/>");
        //测试缓存是否正常
        try {
            RedisCacheService redisCacheService = (RedisCacheService) SpringUtil.getBean("redisCacheService");
            String cacheName = "checkservercache";
            redisCacheService.putToCache(cacheName, "test");
            Object object = redisCacheService.getFromCache(cacheName);
            if (object != null) {
                sb.append("Cache is ok!<br/>");
            } else {
                sb.append("<font color=red>cache is bad!</font><br/>");
            }

        } catch (Exception e) {
            isError = true;
            sb.append("<font color=red>cache is bad!</font><br/>");
        }

        if (isError) {
            throw new Exception("checkerror:" + sb.toString());
        }

        out.print(sb.toString());
    %>
    <%!
        public GeneralDao getGeneralService() {
            GeneralDao generalDao = (GeneralDao) SpringUtil.getBean("generalDao");
            return generalDao;
        }

    %>
</div>
</body>
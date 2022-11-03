package com.wangshanhai.dbdoc.utils;

/**
 * The {@code RegUtils} class
 *
 * @author Fly.Sky
 * @since 2022/11/3 8:59
 */
public class RegUtils {
    /**
     * <p>获取数据库类型</p>
     * @apiNote
     *
     * @param jdbcUrl
     *
     * @return java.lang.String
     * @author Fly.Sky
     * @since 2022/11/3 9:25
     * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public static String getDBType(String jdbcUrl){
        return jdbcUrl.replaceAll("\\?[0-9A-Za-z=&_-]+","").split(":")[1];
    }
    /**
     * <p>获取数据库主机信息</p>
     * @apiNote
     *
     * @param jdbcUrl
     *
     * @return java.lang.String
     * @author Fly.Sky
     * @since 2022/11/3 9:26
     * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public static String getDBHost(String jdbcUrl){
        switch (getDBType(jdbcUrl)){
            case "sqlserver":return jdbcUrl.replaceAll("\\?[0-9A-Za-z=&_-]+","").replaceAll("jdbc:[a-zA-Z0-9]+://","").split(";")[0];
            case "mysql":return jdbcUrl.replaceAll("\\?[0-9A-Za-z=&_-]+","").replaceAll("jdbc:[a-zA-Z0-9]+://","").split("/")[0];
            case "kingbase8":return jdbcUrl.replaceAll("\\?[0-9A-Za-z=&_-]+","").replaceAll("jdbc:[a-zA-Z0-9]+://","").split("/")[0];
            case "db2":return jdbcUrl.replaceAll("\\?[0-9A-Za-z=&_-]+","").replaceAll("jdbc:[a-zA-Z0-9]+://","").split("/")[0];
            case "oracle": return jdbcUrl.replace("jdbc:oracle:thin:@","");
            default:return jdbcUrl.replaceAll("\\?[0-9A-Za-z=&_-]+","").replaceAll("jdbc:[a-zA-Z0-9]+://","");
        }
    }
}

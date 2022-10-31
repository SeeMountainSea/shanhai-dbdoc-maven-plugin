package com.wangshanhai.dbdoc.utils;

import com.wangshanhai.dbdoc.entity.ColumnInfo;
import com.wangshanhai.dbdoc.entity.TableInfo;
import com.wangshanhai.dbdoc.form.DBInfo;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code JDBCTest} class
 *
 * @author Fly.Sky
 * @since 2022/10/26 14:24
 */
public class JDBCUtils {
    public static void loadJar(String jarPath) throws Exception {
        File jarFile = new File(jarPath);
        if(!jarFile.exists()){
            throw new RuntimeException("This Path No Find Driver File!");
        }
        // 从URLClassLoader类中获取类所在文件夹的方法，jar也可以认为是一个文件夹
        Method method  = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);;
        //获取方法的访问权限以便写回
        boolean accessible = method.isAccessible();
        method.setAccessible(true);
        // 获取系统类加载器
        URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        URL url = jarFile.toURI().toURL();
        method.invoke(classLoader, url);
    }
    /**
     * <p>通过指定驱动jar文件创建数据库连接</p>
     * @apiNote
     *
     * @param dbInfo
     * @param driverJarPath
     *
     * @return java.sql.Connection
     * @author Fly.Sky
     * @since 2022/10/27 16:47
     * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public static Connection createConnection(DBInfo dbInfo,String driverJarPath) throws Exception {
        loadJar(driverJarPath);
        return createConnection(dbInfo);
    }
    /**
     * <p>创建链接对象</p>
     * @apiNote
     *
     * @param dbInfo
     *
     * @return java.sql.Connection
     * @author Fly.Sky
     * @since 2022/10/26 17:21
     * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    public static Connection createConnection(DBInfo dbInfo) throws Exception {
        Class.forName(dbInfo.getDriver());
        return DriverManager.getConnection(dbInfo.getJdbcUrl(), dbInfo.getUser(), dbInfo.getPassword());
    }
    /**
     * <p>获取表的所有字段信息</p>
     *
     * @param connection 连接对象
     * @param tableName  表名称
     * @return java.util.List<ColumnInfo>
     * @apiNote
     * @author Fly.Sky
     * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
     * @since 2022/10/26 15:15
     */
    public static List<ColumnInfo> queryAllColumnsInfo(Connection connection, String tableName) throws Exception {
        DatabaseMetaData metaData = connection.getMetaData();
        List<ColumnInfo> resp = new ArrayList<>();
        ResultSet colRet = metaData.getColumns(connection.getCatalog(), connection.getSchema(), tableName, "%");
        while (colRet.next()) {
            ColumnInfo.ColumnInfoBuilder builder = ColumnInfo.builder();
            builder.name(colRet.getString("COLUMN_NAME"));
            builder.dataType(colRet.getString("DATA_TYPE"));
            builder.size(colRet.getString("COLUMN_SIZE"));
            builder.remark(colRet.getString("REMARKS"));
            builder.decimalDigits(colRet.getString("DECIMAL_DIGITS"));
            builder.numPrecRadix(colRet.getString("NUM_PREC_RADIX"));
            builder.columnDef(colRet.getString("COLUMN_DEF"));
            builder.isAutoincrement(colRet.getString("IS_AUTOINCREMENT"));
            builder.typeName(colRet.getString("TYPE_NAME"));
            builder.tableCat(colRet.getString("TABLE_CAT"));
            builder.tableSchema(colRet.getString("TABLE_SCHEM"));
            builder.nullable(colRet.getString("NULLABLE"));
            builder.tableName(tableName);
            builder.bufferLength(colRet.getString("BUFFER_LENGTH"));
            builder.sqlDataType(colRet.getString("SQL_DATA_TYPE"));
            builder.sqlDatetimeSub(colRet.getString("SQL_DATETIME_SUB"));
            builder.charOctetLength(colRet.getString("CHAR_OCTET_LENGTH"));
            builder.ordinalPosition(colRet.getString("ORDINAL_POSITION"));
            builder.isNullable(colRet.getString("IS_NULLABLE"));
            resp.add(builder.build());
        }
        return resp;
    }

    /**
     * <p>获取全量表名</p>
     *
     * @param connection 连接对象
     * @return java.util.List<TableInfo>
     * @apiNote
     * @author Fly.Sky
     * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
     * @since 2022/10/26 15:10
     */
    public static List<TableInfo> queryAllTablesInfo(Connection connection) throws Exception {
        DatabaseMetaData metaData = connection.getMetaData();
        List<TableInfo> resp = new ArrayList<>();
        //获取数据库下面所有表
        ResultSet tables = metaData.getTables(connection.getCatalog(), metaData.getConnection().getSchema(), "%", new String[]{"TABLE"});
        while (tables.next()) {
            TableInfo.TableInfoBuilder tableInfoBuilder = TableInfo.builder();
            // 列的个数
            ResultSetMetaData resultSetMetaData = tables.getMetaData();
            tableInfoBuilder.tableName(tables.getString("TABLE_NAME"));
            for (int i = 1; i < resultSetMetaData.getColumnCount(); i++) {
                switch (resultSetMetaData.getColumnName(i)) {
                    case "TABLE_SCHEM":
                        tableInfoBuilder.tableSchema(tables.getString(i));
                        break;
                    case "REMARKS":
                        tableInfoBuilder.tableRemarks(tables.getString(i));
                        break;
                    default:
                        ;
                }
            }
            resp.add(tableInfoBuilder.build());
        }
        return resp;
    }
}

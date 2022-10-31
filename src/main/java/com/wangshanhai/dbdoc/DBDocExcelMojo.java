package com.wangshanhai.dbdoc;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.wangshanhai.dbdoc.entity.ColumnInfo;
import com.wangshanhai.dbdoc.entity.DocInfo;
import com.wangshanhai.dbdoc.entity.TableInfo;
import com.wangshanhai.dbdoc.form.DBInfo;
import com.wangshanhai.dbdoc.utils.JDBCUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code DBDocExcelMojo} class
 *
 * @author Fly.Sky
 * @since 2022/10/27 16:27
 */
@Mojo(name = "generateExcelDoc")
public class DBDocExcelMojo extends AbstractMojo {
    @Parameter(name = "jdbcUrl")
    private String jdbcUrl;
    @Parameter(name = "user")
    private String user;
    @Parameter(name = "password")
    private String password;
    @Parameter(name = "driver")
    private String driver;
    @Parameter(name = "driverFilePath")
    private String driverFilePath;
    @Parameter(name = "outPath")
    private String outPath;
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String filePath=outPath+"DBDoc"+System.currentTimeMillis()+".xlsx";
        this.getLog().info("[ShanHaiDBDoc]-[Begin Generate DBDoc to Excel]-outPath:"+filePath);
        DBInfo dbInfo= DBInfo.builder()
                .jdbcUrl(jdbcUrl)
                .user(user)
                .password(password)
                .driver(driver)
                .build();
        try {
            Connection connection =   JDBCUtils.createConnection(dbInfo,driverFilePath);
            List<TableInfo> tableInfos= JDBCUtils.queryAllTablesInfo(connection);
            List<ColumnInfo> allColumnInfos=new ArrayList<>();
            for(TableInfo t:tableInfos){
                allColumnInfos.addAll(JDBCUtils.queryAllColumnsInfo(connection,t.getTableName()));
            }
            File f = new File(filePath);

            WriteSheet writeDbInfoSheet = EasyExcel.writerSheet(0, "山海DBDoc模型概况").head(DocInfo.class).build();
            WriteSheet writeTableInfoSheet = EasyExcel.writerSheet(1, "数据模型清单").head(TableInfo.class).build();
            WriteSheet writeColumnInfoSheet = EasyExcel.writerSheet(2, "数据模型明细").head(ColumnInfo.class).build();
            ExcelWriter excelWriter =EasyExcel.write(f).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
            excelWriter.write(formatDBInfo(dbInfo,tableInfos,allColumnInfos),writeDbInfoSheet);
            excelWriter.write(tableInfos,writeTableInfoSheet);
            excelWriter.write(allColumnInfos,writeColumnInfoSheet);
            excelWriter.finish();
        }catch (Exception e){
            this.getLog().error("[ShanHaiDBDoc]-[Generate DBDoc Error]-msg:"+e.getMessage());
        }
        this.getLog().info("[ShanHaiDBDoc]-[Generate DBDoc End]-outPath:"+filePath);
    }
    /**
     * <p>格式化信息</p>
     * @apiNote
     *
     * @param dbInfo 数据库
     * @param tableInfos 数据表
     * @param allColumnInfos  数据列
     *
     * @return java.util.List<com.wangshanhai.dbdoc.entity.DocInfo>
     * @author Fly.Sky
     * @since 2022/10/28 9:21
     * @update [序号][日期YYYY-MM-DD] [更改人姓名][变更描述]
     */
    private List<DocInfo> formatDBInfo(DBInfo dbInfo, List<TableInfo> tableInfos,List<ColumnInfo> allColumnInfos){
        List<DocInfo> docInfos=new ArrayList<>();
        DocInfo docInfo=new DocInfo();
        docInfo.setDbHost(dbInfo.getJdbcUrl().replaceAll("\\?[0-9A-Za-z=&_-]+","").replaceAll("[A-Za-z:]+//",""));
        docInfo.setDbUser(dbInfo.getUser());
        docInfo.setGenerateTime(DateUtil.now());
        docInfo.setTablesNum(tableInfos.size());
        docInfo.setDbName(allColumnInfos.get(0).getTableCat());
        docInfo.setSchemaName(allColumnInfos.get(0).getTableSchema());
        docInfo.setDbType(dbInfo.getJdbcUrl().replaceAll("\\?[0-9A-Za-z=&_-]+","").split(":")[1]);
        docInfos.add(docInfo);
        return docInfos;
    }

}

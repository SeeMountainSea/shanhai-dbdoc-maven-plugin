package com.wangshanhai.dbdoc;

import cn.hutool.core.date.DateUtil;
import com.wangshanhai.dbdoc.entity.ColumnInfo;
import com.wangshanhai.dbdoc.entity.DocInfo;
import com.wangshanhai.dbdoc.entity.TableInfo;
import com.wangshanhai.dbdoc.form.DBInfo;
import com.wangshanhai.dbdoc.utils.JDBCUtils;
import com.wangshanhai.dbdoc.utils.RegUtils;
import com.wangshanhai.dbdoc.utils.WordUtil;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The {@code DBDocWordMojo} class
 *
 * @author Fly.Sky
 * @since 2022/10/27 16:27
 */
@Mojo(name = "generateWordDoc")
public class DBDocWordMojo extends AbstractMojo {
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
        String filePath=outPath+"DBDoc"+System.currentTimeMillis()+".docx";
        this.getLog().info("[ShanHaiDBDoc]-[Begin Generate DBDoc to Word]-outPath:"+filePath);
        DBInfo dbInfo= DBInfo.builder()
                .jdbcUrl(jdbcUrl)
                .user(user)
                .password(password)
                .driver(driver)
                .build();
        try {
            Connection connection =   JDBCUtils.createConnection(dbInfo,driverFilePath);
            List<TableInfo> tableInfos= JDBCUtils.queryAllTablesInfo(connection);
            XWPFDocument wordDoc= createDoc();
            createDBInfo(wordDoc,dbInfo,tableInfos,connection);
            int index=1;
            for(TableInfo t:tableInfos){
                List<ColumnInfo> allColumnInfos=JDBCUtils.queryAllColumnsInfo(connection,t.getTableName());
                createTable(index,wordDoc,t.getTableName(),t.getTableRemarks(),allColumnInfos);
                index++;
            }
            outputDoc(wordDoc);
        }catch (Exception e){
            e.printStackTrace();
            this.getLog().error("[ShanHaiDBDoc]-[Generate DBDoc Error]-msg:"+e.getMessage());
        }
        this.getLog().info("[ShanHaiDBDoc]-[Generate DBDoc End]-outPath:"+filePath);
    }
    /**
     * <p>???????????????</p>
     * @apiNote
     *
     * @param dbInfo ?????????
     * @param tableInfos ?????????
     *
     * @return java.util.List<com.wangshanhai.dbdoc.entity.DocInfo>
     * @author Fly.Sky
     * @since 2022/10/28 9:21
     * @update [??????][??????YYYY-MM-DD] [???????????????][????????????]
     */
    private void createDBInfo(XWPFDocument doc,DBInfo dbInfo, List<TableInfo> tableInfos,Connection connection) throws Exception {
        List<DocInfo> docInfos=new ArrayList<>();
        DocInfo docInfo=new DocInfo();
        docInfo.setDbHost(RegUtils.getDBHost(dbInfo.getJdbcUrl()));
        docInfo.setDbUser(dbInfo.getUser());
        docInfo.setGenerateTime(DateUtil.now());
        docInfo.setTablesNum(tableInfos.size());
        docInfo.setDbName(connection.getCatalog());
        docInfo.setSchemaName(connection.getSchema());
        docInfo.setDbType(RegUtils.getDBType(dbInfo.getJdbcUrl()));
        docInfos.add(docInfo);
        //???????????????
        WordUtil.setParagraph(doc,"????????????" , "??????", 12, 10, true, ParagraphAlignment.LEFT,1);
        String [] headers=new String[]{"???????????????","???????????????","???????????????","Schema","????????????","?????????"};
        List<String> nameList = new ArrayList(Arrays.asList(headers));
        //?????????
        int colTotalCount = nameList.size();
        //?????????
        int dataRowCount = docInfos.size();
        //????????????
        XWPFTable xTable = doc.createTable(1, colTotalCount);
        //????????????
        WordUtil.setTableBolder(xTable, 10, "000000");
        // ??????????????????
        int i = 0;
        for (int j = 0; j < colTotalCount; j++) {
            WordUtil.setCellText(xTable.getRow(i).getCell(j), nameList.get(j), 2000, "??????", 10, false);
        }
        // ??????????????????
        XWPFTableRow row = xTable.insertNewTableRow(1);
        row.setHeight(300);
        docInfo=docInfos.get(0);
        for (int j = 0; j < colTotalCount; j++) {
            XWPFTableCell cell = row.createCell();
            XWPFRun run;
            run = WordUtil.setTableCellStyle(cell, ParagraphAlignment.CENTER, "????????????", 9, false);
            switch (j){
                case 0:run.setText(docInfo.getDbType()); break;
                case 1:run.setText(docInfo.getDbHost()); break;
                case 2:run.setText(docInfo.getDbUser()); break;
                case 3:run.setText(docInfo.getSchemaName()); break;
                case 4:run.setText(docInfo.getDbName()); break;
                case 5:run.setText(String.valueOf(docInfo.getTablesNum())); break;
                default:break;
            }
        }

    }
    /**
     * <p>??????Word</p>
     * @apiNote
     *
     *
     * @return org.apache.poi.xwpf.usermodel.XWPFDocument
     * @author Fly.Sky
     * @since 2022/10/28 10:53
     * @update [??????][??????YYYY-MM-DD] [???????????????][????????????]
     */
    public  XWPFDocument createDoc() throws IOException {
        XWPFDocument doc = new XWPFDocument();
        //????????????
//        WordUtil.createDefaultFooter(doc);
        //???????????????
        WordUtil.setMargin(doc, 1000, 1000, 500, 100);
        //????????????
        WordUtil.setParagraph(doc, "??????DBDoc??????????????????", "??????", 20, 10, true, ParagraphAlignment.CENTER,1);
        //???????????????
        WordUtil.setParagraph(doc, "???????????????"+DateUtil.now(), "??????", 10, 10, false, ParagraphAlignment.RIGHT,-2);
        return doc;
    }
    /**
     * <p>????????????</p>
     * @apiNote
     *
     * @param doc
     *
     * @author Fly.Sky
     * @since 2022/10/28 11:17
     * @update [??????][??????YYYY-MM-DD] [???????????????][????????????]
     */
    public  void outputDoc( XWPFDocument doc) throws IOException {
        String filePath=outPath+"DBDoc"+System.currentTimeMillis()+".docx";
        doc.write(new FileOutputStream(new File(filePath)));
    }
    /**
     * <p>????????????????????????</p>
     * @apiNote
     *
     * @param doc Word
     * @param tableName DB??????
     * @param allColumnInfos  DB?????????
     *
     * @author Fly.Sky
     * @since 2022/10/28 10:54
     * @update [??????][??????YYYY-MM-DD] [???????????????][????????????]
     */
    public  void createTable(int index,XWPFDocument doc,String tableName,String tableDesc,List<ColumnInfo> allColumnInfos) throws IOException {
        //???????????????
        WordUtil.setParagraph(doc,index+"."+tableDesc+"("+tableName+")" , "??????", 12, 10, true, ParagraphAlignment.LEFT,1);
        String [] headers=new String[]{"?????????","????????????","????????????","????????????","??????????????????","????????????","???????????????","???????????????"};
        List<String> nameList = new ArrayList(Arrays.asList(headers));
        //?????????
        int colTotalCount = nameList.size();
        //?????????
        int dataRowCount = allColumnInfos.size();
        //????????????
        XWPFTable xTable = doc.createTable(1, colTotalCount);
        //????????????
        WordUtil.setTableBolder(xTable, 10, "000000");
        // ??????????????????
        int i = 0;
        for (int j = 0; j < colTotalCount; j++) {
            WordUtil.setCellText(xTable.getRow(i).getCell(j), nameList.get(j), 2000, "??????", 10, false);
        }
        // ??????????????????
        i++;
        for (int z = i; z < dataRowCount; z++) {
            XWPFTableRow row = xTable.insertNewTableRow(z);
            row.setHeight(300);
            ColumnInfo columnInfo= allColumnInfos.get(z-1);
            for (int j = 0; j < colTotalCount; j++) {
                XWPFTableCell cell = row.createCell();
                XWPFRun run;
                run = WordUtil.setTableCellStyle(cell, ParagraphAlignment.CENTER, "????????????", 9, false);
                switch (j){
                    case 0:run.setText(columnInfo.getName()); break;
                    case 1:run.setText(columnInfo.getRemark()); break;
                    case 2:run.setText(columnInfo.getTypeName()); break;
                    case 3:run.setText(columnInfo.getSize()); break;
                    case 4:run.setText(columnInfo.getNullable()); break;
                    case 5:run.setText(columnInfo.getDecimalDigits()); break;
                    case 6:run.setText(columnInfo.getColumnDef()); break;
                    case 7:run.setText(columnInfo.getIsAutoincrement()); break;
                    default:break;
                }
            }
        }
    }
}

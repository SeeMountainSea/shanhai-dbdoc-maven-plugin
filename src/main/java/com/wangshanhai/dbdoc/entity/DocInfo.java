package com.wangshanhai.dbdoc.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.enums.BooleanEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The {@code DocInfo} class
 *
 * @author Fly.Sky
 * @since 2022/10/26 14:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@HeadFontStyle(fontHeightInPoints = 11,bold = BooleanEnum.FALSE,fontName ="微软雅黑")
public class DocInfo {
    /**
     *数据库类型
     */
    @ExcelProperty("数据库类型")
    private String dbType;
    /**
     *数据库实例
     */
    @ExcelProperty("数据库实例")
    private String dbHost;
    /**
     *数据库用户
     */
    @ExcelProperty("数据库用户")
    private String dbUser;
    /**
      *Schema
      */
    @ExcelProperty("Schema")
    private String schemaName="-";
    /**
     * 数据库名
     */
    @ExcelProperty("数据库名")
    private String dbName="-";
    /**
     *生成时间
     */
    @ExcelProperty("生成时间")
    private String generateTime;
    /**
     *表数量
     */
    @ExcelProperty("表数量")
    private Integer tablesNum;
}

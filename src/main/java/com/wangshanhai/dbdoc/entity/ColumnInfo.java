package com.wangshanhai.dbdoc.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.enums.BooleanEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The {@code ColumnInfo} class
 *
 * @author Fly.Sky
 * @since 2022/10/26 15:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@HeadFontStyle(fontHeightInPoints = 11,bold = BooleanEnum.FALSE,fontName ="微软雅黑")
public class ColumnInfo {
    @ExcelProperty("数据库名")
    private String tableCat="-";
    /**
     * 所属模式
     */
    @ExcelProperty("所属模式")
    private String tableSchema="-";
    /**
     * 所属表
     */
    @ExcelProperty("所属表")
    private String tableName;

    /**
     * 字段名
     */
    @ExcelProperty("字段名")
    private String name;
    /**
     * 字段说明
     */
    @ExcelProperty("字段说明")
    private String remark;
    @ExcelIgnore
    private String dataType;
    /**
     * 字段类型
     */
    @ExcelProperty("字段类型")
    private String typeName;
    /**
     * 字段长度
     */
    @ExcelProperty("字段长度")
    private String size;
    /**
     * 字段允许为空 1：允许
     */
    @ExcelProperty("字段允许为空 1：允许")
    private String nullable;
    /**
     * 字段精度
     */
    @ExcelProperty("字段精度")
    private String decimalDigits;
    @ExcelIgnore
    private String numPrecRadix;
    /**
     * 字段默认值
     */
    @ExcelProperty("字段默认值")
    private String columnDef="-";
    /**
     * 字段自增长
     */
    @ExcelProperty("字段自增长")
    private String isAutoincrement;

    @ExcelIgnore
    private String bufferLength;
    @ExcelIgnore
    private String sqlDataType;
    @ExcelIgnore
    private String sqlDatetimeSub;
    @ExcelIgnore
    private String  charOctetLength;
    @ExcelIgnore
    private String  ordinalPosition;
    @ExcelIgnore
    private String  isNullable;
}

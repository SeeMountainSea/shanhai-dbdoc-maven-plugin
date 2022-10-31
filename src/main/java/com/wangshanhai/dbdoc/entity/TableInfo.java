package com.wangshanhai.dbdoc.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.enums.BooleanEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The {@code TableInfo} class
 *
 * @author Fly.Sky
 * @since 2022/10/26 14:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@HeadFontStyle(fontHeightInPoints = 11,bold = BooleanEnum.FALSE,fontName ="微软雅黑")
public class TableInfo {
    /**
      *表名称
      */
    @ExcelProperty("表名称")
    private String tableName;
    /**
     *表注释
     */
    @ExcelProperty("表注释")
    private String tableRemarks="-";
    /**
     * 所属模式
     */
    @ExcelProperty("所属模式")
    private String tableSchema="-";
}

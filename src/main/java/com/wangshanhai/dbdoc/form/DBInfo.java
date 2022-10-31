package com.wangshanhai.dbdoc.form;

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
public class DBInfo {
    /**
      *JDBC URL
      */
    private String  jdbcUrl;
    /**
     * 数据库用户名
     */
    private String  user;
    /**
     *  数据库密码
     */
    private String  password;
    /**
     *  数据库驱动
     */
    private String  driver;
}

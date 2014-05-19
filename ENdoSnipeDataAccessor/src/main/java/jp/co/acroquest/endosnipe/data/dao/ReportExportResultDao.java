/*
 * Copyright (c) 2004-2013 Acroquest Technology Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  Acroquest Technology Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.data.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jp.co.acroquest.endosnipe.common.util.SQLUtil;
import jp.co.acroquest.endosnipe.data.TableNames;

/**
 * {@link ReportExportResult} のための DAO です。
 * 
 * @author hiramatsu
 *
 */
public class ReportExportResultDao extends AbstractDao implements TableNames
{
    /**
     * 指定したインデックスのテーブルを truncate します。
     *
     * @param database データベース名
     * @param tableIndex テーブルインデックス
     * @param year 次にこのテーブルに入れるデータの年
     * @throws SQLException SQL 実行時に例外が発生した場合
     */
    public static void truncate(final String database, final int tableIndex, final int year)
        throws SQLException
    {
        String tableName = String.format("%s_%02d", REPORT_EXPORT_RESULT, tableIndex);
        truncate(database, tableName);
        alterCheckConstraint(database, tableName, tableIndex, "TO_TIME", year);
    }
    
    /**
     * すべてのレコードを取得します。<br />
     *
     * @param database データベース名
     * @return レコードのリスト
     * @throws SQLException SQL 実行時に例外が発生した場合
     */
    public static List<String> selectAllReportName(final String database)
        throws SQLException
    {
        List<String> reportNameList = new ArrayList<String>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = getConnection(database, true);
            stmt = conn.createStatement();
            String sql = "select * from " + REPORT_EXPORT_RESULT + " order by REPORT_NAME";
            rs = stmt.executeQuery(sql);
            StringBuilder builder = new StringBuilder();
            while (rs.next() == true)
            {
                String[] splittedFileName = rs.getString(2).split("/");
                builder.append(splittedFileName[splittedFileName.length - 1]);
                builder.append("_");
                builder.append(rs.getString(4).replace("-", "").replace(":", "").replace(" ", "_"));
                builder.append("-");
                builder.append(rs.getString(5).replace("-", "").replace(":", "").replace(" ", "_"));
                builder.append(".zip");
                reportNameList.add(builder.toString());
                builder.setLength(0);
            }
        }
        finally
        {
            SQLUtil.closeResultSet(rs);
            SQLUtil.closeStatement(stmt);
            SQLUtil.closeConnection(conn);
        }

        return reportNameList;
    }
}

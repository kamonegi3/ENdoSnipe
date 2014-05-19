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
package jp.co.acroquest.endosnipe.web.explorer.dto;

import java.util.Calendar;
import java.util.List;

/**
 * レポート出力定義のDtoクラス。
 * 
 * @author miyasaka
 *
 */
public class ReportDefinitionDto
{
    /** レポート出力定義のID。 */
    public int reportId_;

    /** レポート名。 */
    public String reportName_;

    /** レポート出力の対象の計測対象名。 */
    public String targetMeasurementName_;

    /** レポート出力の対象の正規表現。 */
    public List<String> targetMeasurementPattern_;

    /** 期間の始まりの日時。 */
    private Calendar reportTermFrom_;

    /** 期間の終わりの日時。 */
    private Calendar reportTermTo_;

    /**the status of report*/
    private String status_;

    /**
     * to check report duplicate or not
     */
    public String message_;

    /**
     * コンストラクタ。
     */
    public ReportDefinitionDto()
    {

    }

    /**
     * レポート出力定義のIDを取得する。
     *
     * @return レポート出力定義のID
     */
    public int getReportId()
    {
        return reportId_;
    }

    /**
     * レポート出力定義のIDを設定する。
     * 
     * @param reportId レポート出力定義のID
     */
    public void setReportId(final int reportId)
    {
        reportId_ = reportId;
    }

    /**
     * レポート名を取得する。
     * 
     * @return レポート名
     */
    public String getReportName()
    {
        return reportName_;
    }

    /**
     * レポート名を設定する。
     * 
     * @param reportName レポート名
     */
    public void setReportName(final String reportName)
    {
        reportName_ = reportName;
    }

    /**
     * レポート出力の対象の計測対象名を取得する。
     * 
     * @return レポート出力の対象の計測対象名
     */
    public String getTargetMeasurementName()
    {
        return targetMeasurementName_;
    }

    /**
     * レポート出力の対象の計測対象名を設定する。
     * 
     * @param targetMeasurementName レポート出力の対象の計測対象名
     */
    public void setTargetMeasurementName(final String targetMeasurementName)
    {
        targetMeasurementName_ = targetMeasurementName;
    }

    /**
     * レポート出力の対象の正規表現を取得する。
     * @return レポート出力の対象の正規表現
     */
    public List<String> getTargetMeasurementPattern()
    {
        return targetMeasurementPattern_;
    }

    /**
     * レポート出力の対象の正規表現を設定する。
     * @param targetMeasurementPattern レポート出力の対象の正規表現
     */
    public void setTargetMeasurementPattern(final List<String> targetMeasurementPattern)
    {
        targetMeasurementPattern_ = targetMeasurementPattern;
    }

    /**
     * 期間の始まりの日時を取得する。
     * 
     * @return 期間の始まりの日時
     */
    public Calendar getReportTermFrom()
    {
        return reportTermFrom_;
    }

    /**
     * 期間の始まりの日時を設定する。
     * 
     * @param reportTermFrom 期間の始まりの日時
     */
    public void setReportTermFrom(final Calendar reportTermFrom)
    {
        reportTermFrom_ = reportTermFrom;
    }

    /**
     * 期間の終わりの日時を取得する。
     * 
     * @return 期間の終わりの日時
     */
    public Calendar getReportTermTo()
    {
        return reportTermTo_;
    }

    /**
     * 期間の終わりの日時を設定する。
     * 
     * @param reportTermTo 期間の終わりの日時
     */
    public void setReportTermTo(final Calendar reportTermTo)
    {
        reportTermTo_ = reportTermTo;
    }

    /**
     * getter methods to get the status
     * @return its status
     */
    public String getStatus()
    {
        return status_;
    }

    /**
     * setter methods to set the status
     * @param status is the data to set
     */
    public void setStatus(final String status)
    {
        status_ = status;
    }

}

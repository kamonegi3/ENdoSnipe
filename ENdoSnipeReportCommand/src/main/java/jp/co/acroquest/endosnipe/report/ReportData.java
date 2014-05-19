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
package jp.co.acroquest.endosnipe.report;

import java.util.Calendar;
import java.util.List;

/**
 * 
 * Data Object to put to the Queue for output report.
 * @author khinewai
 * 
 */
public class ReportData
{
	/**
	 * ReportId for report data
	 */
	public int reportId;

	/**
	 * reportName for report data
	 */
	public String reportName;

	/**
	 * targetMeasurementName for report data
	 */
	public String targetMeasurementName;
	
	
	/**
	 * targetMeasurementPattern for report data
	 */
	public List<String> targetMeasurementPattern;

	/**
	 * reportTermFrom for report data
	 */
	public Calendar reportTermFrom;

	/**
	 * reportTermTo for report data
	 */
	public Calendar reportTermTo;

	/**status_ for report data
	 * 
	 */
	public String status_;

	/**
	 * get Status of reportData
	 * @return
	 */
	public String getStatus()
	{
		return status_;
	}

	/**
	 * set Status of reportData
	 * @param status
	 */
	public void setStatus_(String status)
	{
		this.status_ = status;
	}

	/**
	 * get ReportId of reportData
	 * @return
	 */
	public int getReportId()
	{
		return reportId;
	}

	/**
	 * set ReportId of reportData
	 * @param reportId
	 */
	public void setReportId(int reportId)
	{
		this.reportId = reportId;
	}

	/**
	 * get ReportName of reportData
	 * @return
	 */
	public String getReportName()
	{
		return reportName;
	}

	/**
	 * set ReportName of reportData
	 * @param reportName
	 */
	public void setReportName(String reportName)
	{
		this.reportName = reportName;
	}

	/**
	 * get TargetMeasurementName of reportData
	 * @return
	 */
	public String getTargetMeasurementName()
	{
		return targetMeasurementName;
	}

	/**
	 * set TargetMeasurementName of reportData
	 * @param targetMeasurementName
	 */
	public void setTargetMeasurementName(String targetMeasurementName)
	{
		this.targetMeasurementName = targetMeasurementName;
	}
	
	/**
	 * get TargetMeasurementPattern of reportData
	 * @return
	 */
	public List<String> getTargetMeasurementPattern()
	{
		return targetMeasurementPattern;
	}

	/**
	 * set TargetMeasurementPattern of reportData
	 * @param targetMeasurementPattern
	 */
	public void setTargetMeasurementPattern(List<String> targetMeasurementPattern)
	{
		this.targetMeasurementPattern = targetMeasurementPattern;
	}

	/**
	 * get ReportTermFrom of reportData
	 * @return
	 */
	public Calendar getReportTermFrom()
	{
		return reportTermFrom;
	}

	/**
	 * set ReportTermFrom of reportData
	 * @param reportTermForm
	 */
	public void setReportTermFrom(Calendar reportTermForm)
	{
		this.reportTermFrom = reportTermForm;
	}

	/**
	 * get ReportTermTo of reportData
	 * @return
	 */
	public Calendar getReportTermTo()
	{
		return reportTermTo;
	}

	/**
	 * set ReportTermTo of reportData
	 * @param reportTermTo
	 */
	public void setReportTermTo(Calendar reportTermTo)
	{
		this.reportTermTo = reportTermTo;
	}

}

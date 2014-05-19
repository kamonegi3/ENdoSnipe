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

import java.io.File;
import java.util.Calendar;
import java.util.List;

import jp.co.acroquest.endosnipe.collector.LogMessageCodes;
import jp.co.acroquest.endosnipe.collector.config.DataCollectorConfig;

/**
 * ReporterThread to export the inputted report data
 * @author khinlay
 *
 */
public class ReporterThread implements Runnable, LogMessageCodes
{
	/**
	 * object of ReportDataQueue
	 */
	private ReportDataQueue reportQueue = ReportDataQueue.getInstance();

	/**
	 * flag to run Thread
	 */
	public boolean isRunningThread = true;

	/**
	 * path of Report
	 */
	private static final String REPORT_PATH = "report";

	/**
	 * configuratin to export the report
	 */
	DataCollectorConfig config;

	/**
	 * constructor
	 * @param dataCollectorConfig
	 */
	public ReporterThread(DataCollectorConfig dataCollectorConfig)
	{

		this.config = dataCollectorConfig;
	}

	/**
	 * execution to export the report by getting data from the queue
	 */
	public void run()
	{
		if (this.config == null)
		{
			isRunningThread = false;
			return;
		}
		Reporter reporter = new Reporter();
		while (isRunningThread)
		{

			// キューからデータを取り出す
			ReportData data = this.reportQueue.take();

			if (data != null)
			{
				Calendar fmTime = data.getReportTermFrom();
				Calendar toTime = data.getReportTermTo();
				String targetItemName = data.getTargetMeasurementName();
				String reportNamePath = data.getReportName();
				String[] reportNameSplitList = reportNamePath.split("/");
				int reportNameSplitLength = reportNameSplitList.length;

				String reportName = reportNameSplitList[reportNameSplitLength - 1];
				String status = data.getStatus();
				String tempDirectory = System.getProperty("java.io.tmpdir");
				List<String> matchingPatternList = data.getTargetMeasurementPattern();
				this.deleteTempFile(tempDirectory);
				reporter.createReport(this.config, fmTime, toTime, REPORT_PATH, targetItemName,
					reportName, status, matchingPatternList);
				this.deleteTempFile(tempDirectory);
			}
		}

	}

	/**
	 * This function is delete temp file in the temp directory
	 * 
	 * @param tempDirectory
	 *            get temp file
	 */
	private void deleteTempFile(final String tempDirectory)
	{
		File directory = new File(tempDirectory);
		File[] files = directory.listFiles();
		for (File file : files)
		{
			if (file.isFile())
			{
				String fileName = file.getAbsolutePath();
				if (fileName.endsWith(".xls"))
				{
					file.delete();
				}
			}
		}

	}

	/**
	 * Stop the reporter thread
	 */
	public void stopThread()
	{
		this.isRunningThread = false;

	}

}

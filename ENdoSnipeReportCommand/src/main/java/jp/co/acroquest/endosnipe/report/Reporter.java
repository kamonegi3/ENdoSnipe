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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.co.acroquest.endosnipe.collector.config.DataCollectorConfig;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.data.dao.JavelinMeasurementItemDao;
import jp.co.acroquest.endosnipe.data.dao.ReportExportResultDao;
import jp.co.acroquest.endosnipe.data.db.DBManager;
import jp.co.acroquest.endosnipe.data.entity.JavelinMeasurementItem;
import jp.co.acroquest.endosnipe.report.controller.ReportPublishTask;
import jp.co.acroquest.endosnipe.report.controller.ReportSearchCondition;
import jp.co.acroquest.endosnipe.report.controller.ReportType;
import jp.co.acroquest.endosnipe.report.controller.dispatcher.ReportPublishDispatcher;
import jp.co.acroquest.endosnipe.report.dao.ReportDao;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;

/**
 * BottleneckEyeを起動せずに、レポート作成を行うためのクラスです。<br>
 * 
 * @author iida
 */
public class Reporter
{

	/** 開始／終了時刻を指定する文字列形式。 */
	public static final String TIME_FORMAT = "yyyyMMdd_HHmmss";

	/** ロガー */
	private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger
		.getLogger(ReportPublishDispatcher.class);

	/**
	 * コンストラクタ。
	 */
	public Reporter()
	{

	}

	public void createReport(DataCollectorConfig config, Calendar fmTime, Calendar toTime,
		String reportPath, String targetItemName, String reportName)
	{
		createReport(config, fmTime, toTime, reportPath, targetItemName, reportName, null, null);
	}

	/**
	 * レポート作成を行います。<br/>
	 * 
	 * @param config
	 *            DataCollectorの設定/定数を保持するオブジェクト
	 * @param fmTime
	 *            開始時刻
	 * @param toTime
	 *            終了時刻
	 * @param reportPath
	 *            出力先ディレクトリ
	 * @param targetItemName
	 *            レポート出力対象の親の項目名
	 * @param reportName
	 *            レポート名
	 */
	public void createReport(DataCollectorConfig config, Calendar fmTime, Calendar toTime,
		String reportPath, String targetItemName, String reportName, String status, List<String> matchingPatternList)
	{
		
		// 開始時刻が終了時刻より未来を指していた場合はエラー
		if (fmTime.compareTo(toTime) > 0)
		{
			System.err.println("開始時刻が終了時刻より未来を指しています。");
			return;
		}

		if (config == null)
		{
			return;
		}

		// DBの諸設定を取得
		String dbName = config.getDatabaseName();
		String dbHost = config.getDatabaseHost();
		String dbPort = config.getDatabasePort();
		String dbUser = config.getDatabaseUserName();
		String dbPass = config.getDatabasePassword();
		
		// レポート作成に使用するDBを指定する
		DBManager.updateSettings(false, "", dbHost, dbPort, dbName, dbUser, dbPass);

		// レポート作成時の各設定を行う
		ReportType[] outputReportTypes = new ReportType[] { ReportType.OBJECT,
			ReportType.PERF_DOCTOR };

		Runnable callback = null;

		// レポート出力ディレクトリを決定し、存在しなければ作成する
		// レポート出力先ディレクトリ：
		// <current-dir>/reports/<db-name>/<from>-<to>/
		SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
		String start = format.format(fmTime.getTime());
		String end = format.format(toTime.getTime());
		String leafDirectoryName = reportName + "_" + start + "-" + end;

		String outputParentPath = reportPath + File.separator + dbName;
		
		// レポートのローテートを行う。
		try
		{
			rotateReport(new File(outputParentPath), dbName);
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}
		
		String outputFilePath = outputParentPath + File.separator + leafDirectoryName;

		File outputDir = new File(outputFilePath);
		if (outputDir.exists() == false)
		{
			outputDir.mkdirs();
		}
		
		// TODO 絞り込みのルールを設定する
		boolean limitSameCause = false;
		boolean limitBySameRule = false;

		// レポート出力期間の条件を設定する
		ReportSearchCondition searchCondition = new ReportSearchCondition();
		searchCondition.setDatabases(Arrays.asList(dbName));
		searchCondition.setStartDate(new Timestamp(fmTime.getTimeInMillis()));
		searchCondition.setEndDate(new Timestamp(toTime.getTimeInMillis()));
		searchCondition.setOutputFilePath(outputFilePath);
		searchCondition.setLimitSameCause(limitSameCause);
		searchCondition.setLimitBySameRule(limitBySameRule);

		Timestamp startTime = new Timestamp(fmTime.getTimeInMillis());
		Timestamp endTime = new Timestamp(toTime.getTimeInMillis());
		// 絶対パスを取得
		File currentDirectory = new File(".");

		String outputDirFullPath = currentDirectory.getAbsolutePath() + File.separator + reportPath
			+ File.separator + dbName + File.separator;

		// レポート情報をログに出力する
		LOGGER.log(LogIdConstants.OUTPUT_REPORT_INFO, outputDirFullPath, leafDirectoryName,
			targetItemName);

		// ReportPublishTaskを実行し、レポート作成を行う
		ReportDao reportDao = new ReportDao();
		try
		{
			ReportPublishTask reportTask = new ReportPublishTask(searchCondition,
				outputReportTypes, callback);
			status = "completed";

			// レポートを出力する
			if(matchingPatternList == null || matchingPatternList.size() == 0){
				reportTask.createReport(targetItemName);
			}else{
				for(String matchingPattern : matchingPatternList){
					List<JavelinMeasurementItem> items = JavelinMeasurementItemDao.selectAllByPattern(dbName, matchingPattern);
					for(JavelinMeasurementItem item : items){
						reportTask.createReport(item.itemName);
					}
				}
			}
			
		}
		catch (Exception e)
		{
			status = "failed";

			LOGGER.log(LogIdConstants.REPORT_PUBLISH_STOPPED_WARN, outputDirFullPath
				+ leafDirectoryName);
			reportDao.changeStatus(dbName, status, targetItemName, startTime, endTime);
			return;
		}

		// khine Wai Oo
		reportDao.changeStatus(dbName, status, targetItemName, startTime, endTime);
		// zip圧縮する
		Project project = new Project();
		project.init();

		try
		{
			File baseDir = new File(outputFilePath);
			Zip zipper = new Zip();
			zipper.setProject(project);
			zipper.setTaskName("zip");
			zipper.setTaskType("zip");
			zipper.setDestFile(new File(outputFilePath + ".zip"));
			zipper.setBasedir(baseDir);
			zipper.execute();

			// zip化に成功したら元のディレクトリは削除する
			boolean deleted = deleteDir(baseDir);
			if (deleted == false)
			{
				LOGGER
					.log(LogIdConstants.FAIL_TO_DELETE_DIR, outputDirFullPath + leafDirectoryName);
			}
		}
		catch (BuildException bex)
		{
			LOGGER.log(LogIdConstants.FAIL_TO_ZIP, outputDirFullPath + leafDirectoryName);
		}

	}

	private void rotateReport(File outputDir, String dbName) throws SQLException
	{
		File[] filesInDir = outputDir.listFiles();
		if (filesInDir.length == 0) 
		{
			return;
		}
		
		List<String> fileNamesInTable = ReportExportResultDao.selectAllReportName(dbName);
		
		for (File fileInDir: filesInDir) 
		{
			if (! fileNamesInTable.contains(fileInDir.getName())) 
			{
				fileInDir.delete();
			}
		}
		
	}

	/**
	 * 指定したディレクトリごと削除する。
	 * 
	 * @param dir
	 *            削除するディレクトリ。
	 * @return ディレクトリの削除に失敗した場合。
	 */
	private static boolean deleteDir(File dir)
	{
		boolean result = true;
		File[] children = dir.listFiles();
		for (File child : children)
		{
			if (child.isDirectory() == true)
			{
				// ディレクトリは再帰して削除を行う
				result = deleteDir(child);
				if (result == false)
				{
					break;
				}
			}
			else
			{
				// ファイルは単に削除を行う
				result = child.delete();
				if (result == false)
				{
					break;
				}
			}
		}

		// 全ての削除に成功していれば中身は空なので、自分のディレクトリを削除する
		if (result == true)
		{
			result = dir.delete();
		}

		return result;
	}

	public static void main(String[] args) throws Exception
	{
		Reporter reporter = new Reporter();
		DataCollectorConfig config = new DataCollectorConfig();
		config.setDatabaseHost("126.0.56.101");
		config.setDatabasePort("5432");
		config.setDatabaseName("endosnipedb");
		config.setDatabaseUserName("postgres");
		config.setDatabasePassword("postgres");
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");

		Date fmDate = format.parse("2013/06/04 13:00");
		Calendar fmTime = Calendar.getInstance();
		fmTime.setTime(fmDate);

		Date toDate = format.parse("2013/06/04 14:00");
		Calendar toTime = Calendar.getInstance();
		toTime.setTime(toDate);

		String reportPath = "report";
		String targetItemName = "/";
		String reportName = "test";
		String status = "failed";
		reporter.createReport(config, fmTime, toTime, reportPath, targetItemName, reportName,
			status, null);
	}
}

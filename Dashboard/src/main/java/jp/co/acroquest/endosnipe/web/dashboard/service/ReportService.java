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
package jp.co.acroquest.endosnipe.web.dashboard.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jp.co.acroquest.endosnipe.collector.config.DataCollectorConfig;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.data.dao.JavelinMeasurementItemDao;
import jp.co.acroquest.endosnipe.report.Reporter;
import jp.co.acroquest.endosnipe.web.dashboard.config.DataBaseConfig;
import jp.co.acroquest.endosnipe.web.dashboard.constants.LogMessageCodes;
import jp.co.acroquest.endosnipe.web.dashboard.dao.ReportDefinitionDao;
import jp.co.acroquest.endosnipe.web.dashboard.dao.SchedulingReportDefinitionDao;
import jp.co.acroquest.endosnipe.web.dashboard.dto.ReportDefinitionDto;
import jp.co.acroquest.endosnipe.web.dashboard.dto.SchedulingReportDefinitionDto;
import jp.co.acroquest.endosnipe.web.dashboard.dto.TreeMenuDto;
import jp.co.acroquest.endosnipe.web.dashboard.entity.ReportDefinition;
import jp.co.acroquest.endosnipe.web.dashboard.entity.SchedulingReportDefinition;
import jp.co.acroquest.endosnipe.web.dashboard.manager.DatabaseManager;
import jp.co.acroquest.endosnipe.web.dashboard.manager.EventManager;
import jp.co.acroquest.endosnipe.web.dashboard.manager.ResourceSender;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.wgp.manager.WgpDataManager;

/**
 * レポート出力機能のサービスクラス。
 * 
 * @author miyasaka
 *
 */
@Service
public class ReportService
{
    /** レポートの出力先ディレクトリ名。 */
    private static final String REPORT_PATH = "report";

    /** ロガー。 */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(MapService.class);

    /** レポート情報Dao */
    @Autowired
    protected ReportDefinitionDao reportDefinitionDao;

    /** 日付のフォーマット。 */
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** バッファのサイズ */
    private static final int BUFFER_SIZE = 1024;

    /** ファイルのセパレータ */
    private static final String FOLDER_SEPARATOR = File.separator;

    /**
     * Format of Time
     */
    private static final String TIME_FORMAT = "HH:mm";

    /** レポート情報Dao */
    @Autowired
    protected SchedulingReportDefinitionDao schedulingReportDefinitionDao;

    private final Map<String, Integer> dayMap_ = new HashMap<String, Integer>();

    /**
     * デフォルトコンストラクタ。
     */
    public ReportService()
    {
        this.dayMap_.put("Sunday", Calendar.SUNDAY);
        this.dayMap_.put("Monday", Calendar.MONDAY);
        this.dayMap_.put("Tuesday", Calendar.TUESDAY);
        this.dayMap_.put("Wednesday", Calendar.WEDNESDAY);
        this.dayMap_.put("Thursday", Calendar.THURSDAY);
        this.dayMap_.put("Friday", Calendar.FRIDAY);
        this.dayMap_.put("Saturday", Calendar.SATURDAY);
    }

    /**
     * すべてのシグナルデータを返す。
     * 
     * @return シグナルデータ一覧
     */
    public List<ReportDefinitionDto> getAllReport()
    {
        List<ReportDefinition> reportList = null;
        try
        {
            reportList = reportDefinitionDao.selectAll();
        }
        catch (PersistenceException pEx)
        {
            Throwable cause = pEx.getCause();
            if (cause instanceof SQLException)
            {
                SQLException sqlEx = (SQLException)cause;
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
            }
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION, pEx, pEx.getMessage());

            return new ArrayList<ReportDefinitionDto>();
        }

        List<ReportDefinitionDto> definitionDtoList = new ArrayList<ReportDefinitionDto>();

        for (ReportDefinition reportDefinitioin : reportList)
        {
            ReportDefinitionDto reportDto = this.convertReportDifinitionDto(reportDefinitioin);
            definitionDtoList.add(reportDto);
        }

        return definitionDtoList;
    }

    /**
     * 指定したレポート名をキーに、レポート定義の一覧を取得する。
     * 
     * @param reportName レポート名
     * @return レポート定義の一覧
     */
    public List<ReportDefinitionDto> getReportByReportName(final String reportName)
    {
        List<ReportDefinitionDto> reportDefinitionDtos = new ArrayList<ReportDefinitionDto>();
        List<ReportDefinition> definitionDtos = reportDefinitionDao.selectByName(reportName);

        int definitionDtosLength = definitionDtos.size();

        for (int index = 0; index < definitionDtosLength; index++)
        {
            ReportDefinition definition = definitionDtos.get(index);
            ReportDefinitionDto definitionDto = this.convertReportDifinitionDto(definition);
            reportDefinitionDtos.add(definitionDto);
        }

        return reportDefinitionDtos;
    }

    /**
     * 指定したレポートIDをキーに、レポート定義を取得する。
     * 
     * @param reportId レポートID
     * @return レポート定義
     */
    public ReportDefinitionDto getReportByReportId(final int reportId)
    {
        ReportDefinition reportDefinition = reportDefinitionDao.selectById(reportId);
        ReportDefinitionDto reportDefinitionDto = this.convertReportDifinitionDto(reportDefinition);

        return reportDefinitionDto;
    }

    /**
     * レポート出力の定義をDBに登録する。
     * 
     * @param reportDefinition レポート出力の定義
     * @return レポート出力の定義
     */
    public ReportDefinitionDto insertReportDefinition(final ReportDefinition reportDefinition)
    {
        try
        {
            reportDefinitionDao.insert(reportDefinition);
        }
        catch (DuplicateKeyException dkEx)
        {
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION, dkEx, dkEx.getMessage());
            return new ReportDefinitionDto();
        }

        ReportDefinitionDto reportDefinitionDto = this.convertReportDifinitionDto(reportDefinition);

        return reportDefinitionDto;
    }

    public boolean hasSameSignalName(final long reportId, final String reportName)
    {
        SchedulingReportDefinition schedulingReportDefinition =
                this.schedulingReportDefinitionDao.selectByName(reportName);
        if (schedulingReportDefinition == null)
        {
            // 同一シグナル名を持つ閾値判定定義情報が存在しない場合
            return false;
        }
        else if (schedulingReportDefinition.reportId_ == reportId)
        {
            // シグナル名が一致する閾値判定定義情報が更新対象自身の場合
            return false;
        }
        return true;
    }

    /**
     * レポートを作成する。
     * 
     * @param reportDefinitionDto レポート出力の定義
     */
    public void createReport(final ReportDefinitionDto reportDefinitionDto)
    {

        Reporter reporter = new Reporter();

        DatabaseManager dbMmanager = DatabaseManager.getInstance();
        Calendar fmTime = reportDefinitionDto.getReportTermFrom();
        Calendar toTime = reportDefinitionDto.getReportTermTo();
        String targetItemName = reportDefinitionDto.getTargetMeasurementName();
        String reportNamePath = reportDefinitionDto.getReportName();
        String[] reportNameSplitList = reportNamePath.split("/");
        int reportNameSplitLength = reportNameSplitList.length;
        String reportName = reportNameSplitList[reportNameSplitLength - 1];

        DataBaseConfig dataBaseConfig = dbMmanager.getDataBaseConfig();
        DataCollectorConfig dataCollecterConfig = this.convertDataCollectorConfig(dataBaseConfig);

        reporter.createReport(dataCollecterConfig, fmTime, toTime, REPORT_PATH, targetItemName,
                              reportName);
    }

    /**
     * ReportDefinitionDtoオブジェクトをReportDefinitionオブジェクトに変換する。
     * 
     * @param definitionDto
     *            ReportDefinitionDtoオブジェクト
     * 
     * @return ReportDefinitionオブジェクト
     */
    public ReportDefinition convertReportDefinition(final ReportDefinitionDto definitionDto)
    {

        ReportDefinition reportDefinition = new ReportDefinition();

        reportDefinition.reportId_ = definitionDto.getReportId();
        reportDefinition.reportName_ = definitionDto.getReportName();
        reportDefinition.targetMeasurementName_ = definitionDto.getTargetMeasurementName();

        Calendar fmTimeCal = definitionDto.getReportTermFrom();
        Calendar toTimeCal = definitionDto.getReportTermTo();

        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        reportDefinition.fmTime_ = dateFormat.format(fmTimeCal.getTime());
        reportDefinition.toTime_ = dateFormat.format(toTimeCal.getTime());

        return reportDefinition;
    }

    /**
     * ReportDefinitionオブジェクトをReportDefinitionDtoオブジェクトに変換する。
     * 
     * @param reportDefinition
     *            ReportDefinitionオブジェクト
     * @return ReportDefinitionDtoオブジェクト
     */
    private ReportDefinitionDto convertReportDifinitionDto(final ReportDefinition reportDefinition)
    {

        ReportDefinitionDto definitionDto = new ReportDefinitionDto();

        definitionDto.setReportId(reportDefinition.reportId_);
        definitionDto.setReportName(reportDefinition.reportName_);
        definitionDto.setTargetMeasurementName(reportDefinition.targetMeasurementName_);

        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        Calendar fmTime = Calendar.getInstance();
        Calendar toTime = Calendar.getInstance();
        try
        {
            fmTime.setTime(dateFormat.parse(reportDefinition.fmTime_));
            toTime.setTime(dateFormat.parse(reportDefinition.toTime_));
        }
        catch (ParseException ex)
        {
            LOGGER.log(LogMessageCodes.UNSUPPORTED_REPORT_FILE_DURATION_FORMAT);
        }

        definitionDto.setReportTermFrom(fmTime);
        definitionDto.setReportTermTo(toTime);

        return definitionDto;
    }

    /**
     * クライアントからのレポート出力ダウンロードを受信するためのサーブレットです。
     * 
     * @param response {@link HttpServletResponse}オブジェクト
     * @param fileName ファイル名
     */
    public void doRequest(final HttpServletResponse response, final String fileName)
    {
        if (fileName == null)
        {
            LOGGER.log(LogMessageCodes.UNKNOWN_FILE_NAME);
            String message = "filename is null.";
            handleException(response, message);
            return;
        }

        String filePath = this.getFilePath(fileName);

        OutputStream outputStream = null;

        try
        {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            BufferedInputStream buffInputStream = new BufferedInputStream(fileInputStream);

            String nonSpaceFileName = fileName.replaceAll("[ 　]", "");

            response.setHeader("Content-Disposition", "attachment; filename=" + nonSpaceFileName);

            outputStream = response.getOutputStream();

            int len = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((len = fileInputStream.read(buffer, 0, buffer.length)) != -1)
            {
                outputStream.write(buffer, 0, len);
            }

            buffInputStream.close();
        }
        catch (IOException ioEx)
        {
            LOGGER.log(LogMessageCodes.UNKNOWN_REPORT_FILE_NAME);
            handleException(response, ioEx.getMessage());
        }
        finally
        {

            if (outputStream != null)
            {
                try
                {
                    outputStream.close();
                }
                catch (IOException ioEx)
                {
                    LOGGER.log(LogMessageCodes.UNEXPECTED_ERROR);
                    handleException(response, ioEx.getMessage());
                }
                finally
                {
                    outputStream = null;
                }
            }
        }
    }

    /**
     * レポートファイルを削除する。
     * 
     * @param fileName ファイル名
     * @return ファイル削除に成功した場合はtrue
     */
    public boolean deleteReportFile(final String fileName)
    {
        if (fileName == null)
        {
            LOGGER.log(LogMessageCodes.UNKNOWN_FILE_NAME);
            return false;
        }

        // ファイル削除に成功した場合にtrueになるフラグ
        boolean isDeleteSuccess = false;

        String filePath = this.getFilePath(fileName);

        File file = new File(filePath);

        if (file.exists())
        {
            // ファイルを削除する
            isDeleteSuccess = file.delete();
        }
        else
        {
            LOGGER.log(LogMessageCodes.UNKNOWN_FILE_NAME);
            return false;
        }

        return isDeleteSuccess;
    }

    /**
     * レポート名に該当するレポート定義を削除する。
     * 
     * @param reportName レポート名
     */
    public void deleteReportDefinitionByName(final String reportName)
    {
        try
        {
            // レポート名に該当するレポート定義を削除する
            reportDefinitionDao.deleteByName(reportName);
        }
        catch (PersistenceException pEx)
        {
            Throwable cause = pEx.getCause();
            if (cause instanceof SQLException)
            {
                SQLException sqlEx = (SQLException)cause;
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
            }
            else
            {
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, pEx, pEx.getMessage());
            }
        }
    }

    /**
     * レポートIDに該当するレポート定義を削除する。
     * 
     * @param reportId レポートID
     */
    public void deleteReportDefinitionById(final int reportId)
    {
        try
        {
            // レポートIDに該当するレポート定義を削除する
            reportDefinitionDao.deleteById(reportId);
        }
        catch (PersistenceException pEx)
        {
            Throwable cause = pEx.getCause();
            if (cause instanceof SQLException)
            {
                SQLException sqlEx = (SQLException)cause;
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
            }
            else
            {
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, pEx, pEx.getMessage());
            }
        }
    }

    /**
     * ファイルが存在しないことをクライアント側に表示する。
     * 
     * @param responce {@link HttpServletResponse}オブジェクト
     * @param message エラーメッセージ
     */
    private void handleException(final HttpServletResponse responce, final String message)
    {
        try
        {
            // ContentTypeを指定
            responce.setContentType("text/csv;charset=UTF-8");
            String fileName = new String("error.txt".getBytes("UTF-8"), "ISO-8859-1");
            // Headerを設定
            responce.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            // 内容を出力
            PrintWriter writer = responce.getWriter();
            writer.write(message);
            writer.close();

        }
        catch (IOException ioEx)
        {
            LOGGER.log(LogMessageCodes.UNEXPECTED_ERROR);
        }
    }

    /**
     * DataBaseConfigオブジェクトをDataCollectorConfigオブジェクトに変換する。
     * 
     * @param dataBaseConfig DataBaseConfigオブジェクト
     * @return DataCollectorConfigオブジェクト
     */
    private DataCollectorConfig convertDataCollectorConfig(final DataBaseConfig dataBaseConfig)
    {
        DataCollectorConfig dataCollectorConfig = new DataCollectorConfig();
        dataCollectorConfig.setDatabaseHost(dataBaseConfig.getDatabaseHost());
        dataCollectorConfig.setDatabaseName(dataBaseConfig.getDatabaseName());
        dataCollectorConfig.setDatabasePassword(dataBaseConfig.getDatabasePassword());
        dataCollectorConfig.setDatabasePort(dataBaseConfig.getDatabasePort());
        dataCollectorConfig.setDatabaseType(dataBaseConfig.getDatabaseType());
        dataCollectorConfig.setDatabaseUserName(dataBaseConfig.getDatabaseUserName());

        return dataCollectorConfig;
    }

    /**
     * ファイルの絶対パスを取得する。
     * 
     * @param fileName ファイル名
     * @return ファイルの絶対パス
     */
    private String getFilePath(final String fileName)
    {
        String currentDir = new File(".").getAbsolutePath();
        DatabaseManager dbMmanager = DatabaseManager.getInstance();
        String dbName = dbMmanager.getDataBaseName(1);

        String filePath =
                currentDir + FOLDER_SEPARATOR + REPORT_PATH + FOLDER_SEPARATOR + dbName
                        + FOLDER_SEPARATOR + fileName;

        return filePath;
    }

    public List<SchedulingReportDefinitionDto> getAllSchedule()
    {

        List<SchedulingReportDefinition> reportList = null;
        try
        {
            reportList = schedulingReportDefinitionDao.selectAll();
            System.out.println("reportList:" + reportList);
        }
        catch (PersistenceException pEx)
        {
            Throwable cause = pEx.getCause();
            if (cause instanceof SQLException)
            {
                SQLException sqlEx = (SQLException)cause;
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
            }
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION, pEx, pEx.getMessage());

            return new ArrayList<SchedulingReportDefinitionDto>();
        }

        List<SchedulingReportDefinitionDto> definitionDtoList =
                new ArrayList<SchedulingReportDefinitionDto>();

        for (SchedulingReportDefinition reportDefinitioin : reportList)
        {
            SchedulingReportDefinitionDto reportDto =
                    this.convertSchedulingReportDifinitionDto(reportDefinitioin);
            definitionDtoList.add(reportDto);
        }
        return definitionDtoList;
    }

    /**
     * 
     * @param schedulingReportDefinition is used
     * @return is used
     */
    public SchedulingReportDefinitionDto insertSchedulingReport(
            final SchedulingReportDefinition schedulingReportDefinition)
    {
        try
        {
            schedulingReportDefinitionDao.insert(schedulingReportDefinition);
        }
        catch (DuplicateKeyException dkEx)
        {
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION, dkEx, dkEx.getMessage());
            return new SchedulingReportDefinitionDto();
        }

        SchedulingReportDefinitionDto schedulingReportDefinitionDto =
                this.convertSchedulingReportDifinitionDto(schedulingReportDefinition);

        return schedulingReportDefinitionDto;
    }

    public SchedulingReportDefinitionDto getSchedulingInfo(final int reportId)
    {
        System.out.println("service report name:" + reportId);
        SchedulingReportDefinition schedulingReportDefinition =
                schedulingReportDefinitionDao.selectById(reportId);
        SchedulingReportDefinitionDto schedulingReportDefinitionDto =
                this.convertSchedulingReportDifinitionDto(schedulingReportDefinition);
        return schedulingReportDefinitionDto;
    }

    public SchedulingReportDefinitionDto updateSchedulingInfo(
            final SchedulingReportDefinition schedulingReportDefinition)
    {
        try
        {
            SchedulingReportDefinition beforeSignalInfo =
                    schedulingReportDefinitionDao.selectByName(schedulingReportDefinition.reportName_);
            if (beforeSignalInfo == null)
            {
                return new SchedulingReportDefinitionDto();
            }

            schedulingReportDefinitionDao.update(schedulingReportDefinition);

            String beforeItemName = beforeSignalInfo.reportName_;
            String afterItemName = schedulingReportDefinition.reportName_;

            this.updateMeasurementItemName(beforeItemName, afterItemName);
        }
        catch (PersistenceException pEx)
        {
            Throwable cause = pEx.getCause();
            if (cause instanceof SQLException)
            {
                SQLException sqlEx = (SQLException)cause;
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
            }
            else
            {
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, pEx, pEx.getMessage());
            }

            return new SchedulingReportDefinitionDto();
        }
        catch (SQLException sqlEx)
        {
            LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
            return new SchedulingReportDefinitionDto();
        }

        SchedulingReportDefinitionDto signalDefinitionDto =
                this.convertSchedulingReportDifinitionDto(schedulingReportDefinition);
        /* signalDefinitionDto.setSignalValue(-1);
         sendSignalDefinitionRequest(signalDefinitionDto, SignalConstants.OPERATION_TYPE_UPDATE);*/
        // 各クライアントにシグナル定義の変更を送信する。
        sendSignalDefinition(signalDefinitionDto, "update");

        return signalDefinitionDto;
    }

    public void createSchedulingReport(final SchedulingReportDefinitionDto reportDefinitionDto)
    {
        Reporter reporter = new Reporter();

        DatabaseManager dbMmanager = DatabaseManager.getInstance();
        Calendar time = reportDefinitionDto.getTime();
        String targetItemName = reportDefinitionDto.getTargetMeasurementName();
        String reportNamePath = reportDefinitionDto.getReportName();
        String day = reportDefinitionDto.getDay();
        String term = reportDefinitionDto.getTerm();
        String[] reportNameSplitList = reportNamePath.split("/");
        int reportNameSplitLength = reportNameSplitList.length;
        String reportName = reportNameSplitList[reportNameSplitLength - 1];

        DataBaseConfig dataBaseConfig = dbMmanager.getDataBaseConfig();
        DataCollectorConfig dataCollecterConfig = this.convertDataCollectorConfig(dataBaseConfig);

        //reporter.createReport(dataCollecterConfig, date, time, REPORT_PATH, targetItemName,
        //reportName);
    }

    /**
     * 
     * @param schedulingDefinitionDto is used
     * @return is used
     */
    public SchedulingReportDefinition convertSchedulingReportDefinition(
            final SchedulingReportDefinitionDto schedulingDefinitionDto)
    {
        SchedulingReportDefinition schedulingReportDefinition = new SchedulingReportDefinition();
        schedulingReportDefinition.reportId_ = schedulingDefinitionDto.getReportId();
        schedulingReportDefinition.reportName_ = schedulingDefinitionDto.getReportName();
        schedulingReportDefinition.targetMeasurementName_ =
                schedulingDefinitionDto.getTargetMeasurementName();
        schedulingReportDefinition.term_ = schedulingDefinitionDto.getTerm();
        schedulingReportDefinition.day_ = schedulingDefinitionDto.getDay();
        System.out.println("Day:" + schedulingReportDefinition.day_);
        Calendar time = schedulingDefinitionDto.getTime();
        DateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
        schedulingReportDefinition.time_ = timeFormat.format(time.getTime());
        schedulingReportDefinition.date_ = schedulingDefinitionDto.getDate();

        Calendar lastExportedCalendar = Calendar.getInstance();
        Calendar currentTimeCalendar = Calendar.getInstance();
        lastExportedCalendar.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
        lastExportedCalendar.set(Calendar.MINUTE, time.get(Calendar.MINUTE));

        if (schedulingReportDefinition.day_ != null)
        {
            dayMap_.get(schedulingReportDefinition.day_);
            int lastExportedDay =
                    (dayMap_.get(schedulingReportDefinition.day_))
                            - lastExportedCalendar.get(Calendar.DAY_OF_WEEK);
            lastExportedCalendar.add(Calendar.DAY_OF_MONTH, lastExportedDay);

        }

        if (schedulingReportDefinition.date_ != null)
        {
            int date = Integer.parseInt(schedulingReportDefinition.date_);
            int newDate = date - lastExportedCalendar.get(Calendar.DAY_OF_MONTH);
            lastExportedCalendar.add(Calendar.DAY_OF_MONTH, newDate);

        }
        Timestamp lastExportedTime = new Timestamp(lastExportedCalendar.getTimeInMillis());
        schedulingReportDefinition.lastExportedTime_ = lastExportedTime;
        return schedulingReportDefinition;
    }

    /**
     * check report name and report term exist or not in Database
     * @param reportDefinitionDto get the report data
     * from controller class
     * @return true or false
     */
    public boolean checkReportName(final ReportDefinitionDto reportDefinitionDto)
    {
        ReportDefinition reportDefinition = null;
        String reportName = reportDefinitionDto.getReportName();
        Calendar fmTimeCal = reportDefinitionDto.getReportTermFrom();
        Calendar toTimeCal = reportDefinitionDto.getReportTermTo();

        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        String startTime = dateFormat.format(fmTimeCal.getTime());
        String endTime = dateFormat.format(toTimeCal.getTime());
        reportDefinition = reportDefinitionDao.selectName(reportName, startTime, endTime);
        if (reportDefinition != null)
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    /**
    * 
    * @param schedulingReportDefinition is used
    * @return is used
    */
    private SchedulingReportDefinitionDto convertSchedulingReportDifinitionDto(
            final SchedulingReportDefinition schedulingReportDefinition)
    {
        SchedulingReportDefinitionDto schedulingDefinitionDto = new SchedulingReportDefinitionDto();
        schedulingDefinitionDto.setReportId(schedulingReportDefinition.reportId_);
        schedulingDefinitionDto.setReportName(schedulingReportDefinition.reportName_);
        schedulingDefinitionDto.setTargetMeasurementName(schedulingReportDefinition.targetMeasurementName_);
        schedulingDefinitionDto.setTerm(schedulingReportDefinition.term_);
        schedulingDefinitionDto.setDay(schedulingReportDefinition.day_);
        schedulingDefinitionDto.setDate(schedulingReportDefinition.date_);

        DateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
        java.util.Date date = new java.util.Date();
        Timestamp lastExportedTime = new Timestamp(date.getTime());

        Calendar time = Calendar.getInstance();
        try
        {
            time.setTime(timeFormat.parse(schedulingReportDefinition.time_));
        }
        catch (ParseException ex)
        {
            LOGGER.log(LogMessageCodes.UNSUPPORTED_REPORT_FILE_DURATION_FORMAT);
        }

        schedulingDefinitionDto.setTime(time);
        /*schedulingDefinitionDto.setLastExportedTime(lastExportedTime);
        System.out.println("last export time from Dto:"
                + schedulingDefinitionDto.getLastExportedTime());*/

        return schedulingDefinitionDto;
    }

    public void deleteSchduleReportById(final int reportId)
    {
        try
        {
            // レポートIDに該当するレポート定義を削除する
            schedulingReportDefinitionDao.deleteById(reportId);
        }
        catch (PersistenceException pEx)
        {
            Throwable cause = pEx.getCause();
            if (cause instanceof SQLException)
            {
                SQLException sqlEx = (SQLException)cause;
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, sqlEx, sqlEx.getMessage());
            }
            else
            {
                LOGGER.log(LogMessageCodes.SQL_EXCEPTION, pEx, pEx.getMessage());
            }
        }

    }

    private void sendSignalDefinition(final SchedulingReportDefinitionDto schedulingDefinitionDto,
            final String type)
    {
        // 各クライアントにシグナル定義の追加を通知する。
        EventManager eventManager = EventManager.getInstance();
        WgpDataManager dataManager = eventManager.getWgpDataManager();
        ResourceSender resourceSender = eventManager.getResourceSender();
        if (dataManager != null && resourceSender != null)
        {
            List<TreeMenuDto> treeMenuDtoList = new ArrayList<TreeMenuDto>();
            /*treeMenuDtoList.add(this.convertSignalTreeMenu(signalDefinitionDto));*/
            resourceSender.send(treeMenuDtoList, type);
        }
    }

    private void updateMeasurementItemName(final String beforeItemName, final String afterItemName)
        throws SQLException
    {
        DatabaseManager dbMmanager = DatabaseManager.getInstance();
        String dbName = dbMmanager.getDataBaseName(1);

        JavelinMeasurementItemDao.updateMeasurementItemName(dbName, beforeItemName, afterItemName);
    }

}

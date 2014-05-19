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
package jp.co.acroquest.endosnipe.web.explorer.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletResponse;

import jp.co.acroquest.endosnipe.common.util.MessageUtil;
import jp.co.acroquest.endosnipe.report.Reporter;
import jp.co.acroquest.endosnipe.web.explorer.constants.ResponseConstants;
import jp.co.acroquest.endosnipe.web.explorer.dto.DashboardDto;
import jp.co.acroquest.endosnipe.web.explorer.dto.DashboardReportDefinitionDto;
import jp.co.acroquest.endosnipe.web.explorer.dto.MultipleResourceGraphDefinitionDto;
import jp.co.acroquest.endosnipe.web.explorer.dto.ReportDefinitionDto;
import jp.co.acroquest.endosnipe.web.explorer.dto.ResourceDto;
import jp.co.acroquest.endosnipe.web.explorer.dto.ResponseDto;
import jp.co.acroquest.endosnipe.web.explorer.dto.SchedulingReportDefinitionDto;
import jp.co.acroquest.endosnipe.web.explorer.entity.DashboardInfo;
import jp.co.acroquest.endosnipe.web.explorer.entity.ReportDefinition;
import jp.co.acroquest.endosnipe.web.explorer.entity.SchedulingReportDefinition;
import jp.co.acroquest.endosnipe.web.explorer.service.DashboardService;
import jp.co.acroquest.endosnipe.web.explorer.service.MultipleResourceGraphService;
import jp.co.acroquest.endosnipe.web.explorer.service.ReportService;
import jp.co.acroquest.endosnipe.web.explorer.template.meta.Resource;
import net.arnx.jsonic.JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * レポート出力機能のコントローラクラス。
 * 
 * @author miyasaka
 *
 */
@Controller
@RequestMapping("/report")
public class ReportController
{
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm";

    /** シグナル定義のサービスクラスのオブジェクト。 */
    @Autowired
    protected ReportService reportService_;

    /** ダッシュボードサービスクラスのオブジェクト */
    @Autowired
    protected DashboardService dashboardService_;

    /** 複数リソースグラフのサービスのオブジェクト */
    @Autowired
    protected MultipleResourceGraphService multipleGraphService_;

    /**
     * デフォルトコンストラクタ。
     */
    public ReportController()
    {
        //  new ReportData();

    }

    /**
     * to stop Thread
     */
    @PreDestroy
    public void destroy()
    {
        reportService_.stopThread();

    }

    /**
     * レポート出力の定義をすべて取得する。
     * 
     * @return 全てのレポート出力の定義
     */
    @RequestMapping(value = "/getAllDefinition", method = RequestMethod.POST)
    @ResponseBody
    public List<ReportDefinitionDto> getAllDefinition()
    {
        List<ReportDefinitionDto> reportDefinitionDtos = new ArrayList<ReportDefinitionDto>();

        reportDefinitionDtos = this.reportService_.getAllReport();

        return reportDefinitionDtos;
    }

    /**
     * get all scheduling definition data
     * @return schedule report definition dto
     */
    @RequestMapping(value = "/getAllScheduleDefinition", method = RequestMethod.POST)
    @ResponseBody
    public List<SchedulingReportDefinitionDto> getAllSchedulingDefinition()
    {
        List<SchedulingReportDefinitionDto> reportDefinitionDtos =
                new ArrayList<SchedulingReportDefinitionDto>();

        reportDefinitionDtos = this.reportService_.getAllSchedule(null);
        return reportDefinitionDtos;
    }

    /**
     * get all scheduling definition data related agent
     * @param nodeName for getting schedule 
     *        report only for related agent
     * @return schedule report definition dto
     */
    @RequestMapping(value = "/getAllScheduleDefinitionByAgent", method = RequestMethod.POST)
    @ResponseBody
    public List<SchedulingReportDefinitionDto> getAllScheduleDefinitionByAgent(
            @RequestParam(value = "nodeName") final String nodeName)
    {
        List<SchedulingReportDefinitionDto> reportDefinitionDtos =
                new ArrayList<SchedulingReportDefinitionDto>();

        reportDefinitionDtos = this.reportService_.getAllSchedule(nodeName);
        return reportDefinitionDtos;
    }

    /**
     * 指定したレポート対象名をキーに、レポート定義の一覧を取得する。
     * @param reportName 閾値判定の定義を一意に取得するためのシグナル名
     * @return 閾値判定の定義
     */
    @RequestMapping(value = "/getDefinitionByReportName", method = RequestMethod.POST)
    @ResponseBody
    public List<ReportDefinitionDto> getByTarget(
            @RequestParam(value = "reportName") final String reportName)
    {
        List<ReportDefinitionDto> reportDefinitionDtos =
                this.reportService_.getReportByReportName(reportName);
        return reportDefinitionDtos;
    }

    /**
     * レポート出力の定義を新規に追加する。
     * 
     * @param reportDefinition
     *            レポート出力定義のJSONデータ
     * @return 追加したレポート出力の定義
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ReportDefinitionDto addReportDefinition(
            @RequestParam(value = "reportDefinition") final String reportDefinition)
    {
        ReportDefinitionDto reportDefinitionDto =
                JSON.decode(reportDefinition, ReportDefinitionDto.class);
        boolean isExist = this.reportService_.checkReportName(reportDefinitionDto);
        if (isExist == true)
        {
            reportDefinitionDto.message_ = "duplicate";
            return reportDefinitionDto;
        }
        else
        {
            reportDefinitionDto.setStatus("Creating");
            this.reportService_.createReport(reportDefinitionDto);

            ReportDefinition definition =
                    this.reportService_.convertReportDefinition(reportDefinitionDto);

            // レポート定義をDBに登録する
            ReportDefinitionDto addedDefinitionDto =
                    this.reportService_.insertReportDefinition(definition);
            return addedDefinitionDto;
        }

    }

    /**
     * レポート出力の定義を新規に追加する。
     * 
     * @param reportDefinition
     *            レポート出力定義のJSONデータ
     * @return 追加したレポート出力の定義
     */
    @RequestMapping(value = "/addDashboard", method = RequestMethod.POST)
    @ResponseBody
    public ReportDefinitionDto addDashboardReportDefinition(
            @RequestParam(value = "reportDefinition") final String reportDefinition)
    {
        DashboardReportDefinitionDto dto =
                JSON.decode(reportDefinition, DashboardReportDefinitionDto.class);
        List<DashboardInfo> infoList = dashboardService_.getByName(dto.getDashboardName());

        DashboardInfo dashboardInfo = infoList.get(0);

        // リソースIDの一覧を取得する
        List<String> resourceIdList = getResourceIdList(dashboardInfo);

        // 対象リソースの正規表現一覧を取得する
        List<String> matchingPatternList =
                getMatchingPatternList(resourceIdList, dto.getClusterName());

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

        // 開始終了時刻の文字列をCalenderに変換する
        Date fromDate;
        Date toDate;
        try
        {
            fromDate = dateFormat.parse(dto.getReportTermFrom());
            toDate = dateFormat.parse(dto.getReportTermTo());
        }
        catch (ParseException ex)
        {
            return null;
        }
        Calendar fromCal = Calendar.getInstance();
        fromCal.setTime(fromDate);
        Calendar toCal = Calendar.getInstance();
        toCal.setTime(toDate);

        if (".*".equals(dto.getClusterName()))
        {
            dto.setClusterName("default");
        }

        // ReportDefinitionDtoを作成する
        ReportDefinitionDto reportDefinitionDto = new ReportDefinitionDto();
        reportDefinitionDto.setReportName("/" + dto.getClusterName() + "/" + dto.getReportName());
        reportDefinitionDto.setReportTermFrom(fromCal);
        reportDefinitionDto.setReportTermTo(toCal);
        reportDefinitionDto.setStatus("Creating");
        reportDefinitionDto.setTargetMeasurementPattern(matchingPatternList);
        reportDefinitionDto.setTargetMeasurementName(dto.getReportName());

        // レポートを作成する
        this.reportService_.createReport(reportDefinitionDto);

        ReportDefinition definition =
                this.reportService_.convertReportDefinition(reportDefinitionDto);

        // レポート定義をDBに登録する
        ReportDefinitionDto addedDefinitionDto =
                this.reportService_.insertReportDefinition(definition);

        return addedDefinitionDto;
    }

    /**
     * 対象リソースの正規表現一覧
     * @param resourceIdList グラフIDのリスト
     * @param clusterName 選択されているクラスタ名
     * @return グラフのターゲットを表す正規表現のリスト
     */
    private List<String> getMatchingPatternList(final List<String> resourceIdList,
            final String clusterName)
    {
        List<String> matchingPatternList = new ArrayList<String>();
        for (String graphName : resourceIdList)
        {
            MultipleResourceGraphDefinitionDto graphDto =
                    multipleGraphService_.getmultipleResourceGraphInfo(graphName);

            // 複数リソースグラフの系列が見つからない場合は単一グラフであるため、そのままグラフ名をリストに追加する
            if (graphDto == null)
            {
                matchingPatternList.add(graphName);
                continue;
            }

            // ${ClusterName}を選択しているクラスタ名に置換する
            String mathcingPattern = graphDto.getMeasurementItemPattern();
            mathcingPattern = mathcingPattern.replaceAll("\\$\\{ClusterName\\}", clusterName);
            matchingPatternList.add(mathcingPattern);
        }
        return matchingPatternList;
    }

    /**
     * リソースIDのリストを取得する
     * @param dashboardInfo ダッシュボード情報
     * @return リソースIDのリスト
     */
    private List<String> getResourceIdList(final DashboardInfo dashboardInfo)
    {
        List<String> resourceIdList = new ArrayList<String>();
        DashboardDto resources = JSON.decode(dashboardInfo.data, DashboardDto.class);
        for (ResourceDto resource : resources.getResources())
        {
            String objectName = resource.getObjectName();
            if (!objectName.equals(Resource.OBJ_NAME_MULTIPLE_GRAPH)
                    && !objectName.equals(Resource.OBJ_NAME_GRAPH))
            {
                continue;
            }
            String resourceId = resource.getResourceId();
            resourceIdList.add(resourceId);
        }
        return resourceIdList;
    }

    /**
     * This function is to overide previous report
     * @param reportDefinition
     *            レポート出力定義のJSONデータ
     * @return 追加したレポート出力の定義
     */
    @RequestMapping(value = "/addDuplicateReport", method = RequestMethod.POST)
    @ResponseBody
    public ReportDefinitionDto addDuplicateReportReportDefinition(
            @RequestParam(value = "reportDefinitionDto") final String reportDefinition)
    {
        ReportDefinitionDto reportDefinitionDto =
                JSON.decode(reportDefinition, ReportDefinitionDto.class);
        this.reportService_.createReport(reportDefinitionDto);
        return reportDefinitionDto;

    }

    /**
     * レポートをダウンロードする。
     * 
     * @param res {@link HttpServletResponse}オブジェクト
     * @param reportId レポートID
     * @return ファイル名
     * @throws IOException ファイルへの入出力エラー
     */
    @RequestMapping(value = "/download", method = RequestMethod.POST)
    @ResponseBody
    public String downloadReport(final HttpServletResponse res,
            @RequestParam(value = "reportId") final int reportId)
        throws IOException
    {
        ReportDefinitionDto reportDefinitionDto = this.reportService_.getReportByReportId(reportId);
        // ファイル名を取得する
        String fileName = createFileNameByDefinitionDto(reportDefinitionDto);

        this.reportService_.doRequest(res, fileName);
        return fileName;
    }

    /**
     * レポート名をキーにノードごと削除する。
     *
     * @param reportName
     *            レポート名
     * @return 削除したレポート情報
     */
    @RequestMapping(value = "/deleteByName", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteByName(
            @RequestParam(value = "reportName") final String reportName)
    {
        List<ReportDefinitionDto> definitionDtoList =
                this.reportService_.getReportByReportName(reportName);

        int definitionDtosLength = definitionDtoList.size();

        for (int index = 0; index < definitionDtosLength; index++)
        {
            ReportDefinitionDto definitionDto = definitionDtoList.get(index);
            String fileName = this.createFileNameByDefinitionDto(definitionDto);
            // ファイルを削除する
            this.reportService_.deleteReportFile(fileName);
        }

        this.reportService_.deleteReportDefinitionByName(reportName);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("reportName", reportName);

        return map;
    }

    /**
     * レポートIDをキーにレポート単体を削除する。
     *
     * @param reportId
     *            レポートID
     * @return 削除したレポート情報
     */
    @RequestMapping(value = "/deleteById", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteById(@RequestParam(value = "reportId") final int reportId)
    {
        ReportDefinitionDto reportDefinitionDto = this.reportService_.getReportByReportId(reportId);
        // ファイル名を取得する
        String fileName = createFileNameByDefinitionDto(reportDefinitionDto);
        // ファイルを削除する
        boolean isDeleteSuccess = this.reportService_.deleteReportFile(fileName);

        Map<String, Object> map = new HashMap<String, Object>();

        if (isDeleteSuccess)
        {
            this.reportService_.deleteReportDefinitionById(reportId);
        }

        map.put("reportId", reportId);
        if (isDeleteSuccess)
        {
            map.put(ResponseConstants.RESULT, ResponseConstants.RESULT_SUCCESS);
        }
        else
        {
            map.put(ResponseConstants.RESULT, ResponseConstants.RESULT_FAILURE);
        }

        return map;
    }

    /**
     * レポートIDからファイル名を作成する。
     * 
     * @param reportDefinitionDto レポート出力定義
     * @return ファイル名
     */
    private String createFileNameByDefinitionDto(final ReportDefinitionDto reportDefinitionDto)
    {

        String reportNamePath = reportDefinitionDto.getReportName();
        String[] reportNameSplitList = reportNamePath.split("/");
        int reportNameSplitLength = reportNameSplitList.length;
        String reportName = reportNameSplitList[reportNameSplitLength - 1];

        Calendar fmTime = reportDefinitionDto.getReportTermFrom();
        Calendar toTime = reportDefinitionDto.getReportTermTo();

        SimpleDateFormat format = new SimpleDateFormat(Reporter.TIME_FORMAT);
        String start = format.format(fmTime.getTime());
        String end = format.format(toTime.getTime());

        // ダウンロードするZIPファイル名を作成する
        String fileName = reportName + "_" + start + "-" + end + ".zip";

        return fileName;
    }

    /**
    * 
    * @param schedulingReportDefinition is used
    * @return is used
    */
    @RequestMapping(value = "/addscheduling", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto addSchedulingReport(
            @RequestParam(value = "schedulingReportDefinition") final String schedulingReportDefinition)
    {
        ResponseDto responseDto = new ResponseDto();
        SchedulingReportDefinitionDto schedulingReportDefinitionDto =
                JSON.decode(schedulingReportDefinition, SchedulingReportDefinitionDto.class);

        long reportId = schedulingReportDefinitionDto.getReportId();
        String reportName = schedulingReportDefinitionDto.getReportName();
        boolean hasSameSignalName = this.reportService_.hasSameReportName(reportId, reportName);
        if (hasSameSignalName)
        {
            String errorMessage = MessageUtil.getMessage("WEWD0141", reportName);
            responseDto.setResult(ResponseConstants.RESULT_FAILURE);
            responseDto.setMessage(errorMessage);
            return responseDto;
        }
        SchedulingReportDefinition schedulingDefinition =
                this.reportService_.convertSchedulingReportDefinition(schedulingReportDefinitionDto);

        // DBに追加する
        SchedulingReportDefinitionDto addedDefinitionDto =
                this.reportService_.insertSchedulingReport(schedulingDefinition);

        responseDto.setData(addedDefinitionDto);
        responseDto.setResult(ResponseConstants.RESULT_SUCCESS);
        return responseDto;
    }

    /**
     * delete data by using id.
     * @param reportId get from database
     * @return map
     */
    @RequestMapping(value = "/deleteScheduleById", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteSchedulingReport(
            @RequestParam(value = "reportId") final int reportId)
    {
        this.reportService_.deleteSchduleReportById(reportId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("reportId", reportId);
        return map;
    }

    /**
     * Edit scheduling data.
     * @param schedulingReportDefinition get from database.
     * @return response dto.
     */
    @RequestMapping(value = "/schedulingEdit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto schedulingEdit(
            @RequestParam(value = "schedulingReportDefinition") final String schedulingReportDefinition)
    {
        ResponseDto responseDto = new ResponseDto();
        SchedulingReportDefinitionDto schedulingReportDefinitionDto =
                JSON.decode(schedulingReportDefinition, SchedulingReportDefinitionDto.class);
        boolean hasSameSignalName =
                this.reportService_.hasSameReportName(schedulingReportDefinitionDto.getReportId(),
                                                      schedulingReportDefinitionDto.getReportName());
        if (hasSameSignalName)
        {
            String errorMessage =
                    MessageUtil.getMessage("WEWD0141",
                                           schedulingReportDefinitionDto.getReportName());
            responseDto.setResult(ResponseConstants.RESULT_FAILURE);
            responseDto.setMessage(errorMessage);
            return responseDto;
        }
        SchedulingReportDefinition signalInfo =
                this.reportService_.convertSchedulingReportDefinition(schedulingReportDefinitionDto);

        // DBに登録されている定義を更新する
        SchedulingReportDefinitionDto updatedDefinitionDto =
                this.reportService_.updateSchedulingInfo(signalInfo,
                                                         schedulingReportDefinitionDto.getBeforeReportName());
        responseDto.setResult(ResponseConstants.RESULT_SUCCESS);
        responseDto.setData(updatedDefinitionDto);

        return responseDto;
    }
}

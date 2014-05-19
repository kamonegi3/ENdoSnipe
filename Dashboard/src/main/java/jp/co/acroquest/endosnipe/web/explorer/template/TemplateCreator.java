/*
 * Copyright (c) 2004-2014 Acroquest Technology Co., Ltd. All Rights Reserved.
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
package jp.co.acroquest.endosnipe.web.explorer.template;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import jp.co.acroquest.endosnipe.data.entity.JavelinMeasurementItem;
import jp.co.acroquest.endosnipe.web.explorer.dto.MultipleResourceGraphDefinitionDto;
import jp.co.acroquest.endosnipe.web.explorer.dto.SignalDefinitionDto;
import jp.co.acroquest.endosnipe.web.explorer.dto.SummarySignalDefinitionDto;
import jp.co.acroquest.endosnipe.web.explorer.dto.TreeMenuDto;
import jp.co.acroquest.endosnipe.web.explorer.entity.DashboardInfo;
import jp.co.acroquest.endosnipe.web.explorer.entity.MultipleResourceGraphInfo;
import jp.co.acroquest.endosnipe.web.explorer.entity.SignalInfo;
import jp.co.acroquest.endosnipe.web.explorer.service.DashboardService;
import jp.co.acroquest.endosnipe.web.explorer.service.MultipleResourceGraphService;
import jp.co.acroquest.endosnipe.web.explorer.service.SignalService;
import jp.co.acroquest.endosnipe.web.explorer.service.SummarySignalService;
import jp.co.acroquest.endosnipe.web.explorer.service.TreeMenuService;
import jp.co.acroquest.endosnipe.web.explorer.template.converter.ResourceConvertUtil;
import jp.co.acroquest.endosnipe.web.explorer.template.converter.TemplateConvertUtil;
import jp.co.acroquest.endosnipe.web.explorer.template.meta.Property;
import jp.co.acroquest.endosnipe.web.explorer.template.meta.Resource;
import jp.co.acroquest.endosnipe.web.explorer.template.meta.Template;
import net.arnx.jsonic.JSON;

/**
 * テンプレートを生成するクラス
 * @author kamo
 *
 */
public class TemplateCreator
{

    private static final String CLUSTER_NAME = "${ClusterName}";

    /** 空白文字 */
    private static final String BLANK = "";

    /** 単体グラフを表すシグナルタイプ */
    private static final Object SIGNAL_TYPE_SINGLE = "SINGLE";

    /** 複数グラフを表すシグナルタイプ */
    private static final Object SIGNAL_TYPE_SUMMARY = "SUMMARY";

    /** ミリ秒を秒に変換する定数 */
    private static final int MS_TO_SEC = 1000;

    /** 複数リソースグラフのサービス */
    private final MultipleResourceGraphService graphService_;

    /** ダッシュボードのサービス */
    private final DashboardService dashboardService_;

    /** 単体シグナルのサービス */
    private final SignalService signalService_;

    /** サマリシグナルのサービス */
    private final SummarySignalService summarySignalService_;

    /** ツリーメニューのサービス */
    private final TreeMenuService treeMenuService_;

    /** シグナルの連番 */
    private int signalCount_;

    /**
     * コンストラクタ
     * @param graphService GraphServiceのインスタンス
     * @param dashboardService DashboardServiceのインスタンス
     * @param signalService SignalServiceのインスタンス
     * @param summarySignalService SummarySignalServiceのインスタンス
     */
    public TemplateCreator(final MultipleResourceGraphService graphService,
            final DashboardService dashboardService, final SignalService signalService,
            final SummarySignalService summarySignalService, final TreeMenuService treeMenuService)
    {
        this.graphService_ = graphService;
        this.dashboardService_ = dashboardService;
        this.signalService_ = signalService;
        this.summarySignalService_ = summarySignalService;
        this.treeMenuService_ = treeMenuService;
    }

    /**
     * テンプレートを生成する
     * @param name テンプレートの名前
     * @param template テンプレートのデータ
     */
    public void create(final String name, final Template template)
    {
        //既にDBに登録されているシグナルとグラフを削除する
        deleteGraphs(name);
        deleteSignals(name);

        List<Resource> resources = template.getResources();
        for (Resource resource : resources)
        {
            //グラフの作成
            if (Resource.OBJ_NAME_MULTIPLE_GRAPH.equals(resource.getObjectName()))
            {
                createGraph(name, resource.getProperty());
                continue;
            }

            //シグナルの作成
            if (Resource.OBJ_NAME_SIGNAL.equals(resource.getObjectName()))
            {
                // シグナルの正規表現が構文エラーの場合はテンプレートの作成を中断する。
                try
                {
                    createSignal(name, resource.getProperty());
                }
                catch (PatternSyntaxException e)
                {
                    return;
                }
                continue;
            }
        }

        //ダッシュボードの生成
        createDashboard(name, template);
    }

    /**
     * ダッシュボードを生成する
     * @param name テンプレート名
     * @param template テンプレートオブジェクト
     */
    private void createDashboard(final String name, final Template template)
    {
        //テンプレート→Map→JSON変換
        Map<String, Object> templateMap = TemplateConvertUtil.convert(name, template);
        String json = JSON.encode(templateMap);

        //DashboardをDBに登録
        DashboardInfo dashboardInfo = new DashboardInfo();
        dashboardInfo.data = json;
        dashboardInfo.name = name;

        //既にDBに存在する場合は更新、無い場合は新規作成
        List<DashboardInfo> existDashboard = dashboardService_.getByName(name);
        if (existDashboard.size() == 0)
        {
            dashboardService_.insert(dashboardInfo);
        }
        else
        {
            dashboardService_.update(dashboardInfo, true);
        }
    }

    /**
     * 既にDBに登録されているシグナル・サマリシグナルを削除する
     * @param name テンプレート名
     */
    private void deleteSignals(final String name)
    {
        String treeName = TemplateConvertUtil.getTreeName(name);
        summarySignalService_.deleteChildren(treeName);
        signalService_.deleteChildren(treeName);
    }

    /**
     * 既にDBに登録されているグラフを削除する
     * @param name テンプレート名
     */
    private void deleteGraphs(final String name)
    {
        String treeName = TemplateConvertUtil.getTreeName(name);
        graphService_.deleteMultipleResourceGraphs(treeName);
    }

    /**
     * テンプレートで定義したグラフを作成する
     * @param name テンプレートの名前
     * @param property グラフのプロパティ
     */
    private void createGraph(final String name, final Property property)
    {
        //リソースIDが空文字でない場合は何もしない。
        if (!BLANK.equals(property.getResourceId()))
        {
            return;
        }

        //MultipleResourceGraphInfo を作成する
        MultipleResourceGraphInfo graphInfo = new MultipleResourceGraphInfo();
        graphInfo.measurementItemIdList_ = BLANK;
        graphInfo.measurementItemPattern_ = property.getTarget();
        graphInfo.multipleResourceGraphName_ =
                ResourceConvertUtil.getTreeName(name, Resource.OBJ_NAME_MULTIPLE_GRAPH, property);

        MultipleResourceGraphDefinitionDto existDto =
                graphService_.getmultipleResourceGraphInfo(graphInfo.multipleResourceGraphName_);
        //既に同一の名前のグラフが存在する場合は更新、無い場合は新規作成する。
        if (existDto == null)
        {
            graphService_.insertMultipleResourceGraphInfo(graphInfo);
        }
        else
        {
            graphService_.updatemultipleResourceGraphInfo(graphInfo);
        }

    }

    /**
     * シグナルを生成する
     * @param name テンプレート名
     * @param property テンプレートオブジェクト
     */
    private void createSignal(final String name, final Property property)
        throws PatternSyntaxException
    {
        //リソースIDが空文字でない場合は何もしない
        Property signalProperty = property.getSignal();
        if (!BLANK.equals(signalProperty.getResourceId()))
        {
            return;
        }

        String treeName =
                ResourceConvertUtil.getTreeName(name, Resource.OBJ_NAME_SIGNAL, signalProperty);

        //単一シグナルを生成
        if (SIGNAL_TYPE_SINGLE.equals(signalProperty.getObjectType()))
        {
            createSingleSignal(signalProperty, treeName);
            return;
        }

        //サマリシグナルを生成
        if (SIGNAL_TYPE_SUMMARY.equals(signalProperty.getObjectType()))
        {
            createSummarySignal(signalProperty, treeName);
            return;
        }
    }

    /**
     * サマリシグナルを生成する
     * @param signalProperty シグナルのプロパティ
     * @param treeName 登録するツリー名
     */
    private void createSummarySignal(final Property signalProperty, final String treeName)
    {
        String targetRe = signalProperty.getTarget();
        List<String> targets = getSummarySignalNames(targetRe);
        List<String> signalList = new ArrayList<String>();

        for (String target : targets)
        {
            String name = treeName + "_" + (signalCount_++);
            signalProperty.setTarget(target);
            createSingleSignal(signalProperty, name);
            signalList.add(name);
        }

        SummarySignalDefinitionDto dto = new SummarySignalDefinitionDto();
        dto.summarySignalName_ = treeName;
        dto.signalList_ = signalList;
        dto.summarySignalType_ = signalProperty.getMethod();

        summarySignalService_.insertSummarySignalDefinition(dto);
    }

    /**
     * 正規表現で指定したサマリシグナル名のリストを取得する
     * @param re 対象を正規表現で設定する
     * @return サマリシグナル名のリスト
     */
    private List<String> getSummarySignalNames(final String re)
    {
        List<String> names = new ArrayList<String>();

        List<JavelinMeasurementItem> items = null;
        try
        {
            items = graphService_.getMeasurementItemName(re);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return names;
        }

        for (JavelinMeasurementItem item : items)
        {
            names.add(item.itemName);
        }
        return names;
    }

    /**
     * 単一シグナルを生成する
     * @param signalProperty シグナルのプロパティ
     * @param treeName 登録するツリー名
     */
    private void createSingleSignal(final Property signalProperty, final String treeName)
    {
        //SignalInfoを作成
        SignalInfo signalInfo = new SignalInfo();
        signalInfo.escalationPeriod = signalProperty.getPeriod() * MS_TO_SEC;
        signalInfo.level = signalProperty.getLevel();
        signalInfo.matchingPattern = signalProperty.getTarget();
        signalInfo.patternValue = signalProperty.getThreshold();
        signalInfo.signalName = treeName;

        // シグナル名に ${ClusterName} を含まない場合はそのまま登録する
        if (signalInfo.signalName.indexOf(CLUSTER_NAME) == -1)
        {
            addSignal(signalInfo);
            return;
        }

        // シグナル名に ${ClusterName} を含む場合、クラスタの数分だけシグナルを作る
        List<TreeMenuDto> topNodeList = treeMenuService_.getTopNodes();
        TreeMenuDto wildcard = new TreeMenuDto();
        wildcard.setData(".*");
        topNodeList.add(wildcard);

        String matchingPattern = signalInfo.matchingPattern;
        String signalName = signalInfo.signalName;

        for (TreeMenuDto treeMenuDto : topNodeList)
        {
            String clusterName = treeMenuDto.getData();
            signalInfo.matchingPattern = matchingPattern.replace(CLUSTER_NAME, clusterName);
            signalInfo.signalName = signalName.replace(CLUSTER_NAME, clusterName);

            addSignal(signalInfo);
        }

    }

    /**
     * シグナルを追加または更新する
     * @param signalInfo 追加または更新するシグナル情報
     */
    private void addSignal(final SignalInfo signalInfo)
    {
        //正規表現の構文が誤っている場合は例外を投げる
        Pattern.compile(signalInfo.matchingPattern);

        //既に同一名のシグナルがある場合は更新、そうでない場合は新規作成
        SignalDefinitionDto existDto = signalService_.getSignalInfo(signalInfo.signalName);
        if (existDto == null)
        {
            signalService_.insertSignalInfo(signalInfo);
        }
        else
        {
            signalInfo.signalId = existDto.getSignalId();
            signalService_.updateSignalInfo(signalInfo);
        }
    }
}

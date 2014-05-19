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
package jp.co.acroquest.endosnipe.collector.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import jp.co.acroquest.endosnipe.collector.LogMessageCodes;
import jp.co.acroquest.endosnipe.collector.config.DataCollectorConfig;
import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.data.dto.PerfDoctorResultDto;
import jp.co.acroquest.endosnipe.perfdoctor.WarningUnit;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.ImmutableSettings.Builder;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

/**
 * ElasticSearchに関するデータを扱うユーティリティクラス。
 * @author fujii
 *
 */
public class ElasticSearchUtil
{
    /** クラスタ名を設定する位置 */
    private static final int CLUSTER_NAME_POSITION = 1;

    /** ホスト名を設定する位置 */
    private static final int HOST_NAME_POSITION = 2;

    /** エージェント名を設定する位置 */
    private static final int AGENT_NAME_POSITION = 3;

    /** localhostを表す定数 */
    private static final String HOST_LOCALHOST = "localhost";

    /** 計測項目名の区切りパターン */
    private static final Pattern ITEMNAME_SPLIT_PATTERN = Pattern.compile("/");

    /** ロガー */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger
        .getLogger(ElasticSearchUtil.class);

    /** 使用するノード **/
    private static Node node__;
	
    /**
     * インスタンス化を防止するユーティリティクラス。
     */
    private ElasticSearchUtil()
    {
        // Do Nothing.
    }

    /**
     * ElasticSearchにPerformanceDoctorの結果を送信する。
     * @param config DataCollectorConfigオブジェクト
     * @param warnings PerformanceDoctorの診断結果
     */
    public static void sendElasticSearch(final DataCollectorConfig config,
        final List<WarningUnit> warnings)
    {
        boolean esAvailable = config.isEsAvailable();

        if (esAvailable == false)
        {
            return;
        }

        if (warnings.size() <= 0)
        {
            return;
        }
        Client client = null;
        try
        {
            String esClusterName = config.getEsClusterName();
            String esHostName = config.getEsHostName();
            int esHostPort = config.getEsHostPort();
            Builder clusterSettings =
                ImmutableSettings.settingsBuilder().put("cluster.name", esClusterName);
            if (esHostName.equals("") || HOST_LOCALHOST.equals(esHostName))
            {
                if (node__ == null)
                {
                    node__ = NodeBuilder.nodeBuilder().settings(clusterSettings).node();
                }
                client = node__.client();
            }
            else
            {
                client =
                    new TransportClient(clusterSettings)
                        .addTransportAddress(new InetSocketTransportAddress(esHostName, esHostPort));
            }

            for (WarningUnit warning : warnings)
            {
                Timestamp timestamp = new Timestamp(warning.getStartTime());
                String description = warning.getDescription();
                String level = warning.getLevel();
                String className = warning.getClassName();
                String methodName = warning.getMethodName();
                String measurementItemName = warning.getMeasurementItemName();

                String[] splittedItemName = ITEMNAME_SPLIT_PATTERN.split(measurementItemName);
                String clusterName = splittedItemName[CLUSTER_NAME_POSITION];
                String address = splittedItemName[HOST_NAME_POSITION];
                String agentName = splittedItemName[AGENT_NAME_POSITION];

                String esIndexPrefix = config.getEsIndexPrefix();
                String esIndexPostfixPattern = config.getEsIndexPostfix();
                DateFormat indexFormat = new SimpleDateFormat(esIndexPostfixPattern);
                String index = "";
                // インデックスは、<接頭辞> + <接尾辞をフォーマットで変換した文字列>とする。
                // ただし、接尾辞のフォーマットが不正な場合は、接尾辞をそのまま結合する。
                try
                {
                    index = esIndexPrefix + indexFormat.format(timestamp.getTime());
                }
                catch (IllegalArgumentException ex)
                {
                    index = esIndexPrefix + esIndexPostfixPattern;
                }
                String esType = config.getEsType();

                Map<String, Object> jsonDocument = new HashMap<String, Object>();

                jsonDocument.put("OCCURRENCE_TIME", timestamp.toString());
                jsonDocument.put("DESCRIPTION", description);
                jsonDocument.put("LEVEL", level);
                jsonDocument.put("CLASS_NAME", className);
                jsonDocument.put("METHOD_NAME", methodName);
                jsonDocument.put("MEASUREMENT_ITEM_NAME", measurementItemName);
                jsonDocument.put("CLUSTER_NAME", clusterName);
                jsonDocument.put("ADDRESS", address);
                jsonDocument.put("AGENT_NAME", agentName);
                jsonDocument.put("ID", warning.getId());
                client.prepareIndex(index, esType).setSource(jsonDocument).execute().actionGet();

            }

        }
        catch (Exception ex)
        {
            LOGGER.log(LogMessageCodes.FAIL_SEND_ELASTIC_SEARCH, ex);
        }
        finally
        {
            if (client != null)
            {
                client.close();
            }
        }
    }

    /**
     * ElasticSearchにPerformanceDoctorの結果を送信する。
     * @param config DataCollectorConfigオブジェクト
     * @param resultDto PerformanceDoctorの診断結果
     */
    public static void sendElasticSearch(final DataCollectorConfig config,
        final PerfDoctorResultDto resultDto)
    {
        boolean esAvailable = config.isEsAvailable();

        if (esAvailable == false)
        {
            return;
        }
        if (resultDto == null)
        {
            return;
        }

        Client client = null;
        try
        {
            String esClusterName = config.getEsClusterName();
            String esHostName = config.getEsHostName();
            int esHostPort = config.getEsHostPort();
            Builder clusterSettings =
                ImmutableSettings.settingsBuilder().put("cluster.name", esClusterName);
            if (esHostName.equals("") || HOST_LOCALHOST.equals(esHostName))
            {
                if (node__ == null)
                {
                    node__ = NodeBuilder.nodeBuilder().settings(clusterSettings).node();
                }
                client = node__.client();
            }
            else
            {
                client =
                    new TransportClient(clusterSettings)
                        .addTransportAddress(new InetSocketTransportAddress(esHostName, esHostPort));
            }

            Timestamp timestamp = resultDto.getOccurrenceTime();
            String description = resultDto.getDescription();
            String level = resultDto.getLevel();
            String className = resultDto.getClassName();
            String methodName = resultDto.getMethodName();
            String measurementItemName = resultDto.getMeasurementItemName();

            String[] splittedItemName = ITEMNAME_SPLIT_PATTERN.split(measurementItemName);
            String clusterName = splittedItemName[CLUSTER_NAME_POSITION];
            String address = splittedItemName[HOST_NAME_POSITION];
            String agentName = splittedItemName[AGENT_NAME_POSITION];

            String esIndexPrefix = config.getEsIndexPrefix();
            String esIndexPostfixPattern = config.getEsIndexPostfix();
            DateFormat indexFormat = new SimpleDateFormat(esIndexPostfixPattern);
            String index = "";
            // インデックスは、<接頭辞> + <接尾辞をフォーマットで変換した文字列>とする。
            // ただし、接尾辞のフォーマットが不正な場合は、接尾辞をそのまま結合する。
            try
            {
                index = esIndexPrefix + indexFormat.format(timestamp.getTime());
            }
            catch (IllegalArgumentException ex)
            {
                index = esIndexPrefix + esIndexPostfixPattern;
            }
            String esType = config.getEsType();

            Map<String, Object> jsonDocument = new HashMap<String, Object>();

            jsonDocument.put("OCCURRENCE_TIME", timestamp.toString());
            jsonDocument.put("DESCRIPTION", description);
            jsonDocument.put("LEVEL", level);
            jsonDocument.put("CLASS_NAME", className);
            jsonDocument.put("METHOD_NAME", methodName);
            jsonDocument.put("MEASUREMENT_ITEM_NAME", measurementItemName);
            jsonDocument.put("CLUSTER_NAME", clusterName);
            jsonDocument.put("ADDRESS", address);
            jsonDocument.put("AGENT_NAME", agentName);
            jsonDocument.put("ID", "THRESHOLD");
            client.prepareIndex(index, esType).setSource(jsonDocument).execute().actionGet();

        }
        catch (Exception ex)
        {
            LOGGER.log(LogMessageCodes.FAIL_SEND_ELASTIC_SEARCH, ex);
        }
        finally
        {
            if (client != null)
            {
                client.close();
            }
        }

    }

}

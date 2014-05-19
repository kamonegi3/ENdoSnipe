/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2012 Acroquest Technology Co.,Ltd.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package jp.co.acroquest.endosnipe.web.explorer.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import jp.co.acroquest.endosnipe.common.util.MessageUtil;
import jp.co.acroquest.endosnipe.web.explorer.constants.ResponseConstants;
import jp.co.acroquest.endosnipe.web.explorer.dto.ResponseDto;
import jp.co.acroquest.endosnipe.web.explorer.dto.SignalDefinitionDto;
import jp.co.acroquest.endosnipe.web.explorer.entity.SignalInfo;
import jp.co.acroquest.endosnipe.web.explorer.manager.ResourceSender;
import jp.co.acroquest.endosnipe.web.explorer.service.SignalService;
import jp.co.acroquest.endosnipe.web.explorer.service.TreeMenuService;
import net.arnx.jsonic.JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wgp.manager.WgpDataManager;

/**
 * 閾値判定機能のコントローラークラス。
 * 
 * @author miyasaka
 *
 */
@Controller
@RequestMapping("/signal")
public class SignalController
{
    /** Wgpのデータを管理するクラス。 */
    @Autowired
    protected WgpDataManager wgpDataManager;

    /** リソースを送信するクラスのオブジェクト。 */
    @Autowired
    protected ResourceSender resourceSender;

    /** シグナル定義のサービスクラスのオブジェクト。 */
    @Autowired
    protected SignalService signalService;

    /** ツリーメニューのサービスクラスのオブジェクト。 */
    @Autowired
    protected TreeMenuService treeMenuService;

    /** クラスタ名を表す変数 */
    private static final String CLUSTER_NAME = "${ClusterName}";

    /**
     * コンストラクタ。
     */
    public SignalController()
    {

    }

    /**
     * 閾値判定の定義をすべて取得する。
     * 
     * @return 全ての閾値判定の定義
     */
    @RequestMapping(value = "/getAllDefinition", method = RequestMethod.POST)
    @ResponseBody
    public List<SignalDefinitionDto> getAllDefinition()
    {
        List<SignalDefinitionDto> signalDefinitionDtos = new ArrayList<SignalDefinitionDto>();

        signalDefinitionDtos = signalService.getAllSignal();

        return signalDefinitionDtos;
    }

    /**
     * 閾値判定の定義を新規に追加する。
     *
     * @param signalDefinition
     *            閾値判定の定義のJSONデータ
     * @return 追加した閾値判定の定義
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto add(@RequestParam(value = "signalDefinition") final String signalDefinition)
    {
        ResponseDto responseDto = new ResponseDto();
        SignalDefinitionDto signalDefinitionDto =
                JSON.decode(signalDefinition, SignalDefinitionDto.class);

        String signalName = signalDefinitionDto.getSignalName();
        boolean hasSameSignalName = this.signalService.hasSameSignalName(signalName);
        if (hasSameSignalName)
        {
            String errorMessage = MessageUtil.getMessage("WEWD0131", signalName);
            responseDto.setResult(ResponseConstants.RESULT_FAILURE);
            responseDto.setMessage(errorMessage);
            return responseDto;
        }

        // シグナルを追加する
        SignalDefinitionDto addedDefinitionDto = addSignals(signalDefinitionDto);

        // 結果がnullの場合は失敗のレスポンスを返す。
        if (addedDefinitionDto == null)
        {
            String errorMessage = MessageUtil.getMessage("WEWD0132", signalName);
            responseDto.setResult(ResponseConstants.RESULT_FAILURE);
            responseDto.setMessage(errorMessage);
            return responseDto;
        }

        responseDto.setData(addedDefinitionDto);
        responseDto.setResult(ResponseConstants.RESULT_SUCCESS);
        return responseDto;
    }

    /**
     * マッチングパターンに${ClusterName}を含む場合、各クラスタのシグナルを追加する。
     * @param signalDefinitionDto 
     * @return {@link SignalDefinitionDto}オブジェクト。
     */
    private SignalDefinitionDto addSignals(final SignalDefinitionDto signalDefinitionDto)
    {

        String matchingPattern = signalDefinitionDto.getMatchingPattern();

        // 正規表現エラーの場合はMatchingPatternが不正な値であるため、nullを返す。
        try
        {
            Pattern.compile(matchingPattern);
        }
        catch (PatternSyntaxException e)
        {
            return null;
        }

        SignalInfo signalInfo = this.signalService.convertSignalInfo(signalDefinitionDto);
        SignalDefinitionDto addedDefinitionDto = this.signalService.insertSignalInfo(signalInfo);
        return addedDefinitionDto;

    }

    /**
     * 閾値判定の定義を取得する。
     * @param signalName 閾値判定の定義を一意に取得するためのシグナル名
     * @return 閾値判定の定義
     */
    @RequestMapping(value = "/getDefinition", method = RequestMethod.POST)
    @ResponseBody
    public SignalDefinitionDto get(@RequestParam(value = "signalName") final String signalName)
    {
        SignalDefinitionDto signalDefinition = this.signalService.getSignalInfo(signalName);
        return signalDefinition;
    }

    /**
     * 閾値判定の定義を編集する。
     *
     * @param signalDefinition
     *            閾値判定の定義のJSONデータ
     * @return 編集後の閾値判定の定義
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto edit(@RequestParam(value = "signalDefinition") final String signalDefinition)
    {
        ResponseDto responseDto = new ResponseDto();
        SignalDefinitionDto signalDefinitionDto =
                JSON.decode(signalDefinition, SignalDefinitionDto.class);

        long signalId = signalDefinitionDto.getSignalId();
        String signalName = signalDefinitionDto.getSignalName();
        boolean hasSameSignalName = this.signalService.hasSameSignalName(signalId, signalName);

        //　名前が重複する場合は更新に失敗する
        if (hasSameSignalName)
        {
            String errorMessage = MessageUtil.getMessage("WEWD0131", signalName);
            responseDto.setResult(ResponseConstants.RESULT_FAILURE);
            responseDto.setMessage(errorMessage);
            return responseDto;
        }

        // 正規表現が不正の場合は更新に失敗する
        try
        {
            Pattern.compile(signalDefinitionDto.getMatchingPattern());
        }
        catch (PatternSyntaxException e)
        {
            String errorMessage = MessageUtil.getMessage("WEWD0132", signalName);
            responseDto.setResult(ResponseConstants.RESULT_FAILURE);
            responseDto.setMessage(errorMessage);
            return responseDto;
        }

        SignalInfo signalInfo = this.signalService.convertSignalInfo(signalDefinitionDto);

        // DBに登録されている定義を更新する
        SignalDefinitionDto updatedDefinitionDto = this.signalService.updateSignalInfo(signalInfo);
        responseDto.setResult(ResponseConstants.RESULT_SUCCESS);
        responseDto.setData(updatedDefinitionDto);

        return responseDto;
    }

    /**
     * 閾値判定のシグナルを削除する。
     *
     * @param signalName
     *            閾値判定のシグナル名
     * @return 削除した閾値判定のシグナル名
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto delete(@RequestParam(value = "signalName") final String signalName)
    {
        this.signalService.deleteSignalInfo(signalName);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("signalName", signalName);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setResult(ResponseConstants.RESULT_SUCCESS);
        responseDto.setData(map);
        return responseDto;
    }
}

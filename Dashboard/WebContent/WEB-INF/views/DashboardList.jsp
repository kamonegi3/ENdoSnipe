<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../include/ext/javaScriptInclude.jsp"%>
<%@ include file="../include/DashboardListInclude.jsp"%>
<%@ include file="dialog/ReportDialog.jsp"%>
<%@ include file="common/Header.jsp"%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/common/common.css"
	type="text/css" media="all">
</head>
<title>ENdoSnipe Dashboard</title>
<body id="main" oncontextmenu="return false;" onload="self.focus();">
	<div id="persArea"></div>
	<input id="treeData" type="hidden" value='${treeData}' />
	<form:form modelAttribute="dashboardListForm">
		<!-- ダッシュボードモード(運用or編集) -->
		<form:hidden path="dashboardMode" />

		<!-- ResourceTreeViewの展開状態 -->
		<form:hidden path="treeViewOpenNodeData" />

		<!-- 表示しているツリー名 -->
		<form:hidden path="displayTreeArea" />

		<!-- 選択しているResourceTreeViewのID -->
		<form:hidden path="selectedTreeId" />

		<!-- 選択しているResourceDashboardListViewのID -->
		<form:hidden path="selectedDashboardListId" />
	</form:form>
	<script>
		var appView = new ENS.AppView();
	</script>
	<script src="<%=request.getContextPath()%>/resources/js/common/user.js" type="text/javaScript"></script>
	<script type="text/javascript">

		// ダッシュボードモードの取得。
		var dashboardMode = "";
		if($("#dashboardMode").val() == ENS.dashboard.mode.OPERATE){
			dashboardMode = ENS.dashboard.mode.OPERATE;
		}else{
			dashboardMode = ENS.dashboard.mode.EDIT;
			$("#dashboardMode").attr("value", ENS.dashboard.mode.EDIT);
		}

		var perspectiveView

		// 運用モード
		// ・ダッシュボードエリア
		// メニューエリア
		if(dashboardMode == ENS.dashboard.mode.OPERATE){

			var viewArea1 = {
					width : 1200,
					height : 62,
					rowspan : 1,
					colspan : 1,
					css : {
						"overflow" : "hidden",
						"padding-top" : "10px",
						"z-index" : "30"
					}
				};

			var viewArea2 = {
					width : 1200,
					height : 710,
					rowspan : 1,
					colspan : 1
				};

			var table = [ [ new wgp.PerspectiveModel(viewArea1)],
			            [ new wgp.PerspectiveModel(viewArea2)] ];
			perspectiveView = new wgp.PerspectiveView({
				id : "persArea",
				collection : table,
				minimum : false,
				close : false
			});

			// TODO WGPを改修し、barを非表示にする。
			$('#persArea_bar_1_0').hide();
			$('#persArea_bar_2_0').hide();

			perspectiveView.dropView("persArea_drop_0_0", "range_area", "DashboardView");
			perspectiveView.dropView("persArea_drop_1_0", "contents_area", "DashboardView");
			
			$("#persArea_drop_0_0").resizable("disable");

			// リソースリスト・ダッシュボードリスト・レポート出力ダイアログ用の領域を別に用意する。
			var treeArea = $("<div id='tree_area'></div>");
			treeArea.appendTo("body");
			treeArea.hide();

			var listArea = $("<div id='list_area'><div>");
			listArea.appendTo("body");
			listArea.hide();

		// 編集モード
		// ・ツリーエリア(リソースリスト、ダッシュボードリスト)
		// ・ダッシュボードエリア
		// ・メニューエリア
		}else {

			var viewArea1 = {
				width : 300,
				height : 800,
				rowspan : 2,
				colspan : 1
			};
			
			var viewArea2 = {
					width : 900,
					height : 67,
					rowspan : 1,
					colspan : 1,
					css : {
						"overflow" : "hidden",
						"padding-top" : "10px",
						"padding-left" : "10px",
						"z-index" : "30"
					}
				};
			
			var viewArea3 = {
				width : 900,
				height : 733,
				rowspan : 1,
				colspan : 1
			};

			var table = [ [ new wgp.PerspectiveModel(viewArea1),
					new wgp.PerspectiveModel(viewArea2) ],
					[new wgp.PerspectiveModel(viewArea3)]
					];
			perspectiveView = new wgp.PerspectiveView({
				id : "persArea",
				collection : table,
				minimum : false,
				close : false
			});

			// TODO WGPを改修し、barを非表示にする。
			$('#persArea_bar_1_0').hide();

			perspectiveView.dropView("persArea_drop_0_0", "tree_area", "GraphTree");
			perspectiveView.dropView("persArea_drop_0_1", "range_area", "DashboardView");
			perspectiveView.dropView("persArea_drop_1_0", "contents_area", "DashboardView");
			
			$("#persArea_drop_0_0").resizable("disable");
			$("#persArea_drop_0_1").resizable("disable");
		}

		// リソースツリーの生成
		var resourceTreeView = new ENS.ResourceTreeView();

		if(dashboardMode == ENS.dashboard.mode.EDIT){
			resourceTreeView.setEditFunction();

			$("#tree_area")
			.click(
					function() {
						if ($("[id$='mapreduce/task']") != undefined) {

							var elem = $("[id$='mapreduce/task']");

							$("#tree_area").jstree("delete_node",
									elem);
						}
					});

			appView.stopSyncData([wgp.constants.TREE.DATA_ID]);

			// リソースツリーの展開状態・選択状態の復元用データを取得
			var resourceTreeOpenNodes = null;
			var resourceTreeOpenNodesStr = $("#treeViewOpenNodeData").val();
			if(resourceTreeOpenNodesStr > 0){
				resourceTreeOpenNodes = JSON.parse(resourceTreeOpenNodesStr);
			}
			var resourceTreeSelect = null;
			var resourceTreeSelectStr = $("#selectedTreeId").val();
			if(resourceTreeSelectStr.length > 0){
				resourceTreeSelect = resourceTreeSelectStr;
			}

			// リソースツリー構築時の処理をバインド
			$("#" + resourceTreeView.$el.attr("id")).bind("loaded.jstree", function(){
				// 表示状態復元処理を設定
				resourceTreeView.restoreDisplayState(resourceTreeSelect, resourceTreeOpenNodes);
			});

		}
		
		// グラフ範囲入力エリアの生成
		var rangeAreaView = new ENS.GraphControllerView({
			id : "range_area",
			targetId : "range_area"
			});

		// ダッシュボード一覧ツリーの生成
		var resourceDashboardListView = new ENS.ResourceDashboardListView({
			id : "list_area",
			targetId : "contents_area",
			themeUrl : wgp.common.getContextPath()
			+ "/resources/css/jsTree/style.css"
		});
		

		// ダッシュボード一覧ツリーの選択状態の復元用データを取得
		var resourceDashboardListSelect = null;
		var resourceDashboardListSelectStr = $("#selectedDashboardListId").val();
		if(resourceDashboardListSelectStr.length > 0){
			resourceDashboardListSelect = resourceDashboardListSelectStr;
		}

		// ダッシュボード一覧ツリー構築後の処理をバインド
		$("#" + resourceDashboardListView.$el.attr("id")).bind("loaded.jstree", function(){
			resourceDashboardListView.restoreDisplayState(resourceDashboardListSelect, null);
		});

		// ダッシュボード一覧のモード毎の固有処理を設定
		// ・編集モードの場合は編集イベントを設定する。
		// ・運用モードの場合はダイアログを生成する。
		if(ENS.dashboard.mode.EDIT == dashboardMode){
			resourceDashboardListView.setEditFunction();

		}else {
			$("#list_area").dialog({
				title : "Dashboard List",
				autoOpen : false,
				modal : true
			});

			var contextMenu0 = new contextMenu("DashboardSwitching", "Dashboard Switching.");
			var contextMenu1 = new contextMenu("OutputReport", "Output Report.");
			var contextMenuArray = [ contextMenu0, contextMenu1 ];
			contextMenuCreator.initializeContextMenu("dashboard_operate", contextMenuArray);

			var option = {
				onShow: function(event, target){
				},
				onSelect : function(event, target){
					if(event.currentTarget.id == "DashboardSwitching"){
						$("#" + window.resourceDashboardListView.$el.attr("id")).dialog("open");
						return;
					}
					if(event.currentTarget.id == "OutputReport"){
						var context = null;
						for(var i=0, len=ENS.tree.contextOption.length; i<len; i++){
							var tmpCtx = ENS.tree.contextOption[i];
							if(tmpCtx.menu_id !== ENS.tree.OUTPUT_REPORT_TYPE){
								continue;
							}
							context = tmpCtx;
						}
						if(context === null){
							return;
						}
						context.dialogId = "reportDialog";
						context.okObject = {
								
								createReport: function(){
									
									var reportDefinition = {
											clusterName: $("#"+ENS.graphRange.ID_CLUSTER_NAME).val(),
											dashboardName: $("#"+ENS.graphRange.ID_DASHBOARD_NAME).val(),
											reportName: $("#reportName").val(),
											reportTermFrom : $("#jquery-ui-datepicker-from").val(),
											reportTermTo : $("#jquery-ui-datepicker-to").val()
									}
									var settings = {};
									settings[wgp.ConnectionConstants.SUCCESS_CALL_OBJECT_KEY] = this;
									settings[wgp.ConnectionConstants.SUCCESS_CALL_FUNCTION_KEY] = "callback";
									settings.url = ENS.tree.REPORT_ADD_DASHBOARD_URL;
									settings.data = {
											reportDefinition: JSON.stringify(reportDefinition)
									}
									
									// 非同期通信でデータを送信する
									var ajaxHandler = new wgp.AjaxHandler();
									ajaxHandler.requestServerAsync(settings);
								},
								callback: function(){
									
								}
						}
						context.okFunctionName = "createReport";
						$("#reportName").val($("#"+ENS.graphRange.ID_DASHBOARD_NAME).val());
						$("#reportTargetNameArea").hide();
						new ENS.ReportDialogView(context);
						return;
					}
					
				}
			};

			contextMenuCreator.createContextMenu("contents_area",
				"dashboard_operate", option);
		}

		var menuModelArray = [];

		var changeModeMenuModel;
		if(dashboardMode == ENS.dashboard.mode.OPERATE){
			changeModeMenuModel = new ENS.dashboardMenuModel({
				width : 17,
				height : 17,
				styleClass : 'dashboard_menu_icon',
				src : '<%=request.getContextPath()%>/resources/images/map/operateModeIcon.png',
				alt : 'This is the current operation mode. Please click if you want to edit the dashboard.',
				onclick : (function(event){
					$("#dashboardMode").val(ENS.dashboard.mode.EDIT);
					$("#dashboardListForm").attr("action", "<%=request.getContextPath()%>/dashboard/dashboardList");
					saveDisplayState();
					$("#dashboardListForm").submit();
				})
			});

		}
		menuModelArray.push(changeModeMenuModel);

		// 編集モード用のメニューアイコンの設定を行う。
		if(dashboardMode == ENS.dashboard.mode.EDIT){

			var createDashboardMenuModel = new ENS.dashboardMenuModel({
				width : 17,
				height : 17,
				styleClass : 'dashboard_menu_icon',
				src : '<%=request.getContextPath()%>/resources/images/map/createIcon.png',
				alt : 'Please click if you want to create a new dashboard.',
				onclick : (function(event){
					resourceDashboardListView.onCreate();
				})
			});
			menuModelArray.push(createDashboardMenuModel);

			var saveDashboardMenuModel = new ENS.dashboardMenuModel({
				width : 17,
				height : 17,
				styleClass : 'dashboard_menu_icon',
				src : '<%=request.getContextPath()%>/resources/images/map/saveIcon.png',
				alt : 'Please click if you want to save the dashboard.',
				onclick : (function(event){
					if(resourceDashboardListView.childView){
						var selectedId = window.rangeAreaView.graphRangeController._getSelectedTreeId();
						var treeModel = resourceDashboardListView.collection.where({id : selectedId})[0];
						resourceDashboardListView.childView.onSave(treeModel);
					}else{
						console.log("please select a dashboard");
					}
				})
			});
			menuModelArray.push(saveDashboardMenuModel);

			var backgroundMenuModel = new ENS.dashboardMenuModel({
				width : 17,
				height : 17,
				styleClass : "dashboard_menu_icon",
				src : '<%=request.getContextPath()%>/resources/images/map/backgroundIcon.png',
				alt : 'Please click if you want to set the background dashboard.',
				onclick : (function(event){
					if(resourceDashboardListView.childView){
						var dashboardView = resourceDashboardListView.childView;
						var backgroundView = dashboardView.backgroundView;
						var propertyView = new ENS.DashboardElementPropertyView(backgroundView);
					}else{
						console.log("please select a dashboard");
					}
				})
			});
			menuModelArray.push(backgroundMenuModel);

			var linkDashboardMenuModel = new ENS.dashboardMenuModel({
				width : 17,
				height : 17,
				styleClass : 'dashboard_menu_icon',
				src : '<%=request.getContextPath()%>/resources/images/map/createLinkIcon.png',
				alt : 'Please click if you want to add dashboard link.',
				onclick : (function(event){
					if(resourceDashboardListView.childView){
						var selectedId = $("#dashboard_name option:selected").text();
						var treeModel = resourceDashboardListView.collection.where({id : selectedId})[0];
						var dashboardView = resourceDashboardListView.childView;
						dashboardView.setClickAddElementEvent(
							"ENS.ResourceLinkElementView",
							raphaelMapConstants.TEXTAREA_ELEMENT_NAME,
							raphaelMapConstants.TEXTAREA_TYPE_NAME);
						$("[alt='createLink']").addClass("dashboard_menu_icon-active");
					}else{
						console.log("please select a dashboard");
					}
				})
			});
			menuModelArray.push(linkDashboardMenuModel);

			var rectangleMenuModel = new ENS.dashboardMenuModel({
				width : 17,
				height : 17,
				styleClass : 'dashboard_menu_icon',
				src : '<%=request.getContextPath()%>/resources/images/map/rectangleIcon.png',
				alt : 'Please click if you want to add this object.',
				onclick : (function(event){
					if(resourceDashboardListView.childView){
						var dashboardView = resourceDashboardListView.childView;
						dashboardView.setClickAddElementEvent(
							"ENS.ShapeElementView",
							raphaelMapConstants.RECTANGLE_ELEMENT_NAME,
							raphaelMapConstants.POLYGON_TYPE_NAME);
					}
				})
			});
			menuModelArray.push(rectangleMenuModel);

			var ellipseMenuModel = new ENS.dashboardMenuModel({
				width : 17,
				height : 17,
				styleClass : 'dashboard_menu_icon',
				src : '<%=request.getContextPath()%>/resources/images/map/ellipseIcon.png',
				alt : 'Please click if you want to add this object.',
				onclick : (function(event){
					if(resourceDashboardListView.childView){
						var dashboardView = resourceDashboardListView.childView;
						dashboardView.setClickAddElementEvent(
							"ENS.ShapeElementView",
							raphaelMapConstants.ELLIPSE_ELEMENT_NAME,
							raphaelMapConstants.POLYGON_TYPE_NAME);
					}
				})
			});
			menuModelArray.push(ellipseMenuModel);

			var textMenuModel = new ENS.dashboardMenuModel({
				width : 17,
				height : 17,
				styleClass : 'dashboard_menu_icon',
				src : '<%=request.getContextPath()%>/resources/images/map/textIcon.png',
				alt : 'Please click if you want to add this object.',
				onclick : (function(event){
					if(resourceDashboardListView.childView){
						var dashboardView = resourceDashboardListView.childView;
						dashboardView.setClickAddElementEvent(
							"ENS.TextBoxElementView",
							raphaelMapConstants.TEXTAREA_ELEMENT_NAME,
							raphaelMapConstants.TEXTAREA_TYPE_NAME);
					}
				})
			});
			menuModelArray.push(textMenuModel);
			
			var deleteMenuModel =  new ENS.dashboardMenuModel({
				width : 17,
				height : 17,
				styleClass : 'dashboard_menu_icon',
				src : '<%=request.getContextPath()%>/resources/images/map/deleteIcon.png',
				alt : 'Please click if you want to delete this dashboard.',
				onclick : (function(event){
					if(resourceDashboardListView.childView){
						var selectedId = $("#dashboard_name option:selected").text();
						var treeModel = resourceDashboardListView.collection.where({data : selectedId})[0];
						resourceDashboardListView.onRemove(treeModel);
					}else{
						console.log("please select a dashboard");
					}
				})
			});
			menuModelArray.push(deleteMenuModel);
		}

		// メニューアイコンを生成する。
		var menuAreaId = "";
		if(dashboardMode == ENS.dashboard.mode.OPERATE){
			menuAreaId = "persArea_drop_2_0";
		} else {
			menuAreaId = "persArea_drop_1_0";
		}
		var menuView = new ENS.dashboardMenuView({
				id : menuAreaId,
				collection : menuModelArray
			},
			{}
		);

		// メニューアイコンにツールチップを付ける。
		$(".dashboard_menu_icon").tooltip({
			content : function(){
				return $(this).attr("alt");
			},
			items : "[alt]",
			tooltipClass : "tooltip"
		});

	/**
	 * 画面遷移を行う際に、画面の状態を引き継ぐ。
	 */
	function saveDisplayState(){
		var treeIdAttribute = "id";
		var selectedDashboardListId = resourceDashboardListView.getSelectedNodeId(treeIdAttribute);
		$("#selectedDashboardListId").val(selectedDashboardListId);
	}

	// リソース一覧又はダッシュボード一覧タブの選択状態があれば選択する。
	var displayTreeArea = $("#displayTreeArea").val();
	$("#persArea_drop_0_0").find(".tab_menu").find("[href='"+ displayTreeArea +"']").click();

	</script>
	<input type="hidden" id="context" value="<%=request.getContextPath()%>" />
</body>
</html>
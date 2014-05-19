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
var ENS = {};

ENS.ID = {};
ENS.ID.MEASUREMENT_TIME = "measurementTime";
ENS.ID.MEASUREMENT_VALUE = "measurementValue";
ENS.ID.MEASUREMENT_ITEM_NAME = "measurementItemName";

ENS.DATE_FORMAT_DETAIL = 'yyyy/MM/dd HH:mm:ss.fff';
ENS.DATE_FORMAT_DAY = 'yyyy/MM/dd';
ENS.DATE_FORMAT_HOUR = 'yyyy/MM/dd HH:mm';

ENS.URL = {};
ENS.URL.TERM_PERFDOCTOR_DATA_URL = "/performanceDoctor/getPerfDoctor";
ENS.URL.PERFDOCTOR_POSTFIX_ID = "/performanceDoctor";

ENS.URL.TERM_THREADDUMP_DATA_URL = "/threadDump/getThreadDump";
ENS.URL.THREADDUMP_POSTFIX_ID = "/threadDump";
ENS.URL.JVN_LOG_NOTIFY_POSTFIX_ID = "/JvnLog_Notify";


ENS.common = {};
ENS.common.dualslider = {};
ENS.common.dualslider.scaleUnitStrings = 'hours';// for many hours
ENS.common.dualslider.scaleUnitString = 'hour';// for one hour
ENS.common.dualslider.scaleUnit = 60 * 60 * 1000; // millisecond
ENS.common.dualslider.groupString = 'day';// for one day
ENS.common.dualslider.groupStrings = 'days';// for many days
ENS.common.dualslider.groupUnitNum = 24;
ENS.common.dualslider.groupMaxNum = 10;
ENS.common.dualslider.groupDefaultNum = 3;
ENS.common.dualslider.idFrom = 'dualSliderFromValue';
ENS.common.dualslider.idTo = 'dualSliderToValue';

ENS.singleslider = {};
ENS.singleslider.scaleUnitString = 'hour';// for one hour
ENS.singleslider.scaleUnitStrings = 'hours';// for many hours
ENS.singleslider.scaleUnit = 60 * 60 * 1000; // millisecond
ENS.singleslider.groupString = 'day';// for one day
ENS.singleslider.groupStrings = 'days';// for many day
ENS.singleslider.groupUnitNum = 24;
ENS.singleslider.groupMaxNum = 10;
ENS.singleslider.groupDefaultNum = 3;
ENS.singleslider.idTime = 'singlesliderTimeValue';

ENS.nodeinfo = {};
ENS.nodeinfo.parent = {};
ENS.nodeinfo.parent.css = {};
ENS.nodeinfo.viewList = [];

ENS.nodeinfo.parent.css.informationArea = {
	fontSize : "14px",
	float : "right",
	width : "180px",
	height : "350px",
	border : "1px #dcdcdc solid",
	margin : "190px 20px 0px 0px"
};
ENS.nodeinfo.parent.css.legendArea = {
	height : "40px",
	margin : "5px 5px 5px 5px"
};
ENS.nodeinfo.parent.css.annotationLegendArea = {
	margin : "0px 0px 0px 0px",
	padding : "5px 5px 5px 5px"
};
ENS.nodeinfo.parent.css.dualSliderArea = {
	width : "800px",
	margin : "0px 0px 20px 60px"
};
ENS.nodeinfo.parent.css.graphArea = {
	float : "left",
	width : "650px",
	margin : "30px 0px 0px 10px"
};
ENS.nodeinfo.ONEDAY_MILLISEC = 86400000;

ENS.ResourceGraphAttribute = [ "colors", "labels", "valueRange", "xlabel",
		"ylabel", "strokeWidth", "legend", "labelsDiv", "width", "height" ];
ENS.nodeinfo.GRAPH_HEIGHT_MARGIN = 2;
ENS.nodeinfo.GRAPH_TITLE_LENGTH = 30;

ENS.tree = {};
ENS.tree.CLICK_LEFT = 1;
ENS.tree.CLICK_RIGHT = 3;
ENS.tree.ADD_SIGNAL_TYPE = "addSignal";
ENS.tree.EDIT_SIGNAL_TYPE = "editSignal";
ENS.tree.DELETE_SIGNAL_TYPE = "deleteSignal";
ENS.tree.OUTPUT_REPORT_TYPE = "outputReport";
ENS.tree.DELETE_REPORT_TYPE = "deleteReport";
ENS.tree.ADD_MULTIPLE_RESOURCE_GRAPH_TYPE = "addMulResGraph";
ENS.tree.EDIT_MULTIPLE_RESOURCE_GRAPH_TYPE = "editMultipleResourceGraph";
ENS.tree.DELETE_MULTIPLE_RESOURCE_GRAPH_TYPE = "deleteMultipleResourceGraph";

ENS.tree.ADD_SUMMARYSIGNAL_TYPE = "addSummarySignal";
ENS.tree.EDIT_SUMMARYSIGNAL_TYPE = "editSummarySignal";
ENS.tree.DELETE_SUMMARYSIGNAL_TYPE = "deleteSummarySignal";

ENS.tree.SIGNAL_PREFIX_ID = "/singalNode-";
ENS.tree.SIGNAL_ADD_URL = wgp.common.getContextPath() + "/signal/add";
ENS.tree.SIGNAL_GET_URL = wgp.common.getContextPath() + "/signal/getDefinition";
ENS.tree.SIGNAL_EDIT_URL = wgp.common.getContextPath() + "/signal/edit";
ENS.tree.SIGNAL_DELETE_URL = wgp.common.getContextPath() + "/signal/delete";
ENS.tree.SIGNAL_SELECT_ALL_URL = wgp.common.getContextPath()
		+ "/signal/getAllDefinition";
ENS.tree.GET_SQL_PLAN = wgp.common.getContextPath() + "/sqlPlan/getSqlPlan";

ENS.tree.MULTIPLE_RESOURCE_GRAPH_PREFIX_ID = "/mulResGraphNode-";
ENS.tree.MULTIPLE_RESOURCE_GRAPH_SELECT_ALL_URL = wgp.common.getContextPath()
		+ "/multipleResourceGraph/getAllDefinition";
ENS.tree.MULTIPLE_RESOURCE_GRAPH_ADD_URL = wgp.common.getContextPath()
		+ "/multipleResourceGraph/add";
ENS.tree.MULTIPLE_RESOURCE_GRAPH_GET_URL = wgp.common.getContextPath()
		+ "/multipleResourceGraph/getDefinition";
ENS.tree.MULTIPLE_RESOURCE_GRAPH_EDIT_URL = wgp.common.getContextPath()
		+ "/multipleResourceGraph/edit";
ENS.tree.MULTIPLE_RESOURCE_GRAPH_DELETE_URL = wgp.common.getContextPath()
		+ "/multipleResourceGraph/delete";
ENS.tree.MEASUREMENT_ITEM_SELECT_ALL_URL = wgp.common.getContextPath()
		+ "/multipleResourceGraph/getAllMeasurementList";

ENS.tree.SUMMARYSIGNAL_ITEM_SELECT_ALL_URL = wgp.common.getContextPath()
+ "/summarySignal/getAllDefinition";
ENS.tree.SUMMARYSIGNAL_ADD_URL = wgp.common.getContextPath()
+ "/summarySignal/add";
ENS.tree.SUMMARYSIGNAL_GET_URL = wgp.common.getContextPath()
+ "/summarySignal/getDefinition";
ENS.tree.SUMMARYSIGNAL_EDIT_URL = wgp.common.getContextPath()
+ "/summarySignal/edit";
ENS.tree.SUMMARYSIGNAL_DELETE_URL = wgp.common.getContextPath()
+ "/summarySignal/delete";
ENS.tree.SUMMARYSIGNAL_SELECT_ALL_URL = wgp.common.getContextPath()
+ "/summarySignal/getAllDefinition";
ENS.tree.SUMMARY_SIGNAL_SELECT_ALL_URL = wgp.common.getContextPath()
+ "/summarySignal/getAllDefinitions";
ENS.tree.SUMMARY_SIGNAL_CHANGE_STATE_URL = wgp.common.getContextPath()
+ "/summarySignal/changeStatus";

ENS.tree.REPORT_ADD_URL = wgp.common.getContextPath() + "/report/add";
ENS.tree.REPORT_ADD_DASHBOARD_URL = wgp.common.getContextPath() + "/report/addDashboard";
ENS.tree.REPORT_ADD_DUPLICATE_URL = wgp.common.getContextPath()
		+ "/report/addDuplicateReport";
ENS.tree.SIGNAL_NAME = "signalName";
ENS.tree.SIGNAL_VALUE = "signalValue";
ENS.tree.SIGNAL_DIALOG = "signalDialog";
ENS.tree.REPORT_DIALOG = "reportDialog";
ENS.tree.MULTIPLE_RESOURCE_GRAPH_DIALOG = "multipleResourceGraphDialog";

ENS.tree.SUMMARYSIGNAL_NAME = "summarySignalName";
ENS.tree.SUMMARYSIGNAL_VALUE = "summarySignalValue";
ENS.tree.SUMMARYSIGNAL_DIALOG = "summarySignalDialog";

ENS.tree.REPORT_PREFIX_ID = "/reportNode-";
ENS.tree.SCHEDULE_REPORT_PREFIX_ID = "/schedulingReportNode-";
ENS.tree.REPORT_SELECT_ALL_URL = wgp.common.getContextPath()
		+ "/report/getAllDefinition";
ENS.tree.REPORT_SELECT_BY_REPORT_NAME_URL = wgp.common.getContextPath()
		+ "/report/getDefinitionByReportName";
ENS.tree.REPORT_DELETE_BY_NAME_URL = wgp.common.getContextPath()
		+ "/report/deleteByName";
ENS.tree.REPORT_DELETE_BY_ID_URL = wgp.common.getContextPath()
		+ "/report/deleteById";
ENS.tree.GET_DIRECT_CHILDLEN_NODE = wgp.common.getContextPath()
		+ "/tree/getDirectlChildNodes";
ENS.tree.GET_TOP_NODES = wgp.common.getContextPath() + "/tree/getTopNodes";
ENS.tree.GET_CHILD_TARGET_NODES = wgp.common.getContextPath()
		+ "/tree/getChildTargetNodes";
ENS.tree.GET_ALL_CHILD_NODES = wgp.common.getContextPath()
		+ "/tree/getAllChildNodes";
ENS.tree.THREADDUMP_AGENT_SELECT_ALL_URL = wgp.common.getContextPath()
		+ "/threadDump/selectAllAgent";
ENS.tree.THREAD_DUMP_CLICK = wgp.common.getContextPath()+"/threadDump/addThreadDump";

ENS.tree.type = {};
ENS.tree.type.GROUP = "group";
ENS.tree.type.TARGET = "target";
ENS.tree.type.SIGNAL = "signal";
ENS.tree.type.REPORT = "report";
ENS.tree.type.REPORT_SCHEDULE = "schedulingReport";
ENS.tree.type.MULTIPLERESOURCEGRAPH = "mulResGraph";
ENS.tree.type.SUMMARYSIGNAL = "summarySignal";

ENS.tree.OUTPUT_REPORT_SCHEDULE_TYPE = "outputScheduleReport";
ENS.tree.EDIT_SCHEDULE_TYPE = "editScheduling";
ENS.tree.DELETE_REPORT_SCHEDULE_TYPE = "deleteScheduleReport";
ENS.tree.SCHEDULING_REPORT_ADD_URL = wgp.common.getContextPath() + "/report/addscheduling";
ENS.tree.SCHEDULING_EDIT_URL = wgp.common.getContextPath() + "/report/edit";
ENS.tree.REPORT_SCHEDULE_DIALOG = "schedulingReportDialog";
ENS.tree.SCHEDULING_REPORT_SELECT_ALL_URL = wgp.common.getContextPath()
+ "/report/getAllScheduleDefinition";
ENS.tree.SCHEDULING_REPORT_SELECT_ALL_URL_BY_AGENT = wgp.common.getContextPath()
+ "/report/getAllScheduleDefinitionByAgent";
ENS.tree.SCHEDULE_EDIT_URL = wgp.common.getContextPath() + "/report/schedulingEdit";
ENS.tree.REPORT_SCHEDULE_DELETE_BY_NAME_URL = wgp.common.getContextPath() + "/report/deleteScheduleByName";
ENS.tree.REPORT_SCHEDULE_DELETE_BY_ID_URL = wgp.common.getContextPath() + "/report/deleteScheduleById";
ENS.tree.type.REPORT_SCHEDULE = "reportSchedule";

ENS.tree.contextOption = [ {
	menu_id : ENS.tree.ADD_SIGNAL_TYPE,
	menu_name : "Add Signal",
	executeClass : "ENS.SignalDefinitionDialogView",
	showTreeTypes : [ ENS.tree.type.TARGET ],
	executeOption : {
		dialogId : ENS.tree.SIGNAL_DIALOG,
		signalType : ENS.tree.ADD_SIGNAL_TYPE
	},
	children : []
}, {
	menu_id : ENS.tree.EDIT_SIGNAL_TYPE,
	menu_name : "Edit Signal",
	executeClass : "ENS.SignalDefinitionDialogView",
	showTreeTypes : [ ENS.tree.type.SIGNAL ],
	executeOption : {
		dialogId : ENS.tree.SIGNAL_DIALOG,
		signalType : ENS.tree.EDIT_SIGNAL_TYPE
	},
	children : []
}, {
	menu_id : ENS.tree.DELETE_SIGNAL_TYPE,
	menu_name : "Delete Signal",
	executeClass : "",
	showTreeTypes : [ ENS.tree.type.SIGNAL ],
	executeOption : {
		dialogId : ENS.tree.SIGNAL_DIALOG,
		signalType : ENS.tree.DELETE_SIGNAL_TYPE
	},
	children : []
}, {
	menu_id : ENS.tree.OUTPUT_REPORT_TYPE,
	menu_name : "Output Report",
	executeClass : "ENS.ReportDialogView",
	showTreeTypes : [ ENS.tree.type.GROUP, ENS.tree.type.TARGET ],
	executeOption : {
		dialogId : ENS.tree.REPORT_DIALOG,
		signalType : ENS.tree.OUTPUT_REPORT_TYPE
	},
	children : []
}, {
	menu_id : ENS.tree.DELETE_REPORT_TYPE,
	menu_name : "Delete Report",
	executeClass : "",
	showTreeTypes : [ ENS.tree.type.REPORT ],
	executeOption : {
		dialogId : ENS.tree.REPORT_DIALOG,
		signalType : ENS.tree.DELETE_REPORT_TYPE
	},
	children : []
}, {
	menu_id : ENS.tree.ADD_MULTIPLE_RESOURCE_GRAPH_TYPE,
	menu_name : "Add Multiple Resource Graph",
	executeClass : "ENS.MultipleResourceGraphDefinitionDialogView",
	showTreeTypes : [ ENS.tree.type.GROUP, ENS.tree.type.TARGET ],
	executeOption : {
		dialogId : ENS.tree.MULTIPLE_RESOURCE_GRAPH_DIALOG,
		signalType : ENS.tree.ADD_MULTIPLE_RESOURCE_GRAPH_TYPE
	},
	children : []
}, {
	menu_id : ENS.tree.EDIT_MULTIPLE_RESOURCE_GRAPH_TYPE,
	menu_name : "Edit Multiple Resource Graph",
	executeClass : "ENS.MultipleResourceGraphDefinitionDialogView",
	showTreeTypes : [ ENS.tree.type.MULTIPLERESOURCEGRAPH ],
	executeOption : {
		dialogId : ENS.tree.MULTIPLE_RESOURCE_GRAPH_DIALOG,
		signalType : ENS.tree.EDIT_MULTIPLE_RESOURCE_GRAPH_TYPE
	},
	children : []
}, {
	menu_id : ENS.tree.DELETE_MULTIPLE_RESOURCE_GRAPH_TYPE,
	menu_name : "Delete Multiple Resource Graph",
	executeClass : "ENS.MultipleResourceGraphDefinitionDialogView",
	showTreeTypes : [ ENS.tree.type.MULTIPLERESOURCEGRAPH ],
	executeOption : {
		dialogId : ENS.tree.MULTIPLE_RESOURCE_GRAPH_DIALOG,
		signalType : ENS.tree.DELETE_MULTIPLE_RESOURCE_GRAPH_TYPE
	},
	children : []
} , {
	menu_id : ENS.tree.ADD_SUMMARYSIGNAL_TYPE,
	menu_name : "Add Summary Signal",
	executeClass : "ENS.SummarySignalDefinitionDialogView",
	showTreeTypes : [  ENS.tree.type.GROUP, ENS.tree.type.TARGET ],
	executeOption : {
		dialogId : ENS.tree.SUMMARYSIGNAL_DIALOG,
		summarySignalType : ENS.tree.ADD_SUMMARYSIGNAL_TYPE
	},
	children : []
}, {
	menu_id : ENS.tree.EDIT_SUMMARYSIGNAL_TYPE,
	menu_name : "Edit Summary Signal",
	executeClass : "ENS.SummarySignalDefinitionDialogView",
	showTreeTypes : [ ENS.tree.type.SUMMARYSIGNAL ],
	executeOption : {
		dialogId : ENS.tree.SUMMARYSIGNAL_DIALOG,
		summarySignalType : ENS.tree.EDIT_SUMMARYSIGNAL_TYPE
	},
	children : []
}, {
	menu_id : ENS.tree.DELETE_SUMMARYSIGNAL_TYPE,
	menu_name : "Delete Summary Signal",
	executeClass : "",
	showTreeTypes : [ ENS.tree.type.SUMMARYSIGNAL ],
	executeOption : {
		dialogId : ENS.tree.SUMMARYSIGNAL_DIALOG,
		summarySignalType : ENS.tree.DELETE_SUMMARYSIGNAL_TYPE
	},
	children : []
}, {
	menu_id : ENS.tree.OUTPUT_REPORT_SCHEDULE_TYPE,
	menu_name : "Scheduling Report",
	executeClass : "ENS.SchedulingReportDialogView",
	showTreeTypes : [ ENS.tree.type.GROUP, ENS.tree.type.TARGET ],
	executeOption : {
		dialogId : ENS.tree.REPORT_SCHEDULE_DIALOG,
		signalType : ENS.tree.OUTPUT_REPORT_SCHEDULE_TYPE
	},
	children : []
},{
	menu_id : ENS.tree.EDIT_SCHEDULE_TYPE,
	menu_name : "Edit Scheduling",
	executeClass : "ENS.SchedulingReportDialogView",
	showTreeTypes : [ ENS.tree.type.REPORT_SCHEDULE ],
	executeOption : {
		dialogId : ENS.tree.REPORT_SCHEDULE_DIALOG,
		signalType : ENS.tree.EDIT_SCHEDULE_TYPE
	},
	children : []
},{
	menu_id : ENS.tree.DELETE_REPORT_SCHEDULE_TYPE,
	menu_name : "Delete Scheduling Report",
	executeClass : "",
	showTreeTypes : [ ENS.tree.type.REPORT_SCHEDULE ],
	executeOption : {
		dialogId : ENS.tree.REPORT_SCHEDULE_DIALOG,
		signalType : ENS.tree.DELETE_REPORT_SCHEDULE_TYPE
	},
	children : []
}];
ENS.tree.SIGNAL_ICON_STOP = "signal_-1";
ENS.tree.SIGNAL_ICON_0 = "signal_0";
ENS.tree.SIGNAL_ICON_1 = "signal_1";
ENS.tree.SIGNAL_ICON_2 = "signal_2";
ENS.tree.SIGNAL_ICON_3 = "signal_3";
ENS.tree.SIGNAL_ICON_4 = "signal_4";
ENS.tree.SIGNAL_ICON_5 = "signal_5";
ENS.tree.REPORT_ICON = "report";
ENS.tree.MULRECGRAPH_ICON = "leaf";

ENS.tree.DEFAULT_SIGNAL_PATTERN = "3";
ENS.tree.DEFAULT_ESCALATION_PERIOD = "15";

ENS.dashboard = {};
ENS.dashboard.mode = {};
ENS.dashboard.mode.OPERATE = "OPERATE";
ENS.dashboard.mode.EDIT = "EDIT";
ENS.dashboard.fontSize = 16;
ENS.dashboard.fontColor = "#F58400";
ENS.dashboard.MIN_GRAPH_WIDTH = 260;
ENS.dashboard.MIN_GRAPH_HEIGHT = 200;
ENS.dashboard.extraDashboardSize = 10;
ENS.dashboard.moveSpan = 16;
ENS.dashboard.resizeSpan = 16;
ENS.dashboard.backgroundSetting = {
	objectId : "background",
	objectName : "ENS.BackgroundElementView",
	pointX : 0,
	pointY : 0,
	width : 1000,
	height : 750,
	shapeName : raphaelMapConstants.RECTANGLE_ELEMENT_NAME,
	shapeType : raphaelMapConstants.POLYGON_TYPE_NAME,
	zIndex : 0,
	elementAttrList : [ {
		fill : "#FFFFFF",
		stroke : "#000000",
		strokeDasharray : "",
		strokeWidth : 1
	} ]
};
ENS.svg = {};
ENS.svg.attribute = {
	// arrowEnd : "arrow-end",
	// clipRect : "clip-rect",
	// cursor : "cursor",
	// cx : "cx",
	// cy : "cy",
	fill : {
		name : "fill",
		type : "color",
		display : "Fill color"
	},
	fillOpacity : {
		name : "fill-opacity",
		type : "number",
		display : "Transparency",
		unit : "%"
	},
	// font : "font",
	fontFamily : {
		name : "font-family",
		type : "select",
		display : "Font type",
		selection : {
			"Arial" : "Arial",
			"MS Mincho" : "MS Mincho",
			"MS Gothic" : "MS Gothic"
		}
	},
	fontSize : {
		name : "font-size",
		type : "select",
		selection : {
			"6" : 6,
			"8" : 8,
			"9" : 9,
			"10" : 10,
			"11" : 11,
			"12" : 12,
			"14" : 14,
			"16" : 16,
			"18" : 18,
			"20" : 20,
			"22" : 22,
			"24" : 24,
			"26" : 26,
			"28" : 28,
			"36" : 26,
			"48" : 48,
			"72" : 72
		},
		display : "Font size",
		unit : "px"
	},
	fontWeight : "font-weight",
	height : "height",
	href : {
		name : "url",
		type : "string",
		display : "url"
	},
	opacity : "opacity",
	path : "path",
	r : "r",
	rx : "rx",
	ry : "ry",
	src : {
		name : "src",
		type : "string",
		display : "url"
	},
	stroke : {
		name : "stroke",
		type : "color",
		display : "Line color"
	},
	strokeDasharray : {
		name : "stroke-dasharray",
		type : "select",
		display : "Line type",
		selection : {
			"――――" : "",
			"--------" : "-",
			"・・・・" : ".",
			"―・―・" : "-."
		}
	},
	strokeLinecap : "stroke-linecap",
	strokeLinejoin : "stroke-linejoin",
	strokeMiterlimit : "stroke-miterlimit",
	strokeOpacity : "stroke-opacity",
	strokeWidth : {
		name : "stroke-width",
		type : "number",
		display : "Line width"
	},
	target : "target",
	text : {
		name : "text",
		type : "string",
		dislpay : "text"
	},
	textAnchor : {
		name : "text-anchor",
		type : "select",
		display : "Text Anchor",
		selection : {
			"Align Left" : "start",
			"Align Center" : "middle",
			"Align Right" : "end"
		}
	},
	title : "title",
	transform : "transform",
	width : "width",
	x : "x",
	y : "y"
};

ENS.report = {};
ENS.report.DOWNLOAD_URL = wgp.common.getContextPath() + "/report/download";
ENS.perfDoctor = {};
ENS.threadDump = {};
ENS.report.SCHEDULINGREPROT_URL = wgp.common.getContextPath() + "/report/updateSchedulingReport";
ENS.schedulingReportDialog = {};
ENS.tree.agentName = "/default/";
ENS.mulResGraphView = {};
ENS.mulResGraphView.series = {};
ENS.mulResGraphView.series.number = 10;

ENS.graphRange = {};
ENS.graphRange.ID_RANGE = "range_controller_range";
ENS.graphRange.ID_DATEPICKER = "range_controller_datepicker";
ENS.graphRange.ID_PREV_BUTTON = "range_controller_prev";
ENS.graphRange.ID_PLAY_BUTTON = "range_controller_play";
ENS.graphRange.ID_NEXT_BUTTON = "range_controller_next";
ENS.graphRange.ID_SEARCH_BUTTON = "range_controller_search";
ENS.graphRange.ID_SPAN = "range_controller_span";
ENS.graphRange.ID_CLUSTER_NAME = "cluster_name";
ENS.graphRange.ID_DASHBOARD_NAME = "dashboard_name";
ENS.graphRange.TIMER_INTERVAL = 1000;

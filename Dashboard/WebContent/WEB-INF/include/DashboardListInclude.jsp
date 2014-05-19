<meta charset="UTF-8" />
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/lib/slider/ui.slider.extras.css"
	type="text/css" media="all">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/lib/pagination/pagination.css"
	type="text/css" media="all">

<%-- libraries --%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/lib/pagination/jquery.pagination.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/lib/slider/selectToUISlider.jQuery.js">
</script>

<%-- common static value --%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/common/constants.js">
</script>

<%-- dual slider --%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/common/dualSliderView.js">
</script>

<%-- utility --%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/common/endoSnipeUtility.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/common/ensAppView.js">
</script>

<%-- tree --%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/tree/ensTreeManager.js">
</script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/tree/ensTreeView.js">
</script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/tree/resourceTreeView.js">
</script>

<%-- resource map --%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/map/dashboard.css"
	type="text/css" media="all">

<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/element/textField.js">
</script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/view/resourceDashboardView.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/view/resourceDashboardListView.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/view/resourceDashboardMenuView.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/view/shapeView.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/view/signalView.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/view/resourceLinkView.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/view/textboxView.js">
</script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/view/backgroundView.js">
</script>

<%-- nodeInfo graph --%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/view/nodeInfoParentView.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/view/resourceGraphView.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/model/resourceGraphModel.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/view/MulResGraphView.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/view/graphLabel.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/view/graphControllerView.js">
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/view/graphRangeController.js">
</script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/manager/raphaelDashboardManager.js">
</script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/common/command.js">
</script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/command/raphaelDashboardMoveCommand.js">
</script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/command/raphaelDashboardDeleteCommand.js">
</script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/command/raphaelDashboardResizeCommand.js">
</script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/command/raphaelDashboardPasteCommand.js">
</script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/map/dialog/dashboardElementPropertyView.js">
</script>


<%-- dialog view --%>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/dialog/dialogView.js">
</script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/dialog/reportDialogView.js">
</script>
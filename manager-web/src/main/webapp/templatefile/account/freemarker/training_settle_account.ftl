<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Title</title>
	<style>
		body {
			text-align: center;
			font-family: SimSun;
		}
		
		table {
			border-right:1px solid black;
			border-bottom:1px solid black;
		} 
		
		table td {
			border-left:1px solid black;
			border-top:1px solid black;
		} 
		
		table tr {
			height: 40px;
		}
	</style>
</head>
<body>
	<#list data as datum>
	<#if datum_index != 0>
	<div style="page-break-after: always;"></div>
	</#if>
	<h1>${datum.tn}培训经费结算表</h1>
	<div style="width: 95%;text-align: left;margin: 10px auto;">
		培训单位：${datum.tu}
	</div>
	<table style="width: 95%;margin: 10px auto;" border="0" cellspacing="0" cellpadding="0">
        <col width="25%"/>
        <col width="25%"/>
        <col width="25%"/>
        <col width="25%"/>
		<tr><td>培训班名称</td><td colspan="3">${datum.cn}</td></tr>
		<tr><td>班级编号</td><td>${datum.cc}</td><td>培训班来源</td><td></td></tr>
		<tr><td>培训人数</td><td>${datum.pn}</td><td>培训天数</td><td>${datum.td}</td></tr>
		<tr><td colspan="2">培训费总额</td><td colspan="2">${datum.ttf}</td></tr>
		<tr><td rowspan="10">支出</td><td>师资费</td><td colspan="2">${datum.tf}</td></tr>
		<tr><td>资料费</td><td colspan="2">${datum.mf}</td></tr>
		<tr><td>劳务带班费</td><td colspan="2">${datum.pf}</td></tr>
		<tr><td>住宿费</td><td colspan="2">${datum.rmf}</td></tr>
		<tr><td>餐饮费</td><td colspan="2">${datum.ff}</td></tr>
		<tr><td>交通费</td><td colspan="2">${datum.trf}</td></tr>
		<tr><td>现场教学费</td><td colspan="2">${datum.tef}</td></tr>
		<tr><td>场地费</td><td colspan="2">${datum.sf}</td></tr>
		<tr><td>报名费</td><td colspan="2">${datum.ef}</td></tr>
		<tr><td>其他费用</td><td colspan="2">${datum.of}</td></tr>
		<tr><td>合计</td><td colspan="3">${datum.rtf}</td></tr>
		<tr><td>申请人</td><td colspan="3">${datum.au}</td></tr>
		<tr><td colspan="4" style="font-weight: bold;">审核流程</td></tr>
		<tr><td>审核人</td><td colspan="3">审核时间</td></tr>
		<#list datum.list1! as check>
		<tr><td>${check.l_cu}</td><td colspan="3">${check.l_cd}</td></tr>
		</#list>
	</table>
	<div style="width: 95%;text-align: right;margin: 10px auto;">
		打印人：${datum.pu}
	</div>
	<div style="width: 95%;text-align: right;margin: 10px auto;">
		打印日期：${datum.pd}
	</div>
	</#list>
</body>
</html>
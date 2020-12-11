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
	<h1>${datum.cn}</h1>
	<h1>教师使用情况</h1>
	<div style="width: 95%;text-align: left;margin: 10px auto;">
		使用单位：${datum.un}
	</div>
	<table style="width: 95%;margin: 10px auto;" border="0" cellspacing="0" cellpadding="0">
		<tr style="background-color: #d4d4d4;">
			<td>日期</td>
			<td>时间</td>
			<td>教室</td>
			<td>教室所属单位</td>
			<td>价格</td>
		</tr>
		<#list datum.list1! as room>
		<tr>
			<td>${room.l_ud}</td>
			<td>${room.l_ut}</td>
			<td>${room.l_pn}</td>
			<td>${room.l_pu}</td>
			<td>${room.l_pr}</td>
		</tr>
		</#list>
		<#list 0..(13-datum.list1!?size) as i>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
		</#list>
		<tr>
			<td colspan="3">合计</td>
			<td colspan="2">${datum.total!}</td>
		</tr>
	</table>
	<div style="width: 95%;text-align: left;margin: 10px auto;">
		班主任（签名）：
	</div>
	<div style="width: 95%;text-align: right;margin: 10px auto;">
		打印人：${datum.pu}
	</div>
	<div style="width: 95%;text-align: right;margin: 10px auto;">
		打印日期：${datum.pd}
	</div>
	</#list>
</body>
</html>
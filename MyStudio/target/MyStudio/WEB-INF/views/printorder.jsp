<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<link href="D:\MyStudio\src\main\webapp\css\header.css" rel="stylesheet"
	media="print" type="text/css" />
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<style type="text/css">
table th {
	border-bottom: 1px solid black;
	border-top: 1px solid black;
	text-align: centre;
	padding-bottom: 3px;
	padding-top: 3px;
	border-style: solid;
}

table td {
	padding-top: 3px;
	padding-bottom: 2px;
}

table tr {
	padding-top: 3px;
	padding-bottom: 2px;
	border-bottom: 2px solid black;
}

table {
	width: 100%;
	border-style: solid;
}

.tbdash {
	border-top: 1px solid black;
	border-bottom: 1px solid black;
}

.bdash {
	border-bottom: 1px solid black;
}

.tdash {
	border-top: 1px solid black;
}

/* @media screen {
   p.bodyText {font-family:verdana, arial, sans-serif;}
}
 */
@media print {
	p.bodyText {
		font-family: georgia, times, serif;
	}
}
/* @media screen, print {
   p.bodyText {font-size:10pt}
} */
@page {
	/* size: auto;  auto is the current printer page size */
	margin: 0mm; /* this affects the margin in the printer settings */
	size: A5 portrait;
}
</style>
<body>
	<form:form modelAttribute="order">
		<table>
			<tr>
				<td><font size="4px">
						<div style="width: 100%">
							<div align="center">
								<b>SRI VAISHNAVI DIGITAL</b>
							</div>
							<div align="center">
								<b>16, Annai Indhra Gandhi Salai</b>
							</div>
							<div align="center">
								<b>Panruti -607106</b>
							</div>
				</font> <font size="1px">
						<div align="center">
							<b>Bill Copy</b>
						</div>
						<div style="padding-bottom: 5px;">
							<table>
								<tr>
									<td>
										<table style="width: 100%">
											<col width=110>
											<tr>
												<td>Customer's Name</td>
												<td colspan="1">: ${fn:toUpperCase(order.custName)}</td>
											</tr>
											<tr>
												<td>Customer's Address</td>
												<td nowrap="nowrap">:
													${fn:toUpperCase(order.custAddr2)}</td>
											</tr>
											<tr>
												<td><span style="font-weight: bold">Current
														Outstanding </span></td>
												<td><span style="font-weight: bold">:
														${order.creditBalance}</span></td>
											</tr>
											<tr>
												<td>Cover No</td>
												<td>: ${order.orderId}</td>
											</tr>
										</table>
									</td>
									<td align="right">
										<table>
											<tr>
												<td>Date</td>
												<td nowrap="nowrap">: ${order.strOrderDate}</td>
											</tr>
											<tr>
												<td>Bill No</td>
												<td>: ${order.orderId}</td>
											</tr>
											<tr>
												<td>GST No</td>
												<td>:33AGXPD0338F1ZL</td>
											</tr>

											<tr>
												<td>Due Date</td>
												<td>: <fmt:formatDate value="${order.dueDate}"
														pattern="dd/MM/yyyy" /></td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</div> <!-- ordering table -->
						<div>
							<table align="right">
								<thead>
									<tr>
										<th>Sl.No.</th>
										<th>Products & Sizes</th>
										<th nowrap="nowrap">Item Type</th>
										<th>Qty</th>
										<th>Rate</th>
										<th>Amount</th>
									</tr>
									<c:set var="slno" value="${1}" />
								<thead>
									<c:forEach items="${order.products}" var="product">
										<c:forEach items="${product.productItems}" var="productItem">
											<tr>
												<td align="center"><c:out value="${slno}" /></td>
												<td>${fn:toUpperCase(productTypeMap[product.productTypeId])}
													${product.sizeName}</td>
												<td>${fn:toUpperCase(productItem.prodItemName)}</td>
												<td align="center">${productItem.quantity}</td>
												<td align="center">${productItem.rate}</td>
												<td align="right">${productItem.amount}</td>
											</tr>
											<c:set var="slno" value="${slno+1}" />
										</c:forEach>
									</c:forEach>

									<tr>
										<td colspan="6"
											style="padding-bottom: 5px; border-bottom: 2px solid black;">
										</td>
									</tr>

									<!--			<tr  >
				    <td colspan="4">Total</td>
				    <td colspan="1">${order.orderTrxnDetails[0].total}</td>
				</tr>
				-->
									<tr>
										<td colspan="4"></td>
										<td colspan="1">Total</td>
										<td colspan="1" align="right">${order.orderTrxnDetails[0].total}</td>
									</tr>
									<tr>
										<td colspan="4"></td>
										<td colspan="1">CGST(+ 6%)</td>
										<td colspan="1" align="right">${order.orderTrxnDetails[0].cgst}</td>
									</tr>
									<tr>
										<td colspan="4"></td>
										<td colspan="1">SGST(+ 6%)</td>
										<td colspan="1" align="right">${order.orderTrxnDetails[0].sgst}</td>
									</tr>

									<tr>
										<td colspan="4"></td>
										<td colspan="1">Delivery(+)</td>
										<td colspan="1">${order.orderTrxnDetails[0].delvCharge}</td>
									</tr>
									<tr>
										<td colspan="4"></td>
										<td colspan="1">Discount(-)</td>
										<td colspan="1" align="right">${order.orderTrxnDetails[0].discount}</td>
									</tr>
									<!-- <tr>
				    <td colspan="4"></td>
				    <td colspan="1">Round Off</td>
				    <td colspan="1" class="bdash">0.0</td>
				</tr> -->
									<tr>
										<td colspan="4"></td>
										<td colspan="1">Gross Total</td>
										<td colspan="1" align="right">${order.orderTrxnDetails[0].netAmount}</td>
									</tr>
									<tr>
										<td colspan="4"></td>
										<td colspan="1">Advance(-)</td>
										<td colspan="1" align="right">${order.orderTrxnDetails[0].advance}</td>
									</tr>
									<tr>
										<td colspan="6"
											style="padding-bottom: 5px; border-bottom: 2px solid black;">
										</td>
									</tr>
									<tr>
										<td colspan="1"></td>
										<td align="center"></td>
										<td colspan="1"><span style="font-weight: bold">Net
												Balance</span></td>
										<td align="right"><span style="font-weight: bold">${order.orderTrxnDetails[0].balance}</span></td>
									</tr>


									<tr>
										<td colspan="6"
											style="padding-bottom: 5px; border-bottom: 2px solid black;">
										</td>
									</tr>
							</table>
							<table align="right">
								<thead>
									<tr>
										<td align="center">Thanks!!! Visit Again!!!</td>
										<td align="right"><span style="font-weight: bold">for
												Vaishnavi Digital</span></td>

									</tr>
								</thead>
							</table>
						</div>
						</div>
				</font>
		</table>

		<!-- </td> -->
		<!-- Bill 2 -->
		<!-- <td style="vertical-align: text-top;"> -->


		<c:if test="${!order.editMode}">
			<p style="page-break-after: always;">&nbsp;</p>
			<font size="4px">


				<div style="width: 100%"></div> <br> <br>
				<div align="center">
					<b>SRI VAISHNAVI DIGITAL</b>
				</div>
				<div align="center">
					<b>16, Annai Indhra Gandhi Salai</b>
				</div>
				<div align="center">
					<b>Panruti -607106</b>
				</div>
			</font>
			<font size="1px">

				<div align="center">
					<b>Bill Copy</b>
				</div>

				<table>
					<tr>
						<td>
							<table>
								<col width=110>
								<tr>
									<td>Customer's Name</td>
									<td align="left">: ${order.custName}</td>
								</tr>
								<tr>
									<td>Customer's Address</td>
									<td>: ${order.custAddr2}</td>
								</tr>
								<tr>
									<td>Current Outstanding</td>
									<td><span style="font-weight: bold">:
											${order.creditBalance}</span></td>
								</tr>
								<tr>
									<td>Cover No</td>
									<td>: ${order.orderId}</td>
								</tr>
							</table>
						</td>
						<td align="right">
							<table>
								<tr>
									<td>Date</td>
									<td nowrap="nowrap">: ${order.strOrderDate}</td>
								</tr>
								<tr>
									<td>Bill No</td>
									<td>: ${order.orderId}</td>
								</tr>
								<c:forEach items="${order.products}" var="product">
									<c:if test="${product.productTypeId =='102'}">

										<tr>
											<td><span style="font-weight: bold">Total Sheet</span></td>
											<td><span style="font-weight: bold">:${product.noOfSheet * product.noOfCopy}</span></td>
										</tr>

									</c:if>
								</c:forEach>

								<tr>
									<td>Due Date</td>
									<td>: <fmt:formatDate value="${order.dueDate}"
											pattern="dd/MM/yyyy" />
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				</div> <!-- ordering table -->
				<div>
					<table align="right">
						<thead>
							<th>Sl.No.</th>
							<th>Products & Sizes</th>
							<th nowrap="nowrap">Item Type</th>
							<th>Qty</th>
							</tr>

							<c:set var="slno" value="${1}" />
						<thead>

							<c:forEach items="${order.products}" var="product">
								<c:forEach items="${product.productItems}" var="productItem">
									<tr>
										<td align="center"><c:out value="${slno}" /></td>
										<td>${productTypeMap[product.productTypeId]}
											${product.sizeName}</td>
										<td>${productItem.prodItemName}</td>
										<td align="center">${productItem.quantity}</td>

									</tr>
									<c:set var="slno" value="${slno+1}" />
								</c:forEach>
							</c:forEach>

							<table align="right">
								<thead>
									<tr>
										<td align="center">Thanks!!! Visit Again!!!</td>
										<td align="right"><span style="font-weight: bold">for
												Vaishnavi Digital</span></td>

									</tr>
								</thead>
							</table>
					</table>
				</div>
				</div>
			</font>

		</c:if>

		<table>
			</td>
			</tr>
			<tr id="print">
				<td colspan="2"><div align="center" style="padding-top: 50px;">
						<input type="button" value="Print"
							onclick="hidePrint(); javascript:window.print();">
					</div></td>
			</tr>
		</table>
	</form:form>
</body>
<script type="text/javascript">

function hidePrint(){
	
	document.getElementById("print").style.display = "none"; 
	
}


</script>
</html>
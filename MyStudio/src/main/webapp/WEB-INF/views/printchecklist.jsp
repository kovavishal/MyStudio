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
@media print {
	p.bodyText {
		font-family: georgia, times, serif;
	}
}

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
				<td>
				<font size="3px">
					<div style="width: 100%">
						<div align="center">
							<b>SRI VAISHNAVI DIGITAL PRESS</b>
						</div>
						<div align="center">
							16, Annai Indhra Gandhi Salai
						</div>
						<div align="center">
							Panruti -607106
						</div>
				</font> 
				<font size="2px">
						<div align="center">
							<b>CHECK LIST</b>
						</div>
						<div >
						<p>
						<b>Order No:${order.orderId}</b>
						<b style=padding-left:12em>Order Date :${order.strOrderDate.substring(0,10)}</b>
						</p>
						</div>
						<div style="padding-bottom: 5px;">
							<div>
								<table align="right">
									<thead>
										<tr>
											<th>Sl.No.</th>
											<th>Products & Sizes</th>
											<th nowrap="nowrap">Item Type</th>
											<th>Qty</th>
										<%-- <th >Date :${order.strOrderDate}</th> --%>
											<!-- <th  >Amount</th> -->
										</tr>
										<c:set var="slno" value="${1}" />
										</thead>
											<c:forEach items="${order.products}" var="product">
												<c:forEach items="${product.productItems}" var="productItem">
													<tr>
														<td align="center"><c:out value="${slno}" /></td>
														<td>${fn:toUpperCase(productTypeMap[product.productTypeId])}
															${product.sizeName}</td>
														<td>${(productItem.prodItemName)}</td>
														<td align="center">${productItem.quantity}</td>
														<%-- <td align="center"> ${productItem.rate}</td>
														<td  align="right">${productItem.amount}</td> --%>
													</tr>
													<c:set var="slno" value="${slno+1}" />
												</c:forEach>
											</c:forEach>
										<!-- </font> -->
											<tr>
												<td colspan="5"
													style="padding-bottom: 5px; border-bottom: 2px solid black;">
												</td>
											</tr>
											<tr>
												<td colspan="3"></td>
												<td colspan="1">Gross Total</td>
												<td colspan="1" align="right">${order.orderTrxnDetails[0].netAmount}</td>
											</tr>
											<tr>
												<td colspan="3"></td>
												<td>Advance(-)</td>
												<td colspan="1" align="right">${order.orderTrxnDetails[0].advance}</td>
											</tr>
											<tr>
												<td colspan="3"></td>
												<td>Receipts(-)</td>
												<td align="right">${order.balancePayable}</td>
											</tr>
											<tr>
												<td colspan="4"
													style="padding-bottom: 5px; border-bottom: 2px solid black;">
												</td>
											</tr>
											<tr>
												<td><c:set var="NetBalance" scope="application"
										  			value="${order.orderTrxnDetails[0].netAmount - (order.orderTrxnDetails[0].advance + order.balancePayable)}"></c:set>
												</td>

												<td colspan="2" align="right"></td>
												<td><span style="font-weight: bold">Net Balance</span></td>
												<td><span style="font-weight: bold">${NetBalance}</span></td>
											</tr>
								</table>

							</div>
						</div>
				</font>
		</table>

		<p style="page-break-after: always;">&nbsp;</p>
		<font size="4px">
			<table>
				<div align="left">
					<b>Delivery Address :</b>
				</div>
				<tr>
					<td colspan="6"
						style="padding-bottom: 5px; border-bottom: 2px solid black;"></td>
				</tr>

				<tr>
					<td>Customer's Name</td>
					<td colspan="1">: ${order.custName}</td>
				</tr>
				<tr>
					<td>Address</td>
					<td nowrap="nowrap">: ${order.custAddr1}</td>
				</tr>
				<tr>
					<td></td>
					<td nowrap="nowrap">: ${order.custAddr2}</td>
				</tr>
				<tr>
					<td>Mobile</td>
					<td nowrap="nowrap">: ${order.mobileNo}</td>
				</tr>
				<tr>
					<td colspan="6"
						style="padding-bottom: 5px; border-bottom: 2px solid black;"></td>
				</tr>
				<tr>
					<td>Delivery By :</td>
					<td nowrap="nowrap">: ${order.deliveryType}</td>
				</tr>
			</table>

		</font>

		<table >
			<tr  id="print">
				<td colspan="2"><div align="center" style="padding-top: 25px;padding-left:150px">
						<input type="button" value="Print CheckList"  style='background-color: #f44336;color:white'
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
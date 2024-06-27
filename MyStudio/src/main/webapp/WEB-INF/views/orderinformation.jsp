<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="staticcontent.jsp"%>
<title>Order Information</title>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="container-fluid" id="main">
		<form:form modelAttribute="OrderInformation">

			<div class="card">
				<div class="card-header text-center"
					style="padding-bottom: 0px; padding-top: 0px; background: skyblue; color: #30567D; font-weight: 625;">
					<jsp:include page="logout.jsp">
						<jsp:param name="page" value="Sales Order Information" />
					</jsp:include>
				</div>

				<div class="card-body bg-light border-success mb-3 col-lg-12"
					style="color: navy;">
					<div class="row">
						<div class="col-sm-4 col-lg-2">
							<label for="orderno">Order No</label> <input type="text"
								value="${order.orderId}" class="form-control" id="orderno"
								readonly="readonly" />
						</div>

						<div class="col-md-4 col-lg-3">
							<label for="custname">Customer Name</label> <input type="text"
								value="${order.custName}" class="form-control" id="custname"
								readonly="readonly" />
						</div>
						<div class="col-md-4 col-lg-2">
							<label for="mobileno">Mobile No</label> <input type="text"
								value="${order.mobileNo}" class="form-control" id="mobileno"
								readonly="readonly" />
						</div>
						<div class="col-md-4 col-lg-3">
							<label for="mobileno">Order Date</label> <input type="text"
								value="${order.strOrderDate}" class="form-control" id="mobileno"
								readonly="readonly" />
						</div>

						<div class="col-md-4 col-lg-2" align="right"
							style="padding-top: 20px;">
							<button class="btn btn-danger" onclick="closeWindow()"; javascript:window.close();">
								<i class="fa fa-close"
									style='font-size: 20px; padding: 0 1em 0 0'></i>Close
							</button>
						</div>

						<%--  <div class="col-md-4 col-lg-2">
     	<label for="mobileno">Mobile No</label>
     	<input type="text" value="${order.agentName}" class="form-control" id="mobileno" readonly="readonly"/>
     </div>  --%>

					</div>
					<div class="row">
						<div class="col-sm-4 col-lg-3">
							<label for="mobileno">Address - 1</label> <input type="text"
								value="${order.custAddr1}" class="form-control" id="mobileno"
								readonly="readonly" />
						</div>
						<div class="col-sm-4 col-lg-3">
							<label for="mobileno">Address - 2</label> <input type="text"
								value="${order.custAddr2}" class="form-control" id="mobileno"
								readonly="readonly" />
						</div>
						<div class="col-sm-4 col-lg-3">
							<label for="mobileno">Order Status</label> <input type="text"
								value="${fn:toUpperCase(jobStatusMap[order.status])} ${jobStatus.name}"
								class="form-control" id="mobileno" readonly="readonly" />
						</div>
						<div class="col-sm-4 col-lg-3">
							<label for="mobileno">Delivery Mode</label> <input type="text"
								value="${order.deliveryType}" class="form-control" id="mobileno"
								readonly="readonly" />
						</div>

					</div>

					<table
						class="table table-bordered table-responsive-md table-striped text-center">
						<thead>
							<tr>
								<th>Sl.No.</th>
								<th>Products & Sizes</th>
								<th nowrap="nowrap">Item Type</th>
								<th>Qty</th>
								<th>Rate</th>
								<th>Amount</th>
							</tr>
						</thead>
						<c:set var="slno" value="${1}" />
						<!-- <thead> -->
						<c:forEach items="${order.products}" var="product">
							<c:forEach items="${product.productItems}" var="productItem">
								<tr>
									<td align="center"><c:out value="${slno}" /></td>
									<td>${fn:toUpperCase(productTypeMap[product.productTypeId])}
										${product.sizeName}</td>
									<td>${(productItem.prodItemName)}</td>
									<td align="center">${productItem.quantity}</td>
									<td align="center">${productItem.rate}</td>
									<td align="right">${productItem.amount}</td>
								</tr>
								<c:set var="slno" value="${slno+1}" />
							</c:forEach>
						</c:forEach>
						</font>
					</table>
					<table class="table table-bordered table-responsive-md text-center">
						<tr>

							<td colspan="3"></td>
							<td colspan="1">Gross Total</td>
							<td colspan="1" align="right">${order.orderTrxnDetails[0].netAmount}</td>

							<td colspan="3"></td>
							<td>Advance(-)</td>
							<td colspan="1" align="right">${order.orderTrxnDetails[0].advance}</td>

							<td colspan="3"></td>
							<td>Receipts(-)</td>
							<td align="right">${order.balancePayable}</td>

							<!-- <td colspan="4" style="padding-bottom: 5px;   border-bottom: 2px solid black;">
					</td> -->

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
		</form:form>

		<%--  <div class="row ">
  
  	<div class="col-sm-8 col-lg-3">
	    <div class="card bg-light  mb-3" style="color: navy;">
	      <div class="card-body" style="padding: 0.25rem">
	       	<div id="table" class="table-editable">
		      <table class="table table-bordered table-responsive-md table-striped text-center">
		        <tr>
		          <th class="text-center">Product</th>
		          <th class="text-center">No Of Sheet</th>
		           <th class="text-center">No Of Copy</th>
		        </tr>
		        <c:forEach items="${products}" var="product">
		        <tr class="form-group">
		          <td class="pt-3-half">${product.name}</td>
		          <td class="pt-3-half">${product.noOfSheet}</td>
		          <td class="pt-3-half">${product.noOfCopy}</td>
		        </tr>
		        </c:forEach>
		      </table>
    		</div>
		</div>
	   </div>
	   <div style="padding-left: 5px;">
	 	 <button type="button" class="btn btn-info" onclick="goToEmployee(${cookie['empid'].getValue()});">Back</button> 
	   
	   </div>
 	</div>
  
	  <div class="col-sm-8 col-lg-5">
	    <div class="card bg-light  mb-3" style="color: navy;">
	      <div class="card-body" style="padding: 0.25rem">
	       <div id="table" class="table-editable">
	      	<table class="table table-bordered table-responsive-md table-striped text-center">
		        <tr>
		          <th class="text-center">Description</th>
		          <th class="text-center">QTY</th>
		          <th class="text-center">Size</th>
		          <th class="text-center">Remarks</th>
		        </tr>
		        <c:forEach items="${productItems}" var="item">
		        <tr class="form-group">
		          <td class="pt-3-half">${item.prodItemName}</td>
		          <td class="pt-3-half">${item.quantity}</td>
		          <td class="pt-3-half">${item.prodSize}</td>
				 <td class="pt-3-half">${item.remarks}</td>
		        </tr>
		        </c:forEach>
	      </table>
    	 </div>
		</div>
	  </div>
 	</div>
  	</div> --%>



	</div>
</body>
<script type="text/javascript">

/* function goToEmployee(empid){
	document.orderstatusform.action="${pageContext.request.contextPath}/order/allocation/"+empid;
	document.orderstatusform.method="POST";
	document.orderstatusform.submit();
	

} */


function closeWindow(){
		
	window.close(); 
	
}
</script>
</html>
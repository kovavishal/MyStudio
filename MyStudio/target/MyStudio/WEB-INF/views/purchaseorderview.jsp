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
		<form:form modelAttribute="Purchase">

			<div class="card">
				<div class="card-header text-center"
					style="padding-bottom: 0px; padding-top: 0px; background: skyblue; color: #30567D; font-weight: 625;">
					<jsp:include page="logout.jsp">
						<jsp:param name="page" value="Purchase Order View" />
					</jsp:include>
				</div>

				<div class="card-body bg-light border-success mb-3 col-lg-12"
					style="color: navy;">
					<div class="row">
						<div class="col-sm-4 col-lg-1">
							<label for="orderno">Invoice No</label> <input type="text"
								value="${purchase.invoiceNumber}" class="form-control"
								id="orderno" readonly="readonly" />
						</div>

						<div class="col-md-4 col-lg-3">
							<label for="custname">Supplier Name</label> <input type="text"
								value="${suppliers.name}" class="form-control" id="custname"
								readonly="readonly" />
						</div>
						<div class="col-md-4 col-lg-2">
							<label for="mobileno">Mobile No</label> <input type="text"
								value="${suppliers.mobileNo}" class="form-control" id="mobileno"
								readonly="readonly" />
						</div>
						<div class="col-md-4 col-lg-2">
							<label for="mobileno">Invoice Date</label> <input type="text"
								value="${purchase.strInvoiceDate}" class="form-control"
								id="mobileno" readonly="readonly" />
						</div>

						<div class="col-sm-4 col-lg-2">
							<label for="mobileno">Received On</label> <input type="text"
								value="${purchase.strGoodsReceivedDate}" class="form-control"
								id="mobileno" readonly="readonly" />
						</div>

						<div class="col-md-4 col-lg-2" align="right"
							style="padding-top: 20px;">
							<button class="btn btn-danger" onclick="closeWindow()"; javascript:window.close();">
								<i class="fa fa-close"
									style='font-size: 20px; padding: 0 1em 0 0'></i>Close
							</button>
						</div>


					</div>
					<div class="row">
						<div class="col-sm-4 col-lg-2">
							<label for="mobileno">Invoice Amount</label> <input type="text"
								value="${purchase.invoiceAmount}" class="form-control"
								id="mobileno" readonly="readonly" />
						</div>
						<div class="col-sm-4 col-lg-2">
							<label for="mobileno">Discount</label> <input type="text"
								value="${purchase.discount}" class="form-control" id="mobileno"
								readonly="readonly" />
						</div>
						<%-- <input type="text" value="${fn:toUpperCase(jobStatusMap[order.status])} ${jobStatus.name}" class="form-control" id="mobileno" readonly="readonly"/> --%>


						<div class="col-sm-4 col-lg-2">
							<label for="mobileno">Net Invoice Amount</label> <input
								type="text" value="${purchase.netPayable}" class="form-control"
								id="mobileno" readonly="readonly" />
						</div>


						<div class="col-sm-4 col-lg-2">
							<label for="mobileno">Amount Paid</label> <input type="text"
								value="${purchase.amountPaid}" class="form-control"
								id="mobileno" readonly="readonly" />
						</div>

						<div class="col-sm-4 col-lg-2">
							<label for="mobileno">Invoice Balance</label> <input type="text"
								value="${purchase.balancePayable}" class="form-control"
								id="mobileno" readonly="readonly" />
						</div>
						<div class="col-sm-4 col-lg-2">
							<label for="mobileno">Total Payable Amount</label> <input
								type="text" value="${suppliers.creditBalance}"
								class="form-control" id="mobileno" readonly="readonly" />
						</div>
					</div>

					<table
						class="table table-bordered table-responsive-md table-striped text-center"
						style="width: 80%">
						<thead>
							<tr>
								<th>Sl.No.</th>
								<th>Particulars</th>
								<th>Batch No</th>
								<th>Qty</th>
								<th>Rate</th>
								<th>Amount</th>
							</tr>
						</thead>
						<c:set var="slno" value="${1}" />
						<!-- <thead> -->
						<c:forEach items="${purchasePayments}" var="product">
							<tr>
								<td align="center"><c:out value="${slno}" /></td>
								<c:forEach items="${purchaseItems}" var="purchaseitems">
									<c:if test="${purchaseitems.id eq  product.purchaseTypeId}">
										<td align="left">${purchaseitems.name}</td>
									</c:if>
								</c:forEach>
								<%--  <td>${fn:toUpperCase(purchaseItemsTypeMap[product.purchaseTypeId])} ${purchaseItems.name}</td>  --%>

								<td>${(product.batchNo)}</td>
								<td align="center">${product.qty}</td>
								<td align="center">${product.rate}</td>
								<td align="right">${product.amount}</td>
							</tr>
							<c:set var="slno" value="${slno+1}" />
						</c:forEach>

						</font>
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
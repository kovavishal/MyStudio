<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="staticcontent.jsp"%>
<title>Work Order Review</title>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="container-fluid" id="main">
		<form:form method="POST" action="/allocation" modelAttribute="order"
			name="orderstatusform">
			<form:hidden path="custName" />
			<form:hidden path="strOrderId" />
			<form:hidden path="strOrderDate" />
			<form:hidden path="strDueDate" />
			<form:hidden path="status" />
			<form:hidden path="custType" />
			<div class="card">
				<div class="card-header text-center"
					style="padding-bottom: 0px; padding-top: 0px; background: skyblue; color: #30567D; font-weight: 625;">
					<jsp:include page="logout.jsp">
						<jsp:param name="page" value="Work Order Review" />
					</jsp:include>
				</div>

				<div class="card-body bg-light border-success mb-3 col-lg-12"
					style="color: navy;">
					<div class="row">
						<div class="col-sm-4 col-lg-3">
							<label for="orderno">Order No</label> <input type="text"
								value="${orderReview.orderId}" class="form-control" id="orderno"
								readonly="readonly" />
						</div>

						<div class="col-md-4 col-lg-3">
							<label for="custname">Customer Name</label> <input type="text"
								value="${orderReview.custName}" class="form-control"
								id="custname" readonly="readonly" />
						</div>
						<div class="col-md-4 col-lg-3">
							<label for="mobileno">Mobile No</label> <input type="text"
								value="${orderReview.mobileNo}" class="form-control"
								id="mobileno" readonly="readonly" />
						</div>
						<div class="col-md-4 col-lg-3">
							<label for="noofsheet">No of Sheets</label>
							<c:forEach items="${products}" var="product">
								<c:if test="${product.productTypeId =='102'}">
									<tr>
										<input type="text"
											value="${product.noOfSheet * product.noOfCopy}"
											class="form-control" id="noofsheet" readonly="readonly" />
									</tr>
								</c:if>
							</c:forEach>

							<%-- <c:forEach items="${products}" var="product">
     	<input type="text" value="${product.noOfSheet * product.noOfCopy}" class="form-control" id="noofsheet" readonly="readonly"/>
     	</c:forEach> --%>
						</div>
					</div>
				</div>
			</div>
			<div class="row ">

				<div class="col-sm-8 col-lg-3">
					<div class="card bg-light  mb-3" style="color: navy;">
						<div class="card-body" style="padding: 0.25rem">
							<div id="table" class="table-editable">
								<table
									class="table table-bordered table-responsive-md table-striped text-center">
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
						<button type="button" class="btn btn-info"
							onclick="goToEmployee(${cookie['empid'].getValue()});">Back</button>

					</div>
				</div>

				<div class="col-sm-8 col-lg-5">
					<div class="card bg-light  mb-3" style="color: navy;">
						<div class="card-body" style="padding: 0.25rem">
							<div id="table" class="table-editable">
								<table
									class="table table-bordered table-responsive-md table-striped text-center">
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
			</div>
		</form:form>
	</div>
</body>
<script type="text/javascript">


$(function () {
	
	$('#fromdate').datetimepicker({
		format: 'DD/MM/YYYY hh:mm A',
		icons: {
            time: "fa fa-clock-o",
            date: "fa fa-calendar",
            up: "fa fa-arrow-up",
            down: "fa fa-arrow-down",
            autoclose: true
        }
	
    });
	
	$('#todate').datetimepicker({
		format: 'DD/MM/YYYY hh:mm A',
		icons: {
            time: "fa fa-clock-o",
            date: "fa fa-calendar",
            up: "fa fa-arrow-up",
            down: "fa fa-arrow-down"
        }
    });
});
$(document).ready(function() {
$('#custName').autocomplete({
	serviceUrl: '${pageContext.request.contextPath}/autocomplete/agent',
	paramName: "searchTerm",
	delimiter: ",",
	
   	transformResult: function(response) {
		return {      	
		  suggestions: $.map($.parseJSON(response), function(item) {
		      return { value: item.name, data: item.id };
		   })
		 };
   	},
   	onSelect:function(item){
   		console.log("item :: " + item.data);
   		setCustAddr(item.data);
   	}
	
});
});
function goToEmployee(empid){
	document.orderstatusform.action="${pageContext.request.contextPath}/order/allocation/"+empid;
	document.orderstatusform.method="POST";
	document.orderstatusform.submit();
}

</script>
</html>
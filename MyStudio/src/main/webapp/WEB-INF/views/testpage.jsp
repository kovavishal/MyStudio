<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="staticcontent.jsp"%>
<title>Test Page</title>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="container-fluid" id="main">
		<div class="card">
			<div class="card-header text-center"
				style="padding-bottom: 0px; padding-top: 0px; color: #30567D; font-weight: 625;">
				<jsp:include page="logout.jsp">
					<jsp:param name="page" value="Testpage" />
				</jsp:include>
			</div>
			<div class="card-body bg-alert border-success mb-3 col-lg-12"
				style="color: navy;">
				<form:form action="testpage" method="POST" modelAttribute="NewTable"
					name="testpageform">
					<%-- <form:hidden path="custId" id="cid"/> --%>
					<div class="form-row">
						<div class="form-group col-md-2 col-lg-2">
							<label for="name">Name of the staff</label>
							<form:input path="name" class="form-control" id="name" />
						</div>
						<div class="form-group col-md-2 col-lg-3">
							<label for="email">Email Name</label>
							<form:input path="email" class="form-control" id="email" />
						</div>
					</div>
					<div class="form-row">
						<div class="form-group col-md-2 col-lg-2">
							<label for="name">Address</label>
							<form:input path="address" class="form-control" id="address" />
						</div>
						<div class="form-group col-md-2 col-lg-3">
							<label for="email">City</label>
							<form:input path="city" class="form-control" id="city" />
						</div>
					</div>


					<div class="form-group row row col-sm-12 col-md-12 col-lg-2">
						<button type="button" class="btn btn-primary btn-lg btn-block"
							onclick="savedata();">Submit</button>
					</div>
				</form:form>
			</div>
			<div class="card-footer">All right reserved</div>
		</div>
	</div>
</body>
<script type="text/javascript">
function saveReceipt(){
	var expenseId = $('#expenseId').val();
	var amount = $('#payment').val();
	var payable = $('#payable_0').val();
	payable = parseFloat(payable).toFixed(2);
	amount = parseFloat(amount).toFixed(2);
	if(expenseId == '101' && parseFloat(amount) <= parseFloat(payable)){
		return true;
	}else{
		alert("You are not allowed to pay the selected order amount payable");
		return false;
	}
   
}


function savedata(){
	alert("hai");
	document.testpageform.action="${pageContext.request.contextPath}/report/saveNewTable";
	document.testpageform.method="POST";
	document.testpageform.submit();
	
}
</script>
</html>
<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="staticcontent.jsp"%>
<title>Bill Copy</title>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="container-fluid" id="main">
		<div class="card">
			<div class="card-header text-center"
				style="padding-bottom: 0px; padding-top: 0px; background: skyblue; color: #30567D; font-weight: 625;">
				<jsp:include page="logout.jsp">
					<jsp:param name="page" value="Bill Copy" />
				</jsp:include>
			</div>
			<div class="card-body bg-light border-success mb-3 col-lg-12"
				style="color: navy;">
				<div class="form-row">
					<div class="form-group col-md-2 col-lg-2">
						<label for="receiptid">Bill No</label> <input type="text"
							class="form-control" id="orderId" />
					</div>
				</div>


				<div class="form-row text-right">
					<div class="form-group col-md-2 col-lg-2">
						<!-- 	<button type="button" class="btn btn-success" style="width: 100px;" onclick="printOrder();">Print</button> -->
						<button type="button" class="btn btn-success"
							style="width: 300px; font-size: 20px;" onclick="printOrder();">
							<i class="fa fa-print"
								style='font-size: 20px; padding: 0 1em 0 0'></i>Print Copy
						</button>

					</div>
				</div>

			</div>
			<div class="card-footer">All right reserved</div>
		</div>
	</div>
</body>
<script type="text/javascript">
function printOrder() {
	var orderId = $('#orderId').val();
	var mode =1;
	if(orderId != null && orderId != ''){
		console.log("order id to pring" + orderId);
		var url = '${pageContext.request.contextPath}/printorder /'+orderId+"/"+mode;
	  	var win = window.open(url, '_blank');
	  	win.focus();
	}else{
		alert("Enter the order id to print the bill")
	}
}
</script>
</html>
<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="staticcontent.jsp"%>
<title>Home</title>
</head>
<style>
.card {
	/* Add shadows to create the "card" effect */
	box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
	transition: 0.3s;
}

.card .products {
	background-color: white;
}
/* On mouse-over, add a deeper shadow */
.card:hover {
	box-shadow: 0 8px 16px 0 rgba(0, 0, 0, 0.986);
}
</style>
<body>
	
	<%@include file="navbar.jsp"%>

	<div class="container-fluid" id="main"
		style="padding-left: 50px; height: 100%; align: center; position: relative">

		<div
			style="padding-left: 50px; padding-bottom: 0px; padding-top: 0px; color: #30567D; font-weight: 625;">
			<jsp:include page="logout.jsp">
				<jsp:param name="page" value="" />
			</jsp:include>
		</div>
		<div class="card-deck bg-light col-lg-12"
			style="padding-left: 50px; padding-top: 50px; position: relative">

			<a href="<%=request.getContextPath()%>/workorder">
				<div class="card mb-4 bg-green col-lg-6">
					<div class="card-body">
						<img src="images/workorder.jpg"
							style="width: 125px; height: 100px;" alt="Avatar">
					</div>
					<div class="card-title" align="center">
						<a href="<%=request.getContextPath()%>/workorder">New Work
							Order</a>
					</div>

				</div>
			</a> <a href="<%=request.getContextPath()%>/order/status">
				<div class="card mb-4 col-sm-4">
					<div class="card-body">
						<img src="images/work-order-status.jpg"
							style="width: 125px; height: 100px; position: relative"
							alt="Avatar">
					</div>
					<div class="card-title" align="center">
						<a href="<%=request.getContextPath()%>/order/status">Work
							Order Status</a>
					</div>
				</div>
			</a>

			<div class="card mb-4 col-lg-3">
				<a href="<%=request.getContextPath()%>/workorder/cancel">
					<div class="card-body">
						<img src="images/cancel.jpg"
							style="width: 125px; height: 100px; position: relative"
							alt="Avatar">
					</div>
					<div class="card-title" align="center">
						<a href="<%=request.getContextPath()%>/workorder/cancel">Cancel
							Order</a>
					</div>
				</a>
			</div>

			<a href="<%=request.getContextPath()%>/payment/billcopy">
				<div class="card mb-4 col-lg-6">
					<div class="card-body">
						<img src="images/duplicate-bills.png"
							style="width: 125px; height: 100px; position: relative"
							alt="Avatar">
					</div>
					<div class="card-title" align="center">
						<a href="<%=request.getContextPath()%>/payment/billcopy">Duplicate
							Bill</a>
					</div>
				</div>
			</a>

			<div class="w-100 d-none d-xl-block">
				<!-- wrap every 5 on xl-->
			</div>
			<a href="<%=request.getContextPath()%>/payment/receipt">
				<div class="card mb-4 col-lg-6">
					<div class="card-body">
						<img src="images/receipt.jpg"
							style="width: 125px; height: 100px; position: relative"
							alt="Avatar">
					</div>
					<div class="card-title" align="center">
						<a href="<%=request.getContextPath()%>/payment/receipt">Receipt</a>
					</div>
				</div>
			</a> <a href="<%=request.getContextPath()%>/payment/pay">
				<div class="card mb-4 col-lg-6">
					<div class="card-body">
						<img src="images/payment.png"
							style="width: 125px; height: 100px; position: relative"
							alt="Avatar">
					</div>
					<div class="card-title" align="center">
						<a href="<%=request.getContextPath()%>/payment/pay">Payment</a>
					</div>
				</div>
			</a> <a href="<%=request.getContextPath()%>/report/daily">
				<div class="card mb-4 col-lg-6">
					<div class="card-body">
						<img src="images/dailyreport.png"
							style="width: 125px; height: 100px; position: relative"
							alt="Avatar">
					</div>
					<div class="card-title" align="center">
						<a href="<%=request.getContextPath()%>/report/daily">Daily
							Business Report</a>
					</div>
				</div>
			</a> <a href="<%=request.getContextPath()%>/report/agent">
				<div class="card mb-4 col-lg-6">
					<div class="card-body">
						<img src="images/agent.jpg"
							style="width: 125px; height: 100px; position: relative"
							alt="Avatar">
					</div>
					<div class="card-title" align="center">
						<a href="<%=request.getContextPath()%>/report/agent">Agent's
							Report</a>
					</div>

				</div>
			</a>
			<div class="w-100 d-none d-xl-block">
				<!-- wrap every 5 on xl-->
			</div>
			<a href="<%=request.getContextPath()%>/addcustomer">
				<div class="card mb-4 col-sm-4">
					<div class="card-body">
						<img src="images/customer.jpg"
							style="width: 125px; height: 100px; position: relative"
							alt="Avatar">
					</div>
					<div class="card-title" align="center">
						<a href="<%=request.getContextPath()%>/addcustomer">Add
							Customer</a>
					</div>
				</div>
			</a> <a href="<%=request.getContextPath()%>/payment/purchase">
				<div class="card mb-4 col-sm-4">
					<div class="card-body">
						<img src="images/purchase.png"
							style="width: 125px; height: 100px; position: relative"
							alt="Avatar">
					</div>
					<div class="card-title" align="center">
						<a href="<%=request.getContextPath()%>/payment/purchase">Purchase</a>
					</div>
				</div>
			</a> <a href="<%=request.getContextPath()%>/report/customeraddress">
				<div class="card mb-4 col-lg-6">
					<div class="card-body">
						<img src="images/address-book.png"
							style="width: 125px; height: 100px; position: relative"
							alt="Avatar">
					</div>
					<div class="card-title" align="center">
						<a href="<%=request.getContextPath()%>/report/customeraddress">Customer
							Address Book</a>
					</div>
				</div>
			</a> <a href="<%=request.getContextPath()%>/report/mbrsummary">
				<div class="card mb-4 col-lg-6">
					<div class="card-body">
						<img src="images/MBR-summary.jpg"
							style="width: 125px; height: 100px; position: relative"
							alt="Avatar">
					</div>
					<div class="card-title" align="center">
						<a href="<%=request.getContextPath()%>/report/mbrsummary">Monthly
							Business Summary</a>
					</div>

				</div>
			</a>
			<div class="w-100 d-none d-xl-block">
				<!-- wrap every 5 on xl-->
			</div>
			<a
				href="<%=request.getContextPath()%>/report/customeroffersalereport">
				<div class="card mb-4 col-lg-6">
					<div class="card-body">
						<img src="images/offer-sales.jpg"
							style="width: 125px; height: 100px; position: relative"
							alt="Avatar">
					</div>
					<div class="card-title" align="center">
						<a
							href="<%=request.getContextPath()%>/report/customeroffersalereport">Sale-Offer
							Report</a>
					</div>
				</div>
			</a> <a href="<%=request.getContextPath()%>/report/ordervsdespatch">
				<div class="card mb-4 col-lg-6">
					<div class="card-body">
						<img src="images/order-status.jpg"
							style="width: 125px; height: 100px; position: relative"
							alt="Avatar">
					</div>
					<div class="card-title" align="center">
						<a href="<%=request.getContextPath()%>/report/ordervsdespatch">Order
							Status Report</a>
					</div>
				</div>
			</a> <a href="<%=request.getContextPath()%>/report/customervsbusiness">
				<div class="card mb-4 col-lg-6">
					<div class="card-body">
						<img src="images/sms-customer.jpg"
							style="width: 125px; height: 100px; position: relative"
							alt="Avatar">
					</div>
					<div class="card-title" align="center">
						<a href="<%=request.getContextPath()%>/report/customervsbusiness">SMS
							to Customer</a>
					</div>
				</div>
			</a> <a
				href="<%=request.getContextPath()%>/report/customeroutstandingdetails">
				<div class="card mb-4 col-lg-6">
					<div class="card-body">
						<img src="images/outstanding-summary.png"
							style="width: 125px; height: 100px; position: relative"
							alt="Avatar">
					</div>
					<div class="card-title" align="center">
						<a
							href="<%=request.getContextPath()%>/report/customeroutstandingdetails">Outstanding
							Summary Report</a>
					</div>

				</div>
			</a>
		</div>
	</div>

</body>
</html>
<!DOCTYPE html>
<html lang="en">
<head>

<link href="https://fonts.googleapis.com/css?family=Roboto" rel="stylesheet">
<link href="css/navbarcus.css" rel="stylesheet">

<meta charset="UTF-8">
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/customIndex.css" rel="stylesheet">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="css/bootstrap-responsive.css" rel="stylesheet">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">


<title>My Studio</title>

<style>
.sepia {
	filter: sepia(100%);
}
</style>
</head>
<body>


<div class="nav-side-menu">
	<div class="brand">
		<img src="<%=request.getContextPath()%>/images/camera-brand.jpg"
			height="150" width="200">
	</div>
	<i class="fa fa-bars fa-2x toggle-btn" data-toggle="collapse" data-target="#menu-content"></i>
	<c:if test="${cookie['role'].getValue() ne 'Employee' and cookie['role'].getValue() ne 'Agent'}">
		<div class="menu-list">
			<font color="white" style="font-size: 14px; font-weight: 500;">
				<ul id="menu-content" class="menu-content collapse out">
					<c:if test="${cookie['role'].getValue() ne 'Purchase'}">
							<%-- 	
        					 <a href="<%=request.getContextPath()%>/report/dashboard">
        					 <li style='background-color:red; color:white'>&nbsp;&nbsp;DashBoard</li></a> --%>

							<li data-toggle="collapse" data-target="#dashboard"
									style='background-color: red; color: white' class="collapsed">
								<ul>
									<a href="<%=request.getContextPath()%>/report/dashboard"><li>&nbsp;&nbsp;DASHBOARD</li></a>
								</ul>
							</li>
							<li data-toggle="collapse" data-target="#workorder"
								class="collapsed"><span class="arrow">&nbsp;Work Order</span>
								<ul class="sub-menu collapse" id="workorder">
									<a href="<%=request.getContextPath()%>/workorder"><li>New Order</li></a>
									<a href="<%=request.getContextPath()%>/order/status"><li>Order Status</li></a>
									<a href="<%=request.getContextPath()%>/workorder/cancel"><li>Cancel Order</li></a>
									<a href="<%=request.getContextPath()%>/payment/billcopy"><li>Duplicate Bill</li></a>
								</ul>
							</li>

							<li data-toggle="collapse" data-target="#customertab" class="collapsed">
								<span class="arrow">&nbsp;Master Management</span>
								<ul class="sub-menu collapse" id="customertab">
									<a href="<%=request.getContextPath()%>/addcustomer"><li>New Customer</li></a>
									<c:if test="${cookie['role'].getValue() eq 'SuperAdmin'}">
										<a href="<%=request.getContextPath()%>/editcustomer"><li>Edit Customer</li></a>
										<a href="<%=request.getContextPath()%>/supplier/edit"><li>Edit Supplier</li></a>
									</c:if>
									<a href="<%=request.getContextPath()%>/employee"><li>New Employee</li></a>
									<a href="<%=request.getContextPath()%>/editemployee"><li>Edit Employee</li></a>
								</ul>
							</li>
						</c:if>
						<li data-toggle="collapse" data-target="#process" class="collapsed">
							<span class="arrow">&nbsp;Purchase Management</span>
							<ul class="sub-menu collapse" id="process">
								<a href="<%=request.getContextPath()%>/payment/purchase"><li>Purchase</li></a>
								<a href="<%=request.getContextPath()%>/payment/pay"><li>Payment</li></a>
								<a href="<%=request.getContextPath()%>/metadata/add"><li>Add Meta Data</li></a>
								<a href="<%=request.getContextPath()%>/supplier"><li>New Supplier</li></a>
								<%-- 	<a href="<%=request.getContextPath()%>/supplier/edit"><li>Edit Supplier</li></a> --%>
								<a href="<%=request.getContextPath()%>/report/supplier"><li>Supplier Report(IND)</li></a>
								<a href="<%=request.getContextPath()%>/report/supplieroutstanding"><li>Supplier's Due Report</li></a>
							</ul>
						</li>
						<c:if test="${cookie['role'].getValue() ne 'Purchase'}">
							<li data-toggle="collapse" data-target="#service" class="collapsed">
								<span class="arrow">&nbsp;Accounts</span>
								<ul class="sub-menu collapse" id="service">
									<a href="<%=request.getContextPath()%>/payment/receipt"><li>Receipt</li></a>
									<%--  <a href="<%=request.getContextPath()%>/payment/receiptagent"><li>Receipt for Agents</li></a>  --%>
									<a href="<%=request.getContextPath()%>/payment/pay"><li>Payment</li></a>
									<a href="<%=request.getContextPath()%>/report/ledger"><li>Ledger Report</li></a>
									<a href="<%=request.getContextPath()%>/report/daily"><li>Daily Report</li></a>
									<a href="<%=request.getContextPath()%>/report/selectedday"><li>Selected Day Report</li></a>
									<a href="<%=request.getContextPath()%>/report/monthly"><li>M B R (Detail)</li></a>
									<a href="<%=request.getContextPath()%>/report/mbrsummary"><li>M B R (Summary)</li></a>
									<a href="<%=request.getContextPath()%>/report/srpreport"><li>S/R/P Report</li></a>
									<%--    <a href="<%=request.getContextPath()%>/report/mbstatement"><li>Monthly Report</li></a> --%>
								</ul>
							</li>
							<li data-toggle="collapse" data-target="#reports" class="collapsed">
								<span class="arrow">&nbsp;Business reports</span>
								<ul class="sub-menu collapse" id="reports">
									<a href="<%=request.getContextPath()%>/report/customer"><li>Customer Report(IND)</li></a>
									<a href="<%=request.getContextPath()%>/report/customeroffersalereport"><li>Offer-Achiever Report</li></a>
									<a href="<%=request.getContextPath()%>/report/customeroutstanding"><li>Customer Due-SMS</li></a>
									<a href="<%=request.getContextPath()%>/report/customeroutstandingdetails"><li>Outstanding Summary</li></a>
									<a href="<%=request.getContextPath()%>/report/dateWiseCustomeroutstandingdetails"><li>Date wise Outstanding Summary</li></a> 
									<a href="<%=request.getContextPath()%>/report/customervsbusiness"><li>Offer-SMS to Cust.</li></a>
									<a href="<%=request.getContextPath()%>/report/cancelorderreport"><li>Cancel Order Report</li></a>
									<a href="<%=request.getContextPath()%>/report/undeliveredalbum"><li>Undeliverd Album</li></a>
									<a href="<%=request.getContextPath()%>/report/agent"><li>Agent Report</li></a>
									<%--    		<a href="<%=request.getContextPath()%>/report/agentcorrection"><li>Agent Report Correction</li></a> --%>
									<%-- 	<a href="<%=request.getContextPath()%>/report/supplier"><li>Supplier Report</li></a> --%>
									<a href="<%=request.getContextPath()%>/report/ordervsdespatch"><li>Order Status Report</li></a>
								</ul>
							</li>

							<li data-toggle="collapse" data-target="#settings" class="collapsed">
								<span class="arrow">&nbsp;Settings</span>
								<ul class="sub-menu collapse" id="settings">
									<a href="<%=request.getContextPath()%>/metadata/products"><li>Add Products</li></a>
									<a href="<%=request.getContextPath()%>/metadata/itemtypes"><li>Add Items</li></a>
									<a href="<%=request.getContextPath()%>/metadata/productsizes"><li>Add Product Size</li></a>
									<a href="<%=request.getContextPath()%>/metadata/ratetypes"><li>Add Rate Type</li></a>
									<a href="<%=request.getContextPath()%>/metadata/itemsizerate"><li>Add Rate Amount</li></a>
									<a href="<%=request.getContextPath()%>/metadata/add"><li>Add Meta Data</li></a>
								</ul>
							</li>


							<li data-toggle="collapse" data-target="#utility" class="collapsed">
								<span class="arrow">&nbsp;Utility</span>
								<ul class="sub-menu collapse" id="utility">
									<a href="<%=request.getContextPath()%>/report/backup">
									<li>Daily Backup</li>
									</a>
									<a href="<%=request.getContextPath()%>/report/customeraddress"><li>Customer Address Report</li></a>
									<%-- <a href="<%=request.getContextPath()%>/report/testpage"><li>TestPage</li></a>  --%>
								</ul>
							</li>
					</c:if>
				</ul>
		</div>
	</c:if>
</font> 
</div>
</body>
</html>
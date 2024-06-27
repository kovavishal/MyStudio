<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="staticcontent.jsp"%>
<title>Work Order Status</title>
<script src="<%=request.getContextPath()%>/js/jquery-3.3.1.js"></script>
<script src="<%=request.getContextPath()%>/js/dataTables.buttons.min.js"></script>
<script src="<%=request.getContextPath()%>/js/buttons.flash.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jszip.min.js"></script>
<script src="<%=request.getContextPath()%>/js/pdfmake.min.js"></script>
<script src="<%=request.getContextPath()%>/js/vfs_fonts.js"></script>
<script src="<%=request.getContextPath()%>/js/buttons.html5.min.js"></script>
<script src="<%=request.getContextPath()%>/js/buttons.print.min.js"></script>
<script
	src="<%=request.getContextPath()%>/js/bootstrap-datetimepicker.min.js"></script>
</head>
<body>

	<%@include file="navbar.jsp"%>
	<div class="container-fluid" id="main">
		<div class="card">
			<div class="card-header text-center"
				style="padding-bottom: 0px; padding-top: 0px; background: skyblue; color: #30567D; font-weight: 625;">
				<jsp:include page="logout.jsp">
					<jsp:param name="page" value="Work Order Status" />
				</jsp:include>
			</div>
			<form:form method="POST" action="search" modelAttribute="order"
				name="orderstatusform" id="orderstatusform">

				<button type="button" class="btn btn-primary" data-toggle="modal"
					data-target="#mandateModel" hidden="true" id="mandateBtn"></button>
				<div class="modal fade" id="mandateModel" tabindex="-1"
					role="dialog" aria-labelledby="mandateModel" aria-hidden="true">
					<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-body" style="color: #30567D; font-weight: 625;">
								Please enter at least one search criteria</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-secondary"
									data-dismiss="modal">Close</button>
							</div>
						</div>
					</div>
				</div>

				<div class="card-body bg-light border-success mb-3 col-lg-12"
					style="color: navy;">
					<div class="row">
						<div class="col-sm-4 col-lg-3">
							<label for="orderno">Order No</label>
							<form:input path="strOrderId" class="form-control" id="orderno" />
						</div>

						<div class="col-md-4 col-lg-3">
							<label for="fromdate">From Date</label>
							<form:input path="strOrderDate" class="form-control"
								id="fromdate" />
						</div>
						<div class="col-md-4 col-lg-3">
							<label for="todate">To Date</label>
							<form:input path="strDueDate" class="form-control" id="todate" />
						</div>
					</div>

					<div class="row">
						<div class="col-md-6 col-lg-3">
							<label for="customertype">Customer Type</label>
							<form:select path="custType" class="form-control" id="custType">
								<option value="">-Select Type-</option>
								<c:forEach items="${customerTypes}" var="customerType">
									<c:choose>
										<c:when test="${customerType.id eq  order.custType}">`
	    			<option value="${customerType.id}" selected="selected">${customerType.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${customerType.id}">${customerType.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
						</div>
						<div class="col-md-6 col-lg-3">
							<label for="custName">Customer Name</label>
							<form:input path="custName" class="form-control" id="custName" />
						</div>
						<div class="col-md-6 col-lg-3">
							<label for="orderstatus">Order Status</label>
							<form:select path="status" class="form-control" id="orderstatus">
								<option value="">-Select Type-</option>
								<c:forEach items="${orderstatus}" var="status">
									<c:choose>
										<c:when test="${status.id eq  order.status}">`
	    			<option value="${status.id}" selected="selected">${status.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${status.id}">${status.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
						</div>
					</div>
					<div class="col-lg-9 text-right" style="padding-top: 10px;">
						<a href="<%= request.getContextPath()%>/order/status"
							style="width: 300px; font-size: 20px" class="btn btn-info"
							role="button"> <i class="fa fa-refresh"
							style='font-size: 20px; padding: 0 1em 0 0'></i>Refresh
						</a>

						<!-- <button type="button" class="btn btn-primary" id="searchBtn">Search</button> -->
						<button type="button" class="btn btn-success" id="searchBtn"
							style="width: 300px; font-size: 20px">
							<i class="fa fa-search"
								style='font-size: 20px; padding: 0 1em 0 0'></i>Search
						</button>


					</div>

				</div>
				<button class="btn btn-danger"
					style="float: left; width: 200px; font-size: 20px;" id="PrintBtn">Print
					to PDF</button>
				<div class="col-sm-8 col-lg-12"
					style="width: 100%; overflow: auto; height: 500px;">

					<table
						class="table table-bordered table-responsive-md table-striped text-center"
						style="color: navy;" id="statustable">
						<tr>
							<th class="text=center">Sl.No.</th>
							<th class="text-center">Order Date</th>
							<th class="text-center">Order No</th>
							<th class="text-center">Cust.Name</th>
							<th class="text-center"><spring:eval
									expression="@environment.getProperty('job.title.design')" /></th>
							<th class="text-center" nowrap="nowrap"><spring:eval
									expression="@environment.getProperty('job.title.pad')" /></th>
							<th class="text-center"><spring:eval
									expression="@environment.getProperty('job.title.print')" /></th>
							<th class="text-center"><spring:eval
									expression="@environment.getProperty('job.title.lamination')" /></th>
							<th class="text-center"><spring:eval
									expression="@environment.getProperty('job.title.bind')" /></th>
							<th class="text-center"><spring:eval
									expression="@environment.getProperty('job.title.despatch')" /></th>
							<th class="text-center">Due Date</th>
							<th class="text-center">Status</th>
						</tr>
						<c:set var="counter" value="0" scope="page" />
						<c:forEach items="${orders}" var="order">
							<c:set var="counter" value="${counter + 1}" scope="page" />
							<tr class="form-group">
								<td class="pt-3-half" nowrap="nowrap">${counter}</td>
								<td class="pt-3-half" nowrap="nowrap">${order.strOrderDate}</td>
								<td class="pt-3-half"><a href="javascript:void(0)"
									onclick="reviewOrder(${order.orderId});">${order.orderId}</a></td>
								<td class="pt-3-half">${order.custName}</td>
								<td class="pt-3-half"><c:forEach
										items="${order.jobAllocations}" var="job">
										<c:if test="${job.id.depotId eq depots['Design']}">
											<c:out value="${jobStatus[job.statusId]}"></c:out>
										</c:if>
									</c:forEach></td>
								<td class="pt-3-half"><c:forEach
										items="${order.jobAllocations}" var="job">
										<c:if
											test="${job.id.depotId eq depots['EmpossPad'] || job.id.depotId eq depots['SparkalPad'] ||job.id.depotId eq depots['3DPad'] || job.id.depotId eq depots['AgarlicPad']||job.id.depotId eq depots['NormalPad']}">
											<c:out value="${jobStatus[job.statusId]}"></c:out>
										</c:if>
									</c:forEach></td>
								<td class="pt-3-half"><c:forEach
										items="${order.jobAllocations}" var="job">
										<c:if test="${job.id.depotId eq depots['Printing']}">
											<c:out value="${jobStatus[job.statusId]}"></c:out>
										</c:if>
									</c:forEach></td>

								<td class="pt-3-half"><c:forEach
										items="${order.jobAllocations}" var="job">
										<c:if test="${job.id.depotId eq depots['Lamination']}">
											<c:out value="${jobStatus[job.statusId]}"></c:out>
										</c:if>
									</c:forEach></td>

								<td class="pt-3-half"><c:forEach
										items="${order.jobAllocations}" var="job">
										<c:if test="${job.id.depotId eq depots['Binding']}">
											<c:out value="${jobStatus[job.statusId]}"></c:out>
										</c:if>
									</c:forEach></td>

								<td class="pt-3-half"><c:forEach
										items="${order.jobAllocations}" var="job">
										<c:if test="${job.id.depotId eq depots['Despatch']}">
											<c:out value="${jobStatus[job.statusId]}"></c:out>
										</c:if>
									</c:forEach></td>
								<td class="pt-3-half" nowrap="nowrap">${order.strDueDate}</td>
								<td class="pt-3-half">${jobStatus[order.status]}</td>
							</tr>
						</c:forEach>
					</table>

				</div>
			</form:form>
			<div class="card-footer">All right reserved</div>
		</div>
	</div>
</body>
<script type="text/javascript">

$(function () {
	
	$('#fromdate').datetimepicker({
		format: 'DD/MM/YYYY',
		icons: {
            time: "fa fa-clock-o",
            date: "fa fa-calendar",
            up: "fa fa-arrow-up",
            down: "fa fa-arrow-down",
            autoclose: true
        }
	
    });
	
	$('#todate').datetimepicker({
		format: 'DD/MM/YYYY',
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
	serviceUrl: '${pageContext.request.contextPath}/order/autocomplete/customer',
	paramName: "searchTerm",
	delimiter: ",",
	minChars:3,
   	transformResult: function(response) {
		return {      	
		  suggestions: $.map($.parseJSON(response), function(item) {
		      return { value: item.name, data: item.id };
		   })
		 };
   	},
   	/* onSelect:function(item){
   		console.log("item :: " + item.data);
   		setCustAddr(item.data);
   	} */
	
});

$("#searchBtn").click(function() {
	//alert("hello");
	if($('#orderno').val()=='' && $('#fromdate').val()=='' && $('#todate').val()=='' 
			&& $('#custType').val()=='' && $('#custName').val()=='' && $('#orderstatus').val()==''){
		$('#mandateBtn').click();
	}else{
		$( "#orderstatusform" ).submit();	
	}
});
 $('#statustable').DataTable();

});

function reviewOrder(orderId){
	document.orderstatusform.action="review/"+orderId ;
	document.orderstatusform.method="POST";
	document.orderstatusform.submit();
}

function printData()
{
   var divToPrint=document.getElementById("statustable");
   newWin= window.open("");
   newWin.document.write(divToPrint.outerHTML);
   newWin.print();
   newWin.close();
}

$('#PrintBtn').on('click',function(){
printData();
})

</script>

</html>
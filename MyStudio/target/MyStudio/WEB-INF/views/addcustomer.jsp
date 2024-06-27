<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="staticcontent.jsp"%>
<title>Customer</title>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="container-fluid" id="main">
		<div class="card">
			<div class="card-header text-center"
				style="padding-bottom: 0px; padding-top: 0px; background: skyblue; color: #30567D; font-weight: 625;">
				<jsp:include page="logout.jsp">
					<jsp:param name="page" value="Customer" />
				</jsp:include>
			</div>
			<div class="card-body bg-light border-success mb-3 col-lg-12"
				style="color: navy;">
				<form:form method="POST" action="addcustomer"
					modelAttribute="customer" name="addcustomerform">
					<form:hidden path="address[0].addrId" />
					<form:hidden path="addNew" />
					<form:hidden path="editMode" id="editMode" />

					<div class="form-row">

						<div class="form-group col-md-2 col-lg-3">
							<label for="custid">Customer No</label>
							<form:input path="custId" class="form-control" id="custid"
								readonly="true" required="true" />
						</div>
						<div class="form-group col-md-2 col-lg-3">
							<label for="name">Customer Name</label>
							<form:input path="firstName" class="form-control" id="name"
								required="true" />
						</div>

						<div class="form-group col-md-2 col-lg-3">
							<label for="area">Area</label>
							<form:select path="address[0].areaId" class="form-control"
								id="area" required="true">
								<option value="">--Select Area--</option>
								<c:forEach items="${areas}" var="area">
									<c:choose>
										<c:when test="${area.id eq  customer.address[0].areaId}">
											<option value="${area.id}" selected="selected">${area.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${area.id}">${area.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
						</div>
					</div>
					<div class="form-row">
						<%-- <div class="form-group col-md-2 col-lg-3">
      	<label for="dob">Date Of Birth</label>
      	<form:input path="strDob" class="form-control" id="dob"/>
    </div> --%>
						<div class="form-group col-md-2 col-lg-3">
							<label for="customertype">Type</label>
							<form:select path="custType" class="form-control"
								id="customerType" onchange="setRate(this.value)" required="true">
								<option value="">--Select Type--</option>
								<c:forEach items="${customerTypes}" var="customerType">
									<c:choose>
										<c:when test="${customerType.id eq  customer.custType}">
											<option value="${customerType.id}" selected="selected">${customerType.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${customerType.id}">${customerType.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
						</div>
						<div class="form-group col-md-6 col-lg-3">
							<label for="studioname">Studio Name</label>
							<form:input path="studioName" class="form-control"
								id="studioname" />
						</div>

						<div class="form-group col-md-2 col-lg-3">
							<label for="address1">Address Line1</label>
							<form:input path="address[0].address1" class="form-control"
								id="address1" required="true" />
						</div>
					</div>

					<div class="form-row">
						<div class="form-group col-md-2 col-lg-3">
							<label for="address2">Address Line2</label>
							<form:input path="address[0].address2" class="form-control"
								required="true" id="address2" />
						</div>
						<div class="form-group col-md-2 col-lg-3">
							<label for="statue">State</label>
							<form:select path="address[0].stateId" class="form-control"
								id="state">
								<c:forEach items="${states}" var="state">
									<c:choose>
										<c:when test="${state.id eq  customer.address[0].stateId}">
											<option value="${state.id}" selected="selected">${state.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${state.id}">${state.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
						</div>
						<div class="form-group col-md-4 col-lg-3">
							<label for="phone">Phone Number</label>
							<form:input path="phoneNo" class="form-control" id="phone" />
						</div>
					</div>

					<div class="form-row">
						<%-- <div class="form-group col-md-2 col-lg-3">
	      <label for="coutry">Country</label>
		     <form:select path="address[0].countryId" class="form-control" id="coutry">
		    	<option value="10001" selected="selected">India</option>
			</form:select>
	    </div> --%>

						<div class="form-group col-md-4 col-lg-3">
							<label for="mobile">Mobile Number</label>
							<form:input path="mobileNo" class="form-control" id="mobile"
								required="true" maxlength="10" />
						</div>

						<div class="form-group col-md-6 col-lg-3">
							<label for="email">E-Mail</label>
							<form:input path="eamil" class="form-control" id="email"
								type="email" required="true"/>
						</div>

						<div class="form-group col-md-2 col-lg-3">
							<label for="rateType">Rate Type</label>
							<form:select path="rateType" class="form-control" id="rateType"
								required="true">
								<option value="">--Select Rate Type--</option>
								<c:forEach items="${rateTypes}" var="rate">
									<c:choose>
										<c:when test="${rate.id eq  customer.rateType}">
											<option value="${rate.id}" selected="selected">${rate.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${rate.id}">${rate.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
						</div>

						<%--  <div class="form-group col-md-2 col-lg-3">
	      <label for="city">City</label>
		     <form:select path="address[0].city" class="form-control" id="city">
		    	<option value="10001" selected="selected">Neyveli</option>
			</form:select>
	    </div> --%>
						<%-- <div class="form-group col-md-6 col-lg-3">
      		<label for="pin">PIN Code</label>
      		<form:input path="address[0].pin" class="form-control" id="pin"/>
    	</div> --%>
					</div>
					<div class="form-row">

						<div class="form-group col-md-6 col-lg-2">
							<label for="creditlimit">Credit Limit</label>
							<form:input path="creditLimit" class="form-control"
								id="creditlimit" required="true" />
						</div>
						<div class="form-group col-md-6 col-lg-2">
							<label for="creditdays">Credit Days</label>
							<form:input path="creditDays" class="form-control"
								id="creditdays" required="true" />
						</div>
						<div class="form-group col-md-6 col-lg-2">
							<label for="balance">Credit Balance</label>
							<c:if test="${!customer.editMode}">
								<form:input path="creditBalance" class="form-control"
									id="balance" />
							</c:if>
							<c:if test="${customer.editMode}">
								<form:input path="creditBalance" class="form-control"
									id="balance" />
							</c:if>
						</div>
						<div class="form-group col-md-6 col-lg-3">
							<label for="gst">GST No</label>
							<form:input path="gstNo" class="form-control" id="gst" />
						</div>

						<%--  <div class="form-group col-md-2 col-lg-3">
	      <label for="department">Department</label>
		     <form:select path="depotId" class="form-control" id="department">
		    	<c:forEach items="${deportments}" var="depot">
		    	<c:choose>
		    		<c:when test="${depot.id eq  depotId}">
		    			<option value="${depot.id}" selected="selected">${depot.name}</option>
		    		</c:when>
		    		<c:otherwise>
		    			<option value="${depot.id}">${depot.name}</option>
		    		</c:otherwise>
		    	</c:choose>
		    	</c:forEach>
			</form:select>
	    </div> --%>
					</div>

					<div class="text-right col-lg-9">
						<a href="<%= request.getContextPath()%>/addcustomer"
							class="btn btn-info" role="button"
							style="width: 300px; font-size: 20px"> <i
							class="fa fa-refresh" style='font-size: 20px; padding: 0 1em 0 0'></i>Refresh
						</a>
						<!-- 	<button type="submit" class="btn btn-primary">Save</button> -->
						<button type="submit" class="btn btn-success"
							style="width: 300px; font-size: 20px">
							<i class="fa fa-plus" style='font-size: 20px; padding: 0 1em 0 0'></i>Add
							Customer
						</button>



					</div>
				</form:form>
			</div>
		</div>
		<div class="card-footer">All right reserved</div>
	</div>
</body>
<script type="text/javascript">

$(function () {
	
	$('#dob').datetimepicker({
		format: 'DD/MM/YYYY hh:mm A',
		icons: {
            time: "fa fa-clock-o",
            date: "fa fa-calendar",
            up: "fa fa-arrow-up",
            down: "fa fa-arrow-down",
            autoclose: true
        }
	
    });
	
});
$(document).ready(function(){
	var editMode = $('#editMode').val();
	console.log("customer edit mode", editMode);
	if(editMode){
		$('#name').autocomplete({
			serviceUrl: '${pageContext.request.contextPath}/autocomplete/customers',
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
	       		$('#custid').val(item.data);
		   		document.addcustomerform.action="${pageContext.request.contextPath}/editcustomer";
		   		document.addcustomerform.method="POST";
		   		document.addcustomerform.submit();
	       	}
			
		});
	}
})
</script>
</html>
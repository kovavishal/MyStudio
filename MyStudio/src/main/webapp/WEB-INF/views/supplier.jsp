<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="staticcontent.jsp"%>
<title>Supplier</title>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="container-fluid" id="main">
		<div class="card">
			<div class="card-header text-center"
				style="padding-bottom: 0px; padding-top: 0px; background: skyblue; color: #30567D; font-weight: 625;">
				<jsp:include page="logout.jsp">
					<jsp:param name="page" value="Supplier" />
				</jsp:include>
			</div>
			<div class="card-body bg-light border-success mb-3 col-lg-12"
				style="color: navy;">
				<form:form method="POST" action="supplier" modelAttribute="supplier"
					name="supplierform">
					<form:hidden path="editMode" id="editmode" />
					<div class="form-row">
						<div class="form-group col-md-2 col-lg-1">

							<c:choose>
								<c:when test="${supplier.editMode}">
									<label for="supplierno">Supplier#</label>
									<form:input path="supId" class="form-control" id="supplierno" />
								</c:when>
								<c:otherwise>
									<label for="supplierno">Supplier#</label>
									<form:input path="supId" class="form-control" readonly="true"
										id="supplierno" />

								</c:otherwise>

							</c:choose>




							<%-- 
	      <label for="supplierno" >Supplier#</label>
	      <form:input path="supId" class="form-control" readonly="true" id="supplierno"/> --%>
						</div>
						<div class="form-group col-md-2 col-lg-4">
							<label for="suppliername">Supplier Name</label>
							<form:input path="name" class="form-control" required="true"
								id="suppliername" />
						</div>
						<div class="form-group col-md-6 col-lg-2">


							<label for="creditbalance">Credit Balance</label>
							<form:input path="creditBalance" class="form-control"
								id="creidtbalance" />



							<%-- 
	      <label for="creditbalance">Credit Balance</label>
	      <form:input path="creditBalance" class="form-control" id="creidtbalance"/> --%>
						</div>
						<div class="form-group col-md-6 col-lg-2">
							<label for="gstno">GST No</label>
							<form:input path="gstNo" class="form-control" id="gstno" />
						</div>
					</div>

					<div class="form-row">
						<div class="form-group col-md-4 col-lg-3">
							<label for="address1">Address - 1 </label>
							<form:input path="address1" class="form-control" id="address1"
								required="true" />
						</div>
						<div class="form-group col-md-4 col-lg-3">
							<label for="address2">Address - 2</label>
							<form:input path="address2" class="form-control" id="address2" />
						</div>
						<div class="form-group col-md-4 col-lg-3">
							<label for="state">State</label>
							<form:select path="stateId" class="form-control" id="stateId"
								required="true">
								<option value="">--Select State--</option>
								<c:forEach items="${states}" var="state">
									<c:choose>
										<c:when test="${state.id eq  supplier.stateId}">
											<option value="${state.id}" selected="selected">${state.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${state.id}">${state.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
						</div>
					</div>

					<div class="form-row">
						<div class="form-group col-md-6 col-lg-2">
							<label for="pincode">Pincode</label>
							<form:input path="pin" class="form-control" id="pincode"
								required="true" />
						</div>
						<div class="form-group col-md-4 col-lg-2">
							<label for="phone">Phone No.</label>
							<form:input path="phoneNo" class="form-control" id="phone"
								maxlength="10" />
						</div>
						<div class="form-group col-md-4 col-lg-2">
							<label for="mobile">Mobile No.</label>
							<form:input path="mobileNo" class="form-control" id="mobile"
								required="true" maxlength="10" />
						</div>

						<div class="form-group col-md-6 col-lg-3">
							<label for="email">E- Mail</label>
							<form:input path="eamil" class="form-control" id="email"
								type="email" />
						</div>
					</div>

					<div class="form-row col-lg-12 text-right">
						<div class="form-group col-md-6 col-lg-9 text-right">
							<a href="<%= request.getContextPath()%>/supplier"
								class="btn btn-info" role="button"
								style="width: 300px; font-size: 20px"><i
								class="fa fa-refresh"
								style='font-size: 20px; padding: 0 1em 0 0'></i>Refresh</a>
							<c:choose>
								<c:when test="${supplier.editMode}">
									<!-- <button type="button" class="btn btn-success" onclick="editSave();">Save</button> -->
									<button type="submit" class="btn btn-success"
										onclick="editSave();" style="width: 300px; font-size: 20px">
										<i class="fa fa-save"
											style='font-size: 20px; padding: 0 1em 0 0'></i>Save
									</button>
								</c:when>
								<c:otherwise>
									<!-- 	<button type="submit" class="btn btn-success">Save</button> -->
									<button type="submit" class="btn btn-success"
										style="width: 300px; font-size: 20px">
										<i class="fa fa-save"
											style='font-size: 20px; padding: 0 1em 0 0'></i>Save
									</button>

								</c:otherwise>

							</c:choose>
						</div>
					</div>
				</form:form>
			</div>
			<div class="card-footer">All right reserved</div>
		</div>
	</div>
</body>
<script type="text/javascript">

function editSave(){
	document.supplierform.action="${pageContext.request.contextPath}/supplier";
	document.supplierform.method="POST";
	document.supplierform.submit();
}

$(document).ready(function() {
var editMode = $('#editmode').val();
console.log("Edit mode enabled : " , editMode);
if(editMode){
	alert("YES  editmode");
}
	$('#suppliername').autocomplete({
		serviceUrl: '${pageContext.request.contextPath}/autocomplete/supplier',
		paramName: "searchTerm",
		delimiter: ",",
		minChars:1,
		document.title = 'Report on '+  $suppliername +'from :'  + $('#fromdate').val() +' To :'+ $('#todate').val(); 
	   	transformResult: function(response) {
			return {      	
			  suggestions: $.map($.parseJSON(response), function(item) {
			      return { value: item.name, data: item.id };
			   })
			 };
	   	},
	   	onSelect:function(item){
	   		console.log("supplier id :: " + item.data);
	   		$('#supplierno').val(item.data);
	   		document.supplierform.action="${pageContext.request.contextPath}/supplier/edit";
	   		document.supplierform.method="POST";
	   		document.supplierform.submit();
	   	}
		
	});
}
});
</script>
</html>
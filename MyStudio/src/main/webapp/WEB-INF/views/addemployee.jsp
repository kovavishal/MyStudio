<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="staticcontent.jsp"%>
<title>Employee</title>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="container-fluid" id="main">
		<div class="card">
			<div class="card-header text-center"
				style="padding-bottom: 0px; padding-top: 0px; background: skyblue; color: #30567D; font-weight: 625;">
				<jsp:include page="logout.jsp">
					<jsp:param name="page" value="Employee" />
				</jsp:include>
			</div>
			<div class="card-body bg-light border-success mb-3 col-lg-12"
				style="color: navy;">
				<c:if test="${message ne null and message ne ''}">
					<div class="alert alert-danger" align="center">${message}</div>
				</c:if>
				<form:form action="addemployee" method="POST"
					modelAttribute="employee" name="employeeform">
					<form:hidden path="editMode" id="editMode" />
					<div class="form-row">
						<div class="form-group col-md-2 col-lg-3">
							<label for="emplyeeno">Employee No</label>
							<form:input path="empId" class="form-control" id="employeno"
								readonly="true" />
						</div>
						<div class="form-group col-md-2 col-lg-3">
							<label for="employeename">Employee Name</label>
							<form:input path="name" class="form-control" id="employeename"
								required="true" />
						</div>
						<div class="form-group col-md-2 col-lg-3">
							<label for="dob">Date of Birth</label>
							<form:input path="strDob" class="form-control" id="dob" required="true" />
						</div>
					</div>

					<div class="form-row">
						<div class="form-group col-md-2 col-lg-3">
							<label for="department">Department</label>
							<form:select path="depotId" class="form-control" id="department"
								onclick="showCredential()" onblur="showCredential()"
								required="true">
								<option value="">--Department--</option>
								<c:forEach items="${deportments}" var="depo">
									<c:choose>
										<c:when test="${depo.id eq  employee.depotId}">`
	    			<option value="${depo.id}" selected="selected">${depo.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${depo.id}">${depo.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
						</div>
						<div class="form-group col-md-4 col-lg-3">
							<label for="advance">Advance</label>
							<form:input path="advance" class="form-control" id="advance" />
						</div>
						<div class="form-group col-md-4 col-lg-3">
							<label for="salary">Salary </label>
							<form:input path="salary" class="form-control" id="salary" />
						</div>
					</div>

					<div class="form-row">
						<div class="form-group col-md-4 col-lg-3">
							<label for="address1">Address 1</label>
							<form:input path="address1" class="form-control" id="address1"
								required="true" />
						</div>
						<div class="form-group col-md-4 col-lg-3">
							<label for="address2">Address 2</label>
							<form:input path="address2" class="form-control" id="address2" required="true" />
						</div>
						<div class="form-group col-md-4 col-lg-3">
							<label for="city">City / Town</label>
							<form:input path="city" class="form-control" id="city"  required="true"/>
						</div>
					</div>
					<div class="form-row">
						<div class="form-group col-md-4 col-lg-3">
							<label for="mobile">Mobile No.</label>
							<form:input path="mobileNo" class="form-control" id="mobile"
								required="true" />
						</div>

						<div class="form-group col-md-6 col-lg-3">
							<label for="email">E- Mail</label>
							<form:input path="eamil" class="form-control" id="email"  />
						</div>

						<div class="form-group col-md-6 col-lg-3">
							<label for="incentive">Incentive</label>
							<form:input path="incentive" class="form-control" id="incentive" />
						</div>
					</div>

					<div class="form-row" id="credential">
						<div class="form-group col-md-4 col-lg-3">
							<label for="userid">User ID</label>
							<form:input path="username" class="form-control" id="userid"
								required="true" />
						</div>

						<div class="form-group col-md-6 col-lg-3">
							<label for="pwd">Password</label>
							<form:input path="password" class="form-control" id="pwd"
								required="true" />
						</div>

						<div class="form-group col-md-2 col-lg-3">
							<label for="department">Role</label>
							<form:select path="roleId" class="form-control" id="role"
								required="true">
								<option value="">--Select Role--</option>
								<c:forEach items="${roles}" var="role">
									<c:choose>
										<c:when test="${role.id eq  employee.roleId}">`
	    			<option value="${role.id}" selected="selected">${role.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${role.id}">${role.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
						</div>
					</div>

					<div class="form-row text-right">
						<div class="form-group col-md-6 col-lg-9">
							<a href="<%= request.getContextPath()%>/employee"
								class="btn btn-info" style="width: 300px; font-size: 20px"
								role="button"> <i class="fa fa-refresh"
								style='font-size: 20px; padding: 0 1em 0 0'></i>Refresh
							</a>
							<!-- <button type="submit" class="btn btn-primary">Save</button> -->

							<button type="submit" class="btn btn-success"
								style="width: 300px; font-size: 20px">
								<i class="fa fa-plus"
									style='font-size: 20px; padding: 0 1em 0 0'></i>Add Employee
							</button>

						</div>
					</div>
				</form:form>
			</div>
			<div class="card-footer">All right reserved</div>
		</div>
	</div>
</body>
<script type="text/javascript">
$(function () {
	$('#dob').datetimepicker({
		format: 'DD/MM/YYYY',
		icons: {
            time: "fa fa-clock-o",
            date: "fa fa-calendar",
            up: "fa fa-arrow-up",
            down: "fa fa-arrow-down",
            autoclose: true
        }
	
    });
});

function resetEmployee(){
	document.employeeform.action="employee";
	document.employeeform.method="GET";
	document.employeeform.submit();
}
function showCredential(){
	var department= $("#department option:selected").text();
			
	if(department == 'Agent'){
		$("#credential").css("display", "none");
		$("#userid").attr("required", false);
		$("#pwd").attr("required", false);
		$("#role").attr("required", false);
		
	}else{
		$("#credential").css("display", "flex");
		$("#userid").attr("required", true);
		$("#pwd").attr("required", true);
		$("#role").attr("required", true);
	}
		
}
$(document).ready(function(){
	var editMode = $('#editMode').val();
	console.log("employee edit mode", editMode);
	if(editMode =='true'){
		$('#employeename').autocomplete({
			serviceUrl: '${pageContext.request.contextPath}/autocomplete/employee',
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
	       		$('#employeno').val(item.data);
		   		document.employeeform.action="${pageContext.request.contextPath}/editemployee";
		   		document.employeeform.method="POST";
		   		document.employeeform.submit();
	       	}
			
		});
	}
});
</script>
</html>
<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="../staticcontent.jsp"%>
<title>Meta Data</title>
</head>
<body>
	<%@include file="../navbar.jsp"%>
	<div class="container-fluid" id="main">
		<form:form method="POST" action="add" modelAttribute="metadata"
			name="metaform">
			<div class="card">
				<div class="card-header text-center"
					style="padding-bottom: 0px; padding-top: 0px; background: skyblue; color: #30567D; font-weight: 625;">
					<jsp:include page="../logout.jsp">
						<jsp:param name="page" value="Add Meta Data" />
					</jsp:include>
				</div>

				<div class="card-body" style="padding-left: 50px;">
					<c:if test="${message ne null and message ne ''}">
						<div class="alert alert-danger col-lg-8" align="center">
							${message}</div>
					</c:if>
					<div class="form-row">
						<div class="form-group col-md-6 col-lg-3">
							<form:select path="methodName" class="form-control match-content"
								required="true" onchange="getData(this.value);" id="metamapping">
								<option value="">--Data Type--</option>
								<c:forEach items="${mappings}" var="mapping">
									<c:choose>
										<c:when test="${mapping.methodName eq  metadata.methodName}">`
	    			<option value="${mapping.methodName}" selected="selected">${mapping.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${mapping.methodName}">${mapping.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
						</div>
					</div>
					<c:if test="${showtable}">
						<div class="form-row">
							<div class="form-group col-md-6 col-lg-3">
								<form:input path="name" class="form-control" placeholder="Name"
									id="metaname" required="true" />
							</div>
							<div class="form-group col-md-6 col-lg-3">
								<button type="button" class="btn btn-primary"
									onclick="addData();">Add</button>
							</div>
						</div>

						<div class="table-responsive col-lg-8" style="padding-left: 0px;">
							<table id="ratetable" class="table table-bordered table-striped"
								style="display: block;">
								<thead>
									<tr>
										<th class="col-sm-1" data-field="id">Id</th>
										<th class="col-sm-3" data-field="name">Name</th>
										<th class="col-sm-1" data-field="checkDelete">Action</th>
									</tr>
								</thead>
							</table>
						</div>
					</c:if>
				</div>
			</div>
		</form:form>
	</div>
</body>
<script type="text/javascript">
var tableClient;
$(document).ready(function(){
	var showtable= '${showtable}';
	var name = $('#metamapping').val();
	console.log("name",name);
	if(showtable){
	tableClient = $('#ratetable').DataTable({
		"autoWidth": false,
		"columnDefs": [
			{"targets": [ 0 ],
		     "visible": true,
		     "searchable": false}
		],
		"ajax": {
			"url": "${pageContext.request.contextPath}/metadata/mapping/"+name,
			"type": "POST",
			"success" :  function(data){
				$.each(data, function(index, obj){
					
					tableClient.row.add([
						
						obj.id,
						obj.name,
						"<input type='button' value='Update' id='id_"+obj.id+"' onclick='updateName("+obj.id+");'>",
					]).draw();
					
				});
			}
		},
	});
	}
	
});

function updateName(id){
	var name = $('#metamapping').val();
	var metaname=$('#metaname').val();
	console.log("metaname",metaname);
	if(metaname != ''){
		$('#metaname').val('');
		$.ajax({
			type: "PUT",
			url: "${pageContext.request.contextPath}/metadata/update/"+name+"/"+id+"/"+metaname,
			timeout : 100000,
			success: function(data){
				tableClient.clear().draw();
				tableClient.ajax.reload();
			},
			error: function(e){
				alert("ERROR: ", e);
			}
		});
	}else{
		alert("Enter name to update");
	}
}
function getData(){
	document.metaform.submit();
}
function addData(){
	var name = $('#metamapping').val();
	if(name!=''){
		document.metaform.action="adddata";
		document.metaform.method="POST";
		document.metaform.submit();
	}
}

</script>
</html>
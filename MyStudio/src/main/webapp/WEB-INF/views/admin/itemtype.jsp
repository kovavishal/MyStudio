<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="../staticcontent.jsp"%>
<title>Add Product Item Type</title>
</head>
<body>
	<%@include file="../navbar.jsp"%>
	<div class="container-fluid" id="main">
		<form:form method="POST" action="additemtype"
			modelAttribute="productItemType">
			<div class="card">
				<div class="card-header text-center"
					style="padding-bottom: 0px; padding-top: 0px; background: skyblue; color: #30567D; font-weight: 625;">
					<jsp:include page="../logout.jsp">
						<jsp:param name="page" value="Add Product Item" />
					</jsp:include>
				</div>

				<div class="card-body" style="padding-left: 50px;">
					<c:if test="${message ne null and message ne ''}">
						<div class="alert alert-danger col-lg-8" align="center">
							${message}</div>
					</c:if>
					<form>
						<div class="form-row">
							<div>
								<form:select path="productId" class="form-control match-content"
									id="productitem" tabindex="${tabIndex}" required="true">
									<option value="">--Select Product--</option>
									<c:forEach items="${products}" var="product">
										<c:choose>
											<c:when test="${product.id eq  productItemType.productId}">`
			    			<option value="${product.id}" selected="selected">${product.name}</option>
											</c:when>
											<c:otherwise>
												<option value="${product.id}">${product.name}</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</form:select>
							</div>
							<div class="form-group col-md-6 col-lg-3">
								<form:input path="name" class="form-control"
									placeholder="Item Name" id="metaname" required="true" />
							</div>
							<div class="form-group col-md-6 col-lg-3">
								<button type="submit" class="btn btn-primary">Add Item</button>
							</div>
						</div>
						<div class="table-responsive col-lg-8" style="padding-left: 0px;">
							<table id="producttable"
								class="table table-bordered table-striped"
								style="display: block;">
								<thead>
									<tr>
										<th class="col-sm-1" data-field="id">Id</th>
										<th class="col-sm-3" data-field="name">Product Name</th>
										<th class="col-sm-3" data-field="name">Item Name</th>
										<th class="col-sm-1" data-field="checkDelete">Action</th>
									</tr>
								</thead>
							</table>
						</div>
					</form>
				</div>
			</div>
		</form:form>
	</div>
</body>
<script type="text/javascript">
var tableClient;
$(document).ready(function(){
	
	tableClient = $('#producttable').DataTable({
		"autoWidth": false,
		"columnDefs": [
			{"targets": [ 0 ],
		     "visible": true,
		     "searchable": false}
		],
		"ajax": {
			"url": "${pageContext.request.contextPath}/metadata/itemtypes",
			"type": "POST",
			"success" :  function(data){
				$.each(data, function(index, obj){
					
					tableClient.row.add([
						
						obj.id,
						obj.productName,
						obj.name,
						"<input type='button' value='Update' id='id_"+obj.id+"' onclick='deleteProductItem("+obj.id+");'>",
					]).draw();
					
				});
			}
		},
	});
	
});

function deleteProductItem(itemTypeId){
	var metaname = $('#metaname').val();
	var prodid = $('#productitem').val();
	if(metaname!= '' && prodid != ''){
	$.ajax({
		type: "PUT",
		url: "${pageContext.request.contextPath}/metadata/deleteitem/" + itemTypeId+"/"+prodid+"/"+metaname,
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


</script>
</html>
<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="../staticcontent.jsp"%>
<title>Add Product Item Size Rate</title>
</head>
<body>
	<%@include file="../navbar.jsp"%>
	<div class="container-fluid" id="main">
		<form:form method="POST" action="additemsizerate"
			modelAttribute="itemSizeRate">
			<div class="card">
				<div class="card-header text-center"
					style="padding-bottom: 0px; padding-top: 0px; background: skyblue; color: #30567D; font-weight: 625;">
					<jsp:include page="../logout.jsp">
						<jsp:param name="page" value="Add Item Size Rate" />
					</jsp:include>
				</div>

				<div class="card-body" style="padding-left: 50px;">

					<c:if test="${message ne null and message ne ''}">
						<div class="alert alert-danger col-lg-8" align="center">
							${message}</div>
					</c:if>

					<div class="form-row">

						<div class="form-group col-md-6 col-lg-2">
							<form:select path="productId" class="form-control match-content"
								onchange="setProductItms(this.value);setProductSize(this.value)"
								id="products" required="true">
								<option value="">--Select Product--</option>
								<c:forEach items="${productTypes}" var="product">
									<c:choose>
										<c:when test="${product.id eq  itemSizeRate.productId}">`
	    			<option value="${product.id}" selected="selected">${product.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${product.id}">${product.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
						</div>
						<div class="form-group col-md-6 col-lg-2">
							<form:select path="itemId" class="form-control match-content"
								id="prodItem" required="true">
								<option value="">--Item--</option>
								<c:forEach items="${productItemTypes}" var="productItem">
									<c:choose>
										<c:when test="${productItem.id eq  itemSizeRate.itemId}">`
	    			<option value="${productItem.id}" selected="selected">${productItem.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${productItem.id}">${productItem.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
						</div>
						<div class="form-group col-md-6 col-lg-2">
							<form:select path="sizeId" class="form-control match-content"
								id="prodSize" required="true">
								<option value="">--Size--</option>
								<c:forEach items="${productSizes}" var="productSize">
									<c:choose>
										<c:when test="${productSize.id eq  itemSizeRate.sizeId}">`
	    			<option value="${productSize.id}" selected="selected">${productSize.size}</option>
										</c:when>
										<c:otherwise>
											<option value="${productSize.id}">${productSize.size}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
						</div>
						<div class="form-group col-md-6 col-lg-2">
							<form:select path="rateId" class="form-control match-content"
								required="true">
								<option value="">--Rate Type--</option>
								<c:forEach items="${rateTypes}" var="rateType">
									<c:choose>
										<c:when test="${rateType.id eq  itemSizeRate.rateId}">`
	    			<option value="${rateType.id}" selected="selected">${rateType.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${rateType.id}">${rateType.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
						</div>
						<div class="form-group col-md-6 col-lg-1">
							<form:input path="amount" class="form-control"
								placeholder="Amount" required="true" />
						</div>
						<div class="form-group col-md-6 col-lg-3">
							<button type="submit" class="btn btn-primary">Add Item
								Size Rate</button>
						</div>
					</div>
					<div class="table-responsive col-lg-10" style="padding-left: 0px;">
						<table id="producttable"
							class="table table-bordered table-striped"
							style="display: block;">
							<thead>
								<tr>
									<th class="col-sm-1" data-field="id">Id</th>
									<th class="col-sm-3" data-field="itemname">Item Name</th>
									<th class="col-sm-3" data-field="sizename">Size Name</th>
									<th class="col-sm-3" data-field="ratename">Rate Name</th>
									<th class="col-sm-3" data-field="name">Amount</th>
									<th class="col-sm-1" data-field="checkDelete">Action</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
		</form:form>
	</div>
</body>
<script type="text/javascript">
var tableClient;
$(document).ready(function(){
	var productid = document.getElementById("products").value;
	setProductSize(productid);
	setProductItms(productid);
	
	tableClient = $('#producttable').DataTable({
		"autoWidth": false,
		"columnDefs": [
			{"targets": [ 0 ],
		     "visible": true,
		     "searchable": false}
		],
		"ajax": {
			"url": "${pageContext.request.contextPath}/metadata/itemsizerates",
			"type": "POST",
			"success" :  function(data){
				$.each(data, function(index, obj){
					
					tableClient.row.add([
						
						obj.id,
						obj.itemName,
						obj.sizeName,
						obj.rateName,
						obj.amount,
						"<input type='button' value='Delete' id='id_"+obj.id+"' onclick='deleteSizeRate("+obj.id+");'>",
					]).draw();
					
				});
			}
		},
	});
	
});

function deleteSizeRate(sizeRateId){
	$.ajax({
		type: "DELETE",
		url: "${pageContext.request.contextPath}/metadata/deletesizerate/" + sizeRateId,
		timeout : 100000,
		success: function(data){
			tableClient.clear().draw();
			tableClient.ajax.reload();
		},
		error: function(e){
			alert("ERROR: ", e);
		}
	});
}

function setProductSize(productid){

	var select = document.getElementById("prodSize");
	$.getJSON('${pageContext.request.contextPath}/productsize/'+productid , function (data) {
		select.options.length = 0;
		$.each(data, function (key, entry) {
			select.options[select.options.length] = new Option(entry.size, entry.id);
		})
	});
}
function setProductItms(productid){
	$.getJSON('${pageContext.request.contextPath}/productitem/'+productid , function (data) {
			var select = document.getElementById("prodItem");
			select.options.length = 0;
			$.each(data, function (key, entry) {
				select.options[select.options.length] = new Option(entry.name, entry.id);
			})
	});
}
</script>
</html>
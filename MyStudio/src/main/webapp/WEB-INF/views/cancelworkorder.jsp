<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="staticcontent.jsp"%>
<title>Work Order Cancellation</title>
<style type="text/css">
table th {
	position: sticky;
	top: 0;
	background: #aac5df;
}
</style>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="container-fluid" id="main">
		<div class="card">
			<div class="card-header"
				style="padding-bottom: 0px; padding-top: 0px; background: skyblue; color: #30567D; font-weight: 625;">
				<jsp:include page="logout.jsp">
					<jsp:param name="page" value="Work Order Cancellation" />
				</jsp:include>
			</div>
			<c:if test="${message ne null and message ne ''}">
				<div class="alert alert-danger text-center"
					style="padding-bottom: 0px; padding-top: 0px;">${message}</div>
			</c:if>
			<div class="card-body bg-light border-success mb-3 col-lg-12"
				style="color: navy; padding-top: 0px; padding-bottom: 0px;">
				<form:form method="POST" action="workorder" modelAttribute="order"
					name="workOrderForm">
					<form:hidden path="custId" />
					<form:hidden path="gstNo" />
					<form:hidden path="creditBalance" />
					<form:hidden path="rateType" />
					<form:hidden path="remarks" />

					<div class="form-row">

						<div class="form-group col-md-2 col-lg-2">
							<label for="orderno">Order No</label>

							<form:input path="orderId" class="form-control" id="orderno"
								tabindex="1" onfocus="disableBtn();"
								onblur="workOrderCancel(this.value);" />


						</div>

						<div class="form-group col-md-2 col-lg-3">
							<label for="orderdate">Order Date</label>
							<form:input path="strOrderDate" class="form-control"
								id="orderdate" tabindex="2" disabled="true" required="true"
								readonly="true" />
						</div>

						<div class="form-group col-md-2 col-lg-2">
							<label for="ordertype">Order Type</label>
							<form:select path="orderType" class="form-control" id="ordertype"
								tabindex="3" disabled="true" readonly="true">
								<c:forEach items="${ordertypes}" var="orderType">
									<c:choose>
										<c:when test="${orderType.id eq  order.orderType}">
											<option value="${orderType.id}" selected="selected">${orderType.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${orderType.id}">${orderType.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
						</div>

						<div class="form-group col-md-2 col-lg-3">
							<label for="agentname">Agent Name</label>
							<form:input path="agentName" class="form-control" id="agentName"
								disabled="true" tabindex="4" readonly="true" />
						</div>

						<div class="form-group col-md-2 col-lg-2">
							<label for="customertype">Customer Type</label>
							<form:select path="custType" class="form-control"
								id="customerType" tabindex="5" readonly="true">
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

					</div>

					<div class="form-row">

						<div class="form-group col-md-6 col-lg-3">
							<label for="customername">Customer Name</label>
							<c:if test="${!order.editMode}">
       	&nbsp; <a
									href="<%=request.getContextPath()%>/addcustomer?addnew=true">Add
									New</a>
							</c:if>
							<form:input path="custName" class="form-control text-uppercase"
								id="customername" tabindex="6" disabled="true" required="true"
								readonly="true" />
						</div>

						<div class="form-group col-md-4 col-lg-3">
							<label for="customeraddress1">Customer Address 1</label>
							<form:input path="custAddr1" class="form-control"
								id="customeraddress1" tabindex="7" required="true"
								readonly="true" />
						</div>
						<div class="form-group col-md-4 col-lg-3">
							<label for="customeraddress2">Customer Address 2</label>
							<form:input path="custAddr2" class="form-control"
								id="customeraddress2" tabindex="8" required="true"
								readonly="true" />
						</div>

						<div class="form-group col-md-6 col-lg-3">
							<label for="wrappername">Wrapper Name</label>
							<form:input path="wrapperName" class="form-control"
								id="wrappername" tabindex="9" readonly="true" />
						</div>

					</div>


					<div class="form-row">

						<div class="form-group col-md-2 col-lg-3">
							<c:if test="${order.editMode}">
								<form:hidden path="products[0].productId" />
							</c:if>
							<label for="product">Products</label>
							<form:select path="products[0].productTypeId"
								class="form-control" id="product"
								onblur="changeProductItems(this.value,${order.cancelMode})"
								tabindex="10" readonly="true">
								<c:forEach items="${products}" var="product">
									<c:choose>
										<c:when
											test="${product.id eq  order.products[0].productTypeId}">
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
							<label for="noofcopies">No Of Copies</label>
							<form:input path="products[0].noOfCopy" class="form-control"
								id="noofcopies" tabindex="11" disabled="true" value="1"
								onblur="noOfCopy(this);" readonly="true" />
						</div>

						<div class="form-group col-md-6 col-lg-2">
							<label for="size">Size</label>
							<form:select path="products[0].size" class="form-control"
								id="size" tabindex="12" disabled="true" readonly="true">
								<c:forEach items="${productSizes}" var="productSize">
									<c:choose>
										<c:when test="${productSize.id eq  order.products[0].size}">`
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
							<label for="noofsheet">No of Sheets</label>
							<form:input path="products[0].noOfSheet" class="form-control"
								id="noofsheet" tabindex="13" disabled="true" required="true"
								readonly="true" />
						</div>

						<div class="form-group col-md-6 col-lg-3">
							<label for="duedate">Due Date</label>
							<form:input path="strDueDate" class="form-control" id="duedate"
								tabindex="14" disabled="true" required="true" readonly="true" />
						</div>

					</div>


					<div class="row ">
						<div class="col-sm-8 col-lg-12">
							<div class="card bg-light  mb-3" style="color: navy;">
								<div id="table" class="table-editable"
									style="height: 345px; overflow: scroll;">
									<table
										class="table table-bordered table-responsive-md table-striped text-center order-list"
										id="productitemtable">
										<thead>
											<tr>
												<th class="text-center" nowrap="nowrap">Item Type</th>
												<th class="text-center">Quantity</th>
												<th class="text-center">Rate</th>
												<th class="text-center">Amount</th>
												<th class="text-center">Remarks</th>
												<th class="text-center" nowrap="nowrap"><c:choose>
														<c:when test="${order.editMode}">
															<button type="button"
																class="btn btn-success btn-rounded btn-sm my-0"
																disabled="true" onclick="editModeAddRow()">Add
																Row</button>
														</c:when>
														<c:otherwise>
															<button type="button"
																class="btn btn-success btn-rounded btn-sm my-0"
																disabled="true" onclick="addRow()">Add Row</button>
														</c:otherwise>
													</c:choose></th>
											</tr>
										</thead>
										<tbody>
											<c:set var="tabIndex">15</c:set>
											<c:forEach items="${productitems}" var="productItem"
												varStatus="loop">
												<tr class="form-group">

													<td style="width: 200px;"><c:if
															test="${order.editMode}">
															<form:hidden
																path="products[0].productItems[${loop.index}].prodItemId" />
														</c:if> <form:select
															path="products[0].productItems[${loop.index}].prodItemTypeId"
															class="form-control match-content"
															id="productitem_${loop.index}" tabindex="${tabIndex}"
															readonly="true">
															<c:forEach items="${productItemTypes}"
																var="productItemType">
																<c:choose>
																	<c:when
																		test="${productItem.prodItemTypeId eq productItemType.id}">
																		<option value="${productItemType.id}"
																			selected="selected">${productItemType.name}</option>
																	</c:when>
																	<c:otherwise>
																		<option value="${productItemType.id}">${productItemType.name}</option>
																	</c:otherwise>
																</c:choose>
															</c:forEach>
														</form:select></td>
													<c:set var="index" value="${tabIndex+1}" />
													<td><form:input
															path="products[0].productItems[${loop.index}].quantity"
															class="form-control" id="qty_${loop.index}"
															placeholder="Qty"
															onblur="setAmount(this.value,${loop.index})"
															tabindex="${tabIndex}" readonly="true" /></td>
													<td><form:input
															path="products[0].productItems[${loop.index}].rate"
															class="form-control" id="rate_${loop.index}"
															placeholder="Rate" readonly="true" /></td>
													<td><form:input
															path="products[0].productItems[${loop.index}].amount"
															class="form-control" id="amount_${loop.index}"
															placeholder="Amount" readonly="true" /></td>
													<c:set var="index" value="${tabIndex+1}" />
													<td><form:input
															path="products[0].productItems[${loop.index}].remarks"
															class="form-control" id="remark_${loop.index}"
															placeholder="Remarks" tabindex="${tabIndex}"
															readonly="true" /></td>
													<td nowrap="nowrap"><span class="table-remove"><button
																type="button"
																class="btn btn-danger btn-rounded btn-sm my-0 ibtnDel"
																disabled="true">Remove</button></span></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
							<div>

								<div align="center">

									<!-- 	 <button type="submit" class="btn btn-info" style="width: 100px;" onclick="cancelOrder();">Cancel</button> -->
									<button type="submit" class="btn btn-danger" id="cancelBtn"
										style="width: 300px; font-size: 20px;"
										onclick="cancelOrder();">
										<i class="fa fa-close"
											style='font-size: 20px; padding: 0 1em 0 0'></i>Cancel
										Order
									</button>


								</div>

								<%--  <div align="right">
  		<button type="button" class="btn btn-primary" onclick="saveCustomer()">Save and Continue</button>
	  	<c:choose>
	  		<c:when test="${order.editMode}">
	  			<button type="submit" class="btn btn-success" style="width: 100px;" onclick="editSave();">Save</button>
	  			<button type="submit" class="btn btn-info" style="width: 100px;" onclick="cancelOrder();">Cancel</button>
	  		</c:when>
	  		<c:otherwise>
				<button type="submit" class="btn btn-success" style="width: 100px;">Save</button>
			</c:otherwise>
		</c:choose>
	
	</div>  --%>
							</div>
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
	
	$('#orderdate').datetimepicker({
		format: 'DD/MM/YYYY hh:mm A',
		icons: {
            time: "fa fa-clock-o",
            date: "fa fa-calendar",
            up: "fa fa-arrow-up",
            down: "fa fa-arrow-down",
            autoclose: true
        }
	
    });
	
	$('#duedate').datetimepicker({
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
	 var counter = 6;
	$('#agentName').autocomplete({
		serviceUrl: '${pageContext.request.contextPath}/autocomplete/agents',
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
    	onSelect:function(item){
       		document.getElementById("agentName").focus();
       	}
	}
	);
	
	$('#customername').autocomplete({
		serviceUrl: '${pageContext.request.contextPath}/autocomplete/customers',
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
       	onSelect:function(item){
       		console.log("item :: " + item.data);
       		setCustAddr(item.data);
       		document.getElementById("customername").focus();
       	}
		
	});
	
	$("table.order-list").on("click", ".ibtnDel", function (event) {
	    $(this).closest("tr").remove();       
    });
 	});

function disableBtn(){
	document.getElementById("cancelBtn").disabled=true;
}

function setAmount(qty,index){
	
	var sizeType = document.getElementById("size").value;
	var itemtype = document.getElementById("productitem_"+index).value;
	var custType = document.getElementById("customerType").value;
	var noOfCopy = document.getElementById("noofcopies").value;
	var noofsheet = document.getElementById("noofsheet").value
	var product = document.getElementById("product").value;
	var custype= $("#customerType option:selected").text();

	if(custype == 'Ameture'){
		document.getElementById("rateType").value=103;
	}
	
	var rateType = document.getElementById("rateType").value
	
	if(product!='undefined' && product ==102 && noofsheet!='undefined' && (noofsheet ==0|| noofsheet=='')){
		alert("Please enter the no of sheet");
		document.getElementById("noofsheet").focus();
	}
	
	var rows = document.getElementById("productitemtable").getElementsByTagName("tbody")[0].getElementsByTagName("tr").length;
	var itemCount = 0;
	for(var i=0; i<rows;i++){
		if($('#qty_'+i).val())
			itemCount = parseFloat(itemCount)+parseFloat($('#qty_'+i).val());
	}
	if(itemCount > parseFloat(noofsheet)){
		alert("You are not allow to add more than max no of sheets");
		$('#qty_'+index).val('');
		return;
	}
	if(custType && qty){
		console.log("querying rate");
		$.getJSON('${pageContext.request.contextPath}/rate/'+rateType+'/'+sizeType+'/'+itemtype , function (data) {
			console.log("value to set" + data.amount);
			if(data.amount != null){
				$('#rate_'+index).val(parseFloat(data.amount).toFixed(2))
				$('#amount_'+index).val(parseFloat(data.amount * qty * noOfCopy).toFixed(2));
			}else{
				$('#rate_'+index).val(0)
				$('#amount_'+index).val(0);
			}
		});
	}
	
}

function changeProductItems(productid,editMode){
    if (document.orderid !='0'){
	console.log("Edit mode : ",editMode);
	console.log("product id to get size : " + productid)
	if(editMode){
		document.workOrderForm.action="${pageContext.request.contextPath}/editproductitems";
		document.workOrderForm.method="POST";
		document.workOrderForm.submit();
	}else{
		var select = document.getElementById("size");
		$.getJSON('${pageContext.request.contextPath}/productsize/'+productid , function (data) {
			select.options.length = 0;
			$.each(data, function (key, entry) {
				select.options[select.options.length] = new Option(entry.size, entry.id);
			})
		});
		
		var rows = document.getElementById("productitemtable").getElementsByTagName("tbody")[0].getElementsByTagName("tr").length;
		$.getJSON('${pageContext.request.contextPath}/productitem/'+productid , function (data) {
			for ( var i = 0, l = rows; i < l; i++ ) {
				var select = document.getElementById("productitem_"+i);
				select.options.length = 0;
				$.each(data, function (key, entry) {
					select.options[select.options.length] = new Option(entry.name, entry.id);
				})
			}
		});
	}
    }
}

function saveCustomer(){
	
	var entered = true;
	
	if($('#orderdate').val() == ''){
		entered=false;
		alert("Enter the order date");
	}
	if($('#customername').val()==''){
		entered=false;
		alert("Enter the customer name");
	}
	
	if(entered){
	document.workOrderForm.action="${pageContext.request.contextPath}/workorderdetail";
	document.workOrderForm.method="POST";
	document.workOrderForm.submit();
	}
}
function addRow(){
	document.workOrderForm.action="${pageContext.request.contextPath}/addrow";
	document.workOrderForm.method="POST";
	document.workOrderForm.submit();
}
function editModeAddRow(){
	document.workOrderForm.action="${pageContext.request.contextPath}/editmodeaddrow";
	document.workOrderForm.method="POST";
	document.workOrderForm.submit();
}

function editSave(){
	document.workOrderForm.action="${pageContext.request.contextPath}/workordereditsave";
	document.workOrderForm.method="POST";
	document.workOrderForm.submit();
}
function editOrder(orderid){
	document.workOrderForm.action="${pageContext.request.contextPath}/editorder/"+orderid;
	document.workOrderForm.method="POST";
	document.workOrderForm.submit();
}
function workOrderCancel(orderid){
	document.getElementById("cancelBtn").disabled=false;
	document.workOrderForm.action="${pageContext.request.contextPath}/cancelorder/"+orderid;
	document.workOrderForm.method="POST";
	document.workOrderForm.submit();
}
function cancelOrder(orderid){
	document.getElementById("cancelBtn").disabled=false;
	document.workOrderForm.action="${pageContext.request.contextPath}/workordercancel";
	document.workOrderForm.method="POST";
	document.workOrderForm.submit();
}

function setCustAddr(custId){
	$.getJSON('${pageContext.request.contextPath}/customer/address/'+custId , function (data) {
		console.log(data);
		if(data.length > 0){
			$.each(data, function (key, entry) {
				console.log("entry.addr1" + entry.addr1)
				$('#customeraddress1').val(entry.addr1);
				$('#customeraddress2').val(entry.addr1);
				$('#custId').val(entry.id);
				$('#creditBalance').val(entry.balance);
				$('#rateType').val(entry.rateType);
				$('#gstNo').val(entry.gstNo);
			})
		}else{
			$('#customeraddress1').val('');
			$('#customeraddress2').val('');
			$('#custId').val('');
			$('#rateType').val('');
			$('#creditBalance').val('');
			$('#gstNo').val('');
		}
	});
}
function noOfCopy(copyObj){
	if(copyObj.value =='' || copyObj.value == 0){
		alert("Please enter the no of copies");
		copyObj.value='';
		copyObj.focus();
	}
}
</script>
</html>
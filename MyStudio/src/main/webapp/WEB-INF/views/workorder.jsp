<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="staticcontent.jsp"%>
<title>Work Order</title>
<style type="text/css">
table th {
	position: sticky;
	top: 0;
	background: #aac5df;
}
 .select_input {
  text-transform: uppercase;
}

i.a {
	word-spacing: 30px;
}
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
					<jsp:param name="page" value="Work Order" />
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
					<form:hidden path="email" />


					<button type="button" class="btn btn-primary" data-toggle="modal"
						data-target="#mandateModel" hidden="true" id="mandateBtn"></button>
					<div class="modal fade" id="mandateModel" tabindex="-1"
						role="dialog" aria-labelledby="mandateModel" aria-hidden="true">
						<div class="modal-dialog" role="document">
							<div class="modal-content">
								<div class="modal-body"
									style="color: #30567D; font-weight: 625;">Please confirm
									to save</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-secondary"
										data-dismiss="modal">Close</button>
								</div>
							</div>
						</div>
					</div>




					<div class="form-row">

						<div class="form-group col-md-2 col-lg-2">
							<label for="orderno">Order No</label>
							<c:choose>
								<c:when test="${order.cancelMode}">
									<form:input path="orderId" class="form-control" id="orderno"
										tabindex="1" onblur="editOrder(this.value);" />
								</c:when>
								<c:otherwise>
									<form:input style="font-size: 20px;font-weight: bold;"
										path="orderId" class="form-control" id="orderno"
										readonly="true" tabindex="1" />
								</c:otherwise>
							</c:choose>
						</div>

						<div class="form-group col-md-2 col-lg-3">
							<label for="orderdate">Order Date</label>
							<form:input path="strOrderDate" class="form-control"
								id="orderdate" tabindex="2" readonly="true" />
						</div>

						<div class="form-group col-md-2 col-lg-2">
							<label for="ordertype">Order Type</label>
							<form:select path="orderType" class="form-control" id="ordertype"
								tabindex="3">
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
								tabindex="4" />
						</div>
						<div class="form-group col-md-2 col-lg-2">
							<label for="customertype">Customer Type</label>
							<form:select path="custType" class="form-control"
								id="customerType" tabindex="5">
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
									href="<%=request.getContextPath()%>/addcustomer?addnew=true"
									target="_blank">Add New</a>
							</c:if>
							<form:input path="custName" class="form-control text-uppercase"
								id="customername" tabindex="6" required="true" />
						</div>
						<div class="form-group col-md-6 col-lg-1">
							<label for="creditbalance">Cr. Balance</label>
							<form:input path="creditBalance" class="form-control"
								id="creditBalance-1" style="color:red;" disabled="true" />
						</div>
						<div class="form-group col-md-4 col-lg-3">
							<label for="customeraddress1">Customer Address 1</label>
							<form:input path="custAddr1" class="form-control"
								id="customeraddress1" tabindex="7" required="true" />
						</div>
						<div class="form-group col-md-4 col-lg-3">
							<label for="customeraddress2">Customer Address 2</label>
							<form:input path="custAddr2" class="form-control"
								id="customeraddress2" tabindex="8" required="true" />
						</div>

						<div class="form-group col-md-6 col-lg-2">
							<label for="wrappername">Wrapper Name</label>
							<form:input path="wrapperName" class="form-control"
								id="wrappername" tabindex="9" />
						</div>

					</div>


					<div class="form-row">

						<div class="form-group col-md-2 col-lg-3">
							<c:if test="${order.editMode}">
								<form:hidden path="products[0].productId" />
							</c:if>
							<label for="product">Products</label>
							<form:select path="products[0].productTypeId"
								class="form-control select_input" id="product"
								onclick="productSelect()"
								onblur="changeProductItems(this.value,${order.editMode})"
								onchange="changeProductItems(this.value,${order.editMode})"
								tabindex="10">
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
								id="noofcopies" onkeypress="return isNumberKey(event)"
								tabindex="11" value="1" onblur="noOfCopy(this);" />
						</div>

						<div class="form-group col-md-6 col-lg-2">
							<label for="size">Size</label>
							<form:select path="products[0].size" class="form-control"
								id="size" tabindex="12">
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
								id="noofsheet" onkeypress="return isNumberKey(event)"
								tabindex="13" required="true" />
						</div>

						<div class="form-group col-md-6 col-lg-3">
							<label for="duedate">Due Date</label>
							<form:input path="strDueDate" class="form-control" id="duedate"
								tabindex="14" required="true" />
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
																class="btn btn-primary btn-rounded btn-sm my-0"
																onclick="editModeAddRow()" data-toggle="tooltip"
																data-placement="left" title="Insert Row">
																<i class="fa fa-plus" style='font-size: 20px'></i>
															</button>
														</c:when>
														<c:otherwise>
															<button type="button"
																class="btn btn-primary btn-rounded btn-sm my-0"
																onclick="addRow()" data-toggle="tooltip"
																data-placement="left" title="Insert Row">
																<i class="fa fa-plus" style='font-size: 20px'></i>
															</button>
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
															class="form-control match-content select_input " 
															onfocus="myFunction(${loop.index})" 
															id="productitem_${loop.index}" tabindex="${tabIndex}">
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
															placeholder="Qty" onkeypress="return isNumberKey(event)"
															onblur="setAmount(this.value,${loop.index})"
															tabindex="${tabIndex}" /></td>
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
															placeholder="Remarks" tabindex="${tabIndex}" /></td>
													<td nowrap="nowrap"><span class="table-remove"><button
																type="button"
																class="btn btn-danger btn-rounded btn-sm my-0 ibtnDel"
																data-toggle="tooltip" data-placement="left"
																title="Remove Row">
																<i class="fa fa-times" style='font-size: 20px'></i>
															</button></span></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
							<div>
								<div align="left">

									<%--  <label >Invoice Amount:</label>
      <label>${balance}</label>  --%>


									<div align="center">
										<c:choose>
											<c:when test="${order.cancelMode}">
												<button type="submit" class="btn btn-danger"
													style="width: 100px;" onclick="cancelOrder();">Cancel</button>
											</c:when>
											<c:when test="${order.editMode}">
												<button type="button" class="btn btn-warning"
													onclick="saveCustomer()">Add Product & Continue</button>
												<button type="submit" class="btn btn-primary"
													style="width: 200px;" onclick="editSave();">Add
													Product</button>

											</c:when>
											<c:when test="${order.enablePayment}">
												<button type="submit" class="btn btn-warning"
													style="width: 300px; font-size: 20px" onclick="save()">
													<i class="fa fa-plus-circle"
														style='font-size: 25px; padding: 0 1em 0 0'></i>Add to
													Cart
												</button>
												<button type="button" class="btn btn-success"
													style="width: 300px; font-size: 20px"
													onclick="saveCustomer()">
													<i class="fa fa-inr"
														style='font-size: 20px; padding: 0 1em 0 0'></i>Continue
													for Payment
												</button>
											</c:when>
											<c:otherwise>
												<button type="submit" class="btn btn-warning"
													style="width: 300px; font-size: 20px" onclick="save()">
													<i class="fa fa-plus-circle"
														style='font-size: 25px; padding: 0 1em 0 0'></i>Add to
													Cart
												</button>

											</c:otherwise>
										</c:choose>
										<%-- <c:otherwise> --%>
										<!-- 	<button type="button" class="btn btn-warning" onclick="saveCustomer()">Add Product & Continue</button> -->
										<!-- <button type="submit" class="btn btn-success" style="width: 100px;">Save</button> -->
										<!-- 	<button type="submit" class="btn btn-primary" style="width:200px;" onclick="save();" >Add Product </button> -->

									</div>
								</div>

								<div class="table-responsive col-lg-12"
									style="padding-left: 0px;">
									<table id="reporttable"
										class="table table-bordered table-striped display wrap"
										style="width: 100%" data-page-length='5'>
										<thead>
											<tr>
												<th style="background-color: rgb(28, 129, 163)"
													class="col-sm-1" data-field="trxnDate"><font
													color="skyblue">Product Name</font></th>
												<th style="background-color: rgb(28, 129, 163)"
													class="col-sm-3" data-field="referenceType"><font
													color="skyblue">Copies</font></th>
												<th style="background-color: rgb(28, 129, 163)"
													class="col-sm-3" data-field="trxnType"><font
													color="skyblue"> Size</font></th>
												<th style="background-color: rgb(28, 129, 163)"
													class="col-sm-3" data-field="description"><font
													color="skyblue">Total Qty</font></th>
												<th style="background-color: rgb(28, 129, 163)"
													class="col-sm-3" data-field="debitAmount"><font
													color="skyblue">Particulars</font></th>
												<th style="background-color: rgb(28, 129, 163)"
													class="col-sm-1" data-field="creditAmount"><font
													color="skyblue">Qty</font></th>
												<th style="background-color: rgb(28, 129, 163)"
													class="col-sm-1" data-field="creditAmount"><font
													color="skyblue">Rate</font></th>
												<th style="background-color: rgb(28, 129, 163)"
													class="col-sm-1" data-field="creditAmount"><font
													color="skyblue">Amount</font></th>
												<th style="background-color: rgb(28, 129, 163)"
													class="col-sm-1" data-field="creditAmount"><font
													color="skyblue">Action</font></th>
											</tr>
										</thead>
										<tfoot>
											<tr>
												<th></th>
												<th></th>
												<th></th>
												<th></th>
												<th></th>
												<th></th>
												<th></th>
												<th></th>
												<th></th>
											</tr>
										</tfoot>
									</table>
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
var enablePayment=false
 var balance;
$(document).ready(function() {
	 document.getElementById("reporttable").style.visibility = "visible";
		var reqData={'order': 'viewworkorder'};
		var tableClient = $('#reporttable').DataTable({
			"columnDefs": [
				    {
				        targets: -1,
				        className: 'dt-body-right'
				    },
				    {
				        targets: 1,
				        className: 'dt-body-center'
				    },
				    {
				        targets: 2,
				        className: 'dt-body-center'
				    },
				    {
				        targets: 3,
				        className: 'dt-body-center'
				    },
				    {
				        targets: 5,
				        className: 'dt-body-center'
				    },
				    {
				        targets: 6,
				        className: 'dt-body-right'
				    },
				    {
				        targets: 7,
				        className: 'dt-body-right'
				    },
				    {
				        targets: 8,
				        className: 'dt-body-center'
				    },
				    
				  ],
		"scrollY": 300,
		"destroy": false,
		"searching":false,
		"paging": false,
		"ordering": false,
			"ajax": {
			"url": "${pageContext.request.contextPath}/viewworkorder/",
			"type": "GET",
			"data" : reqData,
			"success" :  function(data){
				console.log(data.length);
				 if(data.length > 0){ 
				 $.each(data, function(index, obj){
					    if (obj.noofsheets>0){
						tableClient.row.add([
						obj.prodName,	
						obj.noofcopies,
						obj.size,
						obj.noofsheets,
						obj.itemname,
						obj.qty,
						obj.rate,
						obj.amount,
						obj.noofsheets > 0 ?"<input type='button' class='btn btn-danger' id='id_"+obj.prodNo+"' onclick='deleteProduct("+obj.prodNo+");' value='Remove'>":" " ,
						]).draw();
							}else{
					    	tableClient.row.add([
								obj.prodName,
								obj.noofcopies,
								obj.size,
								obj.noofsheets, 
								obj.itemname,
								obj.qty,
								obj.rate,
								obj.amount,
								obj.noofsheets == 0? " ":" "						    
					]).draw();
					    }
				});
				 }else{
					
					 $('#reporttable').DataTable({
						"destroy": true,
						"searching":false,
						 "bLengthChange" : false,
						"bInfo":false,
					});
				 }
			
		}	
	  },
	  "aoColumnDefs": [
			 {
				  
		    "aTargets": [7],
		    "mRender": function (data, type, full) {
		     var formmatedvalue='';
		     if(data != null){
		    	 formmatedvalue=parseFloat(data).toFixed(2);
		     }
		      return formmatedvalue;
		    }
			},
						
		],
"footerCallback": function ( row, data, start, end, display ) {
            
			var api = this.api();
			$(row).find('th:eq(6)').css('color', 'red');
			$(row).find('th:eq(6)').css('font-weight', 'bold');
			$(row).find('th:eq(7)').css('color', 'red');
			$(row).find('th:eq(7)').css('font-weight', 'bold');
 
            // Remove the formatting to get integer data for summation
            var intVal = function ( i ) {
                return typeof i === 'string' ?
                    i.replace(/[\$,]/g, '')*1 :
                    typeof i === 'number' ?
                        i : 0;
            };
 
            // Total over all pages
            total = api
                .column( 7)
                .data()
                .reduce( function (a, b) {
                    return intVal(a) + intVal(b);
                }, 0 );
            
            drTotal=total;
            // Total over this page
            pageTotal = api
                .column( 7, { page: 'current'} )
                .data()
                .reduce( function (a, b) {
                    return intVal(a) + intVal(b);
                }, 0 );
 			
            // Update footer
            $( api.column(7 ).footer() ).html(
                     parseFloat(total).toFixed(2) 
             );
            
                  
        var cBalance = api
        /*  .column( 7, { page: 'current'} ) */
        .column(7 )
        .data()
        .reduce( function (a, b) {
            return intVal(a) + intVal(b);
    
        },0 );
       console.log("cBalance ",cBalance); 
     // Update footer
        $( api.column( 6 ).footer() ).html(
	        	'Total:' 
	    ),
	    $( api.column( 7 ).footer() ).html(
	        	parseFloat(cBalance).toFixed(2) 
	    )},

	  
	  
	  'rowCallback': function(row, data, index){
		  if(data[0] !=''){
			$(row).find('td:eq(0)').css('color', 'red'); 
//			$(row).find('td:eq(0)').css('font-weight', 'bold');    
			}
		  if(data[1] !=0){
			   	$(row).find('td:eq(1)').css('color', 'red');
//				$(row).find('td:eq(1)').css('font-weight', 'bold'); 
			}else {
		        data[1]='';
		    }
				
		  if(data[2] != ''){
				$(row).find('td:eq(2)').css('color', 'red');
//				$(row).find('td:eq(2)').css('font-weight', 'bold'); 
			}
		  if(data[3] !=0){
				$(row).find('td:eq(3)').css('color', 'red');
//				$(row).find('td:eq(3)').css('font-weight', 'bold'); 
			}
			
		  if(data[4] != ''){
			 	$(row).find('td:eq(4)').css('color', 'Blue' ); 
//			 	$(row).find('td:eq(4)').css('font-weight', 'bold'); 
			}
		  if(data[5] != ''){
				$(row).find('td:eq(5)').css('color', 'Blue');
//				$(row).find('td:eq(5)').css('font-weight', 'bold'); 
			}
		  if(data[6] != ''){
				$(row).find('td:eq(6)').css('color', 'Blue');
//				$(row).find('td:eq(6)').css('font-weight', 'bold'); 
			}
		  if(data[7] != ''){
				$(row).find('td:eq(7)').css('color', 'Blue');
//				$(row).find('td:eq(7)').css('font-weight', 'bold'); 
			}
	  }
	  
	});
});



function deleteProduct(prodno){
	
	/* document.forms['workOrderDetailForm'].reset();
	 location.reload(false);  */
	$.ajax({
		type: "GET",
		url: "${pageContext.request.contextPath}/deleteproduct/" + prodno,
		/* timeout : 100000,  */
		success: function(data){
			  var tableClient=$("#reporttable").DataTable();
			  var rows = tableClient
			     .rows()
			     .remove()
			     tableClient.ajax.reload();
			 	 tableClient.draw(); 
			 	/*  document.forms['workOrderDetailForm'].reset();
			 	 location.reload(false); */
			
		},
		
		error: function(e){
			alert("ERROR: ", e);
		}
			
	});
	
	
}
 



$(function () {
	  $('[data-toggle="tooltip"]').tooltip()
	})
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
	
	<!--alert(document.getElementById("product").value);-->
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
	var rows = document.getElementById("productitemtable").getElementsByTagName("tbody")[0].getElementsByTagName("tr").length;
	var noofsheet = document.getElementById("noofsheet").value
	var itemCount = 0;
	for(var i=0; i<rows;i++){
		if($('#qty_'+i).val())
			itemCount = parseFloat(itemCount)+parseFloat($('#qty_'+i).val());
		}
	
	if(itemCount < parseFloat(noofsheet)){
		alert("You are not selected as you requested");
		$('#qty_'+index).val('');
		document.getElementById("noofsheet").focus();
		entered=0;
		return;
	}else if(entered){
		
	document.workOrderForm.action="${pageContext.request.contextPath}/workorderdetail";
	document.workOrderForm.method="POST";
	document.workOrderForm.submit();
	document.forms['workOrderForm'].reset();
	
	 
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
	 var rows = document.getElementById("productitemtable").getElementsByTagName("tbody")[0].getElementsByTagName("tr").length;
		var noofsheet = document.getElementById("noofsheet").value
		var itemCount = 0;
		for(var i=0; i<rows;i++){
			if($('#qty_'+i).val())
				itemCount = parseFloat(itemCount)+parseFloat($('#qty_'+i).val());
			}
		if(itemCount < parseFloat(noofsheet)){
			alert("You are not selected no of sheet as you requested");
			$('#qty_'+index).val('');
			document.getElementById("noofsheet").focus();
			return;
		}else{
	document.workOrderForm.action="${pageContext.request.contextPath}/workordereditsave";
	document.workOrderForm.method="POST";
	document.workOrderForm.submit();
	}
	//document.body.innerHTML += '<div class="alert alert-danger" role="alert"> Selected Product updated</div>';
	
	
}

 function save(){
	
	 var rows = document.getElementById("productitemtable").getElementsByTagName("tbody")[0].getElementsByTagName("tr").length;
		var noofsheet = document.getElementById("noofsheet").value
		var itemCount = 0;
		for(var i=0; i<rows;i++){
			if($('#qty_'+i).val())
				itemCount = parseFloat(itemCount)+parseFloat($('#qty_'+i).val());
			
			}
		if(noofsheet >= 1){
		if(itemCount < parseFloat(noofsheet)){
			alert("You are not selected no of sheet as you requested");
			$('#qty_'+index).val('');
			document.getElementById("noofsheet").focus();
			return;
		}else{
	document.workOrderForm.action="${pageContext.request.contextPath}/workorder";
	document.workOrderForm.method="POST";
	document.workOrderForm.submit();
		}
		}else{
			return;
			}
	/* //document.body.innerHTML += '<div class="alert alert-danger" role="alert">Your Product added in the Cart....</div>';
	document.body.innerHTML += 'class="container-fluid">'document.body.innerHTML'
	document.body.innerHTML +='<div class="row">'

	document.body.innerHTML +=' <div class="col-md-10 col-md-offset-1">'
	document.body.innerHTML +='<div class="alert alert-warning alert-dismissible fade in text-center" role="alert">'
	document.body.innerHTML += '<strong>Holy guacamole!</strong> Best check yo self, you're not looking too good.'
	document.body.innerHTML +='       </div>'
		document.body.innerHTML +='  </div>'
			document.body.innerHTML +='  </div>'
				//document.body.innerHTML +='  </div>'
 */
  
} 

 function  editWorkOrder(){
		document.workOrderDetailForm.action="workorderedit";
		document.workOrderDetailForm.method="POST";
		document.workOrderDetailForm.submit();
	}

function editOrder(orderid){
	document.workOrderForm.action="${pageContext.request.contextPath}/editorder/"+orderid;
	document.workOrderForm.method="POST";
	document.workOrderForm.submit();
	
		  
}
function cancelOrder(orderid){
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
				$('#customeraddress2').val(entry.addr2);
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
document.addEventListener('keyup', doc_keyUp, false);

function doc_keyUp(e) {
    // this would test for whichever key is 40 and the ctrl key at the same time
    if (e.keyCode == 113) {
  	 	alert("Your Product added in the Cart....!!!")
  		save();
    }
    if (e.keyCode == 115) {
  	 	alert("Proceeding to Payment....!!!")
  		saveCustomer();
    }

 }
 // to disable ( F5 ) refresh 
 $(document).bind("keydown", disableF5);
 function disableF5(e) { if ((e.which || e.keyCode) == 116) e.preventDefault(); }; 


 
 
function productSelect() {	         
	 debugger
	        $("#product").html($('#product option').sort(function(x, y) {
	        debugger
	            return $(x).text().toUpperCase() < $(y).text().toUpperCase() ? -1 : 1;
	        }));
	        //$("#product").get(0).selectedIndex = 0;
	        //e.preventDefault();
		}  
 

	function  myFunction(index){	
		if(index == 0){
	
		  $("#productitem_0").html($('#productitem_0 option').sort(function(x, y) {
		        debugger
		            return $(x).text().toUpperCase() < $(y).text().toUpperCase() ? -1 : 1;
		        }));
		        $("#productitem_0").get(0).selectedIndex = 0;
		        e.preventDefault(); 
		}else if(index ==1){
			$("#productitem_1").html($('#productitem_1 option').sort(function(x, y) {
		        debugger
		            return $(x).text().toUpperCase() < $(y).text().toUpperCase() ? -1 : 1;
		        }));
		        $("#productitem_1").get(0).selectedIndex = 0;
		        e.preventDefault(); 
		
		}else if(index ==2){
			$("#productitem_2").html($('#productitem_2 option').sort(function(x, y) {
		        debugger
		            return $(x).text().toUpperCase() < $(y).text().toUpperCase() ? -1 : 1;
		        }));
		        $("#productitem_2").get(0).selectedIndex = 0;
		        e.preventDefault(); 
		
		}
		else if(index ==3){
			$("#productitem_3").html($('#productitem_3 option').sort(function(x, y) {
		        debugger
		            return $(x).text().toUpperCase() < $(y).text().toUpperCase() ? -1 : 1;
		        }));
		        $("#productitem_3").get(0).selectedIndex = 0;
		        e.preventDefault(); 
		
		}else if(index ==4){
			$("#productitem_4").html($('#productitem_4 option').sort(function(x, y) {
		        debugger
		            return $(x).text().toUpperCase() < $(y).text().toUpperCase() ? -1 : 1;
		        }));
		        $("#productitem_4").get(0).selectedIndex = 0;
		        e.preventDefault(); 
		
		}
		
	
	
	}
	 
	
</script>

<SCRIPT language=Javascript>
     
       function isNumberKey(evt)
       {
          var charCode = (evt.which) ? evt.which : evt.keyCode;
          if (charCode != 46 && charCode > 31 && charCode < 48 || charCode > 57)
             return false;

          return true;
       }
     
    </SCRIPT>
<!--<script>
window.onload=function(){
	alert("form load....");
	if(${order.editMode}) {
		changeProductItems(document.getElementById("product").value,true);
	${editMode}='false';
	}
}
</script>-->

</html>
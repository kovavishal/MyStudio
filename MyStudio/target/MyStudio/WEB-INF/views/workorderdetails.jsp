<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="staticcontent.jsp"%>

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

<title>Work Order</title>
</head>
<body>


	<%@include file="navbar.jsp"%>
	<div class="container-fluid" id="main">
		<div class="card">
			<div class="card-header text-center"
				style="padding-bottom: 0px; padding-top: 0px; background: skyblue; color: #30567D; font-weight: 625;">
				<jsp:include page="logout.jsp">
					<jsp:param name="page" value="Work Order Payment" />
				</jsp:include>
			</div>
			<c:if test="${message ne null and message ne ''}">
				<div class="alert alert-danger" align="center">${message}</div>
			</c:if>
			<div class="card-body bg-light border-success mb-3 col-lg-12"
				style="color: navy;">
				<form:form method="POST" action="workorderpayment"
					modelAttribute="order" name="workOrderDetailForm">
					<form:hidden path="orderTrxnDetails[0].trxnId" />
					<form:hidden path="custId" />
					<form:hidden path="strOrderDate" />
					<form:hidden path="orderType" />
					<form:hidden path="agentName" />
					<form:hidden path="custName" />
					<form:hidden path="custType" />
					<form:hidden path="strDueDate" />
					<form:hidden path="wrapperName" />
					<form:hidden path="orderId" />
					<form:hidden path="products[0].productTypeId" />
					<form:hidden path="custAddr1" />
					<form:hidden path="custAddr2" />
					<form:hidden path="rateType" />
					<form:hidden path="email" />
					<form:hidden path="creditBalance" />


					<div class="form-row">
						<div class="form-group col-md-6 col-lg-2">
							<label for="creditbalance">Credit Balance</label>
							<form:input path="creditBalance" class="form-control"
								id="creditBalance" disabled="true" />
						</div>

						<div class="form-group col-md-4 col-lg-2">
							<label for="deliverymode">Delivery Mode</label>
							<form:select path="deliveryTypeId" class="form-control"
								id="deliverymode">
								<c:forEach items="${deliveryTypes}" var="deliveryType">
									<c:choose>
										<c:when test="${deliveryType.id eq  order.deliveryTypeId}">`
	    			<option value="${deliveryType.id}" selected="selected">${deliveryType.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${deliveryType.id}">${deliveryType.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
						</div>

						<div class="form-group col-md-4 col-lg-2">
							<label for="paymentmode">Payment Mode</label>
							<form:select path="orderTrxnDetails[0].pymtType"
								class="form-control" id="paymentmode"
								onchange="showBankDetail();">
								<c:forEach items="${paymentTypes}" var="paymentType">
									<c:choose>
										<c:when
											test="${paymentType.id eq  order.orderTrxnDetails[0].pymtType}">
											<option value="${paymentType.id}" selected="selected">${paymentType.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${paymentType.id}">${paymentType.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
						</div>
						<%--  <div class="form-group col-md-6 col-lg-4">
	      <label for="custid">Remarks</label>
	      <form:input path="custId" class="form-control" id="custid" hidden="true"/>
	    </div> --%>

						<div class="form-group col-md-6 col-lg-4">
							<label for="orderremarks">Remarks</label>
							<form:input path="remarks" class="form-control" id="orderremarks" />
						</div>

						<div class="form-group col-md-6 col-lg-2">
							<label for="gst">GST No</label>
							<form:input path="gstNo" class="form-control" id="gst" />
						</div>
					</div>

					<div class="form-row" id="paymentbank" style="display: none;">
						<div class="form-group col-md-6 col-lg-3">
							<label for="chequeno">Cheque Number</label>
							<form:input path="orderTrxnDetails[0].chequeNo"
								class="form-control" id="chequeno" />
						</div>
						<div class="form-group col-md-4 col-lg-3">
							<label for="chequedate">Cheque Date</label>
							<form:input path="orderTrxnDetails[0].strChequeDate"
								class="form-control" id="chequedate" />
						</div>

						<div class="form-group col-md-4 col-lg-3">
							<label for="chequeamt">Cheque Amount</label>
							<form:input path="orderTrxnDetails[0].chequeAmount"
								class="form-control" id="chequeamt" />
						</div>

						<div class="form-group col-md-4 col-lg-3">
							<label for="bankname">Bank Name</label>
							<form:input path="orderTrxnDetails[0].bankName"
								class="form-control" id="bankname" />
						</div>
					</div>


					<div class="row ">
						<div class="col-sm-8 col-lg-10">
							<div class="card bg-light  mb-2" style="color: navy;">
								<div class="card-header" align="center">Job Allocation</div>
								<div class="card-body" style="padding: 0.25rem">
									<div id="table" class="table-editable">

										<table
											class="table table-bordered table-responsive-md table-striped text-center">
											<tr>
												<c:forEach items="${empDesgs}" var="empDesg">
													<th class="text-center" nowrap="nowrap">${empDesg.key}</th>

												</c:forEach>
											</tr>
											<c:set var="jobSize">${fn:length(empDesgs)}</c:set>
											<tr class="form-group">
												<c:forEach items="${empDesgs}" var="empDesgn"
													varStatus="loop">
													<td class="pt-3-half" style="width: 150px;"><form:select
															path="jobAllocations[${loop.index}].custId"
															class="form-control" id="jobtitle_${loop.index}">
															<option value="0">-Select-</option>
															<c:forEach items="${empDesgn.value}" var="desgn">
																<c:choose>
																	<c:when
																		test="${order.jobAllocations[loop.index].custId eq desgn.empId}">
																		<option value="${desgn.empId}" selected="selected">${desgn.name}</option>
																	</c:when>
																	<c:otherwise>
																		<option value="${desgn.empId}">${desgn.name}</option>
																	</c:otherwise>
																</c:choose>
															</c:forEach>
														</form:select></td>
												</c:forEach>


											</tr>
										</table>
									</div>
								</div>
							</div>



							<div class="col-sm-8 col-lg-12" id="ordertrnx">
								<div class="card col bg-right">
									<div class="card-body">
										<div class="form-row">
										<div class="form-group col-md-6 col-lg-2" style="margin: 0px">
												<label for="discount">Discount ( % )</label>

												<%-- <form:input path="orderTrxnDetails[0].discount"
													type="text" class="form-control" id="discountpercent"
													style="height: 25px;" min="0" max="10" 
													onchange="totalcalc(this.value); " /> --%>
													</br>
													 <select name="item" id="dpercent" onchange="dispercent(value)"
													 style="width:120px">
														 <option value="0">0</option>
  													  	<option value="1">1</option>
    												 	<option value="2">2</option>
    													<option value="3">3</option>
    													<option value="4">4</option>
    												 	<option value="5">5</option>
    													<option value="6">6</option>
    													<option value="7">7</option>
    												 	<option value="8">8</option>
    													<option value="9">9</option>
    													<option value="10">10</option>
    													
  													</select>

											</div>
											<div class="form-group col-md-6 col-lg-2" style="margin: 0px">
												<label for="discount">Discount-1</label>

												<input	type="number" class="form-control" id="discount-1"
													style="height: 25px;" readonly onchange="totalcalc(this.value);" />

											</div>
										
											<div class="form-group col-md-6 col-lg-2" style="margin: 0px">
												<label for="discount">Discount-1 + GST</label>

												<form:input path="orderTrxnDetails[0].discount"
													type="number" class="form-control" id="discount"
													style="height: 25px;" onchange="totalcalc(this.value);" />

											</div>
											<div class="form-group col-md-6 col-lg-3" style="margin: 0px">
												<label for="delcharge">Delivery ( + )</label>

												<form:input path="orderTrxnDetails[0].delvCharge"
													type="number" class="form-control" id="delcharge"
													style="height: 25px;" onchange="totalcalc(this.value);" />
											</div>
											<div class="form-group col-md-6 col-lg-3" style="margin: 0px">
												<label for="advance">Advance ( - )</label>
												<form:input path="orderTrxnDetails[0].advance" type="number"
													class="form-control" id="advance" style="height: 25px;"
													onchange="totalcalc(this.value);" />
											</div>
										</div>
									</div>
								</div>
							</div>




							<div>
								<br> <br>

								<div>
									<div align="center">
										<c:choose>
											<c:when test="${ordersaved ne 'yes'}">
												<!-- <button type="button" class="btn btn-success" style="width: 200px;" onclick="report();">
			 <i class="fa fa-eye" style='font-size:25px'></i>
			View Order</button> -->

												<button type="button" class="btn btn-success"
													style="width: 300px; font-size: 20px"
													onclick="addWorkOrder()">
													<i class="fa fa-arrow-left"
														style='font-size: 25px; padding: 0 1em 0 0'></i>Back to
													Cart
												</button>

												<!-- <button type="button" class="btn btn-warning" style="width: 200px;" onclick="editWorkOrder();">
			<i class="fa fa-edit" style='font-size:20px;color:black'></i> 
			 Go Back</button> -->

												<button type="button" class="btn btn-danger"
													style="width: 300px; font-size: 20px" onclick="saveOrder()">
													<i class='fa fa-check-circle'
														style='font-size: 28px; padding: 0 1em 0 0'></i>Confirm
													Order
												</button>

												<!-- <button type="button" class="btn btn-danger" style="width: 200px;" onclick="saveOrder();">
			<i class='fa fa-check-circle' style='font-size:20px;color:white'></i>
			Confirm Order</button> -->
											</c:when>
											<c:otherwise>
												<button type="button" class="btn btn-success" id="print"
													style="width: 300px; font-size: 20px"
													onclick="printOrder()">
													<i class='fa fa-print'
														style='font-size: 28px; padding: 0 1em 0 0'></i>Print
													Invoice
												</button>
												</button>
												<!-- <button type="button" class="btn btn-success" style="width: 100px;" onclick="invoiceEmail();">Email</button> -->
												<!-- <button type="button" class="btn btn-warning" id="moreorders" style="width: 150px;"  disabled onclick="printOrder();"><h5>More Orders</h5></button> -->
											</c:otherwise>
										</c:choose>
									</div>
								</div>

							</div>

							<%-- 	<div>
								<div align="center">
	<c:choose>
		<c:when test="${ordersaved ne 'yes'}">
			<button type="button" class="btn btn-primary" style="width: 100px;" onclick="viewWorkOrder();">Add</button>
			<button type="button" class="btn btn-primary" style="width: 100px;" onclick="editWorkOrder();">Edit</button>
			<button type="button" class="btn btn-success" style="width: 100px;" onclick="saveOrder();">Save</button>
		</c:when>
		<c:otherwise>
		 	<button type="button" class="btn btn-success" style="width: 100px;" onclick="printOrder();">Print</button>
			<!-- <button type="button" class="btn btn-success" style="width: 100px;" onclick="invoiceEmail();">Email</button> -->
		</c:otherwise>
	</c:choose>
	</div>
							</div>  --%>

							<!-- 							
<div class="table-responsive col-lg-12" style="padding-left:0px;">
		<table id="reporttable" class="table table-bordered table-striped display wrap" style="width:100%"  data-page-length='5'>
			<thead >
				<tr>
					<th style="background-color:rgb(28, 129, 163)" class="col-sm-1" data-field="trxnDate">
					<font color="skyblue">Product Name</font></th>
					<th style="background-color:rgb(28, 129, 163)" class="col-sm-3" data-field="referenceType">
					<font color="skyblue">Copies</font></th>
					<th style="background-color:rgb(28, 129, 163)" class="col-sm-3" data-field="trxnType">
					<font color="skyblue"> Size</font></th>
					<th style="background-color:rgb(28, 129, 163)" class="col-sm-3" data-field="description">
					<font color="skyblue"> Qty</font></th>
					<th style="background-color:rgb(28, 129, 163)" class="col-sm-3" data-field="debitAmount">
					<font color="skyblue">Name</font></th>
					<th style="background-color:rgb(28, 129, 163)" class="col-sm-1" data-field="creditAmount">
					<font color="skyblue">Qty</font></th>
					<th style="background-color:rgb(28, 129, 163)" class="col-sm-1" data-field="creditAmount">
					<font color="skyblue">Rate</font></th>
					<th style="background-color:rgb(28, 129, 163)" class="col-sm-1" data-field="creditAmount">
					<font color="skyblue">Amount</font></th>
					<th style="background-color:rgb(28, 129, 163)" class="col-sm-1" data-field="creditAmount" >
					<font color="skyblue">Action</font></th>
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
                <th ></th>
            </tr>
        </tfoot>
		</table>
	</div>
							
 -->

						</div>

						<div class="col-sm-8 col-lg-2" id="ordertrnx">
							<div class="card row bg-light">
								<div class="card-body">
									<div class="form-row">
										<div class="form-group col-md-6 col-lg-12" style="margin: 0px">
											<label for="total">Amount</label>
											<form:input path="orderTrxnDetails[0].total"
												class="form-control" id="total" readonly="true"
												style="height: 25px;" />
										</div>

										<div class="form-group col-md-2 col-lg-12" style="margin: 0px">
											<label for="cgst">CGST</label>
											<form:input path="orderTrxnDetails[0].cgst"
												class="form-control" id="cgst" readonly="true"
												style="height: 25px;" />
										</div>
										<div class="form-group col-md-2 col-lg-12" style="margin: 0px">
											<label for="sgst">SGST</label>
											<form:input path="orderTrxnDetails[0].sgst"
												class="form-control" id="sgst" readonly="true"
												style="height: 25px;" />
										</div>
										<div class="form-group col-md-2 col-lg-12" style="margin: 0px">
											<label for="igst">IGST</label>
											<form:input path="orderTrxnDetails[0].igst"
												class="form-control" id="igst" readonly="true"
												style="height: 25px;" />
										</div>
									</div>
									<div class="form-row">

										<div class="form-group col-md-6 col-lg-12" style="margin: 0px">
											<label for="subtotal">Sub Amount</label>
											<form:input path="orderTrxnDetails[0].subTotal"
												class="form-control" id="subtotal" readonly="true"
												style="height: 25px;color:blue;font-size:larger" />
										</div>
									</div>

									<div class="form-row">

										<div class="form-group col-md-2 col-lg-12" style="margin: 0px">
											<label for="netamount">Net Amount</label>
											<form:input path="orderTrxnDetails[0].netAmount"
												class="form-control" id="netamount" readonly="true"
												style="height: 25px;color:green;font-size:larger" />
										</div>
									</div>

									<div class="form-row">

										<div class="form-group col-md-2 col-lg-12" style="margin: 0px">
											<label for="balance">Balance</label>
											<form:input path="orderTrxnDetails[0].balance"
												class="form-control" id="balance" readonly="true"
												style="height: 25px;color:red;font-size:larger" />
										</div>
									</div>

								</div>
							</div>
						</div>

					</div>
				</form:form>

				<!-- 
<div class="table-responsive col-lg-8" style="padding-left:0px;">
		<table id="reporttable" class="table table-bordered table-striped display nowrap" style="width:80%">
			<thead>
				<tr>
					<th class="col-sm-1" data-field="trxnDate">Product Name</th>
					<th class="col-sm-3" data-field="trxnType">Product Size</th>
					<th class="col-sm-3" data-field="referenceType">No of Copies</th>
					<th class="col-sm-3" data-field="description">Total Qty</th>
					<th class="col-sm-3" data-field="debitAmount">Item Name</th>
					<th class="col-sm-1" data-field="creditAmount">Item Qty</th>
					<th class="col-sm-1" data-field="creditAmount">Item Rate</th>
					<th class="col-sm-1" data-field="creditAmount">Amount</th>
					
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
            </tr>
        </tfoot>
		</table>
	</div>
 -->
				<%-- <div>
								<div align="center">
	<c:choose>
		<c:when test="${ordersaved ne 'yes'}">
			<!-- <button type="button" class="btn btn-success" style="width: 200px;" onclick="report();">
			 <i class="fa fa-eye" style='font-size:25px'></i>
			View Order</button> -->
			
			<button type="button" class="btn btn-success"  style="width: 300px; font-size:20px" onclick="addWorkOrder()"> 
			<i class="fa fa-arrow-left"style='font-size:25px; padding:0 1em 0 0' ></i>Back to Cart</button>
						
			<!-- <button type="button" class="btn btn-warning" style="width: 200px;" onclick="editWorkOrder();">
			<i class="fa fa-edit" style='font-size:20px;color:black'></i> 
			 Go Back</button> -->
			 
			 <button type="button" class="btn btn-danger"  style="width: 300px; font-size:20px" onclick="saveOrder()"> 
			<i class='fa fa-check-circle' style='font-size:28px; padding:0 1em 0 0' ></i>Confirm Order</button>
						
			<!-- <button type="button" class="btn btn-danger" style="width: 200px;" onclick="saveOrder();">
			<i class='fa fa-check-circle' style='font-size:20px;color:white'></i>
			Confirm Order</button> -->
		</c:when>
		<c:otherwise>
		 	<button type="button" class="btn btn-success" id="print" style="width: 150px;" onclick="printOrder();"><h5>Print Invoice</h5></button>
			<!-- <button type="button" class="btn btn-success" style="width: 100px;" onclick="invoiceEmail();">Email</button> -->
			<!-- <button type="button" class="btn btn-warning" id="moreorders" style="width: 150px;"  disabled onclick="printOrder();"><h5>More Orders</h5></button> -->
		</c:otherwise>
	</c:choose>
	</div>
</div>  --%>
			</div>

		</div>

	</div>

</body>

<script type="text/javascript">





/* $(document).ready(function() {
	
	var fValue = $('#creditbalance').val();
	if(fValue!= null && fValue!='')
	$('#creditbalance').val(parseFloat(fValue).toFixed(2));
	
	fValue = $('#discount').val();
	if(fValue!= null && fValue!='')
	$('#discount').val(parseFloat(fValue).toFixed(2));

	fValue = $('#total').val();
	if(fValue!= null && fValue!='')
	$('#total').val(parseFloat(fValue).toFixed(2));
	
	fValue = $('#cgst').val();
	if(fValue!= null && fValue!='')
	$('#cgst').val(parseFloat(fValue).toFixed(2));
	
	fValue = $('#sgst').val();
	if(fValue!= null && fValue!='')
	$('#sgst').val(parseFloat(fValue).toFixed(2));

	fValue = $('#igst').val();
	if(fValue!= null && fValue!='')
	$('#igst').val(parseFloat(fValue).toFixed(2));
	
	fValue = $('#subtotal').val();
	if(fValue!= null && fValue!='')
	$('#subtotal').val(parseFloat(fValue).toFixed(2));
	
	fValue = $('#delcharge').val();
	if(fValue!= null && fValue!='')
	$('#delcharge').val(parseFloat(fValue).toFixed(2));
	
	fValue = $('#netamount').val();
	if(fValue!= null && fValue!='')
	$('#netamount').val(parseFloat(fValue).toFixed(2));
	
	fValue = $('#advance').val();
	if(fValue!= null && fValue!='')
	$('#advance').val(parseFloat(fValue).toFixed(2));
	
	fValue = $('#balance').val();
	if(fValue!= null && fValue!='')
	$('#balance').val(parseFloat(fValue).toFixed(2));
	
	showBankDetail();
	var saved = "${ordersaved}";
	console.log("saved", saved);
	if(saved == 'yes'){
		$("#ordertrnx :input").attr("disabled", true);
	}
}); */
 $(document).ready(function() {
	// alert("hai");
	
	//document.getElementById("reporttable").style.visibility = "hidden";
	var fValue = $('#creditbalance').val();
	;
	if(fValue!= null && fValue!='')
	$('#creditbalance').val(parseFloat(fValue).toFixed(2));
	 
	/* fValue = $('#cgst').val();
	fValue=fValue+$('#sgst').val();
	if(fValue!= null && fValue!='')
	$('#discount').val(parseFloat(fValue).toFixed(2));
 */
	fValue = $('#total').val();
	if(fValue!= null && fValue!='')
	$('#total').val(parseFloat(fValue).toFixed(2));
	
	fValue = $('#cgst').val();
	if(fValue!= null && fValue!='')
	$('#cgst').val(parseFloat(fValue).toFixed(2));
	
	fValue = $('#sgst').val();
	if(fValue!= null && fValue!='')
	$('#sgst').val(parseFloat(fValue).toFixed(2));
	
	fValue = $('#igst').val();
	if(fValue!= null && fValue!='')
	$('#igst').val(parseFloat(fValue).toFixed(2));
	
	
	sgst = $('#sgst').val();
	cgst = $('#cgst').val();
	fValue=(parseFloat(sgst) + parseFloat(sgst)).toFixed(2);
	//alert(Math.round(fValue));
	if(fValue!= null && fValue!=''){
	$('#discount').val(fValue);
	totalcalc(0);
	
	} 
	
	fValue = Math.round($('#subtotal').val());
	if(fValue!= null && fValue!='')
	$('#subtotal').val(parseFloat(fValue).toFixed(2)); 
	
	fValue = $('#delcharge').val();
	if(fValue!= null && fValue!='')
	$('#delcharge').val(parseFloat(fValue).toFixed(2));
	
	fValue =Math.round( $('#netamount').val());
	if(fValue!= null && fValue!='')
	$('#netamount').val(parseFloat(fValue).toFixed(2));
	
	fValue = $('#advance').val();
	if(fValue!= null && fValue!='')
	$('#advance').val(parseFloat(fValue).toFixed(2));
	
	fValue = Math.round($('#balance').val());
	if(fValue!= null && fValue!='')
	$('#balance').val(parseFloat(fValue).toFixed(2));
	
	showBankDetail();
	var saved = "${ordersaved}";
	console.log("saved", saved);
	if(saved == 'yes'){
		$("#ordertrnx :input").attr("disabled", true);
	}
});

/* function refresh(){
	alert('from refresh');
		document.getElementById("reporttable").style.visibility = "hidden";
	var fValue = $('#creditbalance').val();
	if(fValue!= null && fValue!='')
	$('#creditbalance').val(parseFloat(fValue).toFixed(2));
	
	fValue = $('#discount').val();
	if(fValue!= null && fValue!='')
	$('#discount').val(parseFloat(fValue).toFixed(2));

	fValue = $('#total').val();
	if(fValue!= null && fValue!='')
	$('#total').val(parseFloat(fValue).toFixed(2));
	
	fValue = $('#cgst').val();
	if(fValue!= null && fValue!='')
	$('#cgst').val(parseFloat(fValue).toFixed(2));
	
	fValue = $('#sgst').val();
	if(fValue!= null && fValue!='')
	$('#sgst').val(parseFloat(fValue).toFixed(2));

	fValue = $('#igst').val();
	if(fValue!= null && fValue!='')
	$('#igst').val(parseFloat(fValue).toFixed(2));
	
	fValue = $('#subtotal').val();
	if(fValue!= null && fValue!='')
	$('#subtotal').val(parseFloat(fValue).toFixed(2));
	
	fValue = $('#delcharge').val();
	if(fValue!= null && fValue!='')
	$('#delcharge').val(parseFloat(fValue).toFixed(2));
	
	fValue = $('#netamount').val();
	if(fValue!= null && fValue!='')
	$('#netamount').val(parseFloat(fValue).toFixed(2));
	
	fValue = $('#advance').val();
	if(fValue!= null && fValue!='')
	$('#advance').val(parseFloat(fValue).toFixed(2));
	
	fValue = $('#balance').val();
	if(fValue!= null && fValue!='')
	$('#balance').val(parseFloat(fValue).toFixed(2));
	
	showBankDetail();
	var saved = "${ordersaved}";
	console.log("saved", saved);
	if(saved == 'yes'){
		$("#ordertrnx :input").attr("disabled", true);
	}

} */

$(function () {
	
	$('#chequedate').datetimepicker({
		format: 'DD/MM/YYYY hh:mm A',
		icons: {
            time: "fa fa-clock-o",
            date: "fa fa-calendar",
            up: "fa fa-arrow-up",
            down: "fa fa-arrow-down"
        }
    });
	
});

 
function  printOrder() {
	
	var orderId = $('#orderId').val(); 
	var mode=0;
	console.log("order id to printing" + orderId);
 	var url = '${pageContext.request.contextPath}/printorder/'+orderId +"/"+mode ;
	/* document.getElementById("moreorders").disabled="false";
	document.getElementById)("print").disabled="true"; */
 	var win = window.open(url, '_blank');
  	win.focus();
 
}  
function  invoiceEmail() {
	
	var orderId = $('#orderId').val();
	var mode=0;
	console.log("order id to send E-mail" + orderId);
 	var url = '${pageContext.request.contextPath}/sendinvoiceemail/'+orderId +"/"+mode ;
 	var win = window.open(url, '_self');
 	 win.focus();
}  

function showBankDetail(){
	var payType= $("#paymentmode option:selected").text();
	console.log("payment mode" + payType);
	if(payType == 'Cheque')
		$("#paymentbank").css("display", "flex");
	else
		$("#paymentbank").css("display", "none"); 
}
/*  function applyDiscount(discountAmount){
	var total = $('#subtotal').val();
	if(discountAmount>0){
		total = (parseFloat(total) - parseFloat(discountAmount))
		$('#subtotal').val(total.toFixed(2));
		$('#netamount').val(total.toFixed(2));
		$('#balance').val(total.toFixed(2));
	}
	
} */
document.addEventListener('keyup', doc_keyUp, false);

function doc_keyUp(e) {
    // this would test for whichever key is 40 and the ctrl key at the same time
    if (e.keyCode == 113) {
        // call your function to do the thing
        if (confirm("Press OK to Confirm Order !!!")) {
        	saveOrder();
        	
			} 
    	
    }
} 

function totalcalc(value){
	//alert("hhhh");
	var dtotal = $('#total').val()
	var dcgst =$('#cgst').val();
	var dsgst =$('#sgst').val();
	var digst =$('#igst').val();
	
	dtotal=parseFloat(dtotal).toFixed(2);
		
	if(dcgst =='' && dsgst ==''){
		 dcgst=parseFloat(0).toFixed(2);
		 dsgst=parseFloat(0).toFixed(2);
	}else{
		dcgst=parseFloat(dcgst).toFixed(2);
		dsgst=parseFloat(dsgst).toFixed(2);
	}
	if(digst == ''){
		 digst=parseFloat(0).toFixed(2); 
	}else{
		digst=parseFloat(digst).toFixed(2);
	}
	//alert("digst :"+digst);
	//alert(parseFloat(dtotal)+parseFloat(dsgst)+parseFloat(dcgst)+parseFloat(digst));
	var stotal=(parseFloat(dtotal)+parseFloat(dsgst)+parseFloat(dcgst)+parseFloat(digst));
	//alert("reached here");

 	var discount=$('#discount').val()==''? "0":$('#discount').val();
 	//alert("discount 1 :"+$('#discount-1').val());
 	//alert("discount 2 :"+$('#discount').val());
 	discount=Math.round($('#discount').val());
 		//discount1=Math.round($('#discount-1').val());
//  	 discount=parseFloat(discount).toFixed(2)+parseFloat(discount1).toFixed(2);
//discount=(discount+discount1).toFixed(2);
 	//alert("TOTAL discount :"+discount);
 //alert("discount 2 :"+discount);
 	 	//$('#discount').val()==''? "0.00":$('#discount').val();
	$('#subtotal').val((parseFloat(stotal)-parseFloat(discount)).toFixed(2));

	var subtotal= $('#subtotal').val()==''? "0":$('#subtotal').val();
	//alert("subtotal 1 :"+subtotal);
	subtotal=parseFloat(subtotal).toFixed(2);
	//alert("subtotal 2 :"+subtotal);
	
	var dlyamount= $('#delcharge').val()==''?0:$('#delcharge').val();
	dlyamount=parseFloat(dlyamount).toFixed(2);
	//alert("dlyamount  :"+dlyamount);
	$('#netamount').val((parseFloat(subtotal) + parseFloat(dlyamount)).toFixed(2));

	var netamount= $('#netamount').val();
	netamount=parseFloat(netamount).toFixed(2);

	var advance=$('#advance').val()==''?0:$('#advance').val();
	advance=parseFloat(advance).toFixed(2);
	 $('#balance').val((parseFloat(netamount)- parseFloat(advance)).toFixed(2));
	
}


/* function applyDeliveryCharge(deliveryCharge){
	var total = $('#subtotal').val();
	if(deliveryCharge > 0){
		total = (parseFloat(total)+ parseFloat(deliveryCharge))
		$('#netamount').val(total.toFixed(2));
		$('#balance').val(total.toFixed(2));
	}
} */

function applyDeliveryCharge(deliveryCharge){
	 var namount= $('#subtotal').val();
	 var dadvance=$('#advance').val();
	 var dlyamount=$('#delcharge').val();
	//for delivery amount
	dadvance=parseFloat(dadvance).toFixed(2);
	if(dadvance){
		dadvance=parseFloat(dadvance).toFixed(2);    
	}
	else
		{ 
		dadvance=parseFloat(0).toFixed(2); 
		}
	 if (deliveryCharge){
		    namount= (parseFloat(namount) + parseFloat(deliveryCharge));
			$('#netamount').val(namount.toFixed(2));
			dadvance=$('#advance').val();
			if(dadvance){
				var dbalance= (parseFloat(namount) - parseFloat(dadvance));
				$('#balance').val(dbalance.toFixed(2));
			}else{$('#balance').val(namount.toFixed(2)); }
		} 
	 if (deliveryCharge==''){
			//dlyamount=parseFloat(dlyamount).toFixed(2);
			namount= (parseFloat(namount));
			$('#netamount').val(namount.toFixed(2));
			//alert("netamount :"+namount);
			if(dadvance){
				var dbalance= (parseFloat(namount) - parseFloat(dadvance));
				$('#balance').val(dbalance.toFixed(2));
			}
            if(dadvance==''){  
				namount=$('#netamount').val();
				namount= (parseFloat(namount));
				//alert("namount"+namount);
				$('#balance').val(namount.toFixed(2));
				 }
			}
		
	// verifying advance amount
		/* var dadvance=$('#advance').val();
		var dbalance= $('#netamount').val();
		 if ( dadvance==''){ 
			 dadvance=parseFloat(0).toFixed(2);
			 dbalance= parseFloat(dbalance) - parseFloat(dadvance);
			$('#balance').val(dbalance).toFixed(2);
				}
		else{ 
			
			dadvance=parseFloat(dadvance).toFixed(2);
			dbalance= parseFloat(dbalance) - parseFloat(dadvance);
			$('#balance').val(dbalance).toFixed(2);
			}  */
	} 


function applyAdvance(advanceAmount){
	var total = $('#netamount').val();
	if(advanceAmount){
		total = (parseFloat(total) - parseFloat(advanceAmount));
		$('#balance').val(total.toFixed(2));
	}else{  
		 total =parseFloat(total);
		$('#balance').val(total.toFixed(2));
}
}
function  editWorkOrder(){
	document.workOrderDetailForm.action="workorderedit";
	document.workOrderDetailForm.method="POST";
	document.workOrderDetailForm.submit();
}
/* 
function deleteProduct(prodno){
	alert('from deleteproduct  :'+prodno);
	document.forms['workOrderDetailForm'].reset();
	 location.reload(false); 
	$.ajax({
		type: "GET",
		url: "${pageContext.request.contextPath}/deleteproduct/" + prodno,	
		success: function(data){
			alert('success start ');
			  var tableClient=$("#reporttable").DataTable();
			  var rows = tableClient
			     .rows()
			     .remove()
			     tableClient.ajax.reload();
			 	 tableClient.draw(); 
			 	// document.forms['workOrderDetailForm'].reset();
			 	// location.reload(false); 
				alert('success end ');
			
		},
		
		error: function(e){
			alert("ERROR: ", e);
		}
			
	});
	
	
}
  */


function saveOrder(){
	var jobsize = ${jobSize};
	var isAssigned = false;
	for(var i=0; i<jobsize;i++){
		if($('#jobtitle_'+i).val()!= '0')
			isAssigned= true;
	}
	if(!isAssigned){
		alert("Please assign the job to atlease one employee");
	}else if($('#jobtitle_'+(jobsize-1)).val()== '0'){
		alert("Please assign the despatcher to complete the Job");
	}else{
		document.workOrderDetailForm.submit();
	}
	
}

function  addWorkOrder(){
	document.workOrderDetailForm.action="workorder";
	document.workOrderDetailForm.method="POST";
	document.workOrderDetailForm.submit();
}

function isNumberKey(evt)
{
   var charCode = (evt.which) ? evt.which : evt.keyCode;
   if (charCode != 46 && charCode > 31 && charCode < 48 || charCode > 57)
      return false;

   return true;
}

function dispercent(value){
	amount = $('#total').val();
	var dcgst =Math.round($('#cgst').val());
	var dsgst =Math.round($('#sgst').val()); 
	var discountAmt= Math.round((amount*value)/100);
	//alert("Disc Amount"+Math.round(discountAmt));
	$('#discount-1').val(Math.round(discountAmt));
	
	//var discount=Math.round($('#discount').val());
	
	discount=(dcgst+dsgst+discountAmt).toFixed(2);
	//alert("dicount GST"+discount);
	//discount=(dcgst+dsgst+discountAmt).toFixed(2);
	//alert("discount"+discount);
		//discount1=Math.round($('#discount-1').val());
		
	$('#discount').val(discount);
	totalcalc(0);
	//alert('hello');
			
}
/* 
function  viewWorkOrder(){
	document.workOrderDetailForm.action="viewworkorder";
	document.workOrderDetailForm.method="POST";
	document.workOrderDetailForm.submit();
}
 */
 /* function report(){
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
						obj.noofsheets > 0 ?"<input type='button' class='btn btn-danger' id='id_"+obj.prodNo+"' onclick='deleteProduct("+obj.prodNo+");' value='Delete'>":" ",
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
						"language": {
						 "infoEmpty":"No Articals to Invoice"
					    }
					});
				 }
			
		}	
	  },
	  'rowCallback': function(row, data, index){
		  if(data[0] !=''){
			$(row).find('td:eq(0)').css('color', 'red'); 
// 			$(row).find('td:eq(0)').css('font-weight', 'bold');    
			}
		  if(data[1] !=0){
			   	$(row).find('td:eq(1)').css('color', 'red');
// 				$(row).find('td:eq(1)').css('font-weight', 'bold'); 
			}else {
		        data[1]='';
		    }
				
		  if(data[2] != ''){
				$(row).find('td:eq(2)').css('color', 'red');
// 				$(row).find('td:eq(2)').css('font-weight', 'bold'); 
			}
		  if(data[3] !=0){
				$(row).find('td:eq(3)').css('color', 'red');
// 				$(row).find('td:eq(3)').css('font-weight', 'bold'); 
			}
			
		  if(data[4] != ''){
			 	$(row).find('td:eq(4)').css('color', 'Blue' ); 
// 			 	$(row).find('td:eq(4)').css('font-weight', 'bold'); 
			}
		  if(data[5] != ''){
				$(row).find('td:eq(5)').css('color', 'Blue');
// 				$(row).find('td:eq(5)').css('font-weight', 'bold'); 
			}
		  if(data[6] != ''){
				$(row).find('td:eq(6)').css('color', 'Blue');
// 				$(row).find('td:eq(6)').css('font-weight', 'bold'); 
			}
		  if(data[7] != ''){
				$(row).find('td:eq(7)').css('color', 'Blue');
// 				$(row).find('td:eq(7)').css('font-weight', 'bold'); 
			}
	  }
	  
	});
}

 */
 
</script>
</html>
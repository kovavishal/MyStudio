<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="staticcontent.jsp"%>
<title>Purchase</title>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="container-fluid" id="main">

		<div class="card">
			<div class="card-header text-center"
				style="padding-bottom: 0px; padding-top: 0px; background: skyblue; color: #30567D; font-weight: 625;">
				<jsp:include page="logout.jsp">
					<jsp:param name="page" value="Purchase" />
				</jsp:include>
			</div>
			<div class="card-body bg-light border-success mb-3 col-lg-12"
				style="color: navy;">
				<form:form action="purchase" method="POST" modelAttribute="purchase"
					name="purchaseform">
					<div class="form-row">
						<div class="form-group col-md-2 col-lg-2">
							<label for="inviceno">Invoice No</label>
							<form:input type="number" path="invoiceNumber"
								class="form-control" onblur="disableSave()" id="invoiceno"
								required="true" />
						</div>
						<div class="form-group col-md-2 col-lg-3">
							<label for="Invoicedate">Invoice Date</label>
							<form:input path="strInvoiceDate" class="form-control"
								id="invoicedate" required="true" />
						</div>
						<div class="form-group col-md-2 col-lg-3">
							<label for="supliername">Supplier Name</label>
							<form:select path="supId" class="form-control" id="supliername"
								required="true" onchange="setAdvanceAmount(this.value);">
								<option value="">--Supplier Name -</option>
								<c:forEach items="${suppliers}" var="supplier">
									<c:choose>
										<c:when test="${supplier.supId eq  purchase.supId}">
											<option value="${supplier.supId}" selected="selected">${supplier.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${supplier.supId}">${supplier.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
						</div>
						<div class="form-group col-md-2 col-lg-3">
							<input type="radio" name="form-control" id="interstate" value="0"
								onchange="showGst(this.value)">Inter State <input
								type="radio" name="form-control" id="intrastate" value="1"
								checked="checked" onchange="showGst(this.value)">Intera
							State
						</div>
					</div>
					<div class="form-row">
						<div class="form-group col-md-2 col-lg-2">
							<label for="gstno">GST No</label>
							<form:input path="gstNo" class="form-control" id="gstno"
								readonly="true" />
						</div>

						<div class="form-group col-md-2 col-lg-2" id="sgst">
							<label for="sgstamount">SGST Amount</label>
							<form:input path="sgstAmount" class="form-control"
								id="sgstamount" value="0" onkeypress='validate(event)' />
						</div>
						<div class="form-group col-md-2 col-lg-2" id="cgst">
							<label for="cgstamount">CGST Amount</label>
							<form:input path="cgstAmount" class="form-control"
								id="cgstamount" value="0" onkeypress='validate(event)' />
						</div>
						<div class="form-group col-md-2 col-lg-2" style="display: none;"
							id="igst">
							<label for="igstamount">IGST amount</label>
							<form:input path="igstAmount" class="form-control"
								id="igstamount" value="0" onkeypress='validate(event)' />
						</div>
						<div class="form-group col-md-2 col-lg-2" id="advancediv">
							<label for="igstamount">Advance</label>
							<form:input path="advance" class="form-control" id="advance"
								readonly="true" />
						</div>


						<div class="form-group col-md-2 col-lg-2">
							<label for="invoiceamount">Invoice Amount</label>
							<form:input path="invoiceAmount" class="form-control"
								id="invoiceamnount" required="true" onkeypress='validate(event)'
								value="0" />
						</div>
					</div>
					<div class="form-row">
						<div class="form-group col-md-2 col-lg-2">
							<label for="discount">Discount</label>
							<form:input path="discount" class="form-control" id="discount"
								onkeypress='validate(event)' onblur="applyDiscount();" />
						</div>
						<%-- <div class="form-group col-md-2 col-lg-2">
	      <label for="roundoff">Round off</label>
	      <form:input path="roundOff" class="form-control" id="roundoff"/>
	    </div> --%>
						<div class="form-group col-md-2 col-lg-2">
							<label for="netpayable">Net Payable</label>
							<form:input path="netPayable" class="form-control"
								id="netpayable" readonly="true" />
						</div>
						<!-- </div>	
	<div class="form-row"> -->
						<div class="form-group col-md-2 col-lg-2">
							<label for="paid">Amount Paid</label>
							<form:input path="amountPaid" class="form-control" id="paid"
								value="0" onkeypress='validate(event)'
								onblur="calcBalance(this);" />
						</div>
						<div class="form-group col-md-2 col-lg-2">
							<label for="balance">Balance Payable</label>
							<form:input path="balancePayable" class="form-control"
								onblur="enableSave(id)" id="balance" readonly="true" />
						</div>
						<%-- <div class="form-group col-md-2 col-lg-3">
		 <label for="pType">Payment Type</label>
			<form:select  path="paymentId" class="form-control" id="paymentId" required="true" onchange="showBankDetails()">
	      		<option value="">--Payment Type--</option>
		        <c:forEach items="${paymentTypes}" var="paymentType">
		    	<c:choose>
		    		<c:when test="${paymentType.id eq  purchase.paymentId}">
		    			<option value="${paymentType.id}" selected="selected">${paymentType.name}</option>
		    		</c:when>
		    		<c:otherwise>
		    			<option value="${paymentType.id}">${paymentType.name}</option>
		    		</c:otherwise>
		    	</c:choose>
		    	</c:forEach>
	      	</form:select>	
	   </div> --%>
						<div class="form-group col-md-2 col-lg-2">
							<label for="Invoicedate">Goods Received Date</label>
							<form:input path="strGoodsReceivedDate" class="form-control"
								id="goodsRecdDate" required="true" />
						</div>
					</div>
					<div class="form-row" id="bankdetails" style="display: none;">
						<div class="form-group col-md-2 col-lg-2">
							<label for="chqno">Cheque No</label>
							<form:input path="chequeNo" class="form-control" id="chqno" />
						</div>
						<div class="form-group col-md-2 col-lg-3">
							<label for="chqdate">Cheque Date</label>
							<form:input path="strChequeDate" class="form-control"
								id="chqdate" />
						</div>
						<div class="form-group col-md-2 col-lg-3">
							<label for="bankname">Bank Name</label>
							<form:input path="bankName" class="form-control" id="bankname" />
						</div>
					</div>

					<div class="row ">
						<div class="col-sm-8 col-lg-10">
							<div class="card bg-light  mb-3" style="color: navy;">
								<div id="table" class="table-editable"
									style="height: 320px; overflow: scroll;">
									<table
										class="table table-bordered table-responsive-md table-striped text-center order-list"
										id="productitemtable">
										<thead>
											<div>
												<tr>
													<div col-lg-3>

														<th width="10" class="text-center">Particulars</th>
													</div>


													<th class="text-center" nowrap="nowrap">Batch No</th>

													<!-- <th class="text-center" nowrap="nowrap">GST rate</th> -->

													<th class="text-center">Quantity</th>


													<th class="text-center" maxlimit="100"
														cssStyle="Width:100%" nowrap="nowrap">Rate of per
														Item</th>


													<th class="text-center" nowrap="nowrap">Amount of Item</th>

												</tr>
											</div>
										</thead>
										<c:forEach items="${purchasePayments}" var="purchasePayment"
											varStatus="loop">
											<tr>
												<td class="col-sm-2"><form:select
														path="purchasePayments[${loop.index }].purchaseTypeId"
														class="form-control" id="itemname" maxlength="10">
														<c:forEach items="${purchaseItems}" var="pItem">
															<c:choose>
																<c:when
																	test="${pItem.id eq  purchase.purchasePayments[loop.index].purchaseTypeId}">
																	<option value="${pItem.id}" selected="selected">${pItem.name}</option>
																</c:when>
																<c:otherwise>
																	<option value="${pItem.id}">${pItem.name}</option>
																</c:otherwise>
															</c:choose>
														</c:forEach>
													</form:select></td>
												<td class="col-sm-8"><form:input
														path="purchasePayments[${loop.index}].batchNo"
														class="form-control" value="0"
														onkeypress='validate(event)' id="batchno_${loop.index}"></form:input>
												</td>
												<%-- 	          <td class="pt-3-half">
					          <form:input path="purchasePayments[${loop.index}].gstRate" class="form-control" id="gstrate_${loop.index}"></form:input>
					          </td>
				 --%>
												<td class="col-sm-8"><form:input
														path="purchasePayments[${loop.index}].qty"
														class="form-control" id="qty_${loop.index}" value="0"
														onkeypress='validate(event)'
														onblur="calcAmount(${loop.index});"></form:input></td>
												<td class="col-sm-8"><form:input
														path="purchasePayments[${loop.index}].rate"
														class="form-control" id="rate_${loop.index}" value="0"
														onkeypress='validate(event)'
														onblur="calcAmount(${loop.index});"></form:input></td>
												<td class="col-sm-8"><form:input
														path="purchasePayments[${loop.index}].amount"
														class="form-control" id="amount_${loop.index}"
														cssStyle="width:100%"></form:input></td>
											</tr>
										</c:forEach>
									</table>
								</div>
							</div>
						</div>
					</div>
					<div class="form-row text-right col-lg-12">
						<div class="form-group col-md-2 col-lg-12 text-center">
							<a href="<%= request.getContextPath()%>/payment/purchase"
								class="btn btn-info" style="width: 300px; font-size: 20px"
								role="button"><i class="fa fa-refresh"
								style='font-size: 20px; padding: 0 1em 0 0'></i>Refresh</a>

							<!-- <button type="submit" class="btn btn-success" style="width: 100px;">Save </button> -->

							<button type="submit" class="btn btn-success" id="save"
								style="width: 300px; font-size: 20px">
								<i class="fa fa-save"
									style='font-size: 20px; padding: 0 1em 0 0'></i>Save
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
function applyDiscount(){
	var total = $('#invoiceamnount').val();
	var discountAmount=$('#discount').val();
	/* var mcgst= $('#cgstamount').val();
	var migst= $('#igstamount').val();
	var msgst= $('#sgstamount').val(); */

	/* total=(parseFloat(total)+parseFloat(mcgst) +parseFloat(msgst)+parseFloat(migst)); */
	
    if(discountAmount != ''){
		//discountAmount = 0;
		if(parseFloat(discountAmount) < parseFloat(total)){
			total = (parseFloat(total) - parseFloat(discountAmount))
			$('#netpayable').val(total.toFixed(2));
			$('#balance').val(total.toFixed(2));
		}else{
			alert("Discount amount cann't grater than invoice amount");
			$('#discount').val(0);
			$('#netpayable').val(0);
		}
	}else{
		$('#netpayable').val(parseFloat(total).toFixed(2));
		$('#balance').val(parseFloat(total).toFixed(2));
	}
}

function calcBalance(amount){
	var payable = $('#netpayable').val();
	var advance = $('#advance').val();
	console.log("advance",advance);
	console.log("amount",amount.value);
	if(amount.value != ''){
		if(parseFloat(amount.value) <= parseFloat(payable)){
			if(payable){
				var balance =(parseFloat(payable) - parseFloat(amount.value));
				
				if(advance!='' && advance!=0)
					balance = parseFloat(balance)-parseFloat(advance);
				
				$('#balance').val(balance.toFixed(2));
			}
		}else{
			
			alert("You are not allowed to pay more than payable amount")
			amount.value=0;
			$('#balance').val(0);
		}
	}
}


function showGst(location){
	console.log("selected location" , location);
	if(location != '0'){
		console.log("location","interstate");
		$("#sgst").val(0);
		$("#cgst").val(0);
		$("#igst").val(0);
		$("#invoiceamount").val(0);
		
		$("#sgst").css("display", "inline");
		$("#cgst").css("display", "inline");
		$("#igst").css("display", "none");
		
	}else{
		$("#sgst").val(0);
		$("#cgst").val(0);
		$("#igst").val(0);
		$("#invoiceamount").val(0);
		$("#sgst").css("display", "none");
		$("#cgst").css("display", "none");
		$("#igst").css("display", "inline");
		
	}
}

function showBankDetails(trxnType){
	var payType= $("#paymentId option:selected").text();
	console.log("payment mode" + payType);
	if(payType == 'Cheque')
		$("#bankdetails").css("display", "flex");
	else
		$("#bankdetails").css("display", "none");
}

$(function () {
	
	$('#invoicedate').datetimepicker({
		format: 'DD/MM/YYYY',
		icons: {
            time: "fa fa-clock-o",
            date: "fa fa-calendar",
            up: "fa fa-arrow-up",
            down: "fa fa-arrow-down",
            autoclose: true
        }
	
    });
	
	$('#chqdate').datetimepicker({
		format: 'DD/MM/YYYY',
		icons: {
            time: "fa fa-clock-o",
            date: "fa fa-calendar",
            up: "fa fa-arrow-up",
            down: "fa fa-arrow-down",
            autoclose: true
        }
	
    });
	
	$('#goodsRecdDate').datetimepicker({
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

function calcAmount(index){
	var qty = $('#qty_'+index).val();
	var amount =$('#rate_'+index).val();
	if(qty!='' && amount!=''){
		amount = (parseFloat(qty) * parseFloat(amount));
	}
	if(amount!='')
		$('#amount_'+index).val(parseFloat(amount).toFixed(2));
	else
		$('#amount_'+index).val('');
}

function setAdvanceAmount(supId){
	$.getJSON('${pageContext.request.contextPath}/payment/advance/'+supId , 
			function (data) {
		console.log(data);
			$('#advance').val(data.paidAmount);
			$('#gstno').val(data.gstNo);
	});
 }

function validate(evt) {
	  var theEvent = evt || window.event;

	  // Handle paste
	  if (theEvent.type === 'paste') {
	      key = event.clipboardData.getData('text/plain');
	  } else {
	  // Handle key press
	      var key = theEvent.keyCode || theEvent.which;
	      key = String.fromCharCode(key);
	  }
	  var regex = /[0-9]|\./;
	  if( !regex.test(key) ) {
	    theEvent.returnValue = false;
	    if(theEvent.preventDefault) theEvent.preventDefault();
	  }
	}
	function enableSave(id){
		
    if ((document.getElementById('balance').value) >= 0){
    	 document.getElementById('save').style.visibility = 'visible';
		}
	}

	function disableSave(){
		
		 document.getElementById('save').style.visibility = 'hidden';
		}
	$(document).ready(function() {
		document.getElementById('save').style.visibility = 'hidden';
	});
		

</script>
</html>
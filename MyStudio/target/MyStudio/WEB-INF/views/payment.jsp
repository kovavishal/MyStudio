<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="staticcontent.jsp"%>
<title>Payment</title>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="container-fluid" id="main">
		<div class="card">
			<div class="card-header text-center"
				style="padding-bottom: 0px; padding-top: 0px; background: skyblue; color: #30567D; font-weight: 625;">
				<jsp:include page="logout.jsp">
					<jsp:param name="page" value="Payment" />
				</jsp:include>
			</div>
			<div class="card-body bg-light border-success mb-3 col-lg-12"
				style="color: navy;">
				<form:form action="save" method="POST" modelAttribute="voucher"
					name="paymentform">
					<div class="form-row">
						<div class="form-group col-md-2 col-lg-2">
							<label for="voucherid">Voucher No</label>
							<form:input path="voucherId" class="form-control" id="voucherid"
								readonly="true" />
						</div>
						<div class="form-group col-md-2 col-lg-2">
							<label for="vdate">Voucher Date</label>
							<form:input path="strVoucherDate" class="form-control" id="vdate"
								required="true" />
						</div>
						<div class="form-group col-md-2 col-lg-3">
							<label for="acchead">Account Head</label>
							<form:select path="expenseId" class="form-control" id="expenseId"
								required="true" onchange="supplierDetails(this.value);">
								<option value="">--Account Head--</option>
								<c:forEach items="${expenseTypes}" var="expenseType">
									<c:choose>
										<c:when test="${expenseType.id eq  voucher.expenseId}">
											<option value="${expenseType.id}" selected="selected">${expenseType.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${expenseType.id}">${expenseType.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
						</div>

						<div class="form-group col-md-2 col-lg-3">
							<label for="transactiontype">Transaction Type</label>
							<form:select path="strPaymentId" class="form-control"
								id="paymentId" required="true"
								onclick="showBankDetails(this.value);">
								<option value="">--Transaction Type--</option>
								<c:forEach items="${paymentTypes}" var="paymentType">
									<c:choose>
										<c:when test="${paymentType.id eq  voucher.paymentId}">
											<option value="${paymentType.id}" selected="selected">${paymentType.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${paymentType.id}">${paymentType.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
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

					<div id="table" class="table-editable">
						<div class="form-row">
							<div class="form-group col-md-2 col-lg-11">
								<c:if test="${ not empty invoiceNumbers}">
									<table
										class="table table-bordered table-responsive-md table-striped text-center"
										id="reftable">
										<tr>
											<th class="text-center">Reference type</th>
											<th class="text-center">Reference No</th>
											<th class="text-center">Net Amount</th>
											<th class="text-center">Amount Paid</th>
											<th class="text-center">Balance Payable</th>
										</tr>

										<c:forEach items="${voucher.voucherPayments}" var="vPayment"
											varStatus="loop">
											<tr>
												<td><form:select
														path="voucherPayments[${loop.index}].refTypeId"
														class="form-control" id="vPayment_${loop.index}"
														required="true" onchange="updateRefNo(${loop.index});">
														<option value="">Reference Type</option>
														<c:forEach items="${voucherTypes}" var="vType">
															<option value="${vType.id}">${vType.name}</option>
															<%-- <c:choose>
							<c:when test="${vType.id eq  vPayment.refTypeId}">
								<option selected value="${vType.id}">${vType.name}</option>
							</c:when>
							<c:otherwise>
								<option value="${vType.id}">${vType.name}</option>
							</c:otherwise>
						</c:choose> --%>
														</c:forEach>
													</form:select></td>
												<td><form:select
														path="voucherPayments[${loop.index}].refNo"
														class="form-control" id="reference_${loop.index}"
														onblur="setInvoiceAmount(${loop.index});">
														<option value="0">Reference</option>
														<c:forEach items="${invoiceNumbers}" var="invoiceNum">
															<option value="${invoiceNum}">${invoiceNum}</option>
															<%-- <c:choose>
							<c:when test="${invoiceNum eq  vPayment.refTypeId}">
								<option selected value="${invoiceNum}">${invoiceNum}</option>
							</c:when>
							<c:otherwise>
								<option value="${invoiceNum}">${invoiceNum}</option>
							</c:otherwise>
						</c:choose> --%>
														</c:forEach>
													</form:select></td>
												<td class="pt-3-half"><form:input
														path="voucherPayments[${loop.index}].netAmount"
														class="form-control" id="net_${loop.index}"
														disabled="true" /></td>
												<td class="pt-3-half"><form:input
														path="voucherPayments[${loop.index}].paidAmount"
														class="form-control" id="paid_${loop.index}"
														disabled="true" /></td>
												<td class="pt-3-half"><form:input
														path="voucherPayments[${loop.index}].balance"
														class="form-control" id="balance_${loop.index}"
														disabled="true" /></td>
											</tr>
										</c:forEach>
										<%--  <c:otherwise>
        	<c:forEach items="${voucher.voucherPayments}" var="mPayment" varStatus="loop">
        	<tr>
			<td>
				<form:select path="voucherPayments[${loop.index }].refTypeId" class="form-control" id="refTypeId_${loop.index}">
					<option  value="" selected>--Type--</option>
					<option value="1">Advance</option>
					<option value="2">Miscellaneous</option>
				</form:select>		
			</td>	
			<td>
				<form:select path="voucherPayments[${loop.index}].refNo" class="form-control" id="reference_${loop.index}">
					<option selected value="">--Reference--</option>
					<option value="1">--Value 1--</option>
				</form:select>		
			</td>
			<td class="pt-3-half"><form:input path="voucherPayments[${loop.index}].netAmount" class="form-control" id="net_${loop.index}" disabled="true"/></td>
			<td class="pt-3-half"><form:input path="voucherPayments[${loop.index}].paidAmount" class ="form-control" id="paid_${loop.index}" disabled="true"/></td>
        </tr>
        </c:forEach>
        </c:otherwise> --%>
									</table>
								</c:if>

							</div>
						</div>
						<div class="form-row">
							<c:if test="${ not empty invoiceNumbers}">
								<div class="form-group col-md-2 col-lg-2">
									<label for="supCrdtBalance">Current Balance</label>
									<form:input path="creditBalance" class="form-control"
										id="supCrdtBalance" readonly="true" />
								</div>
							</c:if>
							<div class="form-group col-md-2 col-lg-2">
								<label for="payment">Current Payment</label>
								<form:input path="strPayAmount" class="form-control"
									id="payment" required="true"
									onblur="calcBalance(this);addText(this.value);" />
							</div>
							<c:if test="${ not empty invoiceNumbers}">
								<div class="form-group col-md-2 col-lg-2">
									<label for="balpayable">Balance Payable</label>
									<form:input path="balanceAmount" class="form-control"
										id="balpayable" required="true" />
								</div>
							</c:if>
						</div>

						<div class="form-row">
							<div class="form-group col-md-2 col-lg-9">
								<label for="naration">Naration</label>
								<form:input path="naration" class="form-control" id="naration" />
							</div>
						</div>
					</div>


					<div class="form-row text-right">
						<div class="form-group col-md-2 col-lg-9 text-right">
							<a href="<%= request.getContextPath()%>/payment/pay"
								class="btn btn-info" style="width: 300px; font-size: 20px"
								role="button"> <i class="fa fa-refresh"
								style='font-size: 20px; padding: 0 1em 0 0'></i>Refresh
							</a>
							<!-- <button type="button" class="btn btn-primary">Print</button> -->

							<button type="submit" class="btn btn-success"
								style="width: 300px; font-size: 20px">
								<i class="fa fa-inr" style='font-size: 20px; padding: 0 1em 0 0' onSubmit()="callOnSubmit()"></i>Pay
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

$(document).ready(function() {
	showBankDetails($('#paymentId').val());
});

function supplierBalance(supplierId){
	if(!supplierId.startsWith("1000")){
		$.getJSON('${pageContext.request.contextPath}/payment/supplier/'+supplierId , function (data) {
			$('#cbalance').val(data.creditBalance);
		});
	}else{
		$('#cbalance').val("");
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
function supplierDetails(supplierId){
		if($('#expenseId').val()!=""){
			document.paymentform.action="${pageContext.request.contextPath}/payment/details";
			document.paymentform.method="POST";
			document.paymentform.submit();
		}
}
$(function () {
	
	$('#vdate').datetimepicker({
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
	
});
function calcBalance(paymentObj){
	var creditbalance = $('#supCrdtBalance').val();
	console.log("credit balance", creditbalance);
	if(creditbalance && paymentObj.value!=''){
		creditbalance = parseFloat(creditbalance).toFixed(2);
		var amount = parseFloat(paymentObj.value).toFixed(2);
		console.log("amount to be paid",paymentObj.value);
		if(parseFloat(paymentObj.value) <= parseFloat(creditbalance)){
			$('#balpayable').val(parseFloat(creditbalance-amount).toFixed(2));
		}/* else{
			alert("You are not allowed to pay more than current balance");
			paymentObj.value=0;
			$('#balpayable').val(0);
		} */
			
	}
	
}
function addText(amount){
	var payType= $("#expenseId option:selected").text();
	var refNo = $('#reference_0').val();
	    
	$('#naration').val("Paid Rs."+amount+" towards "+payType.toLowerCase()+" "+(refNo == undefined ? '':refNo != 0? ' Ref No '+refNo:''));
}
function setInvoiceAmount(index){
	var supId = $('#expenseId').val();
	var invoiceNum = $('#reference_'+index).val();
	$.getJSON('${pageContext.request.contextPath}/payment/invoice/'+invoiceNum+"/"+supId , function (data) {
			$('#net_'+index).val(data.invoiceAmount);
			$('#paid_'+index).val(data.amountPaid);
			$('#balance_'+index).val(data.balancePayable);
	});
 }
 function updateRefNo(index){
	var refNoTxt= $("#vPayment_"+index+" option:selected").text();
	var refNoTxt= $("#reference_"+index+" option:selected").text();
	if(refNoTxt != 'Invoice'){
		$("#reference_"+index).val("0");
		$("#net_"+index).val("");
		$("#paid_"+index).val("");
		$("#balance_"+index).val("");
	}
	
}
 function callOnSubmit(){
	 alert ("hello");
 }

</script>
</html>
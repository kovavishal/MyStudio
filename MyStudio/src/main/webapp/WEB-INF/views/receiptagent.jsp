<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="staticcontent.jsp"%>
<title>Receipt</title>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="container-fluid" id="main">
		<div class="card">
			<div class="card-header text-center"
				style="padding-bottom: 0px; padding-top: 0px; background: skyblue; color: #30567D; font-weight: 625;">
				<jsp:include page="logout.jsp">
					<jsp:param name="page" value="Receipt" />
				</jsp:include>
			</div>
			<div class="card-body bg-light border-success mb-3 col-lg-12"
				style="color: navy;">
				<form:form action="agentreceipt" method="POST"
					modelAttribute="receipt" name="receiptform" />
				<form:hidden path="custId" id="cid" />
				<div class="form-row">
					<div class="form-group col-md-2 col-lg-2">
						<label for="receiptid">Receipt No</label>
						<form:input path="receiptId" class="form-control" id="receiptid"
							readonly="true" />
					</div>
					<div class="form-group col-md-2 col-lg-2">
						<label for="receiptdate">Receipt Date</label>
						<form:input path="strReceiptDate" class="form-control"
							id="receiptdate" readonly="true" />
					</div>


					<div class="form-group col-md-2 col-lg-2">
						<label for="transactiontype">Transaction Type</label>
						<form:select path="strPaymentId" class="form-control"
							id="paymentId" required="true"
							onclick="showBankDetails(this.value);">
							<option value="">-- select --</option>
							<c:forEach items="${paymentTypes}" var="paymentType">
								<c:choose>
									<c:when test="${paymentType.id eq  receipt.paymentId}">
										<option value="${paymentType.id}" selected="selected">${paymentType.name}</option>
									</c:when>
									<c:otherwise>
										<option value="${paymentType.id}">${paymentType.name}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</form:select>
					</div>
					<div class="form-group col-md-2 col-lg-2">
						<label for="customername">Reference Type</label>
						<form:select path="strExpenseId" class="form-control"
							id="expenseId" required="true" onchange="showCustTxt(this);">
							<option value="">-- select --</option>
							<c:forEach items="${receiptTypes}" var="receiptType">
								<c:choose>
									<c:when test="${receiptType.id eq  receipt.expenseId}">
										<option value="${receiptType.id}" selected="selected">${receiptType.name}</option>
									</c:when>
									<c:otherwise>
										<option value="${receiptType.id}">${receiptType.name}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</form:select>
					</div>

					<div class="form-group col-md-2 col-lg-3" id="cname">
						<label for="custname">Search Customer</label>
						<form:input path="custName" class="form-control" id="custname" />
					</div>

				</div>

				<%-- <div class="form-row">
			<div class="form-group col-md-2 col-lg-2">
				<label for="transactiontype">Transaction Type</label>
				<form:select path="strPaymentId" class="form-control" id="paymentId" required="true" onclick="showBankDetails(this.value);">
	      		<option value="">-- select --</option>
		        <c:forEach items="${paymentTypes}" var="paymentType">
		    	<c:choose>
		    		<c:when test="${paymentType.id eq  receipt.paymentId}">
		    			<option value="${paymentType.id}" selected="selected">${paymentType.name}</option>
		    		</c:when>
		    		<c:otherwise>
		    			<option value="${paymentType.id}">${paymentType.name}</option>
		    		</c:otherwise>
		    	</c:choose>
		    	</c:forEach>
	      		</form:select>
			</div>
			<div class="form-group col-md-2 col-lg-2">
				<label for="customername">Reference Type</label>
				<form:select path="strExpenseId" class="form-control" id="expenseId" required="true" onchange="showCustTxt(this);">
	      		<option value="">-- select --</option>
	      		<c:forEach items="${receiptTypes}" var="receiptType">
		    	<c:choose>
		    		<c:when test="${receiptType.id eq  receipt.expenseId}">
		    			<option value="${receiptType.id}" selected="selected">${receiptType.name}</option>
		    		</c:when>
		    		<c:otherwise>
		    			<option value="${receiptType.id}">${receiptType.name}</option>
		    		</c:otherwise>
		    	</c:choose>
		    	</c:forEach>
	      		</form:select>
			</div>
			<div class="form-group col-md-2 col-lg-3" id="cname">
		      <label for="custname">Search Customer</label>
		      <form:input path="custName" class="form-control" id="custname" />
		    </div>
		</div> --%>

				<div class="form-row" id="bankdetails" style="display: none;">
					<div class="form-group col-md-2 col-lg-2">
						<label for="chqno">Cheque No</label>
						<form:input path="chequeNo" class="form-control" id="chqno" />
					</div>
					<div class="form-group col-md-2 col-lg-3">
						<label for="chqdate">Cheque Date</label>
						<form:input path="strChequeDate" class="form-control" id="chqdate" />
					</div>
					<div class="form-group col-md-2 col-lg-3">
						<label for="bankname">Bank Name</label>
						<form:input path="bankName" class="form-control" id="bankname" />
					</div>
				</div>

				<%-- <div id="table" class="table-editable" >
			<div class="form-row">
			  <div class="form-group col-md-2 col-lg-11">
			  <c:if test="${not empty orderIds }">	
		      <table class="table table-bordered table-responsive-md table-striped text-center" >
		        <tr>
				  <th class="text-center">Reference type</th>
		          <!-- <th class="text-center">Reference No</th> -->
		          <th class="text-center">Net Amount</th>
		          <th class="text-center">Amount Paid</th>
		          <th class="text-center">Amount Payable</th>
				</tr>
		        
		        <c:forEach items="${receipt.receiptPayments}" var="vPayment" varStatus="loop">
		        <tr>
					<td>
						<form:select path="receiptPayments[${loop.index}].refTypeId" class="form-control" id="vPayment_${loop.index}" required="true">
							<option  value="">Reference Type</option>
							<c:forEach items="${voucherTypes}" var="vType">
							<option value="${vType.id}">${vType.name}</option>
							</c:forEach>
						</form:select>		
					</td>	
					<td>
					<form:select path="receiptPayments[${loop.index}].refNo" class="form-control" id="reference_${loop.index}" required="true" onchange="setOrderAmount(${loop.index});">
						<option value="">Order Id</option>
						<c:forEach items="${orderIds}" var="orderId">
							<option value="${orderId}">${orderId}</option>
						</c:forEach>
					</form:select>		
					</td>
					<td class="pt-3-half"><form:input path="receiptPayments[${loop.index}].netAmount" class="form-control" id="net_${loop.index}" readonly="true"/></td>
					<td class="pt-3-half"><form:input path="receiptPayments[${loop.index}].paidAmount" class ="form-control" id="paid_${loop.index}" readonly="true"/></td>
					<td class="pt-3-half"><input type="text" class ="form-control" id="advance_${loop.index}" id="advance" readonly="readonly"/></td>
					<td class="pt-3-half"><input type="text" class ="form-control" id="payable_${loop.index}" readonly="readonly"/></td>
		        </tr>
		        </c:forEach>
		      </table>
			  </c:if>
			--%>
				<div class="form-row">
					<c:if test="${not empty orderIds }">
						<div class="form-group col-md-2 col-lg-2">
							<label for="balance">Total Outstanding</label>
							<form:input path="custBalance" class="form-control" id="balance"
								readonly="true" />
						</div>
					</c:if>
					<div class="form-group col-md-2 col-lg-2">
						<label for="payment">Current Payment</label>
						<form:input path="strPayAmount" class="form-control" id="payment"
							onblur="calcBalance(this);addText(this.value);" required="true" />
					</div>
					<c:if test="${not empty orderIds }">
						<div class="form-group col-md-2 col-lg-2">
							<label for="balpayable">Balance Payable</label>
							<form:input path="balanceAmount" class="form-control"
								id="balpayable" readonly="true" />
						</div>
					</c:if>
				</div>


				<div class="form-row">

					<div class="form-group col-md-2 col-lg-10">
						<label for="naration">Naration</label>
						<form:input path="naration" class="form-control" id="naration" />
					</div>

				</div>
				<div class="form-row text-right">
					<div class="form-group col-md-2 col-lg-9">
						<a href="<%= request.getContextPath()%>/payment/receipt"
							class="btn btn-info" role="button"
							style="width: 300px; font-size: 20px"><i
							class="fa fa-refresh" style='font-size: 20px; padding: 0 1em 0 0'></i>Refresh</a>
						<!-- <button type="button" class="btn btn-primary">Print</button> -->
						<!-- <button type="submit" class="btn btn-success">Save</button> -->



						<button type="submit" class="btn btn-success"
							style="width: 300px; font-size: 20px">
							<i class="fa fa-inr" style='font-size: 20px; padding: 0 1em 0 0'></i>Confirm
							Receipt
						</button>
					</div>
				</div>

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
$(document).ready(function(){

	showBankDetails($('#paymentId').val());
	
	$('#custname').autocomplete({
		serviceUrl: '${pageContext.request.contextPath}/payment/customers',
		paramName: "searchTerm",
		delimiter: ",",
		minChars:1,		
	   	transformResult: function(response) {
			return {      	
			  suggestions: $.map($.parseJSON(response), function(item) {
			      return { value: item.name, data: item.id };
			   })
			 };
       	},
      	onSelect:function(item){
    	   	$('#cid').val(item.data);
    	   	document.receiptform.action="orderdetail";
    	   	document.receiptform.method="POST";
    	   	document.receiptform.submit();
     	}
			
	});
	
})


function showBankDetails(trxnType){
	var payType= $("#paymentId option:selected").text();
	console.log("payment mode" + payType);
	if(payType == 'Cheque')
		$("#bankdetails").css("display", "flex");
	else
		$("#bankdetails").css("display", "none");
}
$(function () {
	
	$('#receiptdate').datetimepicker({
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
	var creditbalance = $('#balance').val();
	var expenseId = $('#expenseId').val();
	var payable = $('#payable_0').val();
	 payable = parseFloat(payable).toFixed(2);
	var amount = parseFloat(paymentObj.value).toFixed(2);
	/* alert("amount :"+amount);
	alert("Payable :"+payable); */
	//alert("expenseId :"+expenseId);
	 if(expenseId == '101' && parseFloat(amount) > parseFloat(payable)){
		alert("You are not allowed to pay for the selected order");
		$('#payment').val(0);
		$('#balpayable').val(creditbalance);
		}else{
		$('#balpayable').val(parseFloat(creditbalance-amount).toFixed(2));
		}
}
	

function showCustTxt(receiptyTye){
	var rType= $("#expenseId option:selected").text();
	console.log("rytpe " , rType);
	 if(rType != 'Order'){
		$('#cid').val(0);
	   	document.receiptform.action="orderdetail";
	   	document.receiptform.method="POST";
	   	document.receiptform.submit();
	}else{ 
		$("#cname").css("display", "inline");
	 } 
}
function setOrderAmount(index){
	var custId = $('#cid').val();
	var orderId = $('#reference_'+index).val();
	$.getJSON('${pageContext.request.contextPath}/payment/receipt/'+orderId+"/"+custId , function (data) {
		console.log(data);
			$('#net_'+index).val(parseFloat(data.netAmount).toFixed(2));
			$('#paid_'+index).val(parseFloat(data.advance).toFixed(2));
			$('#payable_'+index).val(parseFloat(data.balance).toFixed(2));
	});
}
function addText(amount){
	var payType= $("#expenseId option:selected").text();
	var ref = $('#reference_0').val();
	$('#naration').val("The amount being received Rs."+amount+" towards "+payType.toLowerCase()+" "+(ref == undefined ? '' : ref));
}
function saveReceipt(){
	var expenseId = $('#expenseId').val();
	var amount = $('#payment').val();
	var payable = $('#payable_0').val();
	payable = parseFloat(payable).toFixed(2);
	amount = parseFloat(amount).toFixed(2);
	if(expenseId == '101' && parseFloat(amount) <= parseFloat(payable)){
		return true;
	}else{
		alert("You are not allowed to pay the selected order amount payable");
		return false;
	}
   
}
</script>
</html>
<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="staticcontent.jsp"%>
<title>Voucher</title>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="container-fluid" id="main">
		<div class="card">
			<div class="card-body bg-light border-success mb-3 col-lg-12"
				style="color: navy;">
				<form:form method="POST" action="addvoucher"
					modelAttribute="voucher" name="voucherForm">
					<c:set var="tabIndex" value="0" />
					<c:forEach items="${voucher.vouchers}" var="voucher"
						varStatus="loop">

						<div class="form-row">
							<div class="form-group col-md-2 col-lg-2">
								<label for="voucherid_${loop.index}">Voucher ID</label>
								<form:input path="vouchers[${loop.index}].voucherId"
									readonly="true" class="form-control"
									id="voucherid_${loop.index}" />
							</div>
							<c:set var="tabIndex" value="${tabIndex+1}" />
							<div class="form-group col-md-2 col-lg-2">
								<label for="expenseType_${loop.index}">Expense Type</label>
								<form:select path="vouchers[${loop.index}].expenseTypeId"
									class="form-control" id="expenseType_${loop.index}"
									tabindex="${tabIndex}">
									<c:forEach items="${expenseTypes}" var="expenseType">
										<c:choose>
											<c:when
												test="${expenseType.id eq  vouchers[loop.index].expenseTypeId}">
												<option value="${expenseType.id}" selected="selected">${expenseType.name}</option>
											</c:when>
											<c:otherwise>
												<option value="${expenseType.id}">${expenseType.name}</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</form:select>
							</div>
							<c:set var="tabIndex" value="${tabIndex+1}" />
							<div class="form-group col-md-2 col-lg-2">
								<label for="amount_${loop.index }">Amount</label>
								<form:input path="vouchers[${loop.index}].amount"
									class="form-control" id="amount_${loop.index}"
									tabindex="${tabIndex}" />
							</div>
							<c:set var="tabIndex" value="${tabIndex+1}" />
							<div class="form-group col-md-2 col-lg-3">
								<label for="voucherdate_${loop.index }">Voucher Date</label>
								<form:input path="vouchers[${loop.index }].vDate"
									class="form-control" id="voucherdate_${loop.index }"
									tabindex="${tabIndex}" />
							</div>
						</div>
					</c:forEach>
					<c:set var="tabIndex" value="${tabIndex+1}" />
					<div class="form-row" align="left">
						<div align="center">
							<button type="submit" class="btn btn-primary"
								tabindex="${tabIndex}">Save</button>
						</div>
					</div>
				</form:form>
			</div>
		</div>
		<div class="card-footer">All right reserved</div>
	</div>
</body>
<script type="text/javascript">

$(function () {
	var rows = ${voucher.vouchers.size()};
	for(var index = 0; index < rows ;index++){
		console.log("index : " + index);
		$('#voucherdate_'+index).datetimepicker({
			format: 'DD/MM/YYYY hh:mm A',
			icons: {
	            time: "fa fa-clock-o",
	            date: "fa fa-calendar",
	            up: "fa fa-arrow-up",
	            down: "fa fa-arrow-down",
	            autoclose: true
	        }
		
	    });
	}
	
});


</script>
</html>
<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/bootstrap.min.css">
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<title>Work Order</title>
</head>

<body>
	<tr class="form-group">
		<td style="width: 200px;"><form:select
				path="orders[0].products[0].productItems[${index}].prodItemTypeId"
				class="form-control match-content" id="productitem_${index}">
			</form:select></td>
		<td><form:input
				path="orders[0].products[0].productItems[${index}].quantity"
				class="form-control" id="customername_${index}" placeholder="Qty"
				onblur="setAmount(this.value,${loop.index})" /></td>
		<td><form:input
				path="orders[0].products[0].productItems[${index}].rate"
				class="form-control" id="rate_${index}" placeholder="Rate" /></td>
		<td><form:input
				path="orders[0].products[0].productItems[${index}].amount"
				class="form-control" id="amount_${index}" placeholder="Amount" /></td>
		<td><form:input
				path="orders[0].products[0].productItems[${index}].remarks"
				class="form-control" id="remark_${index}" placeholder="Remarks" /></td>
		<td nowrap="nowrap"><span class="table-remove"><button
					type="button"
					class="btn btn-danger btn-rounded btn-sm my-0 ibtnDel">Remove</button></span></td>
	</tr>
</body>
<script type="text/javascript">
$(document).ready(function() {
var productid=${productid}
var index = ${index}
	$.getJSON('${pageContext.request.contextPath}/productitem?productId='+productid , function (data) {
			var select = document.getElementById("productitem_"+index);
			select.options.length = 0;
			$.each(data, function (key, entry) {
				select.options[select.options.length] = new Option(entry.name, entry.id);
			})
	});
	
	 $("#addrow").on("click", function () {
	       /*  var newRow = $("<tr>");
	        var cols = "";

	        cols += '<td><input type="text" class="form-control" name="name' + counter + '"/></td>';
	        cols += '<td><input type="text" class="form-control" name="mail' + counter + '"/></td>';
	        cols += '<td><input type="text" class="form-control" name="phone' + counter + '"/></td>';

	        cols += '<td><input type="button" class="ibtnDel btn btn-md btn-danger "  value="Delete"></td>';
	        newRow.append(cols); */
	        var productid = document.getElementById("product").value;
	        var rowIndex = document.getElementById("productitemtable").getElementsByTagName("tbody")[0].getElementsByTagName("tr").length;
	        $.ajax({url: "${pageContext.request.contextPath}/addrow.jsp?productid="+productid+"&index="+rowIndex, success: function(result){
	        	$("table.order-list").append(result);
	          }});
	        
	  });

});

</script>
</html>
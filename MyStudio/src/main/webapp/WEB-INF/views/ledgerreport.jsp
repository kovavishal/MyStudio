<!DOCTYPE html>
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
<title>Ledger Report</title>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="container-fluid" id="main">
		<div class="card">
			<div class="card-header text-center"
				style="padding-bottom: 0px; padding-top: 0px; background: skyblue; color: #30567D; font-weight: 625;">
				<jsp:include page="logout.jsp">
					<jsp:param name="page" value="Ledger Report" />
				</jsp:include>
			</div>
			<form:form method="POST" action="ledger" modelAttribute="report"
				name="reportform">

				<button type="button" class="btn btn-primary" data-toggle="modal"
					data-target="#mandateModel" hidden="true" id="mandateBtn"></button>
				<div class="modal fade" id="mandateModel" tabindex="-1"
					role="dialog" aria-labelledby="mandateModel" aria-hidden="true">
					<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-body" style="color: #30567D; font-weight: 625;">
								Please enter at least one search criteria</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-secondary"
									data-dismiss="modal">Close</button>
							</div>
						</div>
					</div>
				</div>

				<div class="card-body bg-light border-success mb-3 col-lg-12"
					style="color: navy;">
					<div class="row">

						<div class="col-md-4 col-lg-3">
							<label for="fromdate">From Date</label>
							<form:input path="strFromDate" class="form-control" id="fromdate" />
						</div>
						<div class="col-md-4 col-lg-3">
							<label for="todate">To Date</label>
							<form:input path="strToDate" class="form-control" id="todate" />
						</div>
						<div class="form-group col-md-2 col-lg-3">
							<label for="acchead">Account Head</label>
							<form:select path="typeId" class="form-control" id="expenseId"
								required="true">
								<option value="">Account Head</option>
								<c:forEach items="${expenseTypes}" var="expenseType">
									<c:choose>
										<c:when test="${expenseType.id eq  report.typeId}">
											<option value="${expenseType.id}" selected="selected">${expenseType.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${expenseType.id}">${expenseType.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</form:select>
						</div>
					</div>

					<%-- <div class="row"> 
     <div class="col-md-6 col-lg-3">
      <label for="customertype">Customer Type</label>
      <form:select path="custType" class="form-control" id="custType">
        <option value="">-Select Type-</option>
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
    <div class="col-md-6 col-lg-3">
      <label for="custName">Customer Name</label>
      <form:input path="custName" class="form-control" id="custName"/>
    </div> 
     <div class="col-md-6 col-lg-3">
      <label for="orderstatus">Order Status</label>
      <form:select path="status" class="form-control" id="orderstatus">
        <option value="">-Select Type-</option>
        <c:forEach items="${orderstatus}" var="status">
	    	<c:choose>
	    		<c:when test="${status.id eq  order.status}">`
	    			<option value="${status.id}" selected="selected">${status.name}</option>
	    		</c:when>
	    		<c:otherwise>
	    			<option value="${status.id}">${status.name}</option>
	    		</c:otherwise>
	    	</c:choose>
	    	</c:forEach>
      </form:select>
    </div>
  </div> --%>
					<div class="col-lg-9 text-right" style="padding-top: 10px;">
						<a href="<%= request.getContextPath()%>/report/ledger"
							class="btn btn-info" role="button"
							style="width: 300px; font-size: 20px"><i
							class="fa fa-refresh" style='font-size: 20px; padding: 0 1em 0 0'></i>Refresh</a>

						<!-- <button type="button" class="btn btn-primary" id="searchBtn" onclick="report();">Search</button> -->


						<button type="button" class="btn btn-success" onclick="report();"
							id="searchBtn" style="width: 300px; font-size: 20px">
							<i class="fa fa-search"
								style='font-size: 20px; padding: 0 1em 0 0'></i>Search
						</button>

					</div>
				</div>
				<!--  <div class="col-sm-8 col-lg-12" style="width: 100%;overflow: auto;height: 400px;">
      <table  class="table table-bordered table-responsive-md table-striped text-center" style="color: navy;" id="statustable">
        <tr>
          <th class="text-center">Transaction Date</th>
          <th class="text-center">Transaction Type</th>
          <th class="text-center">Reference Type</th>
          <th class="text-center" nowrap="nowrap">Description</th>
		  <th class="text-center">Amount(Dr)</th>
          <th class="text-center">Amount(Cr)</th>
         </tr>
       
 	</table>  
	</div> -->

				<div class="table-responsive col-lg-12" style="padding-left: 1px;">
					<table id="reporttable"
						class="table table-bordered table-striped display wrap"
						style="width: 100%">
						<thead>
							<tr>
								<th class="col-sm-1 col-lg-12"
									style="width: 10%; position: relative; text-align: center;"
									data-field="trxnDate" nowrap="nowrap">Date</th>
								<th class="col-sm-3 col-lg-12"
									style="width: 10%; position: relative; text-align: center;"
									data-field="trxnType" nowrap="nowrap">Type</th>
								<th class="col-sm-3 col-lg-12"
									style="width: 10%; position: relative; text-align: center;"
									data-field="trxnType" nowrap="nowrap">Chq.Date</th>
								<th class="col-sm-3 col-lg-12"
									style="width: 10%; position: relative; text-align: center;"
									data-field="trxnType" nowrap="nowrap">Chq.No</th>
								<!-- 	<th class="col-sm-3 col-lg-12" data-field="trxnType" nowrap="nowrap" hidden="true">BankName</th>
					<th class="col-sm-3 col-lg-12" data-field="trxnType">Narration</th> -->
								<th class="col-sm-3 col-lg-12"
									style="width: 10%; position: relative; text-align: center;"
									data-field="referenceType" nowrap="nowrap">Ref.Type</th>
								<th class="col-sm-3 col-lg-12"
									style="width: 20%; position: relative; text-align: center;"
									data-field="description">Description</th>
								<th class="col-sm-3 col-lg-12"
									style="width: 15%; position: relative; text-align: center;"
									data-field="debitAmount" nowrap="nowrap">Amt.(Dr)</th>
								<th class="col-sm-1 col-lg-12"
									style="width: 15%; position: relative; text-align: center;"
									data-field="creditAmount" nowrap="nowrap">Amt.(Cr)</th>
							</tr>
						</thead>
						<tfoot>
							<tr>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<!--     <th hidden="true"></th>
                <th></th> -->
								<th></th>
								<th></th>
								<th style="width: 15%; position: relative; text-align: right"></th>
								<th style="width: 15%; position: relative; text-align: right"></th>
							</tr>
						</tfoot>
					</table>
				</div>
			</form:form>
			<div class="card-footer">All right reserved</div>
		</div>
	</div>
</body>
<script type="text/javascript">

$(function () {
	
	$('#fromdate').datetimepicker({
		format: 'DD/MM/YYYY',
		icons: {
            time: "fa fa-clock-o",
            date: "fa fa-calendar",
            up: "fa fa-arrow-up",
            down: "fa fa-arrow-down",
            autoclose: true
        }
	
    });
	
	$('#todate').datetimepicker({
		format: 'DD/MM/YYYY',
		icons: {
            time: "fa fa-clock-o",
            date: "fa fa-calendar",
            up: "fa fa-arrow-up",
            down: "fa fa-arrow-down"
        }
    });
});

function report(){
	var fromdate = $('#fromdate').val();
	var todate = $('#todate').val();
	var type = $('#expenseId').val();
	document.title = 'Ledger Report for the Period ' + $('#fromdate').val() +' To '+ $('#todate').val(); 
	if(fromdate!='' && todate!=''){
	var reqData={
       'fromDate': $('#fromdate').val(),
       'toDate' : $('#todate').val(),
       'type': $('#expenseId').val()
    };
	var tableClient = $('#reporttable').DataTable({
		"destroy": true,
		"searching":true,
		"ajax": {
			"url": "${pageContext.request.contextPath}/report/detail",
			"type": "POST",
			"data" : reqData,
			"success" :  function(data){
				console.log(data.length);
				if(data.length > 0){
				$.each(data, function(index, obj){
					tableClient.row.add([
						obj.trxnDate,
						obj.trxnType,
						obj.chqDate,
						obj.chqNo,
					/* 	obj.bankName, */
					/* 	obj.narration, */
						obj.referenceType,
						obj.description,
						obj.debitAmount,
						obj.creditAmount
					]).draw();
				});
				}else{
					console.log("empty table");
					$('#reporttable').DataTable({
						"destroy": true,
						"searching":false,
						"language": {
					        "infoEmpty":"No data available"
					    }
					});
				}
			}
		},
		"aoColumnDefs": [ {
		    "aTargets": [6 ],
		    "mRender": function (data, type, full) {
		     var formmatedvalue='';
		     if(data != null){
		    	 formmatedvalue=parseFloat(data).toFixed(2);
		     }
		      return formmatedvalue;
		    },
		    className: 'dt-body-right'
		},
		{
		    "aTargets": [ 7 ],
		    "mRender": function (data, type, full) {
		     var formmatedvalue='';
		     if(data != null){
		    	 formmatedvalue=parseFloat(data).toFixed(2);
		     }
		      return formmatedvalue;
		    },
		    className: 'dt-body-right'
		},
	/* 	{
            "targets": [ 4 ],
            "visible": false
        } */
		],
		"footerCallback": function ( row, data, start, end, display ) {
            var api = this.api();
 
            // Remove the formatting to get integer data for summation
            var intVal = function ( i ) {
                return typeof i === 'string' ?
                    i.replace(/[\$,]/g, '')*1 :
                    typeof i === 'number' ?
                        i : 0;
            };
 
            // Total over all pages
            total = api
                .column( 6 )
                .data()
                .reduce( function (a, b) {
                    return intVal(a) + intVal(b);
                }, 0 );
 
            // Total over this page
            pageTotal = api
                .column( 6, { page: 'current'} )
                .data()
                .reduce( function (a, b) {
                    return intVal(a) + intVal(b);
                }, 0 );
 
            // Update footer
            $( api.column( 6 ).footer() ).html( parseFloat(total).toFixed(2));
            
            total = api
            .column( 7 )
            .data()
            .reduce( function (a, b) {
                return intVal(a) + intVal(b);
            }, 0 );

        // Total over this page
        pageTotal = api
            .column( 7, { page: 'current'} )
            .data()
            .reduce( function (a, b) {
                return intVal(a) + intVal(b);
            }, 0 );

        // Update footer
        $( api.column( 7 ).footer() ).html(
                 parseFloat(total).toFixed(2) 
         );
        },
        dom: 'Bfrtip',
        buttons: [
            { extend: 'excelHtml5', footer: true },
            { extend: 'pdfHtml5', footer: true },
            { extend: 'print', footer: true,
            	customize: function ( win ) {
                    $(win.document.body)
                        .css( 'font-size', '12px' );

                    $(win.document.body).find( 'table' )
                        .css( 'font-size', '12px' );     
                },
                customize: function ( doc ) {
                    $(doc.document.body).find('h1').css('font-size', '18pt');
                    $(doc.document.body).find('h1').css('text-align', 'center'); 
                }
    			
                 },
        ]
	});
	
	}else{
		//alert("Enter the date to search")
		$('#mandateBtn').click();
	}
}
</script>
</html>
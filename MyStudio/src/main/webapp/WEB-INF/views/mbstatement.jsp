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
<title>Monthly Business Statement</title>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="container-fluid" id="main">
		<div class="card">
			<div class="card-header text-center"
				style="padding-bottom: 0px; padding-top: 0px; background: skyblue; color: #30567D; font-weight: 625;">
				<jsp:include page="logout.jsp">
					<jsp:param name="page" value="Monthly Business Statement" />
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

					</div>


					<div class="col-lg-9 text-right" style="padding-top: 10px;">
						<a href="<%= request.getContextPath()%>/report/mbrsummary"
							class="btn btn-info" role="button"
							style="width: 300px; font-size: 20px"><i
							class="fa fa-refresh" style='font-size: 20px; padding: 0 1em 0 0'></i>Refresh</a>
						<!-- 	  	<button type="button" class="btn btn-primary" id="searchBtn" onclick="report();">Search</button>
 -->

						<button type="button" class="btn btn-success" onclick="report();"
							id="searchBtn" style="width: 300px; font-size: 20px">
							<i class="fa fa-search"
								style='font-size: 20px; padding: 0 1em 0 0'></i>Search
						</button>



					</div>
				</div>

				<div class="table-responsive col-lg-12" style="padding-left: 0px;">
					<table id="reporttable"
						class="table table-bordered table-striped display nowrap"
						style="width: 100%">
						<thead>
							<tr>
								<th class="col-sm-1" data-field="trxnDate">Date</th>
								<th class="col-sm-3" data-field="trxnType">Cash Sale</th>
								<th class="col-sm-3" data-field="referenceType">Cr.Sale</th>
								<th class="col-sm-3" data-field="description">Total Sale</th>
								<th class="col-sm-3" data-field="debitAmount">Purchase</th>
								<th class="col-sm-1" data-field="creditAmount">Cash
									Purchase</th>
								<th class="col-sm-1" data-field="creditAmount">Cr.Purchase</th>
								<th class="col-sm-1" data-field="creditAmount">Expenses</th>
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
	document.title = 'Business Statement From :' + $('#fromdate').val() +' To :'+ $('#todate').val(); 
	if(fromdate!='' && todate!=''){
	var reqData={
       'fromDate': $('#fromdate').val(),
       'toDate' : $('#todate').val(),
       'report': 'mbrsummary'
    };
	var tableClient = $('#reporttable').DataTable({
		"destroy": true,
		"searching":true,
		"ordering": false,
		"ajax": {
			"url": "${pageContext.request.contextPath}/report/mbstatement",
			"type": "POST",
			"data" : reqData,
			"success" :  function(data){
				console.log(data.length);
				if(data.length > 0){
				$.each(data, function(index, obj){
					tableClient.row.add([
						obj.trxnDate,
						obj.noOfBills,
						obj.days,
						obj.invoiceAmount,
						obj.creditAmount,
						obj.debitAmount,
						obj.payAmount
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
		    "aTargets": [1 ],
		    "mRender": function (data, type, full) {
		     var formmatedvalue='';
		     if(data != null){
		    	 formmatedvalue=data;
		     }
		      return formmatedvalue;
		    },
		    className: 'dt-body-right'
		},
		{
		    "aTargets": [ 2 ],
		    "mRender": function (data, type, full) {
		     var formmatedvalue='';
		     if(data != null){
		    	 formmatedvalue=data;
		     }
		      return formmatedvalue;
		    },
		    className: 'dt-body-right'
		},
		
		{
		    "aTargets": [ 3 ],
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
		    "aTargets": [ 4 ],
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
		    "aTargets": [ 5 ],
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
		    "aTargets": [ 6 ],
		    "mRender": function (data, type, full) {
		     var formmatedvalue='';
		     if(data != null){
		    	 formmatedvalue=parseFloat(data).toFixed(2);
		     }
		      return formmatedvalue;
		    },
		    className: 'dt-body-right'
		}
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
            // starting 
            total = api
                .column( 1 )
                .data()
                .reduce( function (a, b) {
                    return intVal(a) + intVal(b);
                }, 0 );
 			billTotal = total;
            // Total over this page
            pageTotal = api
                .column( 1, { page: 'current'} )
                .data()
                .reduce( function (a, b) {
                    return intVal(a) + intVal(b);
                }, 0 );
 
            // Update footer
              console.log("display",$( api.column( 1 ).footer() ));
            $( api.column(1 ).footer() ).html(total);
	// ending
            // Total over all pages
            // starting 
            total = api
                .column( 2 )
                .data()
                .reduce( function (a, b) {
                    return intVal(a) + intVal(b);
                }, 0 );
 			sheetTotal = total;
            // Total over this page
            pageTotal = api
                .column( 2, { page: 'current'} )
                .data()
                .reduce( function (a, b) {
                    return intVal(a) + intVal(b);
                }, 0 );
 
            // Update footer
            $( api.column(2 ).footer() ).html(total);
	// ending
            
      // Total over all pages
            // starting       
            total = api
            .column( 3 )
            .data()
            .reduce( function (a, b) {
                return intVal(a) + intVal(b);
            }, 0 );
		salesTotal=total;
        // Total over this page
        pageTotal = api
            .column( 3, { page: 'current'} )
            .data()
            .reduce( function (a, b) {
                return intVal(a) + intVal(b);
            }, 0 );

        // Update footer
      
        $( api.column(3 ).footer() ).html(
                  parseFloat(Math.ceil(total))
         );
     // ending

	 // Total over all pages
            // starting       
            total = api
            .column( 4 )
            .data()
            .reduce( function (a, b) {
                return intVal(a) + intVal(b);
            }, 0 );
		receiptTotal=total;
        // Total over this page
        pageTotal = api
            .column( 4, { page: 'current'} )
            .data()
            .reduce( function (a, b) {
                return intVal(a) + intVal(b);
            }, 0 );

        // Update footer
        $( api.column(4 ).footer() ).html(
                  parseFloat(Math.ceil(total))
         );
     // ending
     
      // Total over all pages
            // starting       
            total = api
            .column( 5 )
            .data()
            .reduce( function (a, b) {
                return intVal(a) + intVal(b);
            }, 0 );
		purchaseTotal=total;
        // Total over this page
        pageTotal = api
            .column( 5, { page: 'current'} )
            .data()
            .reduce( function (a, b) {
                return intVal(a) + intVal(b);
            }, 0 );

        // Update footer
        $( api.column(5 ).footer() ).html(
                  parseFloat(Math.ceil(total))
         );
     // ending
      // Total over all pages
            // starting       
            total = api
            .column( 6 )
            .data()
            .reduce( function (a, b) {
                return intVal(a) + intVal(b);
            }, 0 );
		paymentTotal=total;
        // Total over this page
        pageTotal = api
            .column(6, { page: 'current'} )
            .data()
            .reduce( function (a, b) {
                return intVal(a) + intVal(b);
            }, 0 );

        // Update footer
        $( api.column(6 ).footer() ).html(
                 parseFloat(Math.ceil(total))
         );
     // ending
      /*   
        $( api.column( 3 ).footer() ).html(
        	(crTotal-drTotal) > 0? 'Shortage Amount :'+parseFloat((crTotal-drTotal)).toFixed(2): parseFloat((crTotal-drTotal)).toFixed(2) < 0 ?'Excess Amount :'+parseFloat((drTotal-crTotal)).toFixed(2) :'No Shortage' 
        );
         */
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
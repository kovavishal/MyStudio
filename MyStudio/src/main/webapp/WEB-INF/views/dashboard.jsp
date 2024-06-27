<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page isELIgnored="false"%>
<%@include file="staticcontent.jsp"%>
<title>Dashboard</title>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="container-fluid" id="main"
		style="padding-left: 30px; padding-right: 0px; position: relative">
		<div
			style="padding-left: 50px; padding-bottom: 0px; padding-top: 30px; color: #30567D; font-weight: 625;">
			<jsp:include page="logout.jsp">
				<jsp:param name="page" value="" />
			</jsp:include>
		</div>
		<form:form method="POST" action="report/dashboard"
			modelAttribute="dashboard" name="dashboardForm">

			<div class="card-deck bg-dark col-lg-12 "
				style="padding-left: 30px; padding-top: 30px;">

				<div class="card text-white bg-primary mb-3"
					style="max-width: 100%; position: relative">

					<div class="card-header">
						<i class="fa fa-line-chart" aria-hidden="true"
							style='padding: 0 1em 0 0'></i>Sales
					</div>
					<div class="card-body">
						<h5 class="card-title" style="font-size: 30px">
							<label for="totalsale" style="align: center"> <i
								class="fa fa-inr" aria-hidden="true"></i> ${dashboard.totalsale}
							</label>
						</h5>
						<label for="cashsale" style="font-size: 12px;">(Cash:${dashboard.cashsale}
							+ Cr.${dashboard.creditsale})</label>
					</div>
					<%-- <div style="text-align:right;">
							<a style="color: white; text-align: center; padding: 0 0em 0 0"
							href="<%=request.getContextPath()%>/payment/pay"> more info<i
							class="fa fa-angle-double-right" style='font-size: 15px;'></i>
							</a>
							</div> --%>
				</div>
				<div class="card text-white bg-danger mb-3"
					style="max-width: 100%; position: relative">
					<div class="card-header">
						<i class="fa fa-shopping-cart" aria-hidden="true"
							style='padding: 0 1em 0 0'></i>Purchase
					</div>
					<div class="card-body">
						<h5 class="card-title" style="font-size: 30px">
							<label for="totalpurchase"> <i class="fa fa-inr"
								aria-hidden="true"></i> ${dashboard.totalpurchase}
							</label>
						</h5>
						<label for="cashpurchase" style="font-size: 12px;">(Cash:${dashboard.cashpurchase}
							+ Credit:${dashboard.creditpurchase})</label>
					</div>
					<%-- <div style="text-align: right;">
						<a style="color: white; text-align: center; padding: 0 0em 0 0"
						href="<%=request.getContextPath()%>/payment/pay"> more info<i
						class="fa fa-angle-double-right" style='font-size: 15px;'></i>
						</a>
						</div> --%>
				</div>

				<div class="card text-white bg-success mb-3"
					style="max-width: 100%; position: relative;">
					<div class="card-header">
						<i class="fa fa-inr" aria-hidden="true" style='padding: 0 1em 0 0'></i>Receipts
					</div>
					<div class="card-body">
						<h5 class="card-title" style="font-size: 30px">
							<label for="receipts"> <i class="fa fa-inr"
								aria-hidden="true"></i> ${dashboard.receipts}
							</label>
						</h5>
					</div>
					<%-- <div style="text-align: right;">
							<a style="color: white; text-align: center; padding: 0 0em 0 0"
								href="<%=request.getContextPath()%>/payment/pay"> more info<i
								class="fa fa-angle-double-right" style='font-size: 15px;'></i>
							</a>
							</div> --%>
				</div>
				<div class="card text-white bg-warning mb-3"
					style="max-width: 100%; position: relative;">
					<div class="card-header">
						<i class="fa fa-cc-visa" aria-hidden="true"
							style='padding: 0 1em 0 0'></i>Payments
					</div>
					<div class="card-body">
						<h5 class="card-title" style="font-size: 30px">
							<label for="receipts"> <i class="fa fa-inr"
								aria-hidden="true"></i> ${dashboard.payments}
							</label>
						</h5>
					</div>
					<%-- <div style="text-align: right;">
							<a style="color: white; text-align: center; padding: 0 0em 0 0"
								href="<%=request.getContextPath()%>/payment/pay"> more info<i
								class="fa fa-angle-double-right" style='font-size: 15px;'></i>
						</a>
						</div> --%>
				</div>


			</div>
			<div class="card-deck bg-dark col-lg-12">
				</br>
				</br>
				</br>
				</br>
			</div>
			<!-- 	Order List view -->
			<div class="card-deck bg-dark col-lg-12"
				style="padding-left: 30px; padding-top: 10px;">
				<!-- <div class="row col-lg-2"></div> -->
				<div class="card text-white bg-info mb-6">
					<div class="card-header">
						<i class="fa fa-shopping-cart" aria-hidden="true"
							style='padding: 0 1em 0 0'></i>Order Status <label for="receipts"
							style='padding: 0 0 0 16em'> Count</label>
					</div>
					<ul class="list-group list-group-flush">
						<li class="list-group-item list-group-item-info"><i
							class="fa fa-shopping-cart" aria-hidden="true"
							style='padding: 0 1em 0 0'></i>Order Taken <strong><label
								for="receipts" style='font-weight: 725; padding: 0 0 0 16.5em'>
									${dashboard.ordertaken}</label></strong></li>
						<li class="list-group-item list-group-item-info"><i
							class="fa fa-plane" aria-hidden="true" style='padding: 0 1em 0 0'></i>Order
							Delivered <strong><label for="receipts"
								style='font-weight: 725; padding: 0 0 0 15em'>
									${dashboard.orderdelivered}</label></strong></li>
						<li class="list-group-item list-group-item-info"><i
							class="fa fa-close" aria-hidden="true" style='padding: 0 1em 0 0'></i>Order
							Cancelled <strong><label for="receipts"
								style='font-weight: 725; padding: 0 0 0 15em'>
									${dashboard.ordercancelled}</label></strong></li>

						<li class="list-group-item list-group-item-info"><i
							class="fa fa-stack-overflow" aria-hidden="true"
							style='padding: 0 1em 0 0'></i>Ordered Sheets <strong><label
								for="receipts" style='font-weight: 725; padding: 0 0 0 15em'>
									${dashboard.orderedsheets}</label></strong></li>
					</ul>
					<div style="text-align: right;"></div>
				</div>

				<div class="table-responsive col-lg-6" style="padding-left: 0px;">
					<table id="reporttable"
						class="table table-bordered table-striped table-sm table-hover display nowrap"
						style="bgcolor: 'blue'; width: 100%;">
						<thead>
							<tr>


								<th style="height: 40px; color: white" class="col-sm-3"
									data-field="referenceType">Employee Name</th>
								<th style="height: 40px; color: white" class="col-sm-3"
									data-field="debitAmount">Department</th>
								<th style="height: 40px; color: white" class="col-sm-1"
									data-field="creditAmount">Pending Order</th>


							</tr>
						</thead>
						<!-- <tfoot>
            <tr>
              
                 <th></th>
                <th></th>
                <th></th> 
            </tr>
        </tfoot> -->
					</table>
					<%-- <a href="<%= request.getContextPath()%>/report/myreport" class="btn btn-info" role="button">Refresh</a> --%>


					<!-- 
			<p><b>Selected rows data:</b></p>
			<pre id="example-console-rows"></pre>
			
			<p><b>Form data as submitted to the server:</b></p>
			<pre id="example-console-form"></pre>  -->


				</div>


			</div>
			<!-- 	Order List view -->
			<div class="card-deck bg-dark col-lg-12"
				style="padding-left: 30px; padding-top: 10px;">
				<div class="row bg-dark col-lg-2"></div>
				</br>
				</br>
				</br>
				</br>
				</br>
				</br>
				</br>
				<%-- <div class="table-responsive bg-dark col-lg-9" style="padding-left: 0px; padding-top: 10px;">
		<table id="reporttable" class="table table-bordered table-striped display nowrap" style="width:100%">
			<thead>
				<tr >
					
	   				
					<th style="color:white" class="col-sm-3"   data-field="referenceType">Employee Name</th>
					 <th  style="color:white" class="col-sm-3"    data-field="debitAmount">Department</th>
					<th style="color:white" class="col-sm-1"    data-field="creditAmount">Pending Order</th> 
									
					
				</tr>
			</thead>
			<tfoot>
            <tr>
              
                 <th></th>
                <th></th>
                <th></th> 
            </tr>
        </tfoot>
		</table>
			<a href="<%= request.getContextPath()%>/report/myreport" class="btn btn-info" role="button">Refresh</a>
			

		<!-- 
			<p><b>Selected rows data:</b></p>
			<pre id="example-console-rows"></pre>
			
			<p><b>Form data as submitted to the server:</b></p>
			<pre id="example-console-form"></pre>  -->
		 
	</div>  --%>

			</div>

		</form:form>
	</div>
</body>
<script>
$(document).ready(function(){
	
	var tableClient = $('#reporttable').DataTable({
	
		"columnDefs": [
		   
		    {
		        targets: 2,
		        className: 'dt-body-center'
		    }
		  
		  ],
		"scrollY": 250,
		"destroy": false,
		"paging":false,
		"searching":false,
		"ajax": {
			"url": "${pageContext.request.contextPath}/report/dashboardTable",
			"type": "GET",
			 "success" :  function(data){
				console.log(data.length);
				if(data.length > 0){
				$.each(data, function(index, obj){
					tableClient.row.add([
						obj.name,
						obj.city,
						obj.jobOpenCount
						
						]).draw();
				}); 
				}else{
					console.log("empty table");
					$('#reporttable').DataTable({
						"destroy": false,
						"searching":false,
						"language": {
					        "infoEmpty":"No data available"
					    }
					});
				}
			}
		},
		
		/* "aoColumnDefs": [  
		{
		    "aTargets": [ 2 ],
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
 
            // Remove the formatting to get integer data for summation
            var intVal = function ( i ) {
                return typeof i === 'string' ?
                    i.replace(/[\$,]/g, '')*1 :
                    typeof i === 'number' ?
                        i : 0;
            };
            total = api
            .column( 2 )
            .data()
            .reduce( function (a, b) {
                return intVal(a) + intVal(b);
            }, 0 );
			drTotal = total;
        // Total over this page
        pageTotal = api
            .column( 2, { page: 'current'} )
            .data()
            .reduce( function (a, b) {
                return intVal(a) + intVal(b);
            }, 0 );

        // Update footer
         $( api.column(2 ).footer() ).html(
                 'Total Outstanding :'
         );
        $( api.column( 2).footer() ).html(
                 parseFloat(total).toFixed(2) 
         );
        },*/
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
        ], 

        'select': {
            'style': 'multi'
         },
         'order': [[1, 'asc']]
            
	});
})

</script>
</html>
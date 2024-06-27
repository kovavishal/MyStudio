<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="staticcontent.jsp"%>a
<script src="<%=request.getContextPath()%>/js/jquery-3.3.1.js"></script>
<script src="<%=request.getContextPath()%>/js/dataTable.checkboxes.js"></script>
<script
	src="<%=request.getContextPath()%>/js/dataTable.checkboxes.min.js"></script>
<script src="<%=request.getContextPath()%>/js/dataTables.buttons.min.js"></script>
<script src="<%=request.getContextPath()%>/js/buttons.flash.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jszip.min.js"></script>
<script src="<%=request.getContextPath()%>/js/pdfmake.min.js"></script>
<script src="<%=request.getContextPath()%>/js/vfs_fonts.js"></script>
<script src="<%=request.getContextPath()%>/js/buttons.html5.min.js"></script>
<script src="<%=request.getContextPath()%>/js/buttons.print.min.js"></script>
<script
	src="<%=request.getContextPath()%>/js/bootstrap-datetimepicker.min.js"></script>
<link type="text/css" href="/css/dataTables.checkboxes.css"
	rel="stylesheet" />
<!-- <title>Customer's Outstanding Report</title> -->
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="container-fluid" id="main">
		<div class="card">
			<div class="card-header text-center"
				style="padding-bottom: 0px; padding-top: 0px; background: skyblue; color: #30567D; font-weight: 625;">
				<jsp:include page="logout.jsp">
					<jsp:param name="page" value="Undelivered Albums Report" />
				</jsp:include>
			</div>
			<form:form method="POST" action="customeroutstanding"
				modelAttribute="report" name="reportform" id="reportform">

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
					style="color: gray;">
					<%--  <div class="row">
   	  
     <div class="col-md-4 col-lg-3">
     	<label for="fromdate">From Date</label>
    	 <form:input path="strFromDate" class="form-control" id="fromdate"/>
     </div>  
     <div class="col-md-4 col-lg-3">
     	<label for="todate">To Date</label>
     	<form:input path="strToDate" class="form-control" id="todate"/>
     </div>
   
	</div>
	  --%>


					<div class="col-lg-9 text-right" style="padding-top: 0px;">
						<P align="right">
							<a href="<%= request.getContextPath()%>/report/undeliveredalbum"
								class="btn btn-info" style="width: 300px; font-size: 20px"
								role="button"> <i class="fa fa-refresh"
								style='font-size: 20px; padding: 0 1em 0 0'></i>Refresh
							</a>
						</P>


						<%-- 	<button type="button" class="btn btn-primary" id="searchBtn" onclick="report();">Search</button>
 </div> --%>
					</div>

					<div class="table-responsive col-lg-12" style="padding-left: 0px;">
						<table id="reporttable"
							class="table table-bordered table-striped display nowrap"
							style="width: 100%">
							<thead>
								<tr>

									<th class="col-sm-1"
										style="width: 10%; position: relative; text-align: center;"
										data-field="creditAmount">Cust.No</th>
									<th class="col-sm-3"
										style="width: 20%; position: relative; text-align: center;"
										data-field="description">Customer Name</th>
									<th class="col-sm-3"
										style="width: 20%; position: relative; text-align: center;"
										data-field="referenceType">Area</th>
									<th class="col-sm-3"
										style="width: 10%; position: relative; text-align: center;"
										data-field="debitAmount">Mobile No</th>
									<th class="col-sm-3"
										style="width: 20%; position: relative; text-align: center;"
										data-field="description">Order Number</th>
									<th class="col-sm-3"
										style="width: 20%; position: relative; text-align: center;"
										data-field="referenceType">Order Date</th>
									<th class="col-sm-1"
										style="width: 20%; position: relative; text-align: center;"
										data-field="creditAmount">Oustanding Amount</th>
									<th class="col-sm-1"
										style="width: 20%; position: relative; text-align: center;"
										data-field="creditAmount">SMS Count</th>
									<!-- <th><input type="checkbox" id="selectall" name="selectall" /></th> 	 -->

								</tr>
							</thead>
							<tfoot>
								<tr>
									<!--  <th hidden="true"></th> -->
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
						<%-- <a href="<%= request.getContextPath()%>/report/myreport" class="btn btn-info" role="button">Refresh</a> --%>

						<!-- <P align="right"><button type="button" class="btn btn-warning" id="smsBtn" 
	  			  style="width: 300px; font-size:20px">
	  			  <i class='fa fa-envelope' style='font-size:22px;color:red;padding:0 1em 0 0'></i>Send SMS</button></P>

			 -->
						<!-- 
			<p><b>Selected rows data:</b></p>
			<pre id="example-console-rows"></pre>
			
			<p><b>Form data as submitted to the server:</b></p>
			<pre id="example-console-form"></pre>  -->

					</div>
					
			</form:form>
			
	</div>
</body>
<div class="card-footer">All rights reserved to Radisoft
				Consultancy</div>
		</div>
<script type="text/javascript">
 $(function(){

	// add multiple select / deselect functionality
	$("#selectall").click(function () {
		 $('.case').attr('checked', this.checked);
		
	});

	// if all checkbox are selected, check the selectall checkbox
	// and viceversa
	 $(".case").click(function(){

		if($(".case").length == $(".case:checked").length) {
			$("#selectall").attr("checked", "checked");
		} else {
			$("#selectall").removeAttr("checked");
		}

	}); 
});

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
 var today = new Date();
 var dd = String(today.getDate()).padStart(2, '0');
 var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
 var yyyy = today.getFullYear();

 today = dd + '/' + mm + '/' + yyyy;
 
 $(document).ready(function(){
	
    var reqData={'report': 'undeliveredalbum'};
	document.title = 'UNDELIVERED ALBUMS REPORT as on '+today; 	
	var tableClient = $('#reporttable').DataTable({
		"destroy": true,
		"searching":true,
		"ajax": {
			"url": "${pageContext.request.contextPath}/report/undelivered_album_detail",
			"type": "POST",
			"data" : reqData,
			"success" :  function(data){
				console.log(data.length);
				if(data.length > 0){
				$.each(data, function(index, obj){
					tableClient.row.add([
						obj.custId,
						obj.customerName,
						obj.address2,
						obj.mobileNo,
						obj.orderId,
						obj.invoiceDate,
						obj.creditAmount,
						obj.days
					/* 	'<td text-align="center"><input type="checkbox" class="case" name="case" value="1"/></td>'	 */
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
		
		"aoColumnDefs": [

			{
			    "aTargets": [ 0 ],
			    "mRender": function (data, type, full) {
			     var formmatedvalue='';
			     if(data != null){
			    	 formmatedvalue=data;
			     }
			      return formmatedvalue;
			    },
			    className: 'dt-body-center'
			}, 
			{
			    "aTargets": [4 ],
			    "mRender": function (data, type, full) {
			     var formmatedvalue='';
			     if(data != null){
			    	 formmatedvalue=data;
			     }
			      return formmatedvalue;
			    },
			    className: 'dt-body-center'
			}, 
		   { "aTargets": [ 6 ],
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
		    	 formmatedvalue=data;
		     }
		      return formmatedvalue;
		    },
		    className: 'dt-body-center'
		}, 
		
		
		  
		  /* {
	            'targets': [ 0 ],
	            'visible': false
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
            total = api
            .column( 5 )
            .data()
            .reduce( function (a, b) {
                return intVal(a) + intVal(b);
            }, 0 );
			drTotal = total;
        // Total over this page
        pageTotal = api
            .column( 5, { page: 'current'} )
            .data()
            .reduce( function (a, b) {
                return intVal(a) + intVal(b);
            }, 0 );

        // Update footer
       /*   $( api.column(4 ).footer() ).html(
                 'Total Outstanding :'
         ); */
       /*  $( api.column( 5).footer() ).html(
                 parseFloat(total).toFixed(2) 
         ); */
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
        ], 

        'select': {
            'style': 'multi'
         },
         'order': [[0, 'asc']]
           
	});

	
	 $(document).on('click', '#smsBtn', function () {
	        var matches = [];
	        var mobile = [];
	        var balance=[];
	        var checkedcollection = tableClient.$(".case:checked", { "page": "all" });
	        checkedcollection.each(function (index, elem) {
				 matches.push($(elem).val()); 
				 var row = $(this).closest("tr")[0];
	                mobile[index]= row.cells[3].innerHTML+ " ~ " + row.cells[4].innerHTML;
	              /*   balance[index]= row.cells[5].innerHTML; */
	        });
	      //Display selected Row data in Alert Box.
           
	       goBack(mobile);
	    });
	

 }) // main function  ends	

 
function goBack(mobile){
    $("#reporttable").DataTable().destroy();  
	
	 $('#reporttable').DataTable({
		 "ajax":{
				type: "POST",
				url: "${pageContext.request.contextPath}/order/outstandingInformationSMS/" + mobile,
			
				
				timeout : 100000,
				success: function(data){
					/* tableClient.clear().draw();
					tableClient.ajax.reload(); */
					alert("SUCCESS: ", e);
				},
				error: function(e){
					alert("ERROR: ", e);
				}
			}
		 
	   });
 }
	
	
</script>
</html>

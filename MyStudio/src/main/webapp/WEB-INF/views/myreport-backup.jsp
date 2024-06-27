<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="staticcontent.jsp"%>
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
<title>Customer's Outstanding Report</title>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="container-fluid" id="main">
		<div class="card">
			<div class="card-header text-center"
				style="padding-bottom: 0px; padding-top: 0px; color: #30567D; font-weight: 625;">
				<jsp:include page="logout.jsp">
					<jsp:param name="page" value="Customer's Outstanding Report" />
				</jsp:include>
			</div>
			<form:form method="POST" action="ledger" modelAttribute="report"
				name="reportform" id="reportform">

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
						<%-- <a href="<%= request.getContextPath()%>/report/myreport" class="btn btn-info" role="button">Refresh</a> --%>
						<button type="button" class="btn btn-primary" id="searchBtn"
							onclick="report();">Search</button>
					</div>
				</div>

				<div class="table-responsive col-lg-10" style="padding-left: 0px;">
					<table id="reporttable"
						class="table table-bordered table-striped display nowrap"
						style="width: 100%">
						<thead>
							<tr>

								<!-- 				 <th class="col-sm-1" data-field="referenceType">Cust.No</th> -->

								<th class="col-sm-3" data-field="description">Customer Name</th>
								<th class="col-sm-3" data-field="referenceType">Town/City</th>
								<th class="col-sm-3" data-field="referenceType">Area</th>
								<th class="col-sm-3" data-field="debitAmount">Mobile No</th>
								<th class="col-sm-1" data-field="creditAmount">Due Amount</th>
								<th><input type="checkbox" id="selectall" /></th>

							</tr>
						</thead>
						<tfoot>
							<tr>
								<!--     <th></th>-->
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

					<div class="col-lg-9 " style="padding-top: 5px;">
						<P align="right">
							<button type="button" class="btn btn-info" id="smsBtn"
								onclick="goBack();">Send SMS</button>
						</P>
					</div>


					<p>
						<b>Selected rows data:</b>
					</p>
					<pre id="example-console-rows"></pre>

					<p>
						<b>Form data as submitted to the server:</b>
					</p>
					<pre id="example-console-form"></pre>

				</div>
			</form:form>
			<div class="card-footer">All right reserved</div>
		</div>
	</div>
</body>
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

function report(){
	
    var reqData={'report': 'myreport'};
	var tableClient = $('#reporttable').DataTable({
		"destroy": true,
		"searching":true,
		"ajax": {
			"url": "${pageContext.request.contextPath}/report/cust_outstanding_detail",
			"type": "POST",
			"data" : reqData,
			"success" :  function(data){
				console.log(data.length);
				if(data.length > 0){
				$.each(data, function(index, obj){
					tableClient.row.add([
						
						 obj.firstName,
						obj.address2,
						obj.area,
						obj.mobileNo,
						obj.creditAmount,
						'<td align="center"><input type="checkbox" class="case" name="case" value="1"/></td>'	
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
		    "aTargets": [ 4 ],
		    "mRender": function (data, type, full) {
		     var formmatedvalue='';
		     if(data != null){
		    	 formmatedvalue=parseFloat(data).toFixed(2);
		     }
		      return formmatedvalue;
		    }
		},
		
		   {
            'targets': 5,
            'checkboxes': {
               'selectRow': true
            }
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
            total = api
            .column( 4 )
            .data()
            .reduce( function (a, b) {
                return intVal(a) + intVal(b);
            }, 0 );
			drTotal = total;
        // Total over this page
        pageTotal = api
            .column( 4, { page: 'current'} )
            .data()
            .reduce( function (a, b) {
                return intVal(a) + intVal(b);
            }, 0 );

        // Update footer
         $( api.column(3 ).footer() ).html(
                 'Total Outstanding :'
         );
        $( api.column( 4 ).footer() ).html(
                 parseFloat(total).toFixed(2) 
         );
        },
        dom: 'Bfrtip',
        buttons: [
            { extend: 'excelHtml5', footer: true },
            { extend: 'pdfHtml5', footer: true },
            { extend: 'print', footer: true },
        ]
	});
		
 } 

function goBack(){
	
	$.ajax({
		type: "POST",
		url: "${pageContext.request.contextPath}/order/outstandingInformationSMS/" + "999.99",
		timeout : 100000,
		success: function(data){
			tableClient.clear().draw();
			tableClient.ajax.reload();
		},
		error: function(e){
			alert("ERROR: ", e);
		}
	});
}
/* function goBack(){  
alert("hai");
		 
		   // Handle form submission event 
		   $('#reportform').on('smsBtn', function(e){
		      var form = this;
		       var rows_selected = table.column(5).checkboxes.selected();

		      // Iterate over all selected checkboxes
		      $.each(rows_selected, function(index, rowId){
		         // Create a hidden element 
		         $(form).append(
		             $('<input>')
		                .attr('type', 'hidden')
		                .attr('name', 'id[]')
		                .val(rowId)
		         );
		      });

		      // FOR DEMONSTRATION ONLY
		      // The code below is not needed in production
		      
		      // Output form data to a console     
		      $('#example-console-rows').text(rows_selected.join(","));
		      
		      // Output form data to a console     
		      $('#example-console-form').text($(form).serialize());
		       
		      // Remove added elements
		      $('input[name="id\[\]"]', form).remove();
		       
		      // Prevent actual form submission
		      e.preventDefault();
		   });   
		
} */	


</script>
</html>

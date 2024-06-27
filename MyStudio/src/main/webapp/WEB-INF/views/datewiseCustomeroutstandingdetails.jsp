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
<title>Date wise Customer Outstanding Summary Report</title>

<style>
.bs-example {
	margin: 20px;
}

@media screen and (min-width: 1000px) {
	.modal-lg {
		width: 1850px; /* New width for large modal */
		height: 1000px;
	}
}
</style>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="container-fluid" id="main">
		<div class="card">
			<div class="card-header text-center"
				style="padding-bottom: 0px; padding-top: 0px; background: skyblue; color: #30567D; font-weight: 625;">
				<jsp:include page="logout.jsp">
					<jsp:param name="page" value=" Date Wise Customer Outstanding Summary Report" />
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
								Enter the date and customer name to search</div>
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
						<%-- <div class="col-md-6 col-lg-3">
      <label for="custName">Customer Name</label>
      <form:input path="name" class="form-control" id="customername"/>
      <form:hidden path="typeId" class="form-control" id="custid"/>
    </div> --%>
					</div>

					<div class="col-lg-9 text-right" style="padding-top: 10px;">
						<a
							href="<%= request.getContextPath()%>/report/customeroutstandingdetails"
							class="btn btn-info" role="button"
							style="width: 300px; font-size: 20px"> <i
							class="fa fa-refresh" style='font-size: 20px; padding: 0 1em 0 0'></i>Refresh
						</a>


						<button type="button" class="btn btn-success" onclick="report();"
							id="searchBtn" style="width: 300px; font-size: 20px">
							<i class="fa fa-search"
								style='font-size: 20px; padding: 0 1em 0 0'></i>Generate
						</button>

						<!-- 	  	<button type="button" class="btn btn-primary" id="searchBtn" onclick="report();">Search</button>
 -->
					</div>
				</div>
				<div class="table-responsive col-lg-12"
					style="padding-left: 10px; position: relative">
					
					
					
					
					<div class="col-md-4 col-lg-3">
							<label for="todate">Total Opening Balance</label>
							<input type="text" id="textbox1" name="textbox1" >
							
						</div>
					<div class="col-md-4 col-lg-3">
							<label for="todate">Total Closing Balance</label>
							<input type="text" id="textbox2" name="textbox2" >
							
						</div>
					<table id="reporttable"
						class="table table-bordered table-striped display nowrap">
						<thead>
							<tr>

								<th class="col-sm-1"
									style="width: 5%; position: relative; text-align: center;"
									data-field="trxnType">Cust.Id</th>
								<!-- 	<th class="col-sm-1" data-field="description">Cust.Name</th>			 -->
								<th class="col-sm-2"
									style="width: 20%; position: relative; text-align: center;"
									data-field="debitAmount">CUSTOMER NAME</th>
								<th class="col-sm-1"
									style="width: 10%; position: relative; text-align: center;"
									data-field="creditAmount">MOBILE NO.</th>
								<th class="col-sm-2"
									style="width: 10%; position: relative; text-align: center;"
									data-field="trxnType">OPENING BALANCE</th>
								<!--<th class="col-sm-3" data-field="trxnType">Bill Amt.</th>
					<th class="col-sm-3" data-field="trxnType">Receipt Amt</th>
					<th class="col-sm-3" data-field="trxnType">Mode</th> -->
								<th class="col-sm-2"
									style="width: 10%; position: relative; text-align: center;"
									data-field="debitAmount">DEBIT</th>
								<th class="col-sm-2"
									style="width: 10%; position: relative; text-align: center;"
									data-field="creditAmount">CREDIT</th>
								<th class="col-sm-2"
									style="width: 10%; position: relative; text-align: center;"
									data-field="creditAmount">CLOSING BALANCE</th>

							</tr>
						</thead>
						<!-- <tfoot>
           			<tr>
                  	 <th></th>
               		 <th></th>
                	 <th></th>
                	 <th></th>
                	 <th></th>
                	 <th></th>
                	 <th></th>
			     	</tr>
        		</tfoot> -->
					</table>
				</div>
			</form:form>
			<div class="bs-example">
				<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
					aria-labelledby="myModalLabel" aria-hidden="true">
					<div class="modal-dialog modal-lg">
						<div class="modal-content">
							<!-- <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                 <h4 class="modal-title" id="myModalLabel">Modal title</h4>
            </div> -->
							<div class="modal-body"></div>

							<button type="button" class="btn btn-default"
								data-dismiss="modal">Close</button>
							<!--  <div class="modal-footer">
                <button type="button" class="btn btn-primary">Save changes</button>
            </div> -->
						</div>
					</div>
				</div>
			</div>
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
	
	document.title = 'Customer Outstanding Summary '+' from :'  + $('#fromdate').val() +' To :'+ $('#todate').val(); 
	if(fromdate!='' && todate!=''){
	var reqData={
       'fromDate': $('#fromdate').val(),
       'toDate' : $('#todate').val()
       };  
    var totalOpeningBalance = 0;
    var totalClosingBalance = 0;
	var tableClient = $('#reporttable').DataTable({
		 "responsive": true,
		 "destroy": true,
		"searching":true,
		 "ordering": false,
		"ajax": {
			"url": "${pageContext.request.contextPath}/report/cosdetail-test",
			"type": "POST",
			"data" : reqData,
			"success" :  function(data){
				console.log(data.length);
				if(data.length > 0){
				$.each(data, function(index, obj){
					totalOpeningBalance = totalOpeningBalance+obj.openingBalance;
					totalClosingBalance = totalClosingBalance+obj.balance;
					tableClient.row.add([
						obj.custId,
						obj.firstName,
						obj.mobileNo,
						obj.openingBalance,
						obj.invoiceAmount,
						obj.totalReceipt,
						obj.balance
					]).draw();
					
				});
				document.getElementById('textbox1').value = totalOpeningBalance;
				document.getElementById('textbox2').value = totalClosingBalance;
				
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
		 
	/* 	"footerCallback": function ( row, data, start, end, display ) {
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
                .column( 4 )
                .data()
                .reduce( function (a, b) {
                    return intVal(a) + intVal(b);
                }, 0 );
 
            // Total over this page
            pageTotal = api
                .column( 4, { page: 'current'} )
                .data()
                .reduce( function (a, b) {
                    return intVal(a) + intVal(b);
                }, 0 );
 
            // Update footer
            $( api.column( 4 ).footer() ).html( parseFloat(total).toFixed(2));
            
         
        }, */
         dom: 'Bfrtip',
        buttons: [
            { extend: 'excelHtml5', footer: true },
            { extend: 'pdfHtml5', footer: true },
            { extend: 'print', footer: true,
          /*   { extend: 'pageLength',footer: true, */
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
	
	}else{
		//alert("Enter the date to search")
		$('#mandateBtn').click();
	}
}
 $.formattedDate = function(dateToFormat) {
    var dateObject = new Date(dateToFormat);
    //alert("Date object :"+dateObject);
    var day = dateObject.getDate();
    var month = dateObject.getMonth()+1;
    var year = dateObject.getFullYear();
    day = day < 10 ? "0" + day : day; 
    month = month < 10 ? "0" + month : month; 
    var formattedDate = day + "/" + month + "/" + year;    
    //alert(formattedDate);
    return formattedDate;
};


/* $(document).ready(function() {
	
	
	$('#customername').autocomplete({
		serviceUrl: '${pageContext.request.contextPath}/autocomplete/customers',
		paramName: "searchTerm",
		delimiter: ",",
		minChars:3,
	   	transformResult: function(response) {
			return {      	
			  suggestions: $.map($.parseJSON(response), function(item) {
			      return { value: item.name, data: item.id };
			   })
			 };
      	},
      	onSelect:function(item){
       		$('#custid').val(item.data);
       	}
		
	});
	
}); */
// Popup form to show respective order details
 function orderDetails(orderid){
	var url = "${pageContext.request.contextPath}/orderdetails/"+orderid;
  	var win = window.open(url, '_blank');
  	win.focus();
   
}
 

</script>
</html>
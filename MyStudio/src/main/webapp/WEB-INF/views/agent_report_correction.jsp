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
<title>Agent Report</title>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="container-fluid" id="main">
		<div class="card">
			<div class="card-header text-center"
				style="padding-bottom: 0px; padding-top: 0px; background: skyblue; color: #30567D; font-weight: 625;">
				<jsp:include page="logout.jsp">
					<jsp:param name="page" value="Agent Report correction" />
				</jsp:include>
			</div>
			<form:form method="POST" action="agent" name="reportform">

				<button type="button" class="btn btn-primary" data-toggle="modal"
					data-target="#mandateModel" hidden="true" id="mandateBtn"></button>
				<div class="modal fade" id="mandateModel" tabindex="-1"
					role="dialog" aria-labelledby="mandateModel" aria-hidden="true">
					<div class="modal-dialog" role="document">
						<div class="modal-content">
							<div class="modal-body" style="color: #30567D; font-weight: 625;">
								Please enter at the date and agent name to search</div>
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
							<label for="fromdate">From Date</label> <input type="text"
								name="strFromDate" class="form-control" id="fromdate" />
						</div>
						<div class="col-md-4 col-lg-3">
							<label for="todate">To Date</label> <input type="text"
								name="strToDate" class="form-control" id="todate" />
						</div>
						<div class="form-group col-md-2 col-lg-3">
							<label for="acchead">Agent Name</label> <input type="text"
								name="aName" class="form-control" id="aName" /> <input
								type="hidden" name="aId" class="form-control" id="aId" />
						</div>
					</div>

					<div class="col-lg-9 text-right" style="padding-top: 10px;">
						<a href="<%= request.getContextPath()%>/report/agentcorrection"
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
						class="table table-bordered table-striped display nowrap "
						style="width: 100%">
						<thead>
							<tr>
								<th class="col-sm-1 col-lg-12"
									style="width: 10%; position: relative; text-align: center;"
									data-field="trxnDate" nowrap="nowrap">Date</th>
								<th class="col-sm-1 col-lg-12"
									style="width: 10%; position: relative; text-align: center;"
									data-field="trxnType" nowrap="nowrap">Order No.</th>
								<th class="col-sm-1 col-lg-12"
									style="width: 10%; position: relative; text-align: center;"
									data-field="balance" nowrap="nowrap">Cust.No.</th>
								<th class="col-sm-1 col-lg-12"
									style="width: 20%; position: relative; text-align: center;"
									data-field="referenceType" nowrap="nowrap">Customer Name</th>
								<!-- 	<th class="col-sm-1 col-lg-12" style="width:10%;position:relative;text-align:center;"data-field="trxnType" nowrap="nowrap">Sheets</th> -->
								<th class="col-sm-1 col-lg-12"
									style="width: 15%; position: relative; text-align: center;"
									data-field="netAmont" nowrap="nowrap">Bill Amount</th>
								<th class="col-sm-1 col-lg-12"
									style="width: 15%; position: relative; text-align: center;"
									data-field="balance" nowrap="nowrap" align="right">OutStanding</th>
								<th class="col-sm-1 col-lg-12"
									style="width: 20%; position: relative; text-align: center;"
									data-field="netAmont" nowrap="nowrap" align="right">Total</th>
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
$(document).ready(function() {
	$('#aName').autocomplete({
		serviceUrl: '${pageContext.request.contextPath}/autocomplete/agents',
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
       		$('#aId').val(item.data);
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
	var fromdate = $('#fromdate').val();
	var todate = $('#todate').val();
	var aName = $('#aName').val();
	document.title = 'Agent Reports of '+  $('#aName').val()+' from :'  + $('#fromdate').val() +' To :'+ $('#todate').val(); 
	if(fromdate!='' && todate!='' && aName!=''){
	var reqData={
       'fromDate': $('#fromdate').val(),
       'toDate' : $('#todate').val(),
       'aId': $('#aId').val()
    };
	var tableClient = $('#reporttable').DataTable({
		"destroy": true,
		"searching":true,
		 "order": [[ 2, "asc" ]], 
 	
			"ajax": {
			"url": "${pageContext.request.contextPath}/report/adcorrection",
			"type": "POST",
			"data" : reqData,
			"success" :  function(data){
				console.log(data.length);
				if(data.length > 0){
				$.each(data, function(index, obj){
					tableClient.row.add([
						obj.orderDate,
						obj.orderId,
						obj.custId,
						obj.custName,
						/* obj.noOfSheets, */
						obj.netAmount,
						obj.creditBalance,	
						obj.total
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

		'rowCallback': function(row, data, index){
			if(data[7] != undefined){
				$(row).find('td:eq(6)').css('color', 'blue');
				}
		},

		"aoColumnDefs": [ {
		    "aTargets": [ 5 ],
		    "mRender": function (data, type, full) {
		     var formmatedvalue='';
		     if(data != null){
		    	 formmatedvalue=parseFloat(data).toFixed(2);
		     }
		      return formmatedvalue;
		    }
		},
		{
		    "aTargets": [ 5 ],
		    "mRender": function (data, type, full) {
		     var formmatedvalue='';
		     if(data != null){
		    	 formmatedvalue=parseFloat(data).toFixed(2);
		     }
		      return formmatedvalue;
		    }
		},
		{
		    "aTargets": [ 6 ],
		    "mRender": function (data, type, full) {
		     var formmatedvalue='';
		     if(data != null){
		    	 formmatedvalue=parseFloat(data).toFixed(2);
		     }
		      return formmatedvalue;
		    }
		},
		/* {
            "targets": [ 2 ],
            "visible": false
        }, */
		
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
            /* $( api.column( 3 ).footer() ).html(
                     '        Total Sheets:'
             );
            $( api.column( 4 ).footer() ).html('<td bgcolor="#00BFFF">'+
                    total+'</td>'
            ); */
           

//Outstanding
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

        // Update footer with RED COLOR FONT
          /*    $( api.column(5 ).footer() ).html('<font color="red">'+
                 'Total Outstanding :'+ parseFloat(total).toFixed(2)+'</font>'
                 
         );  */

         $( api.column(5 ).footer() ).html(
                 'Total Dues:'
                 );
         $( api.column(6 ).footer() ).html('<td bgcolor="00BFFF">'+
                parseFloat(total).toFixed(2) +'</td>'
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
		$('#mandateBtn').click();
	}
}


</script>
</html>
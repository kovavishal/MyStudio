
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
<!-- <title>Customer Vs. Business Report</title> -->
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="container-fluid" id="main">
		<div class="card">
			<div class="card-header text-center"
				style="padding-bottom: 0px; padding-top: 0px; background: skyblue; color: #30567D; font-weight: 625;">
				<jsp:include page="logout.jsp">
					<jsp:param name="page" value="SMS to Customer" />
				</jsp:include>
			</div>
			<form:form method="POST" action="customervsbusiness"
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
					<div class="row">
						<div class="col-md-4 col-lg-3">
							<label for="days">No of Days</label>
							<form:input path="days" class="form-control" id="days" />
						</div>
						<div class="col-md-4 col-lg-3">
							<label for="days">SMS Message</label>
							<textarea rows="2" cols="80" maxlength="160" id="smsText"></textarea>
						</div>
					</div>
				</div>

				<div class="col-lg-9 text-right" style="padding-top: 0px;">
					<a href="<%= request.getContextPath()%>/report/customervsbusiness"
						class="btn btn-info" style="width: 300px; font-size: 20px"
						role="button" id="refreshBtn"> <i class="fa fa-refresh"
						style='font-size: 20px; padding: 0 1em 0 0'></i>Refresh
					</a>


					<!-- 	  	<button type="button" class="btn btn-primary" id="searchBtn" onclick="report();">Search</button>
 -->

					<button type="button" class="btn btn-success" onclick="report();"
						id="searchBtn" style="width: 300px; font-size: 20px">
						<i class="fa fa-search"
							style='font-size: 20px; padding: 0 1em 0 0'></i>Search
					</button>


				</div>


				<div class="table-responsive col-lg-12" style="padding-left: 0px;">
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
								<th class="col-sm-1" data-field="description">No of Days</th>
								<!-- <th><input type="checkbox" id="id" name="check" onclick  ="checkAll()"/></th>  -->
								<th><input type="checkbox" id="selectall" name="selectall" /></th>

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

					<%-- 	 <a href="<%= request.getContextPath()%>/report/myreport" class="btn btn-info" role="button">Refresh</a> 
			--%>
					<!--  <P align="right"><button type="button" class="btn btn-info" id="smsBtn" onclick="checkAll();">Send SMS</button></P> -->
					<!-- 			 <P align="right"> <button type="button" class="btn btn-info" id="smsBtn">Send SMS</button></P>
 -->
					<P align="right">
						<button type="button" class="btn btn-warning" id="smsBtn"
							style="width: 300px; font-size: 20px">
							<i class='fa fa-envelope'
								style='font-size: 22px; color: red; padding: 0 1em 0 0'></i>Send
							SMS
						</button>
					</P>



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
			 window.alert("for all selection");
			$("#selectall").attr("checked", "checked");
		} else {
			$("#selectall").removeAttr("checked");
		}

	}); 
}); 

var today = new Date();
var dd = today.getDate();
var mm = today.getMonth() + 1; //January is 0!

var yyyy = today.getFullYear();
if (dd < 10) {
  dd = '0' + dd;
} 
if (mm < 10) {
  mm = '0' + mm;
} 
var today = dd + '/' + mm + '/' + yyyy;
function report(){
	var days = $('#days').val();
	var smsText=$('#smsText').val();
	document.title = 'Customer Vs. Business '+$('#days').val()+'  Days Report as on '+ today; 
	if(days!="" && smsText !=""){
		var reqData={
				  'report': 'customervsbusiness', 
			       'days' : $('#days').val(),
			       'smsText':$('#smsText').val() 
			       
			    };
	   
    
	var tableClient = $('#reporttable').DataTable({
		"destroy": true,
		"searching":true,
		/*  "paging":false, */
		"ajax": {
			"url": "${pageContext.request.contextPath}/report/customer_vs_business",
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
						obj.days,
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
            'targets': 6,
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
            .column(4, { page: 'current'} )
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

	}else{
		$('#mandateBtn').click();
	}	

	
	  $(document).on('click', '#smsBtn', function () {
	        var matches = [];
	        var mobile=[];
	        var balance=[];
	       // alert("from smsBtn function");
	        var checkedcollection = tableClient.$(".case:checked", { "page": "all" });
	         checkedcollection.each(function (index, elem) { 
		         
				 matches.push($(elem).val()); 
				 var row = $(this).closest("tr")[0];
	                mobile[index]= row.cells[3].innerHTML+ " ~ " + smsText;
	                //alert(mobile);
	              
	        });
	      
	        //alert(mobile.length);
 		   // mobile=mobile.replace(",", "+");
	      //  mobile=mobile.replace('%', '-');
	      //Display selected Row data in Alert Box.
           
	       goBack(mobile);
	    }); 

}
function goBack(mobile){ 
	  $("#reporttable").DataTable().destroy();  
	  $('#reporttable').DataTable({
		 "ajax":{
				type: "POST",
				 url: "${pageContext.request.contextPath}/order/customervsbusinessSMS/" + mobile, 
				/* timeout : 100000, */
				success: function(data){
					alert(" All SMS SENT SUCCESSFULLY");
				},
				complete: function () {
					document.getElementById("refreshBtn").click();
				},
				error: function(data){
					alert("ERROR: ", data);
				}
			}
		 
	   });
 }
	

 
</script>
</html>

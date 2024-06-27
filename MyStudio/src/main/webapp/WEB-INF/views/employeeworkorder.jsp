<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="staticcontent.jsp"%>
<script src="<%=request.getContextPath()%>/js/blink.js"></script>
<title>Employee Work</title>
</head>
<body>
	<%@include file="navbar.jsp"%>
	<div class="container-fluid" id="main">
		<form:form method="POST" action="employeeorder"
			modelAttribute="jobAllocation">
			<div class="card">
				<div class="card-header text-center"
					style="padding-bottom: 0px; padding-top: 0px; color: #30567D; font-weight: 625;">
					<jsp:include page="logout.jsp">
						<jsp:param name="page" value="Employee Job Updation Screen" />
					</jsp:include>
				</div>

				<div class="card-body" style="padding-left: 1px;">
					<c:if test="${message ne null and message ne ''}">
						<div class="alert alert-danger col-lg-8" align="center">
							${message}</div>
					</c:if>
					<form:form action="" modelAttribute="jobAllocation">
						<form:hidden path="custId"></form:hidden>
						<div class="table-responsive col-lg-12" style="padding-left: 0px;">
							<table id="employeetable"
								class="table table-bordered table-striped display nowrap"
								style="width: 100%; padding-left: 0px;">
								<thead>
									<tr>
										<th class="col-sm-1" data-field="id">Order</th>
										<th class="col-sm-1" data-field="name" nowrap="nowrap">Date</th>
										<th class="col-sm-1" data-field="orderType" nowrap="nowrap">Mode</th>
										<c:if test="${isDespatch}">
											<th class="col-sm-1" data-field="deliveryMode"
												nowrap="nowrap">Delivery</th>
											<th class="col-sm-1" data-field="remarks">Remarks</th>
										</c:if>
										<th class="col-sm-1" data-field="inDate" nowrap="nowrap">Job-In
											Date</th>
										<th class="col-sm-1" data-field="outDate" nowrap="nowrap">Job-Out
											Date</th>
										<th class="col-sm-1" data-field="checkDelete">Status</th>
										<th class="col-sm-1" data-field="checkDelete">Action</th>
										<c:if test="${isDespatch}">
											<th class="col-sm-1" data-field="checkDelete">CheckList</th>
										</c:if>
									</tr>
								</thead>

							</table>
						</div>
					</form:form>
				</div>
			</div>
			<c:set var="sOpen">
				<spring:eval expression="@environment.getProperty('status.open')" />
			</c:set>
		</form:form>
	</div>
</body>
<script type="text/javascript">
var tableClient;
var sOpen = '${sOpen}';
$(document).ready(function(){
	var custid = document.getElementById("custId").value;
	tableClient = $('#employeetable').DataTable({
		 "bStateSave": true,
	    "paging":false,
		"columnDefs": [
			{
				"targets": [ 0 ],
			   	"visible": true,
			   	"searchable": true
			},
		],
		"ajax": {
			"url": "${pageContext.request.contextPath}/order/joballocation/"+custid,
			"type": "POST",
			"success" :  function(data){
				if(data.length>0){
				$.each(data, function(index, obj){
					
					if(obj.isDespatch){
						tableClient.row.add([
					 	obj.id.orderId !=0 ?"<a  href='../employeeorderreview/"+obj.id.orderId+" '>"+obj.id.orderId+"</a>":obj.id.orderId,
					  /*obj.id.orderId !=0?"<a href="#" onclick='window.open("employeeorderreview"+"/"+obj.id.orderId+">Pop Up</a>*/
						obj.orderDate,
						obj.orderType ,
						obj.deliveryMode,
						obj.remarks,
						obj.strInTime,
						obj.strOutTime,
						obj.statusName, 
						obj.statusName == sOpen?"<input type='button' style='background-color:  #f44336;color:white'value='Delivery' id='id_"+obj.id+"' onclick='updaeStatus("+obj.id.orderId+","+obj.custId+");'>":obj.statusName,
					    obj.statusName == sOpen?"<input type='button' style='background-color: #4CAF50;color:white' value='Send SMS'id='id_"+obj.id+"' onclick='checkList("+obj.id.orderId+");'>":obj.statusName	
					 	//obj.statusName == sOpen?"<input type='button' style='background-color: #4CAF50' value={$obj.statusName} id='id_"+obj.id+"' onclick='checkList("+obj.id.orderId+");'>":obj.statusName			
						]).draw();
					}else{
						tableClient.row.add([
						obj.id.orderId !=0?"<a  href='../employeeorderreview/"+obj.id.orderId+"'>"+obj.id.orderId+"</a>":obj.id.orderId,
						obj.orderDate,
						obj.orderType,
						obj.strInTime,
						obj.strOutTime,
						obj.statusName,
						obj.statusName == sOpen?"<input type='button' style='background-color: #4CAF50;color:white' value='Complete' id='id_"+obj.id+"' onclick='updaeStatus("+obj.id.orderId+","+obj.custId+");'>":obj.statusName
						]).draw();
					}
					
			});
			}else{
				$('#employeetable').DataTable({
					"destroy": true,
					"searching":false,
					"language": {
				       
				    }
				});
			}
			}
		},
		'rowCallback': function(row, data, index){
			if(data[7] != undefined){
				if(data[7] == 'Completed'){
					$(row).find('td:eq(7)').css('color', 'blue');
				}
				if(data[7] == 'Open'){
					$(row).find('td:eq(7)').css('color', 'red');
				}
				
				
				if(data[2] == 'Urgent'){
				$(row).find('td:eq(2)').css('color', 'red');
				$(row).find('td:eq(2)').css('font-weight', 'bold');
				$(row).find('td:eq(2)').blink();
				
				}
			if(data[2] == 'Normal'){
				$(row).find('td:eq(2)').css('color', 'blue');
				
			}
			if(data[2] == 'Express'){
				$(row).find('td:eq(2)').css('color', 'orangered');
			}
				
						
			}else{
				if(data[5] == 'Completed'){
					$(row).find('td:eq(5)').css('color', 'blue');
				}
				if(data[5] == 'Open'){
					$(row).find('td:eq(5)').css('color', 'red');
				}
				
				if(data[2] == 'Urgent'){
				$(row).find('td:eq(2)').css('color', 'red');
				$(row).find('td:eq(2)').css('font-weight', 'bold');
				$(row).find('td:eq(2)').blink();
				
				}
				if(data[2] == 'Normal'){
					$(row).find('td:eq(2)').css('color', 'blue');
					
				}
				if(data[2] == 'Express'){
					$(row).find('td:eq(2)').css('color', 'orangered');
				}
				
			}
				
			
		},
	});

	$("#ClientTable").on('page.dt', function () {
	    var info = table.page.info();
	        $('#pageInfo').html('Currently showing page ' + (info.page + 1) + ' of ' + info.pages + ' pages.');
	    });
});



function updaeStatus(orderid,empid){
	
	$.ajax({
		type: "POST",
		url: "${pageContext.request.contextPath}/order/updatejob/" + orderid +"/"+empid,
		timeout : 100000,
		success: function(data){
			tableClient.clear().draw();
			tableClient.ajax.reload(null,false);
		},
		error: function(e){
			alert("ERROR: ", e);
		}
	});
}

function checkList(orderid){
	
	var url = "${pageContext.request.contextPath}/printCheckList/"+orderid;
  	var win = window.open(url, '_blank');
  	win.focus();
}
/* function checkListWA(orderid){
	var url = "${pageContext.request.contextPath}/printCheckListWA/"+orderid;
  	var win = window.open(url, '_blank');
  	win.focus();
} */

/* function empreview(orderid){
	var url = '${pageContext.request.contextPath}/employeeorderreview/'+orderid
  	var win = window.open(url, '_blank');
  	win.focus();	
} */



(function ($) {
    $.fn.blink = function (options) {
        var defaults = {delay: 500};
        var options = $.extend(defaults, options);
        return $(this).each(function (x, y) {
            setInterval(function () {
                if ($(y).css("visibility") === "visible") {
                    $(y).css('visibility', 'hidden');
                } else {
                    $(y).css('visibility', 'visible');
                }
            }, options.delay);
        });
    };
}(jQuery));

</script>
</html>
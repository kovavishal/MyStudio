<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="staticcontent.jsp"%>
<title>My Studio</title>
</head>
<body>
	<div class="container login-container"
		style="margin-top: 100px; max-width: 800px;">

		<div class="card bg-light border-success mb-3" style="color: navy;">
			<div class="card-header text-center">
				<h3>
					<b>Backup</b>
				</h3>
			</div>
			<c:if test="${message ne null and message ne ''}">
				<div class="alert alert-danger" align="center">${message}</div>
			</c:if>
			<form:form action="report/backup" method="POST" name="backupform">
				<div align="center">
					<div class="card-body">


						<div class="form-group row row col-sm-12 col-md-12 col-lg-5">
							<button id="Btn" type="submit"
								class="btn btn-primary btn-lg btn-block" onclick="report();">Backup</button>
							<p id="Msg" style="font-size: 25px"></p>

						</div>

					</div>
				</div>
			</form:form>
		</div>
	</div>
</body>
<script>
function report(){
	document.getElementById("Msg").innerHTML="Backup In Progress... Please Wait !!!";
	document.getElementById("Btn").hidden="true";

	document.backupform.action="${pageContext.request.contextPath}/report/backup";
	document.backupform.method="POST";
	document.backupform.submit();
}

</script>
</html>
<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="staticcontent.jsp"%>
<title>StudApp</title>
</head>
<body>
	<div class="container login-container"
		style="margin-top: 100px; max-width: 800px;">

		<div class="card bg-light border-success mb-3" style="color: navy;">
			<div class="card-header text-center">
				<h3>
					<b>My Studio</b>
				</h3>
			</div>
			<c:if test="${message ne null && message ne ''}">
				<div class="alert alert-danger" align="center">${ message}</div>
			</c:if>
			<form method="POST"
				action="<%=request.getContextPath()%>/forgotpassword">
				<div align="center">
					<div class="card-body">

						<div class="form-group row col-sm-12 col-md-12 col-lg-5">
							<label for="emailtext">User Name</label> <input type="text"
								class="form-control" name="userName" id="userName"
								aria-describedby="emailhelp" placeholder="Enter User Name">
						</div>

						<div class="form-group row row col-sm-12 col-md-12 col-lg-5">
							<button type="submit" class="btn btn-primary btn-lg btn-block">Submit</button>
						</div>

					</div>
				</div>
			</form>
		</div>
	</div>

	<script src="<%=request.getContextPath()%>/js/popper.min.js"></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
</body>
</html>
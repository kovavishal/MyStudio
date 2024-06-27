<!DOCTYPE html>
<html lang="en">
<head>
<title>StudApp</title>
<%@include file="staticcontent.jsp"%>
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
			<% if(request.getAttribute("message")!=null){ %>
			<div class="alert alert-danger" align="center">
				<%= 
				request.getAttribute("message")!=null ?  request.getAttribute("message"):""
			%>
			</div>
			<% }%>
			<form method="POST"
				action="<%=request.getContextPath()%>/updatepassword">
				<div align="center">
					<div class="card-body">
						<div class="form-group row col-sm-12 col-md-12 col-lg-5">
							<label for="emailtext">New Password</label> <input
								type="password" class="form-control" name="password"
								id="passwordtext" aria-describedby="passwordhelp"
								placeholder="Enter New Password">
						</div>

						<div class="form-group row row col-sm-12 col-md-12 col-lg-5">
							<button type="submit" class="btn btn-primary btn-lg btn-block">Submit</button>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
<!DOCTYPE html>
<html lang="en">
<head>
<%@ page isELIgnored="false"%>
<%@include file="staticcontent.jsp"%>
<title>My Studio</title>
  <style type="text/css">
    .centerDiv
    {
      width: 80%;
      height:200px;
      margin: 0 auto;
      padding-top:100px;
    }
  </style> 
</head>
<body>
<div class="container  centerDiv"  >
	<div class="card bg-light border-success mb-3" style="color: navy;">
		<div class="card-header text-center" >
			
			<h3>
				<b>Login</b>
			</h3>
		</div>
		<c:if test="${message ne null and message ne ''}">
			<div class="alert alert-danger" align="center">${message}</div>
		</c:if>
		<form:form action="login" method="POST" style="background-color:#3f3c96">
			<div align="center" >
				<div class="row" style="margin:0;" > 
					<div class="card-body" style="margin:0;background-color:#241945">
						<div class="brand">
							<img src="<%=request.getContextPath()%>/images/camera-brand.jpg"
								style="position: relative; width:100%">
						</div>
					</div>
				
					<div class="card-body ">
					
						<div class="form-group row col-sm-12 col-md-12 col-lg-8">
							<label for="emailtext" style="color:white">User Name</label> 
							<input type="text"class="form-control" name="userName" id="emailtext"
								aria-describedby="emailhelp" placeholder="Enter User Name">
						</div>
						<div class="form-group row col-sm-12 col-md-12 col-lg-8">
							<label for="emailpassword" style="color:white">Password</label> <input
								type="password" name="password" class="form-control"
								id="emailpassword" placeholder="Password">
						</div>
						<br><br>
						<div class="form-group row row col-sm-12 col-md-12 col-lg-8">
							<button type="submit" class="btn btn-primary btn-lg btn-block">Submit</button>
						</div>
						<div class="form-group row row col-sm-12 col-md-12 col-lg-5">
							<a href="forgotpassword" style="color:white" class="ForgetPwd">Forget Password?</a>
						</div>
					</div>
				</div>
				</div>
			</form:form>
		</div>
	</div>
</body>
</html>
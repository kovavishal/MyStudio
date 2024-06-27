<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div style="width: 100%; text-align: center;">
	<div style="padding: 0 .25em 0 0">${param.page}</div>

	<div style="float: right; width: 100px;">
		<a href="<%=request.getContextPath()%>/logout" style=""> <i
			class="fa fa-sign-out" aria-hidden="true" style="font-size: 15px;"></i>Logout
		</a>
	</div>

	<c:forEach var="cookies" items="${cookie}">
		<c:if test="${cookies.key=='role'}">
			<c:if
				test="${cookies.value.value ne 'Employee' && cookies.value.value ne 'Purchase' }">
				<div style="float: left; width: 100px;">
					<a href="<%=request.getContextPath()%>/home" style=""> <i
						class="fa fa-home" aria-hidden="true" style="font-size: 15px;"></i>Home
					</a>
				</div>
			</c:if>
		</c:if>
		<c:if test="${cookies.key=='name'}">
			<!-- <i style='padding:0 45em 0 0' ></i> -->
			<div style="float: right; width: 100px;">
				<font color=#b80900 style='font-size: 14px'><i
					class="fa fa-user" style='font-size: 15px;'></i>
					${cookies.value.value}</font>
			</div>
		</c:if>
	</c:forEach>



</div>
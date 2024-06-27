<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<div class="container">
		<div class="col-sm-6" style="height: 130px;">
			<div class="form-group">
				<div class='input-group date' id='datetimepicker8'>
					<input type='text' class="form-control" /> <span
						class="input-group-addon"> <span class="fa fa-calendar">
					</span>
					</span>
				</div>
			</div>
		</div>

	</div>
	<script type="text/javascript">
$(function () {
    $('#datetimepicker8').datetimepicker({
        icons: {
            time: "fa fa-clock-o",
            date: "fa fa-calendar",
            up: "fa fa-arrow-up",
            down: "fa fa-arrow-down"
        }
    });
});
</script>
</body>

</html>
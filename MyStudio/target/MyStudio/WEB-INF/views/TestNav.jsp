<script src="css/bootstrap.min.css"></script>
<script src="js/jquery.min.js"></script>
<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" style="align:left" href="#">Brand</a>
    </div>

    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" style="align:right" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav" >
      <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Dropdown <span class="caret"></span></a>
          <ul class="dropdown-menu">
            <a href="<%=request.getContextPath()%>/workorder"><li>New Order</li></a>
			<li><a href="<%=request.getContextPath()%>/order/status"><li>Order Status</li></a>
			<li><a href="<%=request.getContextPath()%>/workorder/cancel"><li>Cancel Order</li></a>
			<li><a href="<%=request.getContextPath()%>/payment/billcopy"><li>Duplicate Bill</li></a>
            <li role="separator" class="divider"></li>
            <li><a href="#">Separated link</a></li>
            <li role="separator" class="divider"></li>
            <li><a href="#">One more separated link</a></li>
          </ul>
        </li>
      </ul>
       </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>
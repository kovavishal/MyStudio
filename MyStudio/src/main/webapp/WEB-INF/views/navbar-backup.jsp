<div class="nav-side-menu">
	<div class="brand">
		<img src="<%=request.getContextPath()%>/images/camera-brand.jpg">
	</div>
	<i class="fa fa-bars fa-2x toggle-btn" data-toggle="collapse"
		data-target="#menu-content"></i>
	<div class="menu-list">
		<ul id="menu-content" class="menu-content collapse out">
			<li data-toggle="collapse" data-target="#workorder" class="collapsed">
				<a href="#">&nbsp;Work Order<span class="arrow"></span></a>
				<ul class="sub-menu collapse" id="workorder">
					<li><a href="<%=request.getContextPath()%>/workorder">Order
							Form</a></li>
					<li><a href="<%=request.getContextPath()%>/order/status">Order
							Status</a></li>
					<li><a href="<%=request.getContextPath()%>/voucher">Voucher
							Form</a></li>
					<li><a href="#">Cancel Order Form</a></li>
					<li><a href="#">Extra Bill Copy</a></li>
					<li><a href="#">Order View</a></li>
				</ul>
			</li>

			<li data-toggle="collapse" data-target="#customertab"
				class="collapsed"><a href="#">&nbsp;Customer<span
					class="arrow"></span></a>
				<ul class="sub-menu collapse" id="customertab">
					<li><a href="<%=request.getContextPath()%>/addcustomer">Add
							Customer</a></li>
				</ul></li>

			<li data-toggle="collapse" data-target="#process" class="collapsed">
				<a href="#">&nbsp;Process<span class="arrow"></span></a>
				<ul class="sub-menu collapse" id="process">
					<li><a href="#">Payroll Process</a></li>
					<li><a href="#">Staff Advance</a></li>
				</ul>
			</li>

			<li data-toggle="collapse" data-target="#workflow" class="collapsed">
				<a href="#">&nbsp;Work Flow<span class="arrow"></span></a>
				<ul class="sub-menu collapse" id="workflow">
					<li><a href="#">Job Submission</a></li>
				</ul>
			</li>

			<li data-toggle="collapse" data-target="#settings" class="collapsed">
				<a href="#">&nbsp;Settings<span class="arrow"></span></a>
				<ul class="sub-menu collapse" id="settings">
					<li><a href="#">Type of Customer</a></li>
					<li><a href="#">Type of Job</a></li>
					<li><a href="#">Type of Item</a></li>
				</ul>
			</li>

			<li data-toggle="collapse" data-target="#service" class="collapsed">
				<a href="#">&nbsp;Report<span class="arrow"></span></a>
				<ul class="sub-menu collapse" id="service">
					<li><a href="#">Daily Business</a></li>
					<li><a href="#">Daily Transaction</a></li>
					<li><a href="#">Undelivered Order</a></li>
				</ul>
			</li>

		</ul>
	</div>
</div>
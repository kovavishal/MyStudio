<div class="nav-side-menu">
	<div class="brand">
		<img src="<%=request.getContextPath()%>/images/camera-brand.jpg">
	</div>
	<i class="fa fa-bars fa-2x toggle-btn" data-toggle="collapse"
		data-target="#menu-content"></i>
	<c:if test="${cookie['role'].getValue() ne 'Employee'}">
		<div class="menu-list">
			<ul id="menu-content" class="menu-content collapse out">
				<c:if test="${cookie['role'].getValue() ne 'Purchase'}">
					<li data-toggle="collapse" data-target="#workorder"
						class="collapsed"><font color="black"> <span
							class="arrow">&nbsp;Work Order</span>
							<ul class="sub-menu collapse" id="workorder">
								<a href="<%=request.getContextPath()%>/workorder"><li>New
										Order</li></a>
								<a href="<%=request.getContextPath()%>/order/status"><li>Order
										Status</li></a>
								<a href="<%=request.getContextPath()%>/workorder/cancel"><li>Cancel
										Order</li></a>
								<a href="<%=request.getContextPath()%>/payment/billcopy"><li>Duplicate
										Bill</li></a>
							</ul></li>
					</font>
					<li data-toggle="collapse" data-target="#customertab"
						class="collapsed"><span class="arrow">&nbsp;Master
							Management</span>
						<ul class="sub-menu collapse" id="customertab">
							<a href="<%=request.getContextPath()%>/addcustomer"><li>New
									Customer</li></a>
							<c:if test="${cookie['role'].getValue() eq 'SuperAdmin'}">
								<a href="<%=request.getContextPath()%>/editcustomer"><li>Edit
										Customer</li></a>
							</c:if>
							<a href="<%=request.getContextPath()%>/employee"><li>New
									Employee</li></a>
							<a href="<%=request.getContextPath()%>/editemployee"><li>Edit
									Employee</li></a>
							<a href="<%=request.getContextPath()%>/supplier"><li>New
									Supplier</li></a>
							<a href="<%=request.getContextPath()%>/supplier/edit"><li>Edit
									Supplier</li></a>
						</ul></li>
				</c:if>
				<li data-toggle="collapse" data-target="#process" class="collapsed">
					<span class="arrow">&nbsp;Process Management</span>
					<ul class="sub-menu collapse" id="process">
						<a href="<%=request.getContextPath()%>/payment/purchase"><li>Purchase</li></a>
						<a href="<%=request.getContextPath()%>/payment/pay"><li>Payment</li></a>
						<c:if test="${cookie['role'].getValue() ne 'Purchase'}">
							<a href="<%=request.getContextPath()%>/payment/receipt"><li>Receipt</li></a>
						</c:if>
					</ul>
				</li>
				<c:if test="${cookie['role'].getValue() ne 'Purchase'}">
					<li data-toggle="collapse" data-target="#settings"
						class="collapsed"><span class="arrow">&nbsp;Settings</span>
						<ul class="sub-menu collapse" id="settings">
							<a href="<%=request.getContextPath()%>/metadata/products"><li>Add
									Products</li></a>
							<a href="<%=request.getContextPath()%>/metadata/itemtypes"><li>Add
									Items</li></a>
							<a href="<%=request.getContextPath()%>/metadata/productsizes"><li>Add
									Product Size</li></a>
							<a href="<%=request.getContextPath()%>/metadata/ratetypes"><li>Add
									Rate Type</li></a>
							<a href="<%=request.getContextPath()%>/metadata/itemsizerate"><li>Add
									Rate Amount</li></a>
							<a href="<%=request.getContextPath()%>/metadata/add"><li>Add
									Meta Data</li></a>
						</ul></li>

					<li data-toggle="collapse" data-target="#service" class="collapsed">
						<span class="arrow">&nbsp;Utility</span>
						<ul class="sub-menu collapse" id="service">
							<a href="<%=request.getContextPath()%>/report/ledger"><li>Daily
									Backup</li></a>
							<a href="<%=request.getContextPath()%>/report/daily"><li>Restore</li></a>

						</ul>
					</li>

				</c:if>
			</ul>
		</div>
	</c:if>
</div>
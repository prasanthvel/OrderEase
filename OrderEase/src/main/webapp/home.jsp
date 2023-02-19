<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>OrderEase - Home</title>

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" />

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.css" />

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/css/bootstrap.min.css" />

<link rel="stylesheet" type="text/css"
	href="https://cdn.datatables.net/1.13.2/css/jquery.dataTables.css">

<link rel="stylesheet" href="css/main.css" />

</head>

<body>
	<header>
		<div class="wrapper">
			<h1>
				OrderEase<span class="color">.</span>
			</h1>
			<nav>
				<ul>
					<li><a href="index">Home</a></li>
					<li><a href="javascript:openNewCustomerDetailForm()">Create
							Customer</a></li>
					<li><a href="javascript:loadCustomerPage()">Customer</a></li>
					<li><a href="javascript:loadProductPage()">Product</a></li>

				</ul>
			</nav>
		</div>
	</header>

	<div class="info-message" id="info-message">
		<span class="info-close-btn" onclick="closeInfoMessage('info-message')">
			<i style="font-size:24px" class="fa">&#xf057;</i>
		</span>
		<p id="info-message-p" >This is info message.</p>
	</div>

	<div id="content-main" class="content-main">

		<div class="addproduct">
			<div id="bill-amount" class="bill-total"
				style="float: right; padding-right: 20px;">
				<h4>Total Amount</h4>
				<h3 id="total-amount" style="text-align: center;">0</h3>
			</div>

			<h4>Select Products</h4>

			<form id="orderForm" autocomplete="on"
				action="javascript:insertDataToTable()">
				<div class="autocomplete" style="width: 300px;">
					<input id="productInput" type="text" name="productname"
						placeholder="Product Name" required>
				</div>
				<div class="autocomplete" style="width: 200px;">
					<input id="qty" type="number" name="quantity" placeholder="Qty"
						style="width: 100px;" required> <input type="submit" value="Add">
				</div>
			</form>
		</div>

		<!-- Order List -->
		<div>
			<table id="order-list" class="styled-table">
				<thead>
					<tr>
						<th>SNo</th>
						<th style="display: none">Pid</th>
						<th>Product Name</th>
						<th>Qty</th>
						<th>Unit</th>
						<th>MRP</th>
						<th>Price</th>
						<th>Value</th>
						<th>Action</th>
					</tr>
				</thead>
				<tbody id="product-list-table"></tbody>
			</table>
		</div>

		<!-- Customer Details -->
		<footer class="footer">
			<div class="customer-detail">
				<h4>Customer Details</h4>
				<form id="customerForm" autocomplete="on"
					action="javascript:onPlaceOrderClicked()">
					<div class="autocomplete" style="width: 300px;">
						<input id="customer-mobile" type="text" name="customer-mob"
							placeholder="Mobile Number or Email Id" required>
					</div>
					<div class="autocomplete" style="width: 200px;">
						<input type="submit" value="Place Order">
					</div>

				</form>
			</div>

		</footer>

	</div>

	<!-- Add Customer Form -->

	<div id="customer-popup" class="overlay">
		<div class="containera">
			<label for="show" class="close-btn fas fa-times" title="close"
				onClick="closeCustomerDetailForm()"></label>
			<div class="text">Customer Details</div>
			<form action="javascript:onCreateNewCustomer()" id="customer-form">
				<div class="data">
					<label>Name</label> <input id="customer-name" type="text" required>
				</div>
				<div class="data">
					<label>Mobile Number</label> <input id="mobile-no" type="text"
						required>
				</div>
				<div class="data">
					<label>Email Id</label> <input id="email-id" type="text" required>
				</div>
				<div class="data">
					<label>Address</label> <input id="address" type="text" required>
				</div>
				<span id="error"></span>
				<div class="btn">
					<div class="inner"></div>
					<button id="save-customer-btn" type="submit">Save and
						Place Order</button>
				</div>

			</form>
		</div>
	</div>

	<!-- Scripts -->
	<script
		src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/js/bootstrap.bundle.min.js"></script>

	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>

	<script type="text/javascript" charset="utf8"
		src="https://cdn.datatables.net/1.13.2/js/jquery.dataTables.js"></script>

	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.17.21/lodash.min.js"></script>

	<script src="js/script.js"></script>

	<script>
		$(document).ready(function() {
			document.title = "OrderEase - Home";
			$("#order-list").DataTable({
				order : [ [ 0, 'desc' ] ],
				columnDefs : [ {
					target : 1,
					visible : false,
					searchable : false,
				} ],
			});
		});
	</script>

</body>


</html>
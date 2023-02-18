<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
</head>
<body>

	<div class="order-detail">
		<div class="sub-detail">
			<strong>Order detail:</strong> <br>Order Id: <label
				id="order-id"></label> <br>Order Date: <label id="order-date"></label>
			<br>Total Amount: <label id="total-amount"></label> <br>Order
			Status: <label id="order-status"></label>
		</div>
		<div class="sub-detail">
			<strong>Customer detail:</strong> <br>Customer Id: <label
				id="customer-id"></label> <br>Customer Name: <label
				id="customer-name"></label> <br>Mobile Number: <label
				id="customer-mob"></label> <br>Address: <label
				id="customer-address"></label>
		</div>
	</div>

	<div>
		<table id="product-table" class="styled-table">
			<thead>
				<tr>
					<th>S.No</th>
					<th>Product Name</th>
					<th>Qty</th>
					<th>MRP</th>
					<th>Price</th>
					<th>Sub Total</th>
				</tr>
			</thead>
			<tbody id="product-list-table"></tbody>
		</table>
	</div>

	<div>
		<button class="action-btn" id="view-more-btn" onclick="getOrdersByCustomer(${customerId})">View
			more orders by Customer</button>
	</div>

	<script>
		$(document).ready(function(){
			var t = $("#product-table").DataTable({
		        columnDefs: [
		            {
		                searchable: false,
		                orderable: false,
		                targets: 0,
		            },
		        ],
		        order: [[1, 'asc']],
		    });
			
			t.on('order.dt search.dt', function () {
		        let i = 1;
		 
		        t.cells(null, 0, { search: 'applied', order: 'applied' }).every(function (cell) {
		            this.data(i++);
		        });
		    }).draw();
			getCustomerDetails(${customerId});
			getOrderDetails(${orderId});
		})
	
		function getOrderDetails(orderId){
			$.ajax({
				type: "GET",
				url: "/v1/endpoint/customer/get-order-detail",
				data: {'orderId':orderId},
				dataType: "JSON",
				success: function(msg) {
					console.log(msg);
					if(msg.status == 200){
						var order = msg.order;
						// show data in the datatable
						$("#order-id").html(order.id);						
						$("#order-date").html(order.orderDate);
						$("#total-amount").html(order.totalAmount);
						$("#order-status").html(order.orderStatus);
						
						var orderTable = $("#product-table").DataTable();
						
						order.orderDetails.forEach(function(o) {
							orderTable.row.add(
									[
										1,
										o.productName,
										o.quantity,
										o.mrp,
										o.price,
										o.subTotal
									]
								).draw();
						});

						
					}
				}
			});
		}
		
		function getCustomerDetails(customerId){
			$.ajax({
				type: "GET",
				url: "/v1/endpoint/customer/get-customer-detail",
				data: {'customerId':customerId},
				dataType: "JSON",
				success: function(msg) {
					console.log(msg);
					if(msg.status == 200){
						var customer = msg.customer;
						// show data in the datatable
						$("#customer-id").html(customer.id);
						$("#customer-name").html(customer.name);
						$("#customer-mob").html(customer.mobileNumber);
						$("#customer-address").html(customer.address);
					}
				}
			});
		}
		
		function getOrdersByCustomer(customerId){
			//window.history.pushState("", "OrderEase - Order List", "/order-list" + "?customerId=" + customerId);
			document.title = "OrderEase - Order List";
			$("#content-main").load("loadorderlist" + "?customerId=" + customerId);
		}
		
	</script>
</body>
</html>
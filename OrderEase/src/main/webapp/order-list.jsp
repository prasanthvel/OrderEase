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
			<strong><label>Customer detail:</label></strong> <br>Customer Id: <label
				id="customer-id"></label> <br>Customer Name: <label
				id="customer-name"></label> <br>Mobile Number: <label
				id="customer-mob"></label> <br>Address: <label
				id="customer-address"></label>
		</div>
	</div>

	<div>
		<table id="order-table" class="styled-table">
			<thead>
				<tr>
					<th>S.No</th>
					<th>Order Id</th>
					<th>Order Date</th>
					<th style="display: none">Product Count</th>
					<th>Total Amount</th>
					<th>Order Status</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody id="order-list-table"></tbody>
		</table>
	</div>

	<script>
		$(document).ready(function(){
			var t = $("#order-table").DataTable({
		        columnDefs: [
		            {
		                searchable: false,
		                orderable: true,
		                targets: 0,
		            },
		            {
						target : 3,
						visible : false,
						searchable : false,
					} 
		        ],
		        order: [[1, 'asc']],
		    });
			
			t.on('order.dt search.dt', function () {
		        let i = 1;
		 
		        t.cells(null, 0, { search: 'applied', order: 'applied' }).every(function (cell) {
		            this.data(i++);
		        });
		    }).draw();
			
			t.columns.adjust().draw();
			getOrdersByCustomer(${customerId});
		})
	
		function getOrdersByCustomer(customerId){
			$.ajax({
				type: "GET",
				url: "/v1/endpoint/customer/get-customer-orders",
				data: {'customerId':customerId},
				dataType: "JSON",
				success: function(msg) {
					console.log(msg);
					if(msg.status == 200){
						var orderList = msg.customer.orders;
						
						var customer = msg.customer;						
						$("#customer-id").html(customer.id);
						$("#customer-name").html(customer.name);
						$("#customer-mob").html(customer.mobileNumber);
						$("#customer-address").html(customer.address);
						
						if(orderList.length > 0){
							var orderTable = $("#order-table").DataTable();
							
							orderList.forEach(function(o) {
								orderTable.row.add(
										[
											1,
											o.id,
											o.orderDate,
											o.orderDetails.length,
											o.totalAmount,
											o.orderStatus,
											"<button class='action-btn'onClick='getOrderDetails(" +customer.id +","+o.id +");''>View</button><button class='action-btn-delete' onclick='onDeleteOrderClicked("+o.id+")'>Delete</button>"
										]
									).draw();
							});
						}
					}
					
				}
			});
		}
		
		function getOrderDetails(customerId,orderId){
			//window.history.pushState("", "OrderEase - Details", "/order-list" + "?customerId=" + customerId + "&orderId=" + orderId);
			document.title = "OrderEase - Order Detail";
			$("#content-main").load("loadorderspage" + "?customerId=" + customerId + "&orderId=" + orderId);
			//console.log("customerId - "+customerId+" orderId - "+orderId);
		}
		
		function onDeleteOrderClicked(orderId){
			console.log("orderId -  "+orderId);
		}
		
	</script>
</body>
</html>
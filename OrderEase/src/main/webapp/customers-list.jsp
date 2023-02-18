<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
</head>
<body>

	<div>
	<h2>Customers List</h2>
		<div>
			<table id="customer-table" class="styled-table">
				<thead>
					<tr>
						<th>S.No</th>
						<th>Name</th>
						<th>Mobile</th>
						<th>Email</th>
						<th>Address</th>
						<th>Action</th>
					</tr>
				</thead>
				<tbody id="customer-list-table"></tbody>
			</table>
		</div>

	</div>
	<script>
		
		$(document).ready(function(){
			var t = $("#customer-table").DataTable({
		        columnDefs: [
		            {
		                searchable: false,
		                orderable: true,
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
			
			getAllCustomers();
		})
	
		function getAllCustomers(){
			$.ajax({
				type: "GET",
				url: "/v1/endpoint/customer/get-customers",
				data: {},
				dataType: "JSON",
				success: function(msg) {
					console.log(msg);
					if(msg.status == 200){
						var customerList = msg.customerList;
						if(customerList.length > 0){
							var customerTable = $("#customer-table").DataTable();
							
							customerList.forEach(function(c) {
								customerTable.row.add(
										[
											1,
											c.name,
											c.mobileNumber,
											c.emailId,
											c.address,
											"<button class='action-btn' onClick='onLoadOrderDetailClicked("+ c.id +");''>View Orders</button>"
										]
									).draw();
							});
						}
					}
				}
			});
		}
		
		function onLoadOrderDetailClicked(customerId){
			//window.history.pushState("", "OrderEase - Order List", "/order-list" + "?customerId=" + customerId);
			document.title = "OrderEase - Order List";
			$("#content-main").load("loadorderlist" + "?customerId=" + customerId);
		}
		
	</script>
</body>
</html>
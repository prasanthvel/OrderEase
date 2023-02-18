<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
</head>
<body>


	<div class="addproduct">

		<h4>Add Products</h4>

		<form id="productForm" action="javascript:validateProductDetails()" autocomplete="off">
			<div class="autocomplete" style="width: 300px;">
				<input id="productName" type="text" name="productname"
					placeholder="Product Name" required>
			</div>
			<div class="autocomplete" style="width: 100px;">
				<input id="unit" type="text" name="unit" placeholder="Unit" required>
			</div>
			<div class="autocomplete" style="width: 100px;">
				<input id="mrp" type="text" name="mrp" placeholder="MRP" required>
			</div>
			<div class="autocomplete" style="width: 100px;">
				<input id="price" type="text" name="price" placeholder="Price"
					required>
			</div>
			<div class="autocomplete" style="width: 100px;">
				<input id="tax" type="text" name="tax" placeholder="Tax" required>
			</div>
			<div class="autocomplete" style="width: 200px;">
				<input type="submit" value="Create">
			</div>
		</form>
	</div>

	<div>
		<table id="product-table" class="styled-table">
			<thead>
				<tr>
					<th>S.No</th>
					<th>Product Name</th>
					<th>Unit</th>
					<th>MRP</th>
					<th>Price</th>
					<th>Tax</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody id="product-list-table"></tbody>
		</table>
	</div>

	<script>
		$(document).ready(function() {
			var t = $("#product-table").DataTable({
				columnDefs : [ {
					searchable : false,
					orderable : true,
					targets : 0,
				}, ],
				order : [ [ 1, 'asc' ] ],
			});

			t.on('order.dt search.dt', function() {
				let i = 1;

				t.cells(null, 0, {
					search : 'applied',
					order : 'applied'
				}).every(function(cell) {
					this.data(i++);
				});
			}).draw();

			getProductList();
		})

		function getProductList() {
			$.ajax({
				type : "GET",
				url : "/v1/endpoint/product/get-products",
				data : {},
				dataType : "JSON",
				success : function(productResponse) {
					console.log(productResponse);
					var productList = productResponse.productList;
					if (null != productList && productList.length > 0) {
						productList.forEach(function(p) {
							insertDataToTable(p);
						});
					}
				}
			});
		}

		function insertDataToTable(product) {
			var orderTable = $("#product-table").DataTable();

			orderTable.row.add(
					[
							1,
							product.productName,
							product.productUnit,
							product.mrp,
							product.price,
							product.tax,
							"<button class='action-btn-delete' onClick='onDeleteProdcut(this,"
									+ product.id + ")'>Delete</button>" ])
					.draw();
		}

		function validateProductDetails() {
			let pName = document.getElementById("productName").value;
			let pUnit = document.getElementById("unit").value;
			let pMrp = document.getElementById("mrp").value;
			let pPrice = document.getElementById("price").value;
			let pTax = document.getElementById("tax").value;

			if (!pName.trim()) {
				document.getElementById("productName").focus();
				document.getElementById("productName").select();
				showInfoMessage("Enter valid product name!");
				return false;
			}

			if (!pUnit.trim()) {
				document.getElementById("unit").focus();
				document.getElementById("unit").select();
				showInfoMessage("Enter valid product unit!");
				return false;
			}

			if (!pMrp.trim()) {
				document.getElementById("mrp").focus();
				document.getElementById("mrp").select();
				showInfoMessage("Enter valid MRP!");
				return false;
			}

			if (!pPrice.trim()) {
				document.getElementById("price").focus();
				document.getElementById("price").select();
				showInfoMessage("Enter valid Price!");
				return false;
			}

			if (!pTax.trim()) {
				document.getElementById("tax").focus();
				document.getElementById("tax").select();
				showInfoMessage("Enter valid tax percentage!");
				return false;
			}
			
			if(pPrice>pMrp){
				document.getElementById("price").focus();
				document.getElementById("price").select();
				showInfoMessage("Price should not higher then MRP!");
				return false;
			}

			var productObj = {
				"productName" : pName,
				"productUnit" : pUnit,
				"mrp" : pMrp,
				"price" : pPrice,
				"tax" : pTax
			}

			addProductToDB(productObj);
		}

		function addProductToDB(product) {
			$.ajax({
				type : "POST",
				url : "/v1/endpoint/product/add-product",
				data : JSON.stringify(product),
				dataType : "json",
				contentType : "application/json",
				success : function(addProductResponse) {
					console.log(addProductResponse);

					showInfoMessage(addProductResponse.message);

					if (addProductResponse.status == 201) {
						document.getElementById("productForm").reset();
						insertDataToTable(addProductResponse.product);
					} else if (200) {
						// check for the product in list and update
					}
				},
				error : function(jqXHR, exception) {
					var msg = '';
					if (jqXHR.status === 0) {
						msg = 'Not connect.\n Verify Network.';
					} else if (jqXHR.status == 404) {
						msg = 'Requested page not found. [404]';
					} else if (jqXHR.status == 500) {
						msg = 'Internal Server Error [500].';
					} else if (exception === 'parsererror') {
						msg = 'Requested JSON parse failed.';
					} else if (exception === 'timeout') {
						msg = 'Time out error.';
					} else if (exception === 'abort') {
						msg = 'Ajax request aborted.';
					} else {
						msg = 'Uncaught Error.\n' + jqXHR.responseText;
					}
					console.log(msg);
				}
			});
		}

		function onDeleteProdcut(currentRow, _productId) {
			console.log("id to delete -" + _productId);
			$.ajax({
				type : "GET",
				url : "/v1/endpoint/product/delete-product",
				data : {
					"productId" : _productId
				},
				dataType : "json",
				contentType : "application/json",
				success : function(deleteProductResponse) {
					console.log(deleteProductResponse);
					
					showInfoMessage(deleteProductResponse.message);
					
					if (deleteProductResponse.status == 200) {
						var table = $('#product-table').DataTable();
						table.row($(currentRow).parents('tr')).remove().draw();
					}
				},
				error : function(jqXHR, exception) {
					var msg = '';
					if (jqXHR.status === 0) {
						msg = 'Not connect.\n Verify Network.';
					} else if (jqXHR.status == 404) {
						msg = 'Requested page not found. [404]';
					} else if (jqXHR.status == 500) {
						msg = 'Internal Server Error [500].';
					} else if (exception === 'parsererror') {
						msg = 'Requested JSON parse failed.';
					} else if (exception === 'timeout') {
						msg = 'Time out error.';
					} else if (exception === 'abort') {
						msg = 'Ajax request aborted.';
					} else {
						msg = 'Uncaught Error.\n' + jqXHR.responseText;
					}
					showInfoMessage("Action failed!");
					console.log(msg);
				}
			});

		}
	</script>

</body>
</html>
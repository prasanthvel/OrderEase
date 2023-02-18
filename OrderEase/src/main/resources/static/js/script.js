var sno = 0;
let totalAmount = 0;

var productList = new Array();
var customerList = new Array();

var selectedCustomer = null;
let selectedProductId = null;
let selectedCustomerId = null;

var isSaveCustomerAndOrder = false;

var orderDetailsList = new Array();


$(document).ready(function() {
	//	loadOrderDetailsPage(202, 402) /*CAUTION : for testing purpose only Remove after usage*/
	getProductList();
	getCustomerList();
})



function getProductList() {
	$.ajax({
		type: "GET",
		url: "/v1/endpoint/product/get-products",
		data: {},
		dataType: "JSON",
		success: function(productResponse) {
			console.log(productResponse);

			if (productResponse.status == 200) {
				productList = productResponse.productList;

				$('#productInput').autocomplete(
					{
						minLength: 2,
						source: function(request, response) {
							response($.map(productList, function(
								obj, key) {

								selectedProductId = null;
								var name = obj.productName.toUpperCase();

								if (name.indexOf(request.term
									.toUpperCase()) != -1) {
									return {
										label: obj.productName, // Label for Display
										value: obj.id
										// Value
									}
								} else {
									return null;
								}
							}));
						},
						focus: function(event, ui) {
							event.preventDefault();
						},
						// Once a value in the drop down list is selected, do the following:
						select: function(event, ui) {
							event.preventDefault();
							$('#productInput').val(ui.item.label);
							selectedProductId = ui.item.value;
							console.log("selected Item "
								+ ui.item.label + "  "
								+ ui.item.value)
						}
					});
			} else {
				showInfoMessage(productResponse.message);
			}
		}
	});
}

function getCustomerList() {
	$.ajax({
		type: "GET",
		url: "/v1/endpoint/customer/get-customers",
		data: {},
		dataType: "JSON",
		success: function(customerListResponse) {
			console.log(customerListResponse);

			if (customerListResponse.status == 200) {

				customerList = customerListResponse.customerList;

				$('#customer-mobile').autocomplete(
					{
						minLength: 2,
						source: function(request, response) {
							response($.map(customerList, function(
								obj, key) {
								selectedCustomerId = null;
								var email = obj.emailId.toUpperCase();

								var mobile = obj.mobileNumber;

								if (email.indexOf(request.term
									.toUpperCase()) != -1) {
									return {
										label: obj.name + " - " + obj.emailId, // Label for Display
										value: obj.id
										// Value
									}
								} else if (mobile.toString().indexOf(request.term) != -1) {
									return {
										label: obj.name + " - " + obj.mobileNumber, // Label for Display
										value: obj.id
										// Value
									}
								} else {
									return null;
								}
							}));
						},
						focus: function(event, ui) {
							event.preventDefault();
						},
						// Once a value in the drop down list is selected, do the following:
						select: function(event, ui) {
							event.preventDefault();

							$('#customer-mobile').val(ui.item.label);
							selectedCustomerId = ui.item.value;
							// selectedProductId =  ui.item.value;
							//console.log("selected Item "+ ui.item.label + "  "+ ui.item.value);

						}
					});
			} else {
				console.log(customerListResponse.message);
			}
		}
	});
}

function insertDataToTable() {

	let x = document.getElementById("productInput").value;
	let y = document.getElementById("qty").value;


	if (selectedProductId == null) {
		alert("invalid Selection!");
		return false;
	}


	if (x == "") {
		alert("Please select product!");
		return false;
	}
	if (y == "") {
		alert("Quantity should not be empty!");
		return false;
	}

	var product = productList.find(p => p.id == selectedProductId);

	var qty = document.getElementById("qty").value;

	let subTotal = qty * product.price;

	totalAmount += subTotal;

	//console.log(selectedProductId);
	//console.log(product);

	var orderTable = $("#order-list").DataTable();

	orderTable.row.add(
		[
			++sno,
			product.id,
			product.productName,
			qty,
			product.productUnit,
			product.mrp,
			product.price,
			subTotal,
			"<button class='btn btn-delete' onClick='onDeleteRow(this)'> <span class='mdi mdi-delete mdi-24px'></span> <span class='mdi mdi-delete-empty mdi-24px'></span> <span>Delete</span> </button>"
		]
	).draw();

	document.getElementById("orderForm").reset();
	updateTotalAmount(totalAmount);
}

/**
 * @method onPlaceOrderClicked() called from UI - Home Screen
 * Validates the order list and entered user info
 * if customer is exist in DB proceed to place order
 * else @method validateEmail() called and to validate input field value
 * 
*/
function onPlaceOrderClicked() {
	var orderList = getOrderList();
	//loadCustomerPage();
	/*  TODO
	1. Check is customer exist if proceed with order placement
	else validate email id or mobile number and create new customer by collecting details and proceed to place order
	*/

	if (!Array.isArray(orderList) || !orderList.length) {
		alert(" Please add some products to the list! ");
		return false;
	}

	if (selectedCustomerId != null) {
		selectedCustomer = customerList.find(c => c.id == selectedCustomerId);
		console.log(selectedCustomer);
		placeOrder();
	} else {
		console.log("no customer found for the given details");
		validateEmail();
		// create new customer to proceed
		// handle validations here
	}
}

function getOrderList() {
	var orderTable = $("#order-list").DataTable();
	var data = orderTable.rows().data().toArray()
	console.log(data);
	var array = new Array();

	data.forEach(function(p) {
		array.push({
			"productId": p[1],
			"productName": p[2],
			"quantity": p[3],
			"productUnit": p[4],
			"mrp": p[5],
			"price": p[6],
			"subTotal": p[7]
		});
	});

	console.log(array);
	return array;
}

function onDeleteRow(currentRow) {
	var table = $('#order-list').DataTable();
	table.row($(currentRow).parents('tr')).remove().draw();

	let plist = getOrderList();
	let sum = 0;
	if (plist.length > 0) {
		plist.forEach(function(p) {
			sum += (p.price * p.qty);
		});
	}
	updateTotalAmount(sum);
}

/**
 * @method validateEmail() called by @method onPlaceOrderClicked if the entered user not found
 * this method validates the given input is emailId or mobile number
 * if input is valide the proceed to get new user inputs from the form
 * else @returns false to break the execution
 */
function validateEmail() {
	var input = document.getElementById('customer-mobile');
	var validFormat = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})|([0-9]{10})+$/;
	if (input.value == "") {
		alert("  Please enter Customer Email or Phone Number  ");
	}
	else if (validFormat.test(input.value)) {
		let text = "No customer found for given detail.\nProceed to create new customer?";
		if (confirm(text) == true) {
			console.log("open form with input");
			openCustomerDetailForm(input, isEmailId(input));
		}
	}
	else {
		alert("  Email Address / Phone number is not valid, Please provide a valid Email or phone number ");
		return false;
	}
}

/**
 * @param input - input by user to check its a valid mobile number or email id
 * @returns boolean on validation
 * @method isEmailId()  called by @method openCustomerDetailForm() inside validateEmail
 * to fill the form field after checking it is an email id or mobile number
 */
function isEmailId(input) {
	var emailFormat = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;

	if (emailFormat.test(input.value))
		return true;
	else
		return false;
}

/**
 * @method openCustomerDetailForm(@param input - valid user input from the customer details,
 * @param isEmailId - boolean value to know is it an emailId or a mobileNumber)
 * true if its an emailId else false the input is a mobile number
 * based on the boolean sets the value to form
 * Called when creating the user while "placing order"
 */
function openCustomerDetailForm(input, isEmailId) {
	isSaveCustomerAndOrder = true;
	$("#save-customer-btn").html("Save and Place Order");
	$(".overlay").toggle();

	if (isEmailId)
		document.getElementById("email-id").value = input.value;
	else
		document.getElementById("mobile-no").value = input.value;
}

/**
 * @method openCustomerDetailForm() called from front end - when "create customer"
 * is clicked in the nav menu
 * Just open the form without assigning any data in it.
 */
function openNewCustomerDetailForm() {
	isSaveCustomerAndOrder = false;
	$(".overlay").toggle();
	$("#save-customer-btn").html("Add Customer");
}

function validateUserDetails() {
	let customerName = document.getElementById("customer-name").value;
	let customerMobile = document.getElementById("mobile-no").value;
	let customerEmail = document.getElementById("email-id").value;
	let customerAddress = document.getElementById("address").value;

	var error = document.getElementById("error")

	error.textContent = "";

	// validate user inputs for data new form

	var emailValidator = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	var mobileValidator = /^([0-9]{10})+$/;

	if (!mobileValidator.test(customerMobile)) {
		console.log("no valid mobile");
		error.textContent = "Provide valid Mobile Number";
		error.style.color = "red";
		document.getElementById("mobile-no").focus();
		return false;
	}

	if (!emailValidator.test(customerEmail)) {
		console.log("no valid email");
		error.textContent = "Provide valid Email Id";
		error.style.color = "red";
		document.getElementById("email-id").focus();
		return false;
	}
	
	if (!customerName.toString().trim()) {
		console.log("no valid name");
		error.textContent = "Provide valid name";
		error.style.color = "red";
		document.getElementById("customer-name").focus();
		return false;
	}
	
	if (!customerAddress.toString().trim()) {
		console.log("no valid address");
		error.textContent = "Provide valid address";
		error.style.color = "red";
		document.getElementById("address").focus();
		return false;
	}

	return true;
	/*return custObj = {
		"name": customerName,
		"emailId": customerEmail,
		"mobileNumber": customerMobile,
		"address": customerAddress
	}*/
}

function onCreateNewCustomer() {

	if (validateUserDetails()) {
		// valid details form custObj	
		let customerName = document.getElementById("customer-name").value;
		let customerMobile = document.getElementById("mobile-no").value;
		let customerEmail = document.getElementById("email-id").value;
		let customerAddress = document.getElementById("address").value;

		var custObj = {
			"name": customerName,
			"emailId": customerEmail,
			"mobileNumber": customerMobile,
			"address": customerAddress
		}
		
		if (isSaveCustomerAndOrder) {
			placeOrder(isSaveCustomerAndOrder,custObj);
		}
		else {
			createNewCustmerApi(custObj);
		}
	}

}

function placeOrder(isSaveCustomerAndOrder,custObj) {
		
	var date = new Date();
	var current_date = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();

	var order = {
		"totalAmount": totalAmount,
		"orderDate": current_date,
		"orderStatus": "Placed",
		"orderDetails": getOrderList()
	}
	var orders = Array();
	orders.push(order);

	if(isSaveCustomerAndOrder){
		selectedCustomer = custObj; 
	}
	
	selectedCustomer["orders"] = orders;
	
	var str = JSON.stringify(selectedCustomer);
	console.log(str);


	$.ajax({
		type: "POST",
		url: "/v1/endpoint/customer/place-order",
		data: JSON.stringify(selectedCustomer),
		dataType: "json",
		contentType: "application/json",
		success: function(msg) {
			console.log(msg);
			if(isSaveCustomerAndOrder)
				closeCustomerDetailForm();
			if (msg.status == 201) {
				let text = "Order placed successfully!\nWant to view order details?";
				if (confirm(text) == true) {
					// load orders details page
					loadOrderDetailsPage(msg.customerId, msg.orderId);
				} else {
					// clear all entries for new orders					
					location.reload();
				}
			}
		},
		error: function(jqXHR, exception) {
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

function createNewCustmerApi(customerData) {
	$.ajax({
		type: "POST",
		url: "/v1/endpoint/customer/add-customer",
		data: JSON.stringify(customerData),
		dataType: "json",
		contentType: "application/json",
		success: function(msg) {
			console.log(msg);
			if (msg.status == 201 || msg.status == 200) {
				closeCustomerDetailForm();
				showInfoMessage(msg.message);
				if (null != $('#customer-mobile')) {
					getCustomerList();
				}
			}
		},
		error: function(jqXHR, exception) {
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
function closeCustomerDetailForm() {
	var overlay = document.getElementById("customer-popup");
	document.getElementById("customer-form").reset();
	if (overlay.style.display === "none") {
		overlay.style.display = "block";
	} else {
		overlay.style.display = "none";
	}
}

function updateTotalAmount(amount) {
	totalAmount = amount;
	document.getElementById("total-amount").innerHTML = amount;
}


function loadCustomerPage() {
	document.title = "OrderEase - Customer List";
	$("#content-main").load("customerslist");
}

function loadOrderDetailsPage(customerId, orderId) {
	document.title = "OrderEase - Order Details";
	$("#content-main").load("loadorderspage" + "?customerId=" + customerId + "&orderId=" + orderId);
}


function loadProductPage() {
	document.title = "OrderEase - Products";
	$("#content-main").load("products");
}

function showInfoMessage(infoMsg) {
	document.getElementById("info-message").style.display = "block";
	document.getElementById("info-message-p").innerHTML = infoMsg;
	setTimeout(closeInfoMessage, 2000);
}

function closeInfoMessage() {
	document.getElementById("info-message").style.display = "none";
}

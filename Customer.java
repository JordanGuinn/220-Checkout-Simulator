// Jordan Guinn
// April 28th, 2013
// Professor Wong

// DO NOT ADD NEW METHODS OR DATA FIELDS!

class Customer {
	private int customerID;
	private int checkoutDuration;
	private int arrivalTime;

	// New Customer Constructor
	Customer() {
		customerID = 0;
		checkoutDuration = 0;
		arrivalTime = 0;
	}

	// New Customer Constructor with variables
	Customer(int customerid, int checkoutduration, int arrivaltime) {
		customerID = customerid;
		checkoutDuration = checkoutduration;
		arrivalTime = arrivaltime;
	}

	// Method which returns total checkout time
	int getCheckoutDuration() {
		return checkoutDuration;
	}

	// Method which returns arrival time of customer
	int getArrivalTime() {
		return arrivalTime;
	}

	// Method which returns the customer's ID number
	// (in order by who enters the store first)
	int getCustomerID() {
		return customerID;
	}

	// Returns all customer data
	public String toString() {
		return "" + customerID + ":" + checkoutDuration + ":" + arrivalTime;

	}

	// Quick check of Customer class
	public static void main(String[] args) {
		Customer mycustomer = new Customer(20, 30, 40);
		System.out.println("Customer Info:" + mycustomer);

	}
}

// Jordan Guinn
// April 28th, 2013
// Professor Wong

import java.util.*;

//--------------------------------------------------------------------------
//
// Define simulation queues in a checkout area. Queues hold references to Customer 
// and Cashier objects
//
// Customer (FIFO) queue is used to hold waiting customers. If the queue is too long
// (i.e. >  customerQLimnit), customer goes away without entering customer queue
//
// There are several cashiers in a checkout area. Use PriorityQueue to 
// hold BUSY cashiers and FIFO queue to hold FREE cashiers, 
// i.e. a cashier that is FREE for the longest time should start be used first.
//
// To handle cashier in PriorityQueue, we need to define comparator 
// for comparing 2 cashier objects. Here is a constructor from Java API:
//
// 	PriorityQueue(int initialCapacity, Comparator<? super E> comparator) 
//
// For priority queue, the default compare function is "natural ordering"
// i.e. for numbers, minimum value is returned first
//
// User can define own comparator class for PriorityQueue.
// For cashier objects, we like to have smallest end busy interval time first.
//
// The following class define compare() for two cashiers :

class CompareCashier implements Comparator<Cashier> {
	// overide compare() method
	public int compare(Cashier o1, Cashier o2) {
		return o1.getEndBusyIntervalTime() - o2.getEndBusyIntervalTime();
	}
}

// DO NOT ADD NEW METHODS OR DATA FIELDS
class CheckoutArea {

	// Private data fields:

	// define one priority queue
	private PriorityQueue<Cashier> busyCashierQ;

	// define two FIFO queues
	private Queue<Customer> customerQ;
	private Queue<Cashier> freeCashierQ;

	// define customer queue limit
	private int customerQLimit;

	// Constructor
	public CheckoutArea() {
	}

	// Constructor
	public CheckoutArea(int numCashiers, int customerQlimit, int startCashierID) {
		// use ArrayDeque to construct FIFO queue objects
		customerQ = new ArrayDeque<Customer>();
		freeCashierQ = new ArrayDeque<Cashier>();

		// construct PriorityQueue object
		// overide compare() in Comparator to compare Cashier objects
		busyCashierQ = new PriorityQueue<Cashier>(numCashiers,
				new CompareCashier());

		// initialize customerQlimit
		this.customerQLimit = customerQlimit;
		// Construct Cashier objects and insert into FreeCashierQ

		for (int i = 0; i < numCashiers; i++, startCashierID++) {
			freeCashierQ.add(new Cashier(startCashierID));
		}
	}

	public Cashier removeFreeCashierQ() {
		// remove and return a free cashier
		return freeCashierQ.poll();
	}

	public Cashier removeBusyCashierQ() {
		// remove and return a busy cashier
		return busyCashierQ.poll();
	}

	public Customer removeCustomerQ() {
		// remove and return a customer
		return customerQ.remove();
	}

	public void insertFreeCashierQ(Cashier cashier) {
		// insert a free cashier
		freeCashierQ.add(cashier);
	}

	public void insertBusyCashierQ(Cashier cashier) {
		// insert a busy cashier
		busyCashierQ.add(cashier);
	}

	public void insertCustomerQ(Customer customer) {
		// insert a customer
		customerQ.add(customer);
	}

	public boolean emptyFreeCashierQ() {
		// is freeCashierQ empty?
		return freeCashierQ.isEmpty();
	}

	public boolean emptyBusyCashierQ() {
		// is busyCashierQ empty?
		return busyCashierQ.isEmpty();
	}

	public boolean emptyCustomerQ() {
		// is customerQ empty?
		return customerQ.isEmpty();
	}

	public int numFreeCashiers() {
		// get number of free cashiers
		if (freeCashierQ.isEmpty()) {
			return 0;
		} else {
			return freeCashierQ.size();
		}
	}

	public int numBusyCashiers() {
		// get number of busy cashiers
		return busyCashierQ.size();
	}

	public int numWaitingCustomers() {
		// get number of customers

		return customerQ.size();
	}

	public Cashier getFrontBusyCashierQ() {
		// get front of busy cashiers
		// "retrieve" but not "remove"
		return busyCashierQ.peek();
	}

	public boolean isCustomerQTooLong() {
		// is customerQ too long?
		return (customerQ.size() >= customerQLimit);
	}

	public void printStatistics() {
		System.out.println("\t# waiting customers  : " + numWaitingCustomers());
		System.out.println("\t# busy cashiers      : " + numBusyCashiers());
		System.out.println("\t# free cashiers      : " + numFreeCashiers());
	}

	// Quick check of CheckoutArea class
	public static void main(String[] args) {

		CheckoutArea sc = new CheckoutArea(4, 5, 1001);
		Customer c1 = new Customer(1, 18, 10);
		Customer c2 = new Customer(2, 33, 10);
		Customer c3 = new Customer(3, 21, 10);
		Customer c4 = new Customer(4, 37, 10);
		sc.insertCustomerQ(c1);
		sc.insertCustomerQ(c2);
		sc.insertCustomerQ(c3);
		System.out.println("" + sc.customerQ);
		System.out.println("Remove customer:" + sc.removeCustomerQ());
		System.out.println("Remove customer:" + sc.removeCustomerQ());
		System.out.println("Remove customer:" + sc.removeCustomerQ());

		System.out.println("" + sc.freeCashierQ);
		Cashier p1 = sc.removeFreeCashierQ();
		Cashier p2 = sc.removeFreeCashierQ();
		Cashier p3 = sc.removeFreeCashierQ();
		Cashier p4 = sc.removeFreeCashierQ();
		System.out.println("Remove free cashier:" + p1);
		System.out.println("Remove free cashier:" + p2);
		System.out.println("Remove free cashier:" + p3);
		System.out.println("Remove free cashier:" + p4);

		p1.freeToBusy(c1, 13);
		p2.freeToBusy(c2, 13);
		p3.freeToBusy(c3, 13);
		p4.freeToBusy(c4, 13);
		sc.insertBusyCashierQ(p1);
		sc.insertBusyCashierQ(p2);
		sc.insertBusyCashierQ(p3);
		sc.insertBusyCashierQ(p4);
		System.out.println("" + sc.busyCashierQ);
		p1 = sc.removeBusyCashierQ();
		p2 = sc.removeBusyCashierQ();
		p3 = sc.removeBusyCashierQ();
		p4 = sc.removeBusyCashierQ();
		System.out.println("Remove busy cashier:" + p1);
		System.out.println("Remove busy cashier:" + p2);
		System.out.println("Remove busy cashier:" + p3);
		System.out.println("Remove busy cashier:" + p4);

	}

};

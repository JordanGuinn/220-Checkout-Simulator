// Jordan Guinn
// April 28th, 2013
// Professor Wong

import java.util.*;
import java.io.*;
import java.util.Random;

// You may add new functions or data in this class 
// You may modify any functions or data members here
// You must use Customer, Cashier and CheckoutArea
// to implement your simulator

class CheckoutAreaSimulator {

	// Initialize variables
	boolean random = false;
	Cashier tempCashier;
	Customer tempCustomer;

	// Input parameters
	private int numCashiers, customerQLimit;
	private int simulationTime, dataSource;
	private int chancesOfArrival, maxCheckoutTime;

	// Data for statistics
	private int numGoAway, numServed, totalWaitingTime;

	private int customerIDCounter; // Customer's number in CheckoutAreaSimulator
	private CheckoutArea checkoutArea; // Checkout area object
	private Scanner dataFile; // Customer data from file
	private Random dataRandom; // Random customer data

	// Initialize most recent customer's information
	private boolean anyNewArrival;
	private int checkoutTime;

	// New CheckoutAreaSimulator constructor with variables
	private CheckoutAreaSimulator() {
		anyNewArrival = false;
		customerIDCounter = 0;
		numServed = 0;
		numGoAway = 0;
		totalWaitingTime = 0;

	}

	private void setupParameters() {
		// setup dataFile or dataRandom
		Scanner input = new Scanner(System.in);

		// Prompt user for simulation time, as a positive number
		System.out.print("Enter simulation time (a positive integer): ");
		simulationTime = input.nextInt();

		// Reprompt is number entered is invalid
		while (simulationTime > 10000 || simulationTime <= 0) {
			System.out.println("Please enter a number between 0 and 10,000. ");
			simulationTime = input.nextInt();
		}

		// Prompt user for maximum time customer will wait for checkout
		System.out.println("Enter maximum checkout time of customers: ");
		maxCheckoutTime = input.nextInt();

		// Reprompt if number entered is invalid
		while (maxCheckoutTime > 500 || maxCheckoutTime <= 0) {
			System.out.println("Number needs to be less than or equal to 500 and greater than "
							+ " zero: ");
			maxCheckoutTime = input.nextInt();
		}

		// Prompt user for the chances (%) of a new customer arriving
		System.out.println("Enter chances (0% < & <= 100%) of new customer: ");
		chancesOfArrival = input.nextInt();

		// Reprompt if number entered is invalid
		while (chancesOfArrival > 100 || chancesOfArrival < 1) {
			System.out.println("Number needs to be less than or equal to 100 and greater than"
							+ "zero: ");
			chancesOfArrival = input.nextInt();

		}

		// Prompt user for number of cashiers in checkout area
		System.out.println("Enter the number of cashiers: ");
		numCashiers = input.nextInt();

		// Reprompt if number entered is invalid
		while (numCashiers > 10 || numCashiers < 1) {
			System.out.println("Number needs to be less than or equal to 10 and greater than" + "zero: ");
			numCashiers = input.nextInt();
		}

		// Prompt user for maximum number of customers than can wait in line
		System.out.println("Enter customer waiting queue limit: ");
		customerQLimit = input.nextInt();

		// Reprompt if number entered is invalid
		while (customerQLimit > 50 || customerQLimit < 1) {
			System.out.println("Number needs to be less than or equal to 50 or greater than " + " zero: ");
			customerQLimit = input.nextInt();
		}

		// Prompt user for the data file or for the random generation of numbers
		System.out.println("Enter 1/0 to get data from file/Random: ");
		dataSource = input.nextInt();

		// Reprompt if number entered is invalid
		while (dataSource != 1 && dataSource != 0) {
			System.out.println("Please enter either 1 or 0.");
			dataSource = input.nextInt();
		}

		// Creates data to be used in simulation
		switch (dataSource) {

		// Creates random file
		case 0:
			random = true;
			dataRandom = new Random();
			break;

		// Reads file from the project's folder
		case 1:
			System.out.println("Enter a filename: ");
			String fileName = input.next();
			File file = new File(fileName);

			try {
				dataFile = new Scanner(file);
			} catch (Exception e) {
				System.out.println("File is not valid, try again.");
			}
			random = false;
			break;

		default:
			System.out.println("Input is invalid. Re-enter 1 or 0. ");
			System.exit(1);

		}

	}

	private void getCustomerData() {
		// get next customer data : from file or random number generator
		// set anyNewArrival and checkoutTime
		int data1, data2;

		if (random) {
			anyNewArrival = ((dataRandom.nextInt(100) + 1) <= chancesOfArrival);
			checkoutTime = dataRandom.nextInt(maxCheckoutTime) + 1;

		} else {
			
			data1 = dataFile.nextInt();
			data2 = dataFile.nextInt();

			anyNewArrival = (((data1 % 100) + 1) <= chancesOfArrival);
			checkoutTime = (data2 % maxCheckoutTime) + 1;
		}
	}

	// Simulation a checkout area
	private void doSimulation() {
		Cashier currentCashier = null;
		Customer newCustomer = null;
		Customer queueCustomer = null;

		// Initialize new CheckoutArea
		checkoutarea = new CheckoutArea(this.numCashiers, this.customerQLimit,
				1);

		// Loop simulation for time specified by user
		for (int currentTime = 0; currentTime < simulationTime; currentTime++) {
			
			// Print out time in simulation
			getCustomerData();
			System.out.println("Time: " + currentTime);
			
			
			// Explains if no new customers have arrived
			if (!anyNewArrival)
				System.out.println("\tNo new customers arrive");

			// #1: Setup customer data if a new customer has arrived
			if (anyNewArrival) {
				customerIDCounter++;
				newCustomer = new Customer(customerIDCounter, checkoutTime, currentTime);
				System.out.println("\tCustomer #" + customerIDCounter
						+ " arrives with a checkout time of " + checkoutTime
						+ " unit(s)\t");

				// #2: Determine whether or not customer will wait based on line
				if (!checkoutArea.isCustomerQTooLong()) {
					// If so, place customer in queue if there's room
					checkoutArea.insertCustomerQ(newCustomer);
					System.out.println("\tCustomer #" + customerIDCounter
							+ " is waiting in the customer queue \t");
				} else {
					// Otherwise, the customer leaves
					System.out.println("\tCustomer #" + customerIDCounter
							+ " leaves due to a long queue \t");
					numGoAway++;
				}
			}

			// #3: Service customers, thereby freeing cashiers
			for (int i = 0; i < checkoutArea.numBusyCashiers(); i++) {
				if (checkoutArea.getFrontBusyCashierQ()
						.getEndBusyIntervalTime() <= currentTime) {
					// Set cashier from busy to free
					tempCustomer = checkoutArea.getFrontBusyCashierQ()
							.busyToFree();
					// Remove busy cashier place in free queue
					tempCashier = checkoutArea.removeBusyCashierQ();
					checkoutArea.insertFreeCashierQ(tempCashier);

					// Prompt user that the customer was served and the
					// cashier is now free
					System.out.println("\tCustomer #"
							+ tempCustomer.getCustomerID()
							+ " has been serviced\t");
					System.out.println("\tCashier #"
							+ tempCashier.getCashierID() + " is now free\t");


				}

			}
			// #4: Send waiting customers to now free cashiers
			for (int i = 0; i < checkoutArea.numFreeCashiers(); i++) {
				if (checkoutArea.numFreeCashiers() != 0
						&& !checkoutArea.emptyCustomerQ()) {

					// Set current cashier to free cashier
					currentCashier = checkoutArea.removeFreeCashierQ();

					// Remove customer from queue
					queueCustomer = checkoutArea.removeCustomerQ();

					// Insert cashier to busy queue
					currentCashier.freeToBusy(queueCustomer, currentTime);
					checkoutArea.insertBusyCashierQ(currentCashier);
					numServed++;

					// Prompt the user that the customer is now being served
					System.out.println("\tCashier #"
							+ currentCashier.getCashierID()
							+ " is now serving Customer #"
							+ queueCustomer.getCustomerID() + " for "
							+ queueCustomer.getCheckoutDuration()
							+ " unit(s) \t");
				}


			}
			// Update total waiting time of all customers
			if(customerIDCounter > numServed)
				totalWaitingTime++;
			
		} // end simulation loop

		// close data-file
		if (dataSource == 1) {
			this.dataFile.close();
		}

	}

	private void printStatistics() {
		// print out simulation results
		// see the given example in project statement
		// you need to display all free and busy cashiers
		System.out.printf("\n\n");
		System.out.println("***** End of simulation report *****");
		System.out.println();

		System.out.println("\t# of total arrival customers\t:"
				+ this.customerIDCounter);
		System.out.println("\t# of customers that left\t:" + numGoAway);
		System.out.println("\t# of customers served\t\t:" + numServed);

		System.out.println();
		System.out.println("Current Cashiers info:");
		System.out.println();

		System.out.println("\t# of waiting Customers\t: "
				+ checkoutArea.numWaitingCustomers());
		System.out.println("\t# of busy Cashiers\t: "
				+ checkoutArea.numBusyCashiers());
		System.out.println("\t# of free Cashiers\t: "
				+ checkoutArea.numFreeCashiers());

		System.out.println();
		System.out.println("Total waiting time\t:" + totalWaitingTime);
		if (numServed != 0) {
			System.out.printf("Average waiting time\t:"
					+ (double) totalWaitingTime / customerIDCounter);
		} else
			System.out.printf("Average waiting time\t:" + totalWaitingTime);

		System.out.println();
		System.out.println();
		System.out.println("Busy Cashier info: ");
		System.out.println();
		for (int i = 0; i <= checkoutArea.numBusyCashiers(); i++) {
			if (checkoutArea.numBusyCashiers() != 0) {
				Cashier currentCashier = checkoutArea.removeBusyCashierQ();
				currentCashier.setEndIntervalTime(simulationTime, Cashier.BUSY);
				currentCashier.printStatistics();
			}
		}

	}

	// *** main method to run simulation ****

	public static void main(String[] args) {
		CheckoutAreaSimulator runCheckoutAreaSimulator = new CheckoutAreaSimulator();
		runCheckoutAreaSimulator.setupParameters();
		runCheckoutAreaSimulator.doSimulation();
		runCheckoutAreaSimulator.printStatistics();
	}

}

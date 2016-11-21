/*
 * Car Dealership Managment Software v0.1 
 * Developed for CS3354: Object Oriented Design and Programming.
 * Copyright: Vangelis Metsis (vmetsis@txstate.edu)
 */

package dealership;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


/**
 * This class represents a car dealership software, providing some basic operations.
 * Available operations:
 * 1. Show all existing vehicles in the database.
 * 2. Add a new vehicle to the database. 
 * 3. Delete a vehicle from a database (given its VIN).
 * 4. Search for a vehicle (given its VIN).
 * 5. Show a list of vehicles within a given price range. 
 * 6. Show list of users.
 * 7. Add a new user to the database.
 * 8. Update user info (given their id).
 * 9. Sell a vehicle.
 * 10. Show a list of completed sale transactions        
 * 11. Exit program.
 * @author vangelis
 */
public class Dealership {
    
    private final List<Vehicle> vehicleInventory; 
    private final List<User> users;
    private final List<SaleTransaction> transactions;
    
    protected int userIdCounter = 1;
    private final Scanner sc; // Used to read from System's standard input

    /**
     * Default constructor. Initializes the inventory, users, and transactions
     * tables.
     */
    public Dealership() {
        this.vehicleInventory = new ArrayList<>();
        this.users = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.sc = new Scanner(System.in);
}


    /**
     * Constructor. Initializes the inventory, users, and transactions to given values.
     */
    public Dealership(List<Vehicle> vehicleInventory, List<User> users, List<SaleTransaction> transactions) {
        this.vehicleInventory = vehicleInventory;
        this.users = users;
        this.transactions = transactions;
        this.sc = new Scanner(System.in);
    }

    public int basicSearch(String search) {
        int x = 0;

        for (Vehicle i : vehicleInventory) {
            if (i.getVin().equalsIgnoreCase(search)) {
                return x;
            }
            ++x;
        }
        return vehicleInventory.size();
    }

    public boolean vehicleMatch(String id) {
        for (Vehicle i : vehicleInventory) {
            if (i.getVin().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public Vehicle getVehicleAtPosition(int i) {
        return vehicleInventory.get(i);
    }

    public int getVehicleInvSize() {
        return vehicleInventory.size();
    }

    public boolean addVehicleDirectly(Vehicle obj) {
        return vehicleInventory.add(obj);
    }

    public boolean deleteVehicleByStr(String search) {
        for (Vehicle i : vehicleInventory) {
            if (i.getVin().equalsIgnoreCase(search)) {
                vehicleInventory.remove(i);
                return true;
            }
        }
        return false;
    }

    public int getUserDatabaseSize() { return users.size(); }

    public User getUserAtPosition(int i) { return users.get(i); }


    /**
     * This method servers as the main interface between the program and the user.
     * The method interacts with the user by printing out a set of options, and
     * asking the user to select one.
     */
    public void runSoftware() {
        int choice;
        boolean exitProgram = false;
        do {
            printMenu();
            try {
                choice = sc.nextInt();

                switch (choice) {
                    case 1: showAllVehicles(); break;
                    case 2: addNewVehicle(); break;
                    case 3: deleteVehicle(); break;
                    case 4: searchVehicle(); break;
                    case 5: showVehiclesByPrice(); break;
                    case 6: showAllUsers(); break;
                    case 7: addNewUser(); break;
                    case 8: updateUser(); break;
                    case 9: sellVehicle(); break;
                    case 10: showAllSales(); break;
                    case 11: exitProgram = true; break;
                    default: System.err.println("Please select a number between 1 and 11.");
                }
            } catch (InputMismatchException ex) {
                System.err.println("Input mismatch. Please Try again.");
                sc.nextLine();
                continue;
            } catch (BadInputException ex) {
                System.err.println("Bad input. "+ex.getMessage());
                System.err.println("Please try again.");
                sc.nextLine();
                continue;
            }
        } while (!exitProgram);
    }
    
    /**
     * Auxiliary method that prints out the operations menu.
     */
    private static void printMenu() {
        System.out.println(
                "\n1. Show all existing vehicles in the database. \n" +
                "2. Add a new vehicle to the database. \n" +
                "3. Delete a vehicle from a database (given its VIN).\n" +
                "4. Search for a vehicle (given its VIN).\n" +
                "5. Show a list of vehicles within a given price range. \n" +
                "6. Show list of users.\n" +
                "7. Add a new user to the database.\n" +
                "8. Update user info (given their id).\n" +
                "9. Sell a vehicle.\n" +
                "10. Show a list of completed sale transactions\n" +        
                "11. Exit program.\n");
    }

    /**
     * This method allows the user to enter a new vehicle to the inventory 
     * database.
     * @param sc The scanner object used to read user input.
     * @throws Dealership.BadInputException
     */
    public void addNewVehicle() throws BadInputException {
        System.out.println("Select vehicle type:\n"
                + "1. Passenger car\n"
                + "2. Truck\n"
                + "3. Motorcycle");
        int vehicleType = sc.nextInt();
        if (vehicleType < 1 || vehicleType > 3)
            throw new BadInputException("Legal vehicle type values: 1-3.");
        
        sc.nextLine();
        System.out.print("\nEnter VIN (string): ");
        String vin = sc.nextLine();
        if (vin.length() > 10)
            throw new BadInputException("VIN should not be more that 10 characters long.");

        System.out.print("\nEnter Make (string): ");
        String make = sc.nextLine();

        System.out.print("\nEnter Model (string): ");
        String model = sc.nextLine();

        System.out.print("\nEnter Year (int): ");
        int year = sc.nextInt();

        System.out.print("\nEnter Mileage (int): ");
        int mileage = sc.nextInt();
        if (mileage < 0)
            throw new BadInputException("Mileage cannot be negative.");

        System.out.print("\nEnter Price (float): ");
        float price = sc.nextFloat();
        if (price < 0.0f)
            throw new BadInputException("Price cannot be negative.");

        if (vehicleType == 1) {
            sc.nextLine();
            System.out.print("\nEnter body style (string): ");
            String bodyStyle = sc.nextLine();
            
            PassengerCar car = new PassengerCar(vin, make, model, year, mileage, 
                    price, bodyStyle);
            vehicleInventory.add(car);
        } else if (vehicleType == 2) {
            System.out.print("\nEnter max load weight (lb), (float): ");
            float maxLoad = sc.nextFloat();
            if (maxLoad < 0.0f)
                throw new BadInputException("Max load cannot be negative.");
            
            System.out.print("\nEnter truck length (ft), (float): ");
            float tLength = sc.nextFloat();
            if (tLength < 0.0f)
                throw new BadInputException("Truck length cannot be negative.");
            
            Truck tr = new Truck(vin, make, model, year, mileage, price, 
                    maxLoad, tLength);
            vehicleInventory.add(tr);
        } else if (vehicleType == 3) {
            sc.nextLine();
            System.out.print("\nEnter motorcycle type (string): ");
            String type = sc.nextLine();
            
            System.out.print("\nEnter engine displacement: ");
            int displacement = sc.nextInt();
            if (displacement < 0.0f)
                throw new BadInputException("Displacement cannot be negative.");
            
            Motorcycle mc = new Motorcycle(vin, make, model, year, mileage, 
                    price, type, displacement);
            vehicleInventory.add(mc);
        } else {
            System.out.println("Unknown vehicle type entered. Please try again.");
        }
    }

    
    public void searchVehicle() {
        sc.nextLine();
        System.out.print("\nEnter VIN of vehicle to search for (string): ");
        String vin = sc.nextLine();
        
        List<Vehicle> matchingVehicle = new ArrayList<Vehicle>();
        for (Vehicle v : vehicleInventory) {
            if (v.getVin().equals(vin)) {
                matchingVehicle.add(v);
                showVehicles(matchingVehicle);
                return;
            }
        }
        System.out.println("Vehicle with VIN " + vin + " not found in the database"); 
    }
    
    
    /**
     * This method allows the user to delete a vehicle from the inventory database.
     * @param sc The scanner object used to read user input.
     */
    public void deleteVehicle() {
        sc.nextLine();
        System.out.print("\nEnter VIN of vehicle to delete (string): ");
        String vin = sc.nextLine();
        
        for (Vehicle v : vehicleInventory) {
            if (v.getVin().equals(vin)) {
                vehicleInventory.remove(v);
                break;
            }
        }
    }
    
    /**
     * Auxilary private method to print out a list of vehicles in a formatted manner.
     */
    private void showVehicles(List<Vehicle> vehicles) {
        System.out.println("---------------------------------------------------"
                + "-------------------------------------------------------");
        System.out.format("| %12s | %12s | %8s | %8s | %6s | %9s | %10s | %17s | %n", 
                "VEHICLE TYPE", "VIN", "MAKE", "MODEL", "YEAR", "MILEAGE", "PRICE", "EXTRA DETAILS");
        System.out.println("---------------------------------------------------"
                + "-------------------------------------------------------");
        for (Vehicle v : vehicles) {
            System.out.println(v.getFormattedText());
        }
        System.out.println("---------------------------------------------------"
                + "-------------------------------------------------------");
    }

    /**
     * This method prints out all the vehicle currently in the inventory, in a
     * formatted manner.
     */
    public void showAllVehicles() {
        showVehicles(vehicleInventory);
    }

    /**
     * This method allows the user to search for vehicles within a price range.
     * The list of vehicles found is printed out.
     * @param sc The scanner object used to read user input.
     */
    public void showVehiclesByPrice() throws BadInputException {
        System.out.print("\nEnter low price value (float): ");
        float lowValue = sc.nextFloat();
        if (lowValue < 0.0f)
                throw new BadInputException("Low price cannot be negative.");
        
        System.out.print("\nEnter high price value (float): ");
        float highValue = sc.nextFloat();
        if (highValue < 0.0f)
                throw new BadInputException("High price cannot be negative.");
                
        System.out.println("\nSelect vehicle type:\n"
                + "1. Passenger car\n"
                + "2. Truck\n"
                + "3. Motorcycle");
        int vehicleType = sc.nextInt();
        if (vehicleType < 1 || vehicleType > 3)
            throw new BadInputException("Legal vehicle type values: 1-3.");
        
        ArrayList<Vehicle> matchingVehicles = new ArrayList<Vehicle>();
        for (Vehicle v : vehicleInventory) {
            if (v.getPrice() >= lowValue && v.getPrice() <= highValue) {
                if (vehicleType == 1 && v instanceof PassengerCar)
                    matchingVehicles.add(v);
                else if (vehicleType == 2 && v instanceof Truck)
                    matchingVehicles.add(v);
                else if (vehicleType == 3 && v instanceof Motorcycle)
                    matchingVehicles.add(v);
            }
        }
        
        if (matchingVehicles.size() == 0)
            System.out.println("\nNo matching vehicles found.");
        else
            showVehicles(matchingVehicles);
    }

    /**
     * This method allows a new user to be added to the database.
     * @param sc The scanner object used to read user input.
     * @throws Dealership.BadInputException
     */
    public void addNewUser() throws BadInputException {
        System.out.println("Select user type:\n"
                + "1. Customer\n"
                + "2. Employee");
        int userType = sc.nextInt();
        if (userType < 1 || userType > 3)
            throw new BadInputException("Legal user type values: 1-2.");
        
        sc.nextLine();
        System.out.print("\nEnter first name (string): ");
        String firstName = sc.nextLine();
        
        System.out.print("\nEnter last name (string): ");
        String lastName = sc.nextLine();
        
        if (userType == 1) {
            System.out.print("\nEnter phone number (string): ");
            String phoneNumber = sc.nextLine();
            
            System.out.print("\nEnter driver license number (int): ");
            int dlnumber = sc.nextInt();
            if (dlnumber < 0)
                throw new BadInputException("Driver license number cannot be negative.");
            
            users.add(new Customer(userIdCounter++, firstName, lastName, 
                    phoneNumber, dlnumber));
        } else if (userType == 2) {
            System.out.print("\nEnter monthly salary (float): ");
            float monthlySalary = sc.nextFloat();
            if (monthlySalary < 0.0f)
                throw new BadInputException("Monthly salary cannot be negative.");
            
            System.out.print("\nEnter bank account number (int): ");
            int bankAccNumber = sc.nextInt();
            if (bankAccNumber < 0)
                throw new BadInputException("Driver license number cannot be negative.");
            
            users.add(new Employee(userIdCounter++, firstName, lastName, 
                    monthlySalary, bankAccNumber));
        } else {
            System.out.println("Unknown user type. Please try again.");
        }
    }

    /**
     * This method can be used to update a user's information, given their user ID.
     * @param sc The scanner object used to read user input.
     * @throws Dealership.BadInputException
     */
    public void updateUser() throws BadInputException {
        System.out.print("\nEnter user ID: ");
        int userID = sc.nextInt();
        
        User user = null;
        for (User u : users) {
            if (u.getId() == userID)
                user = u;
        }
        
        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        
        sc.nextLine();
        System.out.print("\nEnter first name (string): ");
        String firstName = sc.nextLine();
        
        System.out.print("\nEnter last name (string): ");
        String lastName = sc.nextLine();
        
        if (user instanceof Customer) {
            System.out.print("\nEnter phone number (string): ");
            String phoneNumber = sc.nextLine();
            
            System.out.print("\nEnter driver license number (int): ");
            int dlnumber = sc.nextInt();
            if (dlnumber < 0)
                throw new BadInputException("Driver license number cannot be negative.");
            
            user.setFirstName(firstName);
            user.setLastName(lastName);
            ((Customer)user).setPhoneNumber(phoneNumber);
            ((Customer)user).setDriverLicenceNumber(dlnumber);
            
        } else { //User is an employee
            System.out.print("\nEnter monthly salary (float): ");
            float monthlySalary = sc.nextFloat();
            if (monthlySalary < 0.0f)
                throw new BadInputException("Monthly salary cannot be negative.");
            
            System.out.print("\nEnter bank account number (int): ");
            int bankAccNumber = sc.nextInt();
            if (bankAccNumber < 0)
                throw new BadInputException("Driver license number cannot be negative.");
            
            user.setFirstName(firstName);
            user.setLastName(lastName);
            ((Employee)user).setMonthlySalary(monthlySalary);
            ((Employee)user).setBankAccountNumber(bankAccNumber);
        }
    }

    /**
     * Prints out a list of all users in the database.
     */
    public void showAllUsers() {
        System.out.println("---------------------------------------------------"
                + "------------------------------------------");
        System.out.format("| %10s | %9s | %12s | %12s | %25s          | %n", 
                "USER TYPE", "USER ID", "FIST NAME", "LAST NAME", "OTHER DETAILS");
        System.out.println("---------------------------------------------------"
                + "------------------------------------------");
        for (User u : users) {
            System.out.println(u.getFormattedText());
        }
        System.out.println("---------------------------------------------------"
                + "------------------------------------------");
    }

    /**
     * This method is used to complete a vehicle sale transaction.
     * @param sc The scanner object used to read user input.
     * @throws Dealership.BadInputException
     */
    public void sellVehicle() throws BadInputException {
        System.out.print("\nEnter customer ID (int): ");
        int customerId = sc.nextInt();
        //Check that the customer exists in database
        boolean customerExists = false;
        for (User u : users) {
            if (u.getId() == customerId)
                customerExists = true;
        }
        if (!customerExists) {
            System.out.print("\nThe customer ID you have entered does not exist in the database.\n"
                    + "Please add the customer to the database first and then try again.");
            return;
        }
        
        System.out.print("\nEnter employee ID (int): ");
        int employeeId = sc.nextInt();
        //Check that the employee exists in database
        boolean employeeExists = false;
        for (User u : users) {
            if (u.getId() == employeeId)
                employeeExists = true;
        }
        if (!employeeExists) {
            System.out.print("\nThe employee ID you have entered does not exist in the database.\n"
                    + "Please add the employee to the database first and then try again.");
            return;
        }
        
        sc.nextLine();
        System.out.print("\nEnter VIN (string): ");
        String vin = sc.nextLine();
        //Check that the vehicle exists in database
        Vehicle v = findVehicle(vin);
        if (v == null) {
            System.out.print("\nThe vehicle with the VIN you are trying to sell "
                    + "does not exist in the database. Aborting transaction.");
            return;
        }
        
        Date currentDate = new Date(System.currentTimeMillis());
        
        System.out.print("\nEnter sale price (float): ");
        float price = sc.nextFloat();
        if (price < 0.0f)
            throw new BadInputException("Sale price cannot be negative.");
        
        SaleTransaction trans = new SaleTransaction(customerId, employeeId, vin, 
                currentDate, price);
        transactions.add(trans);
        vehicleInventory.remove(v); //Sold vehicles are automatically removed from the inventory.
        
        System.out.println("\nTransaction Completed!");
    }
    
    /**
     * Prints out a list of all recorded transactions.
     */
    public void showAllSales() {
        for (SaleTransaction sale : transactions) {
            System.out.println(sale.toString());
        }
    }
    
    /**
     * Auxiliary method used to find a vehicle in the database, given its
     * VIN number.
     * @param vin
     * @return The vehicle found, or otherwise null.
     */
    public Vehicle findVehicle(String vin) {
        for (Vehicle v : vehicleInventory) {
            if (v.getVin().equals(vin))
                return v;
        }
        return null;
    }
    
    /**
     * This method is used to read the database from a file, serializable objects.
     * @return A new Dealership object.
     */
    @SuppressWarnings("unchecked") // This will prevent Java unchecked operation warning when
    // converting from serialized Object to Arraylist<>
    public static Dealership readDatabase() {
        System.out.print("Reading database...");
        Dealership cds = null;

        // Try to read existing dealership database from a file
        InputStream file = null;
        InputStream buffer = null;
        ObjectInput input = null;
        try {
            file = new FileInputStream("Dealership.ser");
            buffer = new BufferedInputStream(file);
            input = new ObjectInputStream(buffer);
            
            // Read serialized data
            List<Vehicle> vehicleInventory = (ArrayList<Vehicle>) input.readObject();
            List<User> users = (ArrayList<User>) input.readObject();
            List<SaleTransaction> transactions = (ArrayList<SaleTransaction>) input.readObject();
            cds = new Dealership(vehicleInventory, users, transactions);
            cds.userIdCounter = input.readInt();
            
            input.close();
        } catch (ClassNotFoundException ex) {
            System.err.println(ex.toString());
        } catch (FileNotFoundException ex) {
            System.err.println("Database file not found.");
        } catch (IOException ex) {
            System.err.println(ex.toString());
        } finally {
            close(file);
        }
        System.out.println("Done.");
        
        return cds;
    }
    
    /**
     * This method is used to save the Dealership database as a 
     * serializable object.
     * @param cds
     */
    public void writeDatabase() {
        System.out.print("Writing database...");
        //serialize the database
        OutputStream file = null;
        OutputStream buffer = null;
        ObjectOutput output = null;
        try {
            file = new FileOutputStream("Dealership.ser");
            buffer = new BufferedOutputStream(file);
            output = new ObjectOutputStream(buffer);
            
            output.writeObject(vehicleInventory);
            output.writeObject(users);
            output.writeObject(transactions);
            output.writeInt(userIdCounter);
            
            output.close();
        } catch (IOException ex) {
            System.err.println(ex.toString());
        } finally {
            close(file);
        }
        System.out.println("Done.");
    }
    
    /**
     * Auxiliary convenience method used to close a file and handle possible
     * exceptions that may occur.
     * @param c
     */
    public static void close(Closeable c) {
        if (c == null) {
            return;
        }
        try {
            c.close();
        } catch (IOException ex) {
            System.err.println(ex.toString());
        }
    }
    
    /**
     * The main method of the program.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Dealership cds = readDatabase();

        // If you could not read from the file, create a new database.
        if (cds == null) {
            System.out.println("Creating a new database.");
            cds = new Dealership();
        }

        cds.runSoftware();
        cds.writeDatabase();
    }
}

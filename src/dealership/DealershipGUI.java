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
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.awt.*;         // basic awt classes
import java.awt.event.*;   // event classes
import javax.swing.*;      // swing classes

/**
 * Created by Valar Dohaeris on 11/19/2016.
 */
public class DealershipGUI {
    private final java.util.List<Vehicle> vehicleInventory;
    private final java.util.List<User> users;
    private final java.util.List<SaleTransaction> transactions;
    //FIXME NEED List for logging transactions???

    private int uniqueID = 1; // FIXME -- need for userID

    /**
     * Default constructor.
     */
    public Dealership() {
        this.vehicleInventory = new ArrayList<Vehicle>();
        this.users = new ArrayList<User>();
        this.transactions = new ArrayList<SaleTransaction>();
    }

    /**
     * Constructor. Initializes the inventory, users, and transactions to given values.
     */
    public Dealership(java.util.List<Vehicle> vehicleInventory,
                      java.util.List<User> users,
                      java.util.List<SaleTransaction> transactions) {
        this.vehicleInventory = vehicleInventory;
        this.users = users;
        this.transactions = transactions;
    }

    public void menuOptions() {
        // FIXME - implement JFrame with appropiate buttons/events
    }

    public void addVehicle() throws BadInputException { // FIXME may need to create a new exception ??
        // FIXME - implement add vehicle with new JFrame
    }

    public void searchVehicle() {
        // FIXME - implement vehicle search by VIN
    }

    public void deleteVehicle() {
        // FIXME -- implement deletion of a vehicle
    }

    public void displayVehicleInventory() {
        // FIXME -- implement
    }

    public void displayVehiclesByPrice() {
        // FIXME -- implement
    }

    public void addNewUser() {
        // FIXME
    }

    public void updateUser() {
        // FIXME
    }

    public void displayUserList() {
        // FIXME
    }

    public void sellVehicle() {
        /// FIXME
    }

    public void showSales() {
        // FIXME
    }

    public Vehicle searchByVIN() {
        // FIXME
        return null;
    }

    /**
     * This method is used to save the Dealership database as a
     * serializable object.
     * @param cds
     */
    public void writeDatabase() {
        System.out.print("Writing database..."); // FIXME REPLACE THIS
        //serialize the database
        OutputStream file = null;
        OutputStream buffer = null;
        ObjectOutput output = null;
        try {
            file = new FileOutputStream("Dealership.ser"); // FIXME -- make global
            buffer = new BufferedOutputStream(file);
            output = new ObjectOutputStream(buffer);

            output.writeObject(vehicleInventory);
            output.writeObject(users);
            output.writeObject(transactions);
            output.writeInt(uniqueID);

            output.close();
        } catch (IOException ex) {
            System.err.println(ex.toString());
        } finally {
            close(file);
        }
        System.out.println("Done."); // FIXME
    }

    @SuppressWarnings("unchecked")
    public static DealershipGUI readDatabase() {
        System.out.print("Reading database..."); // FIXME REPLACE THIS with message dialog?
        DealershipGUI cds = null;

        // Try to read existing dealership database from a file
        InputStream file = null;
        InputStream buffer = null;
        ObjectInput input = null;
        try {
            file = new FileInputStream("Dealership.ser"); // FIXME could make a global constant
            buffer = new BufferedInputStream(file);
            input = new ObjectInputStream(buffer);

            // Read serialized data
            java.util.List<Vehicle> vehicleInventory = (ArrayList<Vehicle>) input.readObject();
            java.util.List<User> users = (ArrayList<User>) input.readObject();
            java.util.List<SaleTransaction> transactions = (ArrayList<SaleTransaction>) input.readObject();
            cds = new DealershipGUI(vehicleInventory, users, transactions);
            cds.uniqueID = input.readInt();

            input.close();
        } catch (ClassNotFoundException ex) {
            System.err.println(ex.toString()); // FIXME replace with logging?
        } catch (FileNotFoundException ex) {
            System.err.println("Database file not found."); // FIXME replace with logging?
        } catch (IOException ex) {
            System.err.println(ex.toString()); // FIXME  replace with logging?
        } finally {
           close(file);
        }
        System.out.println("Done."); // FIXME REPLACE..

        return cds;
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

    public static void main (String[] args) {
        DealershipGUI j = readDatabase();

        // If you could not read from the file, create a new database.
        if (j == null) { //FIXME possible logging event
            System.out.println("Creating a new database.");
            j = new Dealership();
        }

        j.menuOptions(); // FIXME use invoke later?
        j.writeDatabase();
    }
}

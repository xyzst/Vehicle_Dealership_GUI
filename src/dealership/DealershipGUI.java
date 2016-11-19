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

import static java.lang.Thread.sleep;

/**
 * @author Darren Rambaud
 */
public class DealershipGUI {
    private final java.util.List<Vehicle> vehicleInventory;
    private final java.util.List<User> users;
    private final java.util.List<SaleTransaction> transactions;
    //FIXME NEED List for logging transactions???
    private static JFrame main = new JFrame("Dealership Management Software v1.0");
    private static JTextField field = new JTextField(35);
    private int uniqueID = 1; // FIXME -- need for userID

    /**
     * Default constructor.
     */
    public DealershipGUI() {
        this.vehicleInventory = new ArrayList<Vehicle>();
        this.users = new ArrayList<User>();
        this.transactions = new ArrayList<SaleTransaction>();
    }

    /**
     * Constructor. Initializes the inventory, users, and transactions to given values.
     */
    public DealershipGUI(java.util.List<Vehicle> vehicleInventory,
                      java.util.List<User> users,
                      java.util.List<SaleTransaction> transactions) {
        this.vehicleInventory = vehicleInventory;
        this.users = users;
        this.transactions = transactions;
    }


    public void menuOptions() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(11, 0));
        JLabel intro = new JLabel("Please select an option below:");

        intro.setHorizontalAlignment(SwingConstants.CENTER);

        field.setText("STATUS: waiting for user input ...");

        JButton existing = new JButton("Show all existing vehicles in the database"); // FIXME use a looping construct to create JButtons/ActionListeners??
        existing.setHorizontalAlignment(SwingConstants.LEFT);
        JButton addvehicle = new JButton("Add a new vehicle to the database");
        addvehicle.setHorizontalAlignment(SwingConstants.LEFT);
        JButton vehicledel = new JButton("Delete a vehicle from the database (given it's VIN)");
        vehicledel.setHorizontalAlignment(SwingConstants.LEFT);
        JButton searchvehicle = new JButton("Search for a vehicle (given it's VIN)");
        searchvehicle.setHorizontalAlignment(SwingConstants.LEFT);
        JButton listvehicle = new JButton("Show a list of vehicles within a given price range");
        listvehicle.setHorizontalAlignment(SwingConstants.LEFT);
        JButton showuser = new JButton("Show list of users");
        showuser.setHorizontalAlignment(SwingConstants.LEFT);
        JButton adduser = new JButton("Add a new user to the database");
        adduser.setHorizontalAlignment(SwingConstants.LEFT);
        JButton updateuser = new JButton("Update user info (given their ID).");
        updateuser.setHorizontalAlignment(SwingConstants.LEFT);
        JButton sellvehicle = new JButton("Sell a vehicle");
        sellvehicle.setHorizontalAlignment(SwingConstants.LEFT);
        JButton showtransax = new JButton("Show a list of completed sale transactions");
        showtransax.setHorizontalAlignment(SwingConstants.LEFT);
        JButton exit = new JButton("Exit program");
        exit.setHorizontalAlignment(SwingConstants.LEFT);

        existing.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                field.setText("STATUS: in display inventory ...");
                displayVehicleInventory();
            }
        });

        addvehicle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                field.setText("STATUS: in adding vehicle ...");
            }
        });

        vehicledel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                field.setText("STATUS: in deleting vehicle ...");
            }
        });

        searchvehicle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                field.setText("STATUS: in searching inventory ...");
            }
        });

        listvehicle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                field.setText("STATUS: in listing vehicle criteria search ...");
            }
        });

        showuser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                field.setText("STATUS: in displaying user database ...");
            }
        });

        adduser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                field.setText("STATUS: adding user ...");
            }
        });

        updateuser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                field.setText("STATUS: updating user ...");
            }
        });

        sellvehicle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                field.setText("STATUS: selling vehicle ...");
            }
        });

        showtransax.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                field.setText("STATUS: showing transactions ...");
            }
        });

        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                field.setText("STATUS: bye! Saving session ...");
            }
        });

        main.setContentPane(mainPanel);
        mainPanel.add(intro, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(field, BorderLayout.SOUTH);
        buttonPanel.add(existing);
        buttonPanel.add(addvehicle);
        buttonPanel.add(vehicledel);
        buttonPanel.add(searchvehicle);
        buttonPanel.add(listvehicle);
        buttonPanel.add(showuser);
        buttonPanel.add(adduser);
        buttonPanel.add(updateuser);
        buttonPanel.add(sellvehicle);
        buttonPanel.add(showtransax);
        buttonPanel.add(exit);

        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.pack();
        main.setVisible(true);
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
        String[] header = {"Vehicle Type", "VIN", "Make", "Model", "Year",
                                "Mileage", "Price", "Miscellaneous Details"};

        for (int i = 0; i < vehicleInventory.size(); ++i) {
            
        }

        JFrame display = new JFrame("Current Inventory");
        display.setVisible(true);
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.pack();

        JTable inventory = new JTable(header); // FIXME
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
            j = new DealershipGUI();
        }

        j.menuOptions(); // FIXME use invoke later?
        j.writeDatabase();
    }
}

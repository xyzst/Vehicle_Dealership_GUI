package dealership;

import javax.swing.*;
import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class DealershipGUI extends JFrame {
    private static Dealership dummy = Dealership.readDatabase(),
                              db = (dummy == null) ? new Dealership() : dummy;

    private DealershipGUI (String title) {
        super(title);
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(11, 0));
        JLabel intro = new JLabel("Please select an option below:");
        JTextField field = new JTextField(35);
        intro.setHorizontalAlignment(SwingConstants.CENTER);
        field.setText("STATUS: waiting for user input ...");

        JButton existing = new JButton("Show all existing vehicles in the database");
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
                Thread qThread = new Thread() {
                    public void run() {
                        displayInventory();
                    }
                };
                qThread.start();
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
                Thread qThread = new Thread() {
                    public void run() {
                        terminateSession();
                    }
                };
                qThread.start();
                field.setText("STATUS: bye! Saving session ...");
            }
        });

        this.setContentPane(mainPanel);
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

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
    }

    public void displayInventory() {
        String[] header = {"TYPE", "VIN", "MAKE", "MODEL", "YEAR", "MILEAGE", "PRICE", "BODY STYLE", "TYPE",
                            "DISPLACEMENT (cc)", "LENGTH (ft)", "MAX LOAD WEIGHT (lb)"};
        String body_style, type, displ, load, length;
        Object[][] data = new Object[db.getVehicleInvSize()][12];

        for (int i = 0; i < db.getVehicleInvSize(); ++i) {
            if(db.getVehicleAtPosition(i) instanceof PassengerCar) {
                data[i][0] = "Passenger";
                body_style = ((PassengerCar)db.getVehicleAtPosition(i)).getBodyStyle();
                data[i][7] = body_style;
                data[i][8] = "N/A";
                data[i][9] = "N/A";
                data[i][10] = "N/A";
                data[i][11] = "N/A";
            }
            else if (db.getVehicleAtPosition(i) instanceof Motorcycle) {
                data[i][0] = "Motorcycle";
                type = ((Motorcycle)db.getVehicleAtPosition(i)).getType();
                displ = Integer.toString(((Motorcycle)db.getVehicleAtPosition(i)).getDisplacement());
                data[i][7] = "N/A";
                data[i][8] = type;
                data[i][9] = displ;
                data[i][10] = "N/A";
                data[i][11] = "N/A";
            }
            else {
                data[i][0] = "Truck";
                load = Float.toString(((Truck)db.getVehicleAtPosition(i)).getMaxLoadWeight());
                length = Float.toString(((Truck)db.getVehicleAtPosition(i)).getLength());
                data[i][7] = "N/A";
                data[i][8] = "N/A";
                data[i][9] = "N/A";
                data[i][10] = length;
                data[i][11] = load;
            }

            data[i][1] = db.getVehicleAtPosition(i).getVin();
            data[i][2] = db.getVehicleAtPosition(i).getMake();
            data[i][3] = db.getVehicleAtPosition(i).getModel();
            data[i][4] = db.getVehicleAtPosition(i).getYear();
            data[i][5] = db.getVehicleAtPosition(i).getMileage();
            data[i][6] = db.getVehicleAtPosition(i).getPrice();
        }

        JFrame display = new JFrame("Inventory List");
        final JTable table = new JTable(data, header);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        table.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(table);
        display.add(scrollPane);

        display.pack();
        display.setVisible(true);
        display.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void terminateSession() {
        db.writeDatabase();
        System.exit(0);
    }

//    public void showDialog(Component f) {
//        JOptionPane.showMessageDialog(f, "Session has been successfully saved!");
//    }

    public static void main (String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DealershipGUI exe = new DealershipGUI("Dealership Management Software v1.0");
                exe.setVisible(true);
            }
        });
    }
}

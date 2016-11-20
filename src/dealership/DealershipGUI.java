package dealership;

import layout.SpringUtilities;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import java.io.*;
import java.util.*;


public class DealershipGUI extends JFrame {
    private static Dealership dummy = Dealership.readDatabase(),
                              db = (dummy == null) ? new Dealership() : dummy;

    private final static String PASSENGERPANEL = "PASSENGER VEHICLE",
                                MOTORCYCLEPANEL = "MOTORCYCLE",
                                TRUCKPANEL = "TRUCK";

    private final static int extraWindowWidth = 100;

    DealershipGUI() {

    }

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
                Thread qThread = new Thread() {
                    public void run() {
                        addVehicle();
                    }
                };
                qThread.start();
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
        Object[][] data = new Object[db.getVehicleInvSize()][header.length];

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

    public void addVehicle() {
        JFrame frame = new JFrame("Adding vehicle");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Create and set up the content pane.
        DealershipGUI x = new DealershipGUI();
        x.newPanelComponent(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public void terminateSession() {
        db.writeDatabase();
        System.exit(0);
    }

    public void newPanelComponent(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel for adding a new passenger vehicle
        JPanel card1 = new JPanel(new GridLayout(7,1,1,1)) {
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += extraWindowWidth;
                return size;
            }
        };
        JLabel pcVIN = new JLabel("VIN (string)");
        JTextField pcVINTF = new JTextField("", 12);
        card1.add(pcVIN);
        card1.add(pcVINTF);
        JLabel pcMAKE = new JLabel("Make (string)");
        JTextField pcMAKETF = new JTextField("", 12);
        card1.add(pcMAKE);
        card1.add(pcMAKETF);
        JLabel pcMODEL = new JLabel("Model (string)");
        JTextField pcMODELTF = new JTextField("", 12);
        card1.add(pcMODEL);
        card1.add(pcMODELTF);
        JLabel pcYEAR = new JLabel("Year (int)");
        JTextField pcYEARTF = new JTextField("", 12);
        card1.add(pcYEAR);
        card1.add(pcYEARTF);
        JLabel pcMILEAGE = new JLabel("Mileage (int)");
        JTextField pcMILEAGETF = new JTextField("", 12);
        card1.add(pcMILEAGE);
        card1.add(pcMILEAGETF);
        JLabel pcPRICE = new JLabel("Price (float)");
        JTextField pcPRICETF = new JTextField("", 12);
        card1.add(pcPRICE);
        card1.add(pcPRICETF);
        JLabel pcBODY = new JLabel("Body Style (string)");
        JTextField pcBODYTF = new JTextField("", 12);
        card1.add(pcBODY);
        card1.add(pcBODYTF);
        JButton pcSUBMIT = new JButton ("Submit");
        card1.add(pcSUBMIT);
        JButton pcCLEAR = new JButton("Clear");
        card1.add(pcCLEAR);

        pcSUBMIT.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                Thread qThread = new Thread() {
                    public void run() {
                        ArrayList<String> err = new ArrayList<>();
                        if (pcVINTF.getText().length() == 0) {
                            err.add("Field 'VIN' is empty");
                        }
                        else if (pcVINTF.getText().length() > 10) {
                            err.add("'VIN' length is too long!");
                        }

                        if (pcMAKETF.getText().length() == 0) {
                            err.add("Field 'Make' is empty");
                        }

                        if (pcMODELTF.getText().length() == 0) {
                            err.add("Field 'Model' is empty");
                        }

                        if (pcYEARTF.getText().length() != 4) {
                            err.add("'Year' value is invalid");
                        }
                        else if (Integer.parseInt(pcYEARTF.getText()) < 0) {
                            err.add("'Year' cannot be a negative value");
                        }
                        else if (Integer.parseInt(pcYEARTF.getText()) < 1886 ||
                                Integer.parseInt(pcYEARTF.getText()) > 2020) {
                            err.add("'Year'  is not within the acceptable range");
                        }

                        if (pcMILEAGETF.getText().length() == 0) {
                            err.add("Field 'Mileage' is empty");
                        }
                        else if (Integer.parseInt(pcMILEAGETF.getText()) < 0) {
                            err.add("'Mileage' cannot be a negative value");
                        }

                        if (pcPRICETF.getText().length() == 0) {
                            err.add("Field 'Price' is empty");
                        }
                        else if (Float.parseFloat(pcPRICETF.getText()) < 0) {
                            err.add("'Price' cannot be a negative value");
                        }

                        if (pcBODYTF.getText().length() == 0) {
                            err.add("Field 'Body Style' is empty");
                        }

                        if (err.isEmpty()) {
                            // add vehicle to FIXME stopped here
                        }
                        else {
                            // show dialog message with errors FIXME stopped here
                        }
                    }
                };
                qThread.start();
            }
        });

        pcCLEAR.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pcVINTF.setText("");
                pcMAKETF.setText("");
                pcYEARTF.setText("");
                pcMILEAGETF.setText("");
                pcPRICETF.setText("");
                pcBODYTF.setText("");
            }
        });

        // Panel for adding a new motorcycle
        JPanel card2 = new JPanel();

        card2.add(new JTextField("TextField", 20));

        // Panel for adding a new truck
        JPanel card3 = new JPanel();
        card3.add(new JTextField("TextField", 20));

        tabbedPane.addTab(PASSENGERPANEL, card1);
        tabbedPane.addTab(MOTORCYCLEPANEL, card2);
        tabbedPane.addTab(TRUCKPANEL, card3);

        pane.add(tabbedPane, BorderLayout.WEST);
    }

    public static void main (String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DealershipGUI exe = new DealershipGUI("Dealership Management Software v1.0");
                exe.setVisible(true);
            }
        });
    }
}

package dealership;

import layout.SpringUtilities;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JOptionPane;
import java.util.logging.*;
import java.io.*;
import java.util.logging.Formatter;


public class DealershipGUI extends JFrame {
    private static Dealership dummy = Dealership.readDatabase(),
                              db = (dummy == null) ? new Dealership() : dummy;
    private static final Logger logger = Logger.getLogger(DealershipGUI.class.getName());
    private static FileHandler fh;

    private final static String PASSENGERPANEL = "PASSENGER VEHICLE",
                                MOTORCYCLEPANEL = "MOTORCYCLE",
                                TRUCKPANEL = "TRUCK",
                                CUSTOMERPANEL = "CUSTOMER",
                                EMPLOYEEPANEL = "EMPLOYEE";

    private final static int extraWindowWidth = 100;

    private JButton exitFrom,
                    exitFromMotor,
                    exitFromTruck;

    DealershipGUI() {
    }

    class CustomFormatter extends Formatter {
        private final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

        @Override
        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder(1000);
            builder.append(df.format(new Date(record.getMillis()))).append(" - ");
            builder.append("[").append(record.getSourceClassName()).append(".");
            builder.append(record.getSourceMethodName()).append("] :");
            builder.append("\n");
            builder.append("\t[").append(record.getLevel()).append("] - ");
            builder.append(formatMessage(record));
            builder.append("\n");
            return builder.toString();
        }

        public String getHead (Handler h) {
            return super.getHead(h);
        }

        public String getTail(Handler h) {
            return super.getTail(h);
        }
    }

    private void initLogger() {
        logger.setUseParentHandlers(false);
        CustomFormatter formatter = new CustomFormatter();
        try {
            fh = new FileHandler("log.txt");
            fh.setFormatter(formatter);
            logger.addHandler(fh);
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "FileHandler threw IOException", e);
        }
    }

    private DealershipGUI (String title) {
        super(title);
        initLogger();
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
                logger.log(Level.INFO, "User pressed 'Show all existing vehicles in database'");
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
                logger.log(Level.INFO, "Add a new vehicle to the database'");
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
                logger.log(Level.INFO, "User pressed 'Delete a vehicle from the database'");
                field.setText("STATUS: in deleting vehicle ...");
                Thread qThread = new Thread() {
                    public void run() {
                        deleteVehicleGUI();
                    }
                };
                qThread.start();
            }
        });

        searchvehicle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "User pressed 'Search for a vehicle'");
                field.setText("STATUS: in searching inventory ...");
                Thread qThread = new Thread() {
                    public void run() {
                        searchVehicleGUI();
                    }
                };
                qThread.start();
            }
        });

        listvehicle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "User pressed 'Show a list of vehicles within a given range'");
                field.setText("STATUS: in listing vehicle criteria search ...");
                Thread qThread = new Thread() {
                    public void run() {
                        vehiclePriceRange();
                    }
                };
                qThread.start();
            }
        });

        showuser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "User pressed 'Show list of users'");
                field.setText("STATUS: in displaying user database ...");
                Thread qThread = new Thread() {
                    public void run() {
                        listUsers();
                    }
                };
                qThread.start();
            }
        });

        adduser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "User pressed 'Add a new user to the database'");
                field.setText("STATUS: adding user ...");
                Thread qThread = new Thread() {
                    public void run() {
                        addUserGUI();
                    }
                };
                qThread.start();
            }
        });

        updateuser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "User pressed 'Update user info'");
                field.setText("STATUS: updating user ...");
                Thread qThread = new Thread() {
                    public void run() {
                        updateUserGUI();
                    }
                };
                qThread.start();
            }
        });

        sellvehicle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "User pressed 'Sell a vehicle'");
                field.setText("STATUS: selling vehicle ...");
                Thread qThread = new Thread() {
                    public void run() {
                        sellVehicleGUI();
                    }
                };
                qThread.start();
            }
        });

        showtransax.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "User pressed 'Show a list of completed sale transactions'");
                field.setText("STATUS: showing transactions ...");
                Thread qThread = new Thread() {
                    public void run() {
                        listTransactionsGUI();
                    }
                };
                qThread.start();
            }
        });

        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "User pressed 'Exit program'");
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
        logger.log(Level.INFO, "User has loaded main menu of GUI");
    }

    public void displayInventory() {
        if (db.getVehicleInvSize() == 0) {
            JOptionPane.showMessageDialog(null, "There is nothing to view as the database\n" +
                            " is currently empty! Now exiting..", "Failure!",
                    JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "User attempted to view an empty database");
            return;
        }

        String[] header = {"TYPE", "VIN", "MAKE", "MODEL", "YEAR", "MILEAGE", "PRICE", "BODY STYLE", "TYPE",
                            "DISPLACEMENT (cc)", "LENGTH (ft)", "MAX LOAD WEIGHT (lb)"};
        String body_style, type, displ, load, length;
        Object[][] data = new Object[db.getVehicleInvSize()][header.length];
        try {
            for (int i = 0; i < db.getVehicleInvSize(); ++i) {
                if (db.getVehicleAtPosition(i) instanceof PassengerCar) {
                    data[i][0] = "Passenger";
                    body_style = ((PassengerCar) db.getVehicleAtPosition(i)).getBodyStyle();
                    data[i][7] = body_style;
                    data[i][8] = "N/A";
                    data[i][9] = "N/A";
                    data[i][10] = "N/A";
                    data[i][11] = "N/A";
                } else if (db.getVehicleAtPosition(i) instanceof Motorcycle) {
                    data[i][0] = "Motorcycle";
                    type = ((Motorcycle) db.getVehicleAtPosition(i)).getType();
                    displ = Integer.toString(((Motorcycle) db.getVehicleAtPosition(i)).getDisplacement());
                    data[i][7] = "N/A";
                    data[i][8] = type;
                    data[i][9] = displ;
                    data[i][10] = "N/A";
                    data[i][11] = "N/A";
                } else {
                    data[i][0] = "Truck";
                    load = Float.toString(((Truck) db.getVehicleAtPosition(i)).getMaxLoadWeight());
                    length = Float.toString(((Truck) db.getVehicleAtPosition(i)).getLength());
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
        }
        catch (ClassCastException e) {
            logger.log(Level.SEVERE, "ClassCastException thrown, possible database corruption");
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Unknown exception thrown! See stack trace..", e);
            e.printStackTrace();
        }

        JFrame display = new JFrame("Inventory List");
        final JTable table = new JTable(data, header);
        table.setPreferredScrollableViewportSize(new Dimension(800, 100));
        table.setFillsViewportHeight(true);
        table.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(table);
        display.add(scrollPane);

        display.pack();
        display.setVisible(true);
        display.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        logger.log(Level.INFO, "User in 'Inventory List' window");
    }

    public void addVehicle() {
        JFrame frame = new JFrame("Adding vehicle");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Create and set up the content pane.
        DealershipGUI x = new DealershipGUI();
        x.newPanelComponentVehicle(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);

        logger.log(Level.INFO, "User in 'Adding Vehicle' window");
    }

    public void deleteVehicleGUI() {
        JFrame frame = new JFrame("Deleting vehicle");

        if (db.getVehicleInvSize() == 0) {
            JOptionPane.showMessageDialog(frame, "There is nothing to delete as the database\n" +
                            " is currently empty! Now exiting removal process..", "Failure!",
                    JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "User attempted to view an empty list (vehicles)");
            return;
        }

        JPanel panel = new JPanel();
        JLabel instr = new JLabel("Enter the vehicle's VIN (to be deleted): ");
        JTextField VIN = new JTextField(12);
        JButton submit = new JButton("Submit");
        JButton exit = new JButton("Exit");

        frame.setContentPane(panel);
        panel.add(instr);
        panel.add(VIN);
        panel.add(submit);
        panel.add(exit);

        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "User submitted a VIN (string)");
                Thread qThread = new Thread() {
                    public void run() {
                        if (db.deleteVehicleByStr(VIN.getText())) {
                            JOptionPane.showMessageDialog(frame, "Removal was successful!", "Success!",
                                    JOptionPane.INFORMATION_MESSAGE);
                            logger.log(Level.INFO, "User was able to remove a vehicle via string value");
                        }
                        else {
                            JOptionPane.showMessageDialog(frame, "Removal was unsuccessful! Please check your input" +
                                            " and try again!", "Failure!",
                                    JOptionPane.ERROR_MESSAGE);
                            logger.log(Level.INFO, "User's search term was not found, nothing removed from vehicle" +
                                    "database");
                        }
                    }
                };
                qThread.start();
            }
        });

        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                frame.dispose();
                logger.log(Level.INFO, "User presses 'Exit' button");
            }
        });


        //Display the window.
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        logger.log(Level.INFO, "User opened up GUI option to delete a vehicle");
    }

    public void searchVehicleGUI() {
        JFrame frame = new JFrame("Vehicle search");
        if (db.getVehicleInvSize() == 0) {
            JOptionPane.showMessageDialog(frame, "The database is empty, there is nothing to search for!", "Failure!",
                    JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "User attempted to search an empty vehicle database");
            return;
        }
        JPanel panel = new JPanel();
        JLabel instr = new JLabel("Enter the VIN of the vehicle: ");
        JTextField search = new JTextField(12);
        JButton submit = new JButton("Submit");
        JButton exit = new JButton("Exit");

        frame.setContentPane(panel);
        panel.add(instr);
        panel.add(search);
        panel.add(submit);
        panel.add(exit);

        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                frame.dispose();
                logger.log(Level.INFO, "User has exited from 'Vehicle Search' window");
            }
        });

        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "User attempted to submit a string value to search for");
                Thread qThread = new Thread() {
                    public void run() {
                        String[] header = {"TYPE", "VIN", "MAKE", "MODEL", "YEAR", "MILEAGE", "PRICE", "BODY STYLE", "TYPE",
                                "DISPLACEMENT (cc)", "LENGTH (ft)", "MAX LOAD WEIGHT (lb)"};

                        Object[][] data = new Object[1][header.length];

                        int index;
                        try {
                            if ((index = db.basicSearch(search.getText())) != db.getVehicleInvSize()) {
                                data[0][1] = db.getVehicleAtPosition(index).getVin();
                                data[0][2] = db.getVehicleAtPosition(index).getMake();
                                data[0][3] = db.getVehicleAtPosition(index).getModel();
                                data[0][4] = db.getVehicleAtPosition(index).getYear();
                                data[0][5] = db.getVehicleAtPosition(index).getMileage();
                                data[0][6] = db.getVehicleAtPosition(index).getPrice();
                                if (db.getVehicleAtPosition(index) instanceof PassengerCar) {
                                    data[0][0] = "Passenger";
                                    data[0][7] = ((PassengerCar) db.getVehicleAtPosition(index)).getBodyStyle();
                                    data[0][8] = "N/A";
                                    data[0][9] = "N/A";
                                    data[0][10] = "N/A";
                                    data[0][11] = "N/A";
                                } else if (db.getVehicleAtPosition(index) instanceof Motorcycle) {
                                    data[0][0] = "Motorcycle";
                                    data[0][7] = "N/A";
                                    data[0][8] = ((Motorcycle) db.getVehicleAtPosition(index)).getType();
                                    data[0][9] = Integer.toString(((Motorcycle) db.getVehicleAtPosition(index)).getDisplacement());
                                    data[0][10] = "N/A";
                                    data[0][11] = "N/A";
                                } else {
                                    data[0][0] = "Truck";
                                    data[0][7] = "N/A";
                                    data[0][8] = "N/A";
                                    data[0][9] = "N/A";
                                    data[0][10] = Float.toString(((Truck) db.getVehicleAtPosition(index)).getLength());
                                    data[0][11] = Float.toString(((Truck) db.getVehicleAtPosition(index)).getMaxLoadWeight());
                                }

                                JFrame display = new JFrame("Result(s)");
                                final JTable table = new JTable(data, header);
                                table.setPreferredScrollableViewportSize(new Dimension(800, 100));
                                table.setFillsViewportHeight(true);
                                table.setEnabled(false);

                                JScrollPane scrollPane = new JScrollPane(table);
                                display.add(scrollPane);

                                display.pack();
                                display.setVisible(true);
                                display.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                            } else {
                                JOptionPane.showMessageDialog(frame, "VIN not found in the database!", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        catch (ClassCastException e) {
                            logger.log(Level.SEVERE, "ClassCastException unexpectedly thrown. Refer to stack trace", e);
                            e.printStackTrace();
                        }
                    }
                };
                qThread.start();
            }
        });

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        logger.log(Level.INFO, "User entered 'Vehicle Search' window");
    }

    public void vehiclePriceRange() {
        JFrame frame = new JFrame("Criteria search");
        if (db.getVehicleInvSize() == 0) {
            JOptionPane.showMessageDialog(frame, "The database is empty, there is nothing to search for!", "Failure!",
                    JOptionPane.ERROR_MESSAGE);
            logger.log(Level.INFO, "User attempted to search within an empty database");
            return;
        }
        JPanel panel = new JPanel(new GridLayout(3,1,2,5));
        JLabel low = new JLabel("Enter the minimum price value: ");
        JLabel high = new JLabel("Enter the maximum price value: ");
        JTextField lowTF = new JTextField(12);
        JTextField highTF = new JTextField(12);
        JButton submit = new JButton("Submit");
        JButton exit = new JButton("Exit");

        frame.setContentPane(panel);
        panel.add(low);
        panel.add(lowTF);
        panel.add(high);
        panel.add(highTF);
        panel.add(submit);
        panel.add(exit);

        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "User has pressed 'Exit'");
                frame.dispose();
            }
        });

        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                Thread qThread = new Thread() {
                    public void run() {
                        logger.log(Level.INFO, "User pressed 'Submit'");
                        String[] header = {"TYPE", "VIN", "MAKE", "MODEL", "YEAR", "MILEAGE", "PRICE", "BODY STYLE", "TYPE",
                                "DISPLACEMENT (cc)", "LENGTH (ft)", "MAX LOAD WEIGHT (lb)"};

                        Object[][] data = new Object[db.getVehicleInvSize()][header.length];
                        try {
                            int index = 0;
                            float low = Float.parseFloat(lowTF.getText());
                            float high = Float.parseFloat(highTF.getText());
                            if (low < 0) {
                                JOptionPane.showMessageDialog(frame, "Invalid range entered!", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
                                logger.log(Level.WARNING, "User entered a negative value, low");
                                return;
                            }

                            if (high < 0) {
                                JOptionPane.showMessageDialog(frame, "Invalid range entered!", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
                                logger.log(Level.WARNING, "User entered a negativ value, high");
                            }

                            if (high < low) {
                                JOptionPane.showMessageDialog(frame, "Invalid range entered!", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
                                logger.log(Level.WARNING, "User entered an invalid range");
                                return;
                            }
                            for (int i = 0; i < db.getVehicleInvSize(); ++i) {
                                if (db.getVehicleAtPosition(i).getPrice() >= low &&
                                        db.getVehicleAtPosition(i).getPrice() <= high) {
                                    data[index][1] = db.getVehicleAtPosition(i).getVin();
                                    data[index][2] = db.getVehicleAtPosition(i).getMake();
                                    data[index][3] = db.getVehicleAtPosition(i).getModel();
                                    data[index][4] = db.getVehicleAtPosition(i).getYear();
                                    data[index][5] = db.getVehicleAtPosition(i).getMileage();
                                    data[index][6] = db.getVehicleAtPosition(i).getPrice();
                                    if (db.getVehicleAtPosition(i) instanceof PassengerCar) {
                                        data[index][0] = "Passenger";
                                        data[index][7] = ((PassengerCar) db.getVehicleAtPosition(i)).getBodyStyle();
                                        data[index][8] = "N/A";
                                        data[index][9] = "N/A";
                                        data[index][10] = "N/A";
                                        data[index][11] = "N/A";
                                    } else if (db.getVehicleAtPosition(i) instanceof Motorcycle) {
                                        data[index][0] = "Motorcycle";
                                        data[index][7] = "N/A";
                                        data[index][8] = ((Motorcycle) db.getVehicleAtPosition(i)).getType();
                                        data[index][9] = Integer.toString(((Motorcycle) db.getVehicleAtPosition(i)).getDisplacement());
                                        data[index][10] = "N/A";
                                        data[index][11] = "N/A";
                                    } else {
                                        data[index][0] = "Truck";
                                        data[index][7] = "N/A";
                                        data[index][8] = "N/A";
                                        data[index][9] = "N/A";
                                        data[index][10] = Float.toString(((Truck) db.getVehicleAtPosition(i)).getLength());
                                        data[index][11] = Float.toString(((Truck) db.getVehicleAtPosition(i)).getMaxLoadWeight());
                                    }
                                    index++;
                                }
                            }
                            if (index == 0) {
                                JOptionPane.showMessageDialog(frame, "There are no vehicles that match your criteria", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
                                logger.log(Level.WARNING, "User did not find their specified vehicle");
                                return;
                            }
                        }
                        catch (NumberFormatException e) {
                            logger.log(Level.WARNING, "User caused a NumberFormatException");
                            e.printStackTrace();
                        }
                        catch (Exception e) {
                            logger.log(Level.SEVERE, "Unknown exception has occurred, refer to stack trace", e);
                            e.printStackTrace();
                        }

                        JFrame display = new JFrame("Result(s)");
                        final JTable table = new JTable(data, header);
                        table.setPreferredScrollableViewportSize(new Dimension(800, 100));
                        table.setFillsViewportHeight(true);
                        table.setEnabled(false);

                        JScrollPane scrollPane = new JScrollPane(table);
                        display.add(scrollPane);

                        display.pack();
                        display.setVisible(true);
                        display.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    }
                };
                qThread.start();
            }
        });

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
    }

    public void listUsers() {
        if (db.getUserDatabaseSize() == 0) {
            JOptionPane.showMessageDialog(null, "There is nothing to view as the database\n" +
                            " is currently empty! Now exiting..", "Failure!",
                    JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "User attempted to view an empty user database");
            return;
        }

        String[] header = {"TYPE", "ID#", "FIRST NAME", "LAST NAME", "PHONE NUMBER", "DL#", "MONTHLY SALARY",
                            "BANK ACCT#"};
        Object[][] data = new Object[db.getUserDatabaseSize()][header.length];
        try {
            for (int i = 0; i < db.getUserDatabaseSize(); ++i) {
                if (db.getUserAtPosition(i) instanceof Customer) {
                    data[i][0] = "Customer";
                    data[i][4] = ((Customer) db.getUserAtPosition(i)).getPhoneNumber();
                    data[i][5] = ((Customer) db.getUserAtPosition(i)).getDriverLicenceNumber();
                    data[i][6] = "N/A";
                    data[i][7] = "N/A";
                } else if (db.getUserAtPosition(i) instanceof Employee) {
                    data[i][0] = "Employee";
                    data[i][4] = "N/A";
                    data[i][5] = "N/A";
                    data[i][6] = ((Employee) db.getUserAtPosition(i)).getMonthlySalary();
                    data[i][7] = ((Employee) db.getUserAtPosition(i)).getBankAccountNumber();
                }

                data[i][1] = db.getUserAtPosition(i).getId();
                data[i][2] = db.getUserAtPosition(i).getFirstName();
                data[i][3] = db.getUserAtPosition(i).getLastName();
            }
        }
        catch (ClassCastException e) {
            logger.log(Level.SEVERE, "ClassCastException thrown unexpectedly, refer to stack trace", e);
            e.printStackTrace();
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Unknown exception thrown, refer to stack trace", e);
        }

        JFrame display = new JFrame("User List");
        final JTable table = new JTable(data, header);
        table.setPreferredScrollableViewportSize(new Dimension(800, 100));
        table.setFillsViewportHeight(true);
        table.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(table);
        display.add(scrollPane);

        display.pack();
        display.setVisible(true);
        display.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        logger.log(Level.INFO, "User has entered 'User List'");
    }

    public void addUserGUI() {
        JFrame frame = new JFrame("Adding new user");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Create and set up the content pane.
        DealershipGUI x = new DealershipGUI();
        x.newPanelComponentUser(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        logger.log(Level.INFO, "User has entered 'Adding new user'");
    }

    public void updateUserGUI() {
        JFrame frame = new JFrame("Update a user");
        JPanel panel = new JPanel();
        JTextField entry = new JTextField(12);
        JLabel id_name = new JLabel("Enter the ID# of the user to be updated: ");
        JButton submit = new JButton("Submit");

        panel.add(id_name);
        panel.add(entry);
        panel.add(submit);

        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                logger.log(Level.INFO, "User pressed 'Submit'");
                int q = 0;
                for (int i = 0; i < db.getUserDatabaseSize(); ++i, q++) {
                    try {
                        if (db.getUserAtPosition(i).getId() == Integer.parseInt(entry.getText())) {
                            break;
                        }
                    }
                    catch (NumberFormatException e) {
                        logger.log(Level.SEVERE, "User submitted non-integer values", e);
                    }
                    catch (Exception ex) {
                        logger.log(Level.SEVERE, "Unknown exception occurred", ex);
                    }
                }
                final User temp = db.getUserAtPosition(q);
                if (temp == null) {
                    JOptionPane.showMessageDialog(frame, "User ID not found!", "Failure!",
                            JOptionPane.ERROR_MESSAGE);
                    logger.log(Level.INFO, "User did not enter a valid ID#");
                    return;
                }
                else {
                    JPanel subpanel = new JPanel(new GridLayout(5,1,2,5));
                    JLabel first = new JLabel("First Name: ");
                    JTextField firstname = new JTextField(temp.getFirstName(), 12);
                    JLabel last = new JLabel("Last Name: ");
                    JTextField lastname = new JTextField(temp.getLastName(), 12);
                    JButton sub_submit = new JButton("Submit Changes");
                    JLabel phoneNum = new JLabel("Phone Number: ");
                    JTextField phoneNumb = new JTextField(12);
                    JLabel dlNum = new JLabel("Driver's License #: ");
                    JTextField dlNumb = new JTextField(12);
                    JLabel salary = new JLabel("Monthly Salary: ");
                    JTextField salaryTF = new JTextField(12);
                    JLabel bank = new JLabel("Bank Account #: ");
                    JTextField bankTF = new JTextField(12);
                    JLabel type = new JLabel();

                    subpanel.add(first);
                    subpanel.add(firstname);
                    subpanel.add(last);
                    subpanel.add(lastname);

                    if (temp instanceof Customer) {
                        phoneNumb.setText(((Customer) temp).getPhoneNumber());
                        dlNumb.setText(Integer.toString(((Customer) temp).getDriverLicenceNumber()));
                        type.setText("Type: Customer");

                        subpanel.add(phoneNum);
                        subpanel.add(phoneNumb);
                        subpanel.add(dlNum);
                        subpanel.add(dlNumb);
                    }
                    else {
                        salaryTF.setText(Float.toString(((Employee) temp).getMonthlySalary()));
                        bankTF.setText(Integer.toString(((Employee) temp).getBankAccountNumber()));
                        type.setText("Type: Employee");

                        subpanel.add(salary);
                        subpanel.add(salaryTF);
                        subpanel.add(bank);
                        subpanel.add(bankTF);
                    }

                    subpanel.add(sub_submit);
                    subpanel.add(type);

                    frame.getContentPane().removeAll();
                    frame.getContentPane().add(subpanel);
                    frame.validate();
                    frame.pack();

                    sub_submit.addActionListener(new ActionListener() {
                        public void actionPerformed (ActionEvent x) {
                            temp.setFirstName(firstname.getText());
                            temp.setLastName(lastname.getText());
                            if (temp instanceof Customer) {
                                ((Customer)temp).setPhoneNumber(phoneNumb.getText());
                                ((Customer)temp).setDriverLicenceNumber(Integer.parseInt(dlNumb.getText()));
                            }
                            else {
                                ((Employee)temp).setMonthlySalary(Float.parseFloat(salaryTF.getText()));
                                ((Employee)temp).setBankAccountNumber(Integer.parseInt(bankTF.getText()));
                            }
                            JOptionPane.showMessageDialog(frame, "User has been successfully updated!", "Success!",
                                    JOptionPane.INFORMATION_MESSAGE);
                            logger.log(Level.INFO, "User able to update a user");
                            frame.dispose();
                        }
                    });
                }
            }
        });

        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        logger.log(Level.INFO, "User entered 'Update User' operation");
    }

    public void sellVehicleGUI(){
        JFrame frame = new JFrame("Selling a vehicle");
        JPanel mainpanel = new JPanel();
        JLabel qCustID = new JLabel("Enter the customer's ID: ");
        JTextField eCustID = new JTextField(12);
        JButton submit = new JButton("Submit");

        mainpanel.add(qCustID);
        mainpanel.add(eCustID);
        mainpanel.add(submit);

        submit.addActionListener(new ActionListener () {
            public void actionPerformed (ActionEvent x) {
                // checking user exists
                if ((Integer.parseInt(eCustID.getText()) < 0)) {
                    JOptionPane.showMessageDialog(frame, "INVALID INPUT: ID cannot be a negative value", "Failure!",
                            JOptionPane.ERROR_MESSAGE);
                    logger.log(Level.WARNING, "User entered bad input, a negative value");
                }
                else if (db.userExists(Integer.parseInt(eCustID.getText())) &&
                        db.getUserAtPosition(Integer.parseInt(eCustID.getText()) - 1) instanceof Customer) {
                    final int custID = Integer.parseInt(eCustID.getText());
                    JPanel subpanel = new JPanel();
                    JLabel qEmpID = new JLabel("Enter your employee ID: ");
                    JTextField eEmpID = new JTextField(12);
                    JButton sub_submit = new JButton("Submit");

                    subpanel.add(qEmpID);
                    subpanel.add(eEmpID);
                    subpanel.add(sub_submit);

                    sub_submit.addActionListener(new ActionListener() {
                        public void actionPerformed (ActionEvent x) {
                            // checking employee exists
                            if ((Integer.parseInt(eEmpID.getText()) < 0)) {
                                JOptionPane.showMessageDialog(frame, "INVALID INPUT: ID cannot be a negative value", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
                                logger.log(Level.WARNING, "User entered bad input, a negative value");
                            }
                            else if (db.userExists(Integer.parseInt(eEmpID.getText())) &&
                                    db.getUserAtPosition(Integer.parseInt(eEmpID.getText()) - 1) instanceof Employee) {
                                final int employeeID = Integer.parseInt(eEmpID.getText());
                                JPanel subsubpanel = new JPanel();
                                JLabel qVIN = new JLabel("Enter the vehicle's VIN: ");
                                JTextField eVIN = new JTextField(12);
                                JButton subsub_submit = new JButton("Submit");

                                subsubpanel.add(qVIN);
                                subsubpanel.add(eVIN);
                                subsubpanel.add(subsub_submit);

                                subsub_submit.addActionListener(new ActionListener() {
                                   public void actionPerformed (ActionEvent x) {
                                       JPanel subsubsubpanel = new JPanel(new GridLayout(6, 4, 4, 4));
                                       if (db.vehicleMatch(eVIN.getText())) {
                                           final String VIN = eVIN.getText();
                                           Date dateOfTransc = new Date(System.currentTimeMillis());
                                           JLabel qPrice = new JLabel("Enter the final sale price of the vehicle: ");
                                           JTextField ePrice = new JTextField(12);
                                           JLabel loggedinas = new JLabel("Logged in as: " +
                                                   ""+db.getUserAtPosition(employeeID - 1).getFirstName()+" " +
                                                   ""+db.getUserAtPosition(employeeID - 1).getLastName()+" " +
                                                   "(id == "+db.getUserAtPosition(employeeID - 1).getId()+")");
                                           JLabel sellingto = new JLabel("Vehicle sold to: " +
                                                   ""+db.getUserAtPosition(custID - 1).getFirstName()+" " +
                                                   ""+db.getUserAtPosition(custID - 1).getLastName()+" " +
                                                   "(id == "+db.getUserAtPosition(custID - 1).getId()+")");
                                           JButton subsubsub_submit = new JButton("Submit");

                                           subsubsub_submit.addActionListener(new ActionListener() {
                                               public void actionPerformed (ActionEvent x) {
                                                   if (Float.parseFloat(ePrice.getText()) < 0) {
                                                       JOptionPane.showMessageDialog(frame, "Price cannot be negative! " +
                                                                       "Please check your input and try again; or exit window\n", "Failure!",
                                                               JOptionPane.ERROR_MESSAGE);
                                                       logger.log(Level.WARNING, "User entered a negative value for 'price'");
                                                   }
                                                   else {
                                                       SaleTransaction trans = new SaleTransaction(custID, employeeID,
                                                               VIN, dateOfTransc, Float.parseFloat(ePrice.getText()));

                                                       if (!db.addTransactionGUI(trans)) {
                                                           JOptionPane.showMessageDialog(frame, "addTransactionGUI() failed!\n" +
                                                                           "Aborting transaction...", "Failure!",
                                                                   JOptionPane.ERROR_MESSAGE);
                                                           logger.log(Level.SEVERE,"addTransactionGUI() failed, transaction not added to db");
                                                           frame.dispose();
                                                       }

                                                       if (!db.deleteVehicleByStr(VIN)) {
                                                           JOptionPane.showMessageDialog(frame, "deleteVehicleByStr failed!\n" +
                                                                           "Aborting transaction...", "Failure!",
                                                                   JOptionPane.ERROR_MESSAGE);
                                                           logger.log(Level.SEVERE, "deleteVehicleByStr failed");
                                                           frame.dispose();
                                                       }

                                                       JOptionPane.showMessageDialog(frame, "Transaction complete!\n " +
                                                                       "The window will close after pressing \"OK\"", "Success!",
                                                               JOptionPane.INFORMATION_MESSAGE);
                                                       logger.log(Level.INFO, "User successful with adding transaction");
                                                       frame.dispose();
                                                   }
                                               }
                                           });

                                           subsubsubpanel.add(qPrice);
                                           subsubsubpanel.add(ePrice);
                                           subsubsubpanel.add(subsubsub_submit);
                                           subsubsubpanel.add(loggedinas);
                                           subsubsubpanel.add(sellingto);

                                       }
                                       else {
                                           JOptionPane.showMessageDialog(frame, "VIN does not exist! " +
                                                           "Please check your input and try again; or exit window\n", "Failure!",
                                                   JOptionPane.ERROR_MESSAGE);
                                           logger.log(Level.WARNING, "User entered an invalid VIN or it does not exist");
                                       }

                                       frame.getContentPane().removeAll();
                                       frame.getContentPane().add(subsubsubpanel);
                                       frame.validate();
                                       frame.pack();
                                   }
                                });
                                frame.getContentPane().removeAll();
                                frame.getContentPane().add(subsubpanel);
                                frame.validate();
                                frame.pack();
                            }
                            else {
                                JOptionPane.showMessageDialog(frame, "Employee does not exist! Please check your input and try again.\n" +
                                                "Need a valid employee ID to continue with transaction.", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
                                logger.log(Level.WARNING, "User entered an ID that does not exist");
                            }
                        }
                    });

                    frame.getContentPane().removeAll();
                    frame.getContentPane().add(subpanel);
                    frame.validate();
                    frame.pack();
                }
                else {
                    JOptionPane.showMessageDialog(frame, "User does not exist! Please check your input and try again.\n" +
                                    "You may need to add the customer to the database before proceeding with a sale.", "Failure!",
                            JOptionPane.ERROR_MESSAGE);
                    logger.log(Level.WARNING, "User entered a User ID that does not exist");
                }
            }
        });

        frame.setContentPane(mainpanel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        logger.log(Level.INFO, "User entered 'sell a vehicle' operation");
    }

    public void listTransactionsGUI() {
        if (db.getSaleTransactionSize() == 0) {
            JOptionPane.showMessageDialog(null, "There is nothing to view as the database\n" +
                            " is currently empty! Now exiting..", "Failure!",
                    JOptionPane.ERROR_MESSAGE);
            logger.log(Level.WARNING, "User attempted to view an empty transaction database");
            return;
        }

        String[] header = {"CUSTOMER ID", "EMPLOYEE ID", "VIN", "DATE", "SALE PRICE"};
        Object[][] data = new Object[db.getSaleTransactionSize()][header.length];

        for (int i = 0; i < db.getSaleTransactionSize(); ++i) {
            data[i][0] = db.getTransactionAtPosition(i).getCustomerId();
            data[i][1] = db.getTransactionAtPosition(i).getEmployeeId();
            data[i][2] = db.getTransactionAtPosition(i).getVin();
            data[i][3] = db.getTransactionAtPosition(i).getDate();
            data[i][4] = db.getTransactionAtPosition(i).getSalePrice();
        }

        JFrame display = new JFrame("Transaction List");
        final JTable table = new JTable(data, header);

        table.setPreferredScrollableViewportSize(new Dimension(800, 100));
        table.setFillsViewportHeight(true);
        table.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(table);
        display.add(scrollPane);

        display.pack();
        display.setVisible(true);
        display.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        logger.log(Level.INFO, "User is in list transactions operation");
    }

    public void terminateSession() {
        db.writeDatabase();
        logger.log(Level.INFO, "User has closed the program via 'Exit' in main menu, exit successful!");
        System.exit(0);
    }

    public void newPanelComponentUser(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel for adding a new Customer
        JPanel card1 = new JPanel(new GridLayout(6,1,1,1)) {
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += extraWindowWidth;
                return size;
            }
        };
        JLabel cFName = new JLabel("First Name (String)");
        JTextField cFNameTF = new JTextField("", 12);
        card1.add(cFName);
        card1.add(cFNameTF);
        JLabel cLName = new JLabel("Last Name (String)");
        JTextField cLNameTF = new JTextField("", 12);
        card1.add(cLName);
        card1.add(cLNameTF);
        JLabel cPhone = new JLabel("Phone Number (String)");
        JTextField cPhoneTF = new JTextField("", 12);
        card1.add(cPhone);
        card1.add(cPhoneTF);
        JLabel cDLN = new JLabel("Driver's License (int)");
        JTextField cDLNTF = new JTextField("", 12);
        card1.add(cDLN);
        card1.add(cDLNTF);

        JButton cSUBMIT = new JButton ("Submit");
        card1.add(cSUBMIT);
        JButton cCLEAR = new JButton("Clear");
        card1.add(cCLEAR);
        exitFrom = new JButton("Exit");
        card1.add(exitFrom);

        exitFrom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Container frame = exitFrom.getParent();
                do {
                    frame = frame.getParent();
                } while (!(frame instanceof JFrame));
                ((JFrame) frame).dispose();
            }
        });

        cSUBMIT.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                Thread qThread = new Thread() {
                    public void run() {
                        String first, last, phone;
                        int dl;
                        ArrayList<String> err = new ArrayList<>();

                        if (cFName.getText().length() == 0) {
                            err.add("Field 'First Name' is empty");
                            logger.log(Level.WARNING, "User submitted an empty field text (First Name)");
                        }

                        first = cFNameTF.getText();


                        if (cLNameTF.getText().length() == 0) {
                            err.add("Field 'Last Name' is empty");
                            logger.log(Level.WARNING, "User submitted an empty field (Last Name)");

                        }

                        last = cLNameTF.getText();

                        if (cPhoneTF.getText().length() == 0) {
                            err.add("Field 'Phone Number' is empty");
                            logger.log(Level.WARNING, "User submitted an empty field (Phone Number)");
                        }

                        phone = cPhoneTF.getText();

                        if (cDLNTF.getText().length() == 0) {
                            err.add("'Year' value is invalid");
                            logger.log(Level.WARNING, "User submitted empty field (Year)");
                        }

                        try {
                            dl = Integer.parseInt(cDLNTF.getText());


                            if (err.isEmpty()) {
                                Customer newObj = new Customer(db.userIdCounter++, first, last, phone, dl);
                                if (db.addUserDirectly(newObj)) {
                                    Container frame = card1.getParent();
                                    do {
                                        frame = frame.getParent();
                                    } while (!(frame instanceof JFrame));
                                    JOptionPane.showMessageDialog(frame, "User has been successfully added!\n" +
                                                    "You may continue to add users by pressing \"Ok\" \n" +
                                                    "or you may exit from this operation by pressing \"Ok\" (in this window)\n" +
                                                    "then \"Exit\" (in the 'Adding new user' window)",
                                            "Success!", JOptionPane.INFORMATION_MESSAGE);
                                    logger.log(Level.INFO, "New user added successfully");
                                } else {
                                    Container frame = card1.getParent();
                                    do {
                                        frame = frame.getParent();
                                    } while (!(frame instanceof JFrame));
                                    JOptionPane.showMessageDialog(frame, "addUserDirectly(User) method failed, " +
                                                    "despite criteria being met. An unknown error has occurred!", "Failure!",
                                            JOptionPane.ERROR_MESSAGE);
                                    logger.log(Level.SEVERE, "addUserDirectly(User) failed");
                                }
                            } else {
                                Container frame = card1.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                for (String i : err) {
                                    JOptionPane.showMessageDialog(frame, i, "Failure!", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                        catch (NumberFormatException e) {
                            logger.log(Level.SEVERE, "User submitted non-integer values", e);
                            e.printStackTrace();
                        }
                        catch (Exception e) {
                            logger.log(Level.SEVERE, "Unknown exception thrown, refer to stack trace");
                            e.printStackTrace();
                        }
                    }
                };
                qThread.start();
            }
        });

        cCLEAR.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.log(Level.INFO, "User cleared the form");
                cFNameTF.setText("");
                cLNameTF.setText("");
                cPhoneTF.setText("");
                cDLNTF.setText("");
            }
        });

        // Panel for adding a new Employee
        JPanel card2 = new JPanel(new GridLayout(6,1,1,1)) {
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += extraWindowWidth;
                return size;
            }
        };
        JLabel eFName = new JLabel("First Name (String)");
        JTextField eFNameTF = new JTextField("", 12);
        card2.add(eFName);
        card2.add(eFNameTF);
        JLabel eLName = new JLabel("Last Name (String)");
        JTextField eLNameTF = new JTextField("", 12);
        card2.add(eLName);
        card2.add(eLNameTF);
        JLabel eSalary = new JLabel("Monthly Salary (float)");
        JTextField eSalaryTF = new JTextField("", 12);
        card2.add(eSalary);
        card2.add(eSalaryTF);
        JLabel eBAN = new JLabel("Bank Account # (int)");
        JTextField eBANTF = new JTextField("", 12);
        card2.add(eBAN);
        card2.add(eBANTF);
        JButton eSUBMIT = new JButton ("Submit");
        card2.add(eSUBMIT);
        JButton eCLEAR = new JButton("Clear");
        card2.add(eCLEAR);
        exitFromMotor = new JButton("Exit");
        card2.add(exitFromMotor);

        exitFromMotor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.log(Level.INFO, "User is exiting the add vehicle window");
                Container frame = exitFrom.getParent();
                do {
                    frame = frame.getParent();
                } while (!(frame instanceof JFrame));
                ((JFrame) frame).dispose();
            }
        });

        eSUBMIT.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                Thread qThread = new Thread() {
                    public void run() {
                        String first, last;
                        int bank;
                        float salary;
                        ArrayList<String> err = new ArrayList<>();

                        if (eFNameTF.getText().length() == 0) {
                            err.add("Field 'First Name' is empty");
                        }

                        first = eFNameTF.getText();


                        if (eLNameTF.getText().length() == 0) {
                            err.add("Field 'Last Name' is empty");
                        }

                        last = eLNameTF.getText();

                        if (eBANTF.getText().length() == 0) {
                            err.add("Field 'Bank Account #' is empty");
                        }
                        else if (Integer.parseInt(eBANTF.getText()) < 0) {
                            err.add("Bank account number cannot be a negative value");
                        }

                        bank = Integer.parseInt(eBANTF.getText());

                        if (eSalaryTF.getText().length() == 0) {
                            err.add("Field 'Monthly Salary' is empty");
                        }
                        else if (Float.parseFloat(eSalaryTF.getText()) < 0) {
                            err.add("Monthly Salary cannot be a negative value");
                        }

                        salary = Float.parseFloat(eSalaryTF.getText());

                        if (err.isEmpty()) {
                            Employee newObj = new Employee(db.userIdCounter++, first, last, salary, bank);
                            if (db.addUserDirectly(newObj)) {
                                Container frame = card2.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "User has been successfully added!\n" +
                                                "You may continue to add users by pressing \"Ok\" \n" +
                                                "or you may exit from this operation by pressing \"Ok\" (in this window)\n" +
                                                "then \"Exit\" (in the 'Adding new user' window)",
                                        "Success!", JOptionPane.INFORMATION_MESSAGE);
                                logger.log(Level.INFO, "User successfully adds a new Employee");
                            }
                            else {
                                Container frame = card2.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "addUserDirectly(User) method failed, " +
                                                "despite criteria being met. An unknown error has occurred!", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
                                logger.log(Level.SEVERE, "Unable to add a new user despite criteria being met in addUserDirectly()");
                            }
                        }
                        else {
                            Container frame = card2.getParent();
                            do {
                                frame = frame.getParent();
                            } while (!(frame instanceof JFrame));
                            for (String i : err) {
                                JOptionPane.showMessageDialog(frame, i, "Failure!", JOptionPane.ERROR_MESSAGE);
                                logger.log(Level.WARNING, "Program displaying error message logs to user");
                            }
                        }
                    }
                };
                qThread.start();
            }
        });

        eCLEAR.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.log(Level.INFO, "User has cleared the form");
                eFNameTF.setText("");
                eLNameTF.setText("");
                eBANTF.setText("");
                eSalaryTF.setText("");
            }
        });

        tabbedPane.addTab(CUSTOMERPANEL, card1);
        tabbedPane.addTab(EMPLOYEEPANEL, card2);

        pane.add(tabbedPane, BorderLayout.WEST);
    }

    public void newPanelComponentVehicle(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel for adding a new passenger vehicle
        JPanel card1 = new JPanel(new GridLayout(9,1,1,1)) {
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
        exitFrom = new JButton("Exit");
        card1.add(exitFrom);

        exitFrom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.log(Level.INFO, "User exited from adding vehicle window gracefully");
                Container frame = exitFrom.getParent();
                do {
                    frame = frame.getParent();
                } while (!(frame instanceof JFrame));
                ((JFrame) frame).dispose();
            }
        });

        pcSUBMIT.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                Thread qThread = new Thread() {
                    public void run() {
                        String VIN, make, model, body;
                        int year, mileage;
                        float price;
                        ArrayList<String> err = new ArrayList<>();

                        if (pcVINTF.getText().length() == 0) {
                            err.add("Field 'VIN' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, VIN");
                        }
                        else if (pcVINTF.getText().length() > 10) {
                            err.add("'VIN' length is too long!");
                            logger.log(Level.WARNING, "User submitted String that is too long");
                        }
                        else if (db.vehicleMatch(pcVINTF.getText())) {
                            err.add("'VIN' already exists in the database!");
                            logger.log(Level.WARNING, "User submitted VIN that already exists");
                        }

                        VIN = pcVINTF.getText();


                        if (pcMAKETF.getText().length() == 0) {
                            err.add("Field 'Make' is empty");
                            logger.log(Level.WARNING, "User submitted an empty field, Make");
                        }

                        make = pcMAKETF.getText();

                        if (pcMODELTF.getText().length() == 0) {
                            err.add("Field 'Model' is empty");
                            logger.log(Level.WARNING, "User submitted an empty field, Model");
                        }

                        model = pcMODELTF.getText();

                        if (pcYEARTF.getText().length() != 4) {
                            err.add("'Year' value is invalid");
                            logger.log(Level.WARNING, "User submitted an empty field, Year");
                        }
                        else if (Integer.parseInt(pcYEARTF.getText()) < 0) {
                            err.add("'Year' cannot be a negative value");
                            logger.log(Level.WARNING, "User submitted a negative value for Year");
                        }
                        else if (Integer.parseInt(pcYEARTF.getText()) < 1886 ||
                                Integer.parseInt(pcYEARTF.getText()) > 2020) {
                            err.add("'Year' is not within the acceptable range");
                            logger.log(Level.WARNING, "User submitted a value out of range for Year");
                        }

                        year = Integer.parseInt(pcYEARTF.getText());

                        if (pcMILEAGETF.getText().length() == 0) {
                            err.add("Field 'Mileage' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, Mileage");
                        }
                        else if (Integer.parseInt(pcMILEAGETF.getText()) < 0) {
                            err.add("'Mileage' cannot be a negative value");
                            logger.log(Level.WARNING, "User submitted negative value, Mileage");
                        }

                        mileage = Integer.parseInt(pcMILEAGETF.getText());

                        if (pcPRICETF.getText().length() == 0) {
                            err.add("Field 'Price' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, Price");
                        }
                        else if (Float.parseFloat(pcPRICETF.getText()) < 0) {
                            err.add("'Price' cannot be a negative value");
                            logger.log(Level.WARNING, "User submitted negative value for 'Price'");
                        }

                        price = Float.parseFloat(pcPRICETF.getText());

                        if (pcBODYTF.getText().length() == 0) {
                            err.add("Field 'Body Style' is empty");
                            logger.log(Level.WARNING, "User submitted negative value for 'Body Style'");
                        }

                        body = pcBODYTF.getText();

                        if (err.isEmpty()) {
                            PassengerCar newObj = new PassengerCar(VIN, make, model, year, mileage, price, body);
                            if (db.addVehicleDirectly(newObj)) {
                                Container frame = card1.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "Vehicle has been successfully added!\n" +
                                        "You may continue to add vehicles by pressing \"Ok\" \n" +
                                        "or you may exit from this operation by pressing \"Ok\" (in this window)\n" +
                                        "then \"Exit\" (in the 'Adding vehicle' window)",
                                        "Success!", JOptionPane.INFORMATION_MESSAGE);
                                logger.log(Level.INFO, "User able to succesfully add a PassengerCar object");
                            }
                            else {
                                Container frame = card1.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "addVehicleDirectly(Vehicle) method failed, " +
                                        "despite criteria being met. An unknown error has occurred!", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
                                logger.log(Level.SEVERE, "addVehicleDirectly() failed unexpectedly");
                            }
                        }
                        else {
                            Container frame = card1.getParent();
                            do {
                                frame = frame.getParent();
                            } while (!(frame instanceof JFrame));
                            for (String i : err) {
                                JOptionPane.showMessageDialog(frame, i, "Failure!", JOptionPane.ERROR_MESSAGE);
                                logger.log(Level.INFO, "Message dialog shown to user indicating bad input");
                            }
                        }
                    }
                };
                qThread.start();
            }
        });

        pcCLEAR.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.log(Level.INFO, "User interacted with clear form button");
                pcVINTF.setText("");
                pcMAKETF.setText("");
                pcMODELTF.setText("");
                pcYEARTF.setText("");
                pcMILEAGETF.setText("");
                pcPRICETF.setText("");
                pcBODYTF.setText("");
            }
        });

        // Panel for adding a new motorcycle
        JPanel card2 = new JPanel(new GridLayout(10,1,1,1)) {
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += extraWindowWidth;
                return size;
            }
        };
        JLabel mVIN = new JLabel("VIN (string)");
        JTextField mVINTF = new JTextField("", 12);
        card2.add(mVIN);
        card2.add(mVINTF);
        JLabel mMAKE = new JLabel("Make (string)");
        JTextField mMAKETF = new JTextField("", 12);
        card2.add(mMAKE);
        card2.add(mMAKETF);
        JLabel mMODEL = new JLabel("Model (string)");
        JTextField mMODELTF = new JTextField("", 12);
        card2.add(mMODEL);
        card2.add(mMODELTF);
        JLabel mYEAR = new JLabel("Year (int)");
        JTextField mYEARTF = new JTextField("", 12);
        card2.add(mYEAR);
        card2.add(mYEARTF);
        JLabel mMILEAGE = new JLabel("Mileage (int)");
        JTextField mMILEAGETF = new JTextField("", 12);
        card2.add(mMILEAGE);
        card2.add(mMILEAGETF);
        JLabel mPRICE = new JLabel("Price (float)");
        JTextField mPRICETF = new JTextField("", 12);
        card2.add(mPRICE);
        card2.add(mPRICETF);
        JLabel mTYPE = new JLabel("Type (string)");
        JTextField mTYPETF = new JTextField("", 12);
        card2.add(mTYPE);
        card2.add(mTYPETF);
        JLabel mDISPL = new JLabel("Displacement (int)");
        JTextField mDISPLTF = new JTextField("", 12);
        card2.add(mDISPL);
        card2.add(mDISPLTF);
        JButton mSUBMIT = new JButton ("Submit");
        card2.add(mSUBMIT);
        JButton mCLEAR = new JButton("Clear");
        card2.add(mCLEAR);
        exitFromMotor = new JButton("Exit");
        card2.add(exitFromMotor);

        exitFromMotor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.log(Level.INFO, "User exited from Add Vehicle gracefully via exit JButton");
                Container frame = exitFrom.getParent();
                do {
                    frame = frame.getParent();
                } while (!(frame instanceof JFrame));
                ((JFrame) frame).dispose();
            }
        });

        mSUBMIT.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                Thread qThread = new Thread() {
                    public void run() {
                        String VIN, make, model, type;
                        int year, mileage, displ;
                        float price;
                        ArrayList<String> err = new ArrayList<>();

                        if (mVINTF.getText().length() == 0) {
                            err.add("Field 'VIN' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, VIN");
                        }
                        else if (mVINTF.getText().length() > 10) {
                            err.add("'VIN' length is too long!");
                            logger.log(Level.WARNING, "User submitted VIN that is too long in length");
                        }
                        else if (db.vehicleMatch(mVINTF.getText())) {
                            err.add("'VIN' already exists in the database!");
                            logger.log(Level.WARNING, "Vehicle already exists, invalid VIN to add");
                        }

                        VIN = mVINTF.getText();


                        if (mMAKETF.getText().length() == 0) {
                            err.add("Field 'Make' is empty");
                            logger.log(Level.WARNING, "User submitted field that is empty, Make");
                        }

                        make = mMAKETF.getText();

                        if (mMODELTF.getText().length() == 0) {
                            err.add("Field 'Model' is empty");
                            logger.log(Level.WARNING, "JTextField is empty, Model");
                        }

                        model = mMODELTF.getText();

                        if (mYEARTF.getText().length() != 4) {
                            err.add("'Year' value is invalid");
                            logger.log(Level.WARNING, "Year TextField is empty");
                        }
                        else if (Integer.parseInt(mYEARTF.getText()) < 0) {
                            err.add("'Year' cannot be a negative value");
                            logger.log(Level.WARNING, "User submitted negative value");
                        }
                        else if (Integer.parseInt(mYEARTF.getText()) < 1886 ||
                                Integer.parseInt(mYEARTF.getText()) > 2020) {
                            err.add("'Year' is not within the acceptable range");
                            logger.log(Level.WARNING, "User submitted unacceptable range for Year");
                        }

                        year = Integer.parseInt(mYEARTF.getText());

                        if (mMILEAGETF.getText().length() == 0) {
                            err.add("Field 'Mileage' is empty");
                            logger.log(Level.WARNING, "User submitted field that is empty, mileage");
                        }
                        else if (Integer.parseInt(mMILEAGETF.getText()) < 0) {
                            err.add("'Mileage' cannot be a negative value");
                            logger.log(Level.WARNING, "User submitted a negative value for mileage");
                        }

                        mileage = Integer.parseInt(mMILEAGETF.getText());

                        if (mPRICETF.getText().length() == 0) {
                            err.add("Field 'Price' is empty");
                            logger.log(Level.WARNING, "User submitted empty field");
                        }
                        else if (Float.parseFloat(mPRICETF.getText()) < 0) {
                            err.add("'Price' cannot be a negative value");
                            logger.log(Level.WARNING, "User submitted negative value, price");
                        }

                        price = Float.parseFloat(mPRICETF.getText());

                        if (mTYPETF.getText().length() == 0) {
                            err.add("Field 'Type' is empty");
                            logger.log(Level.WARNING, "User submitted empty field, Type");
                        }

                        type = mTYPETF.getText();

                        if (mDISPLTF.getText().length() == 0) {
                            err.add("Field 'Displacement' is empty");
                            logger.log(Level.WARNING, "User submitted empty field");
                        }
                        else if (Integer.parseInt(mDISPLTF.getText()) < 0) {
                            err.add("Displacement cannot be a negative value");
                            logger.log(Level.WARNING, "User submitted negative value for displacement");
                        }

                        displ = Integer.parseInt(mDISPLTF.getText());

                        if (err.isEmpty()) {
                            Motorcycle newObj = new Motorcycle(VIN, make, model, year, mileage, price, type, displ);
                            if (db.addVehicleDirectly(newObj)) {
                                Container frame = card2.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "Vehicle has been successfully added!\n" +
                                                "You may continue to add vehicles by pressing \"Ok\" \n" +
                                                "or you may exit from this operation by pressing \"Ok\" (in this window)\n" +
                                                "then \"Exit\" (in the 'Adding vehicle' window)",
                                                "Success!", JOptionPane.INFORMATION_MESSAGE);
                                logger.log(Level.INFO, "Motorcycle object added successfully!");
                            }
                            else {
                                Container frame = card2.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "addVehicleDirectly(Vehicle) method failed, " +
                                                "despite criteria being met. An unknown error has occurred!", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
                                logger.log(Level.INFO, "addVehicleDirectly failed unexpectedly");
                            }
                        }
                        else {
                            Container frame = card2.getParent();
                            do {
                                frame = frame.getParent();
                            } while (!(frame instanceof JFrame));
                            for (String i : err) {
                                JOptionPane.showMessageDialog(frame, i, "Failure!", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                };
                qThread.start();
            }
        });

        mCLEAR.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.log(Level.INFO, "User cleared the form for adding a motorcycle");
                mVINTF.setText("");
                mMAKETF.setText("");
                mMODELTF.setText("");
                mYEARTF.setText("");
                mMILEAGETF.setText("");
                mPRICETF.setText("");
                mTYPETF.setText("");
                mDISPLTF.setText("");
            }
        });

        // Panel for adding a new truck
        JPanel card3 = new JPanel(new GridLayout(10,1,1,1)) {
            public Dimension getPreferredSize() {
                Dimension size = super.getPreferredSize();
                size.width += extraWindowWidth;
                return size;
            }
        };
        JLabel tVIN = new JLabel("VIN (string)");
        JTextField tVINTF = new JTextField("", 12);
        card3.add(tVIN);
        card3.add(tVINTF);
        JLabel tMAKE = new JLabel("Make (string)");
        JTextField tMAKETF = new JTextField("", 12);
        card3.add(tMAKE);
        card3.add(tMAKETF);
        JLabel tMODEL = new JLabel("Model (string)");
        JTextField tMODELTF = new JTextField("", 12);
        card3.add(tMODEL);
        card3.add(tMODELTF);
        JLabel tYEAR = new JLabel("Year (int)");
        JTextField tYEARTF = new JTextField("", 12);
        card3.add(tYEAR);
        card3.add(tYEARTF);
        JLabel tMILEAGE = new JLabel("Mileage (int)");
        JTextField tMILEAGETF = new JTextField("", 12);
        card3.add(tMILEAGE);
        card3.add(tMILEAGETF);
        JLabel tPRICE = new JLabel("Price (float)");
        JTextField tPRICETF = new JTextField("", 12);
        card3.add(tPRICE);
        card3.add(tPRICETF);
        JLabel tWEIGHT = new JLabel("Max Load Weight (float)");
        JTextField tWEIGHTTF = new JTextField("", 12);
        card3.add(tWEIGHT);
        card3.add(tWEIGHTTF);
        JLabel tLENGTH = new JLabel("Length (float)");
        JTextField tLENGTHTF = new JTextField("", 12);
        card3.add(tLENGTH);
        card3.add(tLENGTHTF);
        JButton tSUBMIT = new JButton ("Submit");
        card3.add(tSUBMIT);
        JButton tCLEAR = new JButton("Clear");
        card3.add(tCLEAR);
        exitFromTruck = new JButton("Exit");
        card3.add(exitFromTruck);

        exitFromTruck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Container frame = exitFrom.getParent();
                do {
                    frame = frame.getParent();
                } while (!(frame instanceof JFrame));
                ((JFrame) frame).dispose();
            }
        });

        tSUBMIT.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                Thread qThread = new Thread() {
                    public void run() {
                        String VIN, make, model;
                        int year, mileage;
                        float price, load, length;
                        ArrayList<String> err = new ArrayList<>();

                        if (tVINTF.getText().length() == 0) {
                            err.add("Field 'VIN' is empty");
                            logger.log(Level.WARNING, "Submission of empty field attempted");
                        }
                        else if (tVINTF.getText().length() > 10) {
                            err.add("'VIN' length is too long!");
                            logger.log(Level.WARNING, "Submission of field > 10 == bad");
                        }
                        else if (db.vehicleMatch(tVINTF.getText())) {
                            err.add("'VIN' already exists in the database!");
                            logger.log(Level.WARNING, "VIN already exists");
                        }

                        VIN = tVINTF.getText();


                        if (tMAKETF.getText().length() == 0) {
                            err.add("Field 'Make' is empty");
                            logger.log(Level.WARNING, "Submission of empty field attempted");
                        }

                        make = tMAKETF.getText();

                        if (tMODELTF.getText().length() == 0) {
                            err.add("Field 'Model' is empty");
                            logger.log(Level.WARNING, "Submission of empty field attempted");
                        }

                        model = tMODELTF.getText();

                        if (tYEARTF.getText().length() != 4) {
                            err.add("'Year' value is invalid");
                            logger.log(Level.WARNING, "Invalid year value submitted");
                        }
                        else if (Integer.parseInt(tYEARTF.getText()) < 0) {
                            err.add("'Year' cannot be a negative value");
                            logger.log(Level.WARNING, "Year cannot be negative");
                        }
                        else if (Integer.parseInt(tYEARTF.getText()) < 1886 ||
                                Integer.parseInt(tYEARTF.getText()) > 2020) {
                            err.add("'Year' is not within the acceptable range");
                            logger.log(Level.WARNING, "Not within acceptable range for Year");
                        }

                        year = Integer.parseInt(tYEARTF.getText());

                        if (tMILEAGETF.getText().length() == 0) {
                            err.add("Field 'Mileage' is empty");
                            logger.log(Level.WARNING, "Submission of empty field attempted");
                        }
                        else if (Integer.parseInt(tMILEAGETF.getText()) < 0) {
                            err.add("'Mileage' cannot be a negative value");
                            logger.log(Level.WARNING, "Submission of mileage is negative");
                        }

                        mileage = Integer.parseInt(tMILEAGETF.getText());

                        if (tPRICETF.getText().length() == 0) {
                            err.add("Field 'Price' is empty");
                            logger.log(Level.WARNING, "Submission of empty field attempted");
                        }
                        else if (Float.parseFloat(tPRICETF.getText()) < 0) {
                            err.add("'Price' cannot be a negative value");
                            logger.log(Level.WARNING, "Submission of negative value for price attempted");
                        }

                        price = Float.parseFloat(tPRICETF.getText());

                        if (tLENGTHTF.getText().length() == 0) {
                            err.add("Field 'Length' is empty");
                            logger.log(Level.WARNING, "Submission of empty field attempted");
                        }
                        else if (Float.parseFloat(tLENGTHTF.getText()) < 0) {
                            err.add("Length cannot be a negative value");
                            logger.log(Level.WARNING, "Submission of negative value for length attempted");
                        }

                        length = Float.parseFloat(tLENGTHTF.getText());

                        if (tWEIGHTTF.getText().length() == 0) {
                            err.add("Field 'Weight' is empty");
                            logger.log(Level.WARNING, "Submission of empty field attempted");
                        }
                        else if (Integer.parseInt(tWEIGHTTF.getText()) < 0) {
                            err.add("Weight cannot be a negative value");
                            logger.log(Level.WARNING, "Submission of negative value for weight attempted");
                        }

                        load = Float.parseFloat(tWEIGHTTF.getText());

                        if (err.isEmpty()) {
                            Truck newObj = new Truck(VIN, make, model, year, mileage, price, load, length);
                            if (db.addVehicleDirectly(newObj)) {
                                Container frame = card3.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "Vehicle has been successfully added!\n" +
                                                "You may continue to add vehicles by pressing \"Ok\" \n" +
                                                "or you may exit from this operation by pressing \"Ok\" (in this window)\n" +
                                                "then \"Exit\" (in the 'Adding vehicle' window)",
                                        "Success!", JOptionPane.INFORMATION_MESSAGE);
                                logger.log(Level.INFO, "User added a truck object successfully");
                            }
                            else {
                                Container frame = card3.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "addVehicleDirectly(Vehicle) method failed, " +
                                                "despite criteria being met. An unknown error has occurred!", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
                                logger.log(Level.SEVERE, "addVehicleDirectly failed");
                            }
                        }
                        else {
                            Container frame = card3.getParent();
                            do {
                                frame = frame.getParent();
                            } while (!(frame instanceof JFrame));
                            for (String i : err) {
                                JOptionPane.showMessageDialog(frame, i, "Failure!", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                };
                qThread.start();
            }
        });

        tCLEAR.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tVINTF.setText("");
                tMAKETF.setText("");
                tMODELTF.setText("");
                tYEARTF.setText("");
                tMILEAGETF.setText("");
                tPRICETF.setText("");
                tLENGTHTF.setText("");
                tWEIGHTTF.setText("");
            }
        });

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

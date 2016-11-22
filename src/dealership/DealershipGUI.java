package dealership;

import layout.SpringUtilities;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;


public class DealershipGUI extends JFrame {
    private static Dealership dummy = Dealership.readDatabase(),
                              db = (dummy == null) ? new Dealership() : dummy;

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
        if (db.getVehicleInvSize() == 0) {
            JOptionPane.showMessageDialog(null, "There is nothing to view as the database\n" +
                            " is currently empty! Now exiting..", "Failure!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

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
        x.newPanelComponentVehicle(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public void deleteVehicleGUI() {
        JFrame frame = new JFrame("Deleting vehicle");

        if (db.getVehicleInvSize() == 0) {
            JOptionPane.showMessageDialog(frame, "There is nothing to delete as the database\n" +
                            " is currently empty! Now exiting removal process..", "Failure!",
                    JOptionPane.ERROR_MESSAGE);
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
                Thread qThread = new Thread() {
                    public void run() {
                        if (db.deleteVehicleByStr(VIN.getText())) {
                            JOptionPane.showMessageDialog(frame, "Removal was successful!", "Success!",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            JOptionPane.showMessageDialog(frame, "Removal was unsuccessful! Please check your input" +
                                            " and try again!", "Failure!",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };
                qThread.start();
            }
        });

        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                frame.dispose();
            }
        });


        //Display the window.
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void searchVehicleGUI() {
        JFrame frame = new JFrame("Vehicle search");
        if (db.getVehicleInvSize() == 0) {
            JOptionPane.showMessageDialog(frame, "The database is empty, there is nothing to search for!", "Failure!",
                    JOptionPane.ERROR_MESSAGE);
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
            }
        });

        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                Thread qThread = new Thread() {
                    public void run() {
                        String[] header = {"TYPE", "VIN", "MAKE", "MODEL", "YEAR", "MILEAGE", "PRICE", "BODY STYLE", "TYPE",
                                "DISPLACEMENT (cc)", "LENGTH (ft)", "MAX LOAD WEIGHT (lb)"};

                        Object[][] data = new Object[1][header.length];

                        int index;
                        if ((index = db.basicSearch(search.getText())) != db.getVehicleInvSize()) {
                            data[0][1] = db.getVehicleAtPosition(index).getVin();
                            data[0][2] = db.getVehicleAtPosition(index).getMake();
                            data[0][3] = db.getVehicleAtPosition(index).getModel();
                            data[0][4] = db.getVehicleAtPosition(index).getYear();
                            data[0][5] = db.getVehicleAtPosition(index).getMileage();
                            data[0][6] = db.getVehicleAtPosition(index).getPrice();
                            if (db.getVehicleAtPosition(index) instanceof PassengerCar) {
                                data[0][0] = "Passenger";
                                data[0][7] = ((PassengerCar)db.getVehicleAtPosition(index)).getBodyStyle();
                                data[0][8] = "N/A";
                                data[0][9] = "N/A";
                                data[0][10] = "N/A";
                                data[0][11] = "N/A";
                            }
                            else if (db.getVehicleAtPosition(index) instanceof Motorcycle) {
                                data[0][0] = "Motorcycle";
                                data[0][7] = "N/A";
                                data[0][8] = ((Motorcycle)db.getVehicleAtPosition(index)).getType();
                                data[0][9] = Integer.toString(((Motorcycle)db.getVehicleAtPosition(index)).getDisplacement());
                                data[0][10] = "N/A";
                                data[0][11] = "N/A";
                            }
                            else {
                                data[0][0] = "Truck";
                                data[0][7] = "N/A";
                                data[0][8] = "N/A";
                                data[0][9] = "N/A";
                                data[0][10] = Float.toString(((Truck)db.getVehicleAtPosition(index)).getLength());
                                data[0][11] = Float.toString(((Truck)db.getVehicleAtPosition(index)).getMaxLoadWeight());
                            }

                            JFrame display = new JFrame("Result(s)");
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
                        else {
                            JOptionPane.showMessageDialog(frame, "VIN not found in the database!", "Failure!",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };
                qThread.start();
            }
        });

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
    }

    public void vehiclePriceRange() {
        JFrame frame = new JFrame("Criteria search");
        if (db.getVehicleInvSize() == 0) {
            JOptionPane.showMessageDialog(frame, "The database is empty, there is nothing to search for!", "Failure!",
                    JOptionPane.ERROR_MESSAGE);
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
                frame.dispose();
            }
        });

        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                Thread qThread = new Thread() {
                    public void run() {
                        String[] header = {"TYPE", "VIN", "MAKE", "MODEL", "YEAR", "MILEAGE", "PRICE", "BODY STYLE", "TYPE",
                                "DISPLACEMENT (cc)", "LENGTH (ft)", "MAX LOAD WEIGHT (lb)"};

                        Object[][] data = new Object[db.getVehicleInvSize()][header.length];

                        int index = 0;
                        float low = Float.parseFloat(lowTF.getText());
                        float high = Float.parseFloat(highTF.getText());
                        if (high < low) {
                            JOptionPane.showMessageDialog(frame, "Invalid range entered!", "Failure!",
                                    JOptionPane.ERROR_MESSAGE);
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
                            return;
                        }
                        JFrame display = new JFrame("Result(s)");
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
            return;
        }

        String[] header = {"TYPE", "ID#", "FIRST NAME", "LAST NAME", "PHONE NUMBER", "DL#", "MONTHLY SALARY",
                            "BANK ACCT#"};
        Object[][] data = new Object[db.getUserDatabaseSize()][header.length];

        for (int i = 0; i < db.getUserDatabaseSize(); ++i) {
            if(db.getUserAtPosition(i) instanceof Customer) {
                data[i][0] = "Customer";
                data[i][4] = ((Customer)db.getUserAtPosition(i)).getPhoneNumber();
                data[i][5] = ((Customer)db.getUserAtPosition(i)).getDriverLicenceNumber();
                data[i][6] = "N/A";
                data[i][7] = "N/A";
            }
            else if (db.getUserAtPosition(i) instanceof Employee) {
                data[i][0] = "Employee";
                data[i][4] = "N/A";
                data[i][5] = "N/A";
                data[i][6] = ((Employee)db.getUserAtPosition(i)).getMonthlySalary();
                data[i][7] = ((Employee)db.getUserAtPosition(i)).getBankAccountNumber();
            }

            data[i][1] = db.getUserAtPosition(i).getId();
            data[i][2] = db.getUserAtPosition(i).getFirstName();
            data[i][3] = db.getUserAtPosition(i).getLastName();
        }

        JFrame display = new JFrame("User List");
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

    public void addUserGUI() {
        JFrame frame = new JFrame("Adding new user");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Create and set up the content pane.
        DealershipGUI x = new DealershipGUI();
        x.newPanelComponentUser(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
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
                int q = 0;
                for (int i = 0; i < db.getUserDatabaseSize(); ++i, q++) {
                    if (db.getUserAtPosition(i).getId() == Integer.parseInt(entry.getText())) {
                        break;
                    }
                }
                final User temp = db.getUserAtPosition(q);
                if (temp == null) {
                    JOptionPane.showMessageDialog(frame, "User ID not found!", "Failure!",
                            JOptionPane.ERROR_MESSAGE);
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
    }

    public void sellVehicleGUI(){
        // FIXME
    }

    public void listTransactionsGUI() {
        // FIXME
    }

    public void terminateSession() {
        db.writeDatabase();
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
                        }

                        first = cFNameTF.getText();


                        if (cLNameTF.getText().length() == 0) {
                            err.add("Field 'Last Name' is empty");
                        }

                        last = cLNameTF.getText();

                        if (cPhoneTF.getText().length() == 0) {
                            err.add("Field 'Phone Number' is empty");
                        }

                        phone = cPhoneTF.getText();

                        if (cDLNTF.getText().length() == 0) {
                            err.add("'Year' value is invalid");
                        }

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
                            }
                            else {
                                Container frame = card1.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "addUserDirectly(User) method failed, " +
                                                "despite criteria being met. An unknown error has occurred!", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        else {
                            Container frame = card1.getParent();
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

        cCLEAR.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
                            }
                            else {
                                Container frame = card2.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "addUserDirectly(User) method failed, " +
                                                "despite criteria being met. An unknown error has occurred!", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
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

        eCLEAR.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
                        }
                        else if (pcVINTF.getText().length() > 10) {
                            err.add("'VIN' length is too long!");
                        }
                        else if (db.vehicleMatch(pcVINTF.getText())) {
                            err.add("'VIN' already exists in the database!");
                        }

                        VIN = pcVINTF.getText();


                        if (pcMAKETF.getText().length() == 0) {
                            err.add("Field 'Make' is empty");
                        }

                        make = pcMAKETF.getText();

                        if (pcMODELTF.getText().length() == 0) {
                            err.add("Field 'Model' is empty");
                        }

                        model = pcMODELTF.getText();

                        if (pcYEARTF.getText().length() != 4) {
                            err.add("'Year' value is invalid");
                        }
                        else if (Integer.parseInt(pcYEARTF.getText()) < 0) {
                            err.add("'Year' cannot be a negative value");
                        }
                        else if (Integer.parseInt(pcYEARTF.getText()) < 1886 ||
                                Integer.parseInt(pcYEARTF.getText()) > 2020) {
                            err.add("'Year' is not within the acceptable range");
                        }

                        year = Integer.parseInt(pcYEARTF.getText());

                        if (pcMILEAGETF.getText().length() == 0) {
                            err.add("Field 'Mileage' is empty");
                        }
                        else if (Integer.parseInt(pcMILEAGETF.getText()) < 0) {
                            err.add("'Mileage' cannot be a negative value");
                        }

                        mileage = Integer.parseInt(pcMILEAGETF.getText());

                        if (pcPRICETF.getText().length() == 0) {
                            err.add("Field 'Price' is empty");
                        }
                        else if (Float.parseFloat(pcPRICETF.getText()) < 0) {
                            err.add("'Price' cannot be a negative value");
                        }

                        price = Float.parseFloat(pcPRICETF.getText());

                        if (pcBODYTF.getText().length() == 0) {
                            err.add("Field 'Body Style' is empty");
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
                            }
                            else {
                                Container frame = card1.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "addVehicleDirectly(Vehicle) method failed, " +
                                        "despite criteria being met. An unknown error has occurred!", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        else {
                            Container frame = card1.getParent();
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

        pcCLEAR.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
                        }
                        else if (mVINTF.getText().length() > 10) {
                            err.add("'VIN' length is too long!");
                        }
                        else if (db.vehicleMatch(mVINTF.getText())) {
                            err.add("'VIN' already exists in the database!");
                        }

                        VIN = mVINTF.getText();


                        if (mMAKETF.getText().length() == 0) {
                            err.add("Field 'Make' is empty");
                        }

                        make = mMAKETF.getText();

                        if (mMODELTF.getText().length() == 0) {
                            err.add("Field 'Model' is empty");
                        }

                        model = mMODELTF.getText();

                        if (mYEARTF.getText().length() != 4) {
                            err.add("'Year' value is invalid");
                        }
                        else if (Integer.parseInt(mYEARTF.getText()) < 0) {
                            err.add("'Year' cannot be a negative value");
                        }
                        else if (Integer.parseInt(mYEARTF.getText()) < 1886 ||
                                Integer.parseInt(mYEARTF.getText()) > 2020) {
                            err.add("'Year' is not within the acceptable range");
                        }

                        year = Integer.parseInt(mYEARTF.getText());

                        if (mMILEAGETF.getText().length() == 0) {
                            err.add("Field 'Mileage' is empty");
                        }
                        else if (Integer.parseInt(mMILEAGETF.getText()) < 0) {
                            err.add("'Mileage' cannot be a negative value");
                        }

                        mileage = Integer.parseInt(mMILEAGETF.getText());

                        if (mPRICETF.getText().length() == 0) {
                            err.add("Field 'Price' is empty");
                        }
                        else if (Float.parseFloat(mPRICETF.getText()) < 0) {
                            err.add("'Price' cannot be a negative value");
                        }

                        price = Float.parseFloat(mPRICETF.getText());

                        if (mTYPETF.getText().length() == 0) {
                            err.add("Field 'Type' is empty");
                        }

                        type = mTYPETF.getText();

                        if (mDISPLTF.getText().length() == 0) {
                            err.add("Field 'Displacement' is empty");
                        }
                        else if (Integer.parseInt(mDISPLTF.getText()) < 0) {
                            err.add("Displacement cannot be a negative value");
                        }

                        displ = Integer.parseInt(mTYPETF.getText());

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
                            }
                            else {
                                Container frame = card2.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "addVehicleDirectly(Vehicle) method failed, " +
                                                "despite criteria being met. An unknown error has occurred!", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
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
                mVINTF.setText("");
                mMAKETF.setText("");
                mMODELTF.setText("");
                mYEARTF.setText("");
                mMILEAGETF.setText("");
                mPRICETF.setText("");
                mTYPE.setText("");
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
                        }
                        else if (tVINTF.getText().length() > 10) {
                            err.add("'VIN' length is too long!");
                        }
                        else if (db.vehicleMatch(tVINTF.getText())) {
                            err.add("'VIN' already exists in the database!");
                        }

                        VIN = tVINTF.getText();


                        if (tMAKETF.getText().length() == 0) {
                            err.add("Field 'Make' is empty");
                        }

                        make = tMAKETF.getText();

                        if (tMODELTF.getText().length() == 0) {
                            err.add("Field 'Model' is empty");
                        }

                        model = tMODELTF.getText();

                        if (tYEARTF.getText().length() != 4) {
                            err.add("'Year' value is invalid");
                        }
                        else if (Integer.parseInt(tYEARTF.getText()) < 0) {
                            err.add("'Year' cannot be a negative value");
                        }
                        else if (Integer.parseInt(tYEARTF.getText()) < 1886 ||
                                Integer.parseInt(tYEARTF.getText()) > 2020) {
                            err.add("'Year' is not within the acceptable range");
                        }

                        year = Integer.parseInt(tYEARTF.getText());

                        if (tMILEAGETF.getText().length() == 0) {
                            err.add("Field 'Mileage' is empty");
                        }
                        else if (Integer.parseInt(tMILEAGETF.getText()) < 0) {
                            err.add("'Mileage' cannot be a negative value");
                        }

                        mileage = Integer.parseInt(tMILEAGETF.getText());

                        if (tPRICETF.getText().length() == 0) {
                            err.add("Field 'Price' is empty");
                        }
                        else if (Float.parseFloat(tPRICETF.getText()) < 0) {
                            err.add("'Price' cannot be a negative value");
                        }

                        price = Float.parseFloat(tPRICETF.getText());

                        if (tLENGTHTF.getText().length() == 0) {
                            err.add("Field 'Length' is empty");
                        }
                        else if (Float.parseFloat(tLENGTHTF.getText()) < 0) {
                            err.add("Length cannot be a negative value");
                        }

                        length = Float.parseFloat(tLENGTHTF.getText());

                        if (tWEIGHTTF.getText().length() == 0) {
                            err.add("Field 'Weight' is empty");
                        }
                        else if (Integer.parseInt(tWEIGHTTF.getText()) < 0) {
                            err.add("Weight cannot be a negative value");
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
                            }
                            else {
                                Container frame = card3.getParent();
                                do {
                                    frame = frame.getParent();
                                } while (!(frame instanceof JFrame));
                                JOptionPane.showMessageDialog(frame, "addVehicleDirectly(Vehicle) method failed, " +
                                                "despite criteria being met. An unknown error has occurred!", "Failure!",
                                        JOptionPane.ERROR_MESSAGE);
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

package pms.view;

import pms.controller.ParkingController;
import pms.model.Vehicle;
import pms.util.FeeCalculator;
import pms.util.BillGenerator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Vehicle list frame - Displays vehicles in a table with search and sort
 * @author uSer
 */
public class VehicleListFrame extends javax.swing.JFrame {
    private ParkingController controller;
    private JFrame parentFrame;
    private String userRole;
    
    private JTable vehicleTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cmbSearchType;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnCheckout;
    private JButton btnCalculateFee;
    private JButton btnPrintBill;
    private JButton btnSearch;
    private JButton btnSortBySlot;
    private JButton btnSortByVehicleNumber;
    private JButton btnSortByEntryTime;
    private JButton btnRefresh;
    private JButton btnBack;
    
    private ArrayList<Vehicle> currentVehicleList;
    
    /**
     * Creates new form VehicleListFrame
     * @param parent Parent frame (usually DashboardFrame)
     * @param userRole User role (Admin or Staff)
     */
    public VehicleListFrame(JFrame parent, String userRole) {
        this.parentFrame = parent;
        this.userRole = userRole;
        this.controller = ParkingController.getInstance();
        this.currentVehicleList = new ArrayList<>();
        initComponents();
        loadVehicles();
        applyRolePermissions();
    }
    
    /**
     * Default constructor (for backward compatibility)
     */
    public VehicleListFrame(JFrame parent) {
        this(parent, "Admin"); // Default to Admin
    }
    
    /**
     * Apply role-based permissions
     * Staff: Add + View only (no Edit/Delete)
     * Admin: Full CRUD access
     */
    private void applyRolePermissions() {
        if ("Staff".equals(userRole)) {
            // Staff can only Add and View - disable Edit and Delete
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            
            // Show tooltip explaining restriction
            btnEdit.setToolTipText("Edit is restricted to Admin users only");
            btnDelete.setToolTipText("Delete is restricted to Admin users only");
        }
        // Admin has full access - all buttons enabled based on selection
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Parking Management System - Vehicle List");
        setResizable(true);
        setSize(800, 600);
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title with role indicator
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Vehicle List", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        
        JLabel roleLabel = new JLabel("Role: " + userRole, JLabel.RIGHT);
        roleLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        roleLabel.setForeground(Color.GRAY);
        roleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(roleLabel, BorderLayout.SOUTH);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Search panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Advanced Search"));
        
        searchPanel.add(new JLabel("Search by:"));
        cmbSearchType = new JComboBox<>(new String[]{"Vehicle Number", "Slot Number", "Vehicle Type"});
        searchPanel.add(cmbSearchType);
        
        searchPanel.add(new JLabel("Search:"));
        txtSearch = new JTextField(15);
        searchPanel.add(txtSearch);
        
        btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchVehicle();
            }
        });
        searchPanel.add(btnSearch);
        
        btnPrintBill = new JButton("Print Bill");
        btnPrintBill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printBillByVehicleNumber();
            }
        });
        searchPanel.add(btnPrintBill);
        
        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadVehicles();
            }
        });
        searchPanel.add(btnRefresh);
        
        // Table panel
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Vehicles"));
        
        // Table
        String[] columnNames = {"Vehicle Number", "Vehicle Type", "Slot Number", "Entry Time", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        vehicleTable = new JTable(tableModel);
        vehicleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vehicleTable.setFont(new Font("Arial", Font.PLAIN, 12));
        vehicleTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        vehicleTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        scrollPane.setPreferredSize(new Dimension(750, 350));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Sort panel
        JPanel sortPanel = new JPanel();
        sortPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        sortPanel.setBorder(BorderFactory.createTitledBorder("Sort"));
        
        btnSortBySlot = new JButton("Sort by Slot Number");
        btnSortBySlot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortBySlotNumber();
            }
        });
        sortPanel.add(btnSortBySlot);
        
        btnSortByVehicleNumber = new JButton("Sort by Vehicle Number");
        btnSortByVehicleNumber.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortByVehicleNumber();
            }
        });
        sortPanel.add(btnSortByVehicleNumber);
        
        btnSortByEntryTime = new JButton("Sort by Entry Time");
        btnSortByEntryTime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortByEntryTime();
            }
        });
        sortPanel.add(btnSortByEntryTime);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnAdd = new JButton("Add Vehicle");
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addVehicle();
            }
        });
        
        btnEdit = new JButton("Edit Vehicle");
        btnEdit.setEnabled(false); // Disabled by default
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editVehicle();
            }
        });
        
        btnDelete = new JButton("Delete Vehicle");
        btnDelete.setEnabled(false); // Disabled by default
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteVehicle();
            }
        });
        
        btnCheckout = new JButton("Check Out");
        btnCheckout.setEnabled(false); // Disabled by default
        btnCheckout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkoutVehicle();
            }
        });
        
        btnCalculateFee = new JButton("Calculate Fee");
        btnCalculateFee.setEnabled(false); // Disabled by default
        btnCalculateFee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateFee();
            }
        });
        
        // Enable/disable buttons based on table selection and user role
        vehicleTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                boolean hasSelection = vehicleTable.getSelectedRow() >= 0;
                
                if (hasSelection) {
                    Vehicle selectedVehicle = currentVehicleList.get(vehicleTable.getSelectedRow());
                    boolean isCheckedOut = "OUT".equals(selectedVehicle.getStatus());
                    
                    // Only enable Edit/Delete if user is Admin AND has selection
                    if ("Admin".equals(userRole)) {
                        btnEdit.setEnabled(hasSelection && !isCheckedOut);
                        btnDelete.setEnabled(hasSelection);
                    }
                    // Staff role: Edit/Delete remain disabled (set in applyRolePermissions)
                    
                    // Checkout only for vehicles that are IN
                    btnCheckout.setEnabled(hasSelection && !isCheckedOut);
                    // Calculate fee for any vehicle
                    btnCalculateFee.setEnabled(hasSelection);
                    // Print bill for any vehicle
                    // btnPrintBill is always enabled (can search by vehicle number)
                } else {
                    btnEdit.setEnabled(false);
                    btnDelete.setEnabled(false);
                    btnCheckout.setEnabled(false);
                    btnCalculateFee.setEnabled(false);
                }
            }
        });
        
        btnBack = new JButton("Back");
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnCheckout);
        buttonPanel.add(btnCalculateFee);
        buttonPanel.add(btnBack);
        
        // Center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);
        centerPanel.add(sortPanel, BorderLayout.SOUTH);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        setLocationRelativeTo(parentFrame);
    }
    
    /**
     * Load vehicles into table
     * Made public so it can be called from other frames
     */
    public void loadVehicles() {
        currentVehicleList = controller.getAllVehicles();
        refreshTable();
    }
    
    /**
     * Refresh table with current vehicle list
     */
    private void refreshTable() {
        tableModel.setRowCount(0); // Clear table
        
        for (Vehicle v : currentVehicleList) {
            Object[] row = {
                v.getVehicleNumber(),
                v.getVehicleType(),
                v.getSlotNumber(),
                v.getEntryTime(),
                v.getStatus()
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Advanced search with multiple criteria (Linear Search Algorithm)
     * Time Complexity: O(n) where n is the number of vehicles
     */
    private void searchVehicle() {
        String searchText = txtSearch.getText().trim();
        String searchType = (String) cmbSearchType.getSelectedItem();
        
        if (searchText.isEmpty()) {
            loadVehicles(); // Show all if search is empty
            return;
        }
        
        // Linear Search Algorithm - O(n) time complexity
        ArrayList<Vehicle> searchResults = new ArrayList<>();
        String searchUpper = searchText.toUpperCase();
        
        for (Vehicle v : controller.getAllVehicles()) {
            boolean matches = false;
            
            if ("Vehicle Number".equals(searchType)) {
                // Search by vehicle number
                matches = v.getVehicleNumber().toUpperCase().contains(searchUpper);
            } else if ("Slot Number".equals(searchType)) {
                // Search by slot number
                try {
                    int searchSlot = Integer.parseInt(searchText);
                    matches = v.getSlotNumber() == searchSlot;
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, 
                        "Please enter a valid slot number!", 
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if ("Vehicle Type".equals(searchType)) {
                // Search by vehicle type
                matches = v.getVehicleType().toUpperCase().contains(searchUpper);
            }
            
            if (matches) {
                searchResults.add(v);
            }
        }
        
        currentVehicleList = searchResults;
        refreshTable();
        
        if (searchResults.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No vehicles found matching: " + searchText + " (by " + searchType + ")", 
                "Search Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Found " + searchResults.size() + " vehicle(s) matching: " + searchText + " (by " + searchType + ")", 
                "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Sort vehicles by slot number (Collections.sort with Comparator)
     * Time Complexity: O(n log n) where n is the number of vehicles
     */
    private void sortBySlotNumber() {
        if (currentVehicleList.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No vehicles to sort!", 
                "Sort", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Sort using Collections.sort with Comparator - O(n log n) time complexity
        Collections.sort(currentVehicleList, new Comparator<Vehicle>() {
            @Override
            public int compare(Vehicle v1, Vehicle v2) {
                return Integer.compare(v1.getSlotNumber(), v2.getSlotNumber());
            }
        });
        
        refreshTable();
        JOptionPane.showMessageDialog(this, 
            "Vehicles sorted by Slot Number!", 
            "Sort", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Sort vehicles by vehicle number (Collections.sort with Comparator)
     * Time Complexity: O(n log n) where n is the number of vehicles
     */
    private void sortByVehicleNumber() {
        if (currentVehicleList.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No vehicles to sort!", 
                "Sort", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Sort using Collections.sort with Comparator - O(n log n) time complexity
        Collections.sort(currentVehicleList, new Comparator<Vehicle>() {
            @Override
            public int compare(Vehicle v1, Vehicle v2) {
                return v1.getVehicleNumber().compareToIgnoreCase(v2.getVehicleNumber());
            }
        });
        
        refreshTable();
        JOptionPane.showMessageDialog(this, 
            "Vehicles sorted by Vehicle Number!", 
            "Sort", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Sort vehicles by entry time (Collections.sort with Comparator)
     * Time Complexity: O(n log n) where n is the number of vehicles
     */
    private void sortByEntryTime() {
        if (currentVehicleList.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No vehicles to sort!", 
                "Sort", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Sort using Collections.sort with Comparator - O(n log n) time complexity
        Collections.sort(currentVehicleList, new Comparator<Vehicle>() {
            @Override
            public int compare(Vehicle v1, Vehicle v2) {
                // Compare entry times (assuming format: yyyy-MM-dd HH:mm:ss)
                return v1.getEntryTime().compareTo(v2.getEntryTime());
            }
        });
        
        refreshTable();
        JOptionPane.showMessageDialog(this, 
            "Vehicles sorted by Entry Time!", 
            "Sort", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Add new vehicle
     */
    private void addVehicle() {
        VehicleFormFrame form = new VehicleFormFrame(this);
        form.setVisible(true);
        loadVehicles(); // Refresh after adding
    }
    
    /**
     * Edit selected vehicle (Admin only)
     */
    private void editVehicle() {
        // Check permission
        if (!"Admin".equals(userRole)) {
            JOptionPane.showMessageDialog(this, 
                "Access Denied! Edit functionality is restricted to Admin users only.", 
                "Permission Denied", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int selectedRow = vehicleTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a vehicle to edit!", 
                "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Vehicle selectedVehicle = currentVehicleList.get(selectedRow);
        VehicleFormFrame form = new VehicleFormFrame(this, selectedVehicle);
        form.setVisible(true);
        loadVehicles(); // Refresh after editing
    }
    
    /**
     * Delete selected vehicle (Admin only)
     */
    private void deleteVehicle() {
        // Check permission
        if (!"Admin".equals(userRole)) {
            JOptionPane.showMessageDialog(this, 
                "Access Denied! Delete functionality is restricted to Admin users only.", 
                "Permission Denied", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int selectedRow = vehicleTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a vehicle to delete!", 
                "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Vehicle selectedVehicle = currentVehicleList.get(selectedRow);
        
        // Confirm delete
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete vehicle: " + selectedVehicle.getVehicleNumber() + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = controller.deleteVehicle(selectedVehicle.getVehicleNumber());
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Vehicle deleted successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                loadVehicles(); // Refresh after deleting
                
                // Refresh parent frame if it's DashboardFrame
                if (parentFrame instanceof DashboardFrame) {
                    ((DashboardFrame) parentFrame).updateStatistics();
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to delete vehicle!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Checkout a vehicle (set status to OUT and calculate fee)
     */
    private void checkoutVehicle() {
        int selectedRow = vehicleTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a vehicle to checkout!", 
                "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Vehicle selectedVehicle = currentVehicleList.get(selectedRow);
        
        // Check if already checked out
        if ("OUT".equals(selectedVehicle.getStatus())) {
            JOptionPane.showMessageDialog(this, 
                "This vehicle is already checked out!", 
                "Already Checked Out", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Confirm checkout
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Check out vehicle: " + selectedVehicle.getVehicleNumber() + "?",
            "Confirm Checkout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            // First checkout the vehicle
            double fee = controller.checkoutVehicle(selectedVehicle.getVehicleNumber());
            
            if (fee >= 0) {
                // Get updated vehicle to get exit time
                Vehicle updatedVehicle = controller.getVehicleByNumber(selectedVehicle.getVehicleNumber());
                
                if (updatedVehicle != null) {
                    // Show payment dialog
                    PaymentDialog paymentDialog = new PaymentDialog(this, updatedVehicle, fee);
                    paymentDialog.setVisible(true);
                    
                    // Check if payment was confirmed
                    if (paymentDialog.isPaymentConfirmed()) {
                        String paymentMethod = paymentDialog.getPaymentMethod();
                        
                        // Show final checkout summary
                        double hours = 0.0;
                        if (updatedVehicle.getExitTime() != null) {
                            hours = FeeCalculator.calculateHours(updatedVehicle.getEntryTime(), updatedVehicle.getExitTime());
                        }
                        
                        String summary = String.format(
                            "✓ Checkout Complete!\n\n" +
                            "Vehicle Number: %s\n" +
                            "Hours Parked: %.2f hours\n" +
                            "Parking Fee: %s\n" +
                            "Payment Method: %s\n" +
                            "Status: Payment Received\n\n" +
                            "Vehicle has been checked out successfully!",
                            updatedVehicle.getVehicleNumber(),
                            hours,
                            FeeCalculator.formatFee(fee),
                            paymentMethod
                        );
                        
                        JOptionPane.showMessageDialog(this, 
                            summary, 
                            "Checkout Complete", JOptionPane.INFORMATION_MESSAGE);
                        
                        loadVehicles(); // Refresh after checkout
                        
                        // Refresh parent frame if it's DashboardFrame
                        if (parentFrame instanceof DashboardFrame) {
                            ((DashboardFrame) parentFrame).updateStatistics();
                        }
                    } else {
                        // Payment was cancelled - vehicle is already checked out
                        // User can still view the bill later
                        JOptionPane.showMessageDialog(this, 
                            "Vehicle has been checked out.\nPayment can be processed later.", 
                            "Checkout Complete", JOptionPane.INFORMATION_MESSAGE);
                        loadVehicles();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Failed to retrieve vehicle information!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to checkout vehicle!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Calculate and display parking fee for selected vehicle
     */
    private void calculateFee() {
        int selectedRow = vehicleTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a vehicle to calculate fee!", 
                "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Vehicle selectedVehicle = currentVehicleList.get(selectedRow);
        double fee = controller.calculateVehicleFee(selectedVehicle.getVehicleNumber());
        
        if (fee < 0) {
            JOptionPane.showMessageDialog(this, 
                "Could not calculate fee for this vehicle!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String message;
        if ("OUT".equals(selectedVehicle.getStatus()) && selectedVehicle.getExitTime() != null) {
            // Vehicle is checked out - show actual fee
            double hours = FeeCalculator.calculateHours(selectedVehicle.getEntryTime(), selectedVehicle.getExitTime());
            message = String.format(
                "Parking Fee Calculation\n\n" +
                "Vehicle Number: %s\n" +
                "Entry Time: %s\n" +
                "Exit Time: %s\n" +
                "Hours Parked: %.2f hours\n" +
                "Total Fee: %s\n\n" +
                "Fee Rates:\n" +
                "  - Minimum Fee: %s\n" +
                "  - Hourly Rate: %s/hour\n" +
                "  - Daily Maximum: %s/day",
                selectedVehicle.getVehicleNumber(),
                selectedVehicle.getEntryTime(),
                selectedVehicle.getExitTime(),
                hours,
                FeeCalculator.formatFee(fee),
                FeeCalculator.formatFee(FeeCalculator.getMinimumFee()),
                FeeCalculator.formatFee(FeeCalculator.getHourlyRate()),
                FeeCalculator.formatFee(FeeCalculator.getDailyRate())
            );
        } else {
            // Vehicle is still in - show estimated fee
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(new java.util.Date());
            double hours = FeeCalculator.calculateHours(selectedVehicle.getEntryTime(), currentTime);
            message = String.format(
                "Estimated Parking Fee\n\n" +
                "Vehicle Number: %s\n" +
                "Entry Time: %s\n" +
                "Current Time: %s\n" +
                "Hours Parked: %.2f hours\n" +
                "Estimated Fee: %s\n\n" +
                "(Fee will be calculated at checkout)\n\n" +
                "Fee Rates:\n" +
                "  - Minimum Fee: %s\n" +
                "  - Hourly Rate: %s/hour\n" +
                "  - Daily Maximum: %s/day",
                selectedVehicle.getVehicleNumber(),
                selectedVehicle.getEntryTime(),
                currentTime,
                hours,
                FeeCalculator.formatFee(fee),
                FeeCalculator.formatFee(FeeCalculator.getMinimumFee()),
                FeeCalculator.formatFee(FeeCalculator.getHourlyRate()),
                FeeCalculator.formatFee(FeeCalculator.getDailyRate())
            );
        }
        
        JOptionPane.showMessageDialog(this, 
            message, 
            "Parking Fee", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Print bill by entering vehicle number
     */
    private void printBillByVehicleNumber() {
        // Prompt for vehicle number
        String vehicleNumber = JOptionPane.showInputDialog(
            this,
            "Enter Vehicle Number to print bill:",
            "Print Bill",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (vehicleNumber == null || vehicleNumber.trim().isEmpty()) {
            return; // User cancelled or entered empty
        }
        
        vehicleNumber = vehicleNumber.trim();
        
        // Find vehicle
        Vehicle vehicle = controller.getVehicleByNumber(vehicleNumber);
        
        if (vehicle == null) {
            JOptionPane.showMessageDialog(this, 
                "Vehicle not found: " + vehicleNumber, 
                "Vehicle Not Found", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Generate and display bill
        displayBill(vehicle);
    }
    
    /**
     * Display bill for selected vehicle or by vehicle number
     * @param vehicle Vehicle to generate bill for
     */
    private void displayBill(Vehicle vehicle) {
        if (vehicle == null) {
            JOptionPane.showMessageDialog(this, 
                "Vehicle not found!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Generate bill in HTML format
        String billHTML = BillGenerator.generateBillHTML(vehicle);
        
        // Create a custom dialog to display the bill
        JDialog billDialog = new JDialog(this, "Parking Bill - " + vehicle.getVehicleNumber(), true);
        billDialog.setSize(500, 700);
        billDialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Bill content
        JEditorPane billPane = new JEditorPane("text/html", billHTML);
        billPane.setEditable(false);
        billPane.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(billPane);
        scrollPane.setPreferredSize(new Dimension(480, 600));
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton btnPrint = new JButton("Print");
        btnPrint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    billPane.print();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(billDialog, 
                        "Error printing bill: " + ex.getMessage(), 
                        "Print Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        JButton btnCopy = new JButton("Copy Text");
        btnCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String billText = BillGenerator.generateBill(vehicle);
                java.awt.datatransfer.StringSelection selection = 
                    new java.awt.datatransfer.StringSelection(billText);
                java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
                    .setContents(selection, null);
                JOptionPane.showMessageDialog(billDialog, 
                    "Bill copied to clipboard!", 
                    "Copied", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                billDialog.dispose();
            }
        });
        
        buttonPanel.add(btnPrint);
        buttonPanel.add(btnCopy);
        buttonPanel.add(btnClose);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        billDialog.add(panel);
        billDialog.setVisible(true);
    }
    
    /**
     * Go back to parent frame
     */
    private void goBack() {
        this.dispose();
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
    }
}


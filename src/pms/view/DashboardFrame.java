package pms.view;

import pms.controller.ParkingController;
import pms.model.Vehicle;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Dashboard frame - Shows parking statistics and navigation
 * @author uSer
 */
public class DashboardFrame extends javax.swing.JFrame {
    private ParkingController controller;
    private JFrame parentFrame;
    private String userRole;
    
    private JLabel lblTotalSlots;
    private JLabel lblOccupiedSlots;
    private JLabel lblAvailableSlots;
    private JLabel lblTodayVehicles;
    private JLabel lblTodayEarnings;
    private JLabel lblTotalEarnings;
    
    private JTable recentEntriesTable;
    private DefaultTableModel tableModel;
    
    private JButton btnVehicleList;
    private JButton btnAddVehicle;
    private JButton btnEarningsReport;
    private JButton btnBack;
    private JButton btnRefresh;
    
    /**
     * Creates new form DashboardFrame
     * @param parent Parent frame (HomeFrame)
     * @param userRole User role (Admin or Staff)
     */
    public DashboardFrame(JFrame parent, String userRole) {
        this.parentFrame = parent;
        this.userRole = userRole;
        this.controller = ParkingController.getInstance();
        initComponents();
        updateStatistics();
        applyRolePermissions();
    }
    
    /**
     * Default constructor (for backward compatibility)
     */
    public DashboardFrame(JFrame parent) {
        this(parent, "Admin"); // Default to Admin
    }
    
    /**
     * Apply role-based permissions
     */
    private void applyRolePermissions() {
        // Both Admin and Staff can view dashboard and add vehicles
        // No restrictions needed here as dashboard is view-only
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Parking Management System - Dashboard");
        setResizable(true);
        setSize(900, 700);
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Statistics panel
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(6, 2, 15, 15));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Parking Statistics"));
        
        // Total Slots
        JLabel label1 = new JLabel("Total Slots:");
        label1.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTotalSlots = new JLabel("0");
        lblTotalSlots.setFont(new Font("Arial", Font.BOLD, 16));
        statsPanel.add(label1);
        statsPanel.add(lblTotalSlots);
        
        // Occupied Slots
        JLabel label2 = new JLabel("Occupied Slots:");
        label2.setFont(new Font("Arial", Font.PLAIN, 14));
        lblOccupiedSlots = new JLabel("0");
        lblOccupiedSlots.setFont(new Font("Arial", Font.BOLD, 16));
        lblOccupiedSlots.setForeground(Color.RED);
        statsPanel.add(label2);
        statsPanel.add(lblOccupiedSlots);
        
        // Available Slots
        JLabel label3 = new JLabel("Available Slots:");
        label3.setFont(new Font("Arial", Font.PLAIN, 14));
        lblAvailableSlots = new JLabel("0");
        lblAvailableSlots.setFont(new Font("Arial", Font.BOLD, 16));
        lblAvailableSlots.setForeground(Color.GREEN);
        statsPanel.add(label3);
        statsPanel.add(lblAvailableSlots);
        
        // Today's Vehicles
        JLabel label4 = new JLabel("Vehicles Parked Today:");
        label4.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTodayVehicles = new JLabel("0");
        lblTodayVehicles.setFont(new Font("Arial", Font.BOLD, 16));
        lblTodayVehicles.setForeground(Color.BLUE);
        statsPanel.add(label4);
        statsPanel.add(lblTodayVehicles);
        
        // Today's Earnings
        JLabel label5 = new JLabel("Today's Earnings:");
        label5.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTodayEarnings = new JLabel("$0.00");
        lblTodayEarnings.setFont(new Font("Arial", Font.BOLD, 18));
        lblTodayEarnings.setForeground(new Color(0, 153, 0));
        statsPanel.add(label5);
        statsPanel.add(lblTodayEarnings);
        
        // Total Earnings
        JLabel label6 = new JLabel("Total Earnings:");
        label6.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTotalEarnings = new JLabel("$0.00");
        lblTotalEarnings.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotalEarnings.setForeground(new Color(0, 102, 204));
        statsPanel.add(label6);
        statsPanel.add(lblTotalEarnings);
        
        // Recent Entries Panel
        JPanel recentPanel = new JPanel();
        recentPanel.setLayout(new BorderLayout());
        recentPanel.setBorder(BorderFactory.createTitledBorder("Last 5 Vehicle Entries"));
        
        String[] columnNames = {"Vehicle Number", "Vehicle Type", "Slot Number", "Entry Time"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        recentEntriesTable = new JTable(tableModel);
        recentEntriesTable.setFont(new Font("Arial", Font.PLAIN, 11));
        recentEntriesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        recentEntriesTable.setRowHeight(20);
        JScrollPane scrollPane = new JScrollPane(recentEntriesTable);
        scrollPane.setPreferredSize(new Dimension(800, 120));
        recentPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        btnVehicleList = new JButton("Vehicle List");
        btnVehicleList.setFont(new Font("Arial", Font.PLAIN, 14));
        btnVehicleList.setPreferredSize(new Dimension(200, 40));
        btnVehicleList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openVehicleList();
            }
        });
        
        btnAddVehicle = new JButton("Add Vehicle");
        btnAddVehicle.setFont(new Font("Arial", Font.PLAIN, 14));
        btnAddVehicle.setPreferredSize(new Dimension(200, 40));
        btnAddVehicle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddVehicle();
            }
        });
        
        btnEarningsReport = new JButton("Earnings Report");
        btnEarningsReport.setFont(new Font("Arial", Font.PLAIN, 14));
        btnEarningsReport.setPreferredSize(new Dimension(200, 40));
        btnEarningsReport.setBackground(new Color(0, 153, 0));
        btnEarningsReport.setForeground(Color.WHITE);
        btnEarningsReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openEarningsReport();
            }
        });
        
        btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Arial", Font.PLAIN, 14));
        btnRefresh.setPreferredSize(new Dimension(200, 40));
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStatistics();
            }
        });
        
        btnBack = new JButton("Back to Home");
        btnBack.setFont(new Font("Arial", Font.PLAIN, 14));
        btnBack.setPreferredSize(new Dimension(200, 40));
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });
        
        buttonPanel.add(btnVehicleList);
        buttonPanel.add(btnAddVehicle);
        buttonPanel.add(btnEarningsReport);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnBack);
        
        // Center panel with stats and recent entries
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(statsPanel, BorderLayout.NORTH);
        centerPanel.add(recentPanel, BorderLayout.CENTER);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    /**
     * Update statistics display with dynamic data
     */
    public void updateStatistics() {
        int total = controller.getTotalSlots();
        int occupied = controller.getOccupiedSlots();
        int available = controller.getAvailableSlots();
        int todayCount = controller.getTodayVehicles().size();
        double todayEarnings = controller.getTodayEarnings();
        double totalEarnings = controller.getTotalEarnings();
        
        lblTotalSlots.setText(String.valueOf(total));
        lblOccupiedSlots.setText(String.valueOf(occupied));
        lblAvailableSlots.setText(String.valueOf(available));
        lblTodayVehicles.setText(String.valueOf(todayCount));
        lblTodayEarnings.setText(pms.util.FeeCalculator.formatFee(todayEarnings));
        lblTotalEarnings.setText(pms.util.FeeCalculator.formatFee(totalEarnings));
        
        // Update recent entries table
        updateRecentEntries();
    }
    
    /**
     * Open earnings report frame
     */
    private void openEarningsReport() {
        EarningsReportFrame earningsFrame = new EarningsReportFrame(this);
        earningsFrame.setVisible(true);
    }
    
    /**
     * Update the recent entries table with last 5 vehicles
     */
    private void updateRecentEntries() {
        tableModel.setRowCount(0); // Clear table
        
        ArrayList<Vehicle> lastEntries = controller.getLastNEntries(5);
        for (Vehicle v : lastEntries) {
            Object[] row = {
                v.getVehicleNumber(),
                v.getVehicleType(),
                v.getSlotNumber(),
                v.getEntryTime()
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Open vehicle list frame
     */
    private void openVehicleList() {
        controller.openVehicleList(this, userRole);
    }
    
    /**
     * Get current user role
     * @return User role (Admin or Staff)
     */
    public String getUserRole() {
        return userRole;
    }
    
    /**
     * Open add vehicle form
     */
    private void openAddVehicle() {
        controller.openVehicleForm(this, null);
    }
    
    /**
     * Go back to home frame
     */
    private void goBack() {
        this.dispose();
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
    }
}


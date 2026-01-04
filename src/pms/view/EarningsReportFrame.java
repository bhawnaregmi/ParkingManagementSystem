package pms.view;

import pms.controller.ParkingController;
import pms.controller.ParkingController.VehicleEarning;
import pms.util.FeeCalculator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Earnings report frame - Shows earnings breakdown by vehicle
 * @author uSer
 */
public class EarningsReportFrame extends javax.swing.JFrame {
    private ParkingController controller;
    private JFrame parentFrame;
    
    private JTable earningsTable;
    private DefaultTableModel tableModel;
    private JLabel lblTodayEarnings;
    private JLabel lblTotalEarnings;
    private JButton btnRefresh;
    private JButton btnBack;
    private JComboBox<String> cmbReportType;
    
    /**
     * Creates new form EarningsReportFrame
     * @param parent Parent frame (usually DashboardFrame)
     */
    public EarningsReportFrame(JFrame parent) {
        this.parentFrame = parent;
        this.controller = ParkingController.getInstance();
        initComponents();
        loadEarnings("All");
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Parking Management System - Earnings Report");
        setResizable(true);
        setSize(900, 600);
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Title
        JLabel titleLabel = new JLabel("Earnings Report", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Earnings summary panel
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new GridLayout(2, 2, 15, 15));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Earnings Summary"));
        
        // Today's Earnings
        JLabel label1 = new JLabel("Today's Earnings:");
        label1.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTodayEarnings = new JLabel("$0.00");
        lblTodayEarnings.setFont(new Font("Arial", Font.BOLD, 18));
        lblTodayEarnings.setForeground(new Color(0, 153, 0));
        summaryPanel.add(label1);
        summaryPanel.add(lblTodayEarnings);
        
        // Total Earnings
        JLabel label2 = new JLabel("Total Earnings:");
        label2.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTotalEarnings = new JLabel("$0.00");
        lblTotalEarnings.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotalEarnings.setForeground(new Color(0, 102, 204));
        summaryPanel.add(label2);
        summaryPanel.add(lblTotalEarnings);
        
        // Filter panel
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter"));
        
        filterPanel.add(new JLabel("Report Type:"));
        cmbReportType = new JComboBox<>(new String[]{"All", "Today Only"});
        cmbReportType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String reportType = (String) cmbReportType.getSelectedItem();
                loadEarnings(reportType);
            }
        });
        filterPanel.add(cmbReportType);
        
        // Table panel
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Vehicle Earnings Breakdown"));
        
        String[] columnNames = {"Vehicle Number", "Vehicle Type", "Entry Time", "Exit Time", "Hours Parked", "Earnings"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        earningsTable = new JTable(tableModel);
        earningsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        earningsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        earningsTable.setRowHeight(25);
        
        // Format earnings column
        earningsTable.getColumnModel().getColumn(5).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null) {
                    c.setForeground(new Color(0, 153, 0));
                    ((JLabel) c).setFont(new Font("Arial", Font.BOLD, 12));
                }
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(earningsTable);
        scrollPane.setPreferredSize(new Dimension(850, 350));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String reportType = (String) cmbReportType.getSelectedItem();
                loadEarnings(reportType);
            }
        });
        
        btnBack = new JButton("Back");
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });
        
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnBack);
        
        // Center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(summaryPanel, BorderLayout.NORTH);
        centerPanel.add(filterPanel, BorderLayout.CENTER);
        centerPanel.add(tablePanel, BorderLayout.SOUTH);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        setLocationRelativeTo(parentFrame);
    }
    
    /**
     * Load earnings data
     * @param reportType "All" or "Today Only"
     */
    private void loadEarnings(String reportType) {
        // Update summary
        double todayEarnings = controller.getTodayEarnings();
        double totalEarnings = controller.getTotalEarnings();
        
        lblTodayEarnings.setText(FeeCalculator.formatFee(todayEarnings));
        lblTotalEarnings.setText(FeeCalculator.formatFee(totalEarnings));
        
        // Load vehicle earnings
        ArrayList<VehicleEarning> earnings;
        if ("Today Only".equals(reportType)) {
            earnings = controller.getTodayVehicleEarnings();
        } else {
            earnings = controller.getVehicleEarnings();
        }
        
        // Clear and populate table
        tableModel.setRowCount(0);
        
        double tableTotal = 0.0;
        for (VehicleEarning earning : earnings) {
            pms.model.Vehicle v = earning.getVehicle();
            double fee = earning.getEarnings();
            tableTotal += fee;
            
            // Calculate hours
            double hours = FeeCalculator.calculateHours(v.getEntryTime(), v.getExitTime());
            
            Object[] row = {
                v.getVehicleNumber(),
                v.getVehicleType(),
                v.getEntryTime(),
                v.getExitTime(),
                String.format("%.2f hours", hours),
                FeeCalculator.formatFee(fee)
            };
            tableModel.addRow(row);
        }
        
        // Add total row if there are earnings
        if (earnings.size() > 0) {
            tableModel.addRow(new Object[]{
                "TOTAL", "", "", "", "",
                FeeCalculator.formatFee(tableTotal)
            });
        }
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


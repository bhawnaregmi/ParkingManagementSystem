package pms.view;

import pms.model.Vehicle;
import pms.util.FeeCalculator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Payment dialog for processing parking fee payment
 * @author uSer
 */
public class PaymentDialog extends javax.swing.JDialog {
    private Vehicle vehicle;
    private double fee;
    private String selectedPaymentMethod;
    private boolean paymentConfirmed;
    
    private JLabel lblVehicleNumber;
    private JLabel lblHoursParked;
    private JLabel lblTotalFee;
    private JComboBox<String> cmbPaymentMethod;
    private JButton btnConfirmPayment;
    private JButton btnCancel;
    
    /**
     * Creates new payment dialog
     * @param parent Parent frame
     * @param vehicle Vehicle to process payment for
     * @param fee Total fee amount
     */
    public PaymentDialog(JFrame parent, Vehicle vehicle, double fee) {
        super(parent, true);
        this.vehicle = vehicle;
        this.fee = fee;
        this.paymentConfirmed = false;
        this.selectedPaymentMethod = null;
        
        initComponents();
        populateData();
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Payment - " + vehicle.getVehicleNumber());
        setResizable(false);
        setModal(true);
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("PAYMENT CONFIRMATION", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Information panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Vehicle Number
        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("Vehicle Number:"), gbc);
        gbc.gridx = 1;
        lblVehicleNumber = new JLabel(vehicle.getVehicleNumber());
        lblVehicleNumber.setFont(new Font("Arial", Font.BOLD, 14));
        infoPanel.add(lblVehicleNumber, gbc);
        
        // Hours Parked
        double hours = 0.0;
        if (vehicle.getExitTime() != null && !vehicle.getExitTime().isEmpty()) {
            hours = FeeCalculator.calculateHours(vehicle.getEntryTime(), vehicle.getExitTime());
        } else {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(new java.util.Date());
            hours = FeeCalculator.calculateHours(vehicle.getEntryTime(), currentTime);
        }
        
        gbc.gridx = 0; gbc.gridy = 1;
        infoPanel.add(new JLabel("Hours Parked:"), gbc);
        gbc.gridx = 1;
        lblHoursParked = new JLabel(String.format("%.2f hours", hours));
        lblHoursParked.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(lblHoursParked, gbc);
        
        // Total Fee
        gbc.gridx = 0; gbc.gridy = 2;
        infoPanel.add(new JLabel("Total Fee:"), gbc);
        gbc.gridx = 1;
        lblTotalFee = new JLabel(FeeCalculator.formatFee(fee));
        lblTotalFee.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotalFee.setForeground(new Color(204, 0, 0));
        infoPanel.add(lblTotalFee, gbc);
        
        // Payment Method
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.insets = new Insets(20, 8, 8, 8);
        infoPanel.add(new JLabel("Payment Method:"), gbc);
        gbc.gridx = 1;
        cmbPaymentMethod = new JComboBox<>(new String[]{"Cash", "Credit Card", "Debit Card", "Mobile Payment", "Other"});
        cmbPaymentMethod.setPreferredSize(new Dimension(200, 30));
        infoPanel.add(cmbPaymentMethod, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnConfirmPayment = new JButton("Confirm Payment");
        btnConfirmPayment.setFont(new Font("Arial", Font.BOLD, 14));
        btnConfirmPayment.setPreferredSize(new Dimension(150, 40));
        btnConfirmPayment.setBackground(new Color(0, 153, 0));
        btnConfirmPayment.setForeground(Color.WHITE);
        btnConfirmPayment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmPayment();
            }
        });
        
        btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Arial", Font.PLAIN, 14));
        btnCancel.setPreferredSize(new Dimension(100, 40));
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        buttonPanel.add(btnConfirmPayment);
        buttonPanel.add(btnCancel);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        pack();
        setLocationRelativeTo(getParent());
    }
    
    /**
     * Populate data in the dialog
     */
    private void populateData() {
        // Data is already set in constructor
    }
    
    /**
     * Confirm payment
     */
    private void confirmPayment() {
        selectedPaymentMethod = (String) cmbPaymentMethod.getSelectedItem();
        
        if (selectedPaymentMethod == null || selectedPaymentMethod.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please select a payment method!", 
                "Payment Method Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Confirm payment
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Confirm payment of " + FeeCalculator.formatFee(fee) + 
            " via " + selectedPaymentMethod + "?",
            "Confirm Payment",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            paymentConfirmed = true;
            
            // Show payment received message
            String message = String.format(
                "✓ Payment Received Successfully!\n\n" +
                "Vehicle Number: %s\n" +
                "Amount Paid: %s\n" +
                "Payment Method: %s\n" +
                "Transaction Date: %s\n\n" +
                "Thank you for your payment!",
                vehicle.getVehicleNumber(),
                FeeCalculator.formatFee(fee),
                selectedPaymentMethod,
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())
            );
            
            JOptionPane.showMessageDialog(this, 
                message, 
                "Payment Received", JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
        }
    }
    
    /**
     * Get selected payment method
     * @return Payment method selected
     */
    public String getPaymentMethod() {
        return selectedPaymentMethod;
    }
    
    /**
     * Check if payment was confirmed
     * @return true if payment confirmed, false otherwise
     */
    public boolean isPaymentConfirmed() {
        return paymentConfirmed;
    }
}


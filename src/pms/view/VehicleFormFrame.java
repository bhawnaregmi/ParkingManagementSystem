package pms.view;

import pms.controller.ParkingController;
import pms.model.Vehicle;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Vehicle form frame - For adding/editing vehicles
 * @author uSer
 */
public class VehicleFormFrame extends javax.swing.JDialog {
    private ParkingController controller;
    private JFrame parentFrame;
    private Vehicle vehicleToEdit;
    private boolean isEditMode;
    
    private JTextField txtVehicleNumber;
    private JComboBox<String> cmbVehicleType;
    private JComboBox<Integer> cmbSlotNumber;
    private JTextField txtEntryTime;
    private JButton btnSave;
    private JButton btnCancel;
    
    /**
     * Creates new form VehicleFormFrame for adding
     */
    public VehicleFormFrame(JFrame parent) {
        super(parent, true);
        this.parentFrame = parent;
        this.controller = ParkingController.getInstance();
        this.isEditMode = false;
        initComponents();
        populateSlotComboBox();
        setCurrentTime();
    }
    
    /**
     * Creates new form VehicleFormFrame for editing
     */
    public VehicleFormFrame(JFrame parent, Vehicle vehicle) {
        super(parent, true);
        this.parentFrame = parent;
        this.controller = ParkingController.getInstance();
        this.vehicleToEdit = vehicle;
        this.isEditMode = true;
        initComponents();
        populateSlotComboBox();
        populateForm();
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(isEditMode ? "Edit Vehicle" : "Add Vehicle");
        setResizable(false);
        setModal(true);
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Vehicle Number
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Vehicle Number:"), gbc);
        gbc.gridx = 1;
        txtVehicleNumber = new JTextField(20);
        formPanel.add(txtVehicleNumber, gbc);
        
        // Vehicle Type
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Vehicle Type:"), gbc);
        gbc.gridx = 1;
        cmbVehicleType = new JComboBox<>(new String[]{"Car", "Bike", "Van"});
        formPanel.add(cmbVehicleType, gbc);
        
        // Slot Number
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Slot Number:"), gbc);
        gbc.gridx = 1;
        cmbSlotNumber = new JComboBox<>();
        cmbSlotNumber.setPreferredSize(new Dimension(200, 25));
        formPanel.add(cmbSlotNumber, gbc);
        
        // Entry Time
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Entry Time:"), gbc);
        gbc.gridx = 1;
        txtEntryTime = new JTextField(20);
        formPanel.add(txtEntryTime, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveVehicle();
            }
        });
        
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        pack();
        setLocationRelativeTo(parentFrame);
    }
    
    /**
     * Populate slot combo box with available slots
     */
    private void populateSlotComboBox() {
        cmbSlotNumber.removeAllItems();
        java.util.List<Integer> availableSlots = controller.getAvailableSlotNumbers();
        
        // If editing, include the current slot
        if (isEditMode && vehicleToEdit != null) {
            cmbSlotNumber.addItem(vehicleToEdit.getSlotNumber());
        }
        
        for (Integer slot : availableSlots) {
            if (isEditMode && vehicleToEdit != null && slot == vehicleToEdit.getSlotNumber()) {
                continue; // Skip current slot if already added
            }
            cmbSlotNumber.addItem(slot);
        }
    }
    
    /**
     * Set current time in entry time field
     */
    private void setCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        txtEntryTime.setText(sdf.format(new Date()));
    }
    
    /**
     * Populate form with vehicle data (for edit mode)
     */
    private void populateForm() {
        if (vehicleToEdit != null) {
            txtVehicleNumber.setText(vehicleToEdit.getVehicleNumber());
            cmbVehicleType.setSelectedItem(vehicleToEdit.getVehicleType());
            cmbSlotNumber.setSelectedItem(vehicleToEdit.getSlotNumber());
            txtEntryTime.setText(vehicleToEdit.getEntryTime());
        }
    }
    
    /**
     * Save vehicle
     */
    private void saveVehicle() {
        String vehicleNumber = txtVehicleNumber.getText().trim();
        String vehicleType = (String) cmbVehicleType.getSelectedItem();
        Integer slotNumberObj = (Integer) cmbSlotNumber.getSelectedItem();
        String entryTime = txtEntryTime.getText().trim();
        
        // Validation
        if (vehicleNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vehicle number cannot be empty!", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!controller.isValidVehicleNumber(vehicleNumber)) {
            JOptionPane.showMessageDialog(this, 
                "Invalid vehicle number format! Use alphanumeric characters (3-15 chars).", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (slotNumberObj == null) {
            JOptionPane.showMessageDialog(this, "Please select a slot number!", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int slotNumber = slotNumberObj;
        
        if (entryTime.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Entry time cannot be empty!", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check for duplicate vehicle number
        if (isEditMode) {
            if (controller.isDuplicateVehicle(vehicleNumber, vehicleToEdit.getVehicleNumber())) {
                JOptionPane.showMessageDialog(this, 
                    "Vehicle number already exists! Please use a different number.", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            if (controller.isDuplicateVehicle(vehicleNumber)) {
                JOptionPane.showMessageDialog(this, 
                    "Vehicle number already exists! Please use a different number.", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Check for duplicate IN status (prevent same vehicle having IN status twice)
        if (!isEditMode) {
            Vehicle existing = controller.getVehicleByNumber(vehicleNumber);
            if (existing != null && "IN".equals(existing.getStatus())) {
                JOptionPane.showMessageDialog(this, 
                    "This vehicle is already parked (status: IN)! Cannot add duplicate entry.", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Check if slot is occupied (for new vehicles or when changing slot)
        if (!isEditMode || vehicleToEdit.getSlotNumber() != slotNumber) {
            if (controller.isSlotOccupied(slotNumber)) {
                JOptionPane.showMessageDialog(this, 
                    "Selected slot " + slotNumber + " is already occupied! Please select an available slot.", 
                    "Slot Conflict", JOptionPane.ERROR_MESSAGE);
                populateSlotComboBox(); // Refresh available slots
                return;
            }
        }
        
        // Validate slot number range
        if (slotNumber < 1 || slotNumber > controller.getTotalSlots()) {
            JOptionPane.showMessageDialog(this, 
                "Invalid slot number! Must be between 1 and " + controller.getTotalSlots() + ".", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create vehicle object
        Vehicle vehicle = new Vehicle(vehicleNumber, vehicleType, slotNumber, entryTime, "IN");
        
        // Save vehicle
        boolean success;
        if (isEditMode) {
            success = controller.updateVehicle(vehicleToEdit.getVehicleNumber(), vehicle);
            if (success) {
                JOptionPane.showMessageDialog(this, "Vehicle updated successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update vehicle!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            success = controller.addVehicle(vehicle);
            if (success) {
                JOptionPane.showMessageDialog(this, "Vehicle added successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add vehicle! Slot may be occupied.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Refresh parent frame if it's DashboardFrame
        if (parentFrame instanceof DashboardFrame) {
            ((DashboardFrame) parentFrame).updateStatistics();
        }
        
        // Refresh parent frame if it's VehicleListFrame
        if (parentFrame instanceof VehicleListFrame) {
            ((VehicleListFrame) parentFrame).loadVehicles();
        }
        
        dispose();
    }
}


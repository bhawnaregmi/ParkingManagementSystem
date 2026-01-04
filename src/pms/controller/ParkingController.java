package pms.controller;

import pms.model.*;
import pms.util.Validator;
import pms.util.FeeCalculator;
import pms.view.*;
import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Controller class - Connects views and models
 * Handles business logic and coordinates between UI and data
 * Uses Singleton pattern to ensure all frames share the same data store
 * @author uSer
 */
public class ParkingController {
    private static ParkingController instance;
    private ParkingDataStore dataStore;
    
    /**
     * Private constructor - Initialize data store
     */
    private ParkingController() {
        this.dataStore = new ParkingDataStore();
    }
    
    /**
     * Get singleton instance of ParkingController
     * @return The single instance of ParkingController
     */
    public static ParkingController getInstance() {
        if (instance == null) {
            instance = new ParkingController();
        }
        return instance;
    }
    
    /**
     * Open dashboard frame
     * @param parent Parent frame (HomeFrame)
     * @param userRole User role (Admin or Staff)
     */
    public void openDashboard(JFrame parent, String userRole) {
        DashboardFrame dashboard = new DashboardFrame(parent, userRole);
        dashboard.setVisible(true);
    }
    
    /**
     * Open dashboard frame (backward compatibility - defaults to Admin)
     * @param parent Parent frame (HomeFrame)
     */
    public void openDashboard(JFrame parent) {
        openDashboard(parent, "Admin");
    }
    
    /**
     * Open vehicle list frame
     * @param parent Parent frame (usually DashboardFrame)
     * @param userRole User role (Admin or Staff)
     */
    public void openVehicleList(JFrame parent, String userRole) {
        VehicleListFrame vehicleList = new VehicleListFrame(parent, userRole);
        vehicleList.setVisible(true);
    }
    
    /**
     * Open vehicle list frame (backward compatibility - defaults to Admin)
     * @param parent Parent frame (usually DashboardFrame)
     */
    public void openVehicleList(JFrame parent) {
        openVehicleList(parent, "Admin");
    }
    
    /**
     * Open vehicle form frame for adding
     * @param parent Parent frame
     */
    public void openVehicleForm(JFrame parent, Vehicle vehicle) {
        VehicleFormFrame form;
        if (vehicle == null) {
            form = new VehicleFormFrame(parent);
        } else {
            form = new VehicleFormFrame(parent, vehicle);
        }
        form.setVisible(true);
    }
    
    /**
     * Add a vehicle and save to file
     * @param vehicle Vehicle to add
     * @return true if successful, false otherwise
     */
    public boolean addVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            return false;
        }
        boolean success = dataStore.addVehicle(vehicle);
        if (success) {
            dataStore.saveVehiclesToFile(); // Save to file after adding
        }
        return success;
    }
    
    /**
     * Update a vehicle and save to file
     * @param vehicleNumber Vehicle number to update
     * @param updatedVehicle Updated vehicle object
     * @return true if successful, false otherwise
     */
    public boolean updateVehicle(String vehicleNumber, Vehicle updatedVehicle) {
        if (vehicleNumber == null || updatedVehicle == null) {
            return false;
        }
        boolean success = dataStore.updateVehicle(vehicleNumber, updatedVehicle);
        if (success) {
            dataStore.saveVehiclesToFile(); // Save to file after updating
        }
        return success;
    }
    
    /**
     * Delete a vehicle and save to file
     * @param vehicleNumber Vehicle number to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteVehicle(String vehicleNumber) {
        if (vehicleNumber == null) {
            return false;
        }
        boolean success = dataStore.deleteVehicle(vehicleNumber);
        if (success) {
            dataStore.saveVehiclesToFile(); // Save to file after deleting
        }
        return success;
    }
    
    /**
     * Save all vehicles to file (can be called on exit)
     * @return true if saved successfully, false otherwise
     */
    public boolean saveAllData() {
        return dataStore.saveVehiclesToFile();
    }
    
    /**
     * Get all vehicles
     * @return List of all vehicles
     */
    public ArrayList<Vehicle> getAllVehicles() {
        return dataStore.getAllVehicles();
    }
    
    /**
     * Get vehicle by vehicle number
     * @param vehicleNumber Vehicle number to search
     * @return Vehicle if found, null otherwise
     */
    public Vehicle getVehicleByNumber(String vehicleNumber) {
        return dataStore.getVehicleByNumber(vehicleNumber);
    }
    
    /**
     * Get total number of slots
     * @return Total slots
     */
    public int getTotalSlots() {
        return dataStore.getTotalSlots();
    }
    
    /**
     * Get number of occupied slots
     * @return Number of occupied slots
     */
    public int getOccupiedSlots() {
        return dataStore.getOccupiedSlots();
    }
    
    /**
     * Get number of available slots
     * @return Number of available slots
     */
    public int getAvailableSlots() {
        return dataStore.getAvailableSlotsCount();
    }
    
    /**
     * Check if slot is occupied
     * @param slotNumber Slot number to check
     * @return true if occupied, false otherwise
     */
    public boolean isSlotOccupied(int slotNumber) {
        return dataStore.isSlotOccupied(slotNumber);
    }
    
    /**
     * Get list of available slot numbers
     * @return List of available slot numbers
     */
    public List<Integer> getAvailableSlotNumbers() {
        List<Integer> availableSlots = new ArrayList<>();
        for (ParkingSlot slot : dataStore.getAvailableSlots()) {
            availableSlots.add(slot.getSlotNumber());
        }
        return availableSlots;
    }
    
    /**
     * Validate vehicle number
     * @param vehicleNumber Vehicle number to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidVehicleNumber(String vehicleNumber) {
        return Validator.isValidVehicleNumber(vehicleNumber);
    }
    
    /**
     * Check if vehicle number is duplicate
     * @param vehicleNumber Vehicle number to check
     * @return true if duplicate, false otherwise
     */
    public boolean isDuplicateVehicle(String vehicleNumber) {
        return Validator.isDuplicateVehicle(vehicleNumber, dataStore.getAllVehicles());
    }
    
    /**
     * Check if vehicle number is duplicate (excluding a specific vehicle)
     * @param vehicleNumber Vehicle number to check
     * @param excludeVehicleNumber Vehicle number to exclude
     * @return true if duplicate, false otherwise
     */
    public boolean isDuplicateVehicle(String vehicleNumber, String excludeVehicleNumber) {
        return Validator.isDuplicateVehicle(vehicleNumber, excludeVehicleNumber, dataStore.getAllVehicles());
    }
    
    /**
     * Validate positive integer
     * @param str String to validate
     * @return true if valid positive integer, false otherwise
     */
    public boolean isPositiveInt(String str) {
        return Validator.isPositiveInt(str);
    }
    
    /**
     * Get vehicles parked today
     * @return List of vehicles with entry time matching today's date
     */
    public ArrayList<Vehicle> getTodayVehicles() {
        ArrayList<Vehicle> todayVehicles = new ArrayList<>();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new java.util.Date());
        
        for (Vehicle v : dataStore.getAllVehicles()) {
            if (v.getEntryTime() != null && v.getEntryTime().startsWith(today)) {
                todayVehicles.add(v);
            }
        }
        return todayVehicles;
    }
    
    /**
     * Get last N vehicle entries (most recent first)
     * @param n Number of recent entries to return
     * @return List of last N vehicles sorted by entry time (most recent first)
     */
    public ArrayList<Vehicle> getLastNEntries(int n) {
        ArrayList<Vehicle> allVehicles = dataStore.getAllVehicles();
        ArrayList<Vehicle> sorted = new ArrayList<>(allVehicles);
        
        // Sort by entry time descending (most recent first)
        java.util.Collections.sort(sorted, new java.util.Comparator<Vehicle>() {
            @Override
            public int compare(Vehicle v1, Vehicle v2) {
                return v2.getEntryTime().compareTo(v1.getEntryTime());
            }
        });
        
        // Return first N entries
        ArrayList<Vehicle> result = new ArrayList<>();
        int count = Math.min(n, sorted.size());
        for (int i = 0; i < count; i++) {
            result.add(sorted.get(i));
        }
        return result;
    }
    
    /**
     * Checkout a vehicle (set status to OUT and calculate fee)
     * @param vehicleNumber Vehicle number to checkout
     * @return Fee amount if successful, -1 if vehicle not found or already checked out
     */
    public double checkoutVehicle(String vehicleNumber) {
        if (vehicleNumber == null) {
            return -1;
        }
        
        Vehicle vehicle = dataStore.getVehicleByNumber(vehicleNumber);
        if (vehicle == null) {
            return -1;
        }
        
        // Check if already checked out
        if ("OUT".equals(vehicle.getStatus())) {
            return -1;
        }
        
        // Set exit time to current time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String exitTime = sdf.format(new Date());
        vehicle.setExitTime(exitTime);
        vehicle.setStatus("OUT");
        
        // Free up the slot
        ParkingSlot slot = dataStore.getSlotByNumber(vehicle.getSlotNumber());
        if (slot != null) {
            slot.setOccupied(false);
        }
        
        // Calculate fee
        double fee = FeeCalculator.calculateFee(vehicle.getEntryTime(), exitTime);
        
        // Save to file
        dataStore.saveVehiclesToFile();
        
        return fee;
    }
    
    /**
     * Calculate fee for a vehicle (if checked out) or estimate fee (if still in)
     * @param vehicleNumber Vehicle number
     * @return Fee amount, or -1 if vehicle not found
     */
    public double calculateVehicleFee(String vehicleNumber) {
        if (vehicleNumber == null) {
            return -1;
        }
        
        Vehicle vehicle = dataStore.getVehicleByNumber(vehicleNumber);
        if (vehicle == null) {
            return -1;
        }
        
        if ("OUT".equals(vehicle.getStatus()) && vehicle.getExitTime() != null) {
            // Vehicle is checked out - calculate actual fee
            return FeeCalculator.calculateFee(vehicle.getEntryTime(), vehicle.getExitTime());
        } else {
            // Vehicle is still in - calculate fee to now (estimate)
            return FeeCalculator.calculateFeeToNow(vehicle.getEntryTime());
        }
    }
    
    /**
     * Calculate today's earnings from checked-out vehicles
     * @return Today's total earnings
     */
    public double getTodayEarnings() {
        double totalEarnings = 0.0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());
        
        for (Vehicle v : dataStore.getAllVehicles()) {
            // Only count checked-out vehicles that were checked out today
            if ("OUT".equals(v.getStatus()) && v.getExitTime() != null && v.getExitTime().startsWith(today)) {
                double fee = FeeCalculator.calculateFee(v.getEntryTime(), v.getExitTime());
                totalEarnings += fee;
            }
        }
        
        return Math.round(totalEarnings * 100.0) / 100.0;
    }
    
    /**
     * Calculate total earnings from all checked-out vehicles
     * @return Total earnings
     */
    public double getTotalEarnings() {
        double totalEarnings = 0.0;
        
        for (Vehicle v : dataStore.getAllVehicles()) {
            // Only count checked-out vehicles
            if ("OUT".equals(v.getStatus()) && v.getExitTime() != null) {
                double fee = FeeCalculator.calculateFee(v.getEntryTime(), v.getExitTime());
                totalEarnings += fee;
            }
        }
        
        return Math.round(totalEarnings * 100.0) / 100.0;
    }
    
    /**
     * Get vehicle-wise earnings breakdown
     * @return List of vehicles with their earnings (only checked-out vehicles)
     */
    public ArrayList<VehicleEarning> getVehicleEarnings() {
        ArrayList<VehicleEarning> earnings = new ArrayList<>();
        
        for (Vehicle v : dataStore.getAllVehicles()) {
            // Only include checked-out vehicles
            if ("OUT".equals(v.getStatus()) && v.getExitTime() != null) {
                double fee = FeeCalculator.calculateFee(v.getEntryTime(), v.getExitTime());
                VehicleEarning earning = new VehicleEarning(v, fee);
                earnings.add(earning);
            }
        }
        
        return earnings;
    }
    
    /**
     * Get today's vehicle-wise earnings
     * @return List of vehicles checked out today with their earnings
     */
    public ArrayList<VehicleEarning> getTodayVehicleEarnings() {
        ArrayList<VehicleEarning> earnings = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());
        
        for (Vehicle v : dataStore.getAllVehicles()) {
            // Only include vehicles checked out today
            if ("OUT".equals(v.getStatus()) && v.getExitTime() != null && v.getExitTime().startsWith(today)) {
                double fee = FeeCalculator.calculateFee(v.getEntryTime(), v.getExitTime());
                VehicleEarning earning = new VehicleEarning(v, fee);
                earnings.add(earning);
            }
        }
        
        return earnings;
    }
    
    /**
     * Get data store (for advanced operations if needed)
     * @return ParkingDataStore instance
     */
    public ParkingDataStore getDataStore() {
        return dataStore;
    }
    
    /**
     * Inner class to represent vehicle earnings
     */
    public static class VehicleEarning {
        private Vehicle vehicle;
        private double earnings;
        
        public VehicleEarning(Vehicle vehicle, double earnings) {
            this.vehicle = vehicle;
            this.earnings = earnings;
        }
        
        public Vehicle getVehicle() {
            return vehicle;
        }
        
        public double getEarnings() {
            return earnings;
        }
    }
}


package pms.model;

import pms.util.FileHandler;
import java.util.ArrayList;

/**
 * Data store for managing vehicles and parking slots
 * Provides CRUD operations for the parking management system
 * @author uSer
 */
public class ParkingDataStore {
    private ArrayList<Vehicle> vehicles;
    private ArrayList<ParkingSlot> parkingSlots;
    private static final int TOTAL_SLOTS = 50; // Default total slots
    
    // Constructor
    public ParkingDataStore() {
        this.vehicles = new ArrayList<>();
        this.parkingSlots = new ArrayList<>();
        initializeSlots();
        loadVehiclesFromFile();
    }
    
    /**
     * Load vehicles from file and update slot occupancy
     */
    private void loadVehiclesFromFile() {
        ArrayList<Vehicle> loadedVehicles = FileHandler.loadVehiclesFromFile();
        for (Vehicle v : loadedVehicles) {
            vehicles.add(v);
            // Update slot occupancy based on loaded vehicles
            ParkingSlot slot = getSlotByNumber(v.getSlotNumber());
            if (slot != null && "IN".equals(v.getStatus())) {
                slot.setOccupied(true);
            }
        }
    }
    
    /**
     * Save all vehicles to file
     * @return true if saved successfully, false otherwise
     */
    public boolean saveVehiclesToFile() {
        return FileHandler.saveVehiclesToFile(vehicles);
    }
    
    /**
     * Initialize parking slots
     */
    private void initializeSlots() {
        for (int i = 1; i <= TOTAL_SLOTS; i++) {
            parkingSlots.add(new ParkingSlot(i, false));
        }
    }
    
    /**
     * Add a new vehicle to the system
     * Prevents duplicate IN status for same vehicle number
     * @param v Vehicle to add
     * @return true if added successfully, false otherwise
     */
    public boolean addVehicle(Vehicle v) {
        if (v == null) {
            return false;
        }
        
        // Prevent duplicate IN status for same vehicle number
        if ("IN".equals(v.getStatus())) {
            Vehicle existing = getVehicleByNumber(v.getVehicleNumber());
            if (existing != null && "IN".equals(existing.getStatus())) {
                return false; // Vehicle already has IN status
            }
        }
        
        // Check if slot is available
        ParkingSlot slot = getSlotByNumber(v.getSlotNumber());
        if (slot != null && !slot.isOccupied()) {
            vehicles.add(v);
            if ("IN".equals(v.getStatus())) {
                slot.setOccupied(true);
            }
            return true;
        }
        return false;
    }
    
    /**
     * Get all vehicles
     * @return List of all vehicles
     */
    public ArrayList<Vehicle> getAllVehicles() {
        return new ArrayList<>(vehicles);
    }
    
    /**
     * Get all vehicles with status "IN"
     * @return List of vehicles currently in parking
     */
    public ArrayList<Vehicle> getActiveVehicles() {
        ArrayList<Vehicle> activeVehicles = new ArrayList<>();
        for (Vehicle v : vehicles) {
            if ("IN".equals(v.getStatus())) {
                activeVehicles.add(v);
            }
        }
        return activeVehicles;
    }
    
    /**
     * Update a vehicle by vehicle number
     * @param vehicleNumber Vehicle number to find
     * @param updated Updated vehicle object
     * @return true if updated successfully, false otherwise
     */
    public boolean updateVehicle(String vehicleNumber, Vehicle updated) {
        if (vehicleNumber == null || updated == null) {
            return false;
        }
        
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle v = vehicles.get(i);
            if (vehicleNumber.equals(v.getVehicleNumber())) {
                // If slot changed, update slot occupancy
                if (v.getSlotNumber() != updated.getSlotNumber()) {
                    ParkingSlot oldSlot = getSlotByNumber(v.getSlotNumber());
                    ParkingSlot newSlot = getSlotByNumber(updated.getSlotNumber());
                    
                    if (oldSlot != null) {
                        oldSlot.setOccupied(false);
                    }
                    if (newSlot != null && !newSlot.isOccupied()) {
                        newSlot.setOccupied(true);
                    } else if (newSlot != null && newSlot.isOccupied()) {
                        return false; // New slot is already occupied
                    }
                }
                vehicles.set(i, updated);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Delete a vehicle by vehicle number
     * @param vehicleNumber Vehicle number to delete
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteVehicle(String vehicleNumber) {
        if (vehicleNumber == null) {
            return false;
        }
        
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle v = vehicles.get(i);
            if (vehicleNumber.equals(v.getVehicleNumber())) {
                // Free up the slot
                ParkingSlot slot = getSlotByNumber(v.getSlotNumber());
                if (slot != null) {
                    slot.setOccupied(false);
                }
                vehicles.remove(i);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get vehicle by vehicle number
     * @param vehicleNumber Vehicle number to search
     * @return Vehicle if found, null otherwise
     */
    public Vehicle getVehicleByNumber(String vehicleNumber) {
        if (vehicleNumber == null) {
            return null;
        }
        
        for (Vehicle v : vehicles) {
            if (vehicleNumber.equals(v.getVehicleNumber())) {
                return v;
            }
        }
        return null;
    }
    
    /**
     * Get parking slot by slot number
     * @param slotNumber Slot number to find
     * @return ParkingSlot if found, null otherwise
     */
    public ParkingSlot getSlotByNumber(int slotNumber) {
        for (ParkingSlot slot : parkingSlots) {
            if (slot.getSlotNumber() == slotNumber) {
                return slot;
            }
        }
        return null;
    }
    
    /**
     * Get all parking slots
     * @return List of all parking slots
     */
    public ArrayList<ParkingSlot> getAllSlots() {
        return new ArrayList<>(parkingSlots);
    }
    
    /**
     * Get available slots
     * @return List of available (unoccupied) slots
     */
    public ArrayList<ParkingSlot> getAvailableSlots() {
        ArrayList<ParkingSlot> available = new ArrayList<>();
        for (ParkingSlot slot : parkingSlots) {
            if (!slot.isOccupied()) {
                available.add(slot);
            }
        }
        return available;
    }
    
    /**
     * Get total number of slots
     * @return Total number of slots
     */
    public int getTotalSlots() {
        return parkingSlots.size();
    }
    
    /**
     * Get number of occupied slots
     * @return Number of occupied slots
     */
    public int getOccupiedSlots() {
        int count = 0;
        for (ParkingSlot slot : parkingSlots) {
            if (slot.isOccupied()) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Get number of available slots
     * @return Number of available slots
     */
    public int getAvailableSlotsCount() {
        return getTotalSlots() - getOccupiedSlots();
    }
    
    /**
     * Check if a slot is occupied
     * @param slotNumber Slot number to check
     * @return true if occupied, false otherwise
     */
    public boolean isSlotOccupied(int slotNumber) {
        ParkingSlot slot = getSlotByNumber(slotNumber);
        return slot != null && slot.isOccupied();
    }
}


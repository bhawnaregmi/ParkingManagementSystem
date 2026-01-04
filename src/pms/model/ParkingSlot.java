package pms.model;

/**
 * Represents a parking slot in the parking management system
 * @author uSer
 */
public class ParkingSlot {
    private int slotNumber;
    private boolean isOccupied;
    
    // Default constructor
    public ParkingSlot() {
        this.isOccupied = false;
    }
    
    // Parameterized constructor
    public ParkingSlot(int slotNumber, boolean isOccupied) {
        this.slotNumber = slotNumber;
        this.isOccupied = isOccupied;
    }
    
    // Getters and Setters
    public int getSlotNumber() {
        return slotNumber;
    }
    
    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }
    
    public boolean isOccupied() {
        return isOccupied;
    }
    
    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
    
    @Override
    public String toString() {
        return "ParkingSlot{" +
                "slotNumber=" + slotNumber +
                ", isOccupied=" + isOccupied +
                '}';
    }
}


package pms.model;

/**
 * Represents a vehicle in the parking management system
 * @author uSer
 */
public class Vehicle {
    private String vehicleNumber;
    private String vehicleType; // Car, Bike, Van
    private int slotNumber;
    private String entryTime;
    private String exitTime; // Exit time when vehicle leaves
    private String status; // "IN", "OUT"
    
    // Default constructor
    public Vehicle() {
        this.status = "IN";
    }
    
    // Parameterized constructor
    public Vehicle(String vehicleNumber, String vehicleType, int slotNumber, String entryTime, String status) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.slotNumber = slotNumber;
        this.entryTime = entryTime;
        this.status = status;
        this.exitTime = null;
    }
    
    // Full parameterized constructor with exit time
    public Vehicle(String vehicleNumber, String vehicleType, int slotNumber, String entryTime, String exitTime, String status) {
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.slotNumber = slotNumber;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.status = status;
    }
    
    // Getters and Setters
    public String getVehicleNumber() {
        return vehicleNumber;
    }
    
    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
    
    public String getVehicleType() {
        return vehicleType;
    }
    
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    
    public int getSlotNumber() {
        return slotNumber;
    }
    
    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }
    
    public String getEntryTime() {
        return entryTime;
    }
    
    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getExitTime() {
        return exitTime;
    }
    
    public void setExitTime(String exitTime) {
        this.exitTime = exitTime;
    }
    
    @Override
    public String toString() {
        return "Vehicle{" + 
                "vehicleNumber='" + vehicleNumber + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", slotNumber=" + slotNumber +
                ", entryTime='" + entryTime + '\'' +
                ", exitTime='" + exitTime + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vehicle vehicle = (Vehicle) obj;
        return vehicleNumber != null && vehicleNumber.equals(vehicle.vehicleNumber);
    }
    
    @Override
    public int hashCode() {
        return vehicleNumber != null ? vehicleNumber.hashCode() : 0;
    }
}


package pms.util;

import pms.model.Vehicle;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Utility class for validation operations
 * @author uSer
 */
public class Validator {
    
    // Vehicle number pattern: alphanumeric, 3-15 characters
    private static final Pattern VEHICLE_NUMBER_PATTERN = Pattern.compile("^[A-Z0-9]{3,15}$", Pattern.CASE_INSENSITIVE);
    
    /**
     * Check if a string is empty or null
     * @param str String to check
     * @return true if empty or null, false otherwise
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Validate vehicle number format
     * @param vehicleNumber Vehicle number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidVehicleNumber(String vehicleNumber) {
        if (isEmpty(vehicleNumber)) {
            return false;
        }
        return VEHICLE_NUMBER_PATTERN.matcher(vehicleNumber.trim()).matches();
    }
    
    /**
     * Check if a string represents a positive integer
     * @param str String to check
     * @return true if it's a positive integer, false otherwise
     */
    public static boolean isPositiveInt(String str) {
        if (isEmpty(str)) {
            return false;
        }
        
        try {
            int value = Integer.parseInt(str.trim());
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Parse string to positive integer
     * @param str String to parse
     * @return Integer value, or -1 if invalid
     */
    public static int parsePositiveInt(String str) {
        if (isEmpty(str)) {
            return -1;
        }
        
        try {
            int value = Integer.parseInt(str.trim());
            return value > 0 ? value : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * Check if vehicle number already exists in the list
     * @param vehicleNumber Vehicle number to check
     * @param vehicles List of vehicles
     * @return true if duplicate exists, false otherwise
     */
    public static boolean isDuplicateVehicle(String vehicleNumber, List<Vehicle> vehicles) {
        if (isEmpty(vehicleNumber) || vehicles == null) {
            return false;
        }
        
        String normalizedNumber = vehicleNumber.trim().toUpperCase();
        for (Vehicle v : vehicles) {
            if (v != null && normalizedNumber.equals(v.getVehicleNumber().toUpperCase())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if vehicle number already exists (excluding a specific vehicle)
     * Useful for update operations
     * @param vehicleNumber Vehicle number to check
     * @param excludeVehicleNumber Vehicle number to exclude from check
     * @param vehicles List of vehicles
     * @return true if duplicate exists, false otherwise
     */
    public static boolean isDuplicateVehicle(String vehicleNumber, String excludeVehicleNumber, List<Vehicle> vehicles) {
        if (isEmpty(vehicleNumber) || vehicles == null) {
            return false;
        }
        
        String normalizedNumber = vehicleNumber.trim().toUpperCase();
        String normalizedExclude = excludeVehicleNumber != null ? excludeVehicleNumber.trim().toUpperCase() : "";
        
        for (Vehicle v : vehicles) {
            if (v != null && normalizedNumber.equals(v.getVehicleNumber().toUpperCase())) {
                // Skip if it's the vehicle we're updating
                if (!normalizedExclude.equals(v.getVehicleNumber().toUpperCase())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Validate vehicle type
     * @param vehicleType Vehicle type to validate
     * @return true if valid (Car, Bike, or Van), false otherwise
     */
    public static boolean isValidVehicleType(String vehicleType) {
        if (isEmpty(vehicleType)) {
            return false;
        }
        String type = vehicleType.trim();
        return "Car".equalsIgnoreCase(type) || 
               "Bike".equalsIgnoreCase(type) || 
               "Van".equalsIgnoreCase(type);
    }
    
    /**
     * Validate slot number range
     * @param slotNumber Slot number to validate
     * @param maxSlots Maximum number of slots
     * @return true if valid, false otherwise
     */
    public static boolean isValidSlotNumber(int slotNumber, int maxSlots) {
        return slotNumber > 0 && slotNumber <= maxSlots;
    }
}


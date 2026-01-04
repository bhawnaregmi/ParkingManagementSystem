package pms.util;

import pms.model.Vehicle;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles file-based data persistence for vehicles
 * Saves and loads vehicle data to/from a text file
 * @author uSer
 */
public class FileHandler {
    private static final String DATA_FILE = "parking_data.txt";
    
    /**
     * Saves a list of vehicles to a file
     * Format: vehicleNumber|vehicleType|slotNumber|entryTime|exitTime|status
     * @param vehicles List of vehicles to save
     * @return true if saved successfully, false otherwise
     */
    public static boolean saveVehiclesToFile(List<Vehicle> vehicles) {
        if (vehicles == null) {
            return false;
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Vehicle v : vehicles) {
                if (v != null) {
                    String exitTime = v.getExitTime() != null ? v.getExitTime() : "";
                    writer.println(
                        v.getVehicleNumber() + "|" +
                        v.getVehicleType() + "|" +
                        v.getSlotNumber() + "|" +
                        v.getEntryTime() + "|" +
                        exitTime + "|" +
                        v.getStatus()
                    );
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving vehicles to file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Loads vehicles from a file
     * @return List of vehicles loaded from file, empty list if file doesn't exist or error occurs
     */
    public static ArrayList<Vehicle> loadVehiclesFromFile() {
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        File file = new File(DATA_FILE);
        
        // If file doesn't exist, return empty list
        if (!file.exists()) {
            return vehicles;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                
                String[] parts = line.split("\\|");
                // Support both old format (5 parts) and new format (6 parts with exitTime)
                if (parts.length == 5 || parts.length == 6) {
                    try {
                        String vehicleNumber = parts[0];
                        String vehicleType = parts[1];
                        int slotNumber = Integer.parseInt(parts[2]);
                        String entryTime = parts[3];
                        String exitTime = (parts.length == 6 && !parts[4].isEmpty()) ? parts[4] : null;
                        String status = parts.length == 6 ? parts[5] : parts[4];
                        
                        Vehicle vehicle;
                        if (exitTime != null) {
                            vehicle = new Vehicle(vehicleNumber, vehicleType, slotNumber, entryTime, exitTime, status);
                        } else {
                            vehicle = new Vehicle(vehicleNumber, vehicleType, slotNumber, entryTime, status);
                        }
                        vehicles.add(vehicle);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing vehicle data: " + line);
                        // Skip invalid line
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading vehicles from file: " + e.getMessage());
        }
        
        return vehicles;
    }
    
    /**
     * Checks if the data file exists
     * @return true if file exists, false otherwise
     */
    public static boolean dataFileExists() {
        return new File(DATA_FILE).exists();
    }
    
    /**
     * Gets the path to the data file
     * @return String path to the data file
     */
    public static String getDataFilePath() {
        return new File(DATA_FILE).getAbsolutePath();
    }
}


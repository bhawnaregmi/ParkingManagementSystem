package pms.util;

import pms.model.Vehicle;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for generating parking bills/receipts
 * @author uSer
 */
public class BillGenerator {
    
    /**
     * Generate a formatted bill/receipt for a vehicle
     * @param vehicle Vehicle to generate bill for
     * @return Formatted bill string
     */
    public static String generateBill(Vehicle vehicle) {
        if (vehicle == null) {
            return "Error: Vehicle not found!";
        }
        
        StringBuilder bill = new StringBuilder();
        
        // Bill Header
        bill.append("========================================\n");
        bill.append("     PARKING MANAGEMENT SYSTEM\n");
        bill.append("          PARKING RECEIPT\n");
        bill.append("========================================\n\n");
        
        // Vehicle Information
        bill.append("VEHICLE INFORMATION:\n");
        bill.append("  Vehicle Number: ").append(vehicle.getVehicleNumber()).append("\n");
        bill.append("  Vehicle Type:   ").append(vehicle.getVehicleType()).append("\n");
        bill.append("  Slot Number:    ").append(vehicle.getSlotNumber()).append("\n");
        bill.append("  Status:         ").append(vehicle.getStatus()).append("\n\n");
        
        // Parking Time Information
        bill.append("PARKING TIME:\n");
        bill.append("  Entry Time:     ").append(vehicle.getEntryTime()).append("\n");
        
        if (vehicle.getExitTime() != null && !vehicle.getExitTime().isEmpty()) {
            bill.append("  Exit Time:      ").append(vehicle.getExitTime()).append("\n");
        } else {
            bill.append("  Exit Time:      ").append("Still Parked").append("\n");
        }
        
        // Calculate hours and fee
        double hours = 0.0;
        double fee = 0.0;
        
        if (vehicle.getExitTime() != null && !vehicle.getExitTime().isEmpty()) {
            hours = FeeCalculator.calculateHours(vehicle.getEntryTime(), vehicle.getExitTime());
            fee = FeeCalculator.calculateFee(vehicle.getEntryTime(), vehicle.getExitTime());
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(new Date());
            hours = FeeCalculator.calculateHours(vehicle.getEntryTime(), currentTime);
            fee = FeeCalculator.calculateFeeToNow(vehicle.getEntryTime());
        }
        
        bill.append("  Hours Parked:   ").append(String.format("%.2f", hours)).append(" hours\n\n");
        
        // Fee Information
        bill.append("PAYMENT INFORMATION:\n");
        if (vehicle.getExitTime() != null && !vehicle.getExitTime().isEmpty()) {
            bill.append("  Total Fee:      ").append(FeeCalculator.formatFee(fee)).append("\n");
        } else {
            bill.append("  Estimated Fee:  ").append(FeeCalculator.formatFee(fee)).append("\n");
            bill.append("  (Fee calculated at checkout)\n");
        }
        bill.append("\n");
        
        // Fee Rates
        bill.append("FEE RATES:\n");
        bill.append("  Minimum Fee:    ").append(FeeCalculator.formatFee(FeeCalculator.getMinimumFee())).append("\n");
        bill.append("  Hourly Rate:    ").append(FeeCalculator.formatFee(FeeCalculator.getHourlyRate())).append("/hour\n");
        bill.append("  Daily Maximum:  ").append(FeeCalculator.formatFee(FeeCalculator.getDailyRate())).append("/day\n\n");
        
        // Footer
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bill.append("Generated on: ").append(sdf.format(new Date())).append("\n");
        bill.append("========================================\n");
        bill.append("        THANK YOU FOR PARKING!\n");
        bill.append("========================================\n");
        
        return bill.toString();
    }
    
    /**
     * Generate a simple bill for display in dialog
     * @param vehicle Vehicle to generate bill for
     * @return Formatted bill string (HTML format for JOptionPane)
     */
    public static String generateBillHTML(Vehicle vehicle) {
        if (vehicle == null) {
            return "<html><body><h3>Error: Vehicle not found!</h3></body></html>";
        }
        
        StringBuilder bill = new StringBuilder();
        bill.append("<html><body style='font-family: Arial; padding: 10px;'>");
        
        // Header
        bill.append("<div style='text-align: center; border-bottom: 2px solid #000; padding-bottom: 10px; margin-bottom: 15px;'>");
        bill.append("<h2 style='margin: 5px;'>PARKING MANAGEMENT SYSTEM</h2>");
        bill.append("<h3 style='margin: 5px; color: #333;'>PARKING RECEIPT</h3>");
        bill.append("</div>");
        
        // Vehicle Information
        bill.append("<div style='margin-bottom: 15px;'>");
        bill.append("<h3 style='color: #0066cc; margin-bottom: 10px;'>VEHICLE INFORMATION</h3>");
        bill.append("<table style='width: 100%; border-collapse: collapse;'>");
        bill.append("<tr><td style='padding: 5px; font-weight: bold;'>Vehicle Number:</td><td style='padding: 5px;'>").append(vehicle.getVehicleNumber()).append("</td></tr>");
        bill.append("<tr><td style='padding: 5px; font-weight: bold;'>Vehicle Type:</td><td style='padding: 5px;'>").append(vehicle.getVehicleType()).append("</td></tr>");
        bill.append("<tr><td style='padding: 5px; font-weight: bold;'>Slot Number:</td><td style='padding: 5px;'>").append(vehicle.getSlotNumber()).append("</td></tr>");
        bill.append("<tr><td style='padding: 5px; font-weight: bold;'>Status:</td><td style='padding: 5px;'>").append(vehicle.getStatus()).append("</td></tr>");
        bill.append("</table>");
        bill.append("</div>");
        
        // Parking Time
        bill.append("<div style='margin-bottom: 15px;'>");
        bill.append("<h3 style='color: #0066cc; margin-bottom: 10px;'>PARKING TIME</h3>");
        bill.append("<table style='width: 100%; border-collapse: collapse;'>");
        bill.append("<tr><td style='padding: 5px; font-weight: bold;'>Entry Time:</td><td style='padding: 5px;'>").append(vehicle.getEntryTime()).append("</td></tr>");
        
        if (vehicle.getExitTime() != null && !vehicle.getExitTime().isEmpty()) {
            bill.append("<tr><td style='padding: 5px; font-weight: bold;'>Exit Time:</td><td style='padding: 5px;'>").append(vehicle.getExitTime()).append("</td></tr>");
        } else {
            bill.append("<tr><td style='padding: 5px; font-weight: bold;'>Exit Time:</td><td style='padding: 5px; color: #ff6600;'>Still Parked</td></tr>");
        }
        
        // Calculate hours
        double hours = 0.0;
        if (vehicle.getExitTime() != null && !vehicle.getExitTime().isEmpty()) {
            hours = FeeCalculator.calculateHours(vehicle.getEntryTime(), vehicle.getExitTime());
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(new Date());
            hours = FeeCalculator.calculateHours(vehicle.getEntryTime(), currentTime);
        }
        
        bill.append("<tr><td style='padding: 5px; font-weight: bold;'>Hours Parked:</td><td style='padding: 5px; font-size: 16px; color: #0066cc;'><b>").append(String.format("%.2f", hours)).append(" hours</b></td></tr>");
        bill.append("</table>");
        bill.append("</div>");
        
        // Payment Information
        bill.append("<div style='margin-bottom: 15px; background-color: #f0f0f0; padding: 10px; border-radius: 5px;'>");
        bill.append("<h3 style='color: #0066cc; margin-bottom: 10px;'>PAYMENT INFORMATION</h3>");
        
        double fee = 0.0;
        if (vehicle.getExitTime() != null && !vehicle.getExitTime().isEmpty()) {
            fee = FeeCalculator.calculateFee(vehicle.getEntryTime(), vehicle.getExitTime());
            bill.append("<p style='font-size: 18px; color: #cc0000;'><b>Total Fee: ").append(FeeCalculator.formatFee(fee)).append("</b></p>");
        } else {
            fee = FeeCalculator.calculateFeeToNow(vehicle.getEntryTime());
            bill.append("<p style='font-size: 18px; color: #ff6600;'><b>Estimated Fee: ").append(FeeCalculator.formatFee(fee)).append("</b></p>");
            bill.append("<p style='font-size: 12px; color: #666;'>(Fee calculated at checkout)</p>");
        }
        bill.append("</div>");
        
        // Fee Rates
        bill.append("<div style='margin-bottom: 15px;'>");
        bill.append("<h4 style='color: #666; margin-bottom: 5px;'>Fee Rates:</h4>");
        bill.append("<ul style='margin: 5px; padding-left: 20px;'>");
        bill.append("<li>Minimum Fee: ").append(FeeCalculator.formatFee(FeeCalculator.getMinimumFee())).append("</li>");
        bill.append("<li>Hourly Rate: ").append(FeeCalculator.formatFee(FeeCalculator.getHourlyRate())).append("/hour</li>");
        bill.append("<li>Daily Maximum: ").append(FeeCalculator.formatFee(FeeCalculator.getDailyRate())).append("/day</li>");
        bill.append("</ul>");
        bill.append("</div>");
        
        // Footer
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bill.append("<div style='text-align: center; border-top: 2px solid #000; padding-top: 10px; margin-top: 15px; color: #666; font-size: 12px;'>");
        bill.append("<p>Generated on: ").append(sdf.format(new Date())).append("</p>");
        bill.append("<p style='font-size: 14px; color: #0066cc;'><b>THANK YOU FOR PARKING!</b></p>");
        bill.append("</div>");
        
        bill.append("</body></html>");
        
        return bill.toString();
    }
}


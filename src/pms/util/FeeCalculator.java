package pms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for calculating parking fees
 * @author uSer
 */
public class FeeCalculator {
    
    // Fee rates (in local currency units)
    private static final double HOURLY_RATE = 5.0; // $5 per hour
    private static final double DAILY_RATE = 50.0; // $50 per day (maximum)
    private static final double MINIMUM_FEE = 2.0; // Minimum $2 for any parking
    
    // Date format for parsing entry/exit times
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Calculate parking fee based on entry and exit times
     * @param entryTime Entry time string (format: yyyy-MM-dd HH:mm:ss)
     * @param exitTime Exit time string (format: yyyy-MM-dd HH:mm:ss)
     * @return Calculated fee amount
     */
    public static double calculateFee(String entryTime, String exitTime) {
        if (entryTime == null || exitTime == null || entryTime.isEmpty() || exitTime.isEmpty()) {
            return 0.0;
        }
        
        try {
            Date entry = DATE_FORMAT.parse(entryTime);
            Date exit = DATE_FORMAT.parse(exitTime);
            
            return calculateFee(entry, exit);
        } catch (ParseException e) {
            System.err.println("Error parsing dates: " + e.getMessage());
            return 0.0;
        }
    }
    
    /**
     * Calculate parking fee based on entry and exit Date objects
     * @param entryTime Entry time
     * @param exitTime Exit time
     * @return Calculated fee amount
     */
    public static double calculateFee(Date entryTime, Date exitTime) {
        if (entryTime == null || exitTime == null) {
            return 0.0;
        }
        
        // Ensure exit time is after entry time
        if (exitTime.before(entryTime)) {
            return 0.0;
        }
        
        // Calculate time difference in milliseconds
        long timeDifference = exitTime.getTime() - entryTime.getTime();
        
        // Convert to hours (with decimal precision)
        double hours = timeDifference / (1000.0 * 60.0 * 60.0);
        
        // Calculate fee
        double fee = 0.0;
        
        if (hours <= 0.5) {
            // First 30 minutes: minimum fee
            fee = MINIMUM_FEE;
        } else if (hours <= 24) {
            // Hourly rate for up to 24 hours
            fee = Math.ceil(hours) * HOURLY_RATE;
            // Apply daily maximum
            if (fee > DAILY_RATE) {
                fee = DAILY_RATE;
            }
        } else {
            // More than 24 hours: daily rate + hourly for remainder
            int days = (int) (hours / 24);
            double remainingHours = hours % 24;
            
            fee = (days * DAILY_RATE) + (Math.ceil(remainingHours) * HOURLY_RATE);
        }
        
        // Ensure minimum fee
        if (fee < MINIMUM_FEE) {
            fee = MINIMUM_FEE;
        }
        
        return Math.round(fee * 100.0) / 100.0; // Round to 2 decimal places
    }
    
    /**
     * Calculate parking fee from entry time to current time
     * @param entryTime Entry time string (format: yyyy-MM-dd HH:mm:ss)
     * @return Calculated fee amount
     */
    public static double calculateFeeToNow(String entryTime) {
        if (entryTime == null || entryTime.isEmpty()) {
            return 0.0;
        }
        
        try {
            Date entry = DATE_FORMAT.parse(entryTime);
            Date now = new Date();
            return calculateFee(entry, now);
        } catch (ParseException e) {
            System.err.println("Error parsing entry time: " + e.getMessage());
            return 0.0;
        }
    }
    
    /**
     * Calculate hours parked
     * @param entryTime Entry time string
     * @param exitTime Exit time string
     * @return Hours parked (rounded to 2 decimal places)
     */
    public static double calculateHours(String entryTime, String exitTime) {
        if (entryTime == null || exitTime == null || entryTime.isEmpty() || exitTime.isEmpty()) {
            return 0.0;
        }
        
        try {
            Date entry = DATE_FORMAT.parse(entryTime);
            Date exit = DATE_FORMAT.parse(exitTime);
            
            long timeDifference = exit.getTime() - entry.getTime();
            double hours = timeDifference / (1000.0 * 60.0 * 60.0);
            
            return Math.round(hours * 100.0) / 100.0;
        } catch (ParseException e) {
            return 0.0;
        }
    }
    
    /**
     * Get hourly rate
     * @return Hourly parking rate
     */
    public static double getHourlyRate() {
        return HOURLY_RATE;
    }
    
    /**
     * Get daily rate
     * @return Daily parking rate
     */
    public static double getDailyRate() {
        return DAILY_RATE;
    }
    
    /**
     * Get minimum fee
     * @return Minimum parking fee
     */
    public static double getMinimumFee() {
        return MINIMUM_FEE;
    }
    
    /**
     * Format fee as currency string
     * @param fee Fee amount
     * @return Formatted currency string
     */
    public static String formatFee(double fee) {
        return String.format("$%.2f", fee);
    }
}


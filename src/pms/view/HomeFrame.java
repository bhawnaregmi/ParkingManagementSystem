package pms.view;

import pms.controller.ParkingController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Home frame - Main entry point of the application
 * @author uSer
 */
public class HomeFrame extends javax.swing.JFrame {
    private ParkingController controller;
    private String userRole;
    private JButton btnOpenDashboard;
    private JButton btnExit;
    private JButton btnLogout;
    
    /**
     * Creates new form HomeFrame
     * @param userRole User role (Admin or Staff)
     */
    public HomeFrame(String userRole) {
        this.userRole = userRole;
        initComponents();
        this.controller = ParkingController.getInstance();
    }
    
    /**
     * Default constructor (for backward compatibility)
     */
    public HomeFrame() {
        this("Admin"); // Default to Admin
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Parking Management System - Home");
        setResizable(false);
        
        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Title label
        JLabel titleLabel = new JLabel("Parking Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        
        // User role label
        JLabel roleLabel = new JLabel("Logged in as: " + userRole, JLabel.CENTER);
        roleLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        roleLabel.setForeground(Color.GRAY);
        roleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        
        btnOpenDashboard = new JButton("Open Dashboard");
        btnOpenDashboard.setFont(new Font("Arial", Font.PLAIN, 16));
        btnOpenDashboard.setPreferredSize(new Dimension(200, 50));
        btnOpenDashboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDashboard();
            }
        });
        
        btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Arial", Font.PLAIN, 16));
        btnLogout.setPreferredSize(new Dimension(200, 50));
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        
        btnExit = new JButton("Exit");
        btnExit.setFont(new Font("Arial", Font.PLAIN, 16));
        btnExit.setPreferredSize(new Dimension(200, 50));
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitApplication();
            }
        });
        
        buttonPanel.add(btnOpenDashboard);
        buttonPanel.add(btnLogout);
        buttonPanel.add(btnExit);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(roleLabel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    /**
     * Open dashboard frame
     */
    private void openDashboard() {
        this.setVisible(false);
        controller.openDashboard(this, userRole);
    }
    
    /**
     * Logout and return to login screen
     */
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Logout Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Save data before logout
            controller.saveAllData();
            this.dispose();
            new LoginFrame().setVisible(true);
        }
    }
    
    /**
     * Exit application
     */
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit?",
            "Exit Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Save data before exit
            controller.saveAllData();
            System.exit(0);
        }
    }
    
    /**
     * Get current user role
     * @return User role (Admin or Staff)
     */
    public String getUserRole() {
        return userRole;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomeFrame().setVisible(true);
            }
        });
    }
}


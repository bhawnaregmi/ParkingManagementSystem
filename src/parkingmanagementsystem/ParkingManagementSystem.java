/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package parkingmanagementsystem;

import pms.view.HomeFrame;

/**
 * Main class to launch the Parking Management System
 * @author uSer
 */
public class ParkingManagementSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ParkingManagementSystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        /* Create and display the login frame */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new pms.view.LoginFrame().setVisible(true);
            }
        });
    }
    
}

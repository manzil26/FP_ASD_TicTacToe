/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231037 - Al-khiqmah Manzilatul Mukaromah
 * 2 - 5026231095 - Akhtar Zia Faizarrobbi
 * 3 - 5026231227 - Arjuna Veetaraq
 */


package connectFour;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static final String TITLE = "Connect Four";
    public static void main(String[] args) {
        JFrame frame = new JFrame(TITLE);
        // Set the content-pane of the JFrame to an instance of main JPanel
        frame.setContentPane(new GameMain());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null); // center the application window
        frame.setVisible(true);
    }
}

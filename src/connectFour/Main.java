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
    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);

        // Input name for the Human player
        System.out.print("Enter name for Human player: ");
        String humanName = scanner.nextLine();

        // Set AI's name automatically to "Computer"
        String aiName = "Computer";

        // Create a new Board instance
        Board gameBoard = new Board();

        // Create a new Connect4 game instance with the human player, AI player, and the board
        Connect4 game = new Connect4(humanName, aiName, gameBoard);

        // Start the game
        game.playGame();
    }
}

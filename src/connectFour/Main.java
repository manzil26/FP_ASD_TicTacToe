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

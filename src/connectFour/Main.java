package connectFour;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        boolean isAIMode = true;  // Set true untuk mode AI
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.print("Enter name for Human player: ");
        String humanName = scanner.nextLine();
        System.out.print("Enter name for AI player: ");
        String aiName = scanner.nextLine();

        // Membuat game baru dan memulai permainan
        Connect4 game = new Connect4(humanName, aiName);

    }
}

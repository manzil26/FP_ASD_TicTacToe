package connectFour;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        boolean isAIMode = true;  // Set true untuk mode AI

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Connect4");  // Judul Game
            GameMain game = new GameMain();
            game.initGame(isAIMode);  // Inisialisasi game dengan AI mode
            frame.setContentPane(game);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

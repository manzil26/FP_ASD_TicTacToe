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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Tic-Tac-Toe: Two-player Graphic version with better OO design.
 * The Board and Cell classes are separated in their own classes.
 */
public class GameMain extends JPanel {
    private AIPlayerMinimax aiPlayer;
    private static final long serialVersionUID = 1L; // to prevent serializable warning
    private boolean isAIMode = true; // Default: manusia vs AI


    // Define named constants for the drawing graphics
    public static final String TITLE = "Connect Four";
    public static final Color COLOR_BG = Color.WHITE;
    // Tema Pantai Saat Senja
    public static final Color COLOR_BG_STATUS = new Color(230, 190, 255, 200); // Latar belakang ungu lavender lembut dengan transparansi
    public static final Color COLOR_CROSS = new Color(255, 127, 80, 215); // Warna salib jingga coral hangat (Coral)
    public static final Color COLOR_NOUGHT = new Color(255, 165, 0); // Warna lingkaran jingga keemasan (Orange)
    public static final Font FONT_STATUS = new Font("Arial Rounded MT Bold", Font.BOLD, 14); // Font yang elegan namun tetap kasual


    // Define game objects
    private Board board;         // the game board
    private State currentState;  // the current state of the game
    private Seed currentPlayer;  // the current player
    private JLabel statusBar;    // for displaying status message

    // SoundEffect objects for sound effects
    private SoundEffect moveSound;
    private SoundEffect winSound;
    private SoundEffect drawSound;
    private SoundEffect moveAISound;
    public static Player player1 = new Player("Player 1");
    public static Player player2 = new Player("Player 2");
    private JLabel player1ScoreLabel;
    private JLabel player2ScoreLabel;

    /** Constructor to setup the UI and game components */
    public GameMain() {
        board = new Board();
        aiPlayer = new AIPlayerMinimax(board);
        aiPlayer.setSeed(Seed.NOUGHT); // Pastikan seed AI diatur di sini


        // Initialize sound effects
        moveSound = new SoundEffect("connectFour/beep.wav");
        winSound = new SoundEffect("connectFour/mati.wav");
        drawSound = new SoundEffect("connectFour/meledak.wav");
        moveAISound = new SoundEffect("connectFour/cute.wav"); // File suara langkah AI

        player1ScoreLabel = new JLabel("Player X (Score: 0)");
        player2ScoreLabel = new JLabel("Player O (Score: 0)");

        player1ScoreLabel.setFont(FONT_STATUS);
        player2ScoreLabel.setFont(FONT_STATUS);

        player1ScoreLabel.setHorizontalAlignment(JLabel.CENTER);
        player2ScoreLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel scorePanel = new JPanel(new GridLayout(1, 2));
        scorePanel.add(player1ScoreLabel);
        scorePanel.add(player2ScoreLabel);

        super.add(scorePanel, BorderLayout.PAGE_START);




        super.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int colSelected = mouseX / Cell.SIZE;

                if (currentState == State.PLAYING) {
                    if (currentPlayer == Seed.CROSS) {
                        // Langkah manusia
                        if (colSelected >= 0 && colSelected < Board.COLS) {
                            for (int row = Board.ROWS - 1; row >= 0; row--) {
                                if (board.cells[row][colSelected].content == Seed.NO_SEED) {
                                    currentState = board.stepGame(currentPlayer, row, colSelected);
                                    moveSound.play();

                                    // Periksa kemenangan atau hasil seri
                                    if (currentState == State.CROSS_WON || currentState == State.NOUGHT_WON) {
                                        winSound.play();
                                    } else if (currentState == State.DRAW) {
                                        drawSound.play();
                                    }

                                    // Berikan giliran ke AI dengan jeda
                                    currentPlayer = Seed.NOUGHT;
                                    Timer aiTimer = new Timer(500, event -> { // Jeda 500 ms
                                        if (currentPlayer == Seed.NOUGHT && currentState == State.PLAYING) {
                                            // Langkah AI
                                            int[] aiMove = aiPlayer.move();
                                            currentState = board.stepGame(currentPlayer, aiMove[0], aiMove[1]);
                                            moveAISound.play();

                                            // Periksa kemenangan atau hasil seri
                                            if (currentState == State.CROSS_WON || currentState == State.NOUGHT_WON) {
                                                winSound.play();
                                            } else if (currentState == State.DRAW) {
                                                drawSound.play();
                                            }

                                            // Berikan giliran kembali ke manusia
                                            currentPlayer = Seed.CROSS;
                                            repaint();
                                        }
                                    });
                                    aiTimer.setRepeats(false);
                                    aiTimer.start();
                                    repaint();
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    newGame();
                    repaint();
                }
            }

        });



        // Setup the status bar (JLabel) to display status message
        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));
        statusBar.setText(String.format("X: %d | O: %d - %s's Turn",
                player1.getScore(), player2.getScore(),
                (currentPlayer == Seed.CROSS ? "X" : "O")));

        super.setLayout(new BorderLayout());
        super.add(statusBar, BorderLayout.PAGE_END); // same as SOUTH
        super.setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        // account for statusBar in height
        super.setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));

        // Set up Game
        initGame(true);
        newGame();
    }

    /** Initialize the game (run once) */
    public void initGame(boolean isAIMode) {
        this.isAIMode = isAIMode;
        board = new Board();

        // Inisialisasi pemain
        player1 = new Player("Player X");
        player2 = new Player("Player O");

        if (isAIMode) {
            aiPlayer = new AIPlayerMinimax(board);
        }
    }



    /** Reset the game-board contents and the current-state, ready for new game */
    public void newGame() {
        for (int row = 0; row < Board.ROWS; ++row) {
            for (int col = 0; col < Board.COLS; ++col) {
                board.cells[row][col].content = Seed.NO_SEED; // all cells empty
            }
        }

        currentPlayer = Seed.CROSS;    // cross plays first
        currentState = State.PLAYING;  // ready to play
    }

    /** Custom painting codes on this JPanel */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG); // set its background color
        player1ScoreLabel.setText(String.format("Player X (Score: %d)", GameMain.player1.getScore()));
        player2ScoreLabel.setText(String.format("Player O (Score: %d)", GameMain.player2.getScore()));

        board.paint(g); // Ask the game board to paint itself

        // Tampilkan status giliran dan skor pemain
        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText(String.format("X's Turn (Score: %d) vs O's Turn (Score: %d)",
                    GameMain.player1.getScore(), GameMain.player2.getScore()));
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText(String.format("It's a Draw! Final Score: X: %d, O: %d. Click to play again.",
                    GameMain.player1.getScore(), GameMain.player2.getScore()));
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText(String.format("'X' Won! Final Score: X: %d, O: %d. Click to play again.",
                    GameMain.player1.getScore(), GameMain.player2.getScore()));
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText(String.format("'O' Won! Final Score: X: %d, O: %d. Click to play again.",
                    GameMain.player1.getScore(), GameMain.player2.getScore()));
        }
    }

    /** The entry "main" method */
    public static void main(String[] args) {
        // Run GUI construction codes in Event-Dispatching thread for thread safety
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame(TITLE);
                // Set the content-pane of the JFrame to an instance of main JPanel
                frame.setContentPane(new GameMain());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null); // center the application window
                frame.setVisible(true);            // show it
            }
        });
    }
}
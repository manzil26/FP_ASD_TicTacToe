package TikTakTu;

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
    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Color COLOR_CROSS = new Color(239, 105, 80);  // Red #EF6950
    public static final Color COLOR_NOUGHT = new Color(64, 154, 225); // Blue #409AE1
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

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


    /** Constructor to setup the UI and game components */
    public GameMain() {

        // Initialize sound effects
        moveSound = new SoundEffect("TikTakTu/beep.wav");
        winSound = new SoundEffect("TikTakTu/mati.wav");
        drawSound = new SoundEffect("TikTakTu/meledak.wav");
        moveAISound = new SoundEffect("TikTakTu/cute.wav"); // File suara langkah AI




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
    public void paintComponent(Graphics g) {  // Callback via repaint()
        super.paintComponent(g);
        setBackground(COLOR_BG); // set its background color

        board.paint(g);  // ask the game board to paint itself

        // Print status-bar message
        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            statusBar.setText((currentPlayer == Seed.CROSS) ? "X's Turn" : "O's Turn");
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again.");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'X' Won! Click to play again.");
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'O' Won! Click to play again.");
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

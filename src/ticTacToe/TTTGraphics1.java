package ticTacToe;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

public class TTTGraphics1 extends JFrame {
    private static final long serialVersionUID = 1L;

    public static final int ROWS = 3;
    public static final int COLS = 3;
    public static final int CELL_SIZE = 120;
    public static final int BOARD_WIDTH = CELL_SIZE * COLS;
    public static final int BOARD_HEIGHT = CELL_SIZE * ROWS;
    public static final int GRID_WIDTH = 10;
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_CROSS = new Color(211, 45, 65);
    public static final Color COLOR_NOUGHT = new Color(76, 181, 245);
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    public enum State { PLAYING, DRAW, CROSS_WON, NOUGHT_WON }
    private State currentState;

    public enum Seed { CROSS, NOUGHT, NO_SEED }
    private Seed currentPlayer;
    private Seed[][] board;

    private GamePanel gamePanel;
    private JLabel statusBar;
    private SoundManager soundManager;

    public TTTGraphics1() {
        initGame();
        soundManager = new SoundManager();

        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                int row = mouseY / CELL_SIZE;
                int col = mouseX / CELL_SIZE;

                if (currentState == State.PLAYING) {
                    if (row >= 0 && row < ROWS && col >= 0 && col < COLS && board[row][col] == Seed.NO_SEED) {
                        currentState = stepGame(currentPlayer, row, col);
                        SoundManager.playSound("ticTacToe/move_sound.wav");  // Play move sound
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;

                        if (currentState == State.PLAYING && currentPlayer == Seed.NOUGHT) {
                            aiMove();  // AI makes its move after human
                        }
                    }
                } else {
                    newGame();
                }
                repaint();
            }
        });

        statusBar = new JLabel("       ");
        statusBar.setFont(FONT_STATUS);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));
        statusBar.setOpaque(true);
        statusBar.setBackground(Color.LIGHT_GRAY);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(gamePanel, BorderLayout.CENTER);
        cp.add(statusBar, BorderLayout.PAGE_END);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setTitle("Tic Tac Toe");
        setVisible(true);

        newGame();
    }

    public void initGame() {
        board = new Seed[ROWS][COLS];
    }

    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                board[row][col] = Seed.NO_SEED;
            }
        }
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
    }

    public State stepGame(Seed player, int selectedRow, int selectedCol) {
        board[selectedRow][selectedCol] = player;

        if (board[selectedRow][0] == player && board[selectedRow][1] == player && board[selectedRow][2] == player
                || board[0][selectedCol] == player && board[1][selectedCol] == player && board[2][selectedCol] == player
                || selectedRow == selectedCol && board[0][0] == player && board[1][1] == player && board[2][2] == player
                || selectedRow + selectedCol == 2 && board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            if (player == Seed.CROSS) {
                soundManager.playSound("ticTacToe/cross_win_sound.wav"); // Play cross win sound
                return State.CROSS_WON;
            } else {
                soundManager.playSound("ticTacToe/nought_win_sound.wav"); // Play nought win sound
                return State.NOUGHT_WON;
            }
        } else {
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    if (board[row][col] == Seed.NO_SEED) {
                        return State.PLAYING;
                    }
                }
            }
            soundManager.playSound("ticTacToe/draw_sound.wav");  // Play draw sound
            return State.DRAW;
        }
    }

    public void aiMove() {
        int[] bestMove = minimax(board, 0, true);
        int row = bestMove[0];
        int col = bestMove[1];
        currentState = stepGame(Seed.NOUGHT, row, col);
        currentPlayer = Seed.CROSS;  // Switch back to cross after AI move
        repaint();
    }

    public int[] minimax(Seed[][] board, int depth, boolean isMaximizing) {
        int[] bestMove = {-1, -1};
        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] == Seed.NO_SEED) {
                    board[row][col] = isMaximizing ? Seed.NOUGHT : Seed.CROSS;
                    int score = evaluate(board, depth);
                    board[row][col] = Seed.NO_SEED;

                    if (isMaximizing && score > bestScore) {
                        bestScore = score;
                        bestMove[0] = row;
                        bestMove[1] = col;
                    } else if (!isMaximizing && score < bestScore) {
                        bestScore = score;
                        bestMove[0] = row;
                        bestMove[1] = col;
                    }
                }
            }
        }
        return bestMove;
    }

    public int evaluate(Seed[][] board, int depth) {
        if (checkWin(board, Seed.NOUGHT)) {
            return 10 - depth;  // AI win
        } else if (checkWin(board, Seed.CROSS)) {
            return depth - 10;  // Player win
        } else {
            return 0;  // Draw
        }
    }

    public boolean checkWin(Seed[][] board, Seed player) {
        for (int row = 0; row < ROWS; row++) {
            if (board[row][0] == player && board[row][1] == player && board[row][2] == player) {
                return true;
            }
        }
        for (int col = 0; col < COLS; col++) {
            if (board[0][col] == player && board[1][col] == player && board[2][col] == player) {
                return true;
            }
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }
        return false;
    }

    class GamePanel extends JPanel {
        private static final long serialVersionUID = 1L;

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(COLOR_BG);

            g.setColor(Color.LIGHT_GRAY);
            for (int row = 1; row < ROWS; row++) {
                g.fillRoundRect(0, CELL_SIZE * row - GRID_WIDTH / 2, BOARD_WIDTH - 1, GRID_WIDTH, GRID_WIDTH, GRID_WIDTH);
            }
            for (int col = 1; col < COLS; col++) {
                g.fillRoundRect(CELL_SIZE * col - GRID_WIDTH / 2, 0, GRID_WIDTH, BOARD_HEIGHT - 1, GRID_WIDTH, GRID_WIDTH);
            }

            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (board[row][col] == Seed.CROSS) {
                        g.setColor(COLOR_CROSS);
                        g.drawLine(CELL_SIZE * col + 20, CELL_SIZE * row + 20, CELL_SIZE * (col + 1) - 20, CELL_SIZE * (row + 1) - 20);
                        g.drawLine(CELL_SIZE * col + 20, CELL_SIZE * (row + 1) - 20, CELL_SIZE * (col + 1) - 20, CELL_SIZE * row + 20);
                    } else if (board[row][col] == Seed.NOUGHT) {
                        g.setColor(COLOR_NOUGHT);
                        g.drawOval(CELL_SIZE * col + 20, CELL_SIZE * row + 20, CELL_SIZE - 40, CELL_SIZE - 40);
                    }
                }
            }

            String message = "Player " + (currentPlayer == Seed.CROSS ? "X" : "O") + "'s Turn";
            if (currentState == State.DRAW) {
                message = "Draw!";
            } else if (currentState == State.CROSS_WON) {
                message = "Player X Wins!";
            } else if (currentState == State.NOUGHT_WON) {
                message = "Player O Wins!";
            }
            statusBar.setText(message);
        }
    }

    public static void main(String[] args) {
        new TTTGraphics1();
    }
}

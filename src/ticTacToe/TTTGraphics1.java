/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #1
 * 1 - 5026231037 - Al-khiqmah Manzilatul Mukaromah
 * 2 - 5026231095 - Akhtar Zia Faizarrobbi
 * 3 - 5026231227 - Arjuna Veetaraq
 */

package ticTacToe;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TTTGraphics1 extends JFrame {
    private static final long serialVersionUID = 1L;

    public static final int CELL_SIZE = 120;
    public static final int BOARD_WIDTH = CELL_SIZE * Board.COLS;
    public static final int BOARD_HEIGHT = CELL_SIZE * Board.ROWS;
    public static final int GRID_WIDTH = 10;
    // Tema Anak-Anak
    // Tema Lavender Anak-Anak
    // Tema Lavender Anak-Anak
    public static final Color COLOR_BG = new Color(255, 204, 153); // Latar belakang jingga lembut seperti pasir terkena sinar senja
    public static final Color COLOR_CROSS = new Color(255, 94, 77); // Warna salib merah jingga seperti matahari tenggelam
    public static final Color COLOR_NOUGHT = new Color(255, 165, 0); // Warna lingkaran jingga keemasan (Orange)
    public static final Font FONT_STATUS = new Font("Comic Sans MS", Font.BOLD, 16); // Font menyenangkan dan kasual


    private State currentState;
    private Seed currentPlayer;
    private Board board;

    private GamePanel gamePanel;
    private JLabel statusBar;
    private JLabel scoreBoard;
    private SoundManager soundManager;

    private boolean isGameComplete;
    private int playerXWins = 0;
    private int playerOWins = 0;
    private int draws = 0;

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
                    if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS &&
                            board.cells[row][col].content == Seed.NO_SEED) {
                        currentState = stepGame(currentPlayer, row, col);
                        SoundManager.playSound("ticTacToe/move_sound.wav");
                        currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;

                        if (currentState == State.PLAYING && currentPlayer == Seed.NOUGHT) {
                            aiMove();
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

        scoreBoard = new JLabel(getScoreText());
        scoreBoard.setFont(FONT_STATUS);
        scoreBoard.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));
        scoreBoard.setOpaque(true);
        scoreBoard.setBackground(Color.LIGHT_GRAY);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(gamePanel, BorderLayout.CENTER);
        cp.add(statusBar, BorderLayout.PAGE_END);
        cp.add(scoreBoard, BorderLayout.PAGE_START);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setTitle("Tic Tac Toe");
        setVisible(true);

        newGame();
    }

    public void initGame() {
        board = new Board();
        board.initGame();
    }

    public void newGame() {
        board.newGame();
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
        isGameComplete = false;
        updateScoreBoard();
    }

    public State stepGame(Seed player, int selectedRow, int selectedCol) {
        State result = board.stepGame(player, selectedRow, selectedCol);
        if (result == State.CROSS_WON) {
            soundManager.playSound("ticTacToe/cross_win_sound.wav");
            playerXWins++;
        } else if (result == State.NOUGHT_WON) {
            soundManager.playSound("ticTacToe/nought_win_sound.wav");
            playerOWins++;
        } else if (result == State.DRAW) {
            soundManager.playSound("ticTacToe/draw_sound.wav");
            draws++;
        }
        updateScoreBoard();
        return result;
    }

    public void aiMove() {
        int[] bestMove = minimax(0, true);
        int row = bestMove[0];
        int col = bestMove[1];
        currentState = stepGame(Seed.NOUGHT, row, col);
        currentPlayer = Seed.CROSS;
        repaint();
    }

    public int[] minimax(int depth, boolean isMaximizing) {
        int[] bestMove = {-1, -1};
        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLS; col++) {
                if (board.cells[row][col].content == Seed.NO_SEED) {
                    board.cells[row][col].content = isMaximizing ? Seed.NOUGHT : Seed.CROSS;
                    int score = evaluate(depth);
                    board.cells[row][col].content = Seed.NO_SEED;

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

    public int evaluate(int depth) {
        if (checkWin(Seed.NOUGHT)) {
            return 10 - depth;
        } else if (checkWin(Seed.CROSS)) {
            return depth - 10;
        }
        return 0;
    }

    public boolean checkWin(Seed player) {
        for (int row = 0; row < Board.ROWS; row++) {
            if (board.cells[row][0].content == player &&
                    board.cells[row][1].content == player &&
                    board.cells[row][2].content == player) {
                return true;
            }
        }
        for (int col = 0; col < Board.COLS; col++) {
            if (board.cells[0][col].content == player &&
                    board.cells[1][col].content == player &&
                    board.cells[2][col].content == player) {
                return true;
            }
        }
        if (board.cells[0][0].content == player &&
                board.cells[1][1].content == player &&
                board.cells[2][2].content == player) {
            return true;
        }
        if (board.cells[0][2].content == player &&
                board.cells[1][1].content == player &&
                board.cells[2][0].content == player) {
            return true;
        }
        return false;
    }

    private String getScoreText() {
        return "X Wins: " + playerXWins + " | O Wins: " + playerOWins + " | Draws: " + draws;
    }

    private void updateScoreBoard() {
        scoreBoard.setText(getScoreText());
    }

    class GamePanel extends JPanel {
        private static final long serialVersionUID = 1L;

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(COLOR_BG);

            g.setColor(Color.LIGHT_GRAY);
            for (int row = 1; row < Board.ROWS; row++) {
                g.fillRoundRect(0, CELL_SIZE * row - GRID_WIDTH / 2,
                        BOARD_WIDTH - 1, GRID_WIDTH, GRID_WIDTH, GRID_WIDTH);
            }
            for (int col = 1; col < Board.COLS; col++) {
                g.fillRoundRect(CELL_SIZE * col - GRID_WIDTH / 2, 0,
                        GRID_WIDTH, BOARD_HEIGHT - 1, GRID_WIDTH, GRID_WIDTH);
            }

            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            for (int row = 0; row < Board.ROWS; row++) {
                for (int col = 0; col < Board.COLS; col++) {
                    if (board.cells[row][col].content == Seed.CROSS) {
                        g.setColor(COLOR_CROSS);
                        g.drawLine(CELL_SIZE * col + 20, CELL_SIZE * row + 20,
                                CELL_SIZE * (col + 1) - 20, CELL_SIZE * (row + 1) - 20);
                        g.drawLine(CELL_SIZE * col + 20, CELL_SIZE * (row + 1) - 20,
                                CELL_SIZE * (col + 1) - 20, CELL_SIZE * row + 20);
                    } else if (board.cells[row][col].content == Seed.NOUGHT) {
                        g.setColor(COLOR_NOUGHT);
                        g.drawOval(CELL_SIZE * col + 20, CELL_SIZE * row + 20,
                                CELL_SIZE - 40, CELL_SIZE - 40);
                    }
                }
            }

            String message;
            if (currentState == State.DRAW) {
                message = "Draw! Click to start new game";
            } else if (currentState == State.CROSS_WON) {
                message = "Player X Wins! Click to start new game";
            } else if (currentState == State.NOUGHT_WON) {
                message = "Player O Wins! Click to start new game";
            } else if (!canContinueGame()) {
                message = "Game incomplete - no valid moves left";
            } else {
                message = "Player " + (currentPlayer == Seed.CROSS ? "X" : "O") + "'s Turn";
            }
            statusBar.setText(message);
        }
    }

    public static void main(String[] args) {
        new TTTGraphics1();
    }

    private boolean canContinueGame() {
        if (isGameComplete) return false;

        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLS; col++) {
                if (board.cells[row][col].content == Seed.NO_SEED) {
                    return true;
                }
            }
        }
        return false;
    }
}

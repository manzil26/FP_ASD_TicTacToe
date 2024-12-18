package connectFour;

import java.util.ArrayList;
import java.util.List;

public class Connect4 {
    public static final int ROWS = 6;
    public static final int COLS = 7;
    public static final int AI_PLAYER = 1;  // Representation of AI
    public static final int HUMAN_PLAYER = 2;  // Representation of Human Player
    public static final int EMPTY = 0;
    public static final int MAX_DEPTH = 5;  // AI search depth

    private int[][] board;
    private Player humanPlayer;
    private Player aiPlayer;
    private Board gameBoard;

    // Constructor
    public Connect4(String humanName, String aiName, Board board) {
        this.board = new int[ROWS][COLS]; // Initialize the game board
        this.gameBoard = board;          // Shared Board instance
        this.humanPlayer = new Player(humanName);
        this.aiPlayer = new Player(aiName);
    }

    // Method to play the game
    public void playGame() {
        boolean isAITurn = false;

        while (true) {
            printBoard();

            // Check for game over conditions
            if (isWinningMove(board, HUMAN_PLAYER)) {
                System.out.println(humanPlayer.getName() + " wins!");
                humanPlayer.addScore(1);
                break;
            } else if (isWinningMove(board, AI_PLAYER)) {
                System.out.println(aiPlayer.getName() + " wins!");
                aiPlayer.addScore(1);
                break;
            } else if (isDraw(board)) {
                System.out.println("It's a draw!");
                break;
            }

            // Player moves
            if (isAITurn) {
                int bestMove = getBestMove();
                makeMove(board, bestMove, AI_PLAYER);
                System.out.println(aiPlayer.getName() + " chooses column: " + bestMove);
            } else {
                int col;
                do {
                    System.out.print(humanPlayer.getName() + ", enter your move (0-6): ");
                    col = new java.util.Scanner(System.in).nextInt();
                } while (!isValidMove(col));
                makeMove(board, col, HUMAN_PLAYER);
            }

            isAITurn = !isAITurn; // Toggle turn
        }

        // Display the scoreboard
        System.out.println("Scoreboard:");
        System.out.println(humanPlayer.getName() + ": " + humanPlayer.getScore());
        System.out.println(aiPlayer.getName() + ": " + aiPlayer.getScore());
    }

    // AI Logic: Get the best move for the AI
    public int getBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int col : validMoves(board)) {
            int[][] newBoard = simulateMove(board, col, AI_PLAYER);
            int score = minimax(newBoard, MAX_DEPTH, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (score > bestScore) {
                bestScore = score;
                bestMove = col;
            }
        }
        return bestMove;
    }

    // Minimax algorithm with alpha-beta pruning
    private int minimax(int[][] board, int depth, boolean isMaximizingPlayer, int alpha, int beta) {
        if (isWinningMove(board, AI_PLAYER)) return 1000;
        if (isWinningMove(board, HUMAN_PLAYER)) return -1000;
        if (isDraw(board) || depth == 0) return evaluateBoard(board);

        if (isMaximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int col : validMoves(board)) {
                int[][] newBoard = simulateMove(board, col, AI_PLAYER);
                int eval = minimax(newBoard, depth - 1, false, alpha, beta);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col : validMoves(board)) {
                int[][] newBoard = simulateMove(board, col, HUMAN_PLAYER);
                int eval = minimax(newBoard, depth - 1, true, alpha, beta);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }

    // Evaluate the board
    private int evaluateBoard(int[][] board) {
        int score = 0;
        // Add heuristics here (e.g., patterns, central positions).
        return score;
    }

    // Check for a winning move
    private boolean isWinningMove(int[][] board, int player) {
        // Check horizontal, vertical, and diagonal wins
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                if (board[row][col] == player &&
                        board[row][col + 1] == player &&
                        board[row][col + 2] == player &&
                        board[row][col + 3] == player) {
                    return true;
                }
            }
        }

        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] == player &&
                        board[row + 1][col] == player &&
                        board[row + 2][col] == player &&
                        board[row + 3][col] == player) {
                    return true;
                }
            }
        }

        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                if (board[row][col] == player &&
                        board[row + 1][col + 1] == player &&
                        board[row + 2][col + 2] == player &&
                        board[row + 3][col + 3] == player) {
                    return true;
                }
            }
        }

        for (int row = 3; row < ROWS; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                if (board[row][col] == player &&
                        board[row - 1][col + 1] == player &&
                        board[row - 2][col + 2] == player &&
                        board[row - 3][col + 3] == player) {
                    return true;
                }
            }
        }

        return false;
    }

    // Check for a draw
    private boolean isDraw(int[][] board) {
        for (int col = 0; col < COLS; col++) {
            if (board[0][col] == EMPTY) {
                return false;
            }
        }
        return true;
    }

    // Get valid moves
    private List<Integer> validMoves(int[][] board) {
        List<Integer> moves = new ArrayList<>();
        for (int col = 0; col < COLS; col++) {
            if (board[0][col] == EMPTY) {
                moves.add(col);
            }
        }
        return moves;
    }

    // Simulate a move
    private int[][] simulateMove(int[][] board, int col, int player) {
        int[][] newBoard = new int[ROWS][COLS];
        for (int row = 0; row < ROWS; row++) {
            System.arraycopy(board[row], 0, newBoard[row], 0, COLS);
        }
        for (int row = ROWS - 1; row >= 0; row--) {
            if (newBoard[row][col] == EMPTY) {
                newBoard[row][col] = player;
                break;
            }
        }
        return newBoard;
    }

    // Make a move on the board
    private void makeMove(int[][] board, int col, int player) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col] == EMPTY) {
                board[row][col] = player;
                break;
            }
        }
    }

    // Check if the move is valid
    private boolean isValidMove(int col) {
        return col >= 0 && col < COLS && board[0][col] == EMPTY;
    }

    // Print the board
    private void printBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                System.out.print((board[row][col] == EMPTY ? "." : board[row][col] == HUMAN_PLAYER ? "O" : "X") + " ");
            }
            System.out.println();
        }
        System.out.println("0 1 2 3 4 5 6");
    }
}
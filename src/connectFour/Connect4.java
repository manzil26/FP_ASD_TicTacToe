package connectFour;

import java.util.ArrayList;
import java.util.List;

public class Connect4 {
    public static final int ROWS = 6;
    public static final int COLS = 7;
    public static final int AI_PLAYER = 1;  // Representasi AI
    public static final int HUMAN_PLAYER = 2;  // Representasi pemain manusia
    public static final int EMPTY = 0;
    public static final int MAX_DEPTH = 5;  // Kedalaman pencarian AI

    private int[][] board;

    public Connect4() {
        board = new int[ROWS][COLS];
    }

    public void playGame() {
        boolean isAITurn = false;
        while (true) {
            printBoard();
            if (isWinningMove(board, HUMAN_PLAYER)) {
                System.out.println("Human wins!");
                break;
            } else if (isWinningMove(board, AI_PLAYER)) {
                System.out.println("AI wins!");
                break;
            } else if (isDraw(board)) {
                System.out.println("It's a draw!");
                break;
            }

            if (isAITurn) {
                int bestMove = getBestMove();
                makeMove(board, bestMove, AI_PLAYER);
                System.out.println("AI chooses column: " + bestMove);
            } else {
                int col;
                do {
                    System.out.print("Enter your move (0-6): ");
                    col = new java.util.Scanner(System.in).nextInt();
                } while (!isValidMove(col));
                makeMove(board, col, HUMAN_PLAYER);
            }
            isAITurn = !isAITurn;
        }
    }

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

    private int evaluateBoard(int[][] board) {
        int score = 0;
        // Tambahkan heuristik evaluasi posisi di sini (contoh: pola 2, 3, atau posisi tengah).
        return score;
    }

    private boolean isWinningMove(int[][] board, int player) {
        // Periksa kemenangan horizontal, vertikal, dan diagonal
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

    private boolean isDraw(int[][] board) {
        for (int col = 0; col < COLS; col++) {
            if (board[0][col] == EMPTY) {
                return false;
            }
        }
        return true;
    }

    private List<Integer> validMoves(int[][] board) {
        List<Integer> moves = new ArrayList<>();
        for (int col = 0; col < COLS; col++) {
            if (board[0][col] == EMPTY) {
                moves.add(col);
            }
        }
        return moves;
    }

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

    private void makeMove(int[][] board, int col, int player) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col] == EMPTY) {
                board[row][col] = player;
                break;
            }
        }
    }

    private boolean isValidMove(int col) {
        return col >= 0 && col < COLS && board[0][col] == EMPTY;
    }

    private void printBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                System.out.print((board[row][col] == EMPTY ? "." : board[row][col] == HUMAN_PLAYER ? "O" : "X") + " ");
            }
            System.out.println();
        }
        System.out.println("0 1 2 3 4 5 6");
    }

    public static void main(String[] args) {
        Connect4 game = new Connect4();
        game.playGame();
    }
}


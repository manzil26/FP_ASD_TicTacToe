package connectFour;

import java.util.*;

public class AIPlayerMinimax extends AIPlayer {

    public AIPlayerMinimax(Board board) {
        super(board);
    }

    @Override
    int[] move() {
        int[] result = minimax(4, mySeed, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return new int[] {result[1], result[2]}; // row, col
    }

    private int[] minimax(int depth, Seed player, int alpha, int beta) {
        List<int[]> nextMoves = generateMoves();

        int bestScore = (player == mySeed) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        int bestRow = -1;
        int bestCol = -1;

        if (nextMoves.isEmpty() || depth == 0) {
            bestScore = evaluate();
        } else {
            for (int[] move : nextMoves) {
                cells[move[0]][move[1]].content = player;
                if (player == mySeed) {
                    currentScore = minimax(depth - 1, oppSeed, alpha, beta)[0];
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                    alpha = Math.max(alpha, bestScore);
                } else {
                    currentScore = minimax(depth - 1, mySeed, alpha, beta)[0];
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                    beta = Math.min(beta, bestScore);
                }
                cells[move[0]][move[1]].content = Seed.NO_SEED;

                if (alpha >= beta) break;
            }
        }
        return new int[] {bestScore, bestRow, bestCol};
    }

    private List<int[]> generateMoves() {
        List<int[]> nextMoves = new ArrayList<>();
        if (hasWon(mySeed) || hasWon(oppSeed)) {
            return nextMoves;
        }

        for (int col = 0; col < COLS; ++col) {
            int row = getFirstAvailableRow(col);
            if (row != -1) {
                nextMoves.add(new int[] {row, col});
            }
        }
        return nextMoves;
    }
    public int getFirstAvailableRow(int col) {
        for (int row = ROWS - 1; row >= 0; row--) {  // Cari dari baris bawah ke atas
            if (cells[row][col].content == Seed.NO_SEED) {
                return row;
            }
        }
        return -1;  // Kolom penuh
    }



    private int evaluate() {
        int score = 0;
        score += evaluateLine(0, 0, 0, 1, 0, 2, 0, 3);  // row 1
        score += evaluateLine(1, 0, 1, 1, 1, 2, 1, 3);  // row 2
        // Add similar calls for columns and diagonals...
        return score;
    }

    private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3, int row4, int col4) {
        int score = 0;
        // Evaluasi untuk setiap cell dalam baris, kolom, atau diagonal
        // Sesuaikan dengan koneksi 4 berturut-turut.
        return score;
    }

    private boolean hasWon(Seed thePlayer) {
        int pattern = 0b000000000;  // 9-bit pattern
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == thePlayer) {
                    pattern |= (1 << (row * COLS + col));
                }
            }
        }
        int[] winningPatterns = {
                0b11110000000000000000000000000000,  // Horizontal
                0b100010001000100010001000,  // Vertical
                0b100100100100100,          // Diagonal 1
                0b001010100010010           // Diagonal 2
        };
        for (int winningPattern : winningPatterns) {
            if ((pattern & winningPattern) == winningPattern) return true;
        }
        return false;
    }
}
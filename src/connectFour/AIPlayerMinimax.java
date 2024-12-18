package connectFour;

import java.util.*;

public class AIPlayerMinimax extends AIPlayer {
    private Board board;          // The game board
    private Seed mySeed;          // The AI's seed
    private Seed oppSeed;         // The opponent's seed
    private int maxDepth = 6;     // Depth limit for Minimax
    public AIPlayerMinimax(Board board) {
        super(board);
    }

    public void setSeed(Seed seed) {
        mySeed = seed;
        oppSeed = (seed == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
    }

    @Override
    public int[] move() {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;

        for (int col = 0; col < Board.COLS; col++) {
            for (int row = Board.ROWS - 1; row >= 0; row--) {
                if (board.cells[row][col].content == Seed.NO_SEED) {
                    // Try move
                    board.cells[row][col].content = mySeed;
                    int score = minimax(0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    // Undo move
                    board.cells[row][col].content = Seed.NO_SEED;

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new int[]{row, col};
                    }
                    break; // Only consider the lowest empty cell in a column
                }
            }
        }
        return bestMove;
    }

    private int minimax(int depth, boolean isMaximizing, int alpha, int beta) {
        State currentState = board.stepGame(mySeed);
        if (currentState == State.CROSS_WON || currentState == State.NOUGHT_WON || currentState == State.DRAW) {
            return evaluateState(currentState, depth);
        }
        if (depth >= maxDepth) {
            return evaluateBoard();
        }

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int col = 0; col < Board.COLS; col++) {
            for (int row = Board.ROWS - 1; row >= 0; row--) {
                if (board.cells[row][col].content == Seed.NO_SEED) {
                    board.cells[row][col].content = isMaximizing ? mySeed : oppSeed;
                    int score = minimax(depth + 1, !isMaximizing, alpha, beta);
                    board.cells[row][col].content = Seed.NO_SEED;

                    if (isMaximizing) {
                        bestScore = Math.max(bestScore, score);
                        alpha = Math.max(alpha, bestScore);
                    } else {
                        bestScore = Math.min(bestScore, score);
                        beta = Math.min(beta, bestScore);
                    }

                    if (beta <= alpha) break; // Prune
                    break; // Only check the lowest empty cell in a column
                }
            }
        }
        return bestScore;
    }

    /**
     * Evaluate the game state for a terminal node.
     */
    private int evaluateState(State state, int depth) {
        if (state == State.CROSS_WON) {
            return (mySeed == Seed.CROSS ? 1000 : -1000) - depth;
        } else if (state == State.NOUGHT_WON) {
            return (mySeed == Seed.NOUGHT ? 1000 : -1000) - depth;
        } else {
            return 0; // Draw
        }
    }

    /**
     * Evaluate the board using a heuristic function.
     */
    private int evaluateBoard() {
        int score = 0;

        // Evaluate all lines
        score += evaluateLines(mySeed);
        score -= evaluateLines(oppSeed);

        return score;
    }

    /**
     * Evaluate all possible lines (horizontal, vertical, diagonal).
     */
    private int evaluateLines(Seed seed) {
        int score = 0;

        // Evaluate horizontal
        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col <= Board.COLS - 4; col++) {
                score += evaluateSegment(seed, row, col, 0, 1); // Check 4 horizontally
            }
        }

        // Evaluate vertical
        for (int col = 0; col < Board.COLS; col++) {
            for (int row = 0; row <= Board.ROWS - 4; row++) {
                score += evaluateSegment(seed, row, col, 1, 0); // Check 4 vertically
            }
        }

        // Evaluate diagonals (top-left to bottom-right)
        for (int row = 0; row <= Board.ROWS - 4; row++) {
            for (int col = 0; col <= Board.COLS - 4; col++) {
                score += evaluateSegment(seed, row, col, 1, 1); // Check diagonal
            }
        }

        // Evaluate diagonals (bottom-left to top-right)
        for (int row = 3; row < Board.ROWS; row++) {
            for (int col = 0; col <= Board.COLS - 4; col++) {
                score += evaluateSegment(seed, row, col, -1, 1); // Check anti-diagonal
            }
        }

        return score;
    }

    /**
     * Evaluate a segment of 4 cells in a specific direction.
     */
    private int evaluateSegment(Seed seed, int row, int col, int rowDelta, int colDelta) {
        int countSeed = 0, countEmpty = 0;

        for (int i = 0; i < 4; i++) {
            Seed content = board.cells[row + i * rowDelta][col + i * colDelta].content;
            if (content == seed) countSeed++;
            else if (content == Seed.NO_SEED) countEmpty++;
        }

        if (countSeed == 4) return 1000; // Winning line
        if (countSeed == 3 && countEmpty == 1) return 50; // Strong position
        if (countSeed == 2 && countEmpty == 2) return 10; // Decent position
        return 0; // Neutral
    }
}
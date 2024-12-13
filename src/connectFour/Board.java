package connectFour;
import java.awt.*;
/**
 * The Board class models the ROWS-by-COLS game board.
 */
public class Board {
    // Define named constants
    public static final int ROWS = 6;   // 6 baris untuk Connect4
    public static final int COLS = 7;   // 7 kolom untuk Connect4
    public Cell[][] cells;             // Papan permainan

    // Define named constants for drawing
    public static final int CANVAS_WIDTH = Cell.SIZE * COLS;  // the drawing canvas
    public static final int CANVAS_HEIGHT = Cell.SIZE * ROWS;
    public static final int GRID_WIDTH = 8;  // Grid-line's width
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2; // Grid-line's half-width
    public static final Color COLOR_GRID = Color.LIGHT_GRAY;  // grid lines
    public static final int Y_OFFSET = 1;  // Fine tune for better display

    // Define properties (package-visible)
    /** Composes of 2D array of ROWS-by-COLS Cell instances */


    /** Constructor to initialize the game board */
    public Board() {
        initGame();
    }

    /** Initialize the game objects (run once) */
    public void initGame() {
        cells = new Cell[ROWS][COLS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }
    }
    public int getFirstAvailableRow(int col) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (cells[row][col].content == Seed.NO_SEED) {
                return row;
            }
        }
        return -1;  // Kolom penuh
    }

    /** Reset the game board, ready for new game */
    public void newGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].newGame(); // clear the cell content
            }
        }
    }

    /**
     *  The given player makes a move on (selectedRow, selectedCol).
     *  Update cells[selectedRow][selectedCol]. Compute and return the
     *  new game state (PLAYING, DRAW, CROSS_WON, NOUGHT_WON).
     */
    public State stepGame(Seed player, int selectedRow, int selectedCol) {
        cells[selectedRow][selectedCol].content = player;  // Place the player's seed

        // Check if this move caused the player to win
        if (hasWon(player, selectedRow, selectedCol)) {
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }

        // Check for draw (if no empty cells)
        boolean draw = true;
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    draw = false;
                    break;
                }
            }
        }
        if (draw) {
            return State.DRAW;  // No more moves, it's a draw
        }

        // Otherwise, game is still playing
        return State.PLAYING;
    }


    private boolean hasWon(Seed player, int row, int col) {
        return (checkLine(player, row, col, 0, 1) ||   // Horizontal
                checkLine(player, row, col, 1, 0) ||   // Vertical
                checkLine(player, row, col, 1, 1) ||   // Diagonal (top-left to bottom-right)
                checkLine(player, row, col, 1, -1));   // Anti-diagonal (bottom-left to top-right)
    }


    private boolean checkLine(Seed player, int row, int col, int deltaRow, int deltaCol) {
        int count = 1;  // Count the current seed itself

        // Check in the forward direction
        count += countConsecutive(player, row, col, deltaRow, deltaCol);

        // Check in the backward direction
        count += countConsecutive(player, row, col, -deltaRow, -deltaCol);

        // Check if there are 4 or more in total
        return count >= 4;
    }

    private int countConsecutive(Seed player, int row, int col, int deltaRow, int deltaCol) {
        int count = 0;
        int currentRow = row + deltaRow;
        int currentCol = col + deltaCol;

        while (currentRow >= 0 && currentRow < Board.ROWS &&
                currentCol >= 0 && currentCol < Board.COLS &&
                cells[currentRow][currentCol].content == player) {
            count++;
            currentRow += deltaRow;
            currentCol += deltaCol;
        }
        return count;
    }




    /** Paint itself on the graphics canvas, given the Graphics context */
    public void paint(Graphics g) {
        // Draw the grid-lines
        g.setColor(COLOR_GRID);
        for (int row = 1; row < ROWS; ++row) {
            g.fillRoundRect(0, Cell.SIZE * row - GRID_WIDTH_HALF,
                    CANVAS_WIDTH - 1, GRID_WIDTH,
                    GRID_WIDTH, GRID_WIDTH);
        }
        for (int col = 1; col < COLS; ++col) {
            g.fillRoundRect(Cell.SIZE * col - GRID_WIDTH_HALF, 0 + Y_OFFSET,
                    GRID_WIDTH, CANVAS_HEIGHT - 1,
                    GRID_WIDTH, GRID_WIDTH);
        }

        // Draw all the cells
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g);  // ask the cell to paint itself
            }
        }
    }
}
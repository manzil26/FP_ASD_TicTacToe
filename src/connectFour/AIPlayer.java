package connectFour;



public abstract class AIPlayer {
    protected int ROWS = Board.ROWS;
    protected int COLS = Board.COLS;
    protected Cell[][] cells;
    protected Seed mySeed;
    protected Seed oppSeed;

    public AIPlayer(Board board) {
        cells = board.cells;
    }

    public void setSeed(Seed seed) {
        this.mySeed = seed;
        oppSeed = (mySeed == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
    }

    abstract int[] move();  // To be implemented by subclasses
}


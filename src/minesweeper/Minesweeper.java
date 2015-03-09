package minesweeper;

import java.util.Random;

public class Minesweeper {

    public enum Cell {UNEXPOSED, EXPOSED, SEALED};
    public static final int SIZE = 10;
    private Cell[][] board = new Cell[SIZE][SIZE];

    protected boolean[][] mined = new boolean[SIZE][SIZE];

    public enum Game {PROGRESS, WON, LOST};

    Minesweeper() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = Cell.UNEXPOSED;
                mined[i][j] = false;
            }
        }
    }
    
    public static Minesweeper create() {
        Minesweeper minesweeper = new Minesweeper();
        minesweeper.placeMinesAtRandom();
        return minesweeper;
    }

    public void exposeCell(int row, int column) {
        if (board[row][column] == Cell.UNEXPOSED) {
            board[row][column] = Cell.EXPOSED;
            
            if ((!mined[row][column]) && (countAdjacentMines(row, column) == 0)) {
                for (int i = row - 1; i <= row + 1; i++) {
                    for (int j = column - 1; j <= column + 1; j++) {
                        if (i != row || j != column) {
                            exposeNeighbor(i, j);
                        }
                    }
                }
            }
        }
    }

    public void exposeNeighbor(int row, int column) {
        if ((row < SIZE && row >= 0) && (column < SIZE && column >= 0))
            exposeCell(row, column);
    }

    public void sealCell(int row, int column) {
        if (board[row][column] == Cell.UNEXPOSED)
            board[row][column] = Cell.SEALED;
    }

    public void unsealCell(int row, int column) {
        if (board[row][column] == Cell.SEALED)
            board[row][column] = Cell.UNEXPOSED;
    }

    public Cell cellStatus(int row, int column) {
        return board[row][column];
    }

    public boolean isMined(int row, int column) {
        if((row < SIZE && row >= 0) && (column < SIZE && column >= 0))
            return mined[row][column];
        
        return false;
    }

    public boolean isAdjacent(int row, int column) {
        return ((!mined[row][column]) && (countAdjacentMines(row, column) > 0));
    }

    public int countAdjacentMines(int row, int column) {
        int numberOfAdjacentMines = 0;
        
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = column - 1; j <= column + 1; j++) {
                if ((i != row || j != column) && (i < SIZE && i >= 0) && (j < SIZE && j >= 0) && (mined[i][j] == true))
                    numberOfAdjacentMines++;
            }
        }

        return numberOfAdjacentMines;
    }

    public Game gameState() {
        Game state = Game.WON;
        
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (mined[i][j] && (board[i][j] == Cell.EXPOSED)) {
                    return Game.LOST;
                } else if (board[i][j] == Cell.UNEXPOSED) {
                    state = Game.PROGRESS;
                }
            }
        }

        return state;
    }
    
    public void placeMinesAtRandom() {
        Random random = new Random();
        int row, column;
        int i = 0;
        
        while (i < SIZE) {
            row = random.nextInt(SIZE);
            column = random.nextInt(SIZE);
            
            if (!mined[row][column]) {
                mined[row][column] = true;
                i++;
            }
        }
    } 
}
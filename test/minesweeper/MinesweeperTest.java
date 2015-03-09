package minesweeper;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import minesweeper.Minesweeper.Cell;
import minesweeper.Minesweeper.Game;
import java.util.*;

public class MinesweeperTest {

    Minesweeper minesweeperToDesignExpose, minesweeperToDesignExposeNeighbor, minesweeper;
    List<Integer> neighboringCells;
    boolean exposeCalled;

    @Before
    public void setUp() {
        neighboringCells = new ArrayList<>();

        minesweeperToDesignExpose = new Minesweeper() {
            @Override
            public void exposeNeighbor(int row, int column) {
                neighboringCells.add(row);
                neighboringCells.add(column);
            }
        };

        minesweeperToDesignExposeNeighbor = new Minesweeper() {
            @Override
            public void exposeCell(int row, int column) {
                exposeCalled = true;
            }
        };

        minesweeper = new Minesweeper();
    }

    @Test
    public void canary() {
        assertTrue(true);
    }

    @Test
    public void exposeUnexposedCell() {
        minesweeperToDesignExpose.exposeCell(5, 2);

        assertEquals(Cell.EXPOSED, minesweeperToDesignExpose.cellStatus(5, 2));
    }

    @Test
    public void exposeCellThatAlreadyIsExposed() {
        minesweeperToDesignExpose.exposeCell(5, 2);
        minesweeperToDesignExpose.exposeCell(5, 2);

        assertEquals(Cell.EXPOSED, minesweeperToDesignExpose.cellStatus(5, 2));
    }

    private void checkBounds(Runnable block) {
        try {
            block.run();
            fail("Expected exception for out of range");
        } catch (IndexOutOfBoundsException ex) {
            assertTrue(true);
        }
    }
    
    @Test
    public void exposeCellWhereRowIsAboveRowRange() {
        checkBounds( () -> minesweeperToDesignExpose.exposeCell(10, 0));
    }

    @Test
    public void exposeCellWhereRowIsBelowRowRange() {
        checkBounds( () -> minesweeperToDesignExpose.exposeCell(-1, 0));
    }

    @Test
    public void exposeCellWhereColumnIsAboveColumnRange() {
        checkBounds( () -> minesweeperToDesignExpose.exposeCell(0, 10));
    }

    @Test
    public void exposeCellWhereColumnIsBelowColumnRange() {
        checkBounds( () -> minesweeperToDesignExpose.exposeCell(0, -1));
    }

    @Test
    public void whenExposeCellIsCalledExposeNeighborIsNotCalledAtAllForAnAlreadyExposedCell() {
        minesweeperToDesignExpose.exposeCell(1, 2);

        neighboringCells.clear();
        List<Integer> empty = new ArrayList<>();

        minesweeperToDesignExpose.exposeCell(1, 2);
        assertEquals(empty, neighboringCells);
    }

    @Test
    public void whenACellIsExposedItTriesToExposeItsNeighbors() {
        minesweeperToDesignExpose.exposeCell(1, 2);

        List<Integer> expected = Arrays.asList(0, 1, 0, 2, 0, 3, 1, 1, 1, 3, 2, 1, 2, 2, 2, 3);
        assertEquals(expected, neighboringCells);
    }

    @Test
    public void exposeNeighborWontCallExposeCellWhenRowIsAboveRowRange() {
        minesweeperToDesignExposeNeighbor.exposeNeighbor(10, 0);

        assertFalse(exposeCalled);
    }

    @Test
    public void exposeNeighborWontCallExposeCellWhenRowIsBelowRowRange() {
        minesweeperToDesignExposeNeighbor.exposeNeighbor(-1, 0);

        assertFalse(exposeCalled);
    }

    @Test
    public void exposeNeighborWontCallExposeCellWhenColumnIsAboveColumnRange() {
        minesweeperToDesignExposeNeighbor.exposeNeighbor(0, 10);

        assertFalse(exposeCalled);
    }

    @Test
    public void exposeNeighborWontCallExposeCellWhenColumnIsBelowColumnRange() {
        minesweeperToDesignExposeNeighbor.exposeNeighbor(0, -1);

        assertFalse(exposeCalled);
    }

    @Test
    public void exposeNeighborCallsExposeForValidCell() {
        minesweeperToDesignExposeNeighbor.exposeNeighbor(1, 4);

        assertTrue(exposeCalled);
    }

    @Test
    public void sealUnexposedCell() {
        minesweeper.sealCell(5, 2);

        assertEquals(Cell.SEALED, minesweeper.cellStatus(5, 2));
    }

    @Test
    public void sealCellWontSealAnExposedCell() {
        minesweeper.exposeCell(5, 2);
        minesweeper.sealCell(5, 2);

        assertEquals(Cell.EXPOSED, minesweeper.cellStatus(5, 2));
    }

    @Test
    public void unsealASealedCell() {
        minesweeper.sealCell(3, 3);
        minesweeper.unsealCell(3, 3);

        assertEquals(Cell.UNEXPOSED, minesweeper.cellStatus(3, 3));
    }

    @Test
    public void exposeCellWontExposeASealedCell() {
        minesweeperToDesignExpose.sealCell(8, 2);

        minesweeperToDesignExpose.exposeCell(8, 2);

        assertEquals(Cell.SEALED, minesweeperToDesignExpose.cellStatus(8, 2));
    }

    @Test
    public void exposeCellWontCallExposeNeighborForASealedCell() {
        minesweeperToDesignExpose.sealCell(1, 2);
        minesweeperToDesignExpose.exposeCell(1, 2);

        List<Integer> empty = new ArrayList<>();
        assertEquals(empty, neighboringCells);
    }

    @Test
    public void sealCellWhereRowIsAboveRowRange() {
        checkBounds( () -> minesweeper.sealCell(10, 0));
    }

    @Test
    public void sealCellWhereRowIsBelowRowRange() {
        checkBounds( () -> minesweeper.sealCell(-1, 0));
    }

    @Test
    public void sealCellWhereColumnIsAboveColumnRange() {
        checkBounds( () -> minesweeper.sealCell(0, 10));
    }

    @Test
    public void sealCellWhereColumnIsBelowColumnRange() {
        checkBounds( () -> minesweeper.sealCell(0, -1));
    }

    @Test
    public void unsealCellWhereRowIsAboveRowRange() {
        checkBounds( () -> minesweeper.unsealCell(10, 0));
    }

    @Test
    public void unsealCellWhereRowIsBelowRowRange() {
        checkBounds( () -> minesweeper.unsealCell(-1, 0));
    }

    @Test
    public void unsealCellWhereColumnIsAboveColumnRange() {
        checkBounds( () -> minesweeper.unsealCell(0, 10));
    }

    @Test
    public void unsealCellWhereColumnIsBelowColumnRange() {
        checkBounds( () -> minesweeper.unsealCell(0, -1));
    }

    @Test
    public void whenAnAdjacentCellIsExposedExposeNeighborIsNotCalled() {
        minesweeperToDesignExpose.mined[1][2] = true;
        minesweeperToDesignExpose.exposeCell(1, 1);
        
        List<Integer> empty = new ArrayList<>();
        assertEquals(empty, neighboringCells);
    }

    @Test
    public void whenAMinedCellIsExposedExposeNeighborIsNotCalled() {
        minesweeperToDesignExpose.mined[1][2] = true;
        minesweeperToDesignExpose.exposeCell(1, 2);
        
        List<Integer> empty = new ArrayList<>();
        assertEquals(empty, neighboringCells);
    }
    
    @Test
    public void isAnEmptyCell() {
        minesweeper.mined[3][4] = false;
        assertFalse(minesweeper.isMined(3, 4));
    }
    
    @Test
    public void isAnAdjacentCell() {
        minesweeper.mined[2][3] = true;
        minesweeper.mined[3][5] = true;
        assertTrue(minesweeper.isAdjacent(3, 4));
    }
    
    @Test
    public void isAMinedCell() {
        minesweeper.mined[3][4] = true;
        assertTrue(minesweeper.isMined(3, 4));
    }
    
    @Test
    public void countAdjacentMines() {
        minesweeper.mined[2][3] = true;
        minesweeper.mined[3][1] = true;
        minesweeper.mined[4][3] = true;

        assertEquals(3, minesweeper.countAdjacentMines(3, 2));
    }
    
    @Test
    public void countAdjacentMinesWhenOnlyMineIsAtSameLocation() {
        minesweeper.mined[3][2] = true;
        assertEquals(0, minesweeper.countAdjacentMines(3, 2));
    }
    
    @Test
    public void isMinedWhereRowIsAboveRowRange() {
        assertFalse(minesweeper.isMined(10, 0));
    }

    @Test
    public void isMinedWhereRowIsBelowRowRange() {
        assertFalse(minesweeper.isMined(-1, 0));
    }

    @Test
    public void isMinedWhereColumnIsAboveColumnRange() {
        assertFalse(minesweeper.isMined(0, 10));
    }

    @Test
    public void inMinedWhereColumnIsBelowColumnRange() {
        assertFalse(minesweeper.isMined(0, -1));
    }

    @Test
    public void gameInProgress() {
        assertEquals(Game.PROGRESS, minesweeper.gameState());
    }

    @Test
    public void gameNotLostIfMinedCellIsSealed() {
        minesweeper.mined[5][4] = true;
        minesweeper.sealCell(5, 4);

        assertEquals(Game.PROGRESS, minesweeper.gameState());
    }

    @Test
    public void gameLostWhenMinedCellIsExposed() {
        minesweeper.mined[5][4] = true;
        minesweeper.exposeCell(5, 4);
        
        assertEquals(Game.LOST, minesweeper.gameState());
    }

    @Test
    public void gameWonIfAllMinedCellsAreSealedRemainingAreExposed() {
        for (int j = 0; j < 10; j++) {
            minesweeper.mined[0][j] = true;
            minesweeper.sealCell(0, j);
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                minesweeper.exposeCell(i, j);
            }
        }

        assertEquals(Game.WON, minesweeper.gameState());
    }

    @Test
    public void boardHasTenMines() {
        minesweeper.placeMinesAtRandom();

        int minesCount = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (minesweeper.isMined(i, j)) {
                    minesCount++;
                }
            }
        }

        assertEquals(10, minesCount);
    }

    @Test
    public void minesPlacedAtRandomLocations() {
        minesweeper.placeMinesAtRandom();

        Minesweeper minesweeper2 = Minesweeper.create();

        assertNotSame(minesweeper, minesweeper2);
    }
}

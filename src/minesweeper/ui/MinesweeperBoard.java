package minesweeper.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import minesweeper.Minesweeper;
import minesweeper.Minesweeper.Cell;
import minesweeper.Minesweeper.Game;
import static minesweeper.Minesweeper.SIZE;

public class MinesweeperBoard extends JFrame {
    
    private Minesweeper minesweeper;
    private static MinesweeperCell[][] cells;
    
    java.net.URL mineURL = MinesweeperBoard.class.getResource("mine.png");
    java.net.URL sealURL = MinesweeperBoard.class.getResource("seal.png");
    
    ImageIcon mine = new ImageIcon(mineURL);
    ImageIcon seal = new ImageIcon(sealURL);
    
    @Override
    protected void frameInit() {
        super.frameInit();
        
        minesweeper = Minesweeper.create();
        cells = new MinesweeperCell[SIZE][SIZE];
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(SIZE, SIZE));
        setTitle("Minesweeper");
        
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cells[i][j] = new MinesweeperCell(i, j);
                getContentPane().add(cells[i][j]);
                cells[i][j].addMouseListener(new CellClickedListener());
            }
        }
    }
    
    public static MinesweeperBoard createBoardFrame() {
        MinesweeperBoard board = new MinesweeperBoard();
        
        board.setSize(500, 500);
        board.setResizable(false);
        board.setVisible(true);
        
        return board;
    }
    
    public static void main(String[] args) {
        JFrame board = MinesweeperBoard.createBoardFrame();
    }
    
    private class CellClickedListener extends MouseAdapter {
        
        @Override
        public void mouseClicked(MouseEvent e) {
            
            MinesweeperCell cell = (MinesweeperCell)e.getSource();
            Cell clicked = minesweeper.cellStatus(cell.row, cell.column);
            
            if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
                if (clicked == Cell.UNEXPOSED) {
                    minesweeper.exposeCell(cell.row, cell.column);
                    displayBoard();
                }
            }
            
            if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
                if (clicked == Cell.UNEXPOSED) {
                    minesweeper.sealCell(cell.row, cell.column);
                    cell.setIcon(seal);
                }
                
                if (clicked == Cell.SEALED) {
                    minesweeper.unsealCell(cell.row, cell.column);
                    cell.setIcon(null);
                }
            }
            
            reportGameState(cell);
        }
        
        public void displayBoard() {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (minesweeper.cellStatus(i, j) == Cell.EXPOSED) {
                        cells[i][j].setEnabled(false);
                        
                        if (minesweeper.isAdjacent(i, j)) {
                            int adjacentMines = minesweeper.countAdjacentMines(i, j);
                            String adjMines = Integer.toString(adjacentMines);
                            cells[i][j].setText(adjMines);
                        } else if (minesweeper.isMined(i, j)) {
                            cells[i][j].setIcon(mine);
                        }
                    }
                }
            }
        }
        
        public void reportGameState(MinesweeperCell theCell) {
            Game state = minesweeper.gameState();
            
            if (state == Game.LOST) {
                JOptionPane.showMessageDialog(theCell, "YOU " + state.toString());
                gameOver();
            }else if (state == Game.WON) {
                JOptionPane.showMessageDialog(theCell, "YOU " + state.toString());
            }
        }
        
        public void gameOver() {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    cells[i][j].setEnabled(false);
                    cells[i][j].setIcon(null);
                    
                    if (minesweeper.isMined(i, j)) {
                        cells[i][j].setIcon(mine);
                    } else if (minesweeper.isAdjacent(i, j)) {
                        int adjacentMines = minesweeper.countAdjacentMines(i, j);
                        String adjMines = Integer.toString(adjacentMines);
                        cells[i][j].setText(adjMines);
                    }
                }
            }
        }
    }
}

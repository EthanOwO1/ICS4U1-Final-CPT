import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * Visual grid on game board<p>
 * Draws 10x10 grid lines<p>
 * Purely visual, no logic
 * @author Ethan, Carsten, Brandon
 * @version 1.0
 */
public class BattleGrid extends JPanel {
    // Properties
    
    /** Cell size */
    int cellSize;
    /** Row count */
    int intRows = 10;
    /** Column count */
    int intCols = 10;

    /**
     * Constructs BattleGrid panel
     * @param x Top-left X
     * @param y Top-left Y
     * @param cellSize Cell size in pixels
     */
    public BattleGrid(int x, int y, int cellSize) {
        this.cellSize = cellSize;
        this.setBounds(x, y, intCols * cellSize + 1, intRows * cellSize + 1);
        this.setOpaque(false);
    }

    /**
     * Draws grid lines
     * @param g Graphics object
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        // Loop through rows and columns to draw grid cells
        for (int i = 0; i < intRows; i++) {
            for (int j = 0; j < intCols; j++) {
                g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }
    }

    /**
     * Main method for testing
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        JFrame theFrame = new JFrame("BattleGrid Test");
        JPanel thePanel = new JPanel();

        thePanel.setLayout(null);
        thePanel.setPreferredSize(new Dimension(1280, 720));
        theFrame.setContentPane(thePanel);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create two grids and add it to the screen.
        BattleGrid grid1 = new BattleGrid(50, 150, 50);
        thePanel.add(grid1);

        BattleGrid grid2 = new BattleGrid(550, 150, 50);
        thePanel.add(grid2);
        theFrame.pack();
        theFrame.setResizable(false);
        theFrame.setVisible(true);
    }
}

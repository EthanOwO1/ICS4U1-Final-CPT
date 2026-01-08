import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class BattleGrid extends JPanel {
    int cellSize;
    int rows = 8;
    int cols = 8;

    public BattleGrid(int x, int y, int cellSize) {
        this.cellSize = cellSize;
        this.setBounds(x, y, cols * cellSize + 1, rows * cellSize + 1);
        this.setOpaque(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }
    }

    // Main method to run this grid "all on its own"
    public static void main(String[] args) {
        JFrame frame = new JFrame("BattleGrid Standalone Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setSize(1050, 600);

        // Create a sample grid and add it to the frame
        BattleGrid testGrid1 = new BattleGrid(50, 50, 50);
        frame.add(testGrid1);

        BattleGrid testGrid2 = new BattleGrid(550, 50, 50);
        frame.add(testGrid2);

        frame.setVisible(true);
    }
}

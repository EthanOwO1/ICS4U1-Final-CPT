import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class BattleGrid extends JPanel {
    // Properties
    
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

    // Inner class for the PopUp Window
    static class PopUpWindow extends JFrame {
        public PopUpWindow() {
            super("Pop Out Panel");
            this.setSize(400, 300);
            this.setLayout(null);
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setVisible(true);
        }
    }

    // Main method to run this grid "all on its own"
    public static void main(String[] args) {
        JFrame frame = new JFrame("BattleGrid Test");
        JPanel thePanel = new JPanel();
        JLabel horizontalCoordinate = new JLabel("A    B    C    D    E    F    G    H");
        horizontalCoordinate.setFont(new Font("SansSerif", Font.BOLD, 25));

        thePanel.setPreferredSize(new Dimension(1280, 720));
        frame.setContentPane(thePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        thePanel.setLayout(null);
        
        horizontalCoordinate.setSize(800, 60);
        horizontalCoordinate.setLocation(70, 100);

        thePanel.add(horizontalCoordinate);

        // Create a sample grid and add it to the frame
        BattleGrid testGrid1 = new BattleGrid(50, 150, 50);
        thePanel.add(testGrid1);

        BattleGrid testGrid2 = new BattleGrid(550, 150, 50);
        thePanel.add(testGrid2);

        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        new PopUpWindow();
    }
}

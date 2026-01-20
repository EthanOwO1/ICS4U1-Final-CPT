import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class helpPanel extends JPanel {

    JButton butReturn;

    public helpPanel() {

        setLayout(null);

        JLabel title = new JLabel("How to Play Battleship");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setBounds(450, 50, 500, 40);
        add(title);

        JTextArea instructions = new JTextArea(
            "Objective:\n" +
            "- Sink all enemy ships before they sink yours.\n\n" +
            "How to Play:\n" +
            "- Click on the enemy grid to fire shots.\n" +
            "- Red = hit, Gray = miss.\n" +
            "- Enemy will fire back after your turn.\n\n" +
            "Good luck, Commander!"
        );

        instructions.setFont(new Font("Arial", Font.PLAIN, 18));
        instructions.setBounds(300, 150, 700, 300);
        instructions.setEditable(false);
        instructions.setOpaque(false);
        add(instructions);

        butReturn = new JButton("Back to Menu");
        butReturn.setBounds(550, 650, 180, 40);
        add(butReturn);

        ShipPlacementDemo demo = new ShipPlacementDemo();
        demo.setBounds(350, 350, 600, 250);
        add(demo);

        // Close the help screen and go back to main menu
        butReturn.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new JPanel()); // temporary reset
            frame.dispose();
            new mainProgram(); // reload menu
        });
    }
}

class ShipPlacementDemo extends JPanel
        implements MouseListener, MouseMotionListener, KeyListener {

    int shipX = 50;
    int shipY = 100;
    int offsetX, offsetY;
    boolean dragging = false;
    double rotation = 0;

    final int gridX = 250;
    final int gridY = 20;
    final int cellSize = 40;
    final int gridSize = 5;

    BufferedImage shipImage;

    public ShipPlacementDemo() {
        setLayout(null);
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);

        // Simple placeholder ship
        shipImage = new BufferedImage(120, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = shipImage.createGraphics();
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, 120, 40);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, 119, 39);
        g.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw grid
        g.setColor(Color.BLACK);
        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {
                g.drawRect(gridX + c * cellSize, gridY + r * cellSize, cellSize, cellSize);
            }
        }

        // Draw ship
        Graphics2D g2 = (Graphics2D) g;
        g2.rotate(rotation, shipX + shipImage.getWidth()/2, shipY + shipImage.getHeight()/2);
        g2.drawImage(shipImage, shipX, shipY, null);
        g2.rotate(-rotation, shipX + shipImage.getWidth()/2, shipY + shipImage.getHeight()/2);

        g.setColor(Color.BLACK);
        g.drawString("Drag the ship • Press R to rotate", 10, 20);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        requestFocusInWindow();

        boolean isVertical = Math.round(rotation / (Math.PI / 2)) % 2 != 0;
        int adjX = isVertical ? 40 : 0;
        int adjY = isVertical ? -40 : 0;
        int vW = isVertical ? 40 : 120;
        int vH = isVertical ? 120 : 40;

        if (e.getX() >= shipX + adjX && e.getX() <= shipX + adjX + vW
         && e.getY() >= shipY + adjY && e.getY() <= shipY + adjY + vH) {

            dragging = true;
            offsetX = e.getX() - shipX;
            offsetY = e.getY() - shipY;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!dragging) return;

        int rawX = e.getX() - offsetX;
        int rawY = e.getY() - offsetY;

        boolean isVertical = Math.round(rotation / (Math.PI / 2)) % 2 != 0;
        int adjX = isVertical ? 40 : 0;
        int adjY = isVertical ? -40 : 0;
        int vW = isVertical ? 40 : 120;
        int vH = isVertical ? 120 : 40;

        int currentVisualX = rawX + adjX;
        int currentVisualY = rawY + adjY;

        // Snap to grid
        if (currentVisualX >= gridX - 10 && currentVisualX + vW <= gridX + gridSize * cellSize + 10 &&
            currentVisualY >= gridY - 10 && currentVisualY + vH <= gridY + gridSize * cellSize + 10) {

            shipX = gridX + Math.round((currentVisualX - gridX) / (float)cellSize) * cellSize - adjX;
            shipY = gridY + Math.round((currentVisualY - gridY) / (float)cellSize) * cellSize - adjY;
        } else {
            shipX = rawX;
            shipY = rawY;
        }

        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'r') {
            rotation += Math.PI / 2;
            repaint();
        }
    }

    // Unused but required
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
}
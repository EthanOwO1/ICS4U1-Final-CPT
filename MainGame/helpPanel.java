import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Help screen panel<p>
 * Displays instructions and includes interactive ship demo
 * @author Ethan, Carsten, Brandon
 * @version 1.0
 */
public class helpPanel extends JPanel implements ActionListener {
    // Properties
    JButton returnButton;
    mainProgram main;

    // Methods
    public void actionPerformed(ActionEvent evt){
        main.theFrame.setContentPane(main.mainMenuPanel);
        main.theFrame.pack();
        main.theFrame.repaint();
    }

    // Constructor
    public helpPanel(mainProgram main) {

        setLayout(null);

        // Title Label
        JLabel helpLabel = new JLabel("How to Play Battleship");
        helpLabel.setFont(new Font("Arial", Font.BOLD, 32));
        helpLabel.setBounds(450, 50, 500, 40);
        add(helpLabel);

        // Instructions
        JTextArea instructionsArea = new JTextArea(
            "Objective:\n" +
            "- Sink all enemy ships before they sink yours.\n\n" +
            "How to Play:\n" +
            "- Click on the enemy grid to fire shots.\n" +
            "- Red = hit, Gray = miss.\n" +
            "- Enemy will fire back after your turn.\n\n" +
            "Good luck, Commander!"
        );

        instructionsArea.setFont(new Font("Arial", Font.PLAIN, 18));
        instructionsArea.setBounds(300, 150, 700, 300);
        instructionsArea.setEditable(false);
        instructionsArea.setOpaque(false);
        add(instructionsArea);

        returnButton = new JButton("Back to Menu");
        returnButton.setBounds(550, 600, 180, 40);
        add(returnButton);

        // Add the interactive ship demo component
        ShipPlacementDemo demo = new ShipPlacementDemo();
        demo.setBounds(350, 350, 600, 250);
        add(demo);

        // Close the help screen and go back to main menu
        returnButton.addActionListener(this);
    }
}

/**
 * Inner class for the interactive ship demo<p>
 * Allows users to practice dragging and rotating a ship
 */
class ShipPlacementDemo extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

    // Properties
    int shipX = 50;
    int shipY = 100;
    int offsetX, offsetY;
    boolean blnDrag = false;
    double dblRot = 0;

    int gridX = 250;
    int gridY = 20;
    int cellSize = 40;
    int gridSize = 5;

    BufferedImage shipImage;

    // Methods
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw grid
        g.setColor(Color.BLACK);
        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {
                g.drawRect(gridX + c * cellSize, gridY + r * cellSize, cellSize, cellSize);
            }
        }

        // Draw the ship with rotation
        Graphics2D g2 = (Graphics2D) g;
        g2.rotate(dblRot, shipX + shipImage.getWidth()/2, shipY + shipImage.getHeight()/2);
        g2.drawImage(shipImage, shipX, shipY, null);
        g2.rotate(-dblRot, shipX + shipImage.getWidth()/2, shipY + shipImage.getHeight()/2);

        g.setColor(Color.BLACK);
        g.drawString("Drag the ship • Press R to rotate", 10, 20);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        requestFocusInWindow();

        // Calculate hit box based on rotation (swap width/height if ship is vertical)
        boolean isVertical = Math.round(dblRot / (Math.PI / 2)) % 2 != 0;
        int adjX = isVertical ? 40 : 0;
        int adjY = isVertical ? -40 : 0;
        int vW = isVertical ? 40 : 120;
        int vH = isVertical ? 120 : 40;

        if (e.getX() >= shipX + adjX && e.getX() <= shipX + adjX + vW
         && e.getY() >= shipY + adjY && e.getY() <= shipY + adjY + vH) {

            blnDrag = true;
            offsetX = e.getX() - shipX;
            offsetY = e.getY() - shipY;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!blnDrag){
            return;
        }

        int rawX = e.getX() - offsetX;
        int rawY = e.getY() - offsetY;

        boolean isVertical = Math.round(dblRot / (Math.PI / 2)) % 2 != 0;
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
        blnDrag = false;
    }

    // Detect 'R' key press to rotate ship
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'r') {
            dblRot += Math.PI / 2;
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

    // Constructor
    public ShipPlacementDemo() {
        setLayout(null);
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);

        // Create a simple ship image (gray rectangle) for drag and drop
        shipImage = new BufferedImage(120, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = shipImage.createGraphics();
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, 120, 40);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, 119, 39);
        g.dispose();
    }

}
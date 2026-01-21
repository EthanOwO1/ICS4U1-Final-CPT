//Battleship Game
//version 1.0
//January 21 2026

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * <h1>Battleship Main Program</h1>
 * Central controller for the game<p>
 * Handles game loop, input, and state<p>
 * Coordinates network, graphics, and logic
 * @author Ethan, Carsten, Brandon
 * @version 1.0
 */
public class mainProgram implements ActionListener, MouseListener, MouseMotionListener, KeyListener{
  
  // Properties 
  /** The main application window */
  JFrame theFrame = new JFrame("Battleship"); 
  /** The container for the main menu components */
  JPanel mainMenuPanel = new JPanel();
  /** The label holding the background image for the menu */
  JLabel menuLabel = new JLabel(); 
  /** The custom panel handling game animations and logic */
  animation thePanel = new animation();
  /** The game loop timer running at 60 FPS */
  Timer theTimer = new Timer(1000/60, this);

  /** The menu bar for the application */
  JMenuBar theMenuBar = new JMenuBar();
  /** The main menu dropdown */
  JMenu theMenu = new JMenu("Menu");
  /** Menu item for instructions */
  JMenuItem instructions = new JMenuItem("Instructions");
  /** Menu item for about info */
  JMenuItem about = new JMenuItem("About");

  /** Button to start the game setup */
  JButton playButton;
  /** Button to open the help screen */
  JButton helpButton;
  /** Button to exit the application */
  JButton exitButton;
  /** Button to confirm ship placement */
  JButton confirmButton;

  /** ID of the ship currently being dragged */
  int intDrag = 0;
  /** X offset for dragging calculation */
  int intOffsetX = 0;
  /** Y offset for dragging calculation */
  int intOffsetY = 0;
  /** Original X position of a ship before dragging */
  int intOriginalX = 0;
  /** Original Y position of a ship before dragging */
  int intOriginalY = 0;
  /** Flag indicating if ships are locked in place */
  boolean shipsLocked = false;
  
  // Grids
  /** Visual grid for the player */
  BattleGrid playerGrid;
  /** Visual grid for the enemy */
  BattleGrid enemyGrid;

  // Enemy Ship Data
  /** Array storing X coordinates of enemy ships */
  int[] enemyShipX = new int[5];
  /** Array storing Y coordinates of enemy ships */
  int[] enemyShipY = new int[5];
  /** Array storing rotation of enemy ships */
  double[] enemyShipRot = new double[5];
  /** Flag indicating if enemy ship data has been received */
  boolean enemyShipsReceived = false;
  
  /** Flag indicating if the battle phase has started */
  boolean battleOn = false;
  /** Counter for hits on the player */
  int playerHits = 0;
  /** Counter for hits on the enemy */
  int enemyHits = 0;
  /** Flag indicating if it is the player's turn */
  boolean myTurn = false;
  /** Label displaying whose turn it is */
  JLabel turnLabel = new JLabel("Waiting...");

   // Button panels
  /** Reference to the help screen panel */
  helpPanel helpScreen;
  /** Reference to the connection/chat panel */
  chatPanel connectScreen;
  /** Container for the about screen */
  JPanel aboutPanel;
  /** Button to return from about screen */
  JButton aboutBackButton;

  // Images
  /** Background image for the main menu */
  BufferedImage menuImage; 

  // Methods
  /**
   * Handles Swing and Timer events<p>
   * Updates animations, menu, and ship placement
   * @param evt ActionEvent
   */
  public void actionPerformed(ActionEvent evt){
    if(evt.getSource() == theTimer){
      // Updates animations at 60 FPS
      thePanel.updateAnimations();
      thePanel.repaint();
    }
    if(evt.getSource() == playButton){
        theFrame.setContentPane(connectScreen);
        theFrame.pack();
        theFrame.repaint();
    }

    if(evt.getSource() == helpButton || evt.getSource() == instructions){
        theFrame.setContentPane(helpScreen);
        theFrame.pack();
        theFrame.repaint();
    }

    if(evt.getSource() == exitButton){
        System.exit(0);
    }

    if(evt.getSource() == about){
        theFrame.setContentPane(aboutPanel);
        theFrame.pack();
        theFrame.repaint();
    }

    if(evt.getSource() == aboutBackButton){
        theFrame.setContentPane(mainMenuPanel);
        theFrame.pack();
        theFrame.repaint();
    }

    if(evt.getSource() == confirmButton){
      // Check if all ships are moved from their starting positions
      if(thePanel.int1Ship3X == 150 && thePanel.int1Ship3Y == 550 || 
         thePanel.int2Ship3X == 290 && thePanel.int2Ship3Y == 550 || 
         thePanel.intShip2X == 430 && thePanel.intShip2Y == 550 || 
         thePanel.intShip4X == 150 && thePanel.intShip4Y == 600 || 
         thePanel.intShip5X == 330 && thePanel.intShip5Y == 600){
        System.out.println("Please place all ships on the grid.");
      }else{
        System.out.println("Ships Confirmed");
        shipsLocked = true;
        confirmButton.setEnabled(false);
        
        // Send Ship Coordinates to Opponent via network
        if(connectScreen.ssm != null){
          String msg = "SHIPS";
          msg += "," + thePanel.int1Ship3X + "," + thePanel.int1Ship3Y + "," + thePanel.dbl1Ship3rot;
          msg += "," + thePanel.int2Ship3X + "," + thePanel.int2Ship3Y + "," + thePanel.dbl2Ship3rot;
          msg += "," + thePanel.intShip2X + "," + thePanel.intShip2Y + "," + thePanel.dblShip2rot;
          msg += "," + thePanel.intShip4X + "," + thePanel.intShip4Y + "," + thePanel.dblShip4rot;
          msg += "," + thePanel.intShip5X + "," + thePanel.intShip5Y + "," + thePanel.dblShip5rot;
          connectScreen.ssm.sendText(msg);
        }
        writePlayerShipsToFile();
        checkBattleStart();
      }
    }
  }

  /**
   * Starts main gameplay phase<p>
   * Switches to game panel
   */
  public void startGame(){
    theFrame.setContentPane(thePanel);
    theFrame.pack();
    theFrame.repaint();
  }

  /**
   * Parses enemy ship data<p>
   * Updates local data and writes to file
   * @param strLine Ship data string
   */
  public void setEnemyShips(String strLine){
    String[] parts = strLine.split(",");
    // Format: SHIPS, x1, y1, rot1, x2, y2, rot2...
    
    // Ship 1
    enemyShipX[0] = Integer.parseInt(parts[1]);
    enemyShipY[0] = Integer.parseInt(parts[2]);
    enemyShipRot[0] = Double.parseDouble(parts[3]);
    // Ship 2
    enemyShipX[1] = Integer.parseInt(parts[4]);
    enemyShipY[1] = Integer.parseInt(parts[5]);
    enemyShipRot[1] = Double.parseDouble(parts[6]);
    // Ship 3
    enemyShipX[2] = Integer.parseInt(parts[7]);
    enemyShipY[2] = Integer.parseInt(parts[8]);
    enemyShipRot[2] = Double.parseDouble(parts[9]);
    // Ship 4
    enemyShipX[3] = Integer.parseInt(parts[10]);
    enemyShipY[3] = Integer.parseInt(parts[11]);
    enemyShipRot[3] = Double.parseDouble(parts[12]);
    // Ship 5
    enemyShipX[4] = Integer.parseInt(parts[13]);
    enemyShipY[4] = Integer.parseInt(parts[14]);
    enemyShipRot[4] = Double.parseDouble(parts[15]);
    
    System.out.println("Enemy ships received.");
    
    writeEnemyShipsToFile();
    enemyShipsReceived = true;
    checkBattleStart();
  }

  /**
   * Checks if battle can start<p>
   * Requires locked ships and enemy data
   */
  public void checkBattleStart(){
    if(shipsLocked && enemyShipsReceived){
      battleOn = true;
      System.out.println("SYSTEM: Both players ready. Battle Start!");
      connectScreen.theArea.append("SYSTEM: Battle Started! Good luck.\n");
      if(myTurn){
          turnLabel.setText("Your Turn");
      }else{
          turnLabel.setText("Opponent's Turn");
      }
    }
  }
  
  /**
   * Writes enemy ships to file
   */
  public void writeEnemyShipsToFile() {
    try (PrintWriter writer = new PrintWriter(new FileWriter(connectScreen.strName + "_enemy_ships.txt"))) {
        // Format: x, y, rotation, length
        writer.println(enemyShipX[0] + "," + enemyShipY[0] + "," + enemyShipRot[0] + "," + 3);
        writer.println(enemyShipX[1] + "," + enemyShipY[1] + "," + enemyShipRot[1] + "," + 3);
        writer.println(enemyShipX[2] + "," + enemyShipY[2] + "," + enemyShipRot[2] + "," + 2);
        writer.println(enemyShipX[3] + "," + enemyShipY[3] + "," + enemyShipRot[3] + "," + 4);
        writer.println(enemyShipX[4] + "," + enemyShipY[4] + "," + enemyShipRot[4] + "," + 5);
    } catch (IOException e) {
        System.out.println("Error writing enemy ships to file.");
    }
  }

  /**
   * Writes player ships to file
   */
  public void writePlayerShipsToFile() {
    try (PrintWriter writer = new PrintWriter(new FileWriter(connectScreen.strName + "_own_ships.txt"))) {
        // Format: x, y, rotation, length
        writer.println(thePanel.int1Ship3X + "," + thePanel.int1Ship3Y + "," + thePanel.dbl1Ship3rot + "," + 3);
        writer.println(thePanel.int2Ship3X + "," + thePanel.int2Ship3Y + "," + thePanel.dbl2Ship3rot + "," + 3);
        writer.println(thePanel.intShip2X + "," + thePanel.intShip2Y + "," + thePanel.dblShip2rot + "," + 2);
        writer.println(thePanel.intShip4X + "," + thePanel.intShip4Y + "," + thePanel.dblShip4rot + "," + 4);
        writer.println(thePanel.intShip5X + "," + thePanel.intShip5Y + "," + thePanel.dblShip5rot + "," + 5);
    } catch (IOException e) {
        System.out.println("Error writing player ships to file.");
    }
  }

  /**
   * Checks for hit from file data
   * @param filename Ship data file
   * @param targetCol Target column
   * @param targetRow Target row
   * @return True if hit
   */
  private boolean checkHitFromFile(String filename, int targetCol, int targetRow) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        String strLine;
        while ((strLine = reader.readLine()) != null) {
            String[] parts = strLine.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            double rot = Double.parseDouble(parts[2]);
            int length = Integer.parseInt(parts[3]);

            // Adjust coordinates based on player (Server vs Client offset) 
            int offset = 0;
            if(connectScreen.strName.equals("Player1")){
                offset = 8;
            }

            // Convert this ship's pixel data into a list of occupied grid cells
            int startCol = (int)Math.round((x + offset - 141) / 40.0);
            int startRow = (int)Math.round((y + offset - 125) / 40.0);
            boolean isVertical = Math.round(rot / (Math.PI / 2)) % 2 != 0;

            // Check every cell occupied by this ship
            for (int i = 0; i < length; i++) {
                int occupiedCol = startCol;
                int occupiedRow = startRow;
                if (isVertical) {
                    occupiedRow += i;
                } else {
                    occupiedCol += i;
                }

                if (occupiedCol == targetCol && occupiedRow == targetRow) {
                    return true; // It's a hit!
                }
            }
        }
    } catch (IOException e) {
        System.out.println("Could not read " + filename + ". Assuming miss.");
        return false;
    }
    return false; // No hit found after checking all ships
  }

  /**
   * Processes opponent's shot<p>
   * Updates grid and checks game over
   * @param msg Shot coordinates message
   */
  public void receiveShot(String msg){
      // Format: SHOT,col,row
      String[] parts = msg.split(",");
      int col = Integer.parseInt(parts[1]);
      int row = Integer.parseInt(parts[2]);
      
      // Ignore if already hit/missed
      if(thePanel.playerGridState[row][col] != 0) return;
      
      boolean hit = checkMyCollision(col, row);
      thePanel.playerGridState[row][col] = hit ? 1 : 2;
      
      // Log the result to chat
      String strCoord = (char)('A' + row) + "" + (col + 1);
      String strResult = hit ? "hit" : "miss";
      String strShooter = connectScreen.strName.equals("Player1") ? "Player2" : "Player1";
      connectScreen.theArea.append("System: " + strShooter + ", " + strCoord + ", " + strResult + "\n");
      connectScreen.theArea.setCaretPosition(connectScreen.theArea.getDocument().getLength());

      if(hit){
          playerHits++;
          if(playerHits == 17){
              gameOver("You Lose!");
          }
      }
      
      thePanel.repaint();
      myTurn = true;
      if(battleOn) {
        turnLabel.setText("Your Turn");
      }
  }
  
  /**
   * Checks if shot hits player ships
   * @param col Column index
   * @param row Row index
   * @return True if hit
   */
  public boolean checkMyCollision(int col, int row){
    return checkHitFromFile(connectScreen.strName + "_own_ships.txt", col, row);
  }

  /**
   * Checks for ship overlap during drag
   * @param currentDragId ID of dragged ship
   * @param x X coordinate
   * @param y Y coordinate
   * @param w Width
   * @param h Height
   * @return True if overlapping
   */
  public boolean checkOverlap(int currentDragId, int x, int y, int w, int h) {
    // Check against Ship 1 (ID 1)
    if (currentDragId != 1) {
      if (isOverlapping(x, y, w, h, thePanel.int1Ship3X, thePanel.int1Ship3Y, thePanel.dbl1Ship3rot, thePanel.oneShip3Image)) {
        return true;
      }
    }

    // Check against Ship 2 (ID 2)
    if (currentDragId != 2) {
      if (isOverlapping(x, y, w, h, thePanel.int2Ship3X, thePanel.int2Ship3Y, thePanel.dbl2Ship3rot, thePanel.twoShip3Image)){
        return true;
      }
    }
    // Check against Ship 3 (ID 3)
    if (currentDragId != 3) {
      if (isOverlapping(x, y, w, h, thePanel.intShip2X, thePanel.intShip2Y, thePanel.dblShip2rot, thePanel.ship2Image)){
        return true;
      }
    }
    // Check against Ship 4 (ID 4)
    if (currentDragId != 4) {
      if (isOverlapping(x, y, w, h, thePanel.intShip4X, thePanel.intShip4Y, thePanel.dblShip4rot, thePanel.ship4Image)){
        return true;
      }
    }
    // Check against Ship 5 (ID 5)
    if (currentDragId != 5) {
      if (isOverlapping(x, y, w, h, thePanel.intShip5X, thePanel.intShip5Y, thePanel.dblShip5rot, thePanel.ship5Image)){
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if two rectangles overlap
   * @param x1 Rect 1 X
   * @param y1 Rect 1 Y
   * @param w1 Rect 1 Width
   * @param h1 Rect 1 Height
   * @param x2 Rect 2 X
   * @param y2 Rect 2 Y
   * @param rot2 Rect 2 Rotation
   * @param img2 Rect 2 Image
   * @return True if overlapping
   */
  public boolean isOverlapping(int x1, int y1, int w1, int h1, int x2, int y2, double rot2, BufferedImage img2) {
      if (img2 == null) return false;
      boolean isVertical = Math.round(rot2 / (Math.PI / 2)) % 2 != 0;
      int w2 = isVertical ? img2.getHeight() : img2.getWidth();
      int h2 = isVertical ? img2.getWidth() : img2.getHeight();
      
      // Standard rectangle intersection with a margin to allow placing ships next to each other
      int margin = 10;
      return x1 + margin < x2 + w2 - margin && x1 + w1 - margin > x2 + margin && y1 + margin < y2 + h2 - margin && y1 + h1 - margin > y2 + margin;
  }

  /**
   * Handles ship dragging<p>
   * Implements grid snapping
   * @param evt MouseEvent
   */
  @Override
  public void mouseDragged(MouseEvent evt) {
    if(shipsLocked){
      return;
    }
    thePanel.repaint(); 

    int gridX = 141;
    int gridY = 125;
    int cellSize = 40;
    int gridSize = 400;

    // Logic for dragging Ship 1 (3-cell)
    if(intDrag == 1){
      int rawX = evt.getX() - intOffsetX;
      int rawY = evt.getY() - intOffsetY;
      if(rawX > gridX - 50 && rawX < gridX + gridSize + 50 && rawY > gridY - 50 && rawY < gridY + gridSize + 50){
        thePanel.int1Ship3X = gridX + (int)(Math.round((double)(rawX - gridX) / cellSize) * cellSize) - 4;
        thePanel.int1Ship3Y = gridY + (int)(Math.round((double)(rawY - gridY) / cellSize) * cellSize) - 4;
      }else{
        thePanel.int1Ship3X = rawX;
        thePanel.int1Ship3Y = rawY;
      }
    // Logic for dragging Ship 2 (3-cell)
    }else if(intDrag == 2){
      int rawX = evt.getX() - intOffsetX;
      int rawY = evt.getY() - intOffsetY;
      if(rawX > gridX - 50 && rawX < gridX + gridSize + 50 && rawY > gridY - 50 && rawY < gridY + gridSize + 50){
        thePanel.int2Ship3X = gridX + (int)(Math.round((double)(rawX - gridX) / cellSize) * cellSize) - 4;
        thePanel.int2Ship3Y = gridY + (int)(Math.round((double)(rawY - gridY) / cellSize) * cellSize) - 4;
      }else{
        thePanel.int2Ship3X = rawX;
        thePanel.int2Ship3Y = rawY;
      }
    // Logic for dragging Ship 3 (2-cell)
    }else if(intDrag == 3){
      int rawX = evt.getX() - intOffsetX;
      int rawY = evt.getY() - intOffsetY;
      if(rawX > gridX - 50 && rawX < gridX + gridSize + 50 && rawY > gridY - 50 && rawY < gridY + gridSize + 50){
        thePanel.intShip2X = gridX + (int)(Math.round((double)(rawX - gridX) / cellSize) * cellSize) - 5;
        thePanel.intShip2Y = gridY + (int)(Math.round((double)(rawY - gridY) / cellSize) * cellSize) - 5;
      }else{
        thePanel.intShip2X = rawX;
        thePanel.intShip2Y = rawY;
      }
    // Logic for dragging Ship 4 (4-cell)
    }else if(intDrag == 4){
      int rawX = evt.getX() - intOffsetX;
      int rawY = evt.getY() - intOffsetY;
      if(rawX > gridX - 50 && rawX < gridX + gridSize + 50 && rawY > gridY - 50 && rawY < gridY + gridSize + 50){
        thePanel.intShip4X = gridX + (int)(Math.round((double)(rawX - gridX) / cellSize) * cellSize) - 5;
        thePanel.intShip4Y = gridY + (int)(Math.round((double)(rawY - gridY) / cellSize) * cellSize) - 5;
      }else{
        thePanel.intShip4X = rawX;
        thePanel.intShip4Y = rawY;
      }
    // Logic for dragging Ship 5 (5-cell)
    }else if(intDrag == 5){
      int rawX = evt.getX() - intOffsetX;
      int rawY = evt.getY() - intOffsetY;
      if(rawX > gridX - 50 && rawX < gridX + gridSize + 50 && rawY > gridY - 50 && rawY < gridY + gridSize + 50){
        thePanel.intShip5X = gridX + (int)(Math.round((double)(rawX - gridX) / cellSize) * cellSize) - 5;
        thePanel.intShip5Y = gridY + (int)(Math.round((double)(rawY - gridY) / cellSize) * cellSize) - 5;
      }else{
        thePanel.intShip5X = rawX;
        thePanel.intShip5Y = rawY;
      }
    }
  }
  @Override
  public void mouseMoved(MouseEvent evt) {
  }

  /**
   * Handles firing shots<p>
   * Sends shot data if player's turn
   * @param evt MouseEvent
   */
  @Override
  public void mouseClicked(MouseEvent evt) {
    int intX = evt.getX();
    int intY = evt.getY();
    
    if(battleOn && myTurn){
      // Check if click is within Enemy Grid (625, 125) to (1025, 525)
      if(intX >= 625 && intX <= 1025 && intY >= 125 && intY <= 525){
          int col = (intX - 625) / 40;
          int row = (intY - 125) / 40;
          
          // Check if already shot there
          if(thePanel.enemyGridState[row][col] == 0){
              // Valid Shot
              boolean blnHit = checkHitFromFile(connectScreen.strName + "_enemy_ships.txt", col, row);
              
              // Update Visuals
              thePanel.enemyGridState[row][col] = blnHit ? 1 : 2;
              thePanel.repaint();
              
              String coord = (char)('A' + row) + "" + (col + 1);
              String result = blnHit ? "hit" : "miss";
              connectScreen.theArea.append("System: " + connectScreen.strName + ", " + coord + ", " + result + "\n");
              connectScreen.theArea.setCaretPosition(connectScreen.theArea.getDocument().getLength());

              // Send to Opponent
              if(connectScreen.ssm != null){
                  connectScreen.ssm.sendText("SHOT," + col + "," + row);
              }

              if(blnHit){
                  enemyHits++;
                  if(enemyHits == 17){
                      gameOver("You Win!");
                  }
              }
              
              // End Turn
              myTurn = false;
              turnLabel.setText("Opponent's Turn");
          }
      }
    }
  }
  /**
   * Initiates ship dragging<p>
   * Detects clicked ship
   * @param evt MouseEvent
   */
  @Override
  public void mousePressed(MouseEvent evt) {
    theFrame.requestFocusInWindow();
    if(shipsLocked){
      return;
    }
    
    // We check ships in reverse order (5 down to 1) so we grab the one on top
    
    // Check click on Ship 5
    boolean isVertical = Math.round(thePanel.dblShip5rot / (Math.PI / 2)) % 2 != 0;
    
    int w = thePanel.ship5Image.getWidth();
    int h = thePanel.ship5Image.getHeight();

    if(isVertical){
      w = thePanel.ship5Image.getHeight();
      h = thePanel.ship5Image.getWidth();
    }

    if(evt.getX() >= thePanel.intShip5X && evt.getX() <= thePanel.intShip5X + w && evt.getY() >= thePanel.intShip5Y && evt.getY() <= thePanel.intShip5Y + h){
        intDrag = 5;
        intOffsetX = evt.getX() - thePanel.intShip5X;
        intOffsetY = evt.getY() - thePanel.intShip5Y;
        intOriginalX = thePanel.intShip5X;
        intOriginalY = thePanel.intShip5Y;
        return;
    }

    // Check click on Ship 4
    isVertical = Math.round(thePanel.dblShip4rot / (Math.PI / 2)) % 2 != 0;

    w = thePanel.ship4Image.getWidth();
    h = thePanel.ship4Image.getHeight();

    if(isVertical){
      w = thePanel.ship4Image.getHeight();
      h = thePanel.ship4Image.getWidth();
    }

    if(evt.getX() >= thePanel.intShip4X && evt.getX() <= thePanel.intShip4X + w && evt.getY() >= thePanel.intShip4Y && evt.getY() <= thePanel.intShip4Y + h){
        intDrag = 4;
        intOffsetX = evt.getX() - thePanel.intShip4X;
        intOffsetY = evt.getY() - thePanel.intShip4Y;
        intOriginalX = thePanel.intShip4X;
        intOriginalY = thePanel.intShip4Y;
        return;
    }

    // Check click on Ship 2
    isVertical = Math.round(thePanel.dblShip2rot / (Math.PI / 2)) % 2 != 0;

    w = thePanel.ship2Image.getWidth();
    h = thePanel.ship2Image.getHeight();

    if(isVertical){
      w = thePanel.ship2Image.getHeight();
      h = thePanel.ship2Image.getWidth();
    }

    if(evt.getX() >= thePanel.intShip2X && evt.getX() <= thePanel.intShip2X + w && evt.getY() >= thePanel.intShip2Y && evt.getY() <= thePanel.intShip2Y + h){
        intDrag = 3;
        intOffsetX = evt.getX() - thePanel.intShip2X;
        intOffsetY = evt.getY() - thePanel.intShip2Y;
        intOriginalX = thePanel.intShip2X;
        intOriginalY = thePanel.intShip2Y;
        return;
    }

    // Check click on Ship 3 (2nd one)
    isVertical = Math.round(thePanel.dbl2Ship3rot / (Math.PI / 2)) % 2 != 0;

    w = thePanel.twoShip3Image.getWidth();
    h = thePanel.twoShip3Image.getHeight();

    if(isVertical){
      w = thePanel.twoShip3Image.getHeight();
      h = thePanel.twoShip3Image.getWidth();
    }   

    if(evt.getX() >= thePanel.int2Ship3X && evt.getX() <= thePanel.int2Ship3X + w && evt.getY() >= thePanel.int2Ship3Y && evt.getY() <= thePanel.int2Ship3Y + h){
        intDrag = 2;
        intOffsetX = evt.getX() - thePanel.int2Ship3X;
        intOffsetY = evt.getY() - thePanel.int2Ship3Y;
        intOriginalX = thePanel.int2Ship3X;
        intOriginalY = thePanel.int2Ship3Y;
        return;
    }

    // Check click on Ship 3 (1st one)
    isVertical = Math.round(thePanel.dbl1Ship3rot / (Math.PI / 2)) % 2 != 0;

    w = thePanel.oneShip3Image.getWidth();
    h = thePanel.oneShip3Image.getHeight();

    if(isVertical){
      w = thePanel.oneShip3Image.getHeight();
      h = thePanel.oneShip3Image.getWidth();
    }

    if(evt.getX() >= thePanel.int1Ship3X && evt.getX() <= thePanel.int1Ship3X + w && evt.getY() >= thePanel.int1Ship3Y && evt.getY() <= thePanel.int1Ship3Y + h){
        intDrag = 1;
        intOffsetX = evt.getX() - thePanel.int1Ship3X;
        intOffsetY = evt.getY() - thePanel.int1Ship3Y;
        intOriginalX = thePanel.int1Ship3X;
        intOriginalY = thePanel.int1Ship3Y;
        return;
    }
  }
  /**
   * Drops ships<p>
   * Checks validity and resets if invalid
   * @param evt MouseEvent
   */
  @Override
  public void mouseReleased(MouseEvent evt) {
    int gridX = 141;
    int gridY = 125;
    int gridSize = 400;
    
    if(intDrag != 0){
      int x = 0; 
      int y = 0; 
      double rot = 0; 
      BufferedImage img = null;
        
      // Determine which ship was being dragged
      if(intDrag == 1){
        x = thePanel.int1Ship3X; 
        y = thePanel.int1Ship3Y; 
        rot = thePanel.dbl1Ship3rot; 
        img = thePanel.oneShip3Image;

      }else if(intDrag == 2){
        x = thePanel.int2Ship3X; 
        y = thePanel.int2Ship3Y; 
        rot = thePanel.dbl2Ship3rot; 
        img = thePanel.twoShip3Image;

      }else if(intDrag == 3){
        x = thePanel.intShip2X; 
        y = thePanel.intShip2Y; 
        rot = thePanel.dblShip2rot; 
        img = thePanel.ship2Image;

      }else if(intDrag == 4){
        x = thePanel.intShip4X; 
        y = thePanel.intShip4Y; 
        rot = thePanel.dblShip4rot; 
        img = thePanel.ship4Image;

      }else if(intDrag == 5){
        x = thePanel.intShip5X; 
        y = thePanel.intShip5Y; 
        rot = thePanel.dblShip5rot; 
        img = thePanel.ship5Image;
      }
        
        if(img != null){
          boolean isVertical = Math.round(rot / (Math.PI / 2)) % 2 != 0;

          int w = img.getWidth();
          int h = img.getHeight();

          if(isVertical){
            w = img.getHeight();
            h = img.getWidth();
          }
          
          // Check if ANY part of the ship is outside the grid
          boolean outOfBounds = x < gridX - 30 || x + w > gridX + gridSize + 30 || y < gridY - 30 || y + h > gridY + gridSize + 30;
          boolean overlapping = checkOverlap(intDrag, x, y, w, h);
          if(outOfBounds || overlapping){
            // Reset to original position
            if(intDrag == 1){ 
              thePanel.int1Ship3X = intOriginalX; 
              thePanel.int1Ship3Y = intOriginalY; 
            } else if(intDrag == 2){ 
              thePanel.int2Ship3X = intOriginalX; 
              thePanel.int2Ship3Y = intOriginalY; 
            } else if(intDrag == 3){ 
              thePanel.intShip2X = intOriginalX; 
              thePanel.intShip2Y = intOriginalY; 
            } else if(intDrag == 4){ 
              thePanel.intShip4X = intOriginalX; 
              thePanel.intShip4Y = intOriginalY; 
            } else if(intDrag == 5){ 
              thePanel.intShip5X = intOriginalX; 
              thePanel.intShip5Y = intOriginalY; 
            }
          }
        }
    }
    thePanel.repaint();
    intDrag = 0;
  }
  @Override
  public void mouseEntered(MouseEvent evt) {
  }
  @Override
  public void mouseExited(MouseEvent evt) {
  }

    @Override
  public void keyTyped(KeyEvent evt) {
  }
  /**
   * Handles key presses<p>
   * Rotates ships with 'r'
   * @param evt KeyEvent
   */
  @Override
  public void keyPressed(KeyEvent evt) {
    if(evt.getKeyChar()=='r' && intDrag!=0){
      double dblDeg = Math.PI/2;

      if(intDrag==1){ 
        thePanel.dbl1Ship3rot += dblDeg;
      }else if(intDrag==2){
        thePanel.dbl2Ship3rot += dblDeg; 
      }else if(intDrag==3){
        thePanel.dblShip2rot += dblDeg; 
      }else if(intDrag==4){
        thePanel.dblShip4rot += dblDeg; 
      }else if(intDrag==5){
        thePanel.dblShip5rot += dblDeg;
      }
      thePanel.repaint();

    }
  }
  @Override
  public void keyReleased(KeyEvent evt) {
  }

  /**
   * Displays Game Over screen
   * @param result Result string
   */
  public void gameOver(String result){
  battleOn = false;
  
  JPanel gameOverPanel = new JPanel();
  gameOverPanel.setLayout(null);
  gameOverPanel.setPreferredSize(new Dimension(1280, 720));
  gameOverPanel.setBackground(new Color(31, 68, 130));

  JLabel gameOverLabel = new JLabel("Game Over", SwingConstants.CENTER);
  gameOverLabel.setFont(new Font("Arial", Font.BOLD, 80));
  gameOverLabel.setForeground(Color.WHITE);
  gameOverLabel.setBounds(0, 100, 1280, 100);
  gameOverPanel.add(gameOverLabel);

  JLabel resultLabel = new JLabel(result, SwingConstants.CENTER);
  resultLabel.setFont(new Font("Arial", Font.BOLD, 40));
  resultLabel.setForeground(Color.WHITE);
  resultLabel.setBounds(0, 250, 1280, 50);
  gameOverPanel.add(resultLabel);

  JButton backToMenuButton = new JButton("Back to Menu");
  backToMenuButton.setBounds(540, 400, 200, 50);
  backToMenuButton.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent evt){
        if(connectScreen.ssm != null){
            connectScreen.ssm.disconnect();
        }
        theFrame.dispose();
        new mainProgram();
    }
  });
  gameOverPanel.add(backToMenuButton);

  theFrame.setContentPane(gameOverPanel);
  theFrame.pack();
  theFrame.repaint();
  }

  /**
   * Initializes Main Menu UI
   */
  public void mainMenu(){
    playButton = new JButton("Play");
    helpButton = new JButton("Help");
    
    theFrame.setContentPane(mainMenuPanel);
    mainMenuPanel.setLayout(null);

    playButton.setSize(200, 60);
    playButton.setLocation(540, 250);
    playButton.addActionListener(this);
    mainMenuPanel.add(playButton);

    helpButton.setSize(200, 60);
    helpButton.setLocation(540, 330);
    helpButton.addActionListener(this);
    mainMenuPanel.add(helpButton);

    exitButton = new JButton("Exit");
    exitButton.setSize(200, 60);
    exitButton.setLocation(540, 410);
    exitButton.addActionListener(this);
    mainMenuPanel.add(exitButton);

    theFrame.pack();
  }

  /**
   * Constructor for mainProgram<p>
   * Sets up UI, network, and game loop
   */
  public mainProgram(){

    mainMenu();

    thePanel.addMouseMotionListener(this);
    thePanel.addMouseListener(this);

    thePanel.setBackground(new Color(31, 68, 130));
    thePanel.setPreferredSize(new Dimension(1280,720)); 
    thePanel.setLayout(null); 
    mainMenuPanel.setPreferredSize(new Dimension(1280,720));

    connectScreen = new chatPanel(this);
    connectScreen.setPreferredSize(new Dimension(1280, 720));
    connectScreen.setLayout(null);
    thePanel.add(connectScreen.theScroll);
    thePanel.add(connectScreen.theField);

    turnLabel.setBounds(650, 550, 200, 50);
    turnLabel.setFont(new Font("Arial", Font.BOLD, 20));
    turnLabel.setForeground(Color.WHITE);
    thePanel.add(turnLabel);
    
    playerGrid = new BattleGrid(141, 125, 40);
    thePanel.add(playerGrid);
    enemyGrid = new BattleGrid(625, 125, 40);
    thePanel.add(enemyGrid);

    helpScreen = new helpPanel(this);
    helpScreen.setPreferredSize(new Dimension(1280, 720));
    helpScreen.setLayout(null);

    aboutPanel = new JPanel();
    aboutPanel.setLayout(null);
    aboutPanel.setPreferredSize(new Dimension(1280, 720));
    aboutPanel.setBackground(new Color(31, 68, 130));

    JLabel aboutLabel = new JLabel("<html><center><h1>Battleship</h1><br>Created by Ethan, Carsten, Brandon<br>Version 1.0</center></html>", SwingConstants.CENTER);
    aboutLabel.setFont(new Font("Arial", Font.BOLD, 30));
    aboutLabel.setForeground(Color.WHITE);
    aboutLabel.setBounds(0, 100, 1280, 300);
    aboutPanel.add(aboutLabel);

    aboutBackButton = new JButton("Back to Menu");
    aboutBackButton.setBounds(540, 500, 200, 60);
    aboutBackButton.addActionListener(this);
    aboutPanel.add(aboutBackButton);


    theFrame.setJMenuBar(theMenuBar);
    theMenuBar.add(theMenu);

    theMenu.add(instructions);
    theMenu.add(about);

    instructions.addActionListener(this);
    about.addActionListener(this);

    confirmButton = new JButton("Confirm Ships");
    confirmButton.setSize(150, 50);
    confirmButton.setLocation(650, 600);
    confirmButton.addActionListener(this);
    thePanel.add(confirmButton);

    InputStream menuStream = this.getClass().getResourceAsStream("graphics/main menu.png");
    try{
        menuImage = ImageIO.read(menuStream);
    }catch(IOException e){
        System.out.println("Unable to load image");
    }
    menuLabel = new JLabel(new ImageIcon(menuImage));
    menuLabel.setSize(1280,680); 
    menuLabel.setLocation(0,0);
    mainMenuPanel.add(menuLabel);
    
    theFrame.addKeyListener(this); 

    theFrame.setFocusable(true);
    theTimer.start();
    theFrame.requestFocus();
    theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    theFrame.pack(); 
    theFrame.setVisible(true); 
    
  }

  /**
   * Application entry point
   * @param args Command line arguments
   */
  public static void main(String[] args){
    new mainProgram();
  }

}


  
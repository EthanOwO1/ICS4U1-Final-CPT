//Battleship Game
//version 0.0
//December 18 2025

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;


public class mainProgram implements ActionListener, MouseListener, MouseMotionListener, KeyListener{
  
  // Properties 
  JFrame theFrame = new JFrame("Battleship"); 
  JPanel mainMenuPanel = new JPanel();
  animation thePanel = new animation();
  Timer theTimer = new Timer(1000/60, this);

  JMenuBar theMenuBar = new JMenuBar();
  JMenu theMenu = new JMenu("Menu");
  JMenuItem instructions = new JMenuItem("Instructions");
  JMenuItem about = new JMenuItem("About");

  JButton playButton;
  JButton helpbutton;
  JButton exitButton;
  JButton confirmButton;

  int intDrag = 0;
  int intOffsetX = 0;
  int intOffsetY = 0;
  int intOriginalX = 0;
  int intOriginalY = 0;
  boolean shipsLocked = false;
  
  // Grids
  BattleGrid playerGrid;
  BattleGrid enemyGrid;

  // Enemy Ship Data
  int[] enemyShipX = new int[5];
  int[] enemyShipY = new int[5];
  double[] enemyShipRot = new double[5];
  boolean enemyShipsReceived = false;
  int[][] enemyShipMap = new int[10][10]; // 0 = Empty, 1 = Ship
  int[][] playerShipMap = new int[10][10];
  
  boolean battleOn = false;
  int playerHits = 0;
  int enemyHits = 0;
  boolean myTurn = false;
  JLabel turnLabel = new JLabel("Waiting...");

   // Button panels
  helpPanel helpScreen;
  chatPanel connectScreen;

  // Methods
  public void actionPerformed(ActionEvent evt){
    if(evt.getSource() == playButton){
      theFrame.setContentPane(thePanel);
      theFrame.pack();
      theFrame.repaint();
    }
    if(evt.getSource() == playButton){
        theFrame.setContentPane(connectScreen);
        theFrame.pack();
        theFrame.repaint();
    }

    if(evt.getSource() == helpbutton){
        theFrame.setContentPane(helpScreen);
        theFrame.pack();
        theFrame.repaint();
    }

    if(evt.getSource() == confirmButton){
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
        
        // Send Ship Coordinates to Opponent
        if(connectScreen.ssm != null){
          String msg = "SHIPS";
          msg += "," + thePanel.int1Ship3X + "," + thePanel.int1Ship3Y + "," + thePanel.dbl1Ship3rot;
          msg += "," + thePanel.int2Ship3X + "," + thePanel.int2Ship3Y + "," + thePanel.dbl2Ship3rot;
          msg += "," + thePanel.intShip2X + "," + thePanel.intShip2Y + "," + thePanel.dblShip2rot;
          msg += "," + thePanel.intShip4X + "," + thePanel.intShip4Y + "," + thePanel.dblShip4rot;
          msg += "," + thePanel.intShip5X + "," + thePanel.intShip5Y + "," + thePanel.dblShip5rot;
          connectScreen.ssm.sendText(msg);
        }
        mapPlayerShips();
        checkBattleStart();
      }
    }
  }

  public void startGame(){
    theFrame.setContentPane(thePanel);
    theFrame.pack();
    theFrame.repaint();
  }

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
    
    // Map enemy ships to the logical grid for collision detection
    mapEnemyShip(0, 3); // Ship 1 (Length 3)
    mapEnemyShip(1, 3); // Ship 2 (Length 3)
    mapEnemyShip(2, 2); // Ship 3 (Length 2)
    mapEnemyShip(3, 4); // Ship 4 (Length 4)
    mapEnemyShip(4, 5); // Ship 5 (Length 5)
    
    enemyShipsReceived = true;
    checkBattleStart();
  }

  public void checkBattleStart(){
    if(shipsLocked && enemyShipsReceived){
      battleOn = true;
      System.out.println("Both players ready. Battle Start!");
      if(myTurn){
          turnLabel.setText("Your Turn");
      }else{
          turnLabel.setText("Opponent's Turn");
      }
    }
  }
  
  public void mapEnemyShip(int index, int length){
      // Convert pixel coordinates (from sender's 141,125 origin) to grid indices
      int col = (int)Math.round((enemyShipX[index] + 4 - 141) / 40.0);
      int row = (int)Math.round((enemyShipY[index] + 4 - 125) / 40.0);
      
      boolean isVertical = Math.round(enemyShipRot[index] / (Math.PI / 2)) % 2 != 0;
      
      for(int i = 0; i < length; i++){
          if(isVertical){
              if(row + i < 10 && col < 10) enemyShipMap[row + i][col] = 1;
          }else{
              if(row < 10 && col + i < 10) enemyShipMap[row][col + i] = 1;
          }
      }
  }
  
  public void mapPlayerShips(){
      mapSinglePlayerShip(thePanel.int1Ship3X, thePanel.int1Ship3Y, thePanel.dbl1Ship3rot, 3);
      mapSinglePlayerShip(thePanel.int2Ship3X, thePanel.int2Ship3Y, thePanel.dbl2Ship3rot, 3);
      mapSinglePlayerShip(thePanel.intShip2X, thePanel.intShip2Y, thePanel.dblShip2rot, 2);
      mapSinglePlayerShip(thePanel.intShip4X, thePanel.intShip4Y, thePanel.dblShip4rot, 4);
      mapSinglePlayerShip(thePanel.intShip5X, thePanel.intShip5Y, thePanel.dblShip5rot, 5);
  }

  public void mapSinglePlayerShip(int x, int y, double rot, int length){
      int col = (int)Math.round((x + 4 - 141) / 40.0);
      int row = (int)Math.round((y + 4 - 125) / 40.0);
      
      boolean isVertical = Math.round(rot / (Math.PI / 2)) % 2 != 0;
      
      for(int i = 0; i < length; i++){
          if(isVertical){
              if(row + i < 10 && col < 10) playerShipMap[row + i][col] = 1;
          }else{
              if(row < 10 && col + i < 10) playerShipMap[row][col + i] = 1;
          }
      }
  }

  public void receiveShot(String msg){
      // Format: SHOT,col,row
      String[] parts = msg.split(",");
      int col = Integer.parseInt(parts[1]);
      int row = Integer.parseInt(parts[2]);
      
      if(thePanel.playerGridState[row][col] != 0) return;
      
      boolean hit = checkMyCollision(col, row);
      thePanel.playerGridState[row][col] = hit ? 1 : 2;
      
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
  
  public boolean checkMyCollision(int col, int row){
      if(col >= 0 && col < 10 && row >= 0 && row < 10){
          return playerShipMap[row][col] == 1;
      }
      return false;
  }

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

  public boolean isOverlapping(int x1, int y1, int w1, int h1, int x2, int y2, double rot2, BufferedImage img2) {
      if (img2 == null) return false;
      boolean isVertical = Math.round(rot2 / (Math.PI / 2)) % 2 != 0;
      int w2 = isVertical ? img2.getHeight() : img2.getWidth();
      int h2 = isVertical ? img2.getWidth() : img2.getHeight();
      
      // Standard rectangle intersection with a margin to allow placing ships next to each other
      int margin = 10;
      return x1 + margin < x2 + w2 - margin && x1 + w1 - margin > x2 + margin && y1 + margin < y2 + h2 - margin && y1 + h1 - margin > y2 + margin;
  }

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

    if(intDrag == 1){
      int rawX = evt.getX() - intOffsetX;
      int rawY = evt.getY() - intOffsetY;
      if(rawX > gridX - 20 && rawX < gridX + gridSize - 20 && rawY > gridY - 20 && rawY < gridY + gridSize - 20){
        thePanel.int1Ship3X = gridX + (int)(Math.round((double)(rawX - gridX) / cellSize) * cellSize) - 4;
        thePanel.int1Ship3Y = gridY + (int)(Math.round((double)(rawY - gridY) / cellSize) * cellSize) - 4;
      }else{
        thePanel.int1Ship3X = rawX;
        thePanel.int1Ship3Y = rawY;
      }
    }else if(intDrag == 2){
      int rawX = evt.getX() - intOffsetX;
      int rawY = evt.getY() - intOffsetY;
      if(rawX > gridX - 20 && rawX < gridX + gridSize - 20 && rawY > gridY - 20 && rawY < gridY + gridSize - 20){
        thePanel.int2Ship3X = gridX + (int)(Math.round((double)(rawX - gridX) / cellSize) * cellSize) - 4;
        thePanel.int2Ship3Y = gridY + (int)(Math.round((double)(rawY - gridY) / cellSize) * cellSize) - 4;
      }else{
        thePanel.int2Ship3X = rawX;
        thePanel.int2Ship3Y = rawY;
      }
    }else if(intDrag == 3){
      int rawX = evt.getX() - intOffsetX;
      int rawY = evt.getY() - intOffsetY;
      if(rawX > gridX - 20 && rawX < gridX + gridSize - 20 && rawY > gridY - 20 && rawY < gridY + gridSize - 20){
        thePanel.intShip2X = gridX + (int)(Math.round((double)(rawX - gridX) / cellSize) * cellSize) - 5;
        thePanel.intShip2Y = gridY + (int)(Math.round((double)(rawY - gridY) / cellSize) * cellSize) - 5;
      }else{
        thePanel.intShip2X = rawX;
        thePanel.intShip2Y = rawY;
      }
    }else if(intDrag == 4){
      int rawX = evt.getX() - intOffsetX;
      int rawY = evt.getY() - intOffsetY;
      if(rawX > gridX - 20 && rawX < gridX + gridSize - 20 && rawY > gridY - 20 && rawY < gridY + gridSize - 20){
        thePanel.intShip4X = gridX + (int)(Math.round((double)(rawX - gridX) / cellSize) * cellSize) - 5;
        thePanel.intShip4Y = gridY + (int)(Math.round((double)(rawY - gridY) / cellSize) * cellSize) - 5;
      }else{
        thePanel.intShip4X = rawX;
        thePanel.intShip4Y = rawY;
      }
    }else if(intDrag == 5){
      int rawX = evt.getX() - intOffsetX;
      int rawY = evt.getY() - intOffsetY;
      if(rawX > gridX - 20 && rawX < gridX + gridSize - 20 && rawY > gridY - 20 && rawY < gridY + gridSize - 20){
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
              boolean hit = (enemyShipMap[row][col] == 1);
              
              // Update Visuals
              thePanel.enemyGridState[row][col] = hit ? 1 : 2;
              thePanel.repaint();
              
              // Send to Opponent
              if(connectScreen.ssm != null){
                  connectScreen.ssm.sendText("SHOT," + col + "," + row);
              }
              
              if(hit){
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
  @Override
  public void mousePressed(MouseEvent evt) {
    theFrame.requestFocusInWindow();
    if(shipsLocked){
      return;
    }
    
    // We check ships in reverse order (5 down to 1) so we grab the one on top
    
    // Ship 5
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

    // Ship 4
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

    // Ship 2
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

    // Ship 3 (2nd one)
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

    // Ship 3 (1st one)
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
          boolean outOfBounds = x < gridX - 10 || x + w > gridX + gridSize + 10 || y < gridY - 10 || y + h > gridY + gridSize + 10;
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

  public void gameOver(String result){
  battleOn = false;

  JOptionPane.showMessageDialog(theFrame, "Game Over! " + result);
  
  if(connectScreen.ssm != null){
    connectScreen.ssm.disconnect();
  }
  theFrame.dispose();
  new mainProgram();
  }

  //main menu
  public void mainMenu(){
    JLabel theTitle = new JLabel("Battleships");
    playButton = new JButton("Play");
    helpbutton = new JButton("Help");
    exitButton = new JButton("Exit");

    Font currentFont = theTitle.getFont();
    Font biggerFont = currentFont.deriveFont(currentFont.getSize() + 20f);

    theTitle.setFont(biggerFont);
    
    theFrame.setContentPane(mainMenuPanel);
    mainMenuPanel.setLayout(null);

    theTitle.setSize(200, 50);
    theTitle.setLocation(550, 50);
    mainMenuPanel.add(theTitle);

    playButton.setSize(100, 30);
    playButton.setLocation(600, 300);
    playButton.addActionListener(this);
    mainMenuPanel.add(playButton);

    helpbutton.setSize(100, 30);
    helpbutton.setLocation(600, 350);
    helpbutton.addActionListener(this);
    mainMenuPanel.add(helpbutton);

    exitButton = new JButton("Exit");
    exitButton.setSize(100, 30);
    exitButton.setLocation(600, 400);
    exitButton.addActionListener(e -> System.exit(0));
    mainMenuPanel.add(exitButton);


    theFrame.pack();
  }

  // Constructors 
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

    helpScreen = new helpPanel();
    helpScreen.setPreferredSize(new Dimension(1280, 720));
    helpScreen.setLayout(null);


    theFrame.setJMenuBar(theMenuBar);
    theMenuBar.add(theMenu);

    theMenu.add(instructions);
    theMenu.add(about);

    confirmButton = new JButton("Confirm Ships");
    confirmButton.setSize(150, 50);
    confirmButton.setLocation(650, 600);
    confirmButton.addActionListener(this);
    thePanel.add(confirmButton);
    
    theFrame.addKeyListener(this); 

    theFrame.setFocusable(true);
    theFrame.requestFocus();
    theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    theFrame.pack(); 
    theFrame.setVisible(true); 
    
  }

  // Main Method
  public static void main(String[] args){
    new mainProgram();
  }

}


  
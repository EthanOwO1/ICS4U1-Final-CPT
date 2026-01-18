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
  boolean bln1Ship3Vertical = false;
  boolean bln2Ship3Vertical = false;
  boolean blnShip2Vertical = false;
  boolean blnShip4Vertical = false;
  boolean blnShip5Vertical = false;
  
  // Grids
  BattleGrid playerGrid;
  BattleGrid enemyGrid;

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
      }
    }
  }

  public void startGame(){
    theFrame.setContentPane(thePanel);
    theFrame.pack();
    theFrame.repaint();
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

    if(intDrag==1){
      // 1st Ship 3

      int rawX = evt.getX() - intOffsetX;
      int rawY = evt.getY() - intOffsetY;

      if(rawX > gridX - 20 && rawX < gridX + gridSize - 20 && rawY > gridY - 20 && rawY < gridY + gridSize - 20){
        thePanel.int1Ship3X = gridX + (int)(Math.round((double)(rawX - gridX) / cellSize) * cellSize) - 4;
        thePanel.int1Ship3Y = gridY + (int)(Math.round((double)(rawY - gridY) / cellSize) * cellSize) - 4;
      }else{
        thePanel.int1Ship3X = rawX;
        thePanel.int1Ship3Y = rawY;
      }

    }else if(intDrag==2){
      // 2nd Ship 3
      int rawX = evt.getX() - intOffsetX;
      int rawY = evt.getY() - intOffsetY;

      if(rawX > gridX - 20 && rawX < gridX + gridSize - 20 && rawY > gridY - 20 && rawY < gridY + gridSize - 20){
        thePanel.int2Ship3X = gridX + (int)(Math.round((double)(rawX - gridX) / cellSize) * cellSize) - 4;
        thePanel.int2Ship3Y = gridY + (int)(Math.round((double)(rawY - gridY) / cellSize) * cellSize) - 4;
      }else{
        thePanel.int2Ship3X = rawX;
        thePanel.int2Ship3Y = rawY;
      }
    }else if(intDrag==3){
      // Ship 2
      int rawX = evt.getX() - intOffsetX;
      int rawY = evt.getY() - intOffsetY;

      if(rawX > gridX - 20 && rawX < gridX + gridSize - 20 && rawY > gridY - 20 && rawY < gridY + gridSize - 20){
        thePanel.intShip2X = gridX + (int)(Math.round((double)(rawX - gridX) / cellSize) * cellSize) - 5;
        thePanel.intShip2Y = gridY + (int)(Math.round((double)(rawY - gridY) / cellSize) * cellSize) - 5;
      }else{
        thePanel.intShip2X = rawX;
        thePanel.intShip2Y = rawY;
      }
    }else if(intDrag==4){
      // Ship 4
      int rawX = evt.getX() - intOffsetX;
      int rawY = evt.getY() - intOffsetY;

      if(rawX > gridX - 20 && rawX < gridX + gridSize - 20 && rawY > gridY - 20 && rawY < gridY + gridSize - 20){
        thePanel.intShip4X = gridX + (int)(Math.round((double)(rawX - gridX) / cellSize) * cellSize) - 5;
        thePanel.intShip4Y = gridY + (int)(Math.round((double)(rawY - gridY) / cellSize) * cellSize) - 5;
      }else{
        thePanel.intShip4X = rawX;
        thePanel.intShip4Y = rawY;
      }
    }else if(intDrag==5){
      // Ship 5
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
  }
  @Override
  public void mousePressed(MouseEvent evt) {
    if(shipsLocked){
      return;
    }
    // 1st Ship 3 
    int intWidth1; 
    int intHeight1; 
    if(bln1Ship3Vertical){
      intWidth1 = 50; 
      intHeight1 = 130; 
    }else{
      intWidth1 = 130; 
      intHeight1 = 50; 
    }
    if(evt.getX()>=thePanel.int1Ship3X && evt.getX()<=thePanel.int1Ship3X+intWidth1 && evt.getY()>=thePanel.int1Ship3Y && evt.getY()<=thePanel.int1Ship3Y+intHeight1){
      intDrag = 1; 
      intOffsetX = evt.getX() - thePanel.int1Ship3X;
      intOffsetY = evt.getY() - thePanel.int1Ship3Y;
      intOriginalX = thePanel.int1Ship3X;
      intOriginalY = thePanel.int1Ship3Y;
    }
    // 2nd Ship 3
    int intWidth2; 
    int intHeight2; 
    if(bln2Ship3Vertical){
      intWidth2 = 50; 
      intHeight2 = 130; 
    }else{
      intWidth2 = 130; 
      intHeight2 = 50; 
    }
    if(evt.getX()>=thePanel.int2Ship3X && evt.getX()<=thePanel.int2Ship3X+intWidth2 && evt.getY()>=thePanel.int2Ship3Y && evt.getY()<=thePanel.int2Ship3Y+intHeight2){
      intDrag = 2;
      intOffsetX = evt.getX() - thePanel.int2Ship3X;
      intOffsetY = evt.getY() - thePanel.int2Ship3Y;
      intOriginalX = thePanel.int2Ship3X;
      intOriginalY = thePanel.int2Ship3Y;
    }
    // Ship 2
    int intWidth3; 
    int intHeight3; 
    if(blnShip2Vertical){
      intWidth3 = 50; 
      intHeight3 = 100; 
    }else{
      intWidth3 = 100; 
      intHeight3 = 50; 
    }
    if(evt.getX()>=thePanel.intShip2X && evt.getX()<=thePanel.intShip2X+intWidth3 && evt.getY()>=thePanel.intShip2Y && evt.getY()<=thePanel.intShip2Y+intHeight3){
      intDrag = 3;
      intOffsetX = evt.getX() - thePanel.intShip2X;
      intOffsetY = evt.getY() - thePanel.intShip2Y;
      intOriginalX = thePanel.intShip2X;
      intOriginalY = thePanel.intShip2Y;
    }
    // Ship 4
    int intWidth4; 
    int intHeight4; 
    if(blnShip4Vertical){
      intWidth4 = 50; 
      intHeight4 = 170; 
    }else{
      intWidth4 = 170; 
      intHeight4 = 50; 
    }
    if(evt.getX()>=thePanel.intShip4X && evt.getX()<=thePanel.intShip4X+intWidth4 && evt.getY()>=thePanel.intShip4Y && evt.getY()<=thePanel.intShip4Y+intHeight4){
      intDrag = 4;
      intOffsetX = evt.getX() - thePanel.intShip4X;
      intOffsetY = evt.getY() - thePanel.intShip4Y;
      intOriginalX = thePanel.intShip4X;
      intOriginalY = thePanel.intShip4Y;

    }
    // Ship 5
    int intWidth5; 
    int intHeight5; 
    if(blnShip5Vertical){
      intWidth5 = 50; 
      intHeight5 = 215; 
    }else{
      intWidth5 = 215; 
      intHeight5 = 50; 
    }
    if(evt.getX()>=thePanel.intShip5X && evt.getX()<=thePanel.intShip5X+intWidth5 && evt.getY()>=thePanel.intShip5Y && evt.getY()<=thePanel.intShip5Y+intHeight5){
      intDrag = 5;
      intOffsetX = evt.getX() - thePanel.intShip5X;
      intOffsetY = evt.getY() - thePanel.intShip5Y;
      intOriginalX = thePanel.intShip5X;  
      intOriginalY = thePanel.intShip5Y;
    }

  }
  @Override
  public void mouseReleased(MouseEvent evt) {
    int gridX = 141;
    int gridY = 125;
    int gridSize = 400;

    if(intDrag == 1){
      if(thePanel.int1Ship3X < gridX - 20 || thePanel.int1Ship3X > gridX + gridSize - 100 || thePanel.int1Ship3Y < gridY - 20 || thePanel.int1Ship3Y > gridY + gridSize - 20){
        thePanel.int1Ship3X = intOriginalX;
        thePanel.int1Ship3Y = intOriginalY;
      }
    }else if(intDrag == 2){
      if(thePanel.int2Ship3X < gridX - 20 || thePanel.int2Ship3X > gridX + gridSize - 100 || thePanel.int2Ship3Y < gridY - 20 || thePanel.int2Ship3Y > gridY + gridSize - 20){
        thePanel.int2Ship3X = intOriginalX;
        thePanel.int2Ship3Y = intOriginalY;
      }
    }else if(intDrag == 3){
      if(thePanel.intShip2X < gridX - 20 || thePanel.intShip2X > gridX + gridSize - 50 || thePanel.intShip2Y < gridY - 20 || thePanel.intShip2Y > gridY + gridSize - 20){
        thePanel.intShip2X = intOriginalX;
        thePanel.intShip2Y = intOriginalY;
      }
    }else if(intDrag == 4){
      if(thePanel.intShip4X < gridX - 20 || thePanel.intShip4X > gridX + gridSize - 150 || thePanel.intShip4Y < gridY - 20 || thePanel.intShip4Y > gridY + gridSize - 20){
        thePanel.intShip4X = intOriginalX;
        thePanel.intShip4Y = intOriginalY;
      }
    }else if(intDrag == 5){
      if(thePanel.intShip5X < gridX - 20 || thePanel.intShip5X > gridX + gridSize - 200 || thePanel.intShip5Y < gridY - 20 || thePanel.intShip5Y > gridY + gridSize - 20){
        thePanel.intShip5X = intOriginalX;
        thePanel.intShip5Y = intOriginalY;
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
        bln1Ship3Vertical = !bln1Ship3Vertical;
      }else if(intDrag==2){
        thePanel.dbl2Ship3rot += dblDeg; 
        bln2Ship3Vertical = !bln2Ship3Vertical; 
      }else if(intDrag==3){
        thePanel.dblShip2rot += dblDeg; 
        blnShip2Vertical = !blnShip2Vertical;
      }else if(intDrag==4){
        thePanel.dblShip4rot += dblDeg; 
        blnShip4Vertical = !blnShip4Vertical;
      }else if(intDrag==5){
        thePanel.dblShip5rot += dblDeg;
        blnShip5Vertical = !blnShip5Vertical;
      }
      thePanel.repaint();

    }
  }
  @Override
  public void keyReleased(KeyEvent evt) {
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

    thePanel.setPreferredSize(new Dimension(1280,720)); 
    thePanel.setLayout(null); 
    mainMenuPanel.setPreferredSize(new Dimension(1280,720));

    connectScreen = new chatPanel(this);
    connectScreen.setPreferredSize(new Dimension(1280, 720));
    connectScreen.setLayout(null);
    thePanel.add(connectScreen.theScroll);
    thePanel.add(connectScreen.theField);

    
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
    confirmButton.setLocation(550, 650);
    confirmButton.addActionListener(this);
    thePanel.add(confirmButton);
    
    theFrame.addKeyListener(this); 

    theFrame.setFocusable(true);
    theFrame.requestFocus();
    theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    theFrame.pack(); 
    theFrame.setVisible(true); 
    
  }

  public void showScreen(String string) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'showScreen'");
  }

  // Main Method
  public static void main(String[] args){
    new mainProgram();
  }

}


  
//Battleship Game
//version 0.0
//December 18 2025

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;


public class mainProgram implements ActionListener, MouseListener, MouseMotionListener{
  
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

  int intDrag = 0;
  int intOffsetX = 0;
  int intOffsetY = 0;
  int intOriginalX = 0;
  int intOriginalY = 0;
  
  // Grids
  BattleGrid playerGrid;
  BattleGrid enemyGrid;

   //button panels
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
  }
  @Override
  public void mouseDragged(MouseEvent evt) {
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
    // 1st Ship 3
    if(evt.getX()>=thePanel.int1Ship3X && evt.getX()<=thePanel.int1Ship3X+130 && evt.getY()>=thePanel.int1Ship3Y && evt.getY()<=thePanel.int1Ship3Y+50){
      intDrag = 1; 
      intOffsetX = evt.getX() - thePanel.int1Ship3X;
      intOffsetY = evt.getY() - thePanel.int1Ship3Y;
      intOriginalX = thePanel.int1Ship3X;
      intOriginalY = thePanel.int1Ship3Y;
    }
    // 2nd Ship 3
    if(evt.getX()>=thePanel.int2Ship3X && evt.getX()<=thePanel.int2Ship3X+130 && evt.getY()>=thePanel.int2Ship3Y && evt.getY()<=thePanel.int2Ship3Y+50){
      intDrag = 2;
      intOffsetX = evt.getX() - thePanel.int2Ship3X;
      intOffsetY = evt.getY() - thePanel.int2Ship3Y;
      intOriginalX = thePanel.int2Ship3X;
      intOriginalY = thePanel.int2Ship3Y;
    }
    // Ship 2
    if(evt.getX()>=thePanel.intShip2X && evt.getX()<=thePanel.intShip2X+100 && evt.getY()>=thePanel.intShip2Y && evt.getY()<=thePanel.intShip2Y+50){
      intDrag = 3;
      intOffsetX = evt.getX() - thePanel.intShip2X;
      intOffsetY = evt.getY() - thePanel.intShip2Y;
      intOriginalX = thePanel.intShip2X;
      intOriginalY = thePanel.intShip2Y;
    }
    // Ship 4
    if(evt.getX()>=thePanel.intShip4X && evt.getX()<=thePanel.intShip4X+170 && evt.getY()>=thePanel.intShip4Y && evt.getY()<=thePanel.intShip4Y+50){
      intDrag = 4;
      intOffsetX = evt.getX() - thePanel.intShip4X;
      intOffsetY = evt.getY() - thePanel.intShip4Y;
      intOriginalX = thePanel.intShip4X;
      intOriginalY = thePanel.intShip4Y;

    }
    // Ship 5
    if(evt.getX()>=thePanel.intShip5X && evt.getX()<=thePanel.intShip5X+215 && evt.getY()>=thePanel.intShip5Y && evt.getY()<=thePanel.intShip5Y+50){
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
      if(thePanel.int1Ship3X < gridX - 20 || thePanel.int1Ship3X > gridX + gridSize - 20 || thePanel.int1Ship3Y < gridY - 20 || thePanel.int1Ship3Y > gridY + gridSize - 20){
        thePanel.int1Ship3X = intOriginalX;
        thePanel.int1Ship3Y = intOriginalY;
      }
    }else if(intDrag == 2){
      if(thePanel.int2Ship3X < gridX - 20 || thePanel.int2Ship3X > gridX + gridSize - 20 || thePanel.int2Ship3Y < gridY - 20 || thePanel.int2Ship3Y > gridY + gridSize - 20){
        thePanel.int2Ship3X = intOriginalX;
        thePanel.int2Ship3Y = intOriginalY;
      }
    }else if(intDrag == 3){
      if(thePanel.intShip2X < gridX - 20 || thePanel.intShip2X > gridX + gridSize - 20 || thePanel.intShip2Y < gridY - 20 || thePanel.intShip2Y > gridY + gridSize - 20){
        thePanel.intShip2X = intOriginalX;
        thePanel.intShip2Y = intOriginalY;
      }
    }else if(intDrag == 4){
      if(thePanel.intShip4X < gridX - 20 || thePanel.intShip4X > gridX + gridSize - 20 || thePanel.intShip4Y < gridY - 20 || thePanel.intShip4Y > gridY + gridSize - 20){
        thePanel.intShip4X = intOriginalX;
        thePanel.intShip4Y = intOriginalY;
      }
    }else if(intDrag == 5){
      if(thePanel.intShip5X < gridX - 20 || thePanel.intShip5X > gridX + gridSize - 20 || thePanel.intShip5Y < gridY - 20 || thePanel.intShip5Y > gridY + gridSize - 20){
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

    connectScreen = new chatPanel();
    connectScreen.setPreferredSize(new Dimension(1280, 720));
    connectScreen.setLayout(null);

    
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


  
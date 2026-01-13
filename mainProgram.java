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

  int intDrag = 0;
  
  // Grids
  BattleGrid playerGrid;
  BattleGrid enemyGrid;

  // Methods
  public void actionPerformed(ActionEvent evt){
    if(evt.getSource() == playButton){
      theFrame.setContentPane(thePanel);
      theFrame.pack();
      theFrame.repaint();
    }
  }
  @Override
  public void mouseDragged(MouseEvent evt) {
    thePanel.repaint(); 
    if(intDrag==1){
      // 1st Ship 3
      int intCenterX = thePanel.int1Ship3X+65; 
      int intCenterY = thePanel.int1Ship3Y+25; 
      if(evt.getY()<intCenterY){
        thePanel.int1Ship3Y -= 10;
      }else if(evt.getY()>intCenterY){
        thePanel.int1Ship3Y += 10; 
      }
      if(evt.getX()<intCenterX){
        thePanel.int1Ship3X -= 10;
      }else if(evt.getX()>intCenterX){
        thePanel.int1Ship3X += 10; 
      }
    }else if(intDrag==2){
      // 2nd Ship 3
      int intCenterX = thePanel.int2Ship3X+65; 
      int intCenterY = thePanel.int2Ship3Y+25; 
      if(evt.getY()<intCenterY){
        thePanel.int2Ship3Y -= 10;
      }else if(evt.getY()>intCenterY){
        thePanel.int2Ship3Y += 10; 
      }
      if(evt.getX()<intCenterX){
        thePanel.int2Ship3X -= 10;
      }else if(evt.getX()>intCenterX){
        thePanel.int2Ship3X += 10; 
      }
    }else if(intDrag==3){
      // Ship 2
      int intCenterX = thePanel.intShip2X+50; 
      int intCenterY = thePanel.intShip2Y+25; 
      if(evt.getY()<intCenterY){
        thePanel.intShip2Y -= 10;
      }else if(evt.getY()>intCenterY){
        thePanel.intShip2Y += 10; 
      }
      if(evt.getX()<intCenterX){
        thePanel.intShip2X -= 10;
      }else if(evt.getX()>intCenterX){
        thePanel.intShip2X += 10; 
      }
    }else if(intDrag==4){
      // Ship 4
      int intCenterX = thePanel.intShip4X+85; 
      int intCenterY = thePanel.intShip4Y+25; 
      if(evt.getY()<intCenterY){
        thePanel.intShip4Y -= 10;
      }else if(evt.getY()>intCenterY){
        thePanel.intShip4Y += 10; 
      }
      if(evt.getX()<intCenterX){
        thePanel.intShip4X -= 10;
      }else if(evt.getX()>intCenterX){
        thePanel.intShip4X += 10; 
      }
    }else if(intDrag==5){
      // Ship 5
      int intCenterX = thePanel.intShip5X+108; 
      int intCenterY = thePanel.intShip5Y+25; 
      if(evt.getY()<intCenterY){
        thePanel.intShip5Y -= 10;
      }else if(evt.getY()>intCenterY){
        thePanel.intShip5Y += 10; 
      }
      if(evt.getX()<intCenterX){
        thePanel.intShip5X -= 10;
      }else if(evt.getX()>intCenterX){
        thePanel.intShip5X += 10; 
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
    }
    // 2nd Ship 3
    if(evt.getX()>=thePanel.int2Ship3X && evt.getX()<=thePanel.int2Ship3X+130 && evt.getY()>=thePanel.int2Ship3Y && evt.getY()<=thePanel.int2Ship3Y+50){
      intDrag = 2;
    }
    // Ship 2
    if(evt.getX()>=thePanel.intShip2X && evt.getX()<=thePanel.intShip2X+100 && evt.getY()>=thePanel.intShip2Y && evt.getY()<=thePanel.intShip2Y+50){
      intDrag = 3;
    }
    // Ship 4
    if(evt.getX()>=thePanel.intShip4X && evt.getX()<=thePanel.intShip4X+170 && evt.getY()>=thePanel.intShip4Y && evt.getY()<=thePanel.intShip4Y+50){
      intDrag = 4;
    }
    // Ship 5
    if(evt.getX()>=thePanel.intShip5X && evt.getX()<=thePanel.intShip5X+215 && evt.getY()>=thePanel.intShip5Y && evt.getY()<=thePanel.intShip5Y+50){
      intDrag = 5;
    }

  }
  @Override
  public void mouseReleased(MouseEvent evt) {
    intDrag = 0;
  }
  @Override
  public void mouseEntered(MouseEvent evt) {
  }
  @Override
  public void mouseExited(MouseEvent evt) {
  }

  public void mainMenu(){
    JLabel theTitle = new JLabel("Battleships");
    playButton = new JButton("Play");

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
    
    playerGrid = new BattleGrid(141, 125, 40);
    thePanel.add(playerGrid);
    enemyGrid = new BattleGrid(766, 125, 40);
    thePanel.add(enemyGrid);

    theFrame.setJMenuBar(theMenuBar);
    theMenuBar.add(theMenu);

    theMenu.add(instructions);
    theMenu.add(about);
    
    theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    theFrame.pack(); 
    theFrame.setVisible(true); 
  }

  // Main Method
  public static void main(String[] args){
    new mainProgram();
  }

}


  
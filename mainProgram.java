//Battleship Game
//version 0.0
//December 18 2025

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;


public class mainProgram implements ActionListener, MouseListener{
  
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
  public void mouseClicked(MouseEvent e) {
  }
  @Override
  public void mousePressed(MouseEvent e) {
  }
  @Override
  public void mouseReleased(MouseEvent e) {
  }
  @Override
  public void mouseEntered(MouseEvent e) {
  }
  @Override
  public void mouseExited(MouseEvent e) {
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


  
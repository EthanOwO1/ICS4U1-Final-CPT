import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;

public class chatPanel extends JPanel implements ActionListener, MouseListener{
  // Properties
  JTextField theField = new JTextField(); 
  JTextArea theArea = new JTextArea();
  JScrollPane theScroll = new JScrollPane(theArea);
  JButton butClient = new JButton("Client Mode");
  JButton butServer = new JButton("Server mode");
  JButton butConnect = new JButton("Connect");
  SuperSocketMaster ssm = null;
  String strName = "Player1";
  PrintWriter chatlog;
  mainProgram main;

  

  // Methods

  public void actionPerformed(ActionEvent evt){
    if(evt.getSource() == theField){
      // The format of the chat should follow the following: "PlayerName, XCoordinate, YCoordinate, Action" - Game Messages
      // The format of the chat should follow the following: "PlayerName: Message" - Regular Messages
      System.out.println("Text Field Action");
      String strFormatted = strName + ": " + theField.getText();
      ssm.sendText(strFormatted);
      theArea.append(strFormatted + "\n");
      theField.setText("");

    } else if(evt.getSource() == butClient){
      System.out.println("Client Button Activated");
      String strIP = JOptionPane.showInputDialog(this, "Enter Server IP Address:");
      if(strIP != null){
        ssm = new SuperSocketMaster(strIP, 6112, this);
        strName = "Player2";
        butClient.setEnabled(false);
        butServer.setEnabled(false);
      }

    } else if(evt.getSource() == butServer){
      System.out.println("Server Button Action");
      ssm = new SuperSocketMaster(6112, this);
      strName = "Player1";
      butClient.setEnabled(false);
      butServer.setEnabled(false);

    } else if(evt.getSource() == butConnect){
      System.out.println("Connect Button Action"); 
      
      if(ssm.connect()){
        theArea.append("Connection Successful\n");
        butConnect.setEnabled(false);
        theField.setText("");
        if(main != null){
          main.startGame();
        }
      } else {
        theArea.append("Connection Failed\n");
      }

    } else if(evt.getSource() == ssm){
      String strLine = ssm.readText();
      theArea.append(strLine + "\n");
      
    }
  }

  // Constructors
  public chatPanel(mainProgram main){
    this.main = main;
    setPreferredSize(new Dimension(1280,720));
    setLayout(null);

    theScroll.setSize(200,600);
    theScroll.setLocation(1080,0);

    theField.setSize(200,100);
    theField.setLocation(1080,600);
    theField.addActionListener(this);

    butClient.setSize(300,100);
    butClient.setLocation(500,200);
    butClient.addActionListener(this);
    add(butClient);

    butServer.setSize(300,100);
    butServer.setLocation(500,300);
    butServer.addActionListener(this);
    add(butServer);

    butConnect.setSize(300,100);
    butConnect.setLocation(500,400);
    butConnect.addActionListener(this);
    add(butConnect);

    addMouseListener(this);

    try {
      chatlog = new PrintWriter(new FileWriter("chatlog.txt"));
    } catch (IOException e) {
      
    }

  }

  // Main Method
  public static void main(String[] args){
    new chatPanel(null);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    int intMouseX = e.getX();
    int intMouseY = e.getY();
    String strFormatted = strName + ", " + intMouseX + ", " + intMouseY + ", Click";
    ssm.sendText(strFormatted);
    theArea.append(strFormatted + "\n");
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
}

  

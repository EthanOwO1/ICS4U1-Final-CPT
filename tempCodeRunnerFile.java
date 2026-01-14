import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;

public class chat implements ActionListener, MouseListener{
  // Properties
  JFrame theframe = new JFrame("Chat");
  JPanel thepanel = new JPanel();
  JTextField theField = new JTextField(); 
  JTextArea theArea = new JTextArea();
  JScrollPane theScroll = new JScrollPane(theArea);
  JButton butClient = new JButton("Client Mode");
  JButton butServer = new JButton("Server mode");
  JButton butConnect = new JButton("Connect");
  SuperSocketMaster ssm = null;
  String strName = "Player1";
  PrintWriter chatlog;

  

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
      ssm = new SuperSocketMaster(theField.getText(), 6112, this);
      strName = "Player2";
      theField.setText("");
      butClient.setEnabled(false);
      butServer.setEnabled(false);

    } else if(evt.getSource() == butServer){
      System.out.println("Server Button Action");
      ssm = new SuperSocketMaster(6112, this);
      strName = "Player1";
      butClient.setEnabled(false);
      butServer.setEnabled(false);

    } else if(evt.getSource() == butConnect){
      System.out.println("Connect Button Action"); 
      strName = "Player2";     
      
      if(ssm.connect()){
        theArea.append("Connection Successful\n");
        butConnect.setEnabled(false);
        theField.setText("");
      } else {
        theArea.append("Connection Failed\n");
      }

    } else if(evt.getSource() == ssm){
      String strLine = ssm.readText();
      theArea.append(strLine + "\n");
      
    }
  }

  // Constructors
  public chat(){
    thepanel.setPreferredSize(new Dimension(1280,720));
    thepanel.setLayout(null);

    theScroll.setSize(300,600);
    theScroll.setLocation(980,0);
    thepanel.add(theScroll);

    theField.setSize(300,100);
    theField.setLocation(980,600);
    theField.addActionListener(this);
    thepanel.add(theField);

    butClient.setSize(300,100);
    butClient.setLocation(0,300);
    butClient.addActionListener(this);
    thepanel.add(butClient);

    butServer.setSize(300,100);
    butServer.setLocation(0,400);
    butServer.addActionListener(this);
    thepanel.add(butServer);

    butConnect.setSize(300,100);
    butConnect.setLocation(0,500);
    butConnect.addActionListener(this);
    thepanel.add(butConnect);

    thepanel.addMouseListener(this);

    theframe.setContentPane(thepanel);
    theframe.pack();
    theframe.setVisible(true);

    try {
      chatlog = new PrintWriter(new FileWriter("chatlog.txt"));
    } catch (IOException e) {
      
    }

  }

  // Main Method
  public static void main(String[] args){
    new chat();
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
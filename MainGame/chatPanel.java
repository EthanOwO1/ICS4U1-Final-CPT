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
  JLabel ipLabel = new JLabel("Server IP:");
  JTextField ipField = new JTextField();
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
      butClient.setVisible(false);
      butServer.setVisible(false);
      ipLabel.setVisible(true);
      ipField.setVisible(true);
      strName = "Player2";
      main.myTurn = false; // Client goes second

    } else if(evt.getSource() == butServer){
      System.out.println("Server Button Action");
      butClient.setVisible(false);
      butServer.setVisible(false);
      strName = "Player1";
      main.myTurn = true; // Server goes first

    } else if(evt.getSource() == butConnect){
      System.out.println("Connect Button Action"); 
      
      if (ssm == null) {
        if (strName.equals("Player1")) { // Server
            ssm = new SuperSocketMaster(6112, this);
        } else { // Client
            String strIP = ipField.getText();
            if (strIP == null || strIP.trim().isEmpty()) {
                theArea.append("Please enter a Server IP Address.\n");
                return;
            }
            ssm = new SuperSocketMaster(strIP, 6112, this);
        }
      }

      if(ssm.connect()){
        theArea.append("Connection Successful\n");
        butConnect.setEnabled(false);
        ipField.setEditable(false);
        theField.setText("");
        if(strName.equals("Player2")){
          // Client sends handshake and starts game
          Timer timer = new Timer(500, new ActionListener(){
            public void actionPerformed(ActionEvent evt){
              ssm.sendText("PAIRED SUCCESSFULLY");
            }
          });
          timer.setRepeats(false);
          timer.start();
          main.startGame();
        } else {
          // Server waits for client to join
          theArea.append("Waiting for client to join...\n");
        }
      } else {
        theArea.append("Connection Failed\n");
        ssm = null; // Allow user to try again
      }

    } else if(evt.getSource() == ssm){
      String strLine = ssm.readText();
      if(strLine.startsWith("PAIRED SUCCESSFULLY")){
        theArea.append("Client connected. Starting game...\n");
        main.startGame();

      }else if(strLine.startsWith("SHIPS")){
        main.setEnemyShips(strLine);
        
      }else if(strLine.startsWith("SHOT")){
        main.receiveShot(strLine);
        
      }else{
        theArea.append(strLine + "\n");
      }
    }
  }

  // Constructors
  public chatPanel(mainProgram main){
    this.main = main;
    setPreferredSize(new Dimension(1280,720));
    setLayout(null);

    theScroll.setSize(200,600);
    theScroll.setLocation(1080,0);
    theArea.setEditable(false);
    theArea.setLineWrap(true);
    theArea.setWrapStyleWord(true);

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

    ipLabel.setBounds(400, 300, 100, 30);
    ipLabel.setVisible(false);
    add(ipLabel);

    ipField.setBounds(500, 300, 300, 30);
    ipField.setVisible(false);
    add(ipField);

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

  

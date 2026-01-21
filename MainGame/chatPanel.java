import java.awt.*;
import java.io.*;
import javax.swing.*;

import java.awt.event.*;

/**
 * Handles connection setup and in-game chat<p>
 * Allows server/client selection and connection<p>
 * Displays chat and network status
 * @author Ethan, Carsten, Brandon
 * @version 1.0
 */
public class chatPanel extends JPanel implements ActionListener, MouseListener{
  // Properties
  /** Chat input field */
  JTextField theField = new JTextField(); 
  /** Chat log display */
  JTextArea theArea = new JTextArea();
  /** Scroll pane for chat */
  JScrollPane theScroll = new JScrollPane(theArea);
  /** Client mode button */
  JButton clientButton = new JButton("Client Mode");
  /** Server mode button */
  JButton serverButton = new JButton("Server mode");
  /** Connect button */
  JButton connectButton = new JButton("Connect");
  /** IP address label */
  JLabel ipLabel = new JLabel("Server IP:");
  /** IP address input */
  JTextField ipField = new JTextField();
  /** Network handler */
  SuperSocketMaster ssm = null;
  /** Player name */
  String strName = "Player1";
  /** Main program reference */
  mainProgram main;

  // Methods
  /**
   * Handles UI and network events<p>
   * Sends chat, sets mode, processes messages
   * @param evt ActionEvent
   */
  public void actionPerformed(ActionEvent evt){
    if(evt.getSource() == theField){
      // User pressed Enter in the chat field
      // System Messages - "SYSTEM: Message"
      // Launching Missiles - "SYSTEM: PlayerName, Coordinate, Hit/Miss"
      // Regular Messages - "PlayerName: Message" 
      System.out.println("SYSTEM: Text Field Action");
      String strFormatted = strName + ": " + theField.getText();
      ssm.sendText(strFormatted);
      theArea.append(strFormatted + "\n");
      theField.setText("");

    } else if(evt.getSource() == clientButton){
      // Switch UI to Client mode (show IP field)
      System.out.println("SYSTEM: Client Button Activated");
      clientButton.setVisible(false);
      serverButton.setVisible(false);
      ipLabel.setVisible(true);
      ipField.setVisible(true);
      strName = "Player2";
      main.blnTurn = false; // Client goes second

    } else if(evt.getSource() == serverButton){
      // Switch UI to Server mode
      System.out.println("SYSTEM: Server Button Action");
      clientButton.setVisible(false);
      serverButton.setVisible(false);
      strName = "Player1";
      main.blnTurn = true; // Server goes first

    } else if(evt.getSource() == connectButton){
      // Attempt to establish network connection
      System.out.println("SYSTEM: Connect Button Action"); 
      
      if (ssm == null) {
        if (strName.equals("Player1")) { // Server
            ssm = new SuperSocketMaster(6112, this);
        } else { // Client
            String strIP = ipField.getText();
            if (strIP == null || strIP.trim().isEmpty()) {
                theArea.append("SYSTEM: Please enter a Server IP Address.\n");
                return;
            }
            ssm = new SuperSocketMaster(strIP, 6112, this);
        }
      }

      if(ssm.connect()){
        theArea.append("SYSTEM: Connection Successful\n");
        connectButton.setEnabled(false);
        ipField.setEditable(false);
        theField.setText("");
        if(strName.equals("Player2")){
          // Client sends handshake and starts game
          Timer timer = new Timer(500, new ActionListener(){
            public void actionPerformed(ActionEvent evt){
              ssm.sendText("SYSTEM: PAIRED SUCCESSFULLY");
            }
          });
          timer.setRepeats(false);
          timer.start();
          main.startGame();
        } else {
          // Server waits for client to join
          theArea.append("SYSTEM: Waiting for client to join...\n");
        }
      } else {
        theArea.append("SYSTEM: Connection Failed\n");
        ssm = null; // Allow user to try again
      }

    } else if(evt.getSource() == ssm){
      // Handle incoming network messages
      String strLine = ssm.readText();
      if(strLine.startsWith("SYSTEM: PAIRED SUCCESSFULLY")){
        theArea.append("SYSTEM: Client connected. Starting game...\n");
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

  /**
   * Handles mouse clicks<p>
   * Sends coordinates over network
   * @param e MouseEvent
   */
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

  // Constructors
  /**
   * Constructor for chat panel
   * @param main Main program reference
   */
  public chatPanel(mainProgram main){
    this.main = main;
    setPreferredSize(new Dimension(1280,720));
    setLayout(null);

    // Chat Panel UI setup

    theScroll.setSize(200,600);
    theScroll.setLocation(1080,0);
    theArea.setEditable(false);
    theArea.setLineWrap(true);
    theArea.setWrapStyleWord(true);

    theField.setSize(200,100);
    theField.setLocation(1080,600);
    theField.addActionListener(this);

    // Connect UI setup
    clientButton.setSize(300,100);
    clientButton.setLocation(500,200);
    clientButton.addActionListener(this);
    add(clientButton);

    serverButton.setSize(300,100);
    serverButton.setLocation(500,300);
    serverButton.addActionListener(this);
    add(serverButton);

    connectButton.setSize(300,100);
    connectButton.setLocation(500,400);
    connectButton.addActionListener(this);
    add(connectButton);

    ipLabel.setBounds(400, 300, 100, 30);
    ipLabel.setVisible(false);
    add(ipLabel);

    ipField.setBounds(500, 300, 300, 30);
    ipField.setVisible(false);
    add(ipField);

    addMouseListener(this);

  }

  /**
   * Main method for testing
   * @param args Command line arguments
   */
  public static void main(String[] args){
    new chatPanel(null);
  }
}

  

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class chat implements ActionListener{
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

  // Methods

  public void actionPerformed(ActionEvent evt){
    if(evt.getSource() == theField){
      System.out.println("Text Field Action");
      String strLine = theField.getText();
      ssm.sendText(strLine);
      theArea.append(strLine + "\n");

    } else if(evt.getSource() == butClient){
      System.out.println("Client Button Activated");
      ssm = new SuperSocketMaster(theField.getText(), 6112, this);
      theField.setText("");
      butClient.setEnabled(false);
      butServer.setEnabled(false);

    } else if(evt.getSource() == butServer){
      System.out.println("Server Button Action");
      ssm = new SuperSocketMaster(6112, this);
      butClient.setEnabled(false);
      butServer.setEnabled(false);

    } else if(evt.getSource() == butConnect){
      System.out.println("Connect Button Action");      
      ssm.connect();

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

    theframe.setContentPane(thepanel);
    theframe.pack();
    theframe.setVisible(true);
  }

  // Main Method
  public static void main(String[] args){
    new chat();
  }
}
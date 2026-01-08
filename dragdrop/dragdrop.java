package dragdrop;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class dragdrop implements ActionListener,MouseListener,MouseMotionListener{
    // Properties
    JFrame theFrame = new JFrame("Drag Drop Test"); 
    JAPanel thePanel = new JAPanel(); 
    boolean blnDrag = false;

    // Methods
    @Override
    public void actionPerformed(ActionEvent evt) {
    }

    @Override
    public void mouseDragged(MouseEvent evt) {
        int intCenterY = thePanel.intOy+25; 
        int intCenterX = thePanel.intOx+50; 
        if(blnDrag == true){
            thePanel.repaint(); 
            if(evt.getY()<intCenterY){
                thePanel.intOy = thePanel.intOy-5; 
            }else if(evt.getY()>intCenterY){
                thePanel.intOy = thePanel.intOy+5; 
            }
            if(evt.getX()<intCenterX){
                thePanel.intOx = thePanel.intOx-5; 
            }else if(evt.getX()>intCenterX){
                thePanel.intOx = thePanel.intOx+5; 
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
        if(evt.getX()>=thePanel.intOx && evt.getX()<=thePanel.intOx+100 && evt.getY()>=thePanel.intOy && evt.getY()<=thePanel.intOy+50){
            blnDrag = true;
        }
    }
    @Override
    public void mouseReleased(MouseEvent evt) {
        blnDrag = false;
    }
    @Override
    public void mouseEntered(MouseEvent evt) {
    }
    @Override
    public void mouseExited(MouseEvent evt) {
    }

    // Constructor
    public dragdrop(){
        thePanel.setPreferredSize(new Dimension(1280,720));
        thePanel.addMouseMotionListener(this);
        thePanel.addMouseListener(this);
        theFrame.setContentPane(thePanel);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.pack();
        theFrame.setVisible(true); 
    }

    // Main Method
    public static void main(String args[]){
        new dragdrop();
    }

}



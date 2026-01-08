package dragdrop;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class JAPanel extends JPanel{
    // Methods
    int intOx = 0;
    int intOy = 0; 
    
    // Properties
    public void paintComponent(Graphics g){ 
        super.paintComponent(g);
        g.setColor(Color.RED); 
        g.fillOval(intOx,intOy,100,50); 
    }

    // Constructor
    public JAPanel(){
        super(); 
    }

}

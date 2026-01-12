import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class animation extends JPanel{
    // Properties
    BufferedImage battleImage; 
    BufferedImage oneShip3Image; 
    BufferedImage twoShip3Image; 
    BufferedImage ship2Image; 
    BufferedImage ship4Image; 
    BufferedImage ship5Image; 

    // Methods
    public void paintComponent(Graphics g){ 
        super.paintComponent(g);
        g.drawImage(battleImage,0,0,null); 
        g.drawImage(oneShip3Image,150,550,null);
        g.drawImage(twoShip3Image,290,550,null);
        g.drawImage(ship2Image,430,550,null);
        g.drawImage(ship4Image,150,600,null);
        g.drawImage(ship5Image,330,600,null);

    }

    // Constructors
    public animation(){
        super(); 
        InputStream battleStream = this.getClass().getResourceAsStream("battleship grid.png"); 
        InputStream oneShip3Stream = this.getClass().getResourceAsStream("1ship3.png"); 
        InputStream twoShip3Stream = this.getClass().getResourceAsStream("2ship3.png"); 
        InputStream ship2Stream = this.getClass().getResourceAsStream("ship2.png"); 
        InputStream ship4Stream = this.getClass().getResourceAsStream("ship4.png"); 
        InputStream ship5Stream = this.getClass().getResourceAsStream("ship5.png"); 
        try{
            battleImage = ImageIO.read(battleStream);
            oneShip3Image = ImageIO.read(oneShip3Stream);
            twoShip3Image = ImageIO.read(twoShip3Stream); 
            ship2Image = ImageIO.read(ship2Stream);
            ship4Image = ImageIO.read(ship4Stream); 
            ship5Image = ImageIO.read(ship5Stream);
        }catch(IOException e){ 
            System.out.println("Unable to load image");
        }
    }

}

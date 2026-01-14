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
    int int1Ship3X = 150; 
    int int1Ship3Y = 550; 
    int int2Ship3X = 290;
    int int2Ship3Y = 550; 
    int intShip2X = 430; 
    int intShip2Y = 550; 
    int intShip4X = 150; 
    int intShip4Y = 600; 
    int intShip5X = 330; 
    int intShip5Y = 600; 

    // Methods
    public void paintComponent(Graphics g){ 
        super.paintComponent(g);
        g.drawImage(battleImage,0,0,null); 
        g.drawImage(oneShip3Image,int1Ship3X,int1Ship3Y,null);
        g.drawImage(twoShip3Image,int2Ship3X,int2Ship3Y,null);
        g.drawImage(ship2Image,intShip2X,intShip2Y,null);
        g.drawImage(ship4Image,intShip4X,intShip4Y,null);
        g.drawImage(ship5Image,intShip5X,intShip5Y,null);

    }

    // Constructors
    public animation(){
        super(); 
        InputStream battleStream = this.getClass().getResourceAsStream("graphics/battleship grid.png"); 
        InputStream oneShip3Stream = this.getClass().getResourceAsStream("graphics/1ship3.png"); 
        InputStream twoShip3Stream = this.getClass().getResourceAsStream("graphics/2ship3.png"); 
        InputStream ship2Stream = this.getClass().getResourceAsStream("graphics/ship2.png"); 
        InputStream ship4Stream = this.getClass().getResourceAsStream("graphics/ship4.png"); 
        InputStream ship5Stream = this.getClass().getResourceAsStream("graphics/ship5.png"); 
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

import java.awt.*;
import java.awt.geom.AffineTransform;

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
    BufferedImage hitImage;
    BufferedImage missImage;
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
    double dbl1Ship3rot = 0; 
    double dbl2Ship3rot = 0;
    double dblShip2rot = 0;
    double dblShip4rot = 0;
    double dblShip5rot = 0;
    
    // Grid States: 0 = Empty, 1 = Hit (Red), 2 = Miss (White)
    int[][] playerGridState = new int[10][10];
    int[][] enemyGridState = new int[10][10];
    
    // Animation Scales (0.0 to 1.0)
    float[][] playerGridScales = new float[10][10];
    float[][] enemyGridScales = new float[10][10];

    // Methods
    public void paintComponent(Graphics g){ 
        super.paintComponent(g);
        g.drawImage(battleImage,0,0,null); 
        drawRotatedImage(g, oneShip3Image, int1Ship3X, int1Ship3Y, dbl1Ship3rot);
        drawRotatedImage(g, twoShip3Image, int2Ship3X, int2Ship3Y, dbl2Ship3rot);
        drawRotatedImage(g, ship2Image, intShip2X, intShip2Y, dblShip2rot);
        drawRotatedImage(g, ship4Image, intShip4X, intShip4Y, dblShip4rot);
        drawRotatedImage(g, ship5Image, intShip5X, intShip5Y, dblShip5rot);
        
        // Draw Shots on Player Grid (141, 125)
        drawGridShots(g, playerGridState, playerGridScales, 141, 125);
        
        // Draw Shots on Enemy Grid (625, 125)
        drawGridShots(g, enemyGridState, enemyGridScales, 625, 125);
    }
    
    public void updateAnimations(){
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                // If there is a hit/miss, grow the scale until it reaches 1.0
                if(playerGridState[i][j] != 0 && playerGridScales[i][j] < 1.0f){
                    playerGridScales[i][j] += 0.1f; // Speed of animation
                    if(playerGridScales[i][j] > 1.0f) playerGridScales[i][j] = 1.0f;
                }
                if(enemyGridState[i][j] != 0 && enemyGridScales[i][j] < 1.0f){
                    enemyGridScales[i][j] += 0.1f; // Speed of animation
                    if(enemyGridScales[i][j] > 1.0f) enemyGridScales[i][j] = 1.0f;
                }
            }
        }
    }
    
    private void drawGridShots(Graphics g, int[][] gridState, float[][] gridScales, int startX, int startY){
        int cellSize = 40;
        for(int row = 0; row < 10; row++){
            for(int col = 0; col < 10; col++){
                if(gridState[row][col] != 0){
                    int x = startX + col * cellSize;
                    int y = startY + row * cellSize;
                    
                    // Calculate animated size
                    float scale = gridScales[row][col];
                    int size = (int)(36 * scale); // Max size 36px
                    int offset = (40 - size) / 2; // Center the image
                    
                    if(gridState[row][col] == 1){
                        g.drawImage(hitImage, x + offset, y + offset, size, size, null); // Red for Hit
                    }else{
                        g.drawImage(missImage, x + offset, y + offset, size, size, null); // White for Miss
                    }
                }
            }
        }
    }

private void drawRotatedImage(Graphics g, BufferedImage image, int intX, int intY, double dblRot){
    if(image != null){
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform(); 
        
        // Determines if ship is vertical
        boolean isVertical = Math.round(dblRot / (Math.PI / 2)) % 2 != 0;

        double dblCenterX;
        double dblCenterY;

        if (isVertical) {
            // If vertical, the hitbox is Height x Width
            dblCenterX = intX + image.getHeight() / 2.0;
            dblCenterY = intY + image.getWidth() / 2.0;
        } else {
            // If horizontal, the hitbox is Width x Height
            dblCenterX = intX + image.getWidth() / 2.0;
            dblCenterY = intY + image.getHeight() / 2.0;
        }

        g2d.translate(dblCenterX, dblCenterY);
        g2d.rotate(dblRot);
        g2d.drawImage(image, -image.getWidth() / 2, -image.getHeight() / 2, null);

        g2d.setTransform(originalTransform); 
    }
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
        InputStream missStream = this.getClass().getResourceAsStream("graphics/miss.png"); 
        InputStream hitStream = this.getClass().getResourceAsStream("graphics/explosion.png");
        try{
            battleImage = ImageIO.read(battleStream);
            oneShip3Image = ImageIO.read(oneShip3Stream);
            twoShip3Image = ImageIO.read(twoShip3Stream); 
            ship2Image = ImageIO.read(ship2Stream);
            ship4Image = ImageIO.read(ship4Stream); 
            ship5Image = ImageIO.read(ship5Stream);
            missImage = ImageIO.read(missStream);
            hitImage = ImageIO.read(hitStream);
        }catch(IOException e){ 
            System.out.println("Unable to load image");
        }
    }

}

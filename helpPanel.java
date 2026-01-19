import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class helpPanel extends JPanel {

    JButton butReturn;

    public helpPanel() {

        setLayout(null);

        JLabel title = new JLabel("How to Play Battleship");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setBounds(450, 50, 500, 40);
        add(title);

        JTextArea instructions = new JTextArea(
            "Objective:\n" +
            "- Sink all enemy ships before they sink yours.\n\n" +
            "How to Play:\n" +
            "- Click on the enemy grid to fire shots.\n" +
            "- Red = hit, Blue = miss.\n" +
            "- Enemy will fire back after your turn.\n\n" +
            "Good luck, Commander!"
        );

        instructions.setFont(new Font("Arial", Font.PLAIN, 18));
        instructions.setBounds(300, 150, 700, 300);
        instructions.setEditable(false);
        instructions.setOpaque(false);
        add(instructions);

        butReturn = new JButton("Back to Menu");
        butReturn.setBounds(550, 500, 180, 40);
        add(butReturn);

        // Close the help screen and go back to main menu
        butReturn.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new JPanel()); // temporary reset
            frame.dispose();
            new mainProgram(); // reload menu
        });
    }
}
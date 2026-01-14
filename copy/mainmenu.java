package copy;

import javax.swing.*;
import java.awt.*;


//main menu
public class mainmenu extends JFrame {

    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);

    public mainmenu() {
        super("welcome to Battleship");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Add screens
        mainPanel.add(new PlayScreen(this), "play");
        mainPanel.add(new MainMenu(this), "menu");
        mainPanel.add(new HelpScreen(this), "help");

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void showScreen(String name) {
        cardLayout.show(mainPanel, name);
    }

    public static void main(String[] args) {
        new mainmenu();
    }
}

class MainMenu extends JPanel {

    public MainMenu(mainmenu app) {

        setPreferredSize(new Dimension(800, 500));
        setLayout(null);
        setBackground(new Color(220, 240, 255));

        JLabel title = new JLabel("Welcome to Battleship", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setBounds(150, 40, 500, 50);
        add(title);

        JButton play = new JButton("Play");
        play.setBounds(280, 200, 240, 40);
        play.addActionListener(e -> app.showScreen("play"));
        add(play);

        JButton help = new JButton("Help");
        help.setBounds(280, 270, 240, 40);
        help.addActionListener(e -> app.showScreen("help"));
        add(help);

        JButton quit = new JButton("Quit");
        quit.setBounds(280, 340, 240, 40);
        quit.addActionListener(e -> System.exit(0));
        add(quit);
    }

}

class HelpScreen extends JPanel {

    public HelpScreen(mainmenu app) {
        setPreferredSize(new Dimension(800, 500));
        setLayout(null);
        setBackground(Color.WHITE);

        JLabel title = new JLabel("How to play", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBounds(100, 20, 600, 50);
        add(title);

        JTextArea info = new JTextArea();
        info.setEditable(false);
        info.setFont(new Font("Arial", Font.PLAIN, 18));
        info.setText(
            "How to play battleship:\n\n" +
            "• Place your ships on the grid.\n" +
            "• Take turns with your opponent to guess where their ships are.\n" +
            "• When you hit a ship, it will be marked on the grid.\n" +
            "• The first player to sink all of their opponent's ships wins!\n\n" +
            "Try the examples to see it in action!"
        );
        info.setBounds(150, 100, 500, 280);
        add(info);

        JButton back = new JButton("Back");
        back.setBounds(350, 400, 100, 35);
        back.addActionListener(e -> app.showScreen("menu"));
        add(back);
    }
}
class PlayScreen extends JPanel {

    public PlayScreen(mainmenu app) {
        setPreferredSize(new Dimension(800, 500));
        setLayout(null);
        setBackground(new Color(200, 220, 255));

        JLabel title = new JLabel("Game Screen", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBounds(100, 20, 600, 50);
        add(title);

        JButton back = new JButton("Back");
        back.setBounds(350, 400, 100, 35);
        back.addActionListener(e -> app.showScreen("menu"));
        add(back);
    }
}

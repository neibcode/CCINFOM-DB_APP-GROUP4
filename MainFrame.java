//Assigned To Balagtas

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame{
    final Dimension appDimension = new Dimension(1000, 700);
    
    private CardLayout cardLayout;
    private JPanel cards;
    public static void main(String[] args){
        new MainFrame();
    }
    public MainFrame(){
        //Window Settings
        this.setTitle("MOIST - Group 5");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(appDimension);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.green);

        //Card Layout
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        cards.add(new WelcomeScreen(), "WELCOME");
        cards.add(new HomeScreen(), "HOME");

        add(cards);
        cardLayout.show(cards, "HOME");
        this.setVisible(true);
    }
}

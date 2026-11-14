package app;

import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameResultsPanel extends JPanel{
    public GameResultsPanel(){
        setBackground(Color.red);
        JLabel placeholder = new JLabel("Game");
        setLayout(new GridBagLayout());
        add(placeholder);

        setVisible(true);
    }
}

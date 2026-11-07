import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

public class WelcomeScreen extends JPanel{

    public WelcomeScreen(){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weighty = 1;

        //Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.orange);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.6;
        add(titlePanel, gbc);

        
        //Welcome Panel
        JPanel welcomePanel = new JPanel();
        welcomePanel.setBackground(Color.white);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.4;
        add(welcomePanel, gbc);
    }
}

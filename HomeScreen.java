import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

public class HomeScreen extends JPanel{

    public HomeScreen(){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;

        //Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(Color.orange);
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.1;
        add(searchPanel, gbc);

        
        //Results Panel (Do CardLayout)
        JPanel resultsPanel = new JPanel();
        resultsPanel.setBackground(Color.white);
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.9;
        add(resultsPanel, gbc);
    }
}

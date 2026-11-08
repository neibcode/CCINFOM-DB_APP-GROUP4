import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class HomeScreen extends JPanel{

    private CardLayout cardLayout;
    private JPanel cards;

    public HomeScreen(){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;

        //Search Panel
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.1;
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(Color.orange);
        searchPanel.add(new searchPanelObjects());
        add(searchPanel, gbc);

        
        //Results Panel (Do CardLayout)
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.9;
        JPanel resultsPanel = new JPanel();
        resultsPanel.setBackground(Color.white);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        cards.add(new GameResultsPanel(), "GameResults");
        cards.add(new UserResultsPanel(), "UserResults");
        resultsPanel.add(cards);
        cardLayout.show(cards, "UserResults");

        this.setVisible(true);
        add(resultsPanel, gbc);
    }

    private class searchPanelObjects extends JPanel{
        private searchPanelObjects(){
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            
            JLabel icon = new JLabel("MOIST");
            gbc.gridx = 0;
            add(icon, gbc);

            JTextField searchField = new JTextField(30);
            gbc.gridx = 1;
            add(searchField, gbc);

            JButton gameReportsButton = new JButton("🏆");
            gbc.gridx = 2;
            add(gameReportsButton, gbc);

            String[] filters = {"in Games", "in Users", "in Something"};
            JComboBox<String> searchFilter = new JComboBox<>(filters);
            gbc.gridx = 3;
            add(searchFilter, gbc);

            JButton profileButton = new JButton("😁");
            gbc.gridx = 4;
            add(profileButton, gbc);
        }
    }
}

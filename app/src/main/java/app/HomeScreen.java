package app;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class HomeScreen extends JPanel{
    private CardLayout cardLayout;
    private JPanel cards;

    private SearchPanelObjects searchPanelObjects;

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
        searchPanelObjects = new SearchPanelObjects();
        searchPanel.add(searchPanelObjects);
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

    private static class SearchPanelObjects extends JPanel{
        private JTextField searchField;
        private JButton searchButton;
        private JButton gameReportsButton;
        private JButton profileButton;
        private JComboBox<String> searchFilter;
        
        private SearchPanelObjects(){
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            
            JLabel icon = new JLabel("MOIST");
            gbc.gridx = 0;
            add(icon, gbc);

            searchField = new JTextField(30);
            gbc.gridx = 1;
            add(searchField, gbc);

            searchButton = new JButton("🔍");
            searchButton.setActionCommand("SEARCH");
            gbc.gridx = 2;
            add(searchButton);

            gameReportsButton = new JButton("🏆");
            gameReportsButton.setActionCommand("REPORTS");
            gbc.gridx = 3;
            add(gameReportsButton, gbc);

            String[] filters = {"in Games", "in Users", "in Something"};
            searchFilter = new JComboBox<>(filters);
            gbc.gridx = 4;
            add(searchFilter, gbc);

            profileButton = new JButton("😁");
            gbc.gridx = 5;
            add(profileButton, gbc);
        }
    }

    public void addHomeScreenListener(ActionListener actionListener){
        searchPanelObjects.searchButton.addActionListener(actionListener);
        searchPanelObjects.gameReportsButton.addActionListener(actionListener);
        searchPanelObjects.profileButton.addActionListener(actionListener);
    }

    public String getSearch(){
        return new String(searchPanelObjects.searchField.getText());
    }

    public String getSearchFilter(){
        return (String) searchPanelObjects.searchFilter.getSelectedItem();
    }
}

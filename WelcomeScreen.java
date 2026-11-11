import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class WelcomeScreen extends JPanel {
    private WelcomePanel welcomePanel;

    public WelcomeScreen() {
        setBackground(Color.green);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        // Title Panel
        gbc.gridx = 0;
        gbc.weightx = 0.6;
        add(new titlePanel(), gbc);

        // Welcome Panel
        gbc.gridx = 1;
        gbc.weightx = 0.4;
        welcomePanel = new WelcomePanel();
        add(welcomePanel, gbc);
    }

    private static class WelcomePanel extends JPanel {
        private JButton logInButton;
        private JButton signUpButton;
        private JTextField usernameField;
        private JPasswordField passwordField;
        
        private WelcomePanel() {
            setBackground(Color.white);
            setLayout(new GridBagLayout());
            GridBagConstraints welcomeGbc = new GridBagConstraints();

            // Welcome Panel Items
            welcomeGbc.gridx = 0;
            welcomeGbc.gridy = 0;
            JLabel welcomeText = new JLabel("Welcome!");
            add(welcomeText, welcomeGbc);

            //Username Field
            welcomeGbc.gridy = 1;
            JLabel usernameLabel = new JLabel("Username ");
            usernameField = new JTextField(20);
            usernameField.setBackground(Color.lightGray);
            usernameField.setToolTipText("Enter username");
            
            welcomeGbc.gridx = 0;
            add(usernameLabel, welcomeGbc);
            welcomeGbc.gridx = 1;
            add(usernameField, welcomeGbc);
            
            //Password Field
            welcomeGbc.gridy = 2;
            JLabel passwordLabel = new JLabel("Password ");
            passwordField = new JPasswordField(20);
            passwordField.setBackground(Color.lightGray);
            passwordField.setToolTipText("Enter password");

            welcomeGbc.gridx = 0;
            add(passwordLabel, welcomeGbc);

            welcomeGbc.gridx = 1;
            add(passwordField, welcomeGbc);
            

            //Login Button
            welcomeGbc.gridy = 4;
            welcomeGbc.fill = GridBagConstraints.BOTH;
            logInButton = new JButton("LOG IN");
            logInButton.setBackground(Color.orange);
            logInButton.setActionCommand("LOGIN");
            add(logInButton, welcomeGbc);

            welcomeGbc.gridy = 5;
            welcomeGbc.fill = GridBagConstraints.BOTH;
            signUpButton = new JButton("SIGN UP");
            signUpButton.setBackground(Color.orange);
            signUpButton.setActionCommand("SIGNUP");
            add(signUpButton, welcomeGbc);
        }
    }

    private static class titlePanel extends  JPanel{
        private titlePanel(){
            setBackground(Color.orange);
            setLayout(new GridBagLayout());
            JLabel title = new JLabel("MOIST");
            add(title);
        }
    }

    public void addWelcomeListener(ActionListener actionListener){
        welcomePanel.signUpButton.addActionListener(actionListener);
        welcomePanel.logInButton.addActionListener(actionListener);
    }

    public String getUsername(){
        return new String(welcomePanel.usernameField.getText());
    }

    public String getPassword(){
        return new String(welcomePanel.passwordField.getPassword());
    }
}

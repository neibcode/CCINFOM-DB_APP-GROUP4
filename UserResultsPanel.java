import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class UserResultsPanel extends JPanel{
    public UserResultsPanel(){
        setBackground(Color.red);
        JLabel placeholder = new JLabel("User");
        setLayout(new GridBagLayout());
        add(placeholder);

        setVisible(true);
    }
}

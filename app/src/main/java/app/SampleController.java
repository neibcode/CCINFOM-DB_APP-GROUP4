package app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SampleController {
    private WelcomeScreen welcomeScreen;
    private HomeScreen homeScreen;
    private MainFrame gui;
    public static void main(String[] args){
        new SampleController();
    }

    public SampleController(){
        welcomeScreen = new WelcomeScreen();
        welcomeScreen.addWelcomeListener(new WelcomeScreenListener());
        homeScreen = new HomeScreen();
        homeScreen.addHomeScreenListener(new HomeScreenListener());
        gui = new MainFrame(welcomeScreen, homeScreen);

        gui.showWelcomeScreen();
    }

    class WelcomeScreenListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            switch (e.getActionCommand()) {
                case "LOGIN":
                    System.out.println("Logged in\nUsername: " + welcomeScreen.getUsername() + "\nPassword: " + welcomeScreen.getPassword());
                    gui.showHomeScreen();
                    break;
                case "SIGNUP":
                    System.out.println("Signed up\nUsername: " + welcomeScreen.getUsername() + "\nPassword: " + welcomeScreen.getPassword());
                    gui.showHomeScreen();
                    break;
                default:
                    break;
            }
        }
    }

    class HomeScreenListener implements ActionListener{
        public  void actionPerformed(ActionEvent e){
            switch (e.getActionCommand()) {
                case "SEARCH":
                    System.out.println("Search: " + homeScreen.getSearch() + " " + homeScreen.getSearchFilter());
                    break;
            
                default:
                    break;
            }
        }
    }
}

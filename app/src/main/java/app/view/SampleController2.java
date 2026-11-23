package app;

import app.view.WelcomeScreen;
import app.view.MainFrame;
import app.view.HomeScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SampleController2 {
    private WelcomeScreen welcomeScreen;
    private HomeScreen homeScreen;
    private MainFrame gui;
    private app.view.MainWindow MainWindow;
    public static void main(String[] args){
        new SampleController2();
    }

    public SampleController2(){
        /*
        welcomeScreen = new WelcomeScreen();
        welcomeScreen.addWelcomeListener(new WelcomeScreenListener());
        homeScreen = new HomeScreen();
        homeScreen.addHomeScreenListener(new HomeScreenListener());
        gui = new MainFrame(welcomeScreen, homeScreen);

        gui.showWelcomeScreen();
        */
        
        MainWindow = new app.view.MainWindow();
        MainWindow.setVisible(true);
        MainWindow.addActionListener(new MainFrameListener());
        
        MainWindow.addGameRecord("Streetfighter", "Capcom", "Jan 9", 4.5f);
        MainWindow.addUserRecord("JustinWongg", "Oct 9");
        MainWindow.addProfileReviewRecord("Streetfighter", "Super cool", "Jan 10", 4.5f);
        MainWindow.addRecordRecord("Streetfighter", "Capcom", "Jan 9", 4.5f);
    }

    class MainFrameListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            switch (e.getActionCommand()) {
                case "LOGIN":
                    System.out.println("Logged in\nUsername: " + MainWindow.getUsername() + "\nPassword: " + MainWindow.getPassword());
                    MainWindow.changeWindowScreen("home");
                    break;
                case "SIGNUP":
                    System.out.println("Signed up\nUsername: " + MainWindow.getUsername() + "\nPassword: " + MainWindow.getPassword());
                    MainWindow.changeWindowScreen("home");
                    break;
                case "search":
                    String searchFilter = MainWindow.getSearchFilter();
                    switch (searchFilter){
                        case "in Games":
                            System.out.println("Searching for \"" + MainWindow.getSearchField() + "\" " + searchFilter);
                            MainWindow.changePanelScreen("gameResults");
                           break;
                        case "in Users":
                            System.out.println("Searching for \"" + MainWindow.getSearchField() + "\" " + searchFilter);
                            MainWindow.changePanelScreen("userResults");
                            break;
                        default:
                            break;
                    }
                    break;
                case "GameResultsFilterChanged":
                    System.out.println(MainWindow.getGameRecordFilter());
                    break;
                case "UserResultsFilterChanged":
                    System.out.println(MainWindow.getUserRecordFilter());
                    break;
                case "records":
                    System.out.println("Records");
                    MainWindow.changePanelScreen("recordResults");
                    break;
                case "profile":
                    System.out.println("Profile");
                    MainWindow.changePanelScreen("profile");
                    break;
                case "Streetfighter":
                    System.out.println("Opened Streetfighter");
                    MainWindow.changePanelScreen("game");
                    MainWindow.setGameText("Streetfighter", "Capcom", "Jan 9", "Cool", "Fighting", 4.5f);
                    break;
                case "JustinWongg":
                    System.out.println("Opened JustinWongg");
                    MainWindow.changePanelScreen("profile");
                    MainWindow.setProfileText("JustinWongg", "Jan 20, 2020");
                    break;
                default:
                    break;
            }
        }
    }
}

package Main;

public class Main {
    public static void main(String[] args) {
        // Set system look and feel
        try {
            javax.swing.UIManager.setLookAndFeel(
                javax.swing.UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception e) {
            e.printStackTrace(); // Print any error that occurs while setting the look and feel
        }

        // Launch the login window
        new view.LoginView().setVisible(true);
    }
}

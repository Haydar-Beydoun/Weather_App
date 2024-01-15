import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Display the app
                AppGUI app = new AppGUI();
                app.setVisible(true);
            }
        });
    }
}

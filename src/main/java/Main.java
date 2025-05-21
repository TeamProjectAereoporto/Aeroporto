import controller.Sistema;
import gui.Login;
import model.*;

import javax.swing.*;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Login");
            URL imageUrl = Main.class.getResource("sfondo.jpg");
            frame.setContentPane(new Login(frame).getLogin());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

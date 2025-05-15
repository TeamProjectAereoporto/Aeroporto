import controller.Sistema;
import gui.Login;
import model.*;

import javax.swing.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Login");
            frame.setContentPane(new Login(frame).getLogin());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setSize(300, 300);
            frame.setResizable(false);
            frame.setLocation(200, 200);
            frame.setVisible(true);
        });
    }
}

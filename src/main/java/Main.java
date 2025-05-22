
import gui.Login;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Login");
            Toolkit kit = Toolkit.getDefaultToolkit();
            File file = new File("src/main/java/imgs/sfondo.jpg");
            URL url = null;
            try {
                url = file.toURI().toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            Image img = Toolkit.getDefaultToolkit().createImage(url);
            frame.setIconImage(img);
            frame.setContentPane(new Login(frame).getLogin());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

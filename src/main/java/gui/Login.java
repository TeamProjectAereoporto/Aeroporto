package gui;

import controller.Sistema;
import model.Admin;
import model.UtenteGenerico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

/**
 * The type finestraPrincipale.
 */
public class Login {

    private final controller.Sistema sistema;
    private  JPanel finestraPrincipale;
    private JLabel username;
    private JLabel password;
    private JTextField campoUsername;
    private JButton invio;
    private JPasswordField campoPassword;
    private JButton registratiButton;
    private static final Logger logger = Logger.getLogger(Login.class.getName());

    public JButton getInvio() {
        return invio;
    }

    public static JFrame frame;
    public Login(JFrame frame, Sistema sistema) {
        this.sistema=sistema;
        Login.frame =frame;
        sistema.aggiungiUtente(new UtenteGenerico("karol","karol"));
        sistema.aggiungiUtente(new Admin("saso","saso","123"));
        frame.getRootPane().setDefaultButton(invio);
        invio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = campoUsername.getText();
                String password = new String(campoPassword.getPassword());
                if(!username.isEmpty() && !password.isEmpty()){
                    int ruolo=sistema.verificaUtenteP(username,password);
                    //apertura interfaccia utente=1 admin=2 else utente non esistente
                    campoPassword.setText("");
                    campoUsername.setText("");
                    switch (ruolo){
                        case 1:
                            HomeUtente home = new HomeUtente(frame, sistema, false);
                            HomeUtente.frame.setVisible(true);
                            frame.setVisible(false);
                            logger.info("sono dentro a utenteGenerico");
                            frame.dispose();
                            break;
                        case 2:
                            AdminPage homeAdmin = new AdminPage(frame, sistema);
                            AdminPage.frame.setVisible(true);
                            frame.setVisible(false);
                            frame.dispose();
                            break;
                        default:
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Credenziali errate. Riprova.",
                                    "Login Fallito",
                                    JOptionPane.WARNING_MESSAGE
                            );
                    }
                }
            }
        });
        invio.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                invio.setBackground(new Color(80, 80, 80));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                invio.setBackground(new Color(60, 60, 60));
            }
        });
        registratiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Register.frame.setVisible(true);
                frame.setVisible(false);
            }
        });
    }
    public JPanel getLogin() {
        return finestraPrincipale;
    }

}
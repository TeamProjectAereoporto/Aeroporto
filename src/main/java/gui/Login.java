package gui;

import controller.Sistema;
import model.Admin;
import model.Utente;
import model.UtenteGenerico;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The type finestraPrincipale.
 */
public class Login {

    private controller.Sistema sistema;
    private  JPanel finestraPrincipale;
    private JLabel immagine;
    private JTextField campoUsername;
    private JButton invio;
    private JPasswordField campoPassword;
    private JLabel username;
    private JLabel password;
    private JButton registratiButton;
    /**
     * The constant frame.
     */
    public static JFrame frame;
    public Login(JFrame frame, Sistema sistema) {
        this.sistema=sistema;
        this.frame=frame;
        sistema.aggiungiUtente(new UtenteGenerico("karol","karol"));
        sistema.aggiungiUtente(new Admin("saso","saso","123"));

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
                    if(ruolo == 1){
                        HomeUtente home = new HomeUtente(frame, sistema, false);
                        home.frame.setVisible(true);
                        frame.setVisible(false);
                        System.out.println("sono dentro a utenteGenerico");
                        frame.dispose();
                    }else if(ruolo==2){
                        AdminPage home = new AdminPage(frame, sistema);
                        home.frame.setVisible(true);
                        frame.setVisible(false);
                        frame.dispose();
                    }else{

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
                Register register = new Register(frame,sistema);
                Register.frame.setVisible(true);
                frame.setVisible(false);
            }
        });
    }
    public JPanel getLogin() {
        return finestraPrincipale;
    }

}
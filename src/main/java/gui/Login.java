package gui;

import controller.Sistema;
import model.Utente;

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

    private controller.Sistema controller;
    private  JPanel finestraPrincipale;
    private JLabel immagine;
    private JTextField campoUsername;
    private JButton invio;
    private JPasswordField campoPassword;
    private JLabel username;
    private JLabel password;
    /**
     * The constant frame.
     */
    public static JFrame frame;

    public Login(JFrame frame) {
        this.frame=frame;
        controller = new Sistema();
        controller.aggiungiUtente(new Utente("karol","karol","utenteGenerico"));
        controller.aggiungiUtente(new Utente("saso","saso","admin"));

        invio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            String username = campoUsername.getText();
            String password = new String(campoPassword.getPassword());
            if(!username.isEmpty() && !password.isEmpty()){
                int ruolo=controller.verificaUtenteP(username,password);
                //apertura interfaccia utente=1 admin=2 else utente non esistente
                if(ruolo == 1){
                    HomeUtente home = new HomeUtente(frame, controller, false);
                    home.frame.setVisible(true);
                    frame.setVisible(false);
                    frame.dispose();
                }else if(ruolo==2){

                    AdminPage home = new AdminPage(frame, controller);
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
    }
    public JPanel getLogin() {
        return finestraPrincipale;
    }

}

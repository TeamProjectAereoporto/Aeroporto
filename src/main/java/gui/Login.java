package gui;

import controller.Sistema;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The type Login.
 */
public class Login {

    private controller.Sistema controller;
    private JPanel Login;
    private JTextField campoUsername;
    private JLabel username;
    private JButton invioButton;
    private JPasswordField campoPassword;
    /**
     * The constant frame.
     */
    public static JFrame frame;

    public Login() {
        controller = new Sistema();
        invioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            String username = campoUsername.getText();
            String password = new String(campoPassword.getPassword());
            if(!username.isEmpty() && !password.isEmpty()){
                int ruolo=controller.verificaUtenteP(username,password);
                //apertura interfaccia utente=1 admin=2 else utente non esistente
                if(ruolo == 1){
                    HomeUtente home = new HomeUtente(frame);
                    home.frame.setVisible(true);
                    frame.setVisible(false);
                    frame.dispose();
                }else if(ruolo==2){
                    AdminPage home = new AdminPage(frame);
                    home.frame.setVisible(true);
                    frame.setVisible(false);
                    frame.dispose();
                }else{

                }
            }
            }
        });
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        frame = new JFrame("MainGUI");
        frame.setContentPane(new Login().Login);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(300,300);
        frame.setResizable(false);
        frame.setLocation(200,200);
        frame.setVisible(true);
    }

}

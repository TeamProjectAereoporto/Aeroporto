package gui;

import controller.Sistema;
import model.UtenteGenerico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register {
    private JTextField usernameField;
    private JPasswordField campoPassword;
    private JLabel password1;
    private JButton inviaButton;
    private JButton annullaButton;
    private JPanel finestraPrincipale;
    public static JFrame frame;
    private Sistema sistema;
    public Register(JFrame chiamante, Sistema sistema){
        this.sistema=sistema;
        frame = new JFrame("Registra il tuo account");
        frame.setContentPane(finestraPrincipale);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setSize(300,200);
        frame.setLocationRelativeTo(null);
        inviaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = campoPassword.getText();
                boolean usernameBool = !username.isEmpty();
                boolean passwordBool = !password.isEmpty();
                if(usernameBool && passwordBool){
                    UtenteGenerico utente = new UtenteGenerico(username,password);
                    sistema.aggiungiUtente(utente);
                    chiamante.setVisible(true);
                    frame.setVisible(false);
                    frame.dispose();
                }
            }
        });
        annullaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            chiamante.setVisible(true);
            frame.setVisible(false);
            frame.dispose();
            }
        });
    }
}

package gui;

import controller.Sistema;
import model.UtenteGenerico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

public class Register {
    private JTextField usernameField;
    private JPasswordField campoPassword;
    private JLabel password1;
    private JButton inviaButton;
    private JPanel finestraPrincipale;
    public static JFrame frame;
    private Sistema sistema;
    public Register(JFrame chiamante, Sistema sistema){
        this.sistema=sistema;
        frame = new JFrame("Registra il tuo account");
        frame.setContentPane(finestraPrincipale);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.getRootPane().setDefaultButton(inviaButton);
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
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                chiamante.setVisible(true);
                frame.setVisible(false);
                frame.dispose();
            }
        });
    }
}

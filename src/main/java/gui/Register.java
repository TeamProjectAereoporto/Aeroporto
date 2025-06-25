package gui;

import controller.Sistema;
import model.UtenteGenerico;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

public class Register {
    private JPanel finestraPrincipale;
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField confermaPasswordField;
    private JButton inviaButton;
    private JPanel sfondoLogin;
    private JLabel usernameLabel;
    public final static JFrame frame = new JFrame("Registra il tuo account");
    private final Sistema sistema;


    public Register(JFrame chiamante, Sistema sistema) {
        this.sistema = sistema;

        frame.setContentPane(finestraPrincipale);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setSize(500, 400);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);

        applyStyles();
        frame.getRootPane().setDefaultButton(inviaButton);
        frame.setVisible(true);

        inviaButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String confermaPassword = confermaPasswordField.getText().trim();

            if (!sistema.verificaUtenteUnivoco(username)) {
                showError("Username già esistente");
                return;
            }

            if (username.isEmpty() || password.isEmpty() || confermaPassword.isEmpty()) {
                showError("Tutti i campi devono essere compilati");
                return;
            }

            if (!password.equals(confermaPassword)) {
                showError("Le password non coincidono");
                return;
            }
            int ruolo = 1;
            UtenteGenerico utente = new UtenteGenerico(username, password, ruolo);
            sistema.aggiungiUtente(utente);
            chiamante.setVisible(true);
            frame.dispose();
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                chiamante.setVisible(true);
                frame.dispose();
            }
        });

        addPlaceholders(usernameField, "Username");
        addPlaceholders(passwordField, "Password");
        addPlaceholders(confermaPasswordField, "Conferma Password");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    private void addPlaceholders(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void applyStyles() {
        Color background = new Color(245, 245, 245);
        Color successGreen = new Color(76, 175, 80);

        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

        // Container panel
        finestraPrincipale.setBackground(background);
        finestraPrincipale.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Field styles
        for (JTextField field : new JTextField[]{usernameField, passwordField, confermaPasswordField}) {
            field.setFont(fieldFont);
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(204, 204, 204)),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
            field.setBackground(Color.WHITE);
        }

        // Button styles
        inviaButton.setFont(buttonFont);
        inviaButton.setBackground(successGreen);
        inviaButton.setForeground(Color.WHITE);
        inviaButton.setFocusPainted(false);
        inviaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        inviaButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Border panel
        sfondoLogin.setBorder(new LineBorder(new Color(120, 120, 120), 2, true));
        sfondoLogin.setBackground(background);
    }
}

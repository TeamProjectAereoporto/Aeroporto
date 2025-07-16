package gui;

import controller.Sistema;
import model.UtenteGenerico;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

public class Register {
    // Pannello principale della finestra di registrazione
    private JPanel finestraPrincipale;
    // Campo di testo per inserire username
    private JTextField usernameField;
    // Campo di testo per inserire password (testo normale, non password field)
    private JTextField passwordField;
    // Campo di testo per confermare la password
    private JTextField confermaPasswordField;
    // Bottone per inviare i dati di registrazione
    private JButton inviaButton;
    // Pannello di sfondo per la sezione login
    private JPanel sfondoLogin;
    // Etichetta username (non usata nel codice, ma dichiarata)
    private JLabel usernameLabel;
    // Finestra JFrame principale per la registrazione
    public final static JFrame frame = new JFrame("Registra il tuo account");
    // Riferimento al sistema backend per la gestione utenti
    private final Sistema sistema;

    // Costruttore della classe Register, riceve la finestra chiamante e il sistema backend
    public Register(JFrame chiamante, Sistema sistema) {
        this.sistema = sistema;

        // Imposta il contenuto della finestra al pannello principale
        frame.setContentPane(finestraPrincipale);
        // Impedisce la chiusura diretta della finestra per gestirla manualmente
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setSize(500, 400); // Dimensione fissa della finestra
        frame.setResizable(true);
        frame.setLocationRelativeTo(null); // Centra la finestra sullo schermo

        // Applica gli stili personalizzati ai componenti UI
        applyStyles();

        // Imposta il pulsante invia come pulsante di default (attivato con Invio)
        frame.getRootPane().setDefaultButton(inviaButton);
        frame.setVisible(true);

        // Azione al click del bottone invia
        inviaButton.addActionListener(e -> {
            // Legge e normalizza i dati inseriti
            String username = usernameField.getText().trim().toLowerCase();
            String password = passwordField.getText().trim();
            String confermaPassword = confermaPasswordField.getText().trim();

            // Verifica che l'username sia univoco nel sistema
            if (!sistema.verificaUtenteUnivoco(username)) {
                showError("Username già esistente");
                return;
            }

            // Verifica che nessun campo sia vuoto
            if (username.isEmpty() || password.isEmpty() || confermaPassword.isEmpty()) {
                showError("Tutti i campi devono essere compilati");
                return;
            }

            // Controlla che la password e la conferma coincidano
            if (!password.equals(confermaPassword)) {
                showError("Le password non coincidono");
                return;
            }

            // Crea un nuovo utente generico con ruolo 1 (utente standard)
            int ruolo = 1;
            UtenteGenerico utente = new UtenteGenerico(username, password, ruolo);
            // Aggiunge l'utente al sistema
            sistema.aggiungiUtente(utente);

            // Rende visibile la finestra chiamante e chiude questa finestra
            chiamante.setVisible(true);
            frame.dispose();
        });

        // Gestisce la chiusura della finestra tramite la X in alto a destra
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Rende visibile la finestra chiamante e chiude questa
                chiamante.setVisible(true);
                frame.dispose();
            }
        });

        // Aggiunge placeholder ai campi di testo per una migliore UX
        addPlaceholders(usernameField, "Username");
        addPlaceholders(passwordField, "Password");
        addPlaceholders(confermaPasswordField, "Conferma Password");
    }

    // Metodo per mostrare un messaggio di errore in un popup
    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    // Metodo per aggiungere un placeholder visivo a un JTextField
    private void addPlaceholders(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);

        // Listener per gestire il comportamento del placeholder al focus
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                // Quando si entra nel campo, se c'è il placeholder lo rimuove
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // Quando si esce dal campo, se vuoto, ripristina il placeholder
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    // Metodo per applicare stili e personalizzazioni grafiche ai componenti
    private void applyStyles() {
        Color background = new Color(245, 245, 245); // colore sfondo chiaro
        Color successGreen = new Color(76, 175, 80); // colore verde per bottone successo

        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

        // Imposta lo sfondo e i bordi del pannello principale
        finestraPrincipale.setBackground(background);
        finestraPrincipale.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Stile per i campi di testo
        for (JTextField field : new JTextField[]{usernameField, passwordField, confermaPasswordField}) {
            field.setFont(fieldFont);
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(204, 204, 204)),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
            field.setBackground(Color.WHITE);
        }

        // Stile per il bottone invia
        inviaButton.setFont(buttonFont);
        inviaButton.setBackground(successGreen);
        inviaButton.setForeground(Color.WHITE);
        inviaButton.setFocusPainted(false);
        inviaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        inviaButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Stile per il pannello di sfondo login (bordo arrotondato e colore)
        sfondoLogin.setBorder(new LineBorder(new Color(120, 120, 120), 2, true));
        sfondoLogin.setBackground(background);
    }
}

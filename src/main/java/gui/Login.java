package gui;

import controller.Sistema;
import model.Admin;
import model.UtenteGenerico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

    private final Sistema sistema; // riferimento al sistema di controllo principale
    private JPanel finestraPrincipale; // pannello principale della finestra di login
    private JLabel username; // etichetta per il campo username
    private JLabel password; // etichetta per il campo password
    private JTextField campoUsername; // campo di testo per inserire username
    private JButton invio; // bottone per inviare il login
    private JPasswordField campoPassword; // campo per inserire la password (mascherata)
    private JButton registratiButton; // bottone per aprire la schermata di registrazione
    private JLabel immagine; // etichetta per l'immagine decorativa
    private static final Logger logger = Logger.getLogger(Login.class.getName()); // logger per tracciare eventi

    public JButton getInvio() {
        return invio; // metodo getter per il bottone invio
    }

    public static JFrame frame; // frame principale condiviso

    public Login(JFrame frame, Sistema sistema) {
        this.sistema = sistema; // inizializza il sistema
        Login.frame = frame; // assegna il frame passato
        frame.getRootPane().setDefaultButton(invio); // imposta il bottone invio come predefinito per Enter

        // aggiunge l'azione di login al bottone invio
        invio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = campoUsername.getText(); // prende username dal campo testo
                String password = new String(campoPassword.getPassword()); // prende password dal campo mascherato

                // verifica che username e password non siano vuoti
                if (!username.isEmpty() && !password.isEmpty()) {
                    boolean loginOk = false;
                    try {
                        loginOk = sistema.login(username, password); // tenta il login, imposta utente o admin
                    } catch (Exception ex) {
                        ex.printStackTrace(); // stampa eventuali eccezioni
                    }

                    // pulisce i campi dopo il tentativo di login
                    campoPassword.setText("");
                    campoUsername.setText("");

                    // se login fallito mostra messaggio di errore
                    if (!loginOk) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Credenziali errate. Riprova.",
                                "Login Fallito",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return; // esce dal metodo
                    }

                    int ruolo = 0; // variabile per ruolo utente
                    // determina il ruolo in base all'oggetto utente o admin nel sistema
                    if (sistema.utente != null) {
                        ruolo = 1; // utente generico
                    } else if (sistema.admin != null) {
                        ruolo = 2; // admin
                    } else {
                        ruolo = 0; // nessun ruolo
                    }

                    // definisce le colonne per la tabella voli
                    String[] colonne = {"Codice Volo", "Compagnia Aerea", "Aeroporto di Origine",
                            "Aeroporto Destinazione", "Orario di Arrivo", "Ritardo", "Stato del Volo", "Gate"};
                    // crea modello tabella non editabile
                    DefaultTableModel model = new DefaultTableModel(colonne, 0) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false; // nessuna cella modificabile
                        }
                    };

                    // switch sul ruolo per aprire la schermata corrispondente
                    switch (ruolo) {
                        case 1: // se utente generico
                            HomeUtente home = new HomeUtente(frame, sistema, model); // crea schermata home utente
                            HomeUtente.frame.setVisible(true); // mostra la finestra home utente
                            frame.setVisible(false); // nasconde la finestra login
                            logger.info("Login effettuato come Utente Generico"); // log evento
                            frame.dispose(); // distrugge il frame login
                            break;
                        case 2: // se admin
                            AdminPage homeAdmin = new AdminPage(frame, sistema, model); // crea schermata admin
                            AdminPage.frame.setVisible(true); // mostra finestra admin
                            frame.setVisible(false); // nasconde login
                            logger.info("Login effettuato come Admin"); // log evento
                            frame.dispose(); // distrugge frame login
                            break;
                        default: // ruolo non riconosciuto
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Errore interno: ruolo non riconosciuto.",
                                    "Errore",
                                    JOptionPane.ERROR_MESSAGE
                            );
                    }
                }
            }
        });

        // cambia colore del bottone invio al passaggio del mouse (hover)
        invio.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                invio.setBackground(new Color(80, 80, 80)); // colore più chiaro
            }

            @Override
            public void mouseExited(MouseEvent e) {
                invio.setBackground(new Color(60, 60, 60)); // colore originale
            }
        });

        // azione per il bottone registrati che apre la schermata di registrazione
        registratiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Register register = new Register(frame, sistema); // crea schermata registrazione
                register.frame.setVisible(true); // mostra finestra registrazione
                frame.setVisible(false); // nasconde login
            }
        });
    }

    public JPanel getLogin() {
        return finestraPrincipale; // restituisce il pannello principale del login
    }

}

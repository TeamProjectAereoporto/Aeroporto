package gui;

import controller.Sistema;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

public class Login {

    private final Sistema sistema;
    private JPanel finestraPrincipale;
    private JLabel username;
    private JLabel password;
    private JTextField campoUsername;
    private JButton invio;
    private JPasswordField campoPassword;
    private JButton registratiButton;
    private JLabel immagine;
    private static final Logger logger = Logger.getLogger(Login.class.getName());

    public JButton getInvio() {
        return invio;
    }

    public static JFrame frame;

    public Login(JFrame frame, Sistema sistema) {
        this.sistema = sistema;
        Login.frame = frame;
        frame.getRootPane().setDefaultButton(invio);

        invio.addActionListener(e-> {
            String username = campoUsername.getText();
            String password = new String(campoPassword.getPassword());

            if (!username.isEmpty() && !password.isEmpty()) {
                boolean loginOk = false;
                try {
                    loginOk = sistema.login(username, password);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                campoPassword.setText("");
                campoUsername.setText("");

                if (!loginOk) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Credenziali errate. Riprova.",
                            "Login Fallito",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                int ruolo = 0;
                if (sistema.getUtente()!= null) {
                    ruolo = 1;
                } else if (sistema.getAdmin() != null) {
                    ruolo = 2;
                } else {
                    ruolo = 0;
                }

                // Creazione dei modelli di tabella
                String[] colonne = {"Codice Volo", "Compagnia", "Origine",
                        "Destinazione", "Orario", "Ritardo", "Stato del Volo", "Gate"};

                // Modello condiviso per l'utente
                DefaultTableModel modelUtente = new DefaultTableModel(colonne, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

                // Modelli separati per admin
                DefaultTableModel modelArrivi = new DefaultTableModel(colonne, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

                DefaultTableModel modelPartenze = new DefaultTableModel(colonne, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

                // Popola i modelli
                sistema.visualizzaVoli().forEach(volo -> {
                    modelUtente.addRow(new Object[]{
                            volo.getCodiceVolo(),
                            volo.getCompagniaAerea(),
                            volo.getAeroportoOrigine(),
                            volo.getAeroportoDestinazione(),
                            volo.getOrarioArrivo(),
                            volo.getRitardo(),
                            volo.getStato().toString(),
                            volo.getGate()
                    });

                    // Per admin: separa arrivi e partenze
                    if (volo.getAeroportoDestinazione().equalsIgnoreCase("Capodichino")) {
                        modelArrivi.addRow(new Object[]{
                                volo.getCodiceVolo(),
                                volo.getCompagniaAerea(),
                                volo.getAeroportoOrigine(),
                                volo.getAeroportoDestinazione(),
                                volo.getOrarioArrivo(),
                                volo.getRitardo(),
                                volo.getStato().toString(),
                                volo.getGate()
                        });
                    } else {
                        modelPartenze.addRow(new Object[]{
                                volo.getCodiceVolo(),
                                volo.getCompagniaAerea(),
                                volo.getAeroportoOrigine(),
                                volo.getAeroportoDestinazione(),
                                volo.getOrarioArrivo(),
                                volo.getRitardo(),
                                volo.getStato().toString(),
                                volo.getGate()
                        });
                    }
                });

                switch (ruolo) {
                    case 1:
                        HomeUtente home = new HomeUtente(frame, sistema, modelPartenze, modelArrivi);
                        home.frame.setVisible(true);
                        frame.setVisible(false);
                        logger.info("Login effettuato come Utente Generico");
                        frame.dispose();
                        break;
                    case 2:
                        AdminPage homeAdmin = new AdminPage(frame, sistema, modelArrivi, modelPartenze);
                        homeAdmin.frame.setVisible(true);
                        frame.setVisible(false);
                        logger.info("Login effettuato come Admin");
                        frame.dispose();
                        break;
                    default:
                        JOptionPane.showMessageDialog(
                                null,
                                "Errore interno: ruolo non riconosciuto.",
                                "Errore",
                                JOptionPane.ERROR_MESSAGE
                        );
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

        registratiButton.addActionListener(e-> {
            Register register = new Register(frame, sistema);
            register.frame.setVisible(true);
            frame.setVisible(false);
        });
    }

    public JPanel getLogin() {
        return finestraPrincipale;
    }
}
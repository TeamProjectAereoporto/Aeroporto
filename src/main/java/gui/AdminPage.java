package gui;

import controller.Sistema;      // Controller del sistema (pattern Controller dell'architettura BCE)
import model.Volo;             // Modello che rappresenta un volo

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.border.EmptyBorder;

/*
  Classe AdminPage - Interfaccia grafica per l'amministratore.
  Permette di visualizzare, aggiungere, modificare ed eliminare voli.
  collegata al controller 'Sistema' che gestisce la logica dell'applicazione.
 */
public class AdminPage {
    // Componenti GUI
    private JPanel principale;
    private JLabel adminTitle;
    private JButton aggiungiVoloButton;
    private JButton eliminaVoloButton;
    private JPanel voliPanel;
    private JLabel voliLable;
    private JTable tabellaVoli;
    private JButton modificaVoloButton;
    private JLabel logoutLabel;
    private JPanel logOutPanel;
    public static final JFrame frame = new JFrame("AdminDashboard"); // Finestra principale della pagina Admin
    private final Sistema sistema; // Controller per interfacciarsi con il Model
    private final String errorMessage= "Errore";
    /*Costruttore della pagina Admin.
     chiamante JFrame della schermata di login, da riaprire al logout.
     controller Oggetto Sistema per gestire i dati e le operazioni.
     model Modello della tabella da visualizzare (DefaultTableModel).
     */
    public AdminPage(JFrame chiamante, Sistema controller, DefaultTableModel model) {
        this.sistema = controller;
        // Popolamento iniziale della tabella dei voli
        popolaTabellaVoli(model);
        tabellaVoli.setModel(model);
        // Listener per il pulsante "Aggiungi Volo"
        aggiungiVoloButton.addActionListener(e -> {
            AggiungiVolo av = new AggiungiVolo(model, sistema, this); // nuova finestra di inserimento volo
            JFrame frame = new JFrame("Aggiungi Volo");
            frame.getRootPane().setDefaultButton(av.getAggiungiVoloButton()); // Invio attiva il bottone
            frame.setContentPane(av.getPrincipale());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setSize(700, 500);
            frame.setLocation(400, 150);
            frame.setVisible(true);
        });
        // Listener per il pulsante "Elimina Volo"
        eliminaVoloButton.addActionListener(e -> {
            int rigaSelezionata = tabellaVoli.getSelectedRow();
            if (rigaSelezionata != -1) {
                int conferma = JOptionPane.showConfirmDialog(null,
                        "Sei sicuro di voler eliminare il volo selezionato?",
                        "Conferma eliminazione",
                        JOptionPane.YES_NO_OPTION);

                if (conferma == JOptionPane.YES_OPTION) {
                    // Estrae il codice del volo dalla prima colonna della riga selezionata
                    Object valoreCodice = tabellaVoli.getValueAt(rigaSelezionata, 0);
                    int codiceVolo = -1;
                    if (valoreCodice instanceof Integer) {
                        codiceVolo = (Integer) valoreCodice;
                    } else if (valoreCodice instanceof String) {
                        try {
                            codiceVolo = Integer.parseInt((String) valoreCodice);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, errorMessage+ " nel formato del codice volo");
                            return;
                        }
                    }
                    // Chiamata al controller per eliminare il volo
                    sistema.eliminaVolo(codiceVolo);
                    // Rimuove la riga dal modello grafico (tabella)
                    model.removeRow(rigaSelezionata);
                    //Rimuove la riga dall'arrayList interno
                    sistema.visualizzaVoli().remove(rigaSelezionata);
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Seleziona un volo da eliminare!",
                        errorMessage,
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        // Imposta il frame principale della dashboard
        frame.setContentPane(principale);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setVisible(true);
        // Listener per "Modifica Volo"
        modificaVoloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tabellaVoli.getSelectedRow();
                if (selectedRow != -1) {
                    // Ottiene il codice del volo dalla tabella
                    Object codiceObj = tabellaVoli.getValueAt(selectedRow, 0);
                    int codiceVolo = -1;
                    if (codiceObj instanceof Integer) {
                        codiceVolo = (Integer) codiceObj;
                    } else if (codiceObj instanceof String) {
                        try {
                            codiceVolo = Integer.parseInt((String) codiceObj);
                        } catch (NumberFormatException _) {
                            JOptionPane.showMessageDialog(null, errorMessage+ "nel formato del codice volo");
                            return;
                        }
                    }
                    // Cerca il volo corrispondente nel sistema
                    Volo voloSelezionato = null;
                    for (Volo v : sistema.visualizzaVoli()) {
                        if (v.getCodiceVolo() == codiceVolo) {
                            voloSelezionato = v;
                            break;
                        }
                    }
                    if (voloSelezionato != null) {
                        ModificaVolo modificaVoloPanel = new ModificaVolo(model, sistema, voloSelezionato);
                        // Finestra per modificare i dati del volo
                        JFrame frameVolo = new JFrame("Modifica Volo");
                        frameVolo.setContentPane(modificaVoloPanel.getPrincipale());
                        frameVolo.getRootPane().setDefaultButton(modificaVoloPanel.getSalvaButton());
                        frameVolo.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                        frameVolo.pack();
                        frameVolo.setSize(700, 500);
                        frameVolo.setLocation(400, 150);
                        frameVolo.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Volo non trovato nel sistema", errorMessage, JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Seleziona un volo da modificare", errorMessage, JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // Applica lo stile grafico alla finestra
        applyStyles(chiamante);
    }
    // Getter per accedere alla tabella da altre classi
    public JTable getTabellaVoli(){
        return tabellaVoli;
    }
    /*
     Popola il modello della tabella con i voli presenti nel sistema.
     Evita di mantenere una struttura in memoria duplicata, recuperando direttamente i dati dal controller.
     */
    private void popolaTabellaVoli(DefaultTableModel model) {
        List<Volo> voli = sistema.visualizzaVoli();
        for (Volo v : voli) {
            model.addRow(new Object[]{
                    v.getCodiceVolo(),
                    v.getCompagniaAerea(),
                    v.getAeroportoOrigine(),
                    v.getAeroportoDestinazione(),
                    v.getOrarioArrivo(),
                    v.getRitardo(),
                    v.getStato(),
                    v.getGate()
            });
        }
    }

    /*
     Applica uno stile coerente a tutti gli elementi della GUI.
     Palette colori ispirata a sistemi aeroportuali.
     Font leggibili e colori con buon contrasto.
     */
    private void applyStyles(JFrame chiamante) {
        Color primaryBlue = new Color(0, 95, 135);
        Color secondaryBlue = new Color(0, 120, 167);
        Color background = new Color(245, 245, 245);
        Color errorRed = new Color(231, 76, 60);
        Color successGreen = new Color(76, 175, 80);

        final String name = "Segoe UI";
        Font titleFont = new Font(name, Font.BOLD, 24);
        Font labelFont = new Font(name, Font.BOLD, 14);
        Font buttonFont = new Font(name, Font.BOLD, 14);
        Font tableFont = new Font(name, Font.PLAIN, 12);

        principale.setBackground(background);
        principale.setBorder(new EmptyBorder(15, 15, 15, 15));
        voliPanel.setBackground(background);
        logOutPanel.setBackground(secondaryBlue);

        adminTitle.setFont(titleFont);
        adminTitle.setForeground(primaryBlue);
        adminTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        voliLable.setFont(labelFont);
        voliLable.setForeground(primaryBlue);
        logoutLabel.setFont(labelFont);
        logoutLabel.setForeground(background);

        styleButton(aggiungiVoloButton, successGreen, Color.WHITE, buttonFont);
        styleButton(modificaVoloButton, secondaryBlue, Color.WHITE, buttonFont);
        styleButton(eliminaVoloButton, errorRed, Color.WHITE, buttonFont);

        tabellaVoli.setFont(tableFont);
        tabellaVoli.setRowHeight(25);
        tabellaVoli.setShowGrid(false);
        tabellaVoli.setIntercellSpacing(new Dimension(0, 0));
        tabellaVoli.setSelectionBackground(new Color(220, 240, 255));
        tabellaVoli.getTableHeader().setFont(labelFont);
        tabellaVoli.getTableHeader().setBackground(primaryBlue);
        tabellaVoli.getTableHeader().setForeground(Color.WHITE);
        tabellaVoli.getTableHeader().setReorderingAllowed(false);

        // Comportamento logout con effetto hover
        logOutPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logOutPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logOutPanel.setBackground(successGreen);
                chiamante.setVisible(true); // Torna alla schermata di login
                Window window = SwingUtilities.getWindowAncestor(principale);
                if (window != null) {
                    window.dispose(); // Chiude la finestra corrente
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                logOutPanel.setBackground(errorRed);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logOutPanel.setBackground(secondaryBlue);
            }
        });
    }

    //Applica stile uniforme ai pulsanti (font, colori, effetto hover).
    private void styleButton(JButton button, Color bg, Color fg, Font font) {
        button.setFont(font);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bg.darker());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bg);
            }
        });
    }
}

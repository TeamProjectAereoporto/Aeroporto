package gui;

import controller.Sistema;
import model.Volo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * The type Home utente.
 */
public class HomeUtente {
    private final Sistema sistema;
    private JButton cercaBigliettoButton;
    private JPanel navbar;
    private JPanel finestraPrincipale;
    private JTable tabellaVoli;
    private JPanel voliPanel;
    private JTextField numeroBiglietto;
    private JTextField nome;
    private JLabel titoloB;
    private JButton logout;
    private JComboBox comboBox1;
    private JButton visualizzaButton;
    private JPanel filterFly;
    private JLabel frecciaSinistra;
    private JLabel data;
    private JPanel formData;
    private JLabel frecciaDestra;
    private LocalDate dataVolo;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    /**
     * The constant frame.
     */
    public final static JFrame frame = new JFrame("Home");

    public JPanel getPanel() {
        // Ritorna il pannello principale della finestra
        return finestraPrincipale;
    }

    /**
     * Instantiates a new Home utente.
     */
    public HomeUtente(JFrame frameChiamante, Sistema sistema, DefaultTableModel modelPartenza, DefaultTableModel modelArrivo) {
        this.sistema = sistema;
        dataVolo = LocalDate.now();
        data.setText(formatter.format(dataVolo));

        inizializzaFrame();        // Inizializza le impostazioni della finestra JFrame
        caricaVoli();              // Carica i dati dei voli nel modello della tabella
        impostaAzioni(frameChiamante);  // Imposta le azioni per i vari bottoni e componenti
        configuraTabella(modelArrivo);
        visualizzaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String voliScelti = comboBox1.getSelectedItem().toString();
                if(voliScelti.equalsIgnoreCase("Voli in Partenza")) {
                    configuraTabella(modelPartenza);   // Configura la tabella per visualizzare i voli
                }else{
                    configuraTabella(modelArrivo);
                }
            }
        });
        frecciaDestra.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dataVolo = dataVolo.plusDays(1);
                data.setText(formatter.format(dataVolo));
            }
        });
        frecciaSinistra.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(LocalDate.now().isBefore(dataVolo)){
                dataVolo = dataVolo.minusDays(1);
                    data.setText(formatter.format(dataVolo));
                }
            }
        });
    }

    private void inizializzaFrame() {
        // Imposta il contenuto della finestra, le proprietà base e la rende visibile
        frame.setContentPane(finestraPrincipale);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1100, 700);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);  // Centra la finestra sullo schermo
        frame.setVisible(true);
    }

    private void configuraTabella(DefaultTableModel model) {
        // Imposta le colonne della tabella e configura proprietà come riordinamento, dimensione e altezza righe
        tabellaVoli.setModel(model);
        tabellaVoli.getTableHeader().setReorderingAllowed(false);  // Disabilita il riordinamento delle colonne
        tabellaVoli.getTableHeader().setResizingAllowed(false);    // Disabilita il ridimensionamento delle colonne
        tabellaVoli.setRowHeight(20);                              // Imposta altezza riga a 20 pixel
    }

    private void caricaVoli() {
        // Ottiene la lista dei voli dal sistema e la aggiunge alla tabella
        ArrayList<Volo> voli = sistema.visualizzaVoli();
        DefaultTableModel model = (DefaultTableModel) tabellaVoli.getModel();
        if (voli != null) {
            for (Volo v : voli) {
                model.addRow(new Object[]{
                        v.getCodiceVolo(),
                        v.getCompagniaAerea(),
                        v.getAeroportoOrigine(),
                        v.getAeroportoDestinazione(),
                        v.getOrarioArrivo(),
                        v.getDataVolo(),
                        v.getRitardo(),
                        v.getStato(),
                        v.getGate()
                });
            }
        }

        // Aggiunge la tabella dentro uno JScrollPane se non già presente nel contenitore voliPanel
        if (!(tabellaVoli.getParent() instanceof JViewport)) {
            voliPanel.removeAll();
            voliPanel.setLayout(new BorderLayout());
            voliPanel.add(new JScrollPane(tabellaVoli), BorderLayout.CENTER);
            voliPanel.revalidate();
            voliPanel.repaint();
        }
    }

    private void impostaAzioni(JFrame frameChiamante) {
        personalizzaBottoneCerca(); // Applica stile personalizzato al bottone cercaBigliettoButton

        // Azione per il bottone cerca biglietto: verifica input e apre la finestra Biglietto se valido
        cercaBigliettoButton.addActionListener(e -> {
            if (!numeroBiglietto.getText().isEmpty() || !nome.getText().isEmpty()) {
                int numero = -1;
                if (!numeroBiglietto.getText().isEmpty()) {
                    if (!numeroBiglietto.getText().trim().matches("\\d{4}")) {
                        // Mostra errore se il codice volo non è un numero di 4 cifre
                        JOptionPane.showMessageDialog(finestraPrincipale, "Il codice volo deve essere un numero intero di 4 cifre", "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    numero = Integer.parseInt(numeroBiglietto.getText().trim());
                }
                // Crea e mostra la finestra Biglietto e nasconde quella attuale
                Biglietto biglietto = new Biglietto(frame, sistema, nome.getText(), numero);
                biglietto.frame.setVisible(true);
            }
        });

        // Imposta il bottone cercaBigliettoButton come predefinito per invio tastiera
        frame.getRootPane().setDefaultButton(cercaBigliettoButton);

        // Azione doppio click su una riga della tabella voli
        tabellaVoli.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tabellaVoli.rowAtPoint(e.getPoint());
                    if (row != -1) {
                        String stato = tabellaVoli.getValueAt(row, 6).toString();
                        // Controlla che lo stato del volo permetta la prenotazione
                        if (!stato.equals("DECOLLATO") && !stato.equals("CANCELLATO") && !stato.equals("ATTERRATO")) {
                            Object[] valori = new Object[tabellaVoli.getColumnCount()];
                            for (int i = 0; i < valori.length; i++) {
                                valori[i] = tabellaVoli.getValueAt(row, i);
                            }
                            // Apre la finestra Prenota per effettuare la prenotazione del volo selezionato
                            Prenota prenotazione = new Prenota(frame, valori, sistema);
                            prenotazione.frame.setVisible(true);
                        } else {
                            // Mostra messaggio di errore se il volo non è prenotabile
                            JOptionPane.showMessageDialog(null, "Non puoi prenotare un volo " + stato.toLowerCase(), "Errore", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        // Azione per il bottone logout: esegue logout, apre la finestra Login e chiude questa finestra
        logout.addActionListener(e -> {
            sistema.logout();
            Login login = new Login(frameChiamante, sistema);
            Login.frame.getRootPane().setDefaultButton(login.getInvio());
            Login.frame.setVisible(true);
            frame.setVisible(false);
            frame.dispose();
        });
    }

    private void personalizzaBottoneCerca() {
        // Imposta colori e stile personalizzato al bottone cercaBigliettoButton
        cercaBigliettoButton.setBackground(new Color(33, 150, 243));
        cercaBigliettoButton.setForeground(Color.WHITE);
        cercaBigliettoButton.setFocusPainted(false);
        cercaBigliettoButton.setBorderPainted(false);
        cercaBigliettoButton.setOpaque(true);

        // Cambia colore del bottone al passaggio del mouse
        cercaBigliettoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                cercaBigliettoButton.setBackground(new Color(30, 136, 229));
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                cercaBigliettoButton.setBackground(new Color(33, 150, 243));
            }
        });
    }
}

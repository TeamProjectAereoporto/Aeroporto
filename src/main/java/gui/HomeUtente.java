package gui;

import controller.Sistema;
import model.Volo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * The type Home utente.
 */
public class HomeUtente {
    private Sistema sistema;
    private JButton cercaBigliettoButton;
    private JPanel navbar;
    private JPanel FinestraPrincipale;
    private JTable tabellaVoli;
    private JPanel voliPanel;
    private JTextField numeroBiglietto;
    private JTextField nome;
    private JLabel titoloB;
    private JButton logout;
    /**
     * The constant frame.
     */
    public static JFrame frame;

    public JPanel getPanel() {
        return FinestraPrincipale;
    }

    /**
     * Instantiates a new Home utente.
     */
    public HomeUtente(JFrame frameChiamante, Sistema sistema, boolean isAdmin) {
        this.sistema = sistema;
        //colonne della tabella
        String[] colonne = {"Codice Volo", "Compagnia Aerea", "Aeroporto di Origine",
                "Aeroporto Destinazione", "Orario di Arrivo", "Ritardo", "Stato del Volo", "Gate"};
        DefaultTableModel model = new DefaultTableModel(colonne, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return isAdmin; // Nessuna cella è modificabile per gli utenti
            }
        };
        //debbubing
        // ora attuale, oppure puoi usare un altro costruttore per una data specifica
        Volo volo1 = new Volo(
                1234,                   // codiceVolo
                "Alitalia",                        // compagniaAerea
                "Fiumicino",                       // aeroporto di origine
                "Linate",
                "12:13",                      // orarioArrivo
                15,                                // ritardo in minuti
                Volo.statoVolo.PROGRAMMATO,         // stato del volo (enum)
                "3"// aeroporto di destinazione
        );
        Volo volo2 = new Volo(
                1232,                   // codiceVolo
                "Alitalia",                        // compagniaAerea
                "Napoli",                       // aeroporto di origine
                "Linate",
                "12:15",                      // orarioArrivo
                25,                                // ritardo in minuti
                Volo.statoVolo.PROGRAMMATO,         // stato del volo (enum)
                "2"// aeroporto di destinazione
        );
        Volo volo3 = new Volo(
                2232,                   // codiceVolo
                "Alitalia",                        // compagniaAerea
                "Napoli",                       // aeroporto di origine
                "Milano",
                "13:00",                      // orarioArrivo
                0,                                // ritardo in minuti
                Volo.statoVolo.PROGRAMMATO,         // stato del volo (enum)
                "1"// aeroporto di destinazione
        );
        sistema.aggiungiVolo(volo1);
        sistema.aggiungiVolo(volo2);
        sistema.aggiungiVolo(volo3);
        tabellaVoli.setModel(model);
        //visualizzazione voli
        ArrayList<Volo> voli = sistema.visualizzaVoli();
        if (voli != null) {
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

        // Assicuro la corretta visualizzazione nella scroll pane
        if (!(tabellaVoli.getParent() instanceof JViewport)) {
            voliPanel.removeAll();
            voliPanel.setLayout(new BorderLayout());
            voliPanel.add(new JScrollPane(tabellaVoli), BorderLayout.CENTER);
            voliPanel.revalidate();
            voliPanel.repaint();
        }
        //personalizzazione del bottone biglietto
        cercaBigliettoButton.setBackground(new Color(33, 150, 243));
        cercaBigliettoButton.setForeground(Color.WHITE);
        cercaBigliettoButton.setFocusPainted(false);
        cercaBigliettoButton.setBorderPainted(false);
        cercaBigliettoButton.setOpaque(true);
        cercaBigliettoButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cercaBigliettoButton.setBackground(new Color(30, 136, 229)); // Hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                cercaBigliettoButton.setBackground(new Color(33, 150, 243)); // Default
            }
        });
        //evento per andare alla GUI biglietti
        // Azione bottone cerca biglietto (apre GUI biglietto)
        cercaBigliettoButton.addActionListener(e -> {
            if(!numeroBiglietto.getText().isEmpty() || !nome.getText().isEmpty()) {
                int numero =-1;
                if(!numeroBiglietto.getText().isEmpty()){
                    if (!numeroBiglietto.getText().trim().matches("\\d{4}") && !numeroBiglietto.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(FinestraPrincipale, "Il codice volo deve essere un numero intero di 4 cifre", "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    numero = Integer.parseInt(numeroBiglietto.getText().trim());
                }
                Biglietto biglietto = new Biglietto(frame, sistema, nome.getText(), numero);
                biglietto.frame.setVisible(true);
                frame.setVisible(false);
            }
        });
        frame = new JFrame("Home");
        frame.setContentPane(FinestraPrincipale);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(900,600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        tabellaVoli.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    Object[]valori;
                    int row = tabellaVoli.rowAtPoint(e.getPoint());
                    if(row!=-1) {
                        int column = tabellaVoli.getColumnCount();
                        valori = new Object[column];
                        for(int i =0; i<column; i++){
                            valori[i]=tabellaVoli.getValueAt(row, i);
                        }
                        for (Object val : valori) {
                            System.out.println(" - " + val);
                        }
                        Prenota prenotazione = new Prenota(frame, valori, sistema);
                        prenotazione.frame.setVisible(true);
                        frame.setVisible(false);
                    }
                }
            }
        });
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sistema.logout(sistema.utente);
                Login login = new Login(frameChiamante,sistema);
                login.frame.getRootPane().setDefaultButton(login.getInvio());
                login.frame.setVisible(true);
                frame.setVisible(false);
                frame.dispose();
            }
        });
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
}
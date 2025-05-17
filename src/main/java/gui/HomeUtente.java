package gui;

import controller.Sistema;
import model.Prenotazione;
import model.Volo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

/**
 * The type Home utente.
 */
public class HomeUtente {
    private controller.Sistema sistema;
    private JButton cercaBigliettoButton;
    private JPanel navbar;
    private JPanel FinestraPrincipale;
    private JTable tabellaVoli;
    private JPanel voliPanel;
    private JSpinner numeroBiglietto;
    private JTextField nome;
    /**
     * The constant frame.
     */
    public static JFrame frame;

    /**
     * Instantiates a new Home utente.
     */
    public HomeUtente(JFrame frameChiamante) {
        sistema = new Sistema();
        //colonne della tabella
        String[] colonne = {"Codice Volo", "Compagnia Aerea", "Aeroporto di Origine",
                "Aeroporto Destinazione", "Orario di Arrivo", "Ritardo", "Stato del Volo"};
        DefaultTableModel model = new DefaultTableModel(colonne, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Nessuna cella è modificabile
            }
        };
        //debbubing
        Date orarioArrivo = new Date(); // ora attuale, oppure puoi usare un altro costruttore per una data specifica
        Volo volo1 = new Volo(
                12345,                   // codiceVolo
                "Alitalia",                        // compagniaAerea
                orarioArrivo,                      // orarioArrivo
                15,                                // ritardo in minuti
                Volo.statoVolo.PROGRAMMATO,         // stato del volo (enum)
                "Fiumicino",                       // aeroporto di origine
                "Linate"                           // aeroporto di destinazione
        );
        sistema.aggiungiVolo(volo1);
        tabellaVoli.setModel(model);
        //visualizzazione voli
        ArrayList<model.Volo> voli = sistema.visualizzaVoli();
        if (voli!=null) {
            for (int i = 0; i < voli.size(); i++)
                model.addRow(new Object[]{voli.get(i).getCodiceVolo(), voli.get(i).getCompagniaAerea(), voli.get(i).getAeroportoOrigine(), voli.get(i).getAeroportoDestinazione(), voli.get(i).getOrarioArrivo(), voli.get(i).getRitardo(), voli.get(i).getStato()});
        }

        if (tabellaVoli.getParent() instanceof JViewport) {
        } else {
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
        cercaBigliettoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int numero = (Integer) numeroBiglietto.getValue();
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
        frame.setLocation(200,200);
        frame.setVisible(true);
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
}

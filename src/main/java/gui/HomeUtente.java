package gui;

import controller.Sistema;
import model.Volo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class HomeUtente {
    private Sistema sistema;
    private JButton cercaBigliettoButton;
    private JPanel navbar;
    private JPanel FinestraPrincipale;
    private JTable tabellaVoli;
    private JPanel voliPanel;
    private JSpinner numeroBiglietto;
    private JTextField nome;

    public static JFrame frame;

    /**
     * Costruttore di HomeUtente.
     *
     * @param frameChiamante il JFrame chiamante (per gestione finestre)
     * @param sistema        istanza condivisa del controller Sistema
     * @param isAdmin        true se l'utente è admin, false se generico
     */
    public HomeUtente(JFrame frameChiamante, Sistema sistema, boolean isAdmin) {
        this.sistema = sistema;

        // Definisco colonne tabella voli
        String[] colonne = {
                "Codice Volo", "Compagnia Aerea", "Aeroporto di Origine",
                "Aeroporto Destinazione", "Orario di Arrivo", "Ritardo", "Stato del Volo", "Gate"
        };

        DefaultTableModel model = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo admin può modificare la tabella, altrimenti no
                return isAdmin;
            }
        };

        // Imposto il modello alla tabella
        tabellaVoli.setModel(model);

        // Popolo la tabella con i voli dal sistema
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

        // Personalizzazione bottone "Cerca Biglietto"
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

        // Azione bottone cerca biglietto (apre GUI biglietto)
        cercaBigliettoButton.addActionListener(e -> {
            int numero = (Integer) numeroBiglietto.getValue();
            Biglietto biglietto = new Biglietto(frame, sistema, nome.getText(), numero);
            biglietto.frame.setVisible(true);
            frame.setVisible(false);
        });

        frame = new JFrame("Home");
        frame.setContentPane(FinestraPrincipale);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(900, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

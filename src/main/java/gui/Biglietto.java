package gui;

import model.Prenotazione;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * The type Biglietto.
 */

public class Biglietto {
    private final controller.Sistema sistema;
    private JPanel finestraPrincipale;
    private JButton modificaButton;
    private JButton cancellaButton;
    private JLabel titolo;
    private JTable tabellaBiglietti;
    private JButton fineButton;
    private JPanel navbar;
    /**
     * The constant frame.
     */
    public final static JFrame frame = new JFrame("Cerca e modifica biglietto");
    private  ArrayList<Prenotazione> biglietti;
    /**
     * Instantiates a new Biglietto.
     *
     * @param frameChiamante the frame chiamante
     * @param sistema        the sistema
     */
    public Biglietto(JFrame frameChiamante, controller.Sistema sistema, String nome, int codiceVolo) {
        this.sistema = sistema;
        inizializzaFrame();
        configuraTabella();
        caricaBiglietti(nome, codiceVolo);
        impostaAzioni(frameChiamante);
    }

    private void inizializzaFrame() {
        frame.setContentPane(finestraPrincipale);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.pack();
        frame.setSize(900, 600);
        frame.setResizable(false);
        frame.setLocation(200, 200);
        frame.setVisible(true);
        finestraPrincipale.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }

    private void configuraTabella() {
        String[] colonne = {"Nome", "Cognome", "Carta d'identità", "Posto", "Numero Biglietto"};
        tabellaBiglietti.setRowHeight(30);
        tabellaBiglietti.setGridColor(new Color(80, 80, 80));
        JTableHeader header = tabellaBiglietti.getTableHeader();
        header.setBackground(new Color(75, 75, 75));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 12));

        DefaultTableModel model = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabellaBiglietti.setModel(model);
        tabellaBiglietti.getTableHeader().setReorderingAllowed(false);
        tabellaBiglietti.getTableHeader().setResizingAllowed(false);
    }

    private void caricaBiglietti(String nome, int codiceVolo) {
        biglietti = sistema.getBiglietti(nome, codiceVolo);
        DefaultTableModel model = (DefaultTableModel) tabellaBiglietti.getModel();
        if (biglietti != null) {
            for (Prenotazione p : biglietti) {
                model.addRow(new Object[]{
                        p.getPasseggero().getNome(),
                        p.getPasseggero().getCognome(),
                        p.getPasseggero().getNumeroDocumento(),
                        p.getPostoAssegnato(),
                        p.getNumeroBiglietto()
                });
            }
        }
    }

    private void impostaAzioni(JFrame frameChiamante) {
        fineButton.addActionListener(e -> {
            frameChiamante.setVisible(true);
            frame.setVisible(false);
            frame.dispose();
        });

        cancellaButton.addActionListener(e -> eliminaBiglietto());

        modificaButton.addActionListener(e -> modificaBiglietto());

        tabellaBiglietti.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    mostraDettagliVolo();
                }
            }
        });
    }

    private void eliminaBiglietto() {
        int riga = tabellaBiglietti.getSelectedRow();
        if (riga != -1) {
            int conferma = JOptionPane.showConfirmDialog(null,
                    "Sei sicuro di voler eliminare questo biglietto?",
                    "Cancellazione biglietto",
                    JOptionPane.YES_NO_OPTION);
            if (conferma == JOptionPane.YES_OPTION) {
                DefaultTableModel model = (DefaultTableModel) tabellaBiglietti.getModel();
                Object valoreNumeroBiglietto = model.getValueAt(riga, 4);
                if (valoreNumeroBiglietto != null) {
                    try {
                        long numeroBiglietto = Long.parseLong(valoreNumeroBiglietto.toString());
                        sistema.cancellaBiglietto(numeroBiglietto);
                        model.removeRow(riga);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null,
                                "Numero biglietto non valido!",
                                "Errore",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private void modificaBiglietto() {
        int riga = tabellaBiglietti.getSelectedRow();
        if (riga != -1) {
            Prenotazione bigliettoDaModificare = biglietti.get(riga);
            DefaultTableModel model = (DefaultTableModel) tabellaBiglietti.getModel();
            modificaBiglietto modifica = new modificaBiglietto(sistema, bigliettoDaModificare, model, frame);
            modificaBiglietto.frame.setVisible(true);
            frame.setVisible(false);
        }
    }

    private void mostraDettagliVolo() {
        int riga = tabellaBiglietti.getSelectedRow();
        Prenotazione prenotazione = biglietti.get(riga);
        String info = "Codice Volo: " + prenotazione.getVolo().getCodiceVolo() + "\n" +
                "Compagnia Aerea: " + prenotazione.getVolo().getCompagniaAerea() + "\n" +
                "Aeroporto di Origine: " + prenotazione.getVolo().getAeroportoOrigine() + "\n" +
                "Aeroporto Destinazione: " + prenotazione.getVolo().getAeroportoDestinazione() + "\n" +
                "Orario di Arrivo: " + prenotazione.getVolo().getOrarioArrivo() + "\n" +
                "Ritardo: " + prenotazione.getVolo().getRitardo() + "'\n" +
                "Gate: " + prenotazione.getVolo().getGate();
        JOptionPane.showMessageDialog(null, info, "Informazioni Volo", JOptionPane.INFORMATION_MESSAGE);
    }
}
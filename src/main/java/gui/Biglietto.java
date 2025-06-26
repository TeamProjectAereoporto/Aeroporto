package gui;

import controller.Sistema;
import model.Prenotazione;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/*
 * Classe che rappresenta la finestra per cercare, modificare ed eliminare biglietti (prenotazioni).
 */
public class Biglietto {
    private final Sistema sistema; // Riferimento al controller sistema
    private JPanel finestraPrincipale; // Pannello principale GUI
    private JButton modificaButton;
    private JButton cancellaButton;
    private JButton fineButton;
    private JTable tabellaBiglietti;
    private JPanel navbar;
    private JLabel titolo;

    private ArrayList<Prenotazione> biglietti; // Lista dei biglietti caricati nella tabella

    public final static JFrame frame = new JFrame("Cerca e modifica biglietto");

    /*
     * Costruttore: inizializza la GUI e carica i biglietti filtrati per nome e codice volo.
     */
    public Biglietto(JFrame frameChiamante, Sistema sistema, String nome, int codiceVolo) {
        this.sistema = sistema;
        inizializzaFrame();
        configuraTabella();
        caricaBiglietti(nome, codiceVolo);
        impostaAzioni(frameChiamante);
    }

    /*
     * Imposta il frame principale e le proprietà base della finestra.
     */
    private void inizializzaFrame() {
        frame.setContentPane(finestraPrincipale);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setSize(900, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null); // Centra la finestra
        frame.setVisible(true);

        // Margini interni al pannello principale
        finestraPrincipale.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }

    /*
     * Configura la tabella che visualizza i biglietti (prenotazioni).
     */
    private void configuraTabella() {
        String[] colonne = {"Nome", "Cognome", "Carta d'identità", "Posto", "Numero Biglietto"};
        tabellaBiglietti.setRowHeight(30);
        tabellaBiglietti.setGridColor(new Color(80, 80, 80));

        // Personalizza header della tabella
        JTableHeader header = tabellaBiglietti.getTableHeader();
        header.setBackground(new Color(75, 75, 75));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);

        // Modello dati non editabile
        DefaultTableModel model = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabellaBiglietti.setModel(model);
    }

    /*
     * Carica i biglietti dal sistema filtrando per nome e codice volo e li inserisce nella tabella.
     */
    private void caricaBiglietti(String nome, int codiceVolo) {
        biglietti = sistema.getBiglietti(sistema.utente.getNomeUtente(),nome, codiceVolo);
        DefaultTableModel model = (DefaultTableModel) tabellaBiglietti.getModel();
        model.setRowCount(0); // Pulisce la tabella prima di caricare i dati
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
    /*
    * Imposta le azioni dei pulsanti e il doppio click sulla tabella.
    */
    private void impostaAzioni(JFrame frameChiamante) {
        // Pulsante "Fine": torna al frame chiamante e chiude questo
        fineButton.addActionListener(e -> {
            frameChiamante.setVisible(true);
            frame.setVisible(false);
            frame.dispose();
        });

        // Pulsante "Cancella": elimina il biglietto selezionato
        cancellaButton.addActionListener(e -> eliminaBiglietto());

        // Pulsante "Modifica": apre la finestra di modifica del biglietto selezionato
        modificaButton.addActionListener(e -> modificaBiglietto());

        // Doppio click sulla tabella mostra dettagli volo relativo alla prenotazione selezionata
        tabellaBiglietti.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    mostraDettagliVolo();
                }
            }
        });
    }

    /*
     * Elimina il biglietto selezionato dopo conferma utente.
     */
    private void eliminaBiglietto() {
        int riga = tabellaBiglietti.getSelectedRow();
        if (riga == -1) {
            JOptionPane.showMessageDialog(frame, "Seleziona un biglietto da eliminare.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int conferma = JOptionPane.showConfirmDialog(frame,
                "Sei sicuro di voler eliminare questo biglietto?",
                "Conferma eliminazione",
                JOptionPane.YES_NO_OPTION);

        if (conferma == JOptionPane.YES_OPTION) {
            DefaultTableModel model = (DefaultTableModel) tabellaBiglietti.getModel();
            Object valoreNumeroBiglietto = model.getValueAt(riga, 4);
            if (valoreNumeroBiglietto != null) {
                try {
                    long numeroBiglietto = Long.parseLong(valoreNumeroBiglietto.toString());
                    sistema.cancellaBiglietto(numeroBiglietto);
                    model.removeRow(riga);
                    biglietti.remove(riga); // Aggiorna anche la lista interna
                    JOptionPane.showMessageDialog(frame, "Biglietto eliminato con successo.", "Successo", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Numero biglietto non valido!", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /*
     * Apre la finestra di modifica per il biglietto selezionato.
     */
    private void modificaBiglietto() {
        int riga = tabellaBiglietti.getSelectedRow();
        if (riga == -1) {
            JOptionPane.showMessageDialog(frame, "Seleziona un biglietto da modificare.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Prenotazione bigliettoDaModificare = biglietti.get(riga);
        DefaultTableModel model = (DefaultTableModel) tabellaBiglietti.getModel();
        modificaBiglietto modifica = new modificaBiglietto(sistema, bigliettoDaModificare, model, frame);
        modificaBiglietto.frame.setVisible(true);
    }

    /*
     * Mostra una finestra di dialogo con i dettagli del volo relativo al biglietto selezionato.
     */
    private void mostraDettagliVolo() {
        int riga = tabellaBiglietti.getSelectedRow();
        if (riga == -1) {
            JOptionPane.showMessageDialog(frame, "Seleziona un biglietto per visualizzare i dettagli del volo.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Prenotazione prenotazione = biglietti.get(riga);
        String info = "Codice Volo: " + prenotazione.getVolo().getCodiceVolo() + "\n" +
                "Compagnia Aerea: " + prenotazione.getVolo().getCompagniaAerea() + "\n" +
                "Aeroporto di Origine: " + prenotazione.getVolo().getAeroportoOrigine() + "\n" +
                "Aeroporto Destinazione: " + prenotazione.getVolo().getAeroportoDestinazione() + "\n" +
                "Orario di Arrivo: " + prenotazione.getVolo().getOrarioArrivo() + "\n" +
                "Ritardo: " + prenotazione.getVolo().getRitardo() + " minuti\n" +
                "Gate: " + prenotazione.getVolo().getGate();
        JOptionPane.showMessageDialog(frame, info, "Informazioni Volo", JOptionPane.INFORMATION_MESSAGE);
    }
}

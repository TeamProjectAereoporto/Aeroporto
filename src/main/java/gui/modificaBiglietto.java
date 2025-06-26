package gui;

import controller.Sistema;
import model.Prenotazione;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

/**
 * Finestra per la modifica dei dati di un biglietto.
 */
public class modificaBiglietto {
    private JTextField nomeField;       // campo per inserire nome passeggero
    private JTextField cognomeField;    // campo per inserire cognome passeggero
    private JTextField ciField;         // campo per inserire codice identificativo/documento
    private JTextField postoField;      // campo per inserire il posto assegnato
    private JButton invioButton;        // bottone per confermare la modifica
    private JPanel finestraPrincipale;  // pannello principale della finestra

    private final Sistema sistema;      // riferimento al sistema di controllo
    public final static JFrame frame  = new JFrame("Modifica Biglietto");  // frame della finestra
    private final DefaultTableModel tableModel;  // modello della tabella da aggiornare

    /*
     * Costruttore per la finestra di modifica del biglietto.
     */
    public modificaBiglietto(Sistema sistema, Prenotazione bigliettoDaModificare, DefaultTableModel tableModel, JFrame chiamante) {
        this.sistema = sistema;
        this.tableModel = tableModel;

        // Imposta il contenuto del frame con il pannello finestraPrincipale
        frame.setContentPane(finestraPrincipale);
        // Disabilita la chiusura automatica con la X (per gestirla manualmente)
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setSize(new Dimension(400, 300));
        frame.setLocationRelativeTo(null);  // centra la finestra
        frame.getRootPane().setDefaultButton(invioButton);  // imposta invioButton come default per ENTER
        frame.setVisible(true);

        // Riempie i campi con i dati attuali del biglietto da modificare
        nomeField.setText(bigliettoDaModificare.getPasseggero().getNome());
        cognomeField.setText(bigliettoDaModificare.getPasseggero().getCognome());
        ciField.setText(bigliettoDaModificare.getPasseggero().getNumeroDocumento());
        postoField.setText(bigliettoDaModificare.getPostoAssegnato());

        applyStyles();  // applica gli stili grafici

        // Gestore evento clic su invioButton per salvare modifiche
        invioButton.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            String cognome = cognomeField.getText().trim();
            String ci = ciField.getText().trim().toUpperCase();
            String posto = postoField.getText().trim().toUpperCase();

            // Controlla che nessun campo sia vuoto
            if (nome.isEmpty() || cognome.isEmpty() || ci.isEmpty() || posto.isEmpty()) {
                showError("Tutti i campi devono essere compilati.");
                return;
            }

            // Validazione formato codice identificativo (es. CC12345CC)
            if (!ci.matches("[A-Za-z]{2}[0-9]{5}[A-Za-z]{2}")) {
                showError("Formato CI non valido. Deve essere: CC12345CC");
                return;
            }

            // Aggiorna i dati del biglietto modificato
            bigliettoDaModificare.getPasseggero().setNome(nome);
            bigliettoDaModificare.getPasseggero().setCognome(cognome);
            bigliettoDaModificare.getPasseggero().setNumeroDocumento(ci);
            bigliettoDaModificare.setPostoAssegnato(posto);

            // Aggiorna la riga corrispondente nella tabella usando il numero biglietto come chiave
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 4).toString().equals(String.format("%10d", bigliettoDaModificare.getNumeroBiglietto()))) {
                    tableModel.setValueAt(nome, i, 0);
                    tableModel.setValueAt(cognome, i, 1);
                    tableModel.setValueAt(ci, i, 2);
                    tableModel.setValueAt(posto, i, 3);
                    break;
                }
            }

            // Rende visibile la finestra chiamante e chiude questa
            chiamante.setVisible(true);
            frame.dispose();
        });

        // Gestisce la chiusura della finestra con la X: riattiva chiamante e chiude frame
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                chiamante.setVisible(true);
                frame.dispose();
            }
        });
    }

    /**
     * Mostra un messaggio di errore in dialogo.
     * @param message testo dell'errore da mostrare
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Applica stili grafici ai componenti della finestra.
     */
    private void applyStyles() {
        Color background = new Color(245, 245, 245);
        Color borderColor = new Color(200, 200, 200);
        Color buttonColor = new Color(76, 175, 80);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

        finestraPrincipale.setBackground(background);
        finestraPrincipale.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Applica stile ai campi testo
        JTextField[] fields = {nomeField, cognomeField, ciField, postoField};
        for (JTextField field : fields) {
            field.setFont(fieldFont);
            field.setBackground(Color.WHITE);
            field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(borderColor),
                    new EmptyBorder(5, 8, 5, 8)
            ));
        }

        // Stile per il bottone invio
        invioButton.setFont(buttonFont);
        invioButton.setBackground(buttonColor);
        invioButton.setForeground(Color.WHITE);
        invioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        invioButton.setFocusPainted(false);
        invioButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }
}

package gui;

import controller.Sistema;
import model.Volo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AggiungiVolo {
    // Riferimento statico al controller del sistema
    static Sistema sistema;

    // Componenti dell'interfaccia
    private JLabel aggiungiVoloLable;
    private JLabel codiceVoloLable;
    private JTextField codiceVoloField;
    private JLabel compagniaAereaLable;
    private JTextField compagniaAereaField;
    private JLabel aeroportoOrigineLable;
    private JTextField aeroportoOrigineField;
    private JTextField destinazioneField;
    private JLabel destinazioneLable;
    private JLabel orarioArrivoLable;
    private JTextField orarioFIeld;
    private JLabel riatdoLable;
    private JTextField ritardoField;
    private JLabel statoVoloLable;
    private JComboBox statoVoloCombo;
    private JPanel principale;
    private JButton aggiungiVoloButton;
    private JButton annullaVoloButton;
    private JLabel gateLabel;
    private JTextField gateField;
    private JRadioButton arrivoButton;
    private JRadioButton partenzaButton;
    private JLabel voloInLable;
    private DefaultTableModel tableModel;
    private static final JFrame frame = new JFrame("Aggiungi Volo");
    private AdminPage adminPage;

    public JPanel getPrincipale(){
        return principale;
    }

    // Costruttore: inizializza componenti, imposta valori di default e listener
    public AggiungiVolo(DefaultTableModel tableModel, Sistema sistema, AdminPage adminPage) {
        this.sistema =sistema;
        this.tableModel = tableModel;
        this.adminPage = adminPage;
        final String aeroporto = "Capodichino"; // Aeroporto fisso per logica del progetto
        applyStyles();

        // Gruppo per i radio button (partenza/arrivo)
        ButtonGroup partenzaArrivo = new ButtonGroup();
        partenzaArrivo.add(partenzaButton);
        partenzaArrivo.add(arrivoButton);
        ritardoField.setText("0"); // Ritardo default a 0

        // Stato iniziale: partenza selezionata
        partenzaButton.setSelected(true);
        gateField.setEnabled(true);
        aeroportoOrigineField.setText(aeroporto);

        // Listener per pulsante "Arrivo"
        arrivoButton.addActionListener(e -> {
            gateField.setText("");
            gateField.setEnabled(false);
            gateLabel.setVisible(false);
            gateField.setVisible(false);
            destinazioneField.setText(aeroporto);
            aeroportoOrigineField.setText("");
        });

        // Listener per pulsante "Partenza"
        partenzaButton.addActionListener(e -> {
            gateField.setEnabled(true);
            gateLabel.setVisible(true);
            gateField.setVisible(true);
            aeroportoOrigineField.setText(aeroporto);
            destinazioneField.setText("");
        });

        // Listener per pulsanti
        aggiungiVoloButton.addActionListener(e -> salvaVolo());
        annullaVoloButton.addActionListener(e -> {
            JFrame finestra = (JFrame) principale.getTopLevelAncestor();
            if (finestra != null) {
                finestra.dispose();
            }
        });
    }

    // Salvataggio di un nuovo volo dopo la validazione dei dati
    private void salvaVolo() {
        // Recupero dei dati dai campi
        String codiceVolo = codiceVoloField.getText().trim();
        String compagniaAerea = compagniaAereaField.getText();
        String aeroportoOrigine = aeroportoOrigineField.getText();
        String aeroportoDestinazione = destinazioneField.getText();
        String ritardo = ritardoField.getText();
        String orario = orarioFIeld.getText();
        String gate = gateField.getText();
        String stato = (String) statoVoloCombo.getSelectedItem();
        Volo.statoVolo statoEnum = Volo.statoVolo.valueOf(stato);
        boolean partenzaButtonSelected = partenzaButton.isSelected();

        final String errore ="Errore";

        // Validazioni sui campi
        if (codiceVolo.isEmpty() || compagniaAerea.isEmpty() || aeroportoOrigine.isEmpty() || aeroportoDestinazione.isEmpty()
                || ritardo.isEmpty() || (partenzaButtonSelected && gate.isEmpty()) || stato == null || stato.isEmpty()) {
            JOptionPane.showMessageDialog(principale, "Tutti i campi devono essere riempiti", errore, JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!codiceVolo.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(principale, "Il codice volo deve essere un numero intero di 4 cifre", errore, JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!gate.matches("[A-Z][1-9]") && partenzaButtonSelected) {
            JOptionPane.showMessageDialog(principale, "Il codice gate contiene solo una lettera maiuscola e un numero intero positivo", errore, JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!orario.matches("([0-9]|1\\d|2[0-3]):[0-5]\\d")){
            JOptionPane.showMessageDialog(principale, "Inserire il formato corretto per l'orario HH:MM", errore, JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Controllo unicità codice volo nella tabella
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).toString().equals(codiceVolo)) {
                JOptionPane.showMessageDialog(principale, "Il codice volo deve essere univoco", errore, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            // Parsing e creazione oggetto volo
            int codice = Integer.parseInt(codiceVolo);
            Volo v = new Volo(codice, compagniaAerea, aeroportoOrigine, aeroportoDestinazione, orario, statoEnum, gate);

            sistema.aggiungiVolo(v); // Inserisce nel DB

            // Ricarica della tabella dal database per riflettere le modifiche
            DefaultTableModel model = (DefaultTableModel) adminPage.getTabellaVoli().getModel();
            model.setRowCount(0);

            for (Volo volo : sistema.visualizzaVoli()) {
                model.addRow(new Object[]{
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

            // Chiude la finestra dopo il salvataggio
            ((JFrame) principale.getTopLevelAncestor()).dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(principale, "Errore: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JButton getSalvaButton() {
        return aggiungiVoloButton;
    }

    // Applica gli stili all'interfaccia utente
    private void applyStyles() {
        // Palette colori coerente con l'interfaccia principale
        Color primaryBlue = new Color(0, 95, 135);
        Color background = new Color(245, 245, 245);
        Color successGreen = new Color(76, 175, 80);

        // Font utilizzati
        final String fontName = "Segoe UI";
        Font labelFont = new Font(fontName, Font.BOLD, 14);
        Font fieldFont = new Font(fontName, Font.PLAIN, 14);
        Font buttonFont = new Font(fontName, Font.BOLD, 14);

        // Stile generale del pannello principale
        principale.setBackground(background);
        principale.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Etichette
        for (JLabel label : new JLabel[]{
                aggiungiVoloLable, codiceVoloLable, compagniaAereaLable,
                aeroportoOrigineLable, destinazioneLable, orarioArrivoLable,
                riatdoLable, statoVoloLable, gateLabel, voloInLable
        }) {
            label.setFont(labelFont);
            label.setForeground(primaryBlue);
        }

        // Campi di testo
        for (JTextField field : new JTextField[]{
                codiceVoloField, compagniaAereaField, aeroportoOrigineField,
                destinazioneField, orarioFIeld, ritardoField, gateField
        }) {
            field.setFont(fieldFont);
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(204, 204, 204)),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
            field.setBackground(Color.WHITE);
        }

        // ComboBox per lo stato del volo
        statoVoloCombo.setFont(fieldFont);
        statoVoloCombo.setBackground(Color.WHITE);
        statoVoloCombo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204)),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));

        // Radio buttons
        for (JRadioButton rb : new JRadioButton[]{arrivoButton, partenzaButton}) {
            rb.setFont(fieldFont);
            rb.setBackground(background);
            rb.setFocusPainted(false);
        }

        // Pulsanti
        styleButton(aggiungiVoloButton, successGreen, Color.WHITE, buttonFont);
        styleButton(annullaVoloButton, background, primaryBlue, buttonFont);
        annullaVoloButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primaryBlue),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));

        // Titolo finestra
        aggiungiVoloLable.setFont(new Font("Segoe UI", Font.BOLD, 18));
        aggiungiVoloLable.setBorder(new EmptyBorder(0, 0, 10, 0));
    }

    // Metodo di supporto per applicare stile ai pulsanti
    private void styleButton(JButton button, Color bg, Color fg, Font font) {
        button.setFont(font);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public JButton getAggiungiVoloButton(){
        return aggiungiVoloButton;
    }
}

package gui;

import controller.Sistema;
import model.Volo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ModificaVolo {
    // Componenti GUI principali
    private JPanel principale;
    private JLabel modificaVoloLabel;
    private JLabel codiceVoloLabel;
    private JTextField codiceVoloField;
    private JLabel compagniaAereaLabel;
    private JTextField compagniaAereaField;
    private JLabel aeroportoOrigineLabel;
    private JTextField aeroportoOrigineField;
    private JLabel aeroportoDestinazioneLabel;
    private JTextField aeroportoDestinazioneField;
    private JLabel orarioDiArrivoLabel;
    private JTextField orarioField;
    private JLabel ritardoLabel;
    private JTextField ritardoField;
    private JLabel gateLabel;
    private JTextField gateField;
    private JLabel voloInLabel;
    private JRadioButton partenzaButton;
    private JRadioButton arrivoButton;
    private JComboBox<String> statoVoloCombo;
    private JLabel statoVoloLabel;
    private JPanel panelButton;
    private JButton salvaButton;
    private JButton salvaVoloButton;
    private JButton annullaButton;

    // Riferimenti a sistema e dati da modificare
    private Sistema sistema;
    private DefaultTableModel tableModel;
    private Volo voloModificato;

    // Costruttore che inizializza i dati e la GUI
    public ModificaVolo(DefaultTableModel tableModel, Sistema sistema, Volo voloDaModificare) {
        this.tableModel = tableModel;
        this.voloModificato = voloDaModificare;
        this.sistema = sistema;

        caricaDatiVolo();   // Carica i dati del volo nella GUI
        applyStyles();      // Applica stile ai componenti
        aggiungiAzioni();   // Aggiunge i listener per i bottoni e radio
    }

    // Inserisce i dati del volo nei campi di testo della GUI
    private void caricaDatiVolo() {
        codiceVoloField.setText(String.format("%04d", voloModificato.getCodiceVolo())); // Codice a 4 cifre con zeri
        compagniaAereaField.setText(voloModificato.getCompagniaAerea());
        aeroportoOrigineField.setText(voloModificato.getAeroportoOrigine());
        aeroportoDestinazioneField.setText(voloModificato.getAeroportoDestinazione());
        orarioField.setText(voloModificato.getOrarioArrivo());
        ritardoField.setText(String.valueOf(voloModificato.getRitardo()));
        gateField.setText(voloModificato.getGate());
        statoVoloCombo.setSelectedItem(voloModificato.getStato().toString());

        // Se il gate è vuoto, significa volo in arrivo, altrimenti partenza
        if (voloModificato.getGate() == null || voloModificato.getGate().isEmpty()) {
            impostaComeArrivo(voloModificato);
        } else {
            impostaComePartenza(voloModificato);
        }
    }

    // Imposta i listener ai bottoni e gruppi radio
    private void aggiungiAzioni() {
        ButtonGroup partenzaArrivo = new ButtonGroup();
        partenzaArrivo.add(partenzaButton);
        partenzaArrivo.add(arrivoButton);

        // Se seleziono arrivo, modifico i campi di conseguenza
        arrivoButton.addActionListener(e -> impostaComeArrivo(voloModificato));
        // Se seleziono partenza, modifico i campi di conseguenza
        partenzaButton.addActionListener(e -> impostaComePartenza(voloModificato));
        // Salva dati volo
        salvaButton.addActionListener(e -> salvaVolo());
        // Annulla e chiudi finestra
        annullaButton.addActionListener(e -> chiudiFinestra());
    }

    // Imposta i campi per volo in arrivo: disabilita e nasconde campo gate
    private void impostaComeArrivo(Volo volo) {
        arrivoButton.setSelected(true);
        gateField.setEnabled(false);
        gateField.setVisible(false);
        gateField.setText("");
        gateLabel.setVisible(false);
        aeroportoDestinazioneField.setText("Capodichino");  // Aeroporto di destinazione fisso per arrivi
        aeroportoOrigineField.setText(volo.getAeroportoOrigine());
    }

    // Imposta i campi per volo in partenza: abilita e mostra campo gate
    private void impostaComePartenza(Volo volo) {
        partenzaButton.setSelected(true);
        gateField.setEnabled(true);
        gateField.setVisible(true);
        gateField.setText(volo.getGate());
        gateLabel.setVisible(true);
        aeroportoOrigineField.setText("Capodichino");  // Aeroporto di origine fisso per partenze
        aeroportoDestinazioneField.setText(volo.getAeroportoDestinazione());
    }

    // Salva i dati modificati del volo dopo validazione
    private void salvaVolo() {
        // Prendo i dati dai campi di testo
        String codiceVolo = codiceVoloField.getText().trim();
        String compagnia = compagniaAereaField.getText().trim();
        String origine = aeroportoOrigineField.getText().trim();
        String destinazione = aeroportoDestinazioneField.getText().trim();
        String orario = orarioField.getText().trim();
        String ritardo = ritardoField.getText().trim();
        String gate = gateField.getText().trim();
        String stato = (String) statoVoloCombo.getSelectedItem();

        // Validazione: se non va bene, esco
        if (!convalidaCampi(codiceVolo, compagnia, origine, destinazione, orario, ritardo, gate, stato)) return;

        try {
            // Converto codice e ritardo in numeri
            int codice = Integer.parseInt(codiceVolo);
            int rit = Integer.parseInt(ritardo);

            // Aggiorno dati del volo con quelli inseriti
            voloModificato.setCodiceVolo(codice);
            voloModificato.setCompagniaAerea(compagnia);
            voloModificato.setAeroportoOrigine(origine);
            voloModificato.setAeroportoDestinazione(destinazione);
            voloModificato.setOrarioArrivo(orario);
            voloModificato.setRitardo(rit);
            voloModificato.setGate(gate);
            voloModificato.setStato(Volo.statoVolo.valueOf(stato));

            aggiornaTabella(codice);          // Aggiorno la tabella nella GUI
            sistema.modificaVolo(voloModificato); // Modifico anche nel sistema (database o memoria)
            chiudiFinestra();                 // Chiudo la finestra dopo salvataggio
        } catch (Exception e) {
            // Se errore, mostro messaggio
            JOptionPane.showMessageDialog(principale, "Errore nei dati inseriti", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Controlla che i dati inseriti siano validi, mostra messaggi di errore se no
    private boolean convalidaCampi(String codice, String compagnia, String origine, String destinazione,
                                   String orario, String ritardo, String gate, String stato) {
        // Controllo campi vuoti, gate obbligatorio se volo in partenza
        if (codice.isEmpty() || compagnia.isEmpty() || origine.isEmpty() || destinazione.isEmpty() ||
                orario.isEmpty() || ritardo.isEmpty() || (partenzaButton.isSelected() && gate.isEmpty()) ||
                stato == null || stato.isEmpty()) {
            JOptionPane.showMessageDialog(principale, "Tutti i campi devono essere compilati", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Controllo codice volo: deve essere 4 cifre numeriche
        if (!codice.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(principale, "Codice volo non valido", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Controllo formato orario HH:mm
        if (!orario.matches("([01]?\\d|2[0-3]):[0-5]\\d")) {
            JOptionPane.showMessageDialog(principale, "Formato orario errato (HH:mm)", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Controllo formato gate (es. A1) se partenza
        if (!gate.matches("[A-Z][1-9]") && partenzaButton.isSelected()) {
            JOptionPane.showMessageDialog(principale, "Formato gate errato (es. A1)", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Controllo unicità codice volo in tabella (escludendo volo modificato)
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String codiceTabella = tableModel.getValueAt(i, 0).toString();
            if (codiceTabella.equals(codice) && voloModificato.getCodiceVolo() != Integer.parseInt(codice)) {
                JOptionPane.showMessageDialog(principale, "Codice volo già esistente", "Errore", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true; // Tutto ok
    }

    // Aggiorna la tabella con i nuovi dati inseriti
    private void aggiornaTabella(int codice) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            // Trova riga corrispondente al codice volo
            if (tableModel.getValueAt(i, 0).toString().equals(String.format("%04d", codice))) {
                tableModel.setValueAt(String.format("%04d", codice), i, 0);
                tableModel.setValueAt(compagniaAereaField.getText(), i, 1);
                tableModel.setValueAt(aeroportoOrigineField.getText(), i, 2);
                tableModel.setValueAt(aeroportoDestinazioneField.getText(), i, 3);
                tableModel.setValueAt(orarioField.getText(), i, 4);
                tableModel.setValueAt(ritardoField.getText(), i, 5);
                tableModel.setValueAt(gateField.getText(), i, 6);
                tableModel.setValueAt(statoVoloCombo.getSelectedItem(), i, 7);
                break; // trovato e aggiornato, esco
            }
        }
    }

    // Chiude la finestra/modale di modifica volo
    private void chiudiFinestra() {
        JFrame frame = (JFrame) principale.getTopLevelAncestor();
        if (frame != null) frame.dispose();
    }

    // Getter per il pannello principale da aggiungere in un JFrame o JDialog
    public JPanel getPrincipale() {
        return principale;
    }

    // Getter per il bottone salva per esempio per impostare default button
    public JButton getSalvaButton() {
        return salvaButton;
    }

    // Imposta il bottone salva come default button della finestra (es. invio)
    public void impostaDefaultButton() {
        JFrame frame = (JFrame) principale.getTopLevelAncestor();
        if (frame != null) frame.getRootPane().setDefaultButton(salvaButton);
    }

    // Applica i colori, font e bordi agli elementi GUI
    private void applyStyles() {
        // Palette colori usata
        Color primaryBlue = new Color(0, 95, 135);
        Color secondaryBlue = new Color(0, 120, 167);
        Color background = new Color(245, 245, 245);
        Color errorRed = new Color(231, 76, 60);
        Color successGreen = new Color(76, 175, 80);

        // Font utilizzati
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

        // Imposta colore e bordo al pannello principale
        principale.setBackground(background);
        principale.setBorder(new EmptyBorder(15, 15, 15, 15));
        principale.setBackground(background);

        // Imposta font e colore etichette
        for (JLabel label : new JLabel[]{
                modificaVoloLabel, codiceVoloLabel, compagniaAereaLabel,
                aeroportoOrigineLabel, aeroportoDestinazioneLabel, orarioDiArrivoLabel,
                ritardoLabel, gateLabel, voloInLabel, statoVoloLabel
        }) {
            label.setFont(labelFont);
            label.setForeground(primaryBlue);
        }

        // Imposta font, bordi e sfondo campi di testo
        for (JTextField field : new JTextField[]{
                codiceVoloField, compagniaAereaField, aeroportoOrigineField,
                aeroportoDestinazioneField, orarioField, ritardoField, gateField
        }) {
            field.setFont(fieldFont);
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(204, 204, 204)),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
            field.setBackground(Color.WHITE);
        }

        // Imposta font e colori bottoni
        for (JButton button : new JButton[]{salvaButton, annullaButton}) {
            button.setFont(buttonFont);
            button.setBackground(secondaryBlue);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        }

        // Radio button colore e font
        for (JRadioButton radio : new JRadioButton[]{partenzaButton, arrivoButton}) {
            radio.setFont(fieldFont);
            radio.setForeground(primaryBlue);
            radio.setBackground(background);
        }

        // Combo box stile semplice
        statoVoloCombo.setFont(fieldFont);
    }
}

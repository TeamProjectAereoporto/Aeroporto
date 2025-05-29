package gui;

import controller.Sistema;
import model.Volo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ModificaVolo {
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
    private Sistema sistema;
    private DefaultTableModel tableModel;
    private Volo voloModificato;
    public ModificaVolo() {}
    public ModificaVolo(DefaultTableModel tableModel, Sistema sistema, Volo voloDaModificare) {
        this.tableModel = tableModel;
        this.voloModificato = voloDaModificare;
        this.sistema = sistema;
        ButtonGroup partenzaArrivo = new ButtonGroup();
        partenzaArrivo.add(partenzaButton);
        partenzaArrivo.add(arrivoButton);
        // Carica i dati del volo nei campi
        codiceVoloField.setText(String.format("%04d", voloDaModificare.getCodiceVolo()));
        compagniaAereaField.setText(voloDaModificare.getCompagniaAerea());
        aeroportoOrigineField.setText(voloDaModificare.getAeroportoOrigine());
        aeroportoDestinazioneField.setText(voloDaModificare.getAeroportoDestinazione());
        orarioField.setText(voloDaModificare.getOrarioArrivo());
        ritardoField.setText(String.valueOf(voloDaModificare.getRitardo()));
        gateField.setText(voloDaModificare.getGate());

        statoVoloCombo.setSelectedItem(voloDaModificare.getStato().toString());
        if (voloDaModificare.getGate() == null || voloDaModificare.getGate().isEmpty()) {
            impostaComeArrivo();
        } else {
            impostaComePartenza();
        }
        applyStyles();
        arrivoButton.addActionListener(e -> impostaComeArrivo());
        partenzaButton.addActionListener(e -> impostaComePartenza());



        salvaButton.addActionListener(e -> salvaVolo());
        annullaButton.addActionListener(e -> {
            JFrame finestra = (JFrame) principale.getTopLevelAncestor();
            if (finestra != null) {
                finestra.dispose();
            }
        });


    }

    public JButton getSalvaButton() {
        return salvaButton;
    }

    public void impostaDefaultButton() {
        JFrame frame = (JFrame) principale.getTopLevelAncestor();
        if (frame != null) {
            frame.getRootPane().setDefaultButton(salvaButton);
        }
    }

    public JPanel getPrincipale() {
        return principale;
    }

    private void impostaComeArrivo() {
        arrivoButton.setSelected(true);
        gateField.setEnabled(false);
        gateField.setText("");
        gateLabel.setVisible(false);
        gateField.setVisible(false);
        aeroportoDestinazioneField.setText("Capodichino");
        aeroportoOrigineField.setText("");
    }

    private void impostaComePartenza() {
        partenzaButton.setSelected(true);
        gateField.setEnabled(true);
        gateLabel.setVisible(true);
        gateField.setVisible(true);
        aeroportoOrigineField.setText("Capodichino");
        aeroportoDestinazioneField.setText("");
    }

    private void salvaVolo() {
        String codiceVolo = codiceVoloField.getText().trim();
        String compagniaAerea = compagniaAereaField.getText();
        String aeroportoOrigine = aeroportoOrigineField.getText();
        String aeroportoDestinazione = aeroportoDestinazioneField.getText();
        String orario = orarioField.getText();
        String ritardo = ritardoField.getText();
        String gate = gateField.getText();
        String stato = (String) statoVoloCombo.getSelectedItem();
        Volo.statoVolo statoEnum = Volo.statoVolo.valueOf(stato);


        boolean partenzaButtonSelected = partenzaButton.isSelected();

        if (codiceVolo.isEmpty() || compagniaAerea.isEmpty() || aeroportoOrigine.isEmpty() || aeroportoDestinazione.isEmpty()
                || ritardo.isEmpty() || (partenzaButtonSelected && gate.isEmpty()) || stato == null || stato.isEmpty()) {
            JOptionPane.showMessageDialog(principale, "Tutti i campi devono essere riempiti", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!codiceVolo.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(principale, "Il codice volo deve essere un numero intero di 4 cifre", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!gate.matches("[A-Z][1-9]")&& partenzaButtonSelected) {
            JOptionPane.showMessageDialog(principale, "Il codice gate contiene solo una lettera maiuscola e un numero intero positivo", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!orario.matches("([0-9]|1\\d|2[0-3]):[0-5]\\d")){
            JOptionPane.showMessageDialog(principale, "Inserire il formato corretto per l'orario HH:MM", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String codiceInTabella = tableModel.getValueAt(i, 0).toString();
            if (codiceInTabella.equals(codiceVolo) && voloModificato.getCodiceVolo() != Integer.parseInt(codiceVolo)) {
                JOptionPane.showMessageDialog(principale, "Il codice volo deve essere univoco", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            int codice = Integer.parseInt(codiceVolo);
            int rit = Integer.parseInt(ritardo);

            voloModificato.setCodiceVolo(codice);
            voloModificato.setCompagniaAerea(compagniaAerea);
            voloModificato.setAeroportoOrigine(aeroportoOrigine);
            voloModificato.setAeroportoDestinazione(aeroportoDestinazione);
            voloModificato.setOrarioArrivo(orario);
            voloModificato.setRitardo(rit);
            voloModificato.setGate(gate);
            voloModificato.setStato(statoEnum);

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).toString().equals(String.format("%04d", codice))) {
                    tableModel.setValueAt(codiceVolo, i, 0);
                    tableModel.setValueAt(compagniaAerea, i, 1);
                    tableModel.setValueAt(aeroportoOrigine, i, 2);
                    tableModel.setValueAt(aeroportoDestinazione, i, 3);
                    tableModel.setValueAt(orario, i, 4);
                    tableModel.setValueAt(ritardo, i, 5);
                    tableModel.setValueAt(gate, i, 6);
                    tableModel.setValueAt(stato, i, 7);
                    break;
                }
            }
            for(Volo v: sistema.visualizzaVoli()){
                System.out.println(v);
            }
            JFrame finestra = (JFrame) principale.getTopLevelAncestor();
            if (finestra != null) {
                finestra.dispose();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(principale, "Ritardo o codice volo non validi", "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(principale, "Stato del volo non valido", "Errore", JOptionPane.ERROR_MESSAGE);


        }
    }

    private void applyStyles() {
        // Palette colori aeroportuale
        Color primaryBlue = new Color(0, 95, 135);
        Color secondaryBlue = new Color(0, 120, 167);
        Color background = new Color(245, 245, 245);
        Color errorRed = new Color(231, 76, 60);
        Color successGreen = new Color(76, 175, 80);

        // Font
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

        // Stile generale
        principale.setBackground(background);
        principale.setBorder(new EmptyBorder(15, 15, 15, 15));
        principale.setBackground(background);

        // Stile etichette
        for (JLabel label : new JLabel[]{
                modificaVoloLabel, codiceVoloLabel, compagniaAereaLabel,
                aeroportoOrigineLabel, aeroportoDestinazioneLabel, orarioDiArrivoLabel,
                ritardoLabel, gateLabel, voloInLabel, statoVoloLabel
        }) {
            label.setFont(labelFont);
            label.setForeground(primaryBlue);
        }

        // Stile campi testo
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

        // Stile combo box
        statoVoloCombo.setFont(fieldFont);
        statoVoloCombo.setBackground(Color.WHITE);
        statoVoloCombo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204)),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));

        // Stile radio buttons
        for (JRadioButton rb : new JRadioButton[]{arrivoButton, partenzaButton}) {
            rb.setFont(fieldFont);
            rb.setBackground(background);
            rb.setFocusPainted(false);
        }

        // Stile pulsanti
        styleButton(salvaButton, successGreen, Color.WHITE, buttonFont);
        styleButton(annullaButton, background, primaryBlue, buttonFont);
        annullaButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primaryBlue),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));

        // Titolo finestra
        modificaVoloLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        modificaVoloLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
    }

    private void styleButton(JButton button, Color bg, Color fg, Font font) {
        button.setFont(font);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
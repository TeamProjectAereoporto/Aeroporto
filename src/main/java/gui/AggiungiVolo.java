package gui;

import controller.Sistema;
import model.Volo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AggiungiVolo {
    static Sistema sistema;
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
    private JComboBox<String> statoVoloCombo;
    private JPanel principale;
    private JButton aggiungiVoloButton;
    private JButton annullaVoloButton;
    private JLabel gateLabel;
    private JTextField gateField;
    private JRadioButton arrivoButton;
    private JRadioButton partenzaButton;
    private JLabel voloInLable;
    private JLabel dataVoloLabel;
    private JTextField dataVoloField;
    private DefaultTableModel tableModelArrivi;
    private DefaultTableModel tableModelPartenze;
    private static final JFrame frame = new JFrame("Aggiungi Volo");
    private AdminPage adminPage;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public JPanel getPrincipale() {
        return principale;
    }

    public AggiungiVolo(DefaultTableModel tableModelArrivi, DefaultTableModel tableModelPartenze, Sistema sistema, AdminPage adminPage) {
        this.sistema = sistema;
        this.tableModelArrivi = tableModelArrivi;
        this.tableModelPartenze = tableModelPartenze;
        this.adminPage = adminPage;

        final String aeroporto = "Capodichino";
        applyStyles();

        ButtonGroup partenzaArrivo = new ButtonGroup();
        partenzaArrivo.add(partenzaButton);
        partenzaArrivo.add(arrivoButton);
        ritardoField.setText("0");

        // Imposta la data corrente come default
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        dataVoloField.setText(sdf.format(new Date()));

        partenzaButton.setSelected(true);
        gateField.setEnabled(true);
        aeroportoOrigineField.setText(aeroporto);

        arrivoButton.addActionListener(e -> {
            gateField.setText("");
            gateField.setEnabled(false);
            gateLabel.setVisible(false);
            gateField.setVisible(false);
            destinazioneField.setText(aeroporto);
            aeroportoOrigineField.setText("");
        });

        partenzaButton.addActionListener(e -> {
            gateField.setEnabled(true);
            gateLabel.setVisible(true);
            gateField.setVisible(true);
            aeroportoOrigineField.setText(aeroporto);
            destinazioneField.setText("");
        });

        aggiungiVoloButton.addActionListener(this::salvaVolo);
        annullaVoloButton.addActionListener(e -> {
            JFrame finestra = (JFrame) principale.getTopLevelAncestor();
            if (finestra != null) {
                finestra.dispose();
            }
        });
    }

    private void salvaVolo(ActionEvent e) {
        String codiceVolo = codiceVoloField.getText().trim();
        String compagniaAerea = compagniaAereaField.getText();
        String aeroportoOrigine = aeroportoOrigineField.getText();
        String aeroportoDestinazione = destinazioneField.getText();
        String ritardo = ritardoField.getText();
        String orario = orarioFIeld.getText();
        String gate = gateField.getText();
        String stato = (String) statoVoloCombo.getSelectedItem();
        String dataVoloStr = dataVoloField.getText().trim();
        Volo.statoVolo statoEnum = Volo.statoVolo.valueOf(stato);
        boolean partenzaButtonSelected = partenzaButton.isSelected();

        final String errore = "Errore";

        if (codiceVolo.isEmpty() || compagniaAerea.isEmpty() || aeroportoOrigine.isEmpty() ||
                aeroportoDestinazione.isEmpty() || ritardo.isEmpty() || dataVoloStr.isEmpty() ||
                (partenzaButtonSelected && gate.isEmpty()) || stato == null || stato.isEmpty()) {
            JOptionPane.showMessageDialog(principale, "Tutti i campi devono essere riempiti", errore, JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!codiceVolo.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(principale, "Il codice volo deve essere un numero intero di 4 cifre", errore, JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (partenzaButtonSelected && !gate.matches("[A-Z][1-9]")) {
            JOptionPane.showMessageDialog(principale, "Il codice gate contiene solo una lettera maiuscola e un numero intero positivo", errore, JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!orario.matches("([0-9]|1\\d|2[0-3]):[0-5]\\d")) {
            JOptionPane.showMessageDialog(principale, "Inserire il formato corretto per l'orario HH:MM", errore, JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!dataVoloStr.matches("(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}")) {
            JOptionPane.showMessageDialog(principale, "Inserire il formato corretto per la data DD/MM/YYYY", errore, JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Controllo unicità codice volo in entrambe le tabelle
        if (codiceVoloEsiste(codiceVolo)) {
            JOptionPane.showMessageDialog(principale, "Il codice volo deve essere univoco", errore, JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int codice = Integer.parseInt(codiceVolo);
            int ritardoInt = Integer.parseInt(ritardo);
            LocalDate dataVolo = LocalDate.parse(dataVoloStr, dateFormatter);
            Volo v = new Volo(codice, compagniaAerea, aeroportoOrigine, aeroportoDestinazione, orario, ritardoInt, statoEnum, gate, dataVolo);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            sistema.aggiungiVolo(v);
            System.out.println("data: "+adminPage.getDataVolo().toString());
            if(formatter.format(adminPage.getDataVolo()).toString().equalsIgnoreCase(dataVoloStr)) {
                // Aggiorna la tabella corretta in base al tipo di volo
                if (partenzaButtonSelected) {
                    tableModelPartenze.addRow(new Object[]{
                            v.getCodiceVolo(),
                            v.getCompagniaAerea(),
                            v.getAeroportoOrigine(),
                            v.getAeroportoDestinazione(),
                            v.getOrarioArrivo(),
                            v.getDataVolo(),
                            v.getRitardo(),
                            v.getStato().toString(),
                            v.getGate()
                    });
                } else {
                    tableModelArrivi.addRow(new Object[]{
                            v.getCodiceVolo(),
                            v.getCompagniaAerea(),
                            v.getAeroportoOrigine(),
                            v.getAeroportoDestinazione(),
                            v.getOrarioArrivo(),
                            v.getRitardo(),
                            v.getStato().toString(),
                            v.getGate(),
                            v.getDataVolo()
                    });
                }
            }
            ((JFrame) principale.getTopLevelAncestor()).dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(principale, "Errore: " + ex.getMessage(), errore, JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean codiceVoloEsiste(String codiceVolo) {
        for (int i = 0; i < tableModelArrivi.getRowCount(); i++) {
            if (tableModelArrivi.getValueAt(i, 0).toString().equals(codiceVolo)) {
                return true;
            }
        }
        for (int i = 0; i < tableModelPartenze.getRowCount(); i++) {
            if (tableModelPartenze.getValueAt(i, 0).toString().equals(codiceVolo)) {
                return true;
            }
        }
        return false;
    }

    public JButton getAggiungiVoloButton() {
        return aggiungiVoloButton;
    }

    private void applyStyles() {
        Color primaryBlue = new Color(0, 95, 135);
        Color background = new Color(245, 245, 245);
        Color successGreen = new Color(76, 175, 80);

        final String fontName = "Segoe UI";
        Font labelFont = new Font(fontName, Font.BOLD, 14);
        Font fieldFont = new Font(fontName, Font.PLAIN, 14);
        Font buttonFont = new Font(fontName, Font.BOLD, 14);

        principale.setBackground(background);
        principale.setBorder(new EmptyBorder(15, 15, 15, 15));

        for (JLabel label : new JLabel[]{
                aggiungiVoloLable, codiceVoloLable, compagniaAereaLable,
                aeroportoOrigineLable, destinazioneLable, orarioArrivoLable,
                riatdoLable, statoVoloLable, gateLabel, voloInLable, dataVoloLabel
        }) {
            label.setFont(labelFont);
            label.setForeground(primaryBlue);
        }

        for (JTextField field : new JTextField[]{
                codiceVoloField, compagniaAereaField, aeroportoOrigineField,
                destinazioneField, orarioFIeld, ritardoField, gateField, dataVoloField
        }) {
            field.setFont(fieldFont);
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(204, 204, 204)),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));
            field.setBackground(Color.WHITE);
        }

        statoVoloCombo.setFont(fieldFont);
        statoVoloCombo.setBackground(Color.WHITE);
        statoVoloCombo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204)),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));

        for (JRadioButton rb : new JRadioButton[]{arrivoButton, partenzaButton}) {
            rb.setFont(fieldFont);
            rb.setBackground(background);
            rb.setFocusPainted(false);
        }

        styleButton(aggiungiVoloButton, successGreen, Color.WHITE, buttonFont);
        styleButton(annullaVoloButton, background, primaryBlue, buttonFont);
        annullaVoloButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primaryBlue),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));

        aggiungiVoloLable.setFont(new Font("Segoe UI", Font.BOLD, 18));
        aggiungiVoloLable.setBorder(new EmptyBorder(0, 0, 10, 0));
    }

    private void styleButton(JButton button, Color bg, Color fg, Font font) {
        button.setFont(font);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public JButton getSalvaButton() {
        return aggiungiVoloButton;
    }
}
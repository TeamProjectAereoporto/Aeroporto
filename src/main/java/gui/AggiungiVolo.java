package gui;

import controller.Sistema;
import model.Volo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

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

    public JPanel getPrincipale(){
        return principale;
    }

    public AggiungiVolo(DefaultTableModel tableModel, Sistema sistema) {
        this.sistema=sistema;
        this.tableModel = tableModel;
        final String aeroporto = "Capodichino";
        applyStyles();

        ButtonGroup partenzaArrivo = new ButtonGroup();
        partenzaArrivo.add(partenzaButton);
        partenzaArrivo.add(arrivoButton);
        ritardoField.setText("0");

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

        aggiungiVoloButton.addActionListener(e -> salvaVolo());
        annullaVoloButton.addActionListener(e -> {
            JFrame finestra = (JFrame) principale.getTopLevelAncestor();
            if (finestra != null) {
                finestra.dispose();
            }
        });
    }

    public static void main(String[] args) {

        String[] colonne = {"Codice", "Destinazione", "Compagnia", "Origine", "Orario", "Ritardo", "Gate", "Stato"};
        DefaultTableModel modelloFinto = new DefaultTableModel(colonne, 0);
        AggiungiVolo av = new AggiungiVolo(modelloFinto, sistema);
        JFrame frame = new JFrame("Aggiungi Volo");
        frame.setContentPane(av.getPrincipale());
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setSize(700, 500);
        frame.setLocation(400, 150);
        frame.setVisible(true);
    }


    private void salvaVolo() {
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

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).toString().equals(codiceVolo)) {
                JOptionPane.showMessageDialog(principale, "Il codice volo deve essere univoco", errore, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            int codice = Integer.parseInt(codiceVolo);

            Volo v = new Volo(codice, compagniaAerea, aeroportoOrigine, aeroportoDestinazione, orario, statoEnum, gate);

            sistema.aggiungiVolo(v);

            tableModel.addRow(new Object[]{
                    codiceVolo,
                    compagniaAerea,
                    aeroportoOrigine,
                    aeroportoDestinazione,
                    orario,
                    ritardo,
                    stato,
                    gate
            });

            ((JFrame) principale.getTopLevelAncestor()).dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(principale, "Il codice volo non è un numero valido", errore, JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(principale, "Stato del volo non valido", errore, JOptionPane.ERROR_MESSAGE);
        }
    }

    public JButton getSalvaButton() {
        return aggiungiVoloButton;
    }
    private void applyStyles() {
        // Palette colori aeroportuale
        Color primaryBlue = new Color(0, 95, 135);
        Color background = new Color(245, 245, 245);
        Color successGreen = new Color(76, 175, 80);

        // Font
        final String fontName = "Segoe UI";
        Font labelFont = new Font(fontName, Font.BOLD, 14);
        Font fieldFont = new Font(fontName, Font.PLAIN, 14);
        Font buttonFont = new Font(fontName, Font.BOLD, 14);

        // Stile generale
        principale.setBackground(background);
        principale.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Stile etichette
        for (JLabel label : new JLabel[]{
                aggiungiVoloLable, codiceVoloLable, compagniaAereaLable,
                aeroportoOrigineLable, destinazioneLable, orarioArrivoLable,
                riatdoLable, statoVoloLable, gateLabel, voloInLable
        }) {
            label.setFont(labelFont);
            label.setForeground(primaryBlue);
        }

        // Stile campi testo
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

    private void styleButton(JButton button, Color bg, Color fg, Font font) {
        button.setFont(font);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
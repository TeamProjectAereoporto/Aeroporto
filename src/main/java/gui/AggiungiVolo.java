package gui;

import controller.Sistema;
import model.Volo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
    private JButton salvaModificheButton;
    private JButton annullaModificheButton;
    private JLabel gateLabel;
    private JTextField gateField;
    private JRadioButton arrivoButton;
    private JRadioButton partenzaButton;
    private JLabel voloInLable;
    private DefaultTableModel tableModel;

    public JPanel getPrincipale(){
        return principale;
    }

    public AggiungiVolo(DefaultTableModel tableModel, Sistema sistema) {
        this.sistema=sistema;
        this.tableModel = tableModel;
        ButtonGroup partenzaArrivo = new ButtonGroup();
        partenzaArrivo.add(partenzaButton);
        partenzaArrivo.add(arrivoButton);

        partenzaButton.setSelected(true);
        gateField.setEnabled(true);
        aeroportoOrigineField.setText("Capodichino");

        arrivoButton.addActionListener(e -> {
            gateField.setText("");
            gateField.setEnabled(false);
            gateLabel.setVisible(false);
            gateField.setVisible(false);
            destinazioneField.setText("Capodichino");
            aeroportoOrigineField.setText("");
        });
        partenzaButton.addActionListener(e -> {
            gateField.setEnabled(true);
            gateLabel.setVisible(true);
            gateField.setVisible(true);
            aeroportoOrigineField.setText("Capodichino");
            destinazioneField.setText("");
        });

        salvaModificheButton.addActionListener(e -> salvaVolo());
        annullaModificheButton.addActionListener(e -> {
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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(700, 340);
        frame.setLocation(400, 300);
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

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).toString().equals(codiceVolo)) {
                JOptionPane.showMessageDialog(principale, "Il codice volo deve essere univoco", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            int codice = Integer.parseInt(codiceVolo);
            Volo.statoVolo statoEnum = Volo.statoVolo.valueOf(stato);  // lo stato deve combaciare con l'enum

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
            JOptionPane.showMessageDialog(principale, "Il codice volo non è un numero valido", "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(principale, "Stato del volo non valido", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

}
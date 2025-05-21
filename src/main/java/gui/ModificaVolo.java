package gui;

import controller.Sistema;
import model.Volo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ModificaVolo {
    private JPanel rootPanel;
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

    private DefaultTableModel tableModel;
    private Volo voloModificato;

    public ModificaVolo(DefaultTableModel tableModel, Sistema sistema, Volo voloDaModificare) {

        this.tableModel = tableModel;
        this.voloModificato = voloDaModificare;

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

        // Imposta radio button e visibilità gate in base al volo
        if (voloDaModificare.getGate() == null || voloDaModificare.getGate().isEmpty()) {
            arrivoButton.setSelected(true);
            gateLabel.setVisible(false);
            gateField.setVisible(false);
            gateField.setEnabled(false);
        } else {
            partenzaButton.setSelected(true);
            gateLabel.setVisible(true);
            gateField.setVisible(true);
            gateField.setEnabled(true);
        }

        arrivoButton.addActionListener(e -> {
            gateField.setText("");
            gateLabel.setVisible(false);
            gateField.setVisible(false);
            gateField.setEnabled(false);
            aeroportoDestinazioneField.setText("Capodichino");
            aeroportoOrigineField.setText("");
        });

        partenzaButton.addActionListener(e -> {
            gateLabel.setVisible(true);
            gateField.setVisible(true);
            gateField.setEnabled(true);
            aeroportoOrigineField.setText("Capodichino");
            aeroportoDestinazioneField.setText("");
        });

        salvaButton.addActionListener(e -> salvaVolo());
        annullaButton.addActionListener(e -> {
            JFrame finestra = (JFrame) principale.getTopLevelAncestor();
            if (finestra != null) {
                finestra.dispose();
            }
        });
    }

    public JPanel getPrincipale() {
        return principale;
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
            String codiceInTabella = tableModel.getValueAt(i, 0).toString();
            if (codiceInTabella.equals(codiceVolo) && voloModificato.getCodiceVolo() != Integer.parseInt(codiceVolo)) {
                JOptionPane.showMessageDialog(principale, "Il codice volo deve essere univoco", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            int codice = Integer.parseInt(codiceVolo);
            int rit = Integer.parseInt(ritardo);
            Volo.statoVolo statoEnum = Volo.statoVolo.valueOf(stato);

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
}


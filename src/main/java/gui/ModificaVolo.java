package gui;

import controller.Sistema;
import model.Volo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
    private JButton annullaButton;
    private Sistema sistema;
    private DefaultTableModel tableModel;
    private Volo voloModificato;

    public ModificaVolo(DefaultTableModel tableModel, Sistema sistema, Volo voloDaModificare) {

        this.tableModel = tableModel;
        this.voloModificato = voloDaModificare;
        this.sistema = sistema;


        ButtonGroup partenzaArrivo = new ButtonGroup();
        partenzaArrivo.add(partenzaButton);
        partenzaArrivo.add(arrivoButton);

        codiceVoloField.setText(String.format("%04d", voloDaModificare.getCodiceVolo()));
        compagniaAereaField.setText(voloDaModificare.getCompagniaAerea());
        aeroportoOrigineField.setText(voloDaModificare.getAeroportoOrigine());
        aeroportoDestinazioneField.setText(voloDaModificare.getAeroportoDestinazione());
        orarioField.setText(voloDaModificare.getOrarioArrivo());
        ritardoField.setText(String.valueOf(voloDaModificare.getRitardo()));
        gateField.setText(voloDaModificare.getGate());
        statoVoloCombo.setSelectedItem(voloDaModificare.getStato().toString());

        // Aggiungi questo debug prima di impostare i valori
        System.out.println("DEBUG - Dati volo:");
        System.out.println("Codice: " + voloDaModificare.getCodiceVolo());
        System.out.println("Compagnia: " + voloDaModificare.getCompagniaAerea());
        System.out.println("Origine: " + voloDaModificare.getAeroportoOrigine());
        System.out.println("Destinazione: " + voloDaModificare.getAeroportoDestinazione());
        System.out.println("Orario arrivo: " + voloDaModificare.getOrarioArrivo());
        System.out.println("Ritardo: " + voloDaModificare.getRitardo());
        System.out.println("Gate: " + voloDaModificare.getGate());
        System.out.println("Stato: " + voloDaModificare.getStato());


        if (voloDaModificare.getGate() == null || voloDaModificare.getGate().isEmpty()) {
            impostaComeArrivo(voloDaModificare);
        } else {
            impostaComePartenza(voloDaModificare);
        }

        arrivoButton.addActionListener(e -> impostaComeArrivo(voloDaModificare));
        partenzaButton.addActionListener(e -> impostaComePartenza(voloDaModificare));


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
    private void impostaComeArrivo(Volo v) {
        arrivoButton.setSelected(true);
        gateField.setEnabled(false);
        gateField.setVisible(false);
        gateLabel.setVisible(false);
        gateField.setText("");
        aeroportoDestinazioneField.setText("Capodichino");
        aeroportoOrigineField.setText("");
    }



    private void impostaComePartenza(Volo v) {
        partenzaButton.setSelected(true);
        gateField.setEnabled(true);
        gateField.setVisible(true);
        gateLabel.setVisible(true);
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


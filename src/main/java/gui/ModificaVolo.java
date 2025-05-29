package gui;

import controller.Sistema;
import model.Volo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ModificaVolo {
    private JPanel principale;
    private Sistema sistema;
    private DefaultTableModel tableModel;
    private Volo voloModificato;

    private JTextField codiceVoloField;
    private JTextField compagniaAereaField;
    private JTextField aeroportoOrigineField;
    private JTextField aeroportoDestinazioneField;
    private JTextField orarioField;
    private JTextField ritardoField;
    private JTextField gateField;

    private JRadioButton partenzaButton;
    private JRadioButton arrivoButton;
    private JComboBox<String> statoVoloCombo;

    private JButton salvaButton;
    private JButton annullaButton;

    public ModificaVolo() {}

    public ModificaVolo(DefaultTableModel tableModel, Sistema sistema, Volo voloDaModificare) {
        this.tableModel = tableModel;
        this.voloModificato = voloDaModificare;
        this.sistema = sistema;

        inizializzaComponenti();
        caricaDatiVolo();
        applicaStili();
        aggiungiAzioni();
    }

    private void inizializzaComponenti() {
        // Qui inserisci il codice per inizializzare i componenti (da GUI designer o manuale)
    }


    private void caricaDatiVolo() {
        codiceVoloField.setText(String.format("%04d", voloModificato.getCodiceVolo()));
        compagniaAereaField.setText(voloModificato.getCompagniaAerea());
        aeroportoOrigineField.setText(voloModificato.getAeroportoOrigine());
        aeroportoDestinazioneField.setText(voloModificato.getAeroportoDestinazione());
        orarioField.setText(voloModificato.getOrarioArrivo());
        ritardoField.setText(String.valueOf(voloModificato.getRitardo()));
        gateField.setText(voloModificato.getGate());
        statoVoloCombo.setSelectedItem(voloModificato.getStato().toString());

        if (voloModificato.getGate() == null || voloModificato.getGate().isEmpty()) {
            impostaComeArrivo();
        } else {
            impostaComePartenza();
        }
    }

    private void aggiungiAzioni() {
        ButtonGroup partenzaArrivo = new ButtonGroup();
        partenzaArrivo.add(partenzaButton);
        partenzaArrivo.add(arrivoButton);

        arrivoButton.addActionListener(e -> impostaComeArrivo());
        partenzaButton.addActionListener(e -> impostaComePartenza());
        salvaButton.addActionListener(e -> salvaVolo());
        annullaButton.addActionListener(e -> chiudiFinestra());
    }

    private void impostaComeArrivo() {
        arrivoButton.setSelected(true);
        gateField.setEnabled(false);
        gateField.setText("");
        aeroportoDestinazioneField.setText("Capodichino");
        aeroportoOrigineField.setText("");
    }

    private void impostaComePartenza() {
        partenzaButton.setSelected(true);
        gateField.setEnabled(true);
        aeroportoOrigineField.setText("Capodichino");
        aeroportoDestinazioneField.setText("");
    }

    private void salvaVolo() {
        String codiceVolo = codiceVoloField.getText().trim();
        String compagnia = compagniaAereaField.getText().trim();
        String origine = aeroportoOrigineField.getText().trim();
        String destinazione = aeroportoDestinazioneField.getText().trim();
        String orario = orarioField.getText().trim();
        String ritardo = ritardoField.getText().trim();
        String gate = gateField.getText().trim();
        String stato = (String) statoVoloCombo.getSelectedItem();

        if (!convalidaCampi(codiceVolo, compagnia, origine, destinazione, orario, ritardo, gate, stato)) return;

        try {
            int codice = Integer.parseInt(codiceVolo);
            int rit = Integer.parseInt(ritardo);

            voloModificato.setCodiceVolo(codice);
            voloModificato.setCompagniaAerea(compagnia);
            voloModificato.setAeroportoOrigine(origine);
            voloModificato.setAeroportoDestinazione(destinazione);
            voloModificato.setOrarioArrivo(orario);
            voloModificato.setRitardo(rit);
            voloModificato.setGate(gate);
            voloModificato.setStato(Volo.statoVolo.valueOf(stato));

            aggiornaTabella(codice);
            chiudiFinestra();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(principale, "Errore nei dati inseriti", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean convalidaCampi(String codice, String compagnia, String origine, String destinazione, String orario, String ritardo, String gate, String stato) {
        if (codice.isEmpty() || compagnia.isEmpty() || origine.isEmpty() || destinazione.isEmpty() ||
                orario.isEmpty() || ritardo.isEmpty() || (partenzaButton.isSelected() && gate.isEmpty()) || stato == null || stato.isEmpty()) {
            JOptionPane.showMessageDialog(principale, "Tutti i campi devono essere compilati", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!codice.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(principale, "Codice volo non valido", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!orario.matches("([01]?\\d|2[0-3]):[0-5]\\d")) {
            JOptionPane.showMessageDialog(principale, "Formato orario errato (HH:mm)", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!gate.matches("[A-Z][1-9]") && partenzaButton.isSelected()) {
            JOptionPane.showMessageDialog(principale, "Formato gate errato (es. A1)", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String codiceTabella = tableModel.getValueAt(i, 0).toString();
            if (codiceTabella.equals(codice) && voloModificato.getCodiceVolo() != Integer.parseInt(codice)) {
                JOptionPane.showMessageDialog(principale, "Codice volo già esistente", "Errore", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private void aggiornaTabella(int codice) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).toString().equals(String.format("%04d", codice))) {
                tableModel.setValueAt(String.format("%04d", codice), i, 0);
                tableModel.setValueAt(compagniaAereaField.getText(), i, 1);
                tableModel.setValueAt(aeroportoOrigineField.getText(), i, 2);
                tableModel.setValueAt(aeroportoDestinazioneField.getText(), i, 3);
                tableModel.setValueAt(orarioField.getText(), i, 4);
                tableModel.setValueAt(ritardoField.getText(), i, 5);
                tableModel.setValueAt(gateField.getText(), i, 6);
                tableModel.setValueAt(statoVoloCombo.getSelectedItem(), i, 7);
                break;
            }
        }
    }

    private void chiudiFinestra() {
        JFrame frame = (JFrame) principale.getTopLevelAncestor();
        if (frame != null) frame.dispose();
    }

    private void applicaStili() {
        // Rendi riutilizzabile se necessario come in Biglietto
    }

    public JPanel getPrincipale() {
        return principale;
    }

    public JButton getSalvaButton() {
        return salvaButton;
    }

    public void impostaDefaultButton() {
        JFrame frame = (JFrame) principale.getTopLevelAncestor();
        if (frame != null) frame.getRootPane().setDefaultButton(salvaButton);
    }
}
package gui;

import controller.Sistema;
import model.Volo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
    private JLabel dataVoloLabel;
    private JTextField dataVoloField;
    private Sistema sistema;
    private DefaultTableModel tableModel;
    private Volo voloModificato;
    private AdminPage adminPage;
    final String errorMessage = "Errore";
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ModificaVolo(DefaultTableModel tableModel, Sistema sistema, Volo voloDaModificare, AdminPage adminPage) {
        this.tableModel = tableModel;
        this.voloModificato = voloDaModificare;
        this.sistema = sistema;
        this.adminPage = adminPage;

        caricaDatiVolo();
        applyStyles();
        aggiungiAzioni();
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
        dataVoloField.setText(voloModificato.getDataVolo().format(dateFormatter));

        if (voloModificato.getGate() == null || voloModificato.getGate().isEmpty()) {
            impostaComeArrivo(voloModificato);
        } else {
            impostaComePartenza(voloModificato);
        }
    }

    private void aggiungiAzioni() {
        ButtonGroup partenzaArrivo = new ButtonGroup();
        partenzaArrivo.add(partenzaButton);
        partenzaArrivo.add(arrivoButton);

        arrivoButton.addActionListener(e -> impostaComeArrivo(voloModificato));
        partenzaButton.addActionListener(e -> impostaComePartenza(voloModificato));
        salvaButton.addActionListener(this::salvaVolo);
        annullaButton.addActionListener(e -> chiudiFinestra());
    }

    private void impostaComeArrivo(Volo volo) {
        arrivoButton.setSelected(true);
        gateField.setEnabled(false);
        gateField.setVisible(false);
        gateField.setText("");
        gateLabel.setVisible(false);
        aeroportoDestinazioneField.setText("Capodichino");
        aeroportoDestinazioneField.setEditable(false);
        aeroportoOrigineField.setEditable(true);
        aeroportoOrigineField.setText("");
    }

    private void impostaComePartenza(Volo volo) {
        partenzaButton.setSelected(true);
        gateField.setEnabled(true);
        gateField.setVisible(true);
        gateField.setText(volo.getGate());
        gateLabel.setVisible(true);
        aeroportoOrigineField.setText("Capodichino");
        aeroportoOrigineField.setEditable(false);
        aeroportoDestinazioneField.setEditable(true);
        aeroportoDestinazioneField.setText("");
    }

    private void salvaVolo(ActionEvent e) {
        String codiceVolo = codiceVoloField.getText().trim();
        String compagnia = compagniaAereaField.getText().trim();
        String origine = aeroportoOrigineField.getText().trim();
        String destinazione = aeroportoDestinazioneField.getText().trim();
        String orario = orarioField.getText().trim();
        String ritardo = ritardoField.getText().trim();
        String gate = gateField.getText().trim();
        String stato = (String) statoVoloCombo.getSelectedItem();
        String dataVoloStr = dataVoloField.getText().trim();

        if (!convalidaCampi(codiceVolo, compagnia, origine, destinazione, orario, ritardo, gate, stato, dataVoloStr)) {
            return;
        }

        try {
            int codice = Integer.parseInt(codiceVolo);
            int rit = Integer.parseInt(ritardo);
            LocalDate dataVolo = LocalDate.parse(dataVoloStr, dateFormatter);

            // Determina se il tipo di volo è cambiato
            boolean eraArrivo = voloModificato.getAeroportoDestinazione().equalsIgnoreCase("Capodichino");
            boolean oraArrivo = destinazione.equalsIgnoreCase("Capodichino");
            boolean tipoCambiato = (eraArrivo != oraArrivo);

            // Aggiorna il volo
            voloModificato.setCodiceVolo(codice);
            voloModificato.setCompagniaAerea(compagnia);
            voloModificato.setAeroportoOrigine(origine);
            voloModificato.setAeroportoDestinazione(destinazione);
            voloModificato.setOrarioArrivo(orario);
            voloModificato.setRitardo(rit);
            voloModificato.setGate(gate);
            voloModificato.setStato(Volo.statoVolo.valueOf(stato));
            voloModificato.setDataVolo(dataVolo);

            // Gestisci il cambio di tabella se necessario
            if (tipoCambiato) {
                adminPage.rimuoviVoloDaTabelle(codice);
                adminPage.aggiungiVoloATabellaCorretta(voloModificato);
            } else {
                adminPage.aggiornaVoloInTabelle(voloModificato);
            }

            sistema.modificaVolo(voloModificato);
            chiudiFinestra();

        } catch (DateTimeParseException dtpe) {
            JOptionPane.showMessageDialog(principale, "Formato data non valido (dd/MM/yyyy)", errorMessage, JOptionPane.ERROR_MESSAGE);
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(principale, "Errore nei dati inseriti", errorMessage, JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean convalidaCampi(String codice, String compagnia, String origine, String destinazione,
                                   String orario, String ritardo, String gate, String stato, String dataVolo) {
        if (codice.isEmpty() || compagnia.isEmpty() || origine.isEmpty() || destinazione.isEmpty() ||
                orario.isEmpty() || ritardo.isEmpty() || (partenzaButton.isSelected() && gate.isEmpty()) ||
                stato == null || stato.isEmpty() || dataVolo.isEmpty()) {
            JOptionPane.showMessageDialog(principale, "Tutti i campi devono essere compilati", errorMessage, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!codice.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(principale, "Codice volo non valido", errorMessage, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!orario.matches("([01]?\\d|2[0-3]):[0-5]\\d")) {
            JOptionPane.showMessageDialog(principale, "Formato orario errato (HH:mm)", errorMessage, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!gate.matches("[A-Z][1-9]") && partenzaButton.isSelected()) {
            JOptionPane.showMessageDialog(principale, "Formato gate errato (es. A1)", errorMessage, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            LocalDate.parse(dataVolo, dateFormatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(principale, "Formato data non valido (dd/MM/yyyy)", errorMessage, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void chiudiFinestra() {
        JFrame frame = (JFrame) principale.getTopLevelAncestor();
        if (frame != null) frame.dispose();
    }

    public JPanel getPrincipale() {
        return principale;
    }

    public JButton getSalvaButton() {
        return salvaButton;
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
        principale.setBackground(background);

        // Stile etichette
        for (JLabel label : new JLabel[]{
                modificaVoloLabel, codiceVoloLabel, compagniaAereaLabel,
                aeroportoOrigineLabel, aeroportoDestinazioneLabel, orarioDiArrivoLabel,
                ritardoLabel, gateLabel, voloInLabel, statoVoloLabel, dataVoloLabel
        }) {
            label.setFont(labelFont);
            label.setForeground(primaryBlue);
        }

        // Stile campi testo
        for (JTextField field : new JTextField[]{
                codiceVoloField, compagniaAereaField, aeroportoOrigineField,
                aeroportoDestinazioneField, orarioField, ritardoField, gateField, dataVoloField
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
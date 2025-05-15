package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * The type Aggiungi volo.
 */
public class AggiungiVolo {
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
    private JButton salvaVoloButton;
    private JButton annullaButton;
    private JLabel gateLabel;
    private JTextField gateField;
    private DefaultTableModel tableModel;

    /**
     * Get principale j panel.
     *
     * @return the j panel
     */
    public JPanel getPrincipale(){
        return principale;
    }

    /**
     * Instantiates a new Aggiungi volo.
     *
     * @param tableModel the table model
     */
    public AggiungiVolo(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
        salvaVoloButton.addActionListener(e -> salvaVolo());
        annullaButton.addActionListener(e -> {//button annulla
            JFrame finestra = (JFrame) principale.getTopLevelAncestor();
            if (finestra != null) {
                finestra.dispose();
            }
        });

    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        String[] colonne = {"Codice", "Destinazione", "Compagnia", "Origine", "Orario", "Ritardo","Gate", "Stato"};
        DefaultTableModel modelloFinto = new DefaultTableModel(colonne, 0);
        AggiungiVolo av = new AggiungiVolo(modelloFinto);
        JFrame frame = new JFrame("Aggiungi Volo");
        frame.setContentPane(av.getPrincipale());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(700, 290);
        frame.setLocation(400, 300);
        frame.setVisible(true);
    }

    //aggiunge volo alla tabella !!!INCOMPLETO AGGIUNGERE VOLO ALL'ARRAYLIST
    private void salvaVolo(){
        String codiceVolo = codiceVoloField.getText();
        String compagniaAerea = compagniaAereaField.getText();
        String aeroportoOrigine = aeroportoOrigineField.getText();
        String aeroportoDestinazione = destinazioneField.getText();
        String ritardo = ritardoField.getText();
        String orario = orarioFIeld.getText();
        String statoVolo = statoVoloCombo.getSelectedItem().toString();

        tableModel.addRow(new Object[]{codiceVolo, aeroportoDestinazione, compagniaAerea, aeroportoOrigine, orario, ritardo, statoVolo});
        ((JFrame) principale.getTopLevelAncestor()).dispose();

    }
}
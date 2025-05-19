package gui;

import model.Prenotazione;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * The type Biglietto.
 */
public class Biglietto {
    private controller.Sistema sistema;
    private JPanel finestraPrincipale;
    private JButton modificaButton;
    private JButton cancellaButton;
    private JLabel titolo;
    private JTable tabellaBiglietti;
    private JButton fineButton;
    private JPanel navbar;
    /**
     * The constant frame.
     */
    public static JFrame frame;

    /**
     * Instantiates a new Biglietto.
     *
     * @param frameChiamante the frame chiamante
     * @param sistema        the sistema
     */
    public Biglietto(JFrame frameChiamante, controller.Sistema sistema, String nome, int numeroBiglietto){
        //inizializzazione sistema
        this.sistema=sistema;
        //caretteristiche frame essenziali
        frame = new JFrame("Cerca e modifica biglietto");
        frame.setContentPane(finestraPrincipale);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(900,600);
        frame.setResizable(false);
        frame.setLocation(200,200);
        frame.setVisible(true);
        //nomi colonne
        String[] colonne= {"Nome","Cognome", "Posto", "NumeroCarta"};
        //setting di tabella
        DefaultTableModel model = new DefaultTableModel(colonne,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabellaBiglietti.setModel(model);
        //biglietti dell'utente
        ArrayList<Prenotazione> biglietti = sistema.getBiglietti(nome, numeroBiglietto, sistema.utente);
        if (biglietti!=null) {
            for (int i = 0; i < biglietti.size(); i++)
                model.addRow(new Object[]{biglietti.get(i).getPasseggero().getNome(),biglietti.get(i).getPasseggero().getCognome(), biglietti.get(i).getPostoAssegnato(), biglietti.get(i).getNumeroBiglietto()});
        }
            //ritorno al frame principale
            fineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameChiamante.setVisible(true);
                frame.setVisible(false);
                frame.dispose();
            }
        });
        cancellaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int riga = tabellaBiglietti.getSelectedRow();
                if(riga!=-1){
                    int conferma = JOptionPane.showConfirmDialog(null,
                            "Sei sicuro di voler eliminare questo biglietto?",
                            "Cancellazione biglietto",
                            JOptionPane.YES_NO_OPTION);
                    if (conferma==JOptionPane.YES_OPTION){
                        DefaultTableModel model1 = (DefaultTableModel) tabellaBiglietti.getModel();
                        Object valoreNumeroBiglietto = model1.getValueAt(riga, 3); // colonna "NumeroCarta"
                        if (valoreNumeroBiglietto != null) {
                            try {
                                long numeroBiglietto = Long.parseLong(valoreNumeroBiglietto.toString());
                                sistema.cancellaBiglietto(numeroBiglietto); // Rimozione dal sistema
                                model1.removeRow(riga); // Rimozione dalla tabella
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(null,
                                        "Numero biglietto non valido!",
                                        "Errore",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }else{
                        JOptionPane.showMessageDialog(null,
                                "Seleziona un volo da eliminare!",
                                "Errore",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        modificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}

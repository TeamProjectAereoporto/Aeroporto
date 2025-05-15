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
    private JButton modificaButton;
    private JButton eliminaButton;
    private JPanel finestraPrincipale;
    private JPanel navbar;
    private JTable tabellaBiglietti;
    private JButton fineButton;
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
        String[] colonne= {"Nome", "Posto", "NumeroCarta"};
        //setting di tabella
        DefaultTableModel model = new DefaultTableModel(colonne,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabellaBiglietti.setModel(model);
        //biglietti dell'utente
        ArrayList<Prenotazione> biglietti = sistema.getBiglietti(nome, numeroBiglietto);
        if (biglietti!=null) {
            for (int i = 0; i < biglietti.size(); i++)
                model.addRow(new Object[]{biglietti.get(i).getPasseggero().getNome(), biglietti.get(i).getPostoAssegnato(), biglietti.get(i).getNumeroBiglietto()});
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
    }
}

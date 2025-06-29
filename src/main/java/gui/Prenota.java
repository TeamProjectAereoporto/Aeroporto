package gui;

import controller.Sistema;
import model.Passeggero;
import model.Prenotazione;
import model.Volo;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;


public class Prenota {
    private JLabel codiceVoloField;
    private JLabel compagniaAereaField;
    private JLabel origineField;
    private JLabel arrivoField;
    private JLabel orarioField;
    private JLabel ritardoField;
    private JLabel statoField;
    private JButton prenotaButton;
    private JPanel finestraPrincipale;
    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField ciFIeld;
    private JLabel gateField;
    public final static JFrame frame = new JFrame("Prenota Biglietto");
    private final Sistema sistema;
    public Prenota(JFrame chiamante, Object[] valori, Sistema sistema){
        this.sistema=sistema;
        //setting label informazioni volo
        codiceVoloField.setText((valori[0].toString()));
        compagniaAereaField.setText((String) valori[1]);
        origineField.setText((String) valori[2]);
        arrivoField.setText((String) valori[3]);
        orarioField.setText((valori[4].toString()));
        ritardoField.setText((valori[5].toString()));
        statoField.setText(valori[6].toString());
        gateField.setText(valori[7].toString());
        //caratteristiche essenziali frame
        frame.setContentPane(finestraPrincipale);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.getRootPane().setDefaultButton(prenotaButton);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        prenotaButton.addActionListener(e ->{
                String nome= nomeField.getText().trim().toLowerCase();
                String cognome = cognomeField.getText().trim().toLowerCase();
                String ci = ciFIeld.getText().trim().toUpperCase();
                if(!nome.isEmpty() && !cognome.isEmpty() && !ci.isEmpty()) {
                    if (ci.matches("[A-Za-z]{2}[0-9]{5}[A-Za-z]{2}")) {
                        Long numeroBiglietto = sistema.creaNumBiglietto();
                        int id = 0;
                        try{
                        if (!sistema.controlloPasseggeroInVolo(ci, Integer.parseInt(codiceVoloField.getText()))) {
                            Passeggero passeggero =  new Passeggero(nome, cognome, ci);
                            id= sistema.aggiungiPasseggero(passeggero);
                            if(id==0){
                                JOptionPane.showMessageDialog(finestraPrincipale,
                                        "Errore gennerico inserimento del passeggero  ",
                                        "Errore inserimento passeggero",
                                        JOptionPane.ERROR_MESSAGE);
                            }else {
                                passeggero.setId_passeggero(id);
                                Prenotazione biglietto = new Prenotazione(numeroBiglietto,
                                        "A5",
                                        Prenotazione.StatoPrenotazione.CONFERMATA,
                                        passeggero,
                                        new Volo(
                                                Integer.parseInt(codiceVoloField.getText()),
                                                compagniaAereaField.getText(),
                                                origineField.getText(),
                                                arrivoField.getText(),
                                                orarioField.getText(),
                                                Integer.parseInt(ritardoField.getText()),
                                                Volo.statoVolo.valueOf(statoField.getText()),
                                                gateField.getText()//si deve correggere
                                        ),
                                        sistema.getUtente());
                                sistema.aggiungiBiglietto(biglietto);
                                JOptionPane.showMessageDialog(finestraPrincipale,
                                        "Prenotazione n°" + numeroBiglietto + " avvenuta con successo",
                                        "Prenotazione riuscita",
                                        JOptionPane.INFORMATION_MESSAGE);
                                chiamante.setVisible(true);
                                frame.setVisible(false);
                                frame.dispose();
                            }
                            }else{
                            JOptionPane.showMessageDialog(finestraPrincipale,
                                    "Il passeggero "+ci+" è già passeggero del seguente volo, codice: "+codiceVoloField.getText(),
                                    "Passeggero già prenotato",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        }catch (SQLException ex){
                            ex.printStackTrace();
                        }
                        } else {
                            JOptionPane.showMessageDialog(finestraPrincipale,
                                    "formato Carta d'identità non valida rispettare il seguente formato: AA12345BC",
                                    "errore",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                }
        });
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                chiamante.setVisible(true);
                frame.setVisible(false);
                frame.dispose();
            }
        });
    }
}

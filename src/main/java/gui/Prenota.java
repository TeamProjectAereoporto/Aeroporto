package gui;

import controller.Sistema;
import model.Passeggero;
import model.Prenotazione;
import model.Volo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Prenota {
    private JLabel codiceVoloField;
    private JLabel compagniaAereaField;
    private JLabel origineField;
    private JLabel arrivoField;
    private JLabel orarioField;
    private JLabel ritardoField;
    private JLabel statoField;
    private JButton prenotaButton;
    private JButton annullaButton;
    private JPanel finestraPrincipale;
    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField ciFIeld;
    public static JFrame frame;
    private Sistema sistema;
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
        //caratteristiche essenziali frame
        frame = new JFrame("Prenota Biglietto");
        frame.setContentPane(finestraPrincipale);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        prenotaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome= nomeField.getText();
                String cognome = cognomeField.getText();
                String ci = ciFIeld.getText();
                if(!nome.isEmpty() && !cognome.isEmpty() && !ci.isEmpty()) {
                    Long numeroBiglietto = sistema.creaNumBiglietto();
                    Prenotazione biglietto = new Prenotazione(numeroBiglietto,
                            "A5",
                            Prenotazione.StatoPrenotazione.CONFERMATA,
                            new Passeggero(nome,cognome,ci),
                            new Volo(
                                    Integer.parseInt(codiceVoloField.getText()),
                                    compagniaAereaField.getText(),
                                    origineField.getText(),
                                    arrivoField.getText(),
                                    orarioField.getText(),
                                    Integer.parseInt(ritardoField.getText()),
                                    Volo.statoVolo.valueOf(statoField.getText()),
                                    "gate"
                            ));
                    sistema.aggiungiBiglietto(biglietto);
                    chiamante.setVisible(true);
                    frame.setVisible(false);
                    frame.dispose();
                }
            }
        });
        annullaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chiamante.setVisible(true);
                frame.setVisible(false);
                frame.dispose();
            }
        });
    }
}

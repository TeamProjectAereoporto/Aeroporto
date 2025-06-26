package gui;

import controller.Sistema;
import model.Passeggero;
import model.Prenotazione;
import model.Volo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;


public class Prenota {
    // Etichette per visualizzare informazioni sul volo
    private JLabel codiceVoloField;
    private JLabel compagniaAereaField;
    private JLabel origineField;
    private JLabel arrivoField;
    private JLabel orarioField;
    private JLabel ritardoField;
    private JLabel statoField;
    private JButton prenotaButton;  // Pulsante per prenotare il biglietto
    private JPanel finestraPrincipale;  // Pannello principale della finestra
    private JTextField nomeField;   // Campo di testo per inserire il nome del passeggero
    private JTextField cognomeField; // Campo di testo per inserire il cognome del passeggero
    private JTextField ciFIeld;     // Campo di testo per inserire il numero di carta d'identità
    private JLabel gateField;       // Etichetta per visualizzare il gate
    public final static JFrame frame = new JFrame("Prenota Biglietto"); // Finestra principale della prenotazione
    private final Sistema sistema;  // Riferimento al sistema di backend

    // Costruttore della classe Prenota, riceve il frame chiamante, i dati del volo e il sistema
    public Prenota(JFrame chiamante, Object[] valori, Sistema sistema){
        this.sistema=sistema;

        // Imposta le etichette con i dati del volo passati come array di oggetti
        codiceVoloField.setText((valori[0].toString()));
        compagniaAereaField.setText((String) valori[1]);
        origineField.setText((String) valori[2]);
        arrivoField.setText((String) valori[3]);
        orarioField.setText((valori[4].toString()));
        ritardoField.setText((valori[5].toString()));
        statoField.setText(valori[6].toString());
        gateField.setText(valori[7].toString());

        // Configurazione della finestra di prenotazione
        frame.setContentPane(finestraPrincipale);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); // blocca la chiusura diretta per gestirla manualmente
        frame.pack();
        frame.getRootPane().setDefaultButton(prenotaButton); // imposta il pulsante prenota come default per invio
        frame.setResizable(false); // finestra non ridimensionabile
        frame.setLocationRelativeTo(null); // centra la finestra sullo schermo

        // Aggiunge l'azione al pulsante prenota
        prenotaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Legge e normalizza i dati inseriti dall'utente
                String nome= nomeField.getText().trim().toLowerCase();
                String cognome = cognomeField.getText().trim().toLowerCase();
                String ci = ciFIeld.getText().trim().toUpperCase();

                // Controlla che i campi non siano vuoti
                if(!nome.isEmpty() && !cognome.isEmpty() && !ci.isEmpty()) {
                    // Controlla il formato della carta d'identità (esempio: AA12345BC)
                    if (ci.matches("[A-Za-z]{2}[0-9]{5}[A-Za-z]{2}")) {
                        // Genera un nuovo numero di biglietto univoco
                        Long numeroBiglietto = sistema.creaNumBiglietto();

                        // Crea un oggetto Prenotazione con i dati inseriti e i dati del volo
                        Prenotazione biglietto = new Prenotazione(numeroBiglietto,
                                "A5",  // posto fisso "A5" (da sistemare se necessario)
                                Prenotazione.StatoPrenotazione.CONFERMATA,
                                new Passeggero(nome, cognome, ci),
                                new Volo(
                                        Integer.parseInt(codiceVoloField.getText()),
                                        compagniaAereaField.getText(),
                                        origineField.getText(),
                                        arrivoField.getText(),
                                        orarioField.getText(),
                                        Integer.parseInt(ritardoField.getText()),
                                        Volo.statoVolo.valueOf(statoField.getText()),
                                        gateField.getText() // Attenzione: commento indica che potrebbe necessitare correzione
                                ),sistema.utente);

                        // Aggiunge il passeggero al sistema (salvataggio nel DB)
                        try {
                            sistema.aggiungiPasseggero(new Passeggero(nome,cognome,ci));
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }

                        // Aggiunge la prenotazione al sistema
                        sistema.aggiungiBiglietto(biglietto);

                        // Ripristina la finestra chiamante e chiude questa finestra
                        chiamante.setVisible(true);
                        frame.setVisible(false);
                        frame.dispose();
                    } else {
                        // Messaggio di errore se il formato della carta d'identità non è corretto
                        JOptionPane.showMessageDialog(finestraPrincipale,
                                "formato Carta d'identità non valida rispettare il seguente formato: AA12345BC",
                                "errore",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Gestisce la chiusura della finestra tramite il bottone di chiusura (X)
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Rende visibile la finestra chiamante e chiude questa
                chiamante.setVisible(true);
                frame.setVisible(false);
                frame.dispose();
            }
        });
    }
}

package gui;

import controller.Sistema;
import model.Prenotazione;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    public static JFrame frame;
    private Sistema controller;
    public Prenota(JFrame chiamante, Object[] valori){
        //setting label informazioni volo
        codiceVoloField.setText((valori[0].toString()));
        compagniaAereaField.setText((String) valori[1]);
        origineField.setText((String) valori[2]);
        arrivoField.setText((String) valori[3]);
        orarioField.setText((valori[4].toString()));
        ritardoField.setText((valori[5].toString()+"'"));
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
                Long numeroBiglietto = controller.creaBiglietto();
                chiamante.setVisible(true);
                frame.setVisible(false);
                frame.dispose();
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

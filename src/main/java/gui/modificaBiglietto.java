package gui;

import controller.Sistema;
import model.Prenotazione;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class modificaBiglietto {

    private JTextField nomeField;
    private JTextField postoField;
    private JTextField ciField;
    private JButton invioButton;
    private JPanel finestraPrincipale;
    private JTextField cognomeField;
    private Sistema sistema;
    public static JFrame frame;
    private DefaultTableModel tableModel;
    public modificaBiglietto(Sistema sistema, Prenotazione bigliettoDaModificare, DefaultTableModel tableModel, JFrame chiamante){
        this.sistema=sistema;
        this.tableModel=tableModel;
        frame = new JFrame("Modifica Biglietto");
        frame.setContentPane(finestraPrincipale);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);  //  Impedisci chiusura automatica
        frame.getRootPane().setDefaultButton(invioButton);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        nomeField.setText(bigliettoDaModificare.getPasseggero().getNome());
        cognomeField.setText(bigliettoDaModificare.getPasseggero().getCognome());
        ciField.setText(bigliettoDaModificare.getPasseggero().getNumeroDocumento());
        postoField.setText(bigliettoDaModificare.getPostoAssegnato());


        invioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome= nomeField.getText().trim();
                String posto = postoField.getText().trim().toUpperCase();
                String ci = ciField.getText().trim().toUpperCase();
                String cognome = cognomeField.getText().trim().toLowerCase();
                if(!nome.isEmpty() && !posto.isEmpty() && !ci.isEmpty() && !cognome.isEmpty()) {
                    if (ci.matches("[A-Za-z]{2}[0-9]{5}[A-Za-z]{2}")) {
                        bigliettoDaModificare.getPasseggero().setNome(nome);
                        bigliettoDaModificare.getPasseggero().setNumeroDocumento(ci);
                        bigliettoDaModificare.getPasseggero().setCognome(cognome);
                        bigliettoDaModificare.setPostoAssegnato(posto);
                        for (Prenotazione p : sistema.utente.bigliettiAcquistati){
                            System.out.println(p);
                        }
                        for (int i = 0; i < tableModel.getRowCount(); i++) {
                            if (tableModel.getValueAt(i, 4).toString().equals(String.format("%10d", bigliettoDaModificare.getNumeroBiglietto()))) {
                                tableModel.setValueAt(nome, i, 0);
                                tableModel.setValueAt(cognome, i, 1);
                                tableModel.setValueAt(ci, i, 2);
                                tableModel.setValueAt(posto, i, 3);

                                break;
                            }
                        }
                        chiamante.setVisible(true);
                        frame.setVisible(false);
                    }else{
                        JOptionPane.showMessageDialog(frame,"Formato carta d'identità non valido RISPETTARE: CC12345CC", "ERRORE", JOptionPane.ERROR_MESSAGE);
                    }
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

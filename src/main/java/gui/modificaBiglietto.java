package gui;

import controller.Sistema;
import model.Prenotazione;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class modificaBiglietto {
    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField ciField;
    private JTextField postoField;
    private JButton invioButton;
    private JPanel finestraPrincipale;

    private Sistema sistema;
    public final static JFrame frame  = new JFrame("Modifica Biglietto");
    private DefaultTableModel tableModel;

    public modificaBiglietto(Sistema sistema, Prenotazione bigliettoDaModificare, DefaultTableModel tableModel, JFrame chiamante) {
        this.sistema = sistema;
        this.tableModel = tableModel;

        frame.setContentPane(finestraPrincipale);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setSize(new Dimension(400, 300) );
        frame.setLocationRelativeTo(null);
        frame.getRootPane().setDefaultButton(invioButton);
        frame.setVisible(true);

        // Riempimento iniziale dei campi
        nomeField.setText(bigliettoDaModificare.getPasseggero().getNome());
        cognomeField.setText(bigliettoDaModificare.getPasseggero().getCognome());
        ciField.setText(bigliettoDaModificare.getPasseggero().getNumeroDocumento());
        postoField.setText(bigliettoDaModificare.getPostoAssegnato());

        applyStyles();

        invioButton.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            String cognome = cognomeField.getText().trim();
            String ci = ciField.getText().trim().toUpperCase();
            String posto = postoField.getText().trim().toUpperCase();

            if (nome.isEmpty() || cognome.isEmpty() || ci.isEmpty() || posto.isEmpty()) {
                showError("Tutti i campi devono essere compilati.");
                return;
            }

            if (!ci.matches("[A-Za-z]{2}[0-9]{5}[A-Za-z]{2}")) {
                showError("Formato CI non valido. Deve essere: CC12345CC");
                return;
            }

            // Salvataggio dati
            bigliettoDaModificare.getPasseggero().setNome(nome);
            bigliettoDaModificare.getPasseggero().setCognome(cognome);
            bigliettoDaModificare.getPasseggero().setNumeroDocumento(ci);
            bigliettoDaModificare.setPostoAssegnato(posto);

            // Aggiornamento tabella
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
            frame.dispose();
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                chiamante.setVisible(true);
                frame.dispose();
            }
        });
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    private void applyStyles() {
        Color background = new Color(245, 245, 245);
        Color borderColor = new Color(200, 200, 200);
        Color buttonColor = new Color(76, 175, 80);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

        finestraPrincipale.setBackground(background);
        finestraPrincipale.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField[] fields = {nomeField, cognomeField, ciField, postoField};
        for (JTextField field : fields) {
            field.setFont(fieldFont);
            field.setBackground(Color.WHITE);
            field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(borderColor),
                    new EmptyBorder(5, 8, 5, 8)
            ));
        }

        invioButton.setFont(buttonFont);
        invioButton.setBackground(buttonColor);
        invioButton.setForeground(Color.WHITE);
        invioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        invioButton.setFocusPainted(false);
        invioButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }
}

package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPage {
    private JPanel principale;
    private JLabel adminTitle;
    private JButton aggiungiVoloButton;
    private JButton eliminaVoloButton;
    private JPanel voliPanel;
    private JLabel voliLable;
    private JTable tabellaVoli;
    private JButton gateButton;

    public AdminPage(){
        String[] colonne = {"Codice Volo", "Compagnia Aerea", "Aeroporto di Origine",
                "Aeroporto Destinazione", "Orario di Arrivo", "Ritardo", "Stato del Volo"};
        DefaultTableModel model = new DefaultTableModel(colonne,0);
        tabellaVoli.setModel(model);

        if (tabellaVoli.getParent() instanceof JViewport) {
        } else {
            voliPanel.removeAll();
            voliPanel.setLayout(new BorderLayout());
            voliPanel.add(new JScrollPane(tabellaVoli), BorderLayout.CENTER);
            voliPanel.revalidate();
            voliPanel.repaint();
        }

        aggiungiVoloButton.addActionListener(e -> {
            AggiungiVolo aggiungiVolo = new AggiungiVolo(model);
            JFrame finestra = new JFrame("Aggiungi Volo");
            finestra.setContentPane(aggiungiVolo.getPrincipale());
            finestra.pack();
            finestra.setSize(700, 290);
            finestra.setLocation(400, 300);
            finestra.setVisible(true);
        });
        eliminaVoloButton.addActionListener(e -> {
            int rigaSelezionata = tabellaVoli.getSelectedRow();
            if (rigaSelezionata != -1) {
                int conferma = JOptionPane.showConfirmDialog(null,
                        "Sei sicuro di voler eliminare il volo selezionato?",
                        "Conferma eliminazione",
                        JOptionPane.YES_NO_OPTION);

                if (conferma == JOptionPane.YES_OPTION) {
                    DefaultTableModel model1 = (DefaultTableModel) tabellaVoli.getModel();
                    model1.removeRow(rigaSelezionata);
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Seleziona un volo da eliminare!",
                        "Errore",
                        JOptionPane.WARNING_MESSAGE);
            }
        });


    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("AdminDashboard");
        frame.setContentPane(new AdminPage().principale);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(700,400);
        frame.setLocation(400,300);
        frame.setVisible(true);
    }
}

package gui;

import controller.Sistema;
import model.Volo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static gui.AggiungiVolo.sistema;
import static javax.swing.UIManager.get;

public class AdminPage {
    private JPanel principale;
    private JLabel adminTitle;
    private JButton aggiungiVoloButton;
    private JButton eliminaVoloButton;
    private JPanel voliPanel;
    private JLabel voliLable;
    private JTable tabellaVoli;
    private JButton modificaVoloButton;
    public static JFrame frame;
    private Sistema sistema;

    public AdminPage(JFrame chiamante, Sistema controller) {
        this.sistema = controller;

        String[] colonne = {"Codice Volo", "Compagnia Aerea", "Aeroporto di Origine",
                "Aeroporto Destinazione", "Orario di Arrivo", "Ritardo", "Stato del Volo", "Gate"};

        DefaultTableModel model = new DefaultTableModel(colonne, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

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
            AggiungiVolo aggiungiVolo = new AggiungiVolo(model, controller);
            JFrame finestra = new JFrame("Aggiungi Volo");
            finestra.setContentPane(aggiungiVolo.getPrincipale());
            finestra.pack();
            finestra.setSize(700, 340);
            finestra.setLocation(500, 400);
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

        frame = new JFrame("AdminDashboard");
        frame.setContentPane(principale);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(800, 400);
        frame.setLocation(400, 300);
        frame.setVisible(true);

        modificaVoloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int selectedRow = tabellaVoli.getSelectedRow();
                if (selectedRow != -1) {
                    Volo voloSelezionato = sistema.visualizzaVoli().get(selectedRow);
                    DefaultTableModel tableModel = (DefaultTableModel) tabellaVoli.getModel();

                    ModificaVolo modificaVoloPanel = new ModificaVolo(tableModel, sistema, voloSelezionato);

                    JFrame frame = new JFrame("Modifica Volo");
                    frame.setContentPane(modificaVoloPanel.getPrincipale());
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.pack();
                    frame.setSize(700, 340);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Seleziona un volo da modificare", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
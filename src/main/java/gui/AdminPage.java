package gui;

import controller.Sistema;
import model.Volo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;


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
            finestra.getRootPane().setDefaultButton(aggiungiVolo.getSalvaButton());
            finestra.pack();
            finestra.setSize(700, 500);
            finestra.setLocation(400, 150);
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
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
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
                    frame.getRootPane().setDefaultButton(modificaVoloPanel.getSalvaButton());
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.pack();
                    frame.setSize(700, 500);
                    frame.setLocation(400, 150);
                    frame.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(null, "Seleziona un volo da modificare", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        applyStyles();
    }
    private void applyStyles() {
        // Palette colori aeroportuale
        Color primaryBlue = new Color(0, 95, 135);
        Color secondaryBlue = new Color(0, 120, 167);
        Color background = new Color(245, 245, 245);
        Color errorRed = new Color(231, 76, 60);
        Color successGreen = new Color(76, 175, 80);

        // Font
        Font titleFont = new Font("Segoe UI", Font.BOLD, 24);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);
        Font tableFont = new Font("Segoe UI", Font.PLAIN, 12);

        // Stile generale
        principale.setBackground(background);
        principale.setBorder(new EmptyBorder(15, 15, 15, 15));
        voliPanel.setBackground(background);

        // Titolo
        adminTitle.setFont(titleFont);
        adminTitle.setForeground(primaryBlue);
        adminTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Etichette
        voliLable.setFont(labelFont);
        voliLable.setForeground(primaryBlue);

        // Stile pulsanti
        styleButton(aggiungiVoloButton, successGreen, Color.WHITE, buttonFont);
        styleButton(modificaVoloButton, secondaryBlue, Color.WHITE, buttonFont);
        styleButton(eliminaVoloButton, errorRed, Color.WHITE, buttonFont);

        // Stile tabella
        tabellaVoli.setFont(tableFont);
        tabellaVoli.setRowHeight(25);
        tabellaVoli.setShowGrid(false);
        tabellaVoli.setIntercellSpacing(new Dimension(0, 0));
        tabellaVoli.setSelectionBackground(new Color(220, 240, 255));
        tabellaVoli.getTableHeader().setFont(labelFont);
        tabellaVoli.getTableHeader().setBackground(primaryBlue);
        tabellaVoli.getTableHeader().setForeground(Color.WHITE);
        tabellaVoli.getTableHeader().setReorderingAllowed(false);
    }

    private void styleButton(JButton button, Color bg, Color fg, Font font) {
        button.setFont(font);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Effetto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bg.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bg);
            }
        });
    }
}
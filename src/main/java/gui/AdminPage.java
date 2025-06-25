package gui;

import controller.Sistema;
import model.Volo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
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
    private JLabel logoutLabel;
    private JPanel logOutPanel;
    public final static JFrame frame = new JFrame("AdminDashboard");
    private final Sistema sistema;

    public AdminPage(JFrame chiamante, Sistema controller, DefaultTableModel model) {
        this.sistema = controller;
        //sistema.generaContenutiCasuali();
        popolaTabellaVoli(model);
        tabellaVoli.setModel(model);

        aggiungiVoloButton.addActionListener(e -> {
            AggiungiVolo av = new AggiungiVolo(model, sistema, this);
            JFrame frame = new JFrame("Aggiungi Volo");
            frame.getRootPane().setDefaultButton(av.getAggiungiVoloButton());
            frame.setContentPane(av.getPrincipale());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setSize(700, 500);
            frame.setLocation(400, 150);
            frame.setVisible(true);
        });

        eliminaVoloButton.addActionListener(e -> {
            int rigaSelezionata = tabellaVoli.getSelectedRow();
            if (rigaSelezionata != -1) {
                System.out.println(rigaSelezionata);
                int conferma = JOptionPane.showConfirmDialog(null,
                        "Sei sicuro di voler eliminare il volo selezionato?",
                        "Conferma eliminazione",
                        JOptionPane.YES_NO_OPTION);

                if (conferma == JOptionPane.YES_OPTION) {
                    Object valoreCodice = tabellaVoli.getValueAt(rigaSelezionata, 0);
                    int codiceVolo = -1;
                    if (valoreCodice instanceof Integer) {
                        codiceVolo = (Integer) valoreCodice;
                    } else if (valoreCodice instanceof String) {
                        try {
                            codiceVolo = Integer.parseInt((String) valoreCodice);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Errore nel formato del codice volo");
                            return;
                        }
                    }
                    sistema.eliminaVolo(codiceVolo);
                    model.removeRow(rigaSelezionata);
                    sistema.visualizzaVoli().remove(rigaSelezionata);
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "Seleziona un volo da eliminare!",
                        "Errore",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        frame.setContentPane(principale);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setVisible(true);

        modificaVoloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tabellaVoli.getSelectedRow();
                if (selectedRow != -1) {
                    Volo voloSelezionato = sistema.visualizzaVoli().get(selectedRow);
                    ModificaVolo modificaVoloPanel = new ModificaVolo(model , sistema, voloSelezionato);
                    JFrame frameVolo = new JFrame("Modifica Volo");
                    frameVolo.setContentPane(modificaVoloPanel.getPrincipale());
                    frameVolo.getRootPane().setDefaultButton(modificaVoloPanel.getSalvaButton());
                    frameVolo.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    frameVolo.pack();
                    frameVolo.setSize(700, 500);
                    frameVolo.setLocation(400, 150);
                    frameVolo.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(null, "Seleziona un volo da modificare", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        applyStyles(chiamante);

    }

    public JTable getTabellaVoli(){
        return tabellaVoli;
    }

    private void popolaTabellaVoli(DefaultTableModel model) {
        ArrayList<Volo> voli = sistema.visualizzaVoli();
        for (Volo v : voli) {
            model.addRow(new Object[]{
                    v.getCodiceVolo(),
                    v.getCompagniaAerea(),
                    v.getAeroportoOrigine(),
                    v.getAeroportoDestinazione(),
                    v.getOrarioArrivo(),
                    v.getRitardo(),
                    v.getStato(),
                    v.getGate()  // Se lo hai rimosso dal DB e dal model, togli anche da qui
            });
        }
    }




    private void applyStyles(JFrame chiamante) {
        // Palette colori aeroportuale
        Color primaryBlue = new Color(0, 95, 135);
        Color secondaryBlue = new Color(0, 120, 167);
        Color background = new Color(245, 245, 245);
        Color errorRed = new Color(231, 76, 60);
        Color successGreen = new Color(76, 175, 80);

        // Font
        final String name = "Segoe UI";
        Font titleFont = new Font(name, Font.BOLD, 24);
        Font labelFont = new Font(name, Font.BOLD, 14);
        Font buttonFont = new Font(name, Font.BOLD, 14);
        Font tableFont = new Font(name, Font.PLAIN, 12);

        // Stile generale
        principale.setBackground(background);
        principale.setBorder(new EmptyBorder(15, 15, 15, 15));
        voliPanel.setBackground(background);
        logOutPanel.setBackground(secondaryBlue);

        // Titolo
        adminTitle.setFont(titleFont);
        adminTitle.setForeground(primaryBlue);
        adminTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Etichette
        voliLable.setFont(labelFont);
        voliLable.setForeground(primaryBlue);
        logoutLabel.setFont(labelFont);
        logoutLabel.setForeground(background);

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
        logOutPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logOutPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logOutPanel.setBackground(successGreen);
                chiamante.setVisible(true);
                Window window = SwingUtilities.getWindowAncestor(principale);
                if (window != null) {
                    window.dispose();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                logOutPanel.setBackground(errorRed);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logOutPanel.setBackground(secondaryBlue);
            }
        });

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
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bg.darker());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bg);
            }
        });
    }

}
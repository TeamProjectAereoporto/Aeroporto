package gui;

import controller.Sistema;
import model.Volo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.border.EmptyBorder;

public class AdminPage {
    private JPanel principale;
    private JLabel adminTitle;
    private JButton aggiungiVoloButton;
    private JButton eliminaVoloButton;
    private JPanel voliPanel;
    private JLabel voliArrivoLabel;
    private JTable tabellaVolArrivo;
    private JButton modificaVoloButton;
    private JLabel logoutLabel;
    private JPanel logOutPanel;
    private JPanel pannelloTuttiVoli;
    private JPanel voliArrivoPanel;
    private JTable tabellaVoliPartenza;
    private JLabel voliPartenzaLabel;
    private JLabel dataAdmin;
    private JLabel frecciaSinistraAdmin;
    private JLabel frecciaDestraAdmin;
    public static final JFrame frame = new JFrame("AdminDashboard");
    private final Sistema sistema;
    private static final String ERRORMESSAGE = "Errore";
    private LocalDate dataVolo;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public AdminPage(JFrame chiamante, Sistema controller, DefaultTableModel modelArrivi, DefaultTableModel modelPartenze) {
        this.sistema = controller;
        dataVolo = LocalDate.now();
        dataAdmin.setText(formatter.format(dataVolo));
        // Popolamento tabelle
        popolaTabellaVoliArrivo(modelArrivi);
        popolaTabellaVoliPartenza(modelPartenze);

        tabellaVolArrivo.setModel(modelArrivi);
        tabellaVoliPartenza.setModel(modelPartenze);

        aggiungiVoloButton.addActionListener(e -> inizializzaAggiungiVolo(modelArrivi, modelPartenze));
        eliminaVoloButton.addActionListener(e -> eliminaVoloSelezionato());
        modificaVoloButton.addActionListener(e -> modificaVoloSelezionato());

        frame.setContentPane(principale);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setVisible(true);

        applyStyles(chiamante);
        frecciaSinistraAdmin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(LocalDate.now().isBefore(dataVolo)){
                    dataVolo = dataVolo.minusDays(1);
                    dataAdmin.setText(formatter.format(dataVolo));
                    popolaTabellaVoliArrivo(modelArrivi);
                    popolaTabellaVoliPartenza(modelPartenze);
                }
            }
        });
        frecciaDestraAdmin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dataVolo = dataVolo.plusDays(1);
                dataAdmin.setText(formatter.format(dataVolo));
                popolaTabellaVoliArrivo(modelArrivi);
                popolaTabellaVoliPartenza(modelPartenze);
            }
        });
    }

    // METODI HELPER PER GESTIONE TABELLE
    public void rimuoviVoloDaTabelle(int codiceVolo) {
        rimuoviVoloDaModello((DefaultTableModel)tabellaVolArrivo.getModel(), codiceVolo);
        rimuoviVoloDaModello((DefaultTableModel)tabellaVoliPartenza.getModel(), codiceVolo);
    }

    private void rimuoviVoloDaModello(DefaultTableModel model, int codiceVolo) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if ((int)model.getValueAt(i, 0) == codiceVolo) {
                model.removeRow(i);
                break;
            }
        }
    }

    public void aggiungiVoloATabellaCorretta(Volo volo) {
        DefaultTableModel model;
        if (volo.getAeroportoDestinazione().equalsIgnoreCase("Capodichino")) {
            model = (DefaultTableModel)tabellaVolArrivo.getModel();
        } else {
            model = (DefaultTableModel)tabellaVoliPartenza.getModel();
        }

        model.addRow(new Object[]{
                volo.getCodiceVolo(),
                volo.getCompagniaAerea(),
                volo.getAeroportoOrigine(),
                volo.getAeroportoDestinazione(),
                volo.getOrarioArrivo(),
                volo.getDataVolo(),
                volo.getRitardo(),
                volo.getStato().toString(),
                volo.getGate()
        });
    }

    public void aggiornaVoloInTabelle(Volo volo) {
        aggiornaVoloInModello((DefaultTableModel)tabellaVolArrivo.getModel(), volo);
        aggiornaVoloInModello((DefaultTableModel)tabellaVoliPartenza.getModel(), volo);
    }

    private void aggiornaVoloInModello(DefaultTableModel model, Volo volo) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if ((int)model.getValueAt(i, 0) == volo.getCodiceVolo()) {
                model.setValueAt(volo.getCompagniaAerea(), i, 1);
                model.setValueAt(volo.getAeroportoOrigine(), i, 2);
                model.setValueAt(volo.getAeroportoDestinazione(), i, 3);
                model.setValueAt(volo.getOrarioArrivo(), i, 4);
                model.setValueAt(volo.getDataVolo(),i,5);
                model.setValueAt(volo.getRitardo(), i, 6);
                model.setValueAt(volo.getStato().toString(), i, 7);
                model.setValueAt(volo.getGate(), i, 8);
                break;
            }
        }
    }

    // METODI PRINCIPALI
    private void inizializzaAggiungiVolo(DefaultTableModel modelArrivi, DefaultTableModel modelPartenze) {
        AggiungiVolo av = new AggiungiVolo(modelArrivi, modelPartenze, sistema, this);
        JFrame frameAddVolo = new JFrame("Aggiungi Volo");
        frameAddVolo.getRootPane().setDefaultButton(av.getAggiungiVoloButton());
        frameAddVolo.setContentPane(av.getPrincipale());
        frameAddVolo.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frameAddVolo.pack();
        frameAddVolo.setSize(700, 500);
        frameAddVolo.setLocation(400, 150);
        frameAddVolo.setVisible(true);
    }

    private void modificaVoloSelezionato() {
        JTable tabellaCorrente = getTabellaAttiva();
        if (tabellaCorrente == null) {
            mostraMessaggio("Seleziona un volo da modificare!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int rigaSelezionata = tabellaCorrente.getSelectedRow();
        if (rigaSelezionata == -1) {
            mostraMessaggio("Seleziona un volo da modificare!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int codiceVolo = ottieniCodiceVoloDaRiga(tabellaCorrente, rigaSelezionata);
        if (codiceVolo == -1) return;

        Volo voloSelezionato = sistema.getVolo(codiceVolo);
        if (voloSelezionato != null) {
            DefaultTableModel model = (DefaultTableModel) tabellaCorrente.getModel();
            ModificaVolo modificaVoloPanel = new ModificaVolo(model, sistema, voloSelezionato, this);

            JFrame frameVolo = new JFrame("Modifica Volo");
            frameVolo.setContentPane(modificaVoloPanel.getPrincipale());
            frameVolo.getRootPane().setDefaultButton(modificaVoloPanel.getSalvaButton());
            frameVolo.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frameVolo.pack();
            frameVolo.setSize(700, 500);
            frameVolo.setLocation(400, 150);
            frameVolo.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Volo non trovato nel sistema", ERRORMESSAGE, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminaVoloSelezionato() {
        JTable tabellaCorrente = getTabellaAttiva();
        if (tabellaCorrente == null) {
            mostraMessaggio("Seleziona un volo da eliminare!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int rigaSelezionata = tabellaCorrente.getSelectedRow();
        if (rigaSelezionata == -1) {
            mostraMessaggio("Seleziona un volo da eliminare!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int conferma = JOptionPane.showConfirmDialog(null,
                "Sei sicuro di voler eliminare il volo selezionato?",
                "Conferma eliminazione",
                JOptionPane.YES_NO_OPTION);

        if (conferma != JOptionPane.YES_OPTION) return;

        int codiceVolo = ottieniCodiceVoloDaRiga(tabellaCorrente, rigaSelezionata);
        if (codiceVolo == -1) return;

        sistema.eliminaVolo(codiceVolo);
        ((DefaultTableModel)tabellaCorrente.getModel()).removeRow(rigaSelezionata);
    }

    // METODI DI SUPPORTO
    private JTable getTabellaAttiva() {
        if (tabellaVolArrivo.getSelectedRow() != -1) {
            return tabellaVolArrivo;
        } else if (tabellaVoliPartenza.getSelectedRow() != -1) {
            return tabellaVoliPartenza;
        }
        return null;
    }

    private int ottieniCodiceVoloDaRiga(JTable tabella, int riga) {
        Object valoreCodice = tabella.getValueAt(riga, 0);
        try {
            if (valoreCodice instanceof Integer) {
                return (Integer) valoreCodice;
            } else if (valoreCodice instanceof String) {
                return Integer.parseInt((String) valoreCodice);
            }
        } catch (NumberFormatException e) {
            mostraMessaggio(ERRORMESSAGE + " nel formato del codice volo", JOptionPane.ERROR_MESSAGE);
        }
        return -1;
    }

    private void popolaTabellaVoliArrivo(DefaultTableModel model) {
        model.setRowCount(0);
        List<Volo> voliArrivo = sistema.visualizzaVoliInArrivo(dataAdmin.getText());
        for (Volo v : voliArrivo) {
            model.addRow(new Object[]{
                    v.getCodiceVolo(),
                    v.getCompagniaAerea(),
                    v.getAeroportoOrigine(),
                    v.getAeroportoDestinazione(),
                    v.getOrarioArrivo(),
                    v.getDataVolo(),
                    v.getRitardo(),
                    v.getStato().toString(),
                    v.getGate()
            });
        }
    }

    private void popolaTabellaVoliPartenza(DefaultTableModel model) {
        model.setRowCount(0);
        List<Volo> voliPartenza = sistema.visualizzaVoliInPartenza(dataAdmin.getText());
        for (Volo v : voliPartenza) {
            model.addRow(new Object[]{
                    v.getCodiceVolo(),
                    v.getCompagniaAerea(),
                    v.getAeroportoOrigine(),
                    v.getAeroportoDestinazione(),
                    v.getOrarioArrivo(),
                    v.getDataVolo(),
                    v.getRitardo(),
                    v.getStato().toString(),
                    v.getGate()
            });
        }
    }

    private void mostraMessaggio(String messaggio, int tipo) {
        JOptionPane.showMessageDialog(null, messaggio, ERRORMESSAGE, tipo);
    }

    // STILI
    private void applyStyles(JFrame chiamante) {
        Color primaryBlue = new Color(0, 95, 135);
        Color secondaryBlue = new Color(0, 120, 167);
        Color background = new Color(245, 245, 245);
        Color errorRed = new Color(231, 76, 60);
        Color successGreen = new Color(76, 175, 80);

        final String name = "Segoe UI";
        Font titleFont = new Font(name, Font.BOLD, 24);
        Font labelFont = new Font(name, Font.BOLD, 14);
        Font buttonFont = new Font(name, Font.BOLD, 14);
        Font tableFont = new Font(name, Font.PLAIN, 12);

        principale.setBackground(background);
        principale.setBorder(new EmptyBorder(15, 15, 15, 15));
        voliPanel.setBackground(background);
        logOutPanel.setBackground(secondaryBlue);

        adminTitle.setFont(titleFont);
        adminTitle.setForeground(primaryBlue);
        adminTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        voliArrivoLabel.setFont(labelFont);
        voliArrivoLabel.setForeground(primaryBlue);
        voliPartenzaLabel.setFont(labelFont);
        voliPartenzaLabel.setForeground(primaryBlue);
        logoutLabel.setFont(labelFont);
        logoutLabel.setForeground(background);

        styleButton(aggiungiVoloButton, successGreen, Color.WHITE, buttonFont);
        styleButton(modificaVoloButton, secondaryBlue, Color.WHITE, buttonFont);
        styleButton(eliminaVoloButton, errorRed, Color.WHITE, buttonFont);

        // Stile tabella arrivi
        tabellaVolArrivo.setFont(tableFont);
        tabellaVolArrivo.setRowHeight(25);
        tabellaVolArrivo.setShowGrid(false);
        tabellaVolArrivo.setIntercellSpacing(new Dimension(0, 0));
        tabellaVolArrivo.setSelectionBackground(new Color(220, 240, 255));
        tabellaVolArrivo.getTableHeader().setFont(labelFont);
        tabellaVolArrivo.getTableHeader().setBackground(primaryBlue);
        tabellaVolArrivo.getTableHeader().setForeground(Color.WHITE);
        tabellaVolArrivo.getTableHeader().setReorderingAllowed(false);

        // Stile tabella partenze
        tabellaVoliPartenza.setFont(tableFont);
        tabellaVoliPartenza.setRowHeight(25);
        tabellaVoliPartenza.setShowGrid(false);
        tabellaVoliPartenza.setIntercellSpacing(new Dimension(0, 0));
        tabellaVoliPartenza.setSelectionBackground(new Color(220, 240, 255));
        tabellaVoliPartenza.getTableHeader().setFont(labelFont);
        tabellaVoliPartenza.getTableHeader().setBackground(primaryBlue);
        tabellaVoliPartenza.getTableHeader().setForeground(Color.WHITE);
        tabellaVoliPartenza.getTableHeader().setReorderingAllowed(false);

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

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(bg.darker());
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(bg);
            }
        });
    }

    // GETTER
    public JTable getTabellaVoliArrivo() {
        return tabellaVolArrivo;
    }
    public LocalDate getDataVolo(){return  dataVolo;}
    public JTable getTabellaVoliPartenza() {
        return tabellaVoliPartenza;
    }
}
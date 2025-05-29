package gui;

import controller.Sistema;
import model.Volo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * The type Home utente.
 */
public class HomeUtente {
    private Sistema sistema;
    private JButton cercaBigliettoButton;
    private JPanel navbar;
    private JPanel FinestraPrincipale;
    private JTable tabellaVoli;
    private JPanel voliPanel;
    private JTextField numeroBiglietto;
    private JTextField nome;
    private JLabel titoloB;
    private JButton logout;

    /**
     * The constant frame.
     */
    public final static JFrame frame = new JFrame("Home");

    public JPanel getPanel() {
        return FinestraPrincipale;
    }

    /**
     * Instantiates a new Home utente.
     */
    public HomeUtente(JFrame frameChiamante, Sistema sistema, boolean isAdmin) {
        this.sistema = sistema;

        inizializzaFrame();
        configuraTabella();
        caricaVoli();
        impostaAzioni(frameChiamante);
    }

    private void inizializzaFrame() {
        frame.setContentPane(FinestraPrincipale);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(900, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void configuraTabella() {
        String[] colonne = {"Codice Volo", "Compagnia Aerea", "Aeroporto di Origine",
                "Aeroporto Destinazione", "Orario di Arrivo", "Ritardo", "Stato del Volo", "Gate"};

        DefaultTableModel model = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabellaVoli.setModel(model);
        tabellaVoli.getTableHeader().setReorderingAllowed(false);
        tabellaVoli.getTableHeader().setResizingAllowed(false);
    }

    private void caricaVoli() {
        sistema.generaContenutiCasuali();
        ArrayList<Volo> voli = sistema.visualizzaVoli();
        DefaultTableModel model = (DefaultTableModel) tabellaVoli.getModel();

        if (voli != null) {
            for (Volo v : voli) {
                model.addRow(new Object[]{
                        v.getCodiceVolo(),
                        v.getCompagniaAerea(),
                        v.getAeroportoOrigine(),
                        v.getAeroportoDestinazione(),
                        v.getOrarioArrivo(),
                        v.getRitardo(),
                        v.getStato(),
                        v.getGate()
                });
            }
        }

        if (!(tabellaVoli.getParent() instanceof JViewport)) {
            voliPanel.removeAll();
            voliPanel.setLayout(new BorderLayout());
            voliPanel.add(new JScrollPane(tabellaVoli), BorderLayout.CENTER);
            voliPanel.revalidate();
            voliPanel.repaint();
        }
    }

    private void impostaAzioni(JFrame frameChiamante) {
        personalizzaBottoneCerca();

        cercaBigliettoButton.addActionListener(e -> {
            if (!numeroBiglietto.getText().isEmpty() || !nome.getText().isEmpty()) {
                int numero = -1;
                if (!numeroBiglietto.getText().isEmpty()) {
                    if (!numeroBiglietto.getText().trim().matches("\\d{4}")) {
                        JOptionPane.showMessageDialog(FinestraPrincipale, "Il codice volo deve essere un numero intero di 4 cifre", "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    numero = Integer.parseInt(numeroBiglietto.getText().trim());
                }
                Biglietto biglietto = new Biglietto(frame, sistema, nome.getText(), numero);
                biglietto.frame.setVisible(true);
                frame.setVisible(false);
            }
        });

        frame.getRootPane().setDefaultButton(cercaBigliettoButton);

        tabellaVoli.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tabellaVoli.rowAtPoint(e.getPoint());
                    if (row != -1) {
                        String stato = tabellaVoli.getValueAt(row, 6).toString();
                        if (!stato.equals("DECOLLATO") && !stato.equals("CANCELLATO") && !stato.equals("ATTERRATO")) {
                            Object[] valori = new Object[tabellaVoli.getColumnCount()];
                            for (int i = 0; i < valori.length; i++) {
                                valori[i] = tabellaVoli.getValueAt(row, i);
                            }
                            Prenota prenotazione = new Prenota(frame, valori, sistema);
                            prenotazione.frame.setVisible(true);
                            frame.setVisible(false);
                        } else {
                            JOptionPane.showMessageDialog(null, "Non puoi prenotare un volo " + stato.toLowerCase(), "Errore", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        logout.addActionListener(e -> {
            sistema.logout(sistema.utente);
            Login login = new Login(frameChiamante, sistema);
            login.frame.getRootPane().setDefaultButton(login.getInvio());
            login.frame.setVisible(true);
            frame.setVisible(false);
            frame.dispose();
        });
    }

    private void personalizzaBottoneCerca() {
        cercaBigliettoButton.setBackground(new Color(33, 150, 243));
        cercaBigliettoButton.setForeground(Color.WHITE);
        cercaBigliettoButton.setFocusPainted(false);
        cercaBigliettoButton.setBorderPainted(false);
        cercaBigliettoButton.setOpaque(true);

        cercaBigliettoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                cercaBigliettoButton.setBackground(new Color(30, 136, 229));
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                cercaBigliettoButton.setBackground(new Color(33, 150, 243));
            }
        });
    }
}
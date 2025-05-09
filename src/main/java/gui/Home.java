package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home {
    private JPanel primoPannello;
    private JLabel userNameLable;
    private JTextField userNameTextField;
    private JLabel passwordLable;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JComboBox<String> ruoloAccessoBox;
    private JLabel ruoloLable;
    private JLabel codiceAdminLable;
    private JTextField codAdminTextField;
    private JButton nonSeiRegistratoButton1;
    private JButton passwordDimenticataButton;
    private JPanel bottoniSottoLoginPanel;


    public Home() {
        codiceAdminLable.setVisible(false);
        codAdminTextField.setVisible(false);

        ruoloAccessoBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ruoloSelezionato = (String) ruoloAccessoBox.getSelectedItem();

                if("admin".equals(ruoloSelezionato)){
                    codiceAdminLable.setVisible(true);
                    codAdminTextField.setVisible(true);
                } else {
                    codiceAdminLable.setVisible(false);
                    codAdminTextField.setVisible(false);
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sistema Aeroporto");
        frame.setContentPane(new Home().primoPannello);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(500,300);
        frame.setLocation(500,330);
        frame.setVisible(true); 
    }

}
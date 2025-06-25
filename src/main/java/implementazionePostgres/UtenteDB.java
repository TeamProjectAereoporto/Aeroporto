package implementazionePostgres;

import connessioneDB.ConnessioneDB;
import dao.UtenteDao;
import model.Admin;
import model.Utente;
import model.UtenteGenerico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteDB implements UtenteDao {
    @Override
    public void aggiungiUtenteDB(Utente utente) throws SQLException {
        String sql = "INSERT INTO utente (username, psw, ruolo)  VALUES (?, ?, ?)";

        try(Connection conn =  ConnessioneDB.getInstance().connection) {
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, utente.getNomeUtente());
        preparedStatement.setString(2, utente.getPsw());
        preparedStatement.setInt(3, utente.getRuolo());
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Utente aggiunto con successo!");
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Errore durante aggiungio utente:  " + e.getMessage());
        }
    }
    public Utente verificaCredenziali(String username, String psw) throws SQLException {
        String sql = "SELECT username, psw, ruolo FROM utente WHERE username = ? AND psw = ?";
        try (Connection conn = ConnessioneDB.getInstance().connection;
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, psw);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int ruolo = rs.getInt("ruolo");
                    System.out.println("Utente trovato: " + username + ", ruolo: " + ruolo);
                    if (ruolo == 1) {
                        return new UtenteGenerico(username, psw, ruolo);
                    } else if (ruolo == 2) {
                        return new Admin(username, psw, ruolo);
                    }
                }
            }
        }
        return null; // credenziali errate o utente non trovato
    }



    @Override
    public Utente getUtenteByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM utente WHERE username = ?";
        try (Connection conn = ConnessioneDB.getInstance().connection;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nomeUtente = rs.getString("username");
                    String psw = rs.getString("psw");
                    int ruolo = rs.getInt("ruolo");

                    if (ruolo == 1) {
                        return new UtenteGenerico(nomeUtente, psw, ruolo);
                    } else if (ruolo == 2) {
                        return new Admin(nomeUtente, psw, ruolo);
                    }
                }
            }
        }
        return null;  // utente non trovato
    }



}

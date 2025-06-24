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
        String sql = "INSERT INTO utente (username, psw)  VALUES (?, ?)";

        try(Connection conn =  ConnessioneDB.getInstance().connection) {
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, utente.getNomeUtente());
        preparedStatement.setString(2, utente.getPsw());
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Utente aggiunto con successo!");
            }
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Errore durante aggiungio utente:  " + e.getMessage());
        }
    }
    public Utente verificaCredenziali(String username, String password) throws SQLException {
        String sql = "SELECT * FROM utente WHERE username = ? AND psw = ?";

        try (Connection conn = ConnessioneDB.getInstance().connection;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nomeUtente = rs.getString("username");
                    String psw = rs.getString("psw");

                    return new UtenteGenerico(nomeUtente, psw);
                }
            }
        }
        return null;
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

                    return new UtenteGenerico(nomeUtente, psw);
                }
            }
        }
        return null;  // utente non trovato
    }


}

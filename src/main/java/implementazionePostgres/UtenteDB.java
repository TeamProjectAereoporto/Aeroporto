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
    // Metodo per aggiungere un utente al database
    @Override
    public void aggiungiUtenteDB(Utente utente) throws SQLException {
        // Query SQL per inserire un nuovo utente
        String sql = "INSERT INTO utente (username, psw, ruolo)  VALUES (?, ?, ?)";

        // Uso try-with-resources per gestire la connessione e lo statement in modo sicuro
        try(Connection conn =  ConnessioneDB.getInstance().connection) {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            // Imposto i parametri della query con i dati dell'utente
            preparedStatement.setString(1, utente.getNomeUtente());
            preparedStatement.setString(2, utente.getPsw());
            preparedStatement.setInt(3, utente.getRuolo());

            // Eseguo l'inserimento e controllo se ha avuto successo
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Utente aggiunto con successo!");
            }
        }catch (SQLException e){
            // Stampo eventuali errori durante l'inserimento
            e.printStackTrace();
            System.out.println("Errore durante aggiungio utente:  " + e.getMessage());
        }
    }

    // Metodo per verificare le credenziali di login e restituire l'utente corrispondente
    public Utente verificaCredenziali(String username, String psw) throws SQLException {
        // Query SQL per cercare un utente con username e password corrispondenti
        String sql = "SELECT username, psw, ruolo FROM utente WHERE username = ? AND psw = ?";
        try (Connection conn = ConnessioneDB.getInstance().connection;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Imposto i parametri per la query
            ps.setString(1, username);
            ps.setString(2, psw);

            // Eseguo la query e verifico se esiste un risultato
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int ruolo = rs.getInt("ruolo");
                    System.out.println("Utente trovato: " + username + ", ruolo: " + ruolo);

                    // Creo l'oggetto utente specifico in base al ruolo (generico o admin)
                    if (ruolo == 1) {
                        return new UtenteGenerico(username, psw, ruolo);
                    } else if (ruolo == 2) {
                        return new Admin(username, psw, ruolo);
                    }
                }
            }
        }
        // Se credenziali errate o utente non trovato restituisco null
        return null;
    }

    // Metodo per recuperare un utente dal database tramite username
    @Override
    public Utente getUtenteByUsername(String username) throws SQLException {
        // Query SQL per selezionare l'utente per username
        String sql = "SELECT * FROM utente WHERE username = ?";
        try (Connection conn = ConnessioneDB.getInstance().connection;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Imposto il parametro della query
            ps.setString(1, username);

            // Eseguo la query e verifico se esiste un utente corrispondente
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String nomeUtente = rs.getString("username");
                    String psw = rs.getString("psw");
                    int ruolo = rs.getInt("ruolo");

                    // Creo e restituisco l'oggetto utente specifico in base al ruolo
                    if (ruolo == 1) {
                        return new UtenteGenerico(nomeUtente, psw, ruolo);
                    } else if (ruolo == 2) {
                        return new Admin(nomeUtente, psw, ruolo);
                    }
                }
            }
        }
        // Se utente non trovato restituisco null
        return null;
    }
    public boolean esisteAlmenoUnAdmin() {
        String sql = "SELECT COUNT(*) FROM utente WHERE ruolo = ?";
        try (Connection conn = ConnessioneDB.getInstance().connection;
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Se il campo ruolo è VARCHAR, metti "2" come stringa
            stmt.setString(1, "2");

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}

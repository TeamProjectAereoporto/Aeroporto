package implementazionePostgres;

import connessioneDB.ConnessioneDB;
import dao.PasseggeroDao;
import model.Passeggero;
import model.Volo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PasseggeroDB implements PasseggeroDao {
    // Connessione al database
    Connection connection = null;

    // Costruttore: ottiene l'istanza della connessione al database
    public PasseggeroDB(){
        try{
            connection = ConnessioneDB.getInstance().connection;
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    // Metodo per aggiungere un passeggero al database
    @Override
    public void aggiungiPasseggero(Passeggero passeggero) throws SQLException {
        // Query SQL per inserire un nuovo passeggero nella tabella "passeggero"
        String sqlInsertClient = "INSERT INTO passeggero(nome, cognome, numero_documento) VALUES(?,?,?)";

        // Uso di try-with-resources per chiudere automaticamente risorse JDBC
        try(Connection connection = ConnessioneDB.getInstance().connection;
            PreparedStatement stmt = connection.prepareStatement(sqlInsertClient)){

            // Impostazione parametri della query con dati del passeggero
            stmt.setString(1, passeggero.getNome());
            stmt.setString(2, passeggero.getCognome());
            stmt.setString(3, passeggero.getNumeroDocumento());

            // Esecuzione dell'update per inserire i dati nel database
            stmt.executeUpdate();
        }catch (SQLException e){
            // Stampa eventuale errore SQL sulla console
            e.printStackTrace();
        }
    }

    // Metodo per ottenere un passeggero dal database (non ancora implementato)
    @Override
    public Passeggero getPasseggero(String cdf) throws SQLException {
        String sql = "SELECT * FROM passeggero WHERE numero_documento = ?";

        try (Connection conn = ConnessioneDB.getInstance().connection;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cdf);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Passeggero(
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("numero_documento")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero del Passeggero: " + e.getMessage());
            throw e;
        }
        return null;  // Se non trovato
    }
}

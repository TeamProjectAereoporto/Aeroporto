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
    public int aggiungiPasseggero(Passeggero passeggero) throws SQLException {
        // Query SQL per inserire un nuovo passeggero nella tabella "passeggero"
        String sqlInsertClient = "INSERT INTO passeggero (nome, cognome, numero_documento) VALUES (?, ?, ?) RETURNING id_passeggero";

        // Uso di try-with-resources per chiudere automaticamente risorse JDBC
        try(Connection connection = ConnessioneDB.getInstance().connection;
            PreparedStatement stmt = connection.prepareStatement(sqlInsertClient)){

            // Impostazione parametri della query con dati del passeggero
            stmt.setString(1, passeggero.getNome());
            stmt.setString(2, passeggero.getCognome());
            stmt.setString(3, passeggero.getNumeroDocumento());

            // Esecuzione dell'update per inserire i dati nel database

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }catch (SQLException e){
            // Stampa eventuale errore SQL sulla console
            e.printStackTrace();
        }
        return 0;
    }

    // Metodo per ottenere un passeggero dal database (non ancora implementato)
    @Override
    public Passeggero getPasseggero(int id) throws SQLException {
        String sql = "SELECT * FROM passeggero WHERE id_passeggero = ?";

        try (Connection conn = ConnessioneDB.getInstance().connection;
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
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
    public int getIdPasseggero(long numeroBiglietto) throws SQLException{
        String sqlGetTicket ="SELECT id_passeggero from prenotazione where numero_biglietto = ?";
        try(Connection connection= ConnessioneDB.getInstance().connection;
            PreparedStatement stmt = connection.prepareStatement(sqlGetTicket)){
            stmt.setLong(1,numeroBiglietto);
            ResultSet rs = stmt.executeQuery();
            return rs.getInt("id_passeggero");
        }
    }
    public int getLastId() throws SQLException {
        String sqlGetLastId = "SELECT id_passeggero FROM passeggero ORDER BY id_passeggero DESC LIMIT 1;";
        int id = 0;
        try (Connection connection = ConnessioneDB.getInstance().connection;
             PreparedStatement stmt = connection.prepareStatement(sqlGetLastId);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                id = rs.getInt("id_passeggero");
            }

        }
        return id;
    }
    public boolean passeggeroGiaPrenotato(String numeroDocumento, int codiceVolo) throws SQLException {
        String query = """
        SELECT EXISTS (
            SELECT 1
            FROM prenotazione p
            JOIN passeggero ps ON p.id_passeggero = ps.id_passeggero
            WHERE ps.numero_documento = ? AND p.codice_volo = ?
        )
        """;

        try (Connection conn = ConnessioneDB.getInstance().connection;
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, numeroDocumento);
            stmt.setInt(2, codiceVolo);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1); // true se esiste già
            }
        }

        return false;
    }

}

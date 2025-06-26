package implementazionePostgres;

import connessioneDB.ConnessioneDB;
import dao.PasseggeroDao;
import model.Passeggero;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
    public Passeggero getPasseggero(String Nome, String Cognome) throws SQLException {
        return null;
    }
}

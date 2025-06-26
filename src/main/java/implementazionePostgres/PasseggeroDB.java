package implementazionePostgres;

import connessioneDB.ConnessioneDB;
import dao.PasseggeroDao;
import model.Passeggero;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PasseggeroDB implements PasseggeroDao {
    Connection connection = null;
    public PasseggeroDB(){
        try{
            connection = ConnessioneDB.getInstance().connection;
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    @Override
    public void aggiungiPasseggero(Passeggero passeggero) throws SQLException {
            String sqlInsertClient = "INSERT INTO passeggero(nome, cognome, numero_documento) VALUES(?,?,?)";
        try(Connection connection = ConnessioneDB.getInstance().connection;
            PreparedStatement stmt = connection.prepareStatement(sqlInsertClient)){
            stmt.setString(1, passeggero.getNome());
            stmt.setString(2, passeggero.getCognome());
            stmt.setString(3, passeggero.getNumeroDocumento());
            stmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Passeggero getPasseggero(String Nome, String Cognome) throws SQLException {
        return null;
    }
}

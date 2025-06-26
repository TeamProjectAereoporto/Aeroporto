package connessioneDB;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//classe dedicata alla creazione della connessione al DB.
//segue il pattern singleton per la connessione al database.
/*questo significa che tramite una sola istanza di utilizziamo sempre la stessa connessione al database senza crearne di nuove
e nonostante questo pattern possa presentare una serie di svantaggi significativi
 */
public class ConnessioneDB {
    private static ConnessioneDB instance; //Implementa il pattern Singleton
    public Connection connection = null; //Oggetto JDBC che rappresenta la connessione attiva con il database
    private String nome = "postgres";
    private String password = "postgres";
    private String url = "jdbc:postgresql://localhost:5432/Aeroporto";
    private String driver = "org.postgresql.Driver";

    private ConnessioneDB() throws SQLException {
        try {
            Class.forName(driver);
            connection = (Connection) DriverManager.getConnection(url, nome, password);
        } catch (ClassNotFoundException ex) {
            System.out.println("Connessione DB fallita: "+ ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static ConnessioneDB getInstance() throws SQLException {
        //	Restituisce l'istanza Singleton e si assicura che la connessione sia aperta e se non c'è la connessione crea una
        if(instance == null) {
            instance = new ConnessioneDB();
        } else if(instance.connection.isClosed()) {
            instance = new ConnessioneDB();
        } return instance;
    }
}

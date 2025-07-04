package dao;

import model.Passeggero;

import java.sql.SQLException;

//l'interfaccia DAO permette di non tralasciare nessun metodo nell'implementazione delle query SQL per il database.
//si è deciso di specializzare le diverse interfacce per non trovarci nella condizione di dover implementare metodi non necessari
//in classi che non ne avessero bisogno.
public interface PasseggeroDao {
    public int aggiungiPasseggero(Passeggero passeggero) throws SQLException;
    public Passeggero getPasseggero(int id) throws SQLException;
    public int getLastId() throws SQLException;
    public boolean passeggeroGiaPrenotato(String numeroDocumento, int codiceVolo) throws SQLException;

}

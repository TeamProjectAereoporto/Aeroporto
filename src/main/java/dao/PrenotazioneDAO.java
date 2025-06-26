package dao;

import model.Passeggero;
import model.Prenotazione;
import model.Utente;
import model.Volo;

import java.sql.SQLException;
import java.util.ArrayList;

public interface PrenotazioneDAO {
    public void addTicket(long numeroBiglietto, Passeggero passeggero, String postoAssegnato, Volo volo, String stato, Utente utente);
    public boolean deleteTicket(long numeroBiglietto);
    public void modifyTicket(long numeroBiglietto,String postoAssegnato, String cdf);
    public ArrayList getTickets(String username, String nome, int codiceVolo) throws SQLException;
}

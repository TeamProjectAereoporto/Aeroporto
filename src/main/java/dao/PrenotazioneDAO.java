package dao;

import model.Passeggero;
import model.Utente;
import model.Volo;

public interface PrenotazioneDAO {
    public void addTicket(long numeroBiglietto, Passeggero passeggero, String postoAssegnato, Volo volo, String stato, Utente utente);
    public boolean deleteTicket(long numeroBiglietto);
    public void modifyTicket(String postoAssegnato, Passeggero passeggero);
    public void getTickets();
}

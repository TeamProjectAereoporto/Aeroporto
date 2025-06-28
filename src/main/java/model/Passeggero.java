package model;

public class Passeggero {

    // Nome del passeggero
    private String nome;

    // Cognome del passeggero
    private String cognome;

    // Numero del documento identificativo del passeggero (es. passaporto, carta d'identità)
    private String numeroDocumento;

    private int id_passeggero;
    // Costruttore per creare un oggetto Passeggero con nome, cognome e numero documento
    public Passeggero(String nome, String cognome, String numeroDocumento){
        this.nome = nome;
        this.cognome = cognome;
        this.numeroDocumento = numeroDocumento;
    }

    // Metodi getter per ottenere i valori dei campi
    public String getCognome() {
        return cognome;
    }

    public String getNome() {
        return nome;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public int getId_passeggero() {
        return id_passeggero;
    }
    // Metodi setter per modificare i valori dei campi
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public void setId_passeggero(int id_passeggero) {
        this.id_passeggero = id_passeggero;
    }
}

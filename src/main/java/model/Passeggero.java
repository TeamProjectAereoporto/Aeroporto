package model;

public class Passeggero {

    private String nome;
    private String cognome;
    private String numeroDocumento;

    Passeggero(String nome, String cognome, String numeroDocumento){
        this.nome=nome;
        this.cognome=cognome;
        this.numeroDocumento=numeroDocumento;
    }
    //getters
    public String getCognome() {
        return cognome;
    }

    public String getNome() {
        return nome;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }
    //setters
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }
}
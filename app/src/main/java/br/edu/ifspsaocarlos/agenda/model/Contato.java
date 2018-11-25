package br.edu.ifspsaocarlos.agenda.model;

import java.io.Serializable;

public class Contato implements Serializable{
    private static final long serialVersionUID = 1L;
    private long id;
    private String nome;
    private String fone;
    private String foneSecundario;
    private String email;
    private String birthdayDate;
    private int isFavored = 0;

    public Contato()
    {
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getFone() {
        return fone;
    }
    public void setFone(String fone) {
        this.fone = fone;
    }
    public String getFoneSecundario() { return foneSecundario; }
    public void setFoneSecundario(String foneSecundario) { this.foneSecundario = foneSecundario; }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getBirthdayDate() { return birthdayDate; }
    public void setBirthdayDate(String birthdayDate) { this.birthdayDate = birthdayDate; }
    public void setIsFavored(int isFavored) { this.isFavored = isFavored; }
    public int getIsFavored() { return isFavored; }

}


package com.example.ifsp.maxbody;

public class Alimento {
    String Nome;
    Double Kcal;
    Integer Id;

    public Alimento(String nome, Double kcal, Integer id){
        this.Nome = nome;
        this.Kcal = kcal;
        this.Id = id;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public Double getKcal() {
        return Kcal;
    }

    public void setKcal(Double kcal) {
        Kcal = kcal;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }
}

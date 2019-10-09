package com.example.ifsp.maxbody;

public class Alimento_Relacionamento {
    Integer Id, Qntd;
    String Nome;

    public Alimento_Relacionamento(Integer id, Integer qntd, String nome){
        this.Id = id;
        this.Qntd = qntd;
        this.Nome = nome;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getQntd() {
        return Qntd;
    }

    public void setQntd(Integer qntd) {
        Qntd = qntd;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }
}

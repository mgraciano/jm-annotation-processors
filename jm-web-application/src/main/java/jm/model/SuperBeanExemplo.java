package jm.model;

import jm.annotations.ToString;

@ToString(value = {"numero", "descricao"}, superClass = "jm.model.BaseBean")
public class SuperBeanExemplo extends SuperBeanExemploExtras {

    private Long numero;
    private String descricao;
    private String observacao;

    public SuperBeanExemplo(Long numero, String descricao, String observacao) {
        this.numero = numero;
        this.descricao = descricao;
        this.observacao = observacao;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}

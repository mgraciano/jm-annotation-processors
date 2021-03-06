package jm.model;

import jm.annotations.ToString;

@ToString({"numero", "descricao"})
public class BeanExemplo extends BeanExemploExtras {

    private Long numero;
    private String descricao;
    private String observacao;

    public BeanExemplo(Long numero, String descricao, String observacao) {
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
